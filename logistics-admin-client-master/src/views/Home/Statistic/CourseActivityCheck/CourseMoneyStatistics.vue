<template>
  <div class="width-100p">
    <!-- <Breadcrumb class="margin-bottom-12 margin-left-16"></Breadcrumb> -->
    <TableView :options="options" :params.sync="filterData">
      <template v-slot:main>
        <div class="timer ">
          资助申请时间：
          <el-date-picker
            v-model="startDate"
            class="setWidth"
            size="small"
            type="date"
            value-format="yyyy-MM-dd" 
            format="yyyy-MM-dd"
            placeholder="起始日期">
          </el-date-picker>
          -
          <el-date-picker
            v-model="endDate"
            class="setWidth"
            size="small"
            type="date"
            value-format="yyyy-MM-dd" 
            format="yyyy-MM-dd"
            placeholder="截至日期">
          </el-date-picker>
        </div>

        <div class="timer flex start-center" style="order:90;">
          资助费用：
          <el-input-number v-model="minFee" class="width-140" size="small" :controls="false" :step="0.01" min="0"></el-input-number>
          <div class="margin-10">至</div>
          <el-input-number v-model="maxFee" class="width-140" size="small" :controls="false"  :step="0.01" min="0"></el-input-number>
        </div>
      </template>
      <!-- <template v-slot:side>
        <el-button class="margin-bottom-8 margin-right-8" size="small" @click="onExport">
          导出
        </el-button>
      </template> -->

      <div class="statistics-wrapper flex start-center wrap">
        <div v-for="item in feeMaps" :key="item.id" class="statistics-item flex margin-right-10 margin-top-10 width-160">
          <span>{{ item.name + "：" }}</span>
          <div>
            <template v-if="item.type === 'price'">
              <b class="text-bold">
                {{ item.data || 0 | money(2, true, false, '元') }}
              </b>
            </template>
            <template v-else-if="item.type === 'studentNum'">
              <b class="text-bold">
                {{ item.data || 0 | money(2, true, false, '次') }}
              </b>
            </template>
            <template v-else-if="item.type === 'lessonNum'">
              <b class="text-bold">
                {{ item.data || 0 | money(2, true, false, '课时') }}
              </b>
            </template>
          </div>
        </div>
      </div>

      <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(282)" v-loading="loading.table"
        @selection-change="selectionList = $event.map(item => item.id)" @expand-change="expandChange">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>
        <el-table-column type="expand">
          <template slot-scope="parentScope">
            <!--课程列表-->
            <el-table :data="subTableData">
              <template v-slot:empty>
                <Results v-if='!subTableLoading'></Results>
                <span v-else></span>
              </template>
              <el-table-column label="学员姓名" prop="studentName" min-width="88"></el-table-column>
              <el-table-column label="单位" prop="unitName" min-width="88"></el-table-column>
              <el-table-column label="学时" min-width="88">
                <template slot-scope="{row}">{{ row.lessonNum || 0 | number  }}</template>
              </el-table-column>
              <el-table-column label="考试成绩" min-width="88">
                <template slot-scope="{row}">{{ row.examScore || 0 | number  }}</template>
              </el-table-column>
              <el-table-column label="缴费金额" min-width="88">
                <template slot-scope="{row}">{{ row.payAmount || 0 | money  }}</template>
              </el-table-column>
              <el-table-column label="资助金额" min-width="88">
                <template slot-scope="{row}">{{ row.supplyAmount || 0 | money  }}</template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column type="index" label="序号" width="55"></el-table-column>
        <el-table-column label="项目编号" prop="projectNo" min-width="100">
        </el-table-column>
        <el-table-column label="项目名称" prop="projectName" min-width="100"></el-table-column>
        <el-table-column label="资助总计" min-width="88">
          <template slot-scope="{row}">{{ row.totalFee || 0 | money  }}</template>
        </el-table-column>
        <el-table-column label="场所费" min-width="88">
          <template slot-scope="{row}">{{ row.siteFundFee || 0 | money  }}</template>
        </el-table-column>
        <el-table-column label="就餐费" min-width="88">
          <template slot-scope="{row}">{{ row.mealFundFee || 0 | money  }}</template>
        </el-table-column>
        <el-table-column label="住宿费" min-width="88">
          <template slot-scope="{row}">{{ row.roomFundFee || 0 | money  }}</template>
        </el-table-column>
        <el-table-column label="交通费" min-width="88">
          <template slot-scope="{row}">{{ row.trafficFundFee || 0 | money  }}</template>
        </el-table-column>
        <el-table-column label="学费" min-width="88">
          <template slot-scope="{row}">{{ row.studyFundFee || 0 | money  }}</template>
        </el-table-column>
        <el-table-column label="审批状态" prop="status" width="85">
          <EleDot slot-scope="{row}" :id="row.flowStatus" type="flowStatus"></EleDot>
        </el-table-column>
        <el-table-column label="申请时间" prop="applyDate" min-width="120"></el-table-column>
      </el-table>
    </TableView>
  </div>

</template>

<script>
import mxTableView from '@/components/mixins/mxTableView';
import { mapGetters } from 'vuex'

export default {
  name: 'CourseMoneyStatistics',
  mixins:[mxTableView],
  components: {
  },
  data() {
    let startDate = new Date()
    let endDate = new Date()
    startDate.setDate(startDate.getDate() - 29)
    startDate = this.$utils.formatTime(startDate.getTime(), 'YYYY-MM-DD')
    endDate = this.$utils.formatTime(endDate.getTime(), 'YYYY-MM-DD')

    return {
      options: {
        main: {
          flowStatus: {
            label: '审批状态',
            options: [
              { id: 1, name: '待审' },
              { id: 2, name: '通过' }
            ]
          },
          orgId: {
            label: '申请机构',
            options: []
          }
        },
        // side: 'name:单位名称'
      },
      filterData: {},

      //资助费用范围
      minFee: undefined, 
      maxFee: undefined,

      feeMaps: [
        { id: 'a', name: '资助总计', type: 'price', data: 0 },
        { id: 'b', name: '场所费用', type: 'price', data: 0 },
        { id: 'c', name: '就餐费用', type: 'price', data: 0 },
        { id: 'd', name: '住宿费用', type: 'price', data: 0 },
        { id: 'e', name: '交通费用', type: 'price', data: 0 },
        { id: 'f', name: '学费', type: 'price', data: 0 },
        { id: 'g', name: '资助人次', type: 'studentNum', data: 0 },
        { id: 'h', name: '总学时', type: 'lessonNum', data: 0 },
      ],
      subTableLoading: true,
      subTableData: [],
      // 默认的起始，终止日期
      startDate,
      endDate 
    }
  },
  async mounted() {
    let { code, data } = await this.$api.FundingStatistics.orgList({ applyType: "allowance" });
    // console.log('data orgList == ', data);
    // console.log('code orgList== ', code);
    if (code === 200) {
      this.options.main.orgId.options = data;
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      app: 'common/app',
      inspectStatus: 'common/inspectStatus',
      fundingObject: 'common/fundingObject',
      passStatus: 'common/passStatus'
    }),
    params() {
      return {
        ...this.filterData,
        dateStart: this.startDate,
        dateEnd: this.endDate,
        minFee: this.minFee, 
        maxFee: this.maxFee
      }
    }
  },
  methods: {
    async getTableData() {
      this.loading.table = true
      // let { code, data } = await this.$api.moneyStatisicsAPI.page(this.params)
      const result = await this.$api.FundingStatistics.courseStaticsPage(this.params);
      const summaryResult = await this.$api.FundingStatistics.courseSummary(this.params);

      // console.log('corseParma === ', this.params);

      let { code, data } = result;
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records;
      // console.log('this.tableData=', this.tableData);
      this.options.total = data.total;
      if (summaryResult.code === 200) {
        const summaryObj = summaryResult.data;
        this.feeMaps[0].data = summaryObj?.totalFee ? summaryObj.totalFee : 0;
        this.feeMaps[1].data = summaryObj?.siteFundFee ? summaryObj.siteFundFee : 0;
        this.feeMaps[2].data = summaryObj?.mealFundFee ? summaryObj.mealFundFee : 0;
        this.feeMaps[3].data = summaryObj?.roomFundFee ? summaryObj.roomFundFee : 0;
        this.feeMaps[4].data = summaryObj?.trafficFundFee ? summaryObj.trafficFundFee : 0;
        this.feeMaps[5].data = summaryObj?.studyFundFee ? summaryObj.studyFundFee : 0;
        this.feeMaps[6].data = summaryObj?.totalStudentNum ? summaryObj.totalStudentNum : 0;
        this.feeMaps[7].data = summaryObj?.totalLessonNum ? summaryObj.totalLessonNum : 0;
      }
    },

    // 导出文件
    onExport() {
      console.log('导出')
    },
    // 操作 - 编辑班级
    onEdit(item) {
      this.$refs.DialogFormClassroomManage.open({
        type: 'OrgRoleEdit',
        formData: item
      })
    },
    // 表格 - 展开
    async expandChange(row, expandedRows) {
      if (expandedRows.length > 0) {
        let newParams = { id: row.id };
        // console.log("newParams == ", newParams);
        this.subTableLoading = true;
        let { code, data } = await this.$api.FundingStatistics.courseStaticsSubPage(newParams);
        // console.log('data result = ', data);
        this.subTableLoading = false;
        if (code !== 200) return false;
        this.subTableData = data.records;
      }

    }
  }
}
</script>

<style scoped lang="stylus">
.tooltip-content
  max-height 300px
.timer
  margin 0 12px 8px 0
  font-size 14px
  /* 表头 */
  .setWidth
      width 140px

.statistics-wrapper
  text-align center
  margin-top 12px
  line-height 20px
  font-size 14px

  .statistics-item
    flex-shrink 0
  .timer
    margin 0 12px 8px 0
    font-size 14px
 
>>> .el-table
  .el-table__cell.el-table__expanded-cell
    padding 0
    .el-table
      border none

      &:before
        display none

      .el-table__cell
        border none 
</style>
