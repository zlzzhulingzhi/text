<template>
  <el-dialog :visible.sync="visible" append-to-body title="讲师详情" :close-on-click-modal="false" :before-close="close" width="800px">

    <el-descriptions class="margin-top" title="" :column="2">
      <el-descriptions-item label="讲师姓名">{{ lectureInfo.lecturerName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="讲师头衔">{{ lectureInfo.title || '-' }}</el-descriptions-item>
      <el-descriptions-item label="手机号">{{ lectureInfo.phone || '-' }}</el-descriptions-item>
      <el-descriptions-item label="性别">{{ lectureInfo.sex | sex }}</el-descriptions-item>
      <el-descriptions-item label="讲师头像">
        <el-image fit="cover" :src="lectureInfo.headImgUrl"></el-image>
      </el-descriptions-item>
      <el-descriptions-item labelClassName="usePlace"></el-descriptions-item>
      <el-descriptions-item label="讲师简介">
        <el-tooltip class="item" effect="dark" :content="lectureInfo.intro" placement="top-start">
          <div class="width-100p introInfo">{{ lectureInfo.intro }}</div>
        </el-tooltip>
      </el-descriptions-item>
    </el-descriptions>
    
  </el-dialog>
</template>

<script>
import {mapActions, mapGetters, mapState} from 'vuex'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'

export default {
  name: 'DialogOrgLecInfo',
  mixins: [mxBaseDialog],
  data() {
    
    return {
      lectureInfo: {}
    }
  },
  created() {

  },
  mounted() {

  },
  computed: {
    ...mapGetters({
      sex: 'common/sex',
      nationDict: 'common/nationDict'
    }),
  },
  watch: {

  },
  methods: {
    // 操作 - 初始化数据
    async initData(params) {
      this.lectureInfo = {}
      let {code, data} = await this.$api.orgLectures.detail({
        id: params.id
      })
      if(code !== 200) return false
      this.lectureInfo = data
    },
  }
}
</script>

<style scoped lang="stylus">
.introInfo
  text-overflow ellipsis
  overflow hidden
  word-break break-all

  display -webkit-box
  -webkit-line-clamp 3
  -webkit-box-orient vertical
>>>.usePlace
 display none
</style>