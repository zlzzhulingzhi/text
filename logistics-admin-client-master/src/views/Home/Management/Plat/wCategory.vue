<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-8" type="primary" size="small" @click="onCreate"
                 icon="el-icon-plus" v-if="permissions.Create">
        新增分类
      </el-button>
    </template>
    <template v-slot:footer>
      <template v-if="permissions.Enabled">
        <el-button v-for="item in enabled" :key="item.id" size="small" :disabled="!selectionList.length"
                   @click="onBatchEnabled(item.id,selectionList.map(a=>a.id))">
          批量{{ item.name }}
        </el-button>
      </template>
    </template>

    <el-table ref="table" class="margin-top-16" :data="tableData"
              :height="$utils.FullViewHeight(190 - 48 * !permissions.Enabled)"
              v-loading="loading.table" @selection-change="selectionList = $event" row-key="id" lazy
              :load="childrenLoad"
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column type="index" label="序号" width="55"></el-table-column>

      <el-table-column label="分类名称" prop="name" min-width="150" show-tooltip-when-overflow></el-table-column>

      <el-table-column label="排序" prop="sort" width="88"></el-table-column>
      <el-table-column label="状态" prop="enabled" width="72">
        <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
      </el-table-column>

      <el-table-column label="操作" width="220" fixed="right"
                       v-if="permissions.Edit || permissions.Delete || permissions.Enabled || permissions.Create">
        <template slot-scope="{row,store}">
          <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row,store)" v-if="permissions.Edit">
            编辑
          </el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row,store)"
                     v-if="permissions.Delete">删除
          </el-button>
          <EleEnabledSwitch v-model="row.enabled" @change="onEnabled(row)"
                            v-if="permissions.Enabled"></EleEnabledSwitch>
          <el-button v-if="((row.parentIds || []).length < maxChildLevel)&&permissions.Create" class="margin-left-10"
                     type="text" size="small" icon="el-icon-circle-plus-outline"
                     @click="onCreateChildren(row,store)">新增子项
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <DialogFormCommonCategory ref="DialogFormCommonCategory"></DialogFormCommonCategory>
  </TableView>
</template>

<script>
import DialogFormCommonCategory from '@/components/dialog/DialogFormCommonCategory'
import {mapActions, mapGetters} from 'vuex'


export default {
  name: 'wCategory',
  components: {
    DialogFormCommonCategory
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          enabled: {
            label: '状态'
          }
        }
      },
      tableData: [],
      filterData: {},
      selectionList: [],
      // 最大子级层级（独立）
      maxChildLevelSelf: 1,
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
      enabled: 'common/enabled'
    }),
    params() {
      return {
        ...this.filterData,
        parentId: 0
      }
    },
    maxChildLevel(){
      return this.maxChildLevelSelf || this.$store.state.config.maxChildLevel
    },
    permissions() {
      return this.$route.meta.childPermissions || {}
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
    }
  },
  methods: {
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),


    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.$api.WCategory.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records.map(item => {
        return {
          ...item,
          //   hasChildren: true
          hasChildren: Boolean(item.hasChildren)
        }
      })
      this.options.total = data.total
    },

    // 加载子节点
    async childrenLoad(d, node, resolve) {
      let {code, data} = await this.$api.WCategory.childrenList({
        parentId: d.id
      })
      if (code !== 200) return resolve([])

      let parentIds = d.parentIds ? [...d.parentIds] : []
      parentIds.push(d.id)

      resolve(data.map(item => {
        return {
          ...item,
          //   hasChildren: parentIds.length < this.maxChildLevel,
          hasChildren: Boolean(item.hasChildren),
          parentIds
        }
      }))
    },

    // 操作 - 新增子项
    async onCreateChildren(item, store) {
      await this.$refs.DialogFormCommonCategory.open({
        type: 'CreateChildren',
        formData: item
      })
      store.states.treeData[item.id].loaded = false
      store.loadData(item, item.id)

      this.afterUpdate()
    },

    // 操作 - 新增分类
    async onCreate() {
      await this.$refs.DialogFormCommonCategory.open({
        type: 'Create'
      })

      this.getTableData().then()
      this.afterUpdate()
    },

    // 操作 - 编辑分类
    async onEdit(item, store) {
      if (item.parentId) {
        let parentItem = [...store.states.data, ...Object.values(store.states.lazyTreeNodeMap).flat()].find(x => x.id === item.parentId)
        await this.$refs.DialogFormCommonCategory.open({
          type: 'EditChildren',
          formData: {
            ...item,
            parentName: parentItem.name
          }
        })
        store.states.treeData[item.parentId].loaded = false
        store.loadData(parentItem, item.parentId)
      } else {
        await this.$refs.DialogFormCommonCategory.open({
          type: 'Edit',
          formData: item
        })
        await this.getTableData()
      }

      this.afterUpdate()
    },

    // 操作 - 删除
    async onDelete(item, store) {
      await this.$confirm(`确认删除分类“${item.name}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.WCategory.delete({idList: [item.id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(item.code)
      if (item.parentId) {
        // 只有一个子项时，需要手动删除
        if (store.states.lazyTreeNodeMap[item.parentId].length === 1) {
          delete store.states.lazyTreeNodeMap[item.parentId]
        }
        let parentItem = [...store.states.data, ...Object.values(store.states.lazyTreeNodeMap).flat()].find(x => x.id === item.parentId)
        store.states.treeData[item.parentId].loaded = false
        store.loadData(parentItem, item.parentId)
      } else {
        await this.getTableData()
      }

      this.afterUpdate()
    },

    // 操作 - 批量删除
    async onBatchDelete() {
      await this.$confirm(`确认删除${this.selectionList.length}个分类吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.WCategory.delete({idList: this.selectionList.map(a => a.id)})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete('', true)
      await this.getTableData()
      this.afterUpdate()
    },

    // 操作 - 启用/禁用
    onEnabled({id, enabled, name}) {
      this.onBatchEnabled(enabled, [id], name)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name) {
      this.loading.table = true
      let {code} = await this.$api.WCategory.batchEnabled({
        idList,
        flag
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg[flag ? 'Enabled' : 'Disabled'](name, !name)

      if (!name) {
        this.selectionList.forEach(item => {
          this.$set(item, 'enabled', flag)
        })
      }
      this.afterUpdate()
    },

    // 操作 - 修改分类之后
    afterUpdate() {
      this.getDictionary(['WCourseCategoryTree'])
    }
  }
}
</script>

<style scoped>

</style>
