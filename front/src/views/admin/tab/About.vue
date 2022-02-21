<template>
  <div style="padding: 16px ">
    <!-- 声明 -->
    <div>
      <van-divider
        :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }"
        >声明</van-divider
      >
      <ul>
        <li><p>如果您使用了本项目，代表您默认同意该声明。</p></li>
        <li>
          <p>
            项目和代码仅供参考学习，禁止商用和非法用途，否则，一切后果请用户自负。
          </p>
        </li>
      </ul>
    </div>

    <!-- 更新日志 -->
    <div class="block">
      <van-divider
        :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }"
        >更新日志</van-divider
      >
      <ul>
        <li>
          <div>
            <div class="timeline">2022-2-18 11:00</div>
            <div class="log-item">新增：短信获取验证码，复制CK</div>
          </div>
        </li>
        <li>
          <div>
            <div class="timeline">2022-2-20 20:00</div>
            <div class="log-item">新增：网站标题、公告定制化</div>
            <div class="log-item">新增：对接青龙，多青龙对接</div>
            <div class="log-item">新增：推送Cookie根据pt_key自动更新、启用</div>
            <div class="log-item">新增：后台登录，支持Gitee、Github登录</div>
            <div class="log-item">新增：后台青龙信息管理</div>
            <div class="log-item">新增：后台社交登录配置</div>
          </div>
        </li>
      </ul>
    </div>

    <div class="block">
      <van-divider
        :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }"
        >打赏开发者</van-divider
      >
      <div style="text-align: center; margin: 16px 0;color: #909399">
        <div>打赏开发者喝杯咖啡吧~</div>
        <van-button
          style="margin-top: 16px"
          color="linear-gradient(to right, #ff6034, #ee0a24)"
          @click="show = true"
          size="small"
        >
          打赏开发者
        </van-button>
      </div>

      <van-image-preview v-model="show" :images="images" :closeable="true">
      </van-image-preview>
    </div>
  </div>
</template>

<script>
export default {
  name: "About",
  data() {
    return {
      show: false,
      images: ["https://blog-bucket-cdn.yiidii.cn/donate.jpg"]
    };
  }
};
</script>

<style scoped>
.block {
  margin-top: 48px;
}

ul {
  margin-left: 16px;
}

li {
  margin-top: 16px;
}

.timeline {
  color: #909399;
}

.log-item {
  margin: 4px 0;
}
</style>
