
package com.example;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RocketEntity extends Entity {
    public double currentFuel;
    public double dryMass;
    public double thrustN;
    public double burnRate;
    public double velocityY = 0;
    public boolean isLaunched = false;

    public RocketEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void configureSpecs(double fuelMass, double dryMass, double thrustN, double isp) {
        this.currentFuel = fuelMass;
        this.dryMass = dryMass;
        this.thrustN = thrustN;
        this.burnRate = thrustN / (isp * 9.81);
    }

    public void ignite() {
        this.isLaunched = true;
        this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 2.0f, 0.5f);
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

                if (this.getWorld().isClient) {
                    this.getWorld().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() - 1.0, this.getZ(), 0, -0.5, 0);
                    this.getWorld().addParticle(ParticleTypes.FLAME, this.getX(), this.getY() - 0.5, this.getZ(), 0, -0.8, 0);
                }
            } else {
                velocityY -= (gravity / 20.0);
            }

            double airDensity = Math.max(0, 1.0 - (this.getY() / 1000.0));
            double drag = 0.5 * airDensity * (velocityY * velocityY) * 0.42;
            velocityY -= (drag / totalMass) * Math.signum(velocityY);

            this.setVelocity(new Vec3d(0, velocityY / 20.0, 0));
            this.move(MovementType.SELF, this.getVelocity());

            if (this.getY() > 500 && !this.getWorld().isClient) {
                this.discard();
            }
        }
    }

    @Override
    protected void initDataTracker() {}
    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}
    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}
}