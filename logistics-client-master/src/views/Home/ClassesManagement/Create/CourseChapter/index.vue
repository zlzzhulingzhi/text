<!--课程章节-->
<template>
  <div class="flex" :style="pageInfo.pageStyle">

    <!--章节-->
    <CourseChapterControls class="shrink-0" :classInfo="classInfo" :selectedLesson.sync="selectedLesson" @success="getPageData"></CourseChapterControls>

    <!--讲义内容/附件-->
    <CourseChapterContents ref="ChapterContents" class="overflow" :classInfo="classInfo" :selectedLesson="selectedLesson" :componentItem="componentItem" :lesson="lesson" :courseId="courseId"></CourseChapterContents>

    <ButtonSave class="width-100p flex center-center bg-f margin-top-10 height-60">
      <el-button @click="onGoPrev">上一步</el-button>
      <!-- <el-button @click="onGoNext">下一步</el-button> -->
      <el-button type="primary" @click="onBack">完成并返回课程详情</el-button>
    </ButtonSave>
  </div>
</template>

<script>
import CourseChapterControls from './CourseChapterControls.vue'
import CourseChapterContents from './CourseChapterContents.vue'

export default {
  components: {
    CourseChapterControls,
    CourseChapterContents
  },
  props: {
    classInfo: {
      type: Object,
      default: () => {
        return {}
      }
    },
    type: {
      type: String,
      default: 'Create'
    },
    componentItem: {
      type: Object
    },
    lesson: {
      type: Object
    },
    courseId: {
      type: [String, Number]
    }
  },
  data() {
    return {
      selectedLesson: {}
    }
  },
  mounted() {
    // if(this.componentItem){
    //   console.log(this.$refs.ChapterContents);
    //   this.$refs.ChapterContents.onEdit(this.componentItem)
    // }
  },
  computed: {
    pageInfo() {
      if (this.type === 'Edit') {
        return {
          pageStyle: {
            height: this.$utils.FullViewHeight(142)
          }
        }
      }

      return {
        pageStyle: {
          height: this.$utils.FullViewHeight(100)
        }
      }
    }
  },
  methods: {
    // 操作 - 完成并返回课程详情
    onBack() {
      this.$router.push({
        name: 'classesList'
      })
    },
    // 操作 - 上一步
    onGoPrev() {
      this.$emit('page-change', {
        page: 'ClassInfo',
        id: this.classInfo.id
      })
    },
    // 操作 - 下一步
    // onGoNext () {
    //   this.$emit('page-change', {
    //     page: 'CourseAttach',
    //     id: this.classInfo.courseId
    //   })
    // },
    getPageData() {
      this.$refs.ChapterContents.getCourseContent()
    }
  }
}
</script>