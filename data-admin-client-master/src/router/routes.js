// 注意： 路由对象必须有 meta , meta 对象 包含 title、keepAlive、isNeedLogin、isNeedOffLine、permission 属性
import * as ManageRoutes from './ManageRoutes'

// 大屏展示
const Home = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home/index.vue'),
    meta: {
      title: '大屏展示',
      keepAlive: false,
      isNeedLogin: false,
      isNeedOffLine: false
    }
  }
]

// 实时数据
const NewHome = [
  {
    path: '/InTime',
    name: 'InTime',
    component: () => import('@/views/NewHome/index.vue'),
    meta: {
      title: '实时统计',
      keepAlive: false,
      isNeedLogin: false,
      isNeedOffLine: false
    }
  }
]

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
    path: '/Loading',
    name: 'Loading',
    component: () => import ('@/views/Common/Loading'),
    meta: {
      title: '正在加载',
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

const Manage = {
  path: '/Manage',
  name: 'Manage',
  component: () => import('@/views/Manage'),
  meta: {
    keepAlive: false,
    isNeedLogin: true,
    isNeedOffLine: false
  },
  children: [
    ...Object.values(ManageRoutes),
    ...Common
  ]
}

const routes = [
  Manage,
  ...Home,
  ...NewHome,
  {path: '*', redirect: '/NotFound'}
]

export default routes