import url from 'url'
import { router } from '@/router'
import store from '@/store'

// 获取页面传参options对象
export const getOptions = (options) => {
  if (options.scene) {
    let scene = decodeURIComponent(options.scene)
    let { query } = url.parse(`?${scene}`, true)
    return query || {}
  }
  return options
}

// 根据路由name获取路由path
export const getRoutePath = (routeName) => {
  if (!routeName) return '/'
  let route = router.options.routes.find(v => v.name === routeName)
  if (!route) return '/'
  return route.path
}

// 是否需要登录
export const isNeedLogin = (routeName) => {
  if (!routeName) return false
  let route = router.options.routes.find(v => v.name === routeName)
  if (!route) return false
  return route.meta.isNeedLogin
}

// 计算时间差（return ms）
export const getTimeDiff = (startTime, endTime) => {
  let stime = startTime ? new Date(startTime.replace(/-/g, '/')) : new Date()
  let etime = endTime ? new Date(endTime.replace(/-/g, '/')) : new Date()
  return etime.getTime() - stime.getTime()
}

// 数组分组
export const groupByList = (list = [], count = 3) => {
  let array = []
  let index = 0
  for (let i = 0; i < list.length; i++) {
    index = parseInt(i / count)
    if (array.length <= index) {
      array.push([])
    }
    array[index].push(list[i])
  }
  return array
}