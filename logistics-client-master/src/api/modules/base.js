import store from '@/store'
const {$TRAIN_BASIC} = require('@/api/config')

// 模板
export const ModuleNameTemplate = {
  get: params => $TRAIN_BASIC.get(`get`, params),
  post: params => $TRAIN_BASIC.post(`post`, params),
  delete: params => $TRAIN_BASIC.delete(`delete`, params),
  put: params => $TRAIN_BASIC.put(`put`, params),
  excel: params => $TRAIN_BASIC.get(`post`, {params, responseType: 'arraybuffer'}) // 下载 excel 文件
}


// 字典管理 Dict Controller
export const Dict = {
  treeList: params => $TRAIN_BASIC.post(`dict/tree/list`, params),
  childrenList: params => $TRAIN_BASIC.post(`dict/children/list`, params),
  batchEnabled: params => $TRAIN_BASIC.post(`dict/batch-enabled`, params),
  create: params => $TRAIN_BASIC.post(`dict/add`, params),
  delete: params => $TRAIN_BASIC.post(`dict/delete`, params),
  update: params => $TRAIN_BASIC.post(`dict/update`, params),
  detail: params => $TRAIN_BASIC.post(`dict/detail`, params),
  page: params => $TRAIN_BASIC.post(`dict/page`, params)
}

// 应用管理 Application Controller
export const Application = {
  page: params => $TRAIN_BASIC.post(`app/page`, params),
  list: params => $TRAIN_BASIC.post(`app/list`, params),
  batchEnabled: params => $TRAIN_BASIC.post(`app/batch-enabled`, params),
  create: params => $TRAIN_BASIC.post(`app/add`, params),
  update: params => $TRAIN_BASIC.post(`app/update`, params),
  delete: params => $TRAIN_BASIC.post(`app/delete`, params),
  detail: params => $TRAIN_BASIC.post(`app/detail`, params)
}


// 应用类型 Application Type Controller
export const ApplicationType = {
  page: params => $TRAIN_BASIC.post(`applicationType/page`, params),
  list: params => $TRAIN_BASIC.post(`applicationType/list`, params),
  create: params => $TRAIN_BASIC.post(`applicationType/add`, params),
  update: params => $TRAIN_BASIC.post(`applicationType/update`, params),
  delete: params => $TRAIN_BASIC.post(`applicationType/delete`, params),
  detail: params => $TRAIN_BASIC.post(`applicationType/detail`, params)
}


// 用户管理 User Controller
export const User = {
  page: params => $TRAIN_BASIC.post(`user/page`, params),
  batchEnabled: params => $TRAIN_BASIC.post(`user/batch-enabled`, params),
  create: params => $TRAIN_BASIC.post(`user/add`, params),
  update: params => $TRAIN_BASIC.post(`user/update`, params),
  delete: params => $TRAIN_BASIC.post(`user/delete`, params),
  detail: params => $TRAIN_BASIC.post(`user/detail`, params),
  resetPasswordAdmin: params => $TRAIN_BASIC.post(`user/admin/reset-password`, params),
  resetPassword: params => $TRAIN_BASIC.post(`user/reset-password`, params),
  loginPermission: params => $TRAIN_BASIC.post(`user/login-permission`, params),
  getUserInfo: params => $TRAIN_BASIC.post(`user/login-info`, params)
}


// 菜单管理 Menu Controller
export const Menu = {
  page: params => $TRAIN_BASIC.post(`menu/page`, params),
  batchEnabled: params => $TRAIN_BASIC.post(`menu/batch-enabled`, params),
  create: params => $TRAIN_BASIC.post(`menu/add`, params),
  update: params => $TRAIN_BASIC.post(`menu/update`, params),
  delete: params => $TRAIN_BASIC.post(`menu/delete`, params),
  detail: params => $TRAIN_BASIC.post(`menu/detail`, params),
  treeTotal: params => $TRAIN_BASIC.post(`menu/tree-total`, params)
}


// 角色管理 Role Controller
export const Role = {
  page: params => $TRAIN_BASIC.post(`role/page`, params),
  list: params => $TRAIN_BASIC.post(`role/list`, params),
  batchEnabled: params => $TRAIN_BASIC.post(`role/batch-enabled`, params),
  create: params => $TRAIN_BASIC.post(`role/add`, params),
  update: params => $TRAIN_BASIC.post(`role/update`, params),
  delete: params => $TRAIN_BASIC.post(`role/delete`, params),
  detail: params => $TRAIN_BASIC.post(`role/detail`, params),
  adminRolePermission: params => $TRAIN_BASIC.post(`role/admin-role-permission`, params),
  orgRolePermission: params => $TRAIN_BASIC.post(`role/org-role-permission`, params)
}


// 导航项管理 Navigation Item Controller
export const NavigationItem = {
  create: params => $TRAIN_BASIC.post(`navigation/add`, params),
  detail: params => $TRAIN_BASIC.post(`navigation/detail`, params),
  list: params => $TRAIN_BASIC.post(`navigation/list`, params),
  page: params => $TRAIN_BASIC.post(`navigation/page`, params),
  update: params => $TRAIN_BASIC.post(`navigation/update`, params)
}

// 微信授权
export const UserOauth = {
  // 授权信息
  info: params => $TRAIN_BASIC.post(`userOauth/info`, params),
  // 解绑微信
  unbindSocial: params => $TRAIN_BASIC.post(`userOauth/unbind-social`, { ...params, appid: store.state.config.wxAppId }),
  // 绑定微信
  bindingSocial: params => $TRAIN_BASIC.post(`userOauth/binding-social`, { ...params, appid: store.state.config.wxAppId })
}