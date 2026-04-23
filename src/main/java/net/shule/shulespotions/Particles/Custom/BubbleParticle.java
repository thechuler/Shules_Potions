package net.shule.shulespotions.Particles.Custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class BubbleParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected BubbleParticle(ClientLevel level, double x, double y, double z,
                             double r, double g, double b,
                             SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0); // importante

        this.sprites = sprites;

        // Movimiento random o leve
        this.xd = (random.nextDouble() - 0.5) * 0.02;
        this.yd = 0.02;
        this.zd = (random.nextDouble() - 0.5) * 0.02;

        // Color dinámico
        this.rCol = (float) r;
        this.gCol = (float) g;
        this.bCol = (float) b;

        this.lifetime = 40;
        this.gravity = 0.0f;
        this.friction = 0.98f;

        this.quadSize *= 1.2f;

        this.setSprite(sprites.get(random));
    }

    @Override
    public void tick() {
        super.tick();

        // Animar sprite
       // this.setSpriteFromAge(sprites);

        // subir lentamente
        this.yd += 0.002;

        // fade out
        this.alpha = 1.0f - ((float) this.age / this.lifetime);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

}
