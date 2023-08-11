<!--活动基本信息-->
<template>
  <el-card shadow="never" :body-style="{ padding: '16px' }">
    <div  slot="header" v-if="type === 'Detail'">
      <span class="text-bold font-14">活动详情</span>
    </div>
    <div class="flex" slot="header" v-else>
      <span class="text-bold font-14">基本信息</span>
      <el-alert class="flex-1 margin-left-32 margin-right-100" title="请遵守国家相关法律法规，切勿上传政治、色情、暴恐、诈骗、侵权等内容。" type="error"
                :closable="false"></el-alert>
    </div>

    <div ref="formWrapper" class="overflow-auto flex center-start relative"
         :style="{ height: $utils.FullViewHeight(isHeight) }">
      <el-form :model="form" ref="form" :rules="rules" label-width="120px" size="medium">
        <el-form-item label="活动名称" v-if="type === 'Detail'">
          <span>{{form.title}}</span>
        </el-form-item>
        <el-form-item label="活动名称" prop="title" v-else>
          <el-input v-model="form.title" placeholder="请输入名称" :maxlength="30" show-word-limit></el-input>
        </el-form-item>
        

        <el-form-item label="活动时间" prop="" v-if="type === 'Detail'">
          <span>{{ form.activityTime }}</span>
        </el-form-item>
        <el-form-item label="活动时间" prop="activityTime" v-else>
          <el-date-picker
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm:00"
            v-model="form.activityTime"
            type="datetime"
            placeholder="选择日期时间"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>


        <el-form-item label="活动封面" prop="coverUrl">
          <el-image v-if="type === 'Detail'" :src="form.coverUrl" style="width: 200px; height: 111px" fit="cover" :preview-src-list="[form.coverUrl]"></el-image>
          <ImageUpload
              v-else
              v-model="form.coverUrl"
              isEdit
              :width="200"
              :height="111"
              :options="{
                         fixedNumber: [16, 9],
                         autoCropWidth: 1920,
                         autoCropHeight: 1080,
                        }"
          >
            
            <div
              slot="tips"
              slot-scope="{ data }"
              class="font-12 text-6 margin-left-12"
            >
              <div class="width-240 break-all line-height-20">
                请选择16: 9比例的图片，支持{{
                  data.accept.replace(/,/g, '、')
                }}格式，大小不超过{{ data.name }}
              </div>
              <div>可选封面</div>
              <div class="flex">
                <img
                  class="width-64 margin-right-4 pointer bor-radius"
                  v-for="(item, index) in (isProduction ? coverListPro : coverList)"
                  :key="index"
                  :src="item.src"
                  @click="togglePic(item)"
                 />
              </div>
            </div>
          </ImageUpload>
        </el-form-item>
        
        <el-form-item label="排序" prop="sort">
          <span v-if="type === 'Detail'">{{form.sort}}</span>
          <el-input-number v-model="form.sort" size="medium" :min="1" :max="999" :step="1" controls-position="right" v-else></el-input-number>
        </el-form-item>
        <el-form-item label="活动简介" prop="introduction" :style="{ maxWidth: '1000px' }">
          <div v-html="form.introduction" v-if="type === 'Detail'"></div>
          <WangEditor ref="editor" :content="form.introduction" v-else></WangEditor>
        </el-form-item>
       
      </el-form>
    </div>
    <!-- <DialogAddStudent ref="DialogAddStudent" @success="selectStudentOk"></DialogAddStudent> -->
    <!-- <FormSaveBar :isCancel="false" :loading="loading" @submit="onSubmit" @cancel="$router.back()"></FormSaveBar> -->
    <ButtonSave>
      <el-button class="width-96 margin-right-6" type="primary" size="medium" @click="$router.push({ name: 'Activity'})"
                 :loading="loading.submit"  v-if="type === 'Detail'">
        返回
      </el-button>
      <el-button class="width-96 margin-right-6" type="primary" size="medium" @click="onSubmit"
                 :loading="loading.submit" v-else>
        保存
      </el-button>
    </ButtonSave>
    <!-- <DialogAddDepartment ref="DialogAddDepartment"></DialogAddDepartment>
    <DialogAddLabel ref="DialogAddLabel"></DialogAddLabel> -->
  </el-card>
</template>

<script>
import ImageUpload from '@/components/form/ImageUpload.vue'
// import DialogAddStudent from '@/components/dialog/Course/DialogAddStudent.vue'
import WangEditor from '@/components/upload/WangEditor.vue'

// import DialogAddDepartment from '@/components/dialog/Course/DialogAddDepartment.vue'
// import DialogAddLabel from '@/components/dialog/Course/DialogAddLabel.vue'
import {mapGetters, mapState} from 'vuex'

const name = '活动'

export default {
  name: 'ActivityInfo',
  components: {
    ImageUpload,
    // DialogAddStudent,
    // DialogAddDepartment,
    // DialogAddLabel,
    WangEditor
  },
  props: {
    ActivityInfo: {
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
    let {required} = this.$rules
    return {
      isItFree: 0,
      typeMapping: {
        Create: {
          submitFn: this.$api.Activity.create,
          // msgSubmit: this.$api.CourseRemote.notification,
          // getMsg: this.$api.CourseRemote.notificationList,
          type: 'Create'
        },
        Edit: {
          submitFn: this.$api.Activity.update,
          // msgSubmit: this.$api.CourseRemote.notification,
          // getMsg: this.$api.CourseRemote.notificationList,
          type: 'Edit'
        }
      },
      form: {
        id: null,
        title: null,
        activityTime: null,
        // categoryIds: null,
        coverUrl: null,
        // courseType: 'record',
        // signUpLimit: 0,
        // coursePrice: 0,
        // lecturers: [],
        // plan: 0,
        // userVisibleStatus: 1, // 1:所有人, 2:部分人
        // visibleUsers: [],
        sort: 1,
        introduction: '',
        // courseManage: 1,
        // signUpNum: 0,
        // playbackDrag: 0, // 0是不允许 1是允许
        // playbackSpeed: 0,
        // courseUserDeptVisibles: [],
        // courseUserGroupVisibles: [],
        // notifyChannels: [],
        // virtualStatus: 0, // 是否开启虚拟报名人数
        // virtualSignUpNum: null,  //虚拟报名人数
        // signUpAuthType: 1, // 是否开启报名密令 1-无需验证 2-密码验证
        // signUpAuthValue: null
      },
      isLimitLess: false,
      // visibleUserList: [],
      // selectedUserList: [],
       coverList: [
        {src: '//seczone-teach-test-1312651338.cos.ap-guangzhou.myqcloud.com/39/2022-08-25/0463bca05fc8bdbf13301780a7513c57.png'},
        {src: '//seczone-teach-test-1312651338.cos.ap-guangzhou.myqcloud.com/39/2022-08-25/56325e7019aec925e074eefe6d626316.png'},
        {src: '//seczone-teach-test-1312651338.cos.ap-guangzhou.myqcloud.com/39/2022-08-25/116653e6bbf0f338de1189bd1804d0d9.png'}
        
      ],
      coverListPro: [
        {src: '//wuhan-jidi-1312651338.cos.ap-guangzhou.myqcloud.com/train/common/common_course_cover_1.png'},
        {src: '//wuhan-jidi-1312651338.cos.ap-guangzhou.myqcloud.com/train/common/common_course_cover_2.png'},
        {src: '//wuhan-jidi-1312651338.cos.ap-guangzhou.myqcloud.com/train/common/common_course_cover_3.png'}
      ],
      rules: {
        
        title: [required],
        activityTime: [required],
        
      },
      checkedKeys: [],

      loading: {
        remove: false,
        submit: false
      },
      showDept: [],
      // 时间规则 - 禁选过去时间
      pickerOptions: {
        disabledDate (time) {
          return time.getTime() < Date.now() - 24*60*60*1000
        }
      }
    }
  },
  computed: {
    pageInfo() {
      return this.typeMapping[this.form.id ? 'Edit' : 'Create']
    },
    
    isHeight() {
      return this.form.id ? 234 : 200
    },
    showSignUpAuth() {
      return Boolean(this.form.signUpAuthType === 2)
    },
    ...mapGetters({
      isProduction: 'isProduction'
    }),
  },
  watch: {
    ActivityInfo: {
      deep: true,
      handler(val) {
        if (val) this.initDetail()
      }
    }
  },
  created() {
    // this.getCategory()
  },
  methods: {
    togglePic(item){
      console.log(item);
      this.form.coverUrl = item.src
    },
    radioChange () {
      this.form.visibleUsers = []
      this.form.courseUserDeptVisibles = []
      this.form.courseUserGroupVisibles = []
    },
    // 选择标签
    async onLabel() {
      // let groupIdList = this.form.courseUserGroupVisibles.map(item => item.groupId);
      let data = await this.$refs.DialogAddLabel.open({
        formData: {
          groupList: this.form.courseUserGroupVisibles
        },
        type: 'AppEdit',
        title: '活动可见指定标签'
      })
      this.form.courseUserGroupVisibles = data
      // let newDeptList = await this.$refs.DialogAddDepartment.open({
      //     formData: {
      //         deptList: this.form.courseUserDeptVisibles
      //     },
      //     title: '活动可见指定标签',
      //     type: 'AppEdit',
      //     option: { label: 'groupName', key: 'groupId' },
      // });
      // this.form.courseUserDeptVisibles = newDeptList;
    },
    // 选择组织
    async onDepartment() {
      let newDeptList = await this.$refs.DialogAddDepartment.open({
        formData: {
          deptList: this.showDept
        },
        type: 'AppEdit',
        title: '活动可见指定组织'
        // option: { children: "children", label: "deptName", key: "deptId" },
      })
      this.showDept = this.showDept.concat(newDeptList.filter((i) => i.deptName !== null))
      // this.form.courseUserDeptVisibles = this.showDept
      this.form.courseUserDeptVisibles = newDeptList.map((item) => {
        if(this.showDept ===[] && this.showDept[0].deptId !== item.deptId){
          item.deptName = null
        }
        return item
      })
    },
    async initDetail() {
      let data = this.ActivityInfo
      // FIXME 赋值重写
      this.form.id = data.id
      this.form.title = data.title
      this.form.activityTime = data.activityTime
      this.form.coverUrl = data.coverUrl
      this.form.sort = data.sort
      this.form.introduction = data.introduction
    },
    getCategory() {
      this.$store.dispatch('common/getDictionary', ['courseCategoryTreeV2'])
    },
    addCategory() {
      let route = this.$router.resolve(
        {
          name: 'CategoryManagement',
          query: {id: 'CourseCategory'}
        })
      window.open(route.href, '_blank')
    },
    getLecturer() {
      this.$store.dispatch('common/getDictionary', ['lecturerList'])
    },
    addLecturer() {
      let route = this.$router.resolve({name: 'Lecturer'})
      window.open(route.href, '_blank')
    },
    // checkChange() {
    //   let checkedNode = this.$refs.tree.getCheckedNodes(true)
    //   this.form.visibleUsers = checkedNode.map(item => {
    //     return {
    //       employeeId: item.employeeId,
    //       userId: item.userId
    //     }
    //   })
    // },
    onAddStudent() {
      if (this.form.userVisibleStatus !== 2) return
      this.$refs.DialogAddStudent.open({
        visibleUsers: this.form.visibleUsers || []
      })
    },
    selectStudentOk(list) {
      this.form.visibleUsers = this.form.visibleUsers.concat(list)
      this.$refs.form.validateField('visibleUsers')
    },
    // 组织删除
    removeDept(item) {
      if (this.loading.remove) return false
      this.showDept.splice(this.showDept.indexOf(item), 1)
      this.form.courseUserDeptVisibles = this.form.courseUserDeptVisibles.filter((i) => !i.fullName.includes(item.deptName))
    },
    // 标签删除
    removeGroup(item) {
      if (this.loading.remove) return falseonSubmit
      this.form.courseUserGroupVisibles.splice(this.form.courseUserGroupVisibles.indexOf(item), 1)
    },
    async removeStudent(item) {
      if (this.loading.remove) return false
      if (item.id) {
        this.loading.remove = true
        let {code} = await this.$api.Course.removeStudent({
          idList: [item.id],
          orgId: item.orgId
        })
        this.loading.remove = false
        if (code !== 200) return false
      }
      this.form.visibleUsers.splice(this.form.visibleUsers.indexOf(item), 1)
    },
    courseTypeChange(value) {
      if (value !== 'live') {
        this.form.courseManage = 1
      }
    },
    onSubmit() {
      // let lecturers = this.form.lecturers.map(item => {
      //   return {
      //     lecturerId: item.id,
      //     lecturerName: item.lecturerName,
      //     lecturerHeadImgUrl: item.headImgUrl,
      //     lecturerIntro: item.intro
      //   }
      // })
      // let visibleUsers =
      //     this.form.userVisibleStatus === 2
      //         ? this.form.visibleUsers.map(item => {
      //           return {
      //             ...item,
      //             realName: undefined,
      //             phone: undefined
      //           }
      //         })
      //         : []
      // this.form.signUpLimit = this.isLimitLess ? 0 : this.form.signUpLimit
      this.form.introduction = this.$refs.editor.getContent()
      
      this.$refs.form.validate(async valid => {
        if (!valid) {
          let errNode = this.$refs.form.$children.find(node => node.validateState === 'error')
          this.$refs.formWrapper.scrollTop = errNode.$el.offsetTop
          this.$message.warning(`${errNode.label}:${errNode.validateMessage}`)
          return false
        }
        this.loading.submit = true

        let {code, data} = await this.pageInfo.submitFn({
          ...this.form,
          // categoryIds: [this.form.categoryIds],
          // lecturers: lecturers,
          // visibleUsers: visibleUsers,
          // coursePrice: this.isItFree === 1 ? this.form.coursePrice * 100 : 0 // 单位分
        })
        this.loading.submit = false
        if (code !== 200) return false
        // let res = await this.pageInfo.msgSubmit({
        //   courseId: data.id || this.form.id,
        //   notifyChannels: this.form.notifyChannels
        // })
        // if (res.code !== 200) return false
        this.$msg[this.pageInfo.type](name)
        // if (this.pageInfo.type === 'Edit') {
        //   this.$router.back()
        // } else {
        // this.$router.replace({name: 'CourseCreate', params: {type: 'Edit', page: 'CourseChapter'}, query: {id: data.id}})
        // }
        this.$emit('page-change', {
          page: 'CourseChapter',
          id: data.id
        })
      })
    }
  }
}
</script>

<style lang="stylus" scoped>
.el-card
  border none

.el-form
  .el-input, .el-select, .el-input-number
    width 300px

  .el-tag
    padding 0 10px

  /deep/ .el-input__inner[maxlength]
    padding-right 70px

  .collapse-content
    max-width 500px
    margin-top -22px

    .el-tree
      /deep/ .el-tree-node__content
        height 36px

>>> .el-tag__close
  color: #666

.label-list {
  flex-direction: row;
  flex-wrap: wrap;
  display: flex;
  width: 100%;
  overflow: auto;
  max-height: 100%;

  .label-item {
    display: block;
    margin-right: 4px;
    margin-left: 4px;
    margin-bottom: 4px;
    margin-top: 4px;
    border-radius: 4px;
    width: calc((100% - (5px * 15)) / 8);
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    cursor pointer
  }

}

.bor-radius
  border-radius 4px


</style>
