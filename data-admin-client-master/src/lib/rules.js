/*** 标准化 表单规则 重定向 ***/
export * from 'knight-rules'
import {required} from 'knight-rules'

/**
 * 本地化 表单规则
 * */

// 身份证号
export const idNumber = {
  pattern: /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
  message: '身份证格式错误！',
  trigger: 'blur'
}

export const IDNumber = [
  required,
  idNumber
]
