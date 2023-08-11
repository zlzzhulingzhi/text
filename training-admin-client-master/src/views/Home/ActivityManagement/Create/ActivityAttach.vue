<template>
  <div>
    <!-- 表格视图 -->
    <TableView :options="options" :params.sync="filterData">
      <div class="margin-top-16 margin-bottom-16 flex end-center">
        <el-button type="primary" size="small"  icon="el-icon-plus" @click="addAttach">添加课程资料</el-button>
      </div>

      <el-table v-loading="loading" :data="listData" :height="$utils.FullViewHeight(isHeight)">
        <template v-slot:empty>
          <Results v-if='!loading'></Results>
          <span v-else></span>
        </template>
        <el-table-column label="资料名称" prop="resourceFileName"></el-table-column>
        <el-table-column label="文件类型" prop="resourceFileType">
          <template slot-scope="{row}">
            {{ (resourceTypeList.find(v => v.type === row.resourceFileType) || {}).name }}
          </template>
        </el-table-column>
        <el-table-column label="文件大小" prop="fileSize">
          <template slot-scope="{row}">
            {{ row.fileSize | fileSize }}
          </template>
        </el-table-column>
        <el-table-column label="添加时间" prop="createTime"></el-table-column>
        <el-table-column label="操作" width="110"  fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)">编辑</el-button>
            <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

    </TableView>
    <DialogCourseAttach ref="DialogCourseAttach" @success="getList"></DialogCourseAttach>
    <DialogChangeComponent ref="DialogChangeComponent" @success="getList"></DialogChangeComponent>

    <ButtonSave class="width-100p flex center-center bg-f margin-top-10 height-60">
        <el-button @click="onFowrod">上一步</el-button>
        <el-button type="primary"  @click="onBack">完成并返回课程详情</el-button>
    </ButtonSave>
  </div>
</template>

<script>
import DialogCourseAttach from '@/components/dialog/Course/DialogCourseAttach.vue'
import DialogChangeComponent from '@/components/dialog/Course/DialogChangeComponent'
import {mapState} from 'vuex'

const name = '讲义'

export default {
  components: {
    DialogCourseAttach,
    DialogChangeComponent
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
      loading: true,
      courseId: null,
      options: {
        total: 0
      },
      filterData: {},
      listData: [],
      treeData: [],
      timer: null
    }
  },
  watch: {
    courseInfo: {
      deep: true,
      handler(course) {
        if (course) this.courseId = parseInt(course.id)
      },
      immediate: true
    },
    params: {
      deep: true,
      immediate: true,
      handler() {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
          this.getList()
        }, 300)
      }
    }
  },
  created() {
    this.getTreeData()
  },
  computed: {
    isHeight() {
      return this.$route.params.type === 'Edit' ? 282 : 260
    },
    ...mapState('common', {
      resourceTypeList: 'resourceTypeList'
    }),
    params() {
      return {
        courseId: this.courseId,
        ...this.filterData
      }
    }
  },
  methods: {
    onBack() {
      window.open(`${origin}/#/Education/Course/Detail?id=${this.courseId}`,Date.now())
    },
    onFowrod() {
      console.log();
      this.$emit('page-change', {
          page: 'CourseChapter',
          id: this.courseId
      })
    },
    // 讲义分页
    async getList() {
      if (!this.courseId) return
      this.loading = true
      let {code, data} = await this.$api.CourseAttach.page(this.params)
      this.loading = false
      if (code !== 200) return false
      this.listData = data.records
      this.options.total = data.total
    },
    // 讲义分类
    async getTreeData() {
      let {code, data} = await this.$api.LibraryType.tree({
        platformType: 1
      })
      if (code !== 200) return false
      this.treeData = data
    },
    // 新增讲义
    addAttach() {
      if (!this.courseId) return this.$message.error('课程ID为空')
      this.$refs.DialogCourseAttach.open({
        type: 'Add',
        courseId: this.courseId,
        treeData: this.treeData
      })
    },
    // 编辑
    onEdit(row) {
      this.$refs.DialogChangeComponent.open({
        type: 'EditResourse',
        formData: {
          id: row.id,
          name: row.resourceFileName,
          fileName: row.resourceFileName,
          fileSize: row.fileSize,
          fileDuration: row.length
        }
      })
    },
    // 删除
    onDelete(row) {
      this.$confirm(`此操作将永久删除该${name}，是否继续？`, '提示', {
        type: 'warning'
      }).then(async () => {
        let {code} = await this.$api.CourseAttach.delete({
          idList: [row.id]
        })
        if (code !== 200) return
        this.$msg.Delete(name)
        this.getList()
      }).catch(() => {
      })
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>