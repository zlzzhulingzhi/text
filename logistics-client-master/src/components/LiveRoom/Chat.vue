<template>
  <div class="height-100p flex column" v-if="roomId">
    <!--聊天区域-->
    <div class="content-top-chat" ref="box">
      <div class="out" v-if="messageList.length === 0">快来互动吧</div>

      <div class="single-message" v-for="(message, index) in messageList" :key="index"
           :class="[message.nameCard,String(userInfo.userId) === message.userID ? 'self' : '']">
        <div class="message-info flex start-center">
          <el-image class="width-24 height-24 radius-100"
                    :src="message.avatar || require('@/assets/livePusher/avatar.png')"></el-image>
          <span class="padding-left-4 padding-right-4">{{ getUserNick(message) }}</span>
          <div class="name-tag">
            <span v-if="message.nameCard === 'Owner'">讲师</span>
            <span v-else-if="message.nameCard === 'Assistant'">助教</span>
            <span v-else>学员</span>
          </div>
          <!--          <span class="message-time">{{ getMessageTime(message) }}</span>-->
        </div>
        <div class="message-wrapper flex">
          <div class="message-content" v-for="(item, index) in message.renderContent" :key="index">
            <!--文字消息-->
            <span v-if="item.name === 'text'">{{ item.content }}</span>
            <!--表情消息-->
            <el-image class="message-icon" v-else-if="item.name === 'img'" :src="item.src"></el-image>
          </div>
        </div>
      </div>
    </div>

    <!--文字及表情输入区域-->
    <div class="content-bottom relative">
      <div class="height-32 bar padding-left-12 flex start-center">
        <!--表情选择-->
        <el-popover placement='top' width='400' trigger='click' v-model='popoverVisible'>
          <div class="emojis">
            <div class="emoji" v-for="emoji in emojiName" :key="emoji" @click="onChooseEmoji(emoji)">
              <el-image :src="emojiUrl + emojiMap[emoji]"></el-image>
            </div>
          </div>
          <div class="height-100p pointer" slot="reference">
            <el-image class="width-16 height-16" :src="require('@/assets/livePusher/icon_face.png')"></el-image>
          </div>
        </el-popover>
      </div>
      <el-input ref="input" type="textarea" v-model="inputMsg"
                placeholder="说点什么吧" resize="none"
                @keyup.enter="onSendMsg">
      </el-input>
      <el-button class="absolute width-48 height-28 padding-0" type="primary" @click="onSendMsg">发送</el-button>
    </div>
  </div>
</template>

<script>
import tim from './mixins/tim'
import {emojiMap, emojiName, emojiUrl} from './utils/emojiMap'

export default {
  name: 'Chat',
  mixins: [tim],
  beforeDestroy() {
    this.destroyChat()
  },
  props: {
    sdkAppId: {
      type: Number,
      required: true
    },
    roomId: Number,
    userInfo: {
      type: Object,
      default: true
    },
    members: {
      type: Object
    }
  },
  data() {
    return {
      inputMsg: '',
      popoverVisible: false,
      emojiMap,
      emojiName,
      emojiUrl
    }
  },
  watch: {
    // 发出一条新消息，自动到最底部
    messageList() {
      this.$nextTick(() => {
        const msg = this.$refs.box
        msg.scrollTop = msg.scrollHeight
      })
    }
  },
  methods: {
    // 获取用户昵称
    getUserNick({nick, userID}) {
      return nick ? nick : userID
    },
    getMessageTime({time}) {
      let hour = new Date(time * 1000).getHours()
      let minute = new Date(time * 1000).getMinutes()
      hour = hour >= 10 ? hour.toString() : `0${hour}`
      minute = minute >= 10 ? minute.toString() : `0${minute}`
      return `${hour}:${minute}`
    },
    // 操作 - 发送消息
    onSendMsg() {
      if (this.inputMsg === '' || /^\s+$/gi.test(this.inputMsg)) {
        return
      }
      this.sendMessage(this.inputMsg)
      this.inputMsg = ''
      this.popoverVisible = false
    },
    // 操作 - 选择表情
    onChooseEmoji(item) {
      this.inputMsg += item
      this.$refs.input.focus()
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

<style lang="stylus" scoped>
.content-top-chat
  flex 1
  width 100%
  padding 0 12px 10px
  overflow auto
  border-radius 10px
  color NEUTRAL_COLOR_3
  font-size 14px
  min-height 300px

  .out
    color NEUTRAL_COLOR_9

  .single-message
    width 100%
    text-align left

    &.self
      .message-info
        flex-direction row-reverse

      .message-wrapper
        justify-content flex-end

    &.Owner
      .message-info
        .name-tag
          background-color #5B81D2
          color #EBF2FF

        .message-wrapper
          .message-content
            background-color #1D61F2
            color #FFF

    &.Assistant
      .message-info
        .name-tag
          background-color #D5965A
          color #FFF6EE

    .message-info
      margin 16px 0 8px
      height 24px
      color #959A9F
      font-size 14px

      .name-tag
        width 32px
        height 16px
        line-height 16px
        border-radius 2px
        background-color #242731
        font-size 11px
        text-align center
        color #5B6064

        span
          transform scale(11 / 12)

    .message-content
      background-color rgba(223, 223, 223, 0.05);
      padding 3px 6px
      border-radius 4px
      font-size 13px
      word-break break-all
      line-height 18px
      color #959A9F

      span
        display inline-block
        vertical-align center

        .message-icon
          width 20px
          height 20px
          vertical-align middle

.divider
  width 100%
  height 2px
  background-color NEUTRAL_COLOR_9

.content-bottom
  width 100%

  .bar
    background-color #242731

  .el-textarea
    >>> .el-textarea__inner
      height 90px
      padding 16px
      color #FFF
      background-color #181A1F
      border none

  .absolute.el-button
    right 12px
    bottom 12px

.emojis
  height 170px
  overflow scroll

  .emoji
    height 30px
    width 30px
    float left
    box-sizing border-box

    img
      width 30px
      height 30px
</style>
