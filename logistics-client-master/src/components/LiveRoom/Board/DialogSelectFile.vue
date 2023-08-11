<template>
  <el-dialog :visible.sync="visible" append-to-body title="文件列表"
             :before-close="close" width="800px">

    <TableView :options="options" :params.sync="filterData">
      <div class="margin-bottom-12 flex between-center">
        <el-button type="primary" size="small" @click="onCreate">从资源库添加课件</el-button>
      </div>
      <!--      <template v-slot:footer>
              <el-button size="small" :disabled="!selectionList.length" @click="onBatchDelete">批量移除</el-button>
            </template>-->

      <el-table v-loading="loading.table" :data="tableData" :height="$utils.FullViewHeight(350)"
                @selection-change="selectionList = $event.map(item => item.id)">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>
        <!--        <el-table-column type="selection" width="50"></el-table-column>-->
        <el-table-column label="文档名称" prop="resourceFileName" min-width="120">
          <template slot-scope="{row}">
            {{ String(row.resourceFileName).split('.')[0] }}
          </template>
        </el-table-column>
        <el-table-column label="文档类型" prop="resourceFileType" width="100"></el-table-column>
        <el-table-column label="文档大小" prop="resourceFileSize" width="100">
          <template slot-scope="{row}">
            {{ row.resourceFileSize | fileSize }}
          </template>
        </el-table-column>
        <el-table-column label="添加时间" prop="createTime" width="150"></el-table-column>
        <el-table-column label="操作" width="110" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="small" disabled key="disabled"
                       v-if="judgeIsAdded(row)">
              已添加
            </el-button>
            <el-button v-else type="text" size="small" @click="onAddFile(row)">添加</el-button>
            <!--            <el-button type="text" size="small" @click="onDelete(row)">删除</el-button>-->
          </template>
        </el-table-column>
      </el-table>

      <ResourcePreview ref="ResourcePreview"></ResourcePreview>
    </TableView>

    <DialogSelectResource ref="DialogSelectResource"></DialogSelectResource>
  </el-dialog>
</template>

<script>
import ResourcePreview from '@/views/Home/Education/Resource/components/ResourcePreview'
import DialogSelectResource from '@/components/dialog/Live/DialogSelectResource'

export default {
  name: 'DialogSelectFile',
  components: {
    ResourcePreview,
    DialogSelectResource
  },
  data() {
    return {
      visible: false, // 弹窗开关
      teduBoard: null,
      fileList: [],

      options: {
        total: 0
      },
      filterData: {},

      tableData: [],
      selectionList: [],

      timer: null,

      loading: {
        submit: false,
        table: true,
        upload: false
      }
    }
  },
  computed: {
    outputData() {
      return {}
    },
    params() {
      return {
        basicLiveId: this.$route.params.id,
        ...this.filterData
      }
    }
  },
  watch: {
    params: {
      deep: true,
      handler() {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    }
  },
  methods: {
    // 打开弹窗
    async open(data = {}) {
      this.visible = true
      this.teduBoard = data.teduBoard
      this.getFileInfoList()
      clearTimeout(this.timer)
      this.timer = setTimeout(() => {
        this.getTableData()
      }, 300)
      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.outputData)
          reject('取消')
        })
      })
    },

    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle', 'close')
    },
    // 确定提交
    async onSubmit() {
      this.visible = false
      this.$emit('handle', 'submit')
    },

    // 操作 - 获取文件列表
    getFileInfoList() {
      this.fileList = this.teduBoard.getFileInfoList()
    },

    judgeIsAdded(item) {
      return this.fileList.find(file => {
        if (!file.downloadURL) return false
        let itemPath = null
        if (/^(ppt(x)?)$/.test(item.resourceFileType)) {
          // ppt
          itemPath = item.resourceFilePath
        } else {
          if (item.transcodeResult) {
            let info = JSON.parse(item.transcodeResult)
            itemPath = info.resultUrl
          }
        }

        return file.downloadURL.includes(itemPath) && file.title === item.resourceFileName
      })
    },

    // 操作 - 上传文件
    async onAddFile(item) {
      let fid = null
      if (/^(ppt(x)?)$/.test(item.resourceFileType)) {
        // ppt
        fid = this.teduBoard.addTranscodeFile({
          title: item.resourceFileName,
          url: `https:${item.resourceFilePath}?for_tiw=1`
        })
      } else {
        // pdf、doc
        try {
          let info = JSON.parse(item.transcodeResult)
          fid = this.teduBoard.addTranscodeFile({
            url: info.resultUrl,
            resolution: info.resolution,
            pages: info.pages,
            title: item.resourceFileName
          })
        } catch (e) {
          this.$message.warning('该文件正在转码中')
          return await this.getTableData()
        }
      }

      this.teduBoard.switchFile(fid)
      await this.onSubmit()
      this.$emit('select')
    },

    // 列表
    async getTableData() {
      if (!this.params) return
      this.loading.table = true
      let {code, data} = await this.$api.BasicLiveAttachment.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    // 操作 - 添加课件
    async onCreate() {
      await this.$refs.DialogSelectResource.open({
        type: 'LiveCourseware',
        submitParams: {
          basicLiveId: this.params.basicLiveId
        }
      })
      await this.getTableData()
    },
    // 操作 - 删除
    async onDelete({id}) {
      await this.$confirm(`确认删除直播课件吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.BasicLiveAttachment.delete({idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete('直播课件')
      await this.getTableData()
    },
    // 操作 - 批量删除
    async onBatchDelete() {
      await this.$confirm(`确认删除${this.selectionList.length}个直播课件吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.BasicLiveAttachment.delete({idList: this.selectionList})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete('直播课件', true)
      await this.getTableData()
    },
    onView(item) {
      this.$refs.ResourcePreview.open({
        filePath: item.resourceFilePath,
        fileType: item.resourceFileType
      })
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-dialog__body
  padding 12px 16px
</style>