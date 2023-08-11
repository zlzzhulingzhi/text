<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="1000px">

    <TableView class="margin-top-16" :options="options" :params.sync="filterData">
      <el-table :data="tableData" v-loading="loading.table" :height="$utils.FullViewHeight(400)">
        <template v-slot:empty>
          <Results></Results>
        </template>
        <el-table-column label="导入时间" prop="importTime" width="160"></el-table-column>
        <el-table-column label="导入操作人" width="160">
          <template slot-scope="{row}">
            {{ row.createName || row.importer }}
          </template>
        </el-table-column>
        <el-table-column label="导入成功/条" prop="success"></el-table-column>
        <el-table-column label="导入失败/条" prop="failure"></el-table-column>
        <el-table-column label="导入报告" width="90">
          <template slot-scope="{row}">
            <el-button type="text" size="medium" @click="onDownload(row)">下载</el-button>
          </template>
        </el-table-column>
      </el-table>
    </TableView>

  </el-dialog>
</template>

<script>
export default {
  name: 'DialogTableImportRecord',
  data() {
    return {
      visible: false,
      loading: {
        table: false,
        download: false
      },
      dialogType: 'Student',
      typeMapping: {
        // Student: { title: '导入记录', pageApi: this.$api.ImportRecord.page, params: { businessType: 1 }, downloadApi: this.$api.ImportRecord.downloadReport },
        // Employee: { title: '导入记录', pageApi: this.$api.ImportRecord.page, params: { businessType: 2 }, downloadApi: this.$api.ImportRecord.downloadReport },
        // Question: { title: '导入记录', pageApi: this.$api.Question.importRecord, params: {}, downloadApi: this.$api.Question.downloadReport }
      },
      options: {
        total: 0
      },
      filterData: {},
      tableData: []
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    params() {
      return {
        ...this.filterData,
        ...this.dialogInfo.params
      }
    }
  },
  methods: {
    open(data = {}) {
      this.visible = true
      this.dialogType = data.type || 'Student'
      this.$nextTick(() => {
        this.getTableData()
      })
    },
    close() {
      this.visible = false
    },
    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.dialogInfo.pageApi(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records || []
      this.options.total = data.total
    },
    async onDownload(row) {
      if (this.loading.download) return false
      this.loading.download = true
      let {code} = await this.dialogInfo.downloadApi({
        id: row.id
      })
      this.loading.download = false
      if (code !== 200) return false
      this.$message.success('下载成功')
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>