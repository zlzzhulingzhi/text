<template>
  <view class="w-full h-full">
    <u-swiper :list="introduceImgArr" circular height="100%" width="100%" keyName="url" :autoplay="true"
      @change="e => current = e.current" indicator indicatorMode="dot" imgMode="scaleToFill">
      <view slot="indicator" class="indicator">
        <view class="indicator__dot" v-for="(item, index) in introduceImgArr" :key="index"
          :class="[index === current && 'indicator__dot--active']">
        </view>
      </view>
    </u-swiper>
    <view class="absolute bottom-16 left-10 right-10 check-more h-10 rounded-full flex justify-center items-center" @click="checkMoreAction">
      <text class="check-more-text">了解更多</text>
    </view>
  </view>
</template>

<script>
export default {
  name: 'HomeFullSwiper',
  props: {
    introduceImgArr: {
      type: Array
    }
  },
  data() {
    return {
      current: 0
    };
  },
  methods: {
    checkMoreAction() {
      let currentObj = this.introduceImgArr[this.current]
      uni.$u.route(this.$utils.getRoutePath('SubHomePage'),{type:currentObj.type})
    }
  },
};
</script>

<style lang="scss" scoped>
.indicator {
  @include flex(row);
  justify-content: center;

  &__dot {
    width: 60rpx;
    height: 5rpx;
    border-radius: 20px;
    background-color: #C0C0C0;
    margin-right: 16rpx;
  }

  &__dot--active {
    width: 60rpx;
    height: 5rpx;
    border-radius: 20px;
    background-color: #FFFFFF;
    margin-right: 16rpx;
  }
}

.check-more {
  border: 1px solid #FFFFFF;
  .check-more-text {
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #FFFFFF;
    line-height: 35px;
    font-size: 16px;
  }
}
</style>