<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <!--应用创建、编辑-->
    <template v-if="/^(AppCreate|AppEdit)$/.test(dialogType)">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">
        <el-form-item prop="txt" label="输入框">
          <el-input class="width-300" type="text" v-model="formData.txt" placeholder="请输入XXX"></el-input>
        </el-form-item>

        <el-form-item prop="sel" label="下拉框">
          <el-select class="width-300" v-model="formData.sel" filterable placeholder="请选择XXX">
            <el-option v-for="item in [{id:1,name: 'A'},{id:2,name:'B'}]" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </template>

    <template v-slot:footer>
      <el-button class="margin-right-14" size="small" @click="close">取消</el-button>
      <el-button type="primary" size="small" @click="onSubmit" :loading="loading.submit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: 'DialogFormTemplate',
  data() {
    let defaultFormData = {
      test: null
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'AppCreate',
      typeMapping: {
        AppCreate: {title: '创建应用', submitFn: this.$api.Application.create, type: 'Create'},
        AppEdit: {title: '编辑应用', submitFn: this.$api.Application.update, type: 'Edit'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {},
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

      this.dialogType = data.type || 'AppCreate'
      if (this.dialogType === 'AppCreate') {
        return false
      }

      if (this.dialogType === 'AppEdit') {
        this.formData = {
          ...data.formData
        }
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