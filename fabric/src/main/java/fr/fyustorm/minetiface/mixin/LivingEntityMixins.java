package fr.fyustorm.minetiface.mixin;

import fr.fyustorm.minetiface.commons.intiface.MinetifaceController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import java.util.UUID;

@Mixin(LivingEntity.class)
public class LivingEntityMixins {

    @Inject(at = @At("TAIL"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    public void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        LivingEntity target = (LivingEntity) (Object) this;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }

        UUID clientId = MinecraftClient.getInstance().player.getUuid();
        UUID playerEventId = null;

        if (target instanceof ServerPlayerEntity && target.getUuid().equals(clientId)) {
            // Self damage
            MinetifaceController.getInstance().onHurt(amount);
        } else if (source.getName().equals("player") && source.getSource() instanceof ServerPlayerEntity playerSource) {
            playerEventId = playerSource.getUuid();
        } else if (source.getName().equals("arrow") && source.getAttacker() instanceof ServerPlayerEntity playerAttacker) {
            playerEventId = playerAttacker.getUuid();
        }

        // On attack
        if (clientId.equals(playerEventId)) {
            MinetifaceController.getInstance().onDamage(amount);
        }
    }
}