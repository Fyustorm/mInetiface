<script setup lang="ts">
defineProps({
	min: Number,
	max: Number,
	step: Number,
});

const value = defineModel<number>({ required: true });
</script>

<template>
	<v-slider :min="min" :max="max" :step="step" v-model="value" hide-details>
		<template v-slot:prepend>
			<div class="d-flex flex-column justify-end align-center">
				<v-text-field
					v-model.number="value"
					:step="step"
					style="width: 90px"
					type="number"
					variant="outlined"
					hide-details
					single-line
				></v-text-field>
			</div>
		</template>
	</v-slider>
</template>
