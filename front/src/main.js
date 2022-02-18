import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

Vue.config.productionTip = false;
import { Button, NavBar, Form, Field, Toast, Dialog } from "vant";
import axios from "axios";
import VueClipboard from "vue-clipboard2";

Vue.use(Button);
Vue.use(NavBar);
Vue.use(Form);
Vue.use(Field);

Vue.use(VueClipboard);

Vue.prototype.$axios = axios;
Vue.prototype.$toast = Toast;
Vue.prototype.$dialog = Dialog;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
