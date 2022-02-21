import request from "@/util/request";

// 网站基本信息
export function baseInfo() {
  return request({
    url: "info",
    method: "get"
  });
}

// 发送验证码
export function jdSmsCode(mobile) {
  return request({
    url: "jd/smsCode?mobile=" + mobile,
    method: "get"
  });
}

// jd登录
export function jdLogin(data) {
  return request({
    url: "jd/login",
    method: "post",
    data: data
  });
}
