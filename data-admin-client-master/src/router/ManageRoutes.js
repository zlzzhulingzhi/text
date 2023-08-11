// 大屏渲染
export const Screen = {
  path: 'Screen',
  name: 'ScreenRender',
  component: () => import('@/components/common/RouterAlive'),
  redirect: { name: '' },
  meta: {
    title: '大屏渲染',
    keepAlive: false,
    isNeedLogin: true,
    isNeedOffLine: true,
    permission: 'Management:Screen'
  },
  children: [
    {
      path: 'Data',
      name: 'ScreenData',
      component: () => import('@/views/Manage/Screen/ScreenData'),
      meta: {
        title: '大屏数据',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Screen:Data'
      }
    }
  ]
}
