// 注意： 路由对象必须有 meta , meta 对象 包含 title、keepAlive、isNeedLogin、isNeedOffLine、permission 属性
import * as HomeRoutes from './HomeRoutes'

const Login = [
  {
    path: '/Login',
    name: 'Login',
    component: () => import('@/views/Login'),
    meta: {
      title: '登录',
      keepAlive: false,
      isNeedLogin: false,
      isNeedOffLine: true
    }
  },
  {
    path: '/LoginPage',
    name: 'LoginPage',
    component: () => import('@/views/Login/copy-login'),
    meta: {
      title: '登录',
      keepAlive: false,
      isNeedLogin: false,
      isNeedOffLine: true
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
  ...Login,
  {path: '*', redirect: '/NotFound'}
]



// 以下路由信息，以及views中的test文件，均待删除

// 开发环境测试页面 - demo
if (process.env['NODE_ENV'] === 'development') {
  const Test = {
    path: '/Test',
    name: 'Test',
    component: () => import('@/views/Test'),
    // 嵌套路由无额外布局时，可使用RouterAlive
    // component: () => import('@/components/common/RouterAlive'),
    meta: {
      title: '开发页 - 开放权限',
      keepAlive: false,
      isNeedLogin: false,
      isNeedOffLine: false
    },
    children: [
      {
        path: 'Demo',
        name: 'Demo - 开放权限',
        component: () => import('@/views/Test/Demo'),
        meta: {
          title: 'Demo',
          keepAlive: false,
          isNeedLogin: false,
          isNeedOffLine: false
        }
      },
      {
        path: 'Demo1',
        name: 'Demo1',
        component: () => import('@/views/Test/Demo1'),
        meta: {
          // 登录之后就能进，不需要权限码 permission
          title: 'Demo1 - 需要登录',
          keepAlive: false,
          isNeedLogin: false,
          isNeedOffLine: false
        }
        // children: [
        //     {
        //       path: 'ResourceList',
        //       name: 'ResourceList',
        //       component: () => import('@/views/Test/Demo1/ResourceList'),
        //       meta: {
        //         title: '资源列表',
        //         keepAlive: false,
        //         isNeedLogin: false,
        //         isNeedOffLine: false
        //       }
        //     },
        // ]
      },
      {
        path: 'Demo2',
        name: 'Demo2',
        component: () => import('@/views/Test/Demo2'),
        meta: {
          // 离线页面，登录了不能进（一般只有登录页）
          title: 'Demo2 - 需要离线',
          keepAlive: false,
          isNeedLogin: false,
          isNeedOffLine: true
        }
      },
      {
        path: 'Demo3',
        name: 'Demo3',
        component: () => import('@/views/Test/Demo3'),
        meta: {
          // 权限页 - 关闭权限，需要登录得到权限码后，修改meta.isNeedOffLine = false，才能访问
          title: 'Demo3 - 关闭权限',
          keepAlive: false,
          isNeedLogin: true,
          isNeedOffLine: true,
          permission: 'project:permission:test'
        }
      },
      {
        path: 'dev',
        name: 'dev',
        component: () => import('@/views/Test/dev'),
        meta: {
          title: 'dev',
          keepAlive: false,
          isNeedLogin: false,
          isNeedOffLine: false,
          permission: ''
        }
      },
      {
        path: 'DemoLive',
        name: 'DemoLive',
        component: () => import('@/views/Test/Live'),
        meta: {
          title: 'DemoLive',
          keepAlive: false,
          isNeedLogin: false,
          isNeedOffLine: false,
          permission: ''
        }
      }
    ]
  }
  routes.push(Test)
}

export default routes