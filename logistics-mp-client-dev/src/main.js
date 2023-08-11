import Vue from 'vue'
import App from '@/App'
import store from '@/store'
import uView from 'uview-ui'
import { router, RouterMount } from '@/router'

import $logger from '@/lib/logger'
import * as $api from '@/api'
import * as $utils from '@/lib/utils'
import * as $message from '@/lib/message'
import filter from '@/lib/filter'

Vue.use(uView)
Vue.use(router)
Vue.use(filter)

Vue.prototype.$logger = $logger
Vue.prototype.$api = $api
Vue.prototype.$utils = $utils
Vue.prototype.$message = $message

Vue.config.productionTip = false
Vue.config.errorHandler = (err, vm, info) => {
  $logger.error(err.stack, $logger.TAG.VUE)
}
Vue.config.warnHandler = (msg, vm, trace) => {
  $logger.warn(msg, $logger.TAG.VUE)
}

App.mpType = 'app'

const app = new Vue({
  ...App,
  store
})

// #ifdef H5
RouterMount(app, router, '#app')
// #endif

// #ifndef H5
app.$mount()
// #endif
