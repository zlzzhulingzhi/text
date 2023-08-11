import store from '@/store'
const { $BASIC } = require('@/api/config')

// 模板
export const ModuleNameTemplate = {
  get: params => $BASIC.get(`get`, params),
  post: params => $BASIC.post(`post`, params),
  delete: params => $BASIC.delete(`delete`, params),
  put: params => $BASIC.put(`put`, params),
  excel: params => $BASIC.get(`post`, { params, responseType: 'arraybuffer' }) // 下载 excel 文件
}


// 字典管理 Dict Controller
export const Dict = {
  treeList: params => $BASIC.post(`dict/tree/list`, params),
  childrenList: params => $BASIC.post(`dict/children/list`, params),
  batchEnabled: params => $BASIC.post(`dict/batch-enabled`, params),
  create: params => $BASIC.post(`dict/add`, params),
  delete: params => $BASIC.post(`dict/delete`, params),
  update: params => $BASIC.post(`dict/update`, params),
  detail: params => $BASIC.post(`dict/detail`, params),
  page: params => $BASIC.post(`dict/page`, params)
}

// 应用管理 Application Controller
export const Application = {
  page: params => $BASIC.post(`app/page`, params),
  list: params => $BASIC.post(`app/list`, params),
  batchEnabled: params => $BASIC.post(`app/batch-enabled`, params),
  create: params => $BASIC.post(`app/add`, params),
  update: params => $BASIC.post(`app/update`, params),
  delete: params => $BASIC.post(`app/delete`, params),
  detail: params => $BASIC.post(`app/detail`, params)
}


// 应用类型 Application Type Controller
export const ApplicationType = {
  page: params => $BASIC.post(`applicationType/page`, params),
  list: params => $BASIC.post(`applicationType/list`, params),
  create: params => $BASIC.post(`applicationType/add`, params),
  update: params => $BASIC.post(`applicationType/update`, params),
  delete: params => $BASIC.post(`applicationType/delete`, params),
  detail: params => $BASIC.post(`applicationType/detail`, params)
}


// 用户管理 User Controller
export const User = {
  page: params => $BASIC.post(`user/page`, params),
  batchEnabled: params => $BASIC.post(`user/batch-enabled`, params),
  create: params => $BASIC.post(`user/add`, params),
  update: params => $BASIC.post(`user/update`, params),
  delete: params => $BASIC.post(`user/delete`, params),
  detail: params => $BASIC.post(`user/detail`, params),
  resetPasswordAdmin: params => $BASIC.post(`user/admin/reset-password`, params),
  resetPassword: params => $BASIC.post(`user/reset-password`, params),
  loginPermission: params => $BASIC.post(`user/login-permission`, params),
  getUserInfo: params => $BASIC.post(`user/login-info`, params)
}


// 菜单管理 Menu Controller
export const Menu = {
  page: params => $BASIC.post(`menu/page`, params),
  batchEnabled: params => $BASIC.post(`menu/batch-enabled`, params),
  create: params => $BASIC.post(`menu/add`, params),
  update: params => $BASIC.post(`menu/update`, params),
  delete: params => $BASIC.post(`menu/delete`, params),
  detail: params => $BASIC.post(`menu/detail`, params),
  treeTotal: params => $BASIC.post(`menu/tree-total`, params)
}


// 角色管理 Role Controller
export const Role = {
  page: params => $BASIC.post(`role/page`, params),
  list: params => $BASIC.post(`role/list`, params),
  batchEnabled: params => $BASIC.post(`role/batch-enabled`, params),
  create: params => $BASIC.post(`role/add`, params),
  update: params => $BASIC.post(`role/update`, params),
  delete: params => $BASIC.post(`role/delete`, params),
  detail: params => $BASIC.post(`role/detail`, params),
  adminRolePermission: params => $BASIC.post(`role/admin-role-permission`, params),
  orgRolePermission: params => $BASIC.post(`role/org-role-permission`, params)
}


// 导航项管理 Navigation Item Controller
export const NavigationItem = {
  create: params => $BASIC.post(`navigation/add`, params),
  detail: params => $BASIC.post(`navigation/detail`, params),
  list: params => $BASIC.post(`navigation/list`, params),
  page: params => $BASIC.post(`navigation/page`, params),
  update: params => $BASIC.post(`navigation/update`, params)
}

// 微信授权
export const UserOauth = {
  // 授权信息
  info: params => $BASIC.post(`userOauth/info`, params),
  // 解绑微信
  unbindSocial: params => $BASIC.post(`userOauth/unbind-social`, { ...params, appid: store.state.config.wxAppId }),
  // 绑定微信
  bindingSocial: params => $BASIC.post(`userOauth/binding-social`, { ...params, appid: store.state.config.wxAppId })
}