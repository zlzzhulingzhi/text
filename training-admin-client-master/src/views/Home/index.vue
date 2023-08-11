<template>
  <section class="flex column home-page bg-global-background">
    <!-- 主体内容 -->
    <section class="flex-1 flex overflow">
      <!-- 侧边导航 -->
      <SideMenu v-if="showAside" :sideMenuList="sideMenuList" isMenuCollapseAble>
        <div slot="logo" v-if="isDev" class="break-all text-center text-3 width-180 padding-8">
          {{ $route.fullPath }}
        </div>
      </SideMenu>
      <!-- 中心内容 -->
      <main :style="{height: $utils.FullViewHeight()}" class="overflow-auto relative flex-1">
        <RouterAlive class="router-alive"></RouterAlive>
      </main>
    </section>
  </section>
</template>

<script>
import {mapActions, mapGetters, mapState} from 'vuex'
import SideMenu from '@/components/common/SideMenu'
import DropMenu from '@/components/common/DropMenu'
import QrcodeVue from 'qrcode.vue'

export default {
  name: 'Home',
  components: {
    SideMenu,
    DropMenu,
    QrcodeVue
  },
  created() {
    let index = this.sideMenuList.findIndex(item => this.$route.matched.find(mItem => mItem.name === item.name))
    if (index < 0) index = 0
    this.activePageName = index.toString()
  },
  data() {
    return {
      activePageName: '0'
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo',
      orgId: 'orgId',
    }),
    ...mapState('config', {
      domain: 'domain'
    }),
    ...mapGetters({
      sideMenuList: 'router/sideMenuList',
      isDev: 'isDev'
    }),
    ...mapState('config', {
      logo: 'logo',
      name: 'name',
      constant: 'constant'
    }),
    ...mapState('router', {
      initRoutePath: 'initRoutePath',
    }),
    activePage() {
      // return this.sideMenuList.filter(item => item.category === this.activePageName)
      let a = this.sideMenuList[this.activePageName]
      if (a && a.children) return a.children
      return []
    },
    showAside() {
      return !this.$route.meta.isIndex && !this.initRoutePath
    }
  },
  methods: {
    ...mapActions({
      logout: 'system/logout',
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

    .logo-image
      width 140px
      height 35px

    .link-btn
      background: HOVER_BACKGROUND_COLOR
      color: HOVER_TEXT_COLOR
      border: none

    .d-text
      display inline

    .h-text
      display none

    &:hover
      .d-text
        display none

      .h-text
        display inline

    >>> .el-tabs
      &.tabs-1
        .el-tabs__active-bar
          transform translateX(0px) !important

      .el-tabs__header
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

@media screen and (max-width: 768px)
  .sm-hide
    display none
</style>