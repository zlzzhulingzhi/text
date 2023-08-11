import Vue from 'vue'
import Vuex from 'vuex'

import common from './common'
import config from './config'
import router from './router'
import system from './system'

import resource from './resource'


Vue.use(Vuex)


const state = {
  // 本地时间流（前一帧）
  localTimePrev: Date.now(),
  // 本地时间
  localTime: Date.now(),
  // 差值: 服务器时间 - 本地时间
  serverTimeDiff: 0
}


const getters = {
  // 服务器时间
  serverTime: state => state.localTime + state.serverTimeDiff,

  // 开发环境
  isDev: () => process.env['NODE_ENV'] === 'development',
  // 测试环境
  isTesting: () => process.env['NODE_ENV'] === 'testing',
  // 上线环境
  isProduction: () => process.env['NODE_ENV'] === 'production',

  // 微信端
  isWeiXin: () => /(?:MicroMessenger)/.test(navigator.userAgent),
  // 支付宝端
  isAlipay: () => /(?:AlipayClient)/.test(navigator.userAgent),

  isWindowsPhone: () => /(?:Windows Phone)/.test(navigator.userAgent),
  isSymbian: (state, getters) => /(?:SymbianOS)/.test(navigator.userAgent) || getters.isWindowsPhone,
  isFireFox: () => /(?:Firefox)/.test(navigator.userAgent),
  isChrome: () => /(?:Chrome|CriOS)/.test(navigator.userAgent),

  isAndroid: () => /(?:Android)/.test(navigator.userAgent),
  isTablet: (state, getters) => /(?:iPad|PlayBook)/.test(navigator.userAgent) || (getters.isAndroid && !/(?:Mobile)/.test(navigator.userAgent)) || (getters.isFireFox && /(?:Tablet)/.test(navigator.userAgent)),
  isIOS: (state, getters) => /(?:iPhone)/.test(navigator.userAgent) && !getters.isTablet,
  isPC: (state, getters) => !getters.isIOS && !getters.isAndroid && !getters.isSymbian,
  isH5: (state, getters) => !getters.isPC,

  credential: (state) => state.resource.credential

}

const mutations = {
  saveLocalTime: state => {
    state.localTimePrev = state.localTime
    state.localTime = Date.now()
  },
  saveServerTime: (state, t) => state.serverTimeDiff = new Date(t) - Date.now()
}


const actions = {
  // 免登录 初始化
  async appInitWithoutToken({dispatch}) {
    await dispatch('common/saveContext', this, {root: true})

    // 初始化过滤器
    await dispatch('common/createFilter', null, {root: true})

    await dispatch('tick')

    // iframe父子通信
    await dispatch('system/getTokenFromParent', null, {root: true})
  },

  // 登录后 初始化
  async appInit({dispatch}) {
    await dispatch('system/getUserInfo', null, {root: true})
    // await dispatch('system/getInnerDomain', null, { root: true }) // 获取二级域名
    let name = await dispatch('router/syncPermission', null, {root: true})

    // 返回登录后跳转页面
    return name
  },


  // 定时获取当前时间
  async tick(ctx) {
    let fn = () => {
      ctx.commit('saveLocalTime')

      // 本地时间差： 1. 切屏了 2. 修改了本地时间
      let localTimeDiff = ctx.state.localTime - ctx.state.localTimePrev

      requestAnimationFrame(fn)
    }

    fn()
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
    router,
    system,
    resource
  }
})
