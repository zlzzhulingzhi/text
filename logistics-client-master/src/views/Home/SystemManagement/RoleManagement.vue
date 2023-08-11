<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-8" type="primary" size="small" @click="onCreate"
                 icon="el-icon-plus">
        新增角色
      </el-button>
    </template>

    <template v-slot:footer>
      <el-button v-for="item in enabled" :key="item.id" size="small" :disabled="!selectionList.length"
                @click="onBatchEnabled(item.id,selectionList)">
        批量{{ item.name }}
      </el-button>
    </template>

    <div class=" flex between-center">
      <div></div>
      <div>
<!--        <el-button v-for="item in enabled" :key="item.id" size="small" :disabled="!selectionList.length"-->
<!--                   @click="onBatchEnabled(item.id,selectionList)">-->
<!--          批量{{ item.name }}-->
<!--        </el-button>-->
        <!--        <el-button type="warning" size="small" :disabled="!selectionList.length"
                           @click="onBatchDelete">
                  批量删除
                </el-button>-->
      </div>
    </div>

    <el-table class="margin-top-16" :data="tableDataComputed" :height="$utils.FullViewHeight(186)"
              v-loading="loading.table"
              @selection-change="selectionList = $event.map(item=>item.id)">
      <template v-slot:empty>
        <Results v-if='!loading.table'></Results>
        <span v-else></span>
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
            <div class="tooltip-content overflow-auto padding-16" slot="content">
              <el-tree class="margin-right-16" v-if="row.treeMenu" :data="row.treeMenu" :props="{children: 'children',label: 'name'}"
                       node-key="id" :default-expanded-keys="row.defaultKeys"></el-tree>
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="enabled" width="72">
        <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
      </el-table-column>

      <el-table-column label="操作" width="170"  fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)"
                     :disabled="Boolean(row.code && (row.code === 'orgMaster'))">编辑
          </el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)"
                     :disabled="Boolean(row.code)">删除
          </el-button>
          <EleEnabledSwitch v-model="row.enabled" @change="onEnabled(row)"
                            :disabled="Boolean(row.code)"></EleEnabledSwitch>
        </template>
      </el-table-column>
    </el-table>

    <DialogFormOrgRole ref="DialogFormOrgRole" @success="getTableData"></DialogFormOrgRole>
  </TableView>
</template>

<script>
import DialogFormOrgRole from '@/components/dialog/Org/DialogFormOrgRole'
import mxTableView from '@/components/mixins/mxTableView'
import {mapActions, mapGetters} from 'vuex'

export default {
  name: 'OrgRole',
  components: {
    DialogFormOrgRole
  },
  mixins: [mxTableView],
  data() {
    return {
      options: {
        main: {
          enabled: {
            label: '角色状态',
            options: 'enabled'
          }
        },
        side: 'name:角色名称'
      },
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      app: 'common/app',
      currentOrgPermissionTree: 'common/currentOrgPermissionTree'
    }),
    permissionTree() {
      return this.app.map(fItem => {
        return {
          id: 'app-' + fItem.id,
          name: fItem.name,
          children: this.currentOrgPermissionTree.filter(item => item.appId === fItem.id)
        }
      })
    },
    tableDataComputed() {
      let filter = (data, ids) => {
        if (!data) return []
        let arr = []
        data.forEach(item => {
          if (ids.includes(item.id)) {
            arr.push({
              ...item,
              children: filter(item.children, ids)
            })
          }
        })
        return arr
      }

      return this.tableData.map(item => {
        let treeMenu = []
        let defaultKeys = []
        this.permissionTree.forEach(pItem => {
          if (!item.menuIdList) return false
          let children = filter(pItem.children, item.menuIdList)
          if (children.length) {
            defaultKeys.push(pItem.id)
            treeMenu.push({
              ...pItem,
              children
            })
          }
        })
        item.treeMenu = treeMenu
        item.defaultKeys = defaultKeys
        return item
      })
    },
    tableDataAPI() {
      return this.$api.OrgRole.page
    }
  },
  methods: {
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),

    // 操作 - 新增角色
    onCreate() {
      this.$refs.DialogFormOrgRole.open({
        type: 'OrgRoleCreate'
      })
    },
    // 操作 - 编辑角色
    onEdit(item) {
      this.$refs.DialogFormOrgRole.open({
        type: 'OrgRoleEdit',
        formData: item
      })
    },
    // 操作 - 删除
    async onDelete({id, name}) {
      await this.$confirm(`确认删除角色“${name}”吗？`, {
        title: '删除确认',
        type: 'warning'
      })
      this.loading.table = true
      let {code} = await this.$api.OrgRole.delete({idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(name)
      await this.getTableData()
      this.getDictionary(['organizationRole']).then()
    },
    // 操作 - 批量删除
    async onBatchDelete() {
      await this.$confirm(`确认删除${this.selectionList.length}个角色吗？`, {
        title: '删除确认',
        type: 'warning'
      })
      this.loading.table = true
      let {code} = await this.$api.OrgRole.delete({idList: this.selectionList})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete('', true)
      await this.getTableData()
      this.getDictionary(['organizationRole']).then()
    },
    // 操作 - 功能权限
    async onGetTreeMenu(item) {
      if (!item.menuNames || item.menuIdList) return false
      let {code, data} = await this.$api.OrgRole.detail({id: item.id})
      if (code !== 200) return false
      if (data.menuIdList) {
        let fItem = this.tableData.find(a => a.id === item.id)
        this.$set(fItem, 'menuIdList', data.menuIdList || [])
      }
    },
    // 操作 - 启用/禁用
    onEnabled({id, enabled, name}) {
      this.onBatchEnabled(enabled, [id], name)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name) {
      this.loading.table = true
      let {code} = await this.$api.OrgRole.batchEnabled({
        idList,
        flag
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg[flag ? 'Enabled' : 'Disabled'](name, !name)
      await this.getTableData()
      this.getDictionary(['organizationRole']).then()
    }
  }
}
</script>

<style scoped lang="stylus">
.tooltip-content
  max-height 300px
</style>
