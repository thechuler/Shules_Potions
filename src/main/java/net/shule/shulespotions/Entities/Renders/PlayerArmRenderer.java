package net.shule.shulespotions.Entities.Renders;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.shule.shulespotions.Entities.Models.PlayerArmModel;
import net.shule.shulespotions.Entities.entity.PlayerArmEntity;

public class PlayerArmRenderer extends PlayerBodyPartRenderer<PlayerArmEntity, PlayerArmModel<PlayerArmEntity>> {

    public PlayerArmRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerArmModel<>(context.bakeLayer(PlayerArmModel.LAYER_LOCATION)), 0.2F);
    }
}
