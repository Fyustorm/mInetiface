package fr.fyustorm.minetiface.commons.intiface;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.fyustorm.minetiface.commons.config.MinetifaceConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice;
import io.github.blackspherefollower.buttplug4j.client.ButtplugClientWSClient;
import io.github.blackspherefollower.buttplug4j.client.ButtplugDeviceException;

public class ToyController {
	private static final ToyController instance = new ToyController();
	private static final Logger LOGGER = LogManager.getLogger();

	private final ButtplugClientWSClient client;
	private List<ButtplugClientDevice> devices;
	private boolean connected = false;


	public static ToyController instance() {
		return instance;
	}

	private ExecutorService executor = Executors.newSingleThreadExecutor();

	private ToyController() {
		devices = new ArrayList<>();
		client = new ButtplugClientWSClient("mInetiface");
	}

	public boolean isConnected() {
		return connected;
	}

	public void connectServer() throws URISyntaxException, Exception {
		LOGGER.info("URL: " + MinetifaceConfig.INSTANCE.serverUrl);
		client.connect(new URI("ws://" + MinetifaceConfig.INSTANCE.serverUrl + "/buttplug"));
		connected = true;
	}

	public void disconnectServer() {
		setScalarLevel(0);
		client.disconnect();
		connected = false;
	}

	public Future<List<ButtplugClientDevice>> scanDevices() throws Exception {
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

	/**
	 * Get the list of devices to be displayed
	 */
	public String getDevicesString() {
		StringBuilder devicesStr = new StringBuilder();
		int count = 1;
		for (ButtplugClientDevice buttplugClientDevice : devices) {
			devicesStr.append(buttplugClientDevice.getName());
			if (count++ < devices.size()) {
				devicesStr.append(", ");
			}
		}
		return devicesStr.toString();
	}

	public void setScalarLevel(double level) {
		try {
			for (ButtplugClientDevice device : devices) {
				if (device.getScalarVibrateCount() > 0) {
					device.sendScalarVibrateCmd(level);
				}
				if (device.getScalarRotateCount() > 0) {
					device.sendScalarRotateCmd(level);
				}
				if (device.getScalarOscillateCount() > 0) {
					device.sendScalarOscillateCmd(level);
				}
			}
		} catch (ButtplugDeviceException e) {
			LOGGER.error("Could not send scalar command", e);
		}
	}

	private double currentPosition = 1;
	private long lastLinearCommandTimestamp = 0;
	private long lastLinearCommandDuration = 0;
	private double lastLinearPosition = 1;

	// public void setLinearLevel(double level) {

	// 	long currentTime = new Date().getTime();
	// 	long timeSinceLastCmd = currentTime - lastLinearCommandTimestamp;

	// 	if (timeSinceLastCmd < MinetifaceConfig.INSTANCE.minTimeBetweenCmd) {
	// 		return;
	// 	}

	// 	long duration;
	// 	if (timeSinceLastCmd >= lastLinearCommandDuration) {
	// 		// Last command is completed so we can invert the position
	// 		lastLinearPosition = currentPosition;
	// 		currentPosition = currentPosition == 0 ? 1 : 0;

	// 		// Calculating time to move depending on the level (0 min intensity, 1 max intensity)
	// 		duration = (long) (MinetifaceConfig.INSTANCE.fullMaxTime - (MinetifaceConfig.INSTANCE.fullMaxTime - MinetifaceConfig.INSTANCE.fullMinTime) * level);
	// 	} else {
	// 		// Last command is not completed, so we are still aiming the same position
	// 		// but we are updating the duration (speed)

	// 		// We are trying to compute the real position
	// 		double percentDone = (double) timeSinceLastCmd / (double) lastLinearCommandDuration;
	// 		double newPosition = currentPosition == 1 ? lastLinearPosition + percentDone : lastLinearPosition - percentDone;
	// 		double percentRemaining = currentPosition == 1 ? 1 - newPosition : newPosition;

	// 		duration = (long) ((MinetifaceConfig.INSTANCE.fullMaxTime - (MinetifaceConfig.INSTANCE.fullMaxTime - MinetifaceConfig.INSTANCE.fullMinTime) * level) * percentRemaining);
	// 		lastLinearPosition = newPosition;
	// 	}

	// 	lastLinearCommandTimestamp = currentTime;
	// 	lastLinearCommandDuration = duration;

	// 	try {
	// 		for (ButtplugClientDevice device : devices) {
	// 			if (device.getLinearCount() > 0) {
	// 				device.sendLinearCmd(currentPosition, duration);
	// 			}
	// 		}
	// 	} catch (ButtplugDeviceException e) {
	// 		LOGGER.error("Could not send linear command", e);
	// 	}
	// }

	public void setLinearLevel(double level) {
		if (level <= 0) {
			return;
		}

		long currentTime = new Date().getTime();
		long timeSinceLastCmd = currentTime - lastLinearCommandTimestamp;

		if (timeSinceLastCmd < lastLinearCommandDuration) {
			return;
		}

		long duration;

		// Last command is completed so we can invert the position
		currentPosition = currentPosition == 0 ? 1 : 0;

		// Calculating time to move depending on the level (0 min intensity, 1 max intensity)
		duration = (long) (MinetifaceConfig.INSTANCE.fullMaxTime - (MinetifaceConfig.INSTANCE.fullMaxTime - MinetifaceConfig.INSTANCE.fullMinTime) * level);

		lastLinearCommandTimestamp = currentTime;
		lastLinearCommandDuration = duration;

		try {
			for (ButtplugClientDevice device : devices) {
				if (device.getLinearCount() > 0) {
					device.sendLinearCmd(currentPosition, duration);
				}
			}
		} catch (ButtplugDeviceException e) {
			LOGGER.error("Could not send linear command", e);
		}
	}

	// public double getLinearPosition() {
	// 	long currentTime = new Date().getTime();
	// 	long timeSinceLastCmd = currentTime - lastLinearCommandTimestamp;

	// 	double percentDone;
	// 	if (timeSinceLastCmd > lastLinearCommandDuration) {
	// 		percentDone = 1;
	// 	} else {
	// 		percentDone = (double) timeSinceLastCmd / (double) lastLinearCommandDuration;
	// 	}

	// 	percentDone = currentPosition == 1 ? (1 - lastLinearPosition) * percentDone : lastLinearPosition * percentDone;

	// 	return currentPosition == 1 ? lastLinearPosition + percentDone : lastLinearPosition - percentDone;
	// }

	public double getLinearPosition() {
		long currentTime = new Date().getTime();
		long timeSinceLastCmd = currentTime - lastLinearCommandTimestamp;

		double percentDone;
		if (timeSinceLastCmd > lastLinearCommandDuration) {
			percentDone = 1;
		} else {
			percentDone = (double) timeSinceLastCmd / (double) lastLinearCommandDuration;
		}

		return currentPosition == 1 ? percentDone : 1 - percentDone;
	}
}
