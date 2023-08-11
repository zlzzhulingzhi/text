<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="420px">

    <el-form :model="formData" ref="form" :rules="rules" label-width="80px" size="small">

      <!-- <el-form-item label="章名称" prop="chapterName" v-if="dialogInfo.el.chapterName">
        <el-input v-model="formData.chapterName" :maxlength="20" show-word-limit></el-input>
      </el-form-item>

      <el-form-item label="章分类" prop="chapterId" v-if="dialogInfo.el.chapterId&&courseMode !== 2">
        <el-select class="width-100p" v-model="formData.chapterId" placeholder="章分类">
          <el-option v-for="item in chapterList" :key="item.chapterId" :label="item.chapterName"
                     :value="item.chapterId"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="节名称" prop="lessonName" v-if="dialogInfo.el.lessonName">
        <el-input v-model="formData.lessonName" :maxlength="20" show-word-limit></el-input>
      </el-form-item>

      <el-form-item label="排序" prop="sort" v-if="dialogInfo.el.sort">
        <el-input-number class="width-100p" v-model="formData.sort" :min="0" :max="9999"
                         controls-position="right"></el-input-number>
      </el-form-item> -->

      <el-form-item label="课程名称" prop="lessonName">
        <el-input v-model="formData.lessonName" :maxlength="20" show-word-limit></el-input>
      </el-form-item>

      <el-form-item label="排序" prop="sort">
        <el-input-number class="width-100p" v-model="formData.sort" :min="0" :max="9999"
                         controls-position="right"></el-input-number>
      </el-form-item>

      <el-form-item>
        <el-button class="width-80 margin-right-6" type="primary" size="small" @click="onSubmit"
                   :loading="loading.submit">确定
        </el-button>
        <el-button class="width-80" size="small" @click="close">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>

export default {
  name: 'DialogFormChapter',
  data() {
    let {required} = this.$rules
    // let defaultFormData = {
    //   courseId: null, // 【课程ID】
    //   deleted: 0, // 【删除状态 0 正常 1删除】
    //   introduction: null, // 【简介】
    //   sort: 0,

    //   // 章
    //   chapterName: null,
    //   lessonNum: null, // 【讲次数量】

    //   // 节
    //   chapterId: null, // 【课程章节ID】
    //   lessonName: null
    // }
    let defaultFormData = {
      lessonName: null,
      clazzId: null,
      sort: null
    }
    return {
      visible: false,

      dialogType: 'ClassesCreate',
      typeMapping: {
        // ChapterCreate: {
        //   title: '新增章', submitFn: this.$api.CourseChapter.add, type: 'Create', name: '章',
        //   el: {
        //     chapterName: true,
        //     chapterId: false,
        //     lessonName: false,
        //     sort: true,
        //   }
        // },
        // ChapterEdit: {
        //   title: '编辑章', submitFn: this.$api.CourseChapter.update, type: 'Edit', name: '章',
        //   el: {
        //     chapterName: true,
        //     chapterId: false,
        //     lessonName: false,
        //     sort: false,
        //   }
        // },
        // LessonCreateDefault: {
        //   title: '新增节', submitFn: this.$api.CourseLesson.add, type: 'Create', name: '节',
        //   el: {
        //     chapterName: false,
        //     chapterId: true,
        //     lessonName: true,
        //     sort: true,
        //   }
        // },
        // LessonCreate: {
        //   title: '新增节', submitFn: this.$api.CourseLesson.add, type: 'Create', name: '节',
        //   el: {
        //     chapterName: false,
        //     chapterId: false,
        //     lessonName: true,
        //     sort: true,
        //   }
        // },
        // LessonEdit: {
        //   title: '编辑节', submitFn: this.$api.CourseLesson.update, type: 'Edit', name: '节',
        //   el: {
        //     chapterName: false,
        //     chapterId: false,
        //     lessonName: true,
        //     sort: false,
        //   }
        // }
        ClassCreate: {
          title: '新增课程信息', submitFn: this.$api.Classes.classAdd, type: 'Create', name: '课程信息',
          el: {
            chapterName: false,
            chapterId: false,
            lessonName: true,
            sort: true,
          }
        },
        ClassEdit: {
          title: '编辑课程信息', submitFn: this.$api.Classes.classUpdate, type: 'Edit', name: '课程信息',
          el: {
            chapterName: false,
            chapterId: false,
            lessonName: true,
            sort: false,
          }
        }
      },

      defaultFormData,
      formData: {
        ...defaultFormData
      },

      rules: {
        lessonName: [required],
        sort: [required]
      },

      chapterList: [],

      loading: {
        submit: false
      },
      // 章节模式
      courseMode:null,

      dialogInfo: {}
    }
  },
  computed: {
    // dialogInfo() {
    //   return this.typeMapping[this.dialogType] || {}
    // },
    outputData() {
      return this.formData
    }
  },
  methods: {
    reset() {
      this.formData = {...this.defaultFormData}
      this.$refs.form && this.$refs.form.resetFields()
    },
    open(data) {
      // console.log(data);
      this.reset()
      this.visible = true
      this.dialogType = data.type

      this.formData.clazzId = data.formData.clazzId
      this.formData.id = data.formData.id
      this.formData.lessonName = data.formData.lessonName
      this.formData.sort = data.formData.sort

      this.dialogInfo = this.typeMapping[this.dialogType] || {}
      // this.courseMode = data.courseMode
      // this.formData = {
      //   ...this.formData,
      //   ...data.formData
      // }

      // this.chapterList = data.chapterList || []

      return new Promise((resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'success') return resolve(this.outputData)

          reject('取消')
        })
      })
    },
    // 关闭弹窗
    close() {
      this.$emit('handle', 'close')
      this.visible = false
    },
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true
      let {code} = await this.dialogInfo.submitFn(this.formData)
      this.loading.submit = false

      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.dialogInfo.name)
      this.$emit('handle', 'success')
      this.visible = false
    }
  }
}
</script>