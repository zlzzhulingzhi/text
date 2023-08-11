/*
 * @Description: 通用函数
 * @Date: 2022-03-10 15:17:05
 * @LastEditTime: 2022-03-29 15:20:26
 */
import {emojiMap, emojiUrl} from './emojiMap'

/**
 * 从 window.location.href 中获取指定key的value
 * @param {*} key 要获取的 key
 * @returns window.location.href 中指定key对应的value
 * @example
 * const value = getUrlParam(key);
 */
export function getUrlParam(key) {
  const url = decodeURI(window.location.href.replace(/^[^?]*\?/, ''));
  const regexp = new RegExp(`(^|&)${key}=([^&#]*)(&|$|)`, 'i');
  const paramMatch = url.match(regexp);

  return paramMatch ? paramMatch[2] : null;
}

export function clearUrlParam() {
  location.href = location.href.slice(0, location.href.indexOf('?') > 0 ? location.href.indexOf('?') : location.href.length);
}

export function isUndefined(value) {
  return value === 'undefined';
}

/**
 * 当前浏览器是否为移动端浏览器
 */
export const isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);

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
