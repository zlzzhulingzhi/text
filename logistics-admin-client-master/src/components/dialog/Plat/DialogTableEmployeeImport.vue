<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="1100px">

    <div class="flex between-center padding-bottom-12">
      <div>
        <div>
          <b class="font-14">{{ fullName }}</b>
        </div>
        <div v-if="tableData.length">
          共计{{ tableData.length }}名员工;
          <span class="text-error" v-if="errorUsers.length">
            存在{{ errorUsers.length }}名员工信息错误，请修改之后重新上传。
          </span>
          <span v-else>
            请确认员工信息。
          </span>
        </div>
        <div v-else>
          请导入员工信息
        </div>
      </div>

      <div class="flex">
        <a class="padding-left-10" :href="xlsxHref" download="导入员工模板">
          <el-button size="small" v-if="permissions.Import">下载模板</el-button>
        </a>
        <template v-if="permissions.Import">
          <!-- <el-upload class="margin-left-10" action="" :http-request="onImport" :accept="'.xls, .xlsx'"
                     :before-upload="onBeforeUpload" :show-file-list="false" v-show="deptId"> -->
            <el-upload class="margin-left-10 margin-right-10" action="" :http-request="onImport" :accept="'.xls, .xlsx'"
                     :before-upload="onBeforeUpload" :show-file-list="false" >
            <el-button size="small">导入员工</el-button>
          </el-upload>
          <!-- <el-button class="margin-left-10" size="small" v-show="!deptId" @click="$message.warning('请先选择组织')">导入员工
          </el-button> -->
        </template>
<!--        <el-button size="small" v-if="permissions.Export" @click="onExport">导出机构员工</el-button>-->
      </div>
    </div>


    <el-table :data="tableData" v-loading="loading.table" :row-class-name="rowClassName"
              :height="$utils.FullViewHeight(400)">
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
      <!-- <el-table-column label="身份证号" prop="idNumber" width="160"></el-table-column> -->
      <!--        <el-table-column label="登录密码" prop="password" width="110"></el-table-column>-->
      <!-- <el-table-column label="机构" prop="orgName" min-width="120"></el-table-column> -->
      <el-table-column label="组织" prop="deptName" min-width="120"></el-table-column>
      <!-- <el-table-column label="角色" width="60">
        <template>
          员工
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" min-width="120">
        <template slot-scope="{row}">
          <span class="text-error">{{ row.remark }}</span>
        </template>
      </el-table-column> -->
    </el-table>
    <template v-slot:footer>
      <div class="text-center">
        <el-button class="margin-right-14" type="primary" size="medium" @click="onSubmit"
                   :loading="loading.submit" :disabled="Boolean(errorUsers.length) || !tableData.length">确认导入
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
  name: 'DialogTableEmployeeImport',
  components: {
    PermissionTree
  },
  data() {
    return {
      visible: false, // 弹窗开关

      parentData: {},
      orgId: null,
      deptId: null,
      fullName: null,
      tableData: [],

      // 存储弹窗所需数据
      dialogType: 'EmployeeImportConfirm',
      typeMapping: {
        EmployeeImportConfirm: {
          title: '导入员工',
          exportApi: this.$api.ImportEmployee.adminDownload,
          previewApi: this.$api.ImportEmployee.adminPreview,
          addApi: this.$api.Employee.batchAdminCreate
        },
        OrgEmployeeImportConfirm: {
          title: '导入员工',
          exportApi: this.$api.ImportEmployee.download,
          previewApi: this.$api.ImportEmployee.preview,
          addApi: this.$api.Employee.batchCreate
        }
      },
      loading: {
        submit: false,
        table: false
      },
      timer: {
        table: null
      },
      xlsxHref: `${location.pathname}导入员工模板.xlsx`
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
    },
    permissions() {
      return this.$route.meta.childPermissions || {}
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
      // this.parentData = data.formData
      this.orgId = data.formData.orgId
      this.deptId = data.formData.deptId
      this.fullName = data.formData.fullName
      this.tableData = []

      return new Promise(async (resolve, reject) => {
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
      let {code} = await this.dialogInfo.addApi(this.tableData)

      this.loading.submit = false
      if (code !== 200) return false

      this.$emit('close', {type: 'success'})
      this.visible = false
    },
    onBeforeUpload(file) {
      let isExcel = ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'application/vnd.ms-excel'].includes(file.type)
      if (!isExcel) this.$message.warning('请选择.xls, .xlsx格式文件上传')
      return isExcel
    },
    // 操作 - 导入员工
    async onImport({file}) {
      let fileFormData = new FormData()
      fileFormData.append('file', file)

      let {code, msg, data} = await this.dialogInfo.previewApi({
        orgId: this.orgId,
        deptId: this.deptId,
        fileFormData
      })
      if (code !== 200) {
        let notify = this.$notify({
          title: '提示',
          message: `${msg}<br/>【点击复制】`,
          duration: 0,
          dangerouslyUseHTMLString: true,
          onClick: () => {
            this.$utils.CopyToClipboard(msg.replace(/<br\/>/g, '\n'))
            setTimeout(() => {
              notify.close()
            }, 1000)
          }
        });
        return false
      }
    //   this.parentData = {
    //     eventId: data,
    //     deptId: this.deptId
    //   }
        this.tableData = data;
    //   this.getTableData().then()
    },
    // 操作 - 导出员工
    async onExport() {
      if (!this.deptId) return this.$message.warning('请先选择组织')
      let {code} = await this.dialogInfo.exportApi({
        orgId: this.orgId,
        deptId: this.deptId
      })
      if (code !== 200) return false
      this.$message.success('导出成功')
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

>>> .el-dialog
  .el-dialog__body
    padding-top 8px
</style>