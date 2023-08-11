import store from '@/store'
import {Session} from 'knight-storage'

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
  loginAccess: params => $AUTH.post(`login-access`, params),
  // loginAccess: params => {
  //   return {
  //     code: 200,
  //     data: {
  //       access_token: 'eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyX3R5cGUiOiJlbXBsb3llZSIsInVzZXJfaWQiOjExNiwidXNlcl9rZXkiOiIwMjIxMzFiNy02MzRkLTQyNmEtOTdlMi02ZjFiZjNhNzMyN2UiLCJ1c2VybmFtZSI6bnVsbH0.g71EemqU1BXz5o5jMMyk-izZb3etIIVEwkrlmvSFfSWj_DNSBugvSeMYPV_XtOBl-3qqmtkvywCFI34orcsZdg',
  //       "isAdmin": true,
  //       "userId": 116,
  //       "roles": [
  //         "机构管理员"
  //       ],
  //       "realName": "网安演示",
  //       "userAccount": "13812345678",
  //       "expiresIn": 10080,
  //       "firstLogin": false,
  //       "headImgUrl": null,
  //       "orgId": 118,
  //       "pcLogoUrl": "//seczone-teach-test-1312651338.cos.ap-guangzhou.myqcloud.com/00/2022-09-14/d168566bafa7de26a87e3832d229a230.png",
  //       "h5LogoUrl": null,
  //       "orgName": "网安培训中心"
  //     }
  //   }
  // },
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
  getCaptcha: params => $AUTH.post(`getCaptcha`, params, {responseType: 'arraybuffer'})
}

// 腾讯云COS
export const ResourceCOS = {
  // 获取临时密钥
  cosCredential: params => $AUTH.post(`cosCredential`, params)
}