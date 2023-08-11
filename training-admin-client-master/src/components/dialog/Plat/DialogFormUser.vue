<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <div v-if="isDev">{{ submitFormData }}}</div>
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

      <!--平台用户创建、编辑-->
      <template v-if="dialogType.startsWith('User')">
        <el-form-item prop="realName" label="姓名">
          <el-input class="width-300" type="text" show-word-limit maxlength="8"
                    v-model.trim="formData.realName"></el-input>
        </el-form-item>

        <el-form-item prop="phone" label="联系方式">
          <el-input class="width-300" type="text" show-word-limit maxlength="11"
                    v-model.trim="formData.phone" :disabled="dialogInfo.type === 'Edit'"></el-input>
        </el-form-item>

        <el-form-item prop="sex" label="性别">
          <el-radio-group v-model="formData.sex">
            <el-radio v-for="item in sex" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="password" label="登录密码">
          <el-input class="width-300" type="password" v-model.trim="formData.password"
                    :show-password="dialogInfo.showPassword" @change="onPasswordChange"
                    auto-complete="new-password"></el-input>
        </el-form-item>

        <el-form-item prop="email" label="用户邮箱">
          <el-input class="width-300" type="text" show-word-limit maxlength="30"
                    v-model.trim="formData.email"></el-input>
        </el-form-item>

        <el-form-item prop="idNumber" label="身份证号">
          <el-input class="width-300" type="text" show-word-limit maxlength="18"
                    v-model.trim="formData.idNumber"></el-input>
        </el-form-item>

        <el-form-item prop="enabled" label="状态">
          <el-radio-group v-model="formData.enabled">
            <el-radio v-for="item in enabled" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="roleIdList" label="所属角色">
          <el-checkbox-group v-model="formData.roleIdList">
            <el-checkbox v-for="item in role" :key="item.id" :label="item.id">{{ item.name }}</el-checkbox>
          </el-checkbox-group>
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
  name: 'DialogFormUser',
  data() {
    let {required, Name, Phone, Password, idNumber} = this.$rules
    let defaultFormData = {
      realName: null,
      email: null,
      idNumber: null,
      password: this.$store.state.config.initPassword,
      phone: null,
      sex: 0,
      enabled: 1,
      roleIdList: [],
      deptIdList: [],
      orgId: null
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'UserCreate',
      typeMapping: {
        UserCreate: {title: '新增用户', submitFn: this.$api.User.create, showPassword: true, type: 'Create'},
        UserEdit: {title: '编辑用户', submitFn: this.$api.User.update, showPassword: false, type: 'Edit'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        realName: Name,
        password: Password,
        roleIdList: [required],
        phone: Phone,
        idNumber: [idNumber]
      },
      passwordChange: false,
      loading: {
        submit: false
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      role: 'common/role',
      sex: 'common/sex'
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
      let password
      if (/Edit$/.test(this.dialogType)) {
        // 编辑时，密码被修改了才传递password
        if (this.passwordChange) password = this.formData.password
      } else {
        password = this.formData.password
      }
      return {
        ...this.formData,
        account: this.formData.phone,
        password
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

      this.dialogType = data.type

      if (/Edit$/.test(this.dialogType)) {
        let {code, data: d} = await this.$api.User.detail({
          id: data.formData.id
        })
        if (code !== 200) return false
        this.formData = {
          ...d,
          password: this.initPassword
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
      let {code} = await this.dialogInfo.submitFn(this.submitFormData)
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.formData.realName)
      this.$emit('success')

      this.close()
    },
    onPasswordChange() {
      this.passwordChange = true
    }
  }
}
</script>

<style scoped lang="stylus">
</style>