<template>
  <el-container class="height-100p">

    <TreeSelectorAsSideBar ref="TreeSelectorAsSideBar" type="AllStudentDept"
                           :visible.sync="treeVisible" :selection.sync="categorySelection"></TreeSelectorAsSideBar>

    <TableView class="flex-1 overflow-auto" :options="options" :params.sync="filterData">
      <template v-slot:main>
        <div class="flex center-center margin-bottom-8" v-if="!treeVisible">
          <div class="font-16 padding-8 padding-left-0 el-icon-s-unfold pointer text-main"
               @click="treeVisible = true"></div>
        </div>
      </template>

      <el-table class="margin-top-16" :data="tableData"
                :height="$utils.FullViewHeight(188)"
                v-loading="loading.table">
        <template v-slot:empty>
          <Results></Results>
        </template>

        <el-table-column type="index" label="序号" width="55"></el-table-column>

        <el-table-column label="学员姓名" prop="realName" min-width="110"></el-table-column>
        <el-table-column label="手机号" prop="phone" min-width="110"></el-table-column>
        <el-table-column label="性别" prop="sex" width="100">
          <template slot-scope="{row}">
            {{ row.sex | sex }}
          </template>
        </el-table-column>
        <el-table-column label="注册时间" prop="createTime" min-width="110"></el-table-column>
        <el-table-column label="状态" prop="enabled" width="66">
          <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
        </el-table-column>
      </el-table>
    </TableView>
  </el-container>
</template>

<script>
import TreeSelectorAsSideBar from '@/components/form/TreeSelectorAsSideBar'

export default {
  name: 'Student',
  components: {
    TreeSelectorAsSideBar
  },
  data() {
    return {
      options: {
        total: 0,
        side: 'phone:学员手机号'
      },
      tableData: [],
      filterData: {},

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
    permissions() {
      return this.$route.meta.childPermissions || {}
    },
    params() {
      if (!this.categorySelection) return this.filterData
      let {id: deptId, orgId} = this.categorySelection
      let deptIdList = []
      if (typeof deptId === 'number') deptIdList = this.$utils.ArrayFlat([this.categorySelection]).map(item => item.id)
      return {
        ...this.filterData,
        orgId,
        deptIdList
      }
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
    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.$api.Student.pageAdmin(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    }
  }
}
</script>

<style scoped lang="stylus">
</style>
