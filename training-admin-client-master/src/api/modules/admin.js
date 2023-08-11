const {$ADMIN, $COURSE} = require('@/api/config')

// 机构管理 UniOrganization Controller
export const UniOrganization = {
  create: params => $ADMIN.post(`/uniOrg/add`, params),
  delete: params => $ADMIN.post(`/uniOrg/delete`, params),
  detail: params => $ADMIN.post(`/uniOrg/detail`, params),
  list: params => $ADMIN.post(`/uniOrg/list`, params),
  page: params => $ADMIN.post(`/uniOrg/page`, params),
  update: params => $ADMIN.post(`/uniOrg/update`, params),

  innerInfo: params => $ADMIN.post(`/uniOrg/inner/info`, params),
  innerList: params => $ADMIN.post(`/uniOrg/inner/list`, params),
  innerPage: params => $ADMIN.post(`/uniOrg/inner/page`, params),

  dictList: params => $ADMIN.post(`/uniOrg/dict/list`, params),
}

// 机构课程、专题课程
export const OrgCourse = {
  // 分页 查询
  page: params => $COURSE.post(`/WCourse/page`, params),
  // 新 分页 查询
  pageV2: params => $COURSE.post(`/WCourse/pageV2`, params),
  // 修改排序
  update: params => $COURSE.post(`/WCourse/update`, params),
  // 下架
  delete: params => $COURSE.post(`/WCourse/delete`, params),
  // 专题详情信息
  detail: params => $COURSE.post(`/platform/base/detail`, params),
  // 专题详情下方学员数据
  stuPage: params => $COURSE.post(`/platform/student/page`, params),
}
