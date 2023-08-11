<template>
  <view class="py-4 h-full record-sign-content">
    <my-calendar ref='calendar' :markDays='markDays' :open="true" :disabledAfter='true' @onDayClick='onDayClick'
      @onYearChange="onYearChange"></my-calendar>
    <SignRecordList :recordObj="curDateRecord"></SignRecordList>
    <u-tabbar :value="value" @change="name => value = name" :fixed="true" :placeholder="true"
      :safeAreaInsetBottom="true">
      <u-tabbar-item text="打卡" icon="photo" @click="recordClickAction"></u-tabbar-item>
      <u-tabbar-item text="记录" icon="play-right"></u-tabbar-item>
    </u-tabbar>
  </view>
</template>
 
<script>
import MyCalendar from './components/my-calendar.vue'
import SignRecordList from './components/sign-record-list.vue'
export default {
  name: 'SignRecord',
  components: {
    MyCalendar,
    SignRecordList
  },
  data() {
    return {
      value: 1,
      curDate: '',
      // clockIned 打卡 true/false clockInDate
      markDays: [],
      curDateRecord: {},
      curRecordParam: {
        searchYear: '',
        searchMonth: ''
      },
      curDayParam: {
        clockInDate: ''
      }
    }
  },
  methods: {
    recordClickAction() {
      uni.$u.route({
        type: 'redirect',
        url: this.$utils.getRoutePath('LocationSign')
      })
    },
    onDayClick(data) {
      this.curDate = data.date;
      this.getCurDateDetail()
    },
    onYearChange(data) {
      console.log('changeYear == ', data)
      this.curRecordParam.searchMonth = data.month
      this.curRecordParam.searchYear = data.year
      this.getCurMonthRecord()
    },
    async getCurMonthRecord() {
      let { code, data } = await this.$api.SignRecord.clockInMonthRecord(this.curRecordParam)
      if (code !== 200) {
        return false
      }
      this.markDays = data
    },
    async getCurDateDetail() {
      this.curDayParam.clockInDate = this.curDate
      let { code, data } = await this.$api.SignRecord.clockInDetail(this.curDayParam)
      // console.log('dayRecord === ', data)
      if(code !== 200) return false
      this.curDateRecord = data
    }
  },
  onLoad(option) {
   
  },
  onUnload() {
  },
  onReady() {
    let today = this.$refs.calendar.getToday().date;
    this.curDate = today;
    let curDateArr = this.curDate && this.curDate.split('-')
    if (curDateArr.length > 0) {
      this.curRecordParam.searchYear = curDateArr[0]
      this.curRecordParam.searchMonth = curDateArr[1]
      this.getCurMonthRecord()
      this.getCurDateDetail()
    }
  }
}
</script>
 
<style lang="scss" scoped>
.record-sign-content {
  background: rgb(243, 244, 246);
}
</style>