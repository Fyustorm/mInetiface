package fr.fyustorm.minetiface.intiface;

import fr.fyustorm.minetiface.config.MinetifaceConfig;
import net.minecraft.block.BlockState;

public class MiningPointsCounter extends AbstractPointsCounter {

	public void onBreak(BlockState blockState) {
		if (!MinetifaceConfig.INSTANCE.miningEnabled) {
			reset();
			return;
		}

		Float blockValue = null;
		if (MinetifaceConfig.INSTANCE.blocksScore.containsKey(blockState.getBlock().getTranslationKey())) {
			blockValue = MinetifaceConfig.INSTANCE.blocksScore.get(blockState.getBlock().getTranslationKey())
					.getScore();
		}

		if (blockValue == null) {
			blockValue = MinetifaceConfig.INSTANCE.defaultBlockScore;
		}

		addSkipDownTick(blockValue * 20 * MinetifaceConfig.INSTANCE.mineDurationMultiplier);
		addInstantPoints(blockValue * 5 * MinetifaceConfig.INSTANCE.mineInstantPointsMultiplier);
		addPoints(blockValue / 10 * MinetifaceConfig.INSTANCE.minePointsMultiplier);
	}
}