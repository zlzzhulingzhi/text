<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <div v-if="isDev">{{ submitFormData }}}</div>
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

      <!--机构员工创建、编辑-->
      <template v-if="/^(OrgEmployeeCreate|OrgEmployeeEdit)$/.test(dialogType)">
        <el-form-item prop="realName" label="员工姓名">
          <el-input class="width-300" type="text" show-word-limit maxlength="10"
                    v-model.trim="formData.realName"></el-input>
        </el-form-item>

        <el-form-item prop="phone" label="手机号码">
          <el-input class="width-300" type="text" show-word-limit maxlength="11" :disabled="Boolean(formData.id)" 
                    v-model.trim="formData.phone"></el-input>
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
                    :show-password="dialogInfo.showPassword" auto-complete="new-password"
                    @change="onPasswordChange"></el-input>
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

        <el-form-item prop="roleIdList" label="所属角色">
          <el-checkbox-group v-model="formData.roleIdList">
            <el-checkbox v-for="item in organizationRole" :key="item.id" :label="item.id">{{ item.name }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item prop="deptIdList" label="所属组织">
          <!--          <el-select class="width-300" v-model="formData.deptIdList" multiple>
                      <el-option v-for="item in deptTree" :key="item.id" :label="item.name" :value="item.id"></el-option>
                    </el-select>-->
          <!--          <el-tree class="padding-top-4" ref="tree" :data="deptTree" :props="{children: 'children',label: 'deptName'}"
                             node-key="id" show-checkbox @check-change="onTreeSelect"></el-tree>-->
          <TreeSelect class="width-300" v-model="formData.deptIdList" :options="deptTree"
                      :normalizer="node => ({id: node.id,label: node.deptName,children: node.children&&node.children.length ? node.children:undefined})"
                      :clearable="false" :defaultExpandLevel="Infinity" placeholder="请选择组织"
          ></TreeSelect>
        </el-form-item>

<!--        <el-form-item v-if="/^(OrgEmployeeCreate)$/.test(dialogType)">
          <div class="flex start-center">
            <el-checkbox v-model="createStudent">
              为该员工注册开通学员账号
            </el-checkbox>
            <ToolTip>
              <div slot="content">提示：开通学员账号后，员工可使用手机号登录前台学习课程，该功能适用于企业内部员工培训场景。</div>
            </ToolTip>
          </div>
        </el-form-item>-->
      </template>

      <template v-else-if="/^(OrgEmployeeEditRole)$/.test(dialogType)">
        <el-form-item prop="roleIdList" label="所属角色">
          <el-checkbox-group v-model="formData.roleIdList">
            <el-checkbox v-for="item in organizationRole" :key="item.id" :label="item.id">{{ item.name }}</el-checkbox>
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
  name: 'DialogFormOrgEmployee',
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
      // deptIdList: []
      deptIdList: null
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'OrgEmployeeCreate',
      typeMapping: {
        OrgEmployeeCreate: {title: '新增员工', submitFn: this.$api.Employee.create, showPassword: true, type: 'Create'},
        OrgEmployeeEdit: {title: '编辑员工', submitFn: this.$api.Employee.update, showPassword: false, type: 'Edit'},
        OrgEmployeeEditRole: {title: '员工授权', submitFn: this.$api.Employee.updateRole, showPassword: false, type: 'Edit'}
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
        deptIdList: [required]
      },
      createStudent: false,
      passwordChange: false,
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
      organizationRole: 'common/organizationRole'
    }),
    // ...mapState('config', {
    //   initPassword: 'initPassword'
    // }),
    ...mapGetters({
      isDev: 'isDev'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    submitFormData() {
      if (/EditRole$/.test(this.dialogType)) return {
        id: this.formData.id,
        roleIdList: this.formData.roleIdList
      }
      // let password
      // if (/Edit$/.test(this.dialogType)) {
      //   // 编辑时，密码被修改了才传递password
      //   if (this.passwordChange) password = this.formData.password
      // } else {
      //   password = this.formData.password
      // }
      return {
        ...this.formData,
        account: this.formData.phone,
        // password,
        deptIdList: [this.formData.deptIdList],
        roleIdList: this.formData.roleIdList.filter(id => this.organizationRole.find(oItem => oItem.id === id))
      }
    }
  },

  methods: {
    reset() {
      this.createStudent = false
      this.formData = {...this.defaultFormData}
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset()

      this.visible = true

      this.dialogType = data.type
      this.deptTree = data.deptTree
      this.$refs.tree && this.$refs.tree.setCheckedKeys([])

      if (this.dialogType === 'OrgEmployeeCreate') {
        this.formData = {
          ...this.formData,
          ...data.formData
        }
        return false
      }

      if (/(Edit|EditRole)$/.test(this.dialogType)) {
        let {code, data: d} = await this.$api.Employee.detail({
          id: data.formData.id
        })
        if (code !== 200) return false

        this.$utils.ArrayFlat(d.menu || []).forEach(item => {
          let node = this.$refs.tree.getNode(item)
          if (node.isLeaf) {
            this.$refs.tree.setChecked(node, true)
          }
        })

        this.formData = {
          ...d,
          // password: this.initPassword,
          deptIdList: d.deptIdList[0]
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
      let {code, data} = await this.dialogInfo.submitFn(this.submitFormData)
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.submitFormData.realName)

      // 同时创建学员
      if (/^(OrgEmployeeCreate)$/.test(this.dialogType) && this.createStudent) {
        let { code: c } = await this.$api.Employee.createStudent({
          id: data.id
        })
        if (c !== 200) return false
        this.$msg.Create('学员')
      }

      this.$emit('success')
      this.close()
    },
    // onPasswordChange() {
    //   this.passwordChange = true
    // }
    /* onTreeSelect() {
       this.formData.deptIdList = this.$refs.tree.getCheckedKeys(true)
     }*/
  }
}
</script>

<style scoped lang="stylus">
</style>