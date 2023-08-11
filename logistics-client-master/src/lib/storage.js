import { Cookie, Local, Session } from './replaceStorage'

window.FirstName = 'logisticsOrg'

// 机构ID
export const OrgId = new Local('OrgId')

// 微信绑定信息
export const WechatCode = new Local('WechatCode')

// 登录令牌
export const Token = new Cookie('Token', 168)

// 用户信息
export const UserInfo = new Local('UserInfo')

// 路由历史队列
export const RouteQueue = new Local('RouteQueue')

// 短信验证码时间戳
export const SmsTimeStamp = new Local('SmsTimeStamp')
export const SmsTimeStampBind = new Local('SmsTimeStampBind')

// 权限缓存
export const PermissionMenuCache = new Local('PermissionMenuCache')

// 试卷
export const PaperCache = new Local('PaperCache')

