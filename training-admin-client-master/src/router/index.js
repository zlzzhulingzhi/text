import Vue from 'vue'
import VueRouter from 'vue-router'
import routes from './routes'
import store from '@/store'
import url from 'url'

Vue.use(VueRouter)

// 拦截重复进入同一路由报错
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function(location, onComplete) {
  return originalPush.call(this, location).then(onComplete).catch(err => err)
}
const originalReplace = VueRouter.prototype.replace
VueRouter.prototype.replace = function(location, onComplete) {
  return originalReplace.call(this, location).then(onComplete).catch(err => err)
}

const router = new VueRouter({
  routes
})

router.beforeEach(async (to, from, next) => {

  // 0. 阶段 - 开发环境打印
  let {isNeedLogin, isNeedOffLine} = to.meta
  if (store.getters.isDev) {
    console.log(`%c enter to ${to.name} [${['开放权限', '需要登录', '需要离线', '关闭权限'][isNeedLogin + isNeedOffLine * 2]}] 权限码：${to.meta.permission} \n当前页面按钮级权限[meta.childPermissions]`, 'color: #36d;', JSON.parse(JSON.stringify(to.meta.childPermissions || {})))
  }


  // 1. 阶段 - 解析 url
  let {search, protocol, hash, host} = location
  let {query} = url.parse(search, true)

  // 页面重定向
  if (query.redirectPage) {
    return window.open(url.format({
      protocol,
      host,
      search: search.replace(/redirectPage=[a-zA-Z0-9\/]+&?/,''),
      hash: `#${query.redirectPage}`
    }), '_self')
  }


  // 2. 阶段 - 初始化（首次进入项目）
  let isLogin = store.getters['system/isLogin']
  if (from.name === null) {
    // 免token 初始化
    let loadingEl = document.createElement('div')
    loadingEl.innerHTML = `<div class="full-screen flex center-center bg-background"><span class="el-icon-loading margin-right-8"></span>页面初始化中...</div>`
    document.body.appendChild(loadingEl)
    let name = await store.dispatch('appInitWithoutToken')
    document.body.removeChild(loadingEl)

    // 登录后初始化 - 获取后台权限，解锁 待开放页
    if (isLogin) {
      let name = await store.dispatch('appInit')
      if (to.name === 'Main' && to.meta.isNeedOffLine) return next({name})
    }
  }


  // 3. 阶段 - 权限判断、页面拦截
  if (isLogin) {
    // 已登录

    // A. 待开放页 - 不可进入，需要根据后台权限解锁
    if (to.meta.isNeedOffLine && to.meta.isNeedLogin) return next('/Disabled')
    // B. 离线页面 - 登录后不可进入
    if (to.meta.isNeedOffLine) return next(false)
  } else {
    // 未登录

    // C. 普通页面 - 未登录，不可进入
    if (to.meta.isNeedLogin) return next('/login')
  }

  // D. 开放页面 - 登录、未登录，都可以进入
  next()


})

router.afterEach(async (to, from) => {
  // 路由历史缓存
  if (to.meta.isNeedLogin) store.commit('router/push', to)
  // 定义标签页动态标题
  document.title = to.meta.title + ' - ' + store.state.config.name
})

// 多标签页同步登出
// 标签页切换、鼠标移入页面时，重新获取token，同步其他标签页token
window.addEventListener('visibilitychange', async function() {
  if (document.visibilityState !== 'visible') return false
  await store.dispatch('system/syncToken')
})


window.onfocus = async function() {
  await store.dispatch('system/syncToken')
}



export default router