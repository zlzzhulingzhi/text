import Request from 'uview-ui/libs/luch-request'
import store from '@/store'
import logger from '@/lib/logger'
import * as Message from '@/lib/message'

export default class HttpRequest {
  constructor({ baseURL }) {

    this.api = new Request()

    // 请求配置
    this.api.setConfig(config => {
      // #ifdef H5
      config.baseURL = baseURL
      // #endif
      // #ifndef H5
      config.baseURL = process.env.VUE_APP_BASE_API + baseURL
      // #endif
      return config
    })

    // 请求拦截
    this.api.interceptors.request.use(config => {
      const token = store.state.system.token
      if (token) config.header['Authorization'] = token
      return config
    }, error => {
      return Promise.reject(error)
    })

    // 响应拦截
    this.api.interceptors.response.use(response => {
      // logger.info(`${response.config.method} SUCCESS. URL: ${response.config.url}, CODE: ${response.statusCode}`)
      let data = response.data
      switch (data.code) {
        case 401:
          store.dispatch('system/logout')
          break
      }
      if (!/200|201/.test(data.code)) Message.showToast(data.msg || data.data || '请求失败')
      return data
    }, err => {
      logger.error(JSON.stringify(err), logger.TAG.REQUEST)
      let msg = '系统开小差了'
      switch (err.statusCode) {
        case 401:
          msg = err.data.msg
          store.dispatch('system/logout')
          break
        case 500:
          msg = '系统开小差了，请稍后重试'
          break
      }
      Message.showToast(`${msg}（${err.statusCode}）`)
      return { ok: false, msg: msg, data: null, code: err.statusCode }
    })
  }

  async get() {
    return await this.api.get(...arguments)
  }
  async post() {
    return await this.api.post(...arguments)
  }
}