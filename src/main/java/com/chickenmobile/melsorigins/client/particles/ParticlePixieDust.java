package com.chickenmobile.melsorigins.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class ParticlePixieDust extends SpriteBillboardParticle {

    public enum SparkleColour {
            PINK(0.94F, 0.85F, 0.98F),
            RED(0.96F, 0.68F, 0.75F),
            ORANGE(0.96F, 0.80F, 0.68F),
            YELLOW(0.95F, 0.96F, 0.68F),
            GREEN(0.70F, 0.96F, 0.68F),
            BLUE(0.68F, 0.85F, 0.96F),
            PURPLE(0.82F, 0.68F, 0.96F),
            WHITE(0.96F, 0.95F, 0.97F),
            GREY(0.91F, 0.90F, 0.91F),
            BLACK(0.62F, 0.61F, 0.64F);

            public final float r;
            public final float g;
            public final float b;
            
            SparkleColour(float r, float g, float b) {
                this.r = r;
                this.g = g;
                this.b = b;
            }
    }

    public ParticlePixieDust(ClientWorld world, double x, double y, double z,
                             SpriteProvider spriteSet, double vx, double vy, double vz) {
        super(world, x, y, z, vx, vy, vz);

        // Position of Particle
        this.x = x;
        this.y = y;
        this.z = z;

        // Velocity of particle
        this.velocityMultiplier = 0.6F;
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;

        // Scale
        this.scale *= 0.5F; // smaller
        this.maxAge = 40; // 2 seconds

        this.setSpriteForAge(spriteSet); // Important otherwise crashes

        // Colour, values are a decimal 0 to 1
        SparkleColour color = SparkleColour.BLUE;
        this.setColor(color.r, color.g, color.b); // each pixie has different coloured sparkles based on wings
        this.setAlpha(alpha);
    }

    private float agePercent;

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.agePercent = this.age / (float)this.maxAge;
        fadeOut();
        slowFall();
    }

    private void fadeOut(){
        this.alpha = 1 - agePercent;
    }

    // Make particles slowly fall when fading out
    private void slowFall(){
        // The more time that progresses, the faster it will fall. Up until it hits player's feet that is...
        this.y -= this.agePercent * 0.03F;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet){
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld world,
                                       double x, double y, double z, double vx, double vy, double vz) {
            return new ParticlePixieDust(world, x, y, z, this.sprites, vx, vy, vz);
        }
    }

}