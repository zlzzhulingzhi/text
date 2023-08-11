<template>
  <div class="width-100p">
    <!-- <Breadcrumb class="margin-bottom-12 margin-left-16"></Breadcrumb> -->
    <TableView :options="options" :params.sync="filterData">
      <template v-slot:main>
        <div class="timer ">
          资助申请时间：
          <el-date-picker v-model="startDate" class="setWidth" size="small" type="date" value-format="yyyy-MM-dd"
            format="yyyy-MM-dd" placeholder="起始日期">
          </el-date-picker>
          -
          <el-date-picker v-model="endDate" class="setWidth" size="small" type="date" value-format="yyyy-MM-dd"
            format="yyyy-MM-dd" placeholder="截至日期">
          </el-date-picker>
        </div>
        <div class="timer flex start-center" style="order:90;">
          <span class="margin-right-12">
            资助费用：
          </span>
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
        <div v-for="item in feeMaps" :key="item.id" class="statistics-item flex margin-right-10 margin-top-10">
          <span>{{ item.name + "：" }}</span>
          <div>
            <template v-if="item.type === 'price'">
              <b class="text-bold">
                {{ item.data || 0 | money(2, true, false, '元') }}
              </b>
            </template>
            <template v-else-if="item.type === 'studentNum'">
              <b class="text-bold">
                {{ item.data || 0 | money(2, true, false, '人') }}
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
      <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(235)" v-loading="loading.table"
        @expand-change="expandChange">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>
        <el-table-column type="expand">
          <template slot-scope="parentScope">
            <el-table :data="subTableData">
              <template v-slot:empty>
                <Results v-if='!subTableLoading'></Results>
                <span v-else></span>
              </template>
              <el-table-column label="课程名称" prop="projectName" min-width="88"></el-table-column>
              <el-table-column label="班级名称" prop="className" min-width="88"></el-table-column>
              <el-table-column label="学时" prop="lessonNum" min-width="88">
              </el-table-column>
              <el-table-column label="考试成绩" prop="examScore" min-width="88">
              </el-table-column>
              <el-table-column label="缴费金额" prop="payAmount" min-width="88">
                <template slot-scope="{row}">{{ row.payAmount || 0 | money }}</template>
              </el-table-column>
              <el-table-column label="资助金额" prop="supplyAmount" min-width="88">
                <template slot-scope="{row}">{{ row.supplyAmount || 0 | money }}</template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column type="index" label="序号" width="55"></el-table-column>
        <el-table-column label="学员姓名" prop="studentName" min-width="100"></el-table-column>
        <el-table-column label="手机号" prop="studentPhone" min-width="100"></el-table-column>
        <el-table-column label="单位名称" prop="unitName" min-width="150"></el-table-column>
        <el-table-column label="总学时" prop="lessonNum" min-width="150">
        </el-table-column>
        <el-table-column label="缴费总计" prop="payAmount" min-width="150">
          <template slot-scope="{row}">{{ row.payAmount || 0 | money }}</template>
        </el-table-column>
        <el-table-column label="资助总计" prop="supplyAmount" min-width="150">
          <template slot-scope="{row}">{{ row.payAmount || 0 | money }}</template>
        </el-table-column>
      </el-table>
    </TableView>
  </div>

</template>

<script>
import mxTableView from '@/components/mixins/mxTableView';
import { mapGetters } from 'vuex'

export default {
  name: 'studentStatistics',
  mixins: [mxTableView],
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
        },
        side: 'studentName:学员姓名,studentPhone:手机号'
      },
      filterData: {},

      //资助费用范围
      minFee: undefined, 
      maxFee: undefined,

      subTableData: [],
      subTableLoading: true,
      feeMaps: [
        { id: 'a', name: '资助总计', type: 'price', data: 0 },
        { id: 'b', name: '总人数', type: 'studentNum', data: 0 },
        { id: 'c', name: '总学时', type: 'lessonNum', data: 0 }
      ],
      // 默认的起始，终止日期
      startDate,
      endDate
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
      const result = await this.$api.FundingStatistics.studentStaticsPage(this.params);
      const summaryResult = await this.$api.FundingStatistics.studentSummary(this.params);
      let { data, code } = result;
      // console.log('params ==== ', this.params);
      // console.log('code3333 == ', code);
      // console.log('data4433 == ', data);
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total

      if (summaryResult.code === 200) {
        const summaryObj = summaryResult.data;
        this.feeMaps[0].data = summaryObj.totalFee ? summaryObj.totalFee : 0;
        this.feeMaps[1].data = summaryObj.totalStudentNum ? summaryObj.totalStudentNum : 0;
        this.feeMaps[2].data = summaryObj.totalLessonNum ? summaryObj.totalLessonNum : 0;
      }
    },

    // 导出文件
    onExport() {
      console.log('导出')
    },

    // 表格 - 展开
    async expandChange(row, expandedRows) {
      if (expandedRows.length > 0) {
        let newParams = this.params;
        // console.log('row 2222 = ', row);
        Object.assign(newParams, { memberId: row.memberId });
        // console.log("newParams == ", newParams);
        this.subTableLoading = true;
        let { code, data } = await this.$api.FundingStatistics.studentStaticsSubPage(newParams);
        this.subTableLoading = false;
        // console.log('data === ', data);
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
  .setWidth
      width 140px

.statistics-wrapper
  text-align center
  margin-top 12px
  line-height 20px
  font-size 14px
  .statistics-item
    flex-shrink 0

  /* 表头 */
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
