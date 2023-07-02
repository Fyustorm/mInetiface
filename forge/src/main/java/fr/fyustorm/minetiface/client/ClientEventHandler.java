package fr.fyustorm.minetiface.client;

import fr.fyustorm.minetiface.MinetifaceMod;
import fr.fyustorm.minetiface.commons.intiface.MinetifaceController;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


@Mod.EventBusSubscriber(modid = MinetifaceMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(ClientEventHandler.class);

    private static int playerTickCount = 0;
    private static int clientTickCount = 0;

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

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        Entity source = event.getSource().getEntity();

        if (source instanceof Player playerSource) {
            UUID entityId = playerSource.getGameProfile().getId();
            if (Minecraft.getInstance().player == null) {
                return;
            }
            UUID playerId = Minecraft.getInstance().player.getGameProfile().getId();

            if (!entityId.equals(playerId)) {
                return;
            }

            // If we are self attacking (arrow for example)
            if (source.equals(event.getEntity())) {
                return;
            }

            MinetifaceController.getInstance().onDamage(event.getAmount());
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            UUID entityId = player.getGameProfile().getId();
            if (Minecraft.getInstance().player == null) {
                return;
            }
            UUID playerId = Minecraft.getInstance().player.getGameProfile().getId();

            if (!entityId.equals(playerId)) {
                return;
            }
            MinetifaceController.getInstance().onHurt(event.getAmount());
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            UUID entityId = player.getGameProfile().getId();
            if (Minecraft.getInstance().player == null) {
                return;
            }
            UUID playerId = Minecraft.getInstance().player.getGameProfile().getId();

            if (!entityId.equals(playerId)) {
                return;
            }

            MinetifaceController.getInstance().onDeath();
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        UUID eventPlayer = event.getPlayer().getGameProfile().getId();
        if (Minecraft.getInstance().player == null) {
            return;
        }
        UUID playerId = Minecraft.getInstance().player.getUUID();

        if (!eventPlayer.equals(playerId)) {
            return;
        }

        String blockId = event.getState().getBlock().getDescriptionId();
        MinetifaceController.getInstance().onBreak(blockId);
    }

    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        if (Minecraft.getInstance().player == null) {
            return;
        }

        Player eventPlayer = event.getEntity();
        UUID eventPlayerId = event.getEntity().getGameProfile().getId();
        UUID playerId = Minecraft.getInstance().player.getUUID();

        if (!eventPlayerId.equals(playerId)) {
            return;
        }

        MinetifaceController.getInstance().onXpChange(eventPlayer.experienceProgress, eventPlayer.totalExperience, event.getAmount());
    }
}

