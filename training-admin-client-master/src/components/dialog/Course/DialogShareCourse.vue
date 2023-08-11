<template>
  <el-dialog title="分享" :visible.sync="showDialog" width="400px" :before-close="close" center append-to-body>
    <div class="flex column center-center">
      <!-- <QrcodeVue :value="url" :size="112"></QrcodeVue> -->

      <div class="flex margin-top-12">
        <el-input class="width-240 margin-right-12" size="small" ref="linkInput" v-model="url"></el-input>
        <el-button class="" size="small" type="primary" @click="copyLink">复制</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import QrcodeVue from 'qrcode.vue'
import store from '@/store'
import {mapState} from "vuex";
export default {
  name: 'CompShare',
  components: {
    QrcodeVue
  },
  data() {
    return {
      showDialog: false,
      courseId: ''
    }
  },
  computed: {
    ...mapState('config', {
      domain: 'domain'
    }),
    ...mapState('system', {
      orgId: 'orgId',
      innerDomain: 'innerDomain'
    }),
    url() {
      if (!this.courseId) return ''
      return `${this.innerDomain}${this.domain.h5Path}/#/pages/course-detail/course-detail?id=${this.courseId}`
    }
  },
  methods: {
    close() {
      this.showDialog = false
    },
    open(data = {}) {
      this.showDialog = true
      this.courseId = data.courseId || ''
    },
    copyLink() {
      navigator.clipboard.writeText(this.url)
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>
