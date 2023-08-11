<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-8" type="primary" size="small" @click="onCreate"
                 icon="el-icon-plus" v-if="permissions.Create">
        新增角色
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

      <el-table-column type="selection" width="50" 
                       :selectable="row=>!row.code"></el-table-column>
      <el-table-column type="index" label="序号" width="55" ></el-table-column>

      <el-table-column label="角色名称" prop="name" min-width="88"></el-table-column>
      <el-table-column label="备注" prop="remark" min-width="88"></el-table-column>
      <el-table-column label="功能权限" prop="menuNames" min-width="150">
        <template slot-scope="{row}">
          <el-tooltip effect="light" enterable v-if="row.menuNames"
                      @mouseenter.native="onGetTreeMenu(row)">
            <div class="text-ellipsis">{{ row.menuNames }}</div>
            <div slot="content">
              <el-tree v-if="row.treeMenu" :data="row.treeMenu" :props="{children: 'children',label: 'menuName'}"
                       node-key="id" :default-expanded-keys="row.defaultKeys"></el-tree>
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="enabled" width="72">
        <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
      </el-table-column>

      <el-table-column label="操作" width="154"  fixed="right"
                       v-if="permissions.Edit || permissions.Delete || permissions.Enabled">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)" v-if="permissions.Edit"
                     :disabled="Boolean(row.code === 'admin')">编辑
          </el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)" v-if="permissions.Delete"
                     :disabled="Boolean(row.code)">
            删除
          </el-button>
          <EleEnabledSwitch v-model="row.enabled" @change="onEnabled(row)"
                            v-if="permissions.Enabled" :disabled="Boolean(row.code)"></EleEnabledSwitch>
        </template>
      </el-table-column>
    </el-table>

    <DialogFormRole ref="DialogFormRole" @success="getTableData"></DialogFormRole>
  </TableView>
</template>

<script>
import DialogFormRole from '@/components/dialog/Plat/DialogFormRole'
import {mapActions, mapGetters, mapState} from 'vuex'

export default {
  name: 'PlatRole',
  components: {
    DialogFormRole
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          enabled: {
            label: '角色状态'
          }
        },
        side: 'name:角色名称'
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
      app: 'common/app'
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
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),
    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.$api.Role.page(this.filterData)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    // 操作 - 新增角色
    onCreate() {
      this.$refs.DialogFormRole.open({
        type: 'RoleCreate'
      })
    },
    // 操作 - 编辑角色
    onEdit(item) {
      this.$refs.DialogFormRole.open({
        type: 'RoleEdit',
        formData: item
      })
    },
    // 操作 - 删除
    async onDelete({id, name}) {
      await this.$confirm(`确认删除角色“${name}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Role.delete({idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(name)
      this.getDictionary(['role']).then()
      await this.getTableData()
    },
    // 操作 - 批量删除
    async onBatchDelete() {
      await this.$confirm(`确认删除${this.selectionList.length}个角色吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Role.delete({idList: this.selectionList})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete('', true)
      this.getDictionary(['role']).then()
      await this.getTableData()
    },
    // 操作 - 功能权限
    async onGetTreeMenu(item) {
      if (!item.menuNames || item.treeMenu) return false
      let {code, data} = await this.$api.Role.detail({id: item.id})
      if (code !== 200) return false
      if (data.menu) {
        let treeMenu = []
        let defaultKeys = []
        this.app.forEach(fItem => {
          let children = data.menu.filter(item => item.appId === fItem.id)
          if (children.length) {
            let id = 'app-' + fItem.id
            treeMenu.push({
              id,
              menuName: fItem.name,
              children
            })
            defaultKeys.push(id)
          }
        })
        this.$set(item, 'treeMenu', treeMenu)
        this.$set(item, 'defaultKeys', defaultKeys)
      }
    },
    // 操作 - 启用/禁用
    onEnabled({id, enabled, name}) {
      this.onBatchEnabled(enabled, [id], name)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name) {
      this.loading.table = true
      let {code} = await this.$api.Role.batchEnabled({
        idList,
        flag
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg[flag ? 'Enabled' : 'Disabled'](name, !name)
      await this.getTableData()
      this.getDictionary(['role']).then()
    }
  }
}
</script>

<style scoped lang="stylus">

</style>
