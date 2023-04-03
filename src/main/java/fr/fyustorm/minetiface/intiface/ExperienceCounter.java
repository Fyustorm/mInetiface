package fr.fyustorm.minetiface.intiface;

import fr.fyustorm.minetiface.config.MinetifaceConfig;

public class ExperienceCounter extends AbstractPointsCounter {

	private int lastLevel;

	public ExperienceCounter() {
		super();
		lastLevel = -1;
	}

	public void onXpChange(float progress, int total, int level) {
		if (!MinetifaceConfig.INSTANCE.xpEnabled) {
			reset();
			return;
		}

		if (lastLevel == -1) {
			lastLevel = level;
			return;
		}

		boolean levelChanged = lastLevel != level;
		lastLevel = level;

		if (levelChanged) {
			total *= 2;
		}

		addSkipDownTick(total * MinetifaceConfig.INSTANCE.xpDurationMultiplier / 10);
		addInstantPoints(total * MinetifaceConfig.INSTANCE.xpInstantPointsMultiplier / 10);
		addPoints(0.5f * MinetifaceConfig.INSTANCE.xpMultiplier);
	}
}