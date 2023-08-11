<template>
  <view>
    <view class="flex justify-between p-4 head">
      <text @click="prevDate">前一天</text>
      <view>{{ $u.timeFormat(form.useDate, 'yyyy年mm月dd日') }}</view>
      <text @click="nextDate">后一天</text>
    </view>
    <view class="flex justify-around py-4">
      <u-icon name="arrow-down" :label="form.building | buildingList" labelPos="left"
        @click="show.building = true"></u-icon>
      <u-icon name="arrow-down" :label="form.floor | floorList" labelPos="left" @click="show.building = true"></u-icon>
      <u-icon name="arrow-down" :label="form.roomType | classroomList" labelPos="left"
        @click="show.roomType = true"></u-icon>
    </view>
    <view class="p-2 bg-gray-100"></view>
    <view>
      <view class="flex justify-between items-center p-4">
        <view class="font-semibold">教室</view>
        <view class="font-semibold">预订情况</view>
      </view>
      <template v-if="listPage.length">
        <view v-for="(item, index) in listPage" :key="item.classroomId">
          <view class="flex justify-between items-center p-4">
            <view class="text-lg">{{ item.building | buildingList }}{{ item.roomNo }}（{{ form.roomType | classroomList
            }}）
            </view>
            <view class="text-lg">
              <u-icon name="checkmark" label="已预订" labelColor="#F9A351" color="#F9A351"
                v-if="item.scheduleNow"></u-icon>
              <u-icon name="home" label="空闲" labelColor="#23B36D" color="#23B36D" v-else></u-icon>
            </view>
          </view>
          <u-cell-group :key="index">
            <template v-if="item.scheduleAfter">
              <u-cell title="预订：有" isLink :arrow-direction="item.expand ? 'up' : 'down'"
                @click="collapseChange(item)"></u-cell>
              <u-transition :show="item.expand">
                <view class="mx-3 my-4">
                  <view class="flex p-2" v-for="(element, idx) in item.list" :key="idx">
                    <text class="mr-8 text-gray-500 text-sm">{{ element.useDate }}（{{ element.dayOfWeek | weekList
                    }}）</text>
                    <u-icon name="checkmark" label="已预订" labelColor="rgb(251,191,36)" color="rgb(251,191,36)" size="14"
                      labelSize="14"></u-icon>
                  </view>
                </view>
              </u-transition>
            </template>
            <u-cell title="预订：无" v-else></u-cell>
          </u-cell-group>
          <u-gap height="10" bgColor="rgb(243,244,246)"></u-gap>
        </view>
      </template>
      <uni-empty v-else></uni-empty>
    </view>
    <u-picker :show="show.building" :columns="[buildingListComputed, floorListComputed]" keyName="name"
      @cancel="cancelBuilding" @confirm="confirmBuilding"></u-picker>
    <u-picker :show="show.roomType" :columns="[classroomList]" keyName="name" @cancel="cancelType"
      @confirm="confirmType"></u-picker>
  </view>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Classroom',
  data() {
    return {
      form: {
        useDate: '',
        roomType: '',
        building: '',
        floor: '',
      },
      params: {
        current: 1,
        size: 10,
        useDate: '',
        roomType: '',
        building: '',
        floor: '',
      },
      listPage: [],
      show: {
        building: false,
        roomType: false
      },
      total: 0
    }
  },
  computed: {
    ...mapGetters({
      buildingList: 'common/buildingList',
      floorList: 'common/floorList',
      classroomList: 'common/classroomList',
      dormitoryList: 'common/dormitoryList'
    }),
    buildingListComputed() {
      return [{ id: '', name: '全部' }, ...this.buildingList];
    },
    floorListComputed() {
      return [{ id: '', name: '全部' }, ...this.floorList];
    },

  },
  onLoad(options) {
    let params = this.$utils.getOptions(options)
    this.form = params
    this.getDetail()
  },
  onReachBottom() {
    if (this.listPage.length >= this.total) return false;
    this.params.current = ++this.params.current;
    this.getDetail();
  },
  methods: {
    async getDetail() {
      uni.showLoading();
      Object.assign(this.params, this.form);
      this.params.useDate = this.$u.timeFormat(this.form.useDate, 'yyyy-mm-dd');
      // console.log('this.parmas == ', this.params);
      let { code, data } = await this.$api.Logistics.classroomPage(this.params)
      uni.hideLoading()
      if (code !== 200) return false
      this.total = data.total;
      // console.log('total === ', data.total);
      if (data && data.records) {
        let listData = data.records.map(item => {
          return {
            ...item,
            expand: false,
            list: []
          }
        });
        this.listPage = this.listPage.concat(listData);
        // console.log('this.listPage  === ', this.listPage)
      }

    },
    async collapseChange(item) {
      item.expand = !item.expand
      if (item.list && item.list.length) return false
      let dateStart = Date.now() + 24 * 60 * 60 * 1000
      let dateEnd = Date.now() + 24 * 60 * 60 * 1000 * 30
      let { code, data } = await this.$api.Logistics.classroomSchedule({
        classroomId: item.classroomId,
        dateStart: this.$u.timeFormat(dateStart, 'yyyy-mm-dd'),
        dateEnd: this.$u.timeFormat(dateEnd, 'yyyy-mm-dd')
      })
      if (code !== 200) return false
      item.list = data
    },
    cancelBuilding() {
      this.show.building = false
    },
    confirmBuilding({ value }) {
      this.form.building = value[0].id
      this.form.floor = value[1].id
      this.cancelBuilding()
      this.clearData()
      this.getDetail()
    },
    cancelType() {
      this.show.roomType = false
    },
    confirmType({ value }) {
      this.form.roomType = value[0].id
      this.cancelType()
      this.clearData()
      this.getDetail()
    },
    prevDate() {
      this.form.useDate = parseInt(this.form.useDate) - 24 * 60 * 60 * 1000
      this.clearData();
      this.getDetail()
    },
    nextDate() {
      this.form.useDate = parseInt(this.form.useDate) + 24 * 60 * 60 * 1000
      this.clearData();
      this.getDetail()
    },
    clearData() {
      this.params.current = 1;
      this.listPage = [];
      this.total = 0;
    }
  }
}
</script>

<style lang="scss" scoped>
.head {
  color: $uni-text-color-inverse;
  background-color: $uni-color-primary;
}
</style>