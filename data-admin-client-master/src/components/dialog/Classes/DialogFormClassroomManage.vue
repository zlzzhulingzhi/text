<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false" :before-close="close" width="600px">

    <!--班级创建、编辑-->
    <template v-if="dialogType.startsWith('')">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

        <el-form-item prop="name" label="教室类别">
          <el-select class="width-300" v-model="formData.name" filterable>
            <el-option v-for="item in classType" :key="item.id" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="code" label="教室编号">
          <el-input class="width-300" controls-position="right" v-model="formData.code"></el-input>
        </el-form-item>

        <el-form-item prop="seats" label="教室座位数">
          <el-input class="width-300" controls-position="right" v-model="formData.seats"></el-input>
        </el-form-item>

        

        <el-form-item>
          <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit" :loading="loading.submit">保存
          </el-button>
          <el-button class="width-80" size="medium" @click="close">取消</el-button>
        </el-form-item>
      </el-form>
    </template>
  </el-dialog>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'DialogFormClassroomManage',
  data () {
    let { required } = this.$rules
    let defaultFormData = {}
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'Create',
      typeMapping: {
        Create: { title: '新增教室', submitFn: this.$api.ClassroomInfoAPI.create, type: 'Create' },
        Edit: { title: '编辑教室', submitFn: this.$api.ClassroomInfoAPI.update, type: 'Edit' }
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        name: [required],
        code: [required],
        seats: [required]
      },
      loading: {
        submit: false
      }
    }
  },
  computed: {
    ...mapGetters({
      classType: 'common/classType'
    }),
    dialogInfo () {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    }
  },

  methods: {
    reset () {
      this.formData = { ...this.defaultFormData }
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open (data = {}) {
      this.reset()

      this.visible = true

      this.dialogType = data.type || 'Create'
      if (this.dialogType === 'Create') {
        return false
      }

      if (this.dialogType === 'Edit') {
        let { code, data: d } = await this.$api.ClassroomInfoAPI.detail({
          id: data.formData.id
        })
        if (code !== 200) return false
        this.formData = {
          ...d
        }
      }
    },
    // 关闭弹窗
    close () {
      this.visible = false
    },
    // 确定提交
    async onSubmit () {
      await this.$refs.form.validate()
      this.loading.submit = true

      let { code } = await this.dialogInfo.submitFn({
        ...this.formData
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.formData.code)

      this.$emit('success')
      this.close()
    },
    onSelectAll (node) {
      node.checked = !(node.checked && node.childNodes.every(item => item.checked))
      node.childNodes.forEach(item => item.checked = node.checked)
    }
  }
}
</script>

<style scoped lang="stylus">
.tree-wrapper
  max-height 300px
</style>