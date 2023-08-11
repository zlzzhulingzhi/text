<template>
  <view class="h-screen">
    <HeaderSwiper ref="headerSwiper" :introduceImgArr="introduceImgArr"></HeaderSwiper>

    <template v-if="listData.length">
      <CardActivity :item="item" v-for="item in listData" :key="item.id" @click="goToDetail(item)"></CardActivity>
    </template>
    <u-loading-icon v-else-if="loading"></u-loading-icon>
    <uni-empty v-else></uni-empty>
  </view>
</template>

<script>
import { mapState, mapGetters } from 'vuex'
import CardActivity from '@/components/views/CardActivity.vue'
import HeaderSwiper from '@/components/views/HeaderSwiper.vue'

export default {
  name: 'tabActivity',
  components: {
    CardActivity,
    HeaderSwiper
  },
  data() {
    return {
      loading: false,
      form: {
        current: 1,
        size: 10,
      },
      total: 0,
      listData: [],
      introduceImgArr: [],
    }
  },
  computed: {
  },
  onLoad() {
    this.getBanners()
    this.getList()
  },
  onReachBottom() {
    if (this.listData.length >= this.total) return false
    this.form.current = ++this.form.current
    this.getList()
  },
  async onPullDownRefresh() {
    this.form.current = 1
    this.listData = []
    this.total = 0
    await this.getList()
    uni.stopPullDownRefresh()
  },
  onShareAppMessage() {},
  methods: {
    async getList() {
      this.loading = true
      let { code, data } = await this.$api.Course.activity(this.form)
      this.loading = false
      if (code !== 200) return false
      if (data.records && data.records.length) {
        let list = data.records
        this.listData = this.listData.concat(list)
      }
      this.total = data.total
    },
    async goToDetail(item) {
      uni.$u.route(this.$utils.getRoutePath('NewActivityDetail'), {
        id: item.id
      })
    },
    // 获取banner图
    async getBanners() {
      let { code, data } = await this.$api.Course.getBanner({ section: 'activity' })
      if(code !== 200) {
        return false
      }
      if(data.length > 0) {
        this.introduceImgArr = data
      }
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>
