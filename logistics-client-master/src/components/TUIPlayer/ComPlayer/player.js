import {mapState} from 'vuex'
window.playerNum = 0

export default {
  beforeDestroy() {
    this.destroyPlayer()
  },
  data() {
    return {
      player: null,
      eleId: `cdn-player-${playerNum++}`,
      isPlay: false,
      options: {},
      wording: {
        1: '网络错误，请检查网络配置',
        2: '主播不在线',
        3: '当前系统环境不支持播放该视频格式',
        4: '当前系统环境不支持播放该视频格式',
        5: '当前系统环境不支持播放该视频格式',
        12: '主播不在线',
        13: '主播不在线',

        1001: '主播不在线',
        1002: '主播不在线',

        2001: '主播不在线',
        2002: '主播不在线',
        2003: '主播不在线',

        2032: '主播不在线',
        2048: '主播不在线'
      }
    }
  },
  props: {
    controls: {
      type: String,
      default: 'none'
    }
  },
  computed: {
    ...mapState('config', {
      images: 'images'
    })
  },
  watch: {
    isPlay: {
      immediate: true,
      handler(val) {
        this.$emit('update:isPlaying', val)
      }
    }
  },
  methods: {
    async onInitPlayer(options) {
      if (options) {
        this.setOptions(options)
        if (!TcPlayer) return false

        this.$refs.ele.innerHTML = ''
        return this.initPlayer()
      }
      if (!this.player) this.initPlayer()
    },

    initPlayer() {
      console.log(`%c 初始化播放器`, 'background: #494e5d;color:#fff')
      this.player = new TcPlayer(this.eleId, {
        ...this.options,
        wording: this.wording,
        controls: this.controls,
        listener: this.playerListener.bind(this),

        poster: this.images.video,
        live: true,
        width: '100%',
        height: '100%',
        autoplay: false,
        x5_player: true,
        x5_type: 'h5-page',
        x5_fullscreen: true,
        x5_orientation: 0,
        webrtcConfig: {
          connectRetryCount: 10
        }
      })
    },

    playerListener(event) {
      if (!/^(timeupdate|webrtcstatupdate|resize)$/.test(event.type)) {
        console.log(`%c 监听 ${this.lineType}`, 'background: #494e5d;color:#fff', event)
      }
      switch (event.type) {
        case 'error':
          this.isPlay = false
          this.$emit('update:errorDetail', {
            ...event.detail,
            text: this.wording[event.detail.code]
          })
          try {
            this.player.destroy()
          } catch (e) {
          }
          return false
        case 'loadeddata':
          this.onPlay()
          break
        case 'play':
          this.isPlay = true
          break
          // webrtcstop
        case 'pause':
          this.isPlay = false
          break
      }
      this.$emit('update:errorDetail', null)
    },

    onPlay() {
      this.player && this.player.play()
    },
    onPause() {
      this.player && this.player.pause()
    },
    destroyPlayer() {
      this.player && this.player.destroy()
    }
  }
}