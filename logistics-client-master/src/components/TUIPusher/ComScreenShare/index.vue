<template>
  <el-card>
    <div slot="header">{{ $t('Screen Sharing') }}</div>
    <div class="width-100p padding-bottom-16 text-center">
      <!--未开始屏幕分享-->
      <div class="text-center" v-show="!isScreenSharing">
        <el-image class="bg-background radius-8 width-100" :src="screenShareImg"></el-image>
        <div class="line-height-32 font-14">
          {{ $t('Select content to share') }}
        </div>
        <el-button size="small" @click="startScreenShare" :type="!isLiveOngoing ? 'info' : 'primary'"
                   :disabled="!isLiveOngoing">
          {{ $t('Share') }}
        </el-button>
      </div>
      <!--      <div v-show="isLiveOngoing && isScreenSharing">-->
      <div v-show="isScreenSharing">
        <!--正在进行屏幕分享-->
        <div v-show="isLiveOngoing" id="share-stream" class="width-100p height-120"></div>
        <!--屏幕分享暂停中-->
        <div v-show="isLivePaused" class="font-14 width-100p height-120">
          {{ $t('Screen sharing paused') }}
        </div>

        <div class="margin-top-12">
<!--          <el-button size="small" type="primary" @click="replaceScreenShare" :disabled="isLivePaused">
            {{ $t('Change Content') }}
          </el-button>-->
          <el-button size="small" type="primary" @click="stopScreenShare" :disabled="isLivePaused">
            {{ $t('Stop Sharing') }}
          </el-button>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
import shareRTC from '@/components/TUIPusher/mixin/share-rtc'
import screenShareImg from '@/assets/livePusher/screenShare.png'
import {LIVE_STAGE} from '@/components/TUIPusher/constants/room'
import {mapGetters, mapMutations, mapState} from 'vuex'

export default {
  name: 'ComScreenShare',
  data() {
    return {
      screenShareImg,
      isScreenSharing: false
    }
  },
  mixins: [shareRTC],
  computed: {
    ...mapGetters({
      sdkAppId: 'livePusher/sdkAppId',
      roomId: 'livePusher/roomId',
      shareUserId: 'livePusher/anchorShareUserId',
      shareUserSig: 'livePusher/anchorShareUserSig'
    }),
    ...mapState('livePusher', {
      liveStage: 'liveStage',
      screenProfile: 'screenProfile'
    }),
    isLiveOngoing() {
      return this.liveStage === LIVE_STAGE.ONGOING
    },
    isLivePaused() {
      return this.liveStage === LIVE_STAGE.PAUSED
    }
  },
  watch: {
    liveStage(val, oldVal) {
      if (val === LIVE_STAGE.PAUSED) {
        this.handleUnPublish()
        this.localStream && this.localStream.stop()
      }
      if (oldVal === LIVE_STAGE.PAUSED) {
        if (this.localStream) {
          this.playStream(this.localStream, 'share-stream')
          this.handlePublish()
        }
      }
      if (val === LIVE_STAGE.ENDED) {
        this.stopScreenShare()
      }
    }
  },
  methods: {
    ...mapMutations({
      updateIsScreenSharing: 'livePusher/updateIsScreenSharing'
    }),
    initScreenShare({sdkAppId, shareUserId, shareUserSig, streamId, enable = false}) {
      this.sdkAppId = sdkAppId
      this.shareUserId = shareUserId
      this.shareUserSig = shareUserSig
      this.roomId = streamId
      this.enable = enable
    },
    async startScreenShare() {
      if (!this.isLiveOngoing) {
        return
      }
      await this.handleJoin()
      this.playStream(this.localStream, 'share-stream')
      this.isScreenSharing = true
      this.updateIsScreenSharing(true)
    },
    pauseScreenShare() {
      this.handleUnPublish()
      this.localStream && this.localStream.stop()
    },
    async replaceScreenShare() {
      const stream = await this.createStream()
      const videoTrack = stream.getVideoTrack()
      this.localStream.replaceTrack(videoTrack)
    },
    async stopScreenShare() {
      await this.handleLeave()
      this.isScreenSharing = false
      this.updateIsScreenSharing(false)
    }
  }
}
</script>

<style lang="stylus" scoped>
.height-120
  height 120px
  line-height 120px
</style>

<i18n>
{
  "en": {
    "Screen Sharing": "Screen Sharing",
    "Select content to share": "Select content to share",
    "Share": "Share",
    "Change Content": "Change Content",
    "Stop Sharing": "Stop Sharing",
    "Screen sharing paused": "Screen sharing paused"
  },
  "zh": {
    "Screen Sharing": "共享屏幕",
    "Select content to share": "选择你要共享的区域",
    "Share": "开始共享",
    "Change Content": "新的共享",
    "Stop Sharing": "停止共享",
    "Screen sharing paused": "屏幕分享暂停中"
  }
}
</i18n>
