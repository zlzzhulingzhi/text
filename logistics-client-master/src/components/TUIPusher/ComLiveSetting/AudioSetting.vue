<!--
 * @Description: 麦克风设置弹窗
 * @Date: 2021-10-27 18:13:37
 * @LastEditTime: 2021-11-09 15:47:10
-->
<template>
  <div class="audio-setting">
    <div class="microphone-select">
      <span class="title">{{ $t('device.Mic') }}</span>
      <el-select class="select-style" v-model="choseMicrophoneId" :placeholder="$t('device.Select a mic')">
        <el-option v-for="item in microphoneList" :key="item.deviceId" :label="item.label"
                   :value="item.deviceId"></el-option>
      </el-select>
    </div>
    <div class="speaker-select">
      <span class="title">{{ $t('device.Speaker') }}</span>
      <el-select class="select-style" v-model="choseSpeakerId" disabled :placeholder="$t('device.Select a speaker')">
        <el-option v-for="item in speakerList" :key="item.deviceId" :label="item.label"
                   :value="item.deviceId"></el-option>
      </el-select>
    </div>
  </div>
</template>

<script>
import rtc from '@/components/TUIPusher/mixin/rtc'
import {mapGetters} from 'vuex'

export default {
  name: 'AudioSetting',
  mixins: [rtc],
  data() {
    return {
      choseMicrophoneId: '',
      choseSpeakerId: '',
      microphoneList: [],
      speakerList: []
    }
  },
  computed: {
    ...mapGetters({
      activeMicrophoneId: 'livePusher/activeMicrophoneId',
      activeSpeakerId: 'livePusher/activeSpeakerId'
    })
  },
  watch: {
    activeMicrophoneId: {
      immediate: true,
      handler(val) {
        this.choseMicrophoneId = val
      }
    },
    activeSpeakerId: {
      immediate: true,
      handler(val) {
        this.choseSpeakerId = val
      }
    },
    choseMicrophoneId(val) {
      const choseMicrophoneDevice = this.microphoneList.find(item => item.deviceId === val)
      this.$store.commit('livePusher/updateActiveMicrophone', choseMicrophoneDevice)
    }
  },
  methods: {
    async getDeviceList() {
      this.microphoneList = await this.getMicrophones()
      this.speakerList = await this.getSpeakers()

      const microphoneIDList = this.microphoneList.map(microphone => microphone.deviceId)
      if (microphoneIDList.indexOf(this.choseMicrophoneId) < 0) {
        this.choseMicrophoneId = this.microphoneList[0].deviceId
      }

      const speakerIDList = this.speakerList.map(speaker => speaker.deviceId)
      if (speakerIDList.indexOf(this.choseSpeakerId) < 0) {
        this.choseSpeakerId = this.speakerList[0].deviceId
      }
    }
  },
  created() {
    navigator.mediaDevices.addEventListener('devicechange', async () => {
      await this.getDeviceList()
    })
    this.getDeviceList()
  }
}
</script>

<style lang="stylus" scoped>
.audio-setting
  padding 0 20px 20px 34px

  .title
    display inline-block
    width 60px

  .select-style
    width 300px
    margin-left 10px
    margin-bottom 10px
</style>

