<template>
<div>
  
    <el-tabs v-model="activePageName" :class="pageInfo.pageClassName" v-loading="loading.page">
      <el-tab-pane v-for="item in pages" :key="item.id" :name="item.id" :label="item.name">
        <component :type="type" :is="item.id"
                   :info.async="info"
                   @confirm="onConfirm"></component>
      </el-tab-pane>
    </el-tabs>
   
    <!-- <el-button type="primary" size="small" v-if="type === 'View'" class="absolute goBackButton" @click="goToActivity">返回</el-button> -->

</div> 
</template>

<script>
import BaseInfo from '@/views/Home/Allowance/FundingCosts/ForActivity/Create/BaseInfo'
import AttachInfo from '@/views/Home/Allowance/FundingCosts/ForActivity/Create/AttachInfo'

export default {
  name: 'ApplyCostActivityCreate',
  components: {
    BaseInfo,
    AttachInfo
  },
  created() {
    this.onInitPage({
      page: this.$route.params.page || this.$route.query.page,
      id: this.$route.query.id
    })
  },
  data() {
    let pages = [
      {id: 'BaseInfo', name: '基本信息'},
      {id: 'AttachInfo', name: '资料附件'}
    ]
    return {
      activePageName: pages[0].id,
      pages,
      id: null,
      info: {},
      loading: {
        page: false
      }
    }
  },
  props: {
    type: {
      type: String,
      default: 'Create'
    }
  },
  computed: {
    pageInfo() {
      let typeMapping = {
        Create: {
          pageClassName: 'create',
          detailAPI: this.$api.ApplyCost.detailActivity,
          submitAPI: this.$api.ApplyCost.saveActivity
        },
        Edit: {
          pageClassName: 'edit',
          detailAPI: this.$api.ApplyCost.detailActivity,
          submitAPI: this.$api.ApplyCost.updateActivity
        },
        View: {
          pageClassName: 'view',
          detailAPI: this.$api.ApplyCost.detailActivity,
          submitAPI: null
        }
      }

      return typeMapping[this.type]
    },
    activePage() {
      let cIndex = this.pages.findIndex(({id}) => id === this.activePageName)
      return {
        ...this.pages[cIndex],
        prev: this.pages[cIndex - 1],
        next: this.pages[cIndex + 1]
      }
    }
  },
  methods: {
    // 操作 - 初始化页面
    onInitPage({page, id}) {
      if (page) this.activePageName = page
      this.id = id || null
      this.getDetail()
    },

    // 操作 - 步骤确认
    onConfirm({type, id}) {
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
          name: 'ApplyCostList',
          params: {
            elTapPane: 'ActivityApply'
          }
        })
      }
    },

    async getDetail() {
      if (!this.pageInfo.detailAPI || !this.id) return false
      this.loading.page = true
      let {code, data} = await this.pageInfo.detailAPI({
        id: this.id
      })
      this.loading.page = false
      if (code !== 200) return false
      this.info = data
    },

    // 跳转到  列表页  活动标签
    goToActivity() {
      this.$router.push({
        name: 'ApplyCostList',
        params: {
          elTapPane: 'ActivityApply'
        }
      })
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

.goBackButton
  top 22px
  right 24px
</style>