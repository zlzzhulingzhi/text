<template>
  <div class="flex center-start player-container relative">
    <video id="player-container-id" preload="auto" playsinline webkit-playsinline></video>
    <slot name="side"></slot>
    <div class="flex end-center text-e font-13 player-banner">
      <span class="pointer margin-right-20" @click="onShare"><i class="el-icon-share"></i> 分享</span>
      <!-- <span class="pointer margin-right-10" @click="onShare"><i class="el-icon-mobile-phone"></i> 手机观看</span> -->
    </div>
    <el-image class="ic-pause" :src="require('@/assets/course/ic_play.png')" fit="fill" v-show="isPause"></el-image>
    <DialogShare ref="DialogShare"></DialogShare>
  </div>
</template>

<script>
/**
 * Web 端播放问题：https://cloud.tencent.com/document/product/881/20219
 */
import DialogShare from './DialogShare.vue'

export default {
  components: {
    DialogShare
  },
  data() {
    return {
      tcplayer: null,
      isPause: false,
      illegalPlay: false,
      isExclude: false
    }
  },
  beforeDestroy() {
    console.log('TCPlayer destroyed')
    this.$emit('onDestroy', this.getCurrentTime())
    setTimeout(() => {
      this.tcplayer.dispose()
    }, 200)
  },
  methods: {
    initPlayer(options) {
      // 初始化
      this.tcplayer = TCPlayer('player-container-id', {
        controlBar: {
          // progressControl: options.controls || false,
          // playbackRateMenuButton: options.controls || false,
          progressControl: options.playbackDrag || false, // 显示进度条
          playbackRateMenuButton: options.playbackSpeed || false // 显示倍数按钮
        },
        playbackRates: [0.5, 1, 1.25, 1.5, 2]
      })
      // 设置封面
      if (options.coverUrl) this.tcplayer.poster(options.coverUrl)
      // 指定播放事件
      this.tcplayer.on('seeked', (data) => {
        console.log('TCPlayer on seeked', data)
        if (!options.controls && !this.isExclude) {
          this.illegalPlay = true
        }
        this.isExclude = false
      })
      // 倍数播放事件
      this.tcplayer.on('ratechange', (data) => {
        console.log('TCPlayer on ratechange', data)
        if (!options.controls && !this.isExclude) {
          this.illegalPlay = true
        }
        this.isExclude = false
      })
      // 播放开始事件
      this.tcplayer.on('play', (data) => {
        console.log('TCPlayer on play', data)
        this.$emit('onPlay')
        this.isPause = false
      })
      // 播放暂停事件
      this.tcplayer.on('pause', (data) => {
        console.log('TCPlayer on pause', data)
        this.$emit('onPause', this.getCurrentTime())
        this.isPause = true
      })
      // 播放结束事件
      this.tcplayer.on('ended', (data) => {
        console.log('TCPlayer on ended', data)
        if (this.illegalPlay) return console.log('非正常播放，成绩不记录')
        this.$emit('onEnd', this.getCurrentTime())
      })

      // 获取倍速事件
      this.tcplayer.on('ratechange', (data) => {
        let rateList = data.target.outerText.match(/(\d+\.)?\d+x/g)
        let rate = parseFloat(rateList[rateList.length - 1]) || 1
        this.$emit('onRatechange', rate)
      })
    },
    play(options) {
      // 播放地址
      const suffix = (options.src.match(/[^\.]\w*$/) || [''])[0]
      if (suffix.toLowerCase() === 'flv') {
        this.tcplayer.swf = options.src
      } else {
        this.tcplayer.src(options.src)
      }
      // 开始播放
      if (options.autoplay) {
        this.tcplayer.on('canplay', (data) => {
          console.log('TCPlayer on canplay', data)
          this.$nextTick(()=>{
            this.tcplayer.play()
          })
        })
      }
      // 定位播放
      this.tcplayer.on('loadedmetadata', (data) => {
        console.log('TCPlayer on loadedmetadata', data)
        this.isExclude = true
        this.tcplayer.currentTime(options.playbackPosition || 0)
      })
    },
    // 获取播放时间
    getCurrentTime() {
      return Math.floor(this.tcplayer.currentTime())
    },
    // 分享
    onShare() {
      this.$refs.DialogShare.open()
    }
  }
}
</script>

<style lang="stylus" scoped>
$main-width = 1366px
$player-width = 966px

.player-container
  width $main-width

  #player-container-id
    width $player-width
    height 'calc(%s * 9 / 16)' % $player-width

  .player-banner
    display none
    position absolute
    width $player-width
    top 0
    left 0
    padding 10px
    background-color rgba(0, 0, 0, 0.5)

  &:hover
    .player-banner
      display flex

  :focus-visible
    outline none

  .player-side
    flex 1
    height 'calc(%s * 9 / 16)' % $player-width
    overflow-y auto

  .ic-pause
    position absolute
    top 'calc(%s * 9 / 16 / 2 - 44px)' % $player-width
    left 'calc(%s / 2 - 44px)' % $player-width
    color #ddd
    pointer-events none
</style>