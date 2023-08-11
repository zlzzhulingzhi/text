<template>
  <el-dialog 
    :visible.sync="visible" 
    append-to-body 
    title="添加评审专家"
    :close-on-click-modal="false"
    :before-close="close"
    width="600px">

    <el-form 
      ref="form" 
      size="small"
      :model="formData"
      :rules="rules"
      label-position="right"
      label-width="120px">

      <el-form-item label="评审专家" prop="name">
        <el-input class="width-300" type="text" v-model="formData.name"></el-input>
      </el-form-item>

      <el-form-item label="评审费(元)" prop="sex">
        <el-input-number class="width-300" controls-position="right" :min="1" :max="100000"></el-input-number>
      </el-form-item>

      <el-form-item label="评审意见" prop="idNumber">
        <el-input class="width-300" type="text" v-model="formData.idNumber" show-word-limit maxlength="18"></el-input>
      </el-form-item>


      <el-form-item>
        <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit" :loading="loading.submit">保存
        </el-button>
        <el-button class="width-80" size="medium" @click="close">取消</el-button>
      </el-form-item>

    </el-form>

  </el-dialog>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: 'DialogAddExpForReview',
  data () {
    let { required } = this.$rules
    let defaultFormData = {
      name: null,
      esex: null,
      idNumber: null,
      phone: null,
      email: null,
      title: null,
      major: null,
      bankAccount: null,
      bank: null,
      remark: null
    }
    return {
      visible: false, // 弹窗开关

      dialogType: 'Create',
      typeMapping: {
        Create: { title: '新增专家信息', submitFn: this.$api.Expert.add, type: 'Create' },
        Edit: { title: '编辑专家信息', submitFn: this.$api.Expert.update, type: 'Edit' }
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        name: [required],
        sex: [required],
        phone: [required],
        title: [required],
      },
      loading: {
        submit: false
      }
    }
  },

  computed: {
    ...mapGetters({
      sex: 'common/sex',
      proTitle: 'common/proTitle'
    }),
    dialogInfo () {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || []
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
        let { code, data: d} = await this.$api.Expert.detail({
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
    }
  }
}
</script>

<style scoped lang="stylus">
  
</style>