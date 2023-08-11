<template>
  <el-dialog :visible.sync="visible" append-to-body title="教室详情" :close-on-click-modal="false" :before-close="close"
    width="824px">

    <div class="font-16 margin-bottom-18 margin-right-16 margin-left-16">

      <el-row class="row-item">
        <el-col :span="8" class="item-col">
          <span class="col-label">单元:&nbsp;{{ classroomInfo.building | dormitoryUnit  }}</span>
        </el-col>
        <el-col :span="8" class="item-col">
          <span class="col-label">楼层:&nbsp;{{ classroomInfo.floor | dormitoryFloor }}</span>
        </el-col>
        <el-col :span="8" class="item-col">
          <span class="col-label">编号:&nbsp;{{ classroomInfo.roomNo}}</span>
        </el-col>
      </el-row>

      <el-row class="row-item">
        <el-col :span="8" class="item-col">
          <span class="col-label">面积:&nbsp;{{ classroomInfo.area }}&nbsp;(m<sup>2</sup>)</span>
        </el-col>
        <el-col :span="8" class="item-col">
          <span class="col-label">座位数:&nbsp;{{ classroomInfo.seats }}</span>
        </el-col>
        <el-col :span="8" class="item-col">
          <span class="col-label">类别:&nbsp;{{ classroomInfo.roomType | classTypeInfo }}</span>
        </el-col>
      </el-row>

      <el-row class="row-item">
        <el-col :span="24" class="item-col">
          <span class="col-label">设备:&nbsp;</span>
          <span v-for="(item, index) in sceneDeviceInfo" :key="index" class="margin-right-8">{{item.deviceName}}</span>
          <span v-if="!sceneDeviceInfo.length">暂无设备</span>
        </el-col>
      </el-row>


      <div class="height-20"></div>
      

      <el-row class="row-item">
        <el-col :span="12" class="item-col">
          <span class="col-label">场地租赁:&nbsp;{{ classroomFeeInfo.siteFee || '--'	 }} 元 / 天</span>
          <span class="margin-left-12">(单价:&nbsp;{{classroomFeeInfo.siteUnitPrice}} 元 / m<sup>2</sup> * 天)</span>
        </el-col>
        <el-col :span="12" class="item-col">
          <span class="col-label">水电费用:&nbsp;{{ classroomFeeInfo.waterPowerFee || '--' }} 元 / 天</span>
          <span class="margin-left-12">(单价:&nbsp;{{classroomFeeInfo.waterPowerUnitPrice}} 元 / m<sup>2</sup> * 天)</span>
        </el-col>
      </el-row>

      <el-row class="row-item">
        <el-col :span="12" class="item-col">
          <span class="col-label">新风费用:&nbsp;{{ classroomFeeInfo.airExhaustFee || '--' }} 元 / 天</span>
          <span class="margin-left-12">(单价:&nbsp;{{classroomFeeInfo.airExhaustUnitPrice}} 元 / m<sup>2 </sup> * 天)</span>
        </el-col>
        <el-col :span="12" class="item-col">
          <span class="col-label">空调费用:&nbsp;{{ classroomFeeInfo.airConditionerFee || '--' }} 元 / 天</span>
          <span class="margin-left-12">(单价:&nbsp;{{classroomFeeInfo.airConditionerUnitPrice}} 元 / m<sup>2</sup> * 天)</span>
        </el-col>
      </el-row>

      <el-row class="row-item">
        <el-col :span="12" class="item-col">
          <span class="col-label">小计:&nbsp;{{ classroomFeeInfo.totalFee || '--' }} 元 / 天</span>
          <ToolTip>
            <div slot="content">
              租赁时间不满一天，租赁费用按照一天计取
            </div>
          </ToolTip>
        </el-col>
      </el-row>

    </div>

    <Banner :list="classroomPicture" errorInfo="教室图片" v-if="classroomPicture?.length"></Banner>


  </el-dialog>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import Banner from '@/components/common/Banner'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'


export default {
  name: 'DialogClassroomDetail',
  mixins: [mxBaseDialog],
  components: {
    Banner
  },
  data() {
    return {
      classroomId: null,
      classroomInfo: {},
      sceneDeviceInfo: [],
      classroomFeeInfo: {},
    }
  },
  computed: {
    ...mapGetters({
      classTypeInfo: 'common/classTypeInfo',
      dormitoryUnit: 'common/dormitoryUnit',
      dormitoryFloor: 'common/dormitoryFloor',
    }),
    classroomPicture() {
      return this.classroomInfo.fileUrl || []
    }
  },

  methods: {
    // 获取教室详情数据
    async getClassroomDetail() {
      let { code, data } = await this.$api.Classroom.classroomDetail({
        id: this.classroomId
      })
      if (code !== 200) return false
      data.fileUrl = data.fileUrl?.map(item => {
        return {
          filePath: item
        }
      })
      this.classroomInfo = data
    },
    // 获取设备数据
    async getSceneDevice() {
      let { code, data } = await this.$api.SceneDevice.sceneDeviceList({
        category: "Classroom"
      })
      if (code !== 200) return false
      this.sceneDeviceInfo = data.filter(item => {
        return this.classroomInfo?.sceneDeviceId?.some(i => i === item.id)
      })
    },


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
      this.classroomId = null,
      this.classroomInfo = {},
      this.sceneDeviceInfo = [],

      // 获取教室id
      this.classroomId = data.formData.id

      // 获取教室详情数据
      await this.getClassroomDetail()

      // 获取设备数据
      await this.getSceneDevice()




      // 打开弹窗  初始化资费数据
      this.classroomFeeInfo = {}

      // 获取教室详情数据
      await this.getClassroomFeeDetail()






    },
  }
}
</script>

<style scoped lang="stylus">
.row-item
  margin-bottom 10px
  height 22px
  line-height 22px
.item-col
  display flex
</style>