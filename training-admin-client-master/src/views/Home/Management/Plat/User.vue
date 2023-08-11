<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-8" type="primary" size="small" @click="onCreate"
                 icon="el-icon-plus" v-if="permissions.Create">
        新增账号
      </el-button>
    </template>

    <template v-slot:footer>
        <template v-if="permissions.Enabled">
            <el-button v-for="item in enabled" :key="item.id" size="small" :disabled="!selectionList.length"
                    @click="onBatchEnabled(item.id,selectionList)">
            批量{{ item.name }}
            </el-button>
        </template>
    </template>
    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(190 - 48 * !permissions.Enabled)"
              v-loading="loading.table"
              @selection-change="selectionList = $event.map(item=>item.id)">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="selection" width="50" ></el-table-column>
      <el-table-column type="index" label="序号" width="55" ></el-table-column>

      <el-table-column label="姓名" prop="realName" min-width="88"></el-table-column>
      <el-table-column label="性别" prop="sex" width="50" >
        <template slot-scope="{row}">
          {{ row.sex | sex }}
        </template>
      </el-table-column>
      <el-table-column label="联系方式" prop="phone" width="110" ></el-table-column>
      <el-table-column label="角色" prop="roleName" min-width="150"></el-table-column>

      <el-table-column label="状态" prop="enabled" width="72">
        <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
      </el-table-column>

      <el-table-column label="操作" width="178"  fixed="right"
                       v-if="permissions.Edit || permissions.ResetPassword || permissions.Enabled">
        <template slot-scope="{row}">
          <div v-if="row.editable">
            <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)" v-if="permissions.Edit">编辑
            </el-button>
            <el-button type="text" size="small" icon="el-icon-lock" @click="onResetPassword(row)"
                       v-if="permissions.ResetPassword">重置密码
            </el-button>
            <EleEnabledSwitch v-model="row.enabled" @change="onEnabled(row)"
                              v-if="permissions.Enabled"></EleEnabledSwitch>
            <el-button v-if="isDev" class="text-error" type="text" size="small" icon="el-icon-delete"
                       @click="onDelete(row)">删除Dev
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <DialogFormUser ref="DialogFormUser" @success="getTableData"></DialogFormUser>
  </TableView>
</template>

<script>
import DialogFormUser from '@/components/dialog/Plat/DialogFormUser'
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'AdminUser',
  components: {
    DialogFormUser
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          enabled: {
            label: '用户状态'
          }
        },
        side: 'searchContent:用户名称或手机号'
      },
      tableData: [],
      filterData: {},
      selectionList: [],
      timer: {
        table: null
      },
      loading: {
        table: null
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
    }
  },
  watch: {
    filterData: {
      deep: true,
      immediate: true,
      handler() {
        clearTimeout(this.timer.table)
        this.timer.table = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    }
  },
  methods: {
    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.$api.User.page(this.filterData)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    // 操作 - 新增用户
    onCreate() {
      this.$refs.DialogFormUser.open({
        type: 'UserCreate'
      })
    },
    // 操作 - 编辑用户
    onEdit(item) {
      this.$refs.DialogFormUser.open({
        type: 'UserEdit',
        formData: item
      })
    },
    // 操作 - 删除
    async onDelete({id, realName}) {
      await this.$confirm(`确认删除用户“${realName}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.User.delete({idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(realName)
      await this.getTableData()
    },
    // 操作 - 批量删除
    async onBatchDelete() {
      await this.$confirm(`确认删除${this.selectionList.length}个用户吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.User.delete({idList: this.selectionList})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete('', true)
      await this.getTableData()
    },
    // 操作 - 重置密码
    async onResetPassword({id, realName}) {
      await this.$confirm(`确认将用户“${realName}”的密码重置为“${this.initPassword}”吗？`, {
        title: '重置密码确认'
      })
      this.loading.table = true
      let {code} = await this.$api.User.resetPasswordAdmin({userId: id})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Reset(realName)
    },
    // 操作 - 启用/禁用
    onEnabled({id, enabled, realName}) {
      this.onBatchEnabled(enabled, [id], realName)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name) {
      this.loading.table = true
      let {code} = await this.$api.User.batchEnabled({
        idList,
        flag
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg[flag ? 'Enabled' : 'Disabled'](name, !name)
      await this.getTableData()
    }
  }
}
</script>

<style scoped>

</style>
