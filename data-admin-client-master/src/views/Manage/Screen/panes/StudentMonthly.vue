<template>
  <TableView :options="options" :params.sync="filterData">
    <el-table v-loading="loading" :data="tableData" :height="$utils.FullViewHeight(226)">
      <el-table-column prop="year" label="时间">
        <template slot-scope="{row}">
          {{ row.year }}年{{ row.month }}月
        </template>
      </el-table-column>
      <el-table-column prop="num" label="数量"></el-table-column>
    </el-table>
  </TableView>
</template>

<script>
export default {
  name: 'StudentMonthly',
  data() {
    return {
      loading: false,
      options: {
        total: 0
      },
      filterData: {},
      tableData: [],
      timer: null
    }
  },
  computed: {
    params() {
      return {
        ...this.filterData
      }
    }
  },
  watch: {
    params: {
      handler() {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    }
  },
  methods: {
    async getTableData() {
      this.loading = true
      let { code, data } = await this.$api.ScreenManage.pageStatStudentMonthly(this.params)
      this.loading = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>