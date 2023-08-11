<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <el-form ref="form" :model="formData" :rules="rules" label-width="100px" size="small">
      <el-form-item prop="fileName" label="视频名称">
        <div class="flex">
          <el-input v-model="formData.fileName" v-if="formData.fileUrl" placeholder="请填写文件名" class="width-300 margin-right-20"></el-input>
          <el-upload
            ref="upload"
            action=""
            :http-request="onImport"
            :accept="String(accept.regular)"
            :before-upload="onBeforeUpload"
            :show-file-list="false"
            :disabled="loading.chooseFile">
            <el-button class="width-76" type="primary" size="small" v-if="!formData.fileUrl" v-loading = "loading.chooseFile">
              选择文件
            </el-button>
            <el-button class="width-76" type="primary" size="small" v-else v-loading = "loading.chooseFile">
              替换文件
            </el-button>
          </el-upload>
          <ToolTip class="margin-right-6">
            <div slot="content">{{ accept.tooltip }}</div>
          </ToolTip>
        </div>
      </el-form-item>

      <el-form-item prop="display" label="是否展示">
        <EleEnabledSwitch v-model="formData.display" type="display"></EleEnabledSwitch>
      </el-form-item>

      <el-form-item prop="sort" label="排序">
        <el-input-number :style="{ width: '300px' }" v-model="formData.sort" :min="0" :max="99999" :step="1" controls-position="right"></el-input-number>
      </el-form-item>
    </el-form>

    <template v-slot:footer>
      <el-button class="margin-right-14" size="small" @click="close">取消</el-button>
      <el-button type="primary" size="small" @click="onSubmit" :loading="loading.submit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
import UploadImage from '@/components/upload/UploadImage.vue'
import CosRequest from '@/lib/cos';
import crypto from 'crypto'
import * as path from 'path'

export default {
  name: 'DialogUploadBanner',
  components: {
    UploadImage
  },
  data() {
    let defaultFormData = {
      display: 1,
      fileType: 'video',
      fileName: null,
      fileUrl: null,
      sort: 0
    }
    let {required} = this.$rules
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'Add',
      typeMapping: {
        Add: { title: '上传播放视频', submitFn: this.$api.ScreenManage.addBanner, type: 'Create' },
        Update: { title: '编辑播放视频', submitFn: this.$api.ScreenManage.updateBanner, type: 'Edit' }
      },

      accept: {
        regular: ['video/mp4'],
        tooltip: '支持上传mp4格式，视频大小不能超过2GB',
        maxFileSize: 2 * 1024 * 1024 * 1024,
      },
      // file: null,  //选择文件后保留源文件

      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        fileUrl: [required],
        display: [required],
        sort: [required],
        fileName: [required],
      },
      loading: {
        submit: false,
        chooseFile: false
      },
      cos: null,
      // 储存选择的文件
      file: null
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    submitParams() {
      return {
        ...this.formData,
      }
    }
  },
  mounted() {
    const md5Tool = crypto.createHmac('md5', '123456')
    const md5 = data => md5Tool.update(data).digest('hex')
    this.cos = new CosRequest({
      fileNameFormat: function(name) {
        let fileName = md5(Date.now().toString())
        let ext = path.extname(name)
        return `train/platform/video/${fileName}${ext}`
      }
    });
  },
  methods: {
    reset() {
      this.formData = {...this.defaultFormData}
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    open(data = {}) {
      this.reset()

      this.visible = true

      this.dialogType = data.type || 'Add'
      
      this.formData = {
        ...this.formData,
        ...(data.formData || {})
      }
    },
    // 关闭弹窗
    close() {
      if(this.loading.chooseFile) return this.$message.warning('正在上传文件，请稍等！')
      this.visible = false
    },

    // 操作 - 上传前校验
    onBeforeUpload(file) {
      if (!this.accept.regular.includes(file.type)) {
        this.$notify.error({
          title: '上传格式错误',
          message: `<div class="break-all">${file.name}格式错误，请上传正确的格式。</div>`,
          dangerouslyUseHTMLString: true,
          duration: 5000,
        });
        return true;
      }
      if (this.accept.maxFileSize < file.size) {
        this.$notify.error({
          title: '上传文件太大',
          message: `<div class="break-all">${file.name}太大，请上传小于2G的视频。</div>`,
          dangerouslyUseHTMLString: true,
          duration: 5000,
        });
        return true;
      }
      this.file = file
      return null;
    },
    // 操作 - 上传
    async onImport({ file }) {
      this.formData.fileUrl = null
      this.loading.chooseFile = true;

      // 1. 上传到cos储存桶，获取url
      this.formData.fileUrl = await new Promise((resolve, reject) => {
        this.cos.uploadFile({
          file,
          onProgress: () => {
          },
          onSuccess: (url) => {
            resolve(url)
            this.formData.fileName = this.file.name
            setTimeout(() => {
              this.loading.chooseFile = false
            }, 1000)
          },
          onError: (err) => {
            this.$message.error(err)
            reject(err)
            setTimeout(() => {
              this.loading.chooseFile = false
            }, 1000)
          }
        })
      })
    },

    // 确定提交
    async onSubmit() {
      if(this.loading.chooseFile) return this.$message.warning('正在上传文件，请稍等！')
      if(!this.formData.fileUrl) return this.$message.warning('请先上传视频文件')
      this.loading.submit = true
      let {code} = await this.dialogInfo.submitFn(this.submitParams)
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.formData.name)
      this.$emit('success')

      this.close()
    }
  }
}
</script>

<style scoped lang="stylus">

</style>