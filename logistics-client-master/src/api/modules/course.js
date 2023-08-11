const { $COURSE, $COURSELIVEADMIN } = require('@/api/config')

// 课程分类
export const CourseCategory = {
  // 分页
  page: params => $COURSE.post(`category/page`, params),
  // 列表
  list: params => $COURSE.post(`category/list`, params || {}),
  // 树
  tree: params => $COURSE.post(`category/tree`, params || {}),
  treeV2: params => $COURSE.post(`category/treeV2`, params || {}),
  // 详情
  detail: params => $COURSE.post(`category/detail`, params),
  // 新增
  create: params => $COURSE.post(`category/add`, params),
  // 修改
  update: params => $COURSE.post(`category/update`, params),
  // 启禁用
  enable: params => $COURSE.post(`category/enable`, params),
  // 删除
  delete: params => $COURSE.post(`category/delete`, params),
  // 新增模板
  addTemplate: params => $COURSE.post(`category/addTemplate`, params),

  // 根据讲师id获取分类下的数量
  countByLecturerId: params => $COURSE.post(`category/countByLecturerId`, params)
}

// 课程
export const Course = {
  // 分页
  page: params => $COURSE.post(`page`, params),
  // 基本信息详情
  baseDetail: params => $COURSE.post(`base/detail`, params),
  // 基本信息新增
  baseAdd: params => $COURSE.post(`base/add`, params),
  // 基本信息修改
  baseUpdate: params => $COURSE.post(`base/update`, params),
  // 基本信息设置（上下架等）
  baseSetting: params => $COURSE.post(`/base/setting`, params),
  // 数量统计
  count: params => $COURSE.post(`category/count`, params),
  // 删除课程
  delete: params => $COURSE.post(`delete`, params),
  // 复制课程
  copy: params => $COURSE.post(`copy`, params),
  // 新增章
  addChapter: params => $COURSE.post(`chapter/add`, params),
  // 修改章
  updateChapter: params => $COURSE.post(`chapter/update`, params),
  // 删除章
  deleteChapter: params => $COURSE.post(`chapter/delete`, params),
  // 新增节
  addLesson: params => $COURSE.post(`lesson/add`, params),
  // 修改节
  updateLesson: params => $COURSE.post(`lesson/update`, params),
  // 删除节
  deleteLesson: params => $COURSE.post(`lesson/delete`, params),
  // 新增附件
  addComponent: params => $COURSE.post(`component/add`, params),
  // 编辑附件
  updateComponent: params => $COURSE.post(`component/update`, params),
  // 删除附件
  deleteComponent: params => $COURSE.post(`component/delete`, params),
  // 修改附件名称
  changeComponent: params => $COURSE.post(`component/changeName`, params),
  // 删除学员
  removeStudent: params => $COURSE.post(`remove/student`, params),
  // 课程章节模式
  settingMode: params => $COURSE.post(`info/setting-mode`, params),
  // 课程附件列表
  listByLesson: params => $COURSE.post(`component/listByLesson`, params),
  // 章节排序
  treeSort: params => $COURSE.post(`content/tree/sort`, params),
  // 新课程详情
  contentList: params => $COURSE.post(`content/list`, params),
  // 排课详情
  contentDetail: params => $COURSE.post(`content/detail`, params),
  // 排课新增
  contentAdd: params => $COURSE.post(`content/add`, params),
  // 排课修改
  contentUpdate: params => $COURSE.post(`content/update`, params),
  // 我的课程
  myCoursePage: params => $COURSE.post(`/myCoursePage`, params),
  // 部门课程分页
  pageByDept: params => $COURSE.post(`/pageByDept`, params),
  // 部门批量添加课程
  deptBatchAdd: params => $COURSE.post(`/deptBatchAdd`, params),
  // 部门批量移除课程
  deptBatchRemove: params => $COURSE.post(`/deptBatchRemove`, params),
  // 部门课程数量
  deptCourseCount: params => $COURSE.post(`/dept/count`, params),
  // 标签课程分页
  pageByGroup: params => $COURSE.post(`/pageByGroup`, params),
  // 标签批量添加课程
  groupBatchAdd: params => $COURSE.post(`/groupBatchAdd`, params),
  // 标签批量移除课程
  groupBatchRemove: params => $COURSE.post(`/groupBatchRemove`, params),
  // 标签课程数量
  groupCourseCount: params => $COURSE.post(`/group/count`, params),




  //通过添加组织选择学员
  addDept: params => $COURSE.post(`/courseDept/batchAdd`, params),
  //通过添加标签选择学员
  studentGroup: params => $COURSE.post(`/courseGroup/batchAdd`, params),
  // 请求组织数据
  courseDept: params => $COURSE.post(`/courseDept/listAssignDept`, params),
  // 请求标签数据
  courseGroup: params => $COURSE.post(`/courseGroup/listAssignGroup`, params),
  // 删除组织
  deleteDept: params => $COURSE.post(`/courseDept/delete`, params),
  // 删除标签
  deleteGroup: params => $COURSE.post(`/courseGroup/delete`, params),


  // 批量开通课程学习权限
  // 部门指定的课程分页
  deptDialogPage: params => $COURSE.post(`/courseDept/page`, params),
  // 标签指定的课程分页
  groupDialogPage: params => $COURSE.post(`/courseGroup/page`, params),


}

// 课程学习数据
export const CourseStat = {
  // 统计
  info: params => $COURSE.post(`info/statistic`, params),
  // 每日统计
  dailyLine: params => $COURSE.post(`statistic/daily/line`, params)
}

// 课程评价
export const CourseComment = {
  // 分页
  page: params => $COURSE.post(`comment/page`, params),
  // 删除
  delete: params => $COURSE.post(`comment/delete`, params)
}

// 课程学员
export const CourseStudent = {
  // 分页
  page: params => $COURSE.post(`student/page`, params),
  // 删除
  delete: params => $COURSE.post(`student/delete`, params),
  // 导出
  export: params => $COURSE.post(`student/exportExcel`, params, { responseType: 'arraybuffer' }),
  // 批量添加
  batchAdd: params => $COURSE.post(`/student/batch/add`, params),
  // 可添加学员
  selectPage: params => $COURSE.get(`/student/select/page/${params.courseId}`, { params })
}

// 课程留言
export const CourseMessage = {
  // 分页
  page: params => $COURSE.post(`message/page`, params),
  // 详情
  detail: params => $COURSE.post(`message/detail`, params),
  // 删除
  delete: params => $COURSE.post(`message/delete`, params)
}

// 课程留言回复
export const CourseMessageReply = {
  // 分页
  page: params => $COURSE.post(`message/reply/page`, params),
  // 新增
  add: params => $COURSE.post(`message/reply/add-official`, params), // 用以区分前端用户回复
  // 修改
  update: params => $COURSE.post(`message/reply/update`, params),
  // 删除
  delete: params => $COURSE.post(`message/reply/delete`, params)
}

// 课程相关
export const CourseRemote = {
  // 机构组织职员
  employeeTree: params => $COURSE.post(`remote/dept/employee/tree`, params || {}),
  // 机构讲师
  lecturerList: params => $COURSE.post(`remote/lecturer/list`, params || {}),
  // 机构学员
  studentList: params => $COURSE.post(`/remote/student/page`, params),
  // 机构管理员组织树形列表显示
  deptTree: params => $COURSE.post(`/remote/dept/tree-list`, params),
  // 查询所有启用标签
  courseGroup: params => $COURSE.post(`/remote/group/list`, params),
  // 课程短信及站内信
  notification: params => $COURSE.post(`/remote/notification/save`, params),
  // 查询课程短信及站内信
  notificationList: params => $COURSE.post(`/remote/notification/list`, params)
}

// 课程讲义
export const CourseAttach = {
  // 分页
  page: params => $COURSE.post(`attach/page`, params),
  // 添加
  add: params => $COURSE.post(`attach/add`, params),
  // 批量添加
  addBatch: params => $COURSE.post(`attach/add/batch`, params),
  // 删除
  delete: params => $COURSE.post(`attach/delete`, params),
  // 修改名称
  changeName: params => $COURSE.post(`attach/changeName`, params)
}

// 课程直播 
export const CourseLive = {
  // 添加
  add: params => $COURSE.post(`live/link/add`, params),
  // 更新
  update: params => $COURSE.post(`live/link/update`, params),
  // 详情
  detail: params => $COURSE.post(`live/link/detail`, params),


  create: params => $COURSELIVEADMIN.post(`basicLiveShow/add`, params),

  basicLiveShowUpdate: params => $COURSELIVEADMIN.post(`basicLiveShow/update`, params),

  info: params => $COURSELIVEADMIN.post(`basicLiveShow/info`, params),

  changeStatus: params => $COURSELIVEADMIN.post(`basicLiveShow/change-status`, params),

  // 添加课程直播
  basicLiveAdd: params => $COURSELIVEADMIN.post(`basicLive/add`, params),
  // 修改直播课程
  liveUpdate: params => $COURSELIVEADMIN.post(`basicLive/update`, params),

  delete: params => $COURSELIVEADMIN.post(`basicLive/delete`, params),

  basicLiveDetail: params => $COURSELIVEADMIN.post(`basicLive/detail`, params),

  preparePage: params => $COURSELIVEADMIN.post(`basicLive/prepare/page/V2`, params),

  myLiveByUserId: params => $COURSELIVEADMIN.post(`basicLive/myLiveByUserId`, params)
}

export const CourseBasicLive = {
  // 添加课程直播
  create: params => $COURSELIVEADMIN.post(`basicLive/add`, params),
  // 修改直播课程
  update: params => $COURSELIVEADMIN.post(`basicLive/update`, params),
  updateLite: params => $COURSELIVEADMIN.post(`basicLive/update-lite`, params),
  delete: params => $COURSELIVEADMIN.post(`basicLive/delete`, params),
  detail: params => $COURSELIVEADMIN.post(`basicLive/detail`, params),
  preparePage: params => $COURSELIVEADMIN.post(`basicLive/prepare/page/V2`, params),
  myLiveByUserId: params => $COURSELIVEADMIN.post(`basicLive/myLiveByUserId`, params),
  statistic: params => $COURSELIVEADMIN.post(`basicLive/statistic`, params),
  // 直播在线人数
  statisticOnlineUsers: params => $COURSELIVEADMIN.post(`basicLive/statistic/onlineUsers`, params),
  statisticWatchLiveStudentPage: params => $COURSELIVEADMIN.post(`basicLive/statistic/watchLiveStudentPage`, params)
}

export const CourseBasicLiveShow = {
  detail: params => $COURSELIVEADMIN.post(`basicLiveShow/detail`, params),
  create: params => $COURSELIVEADMIN.post(`basicLiveShow/add`, params),
  basicLiveShowUpdate: params => $COURSELIVEADMIN.post(`basicLiveShow/update`, params),
  info: params => $COURSELIVEADMIN.post(`basicLiveShow/info`, params),
  changeStatus: params => $COURSELIVEADMIN.post(`basicLiveShow/change-status`, params)
}

export const BasicPlaybackRecord = {
  create: params => $COURSELIVEADMIN.post(`basicPlaybackRecord/add`, params),
  clipFull: params => $COURSELIVEADMIN.post(`basicPlaybackRecord/clip-full`, params),
  courseDetail: params => $COURSELIVEADMIN.post(`basicPlaybackRecord/course/detail`, params),
  coursePage: params => $COURSELIVEADMIN.post(`basicPlaybackRecord/course/page`, params),
  detail: params => $COURSELIVEADMIN.post(`basicPlaybackRecord/detail`, params),
  download: params => $COURSELIVEADMIN.post(`basicPlaybackRecord/download`, params),
  batchEnabled: params => $COURSELIVEADMIN.post(`basicPlaybackRecord/batch-enable`, params)
}


export const BasicLiveAssistant = {
  batchCreate: params => $COURSELIVEADMIN.post(`basicLiveAssistant/batch-add`, params),
  delete: params => $COURSELIVEADMIN.post(`basicLiveAssistant/delete`, params),
  list: params => $COURSELIVEADMIN.post(`basicLiveAssistant/list`, params),
  studentPage: params => $COURSELIVEADMIN.post(`basicLiveAssistant/student/page`, params)
}


export const BasicWhiteBoardRecord = {
  createStream: params => $COURSELIVEADMIN.post(`basicWhiteBoardRecord/createStream`, params),
  stopStream: params => $COURSELIVEADMIN.post(`basicWhiteBoardRecord/stopStream`, params)
}


export const BasicLiveShowTalk = {
  create: params => $COURSELIVEADMIN.post(`basicLiveShowTalk/add`, params),
  close: params => $COURSELIVEADMIN.post(`basicLiveShowTalk/close`, params)
}


// 直播课件管理
export const BasicLiveAttachment = {
  create: params => $COURSELIVEADMIN.post(`basicLiveAttachment/add`, params),
  batchCreate: params => $COURSELIVEADMIN.post(`basicLiveAttachment/batch-add`, params),
  delete: params => $COURSELIVEADMIN.post(`basicLiveAttachment/delete`, params),
  detail: params => $COURSELIVEADMIN.post(`basicLiveAttachment/detail`, params),
  list: params => $COURSELIVEADMIN.post(`basicLiveAttachment/list`, params),
  page: params => $COURSELIVEADMIN.post(`basicLiveAttachment/page`, params),
  update: params => $COURSELIVEADMIN.post(`basicLiveAttachment/update`, params)
}

// 基础直播配置
export const BasicConfig = {
  list: params => $COURSELIVEADMIN.post(`basicConfig/list`, params)
}

// 基础运营配置
export const BasicLiveConfig = {
  create: params => $COURSELIVEADMIN.post(`basicLiveConfig/add`, params),
  list: params => $COURSELIVEADMIN.post(`basicLiveConfig/list`, params)
}

// 基础运营配置
export const Playback = {
  watchLiveStudentPage: params => $COURSE.post(`playback/watchLiveStudentPage`, params),
  statistic: params => $COURSE.post(`playback/statistic`, params),
  recording: params => $COURSE.post(`playback/recording`, params)
}

// 武汉班级管理  课程信息
export const ClassesCourses = {
  page: params => $COURSE.post(`getOrgList`, params)
}