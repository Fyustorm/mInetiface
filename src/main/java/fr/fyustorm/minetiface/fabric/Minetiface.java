package fr.fyustorm.minetiface.fabric;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import fr.fyustorm.minetiface.config.MinetifaceConfig;
import fr.fyustorm.minetiface.intiface.ToyController;
import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class Minetiface implements ModInitializer {

	public static Logger LOGGER = LogManager.getLogger(Minetiface.class);

	public static final String MOD_ID = "minetiface";
	public static final String MOD_NAME = "mInetiface";
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing");
		MinetifaceConfig.loadConfig();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("minetiface-connect").executes(o -> {
				commandConnect(o);
				return Command.SINGLE_SUCCESS;
			}));
		});

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("minetiface-disconnect").executes(o -> {
				commandDisconnect(o);
				return Command.SINGLE_SUCCESS;
			}));
		});
	}

	private void commandConnect(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
		try {
			if (ToyController.isConnected()) {
				commandScan(context);
				return;
			}

			ToyController.connectServer();
			context.getSource().sendFeedback(Text.translatable("commands.connect.success"));
			commandScan(context);
		} catch (URISyntaxException e) {
			throw new SimpleCommandExceptionType(
					Text.translatable("commands.connect.invalid_address", MinetifaceConfig.INSTANCE.serverUrl))
					.create();
		} catch (Exception e) {
			throw new SimpleCommandExceptionType(Text.translatable("commands.connect.error")).create();
		}
	}

	private void commandDisconnect(CommandContext<FabricClientCommandSource> context) {
		ToyController.disconnectServer();
	}

	private void commandScan(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
		executor.submit(() -> {
			context.getSource().sendFeedback(Text.translatable("commands.connect.scan"));
			List<ButtplugClientDevice> devices;
			try {
				devices = ToyController.scanDevices().get();

				if (devices.isEmpty()) {
					context.getSource().sendFeedback(Text.translatable("commands.connect.no_device"));
					return;
				}

				String devicesStr = "";
				int count = 1;
				for (ButtplugClientDevice buttplugClientDevice : devices) {
					devicesStr += buttplugClientDevice.getName();
					if (count++ < devices.size()) {
						devicesStr += ", ";
					}
				}

				context.getSource()
						.sendFeedback(Text.translatable("commands.connect.devices", devices.size(), devicesStr));

				context.getSource()
						.sendFeedback(Text.translatable("commands.connect.add_more"));
			} catch (Exception e) {
				LOGGER.error("Error while scanning devices", e);
				context.getSource().sendFeedback(Text.translatable("commands.connect.scan_failed", e.getMessage()));
			}
		});
	}
}
