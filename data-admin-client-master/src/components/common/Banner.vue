
<template>
  <el-carousel :height="height" :autoplay="false">
    <el-carousel-item v-for="(item,index) in list" :key="index" class="flex center-center">
      <slot item="item">
        <el-image :src="item.filePath" fit="cover">
          <div :style="{height}" class="error-box width-100p flex center-center column bg-f2" slot="error">
            <b class="el-icon-picture"></b>
            <div class="font-24">图片广告</div>
          </div>
        </el-image>
      </slot>
    </el-carousel-item>
  </el-carousel>
</template>

<script>
export default {
  name: 'Banner',
  props: {
    list: {
      type: Array,
      default: () => []
    },
    height:{
      type: String,
      default: '380px'
    }
  }
}
</script>

<style scoped lang="stylus">
.el-image
  width 100%
  min-width 1920px

  >>> .error-box
    font-size 120px
    color SHADOW_COLOR

.el-carousel
  >>> .el-carousel__indicators
    .el-carousel__indicator
      &.is-active
        .el-carousel__button
          width 60px

      .el-carousel__button
        background-color #c4c4c4
        height 12px
        width 12px
        border-radius 6px
</style>