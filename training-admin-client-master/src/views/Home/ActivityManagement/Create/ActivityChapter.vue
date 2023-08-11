<!--课程章节-->
<template>
  <div class="height-100p margin-top-8">
    <!--章节模式及章节-->
    <el-container :style="{ height: $utils.FullViewHeight(isHeight) }">
      <el-aside class="bg-f width-280 margin-right-16 padding-14">
        <el-header style="text-align: left; font-size: 12px; height: 120px; padding: 0">
          <div class="flex between-center font-14 margin-bottom-12">
            <span class="font-14">当前模式: {{ chapterTypeList[courseMode - 1].name }}</span>
            <el-button class="el-icon-s-tools font-18 text-6" type="text" size="mini"
                       @click="setUpCourseAction"></el-button>
          </div>
          <div class="font-14 text-6 margin-bottom-16 flex">
            <span> 什么是章节模式 </span>

            <ToolTip>
              <div slot="content">
                <div>温馨提示：</div>
                <div>1、直播或视频等课程内容只能关联到节，章只作为节的导引，不能关联课程内容。</div>
                <div>2、选择无章节模式时，相当于课程只有一节，课程内容直接显示节内容，不显示导引目录。</div>
              </div>
            </ToolTip>

          </div>
          <div class="margin-bottom-20">
            <el-button v-if="courseMode === 1" class="addChapter-bg" size="small" icon="el-icon-plus"
                       @click="addChapter">添加章
            </el-button>
            <el-button v-if="courseMode !== 3" class="addChapter-bg" size="small" icon="el-icon-plus"
                       @click="addChapterChildrenLesson">添加节
            </el-button>
          </div>
        </el-header>

        <template v-if="courseMode !== 3">
          <ChapterTree :listData="listData" :courseMode="courseMode" @initChapterCentent="initChapterCentent"
                       @setTreeSort="setTreeSort">
            <template v-slot:side="{ data, node }">
              <span class="text-ellipsis font-14" :class="node.level == 1 ? 'width-170' : 'width-200'"
                  :style="{ 'font-weight': data.lessonId ? '400' : '500' }"
                  :title="data.chapterName || data.lessonName">
                <span class="text-bold" v-if="data.chapterName">{{ data.chapterName }}</span>
                <span v-else-if="data.lessonName">{{ data.lessonName }}</span>
              </span>
              <div>
                <i v-if="!data.lessonId" class="el-icon-plus text-c more-btn margin-right-8"
                   @click.stop="addLesson(data)"></i>
                <i class="el-icon-rank more-btn text-c more-rotate"></i>
                <el-tooltip placement="bottom" effect="light">
                  <div slot="content">
                    <div v-if="data.lessonId">
                      <el-button type="text" size="mini" @click="editLesson(data, true)">编辑</el-button>
                      <br/>
                      <el-button type="text" size="mini" @click="deleteLesson(data)">删除</el-button>
                    </div>
                    <div v-else>
                      <el-button type="text" size="mini" @click="editChapter(data, true)">编辑</el-button>
                      <br/>
                      <el-button type="text" size="mini" @click="deleteChapter(data)">删除</el-button>
                    </div>
                  </div>
                  <span class="el-icon-more text-c more-btn"></span>
                </el-tooltip>
              </div>
            </template>
          </ChapterTree>
        </template>
      </el-aside>
      <!--右边布局-->
      <el-container>
        <!--类型选择-->
        <el-header class="flex pointer bg-f flex start-center" height="92px">
          <div class="flex column center-center margin-right-20" v-for="item in courseChapterType" :key="item.id"
               @click="onComponent(item)">
            <el-image class="width-40 height-40" :src="item.icon"></el-image>
            <span class="font-12">{{ item.name }}</span>
          </div>
        </el-header>
        <!--关联内容-->
        <el-main class="margin-top-16 bg-f">
          <template v-if="AppendixParams.appendixList.length > 0">
            <AppendixList :AppendixParams="AppendixParams" @setTreeSort="setTreeSort" @editAttach="editAttach"
              @updataAttachName="updataAttachName" @updataAttachZB="updataAttachZB" @getComponentList="getComponentList"></AppendixList>
          </template>
        </el-main>
      </el-container>
    </el-container>
    <DialogAddChapter ref="DialogAddChapter" @success="getList"></DialogAddChapter>
    <DialogAddLesson ref="DialogAddLesson" @success="getList"></DialogAddLesson>
    <DialogLessonAttach ref="DialogLessonAttach" @success="getComponentList"></DialogLessonAttach>
    <DialogLiveList ref="DialogLiveList" @success="getDetail"></DialogLiveList>
    <DialogChapterType ref="DialogChapterType"></DialogChapterType>
    <DrawerLessonAttachZB ref="DrawerLessonAttachZB" @success="getComponentList"></DrawerLessonAttachZB>
    <DialogChangeComponent ref="DialogChangeComponent" @success="getComponentList"></DialogChangeComponent>

    <!-- <div class="width-100p flex center-center bg-f margin-top-10 height-60">
        <el-button @click="onFowrod">下一步</el-button>
        <el-button type="primary" @click="onBack">完成并返回课程详情</el-button>
    </div> -->

    <ButtonSave class="width-100p flex center-center bg-f margin-top-10 height-60">
      <el-button @click="onBreak">上一步</el-button>
      <el-button @click="onFowrod">下一步</el-button>
      <el-button type="primary" @click="onBack">完成并返回课程详情</el-button>
      <!-- <el-button class="width-96 margin-right-6" type="primary" size="medium" @click="onSubmit"
              :loading="loading.submit">
          下一步
      </el-button> -->
    </ButtonSave>
  </div>
</template>

<script>
import DialogAddChapter from '@/components/dialog/Course/DialogAddChapter'
import DialogAddLesson from '@/components/dialog/Course/DialogAddLesson'
import DialogLessonAttach from '@/components/dialog/Course/DialogLessonAttach'
import DialogLiveList from '@/components/dialog/Course/DialogLiveList'
import DialogChapterType from '@/components/dialog/Course/DialogChapterType'
import DrawerLessonAttachZB from '@/components/dialog/Course/DrawerLessonAttachZB'
import DialogChangeComponent from '@/components/dialog/Course/DialogChangeComponent'

import ChapterTree from './ChapterTree.vue'
import AppendixList from './AppendixList.vue'
import {mapState} from 'vuex'

export default {
  components: {
    DialogAddChapter,
    DialogAddLesson,
    DialogLessonAttach,
    DialogLiveList,
    DialogChapterType,
    DrawerLessonAttachZB,
    DialogChangeComponent,
    ChapterTree,
    AppendixList
  },
  props: {
    courseInfo: {
      type: Object,
      default: () => {
      }
    }
  },
  data() {
    return {
      playbackSpeed: 0,
      courseId: null,
      listData: [],
      liveDetail: {},
      isUpdataName: null,
      lessonId: null,
      chapterId: 0,
      AppendixParams: {
        appendixList: [],
        level: '',
        lessonId: '',
        chapterId: ''
      },
      loading: {
        table: null
      },
      courseMode: 2 // 默认节模式
    }
  },
  computed: {
    ...mapState('common', {
      chapterTypeList: 'chapterTypeList',
      courseChapterType: 'courseChapterType'
    }),
    isHeight() {
      return this.$route.params.type === 'Edit' ? 160 : 142
    }
  },
  watch: {
    courseInfo: {
      deep: true,
      handler(course) {
        if (!course) return
        this.courseId = parseInt(course.id)
        this.courseMode = course.courseMode || 1
        if (course.courseManage) {
          this.getList()
        } else if (course.liveId) {
          this.getDetail()
        }
      },
      immediate: true
    }
  },
  methods: {
    // 退回
    onBack() {
      this.$router.push({
        name: 'CourseDetail',
        query: {
          id: this.courseId
        }
      })
    },
    // next
    onFowrod() {
      this.$emit('page-change', {
        page: 'CourseAttach',
        id: this.courseId
      })
    },
    // next
    onBreak() {
      this.$emit('page-change', {
        page: 'CourseInfo',
        id: this.courseId
      })
    },

    // 设置排序
    async setTreeSort(params) {
      console.log(params);
      params.courseId = this.courseId
      let {code} = await this.$api.Course.treeSort(params)
      if (code !== 200) return false
      this.$message.success('排序设置成功')
    },
    // 关联章节列表
    initChapterCentent(item) {
      this.chapterId = item.chapterId
      this.lessonId = null
      this.AppendixParams.appendixList = []
      if (item.lessonId) {
        this.lessonId = item.lessonId
        this.getComponentList()
      }
      this.AppendixParams.chapterId = this.chapterId
      this.AppendixParams.lessonId = this.lessonId
      this.AppendixParams.level = 'third'
      // this.AppendixParams.courseId = this.courseId
    },
    // 获取附件列表
    async getComponentList() {
      this.appendixList = []
      let {code, data} = await this.$api.Course.listByLesson({id: this.lessonId})
      if (code !== 200) return false
      this.AppendixParams.appendixList = data || []
    },
    //设置章节模式
    async setUpCourseAction() {
      if (!this.courseId) return this.$message.warning('请先完善课程基本信息')
      this.appendixList = []
      this.courseMode = await this.$refs.DialogChapterType.open(this.courseMode)
      let {code} = await this.$api.Course.settingMode({
        courseId: this.courseId,
        courseMode: this.courseMode
      })
      if (code !== 200) return false
      this.$msg.SetUP(this.chapterTypeList[this.courseMode - 1].name)
      this.getList()
    },
    // 章节列表
    async getList() {
      if (!this.courseId) return
      let {code, data} = await this.$api.Course.contentList({
        id: this.courseId,
        courseMode: this.courseMode
      })
      if (code !== 200) return false

      if (this.courseMode === 3) {
        this.lessonId = 0
        if (data.chapterList.length > 0) {
          this.lessonId = (data.chapterList[0].lessonList && data.chapterList[0].lessonList[0].lessonId) || null
          this.AppendixParams.level = 'third'
          this.AppendixParams.lessonId = this.lessonId
          this.AppendixParams.chapterId = this.chapterId
        }
        this.getComponentList(this.lessonId)
        return
      }
      this.listData = data.chapterList ? data.chapterList.map(item => {
        return {
          ...item,
          expand: true
        }
      }) : []
    },
    // 直播详情
    async getDetail() {
      let {code, data} = await this.$api.CourseLive.detail({
        courseId: this.courseId
      })
      if (code !== 200) return false
      this.liveDetail = data
    },

    // 添加章
    addChapter() {
      if (!this.courseId) {
        return this.$message.warning('请先完善课程基本信息')
      }
      let list = this.listData
      this.$refs.DialogAddChapter.open({
        formData: {
          courseId: this.courseId,
          isShow: true,
          sort: list.length ? list[list.length - 1].sort + 1 : 1
        }
      })
    },
    // 编辑章名称
    editChapter(row, isSort) {
      this.$refs.DialogAddChapter.open({
        type: 'Edit',
        formData: {
          id: row.chapterId,
          courseId: this.courseId,
          chapterName: row.chapterName,
          isSort: isSort,
          sort: row.sort
        }
      })
    },
    // 删除章
    deleteChapter(row) {
      if (this.listData.length == 1) return this.$message.warning('章最少需要一章')
      this.$confirm('此操作将永久删除该章，是否继续？', '提示', {
        type: 'warning'
      }).then(async () => {
        let {code} = await this.$api.Course.deleteChapter({
          idList: [row.chapterId]
        })
        if (code !== 200) return false
        this.$msg.Delete('章')
        this.getList()
      }).catch(() => {
      })
    },
    // 添加节
    addLesson({chapterId, lessonList}) {
      let list = lessonList.length + 1
      this.$refs.DialogAddLesson.open({
        formData: {
          courseId: this.courseId,
          chapterId: chapterId,
          isChapter: 'noChapter',
          courseMode: this.courseMode,
          isShow: true,
          sort: list
        }
      })
    },
    // 添加节
    addChapterChildrenLesson() {
        console.log("2");
      this.$refs.DialogAddLesson.open({
        formData: {
          courseId: this.courseId,
          isChapter: 'chapter',
          courseMode: this.courseMode,
          isShow: true,
          sort: 1,
          chapterList: this.listData.map(item => {
            return {
              label: item.chapterName,
              value: item.chapterId
            }
          })
        }
      })
    },
    // 编辑节
    editLesson(row, isSort) {
      this.$refs.DialogAddLesson.open({
        type: 'Edit',
        formData: {
          id: row.lessonId,
          courseId: this.courseId,
          chapterId: row.chapterId,
          lessonName: row.lessonName,
          sort: row.sort,
          isSort: isSort
        }
      })
    },
    // 删除节
    deleteLesson(row) {
      if (this.listData.length == 1 && this.listData && this.listData[0].lessonList.length <= 1) return this.$message.warning('节最少需要一节')
      this.$confirm('此操作将永久删除该节，是否继续？', '提示', {
        type: 'warning'
      })
          .then(async () => {
            let {code} = await this.$api.Course.deleteLesson({
              idList: [row.lessonId]
            })
            if (code !== 200) return false
            this.$msg.Delete('节')
            this.getList()
          })
          .catch(() => {
          })
    },
    // 附件类型事件
    onComponent({type}) {
      if (!this.lessonId) return this.$message.warning('请先选择需要绑定的节')
      switch (type) {
        case 'SP':
          this.addAttachSP(type)
          break
        case 'ZB':
          this.addAttachZB(type)
          break
      }
    },
    // 新增直播
    addAttachZB() {
      this.$refs.DrawerLessonAttachZB.open({
        type: 'Create',
        formData: {
          courseId: this.courseId,
          lessonId: this.lessonId,
          chapterId: this.chapterId
        }
      })
    },
    // 修改直播
    updataAttachZB(row) {
      this.$refs.DrawerLessonAttachZB.open({
        type: 'Edit',
        formData: {
          courseId: this.courseId,
          lessonId: this.lessonId,
          chapterId: this.chapterId
        },
        row: row
      })
    },
    // 新增视频
    addAttachSP(type) {
      this.$refs.DialogLessonAttach.open({
        type: 'Add',
        formData: {
          courseId: this.courseId,
          lessonId: this.lessonId,
          chapterId: this.chapterId
        },
        // courseType: this.courseInfo.courseType,
        courseType: type
      })
    },
    // 编辑附件
    editAttach(row) {
      this.$refs.DialogLessonAttach.open({
        type: 'Edit',
        formData: {
          courseId: this.courseId,
          lessonId: row.lessonId,
          chapterId: this.chapterId,
          componentId: row.componentId
        },
        selectionId: row.resourceFileId,
        // courseType: this.courseInfo.courseType,
        courseType: row.type
      })
    },
    // 修改附件名称
    updataAttachName(row) {
      this.$refs.DialogChangeComponent.open({
        type: 'EditVideo',
        formData: {
          id: row.componentId,
          name: row.componentName,
          fileName: row.componentName,
          fileSize: row.resourceFileSize,
          fileDuration: row.resourceFileDuration
        }
      })
    }
  }
}
</script>

<style lang="stylus" scoped>
.color-ico {
  color: #1D61F2
}

.is-hover {
  display: none;
}

.more-rotate {
  transform: rotate(45deg);
}

.more-btn {
  vertical-align: text-bottom
  margin-left: 6px
  width: 16px
  height: 16px
  display: inline-block;

  &:hover {
    color: #1D61F2
  }
}

.width-170 {
  width: 170px;
  display: inline-block;
  vertical-align: middle;
}

.width-200 {
  width: 200px;
  display: inline-block;
  vertical-align: middle;
}

.chapterName {
  width 100px

  >>> .el-input__inner {
    border 1px solid MAIN_COLOR
    height 24px
    padding 0 0 0 1px;
  }
}

.addChapter-bg {
  background: #EBF2FF;
  color: #1D61F2;
  border: none;
}

.el-card
  border none

  .el-radio
    margin-right 8px

  &.content
    overflow auto

    .chapter
      border-top 1px solid #eee
      border-left 1px solid #eee
      border-right 1px solid #eee
      border-radius 4px
      font-size 14px

      .row
        padding 4px 16px 4px 16px
        border-bottom 1px solid #eee

>>> .el-tree-node {
  &:focus > .el-tree-node__content {
    background: #EBF2FF
    color: #1D61F2
  }

  .el-tree-node__content {
    min-height: 36px;
    height: auto;
    color: #333;
    font-weight: 500;
    padding-right: 10px

    &:hover {
      background-color: #EBF2FF;
      color: #1D61F2
    }
  }
}

>>> .el-tree-node__children {
  .el-tree-node {
    &:focus > .el-tree-node__content {
      color: #1D61F2
    }
    max-height: 36px;

    .el-tree-node__content {
      height: 36px;
      padding-left: 24px !important;
      padding-right: 10px
      font-weight: 400;
      color: #666;

      &:hover {
        background-color: #EBF2FF;
        color: #1d61f2
      }

      .el-tree-node__expand-icon.el-icon-caret-right {
        display: none
      }
    }
  }
}

>>> .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background-color: #EBF2FF;
  color: #1D61F2
}
</style>
