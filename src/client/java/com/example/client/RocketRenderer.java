package com.example.client;

import com.example.RocketEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RocketRenderer extends EntityRenderer<RocketEntity> {
    private final BlockRenderManager blockRenderManager;

    public RocketRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render(RocketEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(-0.5, 0, -0.5);

        this.blockRenderManager.renderBlockAsEntity(Blocks.LIGHTNING_ROD.getDefaultState(), matrices, vertexConsumers, light, 0);
        matrices.translate(0, 1, 0);
        this.blockRenderManager.renderBlockAsEntity(Blocks.IRON_BLOCK.getDefaultState(), matrices, vertexConsumers, light, 0);
        matrices.translate(0, 1, 0);
        this.blockRenderManager.renderBlockAsEntity(Blocks.IRON_BLOCK.getDefaultState(), matrices, vertexConsumers, light, 0);

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(RocketEntity entity) {
        return null;
    }
}