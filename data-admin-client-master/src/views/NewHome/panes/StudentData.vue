<template>
  <ItemWrap :heightThirtyFull="true" :backgroud="require('@/assets/main/bg_side_bottom.png')">
    <div slot="title">学员数量统计</div>

    <div class="width-100p height-100p" ref="chart"></div>

  </ItemWrap>
</template>

<script>
import itemMix from '@/views/Home/panes/itemMix'
import * as echarts from 'echarts'

export default {
  name: 'StudentData',
  mixins: [itemMix],
  mounted() {
    this.chart = echarts.init(this.$refs.chart, '', {
      locale: 'ZH'
    })
  },
  data() {
    return {
      dataAPI: this.$api.Screen.statStuMonthly,
      pageData: [],
      pollingInterval: 0,
      chart: null
    }
  },
  computed: {
    options() {
      return {
        textStyle: {
          color: '#FFF'
        },
        grid: {
          top: 12,
          bottom: 32,
          left: '6%',
          right: '8%',
          containLabel: true
        },
        xAxis: {
          type: 'time',
          splitLine: {
            show: true,
            lineStyle: {
              color: 'rgba(194, 194, 194, 0.2)'
            }
          },
          axisLabel: {
            fontSize: this.$utils.getScaleSize(12),
            showMaxLabel: true
          }
        },
        yAxis: {
          type: 'value',
          splitLine: {
            lineStyle: {
              color: 'rgba(194, 194, 194, 0.2)'
            }
          },
          axisLabel: {
            fontSize: this.$utils.getScaleSize(12)
          }
        },
        series: [
          {
            data: this.pageData.map(item => {
              return [item.month, item.num]
            }),
            type: 'line',
            symbolSize: 10,
            itemStyle: {
              color: '#1AFDFF',
              shadowColor: '#1AFDFF',
              shadowBlur: 6
            },
            lineStyle: {
              color: '#0dfffa'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: '#0765E5' // 0% 处的颜色
                }, {
                  offset: 1, color: 'rgba(0,0,0,0)' // 100% 处的颜色
                }],
                global: false // 缺省为 false
              }
            }
          }
        ]
      }
    }
  },
  watch: {
    options: {
      deep: true,
      immediate: true,
      handler(options) {
        this.$nextTick(() => {
          this.chart.setOption(options)
        })
      }
    }
  }
}
</script>

<style scoped lang="stylus">

</style>