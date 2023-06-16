package fr.fyustorm.minetiface.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class MinetifaceConfig {
	private static final ObjectMapper MAPPER = new ObjectMapper();;
	private static final Logger LOGGER = LogManager.getLogger(MinetifaceConfig.class);
	public static MinetifaceConfig INSTANCE;
	public String serverUrl = "localhost:12345";

	public float minimumFeedback = 10;
	public float maximumFeedback = 30;
	public float maximumScore = 80;
	public float feedbackScoreLostPerTick = 2f;
	public float scoreLostPerTick = 0.05f;
	public int maximumSecondsKeepScore = 30;

	// Intiface (linear)
	/**
	 * Maximum time to go from position 0 to 1 and vice-versa (in ms)
	 */
	public long fullMaxTime = 5000;

	/**
	 * Minimum time to go from position 0 to 1 and vice-versa (in ms)
	 */
	public long fullMinTime = 100;

	/**
	 * Minimum time betwen two commands (in ms)
	 */
	public long minTimeBetweenCmd = 25;

	// GUI
	public boolean showIntensity = true;
	public int intensityColor = 0xFFAA00AA;


	// Masochist
	public boolean masochistEnabled = true;
	public float masochistMultiplier = 1;
	public float masochistInstantPointsMultiplier = 1;
	public float masochistDurationMultiplier = 1;

	// Xp
	public boolean xpEnabled = true;
	public float xpMultiplier = 1;
	public float xpInstantPointsMultiplier = 1;
	public float xpDurationMultiplier = 1;

	// Mining
	public boolean miningEnabled = true;
	public float minePointsMultiplier = 1;
	public float mineInstantPointsMultiplier = 1;
	public float mineDurationMultiplier = 1;
	public Map<String, BlockScoreConfig> blocksScore;
	public static Map<String, BlockScoreConfig> defaultBlocks;
	public float defaultBlockScore = 0;

	// Attack
	public boolean attackEnabled = true;
	public float attackMultiplier = 1;
	public float attackInstantPointsMultiplier = 1;
	public float attackDurationMultiplier = 1;

	static {
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		defaultBlocks = new HashMap<>();
		addBlockConfig(Blocks.COAL_ORE, 5f);
		addBlockConfig(Blocks.IRON_ORE, 5f);
		addBlockConfig(Blocks.COPPER_ORE, 5f);
		addBlockConfig(Blocks.REDSTONE_ORE, 10f);
		addBlockConfig(Blocks.LAPIS_ORE, 20f);
		addBlockConfig(Blocks.GOLD_ORE, 30f);
		addBlockConfig(Blocks.EMERALD_ORE, 40f);
		addBlockConfig(Blocks.DIAMOND_ORE, 40f);
		addBlockConfig(Blocks.DEEPSLATE_DIAMOND_ORE, 50f);
	}

	private static void addBlockConfig(Block block, float score) {
		defaultBlocks.put(block.getTranslationKey(),
				new BlockScoreConfig(block.getTranslationKey(), block.getName().getString(), score));
	}

	public MinetifaceConfig() {
		blocksScore = new HashMap<>(defaultBlocks);
	}

	private static File getConfigFile() {
		return FabricLoader.getInstance().getConfigDir().resolve("minetiface-fabric.json").toFile();
	}

	public static void loadConfig() {
		File config = getConfigFile();
		if (!config.exists()) {
			LOGGER.warn("Config file missing, using defaults");
			INSTANCE = new MinetifaceConfig();
			return;
		}

		try (FileInputStream in = new FileInputStream(config)) {
			INSTANCE = MAPPER.readValue(in, MinetifaceConfig.class);

			if (INSTANCE.blocksScore == null) {
				INSTANCE.blocksScore = new HashMap<>(defaultBlocks);
			} else {
				for (Map.Entry<String, BlockScoreConfig> entry : defaultBlocks.entrySet()) {
					if (!INSTANCE.blocksScore.containsKey(entry.getKey())) {
						INSTANCE.blocksScore.put(entry.getKey(), entry.getValue());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveConfig() {
		File config = getConfigFile();
		try (FileOutputStream out = new FileOutputStream(config)) {
			MAPPER.writeValue(out, INSTANCE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
