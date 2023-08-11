<template>
  <el-dialog class="popover-dialog" :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false" :before-close="close" width="800px">

    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">
      <el-form-item prop="lessonId" label="选择课程">
        <el-select class="margin-right-8 setElSelectWidth" v-model="formData.lessonId" placeholder="选择课程" value-key="id">
          <el-option v-for="item in lessonIdList" :key="item.id" :label="item.lessonName" :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item prop="contentName" label="课时名称">
        <el-input type="text" placeholder="请输入课时名称" v-model="formData.contentName" class="width-400" maxlength="30"
  show-word-limit></el-input>
      </el-form-item>

      <div class="flex">
        <el-form-item prop="startDate" label="上课时间">
          <el-date-picker
            v-model="formData.startDate"
            type="datetime"
            placeholder="选择时间"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm"
            class="width-196">
          </el-date-picker>
        </el-form-item>
        <span class="padding-left-2 padding-right-2 margin-top-6">-</span>
        <el-form-item prop="endDate" label-width="0">
          <el-date-picker
            v-model="formData.endDate"
            type="datetime"
            placeholder="选择时间"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm"
            class="width-196">
          </el-date-picker>
        </el-form-item>
      </div>

      <el-form-item prop="classroomId" label="上课教室">
        <el-input class="width-400 margin-right-8" v-model="classroomInfo" disabled></el-input>
        <el-button type="primary" @click="choseClassroom">选择教室</el-button>
      </el-form-item>
      
      <el-form-item prop="lectureInfo" label="设置讲师">
        <el-select class="margin-right-8 setElSelectWidth" v-model="formData.lectureInfo" placeholder="选择讲师" value-key="id"
                    @focus="getLecturer">
          <el-option v-for="item in lecturerList" :key="item.id" :label="item.lecturerName" :value="item"></el-option>
        </el-select>
        <!-- <el-button type="text" size="small" @click="addLecturer">新增讲师</el-button> -->
      </el-form-item>

      <el-form-item prop="contentDescription" label="课次描述">
        <el-input type="textarea" :rows="8" placeholder="请输入课次描述" v-model="formData.contentDescription" class="width-400" maxlength="300"
  show-word-limit ></el-input>
      </el-form-item>
    </el-form>

    <template v-slot:footer>
      <el-button class="width-80 margin-right-6" type="primary" size="small" @click="onSubmit" :loading="loading.submit">确定
      </el-button>
      <el-button class="width-80" size="small" @click="close">取消</el-button>
    </template>

    <!-- 选择教室 -->
    <DialogPickApplyInfo ref="DialogPickApplyInfo" class="selectClassroomOut"></DialogPickApplyInfo>

  </el-dialog>
</template>

<script>
import {mapGetters} from 'vuex'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'
import DialogPickApplyInfo from '@/components/dialog/Logistics/DialogPickApplyInfo'

export default {
  name: 'DialogAddLessonInfo',
  mixins: [mxBaseDialog],
  components: {
    DialogPickApplyInfo
  },
  data() {
    let {required} = this.$rules

    let defaultFormData = {
      lessonId: null,
      contentName: null,
      startDate: null,
      endDate: null,
      classroomId: null,
      lectureInfo: null,
      contentDescription: null
    }
    return {
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      
      // 校验
      rules: {
        lessonId: [required],
        contentName: [required],
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
        lectureInfo: [required],
        classroomId: [required]
      },

      typeMapping: {
        create: {
          title: '新增课时', submitAPI: this.$api.Classes.classItemAdd, msgText: '新增课时信息'
        },
        edit: {
          title: '编辑课时', submitAPI: this.$api.Classes.classItemUpdate, msgText: '编辑课时信息'
        }
      },

      // 返回给打开弹窗的父组件
      outputData: true,

      // 当前课次信息
      courseInfo: {},

      // 所有节信息
      lessonIdList: [],

      // 教室信息
      classroomInfo: null
    }
  },
  computed: {
    ...mapGetters('common', {
      lecturerList: 'lecturerList'
    }),
    submitFormData() {
      this.formData.lecturerId = this.formData.lectureInfo.id
      this.formData.lecturerName = this.formData.lectureInfo.lecturerName
      return {
        ...this.formData,
        lectureInfo: undefined,
      }
    }
  },
  methods: {
    // 打开弹窗获取传递过来的数据
    initData(data) {
      this.classroomInfo = null
      // 编辑课次时传递过来的课次信息
      this.courseInfo = data.courseInfo
      // 外层的节信息
      this.lessonIdList = data.lessonList
      // 编辑时操作
      if(data.type === 'edit') {
        this.formData.id = this.courseInfo.id
        this.formData.contentName = this.courseInfo.contentName
        this.formData.startDate = this.courseInfo.startDate
        this.formData.endDate = this.courseInfo.endDate
        this.formData.classroomId = this.courseInfo.classroomId

        // 教室信息
        this.classroomInfo = this.courseInfo.roomNo
        
        this.formData.lectureInfo = {
          id: this.courseInfo.lecturerId,
          lecturerName: this.courseInfo.lecturerName
        }
        this.formData.contentDescription = this.courseInfo.contentDescription

      }
    },
    // 操作 - 提交
    // async onSubmit(file = {}) {
    //   let {code} = await this.$api.Course.addComponent({
    //     ...this.submitFormData,
    //     componentTypeCode : "EXTRA",
    //     componentName: this.submitFormData.extraName
    //   })
    //   if(code !== 200) return false
    //   this.$message.success("添加课次信息成功")
    //   this.$emit('success')
    //   this.close()
    // },
    // 刷新讲师信息
    getLecturer() {
      this.$store.dispatch('common/getDictionary', ['lecturerList'])
    },
    // 操作 - 新增讲师
    addLecturer() {
      this.$utils.WindowOpenInParentFrame(`/Management/Org/Lecturer`)
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
  }
}
</script>

<style lang="stylus" scoped>
.dialog-prpover {
  >>> .el-dialog {
    margin-top: 0 !important;
    position: absolute;
    top: 0;
    right: 0;

  }
}

.el-cascader, .el-select
  width 200px

// 日期选择器
>>>.width-196
  width 196px

.setElSelectWidth
  width: 400px
</style>
