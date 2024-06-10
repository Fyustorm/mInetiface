package fr.fyustorm.minetiface.commons.intiface;

import fr.fyustorm.minetiface.commons.config.MinetifaceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MinetifaceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MinetifaceController.class);
	private static final int TICKS_PER_SECOND = 20;
	private final List<AbstractPointsCounter> listPointsCounter;
	private final MiningPointsCounter miningPointsCounter;
	private final AttackPointsCounter attackPointsCounter;
	private final MasochistPointsCounter masochistPointsCounter;
	private final ExperienceCounter experienceCounter;

	private static final MinetifaceController instance;

	private long playerTick;
	private long clientTick;
	private boolean paused;

	static {
		instance = new MinetifaceController();
	}

	public static MinetifaceController getInstance() {
		return instance;
	}

	private MinetifaceController() {
		playerTick = -1;
		clientTick = -1;
		paused = false;

		listPointsCounter = new ArrayList<>();

		miningPointsCounter = new MiningPointsCounter();
		attackPointsCounter = new AttackPointsCounter();
		masochistPointsCounter = new MasochistPointsCounter();
		experienceCounter = new ExperienceCounter();

		listPointsCounter.add(miningPointsCounter);
		listPointsCounter.add(attackPointsCounter);
		listPointsCounter.add(masochistPointsCounter);
		listPointsCounter.add(experienceCounter);
	}

	public float getIntensity() {
		float points = getPoints();
		float instantPoints = getInstantPoints();

		LOGGER.trace("Points {} Instant {}", points, instantPoints);

		return Math.min((points + instantPoints) / 100f, 1f);
	}

	public float getInstantPoints() {

		float instanPoints = 0f;
		for (AbstractPointsCounter abstractPointsCounter : listPointsCounter) {
			instanPoints = Math.max(abstractPointsCounter.getInstantPoints(), instanPoints);
		}
		return instanPoints;
	}

	public float getPoints() {
		float points = 0f;
		for (AbstractPointsCounter abstractPointsCounter : listPointsCounter) {
			points = Math.max(abstractPointsCounter.getPoints(), points);
		}
		return points;
	}

	public int getSkipDownTicks() {
		int skipDownTick = 0;
		for (AbstractPointsCounter abstractPointsCounter : listPointsCounter) {
			skipDownTick += abstractPointsCounter.getSkipDownTicks();
		}
		return skipDownTick;
	}

	/**
	 * Reset all points counters
	 */
	public void reset() {
		for (AbstractPointsCounter abstractPointsCounter : listPointsCounter) {
			abstractPointsCounter.reset();
		}
	}

	public void onPlayerTick() {
		playerTick = ++playerTick % (20 * (60 * TICKS_PER_SECOND));

		for (AbstractPointsCounter abstractPointsCounter : listPointsCounter) {
			abstractPointsCounter.onTick();
		}

		float intensity = getIntensity();
		ToyController.instance().setScalarLevel(intensity);

		if (getPoints() > 0 || getInstantPoints() > MinetifaceConfig.INSTANCE.minimumFeedback) {
			ToyController.instance().setLinearLevel(intensity);
		}
	}

	public void onDamage(float amount) {
		LOGGER.debug("On damage. Amount {}", amount);
		attackPointsCounter.onAttack(amount);
	}

	public void onHurt(float amount) {
		LOGGER.debug("On hurt. Amount {}", amount);
		masochistPointsCounter.onHurt(amount);
	}

	public void onDeath() {
		LOGGER.debug("Player died");
		reset();
		masochistPointsCounter.onDeath();
	}

	/**
	 * On break event
	 * @param blockId breaked block
	 */
	public void onBreak(String blockId) {
		LOGGER.info("Block break {}", blockId);
		miningPointsCounter.onBreak(blockId);
	}

	public void onHarvest(float destroySpeed, String blockId) {
		// LOGGER.debug("Block harvest {} Speed {}", blockId, destroySpeed);
		miningPointsCounter.onHarvest(destroySpeed, blockId);
	}

	public void onClientTick() {
		// Handle when game is paused
		if (playerTick >= 0) {
			if (clientTick != playerTick) {
				clientTick = playerTick;
				paused = false;
			} else {
				if (!paused) {
					paused = true;
					LOGGER.debug("Paused");
					ToyController.instance().setScalarLevel(0);
				}
			}
		}
	}

	public void onXpChange(float progress, int total, int level) {
		LOGGER.debug("Xp changed. Progress {} Total {} Level {}", progress, total, level);
		experienceCounter.onXpChange(progress, total, level);
	}
}
