<template>
  <div>
    <div class="flex start-center">
      <div class="width-100">basicLiveId 直播数据ID</div>
      <el-input-number v-model="liveId"></el-input-number>
    </div>

    <div class="padding-8">
      <el-button @click="getRoomInfo">初始化直播</el-button>
    </div>

    <div class="width-100p bg-background padding-16" :style="{height:$utils.FullViewHeight(300)}">
      <TUIPusher v-if="roomVisible"></TUIPusher>
    </div>
  </div>
</template>

<script>
import TUIPusher from '@/components/TUIPusher'
import {mapMutations} from 'vuex'

export default {
  name: 'LiveDetail',
  components: {
    TUIPusher
  },
  data() {
    return {
      formatRule: 'yyyy-MM-DD HH:mm:ss',
      liveId: 7,
      roomInfo: {},
      providerTypeCode: 'txkzb', // txzb 腾讯直播 , txkzb 腾讯快直播
      roomVisible: false
    }
  },
  watch: {
    liveId:{
      immediate:true,
      handler(val){
        this.$route.params.id = val
      }
    }
  },
  methods: {
    ...mapMutations({
      updateRoomId: 'livePusher/updateRoomId',
      updateAnchorUserInfo: 'livePusher/updateAnchorUserInfo',
    }),
    async getRoomInfo() {
      let {code, data} = await this.$api.BasicLiveShow.info({
        basicLiveId: this.liveId
      })
      if (code !== 200) return false
      if (!data.id) return await this.createRoom()
      await this.updateRoom(data.id)
    },
    async createRoom() {
      let {code: c1, data: d1} = await this.$api.BasicLive.detail({
        id: this.liveId
      })
      if (c1 !== 200) return false

      let {code, data} = await this.$api.BasicLiveShow.create({
        basicLiveId: this.liveId,
        actualStartTime: this.$moment().format(this.formatRule),
        endTime: d1.endTime,
        liveRoomType: 1,
        providerTypeCode: this.providerTypeCode
      })

      if (code !== 200) return false
      await this.setRoomInfo(data)
    },
    async updateRoom(id) {
      let {code, data} = await this.$api.BasicLiveShow.update({
        id,
        actualStartTime: this.$moment().format(this.formatRule),
        providerTypeCode: this.providerTypeCode
      })
      if (code !== 200) return false
      await this.setRoomInfo(data)
    },
    async setRoomInfo(data) {
      this.roomInfo = data
      this.updateRoomId(data.roomId)
      let shareUserId = `share-${data.userId}`
      let {data: shareUserSig} = await this.$api.LiveCommon.customUserSig({
        liveRoomType: 1,
        providerTypeCode: this.providerTypeCode,
        userId: shareUserId
      })
      this.updateAnchorUserInfo({
        userId: String(data.userId),
        userName: data.userName,
        userAvatar: data.userAvatar,
        userSig: data.userSig,
        shareUserId,
        shareUserSig
      })

      this.roomVisible = true
    }
  }
}
</script>

<style scoped lang="stylus">
</style>