import Attack from "@/pages/config/attack.vue";
import General from "@/pages/config/general.vue";
import Masochist from "@/pages/config/masochist.vue";
import Mining from "@/pages/config/mining.vue";
import Xp from "@/pages/config/xp.vue";
import Index from "@/pages/index.vue";
import { createMemoryHistory, createRouter } from "vue-router";

const routes = [
	{ path: "/", component: Index },
	{ path: "/config/attack", component: Attack },
	{ path: "/config/general", component: General },
  { path: "/config/masochist", component: Masochist },
  { path: "/config/mining", component: Mining },
  { path: "/config/xp", component: Xp}
];

const router = createRouter({
	history: createMemoryHistory(),
	routes,
});

export default router;
