<template>
  <ItemWrap :backgroud="require('@/assets/main/training_class_bg.png')" :heightThirtyFull="true">
    <div slot="title">重点培训班</div>

    <div class="carousel-item title" :style="{ fontSize: $utils.getScaleSize('12px') }">
      <div class="index">序号</div>
      <div class="flex-2 text-ellipsis">班级</div>
      <div class="flex-3 text-ellipsis">所属机构</div>
      <div class="width-100 text-ellipsis">教室</div>
      <div class="width-60 text-ellipsis">学员数</div>
    </div>

    <el-carousel direction="vertical" indicator-position="none" height="360px" :interval="5000" @change="sliderChange">
      <el-carousel-item v-for="(list,index) in carouselItems" :key="index">
        <div class="flex column" v-for="(item,oIndex) in list" :key="oIndex">
          <div class="carousel-item carousel" :style="{ fontSize: $utils.getScaleSize('12px') }">
            <div class="index">{{ oIndex + 1 + index * size }}</div>
            <div class="flex-2 text-ellipsis padding-right-16">{{ item.clazzName }}</div>
            <div class="flex-3 text-ellipsis padding-right-16">{{ item.orgName }}</div>
            <div class="width-100 text-ellipsis padding-right-16"><i class="el-icon-monitor margin-right-10"></i>{{ item.classroom }}</div>
            <div class="width-60 text-ellipsis"><i class="el-icon-user margin-right-10"></i>{{ item.studentNum }}</div>
          </div>
          <div class="progress margin-left-32 margin-right-32">
            <div class="progress-bar" :style="{ width: `${item.progress || 0}%` }">
              <div class="progress-mark"></div>
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
  name: 'TodayClass',
  mixins: [itemMix],
  data() {
    return {
      dataAPI: this.$api.Screen.attendClass,
      pageData: [],
      pollingInterval: 0,
      size: 4
    }
  },
  watch: {
    pageData: {
      deep: true,
      handler(newVal, oldVal) {
        if (newVal.length && !oldVal.length) {
          this.pageData = this.pageData.map(item => {
            return {
              ...item,
              progress: Math.floor(item.studentNum / Math.max(...this.pageData.map(v => v.studentNum)) * 100)
            }
          })
        }
      }
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
  },
  methods: {
    sliderChange(index) {
      this.pageData = this.pageData.map(item => {
        return {
          ...item,
          progress: 0
        }
      })
      setTimeout(() => {
        this.pageData = this.pageData.map(item => {
          return {
            ...item,
            progress: Math.floor(item.studentNum / Math.max(...this.pageData.map(v => v.studentNum)) * 100)
          }
        })
      }, 300)
    }
  }
}
</script>

<style scoped lang="stylus">
.carousel-item
  display flex
  margin 0 32px
  line-height 40px
  color NEUTRAL_COLOR_F
  font-size 14px

  &.carousel
    line-height 52px

  .index
    width 60px
    text-align center
    flex-shrink 0

  &.title
    background-color rgba(13, 84, 246, 0.4)

.progress
  border-bottom 1px solid #0D54F6

  .progress-bar
    height 4px
    background linear-gradient(90deg, #1456C7, #09FDFF)
    position relative
    transition all ease 1s

    .progress-mark
      width 4px
      height 10px
      background NEUTRAL_COLOR_F
      position absolute
      right 0
      top -3px
</style>