<template>
  <div class="width-100p">
    <!-- <Breadcrumb class="margin-bottom-12 margin-left-16"></Breadcrumb> -->
    <TableView :options="options" :params.sync="filterData">
      <template v-slot:main>
        <div class="timer">
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


      <div class="flex margin-top-10">
        <span>{{ "活动总计：" }}</span>
        <div>
          <b class="text-bold">
            {{ totalFee || 0 | money(2,true,false,'元')  }}
          </b>
        </div>
      </div>


      <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(271)" v-loading="loading.table"
        @selection-change="selectionList = $event.map(item => item.id)">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>
        <el-table-column type="index" label="序号" width="55"></el-table-column>
        <el-table-column label="活动名称" prop="activityName" min-width="100">
        </el-table-column>
        <el-table-column label="活动主题" prop="activityTheme" min-width="100"></el-table-column>
        <el-table-column label="补贴费用" prop="budgetAmount" min-width="100">
          <template slot-scope="{row}">{{ row.budgetAmount || 0 | money }}</template>
        </el-table-column>
        <el-table-column label="联系人" prop="contactPerson" min-width="100">
        </el-table-column>
        <el-table-column label="联系电话" prop="contactNumber" min-width="100"></el-table-column>
        <el-table-column label="申请日期" prop="applyDate" min-width="100"></el-table-column>
        <el-table-column label="审批状态" prop="status" width="85">
          <EleDot slot-scope="{row}" :id="row.flowStatus" type="flowStatus"></EleDot>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="small" @click.stop="onDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </TableView>
  </div>

</template>

<script>
import mxTableView from '@/components/mixins/mxTableView';
import { mapGetters } from 'vuex'

export default {
  name: 'ActivityMoneyStatistics',
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
      },
      filterData: {},

      //资助费用范围
      minFee: undefined, 
      maxFee: undefined,

      totalFee: 0, // 活动总计
      // 默认的起始，终止日期
      startDate,
      endDate 
    }
  },
  async mounted() {
    let { code, data } = await this.$api.FundingStatistics.orgList({ applyType: "activity" });
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
      const result = await this.$api.FundingStatistics.activityStaticsPage(this.params);
      const summaryResult = await this.$api.FundingStatistics.activitySummary(this.params);
      // console.log('activityParma === ', this.params);
      // console.log('summaryResult == ', summaryResult);
      let { code, data } = result;
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records;
      this.options.total = data.total;
      if (summaryResult.code === 200) {
        this.totalFee = summaryResult.data?.totalFee;
      }
    },

    // 导出文件
    onExport() {
      console.log('导出')
    },

    // 操作 - 详情
    async onDetail(row) {
      this.$utils.WindowOpenInParentFrame(`/FundManagement/ActivityDetail?id=${row.applyId}`)
      // this.$refs.DialogCommonDetail.open({
      //   ...row,
      //   type:'ActivityApply'
      // })
     
      // this.$router.push({
      //   name:'ApplyActivityDetail',
      //   query:{
      //     id:row.applyId,
      //     // page:'BaseInfo'
      //   }
      // })
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
