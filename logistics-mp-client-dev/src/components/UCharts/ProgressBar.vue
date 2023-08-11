<template>
  <view class="bg-white">
    <canvas :canvas-id="canvasId" :id="canvasId" class="charts" :style="chartStyle" @touchend="tap"/>
  </view>
</template>

<script>
import uCharts from '@qiun/ucharts';
var uChartsInstance = {};

export default {
  name: 'ProgressBar',
  data() {
    return {
      cWidth: null,
      cHeight: null
    };
  },
  props: {
    canvasId: {
      type: String,
      required: true
    },
    uChartsData: {
      type: Array,
      required: true
    },
    config: {
      type: Object,
    }
  },
  mounted() {
    //这里的 750 对应 css .charts 的 width
    this.cWidth = uni.upx2px(this.config.width);
    //这里的 500 对应 css .charts 的 height
    this.cHeight = uni.upx2px(this.config.height);

    this.drawCharts(this.canvasId, this.uChartsData);
  },
  computed: {
    chartStyle() {
      // return {
      //   width: this.config.width + 'rpx',
      //   height: this.config.height + 'rpx',
      // }
      return `width: ${this.config.width}rpx; height: ${this.config.height}rpx`
    }
  },
  methods: {
    drawCharts(id,data){
      let showNum = data[0].data*100
      if(showNum != 100) showNum = showNum.toFixed(1)
      else showNum = 100
      const ctx = uni.createCanvasContext(id, this);
      uChartsInstance[id] = new uCharts({
        type: "arcbar",
        context: ctx,
        width: this.cWidth,
        height: this.cHeight,
        series: data,
        animation: true,
        background: "#FFFFFF",
        color: ["#1890FF","#91CB74","#FAC858","#EE6666","#73C0DE","#3CA272","#FC8452","#9A60B4","#ea7ccc"],
        padding: undefined,
        title: {
          name: this.config.title,
          // name: showNum + '%',
          fontSize: 12,
          color: "#2fc25b"
        },
        // subtitle: {
        //   name: "正确率",
        //   fontSize: 25,
        //   color: "#666666"
        // },
        extra: {
          arcbar: {
            type: "circle",
            width: 6,
            backgroundColor: "#f3f4f6",
            startAngle: 1.5,
            endAngle: 0.25,
            gap: 2
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
  /* .charts{
    width: 125rpx;
    height: 125rpx;
  } */
</style>