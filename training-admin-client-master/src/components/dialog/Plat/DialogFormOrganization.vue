<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <!--角色创建、编辑-->
    <template v-if="dialogType.startsWith('OrgPermissionEdit')">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

        <el-form-item prop="menuIdList" class="margin-bottom-0" label="机构权限">
          <!--{{ formData.menuIdList }}-->
          <PermissionTree class="tree-wrapper overflow-auto" v-model="formData.menuIdList" type="Org"></PermissionTree>
        </el-form-item>

        <el-form-item>
          <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
                     :loading="loading.submit">保存
          </el-button>
          <el-button class="width-80" size="medium" @click="close">取消</el-button>
        </el-form-item>
      </el-form>
    </template>
  </el-dialog>
</template>

<script>
import PermissionTree from '@/components/panes/PermissionTree'

export default {
  name: 'DialogFormOrganization',
  components: {
    PermissionTree
  },
  data() {
    let defaultFormData = {
      menuIdList: []
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'OrgPermissionEdit',
      typeMapping: {
        OrgPermissionEdit: {title: '机构授权', submitFn: this.$api.Organization.adminUpdate, type: 'Edit'}
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
    async open(data = {}) {
      this.visible = true

      this.dialogType = data.type
      if (this.dialogInfo.type === 'Edit') {
        let {code, data: d} = await this.$api.Organization.detailAdmin({
          id: data.formData.id
        })
        if (code !== 200) return false

        this.formData = {
          id: d.id,
          menuIdList: d.menuIdList
        }
      }

      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve({})
          reject('取消')
        })
      })
    },
    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle')
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true

      let {code} = await this.dialogInfo.submitFn(this.formData)
      this.loading.submit = false
      if (code !== 200) return false
      this.$message.success('授权成功')
      this.visible = false
      this.$emit('handle','submit')
    }
  }
}
</script>

<style scoped lang="stylus">
.tree-wrapper
  max-height calc(100vh - 35vh - 100px)
</style>