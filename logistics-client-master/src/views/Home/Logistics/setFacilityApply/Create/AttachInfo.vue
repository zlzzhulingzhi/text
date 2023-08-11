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
            <div class="image-wrapper height-60" v-for="(item,index) in row.list" :key="index" :class="/\.pdf$/.test(item.attachUrl) ? 'setOrder2' : 'setOrder1'">
              <!-- pdf展示 -->
              <template v-if="/\.pdf$/.test(item.attachUrl)">
                <el-image class="height-60 width-60" :src="require('@/assets/resource/doc.png')" @click="openPdfDetail(item)"></el-image>
              </template>
              <!-- 图片展示 -->
              <template v-else>
                <el-image class="height-60 flex" :src="item.attachUrl"
                          :preview-src-list="row.list.map(a=>a.attachUrl)"></el-image>
              </template>

              <el-button icon="el-icon-delete" @click="onDeleteImage(row,item,index)" v-if="!detailCSS"></el-button>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" v-if="!detailCSS">
        <template slot-scope="{row}">
          <el-button type="text" size="small" @click.stop="onUpload(row)">上传</el-button>
        </template>
      </el-table-column>
    </el-table>

    <ButtonSave  v-if="!detailCSS">
      <el-button size="medium" @click="onGoPrev">上一步</el-button>
      <el-button type="primary" size="medium" @click="onSubmit" :loading="loading.submit">完成并返回列表</el-button>
    </ButtonSave>

    <div class="height-0 overflow">
      <ImageUpload ref="ImageUpload"
                   @input="onSaveImage"
                   :autoUpload="true"
                   :width="160" :height="160"
                   :accept="'.png,.jpg,.jpeg,.gif,.webp,.pdf'"
                   :fileSize="200"
                   :options="{
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
import ResourcePreview from '@/views/Home/Allowance/ResourcePreview.vue'

export default {
  name: 'AttachInfo',
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
      default: 'Create'
    }
  },
  data() {
    return {
      tableData: [
        {
          id: 'PXSQ-O-001', // attachCode
          name: '国家网安基地培训中心入驻机构需求清单',
          dec: '入驻机构必须按照国家网安基地培训中心入驻机构需求清单表格，填写详细信息并盖章后上传',
          list: []
        },
        // {
        //   id: 'PXSQ-O-002', // attachCode
        //   name: '附件清单2',
        //   list: []
        // },
        // {
        //   id: 'PXSQ-O-003', // attachCode
        //   name: '附件清单3',
        //   list: []
        // },
        
      ],

      selectRow: null,

      loading: {
        page: false,
        submit: false
      },

      // 用以区分 详情页 和 另外两个页面
      detailCSS: false
    }
  },
  computed: {
    pageInfo() {
      let pageStyles = {
        Create: {
          tableHeight: this.$utils.FullViewHeight(160)
        },
        Edit: {
          tableHeight: this.$utils.FullViewHeight(212)
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
      if (this.info && this.info.applyId) {
        // 详情页 修改 detailCSS
        if(this.type === 'View') this.detailCSS = true

        this.loading.page = true
        let {code, data} = await this.$api.AllowanceApply.attachList({
          id: this.info.applyId
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
        id: this.info.applyId
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
      let {code} = await this.$api.AllowanceApply.attachSave({
        applyId: this.info.applyId,
        attachList: [attachItem]
      })

      if (code !== 200) return false

      this.$msg('上传')

      await this.onInitFormData()
    },

    // 操作 - 删除图片
    async onDeleteImage(row, item, index) {
      let {code} = await this.$api.AllowanceApply.attachRemove({
        id: item.id
      })

      if (code !== 200) return false

      this.$msg('删除')

      row.list.splice(index, 1)
    },

    // 操作 - 完成
    async onSubmit() {
      if(!this.tableData.some(item => item.list.length)) return this.$message.warning('至少上传一项附件！')
      this.$emit('confirm', {
        type: 'finish',
        id: this.info.id
      })
    },

    // 进入pdf预览页面
    async openPdfDetail(data) {
      await this.$refs.Preview.open(data)
    }
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