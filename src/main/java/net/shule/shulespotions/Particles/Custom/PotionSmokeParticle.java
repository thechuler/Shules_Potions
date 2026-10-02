package net.shule.shulespotions.Particles.Custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class PotionSmokeParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected PotionSmokeParticle(
            ClientLevel level, double x, double y, double z,
            double r, double g, double b, SpriteSet sprites) {
        super(level, x, y, z);
        this.sprites = sprites;


        this.xd = (random.nextDouble() - 0.5) * 0.05;
        this.yd = 0.05 + random.nextDouble() * 0.05;
        this.zd = (random.nextDouble() - 0.5) * 0.05;


        double maxComponent = Math.max(r, Math.max(g, b));
        if (maxComponent > 0.001) {
            this.rCol = (float) Math.min(1.0, r / maxComponent);
            this.gCol = (float) Math.min(1.0, g / maxComponent);
            this.bCol = (float) Math.min(1.0, b / maxComponent);
            this.quadSize = (float) maxComponent;
        } else {
            this.rCol = (float) r;
            this.gCol = (float) g;
            this.bCol = (float) b;
            this.quadSize = 1.0f;
        }
        

        this.quadSize *= 0.75F + (this.random.nextFloat() * 0.25F);

        this.lifetime = 20 + this.random.nextInt(20);
        this.gravity = -0.01f;
        this.friction = 0.96f;

        this.setSpriteFromAge(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);

        if (this.age > this.lifetime / 2) {
            float halfLife = this.lifetime / 2.0F;
            float progress = (this.age - halfLife) / halfLife;
            this.alpha = Math.max(0.0F, 1.0F - (progress * progress));
        }
    }
}
