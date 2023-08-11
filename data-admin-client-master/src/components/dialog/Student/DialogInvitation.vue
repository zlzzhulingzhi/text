<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false" :before-close="close" width="600px">
    <div class="text-6">选择组织和学员标签，生成邀请注册二维码，学员扫码注册后，将自动给该学员勾选的组织和学员标签</div>
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">
      <el-form-item prop="deptId" label="所属组织">
        <TreeSelect class="width-300" v-model="formData.deptId" :options="deptTree" :normalizer="node => ({id: node.id, label: node.deptName, children: node.children && node.children.length ? node.children : undefined})" clearable :defaultExpandLevel="Infinity" placeholder="请选择组织">
        </TreeSelect>
      </el-form-item>
      <el-form-item prop="groupId" label="学员标签">
        <el-select class="width-300" v-model="formData.groupId" placeholder="请添加学员标签" multiple clearable>
          <el-option v-for="item in studentGroupTree" :key="item.groupId" :label="item.groupName" :value="item.groupId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="endTime" label="有效截止时间">
        <el-date-picker class="width-300" popper-class="nonow" v-model="formData.endTime" type="datetime" placeholder="选择时间" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
      </el-form-item>
      <el-form-item prop="remark" label="备注">
        <el-input class="width-300" v-model="formData.remark" type="textarea" placeholder="备注内容" :rows="3" :maxlength="50" show-word-limit></el-input>
        <div class="text-9 font-13">提示：备注内容将对注册学员提示</div>
      </el-form-item>
      <el-form-item>
        <el-button class="width-80" type="primary" size="medium" @click="submit" :loading="loading.submit">保存</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { mapState, mapGetters } from 'vuex'

export default {
  name: 'DialogInvitation',
  data () {
    let defaultFormData = {
      businessType: 1,
      deptId: null,
      groupId: [],
      endTime: null,
      remark: null,
      enabled: 1
    }
    let { required } = this.$rules
    return {
      loading: {
        submit: false
      },
      visible: false,
      dialogType: 'Create',
      typeMapping: {
        Create: { title: "新增邀请", submitFn: this.$api.StudentInvite.add, type: 'Create' }
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        deptId: [
          {
            validator: (rule, value, callback) => {
              if (!value && !this.formData.groupId.length) return callback(new Error('所属组织和学员标签至少选择一个'))
              callback()
            }, trigger: ['blur', 'change']
          }
        ],
        groupId: [
          {
            validator: (rule, value, callback) => {
              if (!value.length && !this.formData.deptId) return callback(new Error('所属组织和学员标签至少选择一个'))
              callback()
            }, trigger: ['blur', 'change']
          }
        ],
        endTime: [required]
      }
    }
  },
  computed: {
    ...mapState('system', {
      orgId: 'orgId'
    }),
    ...mapGetters('common', {
      deptTree: 'deptTree',
      studentGroupTree: 'studentGroupTree'
    }),
    dialogInfo () {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    params () {
      return {
        ...this.formData,
        startTime: this.$moment().format('YYYY-MM-DD hh:mm:ss'),
        orgId: this.orgId
      }
    }
  },
  methods: {
    reset () {
      this.formData = { ...this.defaultFormData }
      this.$refs.form && this.$refs.form.resetFields()
    },
    close () {
      this.visible = false
    },
    open (data = {}) {
      this.reset()
      this.visible = true
      this.dialogType = data.type || 'Create'
    },
    async submit () {
      let result = await this.$refs.form.validate().catch(() => { })
      if (!result) return false

      this.loading.submit = true
      let { code } = await this.dialogInfo.submitFn(this.params)
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type]('邀请')
      this.$emit("success")
      this.close()
      this.loading.submit = false
    }
  }
}
</script>

<style lang="stylus">
.el-date-picker.nonow
  .el-picker-panel__link-btn.el-button--text
    display none
</style>

<style lang="stylus" scoped>
.el-date-editor
  &.el-input
    width 300px
.el-textarea
  .el-input__count
    bottom -32px
</style>