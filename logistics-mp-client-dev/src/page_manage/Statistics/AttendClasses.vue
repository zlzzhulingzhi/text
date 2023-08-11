<template>
  <view>
    <!-- 当日培训班 -->
    <view class="flex justify-between items-center mt-5">
      <text class="ml-1">培训班查询</text>

      <!-- 此处修改了组件  uni-datatime-picker(加了 108的监听、384-386、465)  calendar(加了第517行)   -->
      <view class="w-64 mr-1 dataRange">
        <uni-datetime-picker
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :clear-icon="true"
        v-model="range"
        type="daterange"
        @change="onChange"
        @touchDataPicker="touchDataPicker"
        @maskClick="maskClick"
        class="pickDataRange"
        id="uniDataTimePicker"/>
      </view>
      
    </view>

    <!-- 班级学员汇总 -->
    <view class="mt-2 text-center text-sm flex justify-around items-center" v-if="!loading.attendClasses">
      <view>当日培训班数量: {{ allClassNum }}</view>
      <view>当日班级学员数量: {{ allStuNum }}</view>
    </view>

    <!-- 班级card -->
    <view class="mt-2 text-sm" v-if="todayList.length">
      <view v-for="(item, index) in todayList" :key="index" class="bg-gray-100 mx-2 mt-2 rounded h-24 flex flex-col justify-center item-center pl-4">
        <view class="flex">
          <text class="mr-1">班级</text>
          <u-line direction="col" length="30rpx" color="#666" style="marginTop: 6rpx; borderLeft: 6rpx solid rgb(102, 102, 102)"></u-line>
          <text class="ml-1">{{ item.clazzName }}</text>
        </view>
        <text class="mt-1">所属机构：{{ item.orgName }}</text>
        <view class="mt-1">
          <text class="mr-4">教室：{{ item.classroom || '-' }}</text>
          <text>学员数量：{{ item.studentNum }}</text>
        </view>
      </view>
    </view>

    <u-loading-icon v-else-if="loading.attendClasses"></u-loading-icon>
    <uni-empty v-else></uni-empty>

    <!-- 查看更多 -->
    <view class="flex justify-center item-center mt-1 mx-2 rounded-1 bg-gray-100 text-xs py-1" @click="goToAllClasses" v-if="todayList.length === 3">
      <uni-icons type="more-filled" size="20"></uni-icons>
      <text class="ml-1">查看更多</text>
    </view>

  </view>
</template>

<script>

export default {
  name: 'AttendClasses',
  data() {
    return {
      // 日期
      range: null,
      // 今日培训班级
      todayList: [],
      // 当日培训班数量
      allClassNum: null,
      // 当日班级学员数量
      allStuNum: null,

      loading: {
        attendClasses: false
      }
    };
  },
  computed: {
    params() {
      let startDate, endDate
      if(this.range) {
        [startDate, endDate] = this.range
      }
      return {
        startDate: startDate,
        endDate: endDate
      }
    }
  },
  watch: {
    params: {
      immediate: true,
      deep: true,
      handler() {
        this.getAttendClasses()
      }
    }
  },
  methods: {
    // 获取培训班数据
    async getAttendClasses() {
      this.loading.attendClasses = true
      let { code, data } = await this.$api.Static.attendClass(this.params);
      this.loading.attendClasses = false
      if (code !== 200) return false;
      this.allClassNum = data.totalClassNum
      this.allStuNum = data.totalStudentNum
      this.todayList = data.top
    },
    onChange() {
      // this.range = []
    },
    // 培训班查看更多
    goToAllClasses() {
      uni.$u.route(this.$utils.getRoutePath('AllTrainClasses'), {...this.params});
    },
    // 点击日期选择器
    touchDataPicker(params) {
      this.$emit('willHideCharts', params)
    },
    // 点击遮罩层
    maskClick() {
      this.$emit('willHideCharts', false)
    }
  },
};
</script>

<style lang="scss" scoped>
// 日期选择器样式
.pickDataRange::v-deep {
  .uni-date__x-input,
  .range-separator {
    height: 52rpx;
    line-height: 52rpx;
  }
}

</style>
