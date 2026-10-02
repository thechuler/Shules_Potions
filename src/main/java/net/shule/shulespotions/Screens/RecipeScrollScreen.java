package net.shule.shulespotions.Screens;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Blocks.Custom.BigCauldronCore;
import net.shule.shulespotions.Blocks.Custom.PotionCauldron;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Items.custom.RecipeScroll;
import net.shule.shulespotions.Potions.EffectLevelRegistry;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.Potions.PotionLiquidUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class RecipeScrollScreen extends Screen {
    private int animationTicks = 0;
    private final ItemStack scrollStack;

    private static final ResourceLocation SCROLL_BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/scroll_screen2.png");

    private static final ResourceLocation CONTAINER =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/container.png");

    private static final ResourceLocation CONTAINER_HOVER =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/container_hover.png");

    private static final ResourceLocation CONTAINER_BLOCKED =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/container_blocked.png");

    private static final ResourceLocation CONTAINER_LOW_TOLERANCY =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/container_low_tolerancy.png");

    private static final ResourceLocation CAULDRON_CONTAINER =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/cauldron_container.png");

    private static final ResourceLocation VITALITY_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/vitality_icon.png");

    private static final ResourceLocation PURITY_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/purity_icon.png");

    private static final ResourceLocation FLAVOR_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/flavor_icon.png");

    private static final ResourceLocation DURATION_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/duration_icon.png");

    private static final ResourceLocation COMPLEXITY_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/complexity_icon.png");


    private static final ResourceLocation STABILITY_BAR =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/stability_bar_container.png");


    private static final int GUI_WIDTH = 348;
    private static final int GUI_HEIGHT = 240;


    private long animationStartTime;

    private int startVitality;
    private int startPurity;
    private int startFlavor;
    private int startStability;
    private int startDuration;

    private int targetVitality;
    private int targetPurity;
    private int targetFlavor;
    private int targetStability;
    private int targetDuration;

    private static final long ANIMATION_DURATION = 1600; // ms

    public RecipeScrollScreen(ItemStack stack) {
        super(stack.getHoverName());
        this.scrollStack = stack;

        PotionLiquid pl = PotionLiquidUtils.getPotionLiquidFromStack(stack);

        if (pl != null) {
            startStatsAnimation(pl);
        }
    }

    @Override
    public void tick() {
        animationTicks++;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        renderBackground(graphics);

        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;

        renderBackgroundTexture(graphics, x, y);

        super.render(graphics, mouseX, mouseY, partialTick);

        renderTitle(graphics, x, y);

        renderCauldron(graphics, x, y);

        renderIngredients(graphics, mouseX, mouseY, x , y);

        PotionLiquid pl = PotionLiquidUtils.getPotionLiquidFromStack(scrollStack);

        renderPotionStats(graphics, mouseX, mouseY, x, y, pl);

        renderPotionEffects(graphics, mouseX, mouseY, x, y, pl);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


    private void renderBackgroundTexture(GuiGraphics graphics, int x, int y) {
        graphics.blit(SCROLL_BACKGROUND, x, y, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);
    }

    private void renderTitle(GuiGraphics graphics, int x, int y) {

        graphics.pose().pushPose();

        graphics.pose().translate(x + (float) GUI_WIDTH / 2, y + 43, 0);

        graphics.pose().scale((float) 1.5, (float) 1.5, 1F);

        Component title = scrollStack.getHoverName();

        int width = this.font.width(title);

        graphics.drawString(this.font, title, -width / 2, 0, 0x70635b, false);

        graphics.pose().popPose();
    }


    private void renderIngredients(GuiGraphics graphics, int mouseX, int mouseY, int x, int y) {

        List<CompoundTag> actions = RecipeScroll.getActions(scrollStack);
        
        List<ItemStack> validIngredients = new ArrayList<>();

        for (CompoundTag actionTag : actions) {
            if ("add_ingredient".equals(actionTag.getString("Type"))) {
                String itemId = actionTag.getCompound("Data").getString("Item");
                Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(itemId));
                if (item != null) {
                    validIngredients.add(new ItemStack(item));
                }
            }
        }

        Block targetBlock = RecipeScroll.getCauldronBlock(scrollStack);
        if (targetBlock == null) {
            targetBlock = ModBlocks.POTION_CAULDRON.get();
        }

        int maxIngredientCount = 0;
        if (targetBlock instanceof PotionCauldron cauldron) {
            maxIngredientCount = cauldron.getMAX_INGREDIENT_COUNT();
        }

        int startX = x + 215;
        int startY = y + 165; 

        int columns = 4;
        int maxSlots = 8;
        int spacing = 20;

        for (int i = 0; i < maxSlots; i++) {
            int column = i % columns;
            int row = i / columns;
            int itemX = startX + (column * spacing);
            int itemY = startY + (row * spacing);

            boolean isHovered = mouseX >= itemX && mouseX <= itemX + 16 && mouseY >= itemY && mouseY <= itemY + 16;
            boolean isBlocked = i >= maxIngredientCount;
            
            if (i < validIngredients.size()) {
                ItemStack ingredient = validIngredients.get(i);
                int delayPerIngredient = 5;
                int startTick = i * delayPerIngredient;
                float progress = Math.max(0F, Math.min(1F, (animationTicks - startTick) / 8.0F));

                float scale;
                if (progress < 0.7F) {
                    scale = progress / 0.7F * 1.2F;
                } else {
                    float t = (progress - 0.7F) / 0.3F;
                    scale = 1.2F - (0.2F * t);
                }

                renderAnimatedIngredient(graphics, ingredient, itemX, itemY, scale, isHovered && !isBlocked);
                
                if (isBlocked) {
                    renderBlockedOverlay(graphics, itemX, itemY, 8, 8);
                    if (isHovered) {
                        graphics.renderTooltip(this.font, Component.translatable("gui.shulespotions.slot_blocked"), mouseX, mouseY);
                    }
                } else {
                    renderIngredientTooltip(graphics, ingredient, mouseX, mouseY, itemX, itemY);
                }
            } else {
                renderEmptyContainer(graphics, itemX, itemY, 8, 8);
                if (isBlocked) {
                    renderBlockedOverlay(graphics, itemX, itemY, 8, 8);
                    if (isHovered) {
                        graphics.renderTooltip(this.font, Component.translatable("gui.shulespotions.slot_blocked"), mouseX, mouseY);
                    }
                }
            }
        }
    }


    private void renderAnimatedIngredient(GuiGraphics graphics, ItemStack stack, int x, int y, float scale, boolean isHovered) {

        graphics.pose().pushPose();

        graphics.pose().translate(x + 8, y + 8, 0);
        
        graphics.pose().scale(0.8F, 0.8F, 1F);

        ResourceLocation containerTex = isHovered ? CONTAINER_HOVER : CONTAINER;

        graphics.blit(containerTex, -11, -11, 0, 0, 22, 22, 22, 22);

        graphics.pose().scale(scale, scale, 1F);

        graphics.renderItem(stack, -8, -8);

        graphics.pose().popPose();
    }


    private void renderIngredientTooltip(GuiGraphics graphics, ItemStack stack, int mouseX, int mouseY, int x, int y) {
        if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
            graphics.renderTooltip(this.font, stack, mouseX, mouseY);
        }
    }



    private void renderPotionStats(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, PotionLiquid pl) {
        if (pl == null) return;

        int vitality = getAnimatedValue(startVitality, targetVitality);
        int purity = getAnimatedValue(startPurity, targetPurity);
        int flavor = getAnimatedValue(startFlavor, targetFlavor);
        int stability = getAnimatedValue(startStability, targetStability);
        int duration = getAnimatedValue(startDuration, targetDuration);

        Block targetBlock = RecipeScroll.getCauldronBlock(scrollStack);
        if (targetBlock == null) {
            targetBlock = ModBlocks.POTION_CAULDRON.get();
        }
        int cauldronLevel = 1;
        if (targetBlock instanceof PotionCauldron cauldron) {
            cauldronLevel = cauldron.getCauldronLevel();
        }
        
        float barScale = 1.3f;
        int vStartX = x + 50;
        int vStartY = y + 50;

        renderVerticalStabilityBar(graphics, stability, vStartX, vStartY, mouseX, mouseY, barScale);

        int statsX = vStartX + (int)(22 * barScale) + 8;
        int statsY = vStartY + 20;
        
        float iconScale = 1.0f; 
        int statSpacingY = (int)(16 * iconScale) + 5;

        renderStatIcon(graphics, DURATION_ICON, Component.translatable("shulespotions.duration.name"), StringUtil.formatTickDuration(duration * 20), statsX, statsY, mouseX, mouseY, iconScale);
        renderStatIcon(graphics, PURITY_ICON, Component.translatable("shulespotions.purity.name"), String.valueOf(purity), statsX, statsY + statSpacingY, mouseX, mouseY, iconScale);
        renderStatIcon(graphics, VITALITY_ICON, Component.translatable("shulespotions.vitality.name"), String.valueOf(vitality), statsX, statsY + statSpacingY * 2, mouseX, mouseY, iconScale);
        renderStatIcon(graphics, FLAVOR_ICON, Component.translatable("shulespotions.flavor.name"), String.valueOf(flavor), statsX, statsY + statSpacingY * 3, mouseX, mouseY, iconScale);
        renderStatIcon(graphics, COMPLEXITY_ICON, Component.translatable("shulespotions.complexity.name"), String.valueOf(cauldronLevel), statsX, statsY + statSpacingY * 4, mouseX, mouseY, iconScale);
    }

    private void renderVerticalStabilityBar(GuiGraphics graphics, int value, int x, int y, int mouseX, int mouseY, float scale) {
        int clampedValue = Math.max(0, Math.min(100, value));
        int barWidth = 16;
        int barMaxHeight = 110;

        int finalColor = 0xFF000000 | 11369299;
        
        float fraction = clampedValue / 100.0f;
        int fillHeight = (int) (barMaxHeight * fraction);
        int fillY = barMaxHeight - fillHeight;

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(scale, scale, 1F);


        graphics.fill(3, fillY, 3 + barWidth, barMaxHeight, finalColor);
        graphics.blit(RecipeScrollScreen.STABILITY_BAR, 0, 0, 0, 0, 22, 110, 22, 110);
        
        graphics.pose().popPose();

        if (mouseX >= x && mouseX <= x + (22 * scale) && mouseY >= y && mouseY <= y + (110 * scale)) {
            graphics.renderTooltip(this.font, Component.translatable("shulespotions.stability.name").append(": " + value), mouseX, mouseY);
        }
    }

    private void renderStatIcon(GuiGraphics graphics, ResourceLocation texture, Component tooltip, String textValue,
                                int x, int y, int mouseX, int mouseY, float scale) {

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(scale, scale, 1F);

        int size = 16;

        graphics.blit(texture, 0, 0, 0, 0, size, size, size, size);
        graphics.setColor(1F, 1F, 1F, 1F);
        graphics.drawString(this.font, textValue, 22, 4, 0x70635b, false);
        graphics.pose().popPose();

        if (mouseX >= x && mouseX <= x + (16 * scale) && mouseY >= y && mouseY <= y + (16 * scale)) {
            graphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
    }



    private void startStatsAnimation(PotionLiquid pl) {

        animationStartTime = System.currentTimeMillis();

        startVitality = 0;
        startPurity = 0;
        startFlavor = 0;
        startStability = 0;
        startDuration = 0;

        targetVitality = pl.getStats().getVitality();
        targetPurity = pl.getStats().getPurity();
        targetFlavor = pl.getStats().getFlavor();
        targetStability = pl.getStats().getStability();
        targetDuration = pl.getStats().getDurationSeconds();
    }



    private float getAnimationProgress() {
        long elapsed = System.currentTimeMillis() - animationStartTime;
        return Math.min(1F, elapsed / (float) ANIMATION_DURATION);
    }



    private int getAnimatedValue(int start, int target) {
        float progress = getAnimationProgress();
        progress = 1F - (float)Math.pow(1F - progress, 3);
        return Math.round(Mth.lerp(progress, start, target));
    }




    private void renderPotionEffects(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, PotionLiquid pl) {
        if (pl == null) return;

        List<MobEffect> effects = new ArrayList<>(PotionLiquidUtils.getPossibleEffects(pl));
        effects.sort((e1, e2) -> Integer.compare(PotionLiquidUtils.getEffectChance(pl, e2), PotionLiquidUtils.getEffectChance(pl, e1)));

        Block targetBlock = RecipeScroll.getCauldronBlock(scrollStack);

        if (targetBlock == null) {
            targetBlock = ModBlocks.POTION_CAULDRON.get();
        }

        int maxEffectCount = 4;
        int cauldronLevel = 1;
        if (targetBlock instanceof PotionCauldron cauldron) {
            maxEffectCount = cauldron.getMAX_EFFECT_COUNT();
            cauldronLevel = cauldron.getCauldronLevel();
        }

        int startX = x + 215;
        int startY = y + 70;

        int spacingX = 20;
        int spacingY = 20;

        int maxColumns = 4;
        int maxSlots = 16;
        int delayPerEffect = 5;

        for (int i = 0; i < maxSlots; i++) {

            int column = i % maxColumns;
            int row = i / maxColumns;
            int iconX = startX + (column * spacingX);
            int iconY = startY + (row * spacingY);

            boolean isHovered = mouseX >= iconX && mouseX <= iconX + 16 && mouseY >= iconY && mouseY <= iconY + 16;
            boolean isBlocked = i >= maxEffectCount;
            
            if (i < effects.size()) {
                MobEffect effect = effects.get(i);
                ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);

                if (id == null) continue;

                ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/mob_effect/" + id.getPath() + ".png");
                int startTick = i * delayPerEffect;
                float scale = getPopAnimationScale(startTick);
                boolean isLowTolerance = cauldronLevel < EffectLevelRegistry.getLevel(effect);

                renderAnimatedEffect(graphics, texture, iconX, iconY, scale, isHovered && !isBlocked);

                if (isBlocked) {
                    renderBlockedOverlay(graphics, iconX, iconY, 9, 9);
                    if (isHovered) {
                        graphics.renderTooltip(this.font, Component.translatable("gui.shulespotions.slot_blocked"), mouseX, mouseY);
                    }

                } else if (isLowTolerance) {
                    renderLowTolerancyOverlay(graphics, iconX, iconY);

                    if (isHovered) {
                        graphics.renderTooltip(this.font, Component.translatable("gui.shulespotions.too_complex"), mouseX, mouseY);
                    }

                } else if (isHovered) {

                    int chance = PotionLiquidUtils.getEffectChance(pl, effect);
                    graphics.renderTooltip(
                            this.font,
                            List.of(Component.translatable(effect.getDescriptionId()), Component.literal(chance + "%")),
                            java.util.Optional.empty(),
                            mouseX,
                            mouseY
                    );
                }
            } else {

                renderEmptyContainer(graphics, iconX, iconY, 9, 9);
                if (isBlocked) {
                    renderBlockedOverlay(graphics, iconX, iconY, 9, 9);
                    if (isHovered) {
                        graphics.renderTooltip(this.font, Component.translatable("gui.shulespotions.slot_blocked"), mouseX, mouseY);
                    }
                }
            }
        }
    }


    private void renderEmptyContainer(GuiGraphics graphics, int x, int y, int offsetX, int offsetY) {
        graphics.pose().pushPose();
        graphics.pose().translate(x + offsetX, y + offsetY, 0);
        graphics.pose().scale(0.8F, 0.8F, 1F);
        graphics.blit(CONTAINER, -11, -11, 0, 0, 22, 22, 22, 22);
        graphics.pose().popPose();
    }

    private void renderBlockedOverlay(GuiGraphics graphics, int x, int y, int offsetX, int offsetY) {
        graphics.pose().pushPose();
        graphics.pose().translate(x + offsetX, y + offsetY, 150);
        graphics.pose().scale(0.8F, 0.8F, 1F);
        
        com.mojang.blaze3d.systems.RenderSystem.enableBlend();
        com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
        graphics.blit(CONTAINER_BLOCKED, -11, -11, 0, 0, 22, 22, 22, 22);
        com.mojang.blaze3d.systems.RenderSystem.disableBlend();
        
        graphics.pose().popPose();
    }

    private void renderLowTolerancyOverlay(GuiGraphics graphics, int x, int y) {
        graphics.pose().pushPose();
        graphics.pose().translate(x + 9, y + 9, 150);
        graphics.pose().scale(0.8F, 0.8F, 1F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.blit(CONTAINER_LOW_TOLERANCY, -11, -11, 0, 0, 22, 22, 22, 22);
        RenderSystem.disableBlend();
        graphics.pose().popPose();
    }

    private void renderAnimatedEffect(GuiGraphics graphics, ResourceLocation texture, int x, int y, float scale, boolean isHovered) {
        graphics.pose().pushPose();
        graphics.pose().translate(x + 9, y + 9, 0);
        graphics.pose().scale(0.8F, 0.8F, 1F);
        ResourceLocation containerTex = isHovered ? CONTAINER_HOVER : CONTAINER;
        graphics.blit(containerTex, -11, -11, 0, 0, 22, 22, 22, 22);
        graphics.pose().scale(scale, scale, 1F);
        graphics.blit(texture, -9, -9, 0, 0, 18, 18, 18, 18);
        graphics.pose().popPose();
    }

    private float getPopAnimationScale(int startTick) {
        float progress = (animationTicks - startTick) / 8.0F;
        progress = Math.max(0F, Math.min(1F, progress));
        if (progress < 0.7F) {
            return progress / 0.7F * 1.2F;
        }
        float t = (progress - 0.7F) / 0.3F;
        return 1.2F - (0.2F * t);
    }

    private void renderCauldron(GuiGraphics graphics, int x, int y) {
        
        int cauldronOffsetX = 160 + 5;
        int cauldronOffsetY = 120 + 10;


        graphics.blit(CAULDRON_CONTAINER, x + cauldronOffsetX - 35, y + cauldronOffsetY - 35, 0, 0, 70, 70, 70, 70);

        Block targetBlock = RecipeScroll.getCauldronBlock(scrollStack);
        if (targetBlock == null) {
            targetBlock = ModBlocks.POTION_CAULDRON.get();
        }

        graphics.pose().pushPose();
        graphics.pose().translate(x + cauldronOffsetX, y + cauldronOffsetY, 150.0f);
        float scale = 35.0f;
        if (targetBlock instanceof BigCauldronCore) {
            scale *= 0.4f;
        }
        graphics.pose().scale(scale, -scale, scale);
        float angle = (Util.getMillis() % 4000L) / 4000.0f * 360.0f;
        graphics.pose().mulPose(Axis.YP.rotationDegrees(angle));
        graphics.pose().mulPose(Axis.XP.rotationDegrees(25.0f));
        graphics.pose().translate(-0.5f, -0.5f, -0.5f);

        BlockState cauldronState = targetBlock.defaultBlockState();
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        Lighting.setupForFlatItems();
        blockRenderer.renderSingleBlock(
                cauldronState, graphics.pose(), bufferSource,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                ModelData.EMPTY, null
        );
        bufferSource.endBatch();
        Lighting.setupFor3DItems();

        graphics.pose().popPose();
    }
}
