<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-12" type="primary" size="small" @click="onCreate" icon="el-icon-plus" v-if="permissions.Create">新建{{ pageInfo.type }}
      </el-button>
    </template>
    <template v-slot:side>
      <el-button type="primary" size="small" @click="item.fn" v-for="item in pageInfo.actionList" :key="item.id">{{ item.title }}</el-button>
    </template>

    <template v-slot:footer>
      <el-button v-for="item in enabled" :key="item.id" size="small" :disabled="!selectionList.length" @click="onBatchEnabled(item.id, selectionList.map(v => v.id))">
        批量{{ item.name }}
      </el-button>

    </template>

    <el-table class="margin-top-16" ref="table" v-loading="loading" :data="tableData" :height="$utils.FullViewHeight(188 + offsetHeight)" @selection-change="selectionList = $event" row-key="id" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" lazy :load="loadChildren">
      <template v-slot:empty>
        <Results></Results>
      </template>
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column :label="`${pageInfo.type}名称`" prop="categoryName" min-width="160"></el-table-column>
      <el-table-column label="图标" prop="categoryIconUrl" min-width="88" v-if="/categoryIconUrl/.test(pageInfo.columns)">
        <template slot-scope="{row}">
          <el-image class="width-50 height-50" v-if="row.categoryIconUrl" :src="row.categoryIconUrl"></el-image>
          <span v-else>暂无图标</span>
        </template>
      </el-table-column>
      <el-table-column label="排序" prop="sort" width="88"></el-table-column>
      <el-table-column label="层级" prop="parentCode" width="88">
        <template slot-scope="{row}">
          <el-tag class="level-tag" :class="`style-${row.parentIds.length}`">{{
              row.parentIds.length | numToCn
            }}级
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="enabled" width="72">
        <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
      </el-table-column>
      <el-table-column label="操作" width="220" fixed="right" v-if="permissions.Edit || permissions.Delete || permissions.Enabled || permissions.Create">
        <template slot-scope="{row, store}">
          <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row, store)" v-if="permissions.Edit">
            编辑
          </el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row, store)" v-if="permissions.Delete">删除
          </el-button>
          <EleEnabledSwitch class="margin-right-8" v-model="row.enabled" @change="onEnabled(row)" v-if="permissions.Enabled"></EleEnabledSwitch>
          <el-button type="text" size="small" icon="el-icon-circle-plus-outline" @click="onCreateChild(row, store)" v-if="(row.parentIds || []).length < maxChildLevel && permissions.Create">新增子项
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <DialogFormCategory ref="DialogFormCategory"></DialogFormCategory>
  </TableView>
</template>

<script>
import DialogFormCategory from '@/components/dialog/DialogFormCategory.vue'
import { mapActions, mapState, mapGetters } from 'vuex'

export default {
  name: 'CategoryView',
  components: {
    DialogFormCategory
  },
  data () {
    return {
      typeMapping: {
        // CourseCategory: {
        //   type: '分类',
        //   TableDataAPI: this.$api.CourseCategory.page,
        //   TableDataParams: null,
        //   ChildDataAPI: this.$api.CourseCategory.list,
        //   DeleteAPI: this.$api.CourseCategory.delete,
        //   EnabledAPI: this.$api.CourseCategory.enable,
        //   columns: 'categoryIconUrl',
        //   commonList: ['courseCategoryList', 'courseCategoryTree'],
        //   dataMap: data => data.map(item => {
        //     return {
        //       ...item,
        //       parentIds: item.parentCode.split(',')
        //     }
        //   })
        // },
        // TaskCategory: {
        //   type: '分类',
        //   TableDataAPI: this.$api.TaskCategory.page,
        //   TableDataParams: {
        //     parentId: 0
        //   },
        //   ChildDataAPI: this.$api.TaskCategory.childrenList,
        //   DeleteAPI: this.$api.TaskCategory.delete,
        //   EnabledAPI: this.$api.TaskCategory.enable,
        //   columns: '',
        //   commonList: ['taskCategory'],
        //   dataMap: data => data.map(item => {
        //     return {
        //       ...item,
        //       parentIds: item.parentCode.split(',')
        //     }
        //   })
        // },
        // QuestionCategory: {
        //   type: '分类',
        //   TableDataAPI: this.$api.Category.page,
        //   TableDataParams: {
        //     groupId: 1,
        //     level: 1
        //   },
        //   ChildDataAPI: this.$api.Category.childrenList,
        //   DeleteAPI: this.$api.Category.delete,
        //   EnabledAPI: this.$api.Category.batchEnabled,
        //   columns: '',
        //   commonList: ['questionCategory'],
        //   dataMap: data => data.map(item => {
        //     return {
        //       ...item,
        //       parentIds: item.parentCode.split(','),
        //       categoryName: item.name,
        //       enabled: Number(item.enabled),
        //       sort: item.sortNum
        //     }
        //   })
        // },
        // PaperCategory: {
        //   type: '分类',
        //   TableDataAPI: this.$api.Category.page,
        //   TableDataParams: {
        //     groupId: 2,
        //     level: 1
        //   },
        //   ChildDataAPI: this.$api.Category.childrenList,
        //   DeleteAPI: this.$api.Category.delete,
        //   EnabledAPI: this.$api.Category.batchEnabled,
        //   columns: '',
        //   commonList: ['paperCategory'],
        //   dataMap: data => data.map(item => {
        //     return {
        //       ...item,
        //       parentIds: item.parentCode.split(','),
        //       categoryName: item.name,
        //       enabled: Number(item.enabled),
        //       sort: item.sortNum
        //     }
        //   })
        // },
        // LibraryType: {
        //   type: '分类',
        //   TableDataAPI: this.$api.LibraryType.page,
        //   TableDataParams: {
        //     platformType: 1,
        //     parentId: 0,
        //     template: 0
        //   },
        //   ChildDataAPI: this.$api.LibraryType.childrenList,
        //   DeleteAPI: this.$api.LibraryType.delete,
        //   EnabledAPI: this.$api.LibraryType.enabled,
        //   columns: '',
        //   commonList: [],
        //   dataMap: data => data.map(item => {
        //     return {
        //       ...item,
        //       parentIds: item.parentCode.split(','),
        //       categoryName: item.name
        //     }
        //   })
        // },

        // ResourceType: {
        //   type: '分类',
        //   TableDataAPI: this.$api.ResourceType.page,
        //   TableDataParams: {
        //     parentId: 0,
        //     template: 0
        //   },
        //   ChildDataAPI: this.$api.ResourceType.childrenList,
        //   DeleteAPI: this.$api.ResourceType.delete,
        //   EnabledAPI: this.$api.ResourceType.enabled,
        //   columns: '',
        //   commonList: ['resCategoryTree'],
        //   dataMap: data => data.map(item => {
        //     return {
        //       ...item,
        //       parentIds: item.parentCode.split(','),
        //       categoryName: item.name
        //     }
        //   })
        // },

        Dept: {
          type: '组织',
          TableDataAPI: this.$api.Dept.page,
          TableDataParams: {
            parentId: 0
          },
          ChildDataAPI: this.$api.Dept.childrenList,
          DeleteAPI: this.$api.Dept.delete,
          EnabledAPI: this.$api.Dept.batchEnabled,
          columns: 'peopleCount',
          commonList: ['deptTree'],
          dataMap: data => data.map(item => {
            return {
              ...item,
              parentIds: item.parentCode.split(','),
              categoryName: item.deptName
            }
          }),
          actionList: [
            { id: 1, title: '批量设置可见课程', fn: () => { this.$router.push({ name: 'DeptBatchAdd', query: { type: 'Course' } }) } },
            { id: 2, title: '批量指派培训项目', fn: () => { this.$router.push({ name: 'DeptBatchAdd', query: { type: 'Task' } }) } },
          ]
        }
      },

      options: {
        total: 0,
        main: {
          enabled: {
            label: '状态'
          }
        }
      },
      filterData: {},
      selectionList: [],
      tableData: [],
      // 最大子级层级（独立）
      maxChildLevelSelf: null,

      loading: false,
      timer: null
    }
  },
  props: {
    type: {
      type: String,
      required: true
    },
    offsetHeight: {
      type: Number,
      default: 0
    },
    isComp: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapState('common', {
      enabled: 'enabled'
    }),
    ...mapGetters('router', {
      flatList: 'flatList'
    }),
    params () {
      return {
        ...this.filterData,
        ...this.pageInfo.TableDataParams
      }
    },
    maxChildLevel () {
      return this.maxChildLevelSelf || this.$store.state.config.maxChildLevel
    },
    permissions () {
      // FIXME console.log(`🚀 ~ permissions ~ this.$route.meta`, this.$route.meta)
      if (this.isComp) {
        let permissions = this.$utils.GetPermission(`${this.$route.meta.permission}:${this.type}`, true)
        return permissions
      }
      return this.$route.meta.childPermissions || {}
    },
    pageInfo () {
      return this.typeMapping[this.type] || {}
    }
  },
  watch: {
    params: {
      deep: true,
      immediate: true,
      handler () {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    }
  },
  methods: {
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),
    // 获取列表
    async getTableData () {
      this.loading = true
      let { code, data } = await this.pageInfo.TableDataAPI(this.params)
      this.loading = false
      if (code !== 200) return false
      this.tableData = []
      this.tableData = this.pageInfo.dataMap(data.records)
      this.options.total = data.total
    },
    // 获取子项
    async loadChildren (tree, treeNode, resolve) {
      let { code, data } = await this.pageInfo.ChildDataAPI({
        parentId: tree.id

      })
      if (code !== 200) return resolve([])
      resolve(this.pageInfo.dataMap(data))
    },
    // 获取父项
    getTreeParentItem (row, store) {
      return [...store.states.data, ...Object.values(store.states.lazyTreeNodeMap).flat()].find(v => v.id === row.parentId)
    },
    // 操作 - 刷新基础数据(公共字段)
    onUpdateCommonStore () {
      this.pageInfo.commonList.length && this.getDictionary(this.pageInfo.commonList)
    },

    // 操作 - 更新子项
    async onUpdateChild (row, store, isDelete = false) {
      if (row.parentId) {
        let parentItem = this.getTreeParentItem(row, store)

        // 只有一个子项时，需要手动删除
        if (isDelete && (store.states.lazyTreeNodeMap[row.parentId].length === 1)) {
          delete store.states.lazyTreeNodeMap[row.parentId]
        }

        store.states.treeData[row.parentId].loaded = false
        store.loadData(parentItem, row.parentId)
      } else {
        await this.getTableData()
      }
      this.onUpdateCommonStore()
    },
    // 操作 - 新增
    async onCreate () {
      await this.$refs.DialogFormCategory.open({
        type: `${this.type}Create`,
        formData: this.pageInfo.TableDataParams
      })
      await this.getTableData()
      this.onUpdateCommonStore()
    },
    // 操作 - 新增子项
    async onCreateChild (row, store) {
      let data = await this.$refs.DialogFormCategory.open({
        type: `${this.type}Create`,
        formData: {
          ...this.pageInfo.TableDataParams,
          parentId: row.id,
          parentName: row.categoryName
        }
      })
      if (!data) return false
      this.$set(row, 'hasChildren', true)
      if (store.states.treeData[row.id]) {
        store.states.treeData[row.id].loaded = false
        store.loadData(row, row.id)
      }
      this.onUpdateCommonStore()
    },
    // 操作 - 编辑/编辑子项
    async onEdit (row, store) {
      let parentItem = null
      if (row.parentId) {
        parentItem = {
          parentId: row.parentId,
          parentName: this.getTreeParentItem(row, store).categoryName
        }
      }

      await this.$refs.DialogFormCategory.open({
        type: `${this.type}Edit`,
        formData: {
          id: row.id,
          categoryName: row.categoryName,
          sort: row.sort,
          enabled: row.enabled,
          categoryIconUrl: row.categoryIconUrl,

          // 试题分类、试卷分类
          groupId: row.groupId,

          ...parentItem
        }
      })

      await this.onUpdateChild(row, store)
    },
    // 操作 - 删除
    async onDelete (row, store) {
      await this.$confirm(`此操作将永久删除该${this.pageInfo.type}，是否继续？`, '提示', {
        type: 'warning'
      })

      let { code } = await this.pageInfo.DeleteAPI({
        idList: [row.id],
        // 文库 、 试卷、 试题
        id: row.id
      })
      if (code !== 200) return false
      this.$msg.Delete(this.pageInfo.type)

      await this.onUpdateChild(row, store, true)
    },
    // 操作 - 启用/禁用
    onEnabled ({ id, enabled, categoryName }) {
      this.onBatchEnabled(enabled, [id], categoryName)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled (enabled, idList, name) {
      this.loading = true
      let { code } = await this.pageInfo.EnabledAPI({
        idList,
        // 文库
        id: idList,
        enabled,
        // 组织
        flag: enabled
      })
      this.loading = false
      if (code !== 200) return false
      this.$msg[enabled ? 'Enabled' : 'Disabled'](name, !name)

      name || this.selectionList.forEach(item => {
        this.$set(item, 'enabled', enabled)
      })

      this.onUpdateCommonStore()
    }
  }
}
</script>

<style scoped lang="stylus"></style>