<template>
  <el-dialog :visible.sync="visible" append-to-body title="选择班级" :close-on-click-modal="false" :before-close="close" top="5vh" width="600px">

    
    <TableView :params.sync="filterData" :options="options" class="is-overflow">

      <el-table :data="tableData" v-loading="loading.table" @row-click="onSelect" :row-class-name="rowClassName" >
        <!-- <el-table-column type="selection" width="88"></el-table-column> -->
        <el-table-column type="index" label="序号" width="120"> </el-table-column>
        <el-table-column prop="name" label="班级名称" min-width="168"></el-table-column>
      </el-table>

    </TableView>
    

    <template v-slot:footer>
      <div class="flex end-center">
        <el-button size="medium" @click="close">取消</el-button>
        <el-button type="primary" size="medium" class="margin-right-14" @click="onSubmit" :loading="loading.submit">确定</el-button>
      </div>
    </template>


  </el-dialog>
</template>
  
<script>
import mxTableView from '@/components/mixins/mxTableView'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'

export default {
  name: 'DialogPickClass',
  components: {
  },
  mixins: [mxTableView, mxBaseDialog],
  data () {
    return {
      options: {
        side: 'name:班级名称'
      },

      loading: {
        submit: false,
        table: false
      },
      // 选中的班级  用以传出弹窗外的数据
      outputData: {}
    }
  },

  methods: {
    onSelect (row) {
      // this.selectedClass = row
      this.outputData = row
    },
    async getTableData () {
      // 防止已进入  课程资助申请页面而没有打开弹窗时触发此接口
      if (!this.visible) return false

      this.loading.table = true
      let { code, data } = await this.$api.Classes.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    // 确定提交
    async onSubmit () {
      this.visible = false
      this.$emit('handle', 'submit')
    },
    rowClassName ({ row }) {
      if (row.id === this.outputData.id) return 'is-active'
    },
    
  }
}
</script>
  
  <style scoped lang="stylus">
  /deep/ .is-overflow
    .el-card
      overflow initial
  >>> .el-dialog
    .el-dialog__body
      padding-top 0
      padding-bottom 0
      .is-active
        td
          background-color BACKGROUND_COLOR !important
  // >>> p
  //   margin 0 !important
</style>