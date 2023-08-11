<template>
  <div>
    
      <el-tabs v-model="activePageName" :class="pageInfo.pageClassName" v-loading="loading.page">
        <el-tab-pane v-for="item in pages" :key="item.id" :name="item.id" :label="item.name">
          <component :type="type" :is="item.id" :info="info"></component>
        </el-tab-pane>
      </el-tabs>
  
      <!-- <el-button type="primary" size="small" v-if="type === 'View'" class="absolute goBackButton" @click="() => $router.go(-1)">返回</el-button> -->
  
  </div>
  </template>
  
  <script>
  import BaseInfo from './BaseInfo'
  import AttachInfo from './AttachInfo'
  
  export default {
    name: 'FacilityApplyCreate',
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
          View: {
            pageClassName: 'view',
            detailAPI: this.$api.Approve.detail,
            submitAPI: null
          }
        }
  
        return typeMapping[this.type]
      },
    },
    methods: {
      // 操作 - 初始化页面
      onInitPage({page, id}) {
        if (page) this.activePageName = page
        this.id = id || null
        this.getDetail()
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