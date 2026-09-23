package com.example.client;

import com.example.RocketEntity;
import com.example.SpaceProgram;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class SpaceProgramClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(SpaceProgram.ROCKET, RocketRenderer::new);

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && client.player.getVehicle() instanceof RocketEntity rocket) {
                int height = drawContext.getScaledWindowHeight();

                double totalMass = rocket.dryMass + rocket.currentFuel;
                double twr = rocket.thrustN / (totalMass * 9.81);

                drawContext.fill(10, height - 70, 210, height - 10, 0xCC020617);
                drawContext.drawText(client.textRenderer, "§b[ FLIGHT TELEMETRY ]", 15, height - 65, 0xFFFFFF, false);
                drawContext.drawText(client.textRenderer, "ALT: " + String.format("%.1f", rocket.getY()) + "m", 15, height - 50, 0xFFFFFF, false);
                drawContext.drawText(client.textRenderer, "VEL: " + String.format("%.1f", rocket.velocityY) + " m/s", 15, height - 38, 0xFFFFFF, false);
                drawContext.drawText(client.textRenderer, "FUEL: " + String.format("%.1f", rocket.currentFuel) + " kg", 15, height - 26, 0xF97316, false);
                drawContext.drawText(client.textRenderer, "TWR: " + String.format("%.2f", twr), 130, height - 26, 0x22C55E, false);
            }
        });
    }
}