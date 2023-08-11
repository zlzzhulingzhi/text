<template>
  <el-card>
    <div slot="header">
      <span>{{ $t('Participants') }}</span>
      <span v-if="groupInfo.memberCount">:{{ groupInfo.memberCount }}</span>
    </div>
    <div class="watch-list-content">
      <div class="watch-list-member">
        <div class="user-item" v-for="(member, index) in memberList" :key="index">
          <!--显示用户头像和昵称信息-->
          <div class="user-info flex">
            <el-avatar class="width-24 height-24 margin-right-8" :src="getUserAvatar(member)"></el-avatar>
            <el-tooltip effect="light" class="item flex-1" :content="getUserNick(member)" placement="bottom" :visible-arrow='false'>
              <span class="user-name">{{ getUserNick(member) }}</span>
            </el-tooltip>
          </div>

          <!--显示用户是否被禁言状态-->
          <div class="user-state" v-if="member.userID !== userInfo.userId">
            <span class="icon" @click="toggleMuteStatus(member)">
              <el-tooltip class="item" effect="light" :content="$t('Mute')" placement="top">
                <SvgIcon icon-name="info" v-show="!member.isMuted"></SvgIcon>
              </el-tooltip>
              <el-tooltip class="item" effect="light" :content="$t('Unmute')" placement="top">
                <SvgIcon icon-name="info-forbidden" v-show="member.isMuted"></SvgIcon>
              </el-tooltip>
            </span>
          </div>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
import {mapState} from 'vuex'

export default {
  name: 'ComParticipants',
  data() {
    return {
      defaultAvatar: require('@/assets/livePusher/avatar.png')
    }
  },
  computed: {
    ...mapState('livePusher', {
      userInfo: 'anchorUserInfo',
      memberList: 'memberList',
      groupInfo: 'groupInfo'
    })
  },
  methods: {
    // 获取用户头像
    getUserAvatar(userInfo) {
      return userInfo.avatar ? userInfo.avatar : this.defaultAvatar
    },
    // 获取用户昵称
    getUserNick(userInfo) {
      return userInfo.nick ? userInfo.nick : userInfo.userID
    },
    // 切换禁言状态
    toggleMuteStatus(userInfo) {
      const muteTime = userInfo.isMuted ? 0 : 7 * 24 * 60 * 60

      this.$EventBus.$emit('tim:setGroupMemberMuteTime', {
        userID: userInfo.userID,
        muteTime
      })
    }
  }
}
</script>

<style lang="stylus" scoped>
.card-container
  width 100%
  height 36%
  display flex
  flex-direction column

.watch-list-content
  flex-grow 1
  width 100%
  margin 10px 0
  height 200px
  border-radius 10px
  padding 0 14px;

.watch-list-member
  color $fontColor
  width 100%
  height 100%
  display flex
  flex-direction column
  overflow auto
  font-size 14px

  .user-item
    display flex
    flex-direction row
    margin 0 0 8px 0
    justify-content space-between
    align-items center
    width 100%

    .user-info

      .user-name
        display inline-block
        max-width 210px
        height 24px
        line-height 24px
        text-align left
        white-space nowrap
        width 100%
        overflow hidden
        text-overflow ellipsis

    .user-state
      height 20px

      .icon
        display inline-block
        width 20px
        height 20px
        cursor pointer
</style>

<i18n>
{
  "en": {
    "Participants": "Participants",
    "Mute": "Mute",
    "Unmute": "Unmute"
  },
  "zh": {
    "Participants": "在线观众",
    "Mute": "禁言",
    "Unmute": "取消禁言"
  }
}
</i18n>
