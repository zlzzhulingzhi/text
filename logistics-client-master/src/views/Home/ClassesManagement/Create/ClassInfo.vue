<!--课程基本信息-->
<template>
  <el-card :style="{height: $utils.FullViewHeight(43)}">
    <div class="flex" slot="header">
      <span class="text-bold font-14">基本信息</span>
      <!-- <el-alert class="flex-1 margin-left-32 margin-right-100" title="请遵守国家相关法律法规，切勿上传政治、色情、暴恐、诈骗、侵权等内容。" type="error" :closable="false"></el-alert> -->
    </div>

    <!--班级创建、编辑-->
    <el-form ref="form" :model="formData" :rules="rules" label-width="160px" size="small" v-loading="loading.page">

      <el-form-item prop="name" label="班级名称">
        <el-input class="width-400" type="text" show-word-limit maxlength="20"
                  v-model.trim="formData.name"></el-input>
      </el-form-item>

      <el-form-item prop="courseName" label="专题名称">
        <el-input class="width-400 margin-right-8" v-model="formData.courseName" disabled></el-input>
        <el-button type="primary" @click="onSelectCourse">选择专题</el-button>
      </el-form-item>

      <el-form-item prop="employeeId" label="负责教师">
        <el-select class="width-400" v-model="formData.employeeId" filterable placeholder="请选择">
          <el-option v-for="(item, index) in lecturer" :key="index" :label="item.realName"
                      :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <div class="flex">
        <el-form-item prop="startDate" label="开课日期">
          <el-date-picker
            v-model="formData.startDate"
            type="date"
            placeholder="选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            class="width-195">
          </el-date-picker>
        </el-form-item>
        <span class="padding-left-2 padding-right-2 margin-top-6">-</span>
        <el-form-item prop="endDate" label-width="0">
          <el-date-picker
            v-model="formData.endDate"
            type="date"
            placeholder="选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            class="width-195">
          </el-date-picker>
        </el-form-item>
      </div>

      <el-form-item prop="classroomId" label="上课教室">
        <el-input class="width-400 margin-right-8" v-model="classroomInfo" disabled></el-input>
        <el-button type="primary" @click="choseClassroom">选择教室</el-button>
      </el-form-item>

      <!-- <el-form-item prop="studentNum" label="计划人数">
        <el-input-number class="width-400" v-model.trim="formData.studentNum" controls-position="right"
                          :min="0" :max="100" :step="1"></el-input-number>               
      </el-form-item> -->

      <el-form-item prop="lessonNum" label="总课时">
        <el-input-number class="width-400" v-model.trim="formData.lessonNum" controls-position="right" :min="1"
                          :max="1000" :step="1"></el-input-number>           
      </el-form-item>

      <el-form-item prop="remark" label="备注">
        <el-input class="width-400" type="textarea" rows="5" show-word-limit maxlength="100"
                  v-model="formData.remark"></el-input>
      </el-form-item>


    </el-form>

    <ButtonSave>
      <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
                    :loading="loading.submit">保存
        </el-button>
    </ButtonSave>

    <!-- 选择主题 -->
    <DialogChooseCourse ref="DialogChooseCourse" class="selectCourseSpeOut"></DialogChooseCourse>
    <!-- 选择教室 -->
    <DialogPickApplyInfo ref="DialogPickApplyInfo" class="selectClassroomOut"></DialogPickApplyInfo>
  </el-card>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import DialogChooseCourse from '@/components/dialog/ClassesManagement/DialogChooseCourse'
import DialogPickApplyInfo from '@/components/dialog/Logistics/DialogPickApplyInfo'

export default {
  name: 'ClassInfo',
  components: {
    DialogChooseCourse,
    DialogPickApplyInfo
  },
  props: {
    classInfo: {
      type: Object,
      default () {
        return {}
      }
    },
    type: {
      type: String,
      default: 'Create'
    }
  },
  data () {
    let {required} = this.$rules
    let defaultFormData = {
      name: null,
      courseName: null,
      employeeId: null,
      startDate: null,
      endDate: null,
      classroomId: null,
      remark: null,
      lessonNum: null,
      state: 1
    }
    return {
      formData: {
        ...defaultFormData
      },
      defaultFormData,
      
      rules: {
        name: [required],
        courseName: [required],
        employeeId: [required],
        startDate: [required],
        endDate: [
          required,
          {
            validator: (rule, value, callback) => {
              if (new Date(value).getTime() < new Date(this.formData.startDate).getTime()) {
                return callback(new Error('结束日期不能晚于开始日期'))
              }

              callback()
            },
            trigger: ['change', 'blur']
          }
        ],
        lessonNum: [required]
      },

      lecturer: [],

      numInputStuShow: false,
      numInputLesShow: false,

      // 选中的教室
      classroomInfo: null,

      loading: {
        page: false,
        submit: false
      },

      typeMapping: {
        Create: {
          title: '新增班级', submitAPI: this.$api.Classes.add, msgText: '新增班级'
        },
        Edit: {
          title: '编辑班级', submitAPI: this.$api.Classes.update, msgText: '编辑班级'
        }
      },
      
    }
  },
  created() {
    this.initData()
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    dialogInfo() {
      return this.typeMapping[this.classInfo.id ? 'Edit' : 'Create']
    },

    // 提交表单数据
    submitFormData() {
      let clazzId = undefined
      // 编辑时，state取null
      if(this.classInfo.id) {
        this.formData.state = null
        clazzId = this.classInfo.id
      }

      return {
        orgId: this.userInfo.orgId,
        ...this.formData,
        id: clazzId
      }
    }
  },
  watch: {
    classInfo: {
      deep: true,
      handler() {
        this.disposeEditData()
      }
    }
  },
  methods: {
    // 操作 - 初始化数据
    async initData(data) {
      // 获取讲师信息
      await this.getLecturers()

      this.disposeEditData()

    },

    // 处理编辑时的数据
    disposeEditData() {
      if(this.type === 'Edit'&&this.classInfo.name) {
        this.formData.name = this.classInfo.name,
        this.formData.courseName = this.classInfo.courseName,
        this.formData.courseId = this.classInfo.courseId,
        this.formData.employeeId = this.classInfo.employeeId,
        this.formData.startDate = this.classInfo.startDate
        this.formData.endDate = this.classInfo.endDate
        this.formData.lessonNum = this.classInfo.lessonNum

        if(this.classInfo.classroomId) {
          this.formData.classroomId = this.classInfo.classroomId,
          // this.classroomInfo = `${this.classInfo.building}${this.classInfo.roomNo}(${this.classInfo.roomType})`
          this.classroomInfo = this.classInfo.roomNo
        }

        this.formData.remark = this.classInfo.remark

      }
    },

    // 获取 - 讲师信息
    async getLecturers() {
      let {code, data} = await this.$api.Lecturers.lecturersList({
        orgId: this.userInfo.orgId
      })
      if (code !== 200) return false
      this.lecturer = data
    },

    // 操作 - 打开选择课程的弹窗
    async onSelectCourse() {
      let course = await this.$refs.DialogChooseCourse.open()
      this.formData.courseName = course.courseName
      this.formData.courseId = course.id
    },

    // 打开  选择教室的弹窗
    async choseClassroom () {
      let classRoom = await this.$refs.DialogPickApplyInfo.open({
        type: 'choseClassroom',
        isMultipleChose: false
      })
      // this.classroomInfo = `${classRoom.building}${classRoom.roomNo}(${classRoom.roomType})`
      this.classroomInfo = classRoom.roomNo
      this.formData.classroomId = classRoom.id
    },

    // 提交保存
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true
      let {code, data} = await this.dialogInfo.submitAPI(this.submitFormData)
      // let {code, data} = await this.$api.Classes.add(this.submitFormData)
      this.loading.submit = false
      if(code !== 200) return false

      this.$msg(this.dialogInfo.msgText)

      this.$emit('page-change', {
        page: 'CourseChapter',
        id: data.id
      })
    }
  }
}
</script>

<style lang="stylus" scoped>
  // 日期选择器
  >>>.width-195
    width 195px
</style>

<style lang="stylus">
.selectClassroomOut
  .selectClassroom
    margin-top: 3vh !important
.selectCourseSpeOut
  .selectCourseSpe
    margin-top: 8vh !important
</style>
