<template>
  <view class="py-4 bg-content flex-col justify-between h-full">
    <view class="flex flex-col items-center rounded py-8 mx-4 bg-white justify-between sign-content">
      <view class="flex flex-col items-center">
        <u--image :src="require('@/static/sign-clock.png')" width="46" height="46"></u--image>
        <text class="text-green-400 text-lg font-bold mt-4">你已在打卡范围内</text>
        <text class="text-green-400 text-base mt-4">国家网安基地培训中心</text>
      </view>

      <view class="rounded-full flex justify-center items-center flex-col circle-border"
        @click="$u.throttle(signTouch, 1000, true)">
        <text class="text-lg">{{ currentTime }}</text>
        <text class="sign-text">{{ signText }}</text>
      </view>
    </view>

    <!-- <view class="sign-place px-4">
      <view class="h-10 mt-2 bg-white flex justify-between px-1 rounded sign-sub">
        <view class="flex h-full items-center">
          <u-icon name="list" size="18"></u-icon>
          <text class="text-sm ml-2">本月待处理异常</text>
        </view>
        <view class="flex h-full items-center">
          <text class="text-sm mr-1">8天</text>
          <u-icon name="arrow-right" size="18"></u-icon>
        </view>
      </view>
    </view> -->
  </view>
</template>

<script>
export default {
  name: 'PersonSign',
  props: {
    signText: {
      type: String,
      default: '学习打卡'
    }
  },
  data() {
    return {
      currentTime: uni.$u.timeFormat(new Date(), 'hh : MM'),
      timeIntervel: null
    };
  },

  mounted() {
    const _this = this
    _this.timeIntervel = setInterval(() => {
      _this.currentTime = uni.$u.timeFormat(new Date(), 'hh : MM')
    }, 1000)
  },
  deactivated() {
    if (this.timeIntervel) {
      clearInterval(this.timeIntervel)
    }
  },
  computed: {
  },

  methods: {
    // 手动打卡
    signTouch() {
      this.$emit('signTouch')
    }
  },
};
</script>

<style lang="scss" scoped>
.bg-content {
  background: rgb(243, 244, 246);
  flex: auto;

  .sign-content {
    height: 80%;

    .circle-border {
      border-width: 6px;
      border-color: rgb(251, 191, 36);
      border-style: solid;
      width: 90px;
      height: 90px;

      .sign-text {
        font-size: 14px;
      }
    }
  }

  .sign-place {
    height: 20%;
    background: rgb(243, 244, 246);
  }
}
</style>