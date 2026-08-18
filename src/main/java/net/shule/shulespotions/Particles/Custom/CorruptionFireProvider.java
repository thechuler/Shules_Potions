package net.shule.shulespotions.Particles.Custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class CorruptionFireProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteSet;

    public CorruptionFireProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Nullable
    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                   double x, double y, double z,
                                   double r, double g, double b) {
        return new CorruptionFireParticle(level, x, y, z, r, g, b, this.spriteSet);
    }
}
