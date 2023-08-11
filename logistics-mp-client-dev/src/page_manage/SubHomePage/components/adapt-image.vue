<template>
  <view class="w-full">
    <image :src="imageUrl" mode="aspectFit" :style="{ 'width': viewWidth + 'px', height: viewHeight + 'px' }"
      @load="imageLoad" class="image-adapt"></image>
  </view>
</template>
 
<script>


export default {
  name: 'adaptImage',
  props: {
    imageUrl: {
      type: String
    }
  },
  data() {
    return {
      viewHeight: 100,
      viewWidth: uni.getWindowInfo().screenWidth
    }
  },
  methods: {
    imageLoad(e) {
      let $width = e.detail.width,    //获取图片真实宽高
        $height = e.detail.height,
        ratio = $width / $height;    //图片的真实宽高比例
      // 图片显示宽度

      let viewWidth = uni.getWindowInfo().screenWidth, //设置图片显示宽度
        viewHeight = viewWidth / ratio;    //计算的高度值
      this.viewHeight = viewHeight
    }

  }
}
</script>
 
<style lang="scss" scoped>
.image-adapt {
  margin-bottom: -10px;
}
</style>