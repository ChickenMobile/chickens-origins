package com.chickenmobile.chickensorigins.client.renderers;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import com.chickenmobile.chickensorigins.ChickensOriginsClient;
import com.chickenmobile.chickensorigins.client.models.ButterflyWingModel;
import com.chickenmobile.chickensorigins.client.particles.ParticlePixieDust;
import com.chickenmobile.chickensorigins.core.registry.ModParticles;
import com.chickenmobile.chickensorigins.util.WingsData;
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
	private final ButterflyWingModel<T> butterflyWings;
	private long lastRenderTime;
	private final int sparkleRenderDelay = 10;

	public WingsFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
		super(context);
		this.butterflyWings = new ButterflyWingModel<>(loader.getModelPart(ChickensOriginsClient.BUTTERFLY));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (entity instanceof PlayerEntity player && ChickensOrigins.IS_PIXIE.test(player)) {
			// Get wingType from wings data.
			String wingType = WingsData.WINGS_DATA.WING_MAP.getOrDefault(player.getUuidAsString(), "none");
			// 'None' is a valid, even for pixie. Don't render.
			if (wingType == "none") {
				return;
			}
			Identifier layer = new Identifier(ChickensOrigins.MOD_ID, "textures/entity/" + wingType + ".png");

			matrices.push();
			matrices.translate(0.0D, -0.5D, 0.2D);
			this.getContextModel().copyStateTo(this.butterflyWings);
			this.butterflyWings.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
			float[] color = DyeColor.WHITE.getColorComponents();
			this.renderWings(matrices, vertexConsumers, RenderLayer.getEntityTranslucent(layer), light, color[0], color[1], color[2]);
			matrices.pop();

			// Render pixie sparkle.
			renderParticles(player, wingType);
		}
	}

	public void renderWings(MatrixStack matrices, VertexConsumerProvider vertexConsumers, RenderLayer renderLayer, int light, float r, float g, float b) {
		VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, renderLayer, false, false);
		this.butterflyWings.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, r, g, b, 0.9F);
	}

	// Unused for now
	public void renderParticles(PlayerEntity player, String wingType) {
		//Random rand = player.world.random;
		// Don't render too many sparkles, render is 3x a tick (too much). This includes when game is paused in SP.
		if (player.age != lastRenderTime && !player.isOnGround() && player.age % sparkleRenderDelay == 0) {
			lastRenderTime = player.age;
			// Create particle based on facing of player
			// minecraft is off by 90deg for normal sin/cos circle mathos
//			double yaw = Math.toRadians(player.getBodyYaw() - 90);
//			double xCos = Math.cos(yaw);
//			double zSin = Math.sin(yaw);

			ParticlePixieDust.SparkleColor color = switch (wingType) {
				case "birdwing", "green_spotted_triangle" -> ParticlePixieDust.SparkleColor.GREEN;
				case "blue_morpho" -> ParticlePixieDust.SparkleColor.BLUE;
				case "buckeye" -> ParticlePixieDust.SparkleColor.BROWN;
				case "monarch" -> ParticlePixieDust.SparkleColor.ORANGE;
				case "pink_rose" -> ParticlePixieDust.SparkleColor.PINK;
				case "purple_emperor" -> ParticlePixieDust.SparkleColor.PURPLE;
				case "red_lacewing" -> ParticlePixieDust.SparkleColor.RED;
				case "tiger_swallowtail" -> ParticlePixieDust.SparkleColor.YELLOW;
				default -> ParticlePixieDust.SparkleColor.WHITE;
			};

			// Create & Render the particle
//			player.getWorld().addParticle(ModParticles.PARTICLE_PIXIE_DUST,
//					player.getX() + (0.2F * xCos),
//					player.getY() + 0.2F + 0.1F * rand.nextGaussian(),
//					player.getZ() + (0.2F * zSin),
//					color.r, color.g, color.b);
			player.getWorld().addParticle(ModParticles.PARTICLE_PIXIE_DUST,
					player.getParticleX(1.0),
					player.getRandomBodyY(),
					player.getParticleZ(1.0),
					color.r, color.g, color.b);

			// rand.nextGaussian()) * 0.005D
		}
	}

}