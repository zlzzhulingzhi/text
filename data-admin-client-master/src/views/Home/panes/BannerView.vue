<template>
  <ItemVideoWrap :backgroud="require('@/assets/main/new_bg_center_top.png')" class="relative">
    <div class="height-100p relative">
      <div class="padding-left-24 padding-right-24 padding-top-50 height-100p">
        <!-- <video class="width-100p" :src="srcUrl" controls autoplay muted loop></video> -->
        <TCPlayer ref="TCPlayer" @playNextOne="playSomeVideo"></TCPlayer>
      </div>
      <!-- <el-image class="background" :src="require('@/assets/main/sub_bg.gif')" fit="contain"></el-image> -->
    </div>
  </ItemVideoWrap>
</template>

<script>
import itemMix from '@/views/Home/panes/itemMix'
import TCPlayer from '@/components/TCPlayer/index.vue'
import CosRequest from '@/lib/cos';

export default {
  name: 'RealTimeData',
  mixins: [itemMix],
  components: {
    TCPlayer
  },
  // created() {
  //   this.getPlayerData()
  // },
  async mounted() {
    // this.height = this.$refs.carouselWrap.clientHeight + 'px'
    this.cos = new CosRequest
    await this.getPlayerData()
    await this.initTCPlayer()
  },
  data() {
    return {
      height: '428px',
      dataAPI: this.$api.Screen.banner,
      pageData: [],
      pollingInterval: 0,
      
      videoList: [],
      // videoList: ['//seczone-teach-test-1312651338.cos.ap-guangzhou.myqcloud.com/train/platform/videoTwo.mp4', '//seczone-teach-test-1312651338.cos.ap-guangzhou.myqcloud.com/train/platform/0f3548dbda2fb2d2c192f01f4a50043b.mp4'],
      i: -1,
      loading: {
        getPlayerData: false
      },
      cos: null,
    }
  },
  computed: {
    // interval() {
    //   if (!this.pageData.length) return 3000
    //   return (this.pageData[0].duration || 0) * 1000
    // },
    // srcUrl() {
    //   return `${location.pathname}video.mp4`
    // }
  },
  methods: {
    async getPlayerData() {
      this.loading.getPlayerData = true
      let { code, data } = await this.$api.Screen.banner()
      this.loading.getPlayerData = false
      if (code !== 200) return false
      this.videoList = (data || []).map(item => item.fileUrl)
    },
    initTCPlayer() {
      this.$refs.TCPlayer.initPlayer({
        playbackDrag: true,
        playbackSpeed: true,
      })
      this.playSomeVideo()
    },
    async playSomeVideo() {
      ++this.i
      if(this.i >= this.videoList.length) this.i = 0
      let bucketName = process.env.VUE_APP_COS_Bucket
      let Key = this.videoList[this.i].replace(`//${bucketName}.cos.ap-guangzhou.myqcloud.com`, '')
      Key = Key.replace('.mp4', '.m3u8')

      // 判断存储桶中有无m3u8
      let isChanged = await new Promise((resolve, reject) => {
        this.cos.doesObjectExist({
          Key,          
          onSuccess: () => {
            resolve(true)
          },
          onError: (err) => {
            resolve(false)
          }
        })
      })
      console.log('存储桶中有无.m3u8', isChanged)
      
      if(!isChanged) this.videoList[this.i] = this.videoList[this.i].replace('.m3u8', '.mp4')

      this.$refs.TCPlayer.play({
        src: this.videoList[this.i],
        autoplay: true
      })
    },

  }
}
</script>

<style scoped lang="stylus">
.background
  position absolute
  top 640px
  left 0px
  right 0px
  opacity 0.8
.video-location
  position absolute
  left 10px
  right 10px
  top 30px
  bottom 0px
  z-index 999

</style>