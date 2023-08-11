<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false" :before-close="close" width="500px">
    <el-form :model="formData" ref="form" :rules="rules" label-width="80px" size="small">
        <el-form-item v-if="formData.isChapter === 'chapter' && formData.courseMode == 1" label="章分类" prop="chapterId" >
            <el-select class="width-100p" v-model="formData.chapterId" placeholder="请选择">
                <el-option
                v-for="item in formData.chapterList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
                </el-option>
            </el-select>
        </el-form-item>
      <el-form-item label="节名称" prop="lessonName" v-if="formData.isSort || formData.isShow">
        <el-input v-model="formData.lessonName" :maxlength="20" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="排序" prop="sort" v-if="!formData.isSort || formData.isShow">
        <el-input-number class="width-100p" v-model="formData.sort" :min="0" :max="9999" controls-position="right"></el-input-number>
      </el-form-item>
      <el-form-item>
        <el-button class="width-80 margin-right-6" type="primary" size="small" @click="onSubmit">确定</el-button>
        <el-button class="width-80" size="small" @click="close">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
const name = '节'

export default {
  name: 'DialogAddLesson',
  data() {
    let defaultFormData = {
      courseId: null,
      chapterId: null,
      lessonName: '',
      sort: null,
      chapterList: null
    }
    let {required} = this.$rules
    return {
      loading: false,
      visible: false,
      // 存储弹窗所需数据
      dialogType: 'Add',
      typeMapping: {
        Add: {title: `新增${name}`, submitFn: this.$api.Course.addLesson, type: 'Create'},
        Edit: {title: `编辑${name}`, submitFn: this.$api.Course.updateLesson, type: 'Edit'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        chapterId: [required],
        lessonName: [required],
        sort: [required]
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
    async open(data = {}) {
      this.reset()
      this.visible = true
      this.dialogType = data.type || 'Add'
      this.formData = Object.assign({}, this.formData, data.formData)
       if (this.formData.isChapter == 'chapter') {
          this.formData.chapterId = data.formData.chapterList && data.formData.chapterList[0].value
      }
    },
    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle', 'close')
    },
    //
    async onSubmit() {
      await this.$refs.form.validate()

      this.loading = true
      let {code} = await this.dialogInfo.submitFn({
        ...this.formData,
        sort: this.formData.sort ? this.formData.sort : undefined
      })
      this.loading = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](name)
      this.$emit('success')

      this.close()
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>