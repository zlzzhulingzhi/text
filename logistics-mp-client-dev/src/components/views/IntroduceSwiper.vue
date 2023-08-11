<template>
  <view class="mt-4 px-4">
    <u-swiper :list="introduceImgArr" circular showTitle height="260" keyName="image" :autoplay="true"
      @click="clickSwiper" @change="e => current = e.current">
    </u-swiper>
    <view class="dot_box">
      <view class="dot_dx">
        <view class="" :key="index" v-for="item, index in introduceImgArr"
          :class="index === current ? 'min_dotActive' : 'min_dot'"></view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'IntroduceSwiper',
  props: {
    introduceImgArr: {
      type: Array
    },
    imgArr: {
      type: Array
    }
  },
  data() {
    return {
      current: 0
    };
  },
  methods: {
    clickSwiper(index) {
      uni.previewImage({
        current: index,
        urls: this.imgArr
      })
    },
  },
};
</script>

<style lang="scss" scoped>
.dot_box {
  width: 100%;
  margin-top: 30rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  .dot_dx {
    display: flex;
    align-items: center;

    .min_dot {
      width: 10rpx;
      height: 10rpx;
      border-radius: 50%;
      background-color: #ccc;
      margin-right: 16rpx;
    }

    .min_dotActive {
      width: 40rpx;
      height: 10rpx;
      border-radius: 20px;
      background-color: #C63636;
      margin-right: 16rpx;
    }
  }
}
</style>