<script setup lang="ts">
import { useAppStore } from '@/stores/app';

const appStore = useAppStore();
function saveFile() {
    appStore.writeFile();
}

</script>

<template>
    <div class="actions">
        <v-btn icon="mdi-content-save" :disabled="!appStore.changed" @click="saveFile" color="primary"></v-btn>
    </div>
</template>

<style scoped>
.actions {
    position: fixed;
    bottom: 55px;
    right: 15px;
}
</style>