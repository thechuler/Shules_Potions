package net.shule.shulespotions.Particles.Custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class OminousFireProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public OminousFireProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                   double x, double y, double z,
                                   double xSpeed, double ySpeed, double zSpeed) {
        return new OminousFireParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
    }
}
