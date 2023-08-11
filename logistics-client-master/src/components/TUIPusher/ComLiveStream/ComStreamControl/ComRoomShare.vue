<!--
 * @Description: 直播间分享组件
 * @Date: 2021-11-03 10:40:21
 * @LastEditTime: 2022-04-27 20:00:45
-->
<template>
  <div>
    <span class="font-24 text-f pointer flex" @click="open()">
      <SvgIcon icon-name="share"></SvgIcon>
    </span>
    <el-dialog :title="$t('Share')" :visible.sync="showRoomShareDialog" width="400px" :before-close="handleClose" center
               append-to-body>
      <div class="flex column center-center">
        <QrcodeVue :value="playerUrl" :size="112"></QrcodeVue>
      </div>
      <div class="text-center">
        <div class="margin-top-16">{{ playerUrl }}</div>
        <el-button class="margin-top-16" type="primary" size="small" @click="handleCopyLink">
          {{ $t('Copy') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import QrcodeVue from 'qrcode.vue'
import {mapState} from 'vuex'

export default {
  name: 'ComRoomShare',
  components: {
    QrcodeVue
  },
  data() {
    return {
      showRoomShareDialog: false,
      liveId: this.$route.params.id
    }
  },
  computed: {
    ...mapState('system', {
      innerDomain: 'innerDomain'
    }),
    playerUrl() {
      return `${this.innerDomain}/#/h5/player/${this.liveId}`
    }
  },
  methods: {
    handleClose() {
      this.showRoomShareDialog = false
    },
    handleCopyLink() {
      navigator.clipboard.writeText(this.playerUrl)
    },
    open(id) {
      if (typeof id === 'string') {
        this.liveId = id
      } else {
        this.liveId = this.$route.params.id
      }
      this.showRoomShareDialog = true
    }
  }
}
</script>

<style lang="stylus" scoped>
</style>

<i18n>
{
  "en": {
    "Share": "Share",
    "Scan to Watch": "Scan to Watch",
    "Save QR Code": "Save QR Code",
    "Copy": "Copy"
  },
  "zh": {
    "Share": "分享直播给好友",
    "Scan to Watch": "扫码观看",
    "Save QR Code": "保存图片",
    "Copy": "复制"
  }
}
</i18n>
