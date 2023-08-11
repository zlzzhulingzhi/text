<template>
  <el-dialog custom-class="preview-dialog" :visible.sync="visible" width="1000px" top="5vh" append-to-body :close-on-click-modal="false" destroy-on-close @close="close">
    <iframe :src="url" frameborder="0" width="100%" height="720" allowfullscreen></iframe>
  </el-dialog>
</template>

<script>
import CosRequest from '@/lib/cos.js'
import url from 'url'
import querystring from 'querystring'
import { mapState } from 'vuex'

let cos = null

export default {
  name: 'DialogPreview',
  data() {
    return {
      visible: false,
      type: null,
      url: null
    }
  },
  computed: {
    ...mapState('config', {
      COSProcessType: 'COSProcessType'
    })
  },
  created() {
    cos = new CosRequest()
  },
  methods: {
    // 打开弹窗
    async open(data = {}) {
      if (!data.url) return this.$message.warning('无效链接')
      this.visible = true
      const fileUrl = await cos.getObjectUrl(/^http(s)?:\/\/.+/.test(data.url) ? data.url : `https:${data.url}`)
      console.log(fileUrl, 'fileUrlfileUrl');
      const suffix = data.url.match(/[^\.]\w*$/)[0]
      if (this.COSProcessType.includes(suffix )) {
        let parse = url.parse(fileUrl)
        this.url = url.format({
          protocol: parse.protocol,
          hostname: parse.hostname,
          pathname: parse.pathname,
          query: {
            'ci-process': 'doc-preview',
            'dstType': 'html',
            ...querystring.parse(parse.query),
          }
        })
      } else {
        this.url = fileUrl
      }
    },
    // 关闭弹窗
    close() {
      this.visible = false
    }
  }
}
</script>

<style lang="stylus">
  .preview-dialog
    &.el-dialog
      background transparent
      box-shadow none

      .el-dialog__header
        border-bottom none
        margin-bottom 8px

        .el-dialog__headerbtn
          right 0

          .el-dialog__close
            color #fff
            font-size 1.2rem

      .el-dialog__body
        padding 10px 0
</style>