import HttpRequest from '@/api/request'

const $AUTH = new HttpRequest({
  baseURL: process.env.VUE_APP_AUTH_API
})

export const Auth = {
  // 微信用户信息
  wxInfo: params => $AUTH.post(`/wc/miniapp-info`, params),
  // 微信手机号
  wxPhone: params => $AUTH.post(`/wc/miniapp-phone`, params),
  // 微信登录
  wxLogin: params => $AUTH.post(`/member/login-miniapp`, params),
  // 绑定手机
  binding: params => $AUTH.post(`/member/binding-miniapp`, params)
}

export const AuthAdmin = {
  // 发送验证码
  sendSmsCheck: params => $AUTH.post(`/index/send-sms-check`, params),
  // 绑定
  binding: params => $AUTH.post(`/index/binding-miniapp`, params),
  // 登录
  login: params => $AUTH.post(`/index/login-miniapp`, params)
}