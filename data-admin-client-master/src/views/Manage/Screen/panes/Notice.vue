<template>
  <TableView :options="options" :params.sync="filterData">
    <el-table v-loading="loading" :data="tableData" :height="$utils.FullViewHeight(226)">
      <el-table-column prop="label" label="标签" width="130">
        <template slot-scope="{row}">
          {{ row.label | noticeLabel }}
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题"></el-table-column>
      <el-table-column prop="content" label="内容" show-tooltip-when-overflow></el-table-column>
      <el-table-column prop="publishDate" label="发布时间" width="130"></el-table-column>
    </el-table>
  </TableView>
</template>

<script>
export default {
  name: 'Notice',
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
      let { code, data } = await this.$api.ScreenManage.pageNotice(this.params)
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