<!--课程基本信息-->
<template>
  <el-card>
    <el-form ref="form" :model="formData" :rules="rules"
             label-width="136px" size="medium "
             class="padding-top-16 padding-bottom-16">
      <div ref="formWrapper" class="overflow-auto" :style="pageInfo.pageStyle" :class="{hideMustIcon: type === 'View'}">

        <div class="flex between-center margin-left-20 margin-right-20 margin-bottom-20">
          <span class="font-14 line-height-32 text-bold">基本信息</span>
          <template v-if="type !== 'View'">
            <el-button type="primary" size="small" @click="preview = false" :loading="loading.submit" v-if="preview">编辑</el-button>
          </template>
        </div>

        <div class="margin-left-16 margin-right-16 info-content" :class="preview ? '' : 'check'">
          <div class="flex wrap padding-16">
            <div class="flex-1">
              <el-form-item prop="orgName" label="机构名称" :style="{ marginBottom: preview ? '12px' : '24px' }">
                <span class="text-6" v-if="preview">{{ formData.orgName }}</span>
                <el-input class="elFormItemWidth" type="text" show-word-limit maxlength="30"
                          v-model.trim="formData.orgName" placeholder="请输入机构名称" v-else></el-input>
              </el-form-item>

              <el-form-item prop="orgCategory" label="机构性质" :style="{ marginBottom: preview ? '12px' : '24px' }">
                <span class="text-6" v-if="preview">{{ formData.orgCategory | orgCategory }}</span>
                <el-select class="elFormItemWidth" v-model="formData.orgCategory" v-else>
                  <el-option v-for="item in orgCategory" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item prop="applyDate" label="申请时间" :style="{ marginBottom: preview ? '12px' : '24px' }">
                <span class="text-6" v-if="preview">{{ formData.applyDate }}</span>
                <el-date-picker class="elFormItemWidth" type="date" placeholder="选择申请时间" v-model="formData.applyDate" :picker-options="pickerOptions" format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-else></el-date-picker>
              </el-form-item>

              <el-form-item  prop="orgIntro" label="机构简介" :style="{ marginBottom: preview ? '12px' : '24px' }">
                <span class="text-6" v-if="preview">{{ formData.orgIntro }}</span>
                <el-input class="width-input-area" type="textarea" :rows="7" show-word-limit :maxlength="300"
                        v-model="formData.orgIntro" v-else></el-input>
              </el-form-item>
            </div>
            
            <div class="flex-1">
              <el-form-item prop="legalPerson" label="法人代表" :style="{ marginBottom: preview ? '0px' : '24px' }">
                <span class="text-6" v-if="preview">{{ formData.legalPerson }}</span>
                <el-input class="elFormItemWidth" type="text" show-word-limit :maxlength="8"
                          v-model.trim="formData.legalPerson" placeholder="请输入机构法人" v-else></el-input>
              </el-form-item>

              <el-form-item prop="legalNumber" label="法人联系电话" :style="{ marginBottom: preview ? '0px' : '24px' }">
                <span class="text-6" v-if="preview">{{ formData.legalNumber }}</span>
                <el-input class="elFormItemWidth" type="text" show-word-limit :maxlength="11"
                          v-model.trim="formData.legalNumber" placeholder="请输入法人联系电话" v-else></el-input>
              </el-form-item>

              <el-form-item prop="legalEmail" label="法人联系邮箱" :style="{ marginBottom: preview ? '0px' : '24px' }">
                <span class="text-6" v-if="preview">{{ formData.legalEmail }}</span>
                <el-input class="elFormItemWidth" type="text" show-word-limit :maxlength="100"
                          v-model.trim="formData.legalEmail" placeholder="请输入法人联系邮箱" v-else></el-input>
              </el-form-item>

              <el-form-item prop="contactPerson" label="联系人姓名" :style="{ marginBottom: preview ? '0px' : '24px' }">
                <span class="text-6" v-if="preview">{{ formData.contactPerson }}</span>
                <el-input class="elFormItemWidth" type="text" show-word-limit :maxlength="8"
                          v-model.trim="formData.contactPerson" placeholder="请输入联系人姓名" v-else></el-input>
              </el-form-item>

              <el-form-item prop="contactNumber" label="联系人电话" :style="{ marginBottom: preview ? '0px' : '24px' }">
                <span class="text-6" v-if="preview">{{ formData.contactNumber }}</span>
                <el-input class="elFormItemWidth" type="text" show-word-limit :maxlength="11"
                          v-model.trim="formData.contactNumber" placeholder="请输入联系电话" v-else></el-input>
              </el-form-item>

              <el-form-item prop="contactEmail" label="联系人邮箱" :style="{ marginBottom: preview ? '0px' : '24px' }">
                <span class="text-6" v-if="preview">{{ formData.contactEmail }}</span>
                <el-input class="elFormItemWidth" type="text" show-word-limit :maxlength="100"
                          v-model.trim="formData.contactEmail" placeholder="请输入联系邮箱" v-else></el-input>
              </el-form-item>
            </div>
          </div>

          <div class="flex center-center margin-top-20 margin-bottom-24" v-if="type !== 'View'">
            <template v-if="!preview">
              <el-button class="width-80 height-36" type="primary" size="small" @click="onSubmitInfo" :loading="loading.submit">保存</el-button>
              <el-button class="width-80 height-36" size="small" @click="onCancelInfo">取消</el-button>
            </template>
          </div>
        </div>

        <div class="flex between-center margin-20" v-if="showProject">
          <div class="font-14 line-height-32 text-bold">项目计划</div>
          <template v-if="type !== 'View'">
            <el-button type="primary" size="small" @click="onAddProject()" v-if="projectList.every(v => v.preview)">新增</el-button>
          </template>
        </div>

        <div class="margin-right-32 margin-left-32 project-content" :class="item.preview ? '' : 'check'" v-for="(item,index) in projectList" :key="index" :name="index" v-if="showProject">
          <div class="flex between-center width-100p project-header">
            <div class="margin-left-12">
              <span>项目计划{{ index + 1 }}:</span>
              <span>{{ item.projectName || '项目名称' }}</span>
            </div>
            <div class="margin-right-12" v-if="type !== 'View'">
              <el-button type="text" size="small" @click.stop="onEditProject(item)" v-if="item.preview && projectList.every(v => v.preview)">编辑</el-button>
              <el-button type="text" size="small" @click.stop="onDeleteProject(item)" v-if="projectList.length > 1">移除</el-button>
            </div>
          </div>

          <div class="flex wrap padding-top-12">
            <div class="flex column flex-1">
              <el-form-item prop="" label="项目名称" label-width="103px" :style="{ marginBottom: item.preview ? '0px' : '24px' }">
                <span class="text-6" v-if="item.preview">{{ item.projectName }}</span>
                <el-input class="elFormItemWidth" type="text" show-word-limit maxlength="30" size="medium"
                          v-model.trim="item.projectName" placeholder="请输入项目名称" v-else></el-input>
              </el-form-item>

              <el-form-item prop="" label="培训规模" label-width="103px" :style="{ marginBottom: item.preview ? '0px' : '24px' }">
                <span class="text-6" v-if="item.preview">{{ item.scale }}</span>
                <el-input class="elFormItemWidth" type="text" show-word-limit maxlength="100" size="medium"
                          v-model.trim="item.scale" placeholder="请输入培训规模" v-else></el-input>
              </el-form-item>
              
            </div>
            
            <div class="flex column flex-1 margin-left-42 projectRight">
              <el-form-item prop="" label="总学时"  label-width="103px" :style="{ marginBottom: item.preview ? '0px' : '24px' }">
                <span class="text-6" v-if="item.preview">{{ item.totalLesson }}</span>
                <el-input-number class="elFormItemWidth" size="medium" controls-position="right"
                                v-model.trim="item.totalLesson" :min="0" :max="99999" v-else></el-input-number>
              </el-form-item>
              
              <el-form-item prop="" label="时间范围" label-width="103px" v-if="item.preview" :style="{ marginBottom: '0px'}">
                <span class="text-6">{{ item.startDate }} - {{ item.endDate }}</span>
              </el-form-item>
              <div class="flex" v-else>
                <el-form-item prop="" label="时间范围" label-width="103px" :style="{ marginBottom: '24px' }">
                  <el-date-picker
                      v-model="item.startDate"
                      type="date"
                      size="medium"
                      placeholder="开始日期"
                      value-format="yyyy-MM-dd"
                      class="timeRangeWidth">
                  </el-date-picker>
                </el-form-item>
                <div class="margin-right-8 margin-left-8 margin-top-6">-</div>
                <el-form-item prop="" label="" label-width="0">
                  <el-date-picker
                      v-model="item.endDate"
                      type="date"
                      size="medium"
                      placeholder="结束日期"
                      value-format="yyyy-MM-dd"
                      class="timeRangeWidth">
                  </el-date-picker>
                </el-form-item>
              </div>
            </div>
          </div>

          <el-form-item class="margin-top-8" prop="" label="专题列表" label-width="103px">
            <div class="margin-right-16">
              <el-table :data="item.courseList">
                <el-table-column label="专题名称" prop="courseName" min-width="120">
                  <template slot-scope="{row}">
                    <span class="text-6" v-if="item.preview">{{ row.courseName || '-' }}</span>
                    <el-input v-model="row.courseName" type="textarea" :rows="1" maxlength="9999"
                              placeholder="专题名称" v-else></el-input>
                  </template>
                </el-table-column>
                <el-table-column label="培训对象" prop="trainObject" min-width="120">
                  <template slot-scope="{row}">
                    <span class="text-6" v-if="item.preview">{{ row.trainObject || '-' }}</span>
                    <el-input v-model="row.trainObject" type="textarea" :rows="1" maxlength="9999"
                              placeholder="培训对象" v-else></el-input>
                  </template>
                </el-table-column>
                <el-table-column label="培训内容及目标" prop="trainContent" min-width="120">
                  <template slot-scope="{row}">
                    <span class="text-6" v-if="item.preview">{{ row.trainContent || '-' }}</span>
                    <el-input v-model="row.trainContent" type="textarea" :rows="1" maxlength="9999"
                              placeholder="培训内容及目标" v-else></el-input>
                  </template>
                </el-table-column>
                <el-table-column label="主讲老师" prop="lecturerName" min-width="120">
                  <template slot-scope="{row}">
                    <span class="text-6" v-if="item.preview">{{ row.lecturerName || '-' }}</span>
                    <el-input v-model="row.lecturerName" type="textarea" :rows="1" maxlength="9999"
                              placeholder="主讲老师" v-else></el-input>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="140" v-if="!item.preview">
                  <template slot-scope="{row,$index}">
                    <el-button type="text" v-if="$index" @click="onSortCourse(item, $index,-1)">
                      上移
                    </el-button>
                    <el-button type="text" v-if="item.courseList.length - 1 !== $index"
                                @click="onSortCourse(item, $index,1)">
                      下移
                    </el-button>
                    <el-button type="text" v-if="item.courseList.length > 1" @click="onDeleteCourse(item.courseList, $index)">移除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <el-button type="text" size="small" icon="el-icon-plus" @click="onAddCourse(item)" v-if="!item.preview">新增专题</el-button>
          </el-form-item>

          <el-form-item label="" label-width="103px" v-if="!item.preview">
            <el-button class="width-80" type="primary" size="small" @click="onSubmitProject(item)">保存</el-button>
            <el-button class="width-80" size="small" @click="onCancelProject(item)" v-if="!(projectList.length === 1 && !projectList[0].id)">取消</el-button>
          </el-form-item>

        </div>

      </div>
    </el-form>

    <ButtonSave v-if="type !== 'View'">
      <el-button class="width-96" type="primary" size="medium" :disabled="!baseFormData.applyId" @click="onNext">下一步</el-button>
    </ButtonSave>

  </el-card>
</template>

<script>
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'BaseInfo',
  async created() {
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
    let {required, Phone, phone} = this.$rules
    let defaultProject = {
      projectName: null,
      startDate: null,
      endDate: null,
      scale: 0,
      totalLesson: 0,
      courseList: [
        {
          courseName: null,
          lecturerName: null,
          trainContent: null,
          trainObject: null
        }
      ],
      preview: true
    }
    return {
      formData: {
        // 申请事由
        applyReason: null,
        // 申请备注
        applyRemark: null,
        // 联系邮箱
        contactEmail: null,
        // 联系电话
        contactNumber: null,
        // 联系人
        contactPerson: null,
        // 法人邮箱
        legalEmail: null,
        // 法人电话
        legalNumber: null,
        // 法人代表
        legalPerson: null,
        // 机构性质(字典值)
        orgCategory: null,
        // 机构简介
        orgIntro: null,
        // 机构名称(快照)
        orgName: null,
      },
      rules: {
        orgName: [required],
        orgCategory: [required],
        orgIntro: [required],
        legalNumber: phone,
        legalEmail: [
          { 
            type: 'email',
            message: '邮箱格式错误！'
          },
        ],
        contactPerson: [required],
        contactNumber: Phone,
        contactEmail: [
          { 
            type: 'email', 
            required: true,
            message: '邮箱格式错误！'
          },
        ],

      },

      // activeProject: [0],
      defaultProject,
      // 计划项目列表
      projectList: [JSON.parse(JSON.stringify(defaultProject))],

      loading: {
        submit: false
      },

      preview: true,

      // 限定日期选择时间
      pickerOptions: {
        disabledDate (time) {
          return time.getTime() > Date.now()
        }
      },

      // 是否是万人计划
      isPlan: false,

      // 是否展示项目计划
      showProject: true
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    ...mapGetters({
      orgCategory: 'common/orgCategory'
    }),
    pageInfo() {
      let pageStyles = {
        Create: {
          pageStyle: {
            height: this.$utils.FullViewHeight(175)
          }
        },
        Edit: {
          pageStyle: {
            height: this.$utils.FullViewHeight(175)
          }
        },
        View: {
          pageStyle: {
            height: this.$utils.FullViewHeight(this.type === 'View' ? 115 : 175)
          }
        }
      }

      let typeMapping = {
        Create: {
          submitFn: this.$api.ApplyQualification.save,
          type: 'Create',
          name: '课程'
        },
        Edit: {
          submitFn: this.$api.ApplyQualification.update,
          type: 'Edit',
          name: '课程'
        }
      }

      return {
        ...pageStyles[this.type],
        ...typeMapping[this.info.id ? 'Edit' : 'Create']
      }
    },
    baseFormData() {
      return {
        ...this.formData,
        projectList: undefined,
        lecturers: undefined
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
      let info = {}
      if (this.info && this.info.id) {
        // 详情信息
        info = {
          applyId: this.info.id,
          ...this.info
        }
        // 编辑-初始化项目列表
        if (this.info.projectList && this.info.projectList.length) {
          this.projectList = this.info.projectList.map(item => {
            return {
              ...item,
              courseList: JSON.parse(JSON.stringify(item.courseList)), // Emm
              preview: true
            }
          })
          // for (let i = 0; i < this.projectList.length; i++) {
          //   this.activeProject.push(i)
          // }
        } else {
          if (this.type === 'View') {
            this.showProject = false
          } else {
            this.showProject = true
            // 详情页
            // 编辑页
            this.projectList = this.projectList.map(item => {
              return {
                ...item,
                preview: false
              }
            })
          }
          // 编辑页
          // this.projectList = this.projectList.map(item => {
          //   return {
          //     ...item,
          //     preview: false
          //   }
          // })
        }
        this.preview = true
      } else if (this.type === 'Create') {
        this.preview = false
        // 机构基础信息
        info = await this.getOrgInfo()

        // 当机构不是万人计划时，弹窗提醒
        if(!this.isPlan) {
          await this.$confirm('本机构当前不是万人计划培训机构，请联系培训管理中心！', '提示', {
            showCancelButton: false,
            showClose: false,
            closeOnClickModal: false,
            type: 'info'
          })

          this.$router.push({name: 'ApplyQualifications'})
        }
      }


      this.formData = {
        ...this.formData,
        ...info
      }
      console.log('this.formData', this.formData)
    },
    // 操作 - 获取当前机构信息
    async getOrgInfo() {
      let {code, data} = await this.$api.UniOrg.info()
      if (code !== 200) return null

      // 判断机构是否是万人计划机构
      if(data.plan === 1) this.isPlan = true

      return {
        orgName: data.name,
        orgCategory: data.category,
        orgIntro: data.intro,

        contactEmail: data.contactEmail,
        contactNumber: data.contactNumber,
        contactPerson: data.contactPerson,

        legalEmail: data.legalEmail,
        legalNumber: data.legalNumber,
        legalPerson: data.legalPerson,
      }
    },

    // 操作 - 选择课程
    async onAddCourse(item) {
      item.courseList.push({
        ...this.defaultProject.courseList[0]
      })
    },
    // 操作 - 删除课程
    onDeleteCourse(list, index) {
      list.splice(index, 1)
    },
    // 操作 - 上移下移课程
    onSortCourse(item, index, sort) {
      let list = item.courseList
      let x = list[index]
      list[index] = list[index + sort]
      list[index + sort] = x

      item.courseList = [...list]
    },

    // 操作 - 删除计划
    async onDeleteProject(item) {
      if (JSON.stringify(item) !== JSON.stringify(this.defaultProject)) {
        await this.$confirm(`确定移除计划${item.projectName || ''}吗？`, '提示？', {})
      }
      if (item.id) {
        let { code } = await this.$api.ApplyQualification.projectRemove({
          projectId: item.id,
          applyId: this.baseFormData.applyId
        })
        if (code !== 200) return false
      }
      let list = this.projectList
      let fIndex = list.findIndex(a => a.id === item.id)
      list.splice(fIndex, 1)
    },
    // 操作 - 新增计划
    onAddProject() {
      if (!this.baseFormData.applyId) return this.$message.warning('请先保存基本信息')

      // this.activeProject.push(this.projectList.length)
      this.preview = true
      this.projectList.push({
        ...this.defaultProject,
        preview: false
      })
    },
    // 操作 - 编辑计划
    onEditProject(item) {
      if (!this.baseFormData.applyId) return this.$message.warning('请先保存基本信息')

      this.preview = true
      // item.preview = false
      this.$set(this.projectList.find(v => v.id === item.id), 'preview', false)
    },
    // 操作- 提交项目计划
    async onSubmitProject(item) {
      if (!this.baseFormData.applyId) return this.$message.warning('请先保存基本信息')
      let params = JSON.parse(JSON.stringify({...item, preview: undefined}))
      if (Object.values(params).some(v => !v)) return this.$message.warning('请完善项目信息')
      if (params.courseList.some(v => !v.courseName || !v.trainObject || !v.trainContent || !v.lecturerName)) return this.$message.warning('请完善课程信息')

      let res = {}
      if (item.id) {
        res = await this.$api.ApplyQualification.projectUpdate({
          ...item,
          applyId: this.baseFormData.applyId,
          projectId: item.id
        })
      } else {
        res = await this.$api.ApplyQualification.projectAdd({
          ...item,
          applyId: this.baseFormData.applyId,
          qualificationId: this.baseFormData.qualificationId
        })
      }
      if (res.code !== 200) return false
      if (res.data && res.data.id) item.id = res.data.id
      item.preview = true
    },
    // 操作 - 取消项目计划
    async onCancelProject(item) {
      if (item.id) return item.preview = true
      this.projectList.pop()
    },

    // 操作 - 提交基本信息
    async onSubmitInfo() {
      // 校验
      this.$refs.form.validate(async valid => {
        if (!valid) {
          // 校验错误 处理
          let errNode = this.$refs.form.$children.find(node => node.validateState === 'error')
          this.$refs.formWrapper.scrollTop = errNode.$el.offsetTop
          this.$message.warning(`${errNode.label}:${errNode.validateMessage}`)
          return false
        }

        this.loading.submit = true

        let {code, data} = await this.pageInfo.submitFn(this.baseFormData)
        this.loading.submit = false
        if (code !== 200) return false

        this.$msg[this.pageInfo.type]('')
        this.preview = true

        if (data && data.applyId) this.baseFormData.applyId = data.applyId
        if (data && data.id) this.baseFormData.qualificationId = data.id
        this.$router.replace({
          name: 'ApplyQualificationsEdit',
          query: {
            id: this.baseFormData.applyId
          }
        })
      })
    },
    // 操作 - 取消基本信息
    onCancelInfo() {
      if (this.baseFormData.applyId) {
        this.preview = true
      } else {
        this.$router.back()
      }
    },

    // 操作 - 下一步
    onNext() {
      this.$emit('confirm', {
        type: 'next',
        id: this.baseFormData.applyId
      })
    }
  }
}
</script>

<style scoped lang="stylus">
.el-card
  /deep/.el-card__body
    padding 0

  .el-form
    /deep/.el-form-item__label
      color NEUTRAL_COLOR_3

    .el-input, .el-select, .el-input-number
      width 300px

    .el-table
      border-top 1px solid #eee
      border-left 1px solid #eee
      border-right 1px solid #eee
      border-bottom none
      min-height inherit

      .el-input
        width 100%

    .width-input-area
      width 420px

      /*>>> .el-input__count
        bottom -40px*/

  >>>.el-form-item__content
    .elFormItemWidth
      max-width 420px
      width 100%

  .timeRangeWidth
    width 200px !important

  .el-collapse
    /deep/.el-collapse-item .el-collapse-item__wrap
      border-radius 0
      border-bottom none

  .info-content,
  .project-content
    border 2px solid NEUTRAL_COLOR_F

    &.check
      border 2px solid #C63636

  .project-header
    height 48px
    line-height 48px
    background #f8fafc
    font-size 13px

>>>.hideMustIcon
  .el-form-item__label
    &:before
      display none
</style>

<style scoped lang="stylus">
@media only screen and (max-width: 1371px)
  .projectRight
    margin-left 0
</style>