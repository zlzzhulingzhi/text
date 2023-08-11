import HttpRequest from '@/api/request'

const $LOGISTICS = new HttpRequest({
  baseURL: process.env.VUE_APP_LOGISTICS_API
})

export const Logistics = {
  // 教室导览
  classroomPage: params => $LOGISTICS.post(`/lite/plat/classroom/page`, params),
  // 教室预订列表
  classroomSchedule: params => $LOGISTICS.post(`/lite/plat/classroom/schedule`, params),
  // 宿舍导览
  dormitoryPage: params => $LOGISTICS.post(`/lite/plat/dormitory/page/v2`, params),
  // /lite/plat/dormitory/page/v2

  // 宿舍预订列表
  dormitorySchedule: params => $LOGISTICS.post(`/lite/plat/dormitory/schedule`, params),
  // 平台报事报修分页
  platMatterReportPage: params => $LOGISTICS.post(`/lite/plat/matter-report/page`, params),
  // 平台报事报修详情
  platMatterReportDetail: params => $LOGISTICS.post(`/lite/plat/matter-report/detail`, params),
  // 机构报事报修分页
  orgMatterReportPage: params => $LOGISTICS.post(`/lite/org/matter-report/page`, params),
  // 机构报事报修详情
  orgMatterReportDetail: params => $LOGISTICS.post(`/lite/org/matter-report/detail`, params),
  // 机构报事报修新增
  orgMatterReportAdd: params => $LOGISTICS.post(`/lite/org/matter-report`, params),
  // 报事报修班级列表
  matterReportClassroomList: params => $LOGISTICS.post(`/lite/matter-report/classroom/list`, params)
}

export const WorkOrder = {
  // 报事报修-关联教室-分页
  matterClassPage: params => $LOGISTICS.post(`/lite/matter-report/classroom/page`, params),
  // 机构-报事报修-新增
  matterReportAdd: params => $LOGISTICS.post(`/lite/org/matter-report`, params),
  // 机构-报事报修分页
  matterReportPage: params => $LOGISTICS.post(`/lite/org/matter-report/page`, params),
  // 机构-报事报修详情
  matterReportDetail: params => $LOGISTICS.post(`/lite/org/matter-report/detail`, params),
  // 平台-报事报修-详情
  platMatterReportDetail: params => $LOGISTICS.post(`/lite/plat/matter-report/detail`, params),
  // 平台-报事报修-机构列表
  matterReportOrgList: params => $LOGISTICS.post(`/lite/plat/matter-report/org/list`, params),
  // 平台-报事报修-分页
  platMatterReportPage: params => $LOGISTICS.post(`/lite/plat/matter-report/page`, params),
  // 平台-开班信息-分页
  openClassPage: params => $LOGISTICS.post(`/lite/plat/clazz/page`,params),
  // /lite/org/list 教务机构列表
  orgList: params => $LOGISTICS.post(`/lite/org/list`,params)
}

export const SignRecord = {
  // 新增学员打卡记录
  /**
   *   
    {
    "clazzId": 0,
    "clockInDate": "",
    "latitude": "",
    "longitude": "",
    "remark": "",
    "siteCode": "ss【宿舍】 jxl【教学楼】"
    }
  *
   */
  memberClockInAdd: params => $LOGISTICS.post(`/lite/memberClockIn/add`,params),
  // 分页查询学员打卡记录
  memberClockInPage: params => $LOGISTICS.post(`/lite/memberClockIn/page`,params),
  // 可选班级列表
  selectClazzList: params => $LOGISTICS.post(`/lite/memberClockIn/select/clazz`,params),
  // 日历学员打卡详情记录
  clockInDetail: params => $LOGISTICS.post(`/lite/memberClockIn/calendar/detail`,params),
  // 日历学员打卡记录 月份
  clockInMonthRecord: params => $LOGISTICS.post(`/lite/memberClockIn/calendar/list`,params)
}

// 学员访客记录
export const MemberVisit = {
  // 分页
  page: params => $LOGISTICS.post(`/memberVisit/lite/page`, params),
  // 详情
  detail: params => $LOGISTICS.post(`/memberVisit/detail`, params),
  // 新增
  add: params => $LOGISTICS.post(`/memberVisit/add`, params)
}


// 机构信息
export const OrgInfo = {
  list: params => $LOGISTICS.post(`/organization/list`, params),
}


// 统计概览
export const Static = {
  // 数据总览
  topSum: params => $LOGISTICS.get(`/lite/stat/overview`, {params}),
  // 培训班List
  attendClass: params => $LOGISTICS.get(`/lite/stat/attend-class`, {params}),
  // 培训班page
  attendClassPage: params => $LOGISTICS.post(`/lite/stat/attend-class/page`, params),
  // 宿舍占用数量/空闲数量
  dormitories: params => $LOGISTICS.get(`/lite/stat/dorm-usage-rate`, {params}),
  dormitoriesV2: params => $LOGISTICS.get(`/lite/stat/dorm-usage-rate/v2`, {params}),
  // 教室占用数量/空闲数量
  classrooms: params => $LOGISTICS.get(`/lite/stat/classroom-usage-rate`, {params}),
}