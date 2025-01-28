package mcmod.aniomis.alwayssaddled

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.entity.Entity

fun interface StartRidingCallback {
    companion object {
        @JvmField
        val EVENT = EventFactory.createArrayBacked(StartRidingCallback::class.java) { listeners ->
            StartRidingCallback { entity, player ->
                for (listener in listeners) {
                    listener.startRiding(entity, player)
                }
            }
        }
    }

    fun startRiding(entity: Entity, player: AbstractClientPlayerEntity)
}