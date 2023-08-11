 const { $ADMIN } = require('@/api/config')

//  Uni Organization Controller
export const UniOrg = {
  info:params => $ADMIN.post(`uniOrg/info`,params),

  create: params => $ADMIN.post(`/uniOrg/add`, params),
  delete: params => $ADMIN.post(`/uniOrg/delete`, params),
  detail: params => $ADMIN.post(`/uniOrg/detail`, params),
  list: params => $ADMIN.post(`/uniOrg/list`, params),
  page: params => $ADMIN.post(`/uniOrg/page`, params),
  update: params => $ADMIN.post(`/uniOrg/update`, params),
  enabled: params => $ADMIN.post(`/uniOrg/enabled`, params),

  innerInfo: params => $ADMIN.post(`/uniOrg/inner/info`, params),
  innerList: params => $ADMIN.post(`/uniOrg/inner/list`, params),
  innerPage: params => $ADMIN.post(`/uniOrg/inner/page`, params),

  dictList: params => $ADMIN.post(`/uniOrg/dict/list`, params),
}