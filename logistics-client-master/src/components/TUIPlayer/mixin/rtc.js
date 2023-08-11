import TRTC from 'trtc-js-sdk'
import {isUndefined} from '@/components/TUIPlayer/utils/utils'
import i18n from '@/locales/i18n'
// 更新音量大小

const logStyle = 'background: #C8FFFA; color: #103C99;line-height:24px'

export default {
  data() {
    return {
      isJoining: false,
      isJoined: false,
      isLeaving: false,
      uplinkNetworkQuality: null,
      downlinkNetworkQuality: null,
    }
  },
  methods: {
    // 初始化客户端
    async initClient() {
      console.log('%c 初始化客户端', logStyle)
      const clientOption = {
        sdkAppId: this.sdkAppId,
        userId: this.userId,
        userSig: this.userSig,
        mode: 'live',
        useStringRoomId: false,
        frameWorkType: 34
      }

      this.client = TRTC.createClient(clientOption)
      this.handleClientEvents()
      return this.client
    },

    // 处理client监听事件
    handleClientEvents() {
      this.client.on('error', (error) => {
        console.log('%c 监听错误', logStyle)
        this.$alert(error, 'ERROR', {
          confirmButtonText: i18n.t('common.Sure')
        })
      })
      this.client.on('client-banned', (error) => {
        console.log('%c 监听客户端被禁', logStyle)
        console.error(`client has been banned for ${error}`)
        this.$alert(i18n.t('rtc.A user with the same name entered'), 'ERROR', {
          confirmButtonText: i18n.t('common.Sure')
        })
      })
      // fired when a remote peer is joining the room
      this.client.on('peer-join', (event) => {
        const {userId} = event
        console.log(`%c 监听用户${userId}进入了房间`, logStyle)
      })
      // fired when a remote peer is leaving the room
      this.client.on('peer-leave', (event) => {
        const {userId} = event
        console.log(`%c 监听用户${userId}离开了房间`, logStyle, event)
        this.removeUser && this.removeUser(userId)
      })

      // fired when a remote stream is added
      this.client.on('stream-added', (event) => {
        const {stream: remoteStream} = event
        const remoteUserID = remoteStream.getUserId()
        console.log(`%c 监听有新增的流[${remoteUserID}]`, logStyle, remoteStream)
        this.handleSubscribe(remoteStream)
      })
      // fired when a remote stream has been subscribed
      this.client.on('stream-subscribed', (event) => {
        const {stream: remoteStream} = event
        console.log(`%c 监听有订阅的流 [${remoteStream.getUserId()}]`, logStyle, remoteStream)
        this.handleSubscribedStream(remoteStream)
      })
      this.client.on('stream-removed', (event) => {
        const {stream: remoteStream} = event
        console.log(`%c 监听有流被移除 [${remoteStream.getUserId()}]`, logStyle, event)
        this.handleStreamRemoved(remoteStream)
      })
      this.client.on('stream-updated', (event) => {
        const {stream: remoteStream} = event
        console.log(`%c 监听流更新 [${remoteStream.getUserId()}] 
        视频状态：${remoteStream.hasVideo()} 
        音频状态：${remoteStream.hasAudio()} `, logStyle, remoteStream)
      })
      this.client.on('mute-audio', (event) => {
        const {userId} = event
        console.log(`%c 监听主播${userId}关闭了 音频`, logStyle)
      })
      this.client.on('unmute-audio', (event) => {
        const {userId} = event
        console.log(`%c 监听主播${userId}打开了 音频`, logStyle)
      })
      this.client.on('mute-video', (event) => {
        const {userId} = event
        console.log(`%c 监听主播${userId}关闭了 视频`, logStyle)
      })
      this.client.on('unmute-video', (event) => {
        const {userId} = event
        console.log(`%c 监听主播${userId}打开了 视频`, logStyle)
      })

      this.client.on('connection-state-changed', (event) => {
        console.log(`%c 监听Rtc客户端状态 [${event.prevState}]==>[${event.state}]`, logStyle)
      })

      this.client.on('network-quality', (event) => {
        const {uplinkNetworkQuality, downlinkNetworkQuality} = event
        console.log(`%c 监听上传质量: ${uplinkNetworkQuality}\n下载质量: ${downlinkNetworkQuality}`, logStyle)
        this.uplinkNetworkQuality = uplinkNetworkQuality
        this.downlinkNetworkQuality = downlinkNetworkQuality
      })
    },

    // 初始化本地流
    async initLocalStream({audio, video, userId, cameraId, microphoneId, mirror}) {
      const localStream = TRTC.createStream({
        audio,
        video,
        userId,
        cameraId,
        microphoneId,
        mirror: mirror !== false
      })
      await localStream.initialize()
      return localStream
    },

    // 播放流
    playStream(stream, dom, options = {}) {
      stream.play(dom, options).catch()
      stream.on('error', (error) => {
        console.error(error)
        const errorCode = error.getCode()
        if (errorCode === 0x4043) {
          // 自动播放失败，需要引导用户产生交互
          this.handleStreamPlayForbidden(stream)
        }
      })
    },

    // 销毁本地流
    destroyLocalStream() {
      this.localStream && this.localStream.stop()
      this.localStream && this.localStream.close()
      this.localStream = null
    },

    // 进房
    async handleJoin(role = 'anchor') {
      if (this.isJoining || this.isJoined) {
        return
      }
      this.isJoining = true
      await this.initClient()
      console.log(`%c 进房 房间号${this.roomId}`, logStyle)
      try {
        await this.client.join({roomId: this.roomId, role})
        this.$message.success(i18n.t('rtc.Entered room successfully'))

        this.isJoining = false
        this.isJoined = true
      } catch (error) {
        this.isJoining = false
        this.$message.error(i18n.t('rtc.Failed to enter room'))
        console.error('join room failed', error)
      }
    },

    // 退房
    async handleLeave() {
      if (!this.isJoined || this.isLeaving) {
        return
      }
      this.isLeaving = true
      try {
        await this.client.leave()
        this.isLeaving = false
        this.isJoined = false
      } catch (error) {
        this.isLeaving = false
        console.error('leave room error', error)
        this.$message.error(i18n.t('rtc.Failed to leave room'))
      }
    },


    // 订阅
    async handleSubscribe(remoteStream, config = {audio: true, video: true}) {
      try {
        await this.client.subscribe(remoteStream, {
          audio: isUndefined(config.audio) ? true : config.audio,
          video: isUndefined(config.video) ? true : config.video
        })
      } catch (error) {
        console.error(`subscribe ${remoteStream.getUserId()} with audio: ${config.audio} video: ${config.video} error`, error)
        this.$message.error(`subscribe ${remoteStream.getUserId()} failed!`)
      }
    },

    // 取消订阅
    async handleUnSubscribe(remoteStream) {
      try {
        await this.client.unsubscribe(remoteStream)
      } catch (error) {
        console.error(`unsubscribe ${remoteStream.getUserId()} error`, error)
        this.$message.error(`unsubscribe ${remoteStream.getUserId()} failed!`)
      }
    }
  }
}
