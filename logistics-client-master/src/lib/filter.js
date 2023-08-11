// Vue 过滤器
import {DateFormat, NumToCn, formatTime, formatDuration, DurationToCn} from '@/lib/utils'

export default {
  install(Vue) {
    Vue.filter('number', FormatNumber)
    Vue.filter('price', FormatPrice)
    Vue.filter('numToCn', NumToCn)
    Vue.filter('DateFormat', DateFormat)
    Vue.filter('formatTime', formatTime)
    Vue.filter('formatDuration', formatDuration)
    Vue.filter('timeConversion', timeConversion)
    Vue.filter('DurationToCn', DurationToCn)
    Vue.filter('filterTypeName', filterTypeName)
    Vue.filter('fileSize', fileSize)
    Vue.filter('filterStateName', filterStateName)
    Vue.filter('filterStatus', filterStatus)
    Vue.filter('moment', moment)
  }
}

const moment = (value, defaultValue = '', format = 'YYYY-MM-DD HH:mm') => {
  if (!value) return defaultValue
  return $moment(value.replace(/-/g, '/')).format(format)
}

const FormatNumber = (value, n = 2, isFormatIntPart = true, keepZero = false) => {
  let num = Number(value)
  // 无穷大
  if (!Number.isFinite(num) || num.toString().includes('e')) return '∞'

  // 非数字 返回默认值
  if (isNaN(num)) num = 0

  // 数字精度 处理
  num = num.toFixed(n)
  // 数字格式化处理
  let intPart = num.toString().split('.')[0]
  if (isFormatIntPart) intPart = intPart.replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
  let floatPart = n ? num.toString().match(/\.\d+$/)[0] : ''
  floatPart = parseInt(floatPart.split('').reverse().join(''))
  if (!floatPart) return intPart
  floatPart = floatPart.toString().split('').reverse().join('')
  return intPart + '.' + floatPart
}

const FormatPrice = (value) => {
  return FormatNumber(value / 100)
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