<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false" :before-close="close" width="500px">
    <el-form :model="formData" ref="form" :rules="rules" label-width="80px" size="small">
      <el-form-item :label="`上级${dialogInfo.alias || '分类'}`" v-if="formData.parentId">
        <el-input v-model="formData.parentName" disabled></el-input>
      </el-form-item>
      <el-form-item :label="`${dialogInfo.alias || '分类'}名称`" prop="categoryName">
        <el-input v-model="formData.categoryName" :maxlength="20" show-word-limit></el-input>
      </el-form-item>

      <el-form-item label="图标" prop="categoryIconUrl" v-if="/categoryIconUrl/.test(dialogInfo.items)">
        <template>
          <ImageUpload class="width-300" ref="ImageUpload" v-model="formData.categoryIconUrl" isEdit :autoUpload="false" :width="160" :height="160" :options="{
                        fixed: false,
                        fixedBox: true,
                        centerBox: false,
                        autoCropWidth: 160,
                        autoCropHeight: 160,
                        original: false,
                        centerBox: false
                      }">
            <div slot="tips" slot-scope="{data}" class="font-12 text-6 margin-left-12">
              请选择 160 * 160 的图片, 支持 {{ data.accept }} 格式图片，大小不超过 {{ data.name }}
            </div>
          </ImageUpload>
        </template>
      </el-form-item>

      <el-form-item label="排序" prop="sort">
        <el-input-number class="width-100p" v-model="formData.sort" :min="0" :max="9999" controls-position="right"></el-input-number>
      </el-form-item>
      <el-form-item label="状态" prop="enabled">
        <el-radio-group v-model="formData.enabled">
          <el-radio v-for="item in enabledList" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <el-button class="width-80 margin-right-6" type="primary" size="small" :loading="loading" @click="onSubmit">确定
        </el-button>
        <el-button class="width-80" size="small" @click="close">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { mapState } from 'vuex'
import ImageUpload from '@/components/form/ImageUpload'

export default {
  name: 'DialogFormCategory',
  components: {
    ImageUpload
  },
  data () {
    let defaultFormData = {
      parentId: undefined,
      parentName: undefined,
      categoryName: null,
      categoryIconUrl: null,
      sort: null,
      enabled: 1
    }
    let { required } = this.$rules
    return {
      loading: false,
      visible: false,
      // 存储弹窗所需数据
      dialogType: 'AddCategory',
      typeMapping: {
        CourseCategoryCreate: {
          title: `新增分类`,
          submitFn: this.$api.CourseCategory.create,
          type: 'Create',
          items: 'categoryIconUrl'
        },
        CourseCategoryEdit: {
          title: `编辑分类`,
          submitFn: this.$api.CourseCategory.update,
          type: 'Edit',
          items: 'categoryIconUrl'
        },

        // TaskCategoryCreate: {
        //   title: `新增分类`,
        //   submitFn: this.$api.TaskCategory.create,
        //   type: 'Create',
        //   items: ''
        // },
        // TaskCategoryEdit: {
        //   title: `编辑分类`,
        //   submitFn: this.$api.TaskCategory.update,
        //   type: 'Edit',
        //   items: ''
        // },

        // QuestionCategoryCreate: {
        //   title: `新增分类`,
        //   submitFn: this.$api.Category.create,
        //   type: 'Create',
        //   items: ''
        // },
        // QuestionCategoryEdit: {
        //   title: `编辑分类`,
        //   submitFn: this.$api.Category.update,
        //   type: 'Edit',
        //   items: ''
        // },
        // PaperCategoryCreate: {
        //   title: `新增分类`,
        //   submitFn: this.$api.Category.create,
        //   type: 'Create',
        //   items: ''
        // },
        // PaperCategoryEdit: {
        //   title: `编辑分类`,
        //   submitFn: this.$api.Category.update,
        //   type: 'Edit',
        //   items: ''
        // },

        // LibraryTypeCreate: {
        //   title: `新增分类`,
        //   submitFn: this.$api.LibraryType.create,
        //   type: 'Create',
        //   items: ''
        // },
        // LibraryTypeEdit: {
        //   title: `编辑分类`,
        //   submitFn: this.$api.LibraryType.update,
        //   type: 'Edit',
        //   items: ''
        // },

        // ResourceTypeCreate: {
        //   title: `新增分类`,
        //   submitFn: this.$api.ResourceType.create,
        //   type: 'Create',
        //   items: ''
        // },
        // ResourceTypeEdit: {
        //   title: `编辑分类`,
        //   submitFn: this.$api.ResourceType.update,
        //   type: 'Edit',
        //   items: ''
        // },

        DeptCreate: {
          title: `新增组织`,
          submitFn: this.$api.Dept.create,
          type: 'Create',
          items: '',
          alias: '组织'
        },
        DeptEdit: {
          title: `编辑组织`,
          submitFn: this.$api.Dept.update,
          type: 'Edit',
          items: '',
          alias: '组织'
        }
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        categoryName: [required],
        sort: [required],
        enabled: [required]
      }
    }
  },
  computed: {
    ...mapState('common', {
      enabledList: 'enabled'
    }),
    dialogInfo () {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    submitFormData () {
      if (/^(PaperCategory|QuestionCategory|LibraryType|ResourceType)/.test(this.dialogType)) {
        return {
          ...this.formData,
          groupId: this.formData.groupId,
          name: this.formData.categoryName,
          sortNum: this.formData.sort || undefined
        }
      }

      if (/^(Dept)/.test(this.dialogType)) {
        return {
          ...this.formData,
          deptName: this.formData.categoryName
        }
      }


      return {
        ...this.formData
      }
    }
  },
  methods: {
    reset () {
      this.formData = { ...this.defaultFormData }
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open (data = {}) {
      this.reset()
      this.visible = true
      this.dialogType = data.type
      this.formData = {
        ...this.formData,
        ...data.formData
      }

      return new Promise((resolve, reject) => {
        this.$once('handle', ({ type, data }) => {
          if (type === 'success') {
            resolve(data)
          } else {
            reject('取消')
          }
        })
      })
    },
    // 关闭弹窗
    close () {
      this.visible = false
      this.$emit('handle', {})
    },
    // 提交
    async onSubmit () {
      if (this.loading) return false
      await this.$refs.form.validate()
      this.loading = true
      if (/categoryIconUrl/.test(this.dialogInfo.items)) {
        this.formData.categoryIconUrl = await this.$refs.ImageUpload.uploadFile()
      }
      let { code } = await this.dialogInfo.submitFn(this.submitFormData)
      this.loading = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.dialogInfo.title.slice(-2))
      this.$emit('handle', {
        type: 'success',
        data: this.formData
      })

      this.close()
    }
  }
}
</script>

<style scoped lang="stylus"></style>