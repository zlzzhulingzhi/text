<!--主页 显示机构简介-->
<template>
  <view class="bg-green-400 w-full h-full relative">
    <HomeFullSwiper ref="headerSwiper" :introduceImgArr="homeFullImageArr"></HomeFullSwiper>
  </view>
</template>

<script>
import { mapState } from 'vuex'
import HomeFullSwiper from '@/components/views/HomeFullSwiper.vue'

export default {
  name: 'tabNewHome',
  components: {
    HomeFullSwiper
  },
  data() {
    const urlPath = 'https://wuhan-jidi-1312651338.cos.ap-guangzhou.myqcloud.com/miniapp'
    return {
      showTexts: { student: '课程', org: '机构', admin: '平台' },
      homeFullImageArr: [
        { url: `${urlPath}/home_1.png`, type: 'subHome' },
        { url: `${urlPath}/home_2.png`, type: 'classRoom' },
        { url: `${urlPath}/home_3.png`, type: 'dormitory' },
        { url: `${urlPath}/home_4.png`, type: 'carting' },
      ]
    };
  },

  computed: {
    ...mapState('config', {
      cosPath: 'COS_PATH'
    }),
    ...mapState('system', {
      identity: 'identity'
    }),
  },
  watch: {
    identity: {
      deep: true,
      handler(newVal) {
        let identity = newVal || 'student'
        this.$nextTick(() => {
          setTimeout(() => {
            uni.setTabBarItem({
              index: 1,
              text: this.showTexts[identity]
            })
          }, 500)
        })
      }
    }
  },

  onLoad() {
    setTimeout(() => {
      uni.setTabBarItem({
        index: 1,
        text: this.showTexts[this.identity]
      })
    }, 500)
  },
  onShareAppMessage() {},
  methods: {
    showNavigation() {
      uni.previewImage({
        current: 0,
        urls: [`${this.cosPath}/navigation.jpg`]
      })
    }
  }
};
</script>

<style lang="scss" scoped>

</style>