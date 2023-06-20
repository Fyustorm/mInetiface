package fr.fyustorm.minetiface.commons.intiface;

import fr.fyustorm.minetiface.commons.config.MinetifaceConfig;

public class AttackPointsCounter extends AbstractPointsCounter {
	public void onAttack(float amount) {
		if (!MinetifaceConfig.INSTANCE.attackEnabled) {
			reset();
			return;
		}

		addPoints(amount / 20 * MinetifaceConfig.INSTANCE.attackMultiplier);
		addInstantPoints(amount * 10 * MinetifaceConfig.INSTANCE.attackInstantPointsMultiplier);
		addSkipDownTick(amount * 10 * MinetifaceConfig.INSTANCE.attackDurationMultiplier);
	}
}