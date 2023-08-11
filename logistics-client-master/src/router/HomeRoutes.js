// 首页
export const Main = {
  path: '/',
  name: 'Main',
  component: () => import('@/components/common/RouterAlive'),
  meta: {
    title: '首页',
    keepAlive: false,
    isNeedLogin: true,
    isNeedOffLine: false,
    permission: 'System:Main'
  },
  redirect: {name: 'Index'},
  children: [
    {
      path: 'Index',
      name: 'Index',
      component: () => import ('@/views/Home/Index/Org'),
      meta: {
        title: '我的机构',
        keepAlive: false,
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
        keepAlive: false,
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
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'System:Main:View'
      }
    }
  ]
}

// 后勤服务
export const Logistics = {
  path: 'Logistics',
  name: 'Logistics',
  component: () => import('@/components/common/RouterAlive'),
  meta: {
    title: '后勤申请',
    keepAlive: false,
    isNeedLogin: true,
    isNeedOffLine: false,
    permission: 'Logistics'
  },
  redirect: {name: 'FacilityApply'},
  children: [
    // 教室预订情况
    {
      path: 'ClassroomOrderInfo',
      name: 'ClassroomOrderInfo',
      component: () => import ('@/views/Home/Logistics/ClassroomOrderInfo'),
      meta: {
        title: '教室预订情况',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Org:Classroom:Schedule'
      }
    },


    // 教室宿舍申请
    {
      path: 'FacilityApply',
      name: 'FacilityApply',
      component: () => import ('@/components/common/RouterAlive'),
      meta: {
        title: '培训申请',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Logistics:Apply'
      },
      redirect: {name: 'FacilityApplyList'},
      children: [
        {
          path: 'List',
          name: 'FacilityApplyList',
          component: () => import ('@/views/Home/Logistics/FacilityApply'),
          meta: {
            title: '培训申请列表',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Logistics:Apply:List'
          }
        },
        {
          path: 'Create',
          name: 'FacilityApplyCreate',
          component: () => import ('@/views/Home/Logistics/setFacilityApply/Create'),
          meta: {
            title: '新建培训申请',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Logistics:Apply:Create'
          }
        },
        {
          path: 'Edit',
          name: 'FacilityApplyEdit',
          component: () => import ('@/views/Home/Logistics/setFacilityApply/Edit'),
          meta: {
            title: '编辑培训申请',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Logistics:Apply:Edit'
          }
        },
        {
          path: 'Detail',
          name: 'FacilityApplyDetail',
          component: () => import ('@/views/Home/Logistics/setFacilityApply/Detail'),
          meta: {
            title: '培训申请详情',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Logistics:Apply:View'
          }
        }
      ]
    }
  ]
}

// 资助申请
export const Allowance = {
  path: 'Allowance',
  name: 'Allowance',
  component: () => import('@/components/common/RouterAlive'),
  meta: {
    title: '资助申请',
    keepAlive: false,
    isNeedLogin: true,
    isNeedOffLine: false,
    permission: 'Allowance'
  },
  redirect: {name: 'ApplyQualifications'},
  children: [
    // 资助资质申请  二级菜单
    {
      path: 'Qualifications',
      name: 'ApplyQualifications',
      component: () => import('@/components/common/RouterAlive'),
      redirect: {name: 'ApplyQualificationsList'},
      meta: {
        title: '资助资质申请',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Allowance:Apply:Qualifications'
      },
      // 资助资质申请  列表、新建、编辑、详情页面
      children: [
        {
          path: 'List',
          name: 'ApplyQualificationsList',
          component: () => import ('@/views/Home/Allowance/Qualifications'),
          meta: {
            title: '资助资质申请列表',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Allowance:Apply:Qualifications:List'
          }
        },
        {
          path: 'Create',
          name: 'ApplyQualificationsCreate',
          component: () => import ('@/views/Home/Allowance/Qualifications/Create'),
          meta: {
            title: '新建资质申请',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Allowance:Apply:Qualifications:Create'
          }
        },
        {
          path: 'Edit',
          name: 'ApplyQualificationsEdit',
          component: () => import ('@/views/Home/Allowance/Qualifications/Edit'),
          meta: {
            title: '编辑资质申请',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Allowance:Apply:Qualifications:Edit'
          }
        },
        {
          path: 'Detail',
          name: 'ApplyQualificationsDetail',
          component: () => import ('@/views/Home/Allowance/Qualifications/Detail'),
          meta: {
            title: '资质申请详情',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Allowance:Apply:Qualifications:View'
          }
        }
      ]
    },


    // 资助费用申请  二级菜单
    {
      path: 'Cost',
      name: 'ApplyCost',
      component: () => import ('@/components/common/RouterAlive'),
      redirect: {name: 'ApplyCostList'},
      meta: {
        title: '资助费用申请',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Allowance:Apply:Cost'
      },
      children: [
        // 资助费用列表页  包含两tab页
        {
          path: 'CostList',
          name: 'ApplyCostList',
          component: () => import ('@/views/Home/Allowance/FundingCosts'),
          // redirect: {name: ''},
          meta: {
            title: '资助费用申请',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Allowance:Apply:Cost:List'
          },
        },

        
        // 活动资助费用申请  新建、编辑、详情页面
        {
          path: 'ActivityCreate',
          name: 'ApplyCostActivityCreate',
          component: () => import ('@/views/Home/Allowance/FundingCosts/ForActivity/Create'),
          meta: {
            title: '活动资助费用申请',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Allowance:Apply:Cost:ActivityCreate'
          }
        },
        {
          path: 'ActivityEdit',
          name: 'ApplyCostActivityEdit',
          component: () => import ('@/views/Home/Allowance/FundingCosts/ForActivity/Edit'),
          meta: {
            title: '活动资助费用编辑',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Allowance:Apply:Cost:ActivityEdit'
          }
        },
        {
          path: 'ActivityDetail',
          name: 'ApplyCostActivityDetail',
          component: () => import ('@/views/Home/Allowance/FundingCosts/ForActivity/Detail'),
          meta: {
            title: '活动资助费用详情',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Allowance:Apply:Cost:ActivityDetail'
          }
        },
        

        // 课程资助费用申请  新建、编辑、详情页面
        {
          path: 'CourseCreate',
          name: 'ApplyCostCourseCreate',
          component: () => import ('@/views/Home/Allowance/FundingCosts/ForCourse/Create'),
          meta: {
            title: '课程资助费用申请',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Allowance:Apply:Cost:CourseCreate'
          }
        },
        {
          path: 'CourseEdit',
          name: 'ApplyCostCourseEdit',
          component: () => import ('@/views/Home/Allowance/FundingCosts/ForCourse/Edit'),
          meta: {
            title: '课程资助费用编辑',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Allowance:Apply:Cost:CourseEdit'
          }
        },
        {
          path: 'CourseDetail',
          name: 'ApplyCostCourseDetail',
          component: () => import ('@/views/Home/Allowance/FundingCosts/ForCourse/Detail'),
          meta: {
            title: '课程资助费用详情',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'Allowance:Apply:Cost:CourseDetail'
          }
        }
      ]
    },
  ]
}

// 班级管理  一级菜单
export const ClassesManagement = {
  path: 'ClassesManagement',
  name: 'ClassesManagement',
  component: () => import('@/components/common/RouterAlive'),
  meta: {
    title: '班级管理',
    keepAlive: false,
    isNeedLogin: true,
    isNeedOffLine: false,
    permission: 'Class'
  },
  redirect: {name: 'ClassesManage'},
  children: [
    // 班级管理  二级菜单
    // {
    //   path: 'Manage',
    //   name: 'ClassesManage',
    //   component: () => import ('@/views/Home/ClassesManagement/Manage'),
    //   meta: {
    //     title: '班级管理',
    //     keepAlive: false,
    //     isNeedLogin: true,
    //     isNeedOffLine: false,
    //     permission: 'Class:Manage'
    //   },
    // },
    // 新版班级管理
    {
      path: '/',
      name: 'ClassesManage',
      component: () => import('@/components/common/RouterAlive'),
      meta: {
        title: '',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Class:Manage'
      },
      redirect: {name: 'classesList'},
      children: [
        {
          path: 'list',
          name: 'classesList',
          component: () => import ('@/views/Home/ClassesManagement/List'),
          meta: {
            title: '班级列表',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: ''
          },
        },
        {
          path: 'create',
          name: 'classesCreate',
          component: () => import ('@/views/Home/ClassesManagement/Create'),
          meta: {
            title: '新增班级',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: ''
          },
        },
        {
          path: 'edit',
          name: 'ClassEdit',
          component: () => import ('@/views/Home/ClassesManagement/Edit'),
          meta: {
            title: '编辑班级',
            keepAlive: false,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: ''
          },
        }
      ]
    },
    // 打卡详情
    {
      path: 'studentClockDetail',
      name: 'studentClockDetail',
      component: () => import ('@/views/Home/ClassesManagement/studentClockDetail'),
      meta: {
        title: '打卡记录',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Class:studentClockDetail'
      },
    },
    // 培训签到  二级菜单
    {
      path: 'SignIn',
      name: 'SignIn',
      component: () => import ('@/views/Home/ClassesManagement/SignIn'),
      meta: {
        title: '培训签到',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Class:SignIn'
      }
    },
    // 班级考勤  二级菜单
    {
      path: 'Attend',
      name: 'Attend',
      component: () => import ('@/views/Home/ClassesManagement/Attend'),
      meta: {
        title: '班级考勤',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Class:Attend'
      }
    },
    // 考勤打卡  二级菜单
    {
      path: 'ClockIn',
      name: 'ClockIn',
      component: () => import ('@/views/Home/ClassesManagement/ClockIn'),
      meta: {
        title: '考勤打卡',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Class:ClockIn'
      }
    },
  ]
}

// 系统管理  一级菜单
export const SystemManagement = {
  path: 'SystemManagement',
  name: 'SystemManagement',
  component: () => import('@/components/common/RouterAlive'),
  meta: {
    title: '系统管理',
    keepAlive: false,
    isNeedLogin: true,
    isNeedOffLine: false,
    permission: 'System'
  },
  redirect: {},
  children: [
    // 用户管理  二级菜单
    {
      path: 'UserManagement',
      name: 'UserManagement',
      component: () => import ('@/views/Home/SystemManagement/UserManagement'),
      meta: {
        title: '用户管理',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'System:Management:Org:User'
      }
    },
    // 角色管理  二级菜单
    {
      path: 'RoleManagement',
      name: 'RoleManagement',
      component: () => import ('@/views/Home/SystemManagement/RoleManagement'),
      meta: {
        title: '角色管理',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'System:Management:Org:Role'
      }
    }
  ]
}