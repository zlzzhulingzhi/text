const state = {
  // 小程序AppId
  APPID: 'wxdda2a564a5ea6725',
  // 小程序静态资源存储位置
  COS_PATH: 'https://wuhan-jidi-1312651338.cos.ap-guangzhou.myqcloud.com/miniapp',
  // 全局事件名
  EVENT: {
    LOGIN: 'UNI_EVENT_LOGIN', // 登录成功
    ORDER: 'UNI_EVENT_ORDER', // 下单支付
    PROFILE: 'UNI_EVENT_PROFILE', // 修改资料
  }
}

const getters = {}

const mutations = {}

const actions = {}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}