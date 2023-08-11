<template>
  <TableView :options="options" :params.sync="filterData">

    <template v-slot:main>
      <div class="margin-bottom-8 margin-right-12 timer">
          <el-date-picker
            v-model="startDate"
            class="setWidth"
            size="small"
            type="date"
            value-format="yyyy-MM-dd 00:00:00" 
            format="yyyy-MM-dd"
            placeholder="起始日期">
          </el-date-picker>
          -
          <el-date-picker
            v-model="endDate"
            class="setWidth"
            size="small"
            type="date"
            value-format="yyyy-MM-dd 23:59:59" 
            format="yyyy-MM-dd"
            placeholder="截至日期">
          </el-date-picker>
        </div>
    </template>

    <div class="flex start-center text-3 text-bold margin-top-18">
      <div class="margin-right-80">累计开班：<span>{{ totalClasses || '--' }}</span><span v-if="totalClasses">班次</span></div>
      <div>累计培训：<span>{{ totalStudents || '--' }}</span><span v-if="totalStudents">人次</span></div>
    </div>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(226)"  v-loading="loading.table">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="index" label="序号" width="60"></el-table-column>
      <el-table-column label="机构名称" prop="orgName" min-width="120"></el-table-column>
      <el-table-column label="培训班总数" prop="clazzCount" min-width="120">
        <template slot-scope="{ row }">
          <!-- <span @click="toDetail({roomType: row.roomType, summaryType: 'total'})" class="pointer hoverStyle">{{ row.totalClazzCount }}</span> -->
          <span>{{ row.clazzCount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="学生总数" prop="studentCount" min-width="120">
        <template slot-scope="{ row }">
          <!-- <span @click="toDetail({roomType: row.roomType, summaryType: 'use'})" class="pointer hoverStyle">{{ row.useNum }}</span> -->
          <span>{{ row.studentCount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总学时" prop="lessonNumCount" min-width="120">
        <template slot-scope="{ row }">
          <!-- <span @click="toDetail({roomType: row.roomType, summaryType: 'use'})" class="pointer hoverStyle">{{ row.useNum }}</span> -->
          <span>{{ row.lessonNumCount }}</span>
        </template>
      </el-table-column>
      
    </el-table>

  </TableView>
</template>
  
<script>
import { mapState} from 'vuex'
import mxTableView from '@/components/mixins/mxTableView'
export default {
  name: 'OrgTrainStatisticsList',
  components: {
  },
  mixins: [mxTableView],
  data() {
    return {
      options: {
        main: {
          orgId: {
            label: '机构',
            options: 'organization'
          },
          // date: {
          //   order: 90
          // }
        }
      },

      // 开始 截止日期
      startDate: undefined,
      endDate: undefined,

      totalClasses: null,
      totalStudents: null,

      totalTime: null

    }
  },
  // created() {
  //   this.getTotalNum()
  // },
  computed: {
    ...mapState({
    }),
    params() {
      return {
        ...this.filterData,
        startDate: this.startDate,
        endDate: this.endDate
      }
    },
    TotalSummaryParams() {
      return {
        ...this.params
      }
    },
    tableDataAPI() {
      return this.$api.OperationalStatistics.orgClassPage
    }
  },
  watch: {
    TotalSummaryParams: {
      deep: true,
      immediate: true,
      handler() {
        clearTimeout(this.totalTime)
        this.totalTime = setTimeout(() => {
          this.getTotalNum().then()
        }, 300)
      }
    }
  },
  methods: {
    // 操作 - 跳转到详情页
    // toDetail(row) {
    //   this.$router.push({
    //     name: 'ClassroomStatisticsDetail',
    //     params: {
    //       roomType: row.roomType,
    //       summaryType: row.summaryType,
    //       useDate: this.date
    //     }
    //   })
    //   // this.$utils.WindowOpenInParentFrame(`/Classroom/ClassroomStatisticsDetail?roomType=${row.roomType}&summaryType=${row.summaryType}&useDate=${this.date}`)
    // }
    async getTotalNum() {
      let {code, data} = await this.$api.OperationalStatistics.orgClassTotalSummary({
        ...this.params,
      })
      if(code !== 200) return false
      this.totalClasses = data.clazzCount
      this.totalStudents = data.studentCount
    }
  }
}
</script>
  
<style lang="stylus" scoped>
.timer
  margin 0 12px 8px 0
  font-size 14px
  .setWidth
      width 140px
</style>
  