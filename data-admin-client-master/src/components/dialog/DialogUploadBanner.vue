<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <el-form ref="form" :model="formData" :rules="rules" label-width="100px" size="small">
      <el-form-item prop="fileUrl" label="轮播图">
        <UploadImage v-model="formData.fileUrl" :width="192" :height="89.5">
          <span slot="tips" slot-scope="{data}" class="font-13 text-9 margin-left-16 margin-bottom-2" :style="{ lineHeight: 1.5 }">
            推荐尺寸：1920x895，<br>支持格式：{{ data.accept }}，<br>文件大小上限 {{ data.name }}
          </span>
        </UploadImage>
      </el-form-item>
      <el-form-item prop="display" label="是否展示">
        <EleEnabledSwitch v-model="formData.display" type="display"></EleEnabledSwitch>
      </el-form-item>
      <el-form-item prop="sort" label="排序">
        <el-input-number :style="{ width: '192px' }" v-model="formData.sort" :min="0" :max="99999" :step="1" controls-position="right"></el-input-number>
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

export default {
  name: 'DialogUploadBanner',
  components: {
    UploadImage
  },
  data() {
    let defaultFormData = {
      display: 1,
      duration: 5,
      fileType: 'pic',
      fileUrl: null,
      sort: 0
    }
    let {required} = this.$rules
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'Add',
      typeMapping: {
        Add: { title: '上传轮播图', submitFn: this.$api.ScreenManage.addBanner, type: 'Create' },
        Update: { title: '编辑轮播图', submitFn: this.$api.ScreenManage.updateBanner, type: 'Edit' }
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        fileUrl: [required],
        display: [required],
        sort: [required]
      },
      loading: {
        submit: false
      }
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    }
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
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      this.loading.submit = true
      let {code} = await this.dialogInfo.submitFn(this.formData)
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