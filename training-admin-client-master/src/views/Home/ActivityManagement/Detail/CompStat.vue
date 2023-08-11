<!--学习数据-->
<template>
  <div class="padding-16">
    <span class="text-bold font-14">数据概况</span>
    <el-row class="margin-top-32 margin-bottom-32">
      <el-col class="flex column center-center" :span="6">
        <span class="text-bold font-18 margin-bottom-16">{{ statistic.signUpNum || '0' }}</span>
        <span>总报名人数</span>
      </el-col>
      <el-col class="flex column center-center" :span="6">
        <span class="text-bold font-18 margin-bottom-16">{{ statistic.studyTotalNum || '0' }}</span>
        <span>总学习人数</span>
      </el-col>
      <el-col class="flex column center-center" :span="6">
        <span class="text-bold font-18 margin-bottom-16">{{ statistic.studyCompletedNum || '0' }}</span>
        <span>总完成人数</span>
      </el-col>
      <el-col class="flex column center-center" :span="6">
        <span class="text-bold font-18 margin-bottom-16">{{ $utils.getPercent(statistic.studyCompletedNum, statistic.signUpNum) }}</span>
        <span>累计完课率</span>
      </el-col>
    </el-row>
    <el-divider></el-divider>
    <div class="flex between-center text-bold font-14 margin-bottom-32">
      <span>学习走势图</span>
      <div>
        <span>时间筛选：</span>
        <el-select class="width-100" v-model="dateType" placeholder="请选择" size="small" @change="dateChange">
          <el-option label="近7天" :value="7"></el-option>
          <el-option label="近15天" :value="15"></el-option>
          <el-option label="近30天" :value="30"></el-option>
        </el-select>
      </div>
    </div>
    <ChartTrend :data="listData"></ChartTrend>
  </div>
</template>

<script>
import ChartTrend from './ChartTrend.vue'

export default {
  components: {
    ChartTrend
  },
  data() {
    return {
      dateType: 7,
      startDate: this.$moment().subtract(7, 'days').format('YYYY-MM-DD'),
      endDate: this.$moment().format('YYYY-MM-DD'),
      listData: []
    }
  },
  props: {
    statistic: {
      type: Object,
      default: () => {}
    }
  },
  watch: {
    statistic: {
      deep: true,
      handler() {
        this.getDailyLine()
      },
      immediate: true
    }
  },
  methods: {
    // 统计数据
    async getDailyLine() {
      if (!this.statistic.id) return
      let { code, data } = await this.$api.CourseStat.dailyLine({
        courseId: this.statistic.id,
        startStatisticDate: this.startDate,
        endStatisticDate: this.endDate
      })
      if (code !== 200) return false
      this.listData = data
    },
    dateChange(value) {
      this.startDate = this.$moment().subtract(value, 'days').format('YYYY-MM-DD')
      this.endDate = this.$moment().format('YYYY-MM-DD')
      this.getDailyLine()
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>