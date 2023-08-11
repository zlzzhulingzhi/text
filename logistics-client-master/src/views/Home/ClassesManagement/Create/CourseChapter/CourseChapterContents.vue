<template>
  <div class="flex column flex-1 container">
    <!--新增按钮-->
    <div class="bg-f flex start-center padding-left-32 height-92 shrink-0">
      <el-button type="primary" size="medium" @click="onAddLessonInfo({type: 'create'})">添加课时信息</el-button>
    </div>
    <!--讲义 关联 内容/附件-->

    <div class="margin-top-16 bg-f flex-1 padding-top-8 padding-left-20 padding-right-20 padding-bottom-8 border-f2">
      <!--表头-->
      <div class="flex between-center padding-left-8 width-100p font-16 text-3 height-44">
        <div class="width-200">课时名称</div>
        <div class="width-260">上课时间</div>
        <div class="width-100">上课教室</div>
        <div class="width-100">讲师</div>
        <div class="flex-1">课时描述</div>
        <div class="width-80 padding-right-8">操作</div>
      </div>

      <div class="overflow-auto" :style="{ height: $utils.FullViewHeight(307) }" v-loading="loading.page">
        <!--列表-->
        <el-tree ref="tree" :data="contentList" :draggable="draggable" :allow-drop="allowDrop" @node-drop="handleDrop">
          <template slot-scope="{ node, data }">
            <div class="flex between-center width-100p line-height-64 border-bottom-f2 text-bold-400">
              <!-- 课时名称 -->
              <div class="height-64 width-200 font-14 flex column center margin-left-8">
                <el-tooltip class="item" effect="light" :content="data.contentName" placement="top-start">
                  <div class="inCaseOfTooLong">
                    {{ data.contentName }}
                  </div>
                </el-tooltip>
              </div>
              <!-- 上课时间 -->
              <div class="width-260 flex column font-14">
                <div class="flex">
                  <span>{{ data.startDate }}</span>
                  <span v-if="data.endDate && data.startDate !== data.endDate">&nbsp;-&nbsp;{{ data.endDate }}</span>
                </div>
              </div>
              <!-- 上课教室 -->
              <div class="width-100 flex column font-14">
                <div class="inCaseOfTooLong">{{ data.roomNo }}</div>
              </div>
              <!-- 讲师 -->
              <div class="width-100 flex column font-14">
                <!-- <el-tooltip class="item" effect="light" :content="data.lecturerName" placement="top-start">
                  <div class="inCaseOfTooLong">{{ data.lecturerName }}</div>
                </el-tooltip> -->
                <div class="inCaseOfTooLong">{{ data.lecturerName }}</div>
              </div>
              <!-- 课时描述 -->
              <div class="flex-1 flex column font-14 introduce">
                <el-tooltip
                  class="item"
                  effect="light"
                  :content="data.contentDescription"
                  placement="top-start"
                  v-if="data.contentDescription">
                  <div class="introduceItem">{{ data.contentDescription }}</div>
                </el-tooltip>
                <div v-else>-</div>
              </div>
              <!-- 操作 -->
              <div class="width-80 height-64 font-14">
                <el-button
                  class="text-main text-bold font-16"
                  type="text"
                  icon="el-icon-edit"
                  size="mini"
                  @click.stop="onAddLessonInfo({ data, type: 'edit' })"></el-button>
                <el-button
                  class="text-main text-bold font-16"
                  type="text"
                  icon="el-icon-rank"
                  @mousedown.native="draggable = true" 
                  @mouseup.native="draggable = false"
                  size="mini"
                  v-if="node.level === 1">
                </el-button>
                <el-button
                  class="text-main text-bold font-16"
                  type="text"
                  icon="el-icon-delete"
                  size="mini"
                  @click.stop="deleteLessonItem(data, node)">
                </el-button>
              </div>
            </div>
          </template>
        </el-tree>
      </div>
    </div>

    <!-- 添加课时弹窗 -->
    <DialogAddLessonInfo ref="DialogAddLessonInfo"></DialogAddLessonInfo>
  </div>
</template>

<script>
// 添加课时弹窗
import DialogAddLessonInfo from '@/components/dialog/ClassesManagement/DialogAddLessonInfo'
export default {
  name: 'CourseChapterContents',
  components: {
    // DialogFormCourseContent,
    // DialogAddDocument
    DialogAddLessonInfo
  },
  mounted() {
    // 拖拽事件对象，修改鼠标样式
    this.$refs.tree.$on('tree-node-drag-over', (event) => {
      event.dataTransfer.dropEffect = 'move';
    });
  },
  props: {
    // 选中节
    selectedLesson: {
      type: Object,
      default: () => {
        return {};
      },
    },
    // 班级信息
    classInfo: {
      type: Object,
      default: () => {
        return {};
      },
    },
    componentItem: {
      type: Object,
    },
    lesson: {
      type: Object,
    },
    courseId: {
      type: [String, Number],
    },
  },
  data() {
    return {
      // 课时信息
      contentList: [],
      draggable: false,

      loading: {
        page: false,
      },
      courseContent: {},

      isInit: true, // 防重复调用onEdit
    };
  },
  computed: {
    callEditParams() {
      if (!this.courseContent.chapterList) return false;
      if (!this.selectedChapter.chapterId) return false;
      if (!this.isInit) return false;
      return this.componentItem;
    },
  },
  watch: {
    selectedLesson() {
      this.getContentList();
      this.getCourseContent();
    },
    callEditParams: {
      deep: true,
      handler(val) {
        if (val) {
          this.isInit = false;
          this.onEdit(this.componentItem, this.lesson);
        }
      },
    },
  },
  methods: {
    // 获取 - 获取课时信息
    async getContentList() {
      if (!this.selectedLesson?.id) return false;
      this.loading.page = true;
      let { code, data } = await this.$api.Classes.classItemsList({
        lessonId: this.selectedLesson.id,
        clazzId: this.classInfo.id,
      });
      this.loading.page = false;
      if (code !== 200) return false;
      this.contentList = data;
    },
    // 添加、编辑课时信息
    async onAddLessonInfo(params) {
      if(!this.selectedLesson.id) {
        this.$message.warning('请先新增课程信息！')
        return false
      }
      let outputData = await this.$refs.DialogAddLessonInfo.open({
        formData: {
          clazzId: this.classInfo.id,
          lessonId: this.selectedLesson.id,
        },
        type: params.type,
        courseInfo: params.data,
        // 所有的节信息
        lessonList: this.courseContent
        // lessonIdList: lessonIdList

      })
      if(outputData) this.getContentList()
    },

    // 获取 - 所有课程信息
    async getCourseContent() {
      if (!this.classInfo?.id) return false;
      this.loading.page = true;
      let {code, data} = await this.$api.Classes.classesList({
        clazzId: this.classInfo.id
      })
      this.loading.page = false;
      if (code !== 200) return false;

      this.courseContent = data || {};
      // // 默认选中
      // let selectedChapter = this.treeData[0]
      // if (selectedChapter) {
      //   // 第一章第一节 || 第一章
      //   selectedChapter = (selectedChapter.lessonList && selectedChapter.lessonList[0]) || selectedChapter

      //   this.$nextTick(() => {
      //     this.$refs.tree && this.$refs.tree.setCurrentKey(selectedChapter.id)
      //     this.onSelectChapter(selectedChapter)
      //   })
    },

    // 拖拽判断
    allowDrop(draggingNode, dropNode, type) {
      return type !== 'inner';
    },
    // 操作 - 节点 排序
    async handleDrop(draggingNode) {

      let chapterList = this.$refs.tree.data;

      let sortLists = chapterList.map(item => item.id)
      
      let { code } = await this.$api.Classes.classItemSortUp({
        sortedIds: sortLists
      });

      if (code !== 200) return false;

      this.$message.success('排序设置成功');
    },

    // 删除课程
    async deleteLessonItem(row) {
      let { code } = await this.$api.Classes.classItemDelete({
        idList: [row.id],
      });
      if (code !== 200) return false;

      this.$msg.Delete('课时');

      // 获取课时信息
      this.getContentList();
    },
  },
};
</script>

<style scoped lang="stylus">
// 最外层添加最小宽度，放置小屏幕造成显示问题  
.container
  min-width 930px

>>>.introduce 
  width calc(100% - 755px)
  // min-width 400px
  
.introduceItem, .inCaseOfTooLong
  width 90%
  overflow hidden
  white-space nowrap
  text-overflow ellipsis

>>> .el-tree-node {
  &:focus > .el-tree-node__content {
    background: BACKGROUND_COLOR
    color: MAIN_COLOR
  }

  .el-tree-node__content {
    min-height: 36px;
    height: auto;
    color: #333;
    font-weight: 500;
    padding-right: 10px

    &:hover {
      background-color: BACKGROUND_COLOR;
      color: MAIN_COLOR
    }

    .el-tree-node__expand-icon.el-icon-caret-right {
      display: none
    }
  }
}

>>> .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background-color: BACKGROUND_COLOR;
  color: MAIN_COLOR
}
</style>
