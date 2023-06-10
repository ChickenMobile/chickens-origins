package com.chickenmobile.melsorigins.client.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;

import static java.awt.geom.Point2D.distanceSq;

public class ButterflyWingModel<T extends LivingEntity> extends AnimalModel<T> {
	public final ModelPart rWing;
	public final ModelPart lWing;
	public final ModelPart wingBase;
	public State state = State.IDLE;
	private final Float clampValue = 0.05f;
	private Float prevSpeedValue = 0.0f;

	public ButterflyWingModel(ModelPart root) {
		this.wingBase = root.getChild("wingBase");
		this.rWing = this.wingBase.getChild("rWing");
		this.lWing = this.wingBase.getChild("lWing");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		// Holds both wings and is rotated with player
		ModelPartData wingBasePart =
				modelPartData.addChild("wingBase", ModelPartBuilder.create(), ModelTransform.NONE);

		wingBasePart.addChild("rWing", ModelPartBuilder.create()
				.uv(0, 0)
				.cuboid(0.0F, 0.0F, 0.0F, 16.0F, 24.0F, 0.0F),
				ModelTransform.NONE);

		wingBasePart.addChild("lWing", ModelPartBuilder.create()
				.uv(0, 0).mirrored() // Hopefully works?
				.cuboid(-16.0F, 0.0F, 0.0F, 16.0F, 24.0F, 0.0F),
				ModelTransform.NONE);

		return TexturedModelData.of(modelData, 32, 24);
	}

	private double distanceTo(Vector3d p1, Vector3d p2) {
		return Math.abs(Math.sqrt(
				Math.pow(p2.x - p1.x, 2) +
				Math.pow(p2.y - p1.y, 2) +
				Math.pow(p2.z - p1.z, 2)));
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		if(entity instanceof PlayerEntity player) {
			this.wingBase.resetTransform();

			if(player.isInSneakingPose()){
				this.wingBase.translate(new Vec3f(0, 4.2F, -3.0F));
				this.wingBase.pitch = 0.5F;
			}

			float age = player.age / 20.0F; // Seconds
			if(player.isOnGround()){
				this.lWing.yaw = (float)Math.sin(age) / 5F + 0.8F;
			}
			else {
				// Wings flap a little faster while hovering & moving vs standing on ground
				// Get speed in dist based on previous coords to now
				double dist = distanceTo(
						new Vector3d(player.getX(), player.getY(), player.getZ()),
						new Vector3d(player.prevX, player.prevY, player.prevZ));

				// Clamp value so wings do erratically change speeds
				if (dist > dist + this.clampValue) {
					dist = prevSpeedValue + this.clampValue;
				}
				else if (dist < dist - this.clampValue){
					dist = prevSpeedValue - this.clampValue;
				}
				this.prevSpeedValue = (float)dist; // Set prev value

				// Min +5x, Max +6x anim speed
				float magnitude = (float)Math.min(1F + dist, 2F) + 4F;

				// yaw is from (0 to Math.PI) therefore * PI/2 will get a full rotation of angle of SIN -1 to 1
				// SIN determines how fast wings flap, changed by magnitude which is always >1.
				// +xF = start angle more towards back
				this.lWing.yaw = (float)Math.sin(age * magnitude) / 2F + 0.8F;

				// Swing limbs less
				player.limbDistance *= 0.8F;
			}

			this.rWing.yaw = -this.lWing.yaw;
		}
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.wingBase);
	}

	public enum State {
		IDLE, CROUCHING, FLYING
	}
}