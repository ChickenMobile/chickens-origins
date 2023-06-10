package com.chickenmobile.melsorigins.client.renderers;

import com.chickenmobile.melsorigins.MelsOrigins;
import com.chickenmobile.melsorigins.client.MelsOriginsClient;
import com.chickenmobile.melsorigins.client.models.ButterflyWingModel;
import com.chickenmobile.melsorigins.core.registry.ModParticles;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class WingsFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private ButterflyWingModel<T> butterflyWings;
	private WingType wingType = WingType.BLUEMORPHO;

	public enum WingType {
		MONARCH, BLUEMORPHO, BUCKEYE
	}

	public WingsFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
		super(context);
		this.butterflyWings = new ButterflyWingModel<>(loader.getModelPart(MelsOriginsClient.BUTTERFLY));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		// TODO: Render only if pixie.
		if(entity instanceof PlayerEntity player && MelsOrigins.IS_PIXIE.test(player)) {
			//TODO: Get wingType from 'dye' colour in player data
			Identifier layer = new Identifier(MelsOrigins.MOD_ID, "textures/entity/" + wingType.toString().toLowerCase() + ".png");
			float[] color = DyeColor.WHITE.getColorComponents();

			matrices.push();
			matrices.translate(0.0D, -0.5D, 0.125D);
			this.getContextModel().copyStateTo(this.butterflyWings);
			this.butterflyWings.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
			this.renderWings(matrices, vertexConsumers, RenderLayer.getEntityTranslucent(layer), light, color[0], color[1], color[2]);
			matrices.pop();

			// Render pixie sparkle
			//renderParticles(player);
		}
	}

	public void renderWings(MatrixStack matrices, VertexConsumerProvider vertexConsumers, RenderLayer renderLayer, int light, float r, float g, float b) {
		VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, renderLayer, false, false);
		this.butterflyWings.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, r, g, b, 0.9F);
	}

	public void renderParticles(PlayerEntity player) {
		Random rand = player.world.random;
		if (!player.isOnGround() && rand.nextInt(30) == 0) {
			// Create particle based on facing of player
			// minecraft is off by 90deg for normal sin/cos circle mathos
			double yaw = Math.toRadians(player.getBodyYaw() - 90);
			double xCos = Math.cos(yaw);
			double zSin = Math.sin(yaw);

			// Create & Render the particle
			player.getWorld().addParticle(ModParticles.PARTICLE_PIXIE_DUST,
					player.getX() + (0.2F * xCos),
					player.getY() + 0.2F + 0.1F * rand.nextGaussian(),
					player.getZ() + (0.2F * zSin),
					0, 0, 0);

			// rand.nextGaussian()) * 0.005D
		}
	}

}