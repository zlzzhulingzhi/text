<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <!--角色创建、编辑-->
    <template v-if="dialogType.startsWith('OrgRole')">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

        <el-form-item prop="name" label="角色名称">
          <el-input class="width-300" type="text" show-word-limit maxlength="10"
                    v-model.trim="formData.name" :disabled="Boolean(formData.code)"></el-input>
        </el-form-item>

        <el-form-item prop="sort" label="排序">
          <el-input-number class="width-300" controls-position="right" v-model="formData.sort"
                           :min="0"></el-input-number>
        </el-form-item>

        <el-form-item prop="remark" label="备注">
          <el-input class="width-300" type="textarea" rows="3" show-word-limit maxlength="100"
                    v-model="formData.remark"></el-input>
        </el-form-item>

        <el-form-item prop="enabled" label="角色状态">
          <el-radio-group v-model="formData.enabled" :disabled="Boolean(formData.code)">
            <el-radio v-for="item in enabled" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="功能权限">
          <!--{{ formData.menuIdList }}-->
          <PermissionTree type="OrgRole" v-model="formData.menuIdList"
                          class="tree-wrapper overflow-auto"></PermissionTree>
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
import {mapActions, mapGetters} from 'vuex'

export default {
  name: 'DialogFormRole',
  components: {
    PermissionTree
  },
  data() {
    let {required} = this.$rules
    let defaultFormData = {
      id: null,
      name: null,
      uri: null,
      host: null,
      permission: null,
      sort: null,
      iconUrl: null,
      remark: null,
      enabled: 1
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'OrgRoleCreate',
      typeMapping: {
        OrgRoleCreate: {title: '新增角色', submitFn: this.$api.OrganizationRole.create, type: 'Create'},
        OrgRoleEdit: {title: '编辑角色', submitFn: this.$api.OrganizationRole.update, type: 'Edit'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        name: [required],
        uri: [required],
        permission: [required]
      },
      loading: {
        submit: false
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    }
  },

  methods: {
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),
    reset() {
      this.formData = {...this.defaultFormData}
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset()

      this.visible = true

      this.dialogType = data.type || 'OrgRoleCreate'
      if (this.dialogType === 'OrgRoleCreate') {
        return false
      }

      if (this.dialogType === 'OrgRoleEdit') {
        let {code, data: d} = await this.$api.OrganizationRole.detail({
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
        ...this.formData,
        sort: this.formData.sort ? this.formData.sort : undefined
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.formData.name)

      this.getDictionary(['organizationRole']).then()

      this.$emit('success')
      this.close()
    },
    onSelectAll(node) {
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