<template>
	<v-app>
		<NavigationDrawer />

		<v-main>
			<v-container fill-height>
				<router-view v-slot="{ Component, route }">
					<transition name="fade" mode="out-in">
						<component :is="Component" :key="route.path" />
					</transition>
				</router-view>

				<ActionsFooter />
			</v-container>
		</v-main>

		<OverlayActivator />
		<AppFooter />
	</v-app>
</template>

<script lang="ts" setup>

</script>
