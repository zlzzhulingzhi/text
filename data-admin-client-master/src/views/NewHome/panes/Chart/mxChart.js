import * as echarts from 'echarts'

export default {
  data() {
    return {
      chart: null
    }
  },
  async mounted() {
    await this.initChart(this.$refs.chart)
  },
  watch: {
    chartOptions:{
      deep:true,
      handler(){
        this.onUpdateChart()
      }
    }
  },
  methods: {
    // 初始化 - 表格
    async initChart(dom) {
      this.chart = echarts.init(dom)
      this.onUpdateChart()
      this.$nextTick(() => this.chart.resize())
      window.addEventListener('resize', () => this.chart.resize())
    },
    onUpdateChart() {
      if (!this.chart) return false
      this.chart.setOption(this.chartOptions)
    }
  }
}