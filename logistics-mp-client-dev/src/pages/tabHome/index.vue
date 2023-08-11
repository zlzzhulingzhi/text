<template>
  <view>
    <!--机构/平台-->
    <HeaderSwiper ref="headerSwiper" :introduceImgArr="introduceImgArr"></HeaderSwiper>
    <template v-if="(isOrg || isAdmin)">
      <view class="mb-6">
        <view class="flex justify-around items-center mt-4">
          <view class="flex flex-col justify-center items-center" v-for="item in menuList" :key="item.id"
            @click="goToPage(item)">
            <view class="w-12 h-12 flex justify-center items-cente">
              <u--image :src="item.icon" width="46" height="46"></u--image>
            </view>
            <view class="mt-2 text-sm">
              {{ item.name }}
            </view>
          </view>
        </view>
      </view>
      <template v-if="listData.length">
        <view>
          <template v-if="isOrg">
            <CardCourse :item="item" v-for="item in listData" :key="item.id" @click="goToDetail(item)">
              <template #action>
                <view @click.stop="goToStudent(item)">
                  <u-button type="primary" shape="circle" size="small">预报学员</u-button>
                </view>
              </template>
            </CardCourse>
          </template>

          <template v-else>
            <CardWorkerOrder :item="item" v-for="item in listData" :key="item.id" @click="goToDetail(item)">
            </CardWorkerOrder>
          </template>
        </view>
      </template>
    </template>
    <!--学员-->
    <template v-else>
      <u-sticky bgColor="#FFFFFF">
        <u-tabs :list="tabList" :scrollable="false" lineColor="#C63636" @click="tabClick" :current="currentTab"></u-tabs>
      </u-sticky>
      <template v-if="listData.length">
        <CardCourse :item="item" v-for="item in listData" :key="item.id" @click="goToDetail(item)"></CardCourse>
      </template>
      <u-loading-icon v-else-if="loading.list"></u-loading-icon>
      <uni-empty v-else></uni-empty>
    </template>
    <!--加载页-->
    <u-loading-page :loading="loading.init"></u-loading-page>
  </view>
</template>

<script>
import { mapState, mapGetters } from 'vuex'
import CardCourse from '@/components/views/CardCourse.vue'
import CardWorkerOrder from '@/components/views/CardWorkerOrder.vue'
import HeaderSwiper from '@/components/views/HeaderSwiper.vue'

export default {
  name: 'tabHome',
  components: {
    CardCourse,
    CardWorkerOrder,
    HeaderSwiper
  },
  data() {
    return {
      loading: {
        init: true,
        list: false
      },
      form: {
        current: 1,
        size: 10
      },
      total: 0,
      listData: [],
      currentTab: 0,
      showTexts: { student: '课程', org: '机构', admin: '平台' },
      introduceImgArr: []
    }
  },
  computed: {
    ...mapState('config', {
      cosPath: 'COS_PATH'
    }),
    ...mapState('system', {
      identity: 'identity'
    }),
    ...mapGetters({
      isLogin: 'system/isLogin',
      isStudent: 'system/isStudent',
      isOrg: 'system/isOrg',
      isAdmin: 'system/isAdmin'
    }),
    tabList() {
      return [
        { id: 0, name: '全部', submitFn: this.$api.Course.page },
        { id: 1, name: '我报名的', disabled: !this.isLogin, submitFn: this.$api.Course.myCourse }
      ]
    },
    menuList() {
      let list = [
        { id: 1, name: '报事报修', icon: '/static/img_sign.png', route: this.isAdmin ? 'Query' : 'WorkOrder', params: { type: 'WorkOrder' } },
        { id: 2, name: '教室导览', icon: '/static/img_student.png', route: 'Query', params: { type: 'Classroom' } },
        { id: 3, name: '宿舍导览', icon: '/static/img_class.png', route: 'Query', params: { type: 'Dormitory' } }
      ]
      if (this.isOrg) {
        list.unshift({ id: 4, name: '机构课程', icon: '/static/img_course.png', route: 'OrgCourse' })
      } else if (this.isAdmin) {
        list.push({ id: 4, name: '开课信息', icon: '/static/img_course.png', route: 'Query', params: { type: 'OpenClass' } })
        list.unshift({ id: 5, name: '统计概览', icon: '/static/img_course.png', route: 'Statistics'})

      }

      return list
    }
  },
  watch: {
    identity: {
      deep: true,
      handler(newVal) {
        // 更换平台时更新数据
        this.form.current = 1
        this.listData = [];
        this.getList();
      },
      immediate: false
    }
  },
  onLoad() {
    this.getBanners()
    this.getList()
  },
  onShow() {
    setTimeout(() => {
      uni.setNavigationBarTitle({
        title: this.showTexts[this.identity]
      })
    }, 100)
  },
  onShareAppMessage() { },
  onReady() {
    if (this.identity) return this.loading.init = false
    this.$nextTick(() => {
      uni.$u.route(this.$utils.getRoutePath('Switch'))
    })
    this.loading.init = false
  },
  onReachBottom() {
    if (this.listData.length >= this.total) return false
    this.form.current = ++this.form.current
    this.getList()
  },
  methods: {
    tabClick(item) {
      if (this.currentTab === item.id) return false
      if (!this.isLogin) return this.$message.showToast('请先登录')
      this.currentTab = item.id
      this.form.current = 1
      this.listData = []
      this.getList()
    },
    async getList() {
      let requestAction = this.tabList.find(v => v.id === this.currentTab).submitFn;
      if (this.isAdmin) {
        requestAction = this.$api.WorkOrder.platMatterReportPage;
      } else if (this.isOrg) {
        requestAction = this.$api.Course.orgPage
      }
      this.loading.list = true
      let { code, data } = await requestAction(this.form)
      this.loading.list = false
      if (code !== 200) return false
      if (data.records && data.records.length) {
        if (this.isAdmin) {
          let list = data.records;
          this.listData = this.listData.concat(list);
        } else {
          let list = data.records.map(item => {
            return {
              ...item,
              isSignUp: Boolean(this.currentTab)
            }
          })
          this.listData = this.listData.concat(list)
        }
      }
      this.total = data.total
    },
    async goToDetail(item) {
      if (this.isAdmin) {
        uni.$u.route(this.$utils.getRoutePath('WorkOrderDetail'), {
          id: item.id
        })
      } else {
        uni.$u.route(this.$utils.getRoutePath('ActivityDetail'), {
          id: item.id,
          psn: Boolean(this.currentTab)
        })
      }
    },
    goToStudent(item) {
      this.goToPage({ route: 'ReserveStudent', params: { orgId: item.orgId, courseId: item.id } })
    },
    goToPage(item) {
      if (!item.route) return false
      uni.$u.route(this.$utils.getRoutePath(item.route), item.params)
    },
    // 获取banner图
    async getBanners() {
      let { code, data } = await this.$api.Course.getBanner({ section: 'course' })
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

<style lang="scss">
.menu-item {
  width: 43%;
}
</style>