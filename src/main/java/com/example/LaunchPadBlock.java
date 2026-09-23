package com.example;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class LaunchPadBlock extends Block {
    public LaunchPadBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            double dryMass = 1200.0;
            double fuelMass = 3000.0;
            double thrust = 85000.0;

            RocketEntity rocket = new RocketEntity(SpaceProgram.ROCKET, level);
            rocket.setPos(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
            rocket.configureSpecs(fuelMass, dryMass, thrust, 320.0);
            level.addFreshEntity(rocket);

            player.startRiding(rocket);
            rocket.ignite();

            player.sendSystemMessage(Component.literal("§a[Mission Control] Rocket Assembled & Ignited! Brace for launch."));
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}