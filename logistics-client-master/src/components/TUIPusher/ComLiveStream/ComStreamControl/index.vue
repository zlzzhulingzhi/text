<!--
 * @Description: 推流控制组件
 * @Date: 2021-11-01 14:44:42
 * @LastEditTime: 2022-04-27 20:01:14
-->
<template>
  <div class="stream-control-container">
    <!--头部控制栏-->
    <div class="header-container">
      <div class="left-container">
        <ComRoomName></ComRoomName>
      </div>
      <div class="right-container">
        <ComEndBtn></ComEndBtn>
      </div>
    </div>

    <!--倒计时区域-->
    <div class="center-container">
      <div class="start-animation" v-if="countdown > 0">
        <div class="number">{{ countdown }}</div>
      </div>
    </div>

    <!--底部控制栏-->
    <div class="footer-container">
      <div class="left-container">
        <ComAudio></ComAudio>
        <ComVideo></ComVideo>
      </div>
      <div class="right-container">
        <ComNetworkQuality></ComNetworkQuality>
        <ComRoomTime></ComRoomTime>
        <!-- <ComRoomShare></ComRoomShare> -->
        <el-button v-if="liveStage === LIVE_STAGE.NOT_STARTED" type="primary" @click="startLive">
          {{ $t('Start') }}
        </el-button>
        <el-button v-if="liveStage === LIVE_STAGE.ONGOING" type="primary" @click="pauseLive">
          {{ $t('Pause') }}
        </el-button>
        <el-button v-if="liveStage === LIVE_STAGE.PAUSED" type="primary" @click="goOnLive">
          {{ $t('Resume') }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import {LIVE_STAGE} from '@/components/TUIPusher/constants/room'
import {mapState} from 'vuex'
import ComRoomName from './ComRoomName'
import ComEndBtn from './ComEndBtn'
import ComAudio from './ComAudio'
import ComVideo from './ComVideo'
import ComNetworkQuality from './ComNetworkQuality'
import ComRoomShare from './ComRoomShare'
import ComRoomTime from './ComRoomTime'


export default {
  name: 'ComStreamControl',
  components: {
    ComRoomName,
    ComEndBtn,
    ComAudio,
    ComVideo,
    ComNetworkQuality,
    ComRoomShare,
    ComRoomTime
  },
   props: {
      courseType: {
          tyoe: String,
          default: ''
      }
  },
  data() {
    return {
      LIVE_STAGE,
      countdown: 0
    }
  },
  computed: {
    ...mapState('livePusher', {
      liveStage: 'liveStage',
      basicLiveShowInfo: 'basicLiveShowInfo'
    })
  },
  methods: {
    // 播放开始直播动画
    playStartAnimation() {
      this.countdown = 3
      const interval = setInterval(() => {
        this.countdown = this.countdown - 1
        if (this.countdown < 0) {
          clearInterval(interval)
        }
      }, 1000)
    },
    // 开始直播
    async startLive() {
      if (this.isAudioMuted && this.isVideoMuted) {
        this.$message.warning(this.$t('Please turn your camera or mic on.'))
        return
      }
      this.playStartAnimation()
      setTimeout(() => {
        this.$store.commit('livePusher/updateLiveStage', LIVE_STAGE.ONGOING)
        if (this.courseType === 'courseLive') {
            this.$api.CourseLive.changeStatus({
                basicLiveShowId: this.basicLiveShowInfo.id,
                status: 1 // 直播状态 1直播中 2直播结束
            })
        } else {
            this.$api.BasicLiveShow.changeStatus({
                basicLiveShowId: this.basicLiveShowInfo.id,
                status: 1 // 直播状态 1直播中 2直播结束
            })
        }
        
      }, 3000)
    },
    // 暂停直播
    pauseLive() {
      this.$store.commit('livePusher/updateLiveStage', LIVE_STAGE.PAUSED)
    },
    // 继续直播
    goOnLive() {
      if (this.isAudioMuted && this.isVideoMuted) {
        this.$message.warning(this.$t('Please turn your camera or mic on.'))
        return
      }
      this.$store.commit('livePusher/updateLiveStage', LIVE_STAGE.ONGOING)
    }
  }
}
</script>

<style lang="stylus" scoped>
.stream-control-container
  width 100%
  height 100%
  position relative
  display flex
  flex-direction column
  justify-content space-between

  .header-container
    position relative
    width 100%
    height 50px
    background-color $backgroundColor
    padding 0 4px 0 10px
    display flex
    justify-content space-between
    align-items center

    .left-container
    .right-container
      height 100%
      display flex
      align-items center

  .center-container
    flex-grow 1

    .start-animation
      position absolute
      top 0
      left 0
      width 100%
      height 100%
      background-color rgba(33, 33, 38, 0.4)
      z-index 99

      .number
        position absolute
        top 40%
        left 50%
        transform translateY(-50%)
        font-size 80px
        color #ffffff

  .footer-container
    position relative
    width 100%
    height 60px
    background-color rgba(0, 0, 0, 0.7)
    display flex
    align-items center
    justify-content space-between
    padding 0 20px

    .left-container
      display flex
      align-items center

    .right-container
      display flex
      align-items center
</style>

<i18n>
{
  "en": {
    "Start": "Start",
    "Pause": "Pause",
    "Resume": "Resume",
    "Please turn your camera or mic on.": "Please turn your camera or mic on."
  },
  "zh": {
    "Start": "开始直播",
    "Pause": "暂停直播",
    "Resume": "继续直播",
    "Please turn your camera or mic on.": "请开启摄像头或麦克风"
  }
}
</i18n>
