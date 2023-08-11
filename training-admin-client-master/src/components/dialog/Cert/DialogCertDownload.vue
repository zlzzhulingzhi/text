<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title"
             :close-on-click-modal="true"
             :before-close="close" :width="dialogInfo.width">

    <template v-if="/^CertDownload$/.test(dialogType)">
      <template v-if="oData.certImageUrl">
        <el-image class="width-800" :src="oData.certImageUrl">
          <div class="text-center" slot="placeholder">
            <i class="el-icon-loading"></i>
            正在加载中...
          </div>
        </el-image>

        <div class="flex center-center margin-top-12">
          <el-button v-if="oData.certImagePdfUrl" class="margin-right-14" type="primary" size="medium"
                     @click="onDownloadPDF">
            下载PDF
          </el-button>
          <span>可右键点击证书保存图片</span>
          <!-- <el-button size="medium" @click="onDownloadPNG">
            下载PNG
          </el-button> -->
        </div>
      </template>
      <div class="text-center" v-else>
        <div class="line-height-32 margin-bottom-16">
          证书生成中
        </div>
        <el-button size="medium" @click="onSubmit">
          关闭
        </el-button>
      </div>
    </template>

    <template v-else-if="/^CertBatchDownload$/.test(dialogType)">
      <div class="line-height-32">
        <div>当前已选中{{ oData.length }}份证书</div>
        <div v-if="filterData.length !== oData.length">其中{{ oData.length - filterData.length }}份证书正在生成</div>
      </div>
      <div class="flex center-center margin-top-12">
        <template v-if="filterData.length">
          <el-button class="margin-right-14" type="primary" size="medium"
                     @click="onBatchDownloadPDF">
            下载PDF
          </el-button>
          <el-button size="medium" @click="onBatchDownloadPNG">
            下载PNG
          </el-button>
        </template>
        <template v-else>
          <el-button size="medium" @click="onSubmit">
            关闭
          </el-button>
        </template>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import CosRequest from '@/lib/cos'

export default {
  name: 'DialogCertDownload',
  created() {
    let {cos, config} = new CosRequest()
    this.cos = cos
    this.config = config
  },
  data() {
    return {
      visible: false,
      dialogType: 'CertDownload',
      oData: {},
      info: {},
      typeMapping: {
        CertDownload: {title: '下载证书', width: '864px'},
        CertBatchDownload: {title: '批量下载证书', width: '300px'}
      },
      loading: {
        image: true
      },

      cos: null,
      config: null
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    filterData() {
      if (this.dialogType === 'CertBatchDownload') {
        return this.oData.filter(item => item.certImageUrl)
      }
      return []
    }
  },
  methods: {
    async open(data = {}) {
      this.visible = true
      this.dialogType = data.type
      this.oData = data.data
      this.info = data.info
    },
    onSubmit() {
      this.$emit('success')
      this.close()
    },
    close() {
      this.visible = false
    },
    // 工具 - 获取文件blob
    async getFileBody(url, userName) {
      try {
        let {pathname} = new URL(url)
        let ext = pathname.match(/\..+$/)[0]
        let {Body} = await this.cos.getObject({
          ...this.config,
          Key: pathname,
          DataType: 'blob'
        })
        return {
          name: `${userName}_${this.info.name}_${this.$moment().format('yyyyMMDD')}${ext}`,
          body: Body
        }
      } catch (e) {
        return false
      }
    },
    onDownloadPDF() {
      this.onDownloadPNG('pdf')
    },
    async onDownloadPNG(type = 'png') {
      let k = {n: 'certImageUrl', f: 'DownloadFile'}
      if (type === 'pdf') k = {n: 'certImagePdfUrl', f: 'DownloadPDF'}
      let data = await this.getFileBody(this.oData[k.n], this.oData.userName)
      if (!data) return this.$message.error('系统异常，下载失败')
      await this.$utils[k.f](data)
    },
    onBatchDownloadPDF() {
      this.onBatchDownloadPNG('pdf')
    },
    async onBatchDownloadPNG(type = 'png') {
      let k = {n: 'certImageUrl'}
      if (type === 'pdf') k = {n: 'certImagePdfUrl'}
      let fileList = []
      for (const item of this.filterData) {
        let data = await this.getFileBody(item[k.n], item.userName)
        if (data) fileList.push(data)
      }
      await this.$utils.DownloadFileAsZip({
        name: `${fileList.length}份_${this.info.name}_${this.$moment().format('yyyyMMDD')}`,
        fileList
      })
    }
  }
}
</script>

<style scoped lang="stylus">
.width-800
  width 800px
</style>