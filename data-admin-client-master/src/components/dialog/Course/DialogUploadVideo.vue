<template>
  <el-dialog :title="fileType | filterTypeName" :visible.sync="dialogVisible" width="70%" :close-on-click-modal="false" append-to-body :before-close="uploadHandleClose">
    <div class="upload-content">
      <el-form ref="form" label-width="80px">
        <el-form-item label="选择分类">
          <el-cascader :disabled="this.isUploadHint" ref="cascader" v-model="selectedList" :props="{value: 'id', label: 'name', checkStrictly: true}" :options="resCategoryTree" clearable></el-cascader>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" row-key="id">
        <template v-slot:empty>
          <div class="empty-table-content">
            <div>
              <div class="empty-icon el-icon-upload"></div>
              <div style="overflow:hidden;">
                <input :accept="Regular" class="table-upload" type="file" @change="onUpload" multiple="multiple" value="点击上传" />
                <div class="empty-table-text" type="primary">
                  将文件拖到此处，或<span style="color: #1D61F2; font-weight: 600;">&nbsp;选择文件</span>
                </div>
              </div>
              <div class="empty-table-tooltip">{{ tooltip }}</div>
            </div>
          </div>
        </template>
        <el-table-column prop="name" label="文件名称" width="400">
          <template slot-scope="{ row }">
            <!-- <img src=""> -->
            <span class="file-name">
              {{ row.name }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="size" label="大小">
          <template slot-scope="{ row }">
            {{ row.size | fileSize }}
          </template>
        </el-table-column>
        <el-table-column prop="videoTime" label="时长" v-if="(fileType === 'video') || (fileType === 'audio')">
          <template slot-scope="{ row }">
            {{ row.videoTime | timeConversion }}
          </template>
        </el-table-column>
        <el-table-column prop="schedule" label="进度">
          <template slot-scope="{ row }">
            <el-progress :stroke-width="5" :color="customColors" :percentage="row.schedule" v-if="row.schedule !== 100"></el-progress>
            <span v-else style="color: #13C97F; ">
              <span style="color: #13C97F; margin-right: 4px;">{{ row.schedule }}%</span>
              <span class="el-icon-circle-check" style="font-weight: 600;"></span>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="speed" label="速度"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="{ row, $index }">
            <el-button type="text" :disabled="isUploadHint || row.isCancel" @click="UploadName(row)">编辑</el-button>
            <el-button type="text" v-if="!row.isCancel" @click="UploadCancel(row, $index)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="sureUpload" :disabled="isUploadHint">开始上传</el-button>
      <el-button @click="uploadHandleClose">关 闭</el-button>
      <!-- <el-button type="primary" @click="uploadHandleClose">确 定</el-button> -->
    </span>
  </el-dialog>
</template>

<script>
import flvjs from 'flv.js/dist/flv.js'
import upload from '@/lib/upload.js'
import CosRequest from '@/lib/cos.js'
import { mapGetters } from 'vuex'

export default {
  data () {
    return {
      cos: null,
      dialogVisible: false,
      uploadList: [],
      isUploadHint: false,
      isUploadHintNum: 0,
      uploadParams: {
        categoryIds: [], // 分类id
        createBy: 0, // 创建人
        deleted: 0, // 状态码0正常, 1为删除
        fileName: '', // 文件名称
        filePath: '', // 文件路径
        fileSize: 0, // 文件大小
        fileType: '', // 文件类型
        id: 0, // 文件id
        length: 0, // 文件时长,  仅限于音频视频
        thumbnailUrl: '', // 略缩图地址
        remark: ''
      },
      tooltip: '',
      Regular: [],
      tableData: [],
      obsClient: null,
      uploadFileNum: 0,
      main: {
        enabled: {
          label: '资源类型',
          options: []
          // options: this.$store.state.common.enabled
        }
      },
      options: [],
      value: '',
      form: '',
      fileType: '',
      customColors: [{ color: '#1D61F2', percentage: 100 }],
      selectedList: [],
      isDisabled: true
    }
  },
  computed: {
    ...mapGetters('common', {
      resCategoryTree: 'resCategoryTree'
    })
  },
  methods: {
    // 确定上传
    async sureUpload () {
      // if (this.selectedList.length == 0) return this.$message.error('请选择分类，分类不能为空')
      if (this.uploadList.length == 0) return this.$message.error('请选择需要上传的文件')
      this.isUploadHint = true
      let cos = new CosRequest()
      this.uploadList.forEach((item, index) => {
        item.cos = cos
        cos.sliceUploadFile({
          file: item.file,
          onProgress: (progressData) => {
            item.schedule = Math.floor(progressData.percent * 100)
            let speed = parseInt(progressData.speed / 1024 / 1024 * 100) / 100
            if (Math.floor(progressData.percent * 100) == 100) {
              item.speed = '完成'
              item.isCancel = true
              if (index == this.isUploadHintNum) {
                this.uploadList = []
                this.isUploadHint = false
              }
            } else {
              item.speed = speed + 'Mb/s'
            }
          },
          onCancelTask: (tid) => {
            item.cancelTaskId = tid
          },
          onSuccess: (url) => {
            this.settingUpload(url, item)
          },
          onError: (err) => {
            this.$message.error(err)
          }
        })
      })
    },
    // 取消上传文件
    async UploadCancel (row, rowIndex) {
      await this.$confirm(`是否取消${row.name}文件上传？`, { title: '关闭窗口' })
      if (row.cancelTaskId) {
        row.cos.cancelTasks(row.cancelTaskId)
        this.isUploadHintNum = this.uploadList.length - 1
        this.uploadList.splice(rowIndex, 1)
        if (rowIndex == 0 && this.uploadList.length == 0) {
          this.uploadList = []
          this.isUploadHint = false
        }
      }
      this.tableData.forEach((item, index) => {
        if (item.name === row.name) {
          return this.tableData.splice(index, 1)
        }
      })
    },
    // settingUpload(result, fileObj) {
    //   ++this.uploadFileNum
    //   // this.uploadParams.categoryIds = [this.selectedList[this.selectedList.length - 1]]; // 分类id
    //   this.uploadParams.categoryIds = [...this.selectedList]
    //   this.uploadParams.deleted = 0 // 状态码0正常, 1为删除
    //   this.uploadParams.fileName = fileObj.name // 文件名称
    //   this.uploadParams.filePath = result
    //   this.uploadParams.fileSize = fileObj.size // 文件大小
    //   this.uploadParams.fileType = this.fileType // 文件类型
    //   this.uploadParams.remark = fileObj.remark
    //   if (fileObj.videoTime !== '00:00') this.uploadParams.length = fileObj.videoTime // 文件时长,  仅限于音频视频
    //   this.uploadParams.thumbnailUrl = '' // 略缩图地址
    //   this.$nextTick(async () => {
    //     let {code, data} = await this.$api.ResourceFile.upload([this.uploadParams])
    //     if (code !== 200) return false
    //     if (this.uploadFileNum == this.tableData.length) {
    //       this.$message.success(`文件上传成功`)
    //       this.$emit('success', data)
    //     }
    //   })
    // },

    // 修改上传的文件名称
    UploadName (row) {
      this.$prompt('修改素材名称', '修改名称', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: row.name,
        inputErrorMessage: '素材名称不能为空',
        inputPattern: /\S/
      }).then(({ value }) => {
        this.$message.success(`修改素材名为:${value}`)
        row.name = value
      })
    },
    // 打开上传窗口
    show (type, { selection }) {
      this.dialogVisible = true
      if (selection && selection.length) this.selectedList = selection
      this.fileType = type
      let { tooltips, Regular } = upload.filterType(type)
      this.tooltip = tooltips
      this.Regular = Regular
    },

    // 点击上传按钮传入上传文件
    async onUpload (e) {
      const fileList = e.srcElement.files
      if (fileList.length > 100) return this.$message.error(`上传文件请少于100个`)
      const newFileList = await this.filterTypeAction(fileList)
      newFileList.forEach(item => {
        // 这里由于file对象和普通对象不一样，vue2.X版本没有在file对象里写setter和getter所以不会触发视图更新，所以要这么写
        let newType = item.name.split('.')[item.name.split('.').length - 1]
        let fileItem = {
          name: item.name,
          lastModified: item.lastModified,
          lastModifiedDate: item.lastModifiedDate,
          size: item.size,
          type: item.type,
          webkitRelativePath: item.webkitRelativePath,
          speed: '--',
          schedule: 0,
          videoTime: '00:00',
          file: item,
          remark: newType
        }
        // 视频文件上传获取视频时长
        this.getVideoDuration(item, fileItem)
        this.tableData.push(fileItem)
        this.uploadList.push(fileItem)
      })

      this.isUploadHintNum = this.uploadList.length
    },
    // 过滤文件
    async filterTypeAction (fileList) {
      let newFilteList = []
      let errFilteList = []
      await Object.keys(fileList).forEach(item => {
        // 判断格式是否正确且大小不超过5G  1G/1073741824   5G/ 5368709120
        if (this.Regular.includes(fileList[item].type) && fileList[item].size < 5368709120) {
          newFilteList.push(fileList[item])
        } else {
          errFilteList.push(fileList[item].name)
        }
      })
      if (errFilteList.length >= 1) {
        this.$message({
          showClose: true,
          message: `此:${errFilteList.join('、')}格式不正确或过大,请上传正确的格式或小于5G文件。`,
          type: 'error',
          duration: 0
        })
      }
      // this.$message.error(`此:${errFilteList.join('、')}格式不正确或过大,请上传正确的格式或小于5G文件。`);
      return newFilteList
    },
    // 关闭上传接口
    async uploadHandleClose () {
      if (this.uploadList.some(item => item.schedule !== 100)) {
        await this.$confirm(`文件正在上传，确定关闭吗？`, { title: '关闭窗口' })
      }
      this.isUploadHint = false
      this.dialogVisible = false
      this.selectedList = []
      this.tableData = []
      this.uploadFileNum = 0
      this.uploadList.forEach(async item => {
        if (item.cancelTaskId) {
          item.cos.cancelTasks(item.cancelTaskId)
        }
      })
      this.uploadList = []
      // await this.$parent.getfileList()
      this.$emit('onClose')
    },
    // 获取视频时长
    async getVideoDuration (file, fileObj) {
      let url = await URL.createObjectURL(file)
      let audioElement = null
      let flvPlayerObjM1 = null
      if (file.type === 'video/x-flv') {
        flvPlayerObjM1 = await flvjs.createPlayer({
          type: 'flv',
          isLive: true, //是否直播
          url: url, //地址
          hasAudio: true,
          hasVideo: true
        })
        audioElement = await new Audio()
        await flvPlayerObjM1.attachMediaElement(audioElement)
        await flvPlayerObjM1.load()
      } else {
        audioElement = await new Audio(url)
      }
      await audioElement.addEventListener('loadedmetadata', () => {
        // 视频时长值的获取要等到这个匿名函数执行完毕才产生
        let result = audioElement.duration //得到时长为秒，小数
        fileObj.videoTime = parseInt(result)
        if (file.type === 'video/x-flv') flvPlayerObjM1.destroy()
      })
    }
  }
}
</script>

<style lang="stylus">
.el-cascader-node
  .el-radio
    .el-radio__input
      .el-radio__inner
        border-radius 0 !important
        &::after
          box-sizing content-box
          content ''
          border 1px solid #fff
          border-left 0
          border-top 0
          height 7px
          left 4px
          position absolute
          top 1px
          transform rotate(45deg) scaleY(0)
          width 3px
          transition transform 0.15s ease-in 0.05s
          transform-origin center
          border-radius 0
          background-color MAIN_COLOR
      &.is-checked .el-radio__inner::after
        transform rotate(45deg) scaleY(1)
</style>

<style scoped lang="stylus">
.file-name
  display inline-block
  width 400px
  white-space nowrap
  text-overflow ellipsis
  overflow hidden
  vertical-align bottom
.is-position
  position absolute
.now-upload
  display flex
  justify-content space-between
  margin-bottom 5px
.upload-btn
  height 32px
  width 96px
  padding 0
  position absolute
  z-index 0
.show-upload
  position absolute
  z-index 1
  opacity 0
  width 96px
  height 36px
.table-upload
  position absolute
  z-index 1
  opacity 0
  top 0
  left 0
  width 100%
  height 100%
.upload-form
  height 36px
  position relative
.upload-tooltip
  position absolute
  left -30px
  top 16px
  font-size 20px
  cursor pointer
.upload-num
  line-height 0
.empty-table-content
  display flex
  justify-content center
  .empty-icon
    font-size 50px
    margin-top 64px
.empty-table-text
  line-height 1
.empty-table-tooltip
  flex-flow nowrap
  width 600px !important
</style>

