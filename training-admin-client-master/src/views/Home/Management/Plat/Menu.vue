<template>
  <div class="flex">
    <el-card v-loading="loading.tree" class="tree-card margin-right-16">
      <div :style="{height:$utils.FullViewHeight(32)}" class="width-200 overflow-auto">
        <!--        <el-select class="padding-16" v-model="appId" size="small">
                  <el-option v-for="item in app" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
                <el-tree :data="treeMenuFilter" :props="{children: 'children',label: 'menuName'}" node-key="id"
                         @node-click="onNodeClick" :default-expanded-keys="[0,parentId]"
                         accordion></el-tree>-->
        <el-tree :data="treeMenuAll" :default-expanded-keys="[parentId || appId || 0]"
                 :props="{children: 'children',label: 'menuName'}" accordion class="padding-top-4 tree-style-main"
                 node-key="id" @node-click="onNodeClick">
          <div slot-scope="{ data }" class="font-14">
            <span class="margin-right-4"></span>
            {{ data.menuName }}
          </div>
        </el-tree>
      </div>
    </el-card>

    <TableView :options="options" :params.sync="filterData" class="flex-1">
      <template v-slot:main>
        <el-button v-if="permissions.Create" class="margin-bottom-8 margin-right-8" icon="el-icon-plus" size="small"
                   type="primary" @click="onCreate">
          新增菜单
        </el-button>
      </template>

      <!--<div class="margin-top-16 flex between-center" v-if="permissions.Enabled">
        <div></div>
        <div>
          <el-button v-for="item in enabled" :key="item.id" size="small" :disabled="!selectionList.length"
                     @click="onBatchEnabled(item.id,selectionList)">
            批量{{ item.name }}
          </el-button>
          <el-button type="warning" size="small" :disabled="!selectionList.length"
                     @click="onBatchDelete">
            批量删除
          </el-button>
        </div>
      </div>-->

      <el-table v-loading="loading.table" :data="tableData" :height="$utils.FullViewHeight(188)" class="margin-top-16"
                @selection-change="selectionList = $event.map(item=>item.id)">
        <template v-slot:empty>
          <Results></Results>
        </template>

        <el-table-column label="序号" type="index" width="55"></el-table-column>

        <el-table-column label="菜单名称" min-width="100" prop="name"></el-table-column>
        <!--        <el-table-column v-if="!isDev" label="路由地址" min-width="100" prop="uri"></el-table-column>-->
        <el-table-column label="权限码" min-width="150" prop="permission"></el-table-column>
        <el-table-column label="图标" min-width="72" prop="iconUrl"></el-table-column>
        <el-table-column label="类型" prop="type" width="50">
          <template slot-scope="{row}">
            {{ row.type | menuType }}
          </template>
        </el-table-column>
        <el-table-column label="排序" prop="sort" width="50"></el-table-column>
        <el-table-column label="备注" min-width="100" prop="remark" show-tooltip-when-overflow></el-table-column>
        <el-table-column label="菜单状态" prop="enabled" width="72">
          <EleDot :id="row.enabled" slot-scope="{row}"></EleDot>
        </el-table-column>


        <el-table-column v-if="permissions.Edit || permissions.Delete || permissions.Enabled" fixed="right" label="操作"
                         width="154">
          <template slot-scope="{row}">
            <el-button v-if="permissions.Edit" icon="el-icon-edit" size="small" type="text" @click="onEdit(row)">编辑
            </el-button>
            <el-button v-if="permissions.Delete" :disabled="initCategory.test(row.category)" icon="el-icon-delete"
                       size="small"
                       type="text" @click="onDelete(row)">删除
            </el-button>
            <EleEnabledSwitch v-if="permissions.Enabled" v-model="row.enabled"
                              :disabled="initCategory.test(row.category)" @change="onEnabled(row)"></EleEnabledSwitch>
          </template>
        </el-table-column>
      </el-table>

    </TableView>

    <DialogFormMenu ref="DialogFormMenu" @success="getPageData"></DialogFormMenu>
  </div>
</template>

<script>
import DialogFormMenu from '@/components/dialog/Plat/DialogFormMenu'
import {mapActions, mapGetters, mapState} from 'vuex'

export default {
  name: 'Menu',
  components: {
    DialogFormMenu
  },
  async created() {
    if (this.$route.meta.keepAlive) return false
    await this.getTreeTotal()
  },
  async activated() {
    await this.getTreeTotal()
  },
  props: {
    type: {
      type: String,
      default: 'Plat'
    }
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          enabled: {
            label: '菜单状态'
          }
        }
      },
      tableData: [],
      filterData: {},
      selectionList: [],

      treeMenu: [],
      treeKeys: [],
      appId: '',
      parentId: 0,

      timer: {
        table: null
      },
      loading: {
        table: false,
        tree: false
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      menuType: 'common/menuType',
      app: 'common/app',
      isDev: 'isDev'
    }),
    ...mapState('config', {
      initCategory: 'initCategory'
    }),
    permissions() {
      return this.$route.meta.childPermissions || {}
    },
    pageInfo() {
      let typeMapping = {
        Plat: {
          appTypeId: 2
        },
        Org: {
          appTypeId: 3
        }
      }
      return typeMapping[this.type]
    },
    params() {
      return {
        ...this.filterData,
        appTypeId: this.pageInfo.appTypeId,
        parentId: this.parentId,
        appId: Number(this.appId.slice('4'))
      }
    },
    appFilter() {
      return this.app.filter(item => item.appTypeId === this.pageInfo.appTypeId)
    },
    treeMenuAll() {
      return this.appFilter.map(fItem => {
        return {
          id: `app-${fItem.id}`,
          appId: fItem.id,
          menuName: fItem.name,
          children: this.treeMenu.filter(item => item.appId === fItem.id)
        }
      })
    }
  },
  watch: {
    params: {
      deep: true,
      immediate: true,
      handler() {
        clearTimeout(this.timer.table)
        this.timer.table = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    },
    appFilter: {
      immediate: true,
      handler(app) {
        if (app[0]) this.appId = `app-${app[0].id}`
      }
    }
  },
  methods: {
    ...mapActions({
      getPermissionData: 'system/getPermissionData',
      syncPermission: 'router/syncPermission'
    }),
    async getPageData() {
      this.getTableData().then()
      this.getTreeTotal().then()
      // await this.getPermissionData()
      // this.syncPermission()
    },
    async getTreeTotal() {
      this.loading.tree = true
      let {code, data} = await this.$api.Menu.treeTotal({
        category: 'admin'
      })
      this.loading.tree = false
      if (code !== 200) return false
      this.treeMenu = data.menu || []
    },

    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.$api.Menu.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    // 操作 - 选择节点
    onNodeClick(d) {
      if (typeof d.id === 'string') {
        this.appId = d.id
        this.parentId = 0
        return false
      }

      if (d.children.length) {
        this.parentId = d.id
      }
    },
    // 操作 - 新增菜单
    onCreate() {
      this.$refs.DialogFormMenu.open({
        type: 'MenuCreate',
        formData: {
          parentId: this.parentId,
          appId: this.params.appId
        },
        treeMenu: this.treeMenuAll
      })
    },
    // 操作 - 编辑菜单
    onEdit(item) {
      this.$refs.DialogFormMenu.open({
        type: 'MenuEdit',
        formData: item,
        treeMenu: this.treeMenuAll
      })
    },
    // 操作 - 删除
    async onDelete({id, name}) {
      await this.$confirm(`确认删除菜单“${name}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Menu.delete({idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(name)
      await this.getPageData()
    },
    // 操作 - 批量删除
    async onBatchDelete() {
      await this.$confirm(`确认删除${this.selectionList.length}个菜单吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Menu.delete({idList: this.selectionList})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete('', true)
      await this.getPageData()
    },
    // 操作 - 启用/禁用
    onEnabled({id, enabled, name}) {
      this.onBatchEnabled(enabled, [id], name)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name) {
      this.loading.table = true
      let {code} = await this.$api.Menu.batchEnabled({
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

<style lang="stylus" scoped>
.tree-card
  >>> .el-card__body
    padding 0
</style>
