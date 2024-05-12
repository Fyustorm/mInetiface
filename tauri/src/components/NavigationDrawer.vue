<script setup lang="ts">
import { useAppStore } from "@/stores/app";
import { useDisplay } from "vuetify";

const { lgAndUp, mdAndDown } = useDisplay();
const appStore = useAppStore();
const router = useRouter();

const drawer = ref(true);
const items = [
	{ title: "Main", icon: "mdi-cog", to: "/config/general" },
	{ title: "Mining", icon: "mdi-pickaxe", to: "/config/mining" },
	{ title: "Attack", icon: "mdi-sword", to: "/config/attack" },
	{ title: "Experience", icon: "mdi-one-up", to: "/config/xp" },
	{
		title: "Masochist",
		icon: "mdi-heart-broken-outline",
		to: "/config/masochist",
	},
];

function navigate(item: { to: string }) {
	router.push(item.to);
}
</script>

<template>
	<template v-if="appStore.configLoaded">
		<v-app-bar v-if="mdAndDown">
			<v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
			<v-toolbar-title>mInetiface</v-toolbar-title>
			<!-- <v-btn density="compact" icon="mdi-theme-light-dark" @click="toggleTheme"></v-btn> -->
		</v-app-bar>
		<v-navigation-drawer v-model="drawer">
			<template v-if="lgAndUp">
				<v-list-item>
					<v-list-item-title class="text-h6 pa-2">
						mInetiface
					</v-list-item-title>
				</v-list-item>
				<v-divider></v-divider>
			</template>
			<v-list dense nav>
				<router-link
					:to="item.to"
					v-for="item in items"
					custom
					v-slot="{ isActive }"
				>
					<v-list-item @click="navigate(item)" :active="isActive">
						<v-list-item-title>{{ item.title }}</v-list-item-title>
						<template v-slot:prepend>
							<v-icon>{{ item.icon }}</v-icon>
						</template>
					</v-list-item>
				</router-link>
			</v-list>
		</v-navigation-drawer>
	</template>
</template>
