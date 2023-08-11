import HttpRequest from '@/api/request'

const $ADMIN = new HttpRequest({
  baseURL: process.env.VUE_APP_ADMIN_API
})

export const Member = {
  // 信息（学员）
  info: params => $ADMIN.post(`/uniMember/info`, params),
  // 机构信息
  orgInfo: params => $ADMIN.post(`/systemEmployeeUser/info`, params),
  // 平台信息
  adminInfo: params => $ADMIN.post(`/systemUser/info`, params),
  // 修改
  update: params => $ADMIN.post(`/uniMember/update`, params)
}