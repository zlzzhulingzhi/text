<template>
  <view>
    <view class="bg-gray-100">
      <!--封面图-->
      <view class="relative">
        <u--image :src="activityDetail.coverUrl" width="100%" height="100%" mode="widthFix"></u--image>
        <view class="absolute bottom-0 w-full h-3 bg-white rounded-t-xl"></view>
      </view>
      <view class="px-4 pt-2 pb-4 bg-white">
        <view class="text-lg font-semibold mb-3">{{ activityDetail.title }}</view>
        <view class="flex my-3 mt-4">
          <u-icon name="clock-fill" color="#CCCCCC" size="16"></u-icon>
          <text class="text-sm text-gray-500 ml-2">{{ activityTime }}</text>
        </view>
      </view>
      <!--活动介绍-->
      <view class="bg-white p-4 rounded-md mt-3 mb-16">
        <view class="font-semibold mb-2">活动介绍</view>
        <u-parse :content="activityDetail.introduction"></u-parse>
      </view>
    </view>
    <!--分享弹窗-->
    <popup-share ref="popupShare"></popup-share>
    <!--加载页-->
    <u-loading-page :loading="loading.init"></u-loading-page>
  </view>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
export default {
  name: 'NewActivityDetail',
  data() {
    return {
      loading: {
        init: true
      },
      activityId: null,
      activityDetail: {},
      isPersonal: false
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    ...mapGetters({
      isLogin: 'system/isLogin',
      shareTypeMap: 'common/shareTypeMap'
    }),
    activityTime() {
      return uni.$u.timeFormat(this.activityDetail.activityTime,'mm月dd日 hh:MM')
    }
  },
  async onLoad(options) {
    // 扫码即学员
    if (options.scene) this.$store.commit('system/updateIdentity', 'student')
    let params = await this.$utils.getOptions(options)
    this.$logger.info(`🚀 ~ activityDetail onLoad ~ ${JSON.stringify(params)}`)
    this.activityId = params.id
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
      let { code, data } = await this.$api.Course.activityDetail({
        id: this.activityId
      })
      this.loading.init = false
      if (code !== 200) return false
      this.activityDetail = data || {}
      uni.setNavigationBarTitle({
        title: this.activityDetail.title
      })
    },
    // 分享
    showShare() {
      this.$refs.popupShare.open({
        id: this.activityId,
        page: 'pages/goods/activityDetail/index',
        scene: `id=${this.activityId}${this.employeeId ? `&ep=${this.employeeId}` : ''}`,
        memberId: this.userInfo.memberId,
        type: this.shareTypeMap.COURSE
      })
    }
  },
  onShareAppMessage() {
    return {
      title: this.activityDetail.courseName,
      path: `/pages/goods/activityDetail/index?id=${this.activityId}${this.employeeId ? `&ep=${this.employeeId}` : ''}`,
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