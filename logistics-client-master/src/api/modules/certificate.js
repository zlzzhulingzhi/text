const {$CERTIFICATE} = require('@/api/config')

// 证书
export const StandardTemplate = {
  // 分页
  page: params => $CERTIFICATE.post(`standardTemplate/page`, params),
  // 删除
  delete: params => $CERTIFICATE.post(`standardTemplate/delete`, params),
  // 查询可选配置
  searchConfig: params => $CERTIFICATE.post(`standardTemplate/searchConfig`, params || {}),
  // 查询模板可选配置
  templateConfig: params => $CERTIFICATE.post(`standardTemplate/templateConfig`, params || {}),
  // 添加
  create: params => $CERTIFICATE.post(`standardTemplate/add`, params),
  // 启用禁用
  batchEnabled: params => $CERTIFICATE.post(`standardTemplate/batch-enabled`, params),
  // 详情
  detail: params => $CERTIFICATE.post(`standardTemplate/detail`, params),
  // 预览
  preview: params => $CERTIFICATE.post(`standardTemplate/preview`, params),
  // 查询项详情
  detailSearchConfig: params => $CERTIFICATE.post(`standardTemplate/detail/searchConfig`, params),
  // 修改
  update: params => $CERTIFICATE.post(`standardTemplate/update`, params),
  // 修改查询项
  updateSearchConfig: params => $CERTIFICATE.post(`standardTemplate/update/searchConfig`, params)
}

export const Cert = {
  // 考试分页
  examPage: params => $CERTIFICATE.post(`cert/exam/page`, params),
  // 考试用户列表
  examUserList: params => $CERTIFICATE.post(`cert/exam-user/list`, params),
  // 用户列表
  userPage: params => $CERTIFICATE.post(`cert/user/page`, params),
  // 学员列表
  studentPage: params => $CERTIFICATE.post(`cert/student/page`, params),
  // 分页
  page: params => $CERTIFICATE.post(`cert/page`, params),
  // 发放证书用户分页
  awardPage: params => $CERTIFICATE.post(`cert/award/page`, params),
  // 发放证书
  award: params => $CERTIFICATE.post(`cert/award`, params),
  // 证书重新发布
  awardRetry: params => $CERTIFICATE.post(`cert/award-retry`, params),
  // 添加
  create: params => $CERTIFICATE.post(`cert/add`, params),
  // 删除
  delete: params => $CERTIFICATE.post(`cert/delete`, params),
  // 修改
  update: params => $CERTIFICATE.post(`cert/update`, params),
  // 详情
  detail: params => $CERTIFICATE.post(`cert/detail`, params),
  // 启用禁用
  batchEnabled: params => $CERTIFICATE.post(`cert/batch-enabled`, params),
  // 证书记录 启用禁用
  awardBatchEnabled: params => $CERTIFICATE.post(`cert/award/batch-enabled`, params)
}