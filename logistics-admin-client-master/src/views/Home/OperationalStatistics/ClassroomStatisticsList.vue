<template>
  <TableView :options="options" :params.sync="filterData">

    <template v-slot:main>
      <el-date-picker class="margin-right-12 margin-bottom-8 width-140" v-model="date" type="date" :clearable="false" size="small"  value-format="yyyy-MM-dd" format="yyyy-MM-dd" placeholder="选择日期">
        </el-date-picker>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(226)"  v-loading="loading.table">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="index" label="序号" width="60"></el-table-column>
      <el-table-column label="教室类型" prop="roomType" min-width="120">
        <template slot-scope="{row}">
          <span>{{ row.roomType | classTypeInfo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总数（间）" prop="totalNum" min-width="120">
        <template slot-scope="{ row }">
          <span @click="toDetail({roomType: row.roomType, summaryType: 'total'})" class="pointer hoverStyle">{{ row.totalNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="使用数量（间）" prop="useNum" min-width="120">
        <template slot-scope="{ row }">
          <span @click="toDetail({roomType: row.roomType, summaryType: 'use'})" class="pointer hoverStyle">{{ row.useNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="空闲数量（间）" prop="freeNum" min-width="120">
        <template slot-scope="{ row }">
          <span @click="toDetail({roomType: row.roomType, summaryType: 'free'})" class="pointer hoverStyle">{{ row.freeNum }}</span>
        </template>
      </el-table-column>
    </el-table>

  </TableView>
</template>
  
<script>
import { mapState} from 'vuex'
import mxTableView from '@/components/mixins/mxTableView'
export default {
  name: 'ClassroomStatisticsList',
  components: {
  },
  mixins: [mxTableView],
  data() {
    let startDate = new Date()
    startDate = this.$utils.formatTime(startDate.getTime(), 'YYYY-MM-DD')
    return {
      options: {
        main: {
          roomType: {
            label: '教室类型',
            options: 'classTypeInfo'
          },
          // date: {
          //   order: 90
          // }
        }
      },
      date: startDate,
    }
  },
  computed: {
    ...mapState({
      classTypeInfo: 'common/classTypeInfo'
    }),
    params() {
      return {
        ...this.filterData,
        useDate: this.date
      }
    },
    tableDataAPI() {
      return this.$api.OperationalStatistics.classroomSummary
    }
  },
  methods: {
    // 操作 - 跳转到详情页
    toDetail(row) {
      this.$router.push({
        name: 'ClassroomStatisticsDetail',
        params: {
          roomType: row.roomType,
          summaryType: row.summaryType,
          useDate: this.date
        }
      })
      // this.$utils.WindowOpenInParentFrame(`/Classroom/ClassroomStatisticsDetail?roomType=${row.roomType}&summaryType=${row.summaryType}&useDate=${this.date}`)
    }
  }
}
</script>
  
<style lang="stylus" scoped>
.el-form
  .width-140
    width 140px

// 统计数字悬浮样式
.hoverStyle
  &:hover
    color MAIN_COLOR
</style>
  