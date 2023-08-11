<template>
  <ol class="flex start-center list">
    <li>
      <el-dropdown>
        <span class="flex center-center el-dropdown-link">
          <template v-if="userInfo">
            <el-avatar class="margin-right-4" :size="24" :src="userInfo.headImgUrl"
                       v-if="userInfo.headImgUrl"></el-avatar>
            <el-avatar class="margin-right-4" :size="24" v-else>{{ (userInfo.realName || '').substr(0, 1) }}</el-avatar>
          </template>
          <span v-if="userInfo">{{ userInfo.userAccount }}</span>
          <span v-else>用户</span>
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <!-- <el-dropdown-item @click.native="clearOrgId" v-if="$route.name !== 'Index'">切换机构
          </el-dropdown-item> -->
          <el-dropdown-item @click.native="$router.push({ name: 'Account' })">账号设置</el-dropdown-item>
          <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </li>
  </ol>
</template>

<script>
import {mapActions, mapMutations, mapState} from 'vuex'

export default {
  name: 'DropMenu',
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    })
  },
  methods: {
    ...mapMutations({
      setOrgId: 'system/setOrgId'
    }),
    ...mapActions({
      logout: 'system/logout'
    }),
    // clearOrgId() {
    //   this.setOrgId(null)
    //   this.$router.push({ name: 'Index' })
    // }
  }
}
</script>

<style lang="stylus" scoped>
.list
  li
    padding 0 8px
    cursor pointer
    line-height 48px

    &:hover
      background-color BACKGROUND_COLOR
</style>