<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>

      <!-- <el-button class="width-68 height-32 padding-0 margin-bottom-6 margin-right-8" type="primary">新增教室</el-button> -->

      <div class="margin-bottom-6">
        <span class="text-6 font-14 margin-right-2">开始日期:</span>
        <el-date-picker class="margin-right-12" v-model="tradeDate.submit" type="daterange" size="small" range-separator="至" value-format="yyyy-MM-dd" format="yyyy-MM-dd" start-placeholder="开始日期" end-placeholder="结束日期">
        </el-date-picker>
      </div>

      <!-- <div class="margin-bottom-6 margin-right-5">
        <span class="text-6 font-14 margin-right-8">开始使用时间:</span>
        <el-date-picker class="margin-right-8" v-model="tradeDate.start" type="daterange" size="small" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
        </el-date-picker>
      </div>

      <div class="margin-bottom-6 margin-right-5">
        <span class="text-6 font-14 margin-right-8">结束使用日期:</span>
        <el-date-picker class="margin-right-8" v-model="tradeDate.end" type="daterange" size="small" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
        </el-date-picker>
      </div> -->

    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(226)" v-loading="loading.table">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="index" label="序号" width="60"></el-table-column>
      <el-table-column label="单元" prop="building" min-width="120">
        <template slot-scope="{row}">
          {{row.building || '未提供'}}
        </template>
      </el-table-column>
      <el-table-column label="楼层" prop="floor" min-width="120">
        <template slot-scope="{row}">
          {{row.floor || '未提供'}}
        </template>
      </el-table-column>
      <el-table-column label="机构名称" prop="orgName" min-width="120"></el-table-column>
      <el-table-column label="房号" prop="roomNo" min-width="120"></el-table-column>
      <el-table-column label="房型" prop="roomType" min-width="120"></el-table-column>
      <!-- <el-table-column label="申请时间" prop="createTime" min-width="120"></el-table-column> -->
      <el-table-column label="结束日期" prop="useDateEnd" min-width="120"></el-table-column>
      <el-table-column label="开始日期" prop="useDateStart" min-width="120"></el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" size="small" @click.stop="clickDetailData(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <DialogApplyDetail ref="DialogApplyDetail"></DialogApplyDetail>
  </TableView>
</template>
  
  <script>
import { mapGetters } from 'vuex'
import DialogApplyDetail from '@/components/dialog/Apply/DialogApplyDetail'
export default {
  name: 'OrderList',
  components: {
    DialogApplyDetail
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          // tradeDate: {
          //   order: 300,
          // },
          orgId: {
            label: '申请机构',
            options: 'organization'
          }
          // classStatus: {
          //   label: '房型',
          //   options: 'classStatus'
          // }
        },
        
      },
      tableData: [],
      tradeDate: {
        submit: [],
        start: [],
        end: []
      },
      filterData: {},
      selectionList: [],
      timer: {
        table: null
      },
      loading: {
        table: null,
        doPass: null
      },
      rejectedValue: null
    }
  },
  computed: {
    ...mapGetters({
      organization: 'common/organization'
    }),
    params() {
      let [startDate, endDate] = this.tradeDate.submit || []
      return {
        ...this.filterData,
        applyDateStart:startDate,
        applyDateEnd:endDate,
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
    // 操作 - 获取申请数据
    async getTableData() {
      this.loading.table = true
      let { code, data } = await this.$api.Approve.classPage(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    // 操作 - 处理申请
    async doPass(params) {
      const isPass = params.isPass
      if (!isPass) {
        let { value } = await this.$prompt(`确认驳回“${params.applyUser}”的审批吗？请输入驳回理由`, {
          title: '驳回确认',
          showInput: true,
          inputPlaceholder: '请输入驳回理由(1-30字)',
          inputErrorMessage: '请输入驳回理由(1-30字)',
          inputPattern: /^.{1,30}$/
        })
        this.rejectedValue = value
      }
      const _params = {
        result: params.isPass ? 1 : 0,
        applyId: params.id,
        comment: params.isPass ? null : this.rejectedValue,
        attachList: null
      }
      this.loading.doPass = true
      let { code } = await this.$api.Approve.settle(_params)
      this.loading.doPass = false
      if (code !== 200) return false
      this.$message.success(params.isPass ? '通过审批' : '驳回审批')
      if (this.rejectedValue) this.rejectedValue = ''
      await this.getTableData()
    },
    // 操作 - 行点击事件
    clickData(row, event, column) {
      // console.log(row,event,column);
      if (row.applyResult === 1 || row.applyResult === 0) return
      this.$refs.DialogApplyDetail.open(row)
    },
    // 操作 - 点击详情
    clickDetailData(row) {
      this.$refs.DialogApplyDetail.open({
        ...row,
        type: 'ResultDetail'
      })
    }
  }
}
</script>
  
  <style scoped></style>
  