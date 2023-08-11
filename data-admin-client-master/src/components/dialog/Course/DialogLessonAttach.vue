<template>
  <div>
    <el-drawer :title="`选择${fileMapping[currentTab].name}`" :visible.sync="visible" :close-on-click-modal="false" append-to-body direction="rtl" size="900px" :before-close="close">
      <div class="flex padding-left-24 padding-right-24" :style="{height: $utils.FullViewHeight(109)}">
        <TreeSelectorAsSideBar ref="TreeSelectorAsSideBar" type="ResourceAttach" :props="{label: 'name'}" :selection.sync="categorySelection" @update:selections="getList"></TreeSelectorAsSideBar>

        <div class="flex-1">
          <el-form class="margin-top-8 margin-bottom-8" :model="formData" ref="form" inline size="small">
            <!-- <el-form-item>
              <el-cascader v-model="categoryIds" :options="resCategoryTree" :props="props" collapse-tags clearable
                          @change="cascaderChange"></el-cascader>
            </el-form-item> -->
            <el-form-item>
              <el-input v-model="formData.fileName" placeholder="请输入视频名称" clearable @clear="onSearch" @keyup.native.enter="onSearch">
                <el-button slot="append" icon="el-icon-search" @click="onSearch"></el-button>
              </el-input>
            </el-form-item>
            <el-button class="margin-left-12" type="primary" size="small" icon="el-icon-upload2" @click="uploadRecord">本地上传视频</el-button>
          </el-form>
          <el-table ref="table" :height="$utils.FullViewHeight(216)" v-loading="loading" :data="listData" @selection-change="selections = $event">
            <template v-slot:empty>
              <Results></Results>
            </template>
            <el-table-column type="selection" width="55"></el-table-column>
            <!--录播-->
            <template>
              <el-table-column width="300" label="视频名称" prop="fileName"></el-table-column>
              <el-table-column label="视频时长" prop="length">
                <template slot-scope="{ row }">
                  {{ $utils.formatDuration(row.length, ['h', 'm', 's']) || '--' }}
                </template>
              </el-table-column>
              <el-table-column label="上传时间" prop="createTime"></el-table-column>
              <el-table-column label="创建人" prop="realName"></el-table-column>
            </template>
          </el-table>
          <el-pagination class="text-right margin-top-12" background :page-sizes="[10, 25, 50, 100]" layout="total, prev, pager, next, sizes" :total="pagination.total" @size-change="onSizeChange" @current-change="onCurrentChange"></el-pagination>
        </div>
      </div>
      <div class="flex center-center margin-top-32">
        <el-button class="width-80 margin-right-6" type="primary" size="small" @click="onSubmit()">确定</el-button>
        <el-button class="width-80" size="small" @click="close">取消</el-button>
      </div>
    </el-drawer>

    <DialogUploadVideo ref="DialogUploadVideo" @success="onSubmit"></DialogUploadVideo>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import TreeSelectorAsSideBar from '@/components/form/TreeSelectorAsSideBar'
import DialogUploadVideo from '@/components/dialog/Course/DialogUploadVideo'

const name = '关联课程'

export default {
  name: 'DialogLessonAttach',
  components: {
    TreeSelectorAsSideBar,
    DialogUploadVideo
  },
  data () {
    let defaultFormData = {
      categoryId: null,
      fileName: '',
      courseId: null,
      lessonId: null,
      componentId: null
    }
    return {
      loading: false,
      visible: false,
      dialogType: 'Add',
      typeMapping: {
        Add: { title: `添加${name}`, submitFn: this.$api.Course.addComponent, type: 'Create' },
        Edit: { title: `编辑${name}`, submitFn: this.$api.Course.updateComponent, type: 'Edit' }
      },
      fileMapping: {
        // record: {name: '视频', code: 'SP', listFn: this.$api.ResourceFile.pageList},
        // live: {name: '直播', code: 'ZB', listFn: this.$api.BasicLive.page}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      categoryIds: [],
      courseType: null,
      currentTab: 'record',
      listData: [],
      selections: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      props: {
        value: 'id',
        label: 'name',
        multiple: true
      },
      categorySelection: null
    }
  },
  computed: {
    dialogInfo () {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    fileInfo () {
      let fileInfo = this.fileMapping[this.currentTab]
      return fileInfo || {}
    },
    ...mapGetters('common', {
      resCategoryTree: 'resCategoryTree'
    })
  },
  methods: {
    reset () {
      this.categoryIds = []
      this.pagination.current = 1
      this.pagination.size = 10
      this.pagination.total = 0
      this.formData = { ...this.defaultFormData }
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open (data = {}) {
      this.reset()
      this.visible = true
      this.dialogType = data.type || 'Add'
      this.formData = Object.assign({}, this.formData, data.formData)
      this.courseType = data.courseType
      if (this.courseType === 'record') this.currentTab = this.courseType
      //   if (this.courseType === 'live') this.currentTab = this.courseType
      this.selectionId = data.selectionId
      await this.getList()
    },
    // 关闭弹窗
    close () {
      this.visible = false
      this.$emit('handle', 'close')
    },
    // 切换分类
    // tabChange({ name }) {
    //   this.currentTab = name
    //   this.categoryIds = []
    //   this.formData.categoryId = []
    //   this.onSearch()
    // },
    // 选择分类
    cascaderChange (value) {
      this.formData.categoryId = Array.from(new Set(value.flat(Infinity)))
      this.onSearch()
    },
    // 获取资源列表
    async onSearch () {
      this.pagination.current = 1
      await this.getList()
    },
    // 获取列表
    async getList () {
      let params = {
        ...this.pagination,
        total: undefined
      }
      //   if (this.currentTab === 'record') {
      let categoryId = []
      if (this.categorySelection && this.categorySelection.id) {
        categoryId = this.$utils.ArrayFlat([this.categorySelection]).map(item => item.id)
      }
      Object.assign(params, {
        fileType: 'video',
        categoryId,
        fileName: this.formData.fileName,
        enabled: 1
      })
      //   } else if (this.currentTab === 'live') {
      //     Object.assign(params, {
      //       liveName: this.formData.fileName,
      //       shelfStatus: 1
      //     })
      //   }
      let { code, data } = await this.fileInfo.listFn(params)
      if (code !== 200) return false
      this.listData = data.records
      this.pagination.total = data.total
    },
    onSizeChange (val) {
      this.pagination.size = val
      this.getList()
    },
    onCurrentChange (val) {
      this.pagination.current = val
      this.getList()
    },
    // 提交
    async onSubmit (file = {}) {
      if (JSON.stringify(file) !== '{}') this.selections = [file]
      if (!this.selections.length) return this.$message.warning(`请先选择${name}`)

      const res = this.selections.map(async (item, index) => {
        let component = {
          id: this.formData.componentId || undefined,
          componentTypeCode: this.fileInfo.code,
          courseId: this.formData.courseId,
          chapterId: this.formData.chapterId,
          lessonId: this.formData.lessonId,
          orgId: item.orgId,
          resourceFileId: item.id
        }
        Object.assign(component, {
          componentName: item.fileName,
          resourceFileDuration: item.length,
          resourceFileName: item.fileName,
          resourceFilePath: item.filePath,
          resourceFileSize: item.fileSize,
          resourceFileType: item.remark || item.filePath.match(/[^\.]\w*$/)[0],
          sort: index + 1
        })
        console.log('资源', JSON.stringify(component))
        return await this.dialogInfo.submitFn(component)
      })
      // result = [{code:200, ...}, {code: 200, ...}]
      let result = await Promise.all(res)

      this.$emit('success')
      this.close()
    },
    // 上传视频
    uploadRecord () {
      //   let route = this.$router.resolve({ name: 'ResourceList' })
      //   window.open(route.href, '_blank')
      this.$refs.DialogUploadVideo.show('video', {
        selection: this.categorySelection
      })
      // this.close()
    }
  }
}
</script>

<style lang="stylus" scoped>
.dialog-prpover
  >>> .el-dialog
    margin-top 0 !important
    position absolute
    top 0
    right 0
.el-cascader, .el-select
  width 200px
</style>
