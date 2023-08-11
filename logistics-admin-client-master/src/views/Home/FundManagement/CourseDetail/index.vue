<template>
  <!-- <div>123</div> -->
  <el-tabs v-model="activePageName" class="view" v-loading="loading.page">
    <el-tab-pane v-for="item in pages" :key="item.id" :name="item.id" :label="item.name">
      <component type="View" :is="item.id" :info.async="info" :applyType="applyType"></component>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import BaseInfo from './BaseInfo'
import AttachInfo from './AttachInfo'
import ExpertReview from '../ExpertReview'

export default {
  name: 'ApplyCourseDetail',
  components: {
    BaseInfo,
    AttachInfo,
    ExpertReview
  },
  created () {
    this.onInitPage({
      id: this.$route.query.id
    })
  },
  data () {
    let pages = [
      { id: 'BaseInfo', name: '基本信息' },
      { id: 'AttachInfo', name: '资料附件' },
      { id: 'ExpertReview', name: '专家评审' }
    ]
    return {
      activePageName: pages[0].id,
      pages,
      id: null,
      info: {},
      loading: {
        page: false
      },
      applyType: 'allowance'
    }
  },
  computed: {
    // activePage () {
    //   let cIndex = this.pages.findIndex(({ id }) => id === this.activePageName)
    //   return {
    //     ...this.pages[cIndex],
    //     prev: this.pages[cIndex - 1],
    //     next: this.pages[cIndex + 1]
    //   }
    // }
  },
  methods: {
    // 操作 - 初始化页面
    onInitPage ({ id }) {
      this.id = id || null
      this.getDetail()
    },

    async getDetail () {
      if (!this.id) return false
      this.loading.page = true
      let { code, data } = await this.$api.Approve.allowanceDetail({
        id: this.id
      })
      this.loading.page = false
      if (code !== 200) return false
      this.info = data
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-tabs__content
  overflow visible
  position static
.create
  >>> .el-tabs__header
    display none
</style>