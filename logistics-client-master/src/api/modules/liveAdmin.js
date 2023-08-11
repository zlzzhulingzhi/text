const {$LIVE_ADMIN} = require('@/api/config')

// 直播场次管理 Basic Live Show Controller
export const BasicLiveShow = {
  create: params => $LIVE_ADMIN.post(`basicLiveShow/add`, params),
  update: params => $LIVE_ADMIN.post(`basicLiveShow/update`, params),
  info: params => $LIVE_ADMIN.post(`basicLiveShow/info`, params),
  changeStatus: params => $LIVE_ADMIN.post(`basicLiveShow/change-status`, params)
}


// 直播管理 Basic Live Controller
export const BasicLive = {
  create: params => $LIVE_ADMIN.post(`basicLive/add`, params),
  delete: params => $LIVE_ADMIN.post(`basicLive/delete`, params),
  detail: params => $LIVE_ADMIN.post(`basicLive/detail`, params),
  list: params => $LIVE_ADMIN.post(`basicLive/list`, params),
  page: params => $LIVE_ADMIN.post(`basicLive/page`, params),
  update: params => $LIVE_ADMIN.post(`basicLive/update`, params)
}


// 统一对外接口 Common Controller
export const LiveCommon = {
  customUserSig: params => $LIVE_ADMIN.post(`common/customUserSig`, params),
  userSig: params => $LIVE_ADMIN.post(`common/userSig`, params)
}