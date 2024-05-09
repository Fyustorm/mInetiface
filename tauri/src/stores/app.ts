// Utilities
import { writeTextFile } from "@tauri-apps/api/fs";
import { defineStore } from "pinia";

export const useAppStore = defineStore("app", () => {
	const path = ref("");

	const config: MinetifaceConf = reactive({
		serverUrl: "",
		minimumFeedback: 0,
		maximumFeedback: 0,
		maximumScore: 0,
		feedbackScoreLostPerTick: 0,
		scoreLostPerTick: 0,
		maximumSecondsKeepScore: 0,
		fullMaxTime: 0,
		fullMinTime: 0,
		masochistEnabled: false,
		masochistMultiplier: 0,
		masochistInstantPointsMultiplier: 0,
		masochistDurationMultiplier: 0,
		xpEnabled: false,
		xpMultiplier: 0,
		xpInstantPointsMultiplier: 0,
		xpDurationMultiplier: 0,
		miningEnabled: false,
		minePointsMultiplier: 0,
		mineInstantPointsMultiplier: 0,
		mineDurationMultiplier: 0,
		blocksScore: undefined,
		defaultBlockScore: 0,
		attackEnabled: false,
		attackMultiplier: 0,
		attackInstantPointsMultiplier: 0,
		attackDurationMultiplier: 0,
	});

	const configLoaded = ref(false);

	const savedConfig = ref({});

	function saveConfigState() {
		Object.assign(savedConfig.value, config);
	}

	const changed = computed(() => {
		console.log(config);
		return JSON.stringify(savedConfig.value) !== JSON.stringify(config);
	});

	function writeFile() {
		writeTextFile(path.value, JSON.stringify(config, null, "\t"));
		saveConfigState();
  }

	return { config, configLoaded, saveConfigState, changed, path, writeFile };
});
