package net.shule.shulespotions.Screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Fluids.PotionFluidHelper;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.Potions.PotionLiquidUtils;

import java.util.List;

public class RecipeScrollScreen extends Screen {

    private final ItemStack scrollStack;

    private static final ResourceLocation SCROLL_BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/scroll_screen.png");

    private static final ResourceLocation VITALITY_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/vitality_icon.png");

    private static final ResourceLocation PURITY_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/purity_icon.png");

    private static final ResourceLocation FLAVOR_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/flavor_icon.png");

    private static final ResourceLocation STABILITY_ICON =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/stability_icon.png");

    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 166;

    public RecipeScrollScreen(ItemStack stack) {
        super(stack.getHoverName());
        this.scrollStack = stack;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        renderBackground(graphics);

        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;

        renderBackgroundTexture(graphics, x, y);

        super.render(graphics, mouseX, mouseY, partialTick);

        renderTitle(graphics, x, y);

        graphics.drawString(this.font, "Ingredientes", x + 35, y + 40, 0xFFD5AD);

        renderIngredients(graphics, mouseX, mouseY, x , y);

        PotionLiquid pl = getPotionLiquidFromStack(scrollStack);

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

    private void renderTitle(GuiGraphics graphics, int x, int y) {
        graphics.drawCenteredString(
                this.font,
                scrollStack.getHoverName(),
                x + GUI_WIDTH / 2,
                y + 25,
                0xFFD5AD
        );
    }

    // =========================
    // INGREDIENTS
    // =========================

    private void renderIngredients(GuiGraphics graphics, int mouseX, int mouseY, int x, int y) {

        CompoundTag tag = scrollStack.getTag();
        if (tag == null || !tag.contains("SPIngredients")) return;

        ListTag list = tag.getList("SPIngredients", Tag.TAG_STRING);

        int itemX = x + 40; //<---- Posicion inicial de la lista
        int itemY = y + 50;

        for (int i = 0; i < list.size(); i++) {

            String itemId = list.getString(i);

            Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(itemId));
            if (item == null) continue;

            ItemStack ingredient = new ItemStack(item);

            renderIngredientItem(graphics, ingredient, itemX, itemY);

            graphics.setColor(1F, 1F, 1F, 1F);

         //   renderIngredientTooltip(graphics, ingredient, mouseX, mouseY, itemX, itemY);

            itemX += 20; //<--- Espaciado
        }
    }

    private void renderIngredientItem(GuiGraphics graphics, ItemStack stack, int x, int y) {

        graphics.setColor(0F, 0F, 0F, 0.25F);
        graphics.renderItem(stack, x + 1, y + 1); //<-----Se agrega un render fantasma desfasado para crear una sombra :o

        graphics.setColor(0.55F, 0.45F, 0.30F, 0.85F);
        graphics.renderItem(stack, x, y);

        graphics.setColor(1F, 1F, 1F, 1F);
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

        int startX = x + 40;
        int startY = y + 80;
        int spacingY = 15;

        renderStatIcon(graphics, VITALITY_ICON, "Vitality",
                pl.getStats().getVitality(), startX, startY, mouseX, mouseY);

        renderStatIcon(graphics, PURITY_ICON, "Purity",
                pl.getStats().getPurity(), startX, startY + spacingY, mouseX, mouseY);

        renderStatIcon(graphics, FLAVOR_ICON, "Flavor",
                pl.getStats().getFlavor(), startX, startY + spacingY * 2, mouseX, mouseY);

        renderStatIcon(graphics, STABILITY_ICON, "Stability",
                pl.getStats().getStability(), startX, startY + spacingY * 3, mouseX, mouseY);
    }

    private void renderStatIcon(GuiGraphics graphics, ResourceLocation texture, String tooltip,
                                int value,
                                int x, int y,
                                int mouseX, int mouseY) {

        graphics.pose().pushPose();

        float scale = 0.75F;

        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(scale, scale, 1F);

        int size = 16;

        graphics.setColor(0F, 0F, 0F, 0.20F);
        graphics.blit(texture, 0 + 1, 0 + 1, 0, 0, size, size, size, size);

        graphics.setColor(0.65F, 0.55F, 0.40F, 0.90F);
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

    // =========================
    // EFFECTS
    // =========================

    private void renderPotionEffects(GuiGraphics graphics,
                                     int mouseX, int mouseY,
                                     int x, int y,
                                     PotionLiquid pl) {

        if (pl == null) return;

        List<MobEffect> effects = PotionLiquidUtils.resolve(pl);
        if (effects.isEmpty()) return;

        int startX = x + 89;
        int startY = y + 80;
        int spacingX = 18;

        for (int i = 0; i < effects.size(); i++) {

            MobEffect effect = effects.get(i);

            ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
            if (id == null) continue;

            ResourceLocation texture =
                    ResourceLocation.fromNamespaceAndPath(
                            "minecraft",
                            "textures/mob_effect/" + id.getPath() + ".png"
                    );

            int iconX = startX + (i * spacingX);
            int iconY = startY;

            graphics.setColor(0F, 0F, 0F, 0.20F);
            graphics.blit(texture, iconX + 1, iconY + 1, 0, 0, 18, 18, 18, 18);

            graphics.setColor(0.65F, 0.55F, 0.40F, 0.90F);
            graphics.blit(texture, iconX, iconY, 0, 0, 18, 18, 18, 18);

            graphics.setColor(1F, 1F, 1F, 1F);

            if (mouseX >= iconX && mouseX <= iconX + 16 &&
                    mouseY >= iconY && mouseY <= iconY + 16) {

                graphics.renderTooltip(this.font,
                        Component.translatable(effect.getDescriptionId()),
                        mouseX, mouseY);
            }
        }
    }

    // =========================
    // HELPERS
    // =========================

    private PotionLiquid getPotionLiquidFromStack(ItemStack stack) {

        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("SPPotionFluid")) return null;

        CompoundTag fluidTag = tag.getCompound("SPPotionFluid");

        FluidStack fluid = FluidStack.loadFluidStackFromNBT(fluidTag);

        return PotionFluidHelper.getPotionLiquid(fluid);
    }
}