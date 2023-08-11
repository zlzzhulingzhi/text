<template>
  <view class="px-4">
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>手机号</text>
      <!-- <u-icon name="arrow-right" :space="8" :label="userInfo.userAccount" labelPos="left"></u-icon> -->
      <text class="text-base text-gray-500">{{ userInfo.userAccount }}</text>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>姓名</text>
      <u-icon name="arrow-right" :space="8" :label="userInfo.userName" labelPos="left"></u-icon>
    </view>
    <!-- <view class="flex justify-center py-4 mt-8 u-border-top u-border-bottom" @click="reBind">
      <text class="text-primary">更换绑定手机号</text>
    </view> -->
  </view>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'MyAccount',
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    })
  },
  methods: {
    reBind() {
      uni.$u.route(this.$utils.getRoutePath('Login'), {
        reBind: true
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>