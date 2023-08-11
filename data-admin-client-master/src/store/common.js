/**
 * 字典 管理
 */
import Vue from 'vue'

const state = {
  context: null,

  // 通知标签
  noticeLabel: [
    { id: 'new', name: '最新' },
    { id: 'urgent', name: '紧急' }
  ],
  // 
  display: [
    { id: 1, name: '展示', status: 'success' },
    { id: 0, name: '隐藏', status: 'error' }
  ],
}

const getters = {}
for (const key in state) {
  getters[key] = function(state) {
    let v = state[key]
    if (v && v.hasOwnProperty('api')) {
      setTimeout(() => {
        state.context.dispatch('common/getPublicData', key, {root: true})
      }, 30)
      return []
    }
    return v
  }
}


const mutations = {
  setList: (state, {keys, values}) => {
    values.forEach(({data}, i) => {
      // 个例处理
      let key = keys[i]
      let apiObj = state[key]

      if (!data) return false

      if (!Array.prototype.isPrototypeOf(data)) {
        data = data.menu || data.records || data.deptList || []
      }

      if (!data.length) return false

      // 字典处理（树）
      if (data[0].dictKey) {
        data = data[0].children.map(item => {
          let id = Number(item.dictKey)
          if (isNaN(id)) id = item.dictKey
          return {
            ...item,
            id,
            name: item.dictValue
          }
        })
      }

      // 赋值
      state[key] = data

      // 重复请求 处理
      if (apiObj.hasOwnProperty('api')) {
        state[key].__proto__ = {
          __proto__: state[key].__proto__,
          ...apiObj
        }
      } else {
        state[key].__proto__ = apiObj.__proto__
      }
    })
  },
  saveContext(state, context) {
    state.context = context
  }
}


const actions = {
  async getDictionary(ctx, updateNameKeys) {
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
  saveContext(ctx, that) {
    ctx.commit('saveContext', that)
  },
  async getPublicData(ctx, updateNameKeys) {
    await ctx.dispatch('getDictionary', updateNameKeys)
  },
  createFilter({getters}) {
    for (const k in getters) {
      Vue.filter(k, function(id, name = 'name', defaultValue = '-', keyName = 'id') {
        let mapsArr = getters[k]
        if (mapsArr.find) {
          let item = mapsArr.find(v => v[keyName] == id)
          if (item) return item[name]
        }
        return defaultValue
      })
    }
  }
}


export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}