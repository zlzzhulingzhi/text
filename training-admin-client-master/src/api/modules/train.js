const {$TRAIN} = require('@/api/config')
const {$UADMIN} = require('@/api/config')

// 单位管理模块
// 单位管理
export const Unit = {
  // 新增单位
  add: params => $TRAIN.post(`unit/add`, params),
  // 删除单位
  delete: params => $TRAIN.post(`unit/delete`, params),
  // 单位详情
  detail: params => $TRAIN.post(`unit/detail`, params),
  // 查询单位列表
  list: params => $TRAIN.post(`unit/list`, params),
  // 分页查询单位
  page: params => $TRAIN.post(`unit/page`, params),
  // 更新单位
  update: params => $TRAIN.post(`unit/update`, params),
  // 批量更新单位
  updateBatch: params => $TRAIN.post(`unit/updateBatch`, params),


  // 单位职员 分页
  userPage: params => $UADMIN.post(`uniMember/page`, params),
}