<template>
  <el-dialog :visible.sync="visible" append-to-body title="教室详情" :close-on-click-modal="false" :before-close="close" width="824px" custom-class="classroomInfo">
    <div v-loading="loading.detail">
      <div class="font-16 margin-bottom-18 margin-right-16 margin-left-16">

        <el-row class="row-item">
        <el-col :span="8" class="item-col">
          <span class="col-label">单元:&nbsp;{{ classroomData.building | dormitoryUnit  }}</span>
        </el-col>
        <el-col :span="8" class="item-col">
          <span class="col-label">楼层:&nbsp;{{ classroomData.floor | dormitoryFloor }}</span>
        </el-col>
        <el-col :span="8" class="item-col">
          <span class="col-label">编号:&nbsp;{{ classroomData.roomNo}}</span>
        </el-col>
      </el-row>

      <el-row class="row-item">
        <el-col :span="8" class="item-col">
          <span class="col-label">面积:&nbsp;{{ classroomData.area }}&nbsp;(m<sup>2</sup>)</span>
        </el-col>
        <el-col :span="8" class="item-col">
          <span class="col-label">座位数:&nbsp;{{ classroomData.seats }}</span>
        </el-col>
        <el-col :span="8" class="item-col">
          <span class="col-label">类别:&nbsp;{{ classroomData.roomType | classTypeInfo }}</span>
        </el-col>
      </el-row>

      <el-row class="row-item">
        <el-col :span="24" class="item-col">
          <span class="col-label">设备:&nbsp;</span><span v-for="(item, index) in sceneDeviceInfo" :key="index" class="margin-right-8">{{item.deviceName}}</span>
          <span v-if="!sceneDeviceInfo.length">暂无设备</span>
        </el-col>
      </el-row>

      </div>
      
      <!-- 教室图片轮播图 -->
      <Banner :list="classroomPicture" errorInfo="教室图片"></Banner>

    </div>
  </el-dialog>
</template>
    
<script>
import { mapGetters, mapState } from 'vuex'
import Banner from '@/components/common/Banner'
export default {
  name: 'DialogClassroomDetail',
  data () {
    components:{
      Banner
    }
    return {
      visible: false, // 弹窗开关
      loading: {
        detail: false
      },
      classroomInfo: {},
      classroomData: {},
      sceneDeviceInfo: [], // 设备信息
    }
  },
  computed: {
    ...mapState({
      classTypeInfo: 'common/classTypeInfo',
    }),
    classroomPicture() {
      return this.classroomData.fileUrl
    }
  },

  methods: {
    // 打开详情弹窗
    async open (params) {
      this.classroomInfo = params.data
      this.visible = true
      const _params = {
        id: params.data.id
      }
      this.loading.detail = true
      let { code, data } = await this.$api.Classroom.manageDetail(_params)
      this.loading.detail = false
      if (code !== 200) return false
      data.fileUrl = data.fileUrl.map(item => {
        return { filePath: item }
      })
      this.classroomData = data
      this.getSceneDevice()
    },
    // 关闭弹窗
    close () {
      this.visible = false
    },
    // 获取设备数据
    async getSceneDevice() {
      let {code, data} = await this.$api.SceneDevice.sceneDeviceList({
        category: "Classroom"
      })
      if(code !== 200) return false
      this.sceneDeviceInfo = data.filter(item => {
        return this.classroomData.sceneDeviceId.some(i => i === item.id)
      })
    },
  }
}
</script>
    
<style scoped lang="stylus">
/deep/.el-dialog .el-dialog__body
  padding 24px 32px!important
.title
  margin-bottom 10px
  font-weight 700
  font-size 16px
.row-item
  margin-bottom 10px
  height 22px
  line-height 22px
.item-col
  display flex
</style>