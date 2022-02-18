<template>
  <div>
    <van-nav-bar title="JDX" left-text="" right-text="" />

    <van-form>
      <van-field
        v-model="form.mobile"
        name="mobile"
        label="手机号"
        placeholder="手机号"
      />
      <van-field
        v-model="form.code"
        name="code"
        label="验证码"
        placeholder="验证码"
      />
      <div style="margin: 16px;">
        <van-button round block type="info" @click="smsCode">
          发送验证码
        </van-button>
        <van-button
          style="margin-top: 16px"
          round
          block
          type="info"
          @click="login"
        >
          提交
        </van-button>
      </div>
    </van-form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      form: {
        mobile: "",
        code: ""
      }
    };
  },
  methods: {
    smsCode: function() {
      let _this = this;
      _this.$toast.loading({
        duration: 0,
        message: "加载中...",
        forbidClick: true
      });
      this.$axios
        .get("api/jd/smsCode?mobile=" + this.form.mobile)
        .then(function(response) {
          _this.$toast.clear();
          let code = response.data.code;
          if (code != 0) {
            _this.$toast.fail(response.data.msg);
            return;
          }
          _this.$toast.success(response.data.msg);
        })
        .catch(function(error) {
          _this.$toast.clear();
          console.err(error);
        });
    },
    login: function() {
      let _this = this;
      _this.$toast.loading({
        duration: 2,
        message: "加载中...",
        forbidClick: true
      });

      this.$axios
        .post("api/jd/login", this.form)
        .then(function(response) {
          _this.$toast.clear();
          let code = response.data.code;
          if (code != 0) {
            _this.$toast.fail(response.data.msg);
            return;
          }

          _this.$dialog
            .alert({
              title: "提示",
              message: response.data.data.cookie,
              confirmButtonText: "点击复制"
            })
            .then(() => {
              _this
                .$copyText(response.data.data.cookie)
                .then(() => {
                  _this.$toast.success("复制成功");
                })
                .catch(() => {
                  _this.$toast.fail("复制失败，请手动复制");
                });
            });
        })
        .catch(function(error) {
          _this.$toast.clear();
          console.error(error);
        });
    }
  }
};
</script>
