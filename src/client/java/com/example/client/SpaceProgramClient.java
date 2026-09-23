package com.example.client;

import com.example.SpaceProgram;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class SpaceProgramClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(SpaceProgram.ROCKET, RocketRenderer::new);
    }
}