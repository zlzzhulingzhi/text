<template>
  <TableView :options="options" :params.sync="filterData">
    <template slot="main">
      <div class="margin-bottom-8 margin-right-12">
        <el-date-picker v-model="tradeDate" type="daterange" start-placeholder="起始日期" end-placeholder="截至日期"
                        value-format="yyyy-MM-dd" format="yyyy-MM-dd" align="center" size="small"  :clearable="false" :picker-options="pickerOptions"></el-date-picker>
      </div>
    </template>

    <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(182)" v-loading="loading.table" @row-click="showClassroomDetail">
      <el-table-column label="教室信息" width="220" fixed="left">
        <template slot-scope="{row}">
          {{row.building | dormitoryUnit}}{{row.roomNo}}({{row.roomType | classTypeInfo}})
        </template>
      </el-table-column>

      <el-table-column v-for="item in range" :key="item.id" min-width="88">

        <template slot="header" slot-scope="{row}">
          <div>{{ item.name }}</div>
        </template>

        <template slot-scope="{row}">

          <template v-if="isUse(row,item)">
            <el-tooltip effect="light" placement="top">
              <div slot="content" class="flex column">
                <span v-for="(item, index) in orgName(row, item)" :key="index">
                  {{ item }}
                </span>
              </div>
              <div class="text-warning">
                <b class="el-icon-check margin-right-4"></b>
                <span>已预订</span>
              </div>
            </el-tooltip>
          </template>
          <!-- <template v-if="isUse(row,item)">
            <el-tooltip effect="light" :content="orgName(row, item)" placement="top">
              <div class="text-warning">
                <b class="el-icon-check margin-right-4"></b>
                <span>已预订</span>
              </div>
            </el-tooltip>
          </template> -->

          <template v-else>
            <div class="text-success">
              <b class="el-icon-house margin-right-4"></b>
              <span>空闲</span>
            </div>  
          </template>

        </template>
        
      </el-table-column>
    </el-table>

    <DialogClassroomDetail ref="DialogClassroomDetail"></DialogClassroomDetail>
  </TableView>
</template>

<script>
import { mapGetters } from 'vuex'
import DialogClassroomDetail from '@/components/dialog/ClassesManagement/DialogClassroomDetail'
import mxTableView from '@/components/mixins/mxTableView'

export default {
  name: 'ClassroomOrderInfoCopy',
  components: {
    DialogClassroomDetail
  },
  mixins: [mxTableView],
  data() {
    let startDate = new Date()
    let endDate = new Date()
    endDate.setDate(endDate.getDate() + 6)
    startDate = this.$utils.formatTime(startDate.getTime(), 'YYYY-MM-DD')
    endDate = this.$utils.formatTime(endDate.getTime(), 'YYYY-MM-DD')

    return {
      dateNow: new Date().getTime(),

      options: {
        main: {
          roomType:{
            label: '教室类型',
            options: 'classTypeInfo'
          }
        },
        side: 'roomNo:教室编号'
      },
      tradeDate: [startDate,endDate],

      pickerOptions: {
        disabledDate:(time)=>{
          let [startDate,endDate] = this.tradeDate
          return time.getTime() < (Date.now() - 24*60*60*1000)
        }
      }
    }
  },
  computed: {
    ...mapGetters({
      classTypeInfo: 'common/classTypeInfo',
      dormitoryUnit: 'common/dormitoryUnit',
      usePeriodList: 'common/usePeriodList',
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
          endDate = this.$utils.formatTime(endDateStamp, 'YYYY-MM-DD')
          this.tradeDate = [startDate, endDate]
          this.$message('所选时间范围超过30天，系统将默认选择从起始时间开始的30天')
        }
      }
      
      // 似乎这个if没用
      if (!startDate) {
        startDate = new Date()
        endDate = new Date()
        endDate.setDate(endDate.getDate() + 7)
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
    tableDataAPI() {
      return this.$api.Classroom.getManageClassroomState
    }
  },
  methods: {
    // 判断  哪间教室哪天有使用
    isUse(row, item) {
      let curColumnDate = new Date(item.value).getTime()
      let usedTimeArray = row.classroomScheduleDetailResponses.map(item => {
        return new Date(item.useDate).getTime()
      })
      return usedTimeArray.some(item => item === curColumnDate)
    },
    // 获取  预订的教室信息
    orgName(row, item) {
      // let curColumnDate = new Date(item.value).getTime()
      // let usedTimeArray = row.classroomScheduleDetailResponses.map(item => {
      //   return {
      //     ...item,
      //     useDate: new Date(item.useDate).getTime()
      //   }
      // })
      // let orgNameIndex = usedTimeArray.findIndex(item => item.useDate === curColumnDate)
      // return row.classroomScheduleDetailResponses[orgNameIndex].orgName

      // 添了时间段后的做法
      let curColumnDate = new Date(item.value).getTime()
      let usedTimeArray = row.classroomScheduleDetailResponses.map(item => {
        return {
          ...item,
          useDate: new Date(item.useDate).getTime()
        }
      })
      let occupyOrgInfoList = usedTimeArray.filter(item => item.useDate === curColumnDate) || []
      occupyOrgInfoList = occupyOrgInfoList.map(item => {
        return `${this.usePeriodList.find(a => a.id === item.usePeriod).name}(${item.orgName})`
      })
      return occupyOrgInfoList
    },
    
    // 班级详情弹窗
    showClassroomDetail(item) {
      this.$refs.DialogClassroomDetail.open({
          formData: item
      })
      
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-date-editor
  width 160px

  &.el-range-editor
    width 240px
</style>
