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
          component: () => import('@/views/Home/Management/Plat/Organization'),
          meta: {
            title: '机构管理',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: false,
            permission: 'System:Management:Plat:Organization'
          }
        },
        // 机构班级 (旧的)
        // {
        //   path: 'orgClass',
        //   name: 'orgClass',
        //   component: () => import('@/views/Home/Management/Plat/orgClass'),
        //   meta: {
        //     title: '机构员工',
        //     keepAlive: true,
        //     isNeedLogin: true,
        //     isNeedOffLine: true,
        //     permission: 'System:Management:Plat:OrgClass'
        //   }
        // },
        // 机构班级 （新的）
        {
          path: 'orgClassV2',
          name: 'orgClassV2',
          component: () => import('@/views/Home/Management/Plat/orgClassV2'),
          meta: {
            title: '机构员工',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:OrgClass'
          }
        },
        // 机构员工
        {
          path: 'Employee',
          name: 'Employee',
          component: () => import('@/views/Home/Management/Plat/Employee'),
          meta: {
            title: '机构员工',
            keepAlive: true,
            isNeedLogin: true,
            isNeedOffLine: true,
            permission: 'System:Management:Plat:Employee'
          }
        },
      ]
    }
  ]
}


// 宿舍管理
export const Dormitory = {
  path: 'Dormitory',
  name: 'Dormitory',
  component: () => import('@/components/common/RouterAlive'),
  redirect: { name: '' },
  meta: { title: '管理中心', keepAlive: false, isNeedLogin: true, isNeedOffLine: true, permission: 'susheguanli' },
  children: [
    // 宿舍管理
    {
      path: 'DormitoryManagement',
      name: 'DormitoryManagement',
      component: () => import('@/views/Home/Dormitory/DormitoryManagement'),
      meta: {
        title: '宿舍管理',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Dormitory'
      }
    },
    // 房态监控
    {
      path: 'RoomState',
      name: 'RoomState',
      component: () => import('@/views/Home/Dormitory/RoomState'),
      meta: {
        title: '房态监控',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Dormitory:Schedule'
      }
    },
    // 入住情况
    {
      path: 'DormitoryInfo',
      name: 'DormitoryInfo',
      component: () => import('@/views/Home/Dormitory/DormitoryInfo'),
      meta: {
        title: '入住情况',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Dormitory:Record'
      }
    },
    // 房间剩余情况
    {
      path: 'DormitoryRemainInfo',
      name: 'DormitoryRemainInfo',
      component: () => import('@/views/Home/Dormitory/DormitoryRemainInfo'),
      meta: {
        title: '房态信息',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Dormitory:Stat'
      }
    }
  ]
}


// 访客管理
export const VisitorManage = {
  path: 'VisitorManage',
  name: 'VisitorManage',
  component: () => import('@/components/common/RouterAlive'),
  redirect: { name: '' },
  meta: { title: '访客管理', keepAlive: false, isNeedLogin: true, isNeedOffLine: true, permission: 'Management:Visitor' },
  children: [
    // 访客管理
    {
      path: 'VisitorManage',
      name: 'VisitorManage',
      component: () => import('@/views/Home/Visitor/VisitorManage'),
      meta: {
        title: '访客管理',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Visitor:Record'
      }
    },
  ]
}


// 教室管理
export const Classroom = {
  path: 'Classroom',
  name: 'Classroom',
  component: () => import('@/components/common/RouterAlive'),
  redirect: { name: '' },
  meta: { title: '管理中心', keepAlive: false, isNeedLogin: true, isNeedOffLine: true, permission: 'jiaoshiguanli1' },
  children: [
    // 教室预订审批
    {
      path: 'ClassroomOrder',
      name: 'ClassroomOrder',
      component: () => import('@/views/Home/Classroom/ClassroomOrder/'),
      meta: {
        title: '教室预订审批',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Classroom:Approve'
      }
    },
    // 培训申请详情
    {
      path: 'Detail',
      name: 'FacilityApplyDetail',
      component: () => import ('@/views/Home/Classroom/setFacilityApply/Detail'),
      meta: {
        title: '培训申请详情',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Logistics:Apply:View'
      }
    },

    // 教室预订分布
    {
      path: 'ClassroomOrderInfo',
      name: 'ClassroomOrderInfo',
      component: () => import('@/views/Home/Classroom/ClassroomOrderInfo'),
      meta: {
        title: '教室预订分布',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Classroom:Schedule'
      }
    },

    // 教室申请记录
    {
      path: 'ClassroomApplyInfo',
      name: 'ClassroomApplyInfo',
      component: () => import('@/views/Home/Classroom/ClassroomApplyRecords'),
      meta: {
        title: '教室申请记录',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Apply:Record'
      }
    },
    // 教室预订结果
    {
      path: 'ClassroomResult',
      name: 'ClassroomResult',
      component: () => import('@/views/Home/Classroom/ClassroomResult'),
      meta: {
        title: '教室管理预订结果',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'System:Classroom:Result'
      }
    },
    // 教室管理
    {
      path: 'ClassroomManagement',
      name: 'ClassroomManagement',
      component: () => import('@/views/Home/Classroom/ClassroomManagement'),
      meta: {
        title: '教室管理',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Classroom'
      }
    },
    // 设备管理
    {
      path: 'EquipmentManagement',
      name: 'EquipmentManagement',
      component: () => import('@/views/Home/Classroom/EquipmentManagement'),
      meta: {
        title: '教室管理',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Class:Device'
      }
    },
    // 报修管理
    {
      path: 'RepairsManagement',
      name: 'RepairsManagement',
      component: () => import('@/views/Home/Classroom/RepairsManagement'),
      meta: {
        title: '报修管理',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Classroom:Repairs'
      }
    }

  ]
}


// 运营统计
export const OperationalStatistics = {
  path: 'OperationalStatistics',
  name: 'OperationalStatistics',
  component: () => import('@/components/common/RouterAlive'),
  redirect: { name: 'ClassroomStatistics' },
  meta: { title: '运营统计', keepAlive: false, isNeedLogin: true, isNeedOffLine: true, permission: 'Management:Statistic' },
  children: [
    // 教室预订统计
    {
      path: 'ClassroomStatistics',
      name: 'ClassroomStatistics',
      component: () => import('@/components/common/RouterAlive'),
      redirect: { name: 'ClassroomStatisticsList' },
      meta: {
        title: '教室预订统计',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Classroom:Statistics'
      },
      children: [
        {
          // 教室预订统计列表
          path: 'List',
          name: 'ClassroomStatisticsList',
          component: () => import('@/views/Home/OperationalStatistics/ClassroomStatisticsList'),
          meta: {
            title: '教室预订统计列表',
            keepAlive: false,
            isNeedLogin: false,
            isNeedOffLine: false,
            permission: 'Management:Classroom:Statistics:List'
          }
        },
        {
          // 教室预订统计详情
          path: 'Detail',
          name: 'ClassroomStatisticsDetail',
          component: () => import('@/views/Home/OperationalStatistics/ClassroomStatisticsDetail'),
          meta: {
            title: '教室预订统计详情',
            keepAlive: false,
            isNeedLogin: false,
            isNeedOffLine: false,
            permission: 'Management:Classroom:Statistics:Detail'
          }
        }
      ]
    },
    // 机构培训统计
    {
      path: 'OrgTrainStatistics',
      name: 'OrgTrainStatistics',
      component: () => import('@/components/common/RouterAlive'),
      redirect: { name: 'OrgTrainStatisticsList' },
      meta: { title: '机构培训统计', keepAlive: false, isNeedLogin: true, isNeedOffLine: true, permission: 'Management:Train:Statistics' },
      children: [
        {
          // 机构培训统计列表
          path: 'List',
          name: 'OrgTrainStatisticsList',
          component: () => import('@/views/Home/OperationalStatistics/OrgTrainStatisticsList'),
          meta: {
            title: '机构培训统计列表',
            keepAlive: false,
            isNeedLogin: false,
            isNeedOffLine: false,
            permission: 'Management:Train:Statistics:List'
          }
        },
      ]
    }
  ]
}


// 流程管理
export const FlowManagement = {
  path: 'FlowManagement',
  name: 'FlowManagement',
  component: () => import('@/components/common/RouterAlive'),
  redirect: { name: '' },
  meta: { title: '流程管理', keepAlive: false, isNeedLogin: true, isNeedOffLine: true, permission: 'Management:Flow' },
  children: [
    // 流程维护
    {
      path: 'FlowManage',
      name: 'FlowManage',
      component: () => import('@/views/Home/FlowManagement/FlowManage/'),
      meta: {
        title: '流程维护',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'System:Flow'
      }
    }
  ]
}


// 评审专家
export const Expert = {
  path: 'ExpertInfo',
  name: 'ExpertInfo',
  component: () => import('@/views/Home/FundManagement/ExpertInfo'),
  meta: {
    title: '评审专家信息',
    keepAlive: false,
    isNeedLogin: true,
    isNeedOffLine: true,
    permission: 'Management:Expert'
  }
}


// 资助管理
export const FundManagement = {
  path: 'FundManagement',
  name: 'FundManagement',
  component: () => import('@/components/common/RouterAlive'),
  redirect: { name: 'QualificationReview' },
  meta: { title: '管理中心', keepAlive: false, isNeedLogin: true, isNeedOffLine: true, permission: 'zizhuguanli' },
  children: [
    // 资助资格审核
    {
      path: 'QualificationReview',
      name: 'QualificationReview',
      component: () => import('@/views/Home/FundManagement/QualificationReview'),
      meta: {
        title: '资助资格审核',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Qualification:Approve'
      }
    },
    // 资助申请审核
    {
      path: 'ApplyCheck',
      name: 'ApplyCheck',
      component: () => import('@/views/Home/FundManagement/ApplyCheck'),
      meta: {
        title: '资助资格评审',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Allowance:Apply:Check'
      }
    },
    // 资助申请公示
    {
      path: 'ApplyPub',
      name: 'ApplyPub',
      component: () => import('@/views/Home/FundManagement/ApplyPub'),
      meta: {
        title: '资助申请公示',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Allowance:Apply:Pub'
      }
    },
    // 预估资金明细
    {
      path: 'Predict',
      name: 'Predict',
      component: () => import('@/views/Home/FundManagement/Predict'),
      meta: {
        title: '预估资金明细',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Management:Allowance:Predict'
      }
    },
    {
      path: 'Detail',
      name: 'ApplyQualificationsDetail',
      component: () => import ('@/views/Home/FundManagement/Detail'),
      meta: {
        title: '资助申请详情',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Approve:QualificationDetail'
      }
    },
    {
      path: 'CourseDetail',
      name: 'ApplyCourseDetail',
      component: () => import ('@/views/Home/FundManagement/CourseDetail'),
      meta: {
        title: '课程申请详情',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Approve:AllowanceDetail'
      }
    },
    {
      path: 'ActivityDetail',
      name: 'ApplyActivityDetail',
      component: () => import ('@/views/Home/FundManagement/ActivityDetail'),
      meta: {
        title: '活动申请详情',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: false,
        permission: 'Approve:ActivityDetail'
      }
    },
  ]
}

// 资助统计表
export const Statistic = {
  path: 'Statistic',
  name: 'Statistic',
  component: () => import('@/components/common/RouterAlive'),
  redirect: { name: '' },
  meta: { title: '资助统计表', keepAlive: false, isNeedLogin: true, isNeedOffLine: true, permission: 'Allowance:Statistic' },
  children: [
    // 机构资助统计
    {
      path: 'applyStatistics',
      name: 'applyStatistics',
      component: () => import('@/views/Home/Statistic/applyStatistics'),
      meta: {
        title: '机构资助统计',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Allowance:Statistic:Apply'
      }
    },
    // 课程资助统计
    {
      path: 'moneyStatistics',
      name: 'moneyStatistics',
      component: () => import('@/views/Home/Statistic/moneyStatistics'),
      meta: {
        title: '课程资助统计',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Allowance:Statistic:Amount'
      }
    },
    // 学员资助统计
    {
      path: 'studentStatistics',
      name: 'studentStatistics',
      component: () => import('@/views/Home/Statistic/studentStatistics'),
      meta: {
        title: '学员资助统计',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Allowance:Statistic:Student'
      }
    },
    // 年资助金额统计
    {
      path: 'orgStatistics',
      name: 'orgStatistics',
      component: () => import('@/views/Home/Statistic/orgStatistics'),
      meta: {
        title: '年资助金额统计',
        keepAlive: false,
        isNeedLogin: true,
        isNeedOffLine: true,
        permission: 'Allowance:Statistic:Org'
      }
    }
  ]
}