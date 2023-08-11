<template>
  <div class="chart-box" ref="chart"></div>
</template>

<script>
import mxChart from '@/views/Home/Statistic/Chart/mxChart'

export default {
  name: 'FeeChart',
  mixins: [mxChart],
  props: {
    type: {
      type: String,
      default: 'bar'
    },
    seriesMaps: {
      type: Array,
      default() {
        return []
      }
    },
    years: {
      type: Array,
      default() {
        return []
      }
    }
  },
  computed: {
    baseStyleOptions() {
      let cx = 0
      let cy = 0
      let r = 5

      return {
        title: {
          text: '费用统计表'
        },
        legend: {
          type: 'plain',
          icon: `path://M ${cx - r}, ${cy} a ${r},${r} 0 1,0 ${r * 2},0 a ${r},${r} 0 1,0 ${-r * 2},0 H-30`,
          padding: [10, 20],
          left: '34%',
          itemGap: this.seriesMaps.length > 2 ? 5 : 10,
          orient: 'horizontal',
        },
        grid: {
          left: '32px',
          right: '32px',
          top: '50px',
          bottom: '32px'
        }
      }
    },

    // 柱状图
    barSeries() {
      return this.seriesMaps.map(item => {
        return {
          type: 'bar',
          ...item
        }
      })
    },

    // 折线图
    lineSeries() {
      return this.seriesMaps.map(item => {
        return {
          type: 'line',
          name: item.name,
          data: item.data
        }
      })
    },

    chartOptions() {
      // 数据
      let series = []
      if (this.type === 'bar') {
        series = this.barSeries
      } else if (this.type === 'line') {
        series = this.lineSeries
      }

      // x轴 数据
      const xAxisData = this.years;
      const type = this.type;
      const utils = this.$utils;
      return {
        ...this.baseStyleOptions,
        tooltip: {
          trigger: 'axis',
          formatter(list) {
            return list.map(item => {
              const totalFee = utils.formatMoney(item.value);
              if (item.seriesId === 'a' || item.seriesName === '年总计') {
                const totalYearName = xAxisData[item.dataIndex] + '年总计: ';
                return `<span class="margin-right-4 text-3 font-size-14 text-bold">
                        ${totalYearName + totalFee}
                  </span>`
              }
              return `<b class="dot margin-right-4" style="background-color: ${item.color};"></b>
              <span class="margin-right-4">${item.seriesName}</span><span>${totalFee}</span>`
            }).join('<br/>')
          }
        },
        xAxis: {
          type: 'category',
          data: xAxisData
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            align:'right',
            margin:2,
            formatter(value, index) {
              // value大于1000时除以1000并拼接k，小于1000按原格式显示
              if (value >= 10000) {
                value = value / 10000 + "w";
              } else if (value >= 1000) {
                value = value / 1000 + "k";
              } else if (value < 1000) {
                value;
              }
              return value;
            },
          },
        },
        color: ['#1F50B8', '#FEC33F', '#13C97F', '#FF4B5B', '#FE993A', '#1BCEC8', '#702BD9'],
        series
      }
    }
  },
  methods: {}
}
</script>

<style scoped lang="stylus">
.chart-box
  height 400px
  width 100%
</style>