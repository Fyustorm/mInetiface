package fr.fyustorm.minetiface.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.fyustorm.minetiface.intiface.MinetifaceController;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

@Mixin(LivingEntity.class)
public class LivingEntityMixins {

    @Inject(at = @At("TAIL"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    public void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        LivingEntity target = (LivingEntity) (Object) this;
        MinetifaceController.getInstance().onDamage(source, target, amount);
    }
}
