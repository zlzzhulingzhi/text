<template>
  <div>
    <div>
      <h2>直播端demo</h2>
      <LiveRoom></LiveRoom>
    </div>

    <div>
      <h2>直播播放端demo</h2>
      <div class="flex start-center">
        <div class="width-100">basicLiveId 直播数据ID</div>
        <el-input-number v-model="basicLiveId"></el-input-number>
      </div>

      <div class="padding-8">
        <el-button @click="onTUIPlayerInit(basicLiveId)">初始化播放器</el-button>
      </div>

      <TUIPlayer ref="TUIPlayer" class="width-100p" :style="{height:$utils.FullViewHeight(300)}"></TUIPlayer>
    </div>

    <div>
      <h2>快直播播放demo</h2>
      <div class="flex start-center">
        <div class="width-100">webrtc 地址</div>
        <el-input v-model="webrtc"></el-input>
      </div>
      <div class="padding-8">
        <el-button @click="onInitPlayerLeb">初始化播放器</el-button>
        <el-button @click="onPlayLeb">播放</el-button>
      </div>
      <div class="video-wrapper">
        <ComLebStream ref="ComLebStream" :controls="'default'"></ComLebStream>
      </div>
    </div>

    <div>
      <h2>标准直播播放demo</h2>
      <div class="flex start-center">
        <div class="width-100">m3u8 地址</div>
        <el-input v-model="m3u8"></el-input>
      </div>
      <div class="flex start-center">
        <div class="width-100">flv 地址</div>
        <el-input v-model="flv"></el-input>
      </div>
      <div class="padding-8">
        <el-button @click="onInitPlayerCdn">初始化播放器</el-button>
        <el-button @click="onPlayCdn">播放</el-button>
      </div>

      <div class="video-wrapper">
        <ComCdnStream ref="ComCdnStream" :controls="'default'"></ComCdnStream>
      </div>
    </div>
  </div>
</template>

<script>
import TUIPlayer from '@/components/TUIPlayer'
import LiveRoom from './LiveRoom'
import {mapMutations} from 'vuex'

export default {
  name: 'TestPlayer',
  components: {
    TUIPlayer,
    LiveRoom
  },
  data() {
    return {
      basicLiveId: 7,

      webrtc: null,

      m3u8: 'https://1253118816.vod2.myqcloud.com/95c5c2c6vodcq1253118816/dbd71c84387702294321115152/playlist_eof.m3u8',
      flv: null
    }
  },
  methods: {
    ...mapMutations({
      updateUserInfo: 'live/updateUserInfo',
      updateRoomId: 'live/updateRoomId'
    }),

    async onTUIPlayerInit(basicLiveId) {
      let {code, data} = await this.$api.BasicLiveShow.info({
        basicLiveId
      })
      if (code !== 200) return false

      this.updateRoomId(data.roomId)
      this.updateUserInfo({
        userId: String(data.userId),
        userName: data.userName,
        userSig: data.userSig
      })

      this.$refs.TUIPlayer.onInitPlayer({
        webrtc: data.pullUdpUrl,
        m3u8: data.pullM3u8Url,
        flv: data.pullFlvUrl,
        basicLiveId,
        userId: data.userId,
        orgId: data.orgId,
      })
    },

    onInitPlayerLeb() {
      this.$refs.ComLebStream.onInitPlayer({
        webrtc: this.webrtc
      })
    },
    onPlayLeb() {
      this.$refs.ComLebStream.onPlay()
    },

    onInitPlayerCdn() {
      this.$refs.ComCdnStream.onInitPlayer({
        m3u8: this.m3u8,
        flv: this.flv
      })
    },
    onPlayCdn() {
      this.$refs.ComCdnStream.onPlay()
    }
  }
}
</script>

<style scoped lang="stylus">
.video-wrapper
  width 100%
  height 450px
</style>