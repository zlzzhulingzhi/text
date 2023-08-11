<template>
  <!-- <div>123</div> -->
  <el-tabs v-model="activePageName" class="view" v-loading="loading.page">
    <el-tab-pane v-for="item in pages" :key="item.id" :name="item.id" :label="item.name">
      <component type="View" :is="item.id" :info.async="info" :applyType="applyType" @confirm="onConfirm"></component>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import BaseInfo from './BaseInfo'
import AttachInfo from './AttachInfo'
import ExpertReview from '../ExpertReview'

export default {
  name: 'ApplyActivityDetail',
  components: {
    BaseInfo,
    AttachInfo,
    ExpertReview
  },
  created () {
    this.onInitPage({
      page: this.$route.params.page || this.$route.query.page,
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
      applyType: 'activity'
    }
  },
  computed: {
    activePage () {
      let cIndex = this.pages.findIndex(({ id }) => id === this.activePageName)
      return {
        ...this.pages[cIndex],
        prev: this.pages[cIndex - 1],
        next: this.pages[cIndex + 1]
      }
    }
  },
  methods: {
    // 操作 - 初始化页面
    onInitPage ({ page, id }) {
      if (page) this.activePageName = page
      this.id = id || null
      this.getDetail()
    },

    // 操作 - 步骤确认
    onConfirm ({ type, id }) {
      if (type === 'next') {
        if (this.activePage.next) {
          this.onInitPage({
            id,
            page: this.activePage.next.id
          })
        }
      } else if (type === 'prev') {
        if (this.activePage.prev) {
          this.onInitPage({
            id,
            page: this.activePage.prev.id
          })
        }
      } else if (type === 'finish') {
        this.$router.push({
          name: 'ApplyQualificationsList'
        })
      }
    },

    async getDetail () {
      if (!this.id) return false
      this.loading.page = true
      let { code, data } = await this.$api.Approve.activityDetail({
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