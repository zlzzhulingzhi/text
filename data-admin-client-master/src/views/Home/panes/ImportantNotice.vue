<template>
  <ItemWrap :heightSmall="true" :backgroud="require('@/assets/main/important_bg.png')">
    <div slot="title">重要通知</div>

    <el-carousel direction="vertical" indicator-position="none" height="120px" :interval="interval" :autoplay="Boolean(interval)">
      <el-carousel-item v-for="(list,index) in carouselItems" :key="index">
        <div class="flex column" v-for="(item,oIndex) in list" :key="oIndex">
          <div class="carousel-item" :style="{ fontSize: $utils.getScaleSize('12px') }">
            <div class="flex start-center title">
              <div class="flex center-center tag">{{ item.label | noticeLabel }}</div>
              <span class="flex-1 text-ellipsis">{{ item.title }}</span>
              <span>{{ item.publishDate }}</span>
            </div>
            <div class="content">
              <span class="line-height-22 height-full">{{ item.content }}</span>
            </div>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>
  </ItemWrap>
</template>

<script>
import itemMix from '@/views/Home/panes/itemMix'

export default {
  name: 'ImportantActions',
  mixins: [itemMix],
  data() {
    return {
      dataAPI: this.$api.Screen.notice,
      pageData: [],
      size: 1,
      pollingInterval: 0
    }
  },
  computed: {
    carouselItems() {
      let size = this.size
      let max = Math.ceil(this.pageData.length / size)
      let arr = []
      for (let i = 0; i < max; i++) {
        arr.push(this.pageData.slice(size * i, size * (i + 1)))
      }
      // console.log('this.pageData === ', this.pageData)
      return arr
    },
    interval() {
      if (!this.pageData.length) return 5000
      return (this.pageData[0].duration || 0) * 1000
    }
  }
}
</script>

<style scoped lang="stylus">
.carousel-item
  margin 0 32px
  color NEUTRAL_COLOR_F
  font-size 14px

  .title
    height 40px
    background-color rgba(11, 65, 209, 0.4)
    padding 0 22px

    .tag
      width 50px
      height 25px
      background #F24F4F
      border-radius 25px
      margin 0 10px

  .content
    margin 16px 16px 30px
</style>