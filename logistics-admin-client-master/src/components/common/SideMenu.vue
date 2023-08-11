<template>
  <aside :style="sideStyle" class="side-menu flex column">
    <slot :isCollapse="isCollapse" name="logo"></slot>

    <!--<div class="text-f">{{ defaultOpenedList }}</div>-->
    <!-- 侧边 菜单栏 -->
    <div class="aside-inner-wrapper flex-1 flex column">
      <el-menu :collapse="isCollapse" :default-active="defaultPath" unique-opened @select="onSelectMenu">
        <template v-for="(item,index) in sideMenuList">
          <el-submenu v-if="item.children&&item.children.length" :key="index" :index="item.routeId">
            <template v-slot:title>
              <EleIcon :src="item.iconUrl"></EleIcon>
              <span>{{ item.name }}</span>
            </template>
            <el-menu-item-group>
              <el-menu-item v-for="(mItem,mIndex) in item.children" :key="mIndex" :index="mItem.routeId">
                <EleIcon :src="mItem.iconUrl"></EleIcon>
                <span>{{ mItem.name }}</span>
                <div class="markNumber height-18 width-18 radius-8 font-14 line-height-18 text-center bg-main text-f margin-left-4" v-if="((mItem.id === 323 && Boolean(qualificationNum)) || (mItem.id === 324 && Boolean(costNum)))">{{ mItem.id === 323 ? qualificationNum : costNum }}</div>
                <div class="markNumber height-18 width-18 radius-8 font-14 line-height-18 text-center bg-main text-f margin-left-4" v-if="(mItem.id === 313 && Boolean(classroomApplyNum))">{{ classroomApplyNum }}</div>
              </el-menu-item>
            </el-menu-item-group>
          </el-submenu>

          <el-menu-item v-else :key="index" :index="item.routeId">
            <EleIcon :src="item.iconUrl"></EleIcon>
            <span>{{ item.name }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </div>

    <!-- 下方的隐藏图标 -->
    <div v-if="isMenuCollapseAble" class="aside-footer font-16 text-right pointer" @click="onToggleMenu">
      <div class="width-100p">
        <i :class="isCollapse?'iconfont icon--spread':'iconfont icon--spread-copy'"></i>
      </div>
    </div>
  </aside>
</template>

<script>
import {mapState} from 'vuex'

export default {
  name: 'SideMenu',
  data () {
    return {
      isCollapse: this.isMenuCollapse
    }
  },

  props: {
    sideMenuList: {
      type: Array,
      required: true
    },
    minWidth: {
      type: [String, Number],
      default: 60
    },
    width: {
      type: [String, Number],
      default: 180
    },
    isMenuCollapse: {
      type: Boolean,
      default: false
    },
    isMenuCollapseAble: {
      type: Boolean,
      default: false
    }
  },

  computed: {
    defaultPath () {
      for (let i = this.$route.matched.length; i--;) {
        let fItem = this.sideMenuListFlat.find(item => item.routeName === this.$route.matched[i].name)
        if (fItem) return fItem.routeId
      }

      return null
    },
    sideStyle () {
      let width = this.width
      if (this.isCollapse) width = this.minWidth
      if (typeof width === 'number') width += 'px'
      return {
        width,
        height: this.$utils.FullViewHeight()
      }
    },
    sideMenuListFlat () {
      return this.$utils.ArrayFlat(this.sideMenuList).filter(item => item.name !== 'NotFound')
    },
    ...mapState('system', {
      qualificationNum: 'qualificationNum',
      costNum: 'costNum',
      classroomApplyNum: 'classroomApplyNum',
    }),
  },

  methods: {
    onToggleMenu () {
      this.$emit('update:isMenuCollapse', this.isCollapse = !this.isCollapse)
    },
    onSelectMenu (index) {
      let fRoute = this.sideMenuListFlat.find(item => item.routeId === index)
      fRoute && fRoute.routeName && this.$router.push({ name: fRoute.routeName })
    }
  }
}
</script>

<style lang="stylus" scoped>
RowHeight = 48px
.el-image
  margin-right 12px
.side-menu
  flex-shrink 0
  overflow hidden
  background-color NEUTRAL_COLOR_1C
  transition 0.3s all ease-in-out
  position relative
  z-index 1000
  .aside-inner-wrapper
    overflow-x hidden
    overflow-y scroll
    width calc(100% + 17px)
    >>> .el-menu
      background-color NEUTRAL_COLOR_1C
      border-right none
      width 100%
      &.el-menu--collapse
        [class^=el-icon-], .iconfont
          margin-left 3px
      .el-submenu
        &.is-active
          // background-color BACKGROUND_COLOR
          .el-submenu__title
            color NEUTRAL_COLOR_3
            background-color BACKGROUND_COLOR
            i
              color NEUTRAL_COLOR_3

        &.is-opened
          .el-submenu__title
            background-color BACKGROUND_COLOR
          .el-menu-item
            background-color BACKGROUND_COLOR
        .el-menu:hover
          background-color #14171D
        .el-menu-item
          min-width auto
          height RowHeight
          line-height RowHeight
          padding-left 48px !important
          background-color #FFF5F5
      .el-menu-item, .el-submenu__title
        padding 0 18px !important
        color NEUTRAL_COLOR_3
        height RowHeight
        line-height RowHeight
        &:hover
          background-color HOVER_BACKGROUND_COLOR
          color MAIN_COLOR
          i
            color MAIN_COLOR
        &:focus, &.is-active
          background-color NEUTRAL_COLOR_0C !important
          color MAIN_COLOR
          font-weight bold
          i
            color MAIN_COLOR
        [class^=el-icon-], .iconfont
          font-size 18px
          width 18px
          margin-right 12px
      .el-menu-item-group__title
        padding 0
  .aside-footer
    height RowHeight
    padding 0 21px 0 18px
    background-color NEUTRAL_COLOR_1C
    line-height RowHeight
    color NEUTRAL_COLOR_6
    &:hover
      color MAIN_COLOR

.markNumber
  display: inline-block
  font-weight: normal
</style>