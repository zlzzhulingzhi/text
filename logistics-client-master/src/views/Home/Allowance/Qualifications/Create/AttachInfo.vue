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

              <el-button icon="el-icon-delete" @click="onDeleteImage(row,item,index)" v-if="type !== 'View'"></el-button>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="88" v-if="type !== 'View'">
        <template slot-scope="{row}">
          <el-button type="text" size="small" @click.stop="onUpload(row)">上传</el-button>
        </template>
      </el-table-column>
    </el-table>

    <ButtonSave v-if="type !== 'View'">
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
          id: 'ZZSQ-O-001', // attachCode
          name: '营业执照副本复印件或组织单位代码证副本复印件',
          list: []
        },
        {
          id: 'ZZSQ-O-002',
          name: '法人身份证复印件',
          list: []
        },
        {
          id: 'ZZSQ-O-003',
          name: '拥有2年以上网络安全培训经历材料',
          list: []
        },
        {
          id: 'ZZSQ-O-004',
          name: '取得良好社会效益证明',
          list: []
        },
        {
          id: 'ZZSQ-O-005',
          name: '成熟的培训方案和教材材料',
          list: []
        },
        {
          id: 'ZZSQ-O-006',
          name: '签约4人以上专职教师证明',
          list: []
        },
        {
          id: 'ZZSQ-O-007',
          name: '培训内容符合网络安全从业人员能力要求国家标准证明',
          list: []
        },
        {
          id: 'ZZSQ-O-008',
          name: '利于网络安全急缺人才培养证明',
          list: []
        },
        {
          id: 'ZZSQ-O-009',
          name: '拥有良好的招生渠道证明',
          list: []
        },
        {
          id: 'ZZSQ-O-010',
          name: '入驻或即将入驻培训中心开展培训证明',
          list: []
        },
        {
          id: 'ZZSQ-O-011',
          name: '在基地年培训规模不低于6000学时证明',
          list: []
        },
        {
          id: 'ZZSQ-O-012',
          name: '在基地年培训学员数量不低于100人证明',
          list: []
        },
        {
          id: 'ZZSQ-O-013',
          name: '其他证明材料',
          list: []
        }
      ],

      selectRow: null,

      loading: {
        page: false,
        submit: false
      }
    }
  },
  computed: {
    pageInfo() {
      let pageStyles = {
        Create: {
          tableHeight: this.$utils.FullViewHeight(210)
        },
        Edit: {
          tableHeight: this.$utils.FullViewHeight(210)
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
        let {code, data} = await this.$api.ApplyQualification.attachList({
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
        sort: this.selectRow.list.length || 0
      }
      let {code} = await this.$api.ApplyQualification.attachSave({
        applyId: this.info.id,
        attachList: [attachItem]
      })

      if (code !== 200) return false

      this.$msg('上传')

      await this.onInitFormData()
    },
    // 操作 - 删除图片
    async onDeleteImage(row, item, index) {
      let {code} = await this.$api.ApplyQualification.attachRemove({
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