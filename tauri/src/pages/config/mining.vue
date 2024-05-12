<script lang="ts" setup>
import { useAppStore } from "@/stores/app";
import {
	MIN_MULTIPLIER,
	STEP_MULTIPLIER,
	MAX_MULTIPLIER,
} from "@/minetiface/consts";

const appStore = useAppStore();
const config = appStore.config;
</script>

<template>
	<v-container>
		<v-row>
			<v-col class="v-col-12 v-col-xl-6">
				<v-card>
					<h2>Mining</h2>
					<div class="form-group">
						<div>Enable ?</div>
						<v-switch v-model="config.miningEnabled" hide-details></v-switch>
					</div>

					<div class="form-group">
						<div>Multiplier</div>
						<Slider
							:min="MIN_MULTIPLIER"
							:max="MAX_MULTIPLIER"
							:step="STEP_MULTIPLIER"
							v-model="config.minePointsMultiplier"
							hide-details
						></Slider>
					</div>

					<div class="form-group">
						<div>Feedback multiplier</div>
						<Slider
							:min="MIN_MULTIPLIER"
							:max="MAX_MULTIPLIER"
							:step="STEP_MULTIPLIER"
							v-model="config.mineInstantPointsMultiplier"
							hide-details
						></Slider>
					</div>

					<div class="form-group">
						<div>Duration multiplier</div>
						<Slider
							:min="MIN_MULTIPLIER"
							:max="MAX_MULTIPLIER"
							:step="STEP_MULTIPLIER"
							v-model="config.mineDurationMultiplier"
							hide-details
						></Slider>
					</div>
				</v-card>
			</v-col>
		</v-row>
	</v-container>
</template>
