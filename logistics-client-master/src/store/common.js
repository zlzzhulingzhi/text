/**
 * 字典 管理
 */
import Vue from 'vue'
import { Navbar, Pages, WCategory, Widget } from '@/api/modules/decorate'

const {
  Application,
  ApplicationType,
  Coupon,
  CourseCategory,
  CourseRemote,
  Dept,
  Dict,
  Difficulty,
  EmployeeRole,
  Exam,
  ExamCategory,
  labelPage,
  Organization,
  OrganizationMenu,
  OrganizationRole,
  OrgRole,
  Paper,
  Question,
  QuestionType,
  ResourceType,
  Role,
  StandardTemplate,
  TaskCategory,
  TaskExam,
  TConfig,
  TTaskConfig,
  OrgIndexPage,
  OrgInfoAPI,
  Classroom,
  QueryInfo,
  SceneDevice,
  UniOrg,
  classesAddStudents
} = require('@/api')

const state = {
  context: null,

  // 教室预定分布 时间段
  usePeriodList : [
    {id: 'allDay', name: '全天'},
    {id: 'morning', name: '上午'},
    {id: 'afternoon', name: '下午'},
  ],
  // 考试结果
  examResult: [
    {id: 1, name: '优'},
    {id: 2, name: '良'},
    {id: 3, name: '中'},
    {id: 4, name: '差'},
    {id: 5, name: '通过'},
    {id: 6, name: '不通过'},
  ],

  clockInArea: [
    {id: 'jxl', name: '教学楼', status: 'success', wordSign: ''},
    {id: 'ss', name: '宿舍', status: 'warning', wordSign: '假'},
    {id: '', name: '所有区域', status: 'error', wordSign: '假'},
  ],

  signInStatus: [
    {id: 1, name: '上课', status: 'success', wordSign: ''},
    {id: 2, name: '请假', status: 'warning', wordSign: '假'},
    {id: 3, name: '旷课', status: 'error', wordSign: '旷'},
  ],

  applyResult: [
    { id: 0, name: '不通过', status: 'error' },
    { id: 1, name: '通过', status: 'success' },
    { id: null, name: '待审批', status: 'warning' },
  ],
  flowStatus: [
    { id: 1, name: '待审批', status: 'warning' },
    { id: 2, name: '已通过', status: 'success' },
    { id: 4, name: '已驳回', status: 'error' },
  ],

  approveNodeStatus:[
    {id: '10', name: '未开始'},
    {id: '11', name: '审批中'},
    {id: '21', name: '已通过'},
    {id: '22', name: '未通过'}
  ],

  dormitoryStatus: [
    { id: '0', name: '正常' },
    { id: '1', name: '自住' },
    { id: '2', name: '维修' },
    { id: '3', name: '工作' },
  ],
  dormitoryApplyStatus: [
    { id: 0, name: '在租中' },
    { id: 1, name: '已退房' },
  ],
  dormitoryApprovalStatus: [
    { id: 0, name: '待审批', status: 'warning' },
    { id: 1, name: '已审批', status: 'success' },
    { id: 2, name: '已驳回', status: 'error' },
  ],

  isRight: [
    { id: 1, name: '是' },
    { id: 0, name: '否' },
  ],
  classesStatus: [
    { id: 1, name: '已开班', status: 'success' },
    { id: 0, name: '未开班', status: 'error' }
  ],

  classStatus: [
    { id: 1, name: '已预订' },
    { id: 2, name: '使用中' },
    { id: 3, name: '已取消' },
    { id: 4, name: '已使用' },
  ],

  classroomApprovalStatus: [
    { id: 0, name: '待审批', status: 'warning' },
    { id: 1, name: '已审批', status: 'success' },
    { id: 2, name: '已驳回', status: 'error' },
  ],


 
  classStateInfo: [
    { id: 1, name: '开班', status: 'success' },
    { id: 2, name: '结班', status: 'error' }
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
  useStatus: [
    { id: 0, name: '未使用' },
    { id: 1, name: '已使用' }
  ],
  // 机构讲师
  lecturerList: { api: CourseRemote.lecturerList },
  // 教室分类列表
  classTypeInfo: { api: QueryInfo.info, params: { code: 'classroom' } },
  // 宿舍分类列表
  dormitoryType: { api: QueryInfo.info, params: { code: 'dormitory' } },
  // 建筑单元列表
  dormitoryUnit: { api: QueryInfo.info, params: { code: 'building' } },
  // 楼层列表
  dormitoryFloor: { api: QueryInfo.info, params: { code: 'floor' } },
  // 培训项目分类
  taskCategory: { api: TaskCategory.tree, params: { categoryTypeId: 1 } },
  // 培训项目 - 阶段模式
  stageModeList: { api: TConfig.list, params: { configParentCode: 'stage_mode' } },
  // 培训项目 - 解锁学习模式
  unlockModeList: { api: TConfig.list, params: { configParentCode: 'unlock_mode' } },
  // 培训项目 - 学员添加限制
  taskerAddList: { api: TConfig.list, params: { configParentCode: 'tasker_add_limit' } },
  // 培训项目 - 解锁单元模式
  unlockUnitList: [
    { id: 1, name: '逐个任务解锁' },
    { id: 2, name: '逐个阶段解锁' }
  ],
  // 培训项目 - 站内信获取配置
  noticeModeList: { api: TTaskConfig.tConfigList, params: { configParentCode: 'notice_mode' } },

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
  appType: {api: ApplicationType.list, params: {id: 1}},
  // 应用列表
  app: {api: Application.list},

  // 设备列表
  sceneDeviceInfo: { api: SceneDevice.sceneDeviceList,params:{category: "Classroom"} },

  classRoomList: { api: Classroom.list },
  // 权限树 - 平台管理菜单(平台)
  adminPermissionTree: { api: Role.adminRolePermission },
  // 权限树 - 全部菜单(机构)
  orgPermissionTree: { api: Role.orgRolePermission },
  // 权限树 - 当前机构权限菜单(机构)
  currentOrgPermissionTree: { api: OrganizationMenu.list },

  // 机构分类 列表
  orgCategory: {api: UniOrg.dictList, params: {code: 'nature'}},
  // 平台管理员角色列表
  employeeRole: { api: EmployeeRole.page, params: { orgId: 0, enabled: 1 } },
  // 机构角色列表
  orgRoleList: { api: OrganizationRole.list },
  // 机构列表
  organization: { api: Organization.list },

  // 机构管理员角色列表
  organizationRole: { api: OrgRole.list },

  // 机构——组织列表
  organizationDept: { api: Coupon.getOrgDept },
  // 机构——角色列表
  orgRole: { api: Coupon.getOrgRole },

  // 题型
  questionType: { api: QuestionType.selectOptionList },
  // 题目分类
  questionCategory: { api: Question.category },
  // 题目分类 - 添加禁用标识
  questionCategoryAll: { api: Question.categoryAll },
  // 题目难度
  questionDifficulty: { api: Difficulty.selectOptionList },

  // 试卷分类
  paperCategory: { api: Paper.category },

  // 考试分类
  examCategory: { api: ExamCategory.treeList },
  // 考试防作弊规则
  ruleAntiCheatingList: { api: Exam.ruleAntiCheatingList },

  // 任务考试防作弊规则
  taskRuleAntiCheatingList: { api: TaskExam.ruleAntiCheatingList },

  // 考试难度
  examDifficulty: { api: Exam.difficultyList },

  // 通用模板分类
  dictCategory: { api: Dict.treeList, params: { code: 't_category' } },
  // 课程分类列表
  courseCategoryList: { api: CourseCategory.list },
  // 课程树
  courseCategoryTree: { api: CourseCategory.tree },
  // 课程树 加禁用标识
  courseCategoryTreeV2: { api: CourseCategory.treeV2 },
  // 机构职员
  deptEmployeeTree: { api: CourseRemote.employeeTree },

  // 资源分类
  resCategoryTree: { api: ResourceType.treeTotal },
  // 课程分类【平台】
  WCourseCategoryTree: { api: WCategory.tree, params: { businessCode: 'businessCourse', orgId: -1 } },
  // 组织树
  deptTree: { api: Dept.treeList, params: { parentId: 0 } },
  // 学员标签
  studentGroupTree: { api: classesAddStudents.list },
  // 课程组织
  courseDeptTree: { api: CourseRemote.deptTree },
  // 课程标签
  courseGroup: { api: CourseRemote.courseGroup },
  // 新建考试
  ruleGroup: { api: Exam.ruleGroup },

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
  navbarH5SelectList: { api: Navbar.selectList, params: { groupCode: 'navbarH5' } },

  // 证书配置项
  certSearchConfig: { api: StandardTemplate.searchConfig },
  certTemplateList: { api: StandardTemplate.page, params: { enabled: 1, size: 999 } },

  // orgList: { api: OrgIndexPage.orgList }
  myOrgList: { api: OrgIndexPage.orgList }
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
  saveContext (state, context) {
    state.context = context
  }
}


const actions = {
  async getDictionary (ctx, updateNameKeys) {
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
  saveContext (ctx, that) {
    ctx.commit('saveContext', that)
  },
  async getPublicData (ctx, updateNameKeys) {
    await ctx.dispatch('getDictionary', updateNameKeys)
  },
  createFilter ({ getters }) {
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