const {QuestionBasket} = require('@/api')

/**
 * paper 试卷
 */
const state = {
  maxQuestionNum: 999,
  // 试卷题目ID集合
  paperQuestionList: [],
  paperQuestionTypeList: []
}


const getters = {
  paperQuestionList(state) {
    return state.paperQuestionList
  },
  paperQuestionTypeList(state) {
    return state.paperQuestionTypeList
  }
}


const mutations = {
  paperQuestionListUpdate: (state, d) => state.paperQuestionList = d,
  paperQuestionTypeListUpdate: (state, d) => state.paperQuestionTypeList = d
}


const actions = {
  // 操作 - 添加试题
  async add(ctx, d) {
    let list = (d.forEach ? d : [d]).map(item => item.id || item)
    let {code} = await QuestionBasket.add({
      questionIdList: list
    })
    if (code !== 200) return false
    ctx.dispatch('getPaperQuestionList')
  },
  // 操作 - 移除试题
  async remove(ctx, d) {
    let list = (d.forEach ? d : [d]).map(item => item.id || item)
    let {code} = await QuestionBasket.remove({
      questionIdList: list
    })
    if (code !== 200) return false
    ctx.dispatch('getPaperQuestionList')
  },
  // 操作 - 清空试题
  async clear({commit}) {
    let {code} = await QuestionBasket.empty()
    if (code !== 200) return false
    commit('paperQuestionListUpdate', [])
    commit('paperQuestionTypeListUpdate', [])
  },
  // 操作 - 获取试题篮数据
  async getPaperQuestionList({commit}) {
    let {code, data} = await QuestionBasket.questionIdList()
    if (code !== 200) return false
    commit('paperQuestionListUpdate', data)

    let {code: c, data: d} = await QuestionBasket.groupCount()
    if (c !== 200) return false
    commit('paperQuestionTypeListUpdate', d)
  }
}


export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}