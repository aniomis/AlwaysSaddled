package mcmod.aniomis.alwayssaddled

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.AbstractHorseEntity

fun interface AbstractHorseRemovedCallback {
    companion object {
        @JvmField
        val EVENT = EventFactory.createArrayBacked(AbstractHorseRemovedCallback::class.java) { listeners ->
            AbstractHorseRemovedCallback { entity ->
                for (listener in listeners) {
                    listener.horseRemoved(entity)
                }
            }
        }
    }

    fun horseRemoved(entity: AbstractHorseEntity)
}