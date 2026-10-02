package net.shule.shulespotions.Particles.Custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class AncientParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final double centerX;
    private final double centerY;
    private final double centerZ;
    private final double radius;
    private final double angularSpeed;
    private double currentAngle;
    private final double yOscillationSpeed;

    public AncientParticle(ClientLevel level, double x, double y, double z,
                           double radius, double startAngle, double angularSpeed,
                           SpriteSet sprites) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.centerX = x;
        this.centerY = y;
        this.centerZ = z;
        this.radius = radius > 0 ? radius : 0.5D;
        this.currentAngle = startAngle;
        this.angularSpeed = angularSpeed != 0 ? angularSpeed : 0.08D;
        this.yOscillationSpeed = 0.08D + (this.random.nextDouble() * 0.04D);

        this.x = this.centerX + Math.cos(this.currentAngle) * this.radius;
        this.y = this.centerY + (this.random.nextDouble() - 0.5D) * 0.2D;
        this.z = this.centerZ + Math.sin(this.currentAngle) * this.radius;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.quadSize = 0.22F * (this.random.nextFloat() * 0.4F + 0.8F);
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.lifetime = (int)(Math.random() * 20.0D) + 50;
        this.hasPhysics = false;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    @Override
    public int getLightColor(float partialTick) {
        return 240;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.currentAngle += this.angularSpeed;
            this.x = this.centerX + Math.cos(this.currentAngle) * this.radius;
            this.z = this.centerZ + Math.sin(this.currentAngle) * this.radius;
            this.y = this.centerY + Math.sin((double)this.age * this.yOscillationSpeed) * 0.08D;
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}