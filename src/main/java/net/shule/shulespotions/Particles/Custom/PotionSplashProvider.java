package net.shule.shulespotions.Particles.Custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class PotionSplashProvider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet sprites;

    public PotionSplashProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Override
    public Particle createParticle(SimpleParticleType type,
                                   ClientLevel level,
                                   double x, double y, double z,
                                   double r, double g, double b) {

        PotionSplashParticle p =
                new PotionSplashParticle(level, x, y, z,
                        (float) r, (float) g, (float) b);

        double angle = level.random.nextDouble() * Math.PI * 2;
        double speed = 0.05 + level.random.nextDouble() * 0.08;

        p.setVelocity(
                Math.cos(angle) * speed,
                0.12 + level.random.nextDouble() * 0.05,
                Math.sin(angle) * speed
        );

        p.pickSprite(this.sprites);
        return p;
    }
}