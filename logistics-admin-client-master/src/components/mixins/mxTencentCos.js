import CosRequest from '@/lib/cos'
// import flvjs from 'flv.js/dist/flv.js'
import path from 'path'
import url from 'url'

// TODO 该混合 待优化

export default {
  created() {
    this.cos = new CosRequest()
    console.log(this.cos)
    // this.vod = new TencentVod()
  },
  data() {
    return {
      cos: null,
      vod: null,
      flvjs,
      // 腾讯云COS 文档转HTML支持的格式
      COSProcessType: [
        'pptx', 'ppt', 'pot', 'potx', 'pps', 'ppsx', 'dps', 'dpt', 'pptm', 'potm', 'ppsm',
        'doc', 'dot', 'wps', 'wpt', 'docx', 'dotx', 'docm', 'dotm',
        'xls', 'xlt', 'et', 'ett', 'xlsx', 'xltx', 'csv', 'xlsb', 'xlsm', 'xltm', 'ets',
        'pdf', 'lrc', 'c', 'cpp', 'h', 'asm', 's', 'java', 'asp', 'bat', 'bas', 'prg', 'cmd', 'rtf', 'txt', 'log', 'xml', 'htm', 'html'
      ]
    }
  },
  methods: {
    // 获取 - iframe可预览链接
    // async getIframePreviewUrl(fileUrl) {
    //   fileUrl = decodeURIComponent(fileUrl)

    //   let oFileUrlCos = await this.cos.getObjectUrl(/^http(s)?:\/\/.+/.test(fileUrl) ? fileUrl : `https:${fileUrl}`)
    //   let oFileUrl = new URL(oFileUrlCos)

    //   if (!this.COSProcessType.includes(path.extname(oFileUrl.pathname).slice(1))) return oFileUrl.toString()

    //   oFileUrl.searchParams.set('dstType', 'html')
    //   oFileUrl.searchParams.set('ci-process', 'doc-preview')
    //   return oFileUrl.toString()
    // },
    // 获取 - 视频时长
    // getVideoDuration(file) {
    //   return new Promise(async resolve => {
    //     let url = URL.createObjectURL(file)
    //     let audioElement = null
    //     let flvPlayerObjM1 = null
    //     if (file.type === 'video/x-flv') {
    //       flvPlayerObjM1 = await flvjs.createPlayer({
    //         type: 'flv',
    //         isLive: true, //是否直播
    //         url, //地址
    //         hasAudio: true,
    //         hasVideo: true
    //       })
    //       audioElement = await new Audio()
    //       await flvPlayerObjM1.attachMediaElement(audioElement)
    //       await flvPlayerObjM1.load()
    //     } else {
    //       audioElement = await new Audio(url)
    //     }
    //     await audioElement.addEventListener('loadedmetadata', () => {
    //       // 视频时长值的获取要等到这个匿名函数执行完毕才产生
    //       resolve(Math.floor(audioElement.duration))
    //       if (file.type === 'video/x-flv') flvPlayerObjM1.destroy()
    //     })
    //   })
    // },

    async getCosFileBody(fileUrl) {
      if (!/^http(s)?:/.test(fileUrl)) fileUrl = `https:${fileUrl}`
      let filePath = url.parse(fileUrl).pathname
      return new Promise((resolve, reject) => {
        this.cos.cos.getObject({
          ...this.cos.config,
          Key: decodeURI(filePath),
          DataType: 'blob'
        }, (err, data) => {
          if (err) {
            reject(err)
          } else {
            resolve(data)
          }
        })
      })
    },

    // 格式化 - 文件地址
    formatFileUrl(filePath) {
      if (!/^http(s)?:/.test(filePath)) filePath = `https:${filePath}`
      filePath = decodeURIComponent(filePath)
      return filePath
    },

    // 操作 - 腾讯下载资源
    async onTencentDownLoad({filePath, fileName}) {
      try {
        filePath = this.formatFileUrl(filePath)
        let params = {
          name: fileName
        }
        // console.log('下载文件', filePath)
        // if (/vod2\.myqcloud\.com/.test(filePath)) {
        //   // 云点播下载
        //   params.url = filePath
        // } else {
        //   // 储存桶下载
        //   let {Body} = await this.getCosFileBody(filePath)
        //   params.body = Body
        // }

        let {Body} = await this.getCosFileBody(filePath)
        params.body = Body
        await this.$utils.DownloadFile(params)
      } catch (e) {
        console.error(e)
      }
    }
  }
}