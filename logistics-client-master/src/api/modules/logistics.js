const {$LOGISTICS, $ORG} = require('@/api/config')


export const OrgIndexPage = {
    // 机构列表
    orgList: params => $LOGISTICS.post(`org-list`, params),
    // 员工权限
    loginPermission: params => $LOGISTICS.post(`login-permission`, params)
  }


//教室管理 Classroom Controller
export const Classroom = {
   add: params => $LOGISTICS.post(`classroom/add`, params),
   classroomSchedulePage: params => $LOGISTICS.post(`classroom/classroomSchedulePage`, params),
   delete: params => $LOGISTICS.post(`classroom/delete`, params),
   detail: params => $LOGISTICS.post(`classroom/detail`, params),
   list: params => $LOGISTICS.post(`classroom/list`, params),
   page: params => $LOGISTICS.post(`classroom/page`, params),
   update: params => $LOGISTICS.post(`classroom/update`, params),
   updateBatch: params => $LOGISTICS.post(`classroom/updateBatch`, params),
  // 教室预订分布
  getManageClassroomState: params => $LOGISTICS.post(`manageClassroom/getManageClassroomState`, params),
  // 教室详情
  manageDetail: params => $LOGISTICS.post(`manageClassroom/manageDetail`, params),
  // 分页查询教室
  managePage: params => $LOGISTICS.post(`manageClassroom/managePage`, params)
}


//宿舍管理 Dormitory Controller
export const Dormitory = {
  add: params => $LOGISTICS.post(`dormitory/add`, params),
  delete: params => $LOGISTICS.post(`dormitory/delete`, params),
  detail: params => $LOGISTICS.post(`dormitory/detail`, params),
  list: params => $LOGISTICS.post(`dormitory/list`, params),
  page: params => $LOGISTICS.post(`dormitory/page`, params),
  update: params => $LOGISTICS.post(`dormitory/update`, params),
  updateBatch: params => $LOGISTICS.post(`dormitory/updateBatch`, params),
  // 宿舍详情
  manageDetail: params => $LOGISTICS.post(`manageDormitory/manageDetail`, params),
  // 分页查询宿舍
  managePage: params => $LOGISTICS.post(`manageDormitory/managePage`, params)
}


//班级管理 Classes Controller
export const Classes = {
  add: params => $LOGISTICS.post(`/clazz/add`, params),
  delete: params => $LOGISTICS.post(`clazz/delete`, params),
  // 班级详情中的学员列表
  detail: params => $LOGISTICS.post(`clazz/detail`, params),
  list: params => $LOGISTICS.post(`clazz/list`, params),
  page: params => $LOGISTICS.post(`clazz/page`, params),
  update: params => $LOGISTICS.post(`clazz/update`, params),

  // 查询已存在的课程信息
  syncClasses: params => $LOGISTICS.post(`/clazz/getCourseLesson`, params),
  // 批量添加课程信息
  addAlreadyClasses: params => $LOGISTICS.post(`/clazzLesson/addBatch`, params),
  // 查询已存在的课时信息
  syncClassItems: params => $LOGISTICS.post(`clazz/getCourseComponent`, params),
  // 批量添加课时信息
  addAlreadyClassesItem: params => $LOGISTICS.post(`/clazzLessonArrange/addBatch`, params),

  // 查询课程、课时信息（混合版）
  getAllCAndCI: params => $LOGISTICS.post(`/clazz/getCourseLessonAndComponent`, params),
  // 批量一次性添加
  addAllCAndCI: params => $LOGISTICS.post(`/clazzLesson/addBatch`, params),

  // 新增课程信息
  classAdd: params => $LOGISTICS.post(`clazzLesson/add`, params),
  // 新增课时信息
  classItemAdd: params => $LOGISTICS.post(`/clazzLessonArrange/add`, params),

  // 更新课程信息
  classUpdate: params => $LOGISTICS.post(`/clazzLesson/update`, params),
  // 更新课时信息
  classItemUpdate: params => $LOGISTICS.post(`/clazzLessonArrange/update`, params),

  // 课程信息列表
  classesList: params => $LOGISTICS.post(`/clazzLesson/list`, params),
  // 课时信息列表
  classItemsList: params => $LOGISTICS.post(`/clazzLessonArrange/listV2`, params),

  // 课程 删除
  classDelete: params => $LOGISTICS.post(`/clazzLesson/delete`, params),
  // 课时 删除
  classItemDelete: params => $LOGISTICS.post(`/clazzLessonArrange/delete`, params),

  // 课程 排序
  classSortUp: params => $LOGISTICS.post(`/clazzLesson/sort`, params),
  // 课时 排序
  classItemSortUp: params => $LOGISTICS.post(`/clazzLessonArrange/sort`, params),
  
}


// 新课表接口
export const newShowClasses = {
  // 起始日期、当前日期
  getStartEndCurDate: params => $LOGISTICS.post(`/clazzLessonArrange/getWeek`, params),
  // 当前周的课程
  listForAllClasses: params => $LOGISTICS.post(`/clazzLessonArrange/listV3`, params),
}


// 添加学员
export const classesAddStudents = {
  // 班级管理
  pageForClass: params => $ORG.post(`/student/getClazzOrgList`, params),
  // 课程资助申请
  pageForAllowance: params => $ORG.post(`/student/getOrgList`, params),
  // 查询所有标签
  list: params => $ORG.post(`/groups/list`, params)
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

// 班级学员管理
export const classesStudents = {
  add: params => $LOGISTICS.post(`clazzStudent/add`, params),
  delete: params => $LOGISTICS.post(`/clazzStudent/delete`, params)
}


// 讲师信息
export const Lecturers = {
  lecturersList: params => $LOGISTICS.post(`employee/getOrgList`, params)
}

// 班级管理 Clazz Controller
export const Clazz = {
  add: params => $LOGISTICS.post(`clazz/add`, params),
  delete: params => $LOGISTICS.post(`clazz/delete`, params),
  detail: params => $LOGISTICS.post(`clazz/detail`, params),
  list: params => $LOGISTICS.post(`clazz/list`, params),
  page: params => $LOGISTICS.post(`clazz/page`, params),
  update: params => $LOGISTICS.post(`clazz/update`, params),
}

// 上课签到 Clazz Sign In Controller
export const ClazzSignIn = {
  batch: params => $LOGISTICS.post(`/sign-in/batch`, params),
  pageRecord: params => $LOGISTICS.post(`/sign-in/page-record`, params),
  page: params => $LOGISTICS.post(`/sign-in/page`, params),
  classSignTable: params => $LOGISTICS.post(`/sign-in/class-sign-table`, params),
  single: params => $LOGISTICS.post(`/sign-in/single`, params),

  // 考勤导出
  export: params => $LOGISTICS.post(`/sign-in/exportExcel`, params, {responseType: 'arraybuffer'}),

  // 考勤记录
  exportRecords: params => $LOGISTICS.post(`/sign-in/getSignInRecord`, params),
}

// 机构用户
export const OrgEmployee = {
  page: params => $LOGISTICS.post(`/employee/page`, params),
  detail: params => $LOGISTICS.post(`/employee/detail`, params),
  add: params => $LOGISTICS.post(`/employee/add`, params),
  update: params => $LOGISTICS.post(`/employee/update`, params),
  batchEnabled: params => $LOGISTICS.post(`/employee/batch-enabled`, params),
  updateRole: params => $LOGISTICS.post(`/employee/update-role`, params)
}

// 机构角色
export const OrgRole = {
  page: params => $LOGISTICS.post(`/organizationRole/page`, params),
  list: params => $LOGISTICS.post(`/organizationRole/list`, params),
  detail: params => $LOGISTICS.post(`/organizationRole/detail`, params),
  add: params => $LOGISTICS.post(`/organizationRole/add`, params),
  update: params => $LOGISTICS.post(`/organizationRole/update`, params),
  delete: params => $LOGISTICS.post(`/organizationRole/delete`, params),
  batchEnabled: params => $LOGISTICS.post(`/organizationRole/batch-enabled`, params)
}

// 机构菜单
export const OrgMenu = {
  list: params => $LOGISTICS.post(`/organizationMenu/list`, params)
}

// 学员打卡
export const StuClockIn = {
  page: params => $LOGISTICS.post(`/memberClockIn/page`, params),
  // 学员打卡班级  记录
  pages: params => $LOGISTICS.post(`/memberClockIn/pages`, params),
  detail: params => $LOGISTICS.post(`/memberClockIn/detail`, params),
  add: params => $LOGISTICS.post(`/memberClockIn/add`, params),
  update: params => $LOGISTICS.post(`/memberClockIn/update`, params),
  delete: params => $LOGISTICS.post(`/memberClockIn/delete`, params),
}


