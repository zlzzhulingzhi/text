<template>
  <ItemWrap :heightMiddle="true" :backgroud="require('@/assets/main/data_overview_bg.png')">
    <div slot="title">数据总览</div>

    <div class="flex column height-100p text-f text-center margin-top-20 data-cover">

      <div class="flex-1 flex" v-for="(row, index) in groupList" :key="index">
        <div class="flex-1 shrink-0" v-for="item in row" :key="item.fieldCode">
          <el-image class="width-78" :src="require(`@/assets/main/overview_${item.fieldCode}.png`)" alt="开班课程"></el-image>
          <div class="data-num" :style="{ fontSize: $utils.getScaleSize('28px') }">{{ item.displayValue || 0 | number }}</div>
          <div class="data-title" :style="{ fontSize: $utils.getScaleSize('14px') }">{{ item.displayName }}</div>
        </div>
      </div>

    </div>
  </ItemWrap>
</template>

<script>
import itemMix from '@/views/Home/panes/itemMix'

export default {
  name: 'DataOverview',
  mixins: [itemMix],
  data() {
    return {
      dataAPI: this.$api.Screen.dataOverview,
      pageData: [],
      size: 3,
      pollingInterval: 0
    }
  },
  computed: {
    groupList() {
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
.data-cover
  position relative
  z-index 9999
.data-num
  font-size 30px
  font-weight bold
  margin-top 16px
  z-index 9999

.data-title
  font-size 16px
  margin-top 2px
</style>