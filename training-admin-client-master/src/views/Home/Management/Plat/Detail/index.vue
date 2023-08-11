<template>
  <div>
    <!-- 面包屑 -->
    <el-breadcrumb class="margin-bottom-16" separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>课程专题管理</el-breadcrumb-item>
      <!-- <el-breadcrumb-item :to="{ name: 'CourseList' }">课程</el-breadcrumb-item> -->
      <el-breadcrumb-item>专题详情</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 头部内容 -->
    <el-card shadow="never" :body-style="{ padding: '16px' }">
      <div class="flex between-center">
        <div class="flex">
          <div class="relative">
            <img :style="{ width: '120px', height: '67.5px', borderRadius: '4px', objectFit: 'cover' }"
                 :src="courseInfo.coverUrl">
          </div>
          <div class="margin-left-16">
            <div class="flex">
              <!-- <div class="tag margin-right-8" :class="courseInfo.courseType">
              <span>{{ (courseTypeList.find(v => v.value === courseInfo.courseType) || {}).name }}</span>
              </div> -->
              <span class="text-bold font-16">{{ courseInfo.courseName }}</span>
              <span class="padding-left-4 padding-right-4 ico-shelfStatus text-f font-12 margin-left-8"
                    :style="{background: courseInfo.shelfStatus ? '#13C97F' : 'red'}">
                    {{ courseInfo.shelfStatus == 0 ? '已下架' : '已上架' }}
                </span>
            </div>
            <div class="margin-top-12 text-6 font-13 flex">
              <div class="margin-right-16 text-ellipsis">讲师：
                {{ (lecturers.slice(0, 10)).map(item => item.lecturerName).join(' ')}}
                <b v-if="lecturers.length > 10">...</b>
              </div>
              <div class="margin-right-16">专题类型：{{ categoryNames }}</div>
              <div>报名人数：{{ courseInfo.signUpNum || '0' }}</div>
              <!-- <span class="margin-right-16">课程价格：{{ courseInfo.coursePrice / 100 | number }}</span>
              <span>报名人数：{{ courseInfo.signUpNum || '0' }}</span>
              <span class="margin-left-16">课程评分：{{ courseInfo.score || '--' }}</span> -->
            </div>
          </div>
        </div>
        <!-- <div class="flex">
          <PopoverShare class="flex start-center margin-right-24" :courseInfo="courseInfo"
                        :courseInfo="courseInfo"></PopoverShare>
          <el-button type="text" @click="onShelve">{{ courseInfo.shelfStatus == 0 ? '上架' : '下架' }}课程</el-button>
          <el-button type="text" @click="onEdit">编辑</el-button>
        </div> -->
      </div>
    </el-card>
    <el-tabs class="margin-top-6" v-model="currentTab" @tab-click="tabClick">
      <!-- <el-tab-pane label="课程目录" name="tab1">
        <CompCourse :courseId="courseId" :courseInfo="courseInfo"></CompCourse>
      </el-tab-pane> -->
      <!-- <el-tab-pane label="学习数据" name="tab2" v-if="courseInfo.courseType === 'record'">
        <CompStat :courseInfo="courseInfo"></CompStat>
      </el-tab-pane> -->
      <!-- <el-tab-pane label="学习记录" name="tab3" v-if="isDev">
        <CompRecord></CompRecord>
      </el-tab-pane> -->
        <!-- <el-tab-pane label="课程评价" name="tab4">
          <CompComment :courseId="courseId"></CompComment>
        </el-tab-pane> -->
      <el-tab-pane label="学员列表" name="tab5">
        <CompStudent :courseId="courseId" :courseInfo="courseInfo" @onDelete="getStat"></CompStudent>
      </el-tab-pane>
      <!-- <el-tab-pane label="课程留言" name="tab6">
        <CompMessage :courseId="courseId"></CompMessage>
      </el-tab-pane> -->
    </el-tabs>
    <!-- <DialogCopyCourse ref="DialogCopyCourse" @success="getTableData"></DialogCopyCourse> -->
  </div>
</template>

<script>
// import CompCourse from './CompCourse.vue'
// import CompStat from './CompStat.vue'
// import CompRecord from './CompRecord.vue'
// import CompComment from './CompComment.vue'
import CompStudent from './CompStudent.vue'
// import CompMessage from './CompMessage.vue'
import {mapGetters, mapState} from 'vuex'

// import PopoverShare from '@/components/dialog/Course/popoverShare.vue'

// import DialogCopyCourse from '@/components/dialog/Course/DialogCopyCourse.vue'

export default {
  name: 'orgCourseSpeDe',
  components: {
    // CompCourse,
    // CompStat,
    // CompRecord,
    // CompComment,
    CompStudent,
    // CompMessage,
    // DialogCopyCourse,
    // PopoverShare
  },
  data() {
    return {
      courseId: null,
      orgId: null,
      courseInfo: {},
      currentTab: 'tab5'
    }
  },
  computed: {
    ...mapState('common', {
      courseTypeList: 'courseType'
    }),
    ...mapGetters({
      isDev: 'isDev'
    }),
    ...mapState('router', {
      storeInfoFromPar: 'storeInfoFromPar'
    }),
    lecturers() {
      return this.courseInfo.lecturers || []
    },
    categoryNames() {
      if(this.courseInfo.categoryNames&&this.courseInfo.categoryNames.length) {
        return this.courseInfo.categoryNames.toString()
      } else {
        return '--'
      }
    }
  },
  created() {
    // this.courseId = this.$route.query.id
    // this.orgId = this.$route.query.orgId
    this.courseId = this.storeInfoFromPar.id
    this.orgId = this.storeInfoFromPar.orgId
    this.getAllInfo()
    // this.getStat()
    // this.getInfo()
  },
  methods: {
    // getTableData() {
    //   this.$router.push({name: 'CourseListIndex'})
    // },
    onCopy() {
      this.$refs.DialogCopyCourse.open({
        formData: {
          id: this.courseId,
          courseName: this.courseInfo.courseName
        }
      })
    },
    async onDelete() {
      let name = '课程'
      await this.$confirm(`此操作将永久删除该${name}，是否继续？`, '提示', {
        type: 'warning'
      })

      let {code} = await this.$api.Course.delete({
        idList: [this.courseId]
      })
      if (code !== 200) return
      this.$msg.Delete(name)
      this.$router.push({name: 'CourseListIndex'})
    },

    async onGooded() {
      let {code} = await this.$api.Course.baseSetting({
        id: this.courseId,
        gooded: this.courseInfo.gooded == 1 ? 0 : 1
      })
      if (code !== 200) return false
      this.courseInfo.gooded = this.courseInfo.gooded == 1 ? 0 : 1
      this.$msg.Update()
    },
    onEdit() {
      window.open(`${origin}/#/Education/Course/List/Edit?id=${this.courseInfo.id}`, Date.now() + '_bank')
    },
    async onShelve() {
      let {code} = await this.$api.Course.baseSetting({
        id: this.courseId,
        shelfStatus: this.courseInfo.shelfStatus == 1 ? 0 : 1
      })
      if (code !== 200) return false
      if (this.courseInfo.shelfStatus == 1) {
        this.courseInfo.shelfStatus = 0
        this.$msg.OffShelve()
      } else {
        this.courseInfo.shelfStatus = 1
        this.$msg.OnShelve()
      }
    },
    tabClick(tab) {
      this.currentTab = tab.name
    },
    // 课程统计信息
    async getStat() {
      if (!this.courseId) return
      let {code, data} = await this.$api.CourseStat.info({
        id: this.courseId
      })
      if (code !== 200) return false
      this.courseInfo = data
    },
    // 课程基本信息
    async getInfo() {
      if (!this.courseId) return
      let {code, data} = await this.$api.Course.baseDetail({
        id: this.courseId
      })
      if (code !== 200) return false
      this.courseInfo = data
    },
    // 获取专题详情所有信息
    async getAllInfo() {
      let { code, data } = await this.$api.OrgCourse.detail({
        id: this.courseId,
        orgId: this.orgId
      })
      if(code !== 200) return false
      this.courseInfo = data
    }
  }
}
</script>

<style lang="stylus" scoped>
.border-1 {
  border-radius: 4px
  border: 1px solid #eee
}

.height-290 {
  height: 290px;
}

.height-135 {
  height: 135px;
}

.position-left {
  top: 0;
  left: 0
  background: #FE993A
  padding: 2px 4px
  border-radius: 4px 0px 4px 0px;
}

.el-card
  border none

/deep/ .el-tabs__content
  // height calc(100vh - 48px - 32px - 30px - 98px - 44px - 8px - 16px)
  height calc(100vh - 32px - 30px - 103px - 44px - 6px - 16px)
  margin-top 16px
  background-color #fff
  overflow auto

.tag
  top 0px
  padding 4px 6px
  color #fff
  font-size 12px
  border-radius 4px

  &.record
    background linear-gradient(227.53deg, rgba(10, 137, 255, 1) 0%, rgba(0, 210, 252, 1) 100%)

  &.live
    background linear-gradient(224.03deg, rgba(248, 114, 32, 1) 0%, rgba(255, 190, 38, 1) 100%)

  &.mix
    background linear-gradient(227.43deg, rgba(112, 41, 255, 1) 0%, rgba(149, 92, 255, 1) 100%)

.ico-shelfStatus {
  display: inline-block;
  height: 20px;
  line-height: 20px
}

>>> .el-tabs__active-bar
  width 56px!important
</style>