import axios from 'axios'
import {Message} from 'element-ui'
import store from '@/store'
import Url from 'url'
import {DownloadFile, GetContentType} from '@/lib/utils'
import qs from 'qs'

export default class HttpRequest {
  constructor({
                baseURL,
                requestSuccess,
                requestFailed,
                responseSuccess,
                responseFailed
              }) {

    this.api = axios.create({
      withCredentials: true,
      baseURL
    })

    /*中间件 - 请求拦截 - 对接后台，自定义请求规则*/
    // 请求成功
    if (!requestSuccess) requestSuccess = config => {
      let {headers} = config

      // 数组对象格式化 ==> listName: v
      if (config.method === 'get' || config.method === 'delete') {
        config.paramsSerializer = params => qs.stringify(params, {arrayFormat: 'repeat'})
      }

      if (store.state.system.token) {
        headers['Authorization'] = `Bearer ${store.state.system.token}`
      }

      return config
    }

    // 请求失败
    if (!requestFailed) requestFailed = err => {
      Message.error('请求超时!')
      return Promise.reject(err)
    }

    // 响应成功 必须return response 对象
    if (!responseSuccess) responseSuccess = async response => {
      let contentDisposition = response.headers['content-disposition']
      let contentType = response.headers['content-type']

      store.commit('saveServerTime', response.headers.date)

      // 格式处理 - 文件流（图片）
      if (/image/.test(contentType)) {
        try {
          response.data = (window.URL || window.webkitURL || window).createObjectURL(new Blob([response.data], {type: 'image/png'}))
          return response
        } catch (e) {
          return response
        }
      }

      // 格式处理 - 文件流（excel）
      if (contentDisposition) {
        let {filename} = Url.parse(contentDisposition.replace(';', '?'), true).query
        DownloadFile({
          body: response.data,
          contentType: GetContentType(filename),
          name: filename
        })
        return response
      }

      /*错误信息 处理*/
      // Buffer类型
      if (response.data.__proto__ === ArrayBuffer.prototype) {
        let data = JSON.parse(new Buffer(response.data).toString())
        if (data.code !== 200) Message.error(data.msg || data.message)
        response.data = data
        return response
      }

      // 401 响应处理
      if (response.data.code === 401) {
        await store.dispatch('system/logout')
      }

      // 基础
      if (response.data.code !== 200 && response.data.code !== 201) {
        Message.error(response.data.msg || response.data.message || '系统异常')
        return response
      }

      return response
    }

    // 响应失败
    if (!responseFailed) responseFailed = async err => {
      switch (err.response.status) {
        case 401:
          await store.dispatch('system/logout')
        default:
          break
      }

      return Promise.reject(err)
    }

    this.api.interceptors.request.use(requestSuccess, requestFailed)
    this.api.interceptors.response.use(responseSuccess, responseFailed)
  }


  async get() {
    return await this.$http('get', ...arguments)
  }

  async post() {
    return await this.$http('post', ...arguments)
  }

  async delete() {
    return await this.$http('delete', ...arguments)
  }

  async put() {
    return await this.$http('put', ...arguments)
  }

  async $http(method, ...arg) {
    let url = arg[0]
    console.log(`%c ${method} to ${url}`, 'color: #409EFF;background-color: #f2f2f2')

    try {
      let {data} = await this.api[method](...arg)
      console.log(`%c success ${method} to ${url}`, 'color: #67C23A;background-color: #f2f2f2')
      return data
    } catch (message) {
      console.log(`%c failed ${method} to ${url}`, 'color: #F56C6C;background-color: #f2f2f2')
      console.error(message)
      return {data: null, code: 500, msg: '异常'}
    }
  }
}