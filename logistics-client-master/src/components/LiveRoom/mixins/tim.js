import TIM from 'tim-js-sdk'
import {decodeText} from '../utils/utils'

// 聊天系统 【群主】

export default {
  data() {
    return {
      // 是否是群主
      isOwner: true,

      tim: null,
      isSdkReady: false,
      // 群信息
      groupProfile: null,
      // 消息列表
      messageList: [],
      // 群成员列表
      memberList: [],
      // 被禁言的用户ID列表
      muteUserIdList: []
    }
  },
  computed: {
    groupID() {
      return this.roomId.toString()
    }
  },
  watch: {
    groupProfile(val) {
      this.$emit('update:groupProfile', val)
    },
    messageList(val) {
      this.$emit('update:messageList', val)
    },
    memberList(val) {
      this.$emit('update:memberList', val)
    },
    muteUserIdList(val) {
      this.$emit('update:muteUserIdList', val)
    }
  },
  methods: {
    // 阶段 - 初始化，创建tim实例
    async initTim() {
      if (!this.roomId) return false
      this.tim = TIM.create({
        SDKAppID: this.sdkAppId
      })
      this.tim.setLogLevel(0)

      this.handleTimEvents()
      await this.loginTim()
    },
    // 阶段 - 销毁群聊
    async destroyChat() {
      if (this.isOwner) {
        await this.dismissGroup()
      } else {
        await this.quitGroup()
      }
      await this.logout()
      this.handleTimEventsOff()
    },


    // 阶段 - 使用 用户ID(userID) 和 签名串(userSig) 登录即时通信 IM
    async loginTim() {
      try {
        await this.tim.login({
          userID: this.userInfo.userId,
          userSig: this.userInfo.userSig
        })
        this.addSuccessLog(`登录聊天室 userId:[${this.userInfo.userId}]`)
      } catch (error) {
        this.addFailedLog(`登录聊天室 Error: ${error.message}.`)
        throw error
      }
    },
    // 阶段 - 登出im
    async logout() {
      this.addSuccessLog(`登出聊天室`)
      try {
        await this.tim.logout()
      } catch (error) {
        this.addFailedLog(`登出聊天室 Error: ${error.message}.`)
      }
    },

    // 阶段 - 监听事件
    handleTimEvents() {
      this.tim.on(TIM.EVENT.SDK_READY, this.SDK_READY)
      this.tim.on(TIM.EVENT.MESSAGE_RECEIVED, this.MESSAGE_RECEIVED)

      this.tim.on(TIM.EVENT.SDK_NOT_READY, this.SDK_NOT_READY)
      this.tim.on(TIM.EVENT.KICKED_OUT, this.KICKED_OUT)
      this.tim.on(TIM.EVENT.ERROR, this.ERROR)
    },
    // 阶段 - 解绑事件
    handleTimEventsOff() {
      this.tim.off(TIM.EVENT.SDK_READY, this.SDK_READY)
      this.tim.off(TIM.EVENT.MESSAGE_RECEIVED, this.MESSAGE_RECEIVED)

      this.tim.off(TIM.EVENT.SDK_NOT_READY, this.SDK_NOT_READY)
      this.tim.off(TIM.EVENT.KICKED_OUT, this.KICKED_OUT)
      this.tim.off(TIM.EVENT.ERROR, this.ERROR)
    },

    // 监听事件 - 登录成功后会触发 SDK_READY 事件，该事件触发后，可正常使用 SDK 接口
    async SDK_READY({name}) {
      this.addSuccessLog(`聊天室 SDK_READY ${name}`)
      if (name === TIM.EVENT.SDK_READY) {
        this.isSdkReady = true
        // 获取个人资料
        this.getMyProfile()
        // 修改个人标配资料
        try {
          await this.tim.updateMyProfile({
            nick: this.userInfo.userName || '',
            avatar: this.userInfo.userAvatar || '',
            gender: TIM.TYPES.GENDER_FEMALE,
            selfSignature: '',
            allowType: TIM.TYPES.ALLOW_TYPE_ALLOW_ANY
          })
        } catch (error) {
          this.addFailedLog(`修改个人标配资料 Error: ${error.message}.`)
        }
        await this.joinGroup()
        if (this.isOwner) {
          await this.onSetMemberOwner()
        } else {
          if (this.members.assistant.includes(this.userInfo.userId)) await this.onSetMemberAssistant()
        }
      }
    },
    // 监听事件 - 收到新消息
    MESSAGE_RECEIVED(event) {
      let messageList = event.data
      messageList.forEach((message) => {
        this.addSuccessLog(`聊天室 MESSAGE_RECEIVED ${message.type}`)
        console.log(message)
        // 监听加群消息
        if (message.type === TIM.TYPES.MSG_GRP_TIP && message.payload.operationType === TIM.TYPES.GRP_TIP_MBR_JOIN) {
          this.handleJoinGroupTip(message)
        }
        // 监听退群消息
        if (message.type === TIM.TYPES.MSG_GRP_TIP && message.payload.operationType === TIM.TYPES.GRP_TIP_MBR_QUIT) {
          this.handleQuitGroupTip(message)
        }
        // 监听文本消息
        if (message.type === TIM.TYPES.MSG_TEXT) {
          this.handleMessageTip(message)
        }

        // 自定义消息【行为】
        if (message.type === TIM.TYPES.MSG_CUSTOM) {
          if (message.payload.data === 'GET_MY_PROFILE') return this.getMyProfile()
          if (message.payload.data === 'LIVE_CONNECT') return this.onLiveConnect()
          if (message.payload.data === 'LIVE_CONNECT_CLOSE') return this.onLiveConnectClose()
          if (/^SYNC_LIVE_STATUS_/.test(message.payload.data)) return this.onSyncLiveStatus(message.payload.data)
        }
      })
    },

    // 监听事件 - 文本消息
    async handleMessageTip(message) {
      this.messageList.push({
        ...message,
        nick: message.nick || message.from,
        content: message.payload.text,
        renderContent: decodeText(message.payload.text),
        userID: message.from,
        avatar: message.avatar,
        time: message.time
      })
    },
    // 监听事件 - 加群消息
    async handleJoinGroupTip(message) {
      let {payload: {operatorID}} = message

      await this.getGroupMemberList()
      await this.getGroupProfile()
    },
    // 监听事件 - 退群消息
    async handleQuitGroupTip(message) {
      this.memberList = this.memberList.filter(member => member.userID !== message.payload.operatorID)
      await this.getGroupProfile()
    },


    // 监听事件 - SDK未就绪
    SDK_NOT_READY() {
      this.addSuccessLog(`聊天室 SDK_NOT_READY`)
    },
    // 监听事件 - 被踢出
    KICKED_OUT() {
      this.addSuccessLog(`聊天室 KICKED_OUT`)
    },
    // 监听事件 - SDK内部出错
    ERROR() {
      this.addSuccessLog(`聊天室 ERROR`)
    },

    // ----------- 群组相关 ------------
    // 阶段 - 加入直播群
    async joinGroup() {
      // 判断群组是否存在
      try {
        // 存在的情况
        await this.tim.searchGroupByID(this.groupID)
      } catch (error) {
        // 不存在的情况
        this.addSuccessLog(`搜索直播群 Error: ${error.message}.`)
        if (this.isOwner) await this.createGroup()
      }

      try {
        this.addSuccessLog(`加入直播群 ${this.groupID}`)
        await this.tim.joinGroup({
          groupID: this.groupID,
          type: TIM.TYPES.GRP_AVCHATROOM
        })

        await this.getGroupMemberList()
        await this.getGroupProfile()
      } catch (error) {
        this.addFailedLog(`加入直播群 Error: ${error.message}.`)
      }
    },
    // 阶段 - 创建群组
    async createGroup() {
      try {
        this.addSuccessLog(`创建直播群 ${this.groupID}`)
        await this.tim.createGroup({
          type: TIM.TYPES.GRP_AVCHATROOM,
          // todo: name不传
          name: 'avchatroom',
          groupID: this.groupID
        })
      } catch (error) {
        this.addFailedLog(`创建直播群 Error: ${error.message}.`)
      }
    },
    // 退群
    async quitGroup() {
      try {
        this.addSuccessLog(`退群 ${this.groupID}`)
        await this.tim.quitGroup(this.groupID)
      } catch (error) {
        this.addFailedLog(`退群 Error: ${error.message}.`)
      }
    },
    // 阶段 - 解散群组
    async dismissGroup() {
      try {
        this.addSuccessLog(`解散直播群 ${this.groupID}`)
        await this.tim.dismissGroup(this.groupID)
      } catch (error) {
        this.addFailedLog(`解散直播群 Error: ${error.message}.`)
      }
    },

    // ----------- 群成员相关 ----------
    // 获取 - 群成员信息
    async getGroupMemberList() {
      try {
        this.addSuccessLog(`获取群成员信息`)
        let {data} = await this.tim.getGroupMemberList({
          groupID: this.groupID,
          count: 30,
          offset: 0
        })

        this.memberList = data.memberList
      } catch (error) {
        this.addFailedLog(`获取群成员信息 Error: ${error.message}.`)
      }
    },
    // 获取 - 在线人数
    async getGroupProfile() {
      this.addSuccessLog(`获取在线人数`)
      let {data} = await this.tim.getGroupProfile({
        groupID: this.groupID,
        groupCustomFieldFilter: []
      })
      this.groupProfile = data.group
    },
    // 操作 - 设置群成员名片
    async onMemberCard(nameCard = null) {
      this.tim.setGroupMemberNameCard({
        groupID: this.groupID,
        nameCard
      }).then(imResponse => {
        this.addSuccessLog(`设置群成员名片 【${imResponse.data.member.nameCard}】`)
      }).catch(error => {
        this.addFailedLog(`设置群成员名片 【${nameCard}】 Error: ${error.message}.`)
      })
    },
    // 操作 - 设置群成员名片【群主】
    async onSetMemberOwner() {
      await this.onMemberCard('Owner')
    },
    // 操作 - 设置群成员名片【助教】
    async onSetMemberAssistant() {
      await this.onMemberCard('Assistant')
      // await this.onCallGetMyProfile(userID)
    },

    // 操作 - 发送消息
    async sendMessage(msgText) {
      // 判断sdk的状态
      if (!this.isSdkReady) return false
      // 创建消息并发送到对应群组
      let message = await this.tim.createTextMessage({
        to: this.groupID,
        conversationType: TIM.TYPES.CONV_GROUP,
        payload: {
          text: msgText
        }
      })
      try {
        this.addSuccessLog(`发送消息`)
        console.log(message)
        await this.tim.sendMessage(message)
        // 发送成功
      } catch (error) {
        this.addFailedLog(`登出聊天室 Error: ${error.message}.`)
      }
      // 放入消息列表
      this.messageList.push({
        ...message,
        nick: message.nick || '',
        content: message.payload.text,
        renderContent: decodeText(message.payload.text),
        userID: message.from,
        time: Number(Date.now().toString().substr(0, 10))
      })
    },

    // 工具 - 发送行为
    async onCallAction(toUserID, payload) {
      // 判断sdk的状态
      if (!this.isSdkReady) return false
      // 点对点发送信息
      let message = this.tim.createCustomMessage({
        to: toUserID,
        conversationType: TIM.TYPES.CONV_C2C,
        payload
      })

      try {
        this.addSuccessLog(`发送行为【${payload.description}】`)
        console.log(message)
        await this.tim.sendMessage(message)
        // 发送成功
      } catch (error) {
        this.addFailedLog(`发送行为【${payload.description}】 Error: ${error.message}.`)
      }
    },

    // 工具 - 发送群体行为
    async onCallGroupAction(payload) {
      // 判断sdk的状态
      if (!this.isSdkReady) return false
      // 点对点发送信息
      let message = this.tim.createCustomMessage({
        to: this.groupID,
        conversationType: TIM.TYPES.CONV_GROUP,
        payload
      })

      try {
        this.addSuccessLog(`发送行为【${payload.description}】`)
        console.log(message)
        await this.tim.sendMessage(message)
        // 发送成功
      } catch (error) {
        this.addFailedLog(`发送行为【${payload.description}】 Error: ${error.message}.`)
      }
    },

    // 操作 - 发送行为 - 获取个人资料
    async onCallGetMyProfile(toUserID) {
      this.addSuccessLog(`发送行为 - 获取个人资料`)
      await this.onCallAction(toUserID, {
        data: 'GET_MY_PROFILE',
        description: '获取个人资料',
        extension: ''
      })
    },
    // 操作 - 执行行为 - 获取个人资料
    getMyProfile() {
      this.addSuccessLog(`执行行为 - 获取个人资料`)
      this.tim.getMyProfile()
    },

    // 操作 - 发送行为 - 调起连麦
    async onCallLiveConnect(toUserID) {
      this.addSuccessLog(`发送行为 - 调起连麦`)
      await this.onCallAction(toUserID, {
        data: 'LIVE_CONNECT',
        description: '调起连麦',
        extension: ''
      })
    },
    // 操作 - 执行行为 - 调起连麦
    onLiveConnect() {
      this.addSuccessLog(`执行行为 - 调起连麦`)
      this.$emit('LiveConnect')
    },

    // 操作 - 发送行为 - 关闭连麦
    async onCallLiveConnectClose(toUserID) {
      this.addSuccessLog(`发送行为 - 关闭连麦`)
      await this.onCallAction(toUserID, {
        data: 'LIVE_CONNECT_CLOSE',
        description: '关闭连麦',
        extension: ''
      })
    },
    // 操作 - 执行行为 - 关闭连麦
    onLiveConnectClose() {
      this.addSuccessLog(`执行行为 - 关闭连麦`)
      this.$emit('LiveConnectClose')
    },

    // 操作 - 发送行为 - 更新直播状态
    async onCallSyncLiveStatus(liveStatus) {
      this.addSuccessLog(`发送行为 - 更新直播状态`)
      await this.onCallGroupAction({
        data: `SYNC_LIVE_STATUS_${liveStatus}`,
        description: '更新直播状态',
        extension: ''
      })
    },
    // 操作 - 执行行为 - 更新直播状态
    onSyncLiveStatus(data) {
      this.addSuccessLog(`执行行为 - 更新直播状态`)
      this.$emit('SyncLiveStatus', (data || '').replace('SYNC_LIVE_STATUS_',''))
    },


    // 操作 - 发送行为 - 同步白板数据
    async onSyncBoardData(data, retry = 3) {
      if (retry <= 0) return false
      let message = await this.tim.createCustomMessage({
        to: this.groupID,
        conversationType: TIM.TYPES.CONV_GROUP,
        priority: TIM.TYPES.MSG_PRIORITY_HIGH,  // 因为im消息有限频，白板消息的优先级调整为最高
        payload: {
          data: JSON.stringify(data),
          description: '',
          extension: 'TXWhiteBoardExt'
        }
      })

      this.tim.sendMessage(message).then(() => {
        this.addSuccessLog(`发送行为 - 同步白板数据`)
      }).catch(err => {
        this.addFailedLog(`发送行为 - 同步白板数据 retry:${retry}`)
        this.onSyncBoardData(data, retry - 1)
      })
    },

    // 操作 - 设置禁言
    async setGroupMemberMuteTime({userID, muteTime}) {
      try {
        this.addSuccessLog(`设置禁言 ${userID}`)
        await this.tim.setGroupMemberMuteTime({
          groupID: this.groupID,
          userID,
          muteTime // 设为0，则表示取消禁言
        })
        let member = this.memberList.find(item => item.userID === userID)
        member.isMuted = muteTime !== 0
        if (muteTime > 0 && this.muteUserIdList.indexOf(userID) < 0) {
          this.muteUserIdList.push(userID)
        } else {
          this.muteUserIdList = this.muteUserIdList.filter(muteUserID => muteUserID !== userID)
        }
      } catch (error) {
        this.addFailedLog(`设置禁言 Error: ${error.message}.`)
      }
    }
  }
}
