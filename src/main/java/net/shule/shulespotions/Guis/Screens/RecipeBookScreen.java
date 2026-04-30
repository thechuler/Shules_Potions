package net.shule.shulespotions.Guis.Screens;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.shule.shulespotions.Guis.Menus.RecipeBookMenu;
import net.shule.shulespotions.Items.custom.RecipeSheetItem;

import static net.shule.shulespotions.util.RecipeUtils.resolveMobEffect;

public class RecipeBookScreen extends AbstractContainerScreen<RecipeBookMenu> {

    public RecipeBookScreen(RecipeBookMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

        guiGraphics.fill(this.leftPos, this.topPos,
                this.leftPos + this.imageWidth,
                this.topPos + this.imageHeight,
                0xFF202020);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int y = 10;

        guiGraphics.drawString(this.font, this.title, 8, 6, 0xFFFFFF, false);

        var level = Minecraft.getInstance().level;

        if (level == null) return;

        for (ResourceLocation recipeId : menu.getRecipes()) {

            int finalY = y;
            RecipeSheetItem.getRecipe(level, recipeId).ifPresent(recipe -> {


                MobEffect effect = resolveMobEffect(level,recipe.getEffectId());


                if (effect != null) {
                    Component name = Component.translatable(effect.getDescriptionId());
                    int color = effect.getColor();

                    guiGraphics.drawString(this.font, name, 8, finalY + 15, color, false);
                }
            });

            y += 10;
        }
    }
}