<template>
  <div v-loading.fullscreen.lock="loading" v-if="loading"></div>
  <div class="full-screen overflow relative page-container" v-else>
    <div class="absolute top"></div>

    <div class="page-content" :style="pageComputed.style">
      <SvgBG :options="options" :screen="pageComputed.screen"></SvgBG>

      <div class="flex relative contents" :style="{
  paddingTop: `${options.mg + options.title_h + options.deco_h / 2 + 16}px`,
  paddingLeft: `${options.mg + options.line_r + 12}px`,
  paddingRight: `${options.mg + options.line_r + 12}px`,
  paddingBottom: `${options.mg + 16}px`,
}">

        <div class="flex column content-left">
          <DataOverview></DataOverview>
          <CombinedUseBase class="margin-top-8"></CombinedUseBase>
          <RealTimeData class="margin-top-8"></RealTimeData>
        </div>

        <div class="flex column content-center">
          <BannerView></BannerView>
          <TodayClass class="margin-top-8"></TodayClass>
        </div>

        <div class="flex column content-right">
          <ImportantNotice></ImportantNotice>
          <CoSponsors class="margin-top-8"></CoSponsors>
          <StudentData class="margin-top-8"></StudentData>
        </div>

      </div>

    </div>
  </div>
</template>

<script>
import SvgBG from '@/views/NewHome/components/SvgBG'
import BannerView from '@/views/NewHome/panes/BannerView'
import DataOverview from '@/views/NewHome/panes/DataOverview'
import CoSponsors from '@/views/NewHome/panes/CoSponsors'
import ImportantNotice from '@/views/NewHome/panes/ImportantNotice'
import RealTimeData from '@/views/NewHome/panes/RealTimeData'
import StudentData from '@/views/NewHome/panes/StudentData'
import TodayClass from '@/views/NewHome/panes/TodayClass'
import WinnersShow from '@/views/NewHome/panes/WinnersShow'
import CombinedUseBase from '@/views/NewHome/panes/CombinedUseBase'
import { mapGetters } from 'vuex'

export default {
  name: 'Home',
  components: {
    SvgBG,
    BannerView,
    DataOverview,
    ImportantNotice,
    RealTimeData,
    StudentData,
    TodayClass,
    WinnersShow,
    CombinedUseBase,
    CoSponsors
  },
  created() {
    if (this.isLogin) return this.loading = false
    window.location.replace(this.$store.state.config.domain.admin)
  },
  mounted() {
    let getScreen = () => {
      this.screen = {
        width: document.body.offsetWidth,
        height: document.body.offsetHeight
      }
    }
    getScreen()
    let fn = () => {
      clearTimeout(this.timer)
      this.timer = setTimeout(getScreen, 100)
    }
    window.addEventListener('resize', fn)
    this.removeEvent = () => window.removeEventListener('resize', fn)
  },
  beforeDestroy() {
    this.removeEvent()
  },
  data() {
    return {
      loading: true,
      // 页面分辨率
      screen: {
        width: 1920,
        height: 1434
      },
      // 实际页面大小
      pageData: {
        width: this.$utils.getScaleSize(1920),
        height: this.$utils.getScaleSize(1080)
      }
    }
  },
  computed: {
    ...mapGetters({
      isLogin: 'system/isLogin'
    }),
    // 配置数据
    options() {
      let line_Start = .24
      let title_w = 565

      return {
        titleText: '国家网络安全人才与创新基地培训中心',

        mg: 0, // 边距

        line_r: 16, // 角距
        line_Rs: .9, // 内角距 相对 角距 比例
        line_Start: .24, // 内角距 初始位置
        line_End: line_Start + .58, // 内角距 结束位置

        title_h: 90, // 标题高度
        title_w, // 标题宽度
        title_deg: 68, // 标题梯度

        deco_w: title_w * .85, // 内装饰宽度
        deco_h: 6, // 内装饰高度
        deco_mg: 2, // 内装饰 与 标题间距

        topbar_h: 16 // 顶部条高度
      }
    },
    pageComputed() {
      let { width: w, height: h } = this.pageData
      let { width, height } = this.screen

      let scaleW = width / w
      let scaleH = height / h


      let tx = 0
      let ty = 0
      let scale = 1

      if (scaleW < scaleH) {
        scale = scaleW
        ty = `${(height - h * scale) / 2}px`
      } else {
        scale = scaleH
        tx = `${(width - w * scale) / 2}px`

      }

      return {
        style: {
          width: w + 'px',
          height: h + 'px',
          transform: `translate(${tx},${ty}) scale(${scale},${scale})`
        },
        screen: {
          width: w,
          height: h
        }
      }
    }
  },
  watch: {
    screen: {
      immediate: true,
      handler(screen) {
        console.log(screen)

      }
    }
  },
  methods: {}
}
</script>

<style scoped lang="stylus">

.page-container
  // background-image -webkit-linear-gradient(#00015b, #2f4689)
  background-image url('~@/assets/main/b.png')
  background-size cover
  background-position center 80%

  .top
    top 0
    width 100vw
    height 12px
    background #0636c4

  .page-content
    position relative
    overflow hidden
    transform-origin 0 0

    .contents
      z-index 9
      width 100%
      height 100%
      overflow hidden

      .content-left, .content-right
        flex 51

      .content-center
        flex 83
        margin 0 12px
.height-250
  height 300px
</style>