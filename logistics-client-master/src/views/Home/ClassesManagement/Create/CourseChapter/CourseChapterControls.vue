<template>
  <div ref="modeTree" class="bg-f width-280 margin-right-16 padding-14">

    <div>课程信息</div>

    <div class="margin-bottom-20 margin-top-10">
      <el-button class="btn-background" size="small" icon="el-icon-plus" @click="onAddClasses()"
        >添加课程信息
      </el-button>
    </div>

    <el-tree
      ref="tree"
      :data="treeData"
      :draggable="draggable"
      default-expand-all
      node-key="id"
      @node-drop="handleDrop"
      :allow-drop="allowDrop"
      @node-click="onSelectLesson"
      v-loading="loading.page">
      <div
        class="flex between-center width-100p overflow height-100p padding-right-10 lesson-node"
        :class="{ active: data.id === selectedLesson.id }"
        slot-scope="{ data, node }">
        <!--内容-->
        <div class="text-ellipsis font-14" :title="data.lessonName">
          {{ data.lessonName }}
        </div>

        <!--按钮-->
        <div class="shrink-0">
          <!--拖拽标识-->
          <el-button
                  class="text-6 font-16"
                  type="text"
                  icon="el-icon-rank"
                  @mousedown.native="draggable = true" 
                  @mouseup.native="draggable = false"
                  size="mini">
                </el-button>

          <el-dropdown trigger="hover" :hide-on-click="false">
            <span class="btn-icon el-icon-more pointer" @click.stop></span>
            <el-dropdown-menu class="font-12" slot="dropdown">
              <el-dropdown-item class="text-main" @click.native="onEditTreeItem(data)">编辑</el-dropdown-item>
              <el-dropdown-item class="text-main" @click.native="deleteLesson(data)">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </el-tree>
    
    <DialogFormChapter ref="DialogFormChapter"></DialogFormChapter>
  </div>
</template>

<script>
import DialogFormChapter from '@/components/dialog/ClassesManagement/DialogFormChapter';

export default {
  name: 'CourseChapterControls',
  components: {
    // DialogSelectCourseMode,
    DialogFormChapter,
  },
  props: {
    // 选中的课程
    selectedLesson: {
      type: Object,
      default() {
        return {};
      },
    },
    // 班级信息
    classInfo: {
      type: Object,
      default() {
        return {};
      },
    },
  },
  data() {
    return {
      courseContent: {}, // 章节原始数据

      loading: {
        page: false,
      },
      timer: null,

      // 所有课程信息
      allClassesInfo: [],

      // 是否可拖曳控制
      draggable: false
    };
  },
  computed: {
    treeData() {
      return this.allClassesInfo;
    },
  },
  watch: {
    classInfo: {
      handler() {
        this.getAllClasses();
      },
    },
  },
  created() {},
  mounted() {
    this.$nextTick(() => {
      setTimeout(() => {
        let topHeight = this.$refs.modeTree.offsetHeight; // 头部高度
        console.log('topHeight', topHeight);
      }, 300);
    });
  },
  methods: {
    // 获取 - 所有课程信息
    async getAllClasses() {
      // 还未获取课程信息时不调用 获取所有课程信息 的接口
      if (this.classInfo?.id === undefined) return false;

      this.loading.page = true;
      let { code, data } = await this.$api.Classes.classesList({
        clazzId: this.classInfo.id,
      });
      this.loading.page = false;
      if (code !== 200) return false;
      this.allClassesInfo = data;

      if (!(this.allClassesInfo && this.allClassesInfo.length)) return false;

      // 默认选择第一节
      let selectedLesson = this.allClassesInfo[0];

      if (selectedLesson) {
        this.$nextTick(() => {
          this.$refs.tree && this.$refs.tree.setCurrentKey(selectedLesson.id);
          this.onSelectLesson(selectedLesson);
        });
      }
    },

    // 操作 - 添加课程信息
    async onAddClasses() {
      await this.$refs.DialogFormChapter.open({
        type: 'ClassCreate',
        formData: {
          sort: this.allClassesInfo?.length + 1,
          clazzId: this.classInfo.id,
        },
      });

      // 获取所有课程信息
      this.getAllClasses();
    },

    // 操作 - 编辑课程信息
    async onEditTreeItem(item) {
      await this.$refs.DialogFormChapter.open({
        type: 'ClassEdit',
        formData: {
          ...item,
        },
      });

      // 获取所有课程信息
      this.getAllClasses();
    },

    // 删除课程
    deleteLesson(row) {
      if (this.allClassesInfo.length == 1) {
        return this.$message.warning('节最少需要一节');
      }
      this.$confirm('此操作将永久删除该课程，是否继续？', '提示', {
        type: 'warning',
      })
        .then(async () => {
          let { code } = await this.$api.Classes.classDelete({
            idList: [row.id],
          });
          if (code !== 200) return false;
          this.$msg.Delete('课程');

          // 获取所有课程信息
          this.getAllClasses();
        })
        .catch(() => {});
    },

    // 判断当前拖拽的节点是否可以被放置
    allowDrop(draggingNode, dropNode, type) {
      // 只允许 同级前后 排序
      if (draggingNode.level === dropNode.level && type !== 'inner') {
        // 不允许跨章
        return !(draggingNode.level === 2 && draggingNode.data.chapterId !== dropNode.data.chapterId);
      }
    },

    // 操作 - 节点排序 - 节点拖拽成功触发
    async handleDrop(draggingNode) {
      let chapterList = this.$refs.tree.data;

      let sortLists = chapterList.map(item => item.id)

      let { code } = await this.$api.Classes.classSortUp({
        sortedIds: sortLists
      });
      if (code !== 200) return false;
      this.$message.success('排序设置成功');
    },

    // 操作 - 选择节
    onSelectLesson(item) {
      if (this.timer) clearTimeout(this.timer);
      this.timer = setTimeout(() => {
        if (!item.id) return false;
        this.$emit('update:selectedLesson', item);
      }, 100);
    },
  },
};
</script>

<style scoped lang="stylus">
.btn-background
  background-color BACKGROUND_COLOR
  color MAIN_COLOR
  border none
.btn-icon
  margin-left 6px
  text-align center
  color NEUTRAL_COLOR_C
  &.el-icon-rank
    transform rotate(45deg)
  &:hover
    color MAIN_COLOR
/* 只允许 节 lesson 有hover,选中效果 */

>>>.el-tree
  height calc(100vh - 260px)
  overflow auto
  .el-tree-node
    .el-tree-node__content
      height 36px
      .el-tree-node__expand-icon
        display none

      .lesson-node
        padding-left 24px
        &:hover, &.active
          background-color BACKGROUND_COLOR
          color MAIN_COLOR

          .el-button, .btn-icon
            color MAIN_COLOR

</style>
