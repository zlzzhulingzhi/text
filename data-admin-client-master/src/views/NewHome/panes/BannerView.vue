<template>
  <ItemVideoWrap :backgroud="require('@/assets/main/bg_center_top.png')">
    <div class="flex column padding-left-24 padding-right-24 padding-bottom-54 height-100p">
      <!-- <div ref="carouselWrap" class="flex-1 overflow">
        <el-carousel :height="height" :interval="interval" :autoplay="Boolean(interval)" indicator-position="none">
          <el-carousel-item v-for="(item,index) in pageData" :key="index">
            <el-image class="width-100p height-100p" fit="cover" :src="item.fileUrl"></el-image>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="flex between-center" :style="{ margin: '32px -10px' }">
        <div class="height-112 flex-1 margin-left-10 margin-right-10" v-for="(item,index) in pageData.slice(0, 3)" :key="index">
          <el-image class="width-100p height-100p" fit="cover" :src="item.fileUrl"></el-image>
        </div>
      </div> -->
      <video class="margin-bottom-16 width-100p" :src="srcUrl" controls autoplay muted loop></video>
      <el-image class="height-44" :src="require('@/assets/main/dot_n_line.png')"></el-image>
    </div>
  </ItemVideoWrap>
</template>

<script>
import itemMix from '@/views/Home/panes/itemMix'

export default {
  name: 'RealTimeData',
  mixins: [itemMix],
  mounted() {
    // this.height = this.$refs.carouselWrap.clientHeight + 'px'
  },
  data() {
    return {
      height: '428px',
      dataAPI: this.$api.Screen.banner,
      pageData: [],
      pollingInterval: 0
    }
  },
  computed: {
    interval() {
      if (!this.pageData.length) return 3000
      return (this.pageData[0].duration || 0) * 1000
    },
    srcUrl() {
      return `${location.pathname}video.mp4`
    }
  }
}
</script>

<style scoped lang="stylus">

</style>