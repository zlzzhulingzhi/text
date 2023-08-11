<!--
 * @Description: 视频流区域
 * @Date: 2021-09-22 16:40:01
 * @LastEditTime: 2022-04-27 16:57:35
-->
<template>
  <div class="pusher-rtc-container">
    <ComStreamPusher class="stream-pusher" :courseType="courseType"></ComStreamPusher>
    <ComStreamControl class="stream-control" :courseType="courseType"></ComStreamControl>
  </div>
</template>

<script>
import ComStreamControl from './ComStreamControl'
import ComStreamPusher from './ComStreamPusher'

export default {
  name: 'ComLiveStream',
  props: {
      courseType: {
          type: String,
          default: ''
      }
  },
  components: {
    ComStreamControl,
    ComStreamPusher
  }
}
</script>

<style lang="stylus" scoped>
.pusher-rtc-container
  position relative
  width 100%
  height 100%

  .stream-pusher
    position relative
    padding-top 50px
    width 100%
    height 100%

  .stream-control
    position absolute
    top 0
    left 0
</style>

