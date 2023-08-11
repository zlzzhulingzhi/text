<template>
  <div class="player-container relative">
    <video id="player-container-id" preload="auto" playsinline webkit-playsinline x5-playsinline :style="videoStyle"></video>
  </div>
</template>

<script>

import {mapState} from 'vuex'
/**
 * Web 端播放问题：https://cloud.tencent.com/document/product/881/20219
 */
export default {
  name: 'TCPlayer',
  data() {
    return {
      tcplayer: null,
    }
  },
  beforeDestroy() {
    console.log('TCPlayer destroyed')
    this.$emit('onDestroy', this.getCurrentTime())
    setTimeout(() => {
      this.tcplayer.dispose()
    }, 200)
  },
  computed: {
    ...mapState('system', {
      scaleNum: 'scaleNum'
    }),
    videoStyle() {
      return {
        width: 100 * this.scaleNum + '%',
        height: 582 * this.scaleNum + 'px',
        transform: `scale(${1/this.scaleNum})`
      }
    }
  },
  methods: {
    initPlayer(options) {
      // 初始化
      this.tcplayer = TCPlayer('player-container-id', {
        controlBar: {
          progressControl: options.playbackDrag || false, // 显示进度条
          playbackRateMenuButton: options.playbackSpeed || false // 显示倍数按钮
        },
        playbackRates: [0.5, 1, 1.25, 1.5, 2],
        ...options.playerConfig
      })
      // 设置封面
      if (options.coverUrl) this.tcplayer.poster(options.coverUrl)
      // 指定播放事件
      this.tcplayer.on('seeked', (data) => {
        console.log('TCPlayer on seeked', data)
      })
      // 倍数播放事件
      this.tcplayer.on('ratechange', (data) => {
        console.log('TCPlayer on ratechange', data)
      })
      // 播放开始事件
      this.tcplayer.on('play', (data) => {
        console.log('TCPlayer on play', data)
      })
      // 播放暂停事件
      this.tcplayer.on('pause', (data) => {
        console.log('TCPlayer on pause', data)
      })
      // 播放结束事件
      this.tcplayer.on('ended', (data) => {
        console.log('TCPlayer on ended', data)
        this.$emit('playNextOne')
      })

      // 获取倍速事件
      this.tcplayer.on('ratechange', (data) => {
        let rateList = data.target.outerText.match(/(\d+\.)?\d+x/g)
        let rate = parseFloat(rateList[rateList.length - 1]) || 1
      })
    },
    play(options) {
      if(!options) {
        return this.$nextTick(()=>{
          this.tcplayer.play()
        })
      }

      if(options.src) {
        // 播放地址
        const suffix = (options.src.match(/[^\.]\w*$/) || [''])[0]
        if (suffix.toLowerCase() === 'flv') {
          this.tcplayer.swf = options.src
        } else {
          this.tcplayer.src(options.src)
        }
      }

      // 开始播放
      if (options.autoplay) {
        this.tcplayer.on('canplay', (data) => {
          console.log('TCPlayer on canplay', data)
          this.$nextTick(() => {
            this.tcplayer.play()
          })
        })
      }
      // 定位播放
      this.tcplayer.on('loadedmetadata', (data) => {
        console.log('TCPlayer on loadedmetadata', data)
        this.tcplayer.currentTime(options.playbackPosition || 0)
      })
    },
    // 获取播放时间
    getCurrentTime() {
      return Math.floor(this.tcplayer.currentTime())
    }
  }
}
</script>

<style scoped lang="stylus">
$main-width = 1366px
$player-width = 966px

.player-container
  width 100%

  #player-container-id
    transform-origin: 0 0
    // width 100%
    // height 582px

  :focus-visible
    outline none

  #player-container-id_html5_api
    width 100% !important
    height 100% !important
    transform: scale(1) !important
</style>
