package net.shule.shulespotions.Particles.Custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class PotionSplashParticle extends TextureSheetParticle {

    private final float r;
    private final float g;
    private final float b;
    private boolean falling = false;

    protected PotionSplashParticle(ClientLevel level,
                                   double x, double y, double z,
                                   double r, double g, double b) {
        super(level, x, y, z);

        this.r = (float) r;
        this.g = (float) g;
        this.b = (float) b;

        this.quadSize = 0.12F + this.random.nextFloat() * 0.1F;

        this.lifetime = 20 + this.random.nextInt(10); // 🔥 más tiempo


        this.xd *= 0.2;
        this.zd *= 0.2;

        this.yd = 0.12F + this.random.nextFloat() * 0.05F;

        this.gravity = 1.35F;
    }

    @Override
    public void tick() {
        super.tick();


        if (!falling) {
            this.yd += 0.08;

            if (this.age > 3) {
                falling = true;
            }
        } else {
            this.yd -= 0.04;
        }


        this.xd *= 0.98;
        this.zd *= 0.98;

        if (this.onGround) {
            this.remove();
        }
    }


    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {

        float ageProgress = ((float) this.age + partialTicks) / (float) this.lifetime;

        float alpha = Mth.clamp(1.0F - ageProgress, 0.0F, 1.0F);

        this.setColor(r, g, b);
        this.setAlpha(alpha);

        super.render(buffer, camera, partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type,
                                       ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {

            PotionSplashParticle p = new PotionSplashParticle(level, x, y, z, xd, yd, zd);

            p.pickSprite(this.sprites);

            return p;
        }
    }

    public void setVelocity(double x, double y, double z) {
        this.xd = x;
        this.yd = y;
        this.zd = z;
    }
}