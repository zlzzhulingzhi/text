import { RouterMount, createRouter } from 'uni-simple-router'
import store from '@/store'

const router = createRouter({
  platform: process.env.VUE_APP_PLATFORM,
  routes: [
    ...ROUTES,
    { path: '*', redirect: '/' }
  ]
})

// 全局路由前置守卫
router.beforeEach(async (to, from, next) => {
  // 首次进入程序
  if (from.name === to.name) {
    await store.dispatch('appInit')
    let identity = store.state.system.identity
    await store.dispatch('appLogin', { identity: identity || 'student' })
  }

  // 需要登录
  // if (to.meta.isNeedLogin) {}

  next()
})

// 全局路由后置守卫
router.afterEach((to, from) => {

})

export {
  router,
  RouterMount
}