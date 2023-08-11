// Vue 过滤器
import { DateFormat, NumToCn, formatTime, formatDuration, formatMoney,FormatNumber } from '@/lib/utils'

export default {
  install(Vue) {
    Vue.filter('number', FormatNumber)
    Vue.filter('numToCn', NumToCn)
    Vue.filter('DateFormat', DateFormat)
    Vue.filter('formatTime', formatTime)
    Vue.filter('formatDuration', formatDuration)
    Vue.filter('timeConversion', timeConversion)
    Vue.filter('filterTypeName', filterTypeName)
    Vue.filter('fileSize', fileSize)
    Vue.filter('filterStateName', filterStateName)
    Vue.filter('filterStatus', filterStatus)
    Vue.filter('money', formatMoney)
  }
}





const filterStatus = (status) => {
  return status == 2 ? '已批改' : '待批改'
}
// 时、分、秒时长转换
const timeConversion = (timeVal) => {
  if (timeVal === '00:00') {
    return '--'
  }
  // 优化建议
  if (timeVal < 60) return timeVal + 's'
  if (timeVal < 3600) return (timeVal / 60).toFixed(2) * 1 + 'm'
  return (timeVal / 3600).toFixed(2) * 1 + 'h'
}
// 类型名称
const filterTypeName = (typeName) => {
  switch (typeName) {
    case 'video':
      return '上传视频'
    case 'audio':
      return '上传音频'
    case 'picture':
      return '上传图片'
    case 'doc':
      return '上传文档'
    case 'other':
      return '上传其他'
  }
}

// 过滤器过滤文件大小
const fileSize = (fileSize) => {
  if (!fileSize) return '未知'
  const filterSize = fileSize / 1024 / 1024
  if (filterSize < 1) {
    let fileSizeKB = (fileSize / 1024).toFixed(2)
    return fileSizeKB + 'KB'
  } else if (filterSize >= 1000) {
    let fileSizeG = (filterSize / 1024).toFixed(2)
    return fileSizeG + 'GB'
  }
  let fileSizeM = filterSize.toFixed(2)
  return fileSizeM + 'MB'
}

// 过滤考试列表hover提示
const filterStateName = (params) => {
  if (params.enabled === 0) {
    return '未发布'
  }
}