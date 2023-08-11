<!--
 * @Description: 快直播播放
-->
<template>
  <div class="width-100p height-100p" :id="eleId" ref="ele"></div>
</template>

<script>
import player from './player'

export default {
  name: 'ComLebStream',
  mixins: [player],
  data() {
    return {
      lineType: 'leb'
    }
  },
  methods: {
    setOptions(options) {
      this.options.webrtc = options.webrtc
    }
  }
}
</script>

<style scoped lang="stylus">
</style>