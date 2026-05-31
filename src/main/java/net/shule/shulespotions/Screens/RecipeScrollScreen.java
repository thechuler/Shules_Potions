package net.shule.shulespotions.Screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Items.custom.RecipeScroll;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.Potions.PotionLiquidUtils;

import java.util.List;


public class RecipeScrollScreen extends Screen {
    private int animationTicks = 0;
    private final ItemStack scrollStack;

    private static final ResourceLocation SCROLL_BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/scroll_screen1.png");

    private static final ResourceLocation VITALITY_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/vitality_icon.png");

    private static final ResourceLocation PURITY_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/purity_icon.png");

    private static final ResourceLocation FLAVOR_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/flavor_icon.png");

    private static final ResourceLocation STABILITY_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/stability_icon.png");

    private static final int GUI_WIDTH = 336;
    private static final int GUI_HEIGHT = 266;


    private long animationStartTime;

    private int startVitality;
    private int startPurity;
    private int startFlavor;
    private int startStability;

    private int targetVitality;
    private int targetPurity;
    private int targetFlavor;
    private int targetStability;

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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        renderBackground(graphics);

        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;

        renderBackgroundTexture(graphics, x, y);

        super.render(graphics, mouseX, mouseY, partialTick);

        renderTitle(graphics, x, y,1.5f);

        renderIngredients(graphics, mouseX, mouseY, x , y);

        PotionLiquid pl = PotionLiquidUtils.getPotionLiquidFromStack(scrollStack);

        renderPotionStats(graphics, mouseX, mouseY, x, y, pl);

        renderPotionEffects(graphics, mouseX, mouseY, x, y, pl);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // =========================
    // BACKGROUND
    // =========================

    private void renderBackgroundTexture(GuiGraphics graphics, int x, int y) {
        graphics.blit(
                SCROLL_BACKGROUND,
                x,
                y,
                0,
                0,
                GUI_WIDTH,
                GUI_HEIGHT,
                GUI_WIDTH,
                GUI_HEIGHT
        );
    }

    // =========================
    // TITLE
    // =========================

    private void renderTitle(GuiGraphics graphics, int x, int y,float scale) {

        graphics.pose().pushPose();



        graphics.pose().translate(
                x + GUI_WIDTH / 2,
                y + 73,
                0
        );

        graphics.pose().scale(scale, scale, 1F);

        Component title = scrollStack.getHoverName();

        int width = this.font.width(title);

        graphics.drawString(
                this.font,
                title,
                -width / 2,
                0,
                0x70635b,
                false
        );

        graphics.pose().popPose();
    }





    private void renderIngredients(GuiGraphics graphics, int mouseX, int mouseY, int x, int y) {

        List<CompoundTag> actions = RecipeScroll.getActions(scrollStack);

        int itemX = x + 232;
        int itemY = y + 110;

        int ingredientIndex = 0;

        for (CompoundTag actionTag : actions) {

            String type = actionTag.getString("Type");

            if (!type.equals("add_ingredient")) {
                continue;
            }

            CompoundTag data = actionTag.getCompound("Data");

            String itemId = data.getString("Item");

            Item item = ForgeRegistries.ITEMS.getValue(
                    ResourceLocation.parse(itemId)
            );

            if (item == null) {
                continue;
            }

            ItemStack ingredient = new ItemStack(item);

            int delayPerIngredient = 5;

            int startTick =
                    ingredientIndex * delayPerIngredient;

            float progress =
                    (animationTicks - startTick) / 8.0F;

            progress = Math.max(
                    0F,
                    Math.min(1F, progress)
            );

            float scale;

            if (progress < 0.7F) {
                scale = progress / 0.7F * 1.2F;
            } else {
                float t =
                        (progress - 0.7F) / 0.3F;

                scale =
                        1.2F - (0.2F * t);
            }

            renderAnimatedIngredient(
                    graphics,
                    ingredient,
                    itemX,
                    itemY,
                    scale
            );

            renderIngredientTooltip(
                    graphics,
                    ingredient,
                    mouseX,
                    mouseY,
                    itemX,
                    itemY
            );

            itemX += 20;
            ingredientIndex++;
        }
    }


    private void renderAnimatedIngredient(GuiGraphics graphics, ItemStack stack, int x, int y, float scale) {

        graphics.pose().pushPose();

        graphics.pose().translate(
                x + 8,
                y + 8,
                0
        );

        graphics.pose().scale(
                scale,
                scale,
                1F
        );

        graphics.renderItem(
                stack,
                -8,
                -8
        );

        graphics.pose().popPose();
    }


    private void renderIngredientTooltip(GuiGraphics graphics, ItemStack stack, int mouseX, int mouseY, int x, int y) {
        if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
            graphics.renderTooltip(this.font, stack, mouseX, mouseY);
        }
    }


    // =========================
    // STATS
    // =========================

    private void renderPotionStats(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, PotionLiquid pl) {

        if (pl == null) return;

        int startX = x + 115;
        int startY = y + 200;
        int spacing = 30;

        int vitality = getAnimatedValue(startVitality, targetVitality);
        int purity = getAnimatedValue(startPurity, targetPurity);
        int flavor = getAnimatedValue(startFlavor, targetFlavor);
        int stability = getAnimatedValue(startStability, targetStability);

        renderStatIcon(graphics, VITALITY_ICON, "Vitality", vitality,
                startX, startY, mouseX, mouseY);

        renderStatIcon(graphics, PURITY_ICON, "Purity",
                purity, startX + spacing , startY, mouseX, mouseY);

        renderStatIcon(graphics, FLAVOR_ICON, "Flavor",
                flavor, startX + spacing * 2, startY , mouseX, mouseY);

        renderStatIcon(graphics, STABILITY_ICON, "Stability",
                stability, startX + spacing * 3, startY , mouseX, mouseY);
    }

    private void renderStatIcon(GuiGraphics graphics, ResourceLocation texture, String tooltip, int value,
                                int x, int y, int mouseX, int mouseY) {

        graphics.pose().pushPose();

        float scale = 0.75F;

        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(scale, scale, 1F);

         int size = 16;

         graphics.blit(texture, 0, 0, 0, 0, size, size, size, size);

        graphics.setColor(1F, 1F, 1F, 1F);

        graphics.drawString(this.font,
                String.valueOf(value),
                22,
                4,
                0xFFD5AD);

        graphics.pose().popPose();


        if (mouseX >= x && mouseX <= x + (16 * scale) &&
                mouseY >= y && mouseY <= y + (16 * scale)) {

            graphics.renderTooltip(this.font,
                    Component.literal(tooltip),
                    mouseX, mouseY);
        }
    }



    private void startStatsAnimation(PotionLiquid pl) {

        animationStartTime = System.currentTimeMillis();

        startVitality = 0;
        startPurity = 0;
        startFlavor = 0;
        startStability = 0;

        targetVitality = pl.getStats().getVitality();
        targetPurity = pl.getStats().getPurity();
        targetFlavor = pl.getStats().getFlavor();
        targetStability = pl.getStats().getStability();
    }

    private float getAnimationProgress() {

        long elapsed = System.currentTimeMillis() - animationStartTime;

        return Math.min(1F, elapsed / (float) ANIMATION_DURATION);
    }

    private int getAnimatedValue(int start, int target) {

        float progress = getAnimationProgress();

        progress = 1F - (float)Math.pow(1F - progress, 3);

        return Math.round(
                Mth.lerp(progress, start, target)
        );
    }



    // =========================
    // EFFECTS
    // =========================


    private void renderPotionEffects(GuiGraphics graphics, int mouseX, int mouseY, int x, int y,
                                     PotionLiquid pl) {

        if (pl == null) return;

        List<MobEffect> effects =
                PotionLiquidUtils.getPossibleEffects(pl);

        if (effects.isEmpty()) return;

        int startX = x + 60;
        int startY = y + 110;

        int spacingX = 20;
        int spacingY = 20;

        int maxColumns = 3;
        int delayPerEffect = 5;
        for (int i = 0; i < effects.size(); i++) {

            MobEffect effect = effects.get(i);

            ResourceLocation id =
                    ForgeRegistries.MOB_EFFECTS.getKey(effect);

            if (id == null) continue;

            ResourceLocation texture =
                    ResourceLocation.fromNamespaceAndPath(
                            "minecraft",
                            "textures/mob_effect/" +
                                    id.getPath() +
                                    ".png"
                    );

            int column = i % maxColumns;
            int row = i / maxColumns;

            int iconX = startX + (column * spacingX);
            int iconY = startY + (row * spacingY);

            int startTick = i * delayPerEffect;

            float scale = getPopAnimationScale(startTick);

            renderAnimatedEffect(
                    graphics,
                    texture,
                    iconX,
                    iconY,
                    scale
            );

            if (mouseX >= iconX && mouseX <= iconX + 16 &&
                    mouseY >= iconY && mouseY <= iconY + 16) {

                int chance = PotionLiquidUtils.getEffectChance(pl, effect);

                graphics.renderTooltip(
                        this.font,
                        List.of(
                                Component.translatable(effect.getDescriptionId()),
                                Component.literal(chance + "%")
                        ),
                        java.util.Optional.empty(),
                        mouseX,
                        mouseY
                );
            }
        }
    }

    private void renderAnimatedEffect(GuiGraphics graphics, ResourceLocation texture, int x, int y, float scale) {

        graphics.pose().pushPose();

        graphics.pose().translate(
                x + 9,
                y + 9,
                0
        );

        graphics.pose().scale(
                scale,
                scale,
                1F
        );

        graphics.blit(
                texture,
                -9,
                -9,
                0,
                0,
                18,
                18,
                18,
                18
        );

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

    }



