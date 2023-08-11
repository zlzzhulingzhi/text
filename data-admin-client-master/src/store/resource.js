const { ResourceFile } = require('@/api')

const state = {
  credential: {}
}

const getters = {}

const mutations = {
  setKeysAction (state, credential) {
    state.credential = credential
  }
}

const actions = {
  // async getHuaweiyunKeys(ctx) {
  //   let {code, data} = await ResourceFile.temporary({})
  //   if (code !== 200) return false
  //   let credential = data.credential || {}
  //   ctx.commit('setKeysAction', credential)
  // }
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}