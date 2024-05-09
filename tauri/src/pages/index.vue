<script lang="ts" setup>
import { useAppStore } from "@/stores/app";
import { open } from "@tauri-apps/api/dialog";
import { readTextFile } from "@tauri-apps/api/fs";

const appStore = useAppStore();
const router = useRouter();

async function openConfig() {
	// Open a selection dialog for image files
	const selected = await open({
		multiple: false,
		filters: [
			{
				name: "Config",
				extensions: ["config"],
			},
		],
	});

	if (selected === null || Array.isArray(selected)) {
		return;
	}

	let contents = await readTextFile(selected);

	const conf = <MinetifaceConf>JSON.parse(contents);
	console.log(conf);
	if (conf === null) {
		return;
	}

	Object.assign(appStore.config, conf);
	appStore.saveConfigState();
	appStore.path = selected;
	appStore.configLoaded = true;
	router.push("/config/general");
}
</script>

<template>
	<v-container>
		<h2>Config file</h2>
		<v-btn @click="openConfig" class="mt-2">Load config file</v-btn>
	</v-container>
</template>
