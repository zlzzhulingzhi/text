/**
 * 系统与用户 管理
 */
import {OrgId, PermissionMenuCache, Token, UserInfo, WechatCode} from '@/lib/storage'
import Router from '@/router'
import {ArrayFlat} from 'knight-utils'

const { ScreenAuth } = require('@/api')

const state = {
  token: window.parent === window ? Token.get() : null,
  uniAppTypeId: 2,
  userInfo: UserInfo.get() || {},
  permissionMenu: PermissionMenuCache.get() || [],
  orgId: OrgId.get() || 0,
  wechatCode: WechatCode.get() || null,
  innerDomain: null,

  // 界面缩放的比例
  scaleNum: null
}


const getters = {
  isLogin: ({token}) => Boolean(token),
  // 权限树状结构
  permissionList(state) {
    return state.permissionMenu || []
  },
  // 权限扁平化结构
  permissionFlat(state) {
    return ArrayFlat(state.permissionMenu || [])
  }
}


const mutations = {
  setOrgId: (state, d) => {
    state.orgId = d
    d ? OrgId.set(d) : OrgId.del()
    // 同步更新 userInfo 的 orgId
    let obj = {...state.userInfo, orgId: d}
    state.userInfo = obj
    UserInfo.set(state.userInfo)
  },
  tokenUpdate: (state, d) => Token.set(state.token = d),
  uniAppTypeIdUpdate: (state, d) => state.uniAppTypeId = d,
  userInfoUpdate: (state, d) => {
    let obj = {}
    for (const k in d) {
      let key = k.replace(/_\w/g, $1 => $1.slice(1).toUpperCase())
      obj[key] = d[k]
    }
    delete obj.accessToken
    if (d.init) {
      delete obj.init
    } else {
      obj = {...state.userInfo, ...obj}
    }
    state.userInfo = obj
    UserInfo.set(state.userInfo)
  },
  permissionMenuUpdate: (state, d) => PermissionMenuCache.set(state.permissionMenu = d),
  // 微信绑定信息
  setWechatCode: (state, d) => {
    state.wechatCode = d
    d ? WechatCode.set(d) : WechatCode.del()
  },
  updateDomain: (state, d) => {
    state.innerDomain = d // TODO 处理域名不存在情况
  },
  updateScaleNum: (state, d) => {
    state.scaleNum = d 
  },
}


const actions = {
  // 登出
  logout(ctx) {
    // localStorage.clear()
    // sessionStorage.clear()
    // TokenApi.logout().then()
    // location.replace('/')
    window.location.replace(ctx.rootState.config.domain.admin)
  },

  // 同步token
  async syncToken(ctx) {
    console.log('syncToken')
    ctx.commit('userInfoUpdate', {
      ...UserInfo.get(),
      init: true
    })

    if (ctx.getters.isLogin) {
      // 已登录
      // await TokenApi.refresh()
      Router.currentRoute.meta.isNeedOffLine && Router.push('/')
    } else {
      // 未登录
      Router.currentRoute.meta.isNeedLogin && ctx.dispatch('logout')
    }
  },

  // 获取用户信息
  async getUserInfo({dispatch}) {
    await dispatch('getPermissionData')
  },
  // 获取权限列表
  async getPermissionData({commit, state}) {
    let {code, data} = await ScreenAuth.menuPermission({
      applicationTypeId: state.uniAppTypeId
    })

    if (code !== 200) return false
    let menuList = []
    try {
      menuList = data.menuList
    } catch (e) {
    }

    commit('permissionMenuUpdate', menuList.map(menu => {
      return {
        ...menu,
        id: menu.id,
        menuName: menu.menuName,
        menuList: undefined,
        children: menu.children || []
      }
    }))
  },
  // iframe父子通信
  getTokenFromParent(ctx) {
    // if (ctx.rootGetters.isDev) {
    //   return setTimeout(() => {
    //     ctx.dispatch('getToken', {
    //       uniAppTypeId: 2,
    //       token: 'eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyX3R5cGUiOiJwbGF0Zm9ybSIsInVzZXJfaWQiOjExNiwidXNlcl9rZXkiOiI0ZmNjYzQzMi05ODIyLTQ3NjMtOGYzYy04ZGYxMzc5M2U2ZTMiLCJ1c2VybmFtZSI6bnVsbH0.kBLX6Ldeg81vJCSBV5589iHMCQ6HQF77TAQukEYImW8gekfq_QB39taQN5xAOw7dPfeip3G_EUmmoK8BNt5EEw'
    //     })
    //   },30)
    // }

    window.addEventListener('message', async ({data}) => {
      if (ctx.rootGetters.isDev) console.log('%c 调试token', 'background-color:green;color:white', data)

      switch (data.type) {
        case 'token':
          // 监听token
          ctx.dispatch('getToken', data)
          break
      }
    })
  },
  async getToken(ctx, d) {
    await ctx.commit('tokenUpdate', d.token)
    await ctx.commit('uniAppTypeIdUpdate', d.uniAppTypeId)
    // let {code, data} = await Token.loginAccess({})
    // if (code !== 200) return false
    // await ctx.commit('tokenUpdate', data.access_token)
    // await ctx.commit('userInfoUpdate', data)
    let {name, query, params} = ctx.rootGetters['router/currentRoute'] // 上次访问页面【缓存】
    let initName = await ctx.dispatch('appInit', null, {root: true}) // 侧边栏第一个页面【后台数据】
    let r = name ? {name, query, params} : {name: initName}
    // await Router.replace(r)

    await Router.replace({name: initName})
  }
}


export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}