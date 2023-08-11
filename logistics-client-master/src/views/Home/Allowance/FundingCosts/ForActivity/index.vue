<template>
  <TableView :options="options" :params.sync="filterData">
    <template slot="main">
      <el-button class="margin-bottom-8 margin-right-12" icon="el-icon-plus" type="primary" size="small" @click="onCreate">
        资助费用申请
      </el-button>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(237)" v-loading="loading.table" @row-click="onView">
      <template v-slot:empty>
        <Results></Results>
      </template>
      <el-table-column label="活动名称" prop="activityName" min-width="160"></el-table-column>
      <el-table-column label="活动主题" prop="activityTheme" min-width="120"></el-table-column>
      <el-table-column label="补贴费用" prop="budgetAmount" min-width="120"></el-table-column>
      <el-table-column label="联系人" prop="contactPerson" width="120"></el-table-column>
      <el-table-column label="联系电话" prop="contactNumber" min-width="150"></el-table-column>
      <el-table-column label="申请人" prop="applyUserName" min-width="120"></el-table-column>
      <el-table-column label="申请日期" prop="applyDate" width="140">
        <template slot-scope="{row}">
          <template v-if="!row.applyDate">
            --
          </template>
          <template v-else>
            {{row.applyDate | formatTime('yyyy-MM-DD')}}
          </template>
        </template>
      </el-table-column>
      <el-table-column label="审批状态" prop="applyStatus" width="88">
        <template slot-scope="{ row }">
          <template v-if="row.applyStatus">
            <template v-if="row.flowStatus === 1">
              <span class="dot bg-warning"></span>
              <span class="text-warning">待审批</span>
            </template>
            <template v-else-if="row.flowStatus === 2">
              <span class="dot bg-success"></span>
              <span class="text-success">已通过</span>
            </template>
            <template v-else-if="row.flowStatus === 4">
              <span class="dot bg-error"></span>
              <span class="text-error">已驳回</span>
            </template>
          </template>
          <template v-else>
            <span class="dot bg-9"></span>
            <span class="text-9">待提交</span>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="240" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" size="small" @click.stop="onView(row)">详情</el-button>

          <template v-if="row.applyStatus">
            <template v-if="row.flowStatus === 1">
              <!--待审批-->
            </template>
            <template v-else-if="row.flowStatus === 2">
              <!--已通过-->
            </template>
            <template v-else-if="row.flowStatus === 4">
              <!--已驳回-->
              <el-button type="text" size="small" @click.stop="onDelete(row)">删除</el-button>
              <el-button type="text" size="small" @click.stop="onCopyApply(row)">复制申请</el-button>
            </template>
            <el-button type="text" size="small" icon="el-icon-edit" class="buttonOfApproveDetail" @click.stop="approveDetail(row)">审批详情</el-button>
          </template>
          <template v-else>
            <!--待提交-->
            <el-button type="text" size="small" @click.stop="onSubmit(row)">提交</el-button>
            <el-button type="text" size="small" @click.stop="onEdit(row)">编辑</el-button>
            <el-button type="text" size="small" @click.stop="onDelete(row)">删除</el-button>
          </template>

        </template>
      </el-table-column>
    </el-table>

    <!-- 审批情况 -->
    <DialogApplyApproveDetail ref="DialogApplyApproveDetail"></DialogApplyApproveDetail>

  </TableView>
</template>
  
<script>
import DialogApplyApproveDetail from '@/components/dialog/Logistics/DialogApplyApproveDetail'
import mxTableView from '@/components/mixins/mxTableView'
  
export default {
  name: 'ActivityApply',
  components: {
    DialogApplyApproveDetail
  },
  mixins: [mxTableView],
  data() {
    return {
      tradeDate: null,
    }
  },
  computed: {
    params() {
      let [startDate, endDate] = this.tradeDate || []
      return {
        ...this.filterData,
        startDate,
        endDate
      }
    },
    tableDataAPI() {
      return this.$api.ApplyCost.pageActivity
    }
  },
  methods: {
    // 操作 - 创建
    onCreate() {
      this.$router.push({
        name: 'ApplyCostActivityCreate'
      })
    },
    // 操作 - 详情
    onView(item) {
      this.$utils.WindowOpenInParentFrame(`/Allowance/Cost/ActivityDetail?id=${item.id}`)
    },
    // 操作 - 提交
    async onSubmit(item) {
      await this.$confirm(`确定提交申请吗？`,  {
        title: '提交确认',
        type: 'warning'
      })
      let {code} = await this.$api.ApplyCost.submitActivity({
        id: item.id
      })
      if (code !== 200) return false
      this.$msg('提交')
      await this.getTableData()
    },
    // 操作 - 编辑
    onEdit(item) {
      this.$router.push({
        name: 'ApplyCostActivityEdit',
        query: {
          id: item.id
        }
      })
    },
    // 操作 - 删除
    async onDelete(item) {
      await this.$confirm(`确定删除申请吗？`,  {
        title: '删除确认',
        type: 'warning'
      })
      let {code} = await this.$api.ApplyQualification.remove({
        id: item.id
      })
      if (code !== 200) return false
      this.$msg.Delete()
      await this.getTableData()
    },
    // 审批流程
    approveDetail(row) {
      console.log('审批详情');
      this.$refs.DialogApplyApproveDetail.open({
        type: 'detail',
        formData: row
      })
    },
    // 操作 - 复制申请
    async onCopyApply(params) {
      let {code} = await this.$api.ApplyCost.copyApplyForActivity({
        id: params.id
      })
      if (code !== 200) return false
      this.$msg('复制申请')
      await this.getTableData()
    }
  }
}
</script>
  
<style scoped lang="stylus">
>>>.buttonOfApproveDetail
  span
    margin-left 0
</style>
  