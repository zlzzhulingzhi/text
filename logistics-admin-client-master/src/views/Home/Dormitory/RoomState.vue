<template>
  <TableView :options="options" :params.sync="filterData">
    <template slot="main">
      <div class="margin-bottom-8 margin-right-12">
        <el-date-picker v-model="tradeDate" type="daterange" start-placeholder="起始日期" end-placeholder="截至日期"
                        value-format="yyyy-MM-dd" format="yyyy-MM-dd" align="center" size="small" :clearable="false" :picker-options="pickerOptions"></el-date-picker>
      </div>
    </template>

    <div v-if="showOrNot">
      <div class="margin-top-8 margin-bottom-8">
        <div class="margin-bottom-20 text-bold">本次时间范围内总体信息</div>
        <template v-for="(item, index) in dormitoryType" >
          <span class="margin-right-30" :key="index">{{item.name}}剩余：{{emptyNum[item.id]}}</span>
        </template>
      </div>
  
      <div class="bg-global-background height-16 relative" style="width: calc(100% + 32px); right: 16px"></div>
    </div> 
    
    <el-table class="margin-top-12" :data="tableData" :height="showOrNot ? $utils.FullViewHeight(277) : $utils.FullViewHeight(182)" v-loading="loading.table">

      <el-table-column label="宿舍信息" min-width="180" fixed="left">
        <template slot-scope="{row}">
          {{row.building | dormitoryUnit}}{{row.roomNo}}({{row.roomType | dormitoryType}})
        </template>
      </el-table-column>

      <el-table-column v-for="item in range" :key="item.id" min-width="88" show-tooltip-when-overflow>
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
      </el-table-column>
    </el-table>
  </TableView>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'RoomState',
  data() {
    let startDate = new Date()
    let endDate = new Date()
    endDate.setDate(endDate.getDate() + 6)
    startDate = this.$utils.formatTime(startDate.getTime(), 'YYYY-MM-DD')
    endDate = this.$utils.formatTime(endDate.getTime(), 'YYYY-MM-DD')


    return {
      dateNow: new Date().getTime(),

      options: {
        total: 0,
        main: {
          // useState: {
          //   label: '入住状态',
          //   options: [
          //     {id: 0, name: '空闲'},
          //     {id: 1, name: '预订'}
          //   ]
          // },
          building: {
            label: '宿舍单元',
            options: 'dormitoryUnit'
          },
          roomType:{
            label: '房型',
            options: 'dormitoryType'
          }
        },
        side: 'numbe:房号'
      },
      tableData: [],
      filterData: {},
      selectionList: [],
      tradeDate: [startDate,endDate],

      timer: {
        table: null
      },
      loading: {
        table: false
      },

      pickerOptions: {
        disabledDate:(time)=>{
          let [startDate,endDate] = this.tradeDate
          return time.getTime() < (Date.now() - 24*60*60*1000) 
        }
      },

      // 剩余数量信息的存储
      emptyNum: {
        single: 0,
        singlePlus: 0,
        double: 0,
        quadruple: 0,
        accessible: 0,
        working: 0,
      },
      // 控制剩余数量信息是否展示
      showOrNot: false
    }
  },
  computed: {
    ...mapGetters({
      dormitoryType: 'common/dormitoryType',
      dormitoryUnit: 'common/dormitoryUnit',
      dormitoryFloor: 'common/dormitoryFloor',
    }),
    params() {
      let [startDate, endDate] = this.tradeDate || []

      // 控制最多选30天
      if(startDate) {
        let startDateStamp = new Date(startDate).getTime() 
        let endDateStamp = new Date(endDate).getTime()
        let timeStampOfTwo = endDateStamp - startDateStamp
        let dayDifference = parseInt(timeStampOfTwo / (1000 * 60 * 60 * 24)) + 1
        if(dayDifference > 30) {
          endDateStamp = startDateStamp + 29*24*60*60*1000
          endDate = this.$utils.formatTime(endDateStamp, 'YYYY-MM-DD 00:00:00')
          this.tradeDate = [startDate, endDate]
          this.$message('所选时间范围超过30天，系统将默认选择从起始时间开始的30天')
        }
      }

      return {
        startDate,
        endDate,
        ...this.filterData
      }
    },
    range() {
      let {startDate, endDate} = this.params
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
    },
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
    // 判断是否预订
    isUse(row, item) {
      let curColumnDate = new Date(item.value).getTime()
      let usedTimeArray = row.dormitoryScheduleDetailResponses.map(item => {
        return new Date(item.useDate).getTime()
      })
      return usedTimeArray.some(item => item === curColumnDate)
    },
    // 获取预订机构信息
    orgName(row, item) {
      let curColumnDate = new Date(item.value).getTime()
      let usedTimeArray = row.dormitoryScheduleDetailResponses.map(item => {
        return {
          ...item,
          useDate: new Date(item.useDate).getTime()
        }
      })
      let orgNameIndex = usedTimeArray.findIndex(item => item.useDate === curColumnDate)
      return row.dormitoryScheduleDetailResponses[orgNameIndex].orgName
    },
    // 获取宿舍信息
    async getTableData() {
      this.showOrNot = false
      this.loading.table = true
      let {code, data} = await this.$api.DormitorySchedule.getDormitoryState(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total

      // 查询条件仅一天是，展示  房间剩余数量
      if(this.tradeDate[0] === this.tradeDate[1]) {
        this.showOrNot = true
        let {code, data} = await this.$api.DormitorySchedule.getDormitoryNum({
          startDate: this.tradeDate[0]
        })
        if(code !== 200) return false
        Object.keys(this.emptyNum).forEach(item => {
          data.forEach(i => {
            if(i.roomType === item) {
              this.emptyNum[item] = i.freeNum
            }
          })
        })
      }
    },
    
  }
}
</script>

<style scoped lang="stylus">
>>> .el-date-editor
  width 160px

  &.el-range-editor
    width 240px

</style>
