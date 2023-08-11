<template>
  <section class="flex column home-page bg-global-background">
<!--    <header
      class="relative bg-f flex between-center font-14 text-6 padding-right-16"
      :style="{ height: constant.headerHeight }">
      <div class="flex start-center">
        <div class="width-180 flex center-center overflow">
          <el-image
            class="logo-image"
            fit="cover"
            :src="!orgId ? logo.Full : userInfo.pcLogoUrl"
            :alt="userInfo.orgName || logo.alt">
            <template
              slot="error"
              class="image-slot"
              v-if="userInfo.orgId">
              <div class="width-100p height-100p bg-e flex center-center text-unselect">LOGO</div>
            </template>
          </el-image>
        </div>
        <el-tabs
          :class="`tabs-${sideMenuList.length}`"
          v-model="activePageName"
          v-if="showAside && sideMenuList.length > 1">
          &lt;!&ndash;  <el-tab-pane v-for="item in menuTypeList" :key="item.id" :label="item.name" :name="item.id"></el-tab-pane>&ndash;&gt;
          <el-tab-pane
            v-for="item in sideMenuList"
            :key="item.id"
            :label="item.meta.title"></el-tab-pane>
        </el-tabs>
      </div>

      <div class="flex">
        <div
          class="line-height-40"
          v-if="isDev">
          orgId:{{ orgId }}
        </div>

        &lt;!&ndash; <el-popover
          class="margin-right-24 flex start-center"
          placement="bottom"
          trigger="hover"
          v-if="userInfo.orgId">
          <div class="padding-8 text-center">
            {{this.innerDomain}}{{this.domain.h5Path}}
            <QrcodeVue
              class="flex padding-12"
              :value="`${innerDomain}${domain.h5Path}/#/`"
              :size="160"
              v-if="innerDomain"></QrcodeVue>
            <div class="margin-bottom-12">扫码查看手机端</div>
            <el-button
              type="primary"
              size="small"
              @click="onLink"
              >点击查看网页端</el-button
            >
          </div>
          <el-button
            class="link-btn"
            slot="reference"
            type="primary"
            size="small"
            @click="onLink">
            <span class="d-text">访问机构网站</span>
            <span class="h-text">点击查看网页</span>
          </el-button>
        </el-popover> &ndash;&gt;
        <div
          v-if="userInfo.orgId"
          class="line-height-56">
          {{ userInfo.roles && userInfo.roles.join('、') }}
        </div>
        <DropMenu></DropMenu>
      </div>
    </header>-->
    <section class="flex-1 flex overflow">
      <SideMenu
        :sideMenuList="activePage"
        isMenuCollapseAble
        v-if="showAside"></SideMenu>
      <main
        class="overflow-auto relative flex-1"
        :style="{ height: $utils.FullViewHeight() }">
        <RouterAlive class="router-alive"></RouterAlive>
      </main>
    </section>
  </section>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex'
import SideMenu from '@/components/common/SideMenu'
import DropMenu from '@/components/common/DropMenu'
import QrcodeVue from 'qrcode.vue'

export default {
  name: 'Home',
  components: {
    SideMenu,
    DropMenu,
    QrcodeVue,
  },
  created() {
    let index = this.sideMenuList.findIndex((item) => this.$route.matched.find((mItem) => mItem.name === item.name))
    if (index < 0) index = 0
    this.activePageName = index.toString()
  },
  data() {
    return {
      activePageName: '0',
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo',
      orgId: 'orgId',
      innerDomain: 'innerDomain',
    }),
    ...mapState('config', {
      domain: 'domain',
    }),
    ...mapState('router', {
      initRoutePath: 'initRoutePath',
    }),
    ...mapGetters({
      sideMenuList: 'router/sideMenuList',
      isDev: 'isDev',
      isTesting: 'isTesting',
    }),
    ...mapState('config', {
      logo: 'logo',
      name: 'name',
      constant: 'constant',
    }),
    activePage() {
      // return this.sideMenuList.filter(item => item.category === this.activePageName)
      let a = this.sideMenuList[this.activePageName]
      if (a && a.children) return a.children
      return []
    },
    showAside() {
      return !this.$route.meta.isIndex && !this.initRoutePath
    },
  },
  methods: {
    ...mapActions({
      logout: 'system/logout',
    }),
    onLink() {
      if (!this.innerDomain) return this.$message.warning('获取不到二级域名')
      window.open(`${this.innerDomain}/#/`, '_blank')
    },
  },
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
