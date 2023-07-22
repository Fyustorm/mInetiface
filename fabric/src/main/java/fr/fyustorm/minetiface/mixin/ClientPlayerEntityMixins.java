package fr.fyustorm.minetiface.mixin;

import fr.fyustorm.minetiface.commons.intiface.MinetifaceController;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixins {
    @Shadow @Final public static Logger LOGGER;

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void playerTick(CallbackInfo ci) {
        MinetifaceController.getInstance().onPlayerTick();
    }

    @Inject(at = @At("HEAD"), method = "setExperience(FII)V")
    private void onXpAdded(float progress, int total, int level, CallbackInfo ci) {
        MinetifaceController.getInstance().onXpChange(progress, total, level);
    }

    @Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        MinetifaceController.getInstance().onHurt(amount);
    }
}
