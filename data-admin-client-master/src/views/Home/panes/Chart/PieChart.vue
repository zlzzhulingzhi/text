<template>
  <div class="chart-box" ref="chart">
  </div>
</template>

<script>
import mxChart from '@/views/Home/panes/Chart/mxChart'
import * as echarts from 'echarts'

export default {
  name: 'PieChart',
  mixins: [mxChart],
  props: {
    name: {
      type: String
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
      let that = this
      return {
        title: {
          text: [`{value|${this.totalValue}}`, `{name|${this.name}}`].join("\n"),
          zLevel: 0,
          top: 'center',
          left: '24%',
          textAlign:'center',
          textStyle: {
            rich: {
              value: {
                color: "#fff",
                fontSize: 18,
                fontWeight: "600",
                lineHeight: 30,
              },
              name: {
                color: "#fff",
                fontSize: 16,
                fontWeight: "500",
              },
            },
          },
        },
        legend: {
          type: 'plain',
          icon: `path://M ${cx - r}, ${cy} a ${r},${r} 0 1,0 ${r * 2},0 a ${r},${r} 0 1,0 ${-r * 2},0 H-30`,
          padding: [10, 20],
          width:'50%',
          orient: 'vertical',
          left: '50%',
          top:'10px',
          // 图例图标大小
          itemHeight:40,
          itemWidth:40,
          textStyle: {
            rich: {
              name: {
                color: "#fff",
                fontSize: 18,
                fontWeight: "350",
                lineHeight: 30,
                width: 80
              },
              data: {
                color: "#1AFDFF",
                fontWeight: "500",
                fontSize: 18,
              },
            },
          },
          formatter: function (labelName) {
            const { seriesMaps } = that
            for (let index = 0; index < seriesMaps.length; index++) {
              const { name, data} = seriesMaps[index];
              if (name == labelName) {
                return `      {name|${labelName}}                 {data|${data}}`
              }
            }
          }
        },
      }
    },

    series() {
      return [
        {
          name: '',
          type: 'pie',
          radius: ['60%', '100%'],
          left: 'left',
          top:'10px',
          width: '50%',
          height:'90%',
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
          labelLine: {
            normal: {
              show: false    // 隐藏所有指示线
            }
          },
          label: {
            show: false
          },
          itemStyle: {
            // normal: {
            //   color: (list) => {
            //     // 注意 ！！！！！ 这里的数组一定要和实际的类目长度相等或大于，不然会缺少颜色报错
            //     let colorList = [
            //       {
            //         colorStart: '#0A5DFF',
            //         colorEnd: '#3A7DFF'
            //       },
            //       {
            //         colorStart: '#606EFD',
            //         colorEnd: '#8098FB'
            //       },
            //       {
            //         colorStart: '#6DD401',
            //         colorEnd: '#48D6A2'
            //       },
            //       {
            //         colorStart: '#037BB6',
            //         colorEnd: '#0EE1D6'
            //       },
            //       {
            //         colorStart: '#FBB072',
            //         colorEnd: '#FFD5B2'
            //       },
            //       {
            //         colorStart: '#A686D5',
            //         colorEnd: '#C1A5E5'
            //       },
            //       {
            //         colorStart: '#50CDA3',
            //         colorEnd: '#0EE1D6'
            //       }
            //     ]
            //     return new echarts.graphic.LinearGradient(1, 0, 0, 0, [{ //左、下、右、上
            //       offset: 0,
            //       color: colorList[list.dataIndex]['colorStart']
            //     }, {
            //       offset: 1,
            //       color: colorList[list.dataIndex]['colorEnd']
            //     }])
            //   }
            // }
          },
        }
      ]
    },

    chartOptions() {
      // 数据
      let series = this.series

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
            let paramsValue = params.value
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
        color: ['#09B7E9', '#FC6C6C', '#96EE82', '#FBB377', '#818AE6', '#90027E', '#E9D453'],
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
  height 50%
  width 100%
</style>