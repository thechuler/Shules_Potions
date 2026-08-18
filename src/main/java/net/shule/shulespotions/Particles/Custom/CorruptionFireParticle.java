package net.shule.shulespotions.Particles.Custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class CorruptionFireParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected CorruptionFireParticle(ClientLevel level, double x, double y, double z,
                                        double r, double g, double b,
                                        SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);
        this.sprites = sprites;
        this.xd = (random.nextDouble() - 0.5) * 0.05;
        this.yd = 0.05 + random.nextDouble() * 0.05;
        this.zd = (random.nextDouble() - 0.5) * 0.05;

        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;

        this.lifetime = 20 + random.nextInt(20);
        this.gravity = -0.05f; // Makes it float upwards
        this.friction = 0.96f;
        this.quadSize *= 1.5f;

        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        this.yd += 0.005; // Accelerate upwards slightly
        this.alpha = 1.0f - ((float) this.age / this.lifetime);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
