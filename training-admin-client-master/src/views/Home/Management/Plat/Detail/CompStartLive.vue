<template>
  <div>
    <el-row :gutter="20" v-if="permissions.Live">
      <el-col :span="8">
        <span class="font-14 text-bold">方式一：自己开播</span>
        <div class="flex column start-center">
          <el-image class="padding-32" :src="require('@/assets/live/img_1.png')"></el-image>
          <span class="font-16 text-6 margin-left-64 margin-right-64">适合使用笔记本或安装摄像头的台式电脑的个人用户直播</span>
          <span class="margin-top-64"><el-button type="primary" plain size="small" :disabled="!liveId"
                                                 @click="goToLive">开始直播</el-button></span>
        </div>
      </el-col>
      <el-col :span="8">
        <span class="font-14 text-bold">方式二：第三方推流直播间</span>
        <div class="flex column start-center">
          <el-image class="padding-32" :src="require('@/assets/live/img_2.png')"></el-image>
          <span class="font-16 text-6 margin-left-64 margin-right-64">适合用于转播其他平台推流直播</span>
          <div class="flex center-center margin-top-64">
            <span class="font-14 width-100">服务器：</span>
            <el-input v-model="liveData.pushObsUrl" size="small" disabled>
              <el-button slot="append" @click="copyLink(liveData.pushObsUrl)">复制</el-button>
            </el-input>
          </div>
          <div class="flex center-center margin-top-16">
            <span class="font-14 width-100">串流密钥：</span>
            <el-input v-model="liveData.pushObsSecret" size="small" disabled>
              <el-button slot="append" @click="copyLink(liveData.pushObsSecret)">复制</el-button>
            </el-input>
          </div>
          <div class="flex center-center margin-top-16">
            <span class="font-14 width-100">直播地址：</span>
            <el-input v-model="liveData.pushRtmpUrl" size="small" disabled>
              <el-button slot="append" @click="copyLink(liveData.pushRtmpUrl)">复制</el-button>
            </el-input>
          </div>
          <div class="flex center-center margin-top-16">
            <span class="font-14 width-100">低延时直播地址：</span>
            <el-input v-model="liveData.pushWebrtcUrl" size="small" disabled>
              <el-button slot="append" @click="copyLink(liveData.pushWebrtcUrl)">复制</el-button>
            </el-input>
          </div>
          <div class="flex center-center margin-top-16">
            <span class="font-14 width-100">拉流地址：</span>
            <el-input v-model="liveData.pullRtmpUrl" size="small" disabled>
              <el-button slot="append" @click="copyLink(liveData.pullRtmpUrl)">复制</el-button>
            </el-input>
          </div>
          <span class="margin-top-32"><el-button type="primary" plain size="small" :disabled="!liveId"
                                                 @click="goThirdLive">获取服务器和密钥</el-button></span>
        </div>
      </el-col>
      <el-col :span="8">
        <span class="font-14 text-bold">方式三：客户端直播</span>
        <div class="flex column start-center">
          <el-image class="padding-32" :src="require('@/assets/live/img_1.png')"></el-image>
          <span class="font-16 text-6 margin-left-64 margin-right-64">适合使用相机、采集卡等较为专业的大型直播场景</span>
          <span class="margin-top-64"><el-button type="primary" plain size="small"
                                                 @click="downloadClient">下载客户端</el-button></span>
        </div>
      </el-col>
    </el-row>
    <Results type="Disabled" :imageSize="220" v-else></Results>
  </div>
</template>

<script>
export default {
  props: {
    liveId: {
      type: [String, Number]
    }
  },
  data() {
    return {
      providerTypeCode: 'txzb',
      liveData: {}
    }
  },
  computed: {
    permissions() {
      return this.$route.meta.childPermissions || {}
    }
  },
  methods: {
    // 进入直播间
    goToLive() {
      //   this.$router.push({ name: 'LiveRoom', params: { id: this.liveId, liveType: 'courseLive' } })
      this.$router.push({
        name: 'CourseLiveRoom',
        params: {id: this.liveId},
        query: {courseId: String(this.$route.query.id)}
      })

      this.liveData = {}
    },
    // 初始化第三方直播
    async goThirdLive() {
      if (Object.keys(this.liveData).length) return this.$message.warning('无需重复获取')
      //   let {code, data} = await this.$api.BasicLiveShow.info({
      //     basicLiveId: this.liveId
      //   })CourseLive.create
      let {code, data} = await this.$api.CourseLive.info({
        basicLiveId: this.liveId
      })
      if (code !== 200) return false
      if (data.id) {
        this.updateRoom(data.id)
      } else {
        this.createRoom()
      }
    },
    async createRoom() {
      //   let {code: c1, data: d1} = await this.$api.BasicLive.detail({
      //     id: this.liveId
      //   })
      let {code: c1, data: d1} = await this.$api.CourseLive.basicLiveDetail({
        id: this.liveId
      })
      if (c1 !== 200) return false
      let {code, data} = await this.$api.CourseLive.create({
        basicLiveId: this.liveId,
        actualStartTime: this.$moment().format('yyyy-MM-DD HH:mm:ss'),
        endTime: d1.endTime,
        liveRoomType: 1,
        providerTypeCode: this.providerTypeCode
      })
      if (code !== 200) return false
      this.liveData = data
      this.liveData.pushWebrtcUrl = `${this.liveData.pushWebrtcUrl}&stopstream_api=https://webrtcpush.myqcloud.com/webrtc/v1/stopstream`
    },
    async updateRoom(id) {
      let {code, data} = await this.$api.CourseLive.basicLiveShowUpdate({
        id,
        actualStartTime: this.$moment().format('yyyy-MM-DD HH:mm:ss'),
        providerTypeCode: this.providerTypeCode
      })
      if (code !== 200) return false
      this.liveData = data
      this.liveData.pushWebrtcUrl = `${this.liveData.pushWebrtcUrl}&stopstream_api=https://webrtcpush.myqcloud.com/webrtc/v1/stopstream`
    },
    copyLink(text) {
      if (!text) return this.$message.warning('请先获取服务器和密钥')
      navigator.clipboard.writeText(text)
      this.$message.success('复制成功')
    },
    // 下载客户端
    downloadClient() {
      window.open('https://obsproject.com/zh-cn/download')
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>