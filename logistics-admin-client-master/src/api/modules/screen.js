const {$SCREEN} = require('@/api/config')

export const Screen = {
    // 预导入今日培训班级信息
    pretrainClass: params => $SCREEN.post(`/import/previewAttendClass`, params.fileFormData),
    // 预导入数据总览信息
    preallData: params => $SCREEN.post(`/import/previewDataOverview`, params.fileFormData),
    // 预导入通知信息
    preinfoData: params => $SCREEN.post(`/import/previewNotice`, params.fileFormData),
    // 预导入动态数据统计信息
    pretwoNumInfo: params => $SCREEN.post(`/import/previewStatDataDynamic`, params.fileFormData),
    // 预导入每月学员数量统计信息
    prestudentNumInfo: params => $SCREEN.post(`/import/previewStatStudentMonthly`, params.fileFormData),



    // 批量插入今日培训班级信息
    trainClass: params => $SCREEN.post(`/import/importBatchAttendClass`, params),
    // 批量插入数据总览信息
    allData: params => $SCREEN.post(`/import/importBatchDataOverview`, params),
    // 批量插入通知信息
    infoData: params => $SCREEN.post(`/import/importBatchNotice`, params),
    // 批量插入动态数据统计信息
    twoNumInfo: params => $SCREEN.post(`/import/importBatchStatDataDynamic`, params),
    // 批量插入每月学员数量统计信息
    studentNumInfo: params => $SCREEN.post(`/import/importBatchStatStudentMonthly`, params),
  }