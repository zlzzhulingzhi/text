<template>
  <view class="px-2">
    <!-- <view class="flex pl-2 h-40 mt-2 bg-white items-center mx-2 rounded-md">
      <view class="flex flex-col w-10 items-center">
        <text class="text-lg text-gray-400"
          :class="isMorningSigned ? 'signed-text' : (isMorningUnSigned ? 'un-signed-text' : '')">8:30</text>
        <view class="h-9 my-1 bg-gray-400 gap-line"></view>
        <text class="text-lg text-gray-400"
          :class="isAfternoonSigned ? 'signed-text' : (isAfternoonUnSigned ? 'un-signed-text' : '')">17:30</text>
      </view>
      <view class="flex flex-col w-full pl-4 items-start">
        <view class="flex flex-col items-start">
          <text class="text-lg text-gray-400"
            :class="isMorningSigned ? 'signed-text' : (isMorningUnSigned ? 'un-signed-text' : '')">上课</text>
          <text class="text-sm text-gray-400" v-if="isMorningSigned">上课打卡（08:10）</text>
        </view>
        <view class="w-full gap-line-x bg-gray-300 my-2"></view>
        <view class="flex flex-col items-start">
          <text class="text-lg text-gray-400"
            :class="isAfternoonSigned ? 'signed-text' : (isAfternoonUnSigned ? 'un-signed-text' : '')">下课</text>
          <text class="text-sm text-gray-400" v-if="isAfternoonSigned">下课打卡（17:50）</text>
        </view>
      </view>
    </view> -->
    <view v-if="isShowStudyRecord" class="flex flex-col px-2 mt-2 pt-2 bg-white items-start rounded-md">
      <text class="text-lg font-medium">学习记录</text>
      <view class="w-full gap-line-x bg-gray-300 my-2"></view>
      <view v-for="item, index in recordObj.studyRecordList" class="flex h-12 justify-between items-center w-full px-2 box-border"
        :key="index">
        <text>{{ item.clazzName }}</text>
        <text>{{ item.createTime.split(' ')[1] }}</text>
      </view>
    </view>
    <view v-if="isShowRestRecord" class="flex flex-col px-2 mt-2 pt-2 bg-white items-start rounded-md">
      <text class="text-lg font-medium">休息记录</text>
      <view class="w-full gap-line-x bg-gray-300 my-2"></view>
      <view v-for="item, index in recordObj.restRecordList" class="flex h-12 justify-between items-center w-full px-2 box-border" :key="index">
        <text>{{ item.clazzName }}</text>
        <text>{{ item.createTime.split(' ')[1] }}</text>
      </view>
    </view>
    <view v-if="!isShowStudyRecord && !isShowRestRecord" class="flex px-2 h-14 mt-2 bg-white items-center rounded-md">
      <text class="text-lg">暂无记录</text>
    </view>
  </view>

</template>

<script>
export default {
  name: 'SignRecordList',
  props: {
    recordObj: {
      type: Object,
      default: () => {
        return {
          studyRecordList: [],
          restRecordList: []
        }
      }
    }
  },
  data() {
    return {

    };
  },
  computed: {
    // isMorningSigned() {
    //   return this.recordObj.morning && this.recordObj.morning.signStatus === 1
    // },
    // isAfternoonSigned() {
    //   return this.recordObj.afternoon && this.recordObj.afternoon.signStatus === 1
    // },
    // isMorningUnSigned() {
    //   return this.recordObj.morning && this.recordObj.morning.signStatus === 2
    // },
    // isAfternoonUnSigned() {
    //   return this.recordObj.afternoon && this.recordObj.afternoon.signStatus === 2
    // }
    isShowStudyRecord() {
      return this.recordObj.studyRecordList && this.recordObj.studyRecordList.length > 0
    },
    isShowRestRecord() {
      return this.recordObj.restRecordList && this.recordObj.restRecordList.length > 0
    }
  },
  methods: {

  },
};
</script>

<style lang="scss" scoped>
.gap-line {
  width: 2px;
}

.gap-line-x {
  height: 0.1px;
}

.signed-text {
  color: black
}

.un-signed-text {
  color: red
}
</style>