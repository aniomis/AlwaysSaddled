package mcmod.aniomis.alwayssaddled.mixin.client;

import com.mojang.authlib.GameProfile;
import mcmod.aniomis.alwayssaddled.AlwayssaddledClient;
import mcmod.aniomis.alwayssaddled.StartRidingCallback;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerStartRidingPublishing extends AbstractClientPlayerEntity {
	public PlayerStartRidingPublishing(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}


	@Inject(at = @At("HEAD"), method = "startRiding", cancellable = false)
	public void startRiding(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
		StartRidingCallback.EVENT.invoker().startRiding(entity, this);
	}
}
