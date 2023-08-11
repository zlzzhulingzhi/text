<template>
  <view class="w-full h-full">
    <adapt-image v-for="(item, index) in imageList" :imageUrl="item" :key="index" @load="imageLoad"></adapt-image>
    <!--加载页-->
    <u-loading-page :loading="loading"></u-loading-page>
  </view>
</template>
 
<script>
import adaptImage from './components/adapt-image.vue'
export default {
  name: 'SubHomePage',
  components: {
    adaptImage
  },
  data() {
    const urlPath = 'https://wuhan-jidi-1312651338.cos.ap-guangzhou.myqcloud.com/miniapp'
    return {
      subHomeImageList: [
        `${urlPath}/sub-home_1.png`,
        `${urlPath}/sub-home_2.jpg`,
        `${urlPath}/sub-home_3.png`,
        `${urlPath}/sub-new-home_4.png`,
        `${urlPath}/sub-new-home_5.png`
      ],
      // 教室
      classroomImageList: [
        `${urlPath}/classroom-image_1.png`,
        `${urlPath}/classroom-new-image_2.png`,
        `${urlPath}/classroom-image_4.png`,
        `${urlPath}/classroom-image_5.png`,
        `${urlPath}/classroom-image_6.png`,
      ],
      // 食堂
      cartingImageList: [
        `${urlPath}/carting-image_1.png`,
        `${urlPath}/carting-image_2.png`,
        `${urlPath}/carting-image_3.png`
      ],
      // 宿舍
      dormitoryImageList: [
        `${urlPath}/dormitory-image_1.png`,
        `${urlPath}/dormitory-image_2.png`,
        `${urlPath}/dormitory-image_3.png`,
        `${urlPath}/dormitory-image_4.jpg`,
        `${urlPath}/dormitory-image_5.jpg`
      ],
      type: '',
      loading: true
    }
  },
  computed: {
    imageList() {
      if (this.type === 'subHome') {
        return this.subHomeImageList
      } else if (this.type === 'classRoom') {
        return this.classroomImageList
      } else if (this.type === 'dormitory') {
        return this.dormitoryImageList
      } else if (this.type === 'carting') {
        return this.cartingImageList
      }
      return []
    }
  },
  methods: {
  },
  onLoad(options) {
    const params = this.$utils.getOptions(options)
    this.type = params.type
    this.loading = true
    setTimeout(() => { this.loading = false }, 1000)
    if (this.type === 'subHome') {
      uni.setNavigationBarTitle({
        title: '培训中心'
      })
    } else if (this.type === 'classRoom') {
      uni.setNavigationBarTitle({
        title: '培训中心教室'
      })
    } else if (this.type === 'dormitory') {
      uni.setNavigationBarTitle({
        title: '培训中心宿舍'
      })
    } else if (this.type === 'carting') {
      uni.setNavigationBarTitle({
        title: '培训中心食堂'
      })
    }
  
  },
  onUnload() {
  },
  onReady() {

  },
  onShareAppMessage() {},
}
</script>
 
<style lang="scss" scoped>

</style>