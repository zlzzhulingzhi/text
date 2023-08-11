<template>
  <el-container class="height-100p">
    <!-- <TreeSelectorAsSideBar ref="TreeSelectorAsSideBar" type="OrgEmployee" :props="{label: 'deptName'}"
                           :visible.sync="treeVisible" :selection.sync="categorySelection"
                           :selectId.sync="deptId" :treeList.sync="deptTree"></TreeSelectorAsSideBar> -->

    <TableView class="flex-1 overflow-auto" :options="options" :params.sync="filterData">
      <template v-slot:main>
        <div class="flex margin-bottom-8">
          <!--额外按钮-->
          <!-- <div class="flex center-center" v-if="!treeVisible">
            <div class="font-16 padding-8 padding-left-0 el-icon-s-unfold pointer text-main"
                 @click="treeVisible = true"></div>
          </div> -->
          
          <el-button class="margin-right-12" type="primary" size="small" @click="onCreate" icon="el-icon-plus"> 新增员工
          </el-button>
        </div>
      </template>

      <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(186)" v-loading="loading.table"
                @selection-change="selectionList = $event.map(item => item.id)">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>

        <el-table-column type="selection" width="50"></el-table-column>
        <el-table-column type="index" label="序号" width="55"></el-table-column>

        <el-table-column label="员工姓名" prop="realName" min-width="88"></el-table-column>
        <el-table-column label="手机号" prop="phone" width="110"></el-table-column>
        <el-table-column label="性别" prop="sex" width="50">
          <template slot-scope="{ row }">
            {{ row.sex | sex }}
          </template>
        </el-table-column>

        <el-table-column label="组织" prop="orgName" min-width="120"></el-table-column>
        <el-table-column label="角色" prop="roleNames" min-width="100"></el-table-column>
        <el-table-column label="注册时间" prop="createTime" width="150"></el-table-column>
        <el-table-column label="状态" prop="enabled" width="72">
          <EleDot slot-scope="{ row }" :id="row.enabled"></EleDot>
        </el-table-column>

        <el-table-column label="操作" width="172" fixed="right">
          <template slot-scope="{ row }">
            <div v-if="row.editable">
              <el-button type="text" size="small" @click="onEditRole(row)">授权</el-button>
              <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)">编辑</el-button>
              <!-- <el-button type="text" size="small" icon="el-icon-lock" @click="onResetPassword(row)">重置密码</el-button> -->
              <!-- <el-button type="text" size="small" @click="onSendMessage(row)">私信</el-button> -->
              <el-button v-if="isDev" class="text-error" type="text" size="small" icon="el-icon-delete"
                         @click="onDelete(row)">删除Dev
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </TableView>

    <DialogFormOrgEmployee ref="DialogFormOrgEmployee" @success="getPageData"></DialogFormOrgEmployee>
  </el-container>
</template>

<script>
import DialogFormOrgEmployee from '@/components/dialog/Org/DialogFormOrgEmployee'
// import TreeSelectorAsSideBar from '@/components/form/TreeSelectorAsSideBar'
import mxTableView from '@/components/mixins/mxTableView'
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'OrgEmployee',
  components: {
    DialogFormOrgEmployee,
    // TreeSelectorAsSideBar
  },
  mixins: [mxTableView],
  data() {
    return {
      options: {
        main: {
          roleId: {
            label: '员工角色',
            options: 'organizationRole'
          }
        },
        side: 'searchContent:员工名称或手机号'
      },
      // treeVisible: true,


      // deptTree: [],
      // deptId: null,
      // categorySelection: null,

    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      organization: 'common/organization',
      isDev: 'isDev'
    }),
    params() {
      let categoryIdList = []
      // if (this.categorySelection) {
      //   categoryIdList = this.$utils.ArrayFlat([this.categorySelection]).map(item => item.id)
      // }

      return {
        ...this.filterData,
        deptIdList: categoryIdList
      }
    },
    tableDataAPI() {
      return this.$api.OrgEmployee.page
    }
  },
  methods: {
    getPageData() {
      this.getTableData()
    },

    // 操作 - 新增员工
    onCreate() {
      this.$refs.DialogFormOrgEmployee.open({
        type: 'OrgEmployeeCreate',
        formData: {
          orgId: this.filterData.orgId
        },
        // deptTree: this.deptTree
      })
    },
    // 操作 - 编辑员工
    onEdit(item) {
      this.$refs.DialogFormOrgEmployee.open({
        type: 'OrgEmployeeEdit',
        formData: item,
        // deptTree: this.deptTree
      })
    },
    // 操作 - 授权
    onEditRole(item) {
      this.$refs.DialogFormOrgEmployee.open({
        type: 'OrgEmployeeEditRole',
        formData: item
      })
    },
    // 操作 - 删除
    async onDelete({id, realName}) {
      await this.$confirm(`确认删除员工“${realName}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.OrgEmployee.delete({idList: [id]})
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
      let {code} = await this.$api.OrgEmployee.delete({idList: this.selectionList})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete('', true)
      await this.getTableData()
    },
    // 操作 - 启用/禁用
    onEnabled({id, realName, enabled}) {
      this.onBatchEnabled(enabled, [id], realName)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name = '') {
      this.loading.table = true
      let {code} = await this.$api.OrgEmployee.batchEnabled({
        idList,
        flag
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg[flag ? 'Enabled' : 'Disabled'](name, !name)
      await this.getTableData()
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
    // async onImport() {
    //   this.$router.push({ name: 'EmployeeImport' })
    // }
  }
}
</script>

<style scoped lang="stylus">
</style>
