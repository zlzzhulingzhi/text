<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <div v-if="isDev">{{ submitFormData }}}</div>
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

      <!--机构员工创建、编辑-->
      <template v-if="dialogType.startsWith('Employee')">
        <el-form-item prop="realName" label="员工姓名">
          <el-input class="width-300" type="text" show-word-limit maxlength="8"
                    v-model.trim="formData.realName"></el-input>
        </el-form-item>

        <el-form-item prop="phone" label="手机号码">
          <el-input class="width-300" type="text" show-word-limit maxlength="11"
                    v-model.trim="formData.phone" :disabled="dialogType !== 'EmployeeCreate'"></el-input>
        </el-form-item>

        <!-- <el-form-item prop="email" label="员工邮箱">
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
          <el-select class="width-300" v-model="formData.orgId" filterable :disabled="dialogType !== 'EmployeeCreate'">
            <el-option v-for="item in organization" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="roleIdList" label="所属角色">
          <el-checkbox-group v-model="formData.roleIdList">
            <el-checkbox v-for="item in orgRole" :key="item.id" :label="item.id">{{ item.name }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <!-- <el-form-item prop="deptIdList" label="所属组织">
          <TreeSelect class="width-300 height-32" v-model="formData.deptIdList" :options="deptTree"
                      :normalizer="node => ({id: node.id,label: node.deptName,children: node.children&&node.children.length ? node.children:undefined})"
                      :clearable="false" :defaultExpandLevel="Infinity" placeholder="请选择组织"></TreeSelect>
        </el-form-item> -->

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
  name: 'DialogFormEmployee',
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
      roleIdList: [],
      // deptIdList: [],
      deptIdList: null,
      orgId: null
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'EmployeeCreate',
      typeMapping: {
        EmployeeCreate: {title: '新增员工', submitFn: this.$api.LogisticsEmployee.adminCreate, showPassword: false, type: 'Create'},
        EmployeeEdit: {title: '编辑员工', submitFn: this.$api.LogisticsEmployee.adminUpdate, showPassword: false, type: 'Edit'}
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
        idNumber: [idNumber],
        orgId: [required],
        deptIdList: [required]
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
        password,
        roleIdList: this.formData.roleIdList.filter(id => this.orgRole.find(oItem => oItem.id === id)),
        deptIdList: [this.formData.deptIdList]
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
        let {code, data: d} = await this.$api.LogisticsEmployee.detail({
          id: data.formData.id
        })
        if (code !== 200) return false
        this.formData = {
          ...d,
          password: this.initPassword,
          // deptIdList: d.deptIdList[0]
        }
        console.log('>>>>>>>>>>>>>',this.formData);
        this.$refs.tree && this.$refs.tree.setCheckedKeys(this.formData.roleIdList)
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
    /*onTreeSelect() {
      this.formData.deptIdList = this.$refs.tree.getCheckedKeys()
    }*/
  }
}
</script>

<style scoped lang="stylus">
</style>