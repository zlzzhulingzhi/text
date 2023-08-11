import {Cookie, Local, Session} from './replaceStorage'

window.FirstName = 'dataAdmin'

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

// 权限缓存
export const PermissionMenuCache = new Local('PermissionMenuCache')

// 试卷
export const PaperCache = new Local('PaperCache')

export const ClassroomManageData = new Local('ClassroomManageData')
export const DormitoryData = new Local('DormitoryData')
export const DormitoryApplyData = new Local('DormitoryApplyData')
export const QualificationReviewData = new Local('QualificationReviewData')
export const ApplyCheckData = new Local('ApplyCheckData')
export const ClassroomOrderInfoData = new Local('ClassroomOrderInfoData')
export const ClassroomInfoData = new Local('ClassroomInfoData')
export const applyStatisticsData = new Session('applyStatisticsData')
export const moneyStatisticsData = new Session('moneyStatisticsData')
export const orgStatisticsData = new Session('orgStatisticsData')
export const ApplyAuditData = new Session('ApplyAuditData')
export const ApplyPubData = new Session('ApplyPubData')
export const ExpertInfoData = new Session('ExpertInfoData')

