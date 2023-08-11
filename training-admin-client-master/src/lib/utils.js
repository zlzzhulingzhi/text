/*** 标准化工具类 重定向 ***/
import {Message} from 'element-ui'
import store from '@/store'
import moment from 'moment'
import $moment from 'moment'
import {DownloadBase} from 'knight-utils'
import jszip from 'jszip'
import ContentType from '@/lib/ContentType.json'
import path from 'path'
import JSEncrypt from 'jsencrypt'

export * from 'knight-utils'

/**
 * 本地化工具类
 * */

// 填充高度全屏
export const FullViewHeight = (h = 0) => {
  return `calc(100vh - ${h}px - ${store.state.config.constant.headerHeight})`
}

// 事件委托 - 获取目标元素
export const GetEventElement = (element, targetClassName, endClassName) => {
  while (!new RegExp(targetClassName).test(element.className)) {
    if (new RegExp(endClassName).test(element.className)) return false
    element = element.offsetParent
  }
  return element
}

// 创建拖拽事件
export const CreateDragEvent = (options = {}) => {
  let {zoom = 1, el, event, moveFn, upFn} = options
  if (!el) return console.error('请传递拖拽目标 el')
  if (!event) return console.error('请传递keydown时间对象 event')
  let sx = event.clientX / zoom - el.offsetLeft
  let sy = event.clientY / zoom - el.offsetTop
  let maxX = el.offsetParent.clientWidth - el.offsetWidth
  let maxY = el.offsetParent.clientHeight - el.offsetHeight

  let $moveFn = ev => {
    let x = ev.clientX / zoom - sx
    let y = ev.clientY / zoom - sy

    // 边界限制
    x = x < 0 ? 0 : (x > maxX ? maxX : x)
    y = y < 0 ? 0 : (y > maxY ? maxY : y)

    if (moveFn) return moveFn({
      x,
      y,
      event: ev
    })
    el.style.top = `${y}px`
    el.style.left = `${x}px`
  }

  let $upFn = ev => {
    upFn && upFn({
      event: ev
    })
    document.removeEventListener('mousemove', $moveFn)
    document.removeEventListener('mouseup', $upFn)
  }
  document.addEventListener('mousemove', $moveFn)
  document.addEventListener('mouseup', $upFn)
}


/**
 * 将 dom 元素全屏
 * @param {dom} element dom元素
 * @example
 * setFullscreen(document.documentElement) // 整个页面进入全屏
 * setFullscreen(document.getElementById("id")) // 某个元素进入全屏
 */
export const SetFullScreen = dom => {
  if (dom.requestFullscreen) return dom.requestFullscreen()
  if (dom.webkitRequestFullscreen) return dom.webkitRequestFullscreen()
  if (dom.mozRequestFullScreen) return dom.mozRequestFullScreen()
  if (dom.msRequestFullscreen) return dom.msRequestFullscreen()
}

/**
 * 退出全屏
 * @example
 * exitFullscreen();
 */
export const ExitFullScreen = () => {
  if (document.exitFullscreen) return document.exitFullscreen()
  if (document.webkitExitFullscreen) return document.webkitExitFullscreen()
  if (document.mozCancelFullScreen) return document.mozCancelFullScreen()
  if (document.msExitFullscreen) return document.msExitFullscreen()
}

export const getPlayerSupport = (function() {
  let De = /tbs\/(\d+) /i, ke = /OS (\d+)_(\d+)_?(\d+)?/, Ie = De.test(navigator.userAgent)
  let Ae = /UCBrowser\/(\d+)\./i.test(navigator.userAgent)
  let Oe = /safari\/(\d+)\./i.test(navigator.userAgent) && !/chrome\/(\d+)\./i.test(navigator.userAgent)
  let Me = /iPhone|iPad|iOS/i.test(navigator.userAgent)
  let je = function() {
    let e = navigator.userAgent.match(ke)
    return e && [parseInt(e[1], 10), parseInt(e[2], 10), parseInt(e[3] || '0', 10)] || []
  }
  let xe = /firefox\/(\d+)\./i.test(navigator.userAgent)

  function t(e, t, n, r) {
    return new (n || (n = Promise))((function(i, o) {
      function s(e) {
        try {
          c(r.next(e))
        } catch (e) {
          o(e)
        }
      }

      function a(e) {
        try {
          c(r.throw(e))
        } catch (e) {
          o(e)
        }
      }

      function c(e) {
        let t
        e.done ? i(e.value) : (t = e.value, t instanceof n ? t : new n((function(e) {
          e(t)
        }))).then(s, a)
      }

      c((r = r.apply(e, t || [])).next())
    }))
  }

  function n(e, t) {
    let n, r, i, o, s = {
      label: 0, sent: function() {
        if (1 & i[0]) throw i[1]
        return i[1]
      }, trys: [], ops: []
    }
    return o = {
      next: a(0),
      throw: a(1),
      return: a(2)
    }, 'function' == typeof Symbol && (o[Symbol.iterator] = function() {
      return this
    }), o

    function a(o) {
      return function(a) {
        return function(o) {
          if (n) throw new TypeError('Generator is already executing.')
          for (; s;) try {
            if (n = 1, r && (i = 2 & o[0] ? r.return : o[0] ? r.throw || ((i = r.return) && i.call(r), 0) : r.next) && !(i = i.call(r, o[1])).done) return i
            switch (r = 0, i && (o = [2 & o[0], i.value]), o[0]) {
              case 0:
              case 1:
                i = o
                break
              case 4:
                return s.label++, {value: o[1], done: !1}
              case 5:
                s.label++, r = o[1], o = [0]
                continue
              case 7:
                o = s.ops.pop(), s.trys.pop()
                continue
              default:
                if (!(i = s.trys, (i = i.length > 0 && i[i.length - 1]) || 6 !== o[0] && 2 !== o[0])) {
                  s = 0
                  continue
                }
                if (3 === o[0] && (!i || o[1] > i[0] && o[1] < i[3])) {
                  s.label = o[1]
                  break
                }
                if (6 === o[0] && s.label < i[1]) {
                  s.label = i[1], i = o
                  break
                }
                if (i && s.label < i[2]) {
                  s.label = i[2], s.ops.push(o)
                  break
                }
                i[2] && s.ops.pop(), s.trys.pop()
                continue
            }
            o = t.call(e, s)
          } catch (e) {
            o = [6, e], r = 0
          } finally {
            n = i = 0
          }
          if (5 & o[0]) throw o[1]
          return {value: o[0] ? o[1] : void 0, done: !0}
        }([o, a])
      }
    }
  }

  ``

  return function(e) {
    t(void 0, void 0, void 0, (function() {
      let e, r, i
      return n(this, (function(o) {
        switch (o.label) {
          case 0:
            return e = !1, ['RTCPeerConnection', 'webkitRTCPeerConnection'].forEach((function(t) {
              e || t in window && (e = !0)
            })), Ie || (Ae && Me || Oe && Me && (0 === (r = je()).length || r[0] < 11 || 11 === r[0] && r[1] < 1 || 11 === r[0] && 1 === r[1] && r[2] < 2)) && (e = !1), [4, t(void 0, void 0, void 0, (function() {
              let e, t, r, i
              return n(this, (function(n) {
                switch (n.label) {
                  case 0:
                    return n.trys.push([0, 2, , 3]), e = new RTCPeerConnection({
                      iceServers: [],
                      sdpSemantics: 'unified-plan'
                    }), t = {}, e.addTransceiver ? (e.addTransceiver('audio', {direction: 'recvonly'}), e.addTransceiver('video', {direction: 'recvonly'})) : t = {
                      offerToReceiveVideo: !0,
                      offerToReceiveAudio: !0
                    }, [4, e.createOffer(t)]
                  case 1:
                    return r = n.sent(), i = r.sdp.toLowerCase().indexOf('h264') > -1, e.close(), [2, i]
                  case 2:
                    return n.sent(), [2, !1]
                  case 3:
                    return [2]
                }
              }))
            }))]
          case 1:
            return i = o.sent(), [2, {
              WebRTCSupport: e,
              isTbs: Ie,
              tbsVersion: Ie ? (s = navigator.userAgent, a = De, c = 1, d = s.match(a), d && d.length >= c && parseInt(d[c], 10)) : null,
              isFirefox: xe,
              isSafari: Oe,
              isIOS: Me,
              iOSVersion: Me ? je().join('.') : null,
              h264Support: i
            }]
        }
        let s, a, c, d
      }))
    })).then((function(t) {
      null == e || e(t)
    }))
  }
})()

// 获取权限 || 当前页面权限
export const GetPermission = (permission, withChild) => {
  let permissionObj = store.getters['system/permissionFlat'].find(item => item.permission === permission)
  if (!withChild) return Boolean(permissionObj)
  if (permissionObj) {
    let obj = {}
    permissionObj.children.forEach(item => {
      let key = item.permission.slice(permission.length + 1)
      obj[key] = true
    })
    return obj
  }
  return false
}

// 数字转换成中文
export const NumToCn = n => {
  if (typeof n !== 'number') return n
  let cnNum = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九']
  let unit = ['', '十', '百', '千', '万']

  let ns = n.toString().split('')
  let cnStr = ns.map((x, i) => {
    if (x === '0') return x
    return cnNum[x] + unit[ns.length - i - 1]
  }).join('')
  cnStr = cnStr.replace(/0+$/, '')
  cnStr = cnStr.replace(/0+/g, '零')
  return cnStr
}

// 计算数组之和
export const Sum = (arr, key = 'total') => {
  let s = 0
  switch (typeof key) {
    case 'string':
      arr.forEach(item => s += Number(item[key]))
      break
    case 'function':
      arr.forEach(item => s += Number(key(item)))
      break
  }
  return s
}
Array.prototype.Sum = function(key) {
  return Sum(this, key)
}


// 提示语
export const $msg = (handleType, name = '', isBatch = false) => {
  return Message.success(`${isBatch ? '批量' : ''}${handleType}${name ? '“' + name + '”' : ''}成功`)
}


// 点击复制
export const CopyToClipboard = text => {
  let inputEl = document.createElement('input')
  inputEl.setAttribute('readonly', 'readonly')
  inputEl.setAttribute('value', text)
  inputEl.value = text
  inputEl.style.height = '0'
  inputEl.style.overflow = 'hidden'
  inputEl.style.opacity = '0'
  inputEl.style.display = 'block'
  inputEl.style.border = 'none'
  inputEl.style.padding = '0'
  document.body.appendChild(inputEl)
  inputEl.select()
  inputEl.setSelectionRange(0, 9999)
  document.execCommand('copy', false)
  document.body.removeChild(inputEl)
  Message.success('复制成功')
  return text
}

// 格式化时间：秒数 => 时分秒
export const formatDuration = (duration, format = ['小时', '分', '秒']) => {
  if (!duration) return duration
  let hour = moment.duration(duration * 1000).hours()
  let minute = moment.duration(duration * 1000).minutes()
  let second = moment.duration(duration * 1000).seconds()
  if (!hour && !minute) return `${second}${format[2]}`
  if (!hour) return `${minute}${format[1]}${second}${format[2]}`
  return `${hour}${format[0]}${minute}${format[1]}${second}${format[2]}`
}

// 格式化时间：秒数 => 时分秒
export const formatTime = (value, format = 'yyyy-MM-DD HH:mm') => {
  return $moment(value).format(format)
}


// 格式化时间：时间范围
export const DateFormat = (time, type = 'Range') => {
  if (!time[0] || !time[1]) return '-'
  let a = new Date(time[0].replace(/-/g, '/'))
  let b = new Date(time[1].replace(/-/g, '/'))
  let formatA = 'YYYY-MM-DD HH:mm'
  let formatB = 'YYYY-MM-DD HH:mm'
  // 2. 同一年
  if (b.getFullYear() === a.getFullYear()) formatB = 'MM-DD HH:mm'
  // 1. 同一天
  if (b - a < 86400000 && b.getDate() === a.getDate()) formatB = 'HH:mm'
  // 3. 跨年（默认）
  return `${moment(a).format(formatA)} - ${moment(b).format(formatB)}`
}


// 计算百分比
export const getPercent = (num, total) => {
  num = parseFloat(num)
  total = parseFloat(total)
  if (isNaN(num) || isNaN(total)) return '0%'
  if (num < 0 || total <= 0) return '0%'
  return `${Math.round(num / total * 10000) / 100}%`
}

export const GetContentType = url => ContentType[path.extname(url).slice(1)]

// 下载外链
export const DownloadFile = async (options = {}) => {
  let {url, body, name = 'image', contentType} = options

  if (!url && !body) return console.error('需要传递 url、data之一')

  // 1. 將 url 转换成 文件流 （针对外链）
  if (url) {
    contentType = contentType || GetContentType(url) || 'image/png'
    body = await GetFileBlob(url, contentType)
  }

  // 2. 將 body 转换成 Blob
  let fileData = (window.URL || window.webkitURL || window).createObjectURL(new Blob([body], {type: contentType}))
  // 下载资源
  DownloadBase(fileData, name)
}

export const DownloadPDF = options => DownloadFile({name: 'pdf', contentType: 'application/pdf', ...options})

// 工具 - 获取文件blob
export const GetFileBlob = (fileUrl, contentType = 'image/png') => {
  return new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest()
    xhr.open('GET', fileUrl.replace(/\\/g, '/'), true)
    xhr.responseType = 'blob'
    xhr.setRequestHeader('Content-Type', contentType)//设置请求头
    xhr.onload = () => {
      if (xhr.status === 200) {
        resolve(xhr.response)
      }
    }
    xhr.send()
  })
}

// 批量下载 - zip压缩包
export const DownloadFileAsZip = async (options = {}) => {
  let {fileList, name = 'zip'} = options
  let zip = new jszip()

  for (const file of fileList) {
    zip.file(file.name, file.body, {binary: true})
  }

  let content = await zip.generateAsync({type: 'blob'})
  const fd = new FileReader()
  fd.readAsDataURL(content)
  fd.onload = () => DownloadBase(fd.result, name)
}

// 加密
export const Encrypt = async (value) => {
  let verify = new JSEncrypt()
  verify.setPublicKey(store.state.config.passwordPublicKey)
  return verify.encrypt(value)
}


// 加密
export const RandomChineseName = () => {
  let getRandom = list => list[Math.floor(Math.random() * list.length)]
  let firstNameList = '赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐'
  let firstName = getRandom(firstNameList)
  let lastNameList = ' 煜轩凌容岚航宇筠渤坤瀚柏霆晟睿诚晨哲智嘉洋谦尧铭松明琛星君涵子璇瑾春一鸣晓庆佳玉诗悦天赫若萌雅涵璐美欣国贤紫亦菲雄霖情晓蕊蓉柯茁瑞娅果兴鑫恩岳岑森升珈尚延颐灵然衍暄正薪桀永琦舜澜尊楠普喆娴缘榛熳优勋滕超苡阡萸幻霈陌艾蓝瑜璐颜薰鹤莲禧隆蔚霏泰承珂璇璠嘉邦俊铭'
  let lastName1 = getRandom(lastNameList)
  let lastName2 = getRandom(lastNameList)
  return firstName + lastName1 + lastName2
}


export const WindowOpenInParentFrame = (routePath) => {
  let {href,origin} = window.parent.location
  let hash = href.match(/\/Embed\/System:\w+/)[0]
  window.open(`${origin}/#${hash}?routePath=${routePath}`, '_blank')
}

