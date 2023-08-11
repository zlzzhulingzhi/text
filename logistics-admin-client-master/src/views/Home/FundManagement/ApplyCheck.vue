<template>
  <!-- 标签切换 -->
  <div class="relative">
    <el-tabs v-model="activePageName" :before-leave="beforeLeave" :class="activityActive ? 'elTabsByHand' : ''">
      <el-tab-pane v-for="item in pages" :key="item.id" :name="item.id" :label="item.name">
        <component :is="item.id"></component>
      </el-tab-pane>
    </el-tabs>
    
    <div class="activity markNumber height-18 width-18 radius-8 font-14 line-height-18 text-center bg-main text-f margin-left-4" v-if="courseNum">{{ courseNum }}</div>
    <div class="course markNumber height-18 width-18 radius-8 font-14 line-height-18 text-center bg-main text-f margin-left-4" v-if="activityNum">{{ activityNum }}</div>
  </div>
</template>
  
  <script>
import ActivityApply from '@/views/Home/FundManagement/VerificationRequest/ActivityApply'
import CourseApply from '@/views/Home/FundManagement/VerificationRequest/CourseApply'
import {mapState} from 'vuex'

export default {
  name: 'ApplyCheck',
  components: {
    ActivityApply,
    CourseApply
  },
  data() {
    return {
      activePageName: 'CourseApply',

      // 是否活动是激活的
      activityActive: false
    }
  },
  computed: {
    pages() {
      let pages = [
        { id: 'CourseApply', name: '主题申请' },
        { id: 'ActivityApply', name: '活动申请' }
      ]
      return pages
    },
    ...mapState('system', {
      courseNum: 'courseNum',
      activityNum: 'activityNum',
    }),
  },
  methods: {
    beforeLeave(activeName, oldActiveName) {
      if(activeName === 'ActivityApply') {
        this.activityActive = true
      } else { 
        this.activityActive = false
      }
    }
  }
}
</script>
  
  <style lang="stylus" scoped>
  >>>.el-tabs
    .el-tabs__item
      margin-right 30px !important

  // 修改底部栏偏移量
  >>>.elTabsByHand
    .el-tabs__active-bar
      transform: translateX(122px) !important

  .markNumber
    display: inline-block
    font-weight: normal

  .activity
    position absolute
    top 10px
    left 91px

  .course
    position absolute
    top 10px
    left 202px
  </style>
  