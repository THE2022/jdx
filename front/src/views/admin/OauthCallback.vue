<template>
  <span
    style="text-align: center;font-size: 24px;display: block;margin-top: 40px"
    >跳转中...</span
  >
</template>

<script>
export default {
  name: "OauthCallback",
  data() {
    return {
      authCallback: {
        source: "",
        code: "",
        state: ""
      }
    };
  },
  created() {
    this.authCallback.source = this.$route.params.source;
  },
  mounted() {
    let query = this.$route.query;
    this.authCallback.code = query.code;
    this.authCallback.state = query.state;
    if (
      this.authCallback.code &&
      this.authCallback.state &&
      this.authCallback.source
    ) {
      this.$store
        .dispatch("user/socialLogin", this.authCallback)
        .then(resp => {
          console.log(resp);
          this.$router.push("/admin");
        })
        .catch(err => {
          console.log(err);
          this.$router.push("/");
        });
    }
  }
};
</script>

<style scoped></style>
