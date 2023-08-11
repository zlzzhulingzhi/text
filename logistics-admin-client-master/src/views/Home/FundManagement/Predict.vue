<template>
  <div class="width-100p">
    <Breadcrumb class="margin-bottom-12 margin-left-16"></Breadcrumb>
    <div class="moneyDetail-header">
      <ul class="moneyDetail-list width-100p flex start-center wrap">
        <li class="moneyDetail-item padding-left-62 padding-right-62 margin-14 flex between-center" v-for="(item,index) in moneyData" :key="item.id">
          <div class="">
            <img style="width: 90px; height: 90px" :src="item.imgUrl">
          </div>
          <div class="flex column center-center moneyDetail-item-right">
            <span class="moneyDetail-item-right-cash">{{item.price}}</span>
            <span>{{item.title}}</span>
          </div>
        </li>
      </ul>
    </div>
    <TableView :options="options" :params.sync="filterData">

      <template v-slot:main>
        <div class="timer">
          培训开始时间：
        </div>
        <div class="timer" style="order:90;">
          培训结束时间： <el-date-picker class="date-picker" v-model="endDate" size="small" type="daterange" start-placeholder="起始日期" end-placeholder="截至日期" value-format="yyyy-MM-dd" align="center"></el-date-picker>
        </div>
        <div class="timer flex start-center" style="order:90;">
          资助预估金额(元)：
          <el-input v-model="beginMoney" class="width-140" size="small"></el-input>
          <div class="margin-10">至</div>
          <el-input v-model="endMoney" class="width-140" size="small"></el-input>
        </div>
      </template>

      <template v-slot:footer>
        <!-- <el-button size="small" :disabled="!selectionList.length" @click="onBatchEnabled(item.id,selectionList)">
          批量审批
        </el-button> -->
      </template>

      <div class=" flex center-center">
        <div>
         
        </div>
      </div>

      <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(215)" v-loading="loading.table" @selection-change="selectionList = $event.map(item=>item.id)">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>
        <el-table-column type="index" label="序号" width="55"></el-table-column>
        <el-table-column label="培训单位" prop="name" min-width="150"></el-table-column>
        <el-table-column label="培训班级" prop="className" min-width="150"></el-table-column>
        <el-table-column label="参训人数" prop="contacts" min-width="150"></el-table-column>
        <el-table-column label="资助预估金额(元)" prop="contacts" min-width="150"></el-table-column>
        <el-table-column label="培训场所资助(元)" prop="contacts" min-width="150"></el-table-column>
        <el-table-column label="学生就餐资助费用(元)" prop="contacts" min-width="100"></el-table-column>
        <el-table-column label="学员住宿资助费用(元)" prop="contacts" min-width="100"></el-table-column>
        <el-table-column label="学员交通资助费用(元)" prop="contacts" min-width="100"></el-table-column>
        <el-table-column label="学员学费资助金额(元)" prop="contacts" min-width="100"></el-table-column>
        <el-table-column label="培训开始时间" prop="contacts" min-width="150"></el-table-column>
        <el-table-column label="培训结束时间" prop="contacts" min-width="150"></el-table-column>
      </el-table>

    </TableView>
  </div>

</template>

<script>
import { DownloadBase } from 'knight-utils'
import { mapGetters } from 'vuex'

export default {
  name: 'Predict',
  components: {
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          tradeDate: {
            order: 80
          }
        },
        side:'className:培训班级,name:培训单位'
      },
      tableData: [],
      filterData: {},
      selectionList: [],
      timer: {
        table: null
      },
      loading: {
        table: true
      },
      dataTime: '', // 资助申请时间
      beginMoney: null, //资助费用范围
      endMoney: null,
      tableArr: [], //定义空数组，作为临时存储每一行已处理的数据
      pos: 0,
      endDate:null,
      moneyData: [
        {
          id: 0,
          imgUrl: require('@/assets/icons/download01.png'),
          title: '培训场所资助 (元)',
          price: '0'
        },
        {
          id: 1,
          imgUrl: require('@/assets/icons/download02.png'),
          title: '学生就餐资助 (元)',
          price: '0'
        },
        {
          id: 2,
          imgUrl: require('@/assets/icons/download03.png'),
          title: '学员住宿资助 (元)',
          price: '0'
        },
        {
          id: 3,
          imgUrl: require('@/assets/icons/download04.png'),
          title: '学员交通资助 (元)',
          price: '0'
        },
        {
          id: 4,
          imgUrl: require('@/assets/icons/download05.png'),
          title: '学员学费资助 (元)',
          price: '0'
        },
        {
          id: 5,
          imgUrl: require('@/assets/icons/download06.png'),
          title: '资助预估总计 (元)',
          price: '0'
        }
      ]
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      app: 'common/app',
      ApplyAuditSatus: 'common/ApplyAuditSatus',
      fundingObject: 'common/fundingObject',
      passStatus: 'common/passStatus'
    }),
    params() {
      return {
        ...this.filterData
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
      let { code, data } = await this.$api.ApplyPubAPI.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.tableArr = []
      this.options.total = data.total
    },

    // 操作 - 新增班级
    onCreate() {
      this.$refs.DialogFormClassroomManage.open({
        type: 'OrgRoleCreate'
      })
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
    // 操作 - 启用/禁用
    onEnabled({ id, enabled, name }) {
      this.onBatchEnabled(enabled, [id], name)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name) {
      this.loading.table = true
      let { code } = await this.$api.ClassroomManageAPI.batchEnabled({
        idList,
        flag
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg[flag ? 'Enabled' : 'Disabled'](name, !name)
      await this.getTableData()
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
>>> .el-table .el-table__header-wrapper .el-table__header thead tr th .cell
  height 52px !important
  line-height 24px
  display flex
  align-items center
.accessory
  &:hover
    color #c63636
    text-decoration underline
.moneyDetail-header
  .moneyDetail-list
    .moneyDetail-item
      background #fff
      height 152px
      width 490px
      box-sizing border-box
      border-radius 15px
      border 1px solid #ccc
      .moneyDetail-item-right
        border-left 1px solid #ccc
        padding-left 50px
        .moneyDetail-item-right-cash
          display block
          margin 14px

.date-picker
  width 240px!important
</style>
