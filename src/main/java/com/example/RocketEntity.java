package com.example;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RocketEntity extends Entity {
    public double currentFuel;
    public double dryMass;
    public double thrustN;
    public double burnRate;
    public double velocityY = 0;
    public boolean isLaunched = false;

    public RocketEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void configureSpecs(double fuelMass, double dryMass, double thrustN, double isp) {
        this.currentFuel = fuelMass;
        this.dryMass = dryMass;
        this.thrustN = thrustN;
        this.burnRate = thrustN / (isp * 9.81);
    }

    public void ignite() {
        this.isLaunched = true;
        this.level().playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 2.0f, 0.5f);
    }

    @Override
    public void tick() {
        super.tick();
        if (isLaunched) {
            double totalMass = dryMass + currentFuel;
            double gravity = 9.81;

            if (currentFuel > 0) {
                double acceleration = (thrustN / totalMass) - gravity;
                velocityY += (acceleration / 20.0);
                currentFuel = Math.max(0, currentFuel - (burnRate / 20.0));

                if (this.level().isClientSide()) {
                    // Stage 1: Intense Core Flame Plume
                    for (int i = 0; i < 3; i++) {
                        double spreadX = (this.random.nextDouble() - 0.5) * 0.4;
                        double spreadZ = (this.random.nextDouble() - 0.5) * 0.4;
                        this.level().addParticle(
                                ParticleTypes.FLAME,
                                this.getX() + spreadX,
                                this.getY() - 0.2,
                                this.getZ() + spreadZ,
                                spreadX * 0.2,
                                -1.2,
                                spreadZ * 0.2
                        );
                    }

                    // Stage 2: Heavy Smoke Trail
                    this.level().addParticle(
                            ParticleTypes.CAMPFIRE_COSY_SMOKE,
                            this.getX(),
                            this.getY() - 0.8,
                            this.getZ(),
                            (this.random.nextDouble() - 0.5) * 0.1,
                            -0.4,
                            (this.random.nextDouble() - 0.5) * 0.1
                    );

                    // Stage 3: Transonic Vapor Cone (high-velocity shockwave ring)
                    if (velocityY > 80.0 && velocityY < 140.0) {
                        for (int angle = 0; angle < 360; angle += 45) {
                            double rad = Math.toRadians(angle);
                            this.level().addParticle(
                                    ParticleTypes.CLOUD,
                                    this.getX() + Math.cos(rad) * 0.8,
                                    this.getY() + 0.5,
                                    this.getZ() + Math.sin(rad) * 0.8,
                                    Math.cos(rad) * 0.1,
                                    0.05,
                                    Math.sin(rad) * 0.1
                            );
                        }
                    }
                }
            } else {
                // Engine flameout / coast phase
                velocityY -= (gravity / 20.0);
                if (this.level().isClientSide()) {
                    this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() - 0.3, this.getZ(), 0, -0.1, 0);
                }
            }

            // Atmosphere Drag
            double airDensity = Math.max(0, 1.0 - (this.getY() / 1000.0));
            double drag = 0.5 * airDensity * (velocityY * velocityY) * 0.42;
            velocityY -= (drag / totalMass) * Math.signum(velocityY);

            // Shudder/rumble under full thrust
            double rumble = (currentFuel > 0 && velocityY > 20) ? (this.random.nextDouble() - 0.5) * 0.06 : 0.0;

            this.setDeltaMovement(new Vec3(rumble, velocityY / 20.0, rumble));
            this.move(MoverType.SELF, this.getDeltaMovement());

            if (this.getY() > 550 && !this.level().isClientSide()) {
                this.discard();
            }
        }
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}