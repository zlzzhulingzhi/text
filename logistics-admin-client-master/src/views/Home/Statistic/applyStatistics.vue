<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <div class="timer">
        选择时间：
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
    </template>

    <!-- <template v-slot:side>
      <el-button class="margin-bottom-8 margin-right-8" size="small" @click="onExport">
        导出
      </el-button>
    </template> -->

    <div class="statistics-wrapper flex wrap start-center">
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

    <el-table ref="table" class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(224)"
      v-loading="loading.table" @expand-change="expandChange">
      <template v-slot:empty>
        <!-- 没有选择日期 -->
        <el-empty class="text-center flex center-center column noDate" 
                  description="请选择日期后进行查询统计" 
                  :image="require('@/assets/results/NoData.png')"
                  :image-size="200" v-if='!(startDate || endDate) && !loading.table'>
        </el-empty>
        <!-- 选择了日期 -->
        <Results v-else-if='(startDate || endDate) && !loading.table'></Results>
        <span v-else></span>
      </template>

      <el-table-column type="expand">
        <template slot-scope="parentScope">
          <template v-if="subApplyType === 'allowance'">
            <!--课程列表-->
            <el-table :data="subTableData" row-key="id">
              <template v-slot:empty>
                <Results v-if='!subTableLoading'></Results>
                <span v-else></span>
              </template>

              <el-table-column label="项目编号" min-width="88">
                <template slot-scope="{row}">{{ row.projectNo }}</template>
              </el-table-column>
              <el-table-column label="项目名称" min-width="88">
                <template slot-scope="{row}">{{ row.projectName }}</template>
              </el-table-column>
              <el-table-column label="资助总计" min-width="88">
                <template slot-scope="{row}">{{ row.totalFee || 0 | money }}</template>
              </el-table-column>
              <el-table-column label="场所费" min-width="88">
                <template slot-scope="{row}">{{ row.siteFundFee || 0 | money }}</template>
              </el-table-column>
              <el-table-column label="就餐费" min-width="88">
                <template slot-scope="{row}">{{ row.mealFundFee || 0 | money }}</template>
              </el-table-column>
              <el-table-column label="住宿费" min-width="88">
                <template slot-scope="{row}">{{ row.roomFundFee || 0 | money }}</template>
              </el-table-column>
              <el-table-column label="交通费" min-width="88">
                <template slot-scope="{row}">{{ row.trafficFundFee || 0 | money }}</template>
              </el-table-column>
              <el-table-column label="学费" min-width="88">
                <template slot-scope="{row}">{{ row.studyFundFee || 0 | money }}</template>
              </el-table-column>
              <el-table-column label="资助人次" prop="totalStudentNum" min-width="88"></el-table-column>
              <el-table-column label="总学时" prop="totalLessonNum" min-width="88"></el-table-column>
              <el-table-column label="审批状态" prop="status" width="85">
                <EleDot slot-scope="{row}" :id="row.flowStatus" type="flowStatus"></EleDot>
              </el-table-column>
              <el-table-column label="申请时间" prop="applyDate" min-width="120"></el-table-column>
            </el-table>
          </template>
          <template v-else>
            <!--活动列表-->
            <el-table :data="subTableData" row-key="applyId">
              <template v-slot:empty>
                <Results v-if='!subTableLoading'></Results>
                <span v-else></span>
              </template>

              <el-table-column label="活动主题" min-width="88">
                <template slot-scope="{row}">{{ row.activityTheme }}</template>
              </el-table-column>
              <el-table-column label="活动名称" min-width="88">
                <template slot-scope="{row}">{{ row.activityName }}</template>
              </el-table-column>
              <el-table-column label="资助总计" min-width="88">
                <template slot-scope="{row}">{{ row.totalFee || 0 | money }}</template>
              </el-table-column>
              <!-- <el-table-column label="场所费" min-width="88">
                <template slot-scope="{row}">{{ row.siteFundFee || 0 | money }}</template>
              </el-table-column>
              <el-table-column label="就餐费" min-width="88">
                <template slot-scope="{row}">{{ row.mealFundFee || 0 | money }}</template>
              </el-table-column>
              <el-table-column label="住宿费" min-width="88">
                <template slot-scope="{row}">{{ row.roomFundFee || 0 | money }}</template>
              </el-table-column>
              <el-table-column label="交通费" min-width="88">
                <template slot-scope="{row}">{{ row.trafficFundFee || 0 | money }}</template>
              </el-table-column>
              <el-table-column label="学费" min-width="88">
                <template slot-scope="{row}">{{ row.studyFundFee || 0 | money }}</template>
              </el-table-column> -->
              <el-table-column label="审批状态" prop="status" min-width="85">
                <EleDot slot-scope="{row}" :id="row.flowStatus" type="flowStatus"></EleDot>
              </el-table-column>
              <el-table-column label="申请时间" prop="applyDate" min-width="90"></el-table-column>
            </el-table>
          </template>
        </template>
      </el-table-column>

      <el-table-column type="index" label="序号" width="55"></el-table-column>
      <el-table-column label="机构名称" prop="orgName" min-width="88"></el-table-column>
      <el-table-column label="资助类别" min-width="88">
        <template slot-scope="{row}">{{ row.applyType | applyTypeStatus }}</template>
      </el-table-column>
      <el-table-column label="资助总计" min-width="88">
        <template slot-scope="{row}">{{ row.totalFee || 0 | money }}</template>
      </el-table-column>
      <el-table-column label="场所费用" min-width="88">
        <template slot-scope="{row}">
          <span v-if="row.applyType === 'activity'">--</span>
          <span v-else>{{ row.siteFundFee || 0 | money }}</span>
        </template>
      </el-table-column>
      <el-table-column label="就餐费用" min-width="88">
        <template slot-scope="{row}">
          <span v-if="row.applyType === 'activity'">--</span>
          <span v-else>{{ row.mealFundFee || 0 | money }}</span>
        </template>
      </el-table-column>
      <el-table-column label="住宿费用" min-width="88">
        <template slot-scope="{row}">
          <span v-if="row.applyType === 'activity'">--</span>
          <span v-else>{{ row.roomFundFee || 0 | money }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交通费用" min-width="88">
        <template slot-scope="{row}">
          <span v-if="row.applyType === 'activity'">--</span>
          <span v-else>{{ row.trafficFundFee || 0 | money }}</span>
        </template>
      </el-table-column>
      <el-table-column label="学费" min-width="88">
        <template slot-scope="{row}">
          <span v-if="row.applyType === 'activity'">--</span>
          <span v-else>{{ row.studyFundFee || 0 | money }}</span>
        </template>
      </el-table-column>
      <el-table-column label="资助人次" prop="totalStudentNum" min-width="88">
        <template slot-scope="{row}">
          <span v-if="row.applyType === 'activity'">--</span>
          <span v-else>{{ row.totalStudentNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总学时" prop="totalLessonNum" min-width="88">
        <template slot-scope="{row}">
          <span v-if="row.applyType === 'activity'">--</span>
          <span v-else>{{ row.totalLessonNum }}</span>
        </template>
      </el-table-column>

      <!-- <el-table-column label="操作" width="100">
        <template slot-scope="{row,isExpanded}">
          <el-button type="text" size="small" @click="onToggle(row)">
            {{ isExpanded ? '收起' : '展开' }}
          </el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <DialogDetail ref="DialogDetail"></DialogDetail>
  </TableView>
</template>

<script>
import DialogDetail from '@/components/dialog/Statistics/DialogDetail';
import mxTableView from '@/components/mixins/mxTableView';
import { mapGetters } from 'vuex';
export default {
  name: 'applyStatistics',
  mixins: [mxTableView],
  components: {
    DialogDetail
  },
  data() {
    return {
      options: {
        main: {
          applyType: {
            label: '资助类别',
            options: [
              { id: 'allowance', name: '课程' },
              { id: 'activity', name: '活动' }
            ]
          },
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
        // 展开子表格判断条件
        subApplyType: 'allowance'
      },

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
      subTableData: [],
      subTableLoading: true,
      // 默认的起始，终止日期
      startDate: null,
      endDate: null
    }
  },
  async mounted() {
    let { code, data } = await this.$api.FundingStatistics.orgList({});
    // console.log('data orgList == ', data);
    // console.log('code orgList== ', code);
    if (code === 200) {
      this.options.main.orgId.options = data;
    }
  },
  computed: {
    params() {
      return {
        ...this.filterData,
        dateStart: this.startDate,
        dateEnd: this.endDate 
      }
    },
    ...mapGetters({
      applyTypeStatus: 'common/applyTypeStatus'
    })
  },
  methods: {
    async getTableData() {
      this.loading.table = true
      const result = await this.$api.FundingStatistics.institutionalFundingStatistics(this.params);
      const summaryResult = await this.$api.FundingStatistics.institutionalFundingStatisticsSummary(this.params);
      let { code, data } = result;
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data?.records ? data.records : [];
      this.options.total = data?.total ? data.total : 0;
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

    // 操作 - 切换
    onToggle(row) {
      console.log('onToggle === ')
      this.$refs.table.toggleRowExpansion(row)
    },
    // 操作 - 导出
    onExport() {
      console.log('导出')
    },
    // 操作 - 详情
    onDetail(item) {
      this.$refs.DialogDetail.open({})
    },
    // 表格 - 展开
    async expandChange(row, expandedRows) {
      if (expandedRows.length > 0) {
        let newParams = this.params;
        const { applyType, orgId } = row;
        this.subApplyType = applyType;
        Object.assign(newParams, { orgId, applyType });
        // console.log("newParams == ", newParams);
        // console.log("row == ", row);
        this.subTableLoading = true;
        let { code, data } = await this.$api.FundingStatistics.institutionalFundingStatisticsSub(newParams);
        this.subTableLoading = false;
        if (code !== 200) return false;
        this.subTableData = data?.records ? data.records : [];
      }
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-table
  .el-table__cell.el-table__expanded-cell
    padding 0

    .el-table
      border none

      &:before
        display none

      .el-table__cell
        border none

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
  .setWidth
      width 140px

>>>.el-table__empty-text
  .noDate
    .el-empty__description
      p
        color red !important
        font-size: 18px
        font-weight: bold
</style>
