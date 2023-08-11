import store from '@/store'

const {$AUTH} = require('@/api/config')

// 模板
export const ModuleNameTemplate = {
  get: params => $AUTH.get(`get`, {params}),
  post: params => $AUTH.post(`post`, params),
  delete: params => $AUTH.delete(`delete`, {params}),
  put: params => $AUTH.put(`put`, params),
  excel: params => $AUTH.get(`post`, {params, responseType: 'arraybuffer'}) // 下载 excel 文件
}

// 登录
export const Token = {
  login: params => $AUTH.post(`login`, params),
  loginSms: params => $AUTH.post(`login-sms`, params),
  logout: params => $AUTH.post(`logout`, params),
  refresh: params => $AUTH.post(`refresh`, params),
  register: params => $AUTH.post(`register`, params),
  sendSms: params => $AUTH.post(`send-sms`, params),
  resetPassword: params => $AUTH.post(`reset-password`, params),
  // 微信登录
  loginSocial: params => $AUTH.post(`login-social`, {...params, appid: store.state.config.wxAppId}),
  // 绑定手机
  bindingSocial: params => $AUTH.post(`binding-social`, {...params, appid: store.state.config.wxAppId}),
  // 发送验证码
  sendSmsCheck: params => $AUTH.post(`send-sms-check`, params),
  // 获取UID
  oauth: params => $AUTH.post(`oauth`, {...params, appid: store.state.config.wxAppId}),
  // 获取图形验证码
  getCaptcha: params => $AUTH.post(`getCaptcha`, params, {responseType: 'arraybuffer'}),
  loginAccess: params => $AUTH.post(`login-access`, params)
}

// 腾讯云COS
export const ResourceCOS = {
  // 获取临时密钥
  cosCredential: params => $AUTH.post(`cosCredential`, params)
}
