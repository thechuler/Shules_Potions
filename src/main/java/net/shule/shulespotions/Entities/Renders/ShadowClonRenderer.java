package net.shule.shulespotions.Entities.Renders;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Entities.entity.ShadowClonEntity;
import net.shule.shulespotions.ShulesPotions;

public class ShadowClonRenderer extends HumanoidMobRenderer<ShadowClonEntity, PlayerModel<ShadowClonEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath
            (ShulesPotions.MODID, "textures/entity/shadow_clon.png");
    public ShadowClonRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowClonEntity pEntity) {
        return TEXTURE;
    }
}