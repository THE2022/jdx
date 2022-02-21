import request from "@/util/request";

// 登录
export function login(data) {
  return request({
    url: "oauth/login",
    method: "post",
    data: data
  });
}

// 青龙列表
export function getQLConfigList() {
  return request({
    url: "admin/ql",
    method: "get"
  });
}

// 删除青龙配置
export function delQLConfig(displayName) {
  return request({
    url: "admin/ql?displayName=" + displayName,
    method: "delete"
  });
}

// 新增青龙配置
export function addQLConfig(data) {
  return request({
    url: "admin/ql",
    method: "post",
    data: data
  });
}

// 系统配置
export function getSystemConfig() {
  return request({
    url: "admin/config",
    method: "get"
  });
}

// 更新系统配置
export function updateWebsiteConfig(data) {
  return request({
    url: "admin/websiteConfig",
    method: "put",
    data: data
  });
}

// 保存社交配置(新增/编辑)
export function saveSocialConfig(data) {
  return request({
    url: "admin/socialConfig",
    method: "post",
    data: data
  });
}

// 删除社交配置
export function delSocialConfig(source) {
  return request({
    url: "admin/socialConfig?source=" + source,
    method: "delete"
  });
}
