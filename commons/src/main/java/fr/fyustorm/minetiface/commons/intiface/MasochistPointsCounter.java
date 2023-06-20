package fr.fyustorm.minetiface.commons.intiface;

import fr.fyustorm.minetiface.commons.config.MinetifaceConfig;

public class MasochistPointsCounter extends AbstractPointsCounter {

	public void onHurt(float health) {
		if (!MinetifaceConfig.INSTANCE.masochistEnabled) {
			super.reset();
			return;
		}

		if (health == 0) {
			return;
		}
		
		addSkipDownTick(health * 10 * MinetifaceConfig.INSTANCE.masochistDurationMultiplier);
		addInstantPoints(health * MinetifaceConfig.INSTANCE.masochistInstantPointsMultiplier);
		addPoints(health * MinetifaceConfig.INSTANCE.masochistMultiplier);
	}

	public void onDeath() {
		if (!MinetifaceConfig.INSTANCE.masochistEnabled) {
			super.reset();
			return;
		}

		addSkipDownTick(100 * MinetifaceConfig.INSTANCE.masochistDurationMultiplier);
		addInstantPoints(50 * MinetifaceConfig.INSTANCE.masochistInstantPointsMultiplier);
		addPoints(50 * MinetifaceConfig.INSTANCE.masochistMultiplier);
	}

	@Override
	public void reset() {
		// Overriding reset because it is called when player died
	}
}