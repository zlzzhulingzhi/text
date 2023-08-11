<template>
  <el-card class="width-100p height-100p">
    <div slot="header">{{ $t('Chat') }}</div>

    <!--聊天区域-->
    <div class="content-top-chat" ref="box">
      <div class="message-wrap">
        <div class="out" v-if="messageList.length === 0">{{ $t('Chat now') }}</div>

        <div class="single-message" v-for="(message, index) in messageList" :key="index">
          <div class="message-info">
            <span class="user-name">{{ getUserNick(message) }}</span>
            <span class="message-time">{{ getMessageTime(message) }}</span>
          </div>
          <div class="message-content">
            <div v-for="(item, index) in message.renderContent" :key="index">
              <!--文字消息-->
              <span v-if="item.name === 'text'">{{ item.content }}</span>
              <!--表情消息-->
              <el-image class="message-icon" v-else-if="item.name === 'img'" :src="item.src"></el-image>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="divider"></div>

    <!--文字及表情输入区域-->
    <div class="content-bottom">
      <!--文字输入-->
      <div class="content-bottom-input">
        <el-input type="text" enterkeyhint="send" class="input" ref="input" v-model="inputMsg" :placeholder="$t('Type a message')"
                  @keyup.enter.native="handleSendMsg" size="small">
          <!--表情选择-->
          <el-popover slot="prepend" placement='top' width='400' trigger='click' v-model='popoverVisible'>
            <div class="emojis">
              <div class="emoji" v-for="emoji in emojiName" :key="emoji" @click="chooseEmoji(emoji)">
                <el-image :src="emojiUrl + emojiMap[emoji]"></el-image>
              </div>
            </div>
            <el-button slot="reference" type="default">
              <SvgIcon class="width-24 height-24" icon-name="emoji"></SvgIcon>
            </el-button>
          </el-popover>

          <el-button v-if="!isH5" slot="append" @click="handleSendMsg">{{ $t('Send') }}</el-button>
        </el-input>
      </div>
    </div>
  </el-card>
</template>

<script>
import tim from '@/components/TUIPlayer/mixin/tim'
import {emojiMap, emojiName, emojiUrl} from '@/components/TUIPlayer/utils/emojiMap'
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'ComIm',
  mixins: [tim],
  created() {
    window.addEventListener('beforeunload', () => {
      this.destroy()
    })
  },
  beforeDestroy() {
    this.destroy()
  },
  data() {
    return {
      inputMsg: '',
      rec: '',
      popoverVisible: false,
      emojiMap,
      emojiName,
      emojiUrl
    }
  },
  computed: {
    ...mapState('live', {
      sdkAppId: 'sdkAppId'
    }),
    ...mapGetters({
      userSig: 'live/userSig',
      isH5: 'isH5',
    }),
  },
  watch: {
    // 发出一条新消息，自动到最底部
    messageList() {
      console.log('messageList change')
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
    // 发送消息
    handleSendMsg() {
      if (this.inputMsg === '' || /^\s+$/gi.test(this.inputMsg)) {
        return
      }
      this.sendMessage(this.inputMsg)
      this.inputMsg = ''
      this.popoverVisible = false
    },
    // 选择表情
    chooseEmoji(item) {
      this.inputMsg += item
      this.$refs.input.focus()
    },
    async destroy() {
      await this.quitGroup()
      await this.logout()
      await this.handleTimEventsOff()
    }
  }
}
</script>

<style lang="stylus" scoped>
>>> .el-input-group__prepend
  padding 0
  width 40px
  text-align center

>>> .el-input-group__append
  padding 0 8px

.el-card
  display flex
  flex-direction column
  border-radius 0

  >>> .el-card__body
    flex 1
    display flex
    flex-direction column
    overflow hidden

.content-top-chat
  flex 1
  width 100%
  margin 10px 0
  overflow auto
  border-radius 10px
  color NEUTRAL_COLOR_3
  font-size 14px

  .out
    color NEUTRAL_COLOR_9

  .single-message
    width 100%
    text-align left

    .message-info
      height 30px
      line-height 30px
      color NEUTRAL_COLOR_3
      font-size 14px

      .user-name
        padding-right 12px

    .message-content
      width 80%
      min-width 260px
      background-color rgba(223, 223, 223, 0.05);
      padding 8px 12px
      border-radius 4px
      font-size 16px
      font-weight 500
      word-break break-all

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
  padding-bottom 12px

  div.content-bottom-input
    text-align left
    position relative
    margin 4px auto 0

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

<i18n>
{
  "en": {
    "Chat now": "Chat now！",
    "Type a message": "Type a message",
    "Chat": "Chat",
    "Send": "Send"
  },
  "zh": {
    "Chat now": "快来互动吧！",
    "Type a message": "说点什么",
    "Chat": "互动消息",
    "Send": "发送"
  }
}
</i18n>
