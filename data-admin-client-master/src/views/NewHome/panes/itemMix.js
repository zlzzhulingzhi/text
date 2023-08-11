import SvgWrap from '@/views/Home/components/SvgWrap'
import ItemWrap from '@/views/Home/components/ItemWrap'
import ItemSubWrap from '@/views/Home/components/ItemSubWrap'
import ItemVideoWrap from '@/views/Home/components/ItemVideoWrap'
export default {
  components: {
    ItemWrap,
    SvgWrap,
    ItemVideoWrap,
    ItemSubWrap
  },
  mounted() {
    this.polling()
  },
  beforeDestroy() {
    clearInterval(this.timer.page)
  },
  data() {
    return {
      // 轮询间隔
      pollingInterval: 5000,

      timer: {
        page: null
      },
      loading: {
        page: false
      }
    }
  },
  methods: {
    polling() {
      this.getPageData().then()
      if (this.pollingInterval) this.timer.page = setInterval(() => this.getPageData(), this.pollingInterval)
    },
    async getPageData() {
      if (!this.dataAPI) return false
      this.loading.page = true
      let {code, data} = await this.dataAPI(this.params)
      this.loading.page = false
      if (code !== 200) return false

      this.pageData = data
    }
  }
}