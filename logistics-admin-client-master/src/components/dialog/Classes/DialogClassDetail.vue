<template>
  <el-dialog :visible.sync="visible" append-to-body title="班级详情" :close-on-click-modal="false"
             :before-close="close" width="1000px" class="popover-dialog">
  
    <el-card >
      <div class="flex around-center width-100p font-16">

        <div class="flex column infoCard">
          <div>班级名称：{{classInfo.name}}</div>
          <div>负责教师：{{classInfo.employeeName}}</div>
          <div>开课日期：{{classInfo.startDate | formatTime('YYYY-MM-DD')}}&nbsp;至&nbsp;{{classInfo.endDate | formatTime('YYYY-MM-DD')}}</div>
          <div v-if="classInfo.building">上课教室：{{classInfo.building | dormitoryUnit}}{{ classInfo.roomNo }}({{ classInfo.roomType |  classTypeInfo}})</div>
          
        </div>  

  

        <div class="flex column infoCard">
          <!-- <div>计划课次：{{classInfo.lessonNum}}</div> -->
          <!-- <div>计划人数：{{classInfo.studentNum}}</div> -->
          <div>专题名称：{{classInfo.courseName}}</div>
          <div>班级课时：{{ classInfo.lessonNum }}</div>
          <div>所属机构：{{ classInfo.orgName }}</div>
          <div v-if="classInfo.building" class="height-20"></div>
        </div>

      </div>
    </el-card>

    <TableView :options="options" :params.sync="filterData" class="margin-top-16">

      <!-- <template v-slot:footer>
        <el-button :disabled="!selectionList.length" size="small" @click="onBatchDelete(selectionList)">批量移除</el-button>
      </template> -->
      <template v-slot:main>
        <el-button class="margin-bottom-8 margin-left-0" type="primary" size="small" style="order: 210" @click="onSeeClasses">课表</el-button>
      </template>
      
      <el-table :data="tableData" :height="$utils.FullViewHeight(375)" v-loading="loading.table" @selection-change="selectionList = $event.map(item=>item.id)">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>

        <!-- <el-table-column type="selection" width="55"></el-table-column> -->
        <el-table-column type="index" label="序号" width="55"></el-table-column>
        <el-table-column label="学员姓名" prop="studentName" min-width-100>
          <template slot-scope="{ row }">
            <span class="pointer" @click="onSeeDetail(row.studentId)">{{ row.studentName || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="电话号码" prop="phone" min-width-100></el-table-column>
      
        <!-- <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" class="margin-left-8" @click="onDelete(row)">移除</el-button>
            <el-button type="text" class="margin-left-8" @click="onDetail(row)">详情</el-button>
          </template>
        </el-table-column> -->
      </el-table>
    </TableView>
    

    <!-- <template v-slot:footer>
      <el-button size="small" @click="onClose">取消</el-button>
      <el-button type="primary" size="small" @click="onSubmit" :loading="loading.submit">确定</el-button>
    </template> -->

    <!-- 课表弹窗 -->
    <DialogCourseList ref="DialogCourseList"></DialogCourseList>
    <DialogCourseListNew ref="DialogCourseListNew"></DialogCourseListNew>

    <DialogOrgStuInfo ref="DialogOrgStuInfo"></DialogOrgStuInfo>

  </el-dialog>
</template>

<script>
import { mapState, mapGetters } from 'vuex'
import mxTableView from '@/components/mixins/mxTableView'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'
// 课表弹窗
import DialogCourseList from '@/components/dialog/Classes/DialogCourseList.vue'
import DialogCourseListNew from '@/components/dialog/Classes/DialogCourseListNew.vue'
import DialogOrgStuInfo from '@/components/dialog/Student/DialogOrgStuInfo'

export default {
  name: 'DialogClassDetail',
  components: {
    DialogCourseList,
    DialogCourseListNew,
    DialogOrgStuInfo
  },
  mixins: [mxTableView, mxBaseDialog],
  data() {
    return {
      visible: false, // 弹窗开关
      options: {
        side: 'realName:学员姓名,phone:电话号码'
      },
     
      classInfo: {},
    }
  },
  computed: {
    params() {
      return {
        ...this.filterData,
        id: this.pParams.id
      }
    },
    tableDataAPI() {
      return this.$api.LogisticsOrg.orgClassDetail
    },
    ...mapGetters({
      classTypeInfo: 'common/classTypeInfo',
      dormitoryUnit: 'common/dormitoryUnit',
    }),
  },
  methods: {
    // 操作 - 初始化数据
    // initData() {
    //   this.selectionList = []
    // },
    // 获取页面所需数据
    async getTableData() {
      // 控制  打开弹窗后才调用接口
      if(!this.visible) return false

      let data = await this.callTableDataAPI()
      if (data) {
        this.classInfo = data
        this.tableData = data.clazzStudentPageResponseList.records
        this.options.total = data.clazzStudentPageResponseList.total
      }
    },
    // 操作 - 关闭弹窗
    close() {
      this.visible = false
      this.selectionList = []
      this.pParams = {}
      this.classInfo = {}
      this.$emit('handle', 'close')
    },
    // 移除
    async onDelete(item) {
      await this.$confirm(`确认移除学员“${item.studentName}”吗？`, {
        title: '移除确认',
        type: 'warning'
      })
      this.loading.table = true
      let { code } = await this.$api.classesStudents.delete({
        idList: [item.id]
      })
      this.loading.false = false
      if(code !== 200) return false
      this.$message.success('移除成功')
      this.getTableData()
    },
    // 批量移除
    async onBatchDelete(params) {
      await this.$confirm(`确认移除${params.length}个学员吗`, {
        title: '批量移除确认',
        type: 'warning'
      })
      this.loading.table = true
      let { code } = await this.$api.classesStudents.delete({
        idList: params
      })
      this.loading.false = false
      if(code !== 200) return false
      this.$message.success('移除成功')
      this.getTableData()
    },
    // 操作 - 详情
    onDetail(params) {
      // this.$router.push({
      //   name: 'studentClockDetail',
      //   query: {
      //     id: params.clazzId,
      //     memberId: params.memberId,
      //     studentName: params.studentName
      //   },
      // })
      this.$utils.WindowOpenInParentFrame(`/ClassesManagement/studentClockDetail&id=${params.clazzId}&memberId=${params.memberId}&studentName=${params.studentName}`)
    },
    // 课表
    onSeeClasses(row) {
      this.$refs.DialogCourseListNew.open({
        formData: {
          id: this.pParams.id,
          orgId: this.pParams.orgId,
          className: this.pParams.name
        }
      })
    },
    // 查看学员详情
    async onSeeDetail(params) {
      let returnInfo = await this.$refs.DialogOrgStuInfo.open({
        id: params
      })
    }
  }
}
</script>

<style scoped lang="stylus">
.infoCard
  div
    margin-bottom 8px
.popover-dialog
  >>>.el-dialog
    min-height: 700px
</style>