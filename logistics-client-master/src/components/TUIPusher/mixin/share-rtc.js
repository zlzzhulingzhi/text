import TRTC from 'trtc-js-sdk'
import i18n from '@/locales/i18n'

let logStyle = 'background: #9D66F7;color:#103c99;line-height:24px'

export default {
  data() {
    return {
      shareClient: null,
      localStream: null
    }
  },
  methods: {
    async initClient() {
      console.log(`%c 初始化分享屏幕客户端`, logStyle)
      this.shareClient = TRTC.createClient({
        mode: 'live',
        sdkAppId: this.sdkAppId,
        userId: this.shareUserId,
        userSig: this.shareUserSig,
        autoSubscribe: false
      })
      this.handleClientEvents()
      return this.shareClient
    },

    async createStream() {
      console.log(`%c 创建分享屏幕流`, logStyle)
      const localStream = TRTC.createStream({
        audio: false,
        screen: true,
        userId: this.shareUserId
      })
      localStream.setScreenProfile(this.screenProfile)
      try {
        await localStream.initialize()
      } catch (error) {
        const systemDenyInfo = i18n.t('rtc.Screen sharing failed') + i18n.t('rtc.Ensure that your browser has access to the screen')
        switch (error.name) {
          case 'NotReadableError':
            this.$alert(systemDenyInfo, 'ERROR', {
              confirmButtonText: i18n.t('common.Sure')
            })
            throw error
          case 'NotAllowedError':
            if (error.message === 'Permission denied by system') {
              this.$alert(systemDenyInfo, 'ERROR', {
                confirmButtonText: i18n.t('common.Sure')
              })
            } else {
              // console.log('User refused to share the screen')
            }
            throw error
          default:
            return
        }
      }
      return localStream
    },

    async initLocalStream() {
      this.localStream = await this.createStream()
      this.handleStreamEvents()
    },

    destroyLocalStream() {
      this.localStream.stop()
      this.localStream.close()
    },

    async handleJoin() {
      if (this.isJoined) {
        return
      }
      await this.initClient()
      try {
        await this.initLocalStream()
        console.log(`%c 进房 房间号 ${this.roomId}`, logStyle)
        await this.shareClient.join({roomId: this.roomId})
        this.isJoined = true
        this.handlePublish()
      } catch (error) {
        // console.log('shareRTC handleJoin error = ', error)
        throw error
      }
    },

    async handlePublish() {
      if (this.isPublished) {
        return
      }
      console.log(`%c 发布流`, logStyle)
      await this.shareClient.publish(this.localStream)
      this.isPublished = true
    },

    async handleUnPublish() {
      if (!this.isPublished) {
        return
      }
      console.log(`%c 取消发布流`, logStyle)
      await this.shareClient.unpublish(this.localStream)
      this.isPublished = false
    },

    async handleLeave() {
      if (!this.isJoined) {
        return
      }
      this.destroyLocalStream()
      if (this.isPublished) {
        this.handleUnPublish()
      }
      console.log(`%c 退房`, logStyle)
      await this.shareClient.leave()
      this.isJoined = false
    },

    playStream(stream, dom) {
      stream.play(dom, {objectFit: 'contain'}).catch()
    },

    handleClientEvents() {
      this.shareClient.on('error', (error) => {
        console.log(`%c 监听错误`, logStyle)
        console.error(error)
        alert(error)
      })
      this.shareClient.on('client-banned', (error) => {
        console.log(`%c 监听客户端被禁`, logStyle)
        console.error(`client has been banned for ${error}`)
      })
    },

    handleStreamEvents() {
      this.localStream.on('player-state-changed', (event) => {
        console.log(`%c 监听播放状态改变 [${event.state}}]`, logStyle, event)
      })

      // 当用户通过浏览器自带的按钮停止屏幕分享时，会监听到 screen-sharing-stopped 事件
      this.localStream.on('screen-sharing-stopped', () => {
        console.log(`%c 监听停止屏幕分享`, logStyle)
        this.stopScreenShare()
      })
    }
  }
}

