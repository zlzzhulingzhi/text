/**
 * 系统与用户 管理
 */
import { OrgId, PermissionMenuCache, Token, UserInfo, WechatCode } from '@/lib/storage'
import Router from '@/router'
import { ArrayFlat } from 'knight-utils'

const { User, OrgIndexPage, Domain, Token: TokenAPI } = require('@/api')

const state = {
  token: null,
  userInfo: UserInfo.get() || {},
  permissionMenu: PermissionMenuCache.get() || [],
  orgId: OrgId.get() || 0,
  wechatCode: WechatCode.get() || null,
  innerDomain: null
}


const getters = {
  isLogin: ({ token }) => Boolean(token),
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
    let obj = { ...state.userInfo, orgId: d }
    state.userInfo = obj
    UserInfo.set(state.userInfo)
  },
  tokenUpdate: (state, d) => {
    state.token = d
    d && Token.set(d)
  },
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
      obj = { ...state.userInfo, ...obj }
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
  }
}


const actions = {
  // 登出
  logout() {
    // localStorage.clear()
    // sessionStorage.clear()
    // Token.del()
    // TokenApi.logout().then()
    // location.replace('/')
  },

  // 同步token
  async syncToken(ctx) {
    console.log('syncToken')
    ctx.commit('tokenUpdate', Token.get())
    ctx.commit('setOrgId', OrgId.get())
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

  // 获取二级域名
  async getInnerDomain({ commit }) {
    let { code, data } = await Domain.domain()
    if (code !== 200) return commit('updateDomain', null)
    commit('updateDomain', data.domain)
  },

  // 获取用户信息
  async getUserInfo({ dispatch }) {
    await dispatch('getPermissionData')
  },
  // 获取权限列表
  async getPermissionData({ commit, state }) {
    if (!state.orgId) return false
    // 管理员权限
    let { code, data } = await OrgIndexPage.loginPermission({
      applicationTypeId: 3,
      orgId: state.orgId
    })
    if (code !== 200) {
      commit('tokenUpdate', '')
      Token.del()
      await Router.replace('/')
      return Promise.reject('没有权限')
    }

    let aPermissions = {
      2: 'System:Main',
      3: 'System:Education',
      4: 'System:Management'
    }
    if (!data.appList) return false
    commit('permissionMenuUpdate', data.appList.map(item => {
      return {
        ...item,
        permission: aPermissions[item.appTypeId],
        menuName: item.appTypeName,
        children: item.applicationList.map(aItem => {
          return {
            ...aItem,
            permission: aItem.permission,
            menuName: aItem.appName,
            children: aItem.menuList || []
          }
        })
      }
    }))
    // 在此储存userInfo信息 - B部分
    commit('userInfoUpdate', {
      ...data,
      appList: undefined
    })
  },

  // 获取token
  async getToken(ctx, d) {
    localStorage.clear()
    sessionStorage.clear()
    await ctx.commit('tokenUpdate', d.token)
    let { code, data } = await TokenAPI.loginAccess({})
    if (code !== 200) return false
    await ctx.commit('tokenUpdate', data.access_token)
    await ctx.commit('userInfoUpdate', {
      ...data,
      init: true
    })
  },
  // iframe父子通信
  getTokenFromParent(ctx) {
    // if (ctx.rootGetters.isDev) {
    //   return setTimeout(() => {
    //     ctx.dispatch('parseToken', {
    //       uniAppTypeId: 4,
    //       token: 'eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyX3R5cGUiOiJlbXBsb3llZSIsInVzZXJfaWQiOjExNiwidXNlcl9rZXkiOiI0MzM3MjExNi02ZDNiLTRlYzktODQ2ZS1mM2IxYTZhOGQ4ZTciLCJ1c2VybmFtZSI6bnVsbH0.k3i8Srt9Ot-M2yAxR7YbLZh56HOtZX1mtz4zkBNWdqxR9KFG0KT0u7nstEOLGGJq2x4t-yfKDFHg7QpPHZyR2w'
    //     })
    //   }, 30)
    // }

    window.addEventListener('message', async ({ data }) => {
     console.log('%c 调试token', 'background-color:green;color:white', data)

      switch (data.type) {
        case 'token':
          // 监听token
          ctx.dispatch('parseToken', data)
          break
      }
    })
  },
  async parseToken(ctx, d) {
    await ctx.commit('tokenUpdate', d.token)
    await ctx.commit('uniAppTypeIdUpdate', d.uniAppTypeId)
    await ctx.commit('router/updateInitRoutePath', d.routePath, { root: true })
    if(d.studentName&&d.id&&d.memberId) {
      let studentInfo = {
        id: d.id,
        studentName: d.studentName,
        memberId: d.memberId
      }
      await ctx.commit('router/storeStudentInfo', studentInfo, { root: true })
    }
    let { code, data } = await TokenAPI.loginAccess({})
    if (code !== 200) return false
    await ctx.commit('tokenUpdate', data.access_token)
    await ctx.commit('userInfoUpdate', data)
    let { name, query, params } = ctx.rootGetters['router/currentRoute'] || {} // 上次访问页面【缓存】
    let initName = await ctx.dispatch('appInit', null, { root: true }) // 侧边栏第一个页面【后台数据】
    let r = name ? { name, query, params } : { name: initName }
    // await Router.replace(r)

    // await Router.replace({ name: initName })

    if (/^\//.test(initName)) {
      await Router.replace(initName)
    } else {
      await Router.replace({ name: initName || 'Main' })
    }
  },

  sendMessageToParent(ctx, data) {
    console.log('%c 发送信息给window.parent', 'background-color:green;color:white', data)
    window.parent.postMessage({
      type: 'message',
      data
    }, '*')
  }
}


export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}