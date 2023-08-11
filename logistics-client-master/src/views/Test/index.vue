<template>
  <section class="flex column home-page bg-global-background">
    <header class="relative bg-f flex between-center font-14 text-6 padding-right-16"
            :style="{height:constant.headerHeight}">

      <div class="flex start-center">
        <div class="width-180 flex center-center">
          <el-image :src="logo.Full" :alt="logo.alt"></el-image>
        </div>
        <el-tabs v-model="activePageName">
          <!--          <el-tab-pane v-for="item in menuTypeList" :key="item.id" :label="item.name" :name="item.id"></el-tab-pane>-->
          <el-tab-pane v-for="item in sideMenuList" :key="item.id" :label="item.meta.title"></el-tab-pane>
        </el-tabs>
      </div>

      <ol class="flex start-center list">
        <li v-if="userInfo">
          <span>欢迎您！</span>
          <span class="text-success">{{ userInfo.realName }}</span>
        </li>
        <li @click="logout">
          退出
          <i class="el-icon-back"></i>
        </li>
      </ol>
    </header>

    <section class="flex-1 flex overflow">
      <SideMenu :sideMenuList="activePage"></SideMenu>
      <main class="overflow-auto relative flex-1" :style="{height: $utils.FullViewHeight()}">
        <RouterAlive class="router-alive"></RouterAlive>
      </main>
    </section>
  </section>
</template>

<script>
import {mapActions, mapGetters, mapState} from 'vuex'
import SideMenu from '@/components/common/SideMenu'

export default {
  name: 'Home',
  components: {
    SideMenu
  },
  created() {
    this.activePageName = this.sideMenuList.findIndex(item => this.$route.matched.find(mItem => mItem.name === item.name)).toString()
  },
  data() {
    return {
      // activePageName: 'home',
      // menuTypeList: [
      //   {id: 'home', name: '首页'},
      //   {id: 'edu', name: '教务中心'},
      //   {id: 'admin', name: '平台管理'}
      // ],
      activePageName: '0'
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    ...mapGetters({
      sideMenuList: 'router/sideMenuList'
    }),
    ...mapState('config', {
      logo: 'logo',
      name: 'name',
      constant: 'constant'
    }),
    activePage() {
      let TestRoutes = this.$router.options.routes.find(item => item.path === '/Test')
      return [{
        ...TestRoutes,
        appId: TestRoutes.name,
        children: TestRoutes.children.map(item => {
          return {
            ...item,
            id: item.name
          }
        })
      }]
    }
  },
  methods: {
    ...mapActions({
      logout: 'system/logout'
    })
  }
}
</script>

<style scoped lang="stylus">
.home-page
  header
    min-width 600px
    z-index 199
    box-shadow 0 3px 6px BACKGROUND_COLOR
    box-sizing border-box

    >>> .el-tabs .el-tabs__header
      margin-bottom 0
      padding-left 0

      .el-tabs__item
        height 48px
        line-height 48px

      .el-tabs__active-bar
        padding 0
        left 0
        background-color transparent

        &:after
          content ""
          display block
          height 100%
          width 50%
          background-color MAIN_COLOR
          margin-left 25%


    .list
      li
        padding 0 8px
        cursor pointer
        line-height 48px

        &:hover
          background-color BACKGROUND_COLOR

  section
    // 解决el-table无法继承flex容器的宽度
    min-width 0

    main
      box-sizing border-box
      min-width 600px
      padding 16px

      .router-alive
        min-width 900px
</style>