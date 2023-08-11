<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <div v-if="isDev">{{ submitFormData }}}</div>
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

      <!--机构学员创建、编辑-->
      <template v-if="dialogType.startsWith('Student')">
        <el-form-item prop="realName" label="学员姓名">
          <el-input class="width-300" type="text" show-word-limit maxlength="10"
                    v-model.trim="formData.realName"></el-input>
        </el-form-item>

        <el-form-item prop="phone" label="手机号码">
          <el-input class="width-300" type="text" show-word-limit maxlength="11"
                    v-model.trim="formData.phone"></el-input>
        </el-form-item>

        <!-- <el-form-item prop="email" label="学员邮箱">
          <el-input class="width-300" type="text" show-word-limit maxlength="30"
                    v-model.trim="formData.email"></el-input>
        </el-form-item> -->


        <!-- <el-form-item prop="idNumber" label="身份证号">
          <el-input class="width-300" type="text" show-word-limit maxlength="18"
                    v-model.trim="formData.idNumber"></el-input>
        </el-form-item> -->

        <!-- <el-form-item prop="password" label="登录密码">
          <el-input class="width-300" type="password" v-model.trim="formData.password"
                    :show-password="dialogInfo.showPassword" @change="onPasswordChange"
                    auto-complete="new-password"></el-input>
        </el-form-item> -->

        <el-form-item prop="sex" label="性别">
          <el-radio-group v-model="formData.sex">
            <el-radio v-for="item in sex" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="enabled" label="状态">
          <el-radio-group v-model="formData.enabled">
            <el-radio v-for="item in enabled" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="orgId" label="所属机构">
          <el-select class="width-300" v-model="formData.orgId" filterable>
            <el-option v-for="item in organization" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </template>

      <el-form-item>
        <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
                   :loading="loading.submit">保存
        </el-button>
        <el-button class="width-80" size="medium" @click="close">取消</el-button>
      </el-form-item>
    </el-form>

    <!--    <template v-slot:footer>
          <el-button class="margin-right-14" size="small" @click="close">取消</el-button>
          <el-button type="primary" size="small" @click="onSubmit">确定</el-button>
        </template>-->
  </el-dialog>
</template>

<script>
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'DialogFormStudent',
  data() {
    let {required, Name, Phone, Password, idNumber} = this.$rules
    let defaultFormData = {
      realName: null,
      // email: null,
      // idNumber: null,
      // password: this.$store.state.config.initPassword,
      phone: null,
      sex: 0,
      enabled: 1,
      orgId: null
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'StudentCreate',
      typeMapping: {
        StudentCreate: {title: '新增学员', submitFn: this.$api.Student.add, showPassword: false, type: 'Create'},
        StudentEdit: {title: '编辑学员', submitFn: this.$api.Student.update, showPassword: false, type: 'Edit'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        realName: Name,
        password: Password,
        phone: Phone,
        idNumber: [idNumber],
        orgId: [required]
      },
      passwordChange: false,
      orgRole: [],
      deptTree: [],
      loading: {
        submit: false
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      sex: 'common/sex',
      organization: 'common/organization'
    }),
    ...mapState('config', {
      initPassword: 'initPassword'
    }),
    ...mapGetters({
      isDev: 'isDev'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    submitFormData() {
      return {
        ...this.formData,
        account: this.formData.phone
      }
    }
  },

  watch: {
    ['formData.orgId']: {
      immediate: true,
      handler(orgId) {
        if (typeof orgId !== 'number') return false
        this.getOrgRole()
        this.getDeptTree()
      }
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

      this.$refs.tree && this.$refs.tree.setCheckedKeys([])

      this.dialogType = data.type
      if (/Create$/.test(this.dialogType)) {
        this.formData = {
          ...this.formData,
          ...data.formData
        }
        return false
      }

      if (/Edit$/.test(this.dialogType)) {
        // let {code, data: d} = await this.$api.Student.detail({
        //   id: data.formData.id,
        //   orgId: data.formData.orgId
        // })
        // if (code !== 200) return false
        // this.formData = {
        //   ...d
        // }
        this.formData = {
          ...this.formData,
          ...data.formData
        }

        // this.$refs.tree && this.$refs.tree.setCheckedKeys(this.formData.roleIdList)
      }
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = false
      let {code} = await this.dialogInfo.submitFn(this.submitFormData)
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.submitFormData.realName)
      this.$emit('success')

      this.close()
    },
    onPasswordChange() {
      this.passwordChange = true
    },
    async getOrgRole() {
      let {code, data} = await this.$api.OrganizationRole.adminList({
        orgId: this.formData.orgId
      })
      if (code !== 200) return false
      this.orgRole = data
    },
    async getDeptTree() {
      let {code, data} = await this.$api.Dept.adminTreeList({
        orgId: this.formData.orgId
      })
      if (code !== 200) return false
      this.deptTree = data ? data.deptList : []
    }
  }
}
</script>

<style scoped lang="stylus">
</style>