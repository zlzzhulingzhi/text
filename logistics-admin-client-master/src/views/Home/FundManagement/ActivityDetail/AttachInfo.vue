<!--课程基本信息-->
<template>
  <el-card>
    <div class="text-bold font-14 line-height-52">附件资料</div>

    <el-table class="margin-bottom-16" :data="tableData" :height="pageInfo.tableHeight" v-loading="loading.page">
      <el-table-column label="材料名称" prop="name" min-width="120"></el-table-column>
      <el-table-column label="要求" prop="dec" min-width="150"></el-table-column>
      <el-table-column label="文件列表" prop="list" min-width="150">
        <template slot-scope="{row}">
          <div class="flex wrap">
            <div class="image-wrapper" v-for="(item,index) in row.list" :key="index" :class="/\.pdf$/.test(item.attachUrl) ? 'setOrder2' : 'setOrder1'">
              <!-- pdf展示 -->
              <template v-if="/\.pdf$/.test(item.attachUrl)">
                <el-image class="height-60 width-60" :src="require('@/assets/resource/doc.png')" @click="openPdfDetail(item)"></el-image>
              </template>
              <!-- 图片展示 -->
              <template v-else>
                <el-image class="height-60 flex" :src="item.attachUrl"
                          :preview-src-list="row.list.map(a=>a.attachUrl)"></el-image>
              </template>

              <!-- <el-button icon="el-icon-delete" @click="onDeleteImage(row,item,index)" v-if="!detailCSS"></el-button> -->
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="88">
        <template slot-scope="{row}">
          <!-- <el-button type="text" size="small" @click.stop="onUpload(row)">上传</el-button> -->
          <el-button type="text" size="small" @click.stop="onDownload(row.list)" v-if="row.list&&row.list.length">下载</el-button>
        </template>
      </el-table-column>
    </el-table>

    <ButtonSave v-if="type !== 'View'">
      <el-button size="medium" @click="onGoPrev">上一步</el-button>
      <el-button type="primary" size="medium" @click="onSubmit" :loading="loading.submit">完成并返回列表</el-button>
    </ButtonSave>

    <div class="height-0 overflow">
      <ImageUpload ref="ImageUpload" @input="onSaveImage"
                   :accept="'.png,.jpg,.jpeg,.gif,.webp'"
                   :autoUpload="true" :width="160" :height="160" :options="{
                     fixedNumber: [1, 1],
                     autoCropWidth: 1920,
                     autoCropHeight: 1080,
                   }">
        <div slot="tips"></div>
      </ImageUpload>
    </div>

    <!--预览-->
    <ResourcePreview ref="Preview"></ResourcePreview>
  </el-card>
</template>

<script>
import ImageUpload from '@/components/form/ImageUpload'
import ResourcePreview from '@/views/Home/FundManagement/ResourcePreview.vue'
import mxTencentCos from '@/components/mixins/mxTencentCos'

export default {
  name: 'AttachInfo',
  mixins: [mxTencentCos],
  components: {
    ImageUpload,
    ResourcePreview
  },
  props: {
    info: {
      type: Object,
      default() {
        return {}
      }
    },
    type: {
      type: String,
      default: 'View'
    }
  },
  data() {
    return {
      tableData: [
        {
          id: 'HDSQ-O-001', // attachCode
          name: '营业执照副本复印件或组织单位代码证副本复印件',
          list: []
        },
        {
          id: 'HDSQ-O-002',
          name: '活动策划方案',
          list: []
        },
        {
          id: 'HDSQ-O-003',
          name: '重要嘉宾简介',
          list: []
        },
        {
          id: 'HDSQ-O-004',
          name: '活动预算明细',
          list: []
        }
      ],

      selectRow: null,

      loading: {
        page: false,
        submit: false,
        download: false
      }
    }
  },
  computed: {
    pageInfo() {
      let pageStyles = {
        Create: {
          tableHeight: this.$utils.FullViewHeight(160)
        },
        Edit: {
          tableHeight: this.$utils.FullViewHeight(151)
        },
        View: {
          tableHeight: this.$utils.FullViewHeight(151)
        }
      }

      return {
        ...pageStyles[this.type]
      }
    }
  },
  watch: {
    info: {
      deep: true,
      immediate: true,
      handler() {
        this.onInitFormData()
      }
    }
  },
  methods: {
    // 操作 - 初始化表单数据
    async onInitFormData() {
      if (this.info && this.info.id) {
        this.loading.page = true
        let {code, data} = await this.$api.Approve.activityAttachList({
          id: this.info.id
        })
        this.loading.page = false
        if (code !== 200) return false
        this.tableData = this.tableData.map(item => {
          return {
            ...item,
            list: data.filter(a => a.attachCode === item.id)
          }
        })
      }
    },
    // 操作 - 上一步
    onGoPrev() {
      this.$emit('confirm', {
        type: 'prev',
        id: this.info.id
      })
    },

    // 操作 - 上传图片
    async onUpload(item) {
      this.selectRow = item
      this.$refs.ImageUpload.onSelectFile()
    },
    // 操作 - 保存图片
    async onSaveImage(image) {
      if (!this.selectRow || !image) return false

      let attachItem = {
        attachCode: this.selectRow.id,
        attachName: null,
        attachUrl: image,
        sort: 0
      }
      let {code} = await this.$api.ApplyQualification.attachSave({
        applyId: this.info.id,
        attachList: [attachItem]
      })

      if (code !== 200) return false

      this.$msg('上传图片')

      await this.onInitFormData()
    },
    // 操作 - 删除图片
    async onDeleteImage(row, item, index) {
      let {code} = await this.$api.ApplyQualification.attachRemove({
        id: item.id
      })

      if (code !== 200) return false

      this.$msg('删除图片')

      row.list.splice(index, 1)
    },

    // 操作 - 完成
    async onSubmit() {
      this.$emit('confirm', {
        type: 'finish',
        id: this.info.id
      })
    },

    // 进入pdf预览页面
    async openPdfDetail(data) {
      await this.$refs.Preview.open(data)
    },

    // 操作 - 下载附件
    async onDownload(list) {
      this.loading.download = true
      for (const item of list) {
        let res = item.attachUrl.split( '.' )
        console.log(res[res.length - 1])
        await this.onTencentDownLoad({
          fileName: `${item.attachCode}.${res[res.length - 1]}`,
          filePath: item.attachUrl
        })
      }
      this.loading.download = false
    },
  }
}
</script>

<style scoped lang="stylus">
.el-form
  .el-input, .el-select, .el-input-number
    width 300px

.image-wrapper
  position relative
  margin-right 8px
  margin-bottom: 4px

  .el-button
    position absolute
    right 0
    bottom 0
    padding 0
    width 18px
    height 18px
    font-size 12px

  // & + .image-wrapper
  //   margin-left 8px

.setOrder1
  order 1

.setOrder2
  order 2
</style>