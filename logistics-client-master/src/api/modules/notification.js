const {$NOTIFICATION} = require('@/api/config')

//通知 站内信
export const Notification = {
  //分页
  page: params => $NOTIFICATION.post('/notification/page', params),
  // 详情
  detail: params => $NOTIFICATION.post('/notification/detail', params),
  //发布前获取学员信息
  getStudentList: params => $NOTIFICATION.post('/notification/getStudentList', params),
  //发布通知
  issue: params => $NOTIFICATION.post('/notification/issue', params)
}