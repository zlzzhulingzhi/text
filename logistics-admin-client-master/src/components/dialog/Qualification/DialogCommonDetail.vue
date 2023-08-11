<template>
  <div>
    <el-dialog :title="dialogInfo.title" :visible.sync="visible" :close-on-click-modal="true" width="800px" append-to-body :before-close="close">
      <div v-loading="loading" v-if="dialogInfo.type === 'CourseApply'">
        <div class="section-item">
          <div class="title">资助项目</div>
          <el-row class="row-item">
            <el-col :span="8" class="item-col">
              <span class="col-label">项目编号：</span><span>{{ applyInfo.projectNo || '--' }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">项目名称(选择班级)：</span><span>{{ applyInfo.projectName }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">补贴费用(合计)：</span><span>{{ applyInfo.totalAllowanceFee }}</span>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="24" class="item-col">
              <span class="col-label">补贴明细：</span>
            </el-col>
          </el-row>
          <el-table class="margin-top-4" :data="applyInfo.applyAllowanceFeeDetailResponses">
            <el-table-column label="培训场所资助租金" prop="roomType" min-width="88"></el-table-column>
            <el-table-column label="学员就餐资助费用" prop="roomType" min-width="88"></el-table-column>
            <el-table-column label="学员交通资助费用" prop="roomType" min-width="88"></el-table-column>
            <el-table-column label="学员学费资助金额" prop="roomType" min-width="88"></el-table-column>
          </el-table>
        </div>
        <div class="section-item">
          <div class="title">课程详情内容</div>
          <el-row class="row-item">
            <el-col :span="6" class="item-col">
              <span class="col-label">实际参训人数：</span><span>{{ applyInfo.studentNum }}</span>
            </el-col>
            <el-col :span="6" class="item-col">
              <span class="col-label">实际培训时间：</span><span>{{ applyInfo.startDate}} -  {{applyInfo.endDate}}</span>
            </el-col>
            <el-col :span="6" class="item-col">
              <span class="col-label">参加考试人数：</span><span>{{ applyInfo.examNum  || '暂无'}}</span>
            </el-col>
            <el-col :span="6" class="item-col">
              <span class="col-label">考试时间：</span><span>{{ applyInfo.examDate  || '暂无'}}</span>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="24" class="item-col">
              <span class="col-label">受资助学员列表：</span>
            </el-col>
          </el-row>
          <el-table class="margin-top-4" :data="applyInfo.applyAllowanceStudentDetailResponseList">
            <el-table-column label="学员姓名" prop="studentName" min-width="88"></el-table-column>
            <el-table-column label="单位" prop="unitName" min-width="88"></el-table-column>
            <el-table-column label="学时" prop="lessonNum" min-width="88"></el-table-column>
            <el-table-column label="缴纳金额" prop="payAmount" min-width="88"></el-table-column>
            <el-table-column label="考试成绩" prop="examScore" min-width="88"></el-table-column>
          </el-table>
        </div>
        <div class="section-item">
          <div class="title">资料附件</div>
          <el-table class="margin-top-4" :data="applyAttachInfo">
            <el-table-column label="附件名称" prop="attachName" min-width="88"></el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template slot-scope="{ row }">
                <el-button type="text" size="small" @click.stop="downLoad(row)">下载</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div v-loading="loading" v-else-if="dialogInfo.type === 'ActivityApply'">
        <div class="section-item">
          <div class="title">基本信息</div>
          <el-row class="row-item">
            <el-col :span="8" class="item-col">
              <span class="col-label">活动名称：</span><span>{{ applyInfo.activityName || '--' }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">活动主题：</span><span>{{ applyInfo.activityTheme }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">经费预算：</span><span>{{ applyInfo.budgetAmount }}</span>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="8" class="item-col">
              <span class="col-label">举办场地：</span><span>{{ applyInfo.activityAddress }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">举办时间：</span><span>{{ applyInfo.activityDate }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">活动规模：</span><span>{{ applyInfo.activityScale }}</span>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="8" class="item-col">
              <span class="col-label">指导单位：</span><span>{{ applyInfo.guideUnits }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">主办单位：</span><span>{{ applyInfo.activityUnits }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">承办单位：</span><span>{{ applyInfo.undertakerUnits }}</span>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="8" class="item-col">
              <span class="col-label">协办单位：</span><span>{{ applyInfo.partnerUnits }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">重要嘉宾：</span><span>{{ applyInfo.guests }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
            </el-col>
          </el-row>
        </div>
        <div class="section-item">
          <div class="title">资料附件</div>
          <el-table class="margin-top-4" :data="tableClassRoomData">
            <el-table-column label="资料名称" prop="roomType" min-width="88"></el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template slot-scope="{ row }">
                <el-button type="text" size="small" @click.stop="downLoad(row)">下载</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      detailData: {}, //详情信息
      tableClassRoomData: [], //开团成员列表
      tableDormitoryData: [], //参团成员列表
      loading: false,
      load: {
        tableO: false,
        tableF: false
      },
      visible: false,
      id: null, //申请信息ID
      applyInfo: [], //信息
      filterData: {},
      applyAttachInfo:[],
      sevm:{},
      dialogType: 'CourseApply',
      typeMapping: {
        CourseApply: {
          title: `课程申请信息详情`,
          type: 'CourseApply',
          detailAPI: this.$api.Approve.allowanceDetail,
          attachAPI: this.$api.Approve.allowanceList,
        },
        ActivityApply: {
          title: `活动申请信息详情`,
          type: 'ActivityApply',
          detailAPI: this.$api.Approve.activityDetail,
          attachAPI: this.$api.Approve.attachList,
        }
      }
    }
  },
  computed: {
    ...mapGetters({
      orderStatus: 'common/orderStatus'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    // AllowanceFeeList(){
    //   return 
    //     return item.allowanceFee
    //   })
    // }
  },
  methods: {
    async open(data = {}) {
      console.log(data)
      this.detailData = data
      this.dialogType = data.type
      this.id = data.id
      this.getDetailData()
      this.getAttachlData() 
      this.visible = true
    },
    async getDetailData() {
      const _params = {
        id: this.id
      }
      this.loading = true
      let { code, data } = await this.dialogInfo.detailAPI(_params)
      this.loading = false
      if (code !== 200) return false
      this.applyInfo = this.dialogInfo.type === 'CourseApply' ? data.applyAllowanceDetailResponse : data.applyActivity
      let newArr = this.applyInfo.applyAllowanceFeeDetailResponses.map((item,index) => {
        return item.allowanceFee
     })
    console.log(newArr);
    },
     // 获取附件信息列表
     async getAttachlData() {
      const _params = {
        id: this.id
      }
      this.loading = true
      let { code, data } = await this.dialogInfo.attachAPI(_params)
      this.loading = false
      if (code !== 200) return false
      this.applyAttachInfo = data
    },
    // 操作 - 下载
    downLoad(row) {
      console.log(row)
    },
    close() {
      this.detailData = null
      this.dialogType = null
      this.applyInfo = null
      this.applyAttachInfo = null
      this.id = null
      this.visible = false
    }
  }
}
</script>

<style lang="stylus" scoped>
.spell-detail-wrap, .section-item
  +.section-item
    margin-top 30px
.title
  margin-bottom 10px
  font-weight 700
  font-size 16px
.row-item
  margin-bottom 10px
.item-col
  display flex
.col-label
  flex-shrink 0
>>>.el-table
  min-height auto
</style>
