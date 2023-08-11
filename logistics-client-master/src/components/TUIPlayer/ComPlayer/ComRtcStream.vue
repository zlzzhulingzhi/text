<!--
 * @Description: 超低延时播放
 * @Date: 2021-11-04 11:02:45
 * @LastEditTime: 2021-11-09 15:56:33
-->
<template>
  <div class="rtc-stream">
    <div id="share-stream" class="share-stream" v-show="anchorShareStream"></div>
    <div id="main-stream" class="stream" :class="{'small-stream':anchorShareStream}"></div>
  </div>
</template>

<script>
import rtc from '@/components/TUIPlayer/mixin/rtc'
import {PLAY_STATE} from '@/components/TUIPlayer/constants/room'
import {mapGetters, mapMutations, mapState} from 'vuex'

export default {
  name: 'ComRtcStream',
  mixins: [rtc],
  async beforeDestroy() {
    await this.handleLeave()
  },
  data() {
    return {
      anchorStream: null,
      anchorShareStream: null
    }
  },
  computed: {
    ...mapGetters({
      userSig: 'live/userSig',
      sdkAppId: 'live/sdkAppId',
      roomId: 'live/roomId',
      userId: 'live/userId',
    }),
    ...mapState('live', {
      playState: 'playState'
    })
  },
  watch: {
    playState(val) {
      if (val === PLAY_STATE.PLAYING) {
        this.playAnchorStream()
        this.playAnchorShareStream()
      } else if (val === PLAY_STATE.PAUSED) {
        this.stopAnchorStream()
        this.stopAnchorShareStream()
      }
    }
  },
  methods: {
    ...mapMutations({
      updatePlayState: 'live/updatePlayState'
    }),
    async initPlayer() {
      if (!this.roomId) return false
      await this.handleJoin('audience')
    },
    playAnchorStream() {
      this.anchorStream && this.playStream(this.anchorStream, 'main-stream')
    },
    stopAnchorStream() {
      this.anchorStream && this.anchorStream.stop()
    },
    playAnchorShareStream() {
      this.anchorShareStream
      && this.playStream(this.anchorShareStream, 'share-stream', {objectFit: 'contain'})
    },
    stopAnchorShareStream() {
      this.anchorShareStream && this.anchorShareStream.stop()
    },
    handleSubscribedStream(remoteStream) {
      if (remoteStream.getUserId().startsWith('share')) {
        this.anchorShareStream = remoteStream
        if (this.playState === PLAY_STATE.PLAYING) {
          this.playAnchorShareStream()
        }
      } else {
        this.anchorStream = remoteStream
        if (this.playState === PLAY_STATE.PLAYING) {
          this.playAnchorStream()
        }
      }
    },
    handleStreamRemoved(remoteStream) {
      if (remoteStream.getUserId().startsWith('share')) {
        this.anchorShareStream = null
        if (this.playState === PLAY_STATE.PLAYING) {
          this.stopAnchorShareStream()
        }
      } else {
        this.anchorStream = null
        if (this.playState === PLAY_STATE.PLAYING) {
          this.stopAnchorStream()
        }
      }
    },

    onPlay() {
      this.updatePlayState(PLAY_STATE.PLAYING)
    },
    onPause() {
      this.updatePlayState(PLAY_STATE.PAUSED)
    }
  }
}
</script>

<style lang="stylus" scoped>
.rtc-stream
  width 100%
  height 100%
  position relative

  .share-stream
    width 100%
    height 100%
    position absolute
    top 0
    left 0

  .stream
    width 100%
    height 100%
    position absolute
    right 0
    bottom 0

    &.small-stream
      width 15%
      height 15%
      position absolute
      right 0
      bottom 0
</style>

