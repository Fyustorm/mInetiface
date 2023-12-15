package fr.fyustorm.minetiface.mixin;

import fr.fyustorm.minetiface.commons.intiface.MinetifaceController;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

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
        LOGGER.info("Attack after");
        double value = 1;
        if (target instanceof LivingEntity livingEntity) {
            ItemStack item = player.getMainHandStack();


            for (Map.Entry<EntityAttribute, Collection<EntityAttributeModifier>> keyPair : item.getAttributeModifiers(EquipmentSlot.MAINHAND).asMap().entrySet()) {
                EntityAttribute attribute = keyPair.getKey();
                Collection<EntityAttributeModifier> modifiers = keyPair.getValue();

                if (!"attribute.name.generic.attack_damage".equals(attribute.getTranslationKey())) {
                    continue;
                }

                LOGGER.debug("Attribute = {}", attribute.getTranslationKey());
                for (EntityAttributeModifier modifier : modifiers) {
                    value = modifier.getValue();
//                    LOGGER.debug("-- Modifier {} Value {}", modifier.getName(), modifier.getValue());
                    break;

                }
            }
            if (livingEntity.getHealth() < value) {
                value *= 1.5;
            }

            MinetifaceController.getInstance().onDamage((float) value);
        }
    }
}
