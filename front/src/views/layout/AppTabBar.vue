<template>
  <van-tabbar v-model="active">
    <van-tabbar-item icon="home-o" name="index" to="/">首页</van-tabbar-item>
    <van-tabbar-item icon="../assets/svg/terminal.svg" name="admin" to="/login">
      <span>后台管理</span>
      <template #icon="props">
        <img
          :src="
            props.active
              ? require('@/assets/svg/computor-active.svg')
              : require('@/assets/svg/computor.svg')
          "
        />
      </template>
    </van-tabbar-item>
  </van-tabbar>
</template>

<script>
export default {
  name: "AppTabBar",
  data() {
    return {
      active: "index"
    };
  },
  watch: {
    $route: {
      handler: function(val) {
        let path = val.path;
        this.changeTab(path);
      },
      // 深度观察监听
      deep: true
    }
  },
  mounted() {
    this.changeTab(this.$route.path);
  },
  methods: {
    changeTab(path) {
      let adminPaths = ["login", "admin", "oauth"];
      let admin = adminPaths.filter(e => path.indexOf(e) != -1).length > 0;
      if (admin) {
        this.active = "admin";
      }
    }
  }
};
</script>

<style scoped></style>
