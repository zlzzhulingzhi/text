import { Token, UserInfo, AuthInfo, Identity } from '@/lib/storage'
import { Auth, AuthAdmin } from '@/api'
import logger from '@/lib/logger'

const state = {
  token: Token.get() || '',
  userInfo: UserInfo.get() || {},
  authInfo: AuthInfo.get() || {},
  identity: Identity.get() || ''
}

const getters = {
  isLogin: (state) => Boolean(state.token),
  hasOpenId: (state) => Boolean(state.authInfo.openId),
  isStudent: (state) => Boolean(state.identity === 'student'),
  isOrg: (state) => Boolean(state.identity === 'org'),
  isAdmin: (state) => Boolean(state.identity === 'admin')
}

const mutations = {
  updateToken: (state, data) => Token.set(state.token = data),
  updateUserInfo: (state, data) => {
    let obj = {}
    for (const k in data) {
      let key = k.replace(/_\w/g, $1 => $1.slice(1).toUpperCase())
      obj[key] = data[k]
    }
    if (data.init) {
      delete obj.init
    } else {
      obj = { ...state.userInfo, ...obj }
    }
    UserInfo.set(state.userInfo = obj)
  },
  updateAuthInfo: (state, data) => AuthInfo.set(state.authInfo = { ...state.authInfo, ...data }),
  updateIdentity: (state, data) => Identity.set(state.identity = data)
}

const actions = {
  async getAuth(ctx) {
    // #ifdef MP-WEIXIN
    if (!ctx.getters.hasOpenId) {
      // 1.1.获取微信Code
      let wxCode = ''
      // uni API Promise 写法返回的结构是数组，其中第1个值是 fail 信息，第2个值是 success 信息
      let [, success] = await uni.login({ provider: 'weixin' })
      if (!success) return logger.error(`微信授权登录失败`, logger.TAG.AUTH)
      wxCode = success.code

      // 1.2.获取AuthInfo
      let { code, data } = await Auth.wxInfo({
        code: wxCode,
        appid: ctx.rootState.config.APPID
      })
      if (code !== 200) return logger.error(`获取AuthInfo失败`, logger.TAG.AUTH)
      ctx.commit('updateAuthInfo', {
        unionId: data.unionid,
        openId: data.openid
      })
    }
    // #endif

    // #ifdef H5
    if (ctx.rootGetters.isDev) {
      ctx.commit('updateAuthInfo', {
        unionId: 'o4ijG1AmSxlJL2FDY-83GyDS-uFI',
        openId: 'oB4Q75Z47mRoPLPY-ZBJ4Qnx-W38'
      })
    }
    // #endif
  },
  async login(ctx, payload = {}) {
    // 1.登录
    let typeMapping = {
      student: { apiFn: Auth.wxLogin, params: {} },
      org: { apiFn: AuthAdmin.login, params: { loginCode: 'org' } },
      admin: { apiFn: AuthAdmin.login, params: { loginCode: 'admin' } }
    }
    let loginInfo = typeMapping[payload.identity || 'student']
    let { code, data } = await loginInfo.apiFn({
      openId: ctx.state.authInfo.openId,
      ...loginInfo.params
    })
    if (code === 200 && data) {
      ctx.commit('updateToken', data.access_token)
      ctx.commit('updateUserInfo', {
        ...data,
        access_token: undefined,
        identity: payload.identity,
        user_name: data.real_name || data.user_name,
        member_id: data.user_id || data.member_id,
        init: true
      })
      ctx.commit('updateIdentity', payload.identity || 'student')
    } else {
      await ctx.dispatch('logout')
      ctx.commit('updateIdentity', 'student')
    }

    // 2.登录完成事件（放置于所有 async/await 方法之后）
    // #ifdef H5
    setTimeout(() => {
      uni.$emit(ctx.rootState.config.EVENT.LOGIN)
    }, 500)
    // #endif
    // #ifndef H5
    uni.$emit(ctx.rootState.config.EVENT.LOGIN)
    // #endif
  },
  logout(ctx) {
    ctx.state.token = ''
    ctx.state.userInfo = {}
    Token.remove()
    UserInfo.remove()
    // 重新登录
    // ctx.dispatch('login')
  }
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}