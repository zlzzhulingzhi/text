import {emojiMap, emojiUrl} from './emojiMap'

/**
 * 从 window.location.href 中获取指定key的value
 * @param {*} key 要获取的 key
 * @returns window.location.href 中指定key对应的value
 * @example
 * const value = getUrlParam(key);
 */
export function getUrlParam(key) {
  const url = window.location.href.replace(/^[^?]*\?/, '')
  const regexp = new RegExp(`(^|&)${key}=([^&#]*)(&|$|)`, 'i')
  const paramMatch = url.match(regexp)

  return paramMatch ? paramMatch[2] : null
}

/**
 * 获取语言
 * @returns language
 */
export function getLanguage() {
  let language = localStorage.getItem('trtc-tuiPlayer-language') || getUrlParam('lang') || navigator.language || 'zh'
  language = language.replace(/_/, '-').toLowerCase()

  if (language === 'zh-cn' || language === 'zh') {
    language = 'zh'
  } else if (language === 'en' || language === 'en-us' || language === 'en-GB') {
    language = 'en'
  }
  return language
}

/** 传入messageBody（群系统消息SystemMessage，群提示消息GroupTip除外）
 * payload = {
 *  msgType: 'TIMTextElem',
 *  msgContent: {
 *    text: 'AAA[龇牙]AAA[龇牙]AAA[龇牙AAA]'
 *  }
 *}
 **/
export function decodeText(content) {
//   console.log(payload);
  const renderDom = []
  let temp = content
  let left = -1
  let right = -1
  while (temp !== '') {
    left = temp.indexOf('[')
    right = temp.indexOf(']')
    switch (left) {
      case 0:
        if (right === -1) {
          renderDom.push({
            name: 'text',
            content: temp
          })
          temp = ''
        } else {
          const emoji = temp.slice(0, right + 1)
          if (emojiMap[emoji]) {
            renderDom.push({
              name: 'img',
              src: emojiUrl + emojiMap[emoji]
            })
            temp = temp.substring(right + 1)
          } else {
            renderDom.push({
              name: 'text',
              content: '['
            })
            temp = temp.slice(1)
          }
        }
        break
      case -1:
        renderDom.push({
          name: 'text',
          content: temp
        })
        temp = ''
        break
      default:
        renderDom.push({
          name: 'text',
          content: temp.slice(0, left)
        })
        temp = temp.substring(left)
        break
    }
  }
  return renderDom
}

/**
 * 检测数据是否为undefined
 * @param {*} data 检测数据
 */
export function isUndefined(data) {
  return typeof data === 'undefined'
}
