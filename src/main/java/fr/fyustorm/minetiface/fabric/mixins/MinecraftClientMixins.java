package fr.fyustorm.minetiface.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.fyustorm.minetiface.intiface.MinetifaceController;
import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixins {
	@Inject(at = @At("TAIL"), method = "tick()V")
	private void onTickEnd(CallbackInfo ci) {
		MinetifaceController.getInstance().onClientTick();
	}
}
