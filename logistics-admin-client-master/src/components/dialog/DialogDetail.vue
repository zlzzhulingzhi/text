<template>
  <el-dialog :visible.sync="visible" append-to-body :title="title" :close-on-click-modal="false" :before-close="close" width="800px">
    <el-descriptions title="" v-loading="loading.basicInfo">


      <el-descriptions-item label="宿舍单元">{{ detailShowData.building | dormitoryUnit }}</el-descriptions-item>
      <el-descriptions-item label="房型名称">{{ detailShowData.roomTypeName }}</el-descriptions-item>
      <el-descriptions-item label="房型编号">{{ detailShowData.roomTypeCode }}</el-descriptions-item>
      <el-descriptions-item label="面积数值" v-if="detailShowData.area">{{ detailShowData.area }} m<sup>2</sup></el-descriptions-item>
      <el-descriptions-item label="状态">{{ detailShowData.enabled | enabled }}</el-descriptions-item>
      <el-descriptions-item label="房间总数">{{ detailShowData.roomNum }}</el-descriptions-item>
      <el-descriptions-item label="维修数量">{{ detailShowData.maintNum }}</el-descriptions-item>
      <el-descriptions-item label="排序">{{ detailShowData.sort }}</el-descriptions-item>
  </el-descriptions>
  </el-dialog>
</template>


<script>
export default {
  name: 'DialogDetail',
  components: {},
  mixins: [],
  props: {
  },
  data() {
    return {
      // 详情数据中用以展示的数据
      detailShowData: {},
      visible: false,
      loading: {
        basicInfo: false
      },
      title: null
    }
  },
  created() {
  },
  mounted() {},
  computed: {

  },
  watch: {},
  methods: {
    async open(data = {}) {
      this.detailShowData = {}

      this.title = data.title
      this.visible = true;
      this.loading.basicInfo = true
      let {code, data: d} = await data.detailUrl({
        ...data.params
      })
      this.loading.basicInfo = false
      if(code !== 200) return false
      this.detailShowData = d
    },

    close() {
      this.visible = false
    }
  }
}
</script>


<style lang="stylus" scoped>

</style>