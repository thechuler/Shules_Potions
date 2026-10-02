package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.ModList;
import net.shule.shulespotions.Potions.EffectLevelRegistry;
import net.shule.shulespotions.Potions.ItemStatRegistry;
import net.shule.shulespotions.Screens.IngredientButton;
import net.shule.shulespotions.Screens.codex.base.CodexSpread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EffectInfoSpread extends CodexSpread {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/gui/effect_codex_screen_effect.png"
            );
            
    private static final ResourceLocation LEVEL_STAR =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/gui/level_star.png"
            );

    private static final ResourceLocation BENEFICIAL_SPRITE = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/beneficial.png");
    private static final ResourceLocation HARMFUL_SPRITE = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/harmfull.png");
    private static final ResourceLocation NEUTRAL_SPRITE = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/neutral.png");
    
    private static final ResourceLocation LEVEL_1 = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_1.png");
    private static final ResourceLocation LEVEL_2 = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_2.png");
    private static final ResourceLocation LEVEL_3 = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_3.png");
    private static final ResourceLocation LEVEL_4 = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_4.png");
    private static final ResourceLocation LEVEL_5 = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_5.png");
    private static final ResourceLocation LEVEL_6 = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_6.png");
    
    private static final ResourceLocation INSTANT_SPRITE = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/instant.png");
    private static final ResourceLocation DURATION_SPRITE = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/duration.png");

    private final MobEffect effect;
    private int guiX, guiY;
    private double scrollOffset = 0;
    private int maxScroll = 0;
    private final List<Button> itemButtons = new ArrayList<>();
    private List<Item> sortedIngredients = new ArrayList<>();

    public EffectInfoSpread(MobEffect effect) {
        this.effect = effect;
    }

    @Override
    public ResourceLocation getBackground() {
        return BACKGROUND;
    }

    @Override
    public void init(int x, int y) {
        this.guiX = x;
        this.guiY = y;
        addBackButton(x, y);
        
        ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        if (effectId != null) {
            sortedIngredients = ItemStatRegistry.getEntries()
                    .entrySet().stream()
                    .filter(e -> e.getValue().getEffectWeight(effectId) != 0)
                    .sorted((a, b) -> Integer.compare(
                            b.getValue().getEffectWeight(effectId),
                            a.getValue().getEffectWeight(effectId)
                    ))
                    .map(Map.Entry::getKey)
                    .toList();
        }

        rebuildItemButtons(x, y);
    }

    private void rebuildItemButtons(int x, int y) {
        itemButtons.clear();

        int columns = 4;
        int iconSize = 28;
        int spacing = iconSize + 6;

        int rows = (int) Math.ceil(sortedIngredients.size() / (double) columns);
        int totalContentHeight = rows * spacing;
        int visibleHeight = 65;
        maxScroll = Math.max(0, totalContentHeight - visibleHeight);
        scrollOffset = 0;

        int gridWidth = columns * spacing - 6;
        int leftPageCenter = getLeftPageCenter(x);
        int titleOffsetX = 10;
        int leftPageStart = leftPageCenter - gridWidth / 2 + titleOffsetX + 5;
        int startY = y + 155;

        for (int i = 0; i < sortedIngredients.size(); i++) {
            Item item = sortedIngredients.get(i);
            ItemStack stack = new ItemStack(item);

            int col = i % columns;
            int row = i / columns;

            int btnX = leftPageStart + col * spacing;
            int btnY = startY + row * spacing - (int) scrollOffset;

            IngredientButton btn = new IngredientButton(btnX, btnY, iconSize, stack, b -> {
                parent.pushSpread(new IngredientInfoSpread(item));
            });
            itemButtons.add(btn);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        int rightPageCenter = getRightPageCenter(guiX);
        int descY = guiY + 38;
        int descriptionWidth = 130;
        int startX = rightPageCenter - descriptionWidth / 2;
        
        String forcedDescKey = "effect." + (id != null ? id.getNamespace() : "") + "." + (id != null ? id.getPath() : "") + ".description";
        Component description = Component.translatableWithFallback(
                forcedDescKey,
                Component.translatable("shulespotions.screen.effect_codex.missing_description").getString()
        );
        
        if (handleTextClick(description, startX, descY, descriptionWidth, mouseX, mouseY)) {
            return true;
        }

        if (sortedIngredients.isEmpty()) return false;

        int columns = 4;
        int spacing = 34;
        int gridWidth = columns * spacing - 6;
        int leftPageCenter = getLeftPageCenter(guiX);
        int titleOffsetX = 10;
        int leftPageStart = leftPageCenter - gridWidth / 2 + titleOffsetX + 5;
        int startY = guiY + 155;
        int visibleHeight = 65;

        if (mouseX >= leftPageStart - 5 && mouseX <= leftPageStart + gridWidth + 5 &&
            mouseY >= startY - 5 && mouseY <= startY + visibleHeight) {
            
            for (Button btn : itemButtons) {
                if (btn.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (maxScroll > 0) {
            scrollOffset = Mth.clamp(scrollOffset - delta * 20, 0, maxScroll);
            return true;
        }
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        renderEffectIcon(graphics, x, y);
        renderEffectInfo(graphics, x, y);
        renderIngredients(graphics, x, y, mouseX, mouseY, partialTick);
        

        int titleOffsetX = 10;
        int textY = y + 110;
        int startX = getLeftPageCenter(x) + titleOffsetX - 31;
        

        if (mouseX >= startX && mouseX <= startX + 18 && mouseY >= textY && mouseY <= textY + 18) {
            String categoryKey = switch (effect.getCategory()) {
                case BENEFICIAL -> "shulespotions.screen.effect_codex.beneficial";
                case HARMFUL -> "shulespotions.screen.effect_codex.harmful";
                case NEUTRAL -> "shulespotions.screen.effect_codex.neutral";
            };
            graphics.renderTooltip(Minecraft.getInstance().font, Component.translatable(categoryKey), mouseX, mouseY);
        }
        

        int levelX = startX + 22;
        if (mouseX >= levelX && mouseX <= levelX + 18 && mouseY >= textY && mouseY <= textY + 18) {
            int level = EffectLevelRegistry.getLevel(effect);
            graphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("shulespotions.screen.effect_codex.level").append(String.valueOf(level)), mouseX, mouseY);
        }
        

        int timeX = levelX + 22;
        if (mouseX >= timeX && mouseX <= timeX + 18 && mouseY >= textY && mouseY <= textY + 18) {
            String timeKey = effect.isInstantenous() ? "shulespotions.screen.effect_codex.instant" : "shulespotions.duration.name";
            graphics.renderTooltip(Minecraft.getInstance().font, Component.translatable(timeKey), mouseX, mouseY);
        }
    }

    private void renderEffectIcon(GuiGraphics graphics, int guiX, int guiY) {
        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        if (id == null) return;

        ResourceLocation tempTexture = ResourceLocation.fromNamespaceAndPath(
                id.getNamespace(),
                "textures/mob_effect/" + id.getPath() + ".png"
        );
        
        ResourceLocation texture;
        if (Minecraft.getInstance().getResourceManager().getResource(tempTexture).isEmpty()) {
            texture = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/missing_effect_icon.png");
        } else {
            texture = tempTexture;
        }

        int iconX = guiX + 74;
        int iconY = guiY + 35;

        graphics.pose().pushPose();
        graphics.pose().translate(iconX, iconY, 0);

        float scale = 64F / 18F;
        graphics.pose().scale(scale, scale, 1F);
        graphics.blit(texture, 0, 0, 0, 0, 18, 18, 18, 18);
        graphics.pose().popPose();
    }

    private void renderEffectInfo(GuiGraphics graphics, int guiX, int guiY) {
        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        int leftPageCenter = getLeftPageCenter(guiX);
        int titleOffsetX = 10;
        int textX = guiX + 28 + titleOffsetX;
        int textY = guiY + 110;

        Component title = Component.translatable(effect.getDescriptionId());
        drawCenteredScaledString(graphics, title, leftPageCenter + titleOffsetX, guiY + 15, 1.5F, effect.getColor());

        

        String modName = "-";
        if (id != null) {
            String modId = id.getNamespace();
            if ("minecraft".equals(modId)) {
                modName = "Minecraft";
            } else {
                modName = ModList.get().getModContainerById(modId)
                        .map(c -> c.getModInfo().getDisplayName())
                        .orElse(modId.substring(0, 1).toUpperCase() + modId.substring(1));
            }
        }

        int descriptionWidth = 130;

        int sourceY = guiY + 206;
        int rightPageCenter = getRightPageCenter(guiX);
        int sourceX = rightPageCenter - 85;
        Component sourceLabel = Component.translatable("shulespotions.screen.effect_codex.source");
        
        graphics.pose().pushPose();
        graphics.pose().translate(sourceX, sourceY, 0);
        graphics.pose().scale(0.8F, 0.8F, 1.0F);
        graphics.drawString(Minecraft.getInstance().font,
                sourceLabel,
                0, 0, 0x7A6A52, false);
        graphics.drawString(Minecraft.getInstance().font,
                modName,
                Minecraft.getInstance().font.width(sourceLabel) + 2, 0, COLOR_TEXT, false);
        graphics.pose().popPose();

        ResourceLocation categorySprite = switch (effect.getCategory()) {
            case BENEFICIAL -> BENEFICIAL_SPRITE;
            case HARMFUL -> HARMFUL_SPRITE;
            case NEUTRAL -> NEUTRAL_SPRITE;
        };
        
        int level = EffectLevelRegistry.getLevel(effect);
        ResourceLocation levelSprite = switch (level) {
            case 1 -> LEVEL_1;
            case 2 -> LEVEL_2;
            case 3 -> LEVEL_3;
            case 4 -> LEVEL_4;
            case 5 -> LEVEL_5;
            default -> LEVEL_6;
        };
        
        ResourceLocation timeSprite = effect.isInstantenous() ? INSTANT_SPRITE : DURATION_SPRITE;
        
        int startX = leftPageCenter + titleOffsetX - 31;
        graphics.blit(categorySprite, startX, textY, 0, 0, 18, 18, 18, 18);
        graphics.blit(levelSprite, startX + 22, textY, 0, 0, 18, 18, 18, 18);
        graphics.blit(timeSprite, startX + 44, textY, 0, 0, 18, 18, 18, 18);


        int descTitleY = guiY + 18;
        Component descTitle = Component.translatable("shulespotions.screen.effect_codex.description");
        drawCenteredScaledString(graphics, descTitle, rightPageCenter, descTitleY, 1.5F, COLOR_TITLE);

        String forcedDescKey = "effect." + (id != null ? id.getNamespace() : "") + "." + (id != null ? id.getPath() : "") + ".description";
        int descX = rightPageCenter - descriptionWidth / 2;
        int descTextY = descTitleY + 20;

        graphics.drawWordWrap(
                Minecraft.getInstance().font,
                Component.translatableWithFallback(
                        forcedDescKey,
                        Component.translatable("shulespotions.screen.effect_codex.missing_description").getString()
                ),
                descX,
                descTextY,
                descriptionWidth,
                COLOR_TEXT
        );
    }

    private void renderIngredients(GuiGraphics graphics, int guiX, int guiY, int mouseX, int mouseY, float partialTick) {
        if (sortedIngredients.isEmpty()) return;

        int columns = 4;
        int spacing = 34;
        int gridWidth = columns * spacing - 6;
        int leftPageCenter = getLeftPageCenter(guiX);
        int titleOffsetX = 10;
        int leftPageStart = leftPageCenter - gridWidth / 2 + titleOffsetX + 5;
        int startY = guiY + 155;
        int visibleHeight = 65;

        Component title = Component.translatable("shulespotions.screen.effect_codex.ingredients");
        drawCenteredScaledString(graphics, title, leftPageCenter + titleOffsetX, guiY + 142, 1.0F, COLOR_TITLE);

        graphics.enableScissor(leftPageStart - 5, startY - 5, leftPageStart + gridWidth + 5, startY + visibleHeight);

        boolean mouseInBox = mouseX >= leftPageStart - 5 && mouseX <= leftPageStart + gridWidth + 5 &&
                             mouseY >= startY - 5 && mouseY <= startY + visibleHeight;
        
        int renderMouseX = mouseInBox ? mouseX : -1;
        int renderMouseY = mouseInBox ? mouseY : -1;

        for (int i = 0; i < itemButtons.size(); i++) {
            Button btn = itemButtons.get(i);
            int row = i / columns;
            btn.setY(startY + row * spacing - (int) scrollOffset);
            btn.render(graphics, renderMouseX, renderMouseY, partialTick);
        }

        graphics.disableScissor();

        if (mouseInBox) {
            for (Button btn : itemButtons) {
                if (btn instanceof IngredientButton ib && ib.isHovered()) {
                    ib.renderToolTip(graphics, mouseX, mouseY);
                }
            }
        }

        if (maxScroll > 0) {
            int scrollBarX = leftPageStart + gridWidth + 8;
            int scrollBarHeight = visibleHeight - 5;
            int thumbHeight = Math.max(10, scrollBarHeight * visibleHeight / (visibleHeight + maxScroll));
            int thumbY = startY + (int) ((scrollBarHeight - thumbHeight) * (scrollOffset / (float) maxScroll));
            
            graphics.fill(scrollBarX, startY, scrollBarX + 2, startY + scrollBarHeight, 0xFF4A3A26);
            graphics.fill(scrollBarX, thumbY, scrollBarX + 2, thumbY + thumbHeight, 0xFF9E8364);
        }
    }
}
