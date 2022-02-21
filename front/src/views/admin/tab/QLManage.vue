<template>
  <div>
    <div style="margin: 16px 0 8px 20px">
      <van-popover
        v-model="showPopover"
        trigger="click"
        :actions="actions"
        @select="showAddQLConfig()"
      >
        <template #reference>
          <van-button type="primary" icon="apps-o" size="small"
            >操作</van-button
          >
        </template>
      </van-popover>
    </div>

    <van-empty v-if="qlConfigList.length == 0" description="暂无配置" />

    <van-swipe-cell
      v-for="qlConfig in qlConfigList"
      :key="qlConfig.displayName"
      :title="qlConfig.displayName"
      style="margin-bottom: 20px"
    >
      <van-cell-group :title="qlConfig.displayName" inset>
        <van-cell title="地址">
          <template #default>
            {{ qlConfig.url }}
          </template>
        </van-cell>
        <van-cell title="客户端ID">
          <template #default>
            {{ qlConfig.clientId }}
          </template>
        </van-cell>
        <van-cell title="客户端密钥">
          <template #default>
            {{ qlConfig.clientSecret }}
          </template>
        </van-cell>
      </van-cell-group>
      <template #right>
        <van-button
          square
          text="删除"
          type="danger"
          class="slide-button"
          @click="delQLConfig(qlConfig.displayName)"
        />
      </template>
    </van-swipe-cell>

    <van-action-sheet v-model="addVisible" title="新增配置">
      <van-form>
        <van-field
          v-model="form.displayName"
          name="displayName"
          label="显示名称"
          placeholder="显示名称"
        />
        <van-field
          v-model="form.url"
          name="url"
          label="地址"
          placeholder="http://127.0.0.1:5701/"
        />
        <van-field
          v-model="form.clientId"
          name="clientId"
          label="客户端ID"
          placeholder="客户端ID"
        />
        <van-field
          v-model="form.clientSecret"
          name="clientSecret"
          label="客户端密钥"
          placeholder="客户端密钥"
        />
        <div style="margin: 16px;">
          <van-button round block type="info" @click="addQLConfig()"
            >提交
          </van-button>
        </div>
      </van-form>
    </van-action-sheet>
  </div>
</template>

<script>
import { getQLConfigList, delQLConfig, addQLConfig } from "@/api/admin";

export default {
  name: "QLManage",
  data() {
    return {
      qlConfigList: [{ displayName: "", clientId: "", clientSecret: "" }],
      showPopover: false,
      actions: [{ text: "新增配置" }],
      addVisible: false,
      form: { displayName: "", clientId: "", clientSecret: "" }
    };
  },
  mounted() {
    this.getQLConfigList();
  },
  methods: {
    getQLConfigList: function() {
      getQLConfigList().then(resp => {
        this.qlConfigList = resp.data;
      });
    },
    delQLConfig: function(displayName) {
      this.$dialog
        .confirm({
          title: "提示",
          message: "确定删除么?"
        })
        .then(() => {
          delQLConfig(displayName).then(resp => {
            this.qlConfigList = resp.data;
          });
        })
        .catch(() => {
          this.$dialog.close();
        });
    },
    showAddQLConfig: function() {
      this.form = { displayName: "", clientId: "", clientSecret: "" };
      this.addVisible = true;
    },
    addQLConfig: function() {
      addQLConfig(this.form).then(resp => {
        this.qlConfigList = resp.data;
        this.form = { displayName: "", clientId: "", clientSecret: "" };
        this.addVisible = false;
      });
    }
  }
};
</script>

<style>
.slide-button {
  height: 100%;
}
</style>
