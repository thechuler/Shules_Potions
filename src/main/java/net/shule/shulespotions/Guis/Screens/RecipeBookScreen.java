package net.shule.shulespotions.Guis.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.shule.shulespotions.Guis.Menus.RecipeBookMenu;

public class RecipeBookScreen extends AbstractContainerScreen<RecipeBookMenu> {

    private final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("shulespotions","textures/gui/recipe_book_frame.png");
    public RecipeBookScreen(RecipeBookMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 256;
        this.imageHeight = 180;;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0,TEXTURE);
        graphics.blit(TEXTURE,leftPos,topPos,0,0,imageWidth,imageHeight);
    }


    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new Button.Builder(Component.literal("HOLA"),Button::onPress));



    }

    private void addRenderableWidget(Button.Builder hola) {
    }
}
