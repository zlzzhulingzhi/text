<template>
  <el-dialog title="分享" :visible.sync="showDialog" width="400px" :before-close="close" center append-to-body>
    <div class="flex column center-center">
      <QrcodeVue :value="playerUrl" :size="112"></QrcodeVue>
        <div class="text-center">
            <div class="margin-top-16">{{ playerUrl }}</div>
            <el-button class="" size="small" type="primary" @click="copyLink">复制</el-button>
        </div>
      <!-- <div class="flex margin-top-12">
        <el-input class="width-240 margin-right-12" size="small" ref="linkInput" v-model="url"></el-input>
        <el-button class="" size="small" type="primary" @click="copyLink">复制</el-button>
      </div> -->
    </div>

  </el-dialog>

    

</template>

<script>
import QrcodeVue from 'qrcode.vue'
import {mapState} from 'vuex'

export default {
  name: 'CompShare',
  components: {
    QrcodeVue
  },
  data() {
    return {
      showDialog: false,
      url: location.href
    }
  },
  computed: {
    ...mapState('config', {
        domain: 'domain'
    }),
    ...mapState('system', {
        orgId: 'orgId'
    }),
    playerUrl() {
      return `${this.domain.inner}/?orgId=${this.orgId}#/Course/Detail?id=${this.$route.query.id}`
    }
  },
  methods: {
    close() {
      this.showDialog = false
    },
    open() {
      this.showDialog = true
    },
    copyLink() {
    //   navigator.clipboard.writeText(this.url)

        this.$utils.CopyToClipboard(this.playerUrl)
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>
