package net.shule.shulespotions.Entities.Renders;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.shule.shulespotions.Entities.Models.PlayerHeadModel;
import net.shule.shulespotions.Entities.entity.PlayerHeadEntity;

public class PlayerHeadRenderer extends PlayerBodyPartRenderer<PlayerHeadEntity, PlayerHeadModel<PlayerHeadEntity>> {

    public PlayerHeadRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerHeadModel<>(context.bakeLayer(PlayerHeadModel.LAYER_LOCATION)), 0.25F);
    }
}
