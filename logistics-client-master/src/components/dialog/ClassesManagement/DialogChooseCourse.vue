<template>
  <el-dialog :visible.sync="visible" append-to-body title="选择专题" :close-on-click-modal="false"
             :before-close="close" width="600px" custom-class="selectCourseSpe">

    <TableView :params.sync="filterData" :options="options">
      <el-table 
          :data="tableData" 
          v-loading="loading.table" 
          @row-click="onselect" 
          height="45vh"
          :row-class-name="rowClassName">
        <el-table-column type="index" label="序号" width="88"></el-table-column>
        <el-table-column label="专题名称" prop="courseName" min-width="168"></el-table-column>
        <!-- <el-table-column prop="categoryName" label="课程分类" min-width="168"></el-table-column> -->
      </el-table>
    </TableView>

    <template v-slot:footer>
      <div class="flex end-center margin-right-16">
        <el-button size="small" @click="close">取消</el-button>
        <el-button type="primary" size="small" @click="onSubmit">确定</el-button>
      </div>
    </template>
    
  </el-dialog>
</template>

<script>
import {mapGetters, mapState} from 'vuex'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'
import mxTableView from '@/components/mixins/mxTableView'

export default {
  name: 'DialogChooseCourse',
  mixins: [mxBaseDialog, mxTableView],
  data() {
    return {
      options: {
        side: 'courseName:专题名称'
      },
      // 选中的课程  传出弹窗的数据
      outputData: {},
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    tableDataAPI() {
      return this.$api.ClassesCourses.page
    }
  },
  methods: {   
    // 操作 - 确定提交
    async onSubmit() {
     this.visible = false
     this.$emit('handle', 'submit')
    },
    // 操作 - 选择课程
    onselect(row) {
      this.outputData = row
    },
    rowClassName({row}) {
      if(row.id === this.outputData.id) return 'is-active'
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