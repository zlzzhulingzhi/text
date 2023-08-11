import Vue from 'vue'
import Vuex from 'vuex'

import common from './common'
import config from './config'
import system from './system'

Vue.use(Vuex)

const state = {}

const getters = {
  isDev: () => process.env.NODE_ENV === 'development',
  isTest: () => process.env.NODE_ENV === 'testing',
  isProd: () => process.env.NODE_ENV === 'production'
}

const mutations = {}

const actions = {
  async appInit({ dispatch }) {
    await dispatch('common/saveContext', this, { root: true })
    await dispatch('common/createFilter', null, { root: true })
    await dispatch('system/getAuth', null, { root: true })
  },
  async appLogin({ dispatch }, payload) {
    await dispatch('system/login', payload, { root: true })
  }
}

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions,
  modules: {
    common,
    config,
    system
  }
})