<template>
  <el-dialog :visible.sync="visible" append-to-body :close-on-click-modal="false"
             :before-close="close" top="5vh" width="600px">

    <template slot="title">
      <span class="text-3 text-bold font-16 margin-right-8">添加学员</span>
      <el-checkbox v-model="isThisCourse">筛选本班级学员</el-checkbox>
    </template>

    <TableView :params.sync="filterData" :options="options" class="is-overflow">

      <el-table class="d1" :data="tableData" ref="multipleTable" v-loading="loading.table" @selection-change="selectionList = $event" height="45vh">
        <el-table-column type="selection" width="88"  :selectable="item => !selectedStudentsIdList?.includes(item.studentId)"></el-table-column>
        <el-table-column type="index" label="序号" width="88"> </el-table-column>
        <el-table-column prop="realName" label="学员姓名" min-width="100"> </el-table-column>
        
      </el-table>

    </TableView>

    <template v-slot:footer>
      <el-button size="small" @click="close">取消</el-button>
      <el-button type="primary" size="small" @click="onSubmit" :loading="loading.submit">确定</el-button>
    </template>

  </el-dialog>
</template>

<script>
import mxTableView from '@/components/mixins/mxTableView'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'

export default {
  name: 'DialogAllowancePickStudents',
  components: {
  },
  mixins: [mxTableView, mxBaseDialog],
  data() {
    return {
      options: {
        side: 'realName:学生姓名'
      },
      loading: {
        table: true,
        submit: false
      },
      clazzId: null,
      isThisCourse: true,
      byCourseId: null,
      selectedStudentsIdList: null,

      influenceWatch: undefined
    }
  },
  computed: {
    params() {
      return {
        ...this.filterData,
        byCourseId: this.isThisCourse ? 1 : 0,
        clazzId: this.clazzId,
        influenceWatch: this.influenceWatch
      }
    },
    outputData() {
      return this.selectionList
    }
  },
  methods: {
    async getTableData() {
      // mixins  中的watch会立即触发，此时调用接口是无用的，故阻拦掉
      if(!this.visible) return false

      this.loading.table = true
      let {code, data} = await this.$api.classesAddStudents.pageForAllowance({
        ...this.params,
        // clazzId: this.clazzId,
      })
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records  
      this.options.total = data.total
    },
    
    // 操作 - 打开弹窗
    open(data = {}) {
      this.tableData = null
      this.influenceWatch = undefined
      this.selectionList = null

      this.isThisCourse = true

      this.clazzId = data.formData?.id

      // 已选择的学员的列表
      this.selectedStudentsIdList = data.selectedStudents?.map(item => item.studentId)

      this.visible = true

      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.outputData)
          reject('取消')
        })
      })
    },

    // 确定提交
    async onSubmit () {
      this.influenceWatch = null
      this.visible = false
      this.$emit('handle', 'submit')
    },

    // 操作 - 关闭弹窗
    close() {
      this.influenceWatch = null
      this.visible = false
      this.$emit('handle', 'close')
    },
  }
}
</script>

<style scoped lang="stylus">
</style>