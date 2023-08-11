<template>
  <TableView :options="options" :params.sync="filterData">
    <el-table v-loading="loading" :data="tableData" :height="$utils.FullViewHeight(226)">
      <el-table-column prop="displayName" label="显示名称"></el-table-column>
      <el-table-column prop="displayValue" label="显示值"></el-table-column>
    </el-table>
  </TableView>
</template>

<script>
export default {
  name: 'DataOverview',
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
      let { code, data } = await this.$api.ScreenManage.pageDataOverview(this.params)
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