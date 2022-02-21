<template>
  <div id="app">
    <AppNav></AppNav>
    <AppTabBar></AppTabBar>
    <router-view />
  </div>
</template>
<script>
import AppNav from "./views/layout/AppNav";
import AppTabBar from "./views/layout/AppTabBar";

export default {
  components: { AppNav, AppTabBar }
};
</script>
<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav {
  color: #42b983;
}
</style>
