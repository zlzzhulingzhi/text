<template>
  <ItemWrap :heightMiddle="true" :backgroud="require('@/assets/main/data_statistics_bg.png')">
    <div slot="title">数据动态统计</div>

    <ClassDataPieChart class="chart" :pageData="pageData.classroom"></ClassDataPieChart>
    <DormitoryDataPieChart class="chart" :pageData="pageData.dormitory"></DormitoryDataPieChart>

  </ItemWrap>
</template>

<script>
import itemMix from '@/views/Home/panes/itemMix'
import ClassDataChart from '@/views/Home/panes/RealTimeData/ClassDataChart'
import DormitoryDataChart from '@/views/Home/panes/RealTimeData/DormitoryDataChart'
import ClassDataPieChart from '@/views/Home/panes/RealTimeData/ClassDataPieChart'
import DormitoryDataPieChart from '@/views/Home/panes/RealTimeData/DormitoryDataPieChart'

export default {
  name: 'RealTimeData',
  mixins: [itemMix],
  components: {
    ClassDataChart,
    DormitoryDataChart,
    ClassDataPieChart,
    DormitoryDataPieChart
  },
  data() {
    return {
      dataAPI: this.$api.Screen.statDynamic,
      pageData: {},
      pollingInterval: 0
    }
  }
}
</script>

<style scoped lang="stylus">
.chart
  width 100%
  height 50%
</style>