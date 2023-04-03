package fr.fyustorm.minetiface.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.fyustorm.minetiface.intiface.MinetifaceController;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixins {
	@Inject(at = @At("TAIL"), method = "tick()V")
	private void playerTick(CallbackInfo ci) {
		MinetifaceController.getInstance().onPlayerTick();
	}

	@Inject(at = @At("HEAD"), method = "setExperience(FII)V")
	private void onXpAdded(float progress, int total, int level, CallbackInfo ci) {
		MinetifaceController.getInstance().onXpChange(progress, total, level);
	}

	@Inject(at = @At("INVOKE"), method = "updateHealth(F)V")
	private void onGoCommitDie(float health, CallbackInfo ci) {
		if (health == 0) {
			MinetifaceController.getInstance().onDeath();
		}
	}

	@Inject(at = @At("HEAD"), method = "requestRespawn()V")
	private void onRespawn(CallbackInfo ci) {
		MinetifaceController.getInstance().onRespawn();
	}
}
