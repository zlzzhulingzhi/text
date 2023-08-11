<template>
  <el-container class="height-100p">

    <TreeSelectorAsSideBar ref="TreeSelectorAsSideBar" type="AllEmployeeDept" 
                           :visible.sync="treeVisible" :selection.sync="categorySelection" ></TreeSelectorAsSideBar>

    <TableView class="flex-1 overflow-auto" :options="optionsComputed" :params.sync="filterData">
      <template v-slot:main>
        <div class="flex center-center margin-bottom-8" v-if="!treeVisible">
          <div class="font-16 padding-8 padding-left-0 el-icon-s-unfold pointer text-main"
               @click="treeVisible = true"></div>
        </div>

        <div class="margin-bottom-8 margin-right-8 flex">
          <el-button type="primary" size="small" @click="onCreate"
                     icon="el-icon-plus" v-if="permissions.Create">
            新增员工
          </el-button>
          <!-- <el-button type="primary" icon="el-icon-plus" size="small" @click="onImport" v-if="permissions.Import">导入员工</el-button> -->
        </div>
      </template>
      <template v-slot:footer>
        <template v-if="permissions.Enabled">
          <el-button v-for="item in enabled" :key="item.id" size="small" :disabled="!selectionList.length"
                     @click="onBatchEnabled(item.id,selectionList)" >
            批量{{ item.name }}
          </el-button>
        </template>
      </template>

      <el-table class="margin-top-16" :data="tableData"
                :height="$utils.FullViewHeight(188)"
                v-loading="loading.table"
                @selection-change="selectionList = $event.map(item=>item.id)">
        <template v-slot:empty>
          <Results></Results>
        </template>

        <el-table-column type="selection" width="50"></el-table-column>
        <el-table-column type="index" label="序号" width="55"></el-table-column>

        <el-table-column label="员工姓名" prop="realName" min-width="88"></el-table-column>
        <el-table-column label="手机号" prop="phone" width="110"></el-table-column>
        <el-table-column label="性别" prop="sex" width="50">
          <template slot-scope="{row}">
            {{ row.sex | sex }}
          </template>
        </el-table-column>

        <!-- <el-table-column label="机构" prop="orgId" min-width="120">
          <template slot-scope="{row}">
            {{ row.orgId | organization }}
          </template>
        </el-table-column> -->
        <el-table-column label="组织" prop="deptNames" min-width="150"></el-table-column>
        <el-table-column label="角色" prop="roleNames" min-width="100"></el-table-column>
        <el-table-column label="注册时间" prop="createTime" width="150"></el-table-column>
        <el-table-column label="状态" prop="enabled" width="72">
          <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
        </el-table-column>

        <el-table-column label="操作" width="118" fixed="right"
                         v-if="permissions.Edit || permissions.ResetPassword || permissions.SendMsg || permissions.Enabled">
          <template slot-scope="{row}">
            <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)" v-if="permissions.Edit">编辑
            </el-button>
            <!-- <el-button type="text" size="small" icon="el-icon-lock" @click="onResetPassword(row)"
                       v-if="permissions.ResetPassword">重置密码
            </el-button> -->
            <el-button type="text" size="small" @click="onSendMessage(row)" v-if="permissions.SendMsg">私信</el-button>
            <EleEnabledSwitch v-model="row.enabled" @change="onEnabled(row)"
                              v-if="permissions.Enabled"></EleEnabledSwitch>
            <el-button v-if="isDev" class="text-error" type="text" size="small" icon="el-icon-delete"
                       @click="onDelete(row)">删除Dev
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <DialogFormEmployee ref="DialogFormEmployee" @success="getTableData"></DialogFormEmployee>
      <DialogTableEmployeeImport ref="DialogTableEmployeeImport"></DialogTableEmployeeImport>
    </TableView>
  </el-container>
</template>

<script>
import DialogFormEmployee from '@/components/dialog/Plat/DialogFormEmployee'
import DialogTableEmployeeImport from '@/components/dialog/Plat/DialogTableEmployeeImport'
import TreeSelectorAsSideBar from '@/components/form/TreeSelectorAsSideBar'
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'Employee',
  components: {
    DialogFormEmployee,
    DialogTableEmployeeImport,
    TreeSelectorAsSideBar
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          roleId: {
            label: '机构角色',
            options: []
          }
        },
        side: 'searchContent:员工名称或手机号'
      },
      tableData: [],
      filterData: {},
      selectionList: [],

      treeVisible: true,
      categorySelection: null,

      timer: {
        table: null
      },
      loading: {
        table: false
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      isDev: 'isDev'
    }),
    ...mapState('config', {
      initPassword: 'initPassword'
    }),
    permissions() {
      return this.$route.meta.childPermissions || {}
    },
    optionsComputed() {
      return {
        ...this.options,
        side: this.permissions.Search ? this.options.side : undefined
      }
    },
    params() {
      if (!this.categorySelection) return this.filterData
      let {id: deptId, orgId} = this.categorySelection
      let deptIdList = []
      if (typeof deptId === 'number') deptIdList = this.$utils.ArrayFlat([this.categorySelection]).map(item => item.id)
      return {
        ...this.filterData,
        orgId:orgId || this.$store.state.system.orgId,
        deptIdList
      }
    }
  },
  watch: {
    params: {
      deep: true,
      immediate: true,
      handler(newVal, oldVal) {
        clearTimeout(this.timer.table)
        this.timer.table = setTimeout(() => {
          this.getTableData()

          if (newVal.orgId !== oldVal.orgId) this.getRoleList()
        }, 300)
      }
    }
  },
  methods: {
    async getRoleList() {
      // 防止 点击“全部组织机构”时携带原本的数据
      this.options.main.roleId.options = []
      
      if (!this.params.orgId) return false
      this.loading.table = true
      let {code, data} = await this.$api.OrganizationRole.adminList({
        orgId: this.params.orgId
      })
      this.loading.table = false
      if (code !== 200) return false
      this.options.main.roleId.options = data || []
    },

    async getTableData() {
      if (this.params.orgId === null) return false
      this.loading.table = true
      let {code, data} = await this.$api.Employee.pageAdmin(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    // 操作 - 新增员工
    onCreate() {
      this.$refs.DialogFormEmployee.open({
        type: 'EmployeeCreate',
        formData: {
          orgId: this.params.orgId,
          deptIdList: this.params.deptIdList ? this.params.deptIdList[0] : null
        }
      })
    },
    // 操作 - 编辑员工
    onEdit(item) {
      this.$refs.DialogFormEmployee.open({
        type: 'EmployeeEdit',
        formData: item
      })
    },
    // 操作 - 删除
    async onDelete({id, realName}) {
      await this.$confirm(`确认删除员工“${realName}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Employee.delete({idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(realName)
      await this.getTableData()
    },
    // 操作 - 批量删除
    async onBatchDelete() {
      await this.$confirm(`确认删除${this.selectionList.length}个员工吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Employee.delete({idList: this.selectionList})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete('', true)
      await this.getTableData()
    },
    // 操作 - 启用/禁用
    onEnabled({id, enabled, realName}) {
      this.onBatchEnabled(enabled, [id], realName)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name) {
      this.loading.table = true
      let {code} = await this.$api.Employee.batchAdminEnabled({
        idList,
        flag,
        orgId: this.params.orgId || this.$store.state.system.orgId
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg[flag ? 'Enabled' : 'Disabled'](name, !name)
      await this.getTableData()
    },
    // 操作 - 重置密码
    async onResetPassword({userId, realName}) {
      await this.$confirm(`确认将员工“${realName}”的密码重置为“${this.initPassword}”吗？`, {
        title: '重置密码确认'
      })
      this.loading.table = true
      let {code} = await this.$api.User.resetPasswordAdmin({userId})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Reset(realName)
    },
    // 操作 - 私信
    async onSendMessage(item) {
      await this.$prompt('', `私信-${item.realName}`, {
        inputPlaceholder: '请输入私信内容',
        inputType: 'textarea',
        inputPattern: /^.{1,100}$/,
        inputErrorMessage: '请输入100字以内的私信内容',
        closeOnClickModal: false
      })

      this.$msg.SendMsg(item.realName)
    },
    // 操作 - 导入
    async onImport() {
      if (!this.categorySelection) return this.$message.warning('请选择组织')
      
      let {orgId, fullName} = this.categorySelection
      await this.$refs.DialogTableEmployeeImport.open({
        type: 'EmployeeImportConfirm',
        formData: {
          orgId,
          fullName: fullName.split('/')[0]
        }
      })
      await this.getTableData()
    }
  }
}
</script>

<style scoped lang="stylus">
</style>
