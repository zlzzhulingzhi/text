const {$ORDER} = require('@/api/config')

export const OrderManage = {
  // 分页
  page: params => $ORDER.post(`/manage/page`, params),
  // 分页（平台）
  pageAdmin: params => $ORDER.post(`/manage/admin/page`, params),
  // 详情
  detail: params => $ORDER.post(`/manage/detail`, params),
  // 详情（平台）
  pageDetail: params => $ORDER.post(`/manage/admin/detail`, params),
  // 删除
  delete: params => $ORDER.post(`/manage/delete`, params),
  // 退课记录
  refundPage: params => $ORDER.post(`/manage/refund/page`, params),
  // 退课记录（平台）
  refundPageAdmin: params => $ORDER.post(`/manage/admin/refund/page`, params)
}

export const OrderRefund = {
  // 退课
  refund: params => $ORDER.post(`/refund`, params)
}