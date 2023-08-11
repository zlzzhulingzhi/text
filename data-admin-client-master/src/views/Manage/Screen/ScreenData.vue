<template>
  <div class="relative">
    <el-tabs v-model="currentTab" @tab-click="tabClick">
      <el-tab-pane :label="item.name" :name="item.code" v-for="item in listTab" :key="item.id">
        <div class="flex end-center bg-f padding-20 margin-top-12" v-if="item.templateUrl">
          <a class="margin-right-12" :href="item.templateUrl" :download="item.templateName">
            <el-button size="small" icon="el-icon-download">下载导入模板</el-button>
          </a>
          <el-upload action="" :show-file-list="false" :data="item" :accept="'.xls, .xlsx'" :before-upload="beforeUpload" :http-request="httpRequest">
            <el-button type="primary" size="small" icon="el-icon-upload2">上传导入模板</el-button>
          </el-upload>
        </div>
      </el-tab-pane>
    </el-tabs>
    <el-button class="btn" type="primary" size="small" @click="goToScreen">大屏展示</el-button>
    <!-- <el-button class="btn_intime" type="primary" size="small" @click="goToScreenIntime">实时数据</el-button> -->
    <div class="bg-f">
      <component :ref="currentTab" :is="currentTab"></component>
    </div>
  </div>
</template>

<script>
import AttendClass from './panes/AttendClass.vue'
import DataOverview from './panes/DataOverview.vue'
import Notice from './panes/Notice.vue'
import StatDataDynamicClass from './panes/StatDataDynamicClass.vue'
import StatDataDynamicDormitory from './panes/StatDataDynamicDormitory.vue'
import StudentMonthly from './panes/StudentMonthly.vue'
import Banner from './panes/Banner.vue'
import PlayerData from './panes/PlayerData.vue'
import { mapState } from 'vuex'

export default {
  name: 'ScreenData',
  components: {
    AttendClass,
    DataOverview,
    Notice,
    StatDataDynamicClass,
    StatDataDynamicDormitory,
    StudentMonthly,
    Banner,
    PlayerData
  },
  data() {
    return {
      loading: {
        table: false
      },
      currentTab: 'AttendClass',
      listTab: [
        {
          id: 1,
          name: '今日培训班级',
          code: 'AttendClass',
          templateUrl: `${location.pathname}数据导入-今日培训班级.xlsx`,
          templateName: '数据导入-今日培训班级',
          preDataKey: 'screenAttendClassDataParseResultList'
        },
        {
          id: 2,
          name: '数据总览',
          code: 'DataOverview',
          templateUrl: `${location.pathname}数据导入-数据总览.xlsx`,
          templateName: '数据导入-数据总览',
          preDataKey: 'screenDataOverviewDataParseResultList'
        },
        {
          id: 3,
          name: '重要通知',
          code: 'Notice',
          templateUrl: `${location.pathname}数据导入-重要通知.xlsx`,
          templateName: '数据导入-重要通知',
          preDataKey: 'screenNoticeDataParseResultList'
        },
        {
          id: 4,
          name: '教室统计',
          code: 'StatDataDynamicClass',
          templateUrl: `${location.pathname}数据导入-教室统计.xlsx`,
          templateName: '数据导入-教室统计',
          preDataKey: 'screenStatDataDynamicDataParseResultList'
        },
        {
          id: 5,
          name: '宿舍统计',
          code: 'StatDataDynamicDormitory',
          templateUrl: `${location.pathname}数据导入-宿舍统计.xlsx`,
          templateName: '数据导入-宿舍统计',
          preDataKey: 'screenStatDataDynamicDataParseResultList'
        },
        {
          id: 6,
          name: '学员数量统计',
          code: 'StudentMonthly',
          templateUrl: `${location.pathname}数据导入-学员数量统计.xlsx`,
          templateName: '数据导入-学员数量统计',
          preDataKey: 'screenStatStudentMonthlyDataParseResultList'
        },
        // {
        //   id: 7,
        //   name: '轮播图',
        //   code: 'Banner',
        //   templateUrl: '',
        //   templateName: '',
        //   preDataKey: ''
        // },
        {
          id: 8,
          name: '播放视频',
          code: 'PlayerData',
          templateUrl: '',
          templateName: '',
          preDataKey: ''
        }
      ],
      typeMapping: {
        AttendClass: {
          previewApi: this.$api.ScreenManage.previewAttendClass,
          importApi: this.$api.ScreenManage.importBatchAttendClass,
          params: {}
        },
        DataOverview: {
          previewApi: this.$api.ScreenManage.previewDataOverview,
          importApi: this.$api.ScreenManage.importBatchDataOverview,
          params: {}
        },
        Notice: {
          previewApi: this.$api.ScreenManage.previewNotice,
          importApi: this.$api.ScreenManage.importBatchNotice,
          params: {}
        },
        StatDataDynamicClass: {
          previewApi: this.$api.ScreenManage.previewStatDataDynamic,
          importApi: this.$api.ScreenManage.importBatchStatDataDynamic,
          params: { module: 'classroom' }
        },
        StatDataDynamicDormitory: {
          previewApi: this.$api.ScreenManage.previewStatDataDynamic,
          importApi: this.$api.ScreenManage.importBatchStatDataDynamic,
          params: { module: 'dormitory' }
        },
        StudentMonthly: {
          previewApi: this.$api.ScreenManage.previewStatStudentMonthly,
          importApi: this.$api.ScreenManage.importBatchStatStudentMonthly,
          params: {}
        },
        Banner: {}
      }
    }
  },
  computed: {
    ...mapState('config', {
      domain: 'domain'
    })
  },
  methods: {
    tabClick(tab) {
      this.currentTab = tab.name
    },
    beforeUpload(file) {
      let isExcel = ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'application/vnd.ms-excel'].includes(file.type)
      if (!isExcel) this.$message.warning('请选择.xls, .xlsx格式文件上传')
      return isExcel
    },
    async httpRequest({ file, data }) {
      this.loading.table = true

      let fileFormData = new FormData()
      fileFormData.append('file', file)
      // 导入预览
      let { code: c1, data: d1 } = await this.typeMapping[data.code].previewApi({
        fileFormData
      })
      this.loading.table = false
      if (c1 !== 200) return false
      // 导入插入
      let { code: c2 } = await this.typeMapping[data.code].importApi({
        [data.preDataKey]: d1,
        // ...data.params
        ...this.typeMapping[data.code].params
      })
      if(c2 !== 200) return false

      this.$message.success('上传成功')
      this.$refs[this.currentTab] && this.$refs[this.currentTab].getTableData()
    },
    goToScreen() {
      window.open(`${location.origin}${location.pathname}`)
    },
    goToScreenIntime() {
      window.open(`${location.origin}${'/#/InTime'}`)
    }
  }
}
</script>

<style lang="stylus" scoped>
.btn
  position absolute
  right 20px
  top 6px
.btn_intime
  position absolute
  right 120px
  top 6px
</style>