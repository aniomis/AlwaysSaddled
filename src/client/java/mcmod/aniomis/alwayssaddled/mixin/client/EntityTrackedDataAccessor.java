package mcmod.aniomis.alwayssaddled.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/***
 * Access the metadata definitions for Entity
 */
@Mixin(Entity.class)
public interface EntityTrackedDataAccessor {
    @Accessor
    public static TrackedData<Boolean> getNO_GRAVITY() {
        throw new AssertionError();
    }
}
