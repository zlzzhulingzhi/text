<template>
  <!-- 活动、课程  标签切换 -->
  <el-tabs v-model="activePageName">
    <el-tab-pane v-for="item in pages" :key="item.id" :name="item.id" :label="item.name">
      <component :is="item.id"></component>
    </el-tab-pane>
  </el-tabs>
</template>
  
  <script>
import ActivityApply from '@/views/Home/Allowance/FundingCosts/ForActivity'
import CourseApply from '@/views/Home/Allowance/FundingCosts/ForCourse'
export default {
  name: 'ApplyCostList',
  components: {
    ActivityApply,
    CourseApply
  },
  created() {
    // 判断有无进行资质申请
    this.isGotQualification()
  },
  mounted(){
    if (this.$route.params.elTapPane) return this.activePageName = this.$route.params.elTapPane
  },
  data() {
    return {
      activePageName: 'CourseApply'
    }
  },
  computed: {
    pages() {
      let pages = [
        { id: 'CourseApply', name: '主题申请' },
        { id: 'ActivityApply', name: '活动申请' }
      ]
      return pages
    }
  },
  methods: {
    async isGotQualification() {
      let {code, data} = await this.$api.ApplyCost.isGotQualification()
      if(code !== 200) return false

      // 当机构没有先进行资质申请时，弹窗提醒
      if(data === false) {
        await this.$confirm('本机构的万人计划培训机构资质还未申请通过，不能进行资助费用申请！', '提示', {
          showCancelButton: false,
          showClose: false,
          closeOnClickModal: false,
          type: 'info'
        })

        this.$router.push({name: 'ApplyQualificationsList'})
      }
    }
  }
}
</script>
  
  <style lang="stylus" scoped>
  .el-tabs .el-tabs__header
    margin-bottom 16px
  </style>
  