const {$LOGISTICS} = require('@/api/config')
// 远程服务统一请求接口 Remote Controller
export const Remote = {
  orgPage: params => $LOGISTICS.post(`/remote/org/page`, params)
}

// 教室管理
export const Classroom = {
  classroomAdd: params => $LOGISTICS.post(`/classroom/add`, params),
  classroomDelete: params => $LOGISTICS.post(`/classroom/delete`, params),
  classroomDetail: params => $LOGISTICS.post(`/classroom/detail`, params),
  classroomPage: params => $LOGISTICS.post(`/classroom/page`, params),
  classroomUpdate: params => $LOGISTICS.post(`/classroom/update`, params),
  classroomUpdateBatch: params => $LOGISTICS.post(`/classroom/updateBatch`, params),
  // 教室预订分布
  getClassroomState: params => $LOGISTICS.post(`/classroom/getClassroomState`, params),


  // 报修管理
  repairsPage: params => $LOGISTICS.post(`/plat/page`, params),
  // 报修详情
  repairsDetail: params => $LOGISTICS.post(`/plat/detail`, params),


  // 教室  资费详情
  feeDetail: params => $LOGISTICS.post(`/classroom/detailFee`, params),


  // 导入教室
  preImport: params => $LOGISTICS.post(`/classroom/preview`, params.fileFormData),
  import: params => $LOGISTICS.post(`/classroom/batchAdd`, params),

  // 教室预订统计
  classroomSummary: params => $LOGISTICS.post(`/classroom/getClassroomSummary`, params),
  classroomSummaryDetail: params => $LOGISTICS.post(`/classroom/getClassroomSummaryDetail`, params),
}

// 运营统计
export const OperationalStatistics = {
  // 教室预订统计
  classroomSummary: params => $LOGISTICS.post(`/classroom/getClassroomSummary`, params),
  classroomSummaryDetail: params => $LOGISTICS.post(`/classroom/getClassroomSummaryDetail`, params),


  // 机构培训统计
  orgClassPage: params => $LOGISTICS.post(`/plat/clazz/getClazzSummary`, params),
  orgClassTotalSummary: params => $LOGISTICS.post(`/plat/clazz/getClazzTotalSummary`, params),
}

// 宿舍管理
export const Dormitory = {
    dormitoryAdd: params => $LOGISTICS.post(`/dormitory/add`, params),
    dormitoryDelete: params => $LOGISTICS.post(`/dormitory/delete`, params),
    dormitoryDetail: params => $LOGISTICS.post(`/dormitory/detail`, params),
    dormitoryPage: params => $LOGISTICS.post('/dormitory/page', params),
    dormitoryUpdate: params => $LOGISTICS.post(`/dormitory/update`, params),
    dormitoryUpdateBatch: params => $LOGISTICS.post(`/dormitory/updateBatch`, params),
}

// 访客管理
export const visitorManage = {
    add: params => $LOGISTICS.post(`/memberVisit/add`, params),
    delete: params => $LOGISTICS.post(`/memberVisit/delete`, params),
    detail: params => $LOGISTICS.post(`/memberVisit/detail`, params),
    page: params => $LOGISTICS.post('/memberVisit/page', params),
    update: params => $LOGISTICS.post(`/memberVisit/update`, params),
}



// 场景设备管理
export const SceneDevice = {
    sceneDeviceAdd: params => $LOGISTICS.post(`/sceneDevice/add`, params),
    sceneDeviceDelete: params => $LOGISTICS.post(`/sceneDevice/delete`, params),
    sceneDeviceDetail: params => $LOGISTICS.post(`/sceneDevice/detail`, params),
    sceneDeviceList: params => $LOGISTICS.post(`/sceneDevice/list`, params),
    sceneDevicePage: params => $LOGISTICS.post('/sceneDevice/page', params),
    sceneDeviceUpdate: params => $LOGISTICS.post(`/sceneDevice/update`, params),
    // sceneDeviceUpdateBatch: params => $LOGISTICS.post(`/sceneDevice/updateBatch`, params),
}

//机构管理 Organization Controller
export const LogisticsOrg = {
  create: params => $LOGISTICS.post(`/organization/add`, params),
  delete: params => $LOGISTICS.post(`/organization/delete`, params),
  detail: params => $LOGISTICS.post(`/organization/detail`, params),
  detailAdmin: params => $LOGISTICS.post(`/organization/admin/detail`, params),
  batchEnabled: params => $LOGISTICS.post(`organization/batch-enabled`, params),

  // 机构列表
  list: params => $LOGISTICS.post(`/organization/list`, params),

  page: params => $LOGISTICS.post(`/organization/page`, params),
  info: params => $LOGISTICS.post(`/organization/info`, params),
  update: params => $LOGISTICS.post(`/organization/update`, params),
  adminUpdate: params => $LOGISTICS.post(`/organization/admin/update`, params),
  categoryList: params => $LOGISTICS.post(`/organization/category/list`, params),
  orgDeptTree: params => $LOGISTICS.post(`/organization/org-dept-tree`, params),
  adminOrgDept: params => $LOGISTICS.post(`/organization/admin/org-dept`, params),


  // 机构班级
  orgClassPage: params => $LOGISTICS.post(`/plat/clazz/page`, params),
  orgClassDetail: params => $LOGISTICS.post(`/plat/clazz/detail`, params),

  // 新机构班级
  orgClassPageV2: params => $LOGISTICS.post(`/plat/clazz/pageV2`, params),

  // 课时信息列表
  classItemsList: params => $LOGISTICS.post(`/clazzLessonArrange/listV2`, params),
}


//职工用户管理 Employee Inner Controller
export const LogisticsEmployee = {
  create: params => $LOGISTICS.post(`/employee/add`, params),
  delete: params => $LOGISTICS.post(`/employee/delete`, params),
  detail: params => $LOGISTICS.post(`/employee/detail`, params),
  page: params => $LOGISTICS.post(`/employee/page`, params),
  resetPassword: params => $LOGISTICS.post(`/employee/reset-password`, params),
  list: params => $LOGISTICS.post(`/employee/list`, params),
  batchAdminEnabled: params => $LOGISTICS.post(`/employee/admin/batch-enabled`, params),
  adminCreate: params => $LOGISTICS.post(`/employee/admin/add`, params),
  adminUpdate: params => $LOGISTICS.post(`/employee/admin/update`, params),
  batchCreate: params => $LOGISTICS.post(`/employee/batch/add`, params),
  batchAdminCreate: params => $LOGISTICS.post(`/employee/batch/admin/add`, params),
  adminList: params => $LOGISTICS.post(`/employee/admin/list`, params),
  pageAdmin: params => $LOGISTICS.post(`/employee/admin/page`, params),
  update: params => $LOGISTICS.post(`/employee/update`, params),
  updateRole: params => $LOGISTICS.post(`/employee/update-role`, params),
  loginPermission: params => $LOGISTICS.post(`employee/login-permission`, params),
  count: params => $LOGISTICS.post(`/employee/count`, params),
  // 创建学员账号
  createStudent: params => $LOGISTICS.post(`/employee/create-student`, params),
  categoryCount: params => $LOGISTICS.post(`/employee/category/count`, params)
}


// 入住情况  宿舍排期
export const DormitorySchedule = {
  preImport: params => $LOGISTICS.post(`/dormitorySchedule/preview`, params.fileFormData),
  delete: params => $LOGISTICS.post(`/dormitorySchedule/delete`, params),
  page: params => $LOGISTICS.post(`/dormitorySchedule/page`, params),
  batchAdd: params => $LOGISTICS.post('/dormitorySchedule/batchAdd', params),
  getDormitoryState: params => $LOGISTICS.post('/dormitory/getDormitoryState', params),
  // 空余各种房型的数量
  getDormitoryNum: params => $LOGISTICS.post('/dormitory/getDormitoryCount', params),
}


// 房态信息
export const DormitoryRemainImport = {
  // 基本信息部分
  // 分页
  page: params => $LOGISTICS.post(`/dormitoryStat/page`, params),
  // 添加
  add: params => $LOGISTICS.post(`/dormitoryStat/add`, params),
  // 删除
  delete: params => $LOGISTICS.post(`/dormitoryStat/delete`, params),
  // 更新
  update: params => $LOGISTICS.post(`/dormitoryStat/update`, params),
  // 详情
  detail: params => $LOGISTICS.post(`/dormitoryStat/detail`, params),

  // 导入剩余数量部分
  // 导入结果分页
  importPage: params => $LOGISTICS.post(`/dormitoryStat/import/page`, params),
  // 预导入
  preImport: params => $LOGISTICS.post(`/dormitoryStat/import/preview`, params.fileFormData),
  // 确定导入
  import: params => $LOGISTICS.post(`/dormitoryStat/import/exec/${params}`, params),
}


// 新课表接口
export const newShowClasses = {
  // 起始日期、当前日期
  getStartEndCurDate: params => $LOGISTICS.post(`/clazzLessonArrange/getWeek`, params),
  // 当前周的课程
  listForAllClasses: params => $LOGISTICS.post(`/clazzLessonArrange/listV3`, params),
}