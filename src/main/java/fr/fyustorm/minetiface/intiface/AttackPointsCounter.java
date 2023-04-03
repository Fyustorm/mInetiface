package fr.fyustorm.minetiface.intiface;

import fr.fyustorm.minetiface.config.MinetifaceConfig;
import net.minecraft.entity.damage.DamageSource;

public class AttackPointsCounter extends AbstractPointsCounter {
	public void onAttack(DamageSource source, float amount) {
		if (!MinetifaceConfig.INSTANCE.attackEnabled) {
			reset();
			return;
		}

		addPoints(amount / 20 * MinetifaceConfig.INSTANCE.attackMultiplier);
		addInstantPoints(amount * 10 * MinetifaceConfig.INSTANCE.attackInstantPointsMultiplier);
		addSkipDownTick(amount * 10 * MinetifaceConfig.INSTANCE.attackDurationMultiplier);
	}
}