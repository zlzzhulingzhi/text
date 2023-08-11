<template>
<div>
    <el-breadcrumb class="margin-bottom-12">
      <el-breadcrumb-item v-for="item in $route.matched.slice(1)" :key="item.id">
        {{ item.meta.title }}
      </el-breadcrumb-item>
    </el-breadcrumb>
  
    <el-tabs v-model="activePageName">
      <template v-if="type === 'Create'">
        <ActivityInfo v-if="activePageName == 'ActivityInfo'" :ActivityInfo="ActivityInfo" @page-change="pageChange"></ActivityInfo>
        <!-- <ActivityChapter v-else-if="activePageName == 'ActivityChapter'" :ActivityInfo="ActivityInfo"
                       @page-change="pageChange"></ActivityChapter>
        <ActivityAttach v-else-if="activePageName == 'ActivityAttach'" :ActivityInfo="ActivityInfo"
                      @page-change="pageChange"></ActivityAttach> -->
      </template>
      
      <template v-else>
        <el-tab-pane v-for="item in pages" :key="item.id" :name="item.id" :label="item.name">
          <component :is="item.id" :ActivityInfo="ActivityInfo" :type="type" @page-change="pageChange"></component>
        </el-tab-pane>
      </template>
    </el-tabs>
    
</div>
</template>

<script>
import ActivityInfo from './ActivityInfo.vue'
// import ActivityChapter from './ActivityChapter.vue'
// import ActivityAttach from './ActivityAttach.vue'

export default {
  name: 'CreateActivity',
  components: {
    ActivityInfo,
    // ActivityChapter,
    // ActivityAttach
  },
  created() {
    this.ActivityId = this.$route.query.id || ''
    this.type = this.$route.params.type || 'Create'
    // if (this.$route.params.page || this.$route.query.page) {
    //   this.activePageName = this.$route.params.page || this.$route.query.page
    // }
    this.getDetail()
  },
  data() {
    let pages = [
      {id: 'ActivityInfo', name: '基本信息'},
      // {id: 'ActivityChapter', name: '课程内容'},
      // {id: 'ActivityAttach', name: '课程资料'}
    ]
    return {
      activePageName: pages[0].id,
      pages,
      ActivityId: '',
      ActivityInfo: {},
      type: 'Create'
    }
  },
  // props: {
  //   type: {
  //     type: String,
  //     default: 'Create'
  //   }
  // },
  methods: {
    pageChange(data) {
      // this.activePageName = data.page
      // this.ActivityId = data.id || ''
      // this.getDetail()
      this.$router.push({ name: 'Activity'})
    },
    async getDetail() {
      if (!this.ActivityId) return false
      let {code, data} = await this.$api.Activity.detail({
        id: this.ActivityId
      })
      if (code !== 200) return false
      this.ActivityInfo = data
    }
  }
}
</script>

<style lang="stylus" scoped>
>>> .el-tabs__content
  overflow visible
  position static



.el-tabs
  >>> .el-tabs__header

    .el-tabs__nav

      .el-tabs__active-bar
        width 56px !important


</style>