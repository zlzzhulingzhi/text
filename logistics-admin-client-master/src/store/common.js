/**
 * 字典 管理
 */
import Vue from 'vue'
import { Navbar, Pages, WCategory, Widget } from '@/api/modules/decorate'

const {
  Application,
  ApplicationType,
  CourseCategory,
  CourseRemote,
  Dept,
  Dict,
  Difficulty,
  EmployeeRole,
  Exam,
  // ExamCategory,
  labelPage,
  Organization,
  OrganizationMenu,
  OrganizationRole,
  LogisticsOrg,
  Paper,
  Question,
  QuestionType,
  ResourceType,
  Role,
  // StandardTemplate,
  TaskCategory,
  TaskExam,
  TConfig,
  TTaskConfig,
  QueryInfo,
  UniOrganization
} = require('@/api')

const state = {
  context: null,

  // 培训申请 时间段
  usePeriodList : [
    {id: 'allDay', name: '全天'},
    {id: 'morning', name: '上午'},
    {id: 'afternoon', name: '下午'},
  ],

  // 班级状态
  classStateInfo: [
    { id: 1, name: '开班', status: 'success' },
    { id: 2, name: '结班', status: 'error' }
  ],

  // 访客管理
  auditStats: [
    { id: 1, name: '通过', status: 'success' },
    { id: 0, name: '不通过', status: 'error' },
    { id: -1, name: '待审批', status: 'error' },
  ],

  passStatus: [
    { id: 0, name: '已驳回', status: 'error' },
    { id: 1, name: '已通过', status: 'success' },
  ],
  approvalStatus: [
    { id: 11, name: '待审核', status: 'warning' },
    { id: 21, name: '已审核', status: 'success' },
  ],
  currentStatus: [
    { id: 0, name: '未结束', status: 'warning' },
    { id: 1, name: '已结束', status: 'success' },
  ],
  fundingObject: [
    { id: 1, name: '网络安全培训单位' },
    { id: 2, name: '学术会议和竞赛' }
  ],

  ApplyAuditSatus: [
    { id: '2', name: '已核查' },
    { id: '5', name: '已评审' }
  ],

  inspectStatus: [
    { id: '1', name: '待核查' },
    { id: '2', name: '已核查' },
    { id: '3', name: '发布失败' },
    { id: '4', name: '已发布' },
    { id: '5', name: '已评审' }
  ],

  flowStatus: [
    { id: 1, name: '待审批', status: 'warning' },
    { id: 2, name: '已通过', status: 'success' },
    { id: 4, name: '已驳回', status: 'error' },
  ],

  approveNodeStatus: [
    { id: '10', name: '未开始' },
    { id: '11', name: '审批中' },
    { id: '21', name: '已通过' },
    { id: '22', name: '未通过' }
  ],

  // 活动、课程(资助类别)
  applyTypeStatus: [
    { id: 'allowance', name: '课程' },
    { id: 'activity', name: '活动' }
  ],

  // 报事报修类别
  repairsCategory: [
    { id: 1, name: '开课' },
    { id: 2, name: '维修' },
    { id: 3, name: '更换' },
    { id: 4, name: '结课' }
  ],


  dormitoryType: { api: QueryInfo.info, params: { code: 'dormitory' } },


  dormitoryUnit: { api: QueryInfo.info, params: { code: 'building' } },


  dormitoryFloor: { api: QueryInfo.info, params: { code: 'floor' } },


  classTypeInfo: { api: QueryInfo.info, params: { code: 'classroom' } },

  // 专家信息职称
  proTitle: { api: QueryInfo.info, params: { code: 'proTitle' } },

  // 教室面积单位
  areaUnit: {api: QueryInfo.info, params: {code: 'areaUnit'}},

  // 民族字典
  // nationDict: {api: Dict.treeList, params: { code: 'nation' }},

  // 负责人类型
  principalTypeList: [
    { id: 'user', name: '用户', status: 'success' },
    { id: 'role', name: '角色', status: 'success' },
  ],

  sex: { api: Dict.treeList, params: { code: 'sex' } },
  enabled: [
    { id: 1, name: '启用', status: 'success' },
    { id: 0, name: '禁用', status: 'error' }
  ],
  showStatus: [
    { id: 1, name: '显示', status: 'success' },
    { id: 0, name: '不显示', status: 'error' }
  ],
  shelfStatus: [
    { id: 1, name: '已上架', status: 'success' },
    { id: 0, name: '已下架', status: 'error' }
  ],
  orderStatus: [
    { id: 0, name: '已取消', status: '9' },
    { id: 1, name: '待付款', status: 'warning' },
    { id: 2, name: '已付款', status: 'success' }
  ],
  refundStatus: [
    { id: 1, name: '退款中' },
    { id: 2, name: '已到账' }
  ],
  menuType: [
    // {id: 0, name: '应用'},
    { id: 1, name: '菜单' },
    { id: 2, name: '板块' },
    { id: 3, name: '按钮' }
  ],
  orgTemplate: [
    { id: 1, name: '模板一（游客禁止访问）' },
    { id: 2, name: '模板二（游客允许访问）' }
  ],
  resourceTypeList: [
    { id: 1, name: '视频', type: 'video' },
    { id: 2, name: '音频', type: 'audio' },
    { id: 3, name: '图片', type: 'picture' },
    { id: 4, name: '文档', type: 'doc' }
    // {id: 5, name: '其他', type: 'other'}
  ],
  judgmentAnswers: [
    { id: '对', name: '对' },
    { id: '错', name: '错' }
  ],
  // 课程类型 - 有时候为了UI效果，使用2个字 , 存在疑问，为什么不统一？
  courseType: [
    { id: 2, name: '录播课', name1: '录播', value: 'record', desc: '' },
    { id: 1, name: '直播课', name1: '直播', value: 'live', desc: '' },
    { id: 3, name: '综合课', name1: '综合', value: 'mix', desc: '（录播课+直播课）' }
  ],
  // 培训项目状态
  taskStatus: [
    { id: 10, name: '未开始', status: '9' },
    { id: 20, name: '进行中', status: 'success' },
    { id: 30, name: '已结束', status: '9' },
    { id: 40, name: '中断', status: 'error' }
  ],
  taskStatusMap: {
    NotStart: 10,
    InProcess: 20
  },
  // 培训项目完成状态
  taskerStatus: [
    { id: 10, name: '未开始', status: '9' },
    { id: 20, name: '进行中', status: 'warning' },
    { id: 30, name: '按时完成', status: 'success' },
    { id: 40, name: '延期完成', status: 'success' }
  ],
  // 培训项目模式
  taskMode: [
    { id: 1, name: '通用时间模式', description: '项目有明确的开始、结束时间，适合领导力培训，新员工入职培训等专项开展的培训' },
    { id: 2, name: '周期模式', description: '长期自动进行，每个学员有独立的开始结束时间，适合例如规章制度培训，专业技能培训等内容固定，周期性进行的培训' }
  ],
  chapterTypeList: [
    { id: 1, name: '章节模式', type: 1 },
    { id: 2, name: '节模式', type: 2 },
    { id: 3, name: '无章节模式', type: 3 }
  ],
  gooded: [
    { id: 0, name: '否' },
    { id: 1, name: '是' }
  ],
  courseChapterType: [
    {
      id: 1,
      name: '添加直播',
      type: 'ZB',
      icon: require('@/assets/resource/live.png')
    },
    {
      id: 2,
      name: '添加视频',
      type: 'SP',
      icon: require('@/assets/resource/Video1.png')
    }
  ],
  fileAbbreviatedList: {
    video: {
      id: 1,
      name: '视频',
      fileType: 'video',
      fileImg: require('@/assets/resource/Video.png')
    },
    audio: {
      id: 2,
      name: '音频',
      fileType: 'audio',
      fileImg: require('@/assets/resource/Audio.png')
    },
    picture: {
      id: 3,
      name: '图片',
      fileType: 'picture',
      fileImg: require('@/assets/resource/image.png')
    },
    doc: {
      id: 4,
      name: '文档',
      fileType: 'doc',
      fileImg: require('@/assets/resource/Documentation.png')
    }
  },
  // 考试模式
  examModel: [
    {
      id: 1,
      label: '标准模式',
      description: '此模式所有考生为统一的开始时间与结束时间，考生越晚进入考试，剩余的时长越短，且只有一次考试次数',
      explain: '统一开始考试与结束考试时间的通用考试模式'
    },
    {
      id: 2,
      label: '高级模式',
      description: '在规定的入场时间内，考生无论如何进入考试，都不影响考试时长，可设置多次考试次数',
      explain: '考生独立考试时长的考试模式'
    }
  ],
  // 新建考试规则类型
  examRule: {
    1: { name: '防作弊规则', label: '' },
    2: { name: '试题规则', label: '' },
    3: { name: '考试规则', label: '' }
  },

  //培训进度表头配置
  taskColumnOptions: [
    { reg: /^realName$/, label: '学员', width: null, minWidth: 150, fixed: 'left' },
    { reg: /^totalRateProgress$/, label: '总进度', width: null, minWidth: 88, fixed: 'left' },
    { reg: /^status$/, label: '状态', width: null, minWidth: 88, fixed: 'left' },
    { reg: /^STAGE:/, label: '阶段', width: null, minWidth: 100, fixed: null },
    { reg: /^DETAIL:COURSE:/, label: '课程', width: null, minWidth: 100, fixed: null },
    { reg: /^DETAIL:EXAM:/, label: '考试', width: null, minWidth: 100, fixed: null }
  ],

  useStatus: [
    { id: 0, name: '未使用' },
    { id: 1, name: '已使用' }
  ],
  // // 培训项目分类
  // taskCategory: { api: TaskCategory.tree, params: { categoryTypeId: 1 } },
  // // 培训项目 - 阶段模式
  // stageModeList: {api: TConfig.list, params: {configParentCode: 'stage_mode'}},
  // // 培训项目 - 解锁学习模式
  // unlockModeList: {api: TConfig.list, params: {configParentCode: 'unlock_mode'}},
  // // 培训项目 - 学员添加限制
  // taskerAddList: {api: TConfig.list, params: {configParentCode: 'tasker_add_limit'}},
  // // 培训项目 - 解锁单元模式
  unlockUnitList: [
    { id: 1, name: '逐个任务解锁' },
    { id: 2, name: '逐个阶段解锁' }
  ],
  // 培训项目 - 站内信获取配置
  // noticeModeList: { api: TTaskConfig.tConfigList, params: { configParentCode: 'notice_mode' } },

  // 培训项目 - 任务类型
  businessTypeList: [
    {
      id: 2,
      name: '添加考试',
      dialog: 'DialogCreateExam',
      type: 'CreateExam',
      icon: require('@/assets/resource/exam.png')
    },
    {
      id: 1,
      name: '添加课程',
      dialog: 'DialogSelectCourse',
      type: 'SelectCourseOne',
      icon: require('@/assets/resource/course.png')
    }
  ],

  // 视频回放 - 	处理状态
  processStatus: [
    { id: -1, name: '失败' },
    { id: 1, name: '正常' },
    { id: 2, name: '转码中' }, // 剪辑转码中
    { id: 3, name: '转码中' } // 下载转码中
  ],
  // 视频回放 - 记录来源
  recordResource: [
    { id: 1, name: '原视频' },
    { id: 2, name: '剪辑' }
  ],
  // 直播详情 - 直播状态
  liveStatus: [
    { id: 0, name: '未直播' },
    { id: 1, name: '直播中' },
    { id: 2, name: '直播结束' }
  ],

  // 平台角色
  role: { api: Role.list },
  // 应用类型
  appType: { api: ApplicationType.list, params: { id: 1 } },
  // 应用列表
  app: { api: Application.list },

  // 权限树 - 平台管理菜单(平台)
  adminPermissionTree: { api: Role.adminRolePermission },
  // 权限树 - 全部菜单(机构)
  orgPermissionTree: { api: Role.orgRolePermission },
  // 权限树 - 当前机构权限菜单(机构)
  currentOrgPermissionTree: { api: OrganizationMenu.list },

  // 机构分类 列表
  orgCategory: { api: UniOrganization.dictList, params: { code: 'nature' } },
  // 平台管理员角色列表
  employeeRole: { api: EmployeeRole.page, params: { orgId: 0, enabled: 1 } },
  // 机构角色列表
  orgRoleList: { api: OrganizationRole.list },
  // 机构列表
  organization: { api: LogisticsOrg.list },

  // 机构管理员角色列表
  organizationRole: { api: OrganizationRole.list },

  // // 机构——组织列表
  // organizationDept: {api: Coupon.getOrgDept},
  // // 机构——角色列表
  // orgRole: {api: Coupon.getOrgRole},

  // 题型
  // questionType: { api: QuestionType.selectOptionList },
  // // 题目分类
  // questionCategory: { api: Question.category },
  // 题目分类 - 添加禁用标识
  // questionCategoryAll: { api: Question.categoryAll },
  // 题目难度
  // questionDifficulty: { api: Difficulty.selectOptionList },

  // 试卷分类
  // paperCategory: { api: Paper.category },

  // 考试分类
  // examCategory: {api: ExamCategory.treeList},
  // 考试防作弊规则
  // ruleAntiCheatingList: { api: Exam.ruleAntiCheatingList },

  // // 任务考试防作弊规则
  // taskRuleAntiCheatingList: { api: TaskExam.ruleAntiCheatingList },

  // // 考试难度
  // examDifficulty: { api: Exam.difficultyList },

  // 通用模板分类
  dictCategory: { api: Dict.treeList, params: { code: 't_category' } },
  // 课程分类列表
  // courseCategoryList: { api: CourseCategory.list },
  // 课程树
  // courseCategoryTree: { api: CourseCategory.tree },
  // // 课程树 加禁用标识
  // courseCategoryTreeV2: { api: CourseCategory.treeV2 },
  // 机构职员
  // deptEmployeeTree: { api: CourseRemote.employeeTree },
  // 机构讲师
  // lecturerList: { api: CourseRemote.lecturerList },
  // // 资源分类
  // resCategoryTree: { api: ResourceType.treeTotal },
  // 课程分类【平台】
  WCourseCategoryTree: { api: WCategory.tree, params: { businessCode: 'businessCourse', orgId: -1 } },
  // 组织树
  deptTree: { api: Dept.treeList, params: { parentId: 0 } },
  // 学员标签
  // studentGroupTree: { api: labelPage.listAll },
  // 课程组织
  // courseDeptTree: { api: CourseRemote.deptTree },
  // 课程标签
  // courseGroup: { api: CourseRemote.courseGroup },
  // // 新建考试
  // ruleGroup: { api: Exam.ruleGroup },

  // 微页面 列表
  waOrgPages: { api: Pages.page, params: { size: 999, pageType: 1 } },
  waH5Pages: { api: Pages.page, params: { size: 999, pageType: 2 } },

  wPageType: [
    { id: 1, name: '电脑网页端' },
    { id: 2, name: '手机移动端' }
  ],
  waOrgWidget: { api: Widget.list, params: { widgetTypeCode: 'waOrg' } },
  waH5Widget: { api: Widget.list, params: { widgetTypeCode: 'waH5' } },

  navbarOrgSelectList: { api: Navbar.selectList, params: { groupCode: 'navbarOrg' } },
  navbarH5SelectList: { api: Navbar.selectList, params: { groupCode: 'navbarH5' } }

}


const getters = {}
for (const key in state) {
  getters[key] = function (state) {
    let v = state[key]
    if (v && v.hasOwnProperty('api')) {
      setTimeout(() => {
        state.context.dispatch('common/getPublicData', key, { root: true })
      }, 30)
      return []
    }
    return v
  }
}


const mutations = {
  setList: (state, { keys, values }) => {
    values.forEach(({ data }, i) => {
      // 个例处理
      let key = keys[i]
      let apiObj = state[key]

      if (!data) return false

      if (!Array.prototype.isPrototypeOf(data)) {
        data = data.menu || data.records || data.deptList || []
      }

      if (!data.length) return false

      // 字典处理（树）
      if (data[0].dictKey) {
        if (data[0].children) {
          data = data[0].children.map(item => {
            let id = Number(item.dictKey)
            if (isNaN(id)) id = item.dictKey
            return {
              ...item,
              id,
              name: item.dictValue
            }
          })
        } else {
          data = data.map(item => {
            let id = item.dictKey
            // let id = Number(item.dictKey)
            // if (isNaN(id)) id = item.dictKey
            return {
              ...item,
              id,
              name: item.dictValue
            }
          })
        }
      }

      // 赋值
      state[key] = data

      // 重复请求 处理
      if (apiObj.hasOwnProperty('api')) {
        state[key].__proto__ = {
          __proto__: state[key].__proto__,
          ...apiObj
        }
      } else {
        state[key].__proto__ = apiObj.__proto__
      }
    })
  },
  saveContext(state, context) {
    state.context = context
  }
}

const actions = {
  async getDictionary(ctx, updateNameKeys) {
    let maps = {}

    for (const stateKey in ctx.state) {
      let v = ctx.state[stateKey]
      if (!updateNameKeys || updateNameKeys.includes(stateKey)) maps[stateKey] = v.api(v.params || {})
    }

    let values = await Promise.all(Object.values(maps))

    ctx.commit('setList', {
      keys: Object.keys(maps),
      values
    })
  },
  saveContext(ctx, that) {
    ctx.commit('saveContext', that)
  },
  async getPublicData(ctx, updateNameKeys) {
    await ctx.dispatch('getDictionary', updateNameKeys)
  },
  createFilter({ getters }) {
    for (const k in getters) {
      Vue.filter(k, function (id, name = 'name', defaultValue = '-', keyName = 'id') {
        let mapsArr = getters[k]
        if (mapsArr.find) {
          let item = mapsArr.find(v => v[keyName] == id)
          if (item) return item[name]
        }
        return defaultValue
      })
    }
  }
}


export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}