<template>
  <u-popup :show="show" closeable @open="onOpen" @close="onClose">
    <view class="flex flex-col items-center bg-gray-100 p-4">
      <u-divider text="分享到"></u-divider>
      <view class="card top flex flex-col justify-center items-center text-white rounded-t-md">
        <text class="text-3xl">海报封面图</text>
        <text class="text-sm mt-3">“查看原图”后会显示实际图片</text>
      </view>
      <view class="card flex flex-col bg-white rounded-b-md relative">
        <view class="flex justify-between mx-4 mt-3">
          <text class="text-lg font-semibold">这里是课程名称</text>
          <u--image :src="`${cosPath}/wxqrcode.jpg`" width="46" height="46"></u--image>
        </view>
        <view class="flex justify-between mx-4 mt-3">
          <text class="text-xs text-error"></text>
          <text class="text-xs text-gray-400">长按识别</text>
        </view>
        <u-divider dashed></u-divider>
        <view class="flex justify-between items-center mx-4">
          <view class="flex items-center">
            <u--image src="/static/img_avatar.png" width="22" height="22"></u--image>
            <text class="text-xs mx-2">[分享者姓名]</text>
            <u--image :src="`${cosPath}/img_share_tag.png`" width="40" height="13" v-if="form.commission"></u--image>
          </view>
          <u--image :src="`${cosPath}/logo_full.png`" width="56" height="14"></u--image>
        </view>
        <view class="flex justify-center items-center tag" @click="viewPoster" v-if="poster">
          <text class="text-sm">查看原图</text>
        </view>
      </view>
      <view class="flex justify-between mt-4 bottom">
        <button class="u-reset-button" open-type="share">
          <u-icon name="weixin-fill" :size="32" color="#10b981" label="发送给好友" labelSize="14" labelPos="bottom"></u-icon>
        </button>
        <view class="m-auto">
          <u-icon name="photo" :size="32" color="#10b981" label="保存海报" labelSize="14" labelPos="bottom" @click="savePoster"></u-icon>
        </view>
      </view>
    </view>
  </u-popup>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'popup-share',
  data() {
    return {
      show: false,
      form: {},
      poster: null
    }
  },
  computed: {
    ...mapState('config', {
      cosPath: 'COS_PATH'
    })
  },
  methods: {
    open(data = {}) {
      this.form = Object.assign(this.form, data)
      this.show = true
    },
    async onOpen() {
      if (this.poster) return false
      uni.showLoading({ title: '正在生成海报', mask: true })
      let { code, data } = await this.$api.Course.poster(this.form)
      uni.hideLoading()
      if (code !== 200) return false
      this.poster = data.url
      this.$emit('success', data.id)
    },
    onClose() {
      this.show = false
    },
    viewPoster() {
      uni.previewImage({
        urls: [/https:/.test(this.poster) ? this.poster : 'https:' + this.poster]
        // urls: [this.poster]
      })
    },
    savePoster() {
      if (!this.poster) return this.$message.showToast('生成海报失败')
      uni.showLoading({ title: '正在保存海报' })
      uni.getImageInfo({
        src: /https:/.test(this.poster) ? this.poster : 'https:' + this.poster,
        // src: this.poster,
        success: (res) => {
          uni.saveImageToPhotosAlbum({
            filePath: res.path,
            success: () => this.$message.showToast('保存海报成功'),
            fail: () => this.$message.showToast('保存海报失败'),
            complete: () => uni.hideLoading()
          })
        },
        fail: () => this.$message.showToast('保存海报失败了'),
        complete: () => uni.hideLoading()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.card {
  width: 240px;
  height: 180px;

  &.top {
    background: linear-gradient(to bottom right, #23B36D, #1BCEC8);
  }

  .tag {
    width: 64px;
    height: 24px;
    position: absolute;
    right: 0;
    bottom: 0;
    background: rgba(51, 51, 51, 0.8);
    color: $uni-text-color-inverse;
    border-radius: 10px 0 7px 0;
  }
}

.bottom {
  width: 350px;
}
</style>