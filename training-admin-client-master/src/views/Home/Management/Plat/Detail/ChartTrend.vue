<template>
  <div ref="chartTrend" :style="{width: 'calc(100vw - 180px - 32px - 32px)', height: '430px'}"></div>
</template>

<script>
import * as echarts from 'echarts/core'
import { LineChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, DatasetComponent, GridComponent } from 'echarts/components'
import { UniversalTransition } from 'echarts/features'
import { CanvasRenderer } from 'echarts/renderers'

let chartTrend = null

export default {
  props: {
    data: {
      type: Array,
      default: () => []
    }
  },
  watch: {
    data: {
      deep: true,
      handler(data) {
        if (data && data.length > 0) {
          // let dimensions = Object.keys(data[0])
          // let source = data
          // let series = []
          // for (let i = 0; i < dimensions.length - 1; i++) {
          //   series.push({ type: 'line', smooth: true })
          // }
          let category = []
          let data1 = []
          let data2 = []
          data.forEach(item => {
            category.push(item.statisticDate.split(' ')[0])
            data1.push(item.learningNum)
            // data2.push(Math.round(item.learningDuration / 3600))
            data2.push(item.learningDuration)
          })
          this.initLine(category, data1, data2)
        } else {
          this.initLine([], [], [])
        }
      }
    }
  },
  created() {
    echarts.use([LineChart, TitleComponent, TooltipComponent, LegendComponent, DatasetComponent, GridComponent, UniversalTransition, CanvasRenderer])
  },
  mounted() {
    chartTrend = echarts.init(this.$refs.chartTrend)
    window.onresize = () => {
      chartTrend.resize()
    }
  },
  methods: {
    initLine(category, data1, data2) {
      chartTrend.setOption({
        tooltip: {
          trigger: 'axis',
          formatter: (params) => {
            return `
              <div>${params[0].name}</div>
              <div class="flex between-center margin-top-4">
                <span><i class="dot bg-main" style="width: 10px; height: 10px;"></i>${params[0].seriesName}</span>
                <span class="text-bold margin-left-20">${params[0].data}</span>
              </div>
              <div class="flex between-center margin-top-4">
                <span><i class="dot bg-success" style="width: 10px; height: 10px;"></i>${params[1].seriesName}</span>
                <span class="text-bold margin-left-20">${this.$utils.formatDuration(params[1].data)}</span>
              </div>
            `
          }
        },
        legend: {
          data: ['学习人数', '学习时长']
        },
        xAxis: [
          {
            type: 'category',
            data: category
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '学习人数(人)',
            minInterval: 1
          },
          {
            type: 'value',
            name: '学习时长(小时)',
            axisLabel: {
              formatter: (value, index) => {
                return (value / 3600).toFixed(2)
              }
            }
          }
        ],
        series: [
          {
            name: '学习人数',
            type: 'line',
            smooth: true,
            data: data1
          },
          {
            name: '学习时长',
            type: 'line',
            smooth: true,
            yAxisIndex: 1,
            data: data2
          }
        ]
      })
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>