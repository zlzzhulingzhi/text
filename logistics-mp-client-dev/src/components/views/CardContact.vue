<template>
  <view class="flex justify-between items-center py-3 px-4 u-border-bottom">
    <view class="flex-1 flex items-center">
      <u-avatar :src="item.headImgUrl" :size="36"></u-avatar>
      <view class="flex-1 ml-3">
        <view class="font-semibold">{{ item.realName }}</view>
        <view class="text-sm mt-1">{{ item.intro || '' }}</view>
      </view>
    </view>
    <view class="flex items-center">
      <u-icon name="weixin-fill" :size="28" color="#10b981" label="加微信" labelPos="bottom" labelSize="12" @click="onWechat"></u-icon>
      <view class="w-4"></view>
      <u-icon name="phone-fill" :size="28" color="#10b981" label="打电话" labelPos="bottom" labelSize="12" @click="onCall"></u-icon>
    </view>
  </view>
</template>

<script>
export default {
  name: 'CardContact',
  props: {
    item: {
      type: Object,
      default: () => {}
    }
  },
  methods: {
    onWechat() {
      this.$emit('onWechat')
    },
    onCall() {
      this.$emit('onCall')
    }
  }
}
</script>

<style lang="scss" scoped>
.u-border-bottom {
	border-color: #EEE !important;
}
</style>