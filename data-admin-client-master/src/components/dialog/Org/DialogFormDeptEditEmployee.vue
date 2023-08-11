<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <!--角色创建、编辑-->
    <template v-if="dialogType.startsWith('OrgEditDept')">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

        <el-form-item prop="deptIdList" label="所属组织">
          <TreeSelect class="width-300" v-model="formData.deptId" :options="deptTree"
                      :normalizer="node => ({id: node.id,label: node.deptName,children: node.children&&node.children.length ? node.children:undefined})"
                      :clearable="false" :defaultExpandLevel="Infinity" placeholder="请选择组织"
          ></TreeSelect>
        </el-form-item>

        <el-form-item prop="employeeIdList" label="用户列表">
          <el-select class="width-300" v-model="formData.employeeIdList" multiple filterable>
            <el-option v-for="item in employeeList" :key="item.id" :label="item.realName"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
                     :loading="loading.submit">保存
          </el-button>
          <el-button class="width-80" size="medium" @click="close">取消</el-button>
        </el-form-item>
      </el-form>
    </template>

    <!--    <template v-slot:footer>
          <el-button class="margin-right-14" size="small" @click="close">取消</el-button>
          <el-button type="primary" size="small" @click="onSubmit">确定</el-button>
        </template>-->
  </el-dialog>
</template>

<script>
import {mapActions, mapGetters, mapState} from 'vuex'
import PermissionTree from '@/components/panes/PermissionTree'

export default {
  name: 'DialogFormRole',
  components: {
    PermissionTree
  },
  data() {
    let {required} = this.$rules
    let defaultFormData = {
      deptId: null,
      employeeIdList: []
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'OrgEditDept',
      typeMapping: {
        OrgEditDept: {title: '转移组织', submitFn: this.$api.Employee.update}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {},
      deptTree: [],
      employeeList: [],
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
    async getData() {
      let {data} = await this.$api.Employee.page({
        deptIdList: []
      })
      this.employeeList = data.records
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset()
      this.getData().then()

      this.visible = true
      this.deptTree = data.deptTree
      this.dialogType = data.type
      // this.employeeList = data.employeeList

      this.formData.deptId = data.deptId
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true

      /*let {code} = await this.dialogInfo.submitFn({
        ...this.formData
      })*/

      for (const item of this.employeeList.filter(item => this.formData.employeeIdList.includes(item.id))) {
        let {data} = await this.$api.Employee.detail({
          id: item.id
        })

        this.dialogInfo.submitFn({
          ...data,
          deptIdList: [this.formData.deptId]
        })
      }

      this.loading.submit = false
      // if (code !== 200) return false
      this.$message.success('操作成功')
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