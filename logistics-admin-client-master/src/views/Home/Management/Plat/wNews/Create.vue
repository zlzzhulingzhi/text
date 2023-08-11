<template>
  <el-card shadow="never" :body-style="{ padding: '16px' }">
    <div class="flex line-height-20" slot="header">
      <span class="text-bold font-14">新建资讯</span>
    </div>
    <el-form class="form" :model="formData" ref="form" :rules="rules" label-width="110px" size="medium">
      <div class="overflow-auto" :style="{height: $utils.FullViewHeight(156)}">
        <el-form-item label="资讯标题" prop="title">
          <el-input style="width:490px" v-model="formData.title" placeholder="请输入名称" :maxlength="30" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="资讯封面" prop="coverUrl">
          
            <ImageUpload class="width-300 wrap" ref="coverUrl" v-model="formData.coverUrl"
                        isEdit :autoUpload="false" :width="112" :height="63"
                        :options="{
                          fixedNumber: [16, 9],
                          autoCropWidth: 1920,
                          autoCropHeight: 1080,
                          original: true,
                        }">
                <div slot="tips" slot-scope="{data}" class="font-12 text-6 margin-left-12 overflow">
                    请选择 16:9 的图片, 支持 {{ data.accept }} 格式图片，<br/>大小不超过 {{ data.name }}
                </div>
            </ImageUpload>
        </el-form-item>
        <el-form-item label="资讯来源" prop="newsSource">
          <el-input style="width:490px" v-model="formData.newsSource" placeholder="请输入资讯来源" :maxlength="200" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="资讯内容" prop="content" :style="{ maxWidth: '1000px' }">
          <WangEditor ref="editor" :content="formData.content"></WangEditor>
        </el-form-item>
      </div>
      <div class="text-center">
        <el-button class="margin-right-14 width-96" type="primary" :loading="loading" @click="onSubmit">发布</el-button>
        <el-button class="width-96" @click="onClose">取消</el-button>
      </div>
    </el-form>
  </el-card>
</template>

<script>
import WangEditor from '@/components/upload/WangEditor.vue'
import ImageUpload from '@/components/form/ImageUpload'

const name = '资讯'

export default {
  components: {
    WangEditor,
    ImageUpload
  },
  data() {
    let {required} = this.$rules
    return {
      loading: false,
      typeMapping: {
        Create: {submitFn: this.$api.WNews.create, type: 'Create'},
        Edit: {submitFn: this.$api.WNews.update, type: 'Edit'}
      },
      formData: {
        id: null,
        title: null,
        coverUrl: null,
        categoryIdList: null,
        newsSource: null,
        content: '',
        summary: '',
        orgId: -1
      },
      rules: {
        title: [required],
        coverUrl: [required],
        categoryIdList: [required],
        newsSource: [required],
        content: [required]
      }
    }
  },
  computed: {
    pageInfo() {
      return this.typeMapping[this.formData.id ? 'Edit' : 'Create']
    }
  },
  created() {
    this.formData.id = this.$route.query.id || null
    this.getDetail()
  },
  methods: {
    async getDetail() {
      if (!this.formData.id) return false
      let {code, data} = await this.$api.WNews.detail({
        id: this.formData.id
      })
      if (code !== 200) return false
      this.formData.id = data.id
      this.formData.title = data.title
      this.formData.coverUrl = data.coverUrl
      this.formData.categoryIdList = data.categoryIdList && data.categoryIdList.length ? data.categoryIdList[0] : null
      this.formData.newsSource = data.newsSource
      this.formData.content = data.content
    },
    async onSubmit() {
      if (this.loading) return false
      this.formData.content = this.$refs.editor.getContent()
      this.formData.summary = this.$refs.editor.getText().substr(0, 300)
      this.formData.coverUrl = await this.$refs.coverUrl.uploadFile()
      this.$refs.form.validate(async valid => {
        if (!valid) {
          return false
        }
        this.loading = true
        let {code} = await this.pageInfo.submitFn({
          ...this.formData,
          categoryIdList: [this.formData.categoryIdList]
        })
        this.loading = false
        if (code !== 200) return false
        this.$msg[this.pageInfo.type](name)
        this.onClose()
      })
    },
    onClose() {
      this.$router.back()
    }
  }
}
</script>

<style lang="stylus" scoped>
.el-card
  border none

  .el-form
    margin-left 32px

    .el-input, .el-select, .el-input-number, .vue-treeselect
      width 450px

    /deep/ .el-input__inner[maxlength]
      padding-right 70px

    .collapse-content
      max-width 500px
      margin-top -22px

      .el-tree
        /deep/ .el-tree-node__content
          height 36px
</style>