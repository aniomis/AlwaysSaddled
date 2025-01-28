package mcmod.aniomis.alwayssaddled.mixin.client;

import mcmod.aniomis.alwayssaddled.ducks.DisguisedJumpingMount;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Targeter;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityAlwaysControllable extends LivingEntity implements Targeter, DisguisedJumpingMount {
	@Unique
	private float jumpStrength = 0f;
	@Unique
	private boolean inAir;

	protected MobEntityAlwaysControllable(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "getControllingPassenger", cancellable = true)
	private void playerFirstPassengerAlwaysControlling(CallbackInfoReturnable<LivingEntity> cir) {
		Entity entity = getFirstPassenger();
		if (entity instanceof PlayerEntity) {
			cir.setReturnValue((LivingEntity)entity);
		}
	}

	@Override
	protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
		// TODO: understand this code. cribbed from the Abstract Horse
		float f = controllingPlayer.sidewaysSpeed * 0.5F;
		float g = controllingPlayer.forwardSpeed;
		if (g <= 0.0F) {
			g *= 0.25F;
		}

		return new Vec3d((double)f, 0.0, (double)g);
	}

	@Unique
	public void setInAir(boolean inAir) {
		this.inAir = inAir;
	}

	@Unique
	public boolean isInAir() {
		return this.inAir;
	}

	@Override
	protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
		// cribbed from the Abstract Horse
		super.tickControlled(controllingPlayer, movementInput);
		Vec2f vec2f = new Vec2f(controllingPlayer.getPitch() * 0.5F, controllingPlayer.getYaw());
		this.setRotation(vec2f.y, vec2f.x);
		this.prevYaw = this.bodyYaw = this.headYaw = this.getYaw();

		if (this.isLogicalSideForUpdatingMovement()) {
			if (this.isOnGround()) {
				this.setInAir(false);
				if (this.jumpStrength > 0.0F && !this.isInAir()) {
					this.jump(this.jumpStrength, movementInput);
				}

				this.jumpStrength = 0.0F;
			}
		}
	}

	@Unique
	protected void jump(float strength, Vec3d movementInput) {
		double d = this.getAttributeValue(EntityAttributes.HORSE_JUMP_STRENGTH) * (double)strength * (double)this.getJumpVelocityMultiplier();
		double e = d + (double)this.getJumpBoostVelocityModifier();
		Vec3d vec3d = this.getVelocity();
		this.setVelocity(vec3d.x, e, vec3d.z);
		this.setInAir(true);
		this.velocityDirty = true;
		if (movementInput.z > 0.0) {
			float f = MathHelper.sin(this.getYaw() * (float) (Math.PI / 180.0));
			float g = MathHelper.cos(this.getYaw() * (float) (Math.PI / 180.0));
			this.setVelocity(this.getVelocity().add((double)(-0.4F * f * strength), 0.0, (double)(0.4F * g * strength)));
		}
	}

	@Override
	protected float getSaddledSpeed(PlayerEntity controllingPlayer) {
		return (float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
	}

	@Override
	public void always_saddled_template_1_20_4$setJumpStrength(int strength) {
		if (strength < 0) {
			strength = 0;
		} else {
			//this.jumping = true;
		}

		if (strength >= 90) {
			this.jumpStrength = 1.0F;
		} else {
			this.jumpStrength = 0.4F + 0.4F * (float)strength / 90.0F;
		}
	}
}