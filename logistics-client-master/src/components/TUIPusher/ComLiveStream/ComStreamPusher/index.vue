<template>
  <div class="stream-container">
    <div id="stream" class="stream" ref="stream" :class="rotateClass">
    </div>
    <div class="tip-container" v-if="attentionInfo"
         :style="{backgroundImage: `url(${require('@/assets/livePusher/bg-end.jpg')})`}">
      <div class="tip">
        {{ attentionInfo }}
      </div>
    </div>
  </div>
</template>

<script>
import rtc from '@/components/TUIPusher/mixin/rtc'
import {LIVE_STAGE} from '@/components/TUIPusher/constants/room'
import {mapGetters, mapMutations, mapState} from 'vuex'

export default {
  name: 'ComStreamPusher',
  mixins: [rtc],
  async created() {
    await this.initDevice()
    await this.initAndPlayStream()

    window.addEventListener('beforeunload', () => {
      this.destroy()
    })
  },
  beforeDestroy() {
    this.destroy()
  },
  props: {
      courseType: {
          tyoe: String,
          default: ''
      }
  },
  data() {
    return {
      LIVE_STAGE
    }
  },
  computed: {
    ...mapGetters({
      activeCameraId: 'livePusher/activeCameraId',
      activeMicrophoneId: 'livePusher/activeMicrophoneId',
      sdkAppId: 'livePusher/sdkAppId',
      userSig: 'livePusher/anchorUserSig',
      roomId: 'livePusher/roomId',
      userId: 'livePusher/anchorUserId',
      shareUserId: 'livePusher/anchorShareUserId'
    }),
    ...mapState('livePusher', {
      liveStage: 'liveStage',
      isSetMirror: 'isSetMirror',
      videoProfile: 'videoProfile',
      isOpenBeauty: 'isOpenBeauty',
      beautyParam: 'beautyParam',
      isAudioMuted: 'isAudioMuted',
      isVideoMuted: 'isVideoMuted',
      isRecordLive: 'isRecordLive',
      isScreenSharing: 'isScreenSharing',
      screenProfile: 'videoProfile',
      basicLiveShowInfo: 'basicLiveShowInfo'
    }),
    rotateClass() {
      return this.isSetMirror ? 'rotateY-180' : 'rotateY-0'
    },
    attentionInfo() {
      let attentionInfo = ''
      switch (this.liveStage) {
        case LIVE_STAGE.NOT_STARTED:
          if (this.isVideoMuted) {
            if (this.isAudioMuted) {
              attentionInfo = this.$t('Please turn your camera or mic on.')
            } else {
              attentionInfo = this.$t('The camera is turned off.')
            }
          } else if (this.activeCameraId === undefined) {
            attentionInfo = this.$t('Please check your camera.')
          }

          break
        case LIVE_STAGE.ONGOING:
          if (this.isVideoMuted && !this.isAudioMuted || this.activeCameraId === undefined) {
            attentionInfo = this.$t('Audio streaming')
          }
          if (this.isVideoMuted && this.isAudioMuted) {
            attentionInfo = this.$t('Please turn your camera or mic on.')
          }
          break
        case LIVE_STAGE.PAUSED:
          attentionInfo = this.$t('Streaming paused')
          break
        case LIVE_STAGE.ENDED:
          attentionInfo = this.$t('Streaming ended')
          break
        default:
          break
      }
      return attentionInfo
    }
  },
  watch: {
    async liveStage(val, oldVal) {
      console.log(`%c 监听房间状态 liveStage ${val}`, 'background: #985;line-height: 24px')
      if (val === LIVE_STAGE.ONGOING && oldVal === LIVE_STAGE.NOT_STARTED) {
        await this.handleJoin()
        await this.doStartLive()
        return
      }
      if (val === LIVE_STAGE.ONGOING && oldVal === LIVE_STAGE.PAUSED) {
        await this.doGoOnLive()
        return
      }
      if (val === LIVE_STAGE.PAUSED) {
        await this.doPauseLive()
        return
      }
      if (val === LIVE_STAGE.ENDED) {
        await this.doStopLive()
      }
    },
    activeCameraId(val, oldVal) {
      if (oldVal) {
        this.switchDevice('video', val)
      }
    },
    activeMicrophoneId(val, oldVal) {
      if (oldVal) {
        this.switchDevice('audio', val)
      }
    },
    videoProfile: {
      deep: true,
      immediate: true,
      handler(val) {
        this.setVideoProfile(val)
      }
    },
    isOpenBeauty: {
      immediate: true,
      handler(val) {
        if (val) {
          if (!this.rtcBeautyPlugin) {
            this.initBeauty()
          }
          this.updateBeauty({
            beauty: this.beautyParam.beautyValue / 100,
            brightness: this.beautyParam.brightnessValue / 100,
            ruddy: this.beautyParam.ruddyValue / 100
          })
        } else {
          this.updateBeauty({
            beauty: 0,
            brightness: 0,
            ruddy: 0
          })
        }
      }
    },
    beautyParam: {
      handler(val) {
        this.updateBeauty({
          beauty: val.beautyValue / 100,
          brightness: val.brightnessValue / 100,
          ruddy: val.ruddyValue / 100
        })
      },
      deep: true
    },
    async isAudioMuted(val) {
      if (!this.localStream) return
      if (val) {
        this.muteAudio()
      } else {
        const audioTrack = this.localStream.getAudioTrack()
        if (audioTrack) {
          this.unmuteAudio()
        } else {
          const newStream = await this.initLocalStream({
            userId: this.userId,
            audio: true,
            video: false,
            microphoneId: this.activeMicrophoneId
          })
          const audioTrack = newStream.getAudioTrack()
          this.localStream.addTrack(audioTrack)
        }
      }
    },
    async isVideoMuted(val) {
      if (!this.localStream) return
      if(this.activeCameraId === undefined) return false
      if (val) {
        this.muteVideo()
      } else {
        const videoTrack = this.localStream.getVideoTrack()
        if (videoTrack) {
          this.unmuteVideo()
        } else {
          const newStream = await this.initLocalStream({
            userId: this.userId,
            audio: false,
            video: true,
            cameraId: this.activeCameraId
          })
          const videoTrack = newStream.getVideoTrack()
          await this.localStream.addTrack(videoTrack)
          this.initBeauty()
        }
      }
    },
    uplinkNetworkQuality(val) {
      const networkLevel = val > 0 ? 6 - val : val
      this.$store.commit('livePusher/updateUplinkQualityLevel', networkLevel)
    },
    isScreenSharing(val) {
      if (val) {
        const config = {
          mode: 'manual',
          videoWidth: this.screenProfile.width,
          videoHeight: this.screenProfile.height,
          videoBitrate: this.videoProfile.bitrate,
          videoFramerate: this.videoProfile.frameRate,
          mixUsers: [
            {
              userId: this.shareUserId,
              pureAudio: false,
              width: this.screenProfile.width,
              height: this.screenProfile.height,
              locationX: 0,
              locationY: 0,
              streamType: 'main',
              zOrder: 1
            },
            {
              userId: this.userId,
              pureAudio: false,
              width: Math.floor(this.screenProfile.width * 0.15),
              height: Math.floor(this.screenProfile.height * 0.15),
              locationX: Math.floor(this.screenProfile.width * 0.85),
              locationY: Math.floor(this.screenProfile.height * 0.85),
              streamType: 'main',
              zOrder: 2
            }
          ]
        }
        this.startMixTranscode(config)
      } else {
        this.stopMixTranscode()
      }
    }
  },
  methods: {
    ...mapMutations({
      updateLiveStage: 'livePusher/updateLiveStage',
      updateActiveCamera: 'livePusher/updateActiveCamera',
      updateActiveMicrophone: 'livePusher/updateActiveMicrophone',
      updateActiveSpeaker: 'livePusher/updateActiveSpeaker'
    }),
    async initDevice() {
      // 获取设备列表并更新到全局
      const cameraList = await this.getCameras()
      const microphoneList = await this.getMicrophones()
      const speakerList = await this.getSpeakers()

      this.updateActiveCamera(cameraList[0] || {})
      this.updateActiveMicrophone(microphoneList[0] || {})
      this.updateActiveSpeaker(speakerList[0] || {})
    },
    // 初使化本地流并播放
    async initAndPlayStream() {
      let video = {}
      if (this.activeCameraId !== undefined) {
        video.video = !this.isVideoMuted
        video.cameraId = this.activeCameraId
      }
      this.localStream = await this.initLocalStream({
        userId: this.userId,
        audio: !this.isAudioMuted,
        ...video,
        microphoneId: this.activeMicrophoneId,
        mirror: this.isSetMirror
      })
      this.setVideoProfile(this.videoProfile)
      this.playStream(this.localStream, 'stream')
      if (this.isOpenBeauty) {
        this.initBeauty()
      }
      this.startGetAudioLevel()
    },
    // 开始直播
    async doStartLive() {
      // 倒计时结束后开始推流
      await this.handlePublish()
      // 延迟两秒发布流到指定CDN, 如果不需要发布流到指定CDN可以去掉
      // 由后台处理
      // setTimeout(async () => {
      //   await this.startPublishCDNStream({
      //     streamId: String(this.roomId),
      //   });
      // }, 2000);
    },
    // 暂停直播
    async doPauseLive() {
      this.muteAudio()
      this.muteVideo()
      await this.handleUnPublish()
      this.stopGetAudioLevel()
      this.destroyLocalStream()
      this.destroyBeauty()
    },
    // 继续直播
    async doGoOnLive() {
      await this.initAndPlayStream()
      await this.handlePublish()
    },
    // 停止直播
    async doStopLive() {
        console.log(this.courseType, '停止直播了？？');
        if (this.courseType === 'courseLive') {
             await this.$api.CourseLive.changeStatus({
                basicLiveShowId: this.basicLiveShowInfo.id,
                status: 2 // 直播状态 1直播中 2直播结束
            })
        } else {
            await this.$api.BasicLiveShow.changeStatus({
                basicLiveShowId: this.basicLiveShowInfo.id,
                status: 2 // 直播状态 1直播中 2直播结束
            })
        }
      await this.handleUnPublish()
      await this.handleLeave()
      this.stopGetAudioLevel()
      this.destroyLocalStream()
      this.destroyBeauty()
    },
    destroy() {
      this.updateLiveStage(LIVE_STAGE.NOT_STARTED)
    //   this.doStopLive()
      this.destroyLocalStream()
    }
  }
}
</script>

<style lang="stylus" scoped>
.stream-container
  background-color #000000
  overflow hidden

  .stream
    width 100%
    height 100%
    position absolute

  .rotateY-180
    & >>> div > video
      transform rotateY(180deg) !important

  .rotateY-0
    & >>> div > video
      transform rotateY(0deg) !important

  .tip-container
    position absolute
    width 100%
    height 100%
    background-size cover
    color NEUTRAL_COLOR_F
    font-weight bold
    font-size 18px
    letter-spacing 1px

    .tip
      position absolute
      top 40%
      left 50%
      transform translateX(-50%)

    .loading
      width 10px
      overflow hidden
</style>

<i18n>
{
  "en": {
    "The camera is turned off.": "The camera is turned off.",
    "Please turn your camera or mic on.": "Please turn your camera or mic on.",
    "Please check your camera.": "Please check your camera.",
    "Audio streaming": "Audio streaming",
    "Streaming paused": "Streaming paused",
    "Streaming ended": "Streaming ended"
  },
  "zh": {
    "The camera is turned off.": "摄像头已关闭",
    "Please turn your camera or mic on.": "请打开摄像头和麦克风",
    "Please check your camera.": "请检查摄像头是否安装",
    "Audio streaming": "语音直播中",
    "Streaming paused": "直播暂停中",
    "Streaming ended": "直播已结束"
  }
}
</i18n>
