<template>
  <view class="bg-white">
    <canvas :canvas-id="canvasId" :id="canvasId" class="charts" @touchend="tap"/>
  </view>
</template>

<script>
import uCharts from '@qiun/ucharts';
var uChartsInstance = {};

export default {
  name: 'Histogram',
  data() {
    return {
      cWidth: 750,
      cHeight: 500
    };
  },
  props: {
    canvasId: {
      type: String,
      required: true
    },
    uChartsData: {
      type: Object,
      required: true
    },
    config: {
      type: Object,
    }
  },
  mounted() {
    //这里的 750 对应 css .charts 的 width
    this.cWidth = uni.upx2px(750);
    //这里的 500 对应 css .charts 的 height
    this.cHeight = uni.upx2px(500);

    this.drawCharts(this.canvasId, this.uChartsData);
  },
  methods: {
    drawCharts(id,data){
      const ctx = uni.createCanvasContext(id, this);
      uChartsInstance[id] = new uCharts({
        type: "column",
        context: ctx,
        width: this.cWidth,
        height: this.cHeight,
        categories: data.categories,
        series: data.series,
        dataLabel: false,
        animation: true,
        background: "#FFFFFF",
        color: ["#1890FF","#91CB74","#FAC858","#EE6666","#73C0DE","#3CA272","#FC8452","#9A60B4","#ea7ccc"],
        padding: [15,15,0,5],
        enableScroll: false,
        legend: {},
        xAxis: {
          disableGrid: true
        },
        yAxis: {
          data: [
            {
              min: 0
            }
          ]
        },
        extra: {
          column: {
            type: "stack",
            width: 30,
            activeBgColor: "#000000",
            activeBgOpacity: 0.08,
            labelPosition: "outside"
          }
        }
      });
    },
    tap(e){
      uChartsInstance[e.target.id].touchLegend(e);
      uChartsInstance[e.target.id].showToolTip(e);
    }
  }
};
</script>

<style scoped>
  .charts{
    width: 750rpx;
    height: 500rpx;
  }
</style>