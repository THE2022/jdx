const Timestamp = new Date().getTime();
module.exports = {
  publicPath: process.env.NODE_ENV === "production" ? "/" : "/",
  outputDir: "dist",
  assetsDir: "static",
  filenameHashing: false,
  productionSourceMap: false,
  devServer: {
    host: "0.0.0.0",
    port: 8080, // 端口号
    https: false,
    open: false,
    disableHostCheck: true,

    // 配置多个代理
    proxy: {
      "/api": {
        target: "http://127.0.0.1:8090", // 要访问的接口域名
        ws: true, // 是否启用websockets
        changeOrigin: true, //开启代理：在本地会创建一个虚拟服务端，然后发送请求的数据，并同时接收请求的数据，这样服务端和服务端进行数据的交互就不会有跨域问题
        pathRewrite: {
          "^/api": "" //这里理解成用'/api'代替target里面的地址,比如我要调用'http://40.00.100.100:3002/user/add'，直接写'/api/user/add'即可
        }
      }
    }
  },
  configureWebpack: {
    output: {
      filename: `js/[name].${Timestamp}.js`,
      chunkFilename: `js/[name].${Timestamp}.js`
    }
  },
  css: {
    //重点.
    extract: {
      // 打包后css文件名称添加时间戳
      filename: `css/[name].${Timestamp}.css`,
      chunkFilename: `css/[name].${Timestamp}.css`
    }
  }
};
