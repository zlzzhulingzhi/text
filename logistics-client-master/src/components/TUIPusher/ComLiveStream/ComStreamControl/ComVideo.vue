<!--
 * @Description: 视频icon，控制打开/关闭摄像头
 * @Date: 2021-11-01 14:47:24
 * @LastEditTime: 2021-11-01 15:00:23
-->
<template>
  <div class="camera" @click="toggleMuteVideo">
    <span class="icon camera-on" v-if="!isVideoMuted">
      <SvgIcon icon-name="camera"></SvgIcon>
    </span>
    <span class="icon camera-off" v-if="isVideoMuted">
      <SvgIcon icon-name="video-muted"></SvgIcon>
    </span>
  </div>
</template>

<script>
import {LIVE_STAGE} from '@/components/TUIPusher/constants/room'
import {mapState} from 'vuex'

export default {
  name: 'ComVideo',
  data() {
    return {
      LIVE_STAGE
    }
  },
  computed: {
    ...mapState('livePusher', {
      isVideoMuted: 'isVideoMuted'
    })
  },
  methods: {
    // 切换摄像头mute状态
    toggleMuteVideo() {
      this.$store.commit('livePusher/updateIsVideoMuted', !this.isVideoMuted)
    }
  }
}
</script>

<style lang="stylus" scoped>
.icon
  display inline-block
  color NEUTRAL_COLOR_F
  font-size 24px
  width 24px
  height 24px
  cursor pointer
</style>
