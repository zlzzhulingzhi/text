const {$TRAIN_BASIC} = require('@/api/config')

// 查询字典表树  宿舍类型、单元、楼层、教室类型
export const QueryInfo = {
  info: params => $TRAIN_BASIC.post(`dict/tree/list`, params)
}