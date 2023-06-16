package fr.fyustorm.minetiface.config;

import java.util.Map;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MinetifaceModMenu implements ModMenuApi {

	private ConfigBuilder builder;
	private ConfigEntryBuilder entryBuild;

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return this::makeConfig;
	}

	private Screen makeConfig(Screen parent) {
		builder = ConfigBuilder.create();
		entryBuild = builder.entryBuilder();

		MinetifaceConfig config = MinetifaceConfig.INSTANCE;
		builder
				.setParentScreen(parent)
				.setTitle(Text.literal("mInetiface"))
				.setSavingRunnable(MinetifaceConfig::saveConfig);

		// General
		builder.getOrCreateCategory(Text.literal("General"))
				.addEntry(entryBuild.startStrField(Text.literal("Intiface server address"), config.serverUrl)
						.setDefaultValue("localhost:12345")
						.setSaveConsumer(s -> config.serverUrl = s)
						.build())
				.addEntry(entryBuild
						.startBooleanToggle(Text.literal("GUI: Show intensity ?"), config.showIntensity)
						.setDefaultValue(true)
						.setSaveConsumer(i -> config.showIntensity = i)
						.build())
				.addEntry(entryBuild
						.startAlphaColorField(Text.literal("GUI: Text color"), config.intensityColor)
						.setDefaultValue(0xFFAA00AA)
						.setSaveConsumer(i -> config.intensityColor = i)
						.build())
				.addEntry(entryBuild
						.startLongField(Text.literal("Minimum time between two device commands"),
								config.minTimeBetweenCmd)
						.setDefaultValue(25)
						.setMin(10)
						.setMax(1000)
						.setSaveConsumer(i -> config.minTimeBetweenCmd = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("(Scalar) Minimum intensity feedback"),
								config.minimumFeedback)
						.setDefaultValue(10)
						.setMin(0)
						.setMax(50)
						.setSaveConsumer(i -> config.minimumFeedback = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("(Scalar) Maximum intensity feedback"),
								config.maximumFeedback)
						.setDefaultValue(30)
						.setMin(0)
						.setMax(50)
						.setSaveConsumer(i -> config.maximumFeedback = i)
						.build())
				.addEntry(entryBuild
						.startLongField(Text.literal("(Linear) Minimum time for full thrust in ms"),
								config.fullMinTime)
						.setDefaultValue(100)
						.setMin(50)
						.setMax(1000000000)
						.setSaveConsumer(i -> config.fullMinTime = i)
						.build())
				.addEntry(entryBuild
						.startLongField(Text.literal("(Linear) Maximum time for full thrust in ms"),
								config.fullMaxTime)
						.setDefaultValue(5000)
						.setMin(100)
						.setMax(1000000000)
						.setSaveConsumer(i -> config.fullMaxTime = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Maximum score"),
								config.maximumScore)
						.setDefaultValue(80)
						.setMin(0)
						.setMax(100)
						.setSaveConsumer(i -> config.maximumScore = i)
						.build())
				.addEntry(entryBuild
						.startIntField(Text.literal("Maximum time in seconds before losing score"),
								config.maximumSecondsKeepScore)
						.setDefaultValue(30)
						.setMin(1)
						.setMax(300)
						.setSaveConsumer(i -> config.maximumSecondsKeepScore = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Score amount lost per tick"),
								config.scoreLostPerTick)
						.setDefaultValue(0.05f)
						.setMin(0.0001f)
						.setMax(10)
						.setSaveConsumer(i -> config.scoreLostPerTick = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Feedback score lost per tick"),
								config.feedbackScoreLostPerTick)
						.setDefaultValue(2f)
						.setMin(0.0001f)
						.setMax(10)
						.setSaveConsumer(i -> config.feedbackScoreLostPerTick = i)
						.build());

		// Mining
		ConfigCategory mininCategory = builder.getOrCreateCategory(Text.literal("Mining"));
		mininCategory
				.addEntry(entryBuild
						.startBooleanToggle(Text.literal("Enable mining ?"),
								config.miningEnabled)
						.setDefaultValue(true)
						.setSaveConsumer(i -> config.miningEnabled = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Score multiplier"),
								config.minePointsMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.minePointsMultiplier = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Feedback score multiplier"),
								config.mineInstantPointsMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.mineInstantPointsMultiplier = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Keep score duration multiplier"),
								config.mineDurationMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.mineDurationMultiplier = i)
						.build());						

		for (Map.Entry<String, BlockScoreConfig> entry : config.blocksScore.entrySet()) {
			mininCategory.addEntry(addBlockEntry(entry.getValue()));
		}

		mininCategory
			.addEntry(entryBuild
				.startFloatField(Text.literal("Every other blocks base score"),
						config.defaultBlockScore)
				.setDefaultValue(0)
				.setMin(0)
				.setMax(50)
				.setSaveConsumer(i -> config.defaultBlockScore = i)
				.build());

		// Masochist
		ConfigCategory masochistCategory = builder.getOrCreateCategory(Text.literal("Masochist"));
		masochistCategory
				.addEntry(entryBuild
						.startBooleanToggle(Text.literal("Enable masochist ?"),
								config.masochistEnabled)
						.setDefaultValue(true)
						.setSaveConsumer(i -> config.masochistEnabled = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Score multiplier"),
								config.masochistMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.masochistMultiplier = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Feedback score multiplier"),
								config.masochistInstantPointsMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.masochistInstantPointsMultiplier = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Keep score duration multiplier"),
								config.masochistDurationMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.masochistDurationMultiplier = i)
						.build());

		// Experience
		ConfigCategory experienceCategory = builder.getOrCreateCategory(Text.literal("Experience"));
		experienceCategory
				.addEntry(entryBuild
						.startBooleanToggle(Text.literal("Enable experience ?"),
								config.xpEnabled)
						.setDefaultValue(true)
						.setSaveConsumer(i -> config.xpEnabled = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Score multiplier"),
								config.xpMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.xpMultiplier = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Feedback score multiplier"),
								config.xpInstantPointsMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.xpInstantPointsMultiplier = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Keep score duration multiplier"),
								config.xpDurationMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.xpDurationMultiplier = i)
						.build());

		// Attack
		ConfigCategory attackCategory = builder.getOrCreateCategory(Text.literal("Attack"));
		attackCategory
				.addEntry(entryBuild
						.startBooleanToggle(Text.literal("Enable attack ?"),
								config.attackEnabled)
						.setDefaultValue(true)
						.setSaveConsumer(i -> config.attackEnabled = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Score multiplier"),
								config.attackMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.attackMultiplier = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Feedback score multiplier"),
								config.attackInstantPointsMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.attackInstantPointsMultiplier = i)
						.build())
				.addEntry(entryBuild
						.startFloatField(Text.literal("Keep score duration multiplier"),
								config.attackDurationMultiplier)
						.setDefaultValue(1)
						.setMin(0)
						.setMax(10)
						.setSaveConsumer(i -> config.attackDurationMultiplier = i)
						.build());

		return builder.build();
	}

	private AbstractConfigListEntry<?> addBlockEntry(BlockScoreConfig block) {
		MinetifaceConfig config = MinetifaceConfig.INSTANCE;

		return entryBuild.startFloatField(Text.literal(block.getName() + " base score"),
				config.blocksScore.get(block.getTranslationKey()).getScore())
				.setDefaultValue(MinetifaceConfig.defaultBlocks.get(block.getTranslationKey()).getScore())
				.setMin(0)
				.setMax(50)
				.setSaveConsumer(i -> config.blocksScore.put(block.getTranslationKey(),
						new BlockScoreConfig(block.getTranslationKey(), block.getName(), i)))
				.build();
	}
}
