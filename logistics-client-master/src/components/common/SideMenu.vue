<template>
  <aside class="side-menu flex column" :style="sideStyle">
    <slot name="logo" :isCollapse="isCollapse"></slot>

    <!--<div class="text-f">{{ defaultOpenedList }}</div>-->
    <div class="aside-inner-wrapper flex-1 flex column">
      <el-menu :collapse="isCollapse" :default-active="defaultPath" unique-opened @select="onSelectMenu">
        <template v-for="(item,index) in sideMenuListInner">
          <!--          <span class="text-f">{{ item.fullPath }}</span>-->
          <el-submenu :key="index" :index="String(item.routeId)" v-if="item.children">
            <template v-slot:title>
              <EleIcon :src="item.meta.icon"></EleIcon>
              <span>{{ item.meta.title }}</span>
            </template>
            <el-menu-item-group>
              <el-menu-item v-for="(mItem,mIndex) in item.children" :key="mIndex" :index="String(mItem.routeId)">
                <!--                <span class="text-f">{{ mItem.fullPath }}</span>-->
                <EleIcon :src="mItem.meta.icon"></EleIcon>
                <span>{{ mItem.meta.title }}</span>
              </el-menu-item>
            </el-menu-item-group>
          </el-submenu>

          <el-menu-item :key="index" :index="String(item.routeId)" v-else>
            <EleIcon :src="item.meta.icon"></EleIcon>
            <span>{{ item.meta.title }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </div>
    <div class="aside-footer font-16 text-right pointer" @click="onToggleMenu" v-if="isMenuCollapseAble">
      <div class="width-100p">
        <i :class="isCollapse?'iconfont icon-spread':'iconfont icon-spread-copy'"></i>
      </div>
    </div>
  </aside>
</template>

<script>
export default {
  name: 'SideMenu',
  data() {
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
    // 应对之前一个menu拆分多个的场景, 目前去除
    /*defaultOpenedList() {
      let arr = []
      let parent = this.sideMenuListInner.find(item => {
        let matchRes = item.fullPath.match(new RegExp(`^(\\/\\w+){1,${2}}\\/?`))
        let str = matchRes ? matchRes[0] : item.fullPath
        return this.defaultPath.startsWith(str)
      })
      if (parent) {
        arr.push(parent.fullPath)
        // 下行代码应对 '/' 的情况，不可删
        if (this.sideMenuListInner.length === 1) arr.push(this.sideMenuListInner[0].fullPath)
      } else {
        if (this.sideMenuListInner[0]) arr.push(this.sideMenuListInner[0].fullPath)
      }
      return arr
    },*/
    defaultPath() {
      // let n = this.$route.matched.filter(item => item.path.endsWith('/')).length
      // n = n && 1
      /*let matchResult = this.$route.path.match(new RegExp(`^(\\/\\w+){1,${3}}\\/?`))
      if (!matchResult) return this.$route.path
      return matchResult[0]*/
      
      // let fItem = this.sideMenuListInnerFlat.find(item => item.routeName === this.$route.name)
      // if (!fItem) return null
      // 处理 平台未配置所有菜单 导致的样式问题
      // 处理方式，从子级依次向父级寻找，直至找到
      let fItem = null
      for(let i = this.$route.matched.length - 1 ; !Boolean(fItem) && i >= 0 ; --i) {
        fItem = this.sideMenuListInnerFlat.find(item => item.permission === this.$route.matched[i].meta.permission)
      }
      console.log(fItem)

      // while (!fItem.routeId) {
      //   fItem = this.sideMenuListInnerFlat.find(item => item.id === fItem.parentId)
      // }
      return fItem.routeId
    },
    sideStyle() {
      let width = this.width
      if (this.isCollapse) width = this.minWidth
      if (typeof width === 'number') width += 'px'
      return {
        width,
        height: this.$utils.FullViewHeight()
      }
    },
    sideMenuListInnerFlat() {
      return this.$utils.ArrayFlat(this.sideMenuListInner).filter(item => item.name !== 'NotFound')
    },
    sideMenuListInner() {
      return this.sideMenuList.map(item => {
        return {
          ...item,
          routeId: `app-${item.appId}`,
          fullPath: this.$router.match(item).fullPath,
          children: item.children && item.children.map(cItem => {
            // if(cItem.name === 'NotFound') console.log(cItem)
            return {
              ...cItem,
              routeId: `route-${cItem.id}`,
              fullPath: this.$router.match(cItem).fullPath
            }
          })
        }
      })
    }
  },

  methods: {
    onToggleMenu() {
      this.$emit('update:isMenuCollapse', this.isCollapse = !this.isCollapse)
    },
    onSelectMenu(index, indexPath) {
      let fParent = this.sideMenuListInner.find(item => item.routeId === indexPath[0])
      let fItem = fParent.children.find(item => item.routeId === indexPath[1])
      this.$router.push({name: fItem.name})
    }
  }
}
</script>

<style scoped lang="stylus">
RowHeight = 48px

.el-image
  margin-right 12px

.side-menu
  flex-shrink 0
  overflow hidden
  background-color NEUTRAL_COLOR_1C
  transition .3s all ease-in-out
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
          //background-color MAIN_COLOR

          .el-submenu__title
            color NEUTRAL_COLOR_3

            i
              color NEUTRAL_COLOR_3
        &.is-opened
          .el-submenu__title
            background-color BACKGROUND_COLOR
          .el-menu-item
            background-color BACKGROUND_COLOR  
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
          color HOVER_TEXT_COLOR

          i
            color HOVER_TEXT_COLOR


        // 为了处理返回后 两个菜单栏带样式，除去了 &:focus
        // &:focus, &.is-active
        &.is-active
          background-color HOVER_BACKGROUND_COLOR !important
          color MAIN_COLOR
          font-weight bold
          i
            color MAIN_COLOR

        [class^=el-icon-], .iconfont
          font-size 18px
          width 18px
          margin-right: 12px

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
</style>