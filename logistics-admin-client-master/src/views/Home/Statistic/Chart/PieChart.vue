<template>
  <div class="chart-box" ref="chart">
  </div>
</template>

<script>
import mxChart from '@/views/Home/Statistic/Chart/mxChart'

export default {
  name: 'PieChart',
  mixins: [mxChart],
  props: {
    name: {
      type: String
    },
    year: {
      type: [Number, String]
    },
    seriesMaps: {
      type: Array,
      default() {
        return []
      }
    }
  },
  data() {
    return {
      totalValue: 0,
      isFirstLoad: true
    }
  },
  mounted() {
    const slectedDataChange = this.slectedDataChange;
    this.chart.on('legendselectchanged', function (params) {
      slectedDataChange(params);
    });
  },
  computed: {
    baseStyleOptions() {
      let cx = 0
      let cy = 0
      let r = 5

      return {
        title: {
          text: this.name
        },
        legend: {
          type: 'plain',
          icon: `path://M ${cx - r}, ${cy} a ${r},${r} 0 1,0 ${r * 2},0 a ${r},${r} 0 1,0 ${-r * 2},0 H-30`,
          padding: [10, 20],
          orient: 'horizontal',
          right: 0,
          itemGap: this.seriesMaps.length > 2 ? 8 : 10
        },
        grid: {
          left: '32px',
          right: '32px',
          top: '50px',
          bottom: '32px'
        }
      }
    },

    series() {
      return [
        {
          name: '',
          type: 'pie',
          radius: [30, 80],
          data: this.seriesMaps.map(item => {
            return {
              name: item.name,
              value: item.data
            }
          }),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
        }
      ]
    },

    chartOptions() {
      // 数据
      let series = this.series

      // x轴 数据
      let xAxisData = []
      let maxYear = new Date().getFullYear()
      for (let i = maxYear - 10; i <= maxYear; i++) {
        xAxisData.push(i)
      }

      let totalValue = 0;
      if (this.isFirstLoad) {
        this.seriesMaps.forEach(item => {
          totalValue += item.data;
        }
        )
        this.totalValue = totalValue;
      }
      totalValue = this.totalValue;
      const utils = this.$utils;
      return {
        ...this.baseStyleOptions,
        tooltip: {
          trigger: 'item',
          formatter(params) {
            let percent = ((params.value / totalValue) * 100).toFixed(3);
            let paramsValue = utils.formatMoney(params.value);
            if (percent < 0.001) {
              return ` 
              <span class="margin-right-4 text-3 font-size-14 text-bold">
                ${params.name}占比小于0.001%
              </span>
              <br/>
              <b class="dot margin-right-4" style="background-color: ${params.color};"></b>
            <span class="margin-right-4">${paramsValue}</span>`
            }
            return `<span class="margin-right-4 text-3 font-size-14 text-bold">
              ${params.name}占比：${percent}%
            </span>
            <br/>
            <b class="dot margin-right-4" style="background-color: ${params.color};"></b>
            <span class="margin-right-4">${paramsValue}</span>
          `
          }
        },
        color: ['#FEC33F', '#13C97F', '#FF4B5B', '#FE993A', '#1BCEC8', '#702BD9'],
        series
      }
    }
  },
  methods: {
    slectedDataChange(params) {
      let selectedObj = params.selected;
      let totalValue = 0;
      this.seriesMaps.forEach(obj => {
        const name = obj.name;
        if (selectedObj[name]) {
          totalValue += obj.data;
        }
      })
      this.totalValue = totalValue;
      this.isFirstLoad = false;
    }
  }
}
</script>

<style scoped lang="stylus">
.chart-box
  height 400px
  width 100%
</style>