package fr.fyustorm.minetiface.mixin;

import fr.fyustorm.minetiface.commons.intiface.MinetifaceController;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
}
