<!--职工二维码名片弹窗-->
<template>
  <u-modal :show="showModal" :showConfirmButton="false" closeOnClickOverlay @close="closeModal">
    <view class="flex flex-col items-center w-full">
      <view class="flex w-full">
        <u-avatar :src="currentPage.headImgUrl" :size="64"></u-avatar>
        <view class="flex-1 ml-3">
          <view class="text-lg mb-2">{{ currentPage.realName }}</view>
          <view class="text-sm text-gray-400">{{ currentPage.intro || '' }}</view>
        </view>
      </view>
      <view class="w-full bg-gray-100 p-3 rounded mt-3 mb-6">
        <text class="text-success">您好！\n请扫描我的微信二维码，添加好友联系我</text>
      </view>
      <view class="qrcode">
        <u--image :src="currentPage.wxQrCode" width="180" height="180"></u--image>
      </view>
      <text class="text-gray-500 mt-6">长按二维码可添加老师微信</text>
    </view>
  </u-modal>
</template>

<script>
export default {
  name: 'ModalContact',
  data() {
    return {
      showModal: false,
      currentPage: {}
    }
  },
  methods: {
    open(data = {}) {
      this.currentPage = data
      this.showModal = true
    },
    closeModal() {
      this.showModal = false
    }
  }
}
</script>

<style lang="scss" scoped>
.qrcode {
  border: 3px solid #1BCEC8;
  border-radius: 4px;
  box-shadow: 5px 5px 0 0 #1BCEC8;
}
</style>