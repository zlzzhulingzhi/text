<template>
  <el-dialog 
    :visible.sync="visible" 
    append-to-body 
    :title="dialogInfo.title"
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

      <el-form-item label="姓名" prop="name">
        <el-input class="width-300" type="text" v-model="formData.name"></el-input>
      </el-form-item>

      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="formData.sex">
          <el-radio v-for="item in sex" :key="item.id" :label="item.id">{{item.name}}</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="身份证号" prop="idNumber">
        <el-input class="width-300" type="text" v-model="formData.idNumber" show-word-limit maxlength="18"></el-input>
      </el-form-item>

      <el-form-item label="联系电话" prop="phone">
        <el-input class="width-300" type="text" v-model="formData.phone"></el-input>
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input class="width-300" type="text" v-model="formData.email"></el-input>
      </el-form-item>

      <el-form-item label="职称" prop="title">
        <el-select class="width-300" v-model="formData.title">
          <el-option v-for="item in proTitle" :key="item.id" :label="item.dictValue" :value="item.dictKey"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="专业" prop="major">
        <el-input class="width-300" type="text" v-model="formData.major"></el-input>
      </el-form-item>

      <el-form-item label="银行卡号" prop="bankAccount">
        <el-input class="width-300" type="text" v-model="formData.bankAccount"></el-input>
      </el-form-item>

      <el-form-item label="开户银行" prop="bank">
        <el-input class="width-300" type="text" v-model="formData.bank"></el-input>
      </el-form-item>

      <el-form-item label="备注" prop="remark">
        <el-input class="width-300" type="textarea" v-model="formData.remark" show-word-limit maxlength="100"></el-input>
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
  name: 'DialogAddExpert',
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