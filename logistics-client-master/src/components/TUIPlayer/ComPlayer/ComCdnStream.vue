<!--
 * @Description: 标准直播播放
-->
<template>
  <div class="width-100p height-100p" :id="eleId" ref="ele"></div>
</template>

<script>
import player from './player'

export default {
  name: 'ComCdnStream',
  mixins: [player],
  data() {
    return {
      lineType: 'cdn'
    }
  },
  methods: {
    setOptions(options) {
      this.options.webrtc = options.m3u8
      this.options.flv = options.flv
    }
  }
}
</script>

<style scoped lang="stylus">
</style>