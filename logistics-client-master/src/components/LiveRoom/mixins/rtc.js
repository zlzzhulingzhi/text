/*
 * @Description: 音视频通话集成
 * @Date: 2022-03-14 17:15:23
 * @LastEditTime: 2022-03-23 17:47:14
 */
import TRTC from 'trtc-js-sdk'
import {isUndefined} from '../utils/utils.js'

/*
需要
sdkAppId
cameraId
microphoneId
roomId
userId
userSig
* */

export default {
  data() {
    return {
      client: null,
      localStream: null,
      remoteStreamList: [],
      mixUserIdList: [],
      isJoining: false,
      isJoined: false,
      isPublishing: false,
      isPublished: false,
      isMutedVideo: false,
      isMutedAudio: false,
      isPlayingLocalStream: false
    }
  },
  computed: {
    mixUsers() {
      return [...this.mixUserIdList, this.userId, ...this.remoteStreamList.map(item => item.getUserId())]
    }
  },
  watch: {
    mixUsers(val) {
      if (val.length === 1) {
        this.onStopMixTranscode().then()
      } else {
        this.onStartMixTranscode().then()
      }
      this.$emit('update:mixUsers', val)
    }
  },

  methods: {
    // 初始化客户端
    async initClient() {
      if (this.client) return false
      this.client = TRTC.createClient({
        mode: 'rtc',
        sdkAppId: this.sdkAppId,
        userId: this.userId,
        userSig: this.userSig
      })
      this.addSuccessLog(`初始化客户端 userId:[${this.userId}].`)
      this.handleClientEvents()
    },

    // 初始化本地流
    async initLocalStream() {
      this.destroyLocalStream()
      this.localStream = TRTC.createStream({
        userId: this.userId,

        audio: true,
        microphoneId: this.microphoneId || undefined,

        video: true,
        cameraId: this.cameraId || undefined
      })
      try {
        this.videoProfile && this.setVideoProfile(this.videoProfile)
        await this.localStream.initialize()
        this.addSuccessLog(`初始化本地流 userId:[${this.userId}].`)
      } catch (error) {
        this.localStream = null
        this.addFailedLog(`初始化本地流 Error: ${error.message}.`)
        throw error
      }
    },

    // 设置视频Profile
    setVideoProfile(option) {
      this.addSuccessLog(`设置视频Profile ${JSON.stringify(option)}.`)
      this.localStream && this.localStream.setVideoProfile(option)
    },

    // 播放本地流
    playLocalStream(domId) {
      console.log(this.mirror)
      this.localStream && this.localStream.play(domId, {
        mirror: this.mirror
      }).then(() => {
        this.isPlayingLocalStream = true
        this.addSuccessLog(`播放本地流 [${this.userId}].`)
      }).catch((error) => {
        this.addFailedLog(`播放本地流 [${this.userId}] Error: ${error.message}`)
      })
    },

    destroyLocalStream() {
      this.addSuccessLog(`销毁本地流`)
      this.localStream && this.localStream.stop()
      this.localStream && this.localStream.close()
      this.localStream = null
      this.isPlayingLocalStream = false
    },

    // 播放远程流
    playRemoteStream(remoteStream, element) {
      if (remoteStream.getType() === 'main' && remoteStream.getUserId().indexOf('share') >= 0) {
        remoteStream.play(element, {
          objectFit: 'contain'
        }).catch()
      } else {
        remoteStream.play(element, {}).catch()
      }
    },

    resumeStream(stream) {
      stream.resume()
    },

    // 加入房间
    async join() {
      if (this.isJoining || this.isJoined) {
        return
      }
      this.isJoining = true
      !this.client && await this.initClient()
      try {
        await this.client.join({roomId: this.roomId})
        this.isJoining = false
        this.isJoined = true

        this.addSuccessLog(`加入房间 roomId:[${this.roomId}].`)

        this.startGetAudioLevel()
      } catch (error) {
        this.isJoining = false
        console.error('join room failed', error)
        this.addFailedLog(`Join room ${this.roomId} failed, please check your params. Error: ${error.message}`)
        throw error
      }
    },

    // 发布流
    async publish() {
      if (!this.isJoined || this.isPublishing || this.isPublished) {
        return
      }
      this.isPublishing = true
      try {
        await this.client.publish(this.localStream)
        this.isPublishing = false
        this.isPublished = true

        this.addSuccessLog('发布流')
      } catch (error) {
        this.isPublishing = false
        console.error('发布流', error)
        this.addFailedLog(`发布流 Error: ${error.message}`)
        throw error
        return Promise.reject('发布流')
      }
    },

    // 取消发布流
    async unPublish() {
      if (!this.isPublished || this.isUnPublishing) {
        return
      }
      this.isUnPublishing = true
      try {
        await this.client.unpublish(this.localStream)
        this.isUnPublishing = false
        this.isPublished = false

        this.addSuccessLog('取消发布流')
      } catch (error) {
        this.isUnPublishing = false
        console.error('取消发布流', error)
        this.addFailedLog(`取消发布流 Error: ${error.message}`)
        throw error
      }
    },

    // 订阅远端流
    async subscribe(remoteStream, config = {audio: true, video: true}) {
      try {
        await this.client.subscribe(remoteStream, {
          audio: isUndefined(config.audio) ? true : config.audio,
          video: isUndefined(config.video) ? true : config.video
        })
        this.addSuccessLog(`订阅远端流 userId:[${remoteStream.getUserId()}].`)
      } catch (error) {
        console.error(`订阅远端流 ${remoteStream.getUserId()} 错误 audio: ${config.audio} video: ${config.video}`, error)
        this.addFailedLog(`订阅远端流 userId:${remoteStream.getUserId()}`)
      }
    },

    // 取消订阅远端流
    async unSubscribe(remoteStream) {
      try {
        await this.client.unsubscribe(remoteStream)
        this.addSuccessLog(`取消订阅远端流 userId:[${remoteStream.getUserId()}].`)
      } catch (error) {
        console.error(`取消订阅远端流 ${remoteStream.getUserId()} 错误`, error)
        this.addFailedLog(`取消订阅远端流 userId:${remoteStream.getUserId()}`)
      }
    },

    // 离开房间
    async leave() {
      if (!this.isJoined || this.isLeaving) {
        return
      }
      this.isLeaving = true
      this.stopGetAudioLevel()
      this.isPublished && await this.unPublish()
      this.localStream && this.destroyLocalStream()

      try {
        await this.client.leave()
        this.isLeaving = false
        this.isJoined = false

        this.addSuccessLog('离开房间')
      } catch (error) {
        this.isLeaving = false
        console.error('离开房间 错误', error)
        this.addFailedLog(`离开房间 Error: ${error.message}`)
        throw error
      }
    },

    // 禁用视频轨道
    muteVideo() {
      if (this.localStream) {
        this.localStream.muteVideo()
        this.isMutedVideo = true
        this.addSuccessLog('禁用视频轨道')
        if (this.mixUsers.length > 1) this.onStartMixTranscode().then()
      }
    },

    // 禁用音频轨道
    muteAudio() {
      if (this.localStream) {
        this.localStream.muteAudio()
        this.isMutedAudio = true
        this.addSuccessLog('禁用音频轨道')
      }
    },

    // 启用视频轨道
    unmuteVideo() {
      if (this.localStream) {
        this.localStream.unmuteVideo()
        this.isMutedVideo = false
        this.addSuccessLog('启用视频轨道')
        if (this.mixUsers.length > 1) this.onStartMixTranscode().then()
      }
    },

    // 启用音频轨道
    unmuteAudio() {
      if (this.localStream) {
        this.localStream.unmuteAudio()
        this.isMutedAudio = false
        this.addSuccessLog('启用音频轨道')
      }
    },

    // 切换 设备 - 音频、视频
    switchDevice(type, deviceId) {
      try {
        if (this.localStream) {
          this.localStream.switchDevice(type, deviceId)
          this.addSuccessLog(`切换 ${type} 设备`)
        }
      } catch (error) {
        console.error('切换 设备 失败', error)
        this.addFailedLog(`切换 ${type} 设备`)
      }
    },

    startGetAudioLevel() {
      this.addSuccessLog(`监听 远端流 音量`)
      // 文档：https://web.sdk.qcloud.com/trtc/webrtc/doc/zh-cn/module-ClientEvent.html#.AUDIO_VOLUME
      this.client.on('audio-volume', (event) => {
        event.result.forEach(({userId, audioVolume}) => {
          if (audioVolume > 2) {
            this.addSuccessLog(`user: ${userId} is speaking, audioVolume: ${audioVolume}`)
          }
        })
      })
      this.client.enableAudioVolumeEvaluation(200)
    },

    stopGetAudioLevel() {
      this.client && this.client.enableAudioVolumeEvaluation(-1)
    },

    handleClientEvents() {
      this.client.on('error', (error) => {
        console.error(error)
      })
      this.client.on('client-banned', async (error) => {
        this.isPublished = false
        this.localStream = null
        await this.leave()
      })
      // fired when a remote peer is joining the room
      this.client.on('peer-join', (event) => {
        const {userId} = event
        console.log(`peer-join ${userId}`, event)
      })
      // fired when a remote peer is leaving the room
      this.client.on('peer-leave', (event) => {
        const {userId} = event
        console.log(`peer-leave ${userId}`, event)
      })

      // 监听 远程流 新增
      this.client.on('stream-added', (event) => {
        const {stream: remoteStream} = event
        const remoteUserId = remoteStream.getUserId()
        this.addSuccessLog(`监听 远程流 新增 userId: [${remoteUserId}].`)
        if (new RegExp(`^.+_${this.userId}$`).test(remoteUserId)) {
          // 屏幕分享流、互动白板流
          if (new RegExp(`^share_${this.userId}$`).test(remoteUserId)) {
            // 订阅 - 屏幕分享流
            this.subscribe(remoteStream)
          } else {
            // 不订阅 - 互动白板流
            this.unSubscribe(remoteStream)
          }
        } else {
          // 订阅其他用户的流
          this.subscribe(remoteStream)
        }
      })

      // 监听 远程流 订阅
      this.client.on('stream-subscribed', (event) => {
        const {stream: remoteStream} = event
        const remoteUserId = remoteStream.getUserId()
        this.addSuccessLog(`监听 远程流 订阅 userId: [${remoteUserId}].`)
        if (!new RegExp(`^.+_${this.userId}$`).test(remoteUserId)) {
          this.remoteStreamList.push(remoteStream)
        }
        this.$nextTick(async () => {
          this.playRemoteStream(remoteStream, remoteUserId)
        })
      })
      // 监听 远程流 取消订阅
      this.client.on('stream-removed', async (event) => {
        const {stream: remoteStream} = event
        this.addSuccessLog(`监听 远程流 取消订阅 userId: [${remoteStream.getUserId()}] type: ${remoteStream.getType()}.`)
        remoteStream.stop()
        const index = this.remoteStreamList.indexOf(remoteStream)
        if (index >= 0) {
          this.remoteStreamList.splice(index, 1)
        }
      })

      // 监听 远程流 更新
      this.client.on('stream-updated', (event) => {
        const {stream: remoteStream} = event
        this.addSuccessLog(`监听 远程流 更新 userId: [${remoteStream.getUserId()}] audio:${remoteStream.hasAudio()}, video:${remoteStream.hasVideo()}.`)
      })

      this.client.on('mute-audio', (event) => {
        const {userId} = event
        console.log(`${userId} mute audio`)
        this.addSuccessLog(`[${event.userId}] mute audio.`)
      })
      this.client.on('unmute-audio', (event) => {
        const {userId} = event
        console.log(`${userId} unmute audio`)
        this.addSuccessLog(`[${event.userId}] unmute audio.`)
      })
      this.client.on('mute-video', (event) => {
        const {userId} = event
        console.log(`${userId} mute video`)
        this.addSuccessLog(`[${event.userId}] mute video.`)
      })
      this.client.on('unmute-video', (event) => {
        const {userId} = event
        console.log(`${userId} unmute video`)
        this.addSuccessLog(`[${event.userId}] unmute video.`)
      })

      this.client.on('connection-state-changed', (event) => {
        console.log(`RtcClient state changed to ${event.state} from ${event.prevState}`)
      })

      this.client.on('network-quality', (event) => {
        const {uplinkNetworkQuality, downlinkNetworkQuality} = event
        this.addSuccessLog(`network-quality uplinkNetworkQuality: ${uplinkNetworkQuality}, downlinkNetworkQuality: ${downlinkNetworkQuality}`)
        this.$emit('update:uplinkNetworkQuality', uplinkNetworkQuality)
        this.$emit('update:downlinkNetworkQuality', downlinkNetworkQuality)
      })
    },

    // 操作 - 混流
    async onStartMixTranscode() {
      this.addSuccessLog(`混流 ${this.mixUsers}`)
      let {width, height} = this.screenProfile
      let mixUserIds = this.mixUsers
      let mixUsers = []
      // 剔除无视频轨道的 直播流（讲师）
      if (this.isMutedVideo) {
        mixUserIds = this.mixUsers.filter(id => id !== this.userId)
        mixUsers.push({
          pureAudio: true,
          streamType: 'main',
          userId: this.userId,
          locationX: 0,
          locationY: 0,
          zOrder: 1
        })
      }

      // 除了讲师直播流，小于2个流
      if (mixUserIds.length <= 2) {
        mixUsers.push({
          pureAudio: false,
          width,
          height,
          locationX: 0,
          locationY: 0,
          streamType: 'main',
          zOrder: 1,
          userId: mixUserIds[0]
        })

        // 右下角
        if (mixUserIds[1]) {
          mixUsers.push({
            pureAudio: false,
            width: Math.floor(width * 0.15),
            height: Math.floor(height * 0.15),
            locationX: Math.floor(width * 0.85),
            locationY: Math.floor(height * 0.85),
            streamType: 'main',
            zOrder: 2,
            userId: mixUserIds[1]
          })
        }
      } else {
        let mainSize = 0.85
        let subSize = 1 - mainSize
        let subWidth = Math.floor(width * subSize - 32)
        let subHeight = Math.floor(height * subSize - (height / width * 32))
        let subX = Math.floor(width * mainSize + 16)
        let baseSubY = Math.floor(height - (mixUserIds.length - 1) * subHeight - (mixUserIds.length - 2) * 16) / 2
        mixUsers.push(...mixUserIds.map((userId, index) => {
          // 第一个(主流)
          if (!index) {
            return {
              pureAudio: false,
              width: Math.floor(width * mainSize),
              height: Math.floor(height * mainSize),
              locationX: 0,
              locationY: Math.floor(height * subSize / 2),
              streamType: 'main',
              zOrder: 1,
              userId
            }
          }
          // 其他流
          return {
            pureAudio: false,
            width: subWidth,
            height: subHeight,
            locationX: subX,
            locationY: baseSubY + (index - 1) * (subHeight + 16),
            streamType: 'main',
            zOrder: 2,
            userId
          }
        }))
      }

      await this.client.startMixTranscode({
        mode: 'manual',
        videoWidth: width,
        videoHeight: height,
        videoBitrate: this.videoProfile.bitrate,
        videoFramerate: this.videoProfile.frameRate,
        mixUsers,
        backgroundImage: this.backgroundImage || ''
      })
    },

    // 操作 - 停止混流
    async onStopMixTranscode() {
      this.addSuccessLog(`停止混流 ${this.mixUsers}`)
      if (!this.client) return false
      await this.client.stopMixTranscode()
    }
  }
}
