const {$EXAM} = require('@/api/config')

// Category Controller
export const ExamCategory = {
  create: params => $EXAM.post(`admin/category/add`, params),
  page: params => $EXAM.post(`admin/category/page`, params),
  treeList: params => $EXAM.post(`admin/category/treeList`, params),
  childrenList: params => $EXAM.post(`admin/category/children/list`, params),
  detail: params => $EXAM.post(`admin/category/detail`, params),
  delete: params => $EXAM.post(`admin/category/delete`, params),
  update: params => $EXAM.post(`admin/category/update`, params),
  enable: params => $EXAM.post(`admin/category/enable`, params)
}

// Exam Controller
export const Exam = {
  deptTreeAndEmployee: params => $EXAM.post(`admin/deptTreeAndEmployee`, params),
  difficultyList: params => $EXAM.post(`admin/difficulty/list`, params),
  ruleAntiCheatingList: params => $EXAM.post(`admin/rule/antiCheating/list`, params),
  create: params => $EXAM.post(`admin/exam/add/v2`, params),
  page: params => $EXAM.post(`admin/exam/page`, params),
  newPage: params => $EXAM.post(`admin/exam/page/v3`, params),
  onShelf: params => $EXAM.post(`admin/exam/onShelf`, params),
  offShelf: params => $EXAM.post(`admin/exam/offShelf`, params),
  detail: params => $EXAM.post(`admin/exam/detail`, params),
  update: params => $EXAM.post(`admin/exam/update/v2`, params),
  enable: params => $EXAM.post(`admin/exam/enable`, params),
  statistics: params => $EXAM.post(`admin/exam/page/v3`, params),
  record: params => $EXAM.post(`admin/exam/record`, params),
  ranking: params => $EXAM.post(`admin/exam/ranking`, params),
  correctRate: params => $EXAM.post(`admin/exam/questionCorrectRate`, params),
  export: params => $EXAM.post(`admin/exam/ranking/export`, params, {responseType: 'blob'}),
  analysis: params => $EXAM.post(`admin/exam/questionAnalysis`, params),
  details: params => $EXAM.post(`admin/exam/record/details`, params),
  delete: params => $EXAM.post(`admin/exam/record/delete`, params),
  studentPage: params => $EXAM.post(`admin/student/page`, params),
  removeStudent: params => $EXAM.post(`admin/remove/student`, params),
  ruleGroup: params => $EXAM.post(`/admin/rule/group`, params),
  examDelete: params => $EXAM.post(`admin/exam/delete`, params),
  examStart: params => $EXAM.post(`admin/exam/start`, params),
  examEnd: params => $EXAM.post(`admin/exam/end`, params),



  // 考试详情指派学员
  // 获取新增组织
  batchAddDept: params => $EXAM.post(`admin/examDept/batchAdd`, params),
  // 获取新增标签
  batchAddGroup: params => $EXAM.post(`admin/examGroup/batchAdd`, params),
  // 获取已有的组织
  listAssignDept: params => $EXAM.post(`admin/examDept/listAssignDept`, params),
  // 获取已有的标签
  listAssignGroup: params => $EXAM.post(`admin/examGroup/listAssignGroup`, params),
  // 删除组织
  deleteDept: params => $EXAM.post(`admin/examDept/delete`, params),
  // 删除标签
  deleteGroup: params => $EXAM.post(`admin/examGroup/delete`, params),
  // 考生分页数据
  userExamRecord: params => $EXAM.post(`admin/exam/assignUserExamRecord/page`, params),
  // 删除考试记录
  deleteRecord: params => $EXAM.post(`admin/exam/record/delete`, params),
  // 请求学生分页数据
  selectPage: params => $EXAM.post(`admin/exam/examAssignUser/page`, params),

  // 考试直接指派学员
  batchAdd: params => $EXAM.post(`admin/exam/assignUser`, params),
  // 导出考试记录
  examExport: params => $EXAM.post(`admin/exam/record/export`, params, {responseType: 'arraybuffer'})


}

// examinee-live
export const ExamineeLiveRoom = {
  page: params => $EXAM.post(`admin/examineeLiveRoom/page`, params),
  live: params => $EXAM.post(`admin/examineeLiveRoom/live`, params),
  update: params => $EXAM.post(`admin/examineeLiveRoom/update`, params),
  detail: params => $EXAM.post(`admin/examineeLiveRoom/detail`, params)
}

export const ExamScoring = {
  page: params => $EXAM.post(`admin/exam/pages`, params),
  recordPage: params => $EXAM.post(`admin/exam/record/page`, params),
  mark: params => $EXAM.post(`admin/exam/mark`, params)
  // details: params => $EXAM.post(`admin/exam/record/details`, params),
}


export const ExamMarking = {
  page: params => $EXAM.post(`admin/marking/page`, params),
  examRecordPage: params => $EXAM.post(`admin/marking/examRecordPage`, params),
  correct: params => $EXAM.post(`admin/marking/correct`, params)
}

export const Invigilation = {
  terminateExam: params => $EXAM.post(`admin/invigilation/terminateExam`, params)
}

export const ExamCert = {
  //考试中的证书
  certAndExamList: params => $EXAM.post(`/admin/tCert/certAndExamList`, params),
  //添加证书到考试
  addCert: params => $EXAM.post(`/admin/tCert/add`, params),
  //从考试中删除证书
  deleteCert: params => $EXAM.post(`/admin/tCert/delete`, params),
  //查看证书中用户
  awardPage: params => $EXAM.post(`/admin/tCert/award/page`, params),
  //证书中心的证书列表
  certPage: params => $EXAM.post(`/admin/tCert/certPage`, params),
  //作废掉某人的证书
  batchEnabled: params => $EXAM.post(`/admin/tCert/batch-enabled`, params)
}