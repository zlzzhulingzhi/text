 <template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <div class="margin-bottom-6">
        <span class="text-6 font-14 margin-right-2">申请日期:</span>
        <el-date-picker class="margin-right-12" v-model="tradeDate.submit" type="daterange" size="small" range-separator="至" value-format="yyyy-MM-dd" format="yyyy-MM-dd" start-placeholder="开始日期" end-placeholder="结束日期">
        </el-date-picker>
      </div>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(226)"  v-loading="loading.table">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="index" label="序号" width="60"></el-table-column>
      <el-table-column label="专题名称" prop="courseName" min-width="120"></el-table-column>
      <el-table-column label="申请事由" prop="applyReason" min-width="120"></el-table-column>
      <el-table-column label="申请机构" prop="orgName" min-width="120"></el-table-column>
      <el-table-column label="联系人" prop="contactPerson" min-width="100"></el-table-column>
      <el-table-column label="联系电话" prop="contactNumber" min-width="120"></el-table-column>
      <el-table-column label="联系邮箱" prop="contactEmail" min-width="120"></el-table-column>
      <el-table-column label="申请人" prop="applyUserName" min-width="100"></el-table-column>
      <el-table-column label="申请时间" prop="applyDate" min-width="120"></el-table-column>
      <el-table-column label="审批状态" prop="status" width="85">
        <EleDot slot-scope="{row}" :id="row.status" type="approvalStatus"></EleDot>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" size="small" @click.stop="clickDetailData(row)">详情</el-button>
          <el-button type="text" size="small" @click.stop="doPass(row)" v-if="row.status === 11" :loading="loading.doPass">审批</el-button>
          <el-button type="text" size="small" v-else @click.stop="onApproveDetail(row)">审批情况</el-button>
        </template>
      </el-table-column>
    </el-table>
    <DialogApplyDetail ref="DialogApplyDetail"></DialogApplyDetail>
    <!-- 审批详情弹窗 -->
    <DialogApplyForFunding ref="DialogApplyForFunding" @success="getTableData"></DialogApplyForFunding>

  </TableView>
</template>
  
  <script>
import { mapGetters,mapState} from 'vuex'
import DialogApplyDetail from '@/components/dialog/Apply/DialogApplyDetail'
import DialogApplyForFunding from '@/components/dialog/Apply/DialogApplyForFunding'
export default {
  name: 'OrderList',
  components: {
    DialogApplyDetail,
    DialogApplyForFunding
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          orgId: {
            label: '申请机构',
            options: 'organization'
          },
          status: {
            label: '审批状态',
            options: 'approvalStatus',
            value:11
          }
        }
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
    }),
    ...mapState({
      approvalStatus: 'common/approvalStatus'
    }),
    params() {
      let [startDate, endDate] = this.tradeDate.submit || []
      return {
        ...this.filterData,
        applyDateStart: startDate,
        applyDateEnd: endDate,
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
      let { code, data } = await this.$api.Approve.pageV2(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
     // 排序
    //  sortChange({prop, order}) {
    //   this.sort.sortField = prop || undefined
    //   this.sort.sortOrder = order ? order.replace('ending', '') : undefined
    // },
    // 操作 - 处理申请
    async doPass(row) {
      // await this.getTableData()
      this.$refs.DialogApplyForFunding.open({
        ...row,
        type: 'classroom'
      })
    },
    // 操作 - 行点击事件
    clickData(row, event, column) {
      if (row.applyResult === 1 || row.applyResult === 0) return
      this.$refs.DialogApplyDetail.open(row)
    },
    // 操作 - 点击详情
    clickDetailData(row) {
      // this.$refs.DialogApplyDetail.open({
      //   ...row,
      //   type: 'OrderDetail'
      // })

      // this.$router.push({
      //   name: 'FacilityApplyDetail',
      //   query: {
      //     id: row.id
      //   }
      // })

      this.$utils.WindowOpenInParentFrame(`/Classroom/Detail?id=${row.id}`)
    },
    // 操作 - 审批情况
    onApproveDetail(row) {
      console.log('审批详情');
      this.$refs.DialogApplyForFunding.open({
        ...row,
        type: 'detail'
      })
    }
  }
}
</script>
  
  <style scoped></style>
  