<template>
  <view>
    <!-- 柱状图 -->
    <view class="mt-5 mb-1 ml-2">当日宿舍占用数量/空闲数量</view>

    <view class="flex justify-center item-center mt-2 h-6">
    <view v-for="(item, index) in list" :key="index" class="mx-2 h-full pb-1" :class="{active : current === item.value}" @click="choseTab(item.value)">{{ item.name }}</view>
    </view>

    <!-- 直方图写法 -->
    <!-- <view v-if="!loading.dormitoryData">
      <view v-for="(item, index) in HandledDormitoryData" :key="index">
        <Histogram v-if="showChartCanvasId === item.canvasId" :canvasId="item.canvasId" :uChartsData="item" class="mt-2"></Histogram>
      </view>
    </view>

    <u-loading-icon v-else></u-loading-icon> -->
    
    <!-- 进度条写法 -->
    <view class="flex flex-col flex-wrap pb-1 pt-5" v-if="!loading.dormitoryData">
      <view v-for="(value, aItem, index) in handleDormitoryData" :key="index">
        <view class="flex flex-wrap" v-if="aItem === current">
          <view
            v-for="(item, index) in value"
            :key="index"
            class="flex justify-center item-center rounded mx-1">
            
            <ProgressBar :canvasId="item.canvasId" :uChartsData="item.series" class="ProgressBarCss ml-1" :config="{width: 124, height: 124, title: item.textIntro.roomNum}"></ProgressBar>
            <!-- 文字介绍 -->
            <view class="ProgressBarCssText text-xs mt-1">
              <view class="flex flex-col ml-2">
                <text>{{ item.textIntro.roomType }}</text>
                <text>占用{{ item.textIntro.usedNum }}</text>
                <text>空闲{{ item.textIntro.roomNum - item.textIntro.usedNum - item.textIntro.maintNum }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <u-loading-icon v-else></u-loading-icon>
    
  </view>
</template>

<script>
// import Histogram from '@/components/UCharts/Histogram.vue';
import ProgressBar from '@/components/UCharts/ProgressBar.vue';

export default {
  name: 'CurrentDormitory',
  components: {
    // Histogram,
    ProgressBar
  },
  data() {
    return {
      loading: {
        dormitoryData: false
      },
      list: [
        {name: 'A栋', value: 'A'},
        {name: 'B栋', value: 'B'},
      ],
      current: 'A',
      // 宿舍信息
      dormitoryData: {},
      // showChartCanvasId: 'canvasIdA',
    };
  },
  created() {
    this.getDormitoryData()
  },
  computed: {
    // HandledDormitoryData() {
    //   let HandleDormitoryData = []
    //   Object.keys(this.dormitoryData).forEach(item => {
    //     let HandleDormitoryDataItem = {
    //       canvasId: null,
    //       categories: [],
    //       series: [],
    //     };
    //     HandleDormitoryDataItem.canvasId = 'canvasId' + item;
    //     HandleDormitoryDataItem.categories = this.dormitoryData[item].xaxis;
    //     HandleDormitoryDataItem.series = this.dormitoryData[item].chartItems.map((item) => {
    //       return {
    //         name: item.name,
    //         textColor: '#333',
    //         data: item.series,
    //       };
    //     });
    //     HandleDormitoryData.push(HandleDormitoryDataItem)
    //   })
    //   return HandleDormitoryData;
    // },
    handleDormitoryData() {
      let colorArray = ['#1890FF', '#91CB74', '#FAC858', '#EE6666', '#73C0DE', '#3CA272'];
      let handleDormitoryData = {
        A: null,
        B: null
      }
      Object.keys(this.dormitoryData).forEach(AorB => {
        handleDormitoryData[AorB] = this.dormitoryData[AorB].map((item, index) => {
          let needItem = {
            canvasId: null,
            series: null,
            textIntro: null,
          }
          needItem.canvasId = 'canvasId' + item.building + index
          needItem.series = [{
            name: '占用率',
            color: colorArray[index],
            data: (item.usedNum || 0) / item.roomNum
          }]
          let roomTypeName = item.roomTypeName
          if(AorB === 'B') {
            let areaNum = item.roomType.substring(0, 2)
            roomTypeName = `${roomTypeName}(${areaNum}平方米)`
          }
          needItem.textIntro = {
            roomType: roomTypeName,
            usedNum: item.usedNum || 0,
            roomNum: item.roomNum,
            maintNum: item.maintNum
          }
          return needItem
        })
      })
      return handleDormitoryData
    }
  },
  watch: {},
  methods: {
    // async getDormitoryData() {
    //   this.loading.dormitoryData = true
    //   let { code, data } = await this.$api.Static.dormitories();
    //   this.loading.dormitoryData = false
    //   if (code !== 200) return false;
    //   this.dormitoryData = data
    // },
    async getDormitoryData() {
      this.loading.dormitoryData = true
      let { code, data } = await this.$api.Static.dormitoriesV2();
      this.loading.dormitoryData = false
      if (code !== 200) return false;
      this.dormitoryData = data
    },
    // 点击tabs选择
    choseTab(item) {
      // this.showChartCanvasId = 'canvasId' + item
      this.current = item
    }
  },
};
</script>

<style lang="scss" scoped>
.active {
  border-bottom: 2px solid #4c92ff;
}

.ProgressBarCssText {
  width: 224rpx;
}
</style>
