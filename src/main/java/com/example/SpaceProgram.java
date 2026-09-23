package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SpaceProgram implements ModInitializer {
    public static final String MOD_ID = "spaceprogram";

    public static final Block LAUNCH_PAD = new LaunchPadBlock(BlockBehaviour.Properties.of().strength(4.0f).requiresCorrectToolForDrops());

    public static final EntityType<RocketEntity> ROCKET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(MOD_ID, "rocket"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, RocketEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 3.0f))
                    .build()
    );

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "launch_pad"), LAUNCH_PAD);
        BlockItem padItem = new BlockItem(LAUNCH_PAD, new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "launch_pad"), padItem);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> {
            content.accept(LAUNCH_PAD);
        });
    }
}