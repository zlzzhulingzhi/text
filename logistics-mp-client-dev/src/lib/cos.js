import COS from 'cos-wx-sdk-v5'
import { Cos } from '@/api'
import * as Message from '@/lib/message'
import logger from '@/lib/logger'

export default class CosRequest {
  config = {
    Bucket: process.env.VUE_APP_COS_BUCKET, // 存储桶名称，必须
    Region: process.env.VUE_APP_COS_REGION, // 存储桶地域，必须
  }

  constructor(options = {}) {
    this.cos = new COS({
      async getAuthorization(options, callback) {
        let { code, data } = await Cos.cosCredential({})
        if (code !== 200) return Message.showToast('获取不到COS凭证')
        let credentials = data && data.credentials
        if (!credentials) return Message.showToast('COS凭证无效')
        callback({
          TmpSecretId: credentials.tmpSecretId,
          TmpSecretKey: credentials.tmpSecretKey,
          XCosSecurityToken: credentials.sessionToken,
          StartTime: data.startTime,
          ExpiredTime: data.expiredTime
        })
      }
    })

    this.config = {
      ...this.config,
      ...options.config || {}
    }
    this.type = options.type
  }

  // 上传对象
  putObject({ filePath, fileType, onProgress, onSuccess, onError }) {
    let fileParams = {FilePath:filePath};
    // 以blob开头，字段需变成Blob
    if (filePath.indexOf('blob') === 0) {
      fileParams = {Body:filePath};
    }
      this.cos.putObject({
        ...this.config,
        ...fileParams,
        Key: this.formatFile(fileType), // 必须
        onProgress: (progressData) => {
          // progressData: { loaded, total, speed, percent }
          logger.info(`COS onProgress: ${JSON.stringify(progressData)}`, logger.TAG.COS)
          onProgress(progressData)
        }
      }, (err, data) => {
        if (err) {
          // err: { statusCode, headers }
          logger.error(`COS onError: ${JSON.stringify(err)}`, logger.TAG.COS)
          onError(err)
        }
        if (data) {
          // data: { statusCode, headers, ETag, Location, VersionId }
          logger.info(`COS onSuccess: ${JSON.stringify(data)}`, logger.TAG.COS)
          onSuccess(`//${data.Location}`)
        }
      })
  }

  // 格式化文件名
  formatFile(extName) {
    let cosPath = this.type ? `/${this.type}` : ''
    let date = uni.$u.timeFormat(Date.now(), 'yyyy-mm-dd')
    let fileName = uni.$u.guid(16, false)
    return `mp-logistics${cosPath}/${date}/${fileName}.${extName}`
  }
}