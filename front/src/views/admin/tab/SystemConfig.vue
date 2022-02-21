<template>
  <div>
    <van-divider
      :style="{
        color: '#1989fa',
        borderColor: '#1989fa',
        padding: '0 16px',
        marginTop: '32px'
      }"
      >网站设置
    </van-divider>
    <van-swipe-cell>
      <van-cell-group inset>
        <van-cell title="标题" :value="title" clickable />
        <van-cell title="公告" :value="notice" clickable />
      </van-cell-group>

      <template #right>
        <van-button
          square
          type="info"
          class="slide-button"
          text="编辑"
          @click="webSiteConfigVisible = true"
        />
        <van-button
          square
          type="danger"
          class="slide-button"
          text="删除"
          @click="webSiteConfigVisible = true"
        />
      </template>
    </van-swipe-cell>

    <van-divider
      :style="{
        color: '#1989fa',
        borderColor: '#1989fa',
        padding: '0 16px',
        marginTop: '48px'
      }"
      >社交登录设置
    </van-divider>

    <div style="margin: 16px 0 16px 20px">
      <van-popover
        v-model="showPopover"
        trigger="click"
        :actions="socialPicker.actions"
        @select="editSocialConfig()"
      >
        <template #reference>
          <van-button type="primary" icon="apps-o" size="small"
            >操作</van-button
          >
        </template>
      </van-popover>
    </div>

    <van-swipe-cell
      v-for="social in socialPlatforms"
      :key="social.source"
      :title="social.source"
      style="margin-bottom: 20px"
    >
      <van-cell-group :title="social.source" inset>
        <van-cell title="客户端ID" clickable>
          <template #default>
            {{ social.clientId }}
          </template>
        </van-cell>
        <van-cell title="客户端密钥" clickable>
          <template #default>
            {{ social.clientSecret }}
          </template>
        </van-cell>
        <van-cell title="重定向地址" clickable>
          <template #default>
            {{ social.redirectUri }}
          </template>
        </van-cell>
        <van-cell title="管理员" clickable>
          <template #default>
            {{ social.admin }}
          </template>
        </van-cell>
      </van-cell-group>
      <template #right>
        <van-button
          square
          text="编辑"
          type="info"
          class="slide-button"
          @click="editSocialConfig(social)"
        />
        <van-button
          square
          text="删除"
          type="danger"
          class="slide-button"
          @click="delSocialConfig(social.source)"
        />
      </template>
    </van-swipe-cell>

    <van-action-sheet v-model="webSiteConfigVisible" title="编辑网站设置">
      <van-form>
        <van-field v-model="title" label="网站标题" placeholder="网站标题" />
        <van-field
          v-model="notice"
          label="公告"
          autosize
          type="textarea"
          placeholder="公告"
        />
        <div style="margin: 16px;">
          <van-button round block type="info" @click="updateWebsiteConfig()"
            >提交
          </van-button>
        </div>
      </van-form>
    </van-action-sheet>

    <van-action-sheet
      v-model="saveSocialConfigVisible"
      :title="saveSocialTitle"
    >
      <van-form>
        <van-field
          :value="form.socialConfig.source"
          label="社交平台"
          placeholder="点击选择社交平台"
          @click="socialPicker.show = true"
        />
        <van-popup v-model="socialPicker.show" position="bottom">
          <van-picker
            :readonly="socialPicker.readonly"
            :title="socialPicker.title"
            show-toolbar
            :columns="supportedSocialSource"
            @confirm="conformSocial"
            @cancel="socialPicker.show = false"
          />
        </van-popup>
        <van-field
          v-model="form.socialConfig.clientId"
          label="客户端ID"
          type="textarea"
          autosize
          placeholder="客户端ID"
        />
        <van-field
          v-model="form.socialConfig.clientSecret"
          label="客户端密钥"
          type="textarea"
          autosize
          placeholder="客户端密钥"
        />
        <van-field
          v-model="form.socialConfig.redirectUri"
          label="重定向地址"
          type="textarea"
          autosize
          placeholder="重定向地址"
        />
        <van-field
          v-model="form.socialConfig.admin"
          label="管理员"
          type="textarea"
          autosize
          placeholder="多个英文半角逗号 , 分割"
        />
        <div style="margin: 16px;">
          <van-button round block type="info" @click="saveSocialConfig()"
            >提交</van-button
          >
        </div>
      </van-form>
    </van-action-sheet>
  </div>
</template>

<script>
import {
  getSystemConfig,
  updateWebsiteConfig,
  saveSocialConfig,
  delSocialConfig
} from "@/api/admin";

export default {
  name: "SystemConfig",
  data() {
    return {
      title: "",
      notice: "",
      socialPlatforms: [
        {
          source: "",
          clientId: "",
          clientSecret: "",
          redirectUri: "",
          admin: ""
        }
      ],

      webSiteConfigVisible: false,
      saveSocialConfigVisible: false,

      showPopover: false,
      saveSocialTitle: "编辑社交登录设置",
      socialPicker: {
        title: "选择社交平台",
        show: false,
        actions: [{ text: "新增配置" }],
        readonly: false
      },
      form: {
        socialConfig: {
          source: "",
          clientId: "",
          clientSecret: "",
          redirectUri: "",
          admin: ""
        }
      },
      supportedSocialSource: ["GITHUB", "GITEE"]
    };
  },
  mounted() {
    this.getSystemConfig();
  },
  methods: {
    getSystemConfig: function() {
      getSystemConfig().then(resp => {
        this.title = resp.data.title;
        this.notice = resp.data.notice;
        this.socialPlatforms = resp.data.socialPlatforms;
      });
    },
    updateWebsiteConfig: function() {
      let param = {};
      param.title = this.title;
      param.notice = this.notice;
      updateWebsiteConfig(param).then(resp => {
        this.title = resp.data.title;
        this.notice = resp.data.notice;
        this.webSiteConfigVisible = false;
      });
    },
    conformSocial: function(value) {
      this.form.socialConfig.source = value;
      this.socialPicker.show = false;
    },

    editSocialConfig: function(param) {
      if (param) {
        this.form.socialConfig = param;
        this.saveSocialTitle = "编辑社交登录设置";
        this.socialPicker.readonly = true;
        this.socialPicker.title = "无法编辑社交平台";
        this.saveSocialConfigVisible = true;
      } else {
        this.socialPicker.title = "选择社交平台";
        this.saveSocialTitle = "新增社交登录设置";
        this.form.socialConfig = {
          source: "",
          clientId: "",
          clientSecret: "",
          redirectUri: "",
          admin: ""
        };
        this.saveSocialConfigVisible = true;
      }
    },
    saveSocialConfig: function() {
      saveSocialConfig(this.form.socialConfig).then(resp => {
        this.form.socialConfig.source = "";
        this.form.socialConfig.clientId = "";
        this.form.socialConfig.clientSecret = "";
        this.form.socialConfig.redirectUri = "";

        this.saveSocialConfigVisible = false;
        this.socialPicker.readonly = false;
        this.socialPlatforms = resp.data;
      });
    },
    delSocialConfig: function(source) {
      this.$dialog
        .confirm({
          title: "提示",
          message: "确定删除么?"
        })
        .then(() => {
          delSocialConfig(source).then(resp => {
            this.socialPlatforms = resp.data;
          });
        })
        .catch(() => {
          this.$dialog.close();
        });
    }
  }
};
</script>

<style scoped></style>
