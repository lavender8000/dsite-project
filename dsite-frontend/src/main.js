import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import './style.css';
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import Toast from 'vue-toastification';
import 'vue-toastification/dist/index.css';
import { useAuthStore } from '@/stores/auth';

library.add(fas);

const pinia = createPinia();
const app = createApp(App);

app
  .component('font-awesome-icon', FontAwesomeIcon)
  .use(pinia)
  .use(router)
  .use(Toast, { timeout: 1800 })

const authStore = useAuthStore();
authStore.checkLogin().then(() => app.mount('#app'));