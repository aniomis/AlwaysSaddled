package mcmod.aniomis.alwayssaddled.mixin.client;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class HorseAttributesForLivingEntity {
    @Inject(at = @At("TAIL"), method = "createLivingAttributes", cancellable = true)
    private static void createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(
            cir.getReturnValue().add(EntityAttributes.HORSE_JUMP_STRENGTH, 0.0)
        );
    }
}
