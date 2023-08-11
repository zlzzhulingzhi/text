/**
 * config 项目配置 管理
 */
const state = {
  // 项目名
  name: '',
  // logo地址
  logo: {
    src: require('@/assets/logo/minLogo.png'),
    Full: require('@/assets/logo/FullLogo.png'),
    alt: ''
  },
  // 备案（富文本）
  ICP: `<div>版权所有 国家网安基地培训中心</div>`,

  // 机构管理系统 org
  loginCode: 'org',

  // 域名映射
  domain: {
    // 微信回调域名
    wechat: process.env.VUE_APP_DOMAIN_WECHAT,
    screen: process.env.VUE_APP_SCREEN_DOMAIN,
    admin: process.env.VUE_APP_ADMIN_DOMAIN
  },

  // 微信公众号 appid
  wxAppId: process.env.VUE_APP_WECHAT_APPID,


  // 腾讯云 配置 Bucket 、 Region 、 SliceSize
  COSConfig: {
    Bucket: process.env.VUE_APP_COS_Bucket
  },
  // 腾讯云COS 文档转HTML支持的格式
  COSProcessType: [
    'pptx', 'ppt', 'pot', 'potx', 'pps', 'ppsx', 'dps', 'dpt', 'pptm', 'potm', 'ppsm',
    'doc', 'dot', 'wps', 'wpt', 'docx', 'dotx', 'docm', 'dotm',
    'xls', 'xlt', 'et', 'ett', 'xlsx', 'xltx', 'csv', 'xlsb', 'xlsm', 'xltm', 'ets',
    'pdf', 'lrc', 'c', 'cpp', 'h', 'asm', 's', 'java', 'asp', 'bat', 'bas', 'prg', 'cmd', 'rtf', 'txt', 'log', 'xml', 'htm', 'html'
  ],

  // 腾讯云直播 appId
  sdkAppId: Number(process.env.VUE_APP_PUSHER_SDK_APP_ID),
  // 腾讯云互动白板 appId
  boardSdkAppId: Number(process.env.VUE_APP_BOARD_SDK_APP_ID),

  // 常量
  constant: {
    // 导航高度
    headerHeight: '48px',
    // 缩放比例
    scale: 1.32
  },
  // 提示映射
  results: {
    Disabled: {
      src: require('@/assets/results/Disabled.png'),
      alt: '没有权限'
    },
    NoData: {
      src: require('@/assets/results/NoData.png'),
      alt: '暂无数据'
    },
    NotFound: {
      src: require('@/assets/results/NotFound.png'),
      alt: '页面找不到'
    },
    PleaseSelect: {
      src: require('@/assets/results/PleaseSelect.png'),
      alt: '请先选择筛选条件'
    }
  },
  // 常用图片
  images: {},

  // 初始密码
  // initPassword: 'waj123456',
  // 密码公钥
  passwordPublicKey: 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcXz5g5hNGAOgFOVhiYY8pYlrH3gXmHkYRBqqEHsVaKqjdY9PQbkfSb9VkD78VYwFyTSzMZn5e1A5XXXDTUL3PezflMc/GjvseB87OQ3a6XDHw5tVXiJruc72oN9GKEvh5TjQUzi2S1sTHXg8vdD/niXw+K/icrQFdzSpy1PWc6QIDAQAB',

  // 初始App, 不可删除、禁用
  disabledApp: /^System:Management:Plat$/,
  // 初始数据类型
  initCategory: /^init$/,

  // $message 预设提示语
  message: {
    Reset: '重置',
    SendMsg: '私信',
    OnShelve: '上架',
    OffShelve: '下架',
    Create: '新增',
    Delete: '删除',
    Enabled: '启用',
    Disabled: '禁用',
    Disable: '作废',
    Edit: '编辑',
    Update: '更新',
    SetUP: '设置',
    End: '结束',
    Remove: '移除'
  },

  // 最大子级层级 = 最大层级 - 1
  maxChildLevel: 5
}


const getters = {}


const mutations = {
  updateLogo: (state, d) => state.logo = {...state.logo, ...d}
}


const actions = {}


export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}