<template>
  <view class="w-full h-full relative">
    <!-- 顶部总数统计 -->
    <TopSum></TopSum>

    <!-- 当日培训班 -->
    <AttendClasses class="outsideContainer" @willHideCharts="willHideCharts"></AttendClasses>

    <!-- 当日宿舍 -->
    <CurrentDormitory v-show="!hideCharts"></CurrentDormitory>

    <!-- 教室数量 -->
    <ClassroomStatus v-show="!hideCharts"></ClassroomStatus>

  </view>
</template>

<script>
import TopSum from './TopSum.vue'
import AttendClasses from './AttendClasses.vue'
import CurrentDormitory from './CurrentDormitory.vue'
import ClassroomStatus from './ClassroomStatus.vue'
import Histogram from '@/components/UCharts/Histogram.vue';
import ProgressBar from '@/components/UCharts/ProgressBar.vue';

export default {
  name: 'Statistics',
  components: {
    TopSum,
    AttendClasses,
    CurrentDormitory,
    ClassroomStatus,
    Histogram,
    ProgressBar,
  },
  data() {
    return {
      hideCharts: false
    }
  },
  methods: {
    willHideCharts(params) {
      // if(params === false) this.hideCharts = params
      // else {
      //   setTimeout(() => {
      //     this.hideCharts = params
      //   }, 300)
      // }
      setTimeout(() => {
          this.hideCharts = params
        }, 300)
    }
  }
};
</script>

<style lang="scss" scoped>
.outsideContainer::v-deep {  
  .uni-date__x-input {
    height: 52rpx !important;
    line-height: 52rpx !important;
  }

  .uni-date-x {
    .range-separator {
      height: 52rpx !important;
      line-height: 52rpx !important;
    }
  }
}
</style>
