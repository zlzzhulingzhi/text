<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

   
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

        <el-form-item prop="name" label="单位名称">
          <el-input class="width-300" type="text" show-word-limit maxlength="20"
                    v-model.trim="formData.name"></el-input>
        </el-form-item>


        <el-form-item prop="contactPerson" label="联系人">
          <el-input class="width-300" v-model="formData.contactPerson" show-word-limit maxlength="8"></el-input>
        </el-form-item>

        <el-form-item prop="contactNumber" label="联系电话">
          <el-input class="width-300" type="text" show-word-limit maxlength="11"
                    v-model.trim="formData.contactNumber"></el-input>
        </el-form-item>

        <el-form-item prop="sort" label="排序">
          <el-input-number class="width-300" v-model.trim="formData.sort" controls-position="right"
          :min="1" :max="9999" :step="1"></el-input-number>
        </el-form-item>

        <el-form-item prop="remark" label="备注">
          <el-input class="width-300" type="textarea" rows="2" show-word-limit maxlength="100"
                    v-model="formData.remark"></el-input>
        </el-form-item>

        

        <el-form-item>
          <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
                     :loading="loading.submit">保存
          </el-button>
          <el-button class="width-80" size="medium" @click="close">取消</el-button>
        </el-form-item>
        
      </el-form>


  </el-dialog>
</template>

<script>
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'DialogFormOrEditUnit',
  components: {
  },
  data() {
    let {required} = this.$rules
    let defaultFormData = {
      name: null,
      contactPerson: null,
      contactNumber: null,
      sort: null,
      remark: null,
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'Create',
      typeMapping: {
        Create: {title: '新增单位', submitFn: this.$api.Unit.add, type: 'Create'},
        Edit: {title: '编辑单位', submitFn: this.$api.Unit.update, type: 'Edit'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        name: [required],
        contactPerson: [required],
        contactNumber: [
          required,
          {
            validator: (rule, value, callback) => {
              if(!/^1[3456789]\d(\d|\*){4}\d{4}$/.test(value)) {
                return callback(new Error('手机号格式错误！'))
              }

              callback()
            },
            trigger: ['blur']
          },
          
        ]
        // sort: [required],
      },
      loading: {
        submit: false
      },
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    }
  },

  methods: {
    // 重置弹窗的内容
    reset() {
      this.formData = {...this.defaultFormData}
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset()

      this.visible = true

      this.dialogType = data.type || 'Create'
      if (this.dialogType === 'Create') {
        return false
      }

      if (this.dialogType === 'Edit') {
        let {code, data: d} = await this.$api.Unit.detail({
          id: data.formData.id
        })
        if (code !== 200) return false
        this.formData = {
          ...d
        }
      }
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true

      let {code} = await this.dialogInfo.submitFn({
        ...this.formData
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.formData.name)

      this.$emit('success')
      this.close()
    },
  }
}
</script>

<style scoped lang="stylus">
.tree-wrapper
  max-height 300px

>>> .el-input--prefix
  .el-input__inner
    width 300px

>>> .el-date-editor.el-input
  width 300px

.el-dialog__wrapper
  display: flex;
  justify-content: center;
  align-items: flex-start;
</style>