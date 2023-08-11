// 首页
export const Main = {
  path: '/',
  name: 'Main',
  component: () => import('@/components/common/RouterAlive'),
  meta: {
    title: '首页',
    keepAlive: true,
    isNeedLogin: true,
    isNeedOffLine: false,
    permission: 'System:Main'
  },
  redirect: { name: 'Dashboard' },
  children: [
    {
      path: 'Index',
      name: 'Index',
      component: () => import('@/views/Home/Index/Org'),
      meta: {
        title: '我的机构',
        keepAlive: true,
        isNeedLogin: true,
        isNeedOffLine: false,
        isIndex: true
      }
    },
    {
      path: 'Account',
      name: 'Account',
      component: () => import('@/views/Home/Index/Account'),
      meta: {
        title: '账号设置',
        keepAlive: true,
        isNeedLogin: true,
        isNeedOffLine: false,
        isIndex: true
      }
    },
    {
      path: 'Dashboard',
      name: 'Dashboard',
      component: () => import('@/views/Home/Main'),
      meta: {
        title: '数据概览',
        keepAlive: true,
        isNeedLogin: false,
        isNeedOffLine: false,
        permission: 'System:Main:View',
        isIndex: true
      }
    }
  ]
}


export const UniManagement = { 
  path: 'UniManagement',
  name: 'UniManagement',
  component: () => import('@/components/common/RouterAlive'),
  redirect: { name: 'Plat' },
  meta: { title: '管理中心', keepAlive: false, isNeedLogin: true, isNeedOffLine: true, permission: 'Management:Unit' },
  children: [
    // 单位职员
    {
      path: 'UnitUser',
      name: 'UnitUser',
      component: () => import('@/views/Home/UniManagement/UnitUser'),
      meta: {
        title: '单位职员',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Managemen:UnitUser'
      },
    },
    {
      path: 'Unit',
      name: 'Unit',
      component: () => import('@/views/Home/UniManagement/Unit'),
      meta: {
        title: '单位管理',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Managemen:Unit'
      },
    }
  ]
}


// 小程序管理
export const ActivityManagement = { 
  path: 'ActivityManagement',
  name: 'ActivityManagement',
  component: () => import('@/components/common/RouterAlive'),
  redirect: { name: 'Activity' },
  meta: { title: '活动管理', keepAlive: false, isNeedLogin: true, isNeedOffLine: true, permission: 'Management:Activity' },
  children: [
    // 活动管理
    {
      path: 'Activity',
      name: 'Activity',
      component: () => import('@/views/Home/ActivityManagement'),
      meta: {
        title: '活动',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Manage:Activity'
      },
    },
    // 新建活动
    {
      path: 'CreateActivity',
      name: 'CreateActivity',
      component: () => import('@/views/Home/ActivityManagement/Create'),
      meta: {
        title: '新建活动',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Manage:Activity:Create'
      },
      beforeEnter (to, from, next) {
        switch (to.params.type) {
          case 'Create':
            to.meta.title = '新增活动'
            return next()
          case 'Edit':
            to.meta.title = '编辑活动'
            return next()
          case 'Detail':
            to.meta.title = '活动详情'
            return next()
          default:
            next(false)
        }
      }
    },
    // 活动详情
    {
      path: 'ActivityDetail',
      name: 'ActivityDetail',
      component: () => import('@/views/Home/ActivityManagement/Detail'),
      meta: {
        title: '新建活动',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Manage:Activity:Detail'
      },
    },
    // 图片管理
    {
      path: 'PicManage',
      name: 'PicManage',
      component: () => import('@/views/Home/ActivityManagement/PicManage'),
      meta: {
        title: '图片管理',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Manage:Banner'
      },
    },
  ]
}


// 管理中心
export const Management = {
  path: 'Management',
  name: 'Management',
  component: () => import('@/components/common/RouterAlive'),
  redirect: { name: 'Plat' },
  meta: { title: '管理中心', keepAlive: false, isNeedLogin: true, isNeedOffLine: true, permission: 'System:Management' },
  children: [
    // 平台管理
    {
      path: 'Plat',
      name: 'Plat',
      component: () => import('@/components/common/RouterAlive'),
      redirect: { name: 'Application' },
      meta: {
        title: '平台管理',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'System:Management:Plat'
      },
      children: [
        // 应用管理
        {
          path: 'Application',
          name: 'Application',
          component: () => import('@/views/Home/Management/Plat/Application'),
          meta: {
            title: '应用管理',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:Application'
          }
        },

        // 功能菜单
        {
          path: 'Menu',
          name: 'Menu',
          component: () => import('@/views/Home/Management/Plat/Menu'),
          meta: {
            title: '功能菜单',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:Menu'
          }
        },

        // 应用管理
        {
          path: 'Application',
          name: 'OrgApplication',
          component: () => import('@/views/Home/Management/Plat/OrgApplication'),
          meta: {
            title: '应用管理',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:OrgApplication'
          }
        },

        // 功能菜单
        {
          path: 'Menu',
          name: 'OrgMenu',
          component: () => import('@/views/Home/Management/Plat/OrgMenu'),
          meta: {
            title: '功能菜单',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:OrgMenu'
          }
        },

        // 运维角色[平台]
        {
          path: 'Role',
          name: 'PlatRole',
          component: () => import('@/views/Home/Management/Plat/Role'),
          meta: {
            title: '角色权限管理',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:Role'
          }
        },

        // 运维用户[平台]
        {
          path: 'User',
          name: 'AdminUser',
          component: () => import('@/views/Home/Management/Plat/User'),
          meta: {
            title: '角色账号管理',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:User'
          }
        },

        // 参数配置
        {
          path: 'Dict',
          name: 'Dict',
          component: () => import('@/views/Home/Management/Plat/Dict'),
          meta: {
            title: '参数配置',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:Dict'
          }
        },

        // 机构管理[租户]
        {
          path: 'Organization',
          name: 'PlatOrganization',
          component: () => import('@/components/common/RouterAlive'),
          meta: {
            title: '机构管理',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'System:Management:Plat:Org'
          },
          redirect: { name: 'PlatOrganizationIndex' },
          children: [
            {
              path: '/',
              name: 'PlatOrganizationIndex',
              component: () => import('@/views/Home/Management/Plat/Organization'),
              meta: {
                title: '机构管理',
                keepAlive: true,
                isNeedLogin: true,
                isNeedOffLine: true,
                permission: 'System:Management:Plat:Organization'
              }
            },
            {
              path: ':type',
              name: 'PlatOrganizationCreate',
              component: () => import('@/views/Home/Management/Plat/Organization/Create'),
              meta: {
                title: '新建机构',
                keepAlive: false,
                isNeedLogin: true,
                isNeedOffLine: true,
                permission: 'System:Management:Plat:Organization:Create'
              },
              beforeEnter (to, from, next) {
                switch (to.params.type) {
                  case 'Create':
                    to.meta.title = '新建机构'
                    return next()
                  case 'Edit':
                    to.meta.title = '编辑机构'
                    return next()
                  default:
                    next(false)
                }
              }
            }
          ]
        },

        // 机构课程
        {
          path: 'orgCourse',
          name: 'orgCourse',
          component: () => import('@/views/Home/Management/Plat/orgCourse'),
          meta: {
            title: '机构课程',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:Course'
          }
        },
        // 课程专题
        {
          path: 'orgCourseSpe',
          name: 'orgCourseSpe',
          component: () => import('@/views/Home/Management/Plat/orgCourseSpe'),
          meta: {
            title: '课程专题',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:CoursePlus'
          }
        },
        // 课程专题详情
        {
          path: 'orgCourseSpeDe',
          name: 'orgCourseSpeDe',
          component: () => import('@/views/Home/Management/Plat/Detail'),
          meta: {
            title: '课程专题详情',
            keepAlive: false,
            isNeedLogin: false,
            isNeedOffLine: false,
            permission: ''
          }
        },

        // 机构学员（旧的）
        {
          path: 'Student',
          name: 'Student',
          component: () => import('@/views/Home/Management/Plat/Student'),
          meta: {
            title: '机构学员',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:Student'
          }
        },
        // 机构学员
        {
          path: 'orgStudents',
          name: 'orgStudents',
          component: () => import('@/views/Home/Management/Plat/OrgStudents'),
          meta: {
            title: '机构学员',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:Member'
          }
        },

        // 机构讲师
        {
          path: 'orgLecture',
          name: 'orgLecture',
          component: () => import('@/views/Home/Management/Plat/OrgLecture'),
          meta: {
            title: '机构讲师',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:Lecturer'
          }
        },

        // 机构员工
        {
          path: 'orgEmployee',
          name: 'orgEmployee',
          component: () => import('@/views/Home/Management/Plat/orgEmployee'),
          meta: {
            title: '机构员工',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:Employee'
          }
        },
        

        


        // 课程分类
        {
          path: 'wBanner',
          name: 'wBanner',
          component: () => import('@/views/Home/Management/Plat/wBanner'),
          meta: {
            title: 'Banner',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'System:Management:Plat:wBanner'
          }
        },


        // 课程分类
        {
          path: 'wCategory',
          name: 'wCategory',
          component: () => import('@/views/Home/Management/Plat/wCategory'),
          meta: {
            title: '课程分类',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:wCategory'
          }
        },

        // 课程管理
        {
          path: 'wCourse',
          name: 'wCourse',
          component: () => import('@/views/Home/Management/Plat/wCourse'),
          meta: {
            title: '课程管理',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:wCourse'
          }
        },

        // 讲师管理
        {
          path: 'wLecturer',
          name: 'wLecturer',
          component: () => import('@/views/Home/Management/Plat/wLecturer'),
          meta: {
            title: '讲师管理',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:wLecturer'
          }
        },

        // 行业资讯
        {
          path: 'wNews',
          name: 'wNews',
          component: () => import('@/components/common/RouterAlive'),
          meta: {
            title: '行业资讯',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: ''
          },
          redirect: { name: 'wNewsIndex' },
          children: [
            {
              path: '/',
              name: 'wNewsIndex',
              component: () => import('@/views/Home/Management/Plat/wNews'),
              meta: {
                title: '行业资讯',
                keepAlive: false,
                isNeedLogin: true,
                isNeedOffLine: true,
                permission: 'System:Management:Plat:wNews'
              }
            },
            {
              path: ':type',
              name: 'wNewsCreate',
              component: () => import('@/views/Home/Management/Plat/wNews/Create'),
              meta: {
                title: '新增资讯',
                keepAlive: false,
                isNeedLogin: true,
                isNeedOffLine: true,
                permission: 'System:Management:Plat:wNews:Create'
              },
              beforeEnter (to, from, next) {
                switch (to.params.type) {
                  case 'Create':
                    to.meta.title = '新增资讯'
                    return next()
                  case 'Edit':
                    to.meta.title = '编辑资讯'
                    return next()
                  default:
                    next(false)
                }
              }
            }
          ]
        },


        // 机构管理
        {
          path: 'wOrg',
          name: 'wOrg',
          component: () => import('@/views/Home/Management/Plat/wOrg'),
          meta: {
            title: '机构管理',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:wOrg'
          }
        }
      ]
    }
  ]
}

