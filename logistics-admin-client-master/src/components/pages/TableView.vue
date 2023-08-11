<template>
  <div class="table-view">
    <!--筛选条件-->
    <FilterControl ref="FilterControl" v-if="$slots.other || $slots.main || $slots.side || options.main || options.side"
                   class="margin-bottom-16" :main="options.main"
                   :side="options.side" :label-width="options.labelWidth" :filterData.sync="filterData">

      <template v-slot:other>
        <slot name="other"></slot>
      </template>
      <template v-slot:main>
        <slot name="main"></slot>
      </template>
      <template v-slot:side>
        <slot name="side"></slot>
      </template>
    </FilterControl>
    <!--表格+分页器-->
    <el-card>
      <slot name="default"></slot>
      <div class="flex between-center" v-if="options.total !== undefined">
        <div>
          <el-checkbox class="check-all" v-if="ElSelection" @change="onSelectionChange"
                       v-model="isCheckAll" :indeterminate="indeterminateCheckAll">
            全选当前页
          </el-checkbox>
          <slot name="footer"></slot>
        </div>
        <PaginationControl class="padding-top-14 padding-bottom-16" :total="options.total" :pagination.sync="pagination"
                           :pageSizes="options.pageSizes"></PaginationControl>
      </div>
    </el-card>
  </div>
</template>

<script>
import PaginationControl from '@/components/form/PaginationControl'
import FilterControl from '@/components/form/FilterControl'

export default {
  name: 'TableView',
  components: {
    FilterControl,
    PaginationControl
  },
  mounted() {
    this.initSelectionCheck()
  },
  data() {
    return {
      filterData: {},
      pagination: {
        pageNum: 1,
        pageSize: 999
      },


      // 全选 状态
      isCheckAll: false,
      indeterminateCheckAll: false,

      // 默认插槽内的 VNode
      ElTable: null,
      ElSelection: null
    }
  },

  props: {
    options: {
      type: Object,
      default() {
        return {
          // 主体筛选条件 - 下拉框、时间控件
          main: {},
          // 侧边筛选条件 - 搜索框
          side: {}
        }
      }
    }
  },
  computed: {
    params() {
      return {
        ...this.filterData,
        current: this.pagination.pageNum,
        size: this.pagination.pageSize
      }
    }
  },

  watch: {
    params: {
      deep: true,
      immediate: true,
      handler(val) {
        this.$emit('update:params', val)
      }
    },
    ['ElTable.componentInstance.selection'](selection) {
      if (!selection) return false
      this.indeterminateCheckAll = false
      // 0
      if (!selection.length) return this.isCheckAll = false
      // all
      if (selection.length === this.ElTable.componentInstance.data.length) return this.isCheckAll = true
      // 半选
      this.indeterminateCheckAll = true
    }
  },

  methods: {
    // 初始化获取 默认插槽 内的el-table, 生成 selection 全选控件
    initSelectionCheck() {
      try {
        // 获取VNode - Table
        this.ElTable = this.$slots.default.find(node => /ElTable$/.test(node.tag))
        if (!this.ElTable) return this.ElSelection = null

        // 获取VNode - Selection
        this.ElSelection = this.ElTable.componentOptions.children.find(node => {
          return node.componentOptions && (node.componentOptions.propsData.type === 'selection')
        })
      } catch (e) {
      }
    },
    // selection 全选控件 change
    onSelectionChange(isCheck) {
      let {clearSelection, toggleAllSelection} = this.ElTable.componentInstance
      clearSelection()
      if (isCheck) toggleAllSelection()
    },
    // 初始化数据
    reset() {
      this.$refs.FilterControl && this.$refs.FilterControl.reset()
    }
  }
}
</script>

<style scoped lang="stylus">
.table-view
  min-width 0

.check-all
  padding 0 12px 0 17px

  >>> .el-checkbox__label
    padding-left 8px
</style>