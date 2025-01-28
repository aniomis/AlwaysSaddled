package mcmod.aniomis.alwayssaddled.mixin.client;

import mcmod.aniomis.alwayssaddled.AbstractHorseRemovedCallback;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseAttributeWatcher extends AnimalEntity implements InventoryChangedListener, RideableInventory, Tameable, JumpingMount, Saddleable {

    protected AbstractHorseAttributeWatcher(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();

        if ((Object)this instanceof AbstractHorseEntity) {
            AbstractHorseRemovedCallback.EVENT.invoker().horseRemoved((AbstractHorseEntity) (Object) this);
        } else {
            // TODO: log an error
        }
    }
}
