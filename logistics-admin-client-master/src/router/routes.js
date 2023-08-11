// 注意： 路由对象必须有 meta , meta 对象 包含 title、keepAlive、isNeedLogin、isNeedOffLine、permission 属性
import * as HomeRoutes from './HomeRoutes'
import url from 'url'
import store from '@/store'

// const Login = [
//   {
//     path: '/Login',
//     name: 'Login',
//     component: () => import('@/views/Login'),
//     meta: {
//       title: '登录',
//       keepAlive: true,
//       isNeedLogin: false,
//       isNeedOffLine: true
//     }
//   },
//   {
//     path: '/wechat/:type',
//     name: 'Wechat',
//     component: () => import ('@/views/Login/Wechat'),
//     meta: {
//       title: '微信登录&绑定',
//       keepAlive: false,
//       isNeedLogin: false,
//       isNeedOffLine: false
//     },
//     async beforeEnter(to, from, next) {
//       if (!to.query.code) return next(false)
//       let {protocol, host} = location
//       store.commit('system/setWechatCode', to.query.code)
//       if (to.params.type === 'login') {
//         return window.open(url.format({protocol, host}), '_top')
//       } else if (to.params.type === 'bind') {
//         return window.open(url.format({protocol, host, search: `?redirectPage=/Account`}), '_top')
//       }
//       next()
//     }
//   }
// ]

const Common = [
  {
    path: '/Disabled',
    name: 'Disabled',
    component: () => import ('@/views/Common/Disabled'),
    meta: {
      title: '无权限',
      keepAlive: false,
      isNeedLogin: true,
      isNeedOffLine: false
    }
  },
  {
    path: '/NotFound',
    name: 'NotFound',
    component: () => import ('@/views/Common/NotFound'),
    meta: {
      title: '页面没找到',
      keepAlive: false,
      isNeedLogin: false,
      isNeedOffLine: false
    }
  },
  {
    path: '/Logout',
    name: 'Logout',
    component: () => import ('@/views/Common/Logout'),
    meta: {
      title: '退出登录',
      keepAlive: false,
      isNeedLogin: true,
      isNeedOffLine: false
    }
  }
]

const Home = {
  path: '/',
  name: 'Home',
  component: () => import('@/views/Home'),
  meta: {
    keepAlive: false,
    isNeedLogin: true,
    isNeedOffLine: false
  },
  redirect: {
    name: 'Main'
  },
  children: [
    ...Object.values(HomeRoutes),
    ...Common
  ]
}


const routes = [
  Home,
  // ...Login,
  {path: '*', redirect: '/NotFound'}
]


export default routes