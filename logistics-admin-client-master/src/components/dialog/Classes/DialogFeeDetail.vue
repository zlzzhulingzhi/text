<template>
  <el-dialog :visible.sync="visible" append-to-body title="教室资费详情" :close-on-click-modal="false" :before-close="close"
    width="824px">

    <template slot="title">
      <span class="text-3 text-bold font-16 margin-right-8">教室资费详情</span>
      <span class="text-3 font-14 margin-right-8">
        {{ classroomInfo.building}}{{ classroomInfo.floor}}{{ classroomInfo.roomNo }}({{ classroomInfo.roomType}})
      </span>
    </template>

    <div class="flex around-center width-100p font-16 margin-bottom-18">

      <div class="flex column infoCard">
        <div>空调费用：{{ classroomFeeInfo.airConditionerFee || '--' }}</div>
        <div>新风费用：{{ classroomFeeInfo.airExhaustFee || '--' }}</div>
        <div>每日总计费用：{{ classroomFeeInfo.totalFee || '--' }}</div>
      </div>

      <div class="flex column infoCard setAlignSelf">
        <div>场地租赁费用：{{ classroomFeeInfo.siteFee || '--'	 }}</div>
        <div>水电费：{{ classroomFeeInfo.waterPowerFee || '--' }}</div>
      </div>

<!-- 

      <el-row class="row-item">
        <el-col :span="12" class="item-col">
          <span class="col-label">场地租赁:&nbsp;{{ classroomFeeInfo.siteFee || '--'	 }}</span>
        </el-col>
        <el-col :span="12" class="item-col">
          <span class="col-label">水电费:&nbsp;{{ classroomFeeInfo.waterPowerFee || '--' }}</span>
        </el-col>
      </el-row>

      <el-row class="row-item">
        <el-col :span="12" class="item-col">
          <span class="col-label">新风费用:&nbsp;{{ classroomFeeInfo.airExhaustFee || '--' }}</span>
        </el-col>
        <el-col :span="12" class="item-col">
          <span class="col-label">空调费用:&nbsp;{{ classroomFeeInfo.airConditionerFee || '--' }}</span>
        </el-col>
      </el-row>


 -->

    </div>



  </el-dialog>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'


export default {
  name: 'DialogFeeDetail',
  mixins: [mxBaseDialog],
  components: {
  },
  data() {
    return {
      classroomInfo: {},
      classroomId: null,
      classroomFeeInfo: {},
    }
  },
  computed: {
    ...mapGetters({
      classTypeInfo: 'common/classTypeInfo',
      dormitoryUnit: 'common/dormitoryUnit',
      dormitoryFloor: 'common/dormitoryFloor',
    }),
  },

  methods: {
    // 获取教室详情数据
    async getClassroomFeeDetail() {
      let { code, data } = await this.$api.Classroom.feeDetail({
        id: this.classroomId
      })
      if (code !== 200) return false
      this.classroomFeeInfo = data
    },
    async initData(data) {
      // 打开弹窗  初始化数据
      this.classroomInfo = null
      this.classroomId = null,
      this.classroomFeeInfo = {}

      // 教室信息
      this.classroomInfo = data.formData
      // 获取教室id
      this.classroomId = data.formData.id

      // 获取教室详情数据
      await this.getClassroomFeeDetail()

    },
  }
}
</script>

<style scoped lang="stylus">
.infoCard
  div
    margin-bottom 8px

.setAlignSelf
  align-self: flex-start

// .el-dialog__wrapper
//   display: flex
//   justify-content: center
//   align-items: center
</style>