package com.example;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SpaceStationGenerator {

    public static void buildOrbitalStation(Level level, BlockPos center, ServerPlayer player) {
        int radius = 4;
        int height = 4;

        // 1. Clear interior atmosphere and build hull
        for (int x = -radius; x <= radius; x++) {
            for (int y = -1; y <= height; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = center.offset(x, y, z);
                    boolean isFloor = (y == -1);
                    boolean isCeiling = (y == height);
                    boolean isWall = (Math.abs(x) == radius || Math.abs(z) == radius);

                    if (isFloor) {
                        // High-tech deck plating
                        level.setBlockAndUpdate(targetPos, Blocks.IRON_BLOCK.defaultBlockState());
                    } else if (isCeiling) {
                        // Internal lighting and ceiling tiles
                        if (x == 0 && z == 0) {
                            level.setBlockAndUpdate(targetPos, Blocks.SEA_LANTERN.defaultBlockState());
                        } else {
                            level.setBlockAndUpdate(targetPos, Blocks.SMOOTH_STONE_SLAB.defaultBlockState());
                        }
                    } else if (isWall) {
                        // Observation windows & armored bulkhead
                        if (y == 1 || y == 2) {
                            level.setBlockAndUpdate(targetPos, Blocks.TINTED_GLASS.defaultBlockState());
                        } else {
                            level.setBlockAndUpdate(targetPos, Blocks.SMOOTH_QUARTZ.defaultBlockState());
                        }
                    } else {
                        // Hollow out living cabin
                        level.setBlockAndUpdate(targetPos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }

        // 2. Interior station amenities: Workbench, Oxygen/Beacon Core, and Storage
        BlockPos bedPos = center.offset(2, 0, 2);
        level.setBlockAndUpdate(bedPos, Blocks.WHITE_BED.defaultBlockState());

        BlockPos craftPos = center.offset(-2, 0, 2);
        level.setBlockAndUpdate(craftPos, Blocks.CRAFTING_TABLE.defaultBlockState());

        BlockPos chestPos = center.offset(0, 0, -2);
        level.setBlockAndUpdate(chestPos, Blocks.BARREL.defaultBlockState());

        // 3. Stock food rations, golden carrots, and emergency oxygen apparatus
        BlockEntity blockEntity = level.getBlockEntity(chestPos);
        if (blockEntity instanceof Container container) {
            container.setItem(0, new ItemStack(Items.GOLDEN_CARROT, 32));
            container.setItem(1, new ItemStack(Items.COOKED_BEEF, 64));
            container.setItem(2, new ItemStack(Items.BREAD, 64));
            container.setItem(3, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 4));
            container.setItem(4, new ItemStack(Items.POTION, 3)); // Emergency medical supplies
            container.setItem(5, new ItemStack(Items.FIREWORK_ROCKET, 16));
            container.setItem(6, new ItemStack(Items.ELYTRA, 1)); // Return-to-Earth device
        }

        // 4. Mission Control broadcast
        player.sendSystemMessage(Component.literal("§b[ISS Intercom] Docking confirmed with Orbital Research Habitat. Welcome aboard!"));
    }
}