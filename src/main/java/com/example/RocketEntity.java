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
                    this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() - 1.0, this.getZ(), 0, -0.5, 0);
                    this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY() - 0.5, this.getZ(), 0, -0.8, 0);
                }
            } else {
                velocityY -= (gravity / 20.0);
            }

            double airDensity = Math.max(0, 1.0 - (this.getY() / 1000.0));
            double drag = 0.5 * airDensity * (velocityY * velocityY) * 0.42;
            velocityY -= (drag / totalMass) * Math.signum(velocityY);

            this.setDeltaMovement(new Vec3(0, velocityY / 20.0, 0));
            this.move(MoverType.SELF, this.getDeltaMovement());

            if (this.getY() > 500 && !this.level().isClientSide()) {
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