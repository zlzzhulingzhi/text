import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import i18n from '@/locales/i18n';
import $xss from 'xss'

// 全局组件
import './components'


// element-ui库
import ElementUI from 'element-ui'
// element-ui 主题
import 'element-ui/lib/theme-chalk/index.css'
// element-ui 自定义主题
import './style/theme/index.css'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
// 全局样式 + 样式库
import './style/global.styl'

// 工具类
import * as $utils from './lib/utils'
// 基于element-ui的表单规则验证
import * as $rules from './lib/rules'
// 过滤器
import filter from './lib/filter'
// 时间处理插件
import $moment from 'moment'
$moment.locale("zh-cn")

// 请求接口
import * as $api from './api'
import {$msg} from './lib/utils'

Vue.use(ElementUI)
Vue.use(filter)

// import Viewer from 'v-viewer'
// import 'viewerjs/dist/viewer.css'
// Vue.use(Viewer);

// import VViewer from "@michaelray/vue-viewer";
// Vue.use(VViewer);


// 绑定预设提示语
Vue.use({
  install(vue) {
    let msg = store.state.config.message
    for (const k in msg) {
      $utils.$msg[k] = $msg.bind(null, msg[k])
    }
    vue.prototype.$msg = $utils.$msg
  }
})

Vue.prototype.$utils = $utils
Vue.prototype.$rules = $rules
Vue.prototype.$api = $api
Vue.prototype.$moment = $moment
Vue.prototype.$xss = $xss
Vue.prototype.$EventBus = new Vue();
Vue.config.productionTip = false

new Vue({
  i18n,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
