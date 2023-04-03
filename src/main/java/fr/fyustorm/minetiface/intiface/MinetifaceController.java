package fr.fyustorm.minetiface.intiface;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class MinetifaceController {
	private static final int TICKS_PER_SECOND = 20;
	private static final Logger LOGGER = LogManager.getLogger();

	private final List<AbstractPointsCounter> listPointsCounter;
	private final MiningPointsCounter miningPointsCounter;
	private final AttackPointsCounter attackPointsCounter;
	private final MasochistPointsCounter masochistPointsCounter;
	private final ExperienceCounter experienceCounter;

	private static final MinetifaceController instance;

	private int playerTick;
	private int clientTick;
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
		return Math.min((getPoints() + getInstantPoints()) / 100f, 1f);
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

	private void reset() {
		for (AbstractPointsCounter abstractPointsCounter : listPointsCounter) {
			abstractPointsCounter.reset();
		}
	}

	public void onPlayerTick() {
		playerTick = ++playerTick % (20 * (60 * TICKS_PER_SECOND));
		for (AbstractPointsCounter abstractPointsCounter : listPointsCounter) {
			abstractPointsCounter.onTick();
		}

		ToyController.setVibrationLevel(getIntensity());
	}

	public void onDamage(DamageSource source, LivingEntity target, float amount) {
		UUID clientId = MinecraftClient.getInstance().getSession().getProfile().getId();

		UUID playerEventId = null;

		if (target instanceof ServerPlayerEntity && target.getUuid().equals(clientId)) {
			// On hurt
			masochistPointsCounter.onHurt(source, amount);
		} else if (source.name.equals("player") && source.getSource() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getSource();
			playerEventId = player.getUuid();
		} else if (source.name.equals("arrow") && source.getAttacker() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getAttacker();
			playerEventId = player.getUuid();
		}

		// On attack
		if (clientId.equals(playerEventId)) {
			attackPointsCounter.onAttack(source, amount);
		}
	}

	public void onHurt(DamageSource source, float health) {
		masochistPointsCounter.onHurt(source, health);
	}

	public void onDeath() {
		reset();
		masochistPointsCounter.onDeath();
	}

	public void onBreak(PlayerEntity player, BlockState blockState) {
		UUID clientId = MinecraftClient.getInstance().getSession().getProfile().getId();
		UUID playerEventId = player.getGameProfile().getId();
		if (!clientId.equals(playerEventId)) {
			return;
		}

		miningPointsCounter.onBreak(blockState);
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
					ToyController.setVibrationLevel(0);
				}
			}
		}
	}

	public void onXpChange(float progress, int total, int level) {
		experienceCounter.onXpChange(progress, total, level);
	}

	public void onRespawn() {
		reset();
	}

	public void onWorldExit(Entity entity) {
		if ((entity instanceof PlayerEntity)) {
			reset();
		}
	}

	public void onWorldEntry(Entity entity) {
		if (entity instanceof ClientPlayerEntity) {
			LOGGER.info("Entered world: " + entity.toString());
		}
	}
}
