<template>
  <el-dialog :visible.sync="visible" append-to-body title="报事报修详情" :close-on-click-modal="false" :before-close="close"
    width="824px">

    <div class="flex around-center width-100p font-16 margin-bottom-18">

      <div class="flex column infoCard">
        <div>机构名称：{{ repairsDetail.orgName }}</div>
        <div>联系人：{{ repairsDetail.contactPerson }}</div>
        <div>联系电话：{{ repairsDetail.contactNumber }}</div>
        <div>教室：{{ repairsDetail.building | dormitoryUnit }}
                  {{ repairsDetail.floor | dormitoryFloor }}
                  {{ repairsDetail.roomNo }}
                  ({{ repairsDetail.roomType | classTypeInfo }})</div>

      </div>

      <div class="flex column infoCard setAlignSelf">
        <div>报事报修类别：{{ repairsDetail.category | repairsCategory }}</div>
        <div>提交时间：{{ repairsDetail.createTime }}</div>
        <div>报事报修说明：{{ repairsDetail.description }}</div>
      </div>

    </div>

    <Banner :list="pictureInfo" errorInfo="报事报修图片" v-if="pictureInfo?.length"></Banner>


  </el-dialog>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import Banner from '@/components/common/Banner'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'


export default {
  name: 'DialogRepairsDetail',
  mixins: [mxBaseDialog],
  components: {
    Banner
  },
  data() {
    return {
      repairsDetail: {},
      // pictureInfo: []
    }
  },
  computed: {
    ...mapGetters({
      classTypeInfo: 'common/classTypeInfo',
      dormitoryUnit: 'common/dormitoryUnit',
      dormitoryFloor: 'common/dormitoryFloor',
      repairsCategory: 'common/repairsCategory'
    }),
    pictureInfo() {
      return this.repairsDetail.attachList.map(item => {
        return {filePath: item.fileUrl}
      })
    }
  },

  methods: {
    async initData(params) {
      // 获取报事报修详细信息
      await this.getRepairsDetail(params.id)
      
    },

    // 操作 - 获取报事报修详细信息
    async getRepairsDetail(params) {
      let {code, data} = await this.$api.Classroom.repairsDetail({
        id: params
      })
      if(code !== 200) return false
      this.repairsDetail = data
      // this.pictureInfo = this.repairsDetail.attachList.map(item => {
      //   return {filePath: item.fileUrl}
      // })
    }
  }
}
</script>

<style scoped lang="stylus">
.infoCard
  div
    margin-bottom 8px

.setAlignSelf
  align-self: flex-start
</style>