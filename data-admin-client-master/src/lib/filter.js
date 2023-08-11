// Vue 过滤器
import {DateFormat, formatDuration, formatTime, NumToCn} from '@/lib/utils'

export default {
  install(Vue) {
    Vue.filter('number', FormatNumber)
    Vue.filter('numToCn', NumToCn)
    Vue.filter('DateFormat', DateFormat)
    Vue.filter('formatTime', formatTime)
    Vue.filter('formatDuration', formatDuration)
    Vue.filter('fileSize', fileSize)
  }
}

// isKeepZero == 是否保留末尾小数
export const FormatNumber = (value, n = 2, isFormatIntPart = true, isKeepZero = false) => {
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

// 文件大小
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