<template>
  <div class="main">
    <el-row :gutter="24">
      <!-- <el-col class="margin-bottom-24" :xs="12" :sm="12" :md="8" :lg="6" :xl="6" v-if="userInfo.isAdmin">
        <el-card class="pointer" shadow="hover" :body-style="{ padding: '20px' }" @click.native="selectOrg()">
          <div class="flex between-center font-14">
            <span class="flex-1 text-bold font-16">平台管理后台</span>
            <div class="tag font-10">平台</div>
          </div>
          <div class="margin-top-16 margin-bottom-42 text-6 font-14">平台管理</div>
          <span class="btn font-13 check">进入</span>
        </el-card>
      </el-col> -->
      <el-col class="margin-bottom-24" :xs="12" :sm="12" :md="8" :lg="6" :xl="6" v-for="item in myOrgList"
        :key="item.orgId">
        <el-card class="pointer" shadow="hover" :body-style="{ padding: '20px' }" @click.native="selectOrg(item)">
          <div class="flex between-center font-14">
            <span class="flex-1 shrink-0 text-bold font-16 impleName" :title="item.orgName">{{ item.orgName }}</span>
            <!-- <div class="tag font-10">{{ item.category || orgCategory }}</div> -->
          </div>
          <div class="margin-top-16 margin-bottom-42 text-6 font-14">{{ item.roleNames || '--' }}</div>
          <!-- <span class="btn font-13" :class="item.enabled ? 'check' : ''">{{ item.enabled ? '试用版' : '已过期' }}</span> -->
        </el-card>
      </el-col>
    </el-row>
    <Results alt="尚未注册机构账号" v-if="!myOrgList.length"></Results>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations, mapState } from 'vuex'

export default {
  name: 'Index',
  // data() {
  //   return {
  //     loading: false,
  //     showEmpty: false,
  //     orgList: []
  //   }
  // },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    ...mapGetters({
      // 异步操作
      orgCategory: 'common/orgCategory',
      myOrgList: 'common/myOrgList'
    })
  },
  created() {
    // this.getOrgList()
  },
  watch: {
    myOrgList(orgList) {
      if (orgList.length === 1) {
        this.selectOrg(orgList[0])
      } else if (orgList.length === 0) {
        this.$router.push({ name: 'Disabled' })
      }
    }
  },
  methods: {
    ...mapMutations({
      setOrgId: 'system/setOrgId'
    }),
    ...mapActions({
      appInit: 'appInit',
      sendMessageToParent: 'system/sendMessageToParent'
    }),
    async selectOrg(item = null) {
      if (item && !item.enabled) return this.$alert('该机构已过期，请联系武汉网安基地平台客服', '提示')
      const loading = this.$loading({ lock: true })
      this.setOrgId(item && item.orgId)
      this.sendMessageToParent({
        ...item
      })
      let name = await this.appInit()
      if (/^\//.test(name)) {
        await this.$router.push(name)
      } else {
        await this.$router.push({ name: name || 'Main' })
      }
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