import Vue from 'vue'
import { Dict, Train, TrainDict, WorkOrder,SignRecord } from '@/api'
import {SignTime} from '@/lib/storage'

const state = {
  context: null,
  // 身份
  idList: [
    { id: 'student', name: '学员' },
    { id: 'org', name: '机构' },
    { id: 'admin', name: '平台' }
  ],
  // 课程状态
  courseStatus: [
    { id: 1, name: '学习中' },
    { id: 2, name: '已学习' }
  ],
  // 分享页面类型
  shareTypeMap: {
    COURSE: 1,
    MICRO: 2,
    BARGAIN: 3,
    GROUPON: 4,
    SHOP: 5
  },
  // 性别
  sexList: [
    { id: 1, name: '男' },
    { id: 2, name: '女' }
  ],
  // 星期
  weekList: [
    { id: 'MONDAY', name: '周一' },
    { id: 'TUESDAY', name: '周二' },
    { id: 'WEDNESDAY', name: '周三' },
    { id: 'THURSDAY', name: '周四' },
    { id: 'FRIDAY', name: '周五' },
    { id: 'SATURDAY', name: '周六' },
    { id: 'SUNDAY', name: '周日' }
  ],
  // 类别(1-开课、2-维修、3-更换、4-结课
  workOrderList: [
    { id: 1, name: '开课' },
    { id: 2, name: '维修' },
    { id: 3, name: '更换' },
    { id: 4, name: '结课' }
  ],
  // 访客记录审核状态
  visitStatus: [
    { id: -1, name: '审核中' },
    { id: 0, name: '审核不通过' },
    { id: 1, name: '审核通过' }
  ],
  // 学历
  eduList: { api: Dict.list, params: { code: 'education' } },
  // 民族
  nationList: { api: Dict.list, params: { code: 'nation' } },
  // 工作单位
  unitList: { api: Train.unitList, params: {} },
  // 单元
  buildingList: { api: TrainDict.list, params: { code: 'building' } },
  // 楼层
  floorList: { api: TrainDict.list, params: { code: 'floor' } },
  // 教室类型
  classroomList: { api: TrainDict.list, params: { code: 'classroom' } },
  // 宿舍类型
  dormitoryList: { api: TrainDict.list, params: { code: 'dormitory' } },
  // 教务机构列表
  orgList: { api: WorkOrder.orgList, params: {} },
  // 可选班级列表
  selectClazzList: {api: SignRecord.selectClazzList,params: {}},
  // 班级id
  clazzId: 0,
  // 记录打卡时间
  signTime:SignTime.get() || null
}

const getters = {}
for (const key in state) {
  getters[key] = function (state) {
    let v = state[key]
    if (v && v.hasOwnProperty('api')) {
      setTimeout(() => {
        state.context.dispatch('common/getPublicData', key, { roote: true })
      }, 30)
      return []
    }
    return v
  }
}

const mutations = {
  saveContext: (state, context) => state.context = context,
  setList: (state, { keys, values }) => {
    values.forEach(({ data }, i) => {
      if (!data) return false
      let key = keys[i]
      // 字典处理
      if (data[0].dictKey) {
        data = data.map(item => {
          return {
            id: item.dictKey,
            name: item.dictValue
          }
        })
      }
      state[key] = data

      // 重复请求处理
      let apiObj = state[key]
      if (apiObj.hasOwnProperty('api')) {
        state[key].__proto__ = {
          __proto__: state[key].__proto__,
          apiObj
        }
      } else {
        state[key].__proto__ = apiObj.__proto__
      }
    })
  },
  saveClazzId: (state,clazzId) => state.clazzId = clazzId,
  updateSignTime: (state,signTime) => {
    state.signTime = signTime
    if(signTime) {
      SignTime.set(signTime)
    } else {
      SignTime.remove()
    }
  },
  updateClazzList: (state,clazzList) => state.selectClazzList = clazzList
}

const actions = {
  saveContext(ctx, that) {
    ctx.commit('saveContext', that)
  },
  createFilter({ getters }) {
    // console.log('getters == ', getters);
    for (const key in getters) {
      Vue.filter(key, function (id, name = 'name', defaultValue = '-', keyName = 'id') {
        let mapArray = getters[key]
        if (mapArray.find) {
          let item = mapArray.find(v => v[keyName] == id)
          if (item) return item[name]
        }
        return defaultValue
      })
    }
  },
  async getPublicData(ctx, updateNameKeys) {
    let maps = {}
    for (const stateKey in ctx.state) {
      let v = ctx.state[stateKey]
      if (!updateNameKeys || updateNameKeys.includes(stateKey)) maps[stateKey] = v.api(v.params || {})
    }
    let values = await Promise.all(Object.values(maps))
    ctx.commit('setList', {
      keys: Object.keys(maps),
      values
    })
  },
  saveClazzId(ctx,clazzId) {
    ctx.commit('saveClazzId',clazzId)
  },
  saveSignTime(ctx,signTime) {
    ctx.commit('updateSignTime',signTime)
  },
  updateClazzList(ctx,clazzList) {
    ctx.commit('updateClazzList',clazzList)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}