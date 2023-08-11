<template>
  <div ref="chart"></div>
</template>

<script>
import itemMix from '@/views/Home/panes/itemMix'
import * as echarts from 'echarts'

export default {
  name: 'DormitoryDataChart',
  mixins: [itemMix],
  props: {
    pageData: {
      type: Array,
      default: () => []
    }
  },
  mounted() {
    this.chart = echarts.init(this.$refs.chart, '', {
      locale: 'ZH'
    })
  },
  data() {
    return {
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
          top: 36,
          bottom: 32,
          left: '12%',
          right: '6%'
        },
        title: {
          text: '教室数据统计',
          left: 'center',
          textStyle: {
            color: '#1AFDFF',
            fontSize: this.$utils.getScaleSize(14)
          }
        },
        legend: {
          data: ['使用中', '空闲'],
          right: '6%',
          textStyle: {
            color: '#FFF',
            fontSize: this.$utils.getScaleSize(10)
          },
          itemWidth: 6,
          itemHeight: 6
        },
        xAxis: {
          data: this.pageData.map(item => item.dataName),
          axisLabel: {
            fontSize: this.$utils.getScaleSize(12)
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(194, 194, 194, 0.2)'
            }
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
            name: '使用中',
            data: this.pageData.map(item => item.using),
            type: 'bar',
            stack: 'one',
            barWidth: 24,
            color: new echarts.graphic.LinearGradient(
                0, 1, 0, 0,
                [
                  {offset: 1, color: '#1A6ACF'},
                  {offset: 0, color: '#03DFFC'}
                ]
            )
          },
          {
            name: '空闲',
            data: this.pageData.map(item => item.free),
            type: 'bar',
            stack: 'one',
            barWidth: 24,
            color: new echarts.graphic.LinearGradient(
                0, 1, 0, 0,
                [
                  {offset: 1, color: '#05A898'},
                  {offset: 0, color: '#5BF4F3'}
                ]
            )
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