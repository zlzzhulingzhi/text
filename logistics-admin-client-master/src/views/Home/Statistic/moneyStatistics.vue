<template>
  <!-- 标签切换 -->
  <el-tabs v-model="activePageName">
    <el-tab-pane v-for="item in pages" :key="item.id" :name="item.id" :label="item.name">
      <component :is="item.id"></component>
    </el-tab-pane>
  </el-tabs>
</template>
  
<script>
import ActivityMoneyStatistics from '@/views/Home/Statistic/CourseActivityCheck/ActivityMoneyStatistics'
import CourseMoneyStatistics from '@/views/Home/Statistic/CourseActivityCheck/CourseMoneyStatistics'
export default {
  name: 'MoneyStatistics',
  components: {
    ActivityMoneyStatistics,
    CourseMoneyStatistics
  },
  data() {
    
    return {
      activePageName: 'CourseMoneyStatistics'
    }
  },
  computed: {
    pages() {
      let pages = [
        { id: 'CourseMoneyStatistics', name: '课程资助统计' },
        { id: 'ActivityMoneyStatistics', name: '活动资助统计' }
      ]
      return pages
    }
  }
}
</script>
  
<style lang="stylus" scoped>
.el-tabs .el-tabs__header
  margin-bottom 16px
</style>
  