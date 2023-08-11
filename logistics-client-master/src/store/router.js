/**
 * 路由与权限 管理
 */

import Router from '@/router'
import {ArrayFlat} from '@/lib/utils'
import {RouteQueue} from '@/lib/storage'


const state = {
  queue: RouteQueue.get() || [{
    path: '/',
    name: 'Main',
    meta: {title: '首页'},
    isFixed: true
  }],
  queueMaxNumber: 4,
  sideMenuList: [],
  initRoutePath: null,
  studentInfo: null
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
      list.sort((a, b) => {
        return a[0] > b[0] ? 1 : -1
      })
      console.log(JSON.parse(JSON.stringify(list)))
    }

    return list
  },
  // 侧边栏结构
  sideMenuList({sideMenuList}) {
    // 测试数据
    /*    let HomeRoute = Router.options.routes.find(item => item.path === '/')
        return HomeRoute.children*/
    return [
      ...sideMenuList
    ]
  }
}


const mutations = {
  push({queue, queueMaxNumber}, {path, name, meta, query}) {
    // 离线页面 不记录
    if (meta.isNeedOffLine) return false

    let fRoute = queue.find(item => item.name === name)

    if (fRoute) {
      // 更新路由 针对动态路由、动态meta等场景
      fRoute.path = path
      fRoute.meta = meta
      fRoute.query = query
    } else {
      queue.push({path, name, meta, query, isFixed: false})
    }

    if (queue.filter(item => !item.isFixed) > queueMaxNumber) {
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
  updateSideMenuList: (state, sideMenuList) => state.sideMenuList = sideMenuList,
  updateInitRoutePath: (state, d) => state.initRoutePath = d,
  storeStudentInfo: (state, d) => state.studentInfo = d,
}


const actions = {
  // 权限同步路由表 - 以后台接口树状结构为核心
  // PS: 不能使用 getters 属性
  syncPermission(ctx) {
    let permissionList = ctx.rootGetters['system/permissionList']
    console.log('syncPermission: 权限同步路由表', JSON.parse(JSON.stringify(permissionList)))
    if (!permissionList) return false
    let HomeRoute = Router.options.routes.find(item => item.path === '/')
    let parents = ctx.getters.flatList.filter(item => item.children && item.children.length)
    let mapFn = (menu) => {
      // menu.sort((a, b) => a.sort - b.sort)
      return menu.map(tItem => {
        let fRoutes = ctx.getters.flatList.find(({meta}) => meta.permission === tItem.permission) || {
          name: 'NotFound'
        }
        let childPermissions = {}
        let childrenOrBrother = tItem.children.length ? tItem.children : menu
        childrenOrBrother.forEach(cItem => {
          let keyList = cItem.permission.split(':')
          let key = keyList[keyList.length - 1]
          childPermissions[key] = true
        })

        if (fRoutes.meta) {

          fRoutes.meta.title = tItem.menuName
          if (fRoutes.path === '/') {
            let parentRoutes = parents.find(item => item.children.find(cItem => cItem.name === fRoutes.name))
            if (parentRoutes) parentRoutes.meta.title = tItem.menuName
          }
          fRoutes.meta.icon = tItem.iconUrl
          // 开启权限
          fRoutes.meta.isNeedOffLine = false
          fRoutes.meta.childPermissions = childPermissions
          tItem.routeName = fRoutes.name
        } else {
          fRoutes.meta = {
            title: tItem.menuName,
            icon: tItem.iconUrl,
            childPermissions
          }
        }

        let children = []
        if (tItem.children) {
          children = mapFn(tItem.children)
          // 重点： 将 空页面的父级路由 重定向 到 有权限的子路由的第一个
          // 要求 空页面的父级路由 的 redirect 为 一个 对象 ！
          if (fRoutes.redirect && children[0]) {
            fRoutes.redirect.name = children[0].name
          }
        }
        return {
          ...tItem,
          ...fRoutes,
          children
        }
      })
    }
    let sideMenu = mapFn(permissionList, HomeRoute.children)
    console.log('同步结束', JSON.parse(JSON.stringify(sideMenu)))
    ctx.commit('updateSideMenuList', sideMenu)

    if(ctx.state.initRoutePath) return ctx.state.initRoutePath
    if(!ctx.rootState.system.orgId) return 'Index'
    let routeName = 'Disabled'
    try {
      routeName = sideMenu[0].children[0].name
    } catch (e) {
    }
    return routeName
  }
}


export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}