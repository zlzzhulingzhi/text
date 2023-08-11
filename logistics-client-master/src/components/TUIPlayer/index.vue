<!--
 * @Description: player组件, 兼容PC端、H5(Android、IOS)端
 * @Date: 2021-10-31 16:33:32
 * @LastEditTime: 2021-11-09 15:57:04
-->
<template>
  <div class="width-100p height-100p flex" :class="{h5player:isH5,'is-full-screen':isFullScreen,ios:isIOS}"
       @click="onControlShow">
    <div ref="ComPlayer" class="width-100p height-100p overflow relative" @mouseenter="handleMouseEnter"
         @mouseleave="handleMouseLeave" @mousemove="handleMouseMove">

      <ComLebStream ref="leb" :isPlaying.sync="isLebPlaying" :errorDetail.sync="lebError"
                    v-show="/^leb$/.test(lineType)"></ComLebStream>
      <ComCdnStream ref="cdn" :isPlaying.sync="isCdnPlaying" :errorDetail.sync="cdnError"
                    v-show="/^cdn$/.test(lineType)"></ComCdnStream>

      <div class="absolute error-detail text-f" v-if="/^leb$/.test(lineType) && lebError">
        <span v-if="!isProduction">[{{ lebError.code }}]</span>{{ lebError.text }}
      </div>
      <div class="absolute error-detail text-f" v-else-if="/^cdn$/.test(lineType) && cdnError">
        [{{ cdnError.code }}]{{ cdnError.text }}
      </div>

      <div v-if="!isH5" class="stream-control width-100p height-50 absolute flex between-center font-32 text-f"
           :class="{hide:!showStreamControl}">
        <div class="flex start-center">
          <!--暂停播放按钮-->
          <div class="margin-left-30 pointer flex" v-if="isPlaying" @click.stop="onPause">
            <SvgIcon class="height-32" icon-name="pause"></SvgIcon>
          </div>
          <!--开始播放按钮-->
          <div class="margin-left-30 pointer flex" v-else @click.stop="onPlay">
            <SvgIcon icon-name="play"></SvgIcon>
          </div>
          <!--分享按钮-->
          <ComShare class="margin-left-30"></ComShare>
        </div>
        <div class="flex">

          <!--线路选择-->
          <div v-if="isPC" class="relative margin-right-30 font-14" @mouseenter="showLineOptions = !showLineOptions"
               @mouseleave="showLineOptions = !showLineOptions">
            <div class="line-height-24">{{ $t(lineOptions[lineType]) }}</div>

            <div class="line-content absolute text-f width-400 padding-50" v-if="showLineOptions">
              <div class="inner radius-4 padding-8">
                <div v-for="(item, index) in lineOptions" :key="index" @click.stop="handleChangeLine(index)"
                     class="padding-8 pointer flex between-center" :class="{active:lineType === index}">
                  <span>{{ $t(item) }}</span>
                  <SvgIcon class="text-shadow" v-if="lineType === index" icon-name="checked"></SvgIcon>
                </div>
              </div>
            </div>
          </div>

          <!--全屏/取消全屏-->
          <div class="margin-right-30 width-24 height-24 text-f font-24 pointer">
            <!--全屏按钮-->
            <div v-if="!isFullScreen" @click.stop="onFullScreen">
              <SvgIcon icon-name="full-screen"></SvgIcon>
            </div>
            <!--取消全屏按钮-->
            <div v-if="isFullScreen" @click.stop="onQuitFullScreen">
              <SvgIcon icon-name="quit-full-screen"></SvgIcon>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="h5-control absolute flex between-center text-f" :class="{hide:isControlHide}">
        <div class="flex">
          <div class="icon-text" :class="{active:/^leb$/.test(lineType)}"
               @click.stop="handleChangeLine('leb')" v-if="checkResult.WebRTCSupport">
            RTC
          </div>
          <div class="icon-text" :class="{active:/^cdn/.test(lineType)}" @click.stop="handleChangeLine('cdn')">
            HLS
          </div>
        </div>
        <div class="flex">
          <!--暂停播放按钮-->
          <!--          <div class="pointer h5-control-btn flex center-center" v-if="isPlaying" @click.stop="onPause">-->
          <!--            <SvgIcon class="height-32" icon-name="pause"></SvgIcon>-->
          <!--          </div>-->
          <!--开始播放按钮-->
          <!--          <div class="pointer h5-control-btn flex center-center" v-else @click.stop="onPlay">-->
          <!--            <SvgIcon icon-name="play"></SvgIcon>-->
          <!--          </div>-->
          <!--横屏/竖屏-->
          <div v-if="isFullScreen" class="pointer h5-control-btn flex center-center" @click.stop="onQuitFullScreen">
            <SvgIcon icon-name="quit-full-screen"></SvgIcon>
          </div>
          <div v-else class="pointer h5-control-btn flex center-center" @click.stop="onFullScreen">
            <SvgIcon icon-name="full-screen"></SvgIcon>
          </div>
        </div>
      </div>

      <!--      <div v-if="isH5" class="play-control absolute text-f">
              &lt;!&ndash;开始播放按钮&ndash;&gt;
              <div class="pointer flex" v-if="!isPlaying" @click.stop="onPlay">
                <SvgIcon icon-name="play"></SvgIcon>
              </div>
            </div>-->

    </div>

    <ComIm ref="ComIm" class="width-300 com-im" v-show="!isIOS||!isFullScreen"></ComIm>
  </div>
</template>

<script>
import ComShare from '@/components/TUIPlayer/ComTools/ComShare'
import ComIm from '@/components/TUIPlayer/ComTools/ComIm'
import ComLebStream from '@/components/TUIPlayer/ComPlayer/ComLebStream'
import ComCdnStream from '@/components/TUIPlayer/ComPlayer/ComCdnStream'
import {mapGetters} from 'vuex'

export default {
  name: 'TUIPlayer',
  components: {
    ComShare,
    ComIm,
    ComLebStream,
    ComCdnStream
  },
  beforeDestroy() {
    this.leaveRoom()
  },
  data() {
    return {
      showStreamControl: false,
      timer: null,
      previousTime: 0,

      isLebPlaying: false,
      isCdnPlaying: false,

      lebError: null,
      cdnError: null,

      lineType: 'leb',
      lineOptions: {
        leb: 'Link 1: LEB',
        cdn: 'Link 2: LVB'
      },
      showLineOptions: false,

      isFullScreen: false,
      isEnter: false,
      isControlHide: false,

      basicLiveId: null,
      userId: null,
      options: {}
    }
  },
  props: {
    checkResult: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  computed: {
    ...mapGetters({
      isIOS: 'isIOS',
      isPC: 'isPC',
      isH5: 'isH5',
      isAndroid: 'isAndroid',
      isProduction: 'isProduction'
    }),
    deviceInfo() {
      // 1 电脑端 2 移动端
      // ios, android, pc
      if (this.isPC) return {clientType: 1, useDevice: 'pc'}
      if (this.isAndroid) return {clientType: 2, useDevice: 'android'}
      if (this.isIOS) return {clientType: 2, useDevice: 'ios'}
    },
    isPlaying() {
      if (this.lineType === 'leb') return this.isLebPlaying
      if (this.lineType === 'cdn') return this.isCdnPlaying
      return false
    }
  },
  methods: {
    onInitPlayer(options) {
      this.options = options
      if (options.type) this.lineType = options.type
      this.$nextTick(() => {
        this.$refs[this.lineType].onInitPlayer(options)
        this.$refs.ComIm.initTim()
        this.enterRoom(options)
      })
    },
    onPlay() {
      this.$refs[this.lineType].onPlay()
    },
    onPause() {
      this.$refs[this.lineType].onPause()
    },
    async enterRoom({basicLiveId, userId, orgId}) {
      console.log('%c 埋点统计 - 进入', 'background: #856;color:#fff')
      this.isEnter = true

      this.basicLiveId = basicLiveId
      this.userId = userId

      await this.$api.BasicLiveShowViewer.create({
        basicLiveShowId: basicLiveId,
        ...this.deviceInfo,
        joinTime: this.$moment().format('yyyy-MM-DD HH:mm:ss'),
        leaveTime: this.$moment().add(3, 'hour').format('yyyy-MM-DD HH:mm:ss'),
        userId,
        orgId
      })

      window.addEventListener('beforeunload', () => {
        this.leaveRoom()
      })
    },
    async leaveRoom() {
      if (!this.isEnter) return false
      console.log('%c 埋点统计 - 离开', 'background: #856;color:#fff')
      this.isEnter = false
      await this.$api.BasicLiveShowViewer.update({
        basicLiveShowId: this.basicLiveId,
        leaveTime: this.$moment().format('yyyy-MM-DD HH:mm:ss'),
        userId: this.userId
      })
    },


    // 操作 - 控制栏
    handleMouseEnter() {
      this.showStreamControl = true
      clearTimeout(this.timer)
      this.timer = setTimeout(this.hideStreamControl, 6000)
    },
    handleMouseLeave() {
      this.hideStreamControl()
    },
    handleMouseMove() {
      const now = Date.now()
      if (now - this.previousTime > 500) {
        this.showStreamControl = true
        clearTimeout(this.timer)
        this.timer = setTimeout(this.hideStreamControl, 6000)
        this.previousTime = now
      }
    },
    hideStreamControl() {
      this.showStreamControl = false
    },

    // 操作 - 线路控制
    handleChangeLine(index) {
      this.lineType = index
      this.$refs[index].onInitPlayer(this.options)
    },

    // 操作 - 全屏、取消全屏
    onFullScreen() {
      this.isFullScreen = true
      this.isControlHide = true
      if (this.isAndroid || this.isPC) {
        this.$utils.SetFullScreen(this.$refs.ComPlayer)
      }
    },
    onQuitFullScreen() {
      this.isFullScreen = false
      if (this.isAndroid || this.isPC) {
        this.$utils.ExitFullScreen()
      }
    },
    onControlShow() {
      if (this.isFullScreen) {
        this.isControlHide = !this.isControlHide
      }
    }
  }
}
</script>

<style lang="stylus" scoped>
.stream-control
  bottom 0
  background-image linear-gradient(140deg, rgba(21, 27, 48, 0.70) 0%, rgba(28, 33, 49, 0.90) 100%)
  box-shadow 0 0 3px 0 rgba(32, 32, 32, 0.40)
  transition transform 0.2s ease-out
  transform translateY(0)

  &.hide
    transform translateY(50px)

.line-content
  right 0
  bottom 0

  .inner
    background-color #494e5d

    > div
      &:hover, .active
        color SHADOW_COLOR

.error-detail
  top 50%
  left 50%
  transform translate(-50%, -50%)
  background-color #000
  padding 8px
  border-radius 8px

// h5
.h5player
  background-color #000

  >>> .com-im
    position absolute
    bottom (24vw / 7.5)
    height 35vh
    width 100vw
    background-color transparent

    .el-card__header
      display none

    .user-name, .message-info, .message-content
      color NEUTRAL_COLOR_F

  .play-control
    top 35vh
    left 50vw
    transform translate(-50%, -50%)
    font-size (48vw / 7.5)

    .pointer
      padding (48vw / 7.5)

  h5height = (64vw / 7.5)

  .h5-control
    top 0
    width 100vw
    height h5height
    z-index 10000

    .h5-control-btn
      width h5height
      height h5height

    .icon-text
      font-size (12vw / 7.5)
      border-radius (8vw / 7.5)
      border-width (1vw / 7.5)
      border-style solid
      padding (2vw / 7.5) (4vw / 7.5)
      margin-left (8vw / 7.5)

      &.active
        border-color SUCCESS_COLOR
        color SUCCESS_COLOR

  &.is-full-screen
    .com-im
      height 0
      overflow hidden

    .h5-control
      transition transform 0.2s ease-out
      transform translateY(0) translateZ(1vw / 7.5)
      background-image linear-gradient(140deg, rgba(21, 27, 48, 0.70) 0%, rgba(28, 33, 49, 0.90) 100%)
      box-shadow 0 0 3px 0 rgba(32, 32, 32, 0.40)

      &.hide
        transform translateY(-100%) translateZ(1vw / 7.5)

/*    &.ios
      width 100vh
      height 100vw
      transform-origin 0 0
      transform rotate(90deg) translateY(-100%)

      .h5-control
        width 100vh
        padding 0 (24vw / 7.5)*/

@media screen and (orientation: landscape)
  /*横屏*/
  .h5player
    &.is-full-screen
      .h5-control
        height (64vh / 7.5)
        padding 0 (32vh / 7.5)

        .h5-control-btn
          width (64vh / 7.5)
          height (64vh / 7.5)


@media screen and (orientation: portrait)
  /*竖屏*/
  .h5player
    &.is-full-screen
      &.ios
        width 100vh
        height 100vw
        transform-origin 0 0
        transform rotate(90deg) translateY(-100%)

        .h5-control
          width 100vh
          padding 0 (32vw / 7.5)
</style>

<i18n>
{
  "en": {
    "Link 1: LEB": "Link 2: LEB",
    "Link 2: LVB": "Link 3: LVB"
  },
  "zh": {
    "Link 1: LEB": "普通线路",
    "Link 2: LVB": "兼容线路"
  }
}
</i18n>
