package net.shule.shulespotions.Entities.Renders;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.shule.shulespotions.Entities.Models.PlayerLegModel;
import net.shule.shulespotions.Entities.entity.PlayerLegEntity;

public class PlayerLegRenderer extends PlayerBodyPartRenderer<PlayerLegEntity, PlayerLegModel<PlayerLegEntity>> {

    public PlayerLegRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerLegModel<>(context.bakeLayer(PlayerLegModel.LAYER_LOCATION)), 0.2F);
    }
}
