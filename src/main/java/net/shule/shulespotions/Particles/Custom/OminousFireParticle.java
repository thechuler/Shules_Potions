package net.shule.shulespotions.Particles.Custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class OminousFireParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    protected OminousFireParticle(ClientLevel level, double x, double y, double z,
                             double r, double g, double b,
                             SpriteSet sprites){
        super(level, x, y, z, 0, 0, 0);

        this.sprites = sprites;


        this.xd = (random.nextDouble() - 0.5) * 0.02;
        this.yd = 0.02;
        this.zd = (random.nextDouble() - 0.5) * 0.02;

        this.lifetime = 20;
        this.gravity = 0.0f;
        this.friction = 0.98f;

        this.quadSize *= 1.6f;

        this.setSprite(sprites.get(random));

    }


    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);

        this.yd += 0.006;


    }




    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

}



