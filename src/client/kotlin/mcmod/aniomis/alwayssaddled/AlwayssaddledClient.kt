package mcmod.aniomis.alwayssaddled

import mcmod.aniomis.alwayssaddled.mixin.client.EntityTrackedDataAccessor
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.passive.AbstractHorseEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Vec3d

object AlwayssaddledClient : ClientModInitializer {
	override fun onInitializeClient() {
		var lastUpdated: Long = 0
		var speed: Double? = null
		var jumpStrength: Double? = null
		var maxHealth: Double? = null
		var removedHorsePos: Vec3d? = null

		// TODO: start riding.
		UseEntityCallback.EVENT.register { player, world, hand, entity, hitResult ->
			if (player !== MinecraftClient.getInstance().player)
				return@register ActionResult.PASS

			val horse = entity as? AbstractHorseEntity ?: return@register ActionResult.PASS

			speed = horse.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)
			jumpStrength = horse.getAttributeValue(EntityAttributes.HORSE_JUMP_STRENGTH)
			maxHealth = horse.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH)
			removedHorsePos = horse.pos

			lastUpdated = System.currentTimeMillis()

			ActionResult.PASS
		}

		AbstractHorseRemovedCallback.EVENT.register { entity ->
			removedHorsePos = entity.pos
			lastUpdated = System.currentTimeMillis()
		}

		StartRidingCallback.EVENT.register { entity, player ->
			if (entity is AbstractHorseEntity) {
				speed = entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)
				jumpStrength = entity.getAttributeValue(EntityAttributes.HORSE_JUMP_STRENGTH)
				maxHealth = entity.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH)

				lastUpdated = System.currentTimeMillis()
				return@register
			}

			if (System.currentTimeMillis() - lastUpdated > 1000
				|| removedHorsePos?.isWithinRangeOf(entity.pos, 1.0, 1.0) == false
			) {
				return@register
			}

			if (entity is LivingEntity) {
				entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)?.baseValue = speed ?: 0.0
				// Attribute added to all living entities in HorseAttributesForLivingEntity
				entity.getAttributeInstance(EntityAttributes.HORSE_JUMP_STRENGTH)?.baseValue = jumpStrength ?: 0.0

				// LibsDisguises sets the NO_GRAVITY Entity Data to true. We need to fix that.
				entity.dataTracker.set(EntityTrackedDataAccessor.getNO_GRAVITY(), false)

				lastUpdated = 0
			}
		}
	}
}