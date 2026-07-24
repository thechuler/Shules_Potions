package net.shule.shulespotions.Screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectButton extends Button {

    private final MobEffect effect;
    private final ResourceLocation texture;
    private long hoverStartTime = 0L;
    private boolean lastHovered = false;
    private static final long POP_DURATION = 180; // ms

    public EffectButton(int x, int y, int size, MobEffect effect, OnPress onPress) {
        super(x, y, size, size, Component.empty(), onPress, DEFAULT_NARRATION);
        this.effect = effect;
        setTooltip(Tooltip.create(Component.translatable(effect.getDescriptionId())));
        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);

        texture = ResourceLocation.fromNamespaceAndPath(
                id.getNamespace(),
                "textures/mob_effect/" + id.getPath() + ".png"
        );
    }

    public MobEffect getEffect() {
        return effect;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        boolean hovered = isHovered();

        if (hovered && !lastHovered) {
            hoverStartTime = System.currentTimeMillis();
        }

        lastHovered = hovered;

        float iconScale = width / 18F;

        if (hovered) {

            long elapsed = System.currentTimeMillis() - hoverStartTime;

            float progress = Math.min(1F, elapsed / (float) POP_DURATION);

            float extraScale;

            if (progress < 0.7F) {

                extraScale = (progress / 0.7F) * 0.20F;

            } else {

                float t = (progress - 0.7F) / 0.3F;

                extraScale = 0.20F - (0.10F * t);
            }

            iconScale *= (1F + extraScale);
        }


        graphics.pose().pushPose();

        graphics.pose().translate(
                getX() + width / 2F,
                getY() + height / 2F,
                0
        );

        graphics.pose().scale(
                iconScale,
                iconScale,
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

    private void renderGlow(GuiGraphics graphics, int x, int y, int width, int height) {

        graphics.setColor(1F, 1F, 0.5F, 0.35F);

        graphics.fill(x - 2, y - 2, x + width + 2,
                y + height + 2, 0xFFFFFFFF);

        graphics.setColor(1F, 1F, 1F, 1F);
    }
}