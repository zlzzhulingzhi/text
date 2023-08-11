<template>
  <PieChart :seriesMaps="pagePieData" name="宿舍总数"></PieChart>
</template>

<script>
import itemMix from '@/views/Home/panes/itemMix'
import PieChart from '@/views/Home/panes/Chart/PieChart'

export default {
  name: 'DormitoryDataPieChart',
  mixins: [itemMix],
  components: {
    PieChart
  },
  props: {
    pageData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {};
  },

  computed: {
    pagePieData() {
      let data = []
      this.pageData.map((item, index) => {
        return data.push({ id: index, name: item.dataName, data: item.using + item.free })
      })
      return data
    }
  },

  mounted() {
  },

  methods: {

  },
};
</script>

<style lang="scss" scoped>

</style>