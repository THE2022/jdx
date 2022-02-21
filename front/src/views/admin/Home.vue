<template>
  <div>
    <div style="background-color: #f6f6f6; padding-bottom: 20px;">
      <van-tabs v-model="active">
        <van-tab title="青龙管理">
          <QLManage />
        </van-tab>
        <van-tab title="系统设置">
          <SystemConfig />
        </van-tab>
        <van-tab title="关于">
          <About />
        </van-tab>
      </van-tabs>
    </div>
  </div>
</template>

<script>
import QLManage from "@/views/admin/tab/QLManage";
import SystemConfig from "@/views/admin/tab/SystemConfig";
import About from "@/views/admin/tab/About";

export default {
  name: "Home",
  components: { QLManage, SystemConfig, About },
  data() {
    return {
      active: 0
    };
  }
};
</script>

<style scoped></style>
