<template>
  <TableView :options="options" :params.sync="filterData">
    <el-table ref="table" class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(442)"
      v-loading="loading.table" @selection-change="selectionList = $event.map(item => item.id)">
      <template v-slot:empty>
        <Results v-if='!loading.table'></Results>
        <span v-else></span>
      </template>
      <el-table-column type="index" label="序号" width="55"></el-table-column>
      <el-table-column label="年份" prop="orgName" min-width="88">
        <template slot-scope="{row}">{{ row.year }}</template>
      </el-table-column>
      <el-table-column label="资助类别" prop="orgName" min-width="88">
        <template slot-scope="{row}">{{ row.category }}</template>
      </el-table-column>
      <el-table-column label="资助总计" min-width="88">
        <template slot-scope="{row}">{{ (isCourseStatic(row) ? row.totalCourseFee : row.totalActivityFee) || 0 | money
        }}</template>
      </el-table-column>
      <el-table-column label="场所费用" min-width="88">
        <template slot-scope="{row}">
          <span v-if="isCourseStatic(row)">{{ row.siteFundFee || 0 | money }}</span>
          <span v-else>{{ "--" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="就餐费用" min-width="88">
        <template slot-scope="{row}">
          <span v-if="isCourseStatic(row)">{{ row.mealFundFee || 0 | money }}</span>
            <span v-else>{{ "--" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="住宿费用" min-width="88">
        <template slot-scope="{row}">
          <span v-if="isCourseStatic(row)">{{ row.roomFundFee || 0 | money }}</span>
          <span v-else>{{ "--" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交通费用" min-width="88">
        <template slot-scope="{row}">
          <span v-if="isCourseStatic(row)">{{ row.trafficFundFee || 0 | money }}</span>
          <span v-else>{{ "--" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="学费" min-width="88">
        <template slot-scope="{row}">
          <span v-if="isCourseStatic(row)">{{ row.studyFundFee || 0 | money }}</span>
          <span v-else>{{ "--" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="资助人次" prop="orgName" min-width="88">
        <template slot-scope="{row}">
          <span v-if="isCourseStatic(row)">{{ row.totalStudentNum || 0 | number }}</span>
          <span v-else>{{ "--" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总学时" prop="orgName" min-width="88">
        <template slot-scope="{row}">
          <span v-if="isCourseStatic(row)">{{ row.totalLessonNum || 0 | number }}</span>
          <span v-else>{{ "--" }}</span>
        </template>
      </el-table-column>
    </el-table>
  </TableView>
</template>

<script>
import mxTableView from '@/components/mixins/mxTableView';
export default {
  name: 'TableData',
  mixins: [mxTableView],
  computed: {
    tableDataAPI() {
      return this.$api.FundingStatistics.annualFundingStatisticsFeeList;
    }
  },
  methods: {
    isCourseStatic(row) {
      return row.category === '课程申请';
    }
  }
}
</script>

<style scoped>

</style>