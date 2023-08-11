import HttpRequest from '@/api/request'

const $TRAIN = new HttpRequest({
  baseURL: process.env.VUE_APP_TRAIN_API
})

export const Train = {
  // 工作单位
  unitList: params => $TRAIN.post(`/basic/lite/unit/appList`, params),
  // 打卡配置表 {"siteCode": ""}
  clockInfoConfig: params => $TRAIN.post(`/basic/clockInConfig/list`,params)
}

export const TrainDict = {
  // 字典
  list: params => $TRAIN.post(`/basic/dict/list`, params)
}