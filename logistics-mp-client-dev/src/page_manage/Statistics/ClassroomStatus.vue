<template>
  <view>
    
    <view class="mt-5 mb-1 ml-2">当日教室占用数量/空闲数量</view>

    <view class="flex flex-row flex-wrap pb-1" v-if="!loading.ProgressBarData">
      <view
        v-for="(item, index) in HandelClassRoomData.chartItems"
        :key="index"
        class="flex justify-center item-center rounded mx-1">
        <ProgressBar :canvasId="item.canvasId" :uChartsData="item.series" class="ProgressBarCss ml-1" :config="{width: 124, height: 124, title: item.textIntro.roomNum}"></ProgressBar>
        <!-- 文字介绍 -->
        <view class="ProgressBarCssText text-xs mt-1">
          <view class="flex flex-col ml-2">
            <text>{{ item.textIntro.roomType }}</text>
            <text>占用{{ item.textIntro.usedNum }}</text>
            <text>空闲{{ item.textIntro.roomNum - item.textIntro.usedNum }}</text>
          </view>
        </view>
      </view>
    </view>

    <u-loading-icon v-else></u-loading-icon>

  </view>
</template>

<script>
import ProgressBar from '@/components/UCharts/ProgressBar.vue';

export default {
  name: 'ClassroomStatus',
  components: {
    ProgressBar,
  },
  data() {
    return {
      // 教室信息
      classRoomData: [],

      loading: {
        ProgressBarData: false
      },
    };
  },
  created() {
    this.getProgressBarData()
  },
  computed: {
    HandelClassRoomData() {
      let colorArray = ['#1890FF', '#91CB74', '#FAC858', '#EE6666', '#73C0DE', '#3CA272'];
      let HandelClassRoomData = {
        chartItems: [],
      };
      HandelClassRoomData.chartItems = this.classRoomData.map((item, index) => {
        return {
          textIntro: item,
          canvasId: 'cancansId' + index,
          series: [
            {
              name: item.roomType,
              color: colorArray[index],
              data: item.usedNum / item.roomNum,
            },
          ],
        };
      });
      return HandelClassRoomData;
    },
  },
  watch: {},
  methods: {
    async getProgressBarData() {
      this.loading.ProgressBarData = true
      let { code, data } = await this.$api.Static.classrooms();
      this.loading.ProgressBarData = false
      if (code !== 200) return false;
      this.classRoomData = data
    }
  },
};
</script>

<style lang="scss" scoped>
.ProgressBarCssText {
  width: 224rpx;
}
</style>
