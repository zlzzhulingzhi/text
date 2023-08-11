<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-12" type="primary" size="small" @click="onCreate"
        icon="el-icon-plus">
        培训申请
      </el-button>
    </template>

    <template v-slot:side>
      <a :href="excelLocation" download="国家网安基地培训中心入驻机构需求清单">
        <el-button class="margin-bottom-8 margin-right-12" type="primary" size="small">
        需求单模板下载
      </el-button>
      </a>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(186)" v-loading="loading.table"
      @selection-change="selectionList = $event.map(item => item.id)" @row-click="onView">
      <template v-slot:empty>
        <Results v-if='!loading.table'></Results>
        <span v-else></span>
      </template>
      <el-table-column label="专题名称" prop="courseName"></el-table-column>
      <el-table-column label="申请事由" prop="applyReason"></el-table-column>
      <el-table-column label="联系电话" prop="contactNumber"></el-table-column>
      <el-table-column label="联系人" prop="contactPerson"></el-table-column>
      <el-table-column label="申请日期" prop="applyDate"></el-table-column>
      <el-table-column label="申请状态" prop="applyResult">
        <!-- <EleDot slot-scope="{row}" :id="row.applyResult" type="applyResult"></EleDot> -->
        <template slot-scope="{ row }">
          <template v-if="row.applyStatus === 1">
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
          <template v-if="row.applyStatus === 2">
            <span class="dot bg-emb"></span>
            <span class="text-emb">已作废</span>
          </template>
          <template v-if="row.applyStatus === 0">
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
            <!--已通过 且 作废-->
            <template v-else-if="row.flowStatus === 2 && row.applyStatus === 2">
              <el-button type="text" size="small" @click.stop="onCopyApply(row)">复制申请</el-button>
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

    <DialogApplyApproveDetail ref="DialogApplyApproveDetail"></DialogApplyApproveDetail>
  </TableView>
</template>
  
<script>
import DialogApplyApproveDetail from '@/components/dialog/Logistics/DialogApplyApproveDetail'
import mxTableView from '@/components/mixins/mxTableView'
import { mapGetters, mapState } from 'vuex'

export default {
  name: 'FacilityApplyList',
  components: {
    DialogApplyApproveDetail
  },
  mixins: [mxTableView],
  data() {
    return {
      options: {
        side: 'courseName:专题名称'
      },
      UniOrgData: {},
      excelLocation: `${location.pathname}国家网安基地培训中心入驻机构需求清单.xlsx`
    }
  },
  created() {
    // this.getUniOrgData()
  },
  computed: {
    ...mapGetters({
      applyResult: 'common/applyResult',
    }),
    tableDataAPI() {
      return this.$api.AllowanceApply.settlePage
    }
  },
  methods: {
    // 操作 - 机构详情信息
    // async getUniOrgData() {
    //   let { code, data: d } = await this.$api.UniOrg.info()
    //   if (code !== 200) return false
    //   this.UniOrgData = d
    // },
    // 操作 - 培训申请 新增
    onCreate() {
      this.$router.push({
        name: 'FacilityApplyCreate'
      })
    },
    // 操作 - 培训申请 提交
    async onSubmit(row) {
      await this.$confirm(`确定提交申请吗？`,  {
        title: '提交确认',
        type: 'warning'
      })
      let { code, data: d } = await this.$api.AllowanceApply.submit({
        id: row.id
      })
      if (code !== 200) return false
      this.$msg('提交')
      this.getTableData()
    },
    // 操作 - 培训申请详情
    onView(row) {
      this.$router.push({
        name: 'FacilityApplyDetail',
        query: {
          id: row.id
        }
      })
      // this.$utils.WindowOpenInParentFrame(`/Logistics/FacilityApply/Detail?id=${row.id}`)
    },
    // 操作 - 培训申请编辑
    onEdit(row) {
      this.$router.push({
        name: 'FacilityApplyEdit',
        query: {
          id: row.id
        }
      })
    },
    // 操作 - 删除培训申请
    async onDelete(row) {
      await this.$confirm(`确定删除申请吗？`,  {
        title: '删除确认',
        type: 'warning'
      })
      let { code, data: d } = await this.$api.AllowanceApply.delete({
        id: row.id
      })
      if (code !== 200) return false
      this.getTableData()
    },
    // 操作 - 培训申请复制
    async onCopyApply(row) {
      let { code, data: d } = await this.$api.AllowanceApply.copyApply({
        id: row.id
      })
      if (code !== 200) return false
      this.getTableData()
    },
    // 审批流程
    approveDetail(row) {
      this.$refs.DialogApplyApproveDetail.open({
        type: 'detail',
        formData: row
      })
    }
  }
}
</script>
  
<style scoped lang="stylus">
.tooltip-content
  max-height 300px

.padding-left-right
  padding-right: 10px !important
  padding-left: 10px !important
  margin-right: 4px
</style>
  