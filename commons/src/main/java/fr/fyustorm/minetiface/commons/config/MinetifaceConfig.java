package fr.fyustorm.minetiface.commons.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class MinetifaceConfig {

    public static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static final String CONFIG_NAME = "minetiface.config";

    private static final ObjectMapper MAPPER = new ObjectMapper();
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
    public float defaultBlockScore = 1;

    // Attack
    public boolean attackEnabled = true;
    public float attackMultiplier = 1;
    public float attackInstantPointsMultiplier = 1;
    public float attackDurationMultiplier = 1;

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        defaultBlocks = new HashMap<>();
        defaultBlocks.put("block.minecraft.coal_ore", new BlockScoreConfig("block.minecraft.coal_ore", "Coal Ore", 4f));
        defaultBlocks.put("block.minecraft.iron_ore", new BlockScoreConfig("block.minecraft.iron_ore", "Iron Ore", 5f));
        defaultBlocks.put("block.minecraft.deepslate_iron_ore", new BlockScoreConfig("block.minecraft.deepslate_iron_ore", "Deepslate Iron Ore", 6f));
        defaultBlocks.put("block.minecraft.copper_ore", new BlockScoreConfig("block.minecraft.copper_ore", "Copper Ore", 4f));
        defaultBlocks.put("block.minecraft.deepslate_copper_ore", new BlockScoreConfig("block.minecraft.deepslate_copper_ore", "Deepslate Copper Ore", 10f));
        defaultBlocks.put("block.minecraft.redstone_ore", new BlockScoreConfig("block.minecraft.redstone_ore", "Redstone Ore", 5f));
        defaultBlocks.put("block.minecraft.deepslate_redstone_ore", new BlockScoreConfig("block.minecraft.deepslate_redstone_ore", "Deepslate Redstone Ore", 6f));
        defaultBlocks.put("block.minecraft.lapis_ore", new BlockScoreConfig("block.minecraft.lapis_ore", "Lapis Ore", 7f));
        defaultBlocks.put("block.minecraft.deepslate_lapis_ore", new BlockScoreConfig("block.minecraft.deepslate_lapis_ore", "Deepslate Lapis Ore", 8f));
        defaultBlocks.put("block.minecraft.gold_ore", new BlockScoreConfig("block.minecraft.gold_ore", "Gold Ore", 9f));
        defaultBlocks.put("block.minecraft.deepslate_gold_ore", new BlockScoreConfig("block.minecraft.deepslate_gold_ore", "Deepslate Gold Ore", 10f));
        defaultBlocks.put("block.minecraft.emerald_ore", new BlockScoreConfig("block.minecraft.emerald_ore", "Emerald Ore", 10f));
        defaultBlocks.put("block.minecraft.deepslate_emerald_ore", new BlockScoreConfig("block.minecraft.deepslate_emerald_ore", "Deepslate Emerald Ore", 12f));
        defaultBlocks.put("block.minecraft.diamond_ore", new BlockScoreConfig("block.minecraft.diamond_ore", "Diamond Ore", 10f));
        defaultBlocks.put("block.minecraft.deepslate_diamond_ore", new BlockScoreConfig("block.minecraft.deepslate_diamond_ore", "Deepslate Diamond Ore", 12f));
    }

    public MinetifaceConfig() {
        blocksScore = new HashMap<>(defaultBlocks);
    }

    public static void loadConfig(Path configFolder) {
        Path configPath = configFolder.resolve(CONFIG_NAME);
        File config = configPath.toFile();
        if (!config.exists()) {
            LOGGER.warn("Config file missing, using defaults");
            INSTANCE = new MinetifaceConfig();
            saveConfig(config);
        } else {
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
                LOGGER.error("Error while loading minetiface config file", e);
            }
        }

        executor.submit(() -> {
            watchConfig(configFolder);
        });
    }

    private static void reloadConfig(File configFile) {
        try (FileInputStream in = new FileInputStream(configFile)) {
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
            LOGGER.info("Config file reloaded");
        } catch (IOException e) {
            LOGGER.error("Error while loading minetiface config file", e);
        }
    }

    private static void saveConfig(File config) {
        try (FileOutputStream out = new FileOutputStream(config)) {
            MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
            MAPPER.writeValue(out, INSTANCE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * We are watching config file for any extern modification
     */
    private static void watchConfig(Path configFolder) {

        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            configFolder.register(watcher, ENTRY_MODIFY);
            WatchKey key;
            for (; ; ) {

                try {
                    key = watcher.take();
                } catch (InterruptedException x) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    // This key is registered only
                    // for ENTRY_CREATE events,
                    // but an OVERFLOW event can
                    // occur regardless if events
                    // are lost or discarded.
                    if (kind != ENTRY_MODIFY) {
                        continue;
                    }

                    // The filename is the
                    // context of the event.
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    if (!ev.context().endsWith(CONFIG_NAME)) {
                        continue;
                    }
                    LOGGER.info("Config file changed");
                    Path filename = ev.context();
                    Path configFile = configFolder.resolve(filename);
                    reloadConfig(configFile.toFile());
                }

                // Reset the key -- this step is critical if you want to
                // receive further watch events.  If the key is no longer valid,
                // the directory is inaccessible so exit the loop.
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while watching config file", e);
        }
    }
}
