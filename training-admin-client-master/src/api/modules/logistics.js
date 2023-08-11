const {$LOGISTICS} = require('@/api/config')
// 远程服务统一请求接口 Remote Controller
export const Remote = {
  orgPage: params => $LOGISTICS.post(`/remote/org/page`, params)
}