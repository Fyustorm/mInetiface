package fr.fyustorm.minetiface.mixin;

import fr.fyustorm.minetiface.commons.intiface.MinetifaceController;
import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixins {
    @Shadow @Final public static Logger LOGGER;
    private float oldHealth = -1;

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void playerTick(CallbackInfo ci) {
        MinetifaceController.getInstance().onPlayerTick();
    }

    @Inject(at = @At("HEAD"), method = "setExperience(FII)V")
    private void onXpAdded(float progress, int total, int level, CallbackInfo ci) {
        MinetifaceController.getInstance().onXpChange(progress, total, level);
    }

    @Inject(at = @At("HEAD"), method = "updateHealth(F)V")
    public void updateHealth(float health, CallbackInfo ci) {
        if (this.oldHealth == -1 ) {
                this.oldHealth = health;
        } else if (this.oldHealth > health) {
            MinetifaceController.getInstance().onHurt(this.oldHealth - health);
            this.oldHealth = health;
        } else {
            // MinetifaceController.getInstance().onHeal(health - this.oldHealth);
            this.oldHealth = health;
        }
    }
}
