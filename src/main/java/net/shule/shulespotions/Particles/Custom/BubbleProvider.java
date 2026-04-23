package net.shule.shulespotions.Particles.Custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class BubbleProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public BubbleProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }


    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double r, double g, double b) {
        return new BubbleParticle(level, x, y, z, r, g, b, sprites);
    }
}
