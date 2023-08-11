<template>
  <el-tabs v-model="activePageName" v-if="CreateVisible">
    <template v-if="type === 'Create'">
      <template>
        <component v-for="item in pages" :key="item.id" :type="type" :is="item.id" v-show="activePageName === item.id" :classInfo.async="classInfo" @page-change="pageChange"></component>
      </template>
    </template>

    <template v-else>
      <el-tab-pane v-for="item in pages" :key="item.id" :name="item.id" :label="item.name">
        <component :type="type" :is="item.id" :classInfo.async="classInfo" @page-change="pageChange" :componentItem="componentItem" :lesson="lesson" :classId="classId"></component>
      </el-tab-pane>
    </template>

  </el-tabs>
</template>

<script>
import ClassInfo from './ClassInfo'
import CourseChapter from './CourseChapter'
// import CourseAttach from './CourseAttach.vue'

export default {
  name: 'CourseCreate',
  components: {
    ClassInfo,
    CourseChapter
    // CourseAttach
  },
  created() {
    this.classId = this.$route.params.id || this.$route.query.id || ''
    this.getDetail()
  },
  data() {
    let pages = [
      { id: 'ClassInfo', name: '基本信息' },
      { id: 'CourseChapter', name: '课表内容' }
      // {id: 'CourseAttach', name: '课程资料'}
    ]
    return {
      activePageName: pages[0].id,
      pages,
      classId: '',
      classInfo: {},
      componentItem: null,
      lesson: null,
      CreateVisible: true
    }
  },
  props: {
    type: {
      type: String,
      default: 'Create'
    }
  },
  methods: {
    pageChange(data) {      
      if (data.page === 'close') {
        this.CreateVisible = false
        setTimeout(() => {
          this.CreateVisible = true
        }, 500)
        this.activePageName = this.pages[0].id
        this.classInfo = {}
        return false
      }
      this.activePageName = data.page
      this.classId = data.id || ''
      this.getDetail()
    },
    async getDetail() {
      if (!this.classId) return false
      let { code, data } = await this.$api.Classes.detail({
        id: this.classId
      })
      if (code !== 200) return false
      this.classInfo = data
      console.log(this.classInfo)
    }
  }
}
</script>

<style lang="stylus" scoped>
>>> .el-tabs__content
  overflow visible
  position static
</style>