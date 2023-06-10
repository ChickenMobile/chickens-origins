package com.chickenmobile.chickensorigins.mixin.client;

import com.chickenmobile.chickensorigins.client.renderers.WingsFeatureRenderer;
import com.chickenmobile.chickensorigins.core.integration.Origins;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
		// Pixie wing renderer
		this.addFeature(new WingsFeatureRenderer(this, ctx.getModelLoader()));
	}

	@Inject(method = "render", at = @At("HEAD"))
	public void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		// Make player appear to bob up and down if flying AND a pixie
		// TODO: check if pixie
		if(Origins.isPixie(abstractClientPlayerEntity)) {
			if (!abstractClientPlayerEntity.isOnGround() && !abstractClientPlayerEntity.isSubmergedInWater()) {
				double bobY = Math.sin(abstractClientPlayerEntity.age / 10.0F) / 10.0F;
				matrixStack.translate(0, bobY, 0);
			}
		}
	}
}
