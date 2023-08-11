<!--
 * @Description: 音频icon，显示音量，控制打开/关闭麦克风
 * @Date: 2021-11-01 14:47:11
 * @LastEditTime: 2021-11-09 15:43:47
-->
<template>
  <div class="microphone" @click="toggleMuteAudio">
    <span class="icon mic-on" v-if="!isAudioMuted">
      <SvgIcon class="icon" icon-name="audio"></SvgIcon>
      <span class="icon green-mic" :style="greenAudioHeight">
        <SvgIcon class="green-icon" icon-name="audio"></SvgIcon>
      </span>
    </span>
    <span class="icon mic-off" v-if="isAudioMuted">
      <SvgIcon icon-name="audio-muted"></SvgIcon>
    </span>
  </div>
</template>

<script>
import {LIVE_STAGE} from '@/components/TUIPusher/constants/room'
import {mapState} from 'vuex'

export default {
  name: 'ComAudio',
  data() {
    return {
      LIVE_STAGE,
      isFullScreen: false
    }
  },
  computed: {
    ...mapState('livePusher', {
      isAudioMuted: 'isAudioMuted',
      audioLevel: 'audioLevel'
    }),
    greenAudioHeight() {
      return {
        height: `${this.audioLevel * 4 * 100}%`
      }
    }
  },
  methods: {
    // 切换麦克风mute状态
    toggleMuteAudio() {
      this.$store.commit('livePusher/updateIsAudioMuted', !this.isAudioMuted)
    }
  }
}
</script>

<style lang="stylus" scoped>
.microphone
  margin-right 20px

.icon
  display inline-block
  color NEUTRAL_COLOR_F
  width 24px
  height 24px
  font-size 24px
  cursor pointer

.mic-on
  position relative

  .green-mic
    position absolute
    overflow hidden
    left 0
    bottom 0
    transition height .1s ease

    .green-icon
      width 24px
      height 24px
      font-size 24px
      color SUCCESS_COLOR
      position absolute
      bottom 0
      left 0
</style>
