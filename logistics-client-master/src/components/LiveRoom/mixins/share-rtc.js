/*
 * @Description: 屏幕分享
 * @Date: 2022-03-16 10:34:54
 * @LastEditTime: 2022-04-01 11:57:06
 */
import TRTC from 'trtc-js-sdk'

export default {
  data() {
    return {
      shareClient: null,
      shareLocalStream: null,
      isShareJoined: false,
      isSharePublished: false
    }
  },
  watch: {
    isSharePublished: {
      immediate: true,
      handler(val) {
        this.$emit('update:isSharePublished', val)
      }
    }
  },

  methods: {
    // 初始化屏幕分享
    initShareClient() {
      this.shareClient = TRTC.createClient({
        mode: 'rtc',
        sdkAppId: this.sdkAppId,
        userId: this.shareUserId,
        userSig: this.shareUserSig,
        autoSubscribe: false
      })
      this.addSuccessLog(`初始化屏幕分享 shareUserId:[${this.shareUserId}].`)
      this.onShareClientEvents()
    },

    // 初始化屏幕分享流
    async initShareLocalStream() {
      this.shareLocalStream = TRTC.createStream({
        screenAudio: false,
        screen: true,
        userId: this.shareUserId
      })
      this.shareLocalStream.setScreenProfile(this.screenProfile)
      try {
        await this.shareLocalStream.initialize()
        this.addSuccessLog(`初始化屏幕分享流 shareUserId:[${this.shareUserId}].`)
      } catch (error) {
        this.addFailedLog(`初始化屏幕分享流 Error: ${error.message}}.`)
        switch (error.name) {
          case 'NotReadableError':
            this.$emit('onShareError', '屏幕分享失败，请确保系统允许当前浏览器获取屏幕内容')
            throw error
          case 'NotAllowedError':
            if (error.message.includes('Permission denied by system')) {
              this.$emit('onShareError', '屏幕分享失败，请确保系统允许当前浏览器获取屏幕内容')
            } else {
              this.addSuccessLog(`用户拒绝分享屏幕`)
            }
            throw error
          default:
            return
        }
      }
      this.onShareStreamEvents()
    },

    // 销毁本地屏幕分享流
    destroyShareLocalStream() {
      this.shareLocalStream.stop()
      this.shareLocalStream.close()
      this.shareLocalStream = null
    },

    // 屏幕分享 进房
    async onShareJoin() {
      if (this.isShareJoined) {
        return
      }
      try {
        await this.shareClient.join({roomId: this.roomId})
        this.isShareJoined = true

        this.addSuccessLog(`屏幕分享 进房 shareUserId:[${this.shareUserId}]`)
      } catch (error) {
        console.log('shareRTC onJoin error', error)
        this.addFailedLog(`屏幕分享 进房 shareUserId:[${this.shareUserId}] ${error.message}.`)
      }
    },

    // 屏幕分享 client 发布屏幕分享流
    async onSharePublish() {
      if (this.isSharePublished) {
        return
      }
      try {
        await this.shareClient.publish(this.shareLocalStream)
        this.isSharePublished = true

        this.addSuccessLog('发布屏幕分享流')
      } catch (error) {
        this.addFailedLog(`发布屏幕分享流 ${error.message}.`)
      }
    },

    // 取消发布屏幕分享流
    async onShareUnpublish() {
      if (!this.isSharePublished) {
        return
      }
      try {
        await this.shareClient.unpublish(this.shareLocalStream)
        this.isSharePublished = false

        this.addSuccessLog('取消发布屏幕分享流')
      } catch (error) {
        console.log(`取消发布屏幕分享流, ${error.message}`)
        this.addFailedLog(`取消发布屏幕分享流, ${error.message}.`)
      }
    },

    // 屏幕分享 退房
    async onShareLeave() {
      if (!this.isShareJoined) {
        return
      }
      this.destroyShareLocalStream()
      try {
        await this.shareClient.leave()
        this.isShareJoined = false

        this.addSuccessLog('屏幕分享 退房')
      } catch (error) {
        console.log(`屏幕分享 退房, ${error.message}`)
        this.addFailedLog(`屏幕分享 退房, ${error.message}.`)
      }
    },

    onShareClientEvents() {
      this.shareClient.on('error', (error) => {
        console.error(error)
      })
      this.shareClient.on('client-banned', (error) => {
        console.error(`client has been banned for ${error}`)
      })
    },

    onShareStreamEvents() {
      this.shareLocalStream.on('player-state-changed', (event) => {
        console.log(`local stream ${event.type} player is ${event.state}`)
      })
      // 当用户通过浏览器自带的按钮停止屏幕分享时，会监听到 screen-sharing-stopped 事件
      this.shareLocalStream.on('screen-sharing-stopped', async () => {
        this.addSuccessLog('自带的按钮 停止屏幕分享')
        await this.onShareUnpublish()
        await this.onShareLeave()
        this.onSetMixUser && this.onSetMixUser([this.shareUserId])
      })
    }
  }
}
