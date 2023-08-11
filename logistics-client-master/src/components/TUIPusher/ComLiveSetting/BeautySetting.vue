<!--
 * @Description: 美颜设置弹窗
 * @Date: 2021-10-27 18:13:37
 * @LastEditTime: 2021-11-09 15:47:16
-->
<template>
  <div class="setting-item">
    <div class="info">{{ $t('Preview') }}</div>
    <div id="beauty-stream" class="local-stream-preview"></div>
    <el-checkbox class="beauty-checkbox" v-model="openBeauty">{{ $t('Beauty filters') }}</el-checkbox>
    <div class="beauty-item beauty">
      <span class="desc">{{ $t('beauty.Beauty') }}</span>
      <el-slider class="slider-style" v-model="beautyInfo.beautyValue"></el-slider>
    </div>
    <div class="beauty-item brightness">
      <span class="desc">{{ $t('beauty.Skin Brightening') }}</span>
      <el-slider class="slider-style" v-model="beautyInfo.brightnessValue"></el-slider>
    </div>
    <div class="beauty-item ruddy">
      <span class="desc">{{ $t('beauty.Rosy Skin') }}</span>
      <el-slider class="slider-style" v-model="beautyInfo.ruddyValue"></el-slider>
    </div>
  </div>
</template>

<script>
import rtc from '@/components/TUIPusher/mixin/rtc'
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'BeautySetting',
  props: {
    activeTab: String,
    dialogVisible: Boolean
  },
  mixins: [rtc],
  data() {
    return {
      choseCameraId: '',
      openBeauty: true,
      beautyInfo: {
        beautyValue: 50,
        brightnessValue: 50,
        ruddyValue: 50
      }
    }
  },
  computed: {
    ...mapGetters({
      activeCameraId: 'livePusher/activeCameraId'
    }),
    ...mapState('livePusher', {
      beautyParam: 'beautyParam',
      isSetMirror: 'isSetMirror',
      videoProfile: 'videoProfile',
      isOpenBeauty: 'isOpenBeauty'
    })
  },
  watch: {
    activeCameraId: {
      immediate: true,
      handler(val) {
        this.choseCameraId = val
      }
    },
    activeTab: {
      immediate: true,
      async handler(val, oldVal) {
        if (oldVal === 'beauty') {
          this.destroyBeauty()
          this.destroyLocalStream()
        }
        if (val === 'beauty') {
          this.localStream = await this.initLocalStream({
            audio: false,
            video: true,
            cameraId: this.choseCameraId,
            mirror: this.isSetMirror
          })
          this.playStream(this.localStream, 'beauty-stream')
          this.initBeauty()
          !this.isOpenBeauty && this.updateBeauty({
            beauty: 0,
            brightness: 0,
            ruddy: 0
          })
        }
      }
    },
    isOpenBeauty: {
      immediate: true,
      handler(val) {
        this.openBeauty = val
      }
    },
    openBeauty(val) {
      val
          ? this.updateBeauty({
            beauty: this.beautyInfo.beautyValue / 100,
            brightness: this.beautyInfo.brightnessValue / 100,
            ruddy: this.beautyInfo.ruddyValue / 100
          })
          : this.updateBeauty({
            beauty: 0,
            brightness: 0,
            ruddy: 0
          })
      this.$store.commit('livePusher/updateIsOpenBeauty', val)
    },
    beautyInfo: {
      handler(val) {
        this.updateBeautyParam(val)
      },
      deep: true
    }
  },
  methods: {
    // 更新美颜参数
    updateBeautyParam(beautyInfo) {
      this.$store.commit('livePusher/updateBeautyParam', this.beautyInfo)
      this.updateBeauty({
        beauty: beautyInfo.beautyValue / 100,
        brightness: beautyInfo.brightnessValue / 100,
        ruddy: beautyInfo.ruddyValue / 100
      })
    }
  }
}
</script>

<style lang="stylus" scoped>
.setting-item
  padding 0 20px 20px 34px

  .info
    margin-bottom 10px

  .title
    display inline-block
    width 42px

  .select
    width 300px
    margin-left 20px
    margin-bottom 10px

  .local-stream-preview
    width 362px
    height 186px
    border-radius 6px
    overflow hidden
    background-color #212126
    margin-bottom 10px

  .beauty-checkbox
    margin-bottom 10px

  .slider-style
    width 360px
</style>

<i18n>
{
  "en": {
    "Preview": "Preview",
    "Beauty filters": "Beauty filters"
  },
  "zh": {
    "Preview": "视频预览",
    "Beauty filters": "开启美颜"
  }
}
</i18n>
