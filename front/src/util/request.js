import axios from "axios";
import router from "../router";
import { Toast, Notify } from "vant";

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 50000
});

service.interceptors.request.use(
  config => {
    Toast.loading({
      duration: 0,
      message: "加载中...",
      forbidClick: true
    });
    // token
    config.headers["token"] = localStorage.getItem("token");
    return config;
  },
  error => {
    Toast.clear();
    console.error("interceptors request error: " + error);
    return Promise.reject(error);
  }
);

service.interceptors.response.use(
  response => {
    Toast.clear();
    const resp = response.data;
    // store.commit('SET_LOADING',false);
    let msg = resp.msg;
    if (resp.code != 0) {
      Notify({ type: "danger", message: msg });
      return Promise.reject(new Error(resp.msg || "Error"));
    } else {
      if (msg && msg.indexOf("处理成功") == -1) {
        Notify({ type: "success", message: msg });
      }
    }

    // if (res.code == 401) {
    //     /* 普通401拦截直接返回到登录页面 */
    //     removeAllCookie();
    //     router.push("/login");
    //     // return;
    // } else if (res.code !== 0) {
    //     console.log("有报错");
    //
    //     Notification({
    //         type: "error",
    //         title: "错误",
    //         message: res.msg
    //     });
    //     return Promise.reject(new Error(res.msg || "Error"));
    // } else {
    //     return res;
    // }
    return resp;
  },
  error => {
    Toast.clear();
    const resp = error.response.data;
    let msg = resp.msg;
    if (msg) {
      Notify({ type: "danger", message: msg });
    }
    if (error.response.status == 401) {
      console.log("aa");
      setTimeout(() => {
        localStorage.removeItem("token");
        localStorage.removeItem("thirdToken");
        router.push("/login");
      }, 500);
    }
    return Promise.reject(error);
  }
);

export default service;
