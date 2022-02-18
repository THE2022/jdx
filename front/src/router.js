import Vue from "vue";
import Router from "vue-router";
import JD from "./views/JD.vue";

Vue.use(Router);

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "jd",
      component: JD,
      meta: {
        title: "JDX"
      }
    }
  ]
});
