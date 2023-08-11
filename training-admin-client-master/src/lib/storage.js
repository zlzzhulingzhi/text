import {Cookie, Local, Session} from './replaceStorage'

window.FirstName = 'trainingAdmin'

// 机构ID
export const OrgId = new Local('OrgId')

// 微信绑定信息
export const WechatCode = new Local('WechatCode')

// 用户信息
export const UserInfo = new Local('UserInfo')

// 路由历史队列
export const RouteQueue = new Local('RouteQueue')

// 短信验证码时间戳
export const SmsTimeStamp = new Local('SmsTimeStamp')

// 权限缓存
export const PermissionMenuCache = new Local('PermissionMenuCache')

// 试卷
export const PaperCache = new Local('PaperCache')
