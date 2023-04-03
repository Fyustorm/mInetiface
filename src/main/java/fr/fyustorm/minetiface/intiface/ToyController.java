package fr.fyustorm.minetiface.intiface;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.fyustorm.minetiface.config.MinetifaceConfig;
import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice;
import io.github.blackspherefollower.buttplug4j.client.ButtplugClientWSClient;
import io.github.blackspherefollower.buttplug4j.client.ButtplugDeviceException;

public class ToyController {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final ButtplugClientWSClient client;
	public static double currentVibrationLevel = 0;
	private static List<ButtplugClientDevice> devices;
	private static boolean connected = false;

	private static ExecutorService executor = Executors.newSingleThreadExecutor();

	static {
		devices = new ArrayList<>();
		client = new ButtplugClientWSClient("mInetiface");
	}

	public static boolean isConnected() {
		return connected;
	}

	public static void connectServer() throws URISyntaxException, Exception {
		LOGGER.info("URL: " + MinetifaceConfig.INSTANCE.serverUrl);
		client.connect(new URI("ws://" + MinetifaceConfig.INSTANCE.serverUrl + "/buttplug"));
		connected = true;
	}

	public static void disconnectServer() {
		setVibrationLevel(0);
		client.disconnect();
		connected = false;
	}

	public static Future<List<ButtplugClientDevice>> scanDevices() throws Exception {
		client.startScanning();

		return executor.submit(() -> {
			try {
				Thread.sleep(5000);
				client.requestDeviceList();
			} catch (Exception e) {
				LOGGER.error("Could not get devices list", e);
			}

			devices = client.getDevices();
			client.stopScanning();

			return devices;
		});
	}

	public static void setVibrationLevel(double level) {
		try {
			for (ButtplugClientDevice device : devices) {
				device.sendScalarVibrateCmd(level);
			}
		} catch (ButtplugDeviceException e) {
			LOGGER.error("Could not send scalar command", e);
		}

		currentVibrationLevel = level;
	}
}
