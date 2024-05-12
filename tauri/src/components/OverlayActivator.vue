<script setup lang="ts">
import { useAppStore } from "@/stores/app";
import { isRegistered, register, unregister } from "@tauri-apps/api/globalShortcut";
import { WebviewWindow } from "@tauri-apps/api/window";

const overlayActivated = ref(false);
const shortcutActivator = "Ctrl+F12";
const mainWindow = WebviewWindow.getByLabel("main");
const appStore = useAppStore();
let backgroundColorSave = '';

initShortcut();

async function initShortcut() {
	if (await isRegistered(shortcutActivator)) {
		await unregister(shortcutActivator);
	}

	register(shortcutActivator, async (_shortcut) => {
		if (await mainWindow?.isVisible()) {
			hideWindow();
		} else {
			showOverlay();
		}
	});
}

async function showOverlay() {
	if (mainWindow === null) {
		return;
	}
	overlayActivated.value = true;

	const root = getThemeDiv();
	backgroundColorSave = root.style.getPropertyValue('--v-theme-background');
	root.style.setProperty('--v-theme-background', 'transparent');
	

	await mainWindow.setFullscreen(true);
	await mainWindow.setDecorations(false);
	await mainWindow.setAlwaysOnTop(true);
	await mainWindow.show();
}

async function disableOverlay() {
	if (mainWindow === null) {
		return;
	}
	overlayActivated.value = false;

	const root = getThemeDiv();
	root.style.setProperty('--v-theme-background', backgroundColorSave);

	await mainWindow.setDecorations(true);
	await mainWindow.setFullscreen(false);
	await mainWindow.setAlwaysOnTop(false);
}

function hideWindow() {
	if (mainWindow === null) {
		return;
	}

	mainWindow.hide();
	appStore.writeFile();
}

function getThemeDiv() : HTMLDivElement {
	const root = document.querySelector("[class*='v-theme']") as HTMLDivElement;
	return root;
}
</script>

<template>
	<div class="overlayActions">
		<v-tooltip v-if="!overlayActivated" :text="'Toggle overlay (' + shortcutActivator + ')'">
			<template v-slot:activator="{ props }">
				<v-btn
					icon="mdi-dock-window"
					color="primary"
					v-bind="props"
					@click="showOverlay"
				>
				</v-btn>
			</template>
		</v-tooltip>
		<v-tooltip v-if="overlayActivated" :text="'Disable overlay'">
			<template v-slot:activator="{ props }">
				<v-btn
					icon="mdi-window-restore"
					color="primary"
					v-bind="props"
					@click="disableOverlay"
					class="mr-2"
				>
				</v-btn>
			</template>
		</v-tooltip>
		<v-tooltip v-if="overlayActivated" :text="'Hide overlay (' + shortcutActivator + ')'">
			<template v-slot:activator="{ props }">
				<v-btn
					icon="mdi-close"
					color="error"
					v-bind="props"
					@click="hideWindow"
				>
				</v-btn>
			</template>
		</v-tooltip>
	</div>
</template>

<style scoped>
.overlayActions {
	position: fixed;
	z-index: 2000;
	top: 10px;
	right: 10px;
}
</style>
