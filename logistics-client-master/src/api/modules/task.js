const {$TASK} = require('@/api/config')

// 任务
export const Task = {
  // 分页
  page: params => $TASK.post(`tTask/page`, params),
  // 分页 - 组织管理
  pageByDept: params => $TASK.post(`tTask/pageByDept`, params),
  //  分页 - 学员标签
  pageByGroup: params => $TASK.post(`tTask/pageByGroup`, params),
  // 详情
  detail: params => $TASK.post(`tTask/detail`, params),
  // 强制结束
  forceEnd: params => $TASK.post(`tTask/force/end`, params),
  // 新增
  create: params => $TASK.post(`tTask/add`, params),
  // 修改
  update: params => $TASK.post(`tTask/update`, params),
  // 启用禁用
  enable: params => $TASK.post(`tTask/enable`, params),
  // 删除
  delete: params => $TASK.post(`tTask/delete`, params),
  // 强制结束
  end: params => $TASK.post(`tTask/force/end`, params),

  // 添加标签
  studentGroup: params => $TASK.post(`tTaskGroup/batch-add`, params),
  // 删除标签
  removeGroup: params => $TASK.post(`tTaskGroup/delete`, params),
  // 添加组织
  addDept: params => $TASK.post(`tTaskDept/batch-add`, params),
  // 删除组织
  removeDept: params => $TASK.post(`tTaskDept/delete`, params),
  // 任务组织列表
  TaskOrganizationList: params => $TASK.post('tTaskDept/list', params),
  // 任务标签列表
  TaskGroupList: params => $TASK.post('tTaskGroup/list', params),
  // 组织任务数量
  deptTaskCount: params => $TASK.post(`/tTaskDept/dept/count`, params),
  // 标签任务数量
  groupTaskCount: params => $TASK.post(`/tTaskGroup/group/count`, params)
}

// 任务分类
export const TaskCategory = {
  // 分页
  page: params => $TASK.post(`category/page`, params),
  // 列表
  childrenList: params => $TASK.post(`category/children/list`, params || {}),
  // 树
  tree: params => $TASK.post(`category/tree`, params || {}),
  // 详情
  detail: params => $TASK.post(`category/detail`, params),
  // 新增
  create: params => $TASK.post(`category/add`, params),
  // 修改
  update: params => $TASK.post(`category/update`, params),
  // 启禁用
  enable: params => $TASK.post(`category/batch-enabled`, params),
  // 删除
  delete: params => $TASK.post(`category/delete`, params),
  // 新增模板
  addTemplate: params => $TASK.post(`category/addTemplate`, params)
}

// 任务阶段
export const TStage = {
  list: params => $TASK.post(`tStage/list`, params),
  batchUpdate: params => $TASK.post(`tStage/batch-update`, params),
  create: params => $TASK.post(`tStage/add`, params),
  reset: params => $TASK.post(`tStage/reset`, params)
}

// 任务配置管理
export const TTaskConfig = {
  list: params => $TASK.post(`tTaskConfig/list`, params),
  create: params => $TASK.post(`tTaskConfig/add`, params),
  tConfigList: params => $TASK.post(`tConfig/list`, params),
  tConfigAdd: params => $TASK.post(`tTaskConfig/add`, params)
}

// 任务考试
export const TaskExam = {
  ruleAntiCheatingList: params => $TASK.post(`exam/admin/rule/antiCheating//list`, params),
  examineeSituationPage: params => $TASK.post(`exam/admin/exam/examineeSituationPage`, params),
  statistics: params => $TASK.post(`exam/admin/exam/statistics`, params),
  record: params => $TASK.post(`exam/admin/exam/record`, params),
  examRecordPage: params => $TASK.post(`exam/admin/marking/examRecordPage`, params),
  details: params => $TASK.post(`exam/admin/exam/record/details`, params),
  correct: params => $TASK.post(`exam/admin/marking/correct`, params),
  examRecordDelete: params => $TASK.post(`exam/admin/exam/record/delete`, params)
}

// 配置管理
export const TConfig = {
  list: params => $TASK.post(`tConfig/list`, params)
}


// 任务阶段
export const TTaskDetail = {
  list: params => $TASK.post(`tTaskDetail/list`, params),
  batchUpdate: params => $TASK.post(`tTaskDetail/batch-update`, params),
  create: params => $TASK.post(`tTaskDetail/add`, params),
  createExam: params => $TASK.post(`tTaskDetail/add-exam`, params),
  detailExam: params => $TASK.post(`tTaskDetail/detail-exam`, params),
  updateExam: params => $TASK.post(`tTaskDetail/update-exam`, params)
}

// 任务统计
export const TTaskStatistics = {
  stageList: params => $TASK.post(`tTaskStatistics/stage/list`, params),
  courseTop: params => $TASK.post(`tTaskStatistics/course/top`, params),
  coursePage: params => $TASK.post(`tTaskStatistics/course/page`, params),
  overViewPage: params => $TASK.post(`tTaskStatistics/overView/page`, params),
  overViewTop: params => $TASK.post(`tTaskStatistics/overView/top`, params)
}

// 任务统计
export const TCert = {
  create: params => $TASK.post(`tCert/add`, params),
  awardBatchEnabled: params => $TASK.post(`tCert/award/batch-enabled`, params),
  awardPage: params => $TASK.post(`tCert/award/page`, params),
  certAndTaskList: params => $TASK.post(`tCert/certAndTaskList`, params),
  certPage: params => $TASK.post(`tCert/certPage`, params),
  delete: params => $TASK.post(`tCert/delete`, params),
  preview: params => $TASK.post(`tCert/preview`, params),
  update: params => $TASK.post(`tCert/update`, params)
}


// 任务发放
export const TTasker = {
  // 学员分页
  studentPage: params => $TASK.post(`tTasker/student/page`, params),
  // 分页
  page: params => $TASK.post(`tTasker/page`, params),
  // 新增
  batchCreate: params => $TASK.post(`tTasker/batch-add`, params),
  // 删除
  delete: params => $TASK.post(`tTasker/delete`, params),
  //导出
  download: params => $TASK.post(`tTasker/download`, params, {responseType: 'arraybuffer'})
}


// 任务发放
export const Tasker = {
  // 学员分页
  studentPage: params => $TASK.post(`remote/student/page`, params),
  // 分页
  page: params => $TASK.post(`tasker/page`, params),
  // 新增
  create: params => $TASK.post(`tasker/add`, params),
  // 删除
  delete: params => $TASK.post(`tasker/delete`, params)
}

// 任务内容
export const TaskDetail = {
  list: params => $TASK.post(`detail/list`, params),
  listDetail: params => $TASK.post(`detail/list-detail`, params),
  // 分页
  page: params => $TASK.post(`detail/page`, params),
  // 新增
  create: params => $TASK.post(`detail/add`, params),
  // 批量新增
  addBatch: params => $TASK.post(`detail/add/batch`, params),
  // 排序
  sorting: params => $TASK.post(`detail/sorting`, params),
  // 删除
  delete: params => $TASK.post(`detail/delete`, params)
}

// 组织关联培训
export const TaskDept = {
  // 分页
  page: params => $TASK.post(`/tTaskDept/taskPage`, params),
  // 批量添加
  batchAdd: params => $TASK.post(`/tTaskDept/batch-add`, params),
  // 批量移除
  batchRemove: params => $TASK.post(`/tTaskDept/delete`, params)
}

// 标签关联培训
export const TaskGroup = {
  // 分页
  page: params => $TASK.post(`/tTaskGroup/taskPage`, params),
  // 批量添加
  batchAdd: params => $TASK.post(`/tTaskGroup/batch-add`, params),
  // 批量移除
  batchRemove: params => $TASK.post(`/tTaskGroup/delete`, params)
}