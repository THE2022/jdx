import Vue from "vue";
import Router from "vue-router";
import Index from "./views/index/Index";
import OauthCallback from "./views/admin/OauthCallback";
import AdminLogin from "./views/admin/Login";
import AdminHome from "./views/admin/Home";

Vue.use(Router);

export default new Router({
  mode: "history",
  // mode: "hash",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "Index",
      component: Index,
      meta: {
        title: "JDX"
      }
    },
    {
      path: "/oauth/callback/:source",
      name: "oauthCallback",
      component: OauthCallback,
      meta: {
        title: "Oauth"
      }
    },
    {
      path: "/login",
      name: "adminLogin",
      component: AdminLogin,
      meta: {
        title: "登录"
      }
    },
    {
      path: "/admin",
      name: "adminIndex",
      component: AdminHome,
      meta: {
        title: "Dashboard"
      }
    }
  ]
});
