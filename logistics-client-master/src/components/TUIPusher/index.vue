<!--
 * @Author: String
 * @Date: 2022-04-22 09:19:20
 * @LastEditTime: 2022-04-27 17:13:58
 * @LastEditors: your name
 * @Description: 
 * @FilePath: /new-teach-client/src/components/TUIPusher/index.vue
-->
<template>
  <div class="width-100p height-100p flex">
    <div class="width-200 margin-right-16 flex column">
      <ComScreenShare class="margin-bottom-16"></ComScreenShare>
      <ComLiveSetting class="flex-1"></ComLiveSetting>
    </div>
    <ComLiveStream class="flex-1" :courseType="courseType"></ComLiveStream>
    <div class="width-300 margin-left-16 flex column">
      <ComParticipants class="margin-bottom-16"></ComParticipants>
      <ComChat class="flex-1"></ComChat>
    </div>
  </div>
</template>

<script>
import ComLiveStream from './ComLiveStream'
import ComScreenShare from './ComScreenShare'
import ComLiveSetting from './ComLiveSetting'
import ComParticipants from './ComParticipants'
import ComChat from './ComChat'

export default {
  name: 'TUIPusher',
  props: {
      courseType: {
          type: String,
          default: ''
      }
  },
  components: {
    ComLiveStream,
    ComScreenShare,
    ComLiveSetting,
    ComParticipants,
    ComChat
  }
}
</script>

<style scoped>

</style>