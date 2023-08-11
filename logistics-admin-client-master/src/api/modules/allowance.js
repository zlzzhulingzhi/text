const { $ALLOWANCE } = require('@/api/config')

// 审批业务 Approve Controller
export const Approve = {
  // 机构入驻申请审批
  settle: params => $ALLOWANCE.post(`approve/settle`, params),
  // 机构入驻申请详情
  detail: params => $ALLOWANCE.post(`approve/settle/detail`, params),
  // 机构入驻申请分页
  page: params => $ALLOWANCE.post(`approve/settle/page`, params),
  pageV2: params => $ALLOWANCE.post(`approve/settle/pageV2`, params),

  // 教室申请记录  分页
  pageRecords: params => $ALLOWANCE.post(`approve/pageRecord`, params),
  // 教室申请记录  作废
  recordInvalidate: params => $ALLOWANCE.post(`approve/settle/invalidated`, params),

  // 教室预订结果详情
  classDetail: params => $ALLOWANCE.post(`approve/settle-classroom/detail`, params),
  // 教室预订结果分页
  classPage: params => $ALLOWANCE.post(`approve/settle-classroom/page`, params),
  // 学术活动申请审批
  activity: params => $ALLOWANCE.post(`approve/activity`, params),
  // 学术活动申请附件-列表
  activityAttachList: params => $ALLOWANCE.post(`approve/activity/attach/list`, params),
  // 学术活动申请-详情
  activityDetail: params => $ALLOWANCE.post(`approve/activity/detail`, params),
  // 学术活动申请-分页
  activityPage: params => $ALLOWANCE.post(`approve/activity/page`, params),
  // 课程申请审批
  allowance: params => $ALLOWANCE.post(`approve/allowance`, params),
  // 课程申请附件-列表
  allowanceList: params => $ALLOWANCE.post(`approve/allowance/attach/list`, params),
  // 课程申请-详情
  allowanceDetail: params => $ALLOWANCE.post(`approve/allowance/detail`, params),
  // 课程申请-分页
  allowancePage: params => $ALLOWANCE.post(`approve/allowance/page`, params),
  // 资质申请审批
  qualification: params => $ALLOWANCE.post(`approve/qualification`, params),
  // 资质申请附件-列表
  qualificationlist: params => $ALLOWANCE.post(`approve/qualification/attach/list`, params),
  // 资质申请-详情
  qualificationdDetail: params => $ALLOWANCE.post(`approve/qualification/detail`, params),
  // 资质申请-分页
  qualificationPage: params => $ALLOWANCE.post(`approve/qualification/page`, params),
  // 申请节点 - 状态（详情）
  approveDetail: params => $ALLOWANCE.post(`approve/applyWorkflowNodes`, params),

  // 升级为 万人计划
  setOrgPlanOrg: params => $ALLOWANCE.post(`approve/qualification/trigger`, params),


  // 统计待审批申请数量
  getQualificationNum: params => $ALLOWANCE.post(`approve/qualificationCount`, params),
  getCostNum: params => $ALLOWANCE.post(`approve/allowanceAndActivityCount`, params),
  getClassroomApplyNum: params => $ALLOWANCE.post(`approve/classroomCount`, params),


  // 培训申请附件-列表
  attachList: params => $ALLOWANCE.post(`approve/settle/attach/list`, params),
}

// 专家信息模块
export const Expert = {
  // 新增专家信息
  add: params => $ALLOWANCE.post(`expert/add`, params),
  // 删除专家信息
  delete: params => $ALLOWANCE.post(`expert/delete`, params),
  // 专家信息详情
  detail: params => $ALLOWANCE.post(`expert/detail`, params),
  // 分页查询专家信息
  page: params => $ALLOWANCE.post(`expert/page`, params),
  // 更新专家信息
  update: params => $ALLOWANCE.post(`expert/update`, params),
}

// 流程管理模块
export const Workflow = {
  // 流程定义维护
  // 新增流程定义
  add: params => $ALLOWANCE.post(`workflow/add`, params),
  // 删除流程定义
  delete: params => $ALLOWANCE.post(`workflow/delete`, params),
  // 流程定义详情
  detail: params => $ALLOWANCE.post(`workflow/detail`, params),
  // 分页查询流程定义
  page: params => $ALLOWANCE.post(`workflow/page`, params),
  // 更新流程定义
  update: params => $ALLOWANCE.post(`workflow/update`, params),
  // 批量启用/禁用
  updateBatch: params => $ALLOWANCE.post(`workflow/updateBatch`, params),



  // 流程节点维护
  // 新增流程节点
  nodeAdd: params => $ALLOWANCE.post(`workflowNode/add`, params),
  // 删除流程节点
  nodeDelete: params => $ALLOWANCE.post(`workflowNode/delete`, params),
  // 流程节点详情
  nodeDetail: params => $ALLOWANCE.post(`workflowNode/detail`, params),
  // 分页查询流程节点
  nodePage: params => $ALLOWANCE.post(`workflowNode/page`, params),
  // 更新流程节点
  nodeUpdate: params => $ALLOWANCE.post(`workflowNode/update`, params),
  // 批量新增
  nodeAddBatch: params => $ALLOWANCE.post(`workflowNode/addBatch`, params),


}

// 资助统计模块
export const FundingStatistics = {
  // 机构资助统计 分页
  institutionalFundingStatistics: params => $ALLOWANCE.post(`stat/allowance/org/page`, params),
  // 机构资助统计 子分页
  institutionalFundingStatisticsSub: params => $ALLOWANCE.post(`stat/allowance/org/sub-page`, params),
  // 机构资助统计 汇总
  institutionalFundingStatisticsSummary: params => $ALLOWANCE.post(`stat/allowance/org/summary`, params),
  // 年度资助统计(费用) 
  annualFundingStatisticsFee: params => $ALLOWANCE.post(`stat/allowance/yearly/fee-chart`, params),
  // 年度资助统计- 列表
  annualFundingStatisticsFeeList: params => $ALLOWANCE.post(`stat/allowance/yearly/list`, params),
  // 年度资助统计-饼图-费用统计
  annualFundingStatisticsFeePie: params => $ALLOWANCE.post(`stat/allowance/yearly/pie/fee-chart`, params),
  // 年度资助统计-饼图-课时人次统计
  annualFundingStatisticsStudyPie: params => $ALLOWANCE.post(`stat/allowance/yearly/pie/study-chart`, params),
  // 年度资助统计-课时人次统计
  annualFundingStatisticsStudy: params => $ALLOWANCE.post(`stat/allowance/yearly/study-chart`, params),
  // 资助统计-机构列表
  orgList: params => $ALLOWANCE.post('stat/allowance/org/list', params),

  // 课程资助统计汇总
  courseSummary: params => $ALLOWANCE.post(`stat/allowance/class/summary`, params),
  // 课程资助统计分页
  courseStaticsPage: params => $ALLOWANCE.post(`stat/allowance/class/page`, params),
  // 课程资助统计子分页
  courseStaticsSubPage: params => $ALLOWANCE.post(`stat/allowance/class/sub-page`, params),
  
  // 活动资助统计汇总
  activitySummary: params => $ALLOWANCE.post(`stat/allowance/activity/summary`, params),
  // 活动资助统计分页
  activityStaticsPage: params => $ALLOWANCE.post(`stat/allowance/activity/page`, params),

  // 学员资助统计汇总
  studentSummary: params => $ALLOWANCE.post(`stat/allowance/student/summary`, params),
  // 学员资助统计分页
  studentStaticsPage: params => $ALLOWANCE.post(`stat/allowance/student/page`, params),
  // 学员资助统计子分页
  studentStaticsSubPage: params => $ALLOWANCE.post(`stat/allowance/student/sub-page`, params),

}

// 评审专家
export const ReviewExport = {
  // 可选评审专家列表
  selectList: params => $ALLOWANCE.post(`expert/list`, params),

  // 评审专家 列表
  ExportList: params => $ALLOWANCE.post(`approve/applyAuditExpert/list`, params),

  // 评审专家 新建
  ExportCreate: params => $ALLOWANCE.post(`approve/applyAuditExpert/add`, params),

  // 评审专家 更新
  ExportUpdate: params => $ALLOWANCE.post(`approve/applyAuditExpert/update`, params),

  // 评审专家 删除
  ExportDelete: params => $ALLOWANCE.post(`approve/applyAuditExpert/delete`, params),


  // 评审附件 列表
  AttachList: params => $ALLOWANCE.post(`approve/applyAuditExpert/attach/list`, params),

  // 评审附件 新建
  AttachCreate: params => $ALLOWANCE.post(`approve/applyAuditExpert/attach/save`, params),

  // 评审附件 删除
  AttachDelete: params => $ALLOWANCE.post(`approve/applyAuditExpert/attach/remove`, params),
}

