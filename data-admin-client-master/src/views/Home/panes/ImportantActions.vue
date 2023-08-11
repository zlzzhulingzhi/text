<template>
  <ItemWrap :heightFull="true" class="padding-bottom-16">
    <div slot="title">培训中心重要活动</div>

    <div class="carousel-item title">
      <div class="index">序号</div>
      <div class="flex-2 text-ellipsis">班级</div>
      <div class="flex-2 text-ellipsis">课程</div>
      <div class="flex-3 text-ellipsis">所属机构</div>
    </div>

    <el-carousel direction="vertical" indicator-position="none" height="110px" :interval="5000">
      <el-carousel-item v-for="(list,index) in carouselItems" :key="index">
        <div class="carousel-item" v-for="(item,oIndex) in list" :key="oIndex">
          <div class="index">{{ oIndex + 1 + index * size }}</div>
          <div class="flex-2 text-ellipsis padding-right-16">{{ item.teamName }}</div>
          <div class="flex-2 text-ellipsis padding-right-16">{{ item.className }}</div>
          <div class="flex-3 text-ellipsis padding-right-16">{{ item.orgName }}</div>
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
      dataAPI: this.$api.Mock.classList,
      pageData: [],
      size: 3
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
      return arr
    }
  }
}
</script>

<style scoped lang="stylus">
.carousel-item
  display flex
  border-bottom 1px solid #2e3aa4
  margin 0 32px
  line-height 32px
  color NEUTRAL_COLOR_F
  font-size 14px

  .index
    width 60px
    text-align center
    flex-shrink 0

  &.title
    background-color #061e8c
</style>