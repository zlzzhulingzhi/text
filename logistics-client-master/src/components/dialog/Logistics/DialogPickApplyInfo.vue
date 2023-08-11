<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false" :before-close="close" :width="dialogInfo.dialogWidth" custom-class="selectClassroom">

    <template v-if="dialogType === 'choseClassroom'">
      <TableView :params.sync="filterData" :options="dialogInfo.options" class="is-overflow">

        <el-table class="d1" :data="tableData" ref="multipleTable" v-loading="loading.table" @selection-change="selectionList = $event" height="62vh" @row-click="handleRowClick" :row-class-name="rowClassName">
          <!-- <el-table-column type="selection" width="88" :selectable="item => !choseClassroomIdList?.includes(item.id)" v-if="isMultipleChose"></el-table-column> -->
          <el-table-column type="selection" width="88" v-if="isMultipleChose"></el-table-column>
          <el-table-column type="index" label="序号" width="88"> </el-table-column>
          <el-table-column prop="building" label="单元" min-width="100"> </el-table-column>
          <el-table-column prop="floor" label="楼层" min-width="100"> </el-table-column>
          <el-table-column prop="roomNo" label="教室编号" min-width="168"></el-table-column>
          <el-table-column prop="seats" label="座位数" min-width="168"></el-table-column>
          <el-table-column prop="roomType" label="教室类型" min-width="168"></el-table-column>
          <el-table-column label="操作" width="88">
            <template slot-scope="{row}">
              <el-button type="text" size="small" @click.stop="seeDetailInfo(row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>

      </TableView>
    </template>

    <template v-if="dialogType === 'choseClass'">
      <TableView :params.sync="filterData" :options="dialogInfo.options" class="is-overflow">

        <el-table :data="tableData" v-loading="loading.table" @row-click="onSelect" :row-class-name="rowClassName">
          <!-- <el-table-column type="selection" width="88"></el-table-column> -->
          <el-table-column type="index" label="序号" width="88"> </el-table-column>
          <el-table-column prop="courseName" label="专题名称" min-width="168"></el-table-column>
          <!-- <el-table-column prop="categoryName" label="专题分类" min-width="168"></el-table-column> -->
        </el-table>

      </TableView>
    </template>

    <template v-if="dialogType === 'choseDormitoryType'">
      <TableView :params.sync="filterData" :options="dialogInfo.options" class="is-overflow">

        <el-table :data="tableData" ref="dormitoryTable" v-loading="loading.table" @selection-change="selectionList = $event" height="56vh" @row-click="">
          <el-table-column type="selection" min-width="88" :selectable="item => !choseDormitoryTypeIdList?.includes(item.dictValue)"></el-table-column>
          <el-table-column type="index" label="序号" width="88"> </el-table-column>
          <el-table-column prop="dictValue" label="宿舍类型" min-width="268">
            <template slot-scope="{row}">
              {{ row.dictValue }}
            </template>
          </el-table-column>
          <!-- <el-table-column prop="apartmentResidue" label="剩余数量" min-width="168" ></el-table-column> -->
          <!-- <el-table-column prop="floor" label="楼层" min-width="168"></el-table-column>
          <el-table-column prop="building" label="单元" min-width="168"></el-table-column>
          <el-table-column prop="roomNo" label="房号" min-width="168"></el-table-column> -->
        </el-table>

      </TableView>
    </template>

    <template v-slot:footer>
      <div class="flex end-center">
        <el-button type="primary" size="medium" @click="onSubmit" :loading="loading.submit">确定</el-button>
        <el-button class="margin-right-14" size="medium" @click="close">取消</el-button>
      </div>
    </template>

    <DialogClassroomDetail ref="DialogClassroomDetail"></DialogClassroomDetail>

  </el-dialog>
</template>
  
  <script>
import { mapActions, mapGetters } from 'vuex'
// import DialogClassroomDetail from '@/components/dialog/Logistics/DialogClassroomDetail'
import DialogClassroomDetail from '@/components/dialog/ClassesManagement/DialogClassroomDetail'

export default {
  name: 'DialogPickApplyInfo',
  components: {
    DialogClassroomDetail
  },
  data() {
    return {
      visible: false, // 弹窗开关

      dialogType: 'choseClassroom',
      typeMapping: {
        choseClassroom: {
          title: '选择教室',
          dialogWidth: '1200px',
          // 模拟后台接口
          dataAPI: this.$api.Classroom.managePage,
          // 后台接口固定参数
          params: null,
          // 初始参数与传递参数
          formData: {
            paperId: null
          },
          options: {
            total: 0,
            main: {
              roomType: {
                label: '教室类别',
                options: 'classTypeInfo'
              }
            },
            side: 'roomNo:教室编号'
          }
        },
        choseClass: {
          title: '选择专题',
          dialogWidth: '600px',
          // 模拟后台接口
          dataAPI: this.$api.Course.page,
          // 后台接口固定参数
          params: null,
          // 初始参数与传递参数
          formData: {
            paperId: null
          },
          options: {
            total: 0,
            side: 'courseName:专题名称'
          }
        },
        choseDormitoryType: {
          title: '选择宿舍类型',
          dialogWidth: '600px',
          // 模拟后台接口
          dataAPI: this.$api.QueryInfo.info,
          // 后台接口固定参数
          params: {
            code: 'dormitory'
          },
          // 初始参数与传递参数
          formData: {
            paperId: null
          },
          options: {
            total: 0
          }
        }
      },

      categoryId: null,
      filterData: {},

      tableData: [],

      formData: {},
      loading: {
        submit: false,
        table: false
      },
      timer: {
        table: null
      },
      selectionList: null,
      choseClassList: null,
      choseClassroomIdList: null,
      choseDormitoryTypeIdList: null,

      // 选择教室是否多选
      isMultipleChose: true,

      // 何处打开的弹窗
      source: null
    }
  },
  computed: {
    ...mapGetters({
      dormitoryType: 'common/dormitoryType',
      classType: 'common/classType',
      classTypeInfo: 'common/classTypeInfo'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    params() {
      // let categoryIds = []
      // if (this.categoryId) categoryIds.push(this.categoryId)
      return {
        ...this.filterData,
        ...this.dialogInfo.params
        //   categoryIds,
        //   pageNum: this.filterData.current,
        //   pageSize: this.filterData.size
      }
    }
  },
  watch: {
    params: {
      deep: true,
      handler() {
        clearTimeout(this.timer.table)
        this.timer.table = setTimeout(() => {
          this.getTableData()
        })
      }
    }
  },

  methods: {
    ...mapActions({
      getPaperQuestionList: 'paper/getPaperQuestionList'
    }),
    // 操作 - 选专题的行点击
    onSelect(row) {
      this.formData = row
      this.selectionList = row
    },
    async getTableData() {
      if (!this.visible) return false
      this.loading.table = true
      let { code, data } = await this.dialogInfo.dataAPI(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = this.dialogType === 'choseDormitoryType' ? data[0].children : data.records
      this.dialogInfo.options.total = data.total
    },
    // 打开弹窗
    open(data = {}) {
      // 清空已选，防止已选的数据被带到新的选项
      this.selectionList = null
      this.formData.id = null

      this.dialogType = data.type
      this.source = data.source
      this.choseClassList = data.choseClassList
      // 获取已选的教室的id
      if(this.choseClassList) this.choseClassroomIdList = this.choseClassList.map(item => item.classroomId)
      
      // 获取已选的宿舍类型id
      this.choseDormitoryTypeIdList = data.choseDormitoryTypeList?.map(item => item.roomType)

      // 选择教室是否多选
      if(data.isMultipleChose === false) this.isMultipleChose = false

      this.visible = true
      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.selectionList)
          reject('取消')
        })
      })
    },
    // 关闭弹窗
    close() {
      this.dialogType = ''
      this.visible = false
      this.isMultipleChose = true
      this.$emit('handle', 'close')
    },
    // 确定提交
    async onSubmit() {
      this.dialogType = ''
      this.visible = false
      this.isMultipleChose = true
      this.$emit('handle', 'submit')
    },
    rowClassName({ row }) {
      if (row.id === this.formData.id) return 'is-active'
    },

    // seeDetailInfo(item) {
    //   this.$refs.DialogClassroomDetail.open({
    //     data: item
    //   })
    // },
    seeDetailInfo(item) {
      this.$refs.DialogClassroomDetail.open({
        formData: item
      })
    },
    // 操作 - 选教室点击行
    handleRowClick(row, column, event) {
      if(this.source === 'formApplyInfo') return false
      if(this.isMultipleChose) this.$refs.multipleTable.toggleRowSelection(row)
      else {
        this.formData = row
        this.selectionList = row
      }
    },
    // 操作 - 选宿舍的行点击
    dormitoryHandleRowClick(row, column, event) {
      this.$refs.dormitoryTable.toggleRowSelection(row)
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
      // padding-top 0
      // padding-bottom 0
      .is-active
        td
          background-color BACKGROUND_COLOR !important
  >>> p
    margin 0 !important
</style>