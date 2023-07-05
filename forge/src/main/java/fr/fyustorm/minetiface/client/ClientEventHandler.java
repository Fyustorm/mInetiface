package fr.fyustorm.minetiface.client;

import fr.fyustorm.minetiface.MinetifaceMod;
import fr.fyustorm.minetiface.commons.intiface.MinetifaceController;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;


@Mod.EventBusSubscriber(modid = MinetifaceMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(ClientEventHandler.class);

    private static int playerTickCount = 0;
    private static int clientTickCount = 0;

    @SubscribeEvent
    public static void test(PlayerEvent.BreakSpeed event) {
        //LOGGER.info("Break speed");
    }

    @SubscribeEvent
    public static void test2(PlayerInteractEvent.LeftClickBlock event) {
        //LOGGER.info("Left click");
        //event.getUseBlock()
    }

    @SubscribeEvent
    public static void onHarvest(PlayerEvent.HarvestCheck event) {
        if (!event.canHarvest()) {
            // TODO Masochist action ?
            return;
        }

        ItemStack item = event.getEntity().getMainHandItem();
        BlockState block = event.getTargetBlock();
        float speed = item.getDestroySpeed(block);

        MinetifaceController.getInstance().onHarvest(speed, block.getBlock().getDescriptionId());
    }

    @SubscribeEvent
    public static void onAttack(CriticalHitEvent event) {
        LOGGER.info("Attack {} and {}", event.getDamageModifier(), event.getOldDamageModifier());
        LOGGER.info("Result" + event.getResult());

        Player player = event.getEntity();
        ItemStack item = player.getMainHandItem();

        AttributeModifier attackModifier;
        try {
            attackModifier = item.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE).stream().filter(attributeModifier -> attributeModifier.getName().startsWith("Weapon")).findFirst().orElseThrow();
        } catch (NoSuchElementException e) {
            return;
        }

        MinetifaceController.getInstance().onDamage((float) (event.getDamageModifier() * attackModifier.getAmount()));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Forge is 80 ticks per second and fabric 20 ticks per second
        // So let's send only 20 ticks per second to have the same tickrate
        playerTickCount++;
        if (playerTickCount == 4) {
            MinetifaceController.getInstance().onPlayerTick();
            playerTickCount = 0;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Forge is 80 ticks per second and fabric 20 ticks per second
        // So let's send only 20 ticks per second to have the same tickrate
        clientTickCount++;
        if (clientTickCount == 4) {
            MinetifaceController.getInstance().onClientTick();
            clientTickCount = 0;
        }
    }

//    @SubscribeEvent
//    public static void onHurt(LivingHurtEvent event) {
//        LOGGER.info("Hurt event");
//        Entity entity = event.getEntity();
//        if (entity instanceof Player player) {
//            UUID entityId = player.getGameProfile().getId();
//            if (Minecraft.getInstance().player == null) {
//                return;
//            }
//            UUID playerId = Minecraft.getInstance().player.getGameProfile().getId();
//
//            if (!entityId.equals(playerId)) {
//                return;
//            }
//            MinetifaceController.getInstance().onHurt(event.getAmount());
//        }
//    }

//    @SubscribeEvent
//    public static void onDeath(LivingDeathEvent event) {
//        LOGGER.info("Death");
//        Entity entity = event.getEntity();
//        LOGGER.info("E {}", entity);
//        LOGGER.info("P2 {}", Minecraft.getInstance().player);
//        if (entity instanceof Player player) {
//            LOGGER.info("P {}", player.getGameProfile().getId());
//
//            UUID entityId = player.getGameProfile().getId();
//            if (Minecraft.getInstance().player == null) {
//                return;
//            }
//            UUID playerId = Minecraft.getInstance().player.getGameProfile().getId();
//
//            if (!entityId.equals(playerId)) {
//                return;
//            }
//
//
//        }
//        MinetifaceController.getInstance().onDeath();
//    }

//    @SubscribeEvent
//    public static void onBreak(BlockEvent.BreakEvent event) {
//        LOGGER.info("Break");
//        UUID eventPlayer = event.getPlayer().getGameProfile().getId();
//        if (Minecraft.getInstance().player == null) {
//            return;
//        }
//        UUID playerId = Minecraft.getInstance().player.getUUID();
//
//        if (!eventPlayer.equals(playerId)) {
//            return;
//        }
//
//        String blockId = event.getState().getBlock().getDescriptionId();
//        MinetifaceController.getInstance().onBreak(blockId);
//    }

//    @SubscribeEvent
//    public static void onXpChange(PlayerXpEvent.XpChange event) {
//        LOGGER.info("XP");
//        if (Minecraft.getInstance().player == null) {
//            return;
//        }
//
//        Player eventPlayer = event.getEntity();
//        UUID eventPlayerId = event.getEntity().getGameProfile().getId();
//        UUID playerId = Minecraft.getInstance().player.getUUID();
//
//        if (!eventPlayerId.equals(playerId)) {
//            return;
//        }
//
//        MinetifaceController.getInstance().onXpChange(eventPlayer.experienceProgress, eventPlayer.totalExperience, event.getAmount());
//    }
}

