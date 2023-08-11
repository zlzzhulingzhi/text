<template>
  <TableView :options="options" :params.sync="filterData">
    <template slot="main">
      <div class="margin-bottom-8 margin-right-12">
        <el-date-picker v-model="date" type="date"
                        value-format="yyyy-MM-dd" format="yyyy-MM-dd" class="width-140" size="small" :clearable="false" :picker-options="pickerOptions"></el-date-picker>
      </div>
    </template>

    <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(182)" v-loading="loading.table" @row-click="showClassroomDetail">
      <!-- <el-table-column label="教室信息" min-width="180" fixed="left"> -->
      
      <el-table-column label="教室信息" min-width="180" align="center">
        <template slot-scope="{row}">
          {{row.building | dormitoryUnit}}{{row.roomNo}}({{row.roomType | classTypeInfo}})
        </template>
      </el-table-column>

      <el-table-column label="申请机构" min-width="180" align="center" v-if="filterData.summaryType === 'use'">
        <template slot-scope="{row}">
          {{ row.classroomScheduleDetailResponses[0]?.orgName }}
        </template>
      </el-table-column>

      <el-table-column label="申请日期" min-width="180" align="center" v-if="filterData.summaryType === 'use'">
        <template slot-scope="{row}">
          {{ row.classroomScheduleDetailResponses[0]?.useDate }}
        </template>
      </el-table-column>

      <el-table-column label="时间段" min-width="180" align="center" v-if="filterData.summaryType === 'use'">
        <template slot-scope="{row}">
          {{ row.usePeriod | usePeriodList }}
        </template>
      </el-table-column>

      <!-- <el-table-column v-for="item in range" :key="item.id" min-width="88" show-tooltip-when-overflow>

        <template slot="header" slot-scope="{row}">
          <div>{{ item.name }}</div>
        </template>

        <template slot-scope="{row}">

          <template v-if="isUse(row,item)">
            <el-tooltip effect="light" :content="orgName(row, item)" placement="top-start">
              <div class="text-warning">
                <b class="el-icon-check margin-right-4"></b>
                <span>已预订</span>
              </div>
            </el-tooltip>
          </template>

          <template v-else>
            <div class="text-success">
              <b class="el-icon-house margin-right-4"></b>
              <span>空闲</span>
            </div>
          </template>

        </template>
        
      </el-table-column> -->
    </el-table>

    <DialogClassroomDetail ref="DialogClassroomDetail"></DialogClassroomDetail>
  </TableView>
</template>

<script>
import { mapGetters } from 'vuex'
import DialogClassroomDetail from '@/components/dialog/Classes/DialogClassroomDetail'

export default {
  name: 'ClassroomStatisticsDetail',
  components: {
    DialogClassroomDetail
  },
  data() {

    return {

      options: {
        total: 0,
        pageSizes: [25, 50, 100],
        main: {
          summaryType: {
            label: '数据类型',
            options: [
              {id: 'total', name: '总数'},
              {id: 'use', name: '使用数量'},
              {id: 'free', name: '空闲数量'}
            ],
            clearable: false,
            value: null
          },
          roomType:{
            label: '教室类型',
            options: 'classTypeInfo',
            clearable: false,
            value: null
          }
        },
      },
      tableData: [],
      filterData: {},
    
      timer: {
        table: null
      },
      loading: {
        table: true
      },

      pickerOptions: {
        // disabledDate:(time)=>{
        //   let [startDate,endDate] = this.tradeDate
        //   return time.getTime() < (Date.now() - 24*60*60*1000)
        // }
      },

      // 查询日期
      date: null,
    }
  },
  created() {
    this.date = this.$route.params.useDate
    this.options.main.summaryType.value = this.filterData.summaryType = this.$route.params.summaryType
    this.options.main.roomType.value = this.filterData.roomType = this.$route.params.roomType
  },
  computed: {
    ...mapGetters({
      classTypeInfo: 'common/classTypeInfo',
      dormitoryUnit: 'common/dormitoryUnit',
    }),
    params() {
      return {
        ...this.filterData,
        useDate: this.date
      }
    },
    range() {
      let {startDate, endDate} = this.params
      startDate = endDate = this.date
      let arr = []
      endDate = new Date(endDate)
      for (let i = 0; i < 99; i++) {
        let d = new Date(startDate)
        d.setDate(d.getDate() + i)
        if (endDate.getTime() < d.getTime()) break
        let name = this.$moment(d).format('MM-DD(dd)')
        arr.push({
          id: name,
          name,
          value: d
        })
      }
      return arr
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
    // 判断  哪间教室哪天有使用
    isUse(row, item) {
      let curColumnDate = new Date(item.value).getTime()
      let usedTimeArray = row.classroomScheduleDetailResponses?.map(item => {
        return new Date(item.useDate).getTime()
      })
      return usedTimeArray?.some(item => item === curColumnDate)
    },
    // 获取  预订的教室信息
    orgName(row, item) {
      let curColumnDate = new Date(item.value).getTime()
      let usedTimeArray = row.classroomScheduleDetailResponses?.map(item => {
        return {
          ...item,
          useDate: new Date(item.useDate).getTime()
        }
      })
      let orgNameIndex = usedTimeArray?.findIndex(item => item.useDate === curColumnDate)
      return row.classroomScheduleDetailResponses[orgNameIndex].orgName
    },
    // 获取表格信息
    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.$api.OperationalStatistics.classroomSummaryDetail(this.params)
      this.loading.table = false
      if (code !== 200) return false
      // if(data === null) {
      //   this.tableData = null
      //   this.options.total = 0
      // } else {
      //   this.tableData = data.records
      //   this.options.total = data.total
      // }
      this.tableData = data.records
      this.options.total = data.total
    },
    // 教室详情弹窗
    showClassroomDetail(item) {
      this.$refs.DialogClassroomDetail.open({
          formData: item
      })
    }
  }
}
</script>

<style scoped lang="stylus">
.el-form
  .width-140
    width 140px

>>> .el-date-editor
  width 160px

  &.el-range-editor
    width 240px
</style>
