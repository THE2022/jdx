import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

Vue.config.productionTip = false;
import {
  Button,
  NavBar,
  Form,
  Field,
  Icon,
  Image as VanImage,
  ImagePreview,
  Popover,
  Tabbar,
  TabbarItem,
  Grid,
  GridItem,
  Divider,
  NoticeBar,
  Tab,
  Tabs,
  Cell,
  CellGroup,
  SwipeCell,
  Card,
  ActionSheet,
  Empty,
  Picker,
  Popup,
  CountDown,
  Radio,
  RadioGroup,
  Toast,
  Dialog
} from "vant";
import axios from "axios";
import VueClipboard from "vue-clipboard2";

Vue.use(Icon);
Vue.use(Button);
Vue.use(NavBar);
Vue.use(Form);
Vue.use(Field);
Vue.use(VanImage);
Vue.use(ImagePreview);
Vue.use(Tabbar);
Vue.use(TabbarItem);
Vue.use(Grid);
Vue.use(GridItem);
Vue.use(Divider);
Vue.use(NoticeBar);
Vue.use(Tab);
Vue.use(Tabs);
Vue.use(Cell);
Vue.use(CellGroup);
Vue.use(SwipeCell);
Vue.use(Card);
Vue.use(ActionSheet);
Vue.use(Empty);
Vue.use(Popover);
Vue.use(Picker);
Vue.use(Popup);
Vue.use(CountDown);
Vue.use(Radio);
Vue.use(RadioGroup);

Vue.use(VueClipboard);

Vue.prototype.$axios = axios;
Vue.prototype.$toast = Toast;
Vue.prototype.$dialog = Dialog;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
