<template>
  <TableView :options="options" :params.sync="filterData">
    <el-table v-loading="loading" :data="tableData" :height="$utils.FullViewHeight(226)">
      <el-table-column prop="dataName" label="教室名称"></el-table-column>
      <el-table-column prop="freeNum" label="空闲数量"></el-table-column>
      <el-table-column prop="usingNum" label="已使用数量"></el-table-column>
    </el-table>
  </TableView>
</template>

<script>
export default {
  name: 'StatDataDynamicClass',
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
        module: 'classroom',
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
      let { code, data } = await this.$api.ScreenManage.pageStatDataDynamic(this.params)
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