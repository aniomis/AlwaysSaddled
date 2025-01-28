package mcmod.aniomis.alwayssaddled.mixin.client;

import com.mojang.authlib.GameProfile;
import mcmod.aniomis.alwayssaddled.ducks.DisguisedJumpingMount;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerMountCanJump extends AbstractClientPlayerEntity {
    public PlayerMountCanJump(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }


    @Inject(at = @At("TAIL"), method = "getJumpingMount", cancellable = true)
    public void getJumpingMount(CallbackInfoReturnable<JumpingMount> cir) {
        if (cir.getReturnValue() != null) {
            return;
        }

        class JumpingProxy implements JumpingMount {
            public Entity disguisedHorseEntity;

            public JumpingProxy(Entity entity) {
                this.disguisedHorseEntity = entity;
            }

            @Override
            public void setJumpStrength(int strength) {
                if (disguisedHorseEntity instanceof DisguisedJumpingMount jumper) {
                    jumper.always_saddled_template_1_20_4$setJumpStrength(strength);
                }
            }

            @Override
            public boolean canJump() {
                return true;
            }

            @Override
            public void startJumping(int height) { }

            @Override
            public void stopJumping() { }
        }

        Entity vehicle = this.getControllingVehicle();
        if (vehicle instanceof LivingEntity livingEntity
            && livingEntity.getAttributeBaseValue(EntityAttributes.HORSE_JUMP_STRENGTH) > 0.0) {
            cir.setReturnValue(new JumpingProxy(vehicle));
        }
    }
}
