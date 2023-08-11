/**
 * 腾讯云对象存储 COS JavaScript SDK 文档地址：
 * https://cloud.tencent.com/document/product/436/11459
 * https://cloud.tencent.com/document/product/436/64960
 */
import COS from 'cos-js-sdk-v5'
import store from '@/store'
import {Message} from 'element-ui'
import url from 'url'
import moment from 'moment'
import crypto from 'crypto'
import * as path from 'path'

const md5Tool = crypto.createHmac('md5', '123456')
const md5 = data => md5Tool.update(data).digest('hex')
const {ResourceCOS} = require('@/api')


export default class CosRequest {
  config = {
    Bucket: 'seczone-teach-test-1312651338', // 存储桶名称，必须
    Region: 'ap-guangzhou', // 存储桶所在地域，必须
    SliceSize: 1024 * 1024 * 20, // 触发分块上传的阈值，超过 20MB 使用分块上传
    AsyncLimit: 6
  }

  constructor(options = {}) {
    let getCredential = options.getCredential || (options => new Promise(async (resolve, reject) => {
      try {
        let {code, data, message} = await ResourceCOS.cosCredential({
          // type: options.type || 'multipart'
        })
        if (code !== 200) return Message.error(message)
        let credentials = data && data.credentials
        if (!credentials) return Message.error('COS凭证无效')
        resolve({
          TmpSecretId: credentials.tmpSecretId,
          TmpSecretKey: credentials.tmpSecretKey,
          XCosSecurityToken: credentials.sessionToken,
          StartTime: data.startTime,
          ExpiredTime: data.expiredTime
        })
      } catch (e) {
        reject(`接口存在错误: ${e}`)
      }
    }))
    try {
      this.config = {
        ...this.config,
        ...store.state.config.COSConfig
      }
    } catch (e) {
    }

    if (options.fileNameFormat) this.fileNameFormat = options.fileNameFormat

    if (options.config) this.config = {
      ...this.config,
      ...options.config
    }

    this.init(getCredential)
  }

  init(getCredential) {
    this.cos = new COS({
      async getAuthorization(options, callback) {
        let data = await getCredential(options)
        callback(data)
      }
    })
  }

  // 格式 {Bucket}.cos.{Region}.myqcloud.com/{orgId}/{YYYY-MM-DD}/{md5(timestamp)}{extname}
  fileNameFormat(name) {
    // let orgId = (store.state.system.userInfo || {}).orgId || '00'
    let orgId = 'train/static'
    let date = moment().format('YYYY-MM-DD')
    let fileName = md5(Date.now().toString())
    let ext = path.extname(name)
    return `${orgId}/${date}/${fileName}${ext}`
  }

  // // 检验存储桶中手否存在文件
  // doesObjectExist(Key) {
  //   this.cos.headObject({
  //       Bucket: 'seczone-teach-test-1312651338', /* 填入您自己的存储桶，必须字段 */
  //       Region: 'ap-guangzhou',  /* 存储桶所在地域，例如ap-beijing，必须字段 */
  //       Key: Key,  /* 存储在桶里的对象键（例如1.jpg，a/b/test.txt），必须字段 */
  //   }, function(err, data) {
  //     if (data) {
  //         console.log('对象存在');
  //     } else if (err.statusCode == 404) {
  //         console.log('对象不存在');
  //     } else if (err.statusCode == 403) {
  //         console.log('没有该对象读权限');
  //     }
  // })
  // }

  // 检验存储桶中手否存在文件
  doesObjectExist({Key, onSuccess, onError}) {
    this.cos.headObject({
        Bucket: process.env.VUE_APP_COS_Bucket, /* 填入您自己的存储桶，必须字段 */
        Region: 'ap-guangzhou',  /* 存储桶所在地域，例如ap-beijing，必须字段 */
        Key: Key,  /* 存储在桶里的对象键（例如1.jpg，a/b/test.txt），必须字段 */
    }).then(data => {
      onSuccess ? onSuccess(data) : this.onSuccess(data)
    }).catch(err => {
      onError ? onError(err) : this.onError(err)
    })
  }

  uploadFile({file, onProgress, onSuccess, onError}) {
    const pattern = /[`~!@#$^&*()=|{}':;',\\\[\]\<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g
    let newName = file.name.replace(pattern, '')
    this.cos.uploadFile({
      Key: this.fileNameFormat(newName), /* 必须 */
      Body: file.Body || file,
      ...this.config,
      onTaskReady: tid => {
        console.log('COS onTaskReady: ', tid)
        this.taskId = tid
      },
      onProgress: (...arg) => onProgress ? onProgress(...arg) : this.onProgress(...arg),
      onFileFinish: (...arg) => this.onFileFinish(...arg)
    }).then(data => {
      onSuccess ? onSuccess(`//${data.Location}`) : this.onSuccess(`//${data.Location}`)
    }).catch(err => {
      onError ? onError(err) : this.onError(err)
    })
  }

  sliceUploadFile({file, onProgress, onCancelTask, onSuccess, onError}) {
    const pattern = /[`~!@#$^&*()=|{}':;',\\\[\]\<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g
    let newName = file.name.replace(pattern, '')
    this.cos.sliceUploadFile({
      ...this.config,
      Key: this.fileNameFormat(newName),
      Body: file,
      onProgress: (...arg) => onProgress ? onProgress(...arg) : this.onProgress(...arg),
      onTaskReady: tid => {
        console.log('COS onTaskReady: ', tid)
        this.taskId = tid
        onCancelTask && onCancelTask(tid)
      },
      onFileFinish: (...arg) => this.onFileFinish(...arg)
    }).then(data => {
      onSuccess ? onSuccess(`//${data.Location}`) : this.onSuccess(`//${data.Location}`)
    }).catch(err => {
      onError ? onError(err) : this.onError(err)
    })
  }

  // onTaskReady(taskId) {
  //   console.log(taskId, '取消的id');
  // }
  cancelTasks(taskId) {
    this.cos.cancelTask(taskId)
  }


  onSuccess(data) {
    console.log('COS onSuccess: ', data)
  }

  onError(err) {
    console.error('COS onError: ', err)
  }

  onProgress(info) {
    console.log('COS onProgress: ', info)
  }

  onFileFinish(err, data) {
    console.log('COS onFileFinish: ', err || data)
  }

  // 暂停任务
  pauseTask() {
    console.log('COS pauseTask: ', this.taskId)
    this.cos.pauseTask(this.taskId)
  }

  // 重始任务
  restartTask() {
    console.log('COS restartTask: ', this.taskId)
    this.cos.restartTask(this.taskId)
  }

  // 取消任务
  cancelTask() {
    console.log('COS cancelTask: ', this.taskId)
    this.cos.cancelTask(this.taskId)
  }

  /**
   * 获取文件链接（带签名），用于预览
   * https://cloud.tencent.com/document/product/436/35651
   * @param 传 https://{Bucket}.cos.{Region}.myqcloud.com/{orgId}/{YYYY-MM-DD}/{timestamp}_{fileName}.{fileType}
   */
  getObjectUrl(fileUrl) {
    let filePath = url.parse(fileUrl).pathname
    return new Promise((resolve, reject) => {
      this.cos.getObjectUrl({
        ...this.config,
        Key: decodeURI(filePath),
        Sign: true,
        Protocol: 'https:',
        Expires: 3600 * 24
      }, (err, data) => {
        console.log('COS getObjectUrl: ', JSON.stringify(data))
        if (err) {
          reject(err)
        } else {
          resolve(data.Url)
        }
      })
    })
  }
}