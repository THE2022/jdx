<template>
  <div>
    <van-notice-bar
      v-if="notice"
      left-icon="volume-o"
      :text="notice"
      mode="closeable"
    />
    <div
      v-if="title"
      style="text-align: center; margin: 40px 0 20px 0; font-size: 32px"
    >
      {{ title }}
    </div>

    <div style="margin: 4px 0 16px 16px">
      <van-radio-group v-model="model" direction="horizontal">
        <van-radio name="ck">获取CK</van-radio>
        <van-radio name="ql">提交青龙</van-radio>
      </van-radio-group>
    </div>

    <van-field
      v-model="form.mobile"
      name="mobile"
      label="手机号"
      placeholder="手机号"
    ></van-field>
    <van-field
      v-model="form.code"
      name="code"
      label="验证码"
      placeholder="验证码"
    >
      <template #button>
        <van-count-down
          v-if="Number(expireTime) > 0"
          ref="countDown"
          millisecond
          :time="expireTime"
          format="ss:SSS"
        />
        <van-button v-else size="small" plain type="info" @click="smsCode"
          >发送验证码</van-button
        >
      </template>
    </van-field>
    <van-field
      v-if="model == 'ql'"
      readonly
      clickable
      label="选择节点"
      :value="form.displayName"
      placeholder="选择青龙节点"
      @click="showQLPicker = true"
    ></van-field>

    <van-field
      v-if="model == 'ql'"
      v-model="form.remark"
      name="remark"
      label="备注"
      placeholder="备注"
    />

    <div style="margin: 16px; ">
      <van-button
        v-if="model == 'ck'"
        round
        block
        :disabled="!form.code"
        type="primary"
        @click="login"
      >
        获取CK
      </van-button>
      <van-button
        v-if="model == 'ql'"
        round
        block
        :disabled="!form.code"
        type="warning"
        @click="login"
      >
        提交青龙
      </van-button>
    </div>

    <!-- -->
    <van-popup v-model="showQLPicker" round position="bottom">
      <van-picker
        show-toolbar
        :columns="qls"
        @cancel="showQLPicker = false"
        @confirm="conformQL"
      />
    </van-popup>
  </div>
</template>
<script>
import { baseInfo, jdSmsCode, jdLogin } from "@/api";

export default {
  data() {
    return {
      title: "",
      notice: "",
      model: "ck",
      showQLPicker: false,
      qls: [],
      expireTime: 0,
      form: {
        mobile: "",
        code: "",
        displayName: "",
        remark: ""
      }
    };
  },
  mounted() {
    this.renderBase();
  },
  methods: {
    renderBase: function() {
      baseInfo()
        .then(resp => {
          this.title = resp.data.title;
          this.notice = resp.data.notice;
          this.qls = resp.data.qls;
          if (this.qls.length > 0) {
            this.form.displayName = this.qls[0];
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    smsCode: function() {
      this.form.code = "";
      jdSmsCode(this.form.mobile).then(resp => {
        this.expireTime = resp.data.expireTime * 1000;
      });
    },
    login: function() {
      let _this = this;
      if (this.model == "ck") {
        this.form.displayName = "";
        this.form.remark = "";
      }
      jdLogin(this.form)
        .then(function(response) {
          // 计时器清零
          _this.expireTime = 0;
          // ck模式只弹出复制CK弹窗
          if (this.model == "ck") {
            _this.$dialog
              .alert({
                title: "提示",
                message: response.data.cookie,
                confirmButtonText: "点击复制"
              })
              .then(() => {
                _this
                  .$copyText(response.data.cookie)
                  .then(() => {
                    _this.$toast.success("复制成功");
                  })
                  .catch(() => {
                    _this.$toast.fail("复制失败，请手动复制");
                  });
              });
          }
          // ql模式
          if (this.model == "ql") {
            // ignore
          }
        })
        .catch(function(error) {
          console.error(error);
        });
    },
    conformQL(qlId) {
      this.form.displayName = qlId;
      this.showQLPicker = false;
    }
  }
};
</script>
