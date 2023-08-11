<template>
  <view>
    <view class="bg-gray-100" v-if="activityDetail.shelfStatus">
      <!--职工名片-->
      <EmployeeCard :item="employeeData"></EmployeeCard>
      <!--封面图-->
      <view class="relative">
        <u--image :src="activityDetail.coverUrl" width="100%" height="100%" mode="widthFix"></u--image>
        <view class="absolute bottom-0 w-full h-3 bg-white rounded-t-xl"></view>
      </view>
      <view class="px-4 pt-2 pb-4 bg-white">
        <view class="text-lg font-semibold mb-3">{{ activityDetail.courseName }}</view>
        <view class="my-3">
          <text class="text-xs text-gray-500">讲师：{{ (activityDetail.lecturers || []).map(v => v.lecturerName).join(', ') }}</text>
        </view>
      </view>
      <!--报名机构-->
      <view class="bg-white p-4 rounded-md mt-3">
        <view class="font-semibold mb-4">报名机构</view>
        <view class="flex items-center">
          <u--image :src="`${cosPath}/ic_location.png`" width="18" height="18">
            <u-icon slot="error" name="gift" size="22"></u-icon>
          </u--image>
          <text class="ml-2">{{ activityDetail.orgName }}</text>
        </view>
      </view>
      <!--活动介绍-->
      <view class="bg-white p-4 rounded-md mt-3 mb-16">
        <view class="font-semibold mb-2">课程介绍</view>
        <u-parse :content="activityDetail.introduction"></u-parse>
      </view>
      <!--底部栏-->
      <view class="flex items-center bg-white px-4 py-3 footer-bar">
        <template v-if="isLogin">
          <view class="flex items-center mx-4" @click="showShare">
            <u--image src="/static/ic_share.png" width="19" height="19"></u--image>
            <text class="text-sm ml-2">分享</text>
          </view>
          <view class="flex-1">
            <u-button type="warning" shape="circle" text="已报名，回到首页" @click="goToOrder" v-if="activityDetail.isSignUp"></u-button>
            <u-button type="primary" shape="circle" text="立即报名" @click="goToPay" v-else></u-button>
          </view>
        </template>
        <!--未登录-->
        <uni-bind-phone class="w-full" @success="getDetail" v-else>
          <view class="flex justify-between items-center">
            <view class="flex items-center ml-4 mr-8">
              <u--image src="/static/ic_share.png" width="19" height="19"></u--image>
              <text class="text-sm ml-2">分享</text>
            </view>
            <view class="flex-1">
              <u-button type="primary" shape="circle" text="立即报名"></u-button>
            </view>
          </view>
        </uni-bind-phone>
      </view>
    </view>
    <view class="pt-8" v-else-if="!loading.init">
      <uni-empty text="课程已下架"></uni-empty>
    </view>
    <!--分享弹窗-->
    <popup-share ref="popupShare"></popup-share>
    <!--加载页-->
    <u-loading-page :loading="loading.init"></u-loading-page>
  </view>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import { loginMixins } from '@/mixins/loginMixins.js'
import EmployeeCard from '@/components/views/EmployeeCard.vue'

export default {
  name: 'ActivityDetail',
  components: {
    EmployeeCard
  },
  mixins: [loginMixins],
  data() {
    return {
      loading: {
        init: true,
        bargain: false
      },
      activityId: null,
      activityDetail: {},
      employeeId: undefined,
      buyerList: [],
      grouponList: [],
      inviteList: [],
      employeeData: {},
      isPersonal: false
    }
  },
  computed: {
    ...mapState('config', {
      cosPath: 'COS_PATH'
    }),
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    ...mapState('common', {
      orderStatusMap: 'orderStatusMap'
    }),
    ...mapGetters({
      isLogin: 'system/isLogin',
      shareTypeMap: 'common/shareTypeMap'
    })
  },
  async onLoad(options) {
    // 扫码即学员
    if (options.scene) this.$store.commit('system/updateIdentity', 'student')
    let params = await this.$utils.getOptions(options)
    this.$logger.info(`🚀 ~ activityDetail onLoad ~ ${JSON.stringify(params)}`)
    this.activityId = params.id
    this.employeeId = params.ep
    this.isPersonal = params.psn ? JSON.parse(params.psn) : false
    if (this.activityId) this.getDetail()
    // #ifdef MP
    uni.hideShareMenu()
    // #endif
  },
  methods: {
    // 登录成功
    onLoginSuccess() {
      if (this.activityId) this.getDetail()
    },
    // 活动详情
    async getDetail() {
      this.loading.init = true
      let apiFn = this.$api.Course.info
      if (this.isPersonal) apiFn = this.$api.Course.personalInfo
      let { code, data } = await apiFn({
        id: this.activityId
      })
      this.loading.init = false
      if (code !== 200) return false
      this.activityDetail = data || {}
      uni.setNavigationBarTitle({
        title: this.activityDetail.courseName
      })
    },
    // 跳转确认订单
    goToPay() {
      uni.navigateTo({
        url: `${this.$utils.getRoutePath('OrderCheck')}?id=${this.activityId}&ep=${this.employeeId}`,
        events: {
          updateOrder: () => this.getDetail()
        }
      })
    },
    // 跳转订单详情
    goToOrder() {
      uni.$u.route({
        url: this.$utils.getRoutePath('Home'),
        type: 'tab',
        params: {
          tab: 1 // 我报名的
        }
      })
    },
    // 分享
    showShare() {
      this.$refs.popupShare.open({
        id: this.activityId,
        page: 'pages/goods/activityDetail/index',
        scene: `id=${this.activityId}${ this.employeeId ? `&ep=${this.employeeId}` : '' }`,
        memberId: this.userInfo.memberId,
        type: this.shareTypeMap.COURSE
      })
    }
  },
  onShareAppMessage() {
    return {
      title: this.activityDetail.courseName,
      path: `/pages/goods/activityDetail/index?id=${this.activityId}${ this.employeeId ? `&ep=${this.employeeId}` : '' }`,
      imageUrl: `https:${this.activityDetail.coverUrl}`
    }
  }
}
</script>

<style lang="scss" scoped>
.btn {
  background: $uni-color-error;
  color: $uni-text-color-inverse;
  border-radius: 50px;
}
</style>