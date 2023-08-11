<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <template v-if="/^CertTemplate$/.test(dialogType)">
      <TableView :options="options" :params.sync="filterData">
        <el-table :data="tableData" v-loading="loading.table" :row-class-name="rowClassName"
                  @row-click="selection = $event">
          <template v-slot:empty>
            <Results></Results>
          </template>
          <el-table-column label="编号" type="index" width="80" ></el-table-column>
          <el-table-column label="证书模板名称" prop="name"></el-table-column>
          <el-table-column label="创建时间" prop="createTime" min-width="120"></el-table-column>
        </el-table>
      </TableView>
    </template>

    <template v-slot:footer>
      <el-button type="primary" size="medium" @click="onSubmit" :loading="loading.submit">确定</el-button>
      <el-button class="margin-right-14" size="medium" @click="close">取消</el-button>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: 'DialogCertTemplateSelect',
  data() {
    return {
      visible: false, // 弹窗开关

      dialogType: 'CertTemplate',
      typeMapping: {
        CertTemplate: {
          title: '选择模板'
        }
      },

      selection: {},

      options: {
        total: 0,
        side: 'name:关键字'
      },
      filterData: {},
      tableData: [],

      loading: {
        submit: false,
        table: false
      },
      timer: {
        table: null
      }
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    outputData() {
      return this.selection
    }
  },

  watch: {
    filterData: {
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
      let {code, data} = await this.$api.StandardTemplate.page({
        ...this.filterData,
        enabled: 1
      })
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    rowClassName({row}) {
      return `pointer ${row.id === this.selection.id ? 'is-active' : ''}`
    },

    // 打开弹窗
    async open(data = {}) {
      this.dialogType = data.type
      this.visible = true
      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.outputData)
          reject('取消')
        })
      })
    },
    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle', 'close')
    },
    // 确定提交
    async onSubmit() {
      this.visible = false
      this.$emit('handle', 'submit')
    }
  }
}
</script>


<style scoped lang="stylus">
>>> .el-dialog
  .el-dialog__body
    padding-top 0
    padding-bottom 0

    .is-active
      td
        background-color BACKGROUND_COLOR !important
</style>