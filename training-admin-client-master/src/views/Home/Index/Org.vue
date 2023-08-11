<template>
  <div class="main">
    <el-row :gutter="24" v-loading="loading">
      <el-col class="margin-bottom-24" :xs="12" :sm="12" :md="8" :lg="6" :xl="6" v-if="userInfo.isAdmin">
        <el-card class="pointer" shadow="hover" :body-style="{ padding: '20px' }" @click.native="selectOrg()">
          <div class="flex between-center font-14">
            <span class="flex-1 text-bold font-16">平台管理后台</span>
            <div class="tag font-10">平台</div>
          </div>
          <div class="margin-top-16 margin-bottom-42 text-6 font-14">平台管理</div>
          <span class="btn font-13 check">进入</span>
        </el-card>
      </el-col>
      <!-- <el-col class="margin-bottom-24" :xs="12" :sm="12" :md="8" :lg="6" :xl="6" v-for="item in orgList" :key="item.orgId">
        <el-card class="pointer" shadow="hover" :body-style="{ padding: '20px' }" @click.native="selectOrg(item)">
          <div class="flex between-center font-14">
            <span class="flex-1 shrink-0 text-bold font-16 impleName" :title="item.orgName" >{{ item.orgName }}</span>
            <div class="tag font-10">{{ (orgCategory.find(v => v.id === item.category) || {}).name }}</div>
          </div>
          <div class="margin-top-16 margin-bottom-42 text-6 font-14">{{ item.roleNames || '--' }}</div>
          <span class="btn font-13" :class="item.enabled ? 'check' : ''">{{ item.enabled ? '试用版' : '已过期' }}</span>
        </el-card>
      </el-col> -->
    </el-row>
    <Results alt="尚未注册机构账号" v-if="!userInfo.isAdmin && showEmpty"></Results>
  </div>
</template>

<script>
import {mapActions, mapGetters, mapMutations, mapState} from 'vuex'

export default {
  name: 'Index',
  data() {
    return {
      loading: false,
      showEmpty: false,
      orgList: []
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    ...mapGetters({
      orgCategory: 'common/orgCategory'
    })
  },
  created() {
    // this.getOrgList()
  },
  methods: {
    ...mapMutations({
      setOrgId: 'system/setOrgId'
    }),
    ...mapActions({
      appInit: 'appInit'
    }),
    // async getOrgList() {
    //   this.loading = true
    //   let {code, data} = await this.$api.OrgIndexPage.orgList()
    //   this.loading = false
    //   if (code !== 200) return false
    //   this.orgList = data || []
    //   this.setOrgId(null)
    //   if (!this.orgList.length) this.showEmpty = true
    // },
    async selectOrg(item = null) {
      if (item && !item.enabled) return this.$alert('该机构已过期，请联系网安加平台客服', '提示')
      const loading = this.$loading({lock: true})
      this.setOrgId(item && item.orgId)
      let name = await this.appInit()
      await this.$router.push({name: name || 'Main'})
      loading.close()
    }
  }
}
</script>

<style lang="stylus" scoped>
.main
  padding 64px

  .el-card
    border none

    &.is-hover-shadow:focus,
    &.is-hover-shadow:hover
      border-radius 8px
      box-shadow 0px 2px 14px 1px rgba(42, 130, 228, 0.2)

    .tag
      padding 2px 6px
      background #EBEBEB

    .btn
      padding 8px 12px
      border-radius 4px
      background NEUTRAL_COLOR_E
      color NEUTRAL_COLOR_6

      &.check
        background #EBF2FF
        color #1D61F2

    .impleName 
        max-width: 100%;
        display: inline-block;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
</style>