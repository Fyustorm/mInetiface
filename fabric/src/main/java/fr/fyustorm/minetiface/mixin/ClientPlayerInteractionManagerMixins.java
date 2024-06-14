package fr.fyustorm.minetiface.mixin;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.fyustorm.minetiface.commons.intiface.MinetifaceController;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixins {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientPlayerInteractionManagerMixins.class);

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "breakBlock(Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"))
    private void onBreak(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
        World world = client.world;
        ClientPlayerEntity player = client.player;
        if (world == null || player == null) {
            return;
        }

        BlockState state = world.getBlockState(pos);
        MinetifaceController.getInstance().onBreak(state.getBlock().getTranslationKey());
    }

    @Inject(method = "attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
    public void attackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        LOGGER.info("Attack before");
        if (target instanceof LivingEntity livingEntity) {
            LOGGER.info("Before " + livingEntity.getHealth());
        }
    }

    @Inject(method = "attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
    public void afterAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        double value = 1;
        if (target instanceof LivingEntity livingEntity) {
            ItemStack item = player.getMainHandStack();

            AttributeModifiersComponent attributes = item.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
            if (attributes != null) {
                // We are searching the item damage attribute
                Identifier damagIdentifier = new Identifier("minecraft:generic.attack_damage");
                for (AttributeModifiersComponent.Entry entry : attributes.modifiers()) {
                    RegistryKey<EntityAttribute> registryKey = entry.attribute().getKey().orElse(null);
                    Identifier identifier = registryKey.getValue();

                    if (damagIdentifier.equals(identifier)) {
                        value = entry.modifier().value();
                        break;
                    }
                }
            }

            // If we killed the entity, increase the value
            if (livingEntity.getHealth() < value) {
                value *= 1.5;
            }

            MinetifaceController.getInstance().onDamage((float) value);
        }
    }
}
