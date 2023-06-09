package fr.fyustorm.minetiface;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.fyustorm.minetiface.commons.config.MinetifaceConfig;
import fr.fyustorm.minetiface.commons.intiface.ToyController;
import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.TranslatableText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinetifaceMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(MinetifaceMod.class);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing minetiface");

        MinetifaceConfig.loadConfig(FabricLoader.getInstance().getConfigDir());

        ClientCommandManager.DISPATCHER.register(ClientCommandManager
                .literal("minetiface-connect")
                .executes(o -> {
                    commandConnect(o);
                    return Command.SINGLE_SUCCESS;
                }));


		ClientCommandManager.DISPATCHER.register(ClientCommandManager
				.literal("minetiface-disconnect")
				.executes(o -> {
					commandDisconnect(o);
					return Command.SINGLE_SUCCESS;
				}));
    }

    private void commandConnect(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        LOGGER.info("Connect command");
        try {
            if (ToyController.instance().isConnected()) {
                commandScan(context);
                return;
            }

            ToyController.instance().connectServer();
            context.getSource().sendFeedback(new TranslatableText("commands.connect.success"));
            commandScan(context);
        } catch (URISyntaxException e) {
            throw new SimpleCommandExceptionType(
					new TranslatableText("commands.connect.invalid_address", MinetifaceConfig.INSTANCE.serverUrl))
                    .create();
        } catch (Exception e) {
            throw new SimpleCommandExceptionType(new TranslatableText("commands.connect.error")).create();
        }
    }

    private void commandDisconnect(CommandContext<FabricClientCommandSource> context) {
        LOGGER.info("Disconnect command");
        ToyController.instance().disconnectServer();
		context.getSource().sendFeedback(new TranslatableText("commands.disconnected"));
    }

    private void commandScan(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        executor.submit(() -> {
            LOGGER.info("Scanning devices");
            context.getSource().sendFeedback(new TranslatableText("commands.connect.scan"));
            List<ButtplugClientDevice> devices;
            try {
                devices = ToyController.instance().scanDevices().get();

                if (devices.isEmpty()) {
                    context.getSource().sendFeedback(new TranslatableText("commands.connect.no_device"));
                    return;
                }

                String devicesStr = ToyController.instance().getDevicesString();

                context.getSource()
                        .sendFeedback(new TranslatableText("commands.connect.devices", devices.size(), devicesStr));

                context.getSource()
                        .sendFeedback(new TranslatableText("commands.connect.add_more"));
            } catch (Exception e) {
                LOGGER.error("Error while scanning devices", e);
                context.getSource().sendFeedback(new TranslatableText("commands.connect.scan_failed", e.getMessage()));
            }
        });
    }
}

