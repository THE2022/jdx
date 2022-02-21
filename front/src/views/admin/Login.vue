<template>
  <div>
    <div
      v-if="title"
      style="text-align: center; margin: 40px 0 20px 0; font-size: 32px"
    >
      {{ title }}
    </div>
    <van-divider>请选择一种登录方式</van-divider>
    <van-grid>
      <van-grid-item
        v-for="s in sources"
        :key="s.source"
        :text="s.displayName"
        :url="'/api/oauth/render/' + s.source"
      >
        <van-image :src="'data:image/svg+xml;base64, ' + s.iconBase64" />
      </van-grid-item>
    </van-grid>
  </div>
</template>

<script>
import { baseInfo } from "@/api";

export default {
  name: "Login",
  data() {
    return {
      title: "",
      sources: [{ displayName: "", source: "", url: "", iconBase64: "" }]
    };
  },
  mounted() {
    this.renderBase();
    if (localStorage.getItem("token")) {
      this.$router.push("/admin");
    }
  },
  methods: {
    renderBase: function() {
      baseInfo()
        .then(resp => {
          this.title = resp.data.title;
          this.sources = resp.data.sources;
        })
        .catch(err => {
          console.log(err);
        });
    }
  }
};
</script>

<style scoped></style>
