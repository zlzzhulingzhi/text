<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">
      <el-form-item prop="name" :label="`${dialogInfo.name}名称`">
        <el-input class="width-300" v-model="formData.name" :placeholder="`请输入${dialogInfo.name}名称`" :maxlength="30" show-word-limit></el-input>
        <el-tooltip class="margin-left-12" effect="dark" :content="`注：此处的${dialogInfo.name}名称仅在课程内容目录显示，并不会同步修改引用的资源库${dialogInfo.name}文件名称。`" placement="top">
          <i class="el-icon-question"></i>
        </el-tooltip>
      </el-form-item>
      <el-form-item :label="`关联${dialogInfo.name}`">
        <div class="flex column content">
          <span>{{ formData.fileName }}</span>
          <span>文件大小 {{ formData.fileSize | fileSize }}，视频时长 {{ $utils.formatDuration(formData.fileDuration, ['时', '分', '秒']) }}</span>
        </div>
      </el-form-item>
    </el-form>

    <template v-slot:footer>
      <el-button class="margin-right-14" size="small" @click="close">取消</el-button>
      <el-button type="primary" size="small" @click="onSubmit" :loading="loading.submit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: 'DialogChangeComponent',
  data() {
    let defaultFormData = {
      id: null,
      name: null
    }
    let {required} = this.$rules
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'Add',
      typeMapping: {
        EditVideo: {title: '编辑视频', submitFn: this.$api.Course.changeComponent, name: '视频'},
        EditResourse: {title: '编辑资料', submitFn: this.$api.CourseAttach.changeName, name: '资料'},
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        name: [required]
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
    },
    params() {
      return {
        id: this.formData.id,
        name: this.formData.name
      }
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
        ...data.formData
      }
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      let result = await this.$refs.form.validate().catch(() => {})
      if (!result) return false

      this.loading.submit = true
      let {code} = await this.dialogInfo.submitFn(this.params)
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg.Edit(this.formData.name)
      this.$emit('success')

      this.close()
    }
  }
}
</script>

<style scoped lang="stylus">
.content
  background NEUTRAL_COLOR_F2
  border-radius 4px
  padding 10px
</style>