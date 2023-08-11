<template>
  <ItemWrap :heightThirtyFull="true" :backgroud="require('@/assets/main/students_num_bg.png')">
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
        // xAxis: {
        //   type: 'time',
        //   splitLine: {
        //     show: true,
        //     lineStyle: {
        //       color: 'rgba(194, 194, 194, 0.2)'
        //     }
        //   },
        //   axisLabel: {
        //     fontSize: this.$utils.getScaleSize(12),
        //     showMaxLabel: true,
        //     margin: 20
        //   },
        //   axisTick: {
        //     //x轴刻度相关设置
        //     alignWithLabel: true,
        //   },
        //   boundaryGap: true,
        // },
        xAxis: {
          type: 'category',
          axisTick: { show: true },
          axisLine: {
            show: true,
          },
          axisLabel: {
            textStyle: {
              fontSize: this.$utils.getScaleSize(12),
              showMaxLabel: true
            },
            formatter: function (value, index) {
              if (value) {
                return value.split('-')[1]
              }
              return ''
            },
            margin: 20,//刻度标签与轴线之间的距离
          },
          boundaryGap: true, //设置为false代表是零刻度开始，设置为true代表离零刻度间隔一段距离
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
        series: [{
          data: this.pageData.map(item => {
            return [item.month, item.num]
          }),
          name: 'hill',
          type: 'bar',
          //位置偏移,距离0刻度的位置
          barCategoryGap: '-45%',
          //图形宽度
          barWidth: 30,
          //图形形状
          // symbol: 'path://M150 50 L130 130 L170 130  Z',
          itemStyle: {
            // borderColor: "#0357F3",
            // borderWidth: 1,
            // 渐变色

              color: (list) => {
                // 注意 ！！！！！ 这里的数组一定要和实际的类目长度相等或大于，不然会缺少颜色报错
                let colorList = [
                  {
                    colorStart: '#0357F3',
                    colorEnd: '#011128'
                  },
                  {
                    colorStart: '#0357F3',
                    colorEnd: '#011128'
                  },
                  {
                    colorStart: '#0357F3',
                    colorEnd: '#011128'
                  },
                  {
                    colorStart: '#0357F3',
                    colorEnd: '#011128'
                  },
                  {
                    colorStart: '#0357F3',
                    colorEnd: '#011128'
                  },
                  {
                    colorStart: '#3FFFB6',
                    colorEnd: '#011128'
                  }
                ]
                return new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ //左、下、右、上
                  offset: 0,
                  color: colorList[list.dataIndex]['colorStart']
                }, {
                  offset: 1,
                  color: colorList[list.dataIndex]['colorEnd']
                }])
              }
            },
          z: 10
        }, {
          name: 'glyph',
          type: 'pictorialBar',
          barGap: '-100%',
          symbolPosition: 'end',
          symbolSize: 50,
          symbolOffset: [0, '-120%']
        }]
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