<template>
  <view class="w-full h-full relative">
   
    <view class="flex justify-start items-center mt-1">
      <text class="ml-1 mr-2">日期</text>

      <view class="w-64 mr-1 dataRange">
        <uni-datetime-picker
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :clear-icon="true"
        v-model="range"
        type="daterange"
        @change="onChange"
        class="pickDataRange" />
      </view>
      
    </view>

    <!-- 班级card -->
    <view class="mt-2 text-sm" v-if="todayList.length">
      <view v-for="(item, index) in todayList" :key="index" class="bg-gray-200 mx-2 mt-2 rounded h-24 flex flex-col justify-center item-center pl-4">
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
    
    <view v-if="!isThereMore && todayList.length" class="text-center py-3 text-xs">暂无更多数据</view>
  </view>
</template>

<script>
export default {
  name: 'AllTrainClasses',
  components: {
  },
  data() {
    return {
      range: [],
      // 今日培训班级
      todayList: [],
      // 当前页
      current: 1,
      // 总数
      total: 0,
      // 数据请求来源
      typeSource: null,

      loading: {
        attendClasses: false
      }
    };
  },
  onLoad(options) {
    this.range = [options.startDate, options.endDate]
    if(!this.range.length) this.getAttendClasses()
  },
  onReachBottom() {
    this.typeSource = 'formReachBottom'
    if(this.isThereMore) this.current++
  },
  computed: {
    params() {
      let startDate, endDate
      if(this.range) {
        [startDate, endDate] = this.range
      }
      return {
        startDate: startDate,
        endDate: endDate,
        size: 10,
        current: this.current
      }
    },
    isThereMore() {
      return Boolean(this.current*10 <= this.total)
    }
  },
  watch: {
    params: {
      deep: true,
      handler() {
        this.getAttendClasses(this.typeSource)
      }
    }
  },
  methods: {
    // 获取培训班数据
    async getAttendClasses(type) {
      this.loading.attendClasses = true
      let { code, data } = await this.$api.Static.attendClassPage(this.params);
      this.loading.attendClasses = false
      if (code !== 200) return false;
      if(type === 'fromData') this.todayList = data.records
      else this.todayList = this.todayList.concat(data.records)
      this.total = data.total
    },
    onChange(params) {
      this.typeSource = 'fromData'
      this.current = 1
      this.range = params
    },
  }
};
</script>

<style lang="scss" scoped>
// 日期选择器样式
.pickDataRange::v-deep {
  .uni-date__x-input,
  .range-separator {
    height: 26px;
    line-height: 26px;
  }
}

.dataRange::v-deep {
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