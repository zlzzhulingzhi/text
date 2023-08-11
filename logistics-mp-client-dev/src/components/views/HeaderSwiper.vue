<template>
  <view class="mt-2 px-4">
    <u-swiper :list="introduceImgArr" circular height="160" keyName="imgUrl" :autoplay="true"
      @change="e => current = e.current" indicator indicatorMode="dot" imgMode="scaleToFill">
      <view slot="indicator" class="indicator">
        <view class="indicator__dot" v-for="(item, index) in introduceImgArr" :key="index"
          :class="[index === current && 'indicator__dot--active']">
        </view>
      </view>

    </u-swiper>
  </view>
</template>

<script>
export default {
  name: 'HeaderSwiper',
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
  },
};
</script>

<style lang="scss" scoped>
.indicator {
  @include flex(row);
  justify-content: center;

  &__dot {
    width: 10rpx;
    height: 10rpx;
    border-radius: 50%;
    background-color: #fff;
    margin-right: 16rpx;
    opacity: 0.6
  }

  &__dot--active {
    width: 40rpx;
    height: 10rpx;
    border-radius: 20px;
    background-color: #fff;
    margin-right: 16rpx;
  }
}
</style>