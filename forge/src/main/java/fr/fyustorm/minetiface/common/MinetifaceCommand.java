package fr.fyustorm.minetiface.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.fyustorm.minetiface.MinetifaceMod;
import fr.fyustorm.minetiface.commons.config.MinetifaceConfig;
import fr.fyustorm.minetiface.commons.intiface.ToyController;
import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MinetifaceCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinetifaceMod.class);

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        LiteralArgumentBuilder<CommandSourceStack> connectCommand = Commands.literal("minetiface-connect")
                .executes(o -> {
                    commandConnect(o);
                    return Command.SINGLE_SUCCESS;
                });

        LiteralArgumentBuilder<CommandSourceStack> disconnectCommand = Commands.literal("minetiface-disconnect")
                .executes(o -> {
                    commandDisconnect(o);
                    return Command.SINGLE_SUCCESS;
                });

        LiteralArgumentBuilder<CommandSourceStack> scanCommand = Commands.literal("minetiface-scan")
                .executes(o -> {
                    commandDisconnect(o);
                    return Command.SINGLE_SUCCESS;
                });

        dispatcher.register(connectCommand);
        dispatcher.register(disconnectCommand);
        dispatcher.register(scanCommand);

        LOGGER.info("Minetiface commands registered");
    }

    private static void commandConnect(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        LOGGER.info("Connect command");
        try {
            if (ToyController.instance().isConnected()) {
                commandScan(context);
                return;
            }

            ToyController.instance().connectServer();
            sendMessage(context, "commands.connect.success");
            commandScan(context);
        } catch (URISyntaxException e) {
            throw new SimpleCommandExceptionType(new LiteralMessage(I18n.get("commands.connect.invalid_address", MinetifaceConfig.INSTANCE.serverUrl)))
                    .create();
        } catch (Exception e) {
            throw new SimpleCommandExceptionType(new LiteralMessage(I18n.get("commands.connect.error"))).create();
        }
    }

    private static void commandDisconnect(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        LOGGER.info("Disconnect command");
        ToyController.instance().disconnectServer();
        sendMessage(context, "commands.disconnected");
    }


    private static void commandScan(CommandContext<CommandSourceStack> context) {
        executor.submit(() -> {
            LOGGER.info("Scanning devices");

            List<ButtplugClientDevice> devices;
            try {
                sendMessage(context, "commands.connect.scan");
                devices = ToyController.instance().scanDevices().get();

                if (devices.isEmpty()) {
                    sendMessage(context, "commands.connect.no_device");
                    return;
                }

                String devicesStr = ToyController.instance().getDevicesString();

                TranslatableContents content = new TranslatableContents("commands.connect.devices", devices.size(), devicesStr);
                sendMessage(context, content);

                sendMessage(context, "commands.connect.add_more");
            } catch (Exception e) {
                LOGGER.error("Error while scanning devices", e);
                try {
                    TranslatableContents content = new TranslatableContents("commands.connect.scan_failed", e.getMessage());
                    sendMessage(context, content);
                } catch (CommandSyntaxException ex) {
                    LOGGER.error("Error while sending feedback error", e);
                }
            }
        });
    }

    private static void sendMessage(CommandContext<CommandSourceStack> commandContext, TranslatableContents content) throws CommandSyntaxException {
        Entity entity = commandContext.getSource().getEntity();
        if (entity == null) {
            return;
        }

        MutableComponent component = content.resolve(commandContext.getSource(), entity, 1);
        commandContext.getSource().getPlayerOrException().sendSystemMessage(component);
    }

    private static void sendMessage(CommandContext<CommandSourceStack> commandContext, String translationKey) throws CommandSyntaxException {
        TranslatableContents content = new TranslatableContents(translationKey);
        sendMessage(commandContext, content);
    }
}
