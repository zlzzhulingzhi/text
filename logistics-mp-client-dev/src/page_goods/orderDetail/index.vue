<template>
  <view>
    <view class="px-8 py-12 top">
      <view class="flex items-center mb-2">
        <u--image src="/static/ic_status_paid.png" width="24" height="24"></u--image>
        <text class="text-3xl ml-3">已报名</text>
      </view>
    </view>
    <!--报名信息-->
    <view class="bg-white rounded-md mx-4 p-3">
      <view class="mb-2">报名信息</view>
      <view class="flex justify-between items-center py-2">
        <text class="mr-3">手机号</text>
        <text class="text-gray-500">{{ formData.phone }}</text>
      </view>
      <view class="flex justify-between items-center py-2">
        <text class="mr-3">姓名</text>
        <text class="text-gray-500">{{ formData.realName }}</text>
      </view>
      <view class="flex justify-between items-center py-2">
        <text class="mr-3">性别</text>
        <text class="text-gray-500">{{ formData.sex | sexList }}</text>
      </view>
      <view class="flex justify-between items-center py-2">
        <text class="mr-3">电子邮箱</text>
        <text class="text-gray-500">{{ formData.email }}</text>
      </view>
      <view class="flex justify-between items-center py-2">
        <text class="mr-3">备注</text>
        <text class="text-gray-500">{{ formData.remark }}</text>
      </view>
    </view>
    <!--报名机构-->
    <view class="flex bg-white rounded-md mx-4 p-3 mt-3">
      <text class="mr-3">报名机构</text>
      <text class="text-gray-500">{{ formData.orgName }}</text>
    </view>
    <!--报名课程-->
    <view class="bg-white rounded-md mx-4 p-3 mt-3">
      <view class="mb-2">报名课程</view>
      <view class="flex" @click="goToActivity">
        <u--image :src="formData.coverUrl" width="70" height="52" :radius="4"></u--image>
        <view class="flex-1 ml-2">
          <view class="mb-2">{{ formData.courseName }}</view>
          <view class="text-gray-500">讲师：{{ formData.lecturers }}</view>
        </view>
      </view>
    </view>
    <!--订单信息-->
    <view class="bg-white rounded-md mx-4 p-3 mt-3">
      <view class="mb-2">订单信息</view>
      <view class="mb-2">报名时间：{{ time }}</view>
    </view>
    <u-gap height="64"></u-gap>
    <!--订单操作-->
    <!-- <view class="flex justify-between items-center bg-white px-4 py-3 footer-bar">
      <u-button type="info" shape="circle" text="取消订单" @click="onCancel"></u-button>
      <view class="w-4"></view>
      <u-button type="success" shape="circle" :loading="loading.submit" text="立即支付" @click="onPay"></u-button>
    </view> -->
    <!--加载页-->
    <u-loading-page :loading="loading.init"></u-loading-page>
  </view>
</template>

<script>
import { mapState, mapGetters } from 'vuex'
import { loginMixins } from '@/mixins/loginMixins.js'
import { Payment } from '@/lib/pay.js'

export default {
  name: 'OrderDetail',
  mixins: [loginMixins],
  data() {
    return {
      loading: {
        init: false,
        submit: false
      },
      orderNo: null,
      orderDetail: {
        shop: {},
        goods: {}
      },
      formData: {}
    }
  },
  computed: {
    ...mapState('system', {
      authInfo: 'authInfo'
    }),
    time() {
      return uni.$u.timeFormat(Date.now(), 'yyyy-mm-dd hh:MM:ss')
    }
  },
  async onLoad(options) {
    let params = await this.$utils.getOptions(options)
    this.formData = params || {}
  },
  methods: {
    // 登录成功
    onLoginSuccess() {
      if (this.orderNo) this.getDetail()
    },
    async getDetail() {
      this.loading.init = true
      let { code, data } = await this.$api.Order.detail({
        orderNo: this.orderNo
      })
      this.loading.init = false
      if (code !== 200) return false
      this.orderDetail = {...data}
      try {
        this.orderDetail.formResult = JSON.parse(data.formResult)
      } catch (error) {
        this.orderDetail.formResult = [{}, {}, {}]
      }
    },
    onCancel() {
      uni.showModal({
        title: '取消订单？',
        content: '取消后，将不再保留当前课程名额',
        cancelText: '我再想想',
        confirmText: '取消订单',
        success: async (res) => {
          if (res.confirm) {
            let { code, data } = await this.$api.Order.cancel({
              orderNo: this.orderDetail.orderNo
            })
            if (code !== 200) return false
            uni.showToast({ title: '取消订单成功', icon: 'noen' })
            this.getDetail()
          }
        }
      })
    },
    async onPay() {
      this.loading.submit = true
      let { code, data } = await this.$api.Order.pay({
        orderNo: this.orderDetail.orderNo,
        platform: 'wxpay',
        platformTradeType: 'JSAPI',
        platformUser: this.authInfo.openId
      })
      this.loading.submit = false
      if (code !== 200) return false

      let { status } = await Payment(data)
      if (status === 'success') this.getDetail()
    },
    goToActivity() {
      uni.$u.route(this.$utils.getRoutePath('ActivityDetail'), {
        id: this.orderDetail.goods.goodsId,
        sp: this.orderDetail.shop.id
      })
    }
  }
}
</script>

<style lang="scss">
page {
  background: $uni-bg-color-grey;
}
</style>

<style lang="scss" scoped>
.top {
  background: linear-gradient(360deg, rgba(235,242,255,0.2) 0%, #5A8FFF 100%);
}
</style>