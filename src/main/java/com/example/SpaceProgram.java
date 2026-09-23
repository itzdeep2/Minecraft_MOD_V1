package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpaceProgram implements ModInitializer {
    public static final String MOD_ID = "spaceprogram";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Block LAUNCH_PAD = new LaunchPadBlock(FabricBlockSettings.create().strength(4.0f).requiresTool());

    public static final EntityType<RocketEntity> ROCKET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "rocket"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RocketEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 3.0f))
                    .build()
    );

    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "launch_pad"), LAUNCH_PAD);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "launch_pad"), new BlockItem(LAUNCH_PAD, new FabricItemSettings()));
        LOGGER.info("Space Program Initialized. Flight systems online.");
    }
}