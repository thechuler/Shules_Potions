package net.shule.shulespotions.Particles.Custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class AncientParticleProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public AncientParticleProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new AncientParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
    }
}