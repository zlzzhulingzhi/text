import HttpRequest from '@/api/request'

const $COURSE = new HttpRequest({
  baseURL: process.env.VUE_APP_COURSE_API
})

export const Course = {
  // 课程分页
  page: params => $COURSE.post(`/lite/page`, params),
  // 机构课程分页
  orgPage: params => $COURSE.post(`/lite/org/page`, params),
  // 机构预报学员
  orgReserveStudent: params => $COURSE.post(`/lite/org/page-reserve-student`, params),
  // 详情
  info: params => $COURSE.post(`/lite/info`, params),
  // 个人课程详情
  personalInfo: params => $COURSE.post(`/lite/personal/info`, params),
  // 我的课程
  myCourse: params => $COURSE.post(`/lite/personal/page`, params),
  // 报名
  signUp: params => $COURSE.post(`/lite/sign-up`, params),
  // 海报
  poster: params => $COURSE.post(`/lite/reporter/create`, params),
  // 活动
  activity: params => $COURSE.post('/lite/activity/page',params),
  // 活动详情
  activityDetail: params => $COURSE.post('/lite/activity/detail',params),
  // 课程/活动banner
  getBanner: params => $COURSE.post(`/lite/banner`,params)
}