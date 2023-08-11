<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="1100px">

    <div class="padding-bottom-12">
      共计{{ tableData.length }}名学员;
      <span class="text-error" v-if="errorUsers.length">
        存在{{ errorUsers.length }}名学员信息错误，请修改之后重新上传。
      </span>
      <span v-else>
        请确认学员信息。
      </span>
    </div>
    <el-table :data="tableData" v-loading="loading.table" :row-class-name="rowClassName"
              :max-height="$utils.FullViewHeight(320)">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="index" label="序号" width="55" ></el-table-column>

      <el-table-column :label="$t('user.Name')" prop="realName" min-width="88"></el-table-column>
      <el-table-column label="手机号" prop="phone" width="110"></el-table-column>
      <el-table-column label="性别" prop="sex" width="50" >
        <template slot-scope="{row}">
          {{ row.sex | sex }}
        </template>
      </el-table-column>
      <el-table-column label="身份证号" prop="idNumber" width="160"></el-table-column>
      <!--        <el-table-column label="登录密码" prop="password" width="110"></el-table-column>-->
      <el-table-column label="机构" prop="orgName" min-width="120"></el-table-column>
      <el-table-column label="组织" prop="deptName" min-width="120"></el-table-column>
      <el-table-column label="角色" width="60">
        <template>
          学员
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" min-width="120">
        <template slot-scope="{row}">
          <span class="text-error">{{ row.remark }}</span>
        </template>
      </el-table-column>
    </el-table>
    <template v-slot:footer>
      <div class="text-center">
        <el-button class="margin-right-14" type="primary" size="medium" @click="onSubmit"
                   :loading="loading.submit" :disabled="Boolean(errorUsers.length)">确认导入
        </el-button>
        <el-button class="width-80" size="medium" @click="close">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import {mapActions, mapGetters, mapState} from 'vuex'
import PermissionTree from '@/components/panes/PermissionTree'

export default {
  name: 'DialogTableEmployeePreview',
  components: {
    PermissionTree
  },
  data() {
    return {
      visible: false, // 弹窗开关

      parentData: {},
      tableData: [],

      // 存储弹窗所需数据
      dialogType: 'EmployeeImportConfirm',
      typeMapping: {
        EmployeeImportConfirm: {title: '导入用户预览'}
      },
      loading: {
        submit: false,
        table: false
      },
      timer: {
        table: null
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
    },
    errorUsers() {
      return this.tableData.filter(item => item.remark)
    }
  },

  methods: {
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),
    async getTableData() {
      this.loading.table = true
      let {code, data: d} = await this.$api.ImportEmployee.page({
        eventId: this.parentData.eventId,
        size: 999
      })
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = d.records
    },
    rowClassName({row}) {
      if (row.remark) return 'is-error'
    },
    // 打开弹窗
    async open(data = {}) {
      this.visible = true

      this.dialogType = data.type
      this.parentData = data.formData

      return new Promise(async (resolve, reject) => {
        this.getTableData().then()
        this.$once('close', ({type, data}) => {
          if (type === 'success') return resolve(data)
          reject('取消')
        })
      })
    },
    // 关闭弹窗
    close() {
      this.$emit('close', {type: 'failed'})
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      this.loading.submit = true
      let {code} = await this.$api.Employee.batchAdminCreate(this.parentData)

      this.loading.submit = false
      if (code !== 200) return false

      this.$emit('close', {type: 'success'})
      this.visible = false
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-table__row
  &.is-error
    background-color lighten(ERROR_COLOR, 95%)

    &:hover > td
      background-color lighten(ERROR_COLOR, 95%)
</style>