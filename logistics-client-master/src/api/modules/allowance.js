const {$ALLOWANCE} = require('@/api/config')


//  机构入驻申请新增 
export const AllowanceApply = {
  qualificationPage: params => $ALLOWANCE.post(`/apply/qualification/page`, params), //机构入驻申请新增
  qualificationSubmit: params => $ALLOWANCE.post(`/apply/qualification/submit`, params), //机构入驻申请新增


  settleDetail: params => $ALLOWANCE.post(`/apply/settle-detail`, params), //培训申请详情
  applyWorkflowNodes: params => $ALLOWANCE.post(`/apply/applyWorkflowNodes`,params), //培训申请审批详情
  settlePage: params => $ALLOWANCE.post(`/apply/settle-page`, params),  //培训申请申请分页
  settleAdd: params => $ALLOWANCE.post(`/apply/settle-add`, params), //培训申请新增
  update: params => $ALLOWANCE.post(`/applySettle/update`,params), //培训申请申请  修改
  submit: params => $ALLOWANCE.post(`/apply/settle/submit`,params), //培训申请申请  提交
  delete: params => $ALLOWANCE.post(`/applySettle/remove`,params), //培训申请申请  删除
  copyApply: params => $ALLOWANCE.post(`/applySettle/copy`,params), //培训申请申请  复制


  // 培训申请附件-列表
  attachList: params => $ALLOWANCE.post(`/applySettle/attach/list`, params),
  // 培训申请附件-删除
  attachRemove: params => $ALLOWANCE.post(`/applySettle/attach/remove`, params),
  // 培训申请附件-保存
  attachSave: params => $ALLOWANCE.post(`/applySettle/attach/save`, params),
}

// 资质申请表单管理 Apply Qualification Controller
export const ApplyQualification = {
  // 资质申请附件-列表
  attachList: params => $ALLOWANCE.post(`/qualification/attach/list`, params),
  // 资质申请附件-删除
  attachRemove: params => $ALLOWANCE.post(`/qualification/attach/remove`, params),
  // 资质申请附件-保存
  attachSave: params => $ALLOWANCE.post(`/qualification/attach/save`, params),
  // 资质申请-详情
  detail: params => $ALLOWANCE.post(`/qualification/detail`, params),
  // 资质申请项目计划-新增
  projectAdd: params => $ALLOWANCE.post(`/qualification/project/add`, params),
  // 资质申请项目计划-编辑
  projectUpdate: params => $ALLOWANCE.post(`/qualification/project/update`, params),
  // 资质申请项目计划-删除
  projectRemove: params => $ALLOWANCE.post(`/qualification/project/remove`, params),
  // 资质申请-删除
  remove: params => $ALLOWANCE.post(`/qualification/remove`, params),
  // 资质申请-保存
  save: params => $ALLOWANCE.post(`/qualification/save`, params),
  // 资质申请-编辑
  update: params => $ALLOWANCE.post(`/qualification/update`, params),
  //  资质申请-复制申请
  copyApply: params => $ALLOWANCE.post(`/qualification/copy`, params),
}



// 资助申请
export const ApplyCost = {
  // 资助申请前判断有无进行资质申请
  isGotQualification: params => $ALLOWANCE.post(`/qualification/check`, params),

  // 课程资助申请
  // 课程申请附件-列表
  attachListCourse: params => $ALLOWANCE.post(`/applyAllowance/attach/list`, params),
  // 课程申请附件-删除
  attachRemoveCourse: params => $ALLOWANCE.post(`/applyAllowance/attach/remove`, params),
  // 课程申请附件-保存
  attachSaveCourse: params => $ALLOWANCE.post(`/applyAllowance/attach/save`, params),

  // 课程申请-详情
  detailCourse: params => $ALLOWANCE.post(`/applyAllowance/detail`, params),
  // 课程申请-分页
  pageCourse: params => $ALLOWANCE.post(`/apply/allowance/page`, params),
  // 课程申请-删除
  removeCourse: params => $ALLOWANCE.post(`/applyAllowance/remove`, params),
  // 课程申请-保存
  saveCourse: params => $ALLOWANCE.post(`/applyAllowance/save`, params),
  // 课程申请-编辑
  updateCourse: params => $ALLOWANCE.post(`/applyAllowance/update`, params),

  // 课程申请提交
  submitCourse: params => $ALLOWANCE.post(`/apply/allowance/submit`, params),
  // 课程申请  复制申请
  copyApplyForCourse: params => $ALLOWANCE.post(`/applyAllowance/copy`, params),





  // 活动资助申请
  // 活动申请附件-列表
  attachListActivity: params => $ALLOWANCE.post(`/applyActivity/attach/list`, params),
  // 活动申请附件-删除
  attachRemoveActivity: params => $ALLOWANCE.post(`/applyActivity/attach/remove`, params),
  // 活动申请附件-保存
  attachSaveActivity: params => $ALLOWANCE.post(`/applyActivity/attach/save`, params),

  // 活动申请-详情
  detailActivity: params => $ALLOWANCE.post(`/applyActivity/detail`, params),
  // 活动申请-分页
  pageActivity: params => $ALLOWANCE.post(`/apply/activity/page`, params),
  // 活动申请-删除
  removeActivity: params => $ALLOWANCE.post(`/applyActivity/remove`, params),
  // 活动申请-保存
  saveActivity: params => $ALLOWANCE.post(`/applyActivity/save`, params),
  // 活动申请-编辑
  updateActivity: params => $ALLOWANCE.post(`/applyActivity/update`, params),

  // 活动申请提交
  submitActivity: params => $ALLOWANCE.post(`/apply/activity/submit`, params),
  // 课程申请  复制申请
  copyApplyForActivity: params => $ALLOWANCE.post(`/applyActivity/copy`, params),
}

