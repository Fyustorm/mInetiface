package fr.fyustorm.minetiface.commons.intiface;


import fr.fyustorm.minetiface.commons.config.MinetifaceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiningPointsCounter extends AbstractPointsCounter {

	private static final Logger LOGGER = LoggerFactory.getLogger(MiningPointsCounter.class);

	public void onBreak(String blockId) {
		if (!MinetifaceConfig.INSTANCE.miningEnabled) {
			reset();
			return;
		}

		Float blockValue = null;
		if (MinetifaceConfig.INSTANCE.blocksScore.containsKey(blockId)) {
			blockValue = MinetifaceConfig.INSTANCE.blocksScore.get(blockId)
					.getScore();
		}

		LOGGER.info("Value {}", blockValue);

		if (blockValue == null) {
			blockValue = MinetifaceConfig.INSTANCE.defaultBlockScore;
		}

		addSkipDownTick(blockValue * 20 * MinetifaceConfig.INSTANCE.mineDurationMultiplier);
		addInstantPoints(blockValue * 5 * MinetifaceConfig.INSTANCE.mineInstantPointsMultiplier);
		addPoints(blockValue / 10 * MinetifaceConfig.INSTANCE.minePointsMultiplier);
	}
}