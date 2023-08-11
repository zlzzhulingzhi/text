<template>
  <view class="h-screen">
    <!-- <u-sticky bgColor="#f3f4f6">
      <u-tabs :list="tabList" :scrollable="false" lineColor="#C63636" @click="tabClick"></u-tabs>
    </u-sticky> -->
    <template v-if="listData.length">
      <CardCourse :item="item" v-for="item in listData" :key="item.id" @click="goToDetail(item)"></CardCourse>
    </template>
    <u-loading-icon v-else-if="loading"></u-loading-icon>
    <uni-empty v-else></uni-empty>
  </view>
</template>

<script>
import { mapState, mapGetters } from 'vuex'
import CardCourse from '@/components/views/CardCourse.vue'

export default {
  name: 'MyCourse',
  components: {
    CardCourse
  },
  data() {
    return {
      loading: false,
      form: {
        current: 1,
        size: 10,
        status: undefined
      },
      total: 0,
      listData: []
    }
  },
  computed: {
    ...mapState('system', {
      authInfo: 'authInfo'
    }),
    ...mapGetters({
      courseStatus: 'common/courseStatus'
    }),
    tabList() {
      return [
        { name: '全部', id: undefined },
        ...this.courseStatus
      ]
    }
  },
  onLoad() {
    this.getList()
  },
  onReachBottom() {
    if (this.listData.length >= this.total) return false
    this.form.current = ++this.form.current
    this.getList()
  },
  methods: {
    tabClick(item) {
      if (this.form.status === item.id) return false
      this.form.status = item.id
      this.listData = []
      this.getList()
    },
    async getList() {
      this.loading = true
      let { code, data } = await this.$api.Course.myCourse(this.form)
      this.loading = false
      if (code !== 200) return false
      if (data.records && data.records.length) {
        let list = data.records.map(item => {
          return {
            ...item,
            isSignUp: true
          }
        })
        this.listData = this.listData.concat(list)
      }
      this.total = data.total
    },
    async goToDetail(item) {
      uni.$u.route(this.$utils.getRoutePath('ActivityDetail'), {
        id: item.id,
        psn: true
      })
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>
