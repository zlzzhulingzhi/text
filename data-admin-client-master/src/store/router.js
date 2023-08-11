/**
 * 路由与权限 管理
 */

import Router from '@/router'
import {ArrayFlat} from '@/lib/utils'
import {RouteQueue} from '@/lib/storage'


const state = {
  // 路由历史记录
  queue: RouteQueue.get() || [],
  queueMaxNumber: 4,
  sideMenuList: []
}


const getters = {
  // 扁平化路由对象
  flatList() {
    return ArrayFlat(Router.options.routes.filter(item => item.path !== '*'))
  },
  // 缓存路由列表
  keepAliveNameList(state, getters, rootState, rootGetters) {
    let list = getters.flatList.filter(item => item.meta.keepAlive).map(item => item.name)

    if (rootGetters.isDev) {
      list.sort((a, b) => a[0] > b[0] ? 1 : -1)
      console.log('缓存路由列表', JSON.parse(JSON.stringify(list)))
    }

    return list
  },
  // 侧边栏结构
  sideMenuList(state) {
    return state.sideMenuList
  },
  currentRoute(state) {
    return state.queue[state.queue.length - 1] || {}
  }
}


const mutations = {
  push({queue, queueMaxNumber}, {path, name, meta, query, params}) {
    let fIndex = queue.findIndex(item => item.name === name)
    if (fIndex > -1) queue.splice(fIndex, 1)

    queue.push({path, name, meta, query, params, isFixed: false})

    if (queue.filter(item => !item.isFixed).length > queueMaxNumber) {
      queue.splice(queue.findIndex(item => !item.isFixed), 1)
    }
    RouteQueue.set(queue)
  },
  async delete({queue}, {name}) {
    if (queue.length === 1) return false
    let i = queue.findIndex(item => item.name === name)
    if (i < 1) return false
    queue.splice(i, 1)
    RouteQueue.set(queue)

    if (Router.history.current.name === name) {
      let {path, query} = (queue[i] || queue[i - 1])
      await Router.push({path, query})
    }
  },
  fixed({queue, queueMaxNumber}, {name, isFixed}) {
    if (queue.filter(item => item.isFixed).length >= queueMaxNumber && !isFixed) return false
    let fRoute = queue.find(item => item.name === name)
    if (fRoute) {
      fRoute.isFixed = !isFixed
      RouteQueue.set(queue)
    }
  },
  updateSideMenuList: (state, d) => state.sideMenuList = d
}


const actions = {
  // 权限同步路由表 - 以后台接口树状结构为核心
  // PS: 不能使用 getters 属性
  syncPermission(ctx) {
    let permissionList = ctx.rootGetters['system/permissionList']
    try {
      console.log('syncPermission: 权限同步路由表', JSON.parse(JSON.stringify(permissionList)))
    } catch (e) {
    }

    if (!permissionList || !permissionList.length) return 'Disabled'

    let loop = list => list ? list.map(item => {
      // list.sort((a, b) => a.sort - b.sort)
      let fRoute = ctx.getters.flatList.find(({meta}) => meta.permission === item.permission) || {name: 'NotFound'}

      let childPermissions = {}
      let childrenOrBrother = item.children && item.children.length ? item.children : list
      childrenOrBrother.forEach(cItem => {
        let keyList = cItem.permission.split(':')
        childPermissions[keyList[keyList.length - 1]] = true
      })

      if (fRoute.meta) {
        fRoute.meta.title = item.menuName
        // 开启权限
        fRoute.meta.isNeedOffLine = false
        // 捆绑子级权限 或 兄弟级权限到路由
        fRoute.meta.childPermissions = childPermissions
      }

      try {
        let fChildren = ctx.getters.flatList.find(({meta}) => meta.permission === item.children[0].permission)
        fRoute.redirect.name = fChildren.name
      } catch (e) {
      }

      return {
        ...item,
        name: item.menuName,
        routeId: String(item.id),
        routeName: fRoute.name,
        // fullPath: Router.match(fRoute).fullPath,
        children: loop(item.children)
      }
    }) : []

    let sideMenu = loop(permissionList)

    ctx.commit('updateSideMenuList', sideMenu)

    return sideMenu[0].children[0].routeName
  }
}


export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}