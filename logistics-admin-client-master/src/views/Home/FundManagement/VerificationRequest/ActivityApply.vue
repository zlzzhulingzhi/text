<template>
  <TableView :options="options" :params.sync="filterData">
    <template slot="main">
      <div class="margin-bottom-6">
        <span class="text-6 font-14 margin-right-2">申请日期:</span>
        <el-date-picker class="margin-right-12" v-model="tradeDate" type="daterange" size="small" range-separator="至" value-format="yyyy-MM-dd" format="yyyy-MM-dd" start-placeholder="开始日期" end-placeholder="结束日期">
        </el-date-picker>
      </div>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(237)" v-loading="loading.table">
      <template v-slot:empty>
        <Results></Results>
      </template>
      <el-table-column label="活动名称" prop="activityName" min-width="160"></el-table-column>
      <el-table-column label="活动主题" prop="activityTheme" min-width="120"></el-table-column>
      <el-table-column label="补贴费用" prop="budgetAmount" min-width="120"></el-table-column>
      <el-table-column label="联系人" prop="contactPerson" width="120"></el-table-column>
      <el-table-column label="联系电话" prop="contactNumber" min-width="150"></el-table-column>
      <el-table-column label="申请人" prop="applyUserName" min-width="120"></el-table-column>
      <el-table-column label="申请时间" prop="applyDate" width="140"></el-table-column>
      <el-table-column label="审批状态" prop="status" width="120">
        <EleDot slot-scope="{row}" :id="row.status" type="approvalStatus"></EleDot>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" size="small" @click.stop="onDetail(row)">详情</el-button>
          <el-button type="text" size="small" @click.stop="onAddExport(row)">专家评审</el-button>
          <el-button type="text" size="small" v-if="row.status === 11" @click.stop="doPass(row)" :loading="loading.doPass">审批</el-button>
          <el-button type="text" size="small" v-else @click.stop="onApproveDetail(row)">审批情况</el-button>
        </template>
      </el-table-column>
    </el-table>
    <DialogApplyForFunding ref="DialogApplyForFunding" @success="getTableData"></DialogApplyForFunding>
    <DialogCommonDetail ref="DialogCommonDetail"></DialogCommonDetail>
    <DialogExpInfoForReview ref="DialogExpInfoForReview"></DialogExpInfoForReview>
  </TableView>
</template>
  
<script>
import DialogApplyForFunding from '@/components/dialog/Apply/DialogApplyForFunding'
import DialogCommonDetail from '@/components/dialog/Qualification/DialogCommonDetail'
import DialogExpInfoForReview from '@/components/dialog/FundManagement/DialogExpInfoForReview'

export default {
  name: 'QualificationReview',
  components: {
    DialogApplyForFunding,
    DialogCommonDetail,
    DialogExpInfoForReview
  },
  data() {
    return {
      options: {
        total: 10,
        main: {
          status: {
            label: '审批状态',
            options: 'approvalStatus'
          }
        },
        side: 'activityName:活动名称'
      },
      tradeDate: null,
      tableData: [],
      filterData: {},
      selectionList: [],
      timer: {
        table: null
      }, 
      loading: {
        table: null,
        doPass:null,
      }
    }
  },
  created() {
    this.getSelectExpList()
  },
  computed: {
    params() {
      let [startDate, endDate] = this.tradeDate || []
      return {
        ...this.filterData,
        applyDateStart:startDate,
        applyDateEnd:endDate
      }
    }
  },
  watch: {
    params: {
      deep: true,
      immediate: true,
      handler() {
        clearTimeout(this.timer.table)
        this.timer.table = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    }
  },
  methods: {
    async getTableData() {
      this.loading.table = true
      let { code, data } = await this.$api.Approve.activityPage(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    // 操作 - 审批
    async doPass(row) {
      this.$refs.DialogApplyForFunding.open({
        ...row,
        type:'activity'
      })
    },
    // 操作 - 详情
    async onDetail(row) {
      this.$utils.WindowOpenInParentFrame(`/FundManagement/ActivityDetail?id=${row.id}`)
      // this.$refs.DialogCommonDetail.open({
      //   ...row,
      //   type:'ActivityApply'
      // })
      // this.$router.push({
      //   name:'ApplyActivityDetail',
      //   query:{
      //     id:row.id,
      //     // page:'BaseInfo'
      //   }
      // })
    },
    // 操作 - 审批情况
    onApproveDetail(row) {
      console.log('审批详情');
      this.$refs.DialogApplyForFunding.open({
        ...row,
        type: 'detail'
      })
    },
    // 操作 - 添加专家、附件信息
    onAddExport(param) {
      this.$refs.DialogExpInfoForReview.open({
        id: param.id,
        type: 'activity',
        selectExports: this.selectExports
      })
    },
    // 获取可选专家列表
    async getSelectExpList() {
      let {code, data} = await this.$api.ReviewExport.selectList()
      if(code !== 200) return false
      this.selectExports = data.map(item => {
        return {
          id: item.id,
          name: item.name
        }
      })
    },
  }
}
</script>
  
  <style scoped></style>
  