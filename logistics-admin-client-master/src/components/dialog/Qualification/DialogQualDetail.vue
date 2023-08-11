<template>
  <div>
    <el-dialog :title="dialogInfo.title" :visible.sync="visible" :close-on-click-modal="true" width="800px" append-to-body :before-close="close">
      <div v-loading="loading" v-if="dialogInfo.type === 'QualificationsApply'">
        <div class="section-item">
          <div class="title">基本信息</div>
          <el-row class="row-item">
            <el-col :span="8" class="item-col">
              <span class="col-label">机构名称：</span><span>{{ qualificationsInfo.orgName || '--' }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">机构性质：</span><span>{{ qualificationsInfo.orgCategory| orgCategory  }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">法人代表：</span><span>{{ qualificationsInfo.legalPerson || '--' }}</span>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="8" class="item-col">
              <span class="col-label">法人联系电话：</span><span>{{ qualificationsInfo.legalNumber || '--' }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">法人联系邮箱：</span><span>{{ qualificationsInfo.legalEmail || '--'}}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">联系人姓名：</span><span>{{ qualificationsInfo.contactPerson || '未填写' }}</span>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="8" class="item-col">
              <span class="col-label">联系人电话：</span><span>{{ qualificationsInfo.contactNumber || '--' }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">联系人邮箱：</span><span>{{ qualificationsInfo.contactEmail || '--' }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="24" class="item-col">
              <span class="col-label">机构简介：</span><span>{{ qualificationsInfo.orgIntro || '未填写'}}</span>
            </el-col>
          </el-row>
        </div>
        <div class="section-item">
          <div class="title">项目计划</div>
          <el-table class="margin-top-4" :data="quaProjectInfo">
            <el-table-column min-width="88" type="expand">
              <template slot-scope="{row}">
                <el-table class="margin-top-4" :data="row.courseList" style="width: 96%;margin: 0 auto;">
                  <el-table-column label="课程名称" prop="courseName" min-width="88">
                    <template slot-scope="{row}">
                      {{row.courseName || '--'}}
                    </template>
                  </el-table-column>
                  <el-table-column label="培训对象" prop="trainObject" min-width="88">
                    <template slot-scope="{row}">
                      {{row.trainObject || '--'}}
                    </template>
                  </el-table-column>
                  <el-table-column label="培训内容及目标" prop="trainContent" min-width="88">
                    <template slot-scope="{row}">
                      {{row.trainContent || '--'}}
                    </template>
                  </el-table-column>
                  <el-table-column label="主讲老师" prop="lecturerName" min-width="88">
                    <template slot-scope="{row}">
                      {{row.lecturerName || '--'}}
                    </template>
                  </el-table-column>
                </el-table>
              </template>
            </el-table-column>
            <el-table-column label="项目名称" prop="projectName" min-width="88">
              <template slot-scope="{row}">
                {{row.projectName || '--'}}
              </template>
            </el-table-column>
            <el-table-column label="总学时" prop="totalLesson" min-width="88"></el-table-column>
            <el-table-column label="培训规模" prop="scale" min-width="88"></el-table-column>
            <el-table-column label="起止日期" prop="startAndEndTime" min-width="88">
              <template slot-scope="{row}">
                <span>{{row.startDate}} - {{row.endDate}}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="section-item">
          <div class="title">资料附件</div>
          <el-table class="margin-top-4" :data="tableClassRoomData">
            <el-table-column label="资料名称" prop="roomType" min-width="88"></el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template slot-scope="{ row }">
                <el-button type="text" size="small" @click.stop="downLoad(row)">下载</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      detailData: {}, //详情信息
      tableClassRoomData: [], //开团成员列表
      tableDormitoryData: [], //参团成员列表
      loading: false,
      load: {
        tableO: false,
        tableF: false
      },
      visible: false,
      id: null, //申请信息ID
      qualificationsInfo: {}, //基础信息
      quaProjectInfo: [], //基础信息
      qualificationsAttachInfo: {}, //附件信息
      courseList: [], //附件信息
      filterData: {},
      dialogType: 'OrderDetail',
      typeMapping: {
        QualificationsApply: {
          title: `资质申请详情`,
          type: 'QualificationsApply',
          detailAPI: this.$api.Approve.qualificationdDetail,
          attachAPI: this.$api.Approve.qualificationlist
        }
      }
    }
  },
  computed: {
    ...mapGetters({
      orderStatus: 'common/orderStatus',
      orgCategory: 'common/orgCategory'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    }
  },
  methods: {
    async open(data = {}) {
      console.log(data)
      this.detailData = data
      this.dialogType = data.type
      this.id = data.id
      this.getTablelData()
      this.getAttachlData()
      this.visible = true
    },
    // 获取基础详情信息
    async getTablelData() {
      const _params = {
        id: this.id
      }
      this.loading = true
      let { code, data } = await this.dialogInfo.detailAPI(_params)
      this.loading = false
      if (code !== 200) return false
      this.qualificationsInfo = data
      this.quaProjectInfo = data.projectList
      // this.courseList = data.projectList.map(item => {
      //   return item.courseList
      // })
    },
    // 获取附件信息列表
    async getAttachlData() {
      const _params = {
        id: this.id
      }
      this.loading = true
      let { code, data } = await this.dialogInfo.attachAPI(_params)
      this.loading = false
      if (code !== 200) return false
      this.quaAttachInfo = data
    },
    // 操作 - 下载
    downLoad(row) {
      console.log(row)
    },
    close() {
      this.visible = false
    }
  }
}
</script>

<style lang="stylus" scoped>
.spell-detail-wrap, .section-item
  +.section-item
    margin-top 30px
.title
  margin-bottom 10px
  font-weight 700
  font-size 16px
.row-item
  margin-bottom 10px
.item-col
  display flex
.col-label
  flex-shrink 0
>>>.el-table
  min-height auto
</style>
