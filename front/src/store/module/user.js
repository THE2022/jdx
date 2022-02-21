import { login } from "@/api/admin";

const getDefaultState = () => {
  return {
    userInfo: {}
  };
};

const state = getDefaultState();

const mutations = {
  RESET_USERINFO: (state, userInfo) => {
    state.userInfo = userInfo;
  }
};

const actions = {
  socialLogin({ commit }, authCallback) {
    return new Promise((resolve, reject) => {
      login(authCallback)
        .then(res => {
          let data = res.data;
          console.log(data);
          commit("RESET_USERINFO", data);
          localStorage.setItem("token", data.token);
          localStorage.setItem("thirdToken", data.thirdToken);
          resolve();
        })
        .catch(error => {
          reject(error);
        });
    });
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions
};
