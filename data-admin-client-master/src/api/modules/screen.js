const { $SCREEN } = require('@/api/config')

export const ScreenAuth = {
  menuPermission: params => $SCREEN.post(`/menu-permission`, params)
}

export const Screen = {
  // 今日培训班
  attendClass: params => $SCREEN.get(`/show/attend-class`, params),
  // 基地风貌
  banner: params => $SCREEN.get(`/show/banner`, params),
  // 数据总览
  dataOverview: params => $SCREEN.get(`/show/data-overview`, params),
  // 重要通知
  notice: params => $SCREEN.get(`/show/notice`, params),
  // 动态数据统计
  statDynamic: params => $SCREEN.get(`/show/stat-dynamic`, params),
  // 每月学员数量统计
  statStuMonthly: params => $SCREEN.get(`/show/stat-stu-monthly`, params),
}

export const ScreenManage = {
  // 预览今日培训班级
  previewAttendClass: params => $SCREEN.post(`/import/previewAttendClass`, params.fileFormData),
  // 预览数据总览
  previewDataOverview: params => $SCREEN.post(`/import/previewDataOverview`, params.fileFormData),
  // 预览重要通知
  previewNotice: params => $SCREEN.post(`/import/previewNotice`, params.fileFormData),
  // 预览动态数据统计
  previewStatDataDynamic: params => $SCREEN.post(`/import/previewStatDataDynamic`, params.fileFormData),
  // 预览每月学员数量统计
  previewStatStudentMonthly: params => $SCREEN.post(`/import/previewStatStudentMonthly`, params.fileFormData),

  // 导入今日培训班级
  importBatchAttendClass: params => $SCREEN.post(`/import/importBatchAttendClass`, params),
  // 导入数据总览
  importBatchDataOverview: params => $SCREEN.post(`/import/importBatchDataOverview`, params),
  // 导入重要通知
  importBatchNotice: params => $SCREEN.post(`/import/importBatchNotice`, params),
  // 导入动态数据统计
  importBatchStatDataDynamic: params => $SCREEN.post(`/import/importBatchStatDataDynamic`, params),
  // 导入每月学员数量统计
  importBatchStatStudentMonthly: params => $SCREEN.post(`/import/importBatchStatStudentMonthly`, params),
  // 添加轮播图
  addBanner: params => $SCREEN.post(`/manage/addBanner`, params),
  // 编辑轮播图
  updateBanner: params => $SCREEN.post(`/manage/updateBanner`, params),
  // 删除轮播图
  deleteBanner: params => $SCREEN.post(`/manage/deleteBanner`, params),

  // 分页查询今日培训班级
  pageAttendClass: params => $SCREEN.post(`/manage/pageAttendClass`, params),
  // 分页查询数据总览
  pageDataOverview: params => $SCREEN.post(`/manage/pageDataOverview`, params),
  // 分页查询重要通知
  pageNotice: params => $SCREEN.post(`/manage/pageNotice`, params),
  // 分页查询动态数据统计
  pageStatDataDynamic: params => $SCREEN.post(`/manage/pageStatDataDynamic`, params),
  // 分页查询每月学员数量统计
  pageStatStudentMonthly: params => $SCREEN.post(`/manage/pageStatStudentMonthly`, params),
  // 分页查询轮播图
  pageBanner: params => $SCREEN.post(`/manage/pageBanner`, params),
}