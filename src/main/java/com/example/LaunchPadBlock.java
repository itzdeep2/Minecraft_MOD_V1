package com.example;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LaunchPadBlock extends Block {
    public LaunchPadBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            double dryMass = 1200.0;
            double fuelMass = 3000.0;
            double thrust = 85000.0;

            RocketEntity rocket = new RocketEntity(SpaceProgram.ROCKET, world);
            rocket.setPosition(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
            rocket.configureSpecs(fuelMass, dryMass, thrust, 320.0);
            world.spawnEntity(rocket);

            player.startRiding(rocket);
            rocket.ignite();

            player.sendMessage(Text.literal("§a[Mission Control] Rocket Assembled & Ignited! Brace for launch."), false);
        }
        return ActionResult.SUCCESS;
    }
}