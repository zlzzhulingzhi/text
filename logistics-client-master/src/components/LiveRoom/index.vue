<!--
 * @Description: 房间显示
 * @Date: 2022-03-16 17:40:28
 * @LastEditTime: 2022-03-29 16:13:06
-->
<template>
  <div class="rtc-container flex" v-if="roomId">

    <!--主体内容-->
    <div class="live-area flex-1 flex center-center overflow">
      <template v-if="liveStatus === 'NoStart' || liveStatus === 'Playing'">
        <div class="stream-container" :id="shareUserId" v-show="isSharePublished"></div>
        <slot name="Playing"></slot>
      </template>
      <template v-else-if="liveStatus === 'Pause'">
        <div class="text-center">
          <div class="text-f margin-bottom-16">直播已暂停或已中断</div>
          <div class="text-95">请点击左上角&lt;继续直播&gt;或&lt;结束直播&gt;</div>
        </div>
      </template>
      <template v-else-if="liveStatus === 'Finish'">
        <div class="text-center">
          <el-image class="margin-bottom-16 width-72 height-72 overflow radius-100 flex"
                    :src="require('@/assets/livePusher/live_end.png')"></el-image>
          <div class="text-f text-bold font-18">直播结束</div>
        </div>
      </template>
    </div>

    <!--侧边信息栏-->
    <div class="info-bar overflow-auto flex column shrink-0">
      <div class="stream-list relative">
        <!-- 本地流播放区域 -->
        <div :id="eleId" class="local-stream stream-wrapper"></div>

        <div class="stream-wrapper absolute flex column end-start">
          <div>
            <SvgIcon className="padding-4 pointer" iconName="audio-muted1" v-if="isMutedAudio"
                     @click.native="unmuteAudio"></SvgIcon>
            <SvgIcon className="padding-4 pointer" iconName="audio1" v-else @click.native="muteAudio"></SvgIcon>

            <SvgIcon className="padding-4 pointer" iconName="video-muted1" v-if="isMutedVideo"
                     @click.native="unmuteVideo"></SvgIcon>
            <SvgIcon className="padding-4 pointer" iconName="video1" v-else @click.native="muteVideo"></SvgIcon>
            <!--            <span class="iconfont icon&#45;&#45;quxiaoyuyin" v-if="isMutedAudio" @click="unmuteAudio"></span>
                        <span class="iconfont icon-yuyin" v-else @click="muteAudio"></span>

                        <span class="iconfont icon-shexiangtou-guanbi" v-if="isMutedVideo" @click="unmuteVideo"></span>
                        <span class="iconfont icon-shexiangtou" v-else @click="muteVideo"></span>-->
          </div>
        </div>

        <!-- 远端流区域 -->
        <div v-for="(item) in remoteStreamList"
             :key="item.getUserId()"
             :id="item.getUserId()"
             class="remote-stream stream-wrapper">
        </div>
      </div>

      <slot name="infoBar"></slot>
    </div>
  </div>
</template>

<script>
import rtc from './mixins/rtc.js'
import shareRtc from './mixins/share-rtc.js'

export default {
  name: 'LiveRoom',
  mixins: [rtc, shareRtc],
  props: {
    // 客户的唯一标识
    sdkAppId: Number,

    // 直播房间ID
    roomId: Number,

    // 用户ID
    userId: String,
    // 用户签名
    userSig: String,
    // 视频参数
    videoProfile: {
      type: Object,
      default() {
        return {
          width: 1920,
          height: 1080,
          frameRate: 15,
          bitrate: 2000
        }
      }
    },

    // 屏幕分享 - 用户ID
    shareUserId: String,
    // 屏幕分享 - 用户签名
    shareUserSig: String,
    // 屏幕分享参数
    screenProfile: {
      type: Object,
      default() {
        return {
          width: 1920,
          height: 1080,
          frameRate: 5,
          bitrate: 1600
        }
      }
    },

    // 面板分享 - 用户ID
    boardUserId: String,

    // 设备 - 摄像头
    cameraId: String,
    // 设备 - 麦克风
    microphoneId: String,
    mirror: {
      type: Boolean,
      default: true
    },
    backgroundImage: String
  },
  data() {
    return {
      inviteLink: '',
      showCopiedTip: false,

      eleId: 'LocalStream',
      // liveStatus : NoStart | Playing | Pause | Finish
      liveStatus: 'NoStart'
    }
  },
  watch: {
    cameraId(val) {
      this.switchDevice('video', val)
    },
    microphoneId(val) {
      this.switchDevice('audio', val)
    },
    videoProfile(val) {
      this.setVideoProfile(val)
    }
  },
  methods: {
    // 操作 - 初始化房间
    async onInitRoom() {
      if (!this.roomId) return false
      // 初始化客户端
      await this.initClient()
      // 初始化本地流
      await this.initLocalStream()
      // 播放本地流
      await this.playLocalStream(this.eleId)

      // 操作 - 加入房间
      await this.join()
    },

    // 操作 - 开始直播
    async onStart() {
      // 操作 - 发布流
      await this.publish()

      this.$emit('onStart')
      this.liveStatus = 'Playing'
    },

    // 操作 - 暂停直播
    async onPause() {
      await this.unPublish()
      this.$emit('onPause')
      this.liveStatus = 'Pause'
    },

    // 操作 - 结束直播
    async onStop() {
      // 操作 - 停止发布流
      await this.unPublish()
      // 操作 - 离开房间
      await this.leave()
      this.$emit('onStop')
      this.liveStatus = 'Finish'
    },


    // 操作 - 屏幕分享
    async onStartScreenShare() {
      await this.initShareClient()
      await this.initShareLocalStream()
      await this.onShareJoin()
      await this.onSharePublish()


      // 操作 - 混流
      await this.onSetMixUser([this.shareUserId])
    },

    // 操作 - 停止屏幕分享
    async onStopScreenShare() {
      await this.onShareUnpublish()
      await this.onShareLeave()
    },

    onSetMixUser(list = []) {
      this.mixUserIdList = list
    },

    // 显示成功的 Log
    addSuccessLog(log) {
      let logStyle = 'background: #28a745;color:#f8f9fa;line-height:24px'
      console.log(`%c${log}`, logStyle)
    },

    // 显示失败的 Log
    addFailedLog(log) {
      let logStyle = 'background: #dc3545;color:#f8f9fa;line-height:24px'
      console.log(`%c${log}`, logStyle)
    }
  }
}
</script>

<style scoped lang="stylus">
.rtc-container

  .live-area
    width 100%
    height 100%
    background-color #111 /*    .el-icon-warning
      font-size 72px
      transform scale(1.2)*/

    .text-95
      color #959A9F

    .stream-container
      height 100%
      background-color #959A9F

  .info-bar
    width 240px
    background-color #181A1F

    .stream-wrapper
      width 240px
      height 140px

      &.absolute
        top 0
        left 0

        .iconfont
          padding 4px
</style>