<template>

  <div class="overflow-auto padding-bottom-16 bg-global-background"
    :style="{ height: $utils.FullViewHeight(32) }">
    <div class="flex bg-white-background padding-top-16 padding-left-16 padding-right-16 height-184">
      <FeeChart type="bar" :seriesMaps="feeMaps" :years="years"></FeeChart>
      <div class="width-46 flex center-center">
        <div class="divide-line"></div>
      </div>
      <NumberChart type="bar" :seriesMaps="numberMaps" :years="numYears"></NumberChart>
    </div>
    <div class="flex margin-top-16 bg-white-background padding-top-16 padding-left-16 padding-right-16">
      <FeeChart type="line" :seriesMaps="feeMaps" :years="years"></FeeChart>
      <div class="width-46 flex center-center">
        <div class="divide-line"></div>
      </div>
      <NumberChart type="line" :seriesMaps="numberMaps" :years="numYears"></NumberChart>
    </div>

    <div class="padding-top-12 padding-left-16 line-height-32 flex margin-top-16 bg-white-background">
      <div class="margin-right-12">年份</div>
      <el-date-picker v-model="year" type="year" value-format="yyyy" placeholder="选择年份" size="small"
        :picker-options="pickerOptions"></el-date-picker>
    </div>
    <div class="height-16 bg-white-background"></div>
    <div class="flex bg-white-background padding-left-16 padding-right-16">
      <PieChart :year="year" :seriesMaps="coursePieMaps" name="资助费占比"></PieChart>
      <div class="width-46 flex center-center">
        <div class="divide-line"></div>
      </div>
      <PieChart :year="year" :seriesMaps="feePieMaps" name="课程资助费占比"></PieChart>
    </div>
    <div class="margin-top-16">
      <TableData></TableData>
    </div>

  </div>

</template>

<script>
import NumberChart from '@/views/Home/Statistic/Chart/NumberChart'
import FeeChart from '@/views/Home/Statistic/Chart/FeeChart'
import PieChart from '@/views/Home/Statistic/Chart/PieChart'
import TableData from '@/views/Home/Statistic/Chart/TableData'

export default {
  name: 'orgStatistics',
  components: {
    NumberChart,
    FeeChart,
    PieChart,
    TableData
  },
  created() {
    this.annualFundingSFP();
    this.annualFSF();
    this.annualFSS();
  },
  data() {
    return {
      year: new Date().getFullYear().toString(),
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        }
      },

      feeMaps: [
        { id: 'a', name: '年总计', data: [] },
        { id: 'b', name: '场所费', data: [] },
        { id: 'c', name: '就餐费', data: [] },
        { id: 'd', name: '住宿费', data: [] },
        { id: 'e', name: '交通费', data: [] },
        { id: 'f', name: '课程费', data: [] },
        { id: 'g', name: '学员费', data: [] }
      ],

      numberMaps: [
        { id: 'a', name: '培训人次', data: [] },
        { id: 'b', name: '培训课时', data: [] }
      ],
      // 资助费用占比饼图数据
      coursePieMaps: [{ id: 'a', name: '活动资助费', data: 0 },
      { id: 'b', name: '课程资助费', data: 0 }],
      // 课程资助费饼图数据
      feePieMaps: [{ id: 'a', name: '场所费', data: 0 },
      { id: 'b', name: '就餐费', data: 0 },
      { id: 'c', name: '住宿费', data: 0 },
      { id: 'd', name: '交通费', data: 0 },
      { id: 'e', name: '课程费', data: 0 },
      { id: 'f', name: '学员费', data: 0 },
      ],

      // 柱状图/折线图 横轴年份
      years: [],
      // 课时-人次统计年份
      numYears: []
    }
  },

  methods: {
    // 饼图-费用统计
    async annualFundingSFP() {
      const { code, data } = await this.$api.FundingStatistics.annualFundingStatisticsFeePie({
        year: this.year
      });

      if (code !== 200) return false;
      this.coursePieMaps.forEach((obj, i) => {
        if (i === 0) {
          obj.data = data?.totalActivityFee;
        } else if (i === 1) {
          obj.data = data?.totalCourseFee;
        }
      });

      // console.log('data?. === ', data)

      this.feePieMaps.forEach((obj, i) => {
        if (i === 0) {
          obj.data = data?.siteFundFee;
        } else if (i === 1) {
          obj.data = data?.mealFundFee;
        } else if (i === 2) {
          obj.data = data?.roomFundFee;
        } else if (i === 3) {
          obj.data = data?.trafficFundFee;
        } else if (i === 4) {
          obj.data = data?.totalCourseFee;
        } else if (i === 5) {
          obj.data = data?.studyFundFee;
        }

      })
      // console.log('this.coursePieMap == ', this.coursePieMaps)
    },
    // 费用统计
    async annualFSF() {
      const { code, data } = await this.$api.FundingStatistics.annualFundingStatisticsFee(
        {}
      );
      // console.log(`feeCode = ${code},\n result = `, data);
      if (code !== 200) return false;
      const { years, itemList } = data;
      this.years = years;
      // console.log('years == ', years);
      // console.log('itemList == ', itemList);
      this.feeMaps.forEach((obj, i) => {
        if (i === 0) {
          obj.data = itemList?.totalFeeList ? itemList.totalFeeList : [];
        } else if (i === 1) {
          obj.data = itemList?.siteFundFeeList ? itemList.siteFundFeeList : [];
        } else if (i === 2) {
          obj.data = itemList?.mealFundFeeList ? itemList.mealFundFeeList : [];
        } else if (i === 3) {
          obj.data = itemList?.roomFundFeeList ? itemList.roomFundFeeList : [];
        } else if (i === 4) {
          obj.data = itemList?.trafficFundFeeList ? itemList.trafficFundFeeList : [];
        } else if (i === 5) {
          obj.data = itemList?.totalCourseFeeList ? itemList.totalCourseFeeList : [];
        } else if (i === 6) {
          obj.data = itemList?.studyFundFeeList ? itemList.studyFundFeeList : [];
        }
      })
    },
    // 课时-人次统计
    async annualFSS() {
      const { code, data } = await this.$api.FundingStatistics.annualFundingStatisticsStudy(
        {}
      );
      // console.log(`styfeeCode = ${code},\n result = `, data);
      if (code !== 200) return false;
      const { years, itemList } = data;
      this.numYears = years;
      this.numberMaps.forEach((obj, i) => {
        if (i === 0) {
          obj.data = itemList?.totalStudentNumList ? itemList.totalStudentNumList : [];
        } else if (i === 1) {
          obj.data = itemList?.totalLessonNumList ? itemList.totalLessonNumList : [];
        }
      })
    }
  },
  watch: {
    year: {
      deep: true,
      immediate: true,
      handler() {
        this.annualFundingSFP();
      }
    }
  }
}
</script>

<style scoped lang="stylus">
.divide-line
  border 1px solid #EEEEEE
  height 200px
  width 0px
</style>
