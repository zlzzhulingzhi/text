<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="1000px">

    <div class="flex between-center padding-bottom-12">
      <div>
        <div>
          <b class="font-14">
            <span v-if="orgName">{{ orgName }} /</span>
            {{ deptName }}
          </b>
        </div>
        <div v-if="tableData.length">
          共计{{ tableData.length }}名学员;
          <span class="text-error" v-if="errorUsers.length">
            存在{{ errorUsers.length }}名学员信息错误，请修改之后重新上传。
          </span>
          <span v-else>
            请确认学员信息。
          </span>
        </div>
        <div v-else>
          请导入学员信息
        </div>
      </div>

      <div class="flex">
        <a class="padding-left-10" href="/导入学员模板.xlsx" download="导入学员模板">
          <el-button size="small" v-if="permissions.Import">下载模板</el-button>
        </a>
        <template v-if="permissions.Import">
          <el-upload class="margin-left-10 margin-right-10" action="" :http-request="onImport" :accept="'.xls, .xlsx'"
                     :before-upload="onBeforeUpload" :show-file-list="false">
            <el-button size="small">导入学员</el-button>
          </el-upload>
        </template>
        <el-button size="small" v-if="permissions.Import" @click="onImportRecord">导入记录</el-button>
        <el-button size="small" v-if="permissions.Export" @click="onExport">导出机构学员</el-button>
      </div>
    </div>


    <el-table :data="tableData" v-loading="loading.table" :row-class-name="rowClassName"
              :height="$utils.FullViewHeight(400)">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="index" label="序号" width="55" ></el-table-column>

      <el-table-column :label="$t('user.Name')" prop="realName" min-width="120"></el-table-column>
     
      
      <el-table-column label="手机号" prop="phone" width="110"></el-table-column>
       <el-table-column label="性别" prop="sex" width="50" >
        <template slot-scope="{row}">
          {{ row.sex | sex }}
        </template>
      </el-table-column>
      <el-table-column label="身份证号" prop="idNumber" min-width="120"></el-table-column>
      <el-table-column label="所属组织" prop="deptName" width="110"></el-table-column>
      <el-table-column label="标签" prop="groupName" width="110"></el-table-column>
      
      
      <!--        <el-table-column label="登录密码" prop="password" width="110"></el-table-column>-->
      <!-- <el-table-column label="机构" prop="orgName" min-width="120"></el-table-column> -->
      <!-- <el-table-column label="组织" prop="deptName" min-width="120"></el-table-column> -->
      <!-- <el-table-column label="角色" width="60">
        <template>
          学员
        </template>
      </el-table-column> -->
      <el-table-column label="备注" prop="remark" min-width="120">
        <template slot-scope="{row}">
          <span class="text-error">{{ row.remark }}</span>
        </template>
      </el-table-column>
    </el-table>
    <template v-slot:footer>
      <div class="text-center">
        <el-button class="margin-right-14" type="primary" size="medium" @click="onSubmit"
                   :loading="loading.submit" :disabled="Boolean(errorUsers.length) || !tableData.length">确认导入
        </el-button>
        <el-button class="width-80" size="medium" @click="close">取消</el-button>
      </div>
    </template>

    <!--导入提示弹窗-->
    <el-dialog title="提示" :visible.sync="showImpDialog" append-to-body :close-on-click-modal="false" width="500px" @close="onClose">
      <!--导入完成-->
      <el-result icon="success" title="导入已完成" v-if="impStepEnd">
        <template slot="subTitle">
          导入成功{{ impResult.success || 0 }}条，导入失败{{ impResult.failure || 0 }}条。<br>如需导入结果明细请直接下载导入报告
        </template>
        <template slot="extra">
          <el-button type="primary" size="medium" :disabled="!impResult.id" :loading="loading.download" @click="onDownload">下载导入报告</el-button>
        </template>
      </el-result>
      <!--导入中-->
      <div class="flex column center-center" v-else>
        <span class="font-18 margin-top-16 margin-bottom-16">数据导入中，请等待……</span>
        <span class="font-13 text-9">温馨提示：关闭当前页面不影响继续导入。<br>导入完成后可在【导入记录】查看导入报告。</span>
      </div>
    </el-dialog>

    <DialogTableImportRecord ref="DialogTableImportRecord"></DialogTableImportRecord>
  </el-dialog>
</template>

<script>
import {mapActions, mapGetters, mapState} from 'vuex'
import PermissionTree from '@/components/panes/PermissionTree'
import DialogTableImportRecord from '@/components/dialog/Org/DialogTableImportRecord'

export default {
  name: 'DialogTableEmployeeImport',
  components: {
    PermissionTree,
    DialogTableImportRecord
  },
  data() {
    return {
      visible: false, // 弹窗开关

      // parentData: {},
      orgId: null,
      orgName: null,
      deptId: null,
      deptName: null,
      tableData: [],

      // 存储弹窗所需数据
      dialogType: 'EmployeeImportConfirm',
      typeMapping: {
        OrgStudentImport: {
          title: '导入学员',
          exportApi: this.$api.Student.export,
          previewApi: this.$api.Student.importPreview,
          addApi: this.$api.Student.importSave,
          downloadApi: this.$api.ImportRecord.downloadReport
        }
      },
      loading: {
        submit: false,
        table: false,
        export: false,
        download: false
      },
      timer: {
        table: null
      },
      showImpDialog: false,
      impStepEnd: false,
      impResult: {}
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
    // async getTableData() {
    //   this.loading.table = true
    //   let {code, data: d} = await this.$api.Student.page({
    //     eventId: this.parentData.eventId,
    //     size: 999
    //   })
    //   this.loading.table = false
    //   if (code !== 200) return false
    //   this.tableData = d.records
    // },
    rowClassName({row}) {
      if (row.remark) return 'is-error'
    },
    // 打开弹窗
    async open(data = {}) {
      this.visible = true

      this.dialogType = data.type
      // this.parentData = data.formData
      this.orgId = data.formData.orgId
      this.orgName = data.formData.orgName
      this.deptId = data.formData.deptId
      this.deptName = data.formData.deptName
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
      this.showImpDialog = true
      this.impStepEnd = false

      this.loading.submit = true
      let {code, data} = await this.dialogInfo.addApi(this.tableData)
      this.loading.submit = false
      if (code !== 200) return false
      this.impResult = data || {}
      this.impStepEnd = true

      this.$emit('close', {type: 'success'})
      // this.visible = false
    },
    onBeforeUpload(file) {
      let isExcel = ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'application/vnd.ms-excel'].includes(file.type)
      if (!isExcel) this.$message.warning('请选择.xls, .xlsx格式文件上传')
      return isExcel
    },
    // 操作 - 导入学员
    async onImport({file}) {
      let fileFormData = new FormData()
      fileFormData.append('file', file)

      let {code, data, msg} = await this.dialogInfo.previewApi({
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

      // this.parentData = {
      //   eventId: data,
      //   deptId: this.deptId
      // }

      // this.getTableData().then()
      this.tableData = data
    },
    // 导入记录
    onImportRecord() {
      this.$refs.DialogTableImportRecord.open({
        type: 'Student'
      })
    },
    // 操作 - 导出学员
    async onExport() {
      this.loading.export = true
      let {code} = await this.dialogInfo.exportApi({
        orgId: this.orgId,
        deptId: this.deptId
      })
      this.loading.export = false
      if (code !== 200) return false
      this.$message.success('导出成功')
    },
    // 关闭导入弹窗
    onClose() {
      this.showImpDialog = false
      this.tableData = []
    },
    // 下载导入报告
    async onDownload() {
      this.loading.download = true
      let {code} = await this.dialogInfo.downloadApi({
        id: this.impResult.id
      })
      this.loading.download = false
      if (code !== 200) return false
      this.$message.success('下载成功')
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