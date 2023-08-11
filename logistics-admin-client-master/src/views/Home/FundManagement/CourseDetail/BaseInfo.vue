<!--课程基本信息-->
<template>
  <el-card>
    <el-form ref="form" :model="formData" :rules="rules"
             label-width="116px" size="medium"
             class="padding-top-16 padding-bottom-16">
      <div ref="formWrapper" class="content overflow-auto" :style="pageInfo.pageStyle" :class="{elFormItemMarginBottom: detailCSS}">

        <!-- 资助项目基本信息 -->
        <div class="section">
          <div class="section-title">资助项目</div>

          <div>
            <el-form-item prop="applyAllowanceAddRequest.projectNo" label="项目编号">
              <span v-if="detailCSS" class="text-6">{{formData.applyAllowanceAddRequest.projectNo}}</span>
              <el-input v-else class="setWidth" type="text" show-word-limit maxlength="30"
                        v-model.trim="formData.applyAllowanceAddRequest.projectNo" placeholder="请输入项目编号"></el-input>
            </el-form-item>

            <el-form-item prop="applyAllowanceAddRequest.projectName" label="项目名称">
              
              <div v-if="!detailCSS">
                <el-button type="primary" size="small" @click="onSelectClass()" :class="{'ifSelectClass':formData.applyAllowanceAddRequest.projectName}">选择班级</el-button>
                <span class="margin-left-12 text-6">提示：项目与班级关联，故需要选择班级</span>
                <div>
                  <el-input class="setWidth margin-right-8" type="text" show-word-limit maxlength="30" v-if="formData.applyAllowanceAddRequest.projectName"
                          v-model="formData.applyAllowanceAddRequest.projectName" disabled></el-input>
                </div>      
              </div>

              <div v-else>
                <span class="text-6">{{formData.applyAllowanceAddRequest.projectName}}</span>
                
              </div>
            </el-form-item>

            <el-form-item prop="applyDate" label="申请时间">
              <el-date-picker class="setWidth" type="date" placeholder="选择申请时间" v-model="formData.applyDate" format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-if="!detailCSS"></el-date-picker>

              <div v-else>
                <span class="text-6">{{formData.applyDate}}</span>
              </div>
            </el-form-item>

            <el-form-item prop="applyAllowanceAddRequest.applyAllowanceFeeAddRequestList" label="补贴费用">
              <el-table :data="formData.applyAllowanceAddRequest.applyAllowanceFeeAddRequestList" show-summary class="hideHeader" :style="{width: '420px'}">

                <template v-slot:empty>
                  <Results></Results>
                </template>

                <el-table-column label="补贴分类" prop="itemName"></el-table-column>

                <el-table-column label="费用（元）" prop="allowanceFee">
                  <template slot-scope="{ row }">
                    <span v-if="detailCSS" class="text-6">{{row.allowanceFee}}</span>
                    <el-input-number v-else :controls="false" :precision="2" :min="0" :max="1000000"
                        v-model="row.allowanceFee" :style="{width: '120px'}"></el-input-number>
                    <span class="margin-left-8">元</span>
                  </template>
                </el-table-column>

              </el-table>
              <div class="height-44 line-height-44 relative font-14 text-bold setWidthTotalSubsidy"><span class="margin-left-16">总计（元）<span class="allFeeStyle">{{formData.applyAllowanceAddRequest.totalAllowanceFee}}元</span></span></div>
            </el-form-item>
          </div>
        </div>

        <!-- 课程主题详情信息 -->
        <div class="section">
          <div class="section-title">课程主题详情</div>

          <div class="flex">

            <div class="flex column flex-1">
  
              <el-form-item prop="applyAllowanceAddRequest.studentNum" label="实际参训人数">
                <span v-if="detailCSS" class="text-6">{{formData.applyAllowanceAddRequest.studentNum}}</span>
                <el-input-number v-else class="setWidth" controls-position="right" :min="0" :max="1000000" :step="1"
                            v-model.trim="formData.applyAllowanceAddRequest.studentNum" ></el-input-number>
              </el-form-item>

              <div class="flex">
                <el-form-item v-if="detailCSS" label="实际培训时间">
                  <span class="text-6">{{formData.applyAllowanceAddRequest.startDate}}&nbsp;-&nbsp;{{formData.applyAllowanceAddRequest.endDate}}</span>
                </el-form-item>

                <div v-else class="flex">
                  <el-form-item prop="applyAllowanceAddRequest.startDate" label="实际培训时间">
                    <el-date-picker
                      v-model="formData.applyAllowanceAddRequest.startDate"
                      type="date"
                      placeholder="开始日期"
                      value-format="yyyy-MM-dd" 
                      format="yyyy-MM-dd"
                      :picker-options="pickerOptions"
                      :style="{width: '200px'}">
                    </el-date-picker>
                  </el-form-item>  
                  <span class="margin-left-6 margin-right-6 margin-top-6 text-6">-</span>
                  <el-form-item prop="applyAllowanceAddRequest.endDate" label-width="0">
                    <el-date-picker
                      v-model="formData.applyAllowanceAddRequest.endDate"
                      type="date"
                      placeholder="结束日期"
                      value-format="yyyy-MM-dd" 
                      format="yyyy-MM-dd"
                      :picker-options="pickerOptions"
                      :style="{width: '200px'}">
                    </el-date-picker>
                  </el-form-item>
                </div>
              </div>

            </div>  

            <div class="flex column flex-1">

              <el-form-item prop="applyAllowanceAddRequest.examNum" label="参加考试人数">
                <span v-if="detailCSS" class="text-6">{{formData.applyAllowanceAddRequest.examNum}}</span>
                <el-input-number v-else class="setWidth" controls-position="right" :min="0" :max="1000000" :step="1"
                            v-model.trim="formData.applyAllowanceAddRequest.examNum" ></el-input-number>
              </el-form-item>

              <el-form-item prop="applyAllowanceAddRequest.examDate" label="考试时间">
                <span v-if="detailCSS" class="text-6">{{formData.applyAllowanceAddRequest.examDate}}</span>
                <el-date-picker
                  v-else 
                  v-model="formData.applyAllowanceAddRequest.examDate"
                  type="date"
                  placeholder="选择日期"
                  value-format="yyyy-MM-dd" 
                  format="yyyy-MM-dd"
                  :picker-options="pickerOptions"
                  :style="{width: '420px'}">
                </el-date-picker>
              </el-form-item>
              
            </div>           

          </div>
            
            <el-form-item prop="applyAllowanceAddRequest.applyAllowanceStudentAddRequests" label="学员列表" class="studentsList">
              <div>
                <el-button type="primary" size="small" icon="el-icon-plus" class="margin-bottom-8" @click="onAddStudents()" v-if="!detailCSS">添加学员</el-button>
                <div>
                  <el-table :data="formData.applyAllowanceAddRequest.applyAllowanceStudentAddRequests" :style="{minHeight: '0'}">
                    
                    <el-table-column label="学员姓名" prop="studentName">
                      <template slot-scope="{ row }">
                        <span v-if="detailCSS" class="text-6">{{row.studentName}}</span>
                        <el-input v-else class="elInputInStudentTable" size="small" v-model="row.studentName" disabled></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="单位" prop="unitName">
                      <template slot-scope="{ row }">
                        <span v-if="detailCSS" class="text-6">{{row.unitName}}</span>
                        <el-input v-else class="elInputInStudentTable" size="small" v-model="row.unitName"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="学时（课时）" prop="lessonNum">
                      <template slot-scope="{ row }">
                        <span v-if="detailCSS" class="text-6">{{row.lessonNum}}</span>
                        <el-input-number v-else class="elInputInStudentTable" size="small" :controls="false" :min="0" :max="1000000"
                              v-model="row.lessonNum" ></el-input-number>
                      </template>
                    </el-table-column>
                    <el-table-column label="考试成绩（分）" prop="examScore">
                      <template slot-scope="{ row }">
                        <span v-if="detailCSS" class="text-6">{{row.examScore}}</span>
                        <el-input v-else class="elInputInStudentTable inputCenter" size="small" type="number"
                              v-model="row.examScore" ></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="考试结果" prop="examResult">
                      <template slot-scope="{ row }">
                        <span v-if="detailCSS" class="text-6">{{row.examResult}}</span>
                        <!-- <el-select v-else class="elInputInStudentTable" v-model="row.examResult" placeholder="请选择" size="small" clearable>
                          <el-option
                            v-for="item in examResult"
                            :key="item.id"
                            :label="item.name"
                            :value="item.name">
                          </el-option>
                        </el-select> -->
                        <el-input v-else type="text" size="small" v-model="row.examResult" placeholder="请输入" maxlength="10"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="缴费金额（元）" prop="payAmount">
                      <template slot-scope="{ row }">
                        <span v-if="detailCSS" class="text-6">{{row.payAmount}}</span>
                        <el-input-number v-else class="elInputInStudentTable" size="small" :precision="2" :controls="false" :min="0" :max="1000000"
                              v-model.trim="row.payAmount" ></el-input-number>
                      </template>
                    </el-table-column>
                    <el-table-column label="资助金额（元）" prop="supplyAmount">
                      <template slot-scope="{ row }">
                        <span v-if="detailCSS" class="text-6">{{row.supplyAmount}}</span>
                        <el-input-number v-else class="elInputInStudentTable" size="small" :precision="2" :controls="false" :min="0" :max="1000000"
                              v-model="row.supplyAmount" ></el-input-number>
                      </template>
                    </el-table-column>
                    <el-table-column label="操作" width="140" v-if="!detailCSS">
                      <template slot-scope="{row,$index}">
                        <el-button type="text" @click="onDeleteStudent(formData.applyAllowanceAddRequest.applyAllowanceStudentAddRequests, $index)">移除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
                
              </div>     
            </el-form-item>

        </div>

      </div>
    </el-form>


    <ButtonSave v-if="!detailCSS">
      <el-button class="width-96" type="primary" size="medium" @click="onSubmit" :loading="loading.submit">
        保存
      </el-button>
      <el-button class="width-96" size="medium" @click="close">
        取消
      </el-button>
    </ButtonSave>

    <!-- <DialogAllowancePickStudents ref="DialogAllowancePickStudents" ></DialogAllowancePickStudents>
    <DialogPickClass ref="DialogPickClass"></DialogPickClass> -->

  </el-card>
</template>

<script>
// import DialogAllowancePickStudents from '@/components/dialog/Allowance/DialogAllowancePickStudents'
// import DialogPickClass from '@/components/dialog/Allowance/DialogPickClass'
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'BaseInfo',
  components: {
    // DialogAllowancePickStudents,
    // DialogPickClass,
  },
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
    let {required} = this.$rules
    let defaultData = {
      
        applyAllowanceAddRequest: {
          projectNo: null,
          projectName: null,
          totalAllowanceFee: null,
          studentNum: null,
          // trainDataRange: null,
          startDate: null,
          endDate: null,
          examNum: null,
          examDate: null,
          applyAllowanceStudentAddRequests: [],
          applyAllowanceFeeAddRequestList: [
            {
              itemCode: 'pxcszzzj',
              itemName: '培训场所资助租金',
              allowanceFee: null
            },
            {
              itemCode: 'xyjczz',
              itemName: '学员就餐资助费用',
              allowanceFee: null
            },
            {
              itemCode: 'xyzszz',
              itemName: '学员住宿资助费用',
              allowanceFee: null
            },
            {
              itemCode: 'xyjtzz',
              itemName: '学员交通资助费用',
              allowanceFee: null
            },
            {
              itemCode: 'xyxfzz',
              itemName: '学员学费资助金额',
              allowanceFee: null
            },
          ],
        },
        applyDate: null,
      
    }
    return {
      
      formData: JSON.parse(JSON.stringify(defaultData)),

      rules: {
        'applyAllowanceAddRequest.projectNo': [required],
        'applyAllowanceAddRequest.projectName': [required],
        // 'applyAllowanceAddRequest.totalAllowanceFee': [required],
        'applyAllowanceAddRequest.studentNum': [required],
        'applyAllowanceAddRequest.startDate': [
          required,
          {
            validator: (rule, value, callback) => {

              if(!this.formData.applyAllowanceAddRequest.endDate) return callback()

              else if (new Date(value).getTime() > new Date(this.formData.applyAllowanceAddRequest.endDate).getTime()) {
                return callback(new Error('开始日期不能晚于结束日期'))
              }

              callback()
            },
            trigger: ['change', 'blur']
          }
        ],
        'applyAllowanceAddRequest.endDate': [
          required,
          {
            validator: (rule, value, callback) => {
              if(new Date(value).getTime() < new Date(this.formData.applyAllowanceAddRequest.startDate).getTime()) {
                return callback(new Error('结束日期不能早于开始日期'))
              }

              callback()
            },
            trigger: ['change', 'blur']
          }
        ],
        'applyAllowanceAddRequest.examNum': [required],
        'applyAllowanceAddRequest.examDate': [
          required,
          {
            validator: (rule, value, callback) => {
              if(!this.formData.applyAllowanceAddRequest.endDate && !this.formData.applyAllowanceAddRequest.startDate) return callback()

              else if (new Date(value).getTime() < new Date(this.formData.applyAllowanceAddRequest.endDate).getTime()) {
                return callback(new Error('考试日期不能早于培训结束日期'))
              }

              callback()
            },
            trigger: ['change', 'blur']
          }
        ],
        'applyAllowanceAddRequest.applyAllowanceStudentAddRequests': [
          required,
          {
            validator: (rule, value, callback) => {
              if(!value.every(item => (item.unitName 
                                   && (item.lessonNum || item.lessonNum === 0) 
                                   && (item.examResult || item.examScore || item.examScore === 0)
                                   && (item.payAmount || item.payAmount === 0)
                                   && (item.supplyAmount || item.supplyAmount === 0)))){
                return callback(new Error('请完整填写信息！'))
              } else if(value.some(item => (item.examScore < 0 || item.examScore > 100))){
                return callback(new Error('请填写正确的考试成绩！'))
              }
              callback()
            },
            trigger: ['change', 'blur']
          }
        ],
        'applyAllowanceAddRequest.applyAllowanceFeeAddRequestList': [required],
      },

      defaultData,

      loading: {
        submit: false
      },

      courseId: null,
      // 所选的班级信息
      classObj: null,

      // 用以区分 详情页 和 另外两个页面
      detailCSS: false,

      // 限定日期选择时间
      pickerOptions: {
        // disabledDate (time) {
        //   return time.getTime() < Date.now() - 86400000
        // }
      }
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    ...mapGetters({
      orgCategory: 'common/orgCategory',
      examResult: 'common/examResult'
    }),
    pageInfo() {
      let pageStyles = {
        Create: {
          pageStyle: {
            height: this.$utils.FullViewHeight(124)
          }
        },
        Edit: {
          pageStyle: {
            height: this.$utils.FullViewHeight(175)
          }
        },
        View: {
          pageStyle: {
            height: this.$utils.FullViewHeight(115)
          }
        }
      }

      let typeMapping = {
        Create: {
          // submitFn: this.$api.ApplyCost.saveCourse,
          type: 'Create',
          name: '课程'
        },
        Edit: {
          // submitFn: this.$api.ApplyCost.updateCourse,
          type: 'Edit',
          name: '课程'
        }
      }

      return {
        ...pageStyles[this.type],
        ...typeMapping[this.info?.applyAllowanceDetailResponse?.applyId ? 'Edit' : 'Create']
      }
    },
    submitFormData() {
      if(this.type === 'Create'&&!(this.info?.applyAllowanceDetailResponse?.applyId)) {
        return {
        ...this.formData
        }
      } else {
        this.formData.applyAllowanceAddRequest = {
          ...this.formData.applyAllowanceAddRequest,
          applyAllowanceStudentUpdateRequests: this.formData.applyAllowanceAddRequest.applyAllowanceStudentAddRequests,
          applyAllowanceStudentAddRequests: undefined,
          applyAllowanceFeeUpdateRequests: this.formData.applyAllowanceAddRequest.applyAllowanceFeeAddRequestList,
          applyAllowanceFeeAddRequestList: undefined
        }
        return {
          ...this.formData,
          applyAllowanceUpdateRequest: this.formData.applyAllowanceAddRequest,
          applyAllowanceAddRequest: undefined,
        }
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
    },
    'formData.applyAllowanceAddRequest.applyAllowanceFeeAddRequestList': {
      deep: true,
      handler() {
        let feeAll = this.formData.applyAllowanceAddRequest.applyAllowanceFeeAddRequestList?.map(item => item.allowanceFee)
        this.formData.applyAllowanceAddRequest.totalAllowanceFee = feeAll?.reduce((preValue, curValue) => preValue + curValue, 0)
      }
    }
  },
  methods: {
    // 操作 - 初始化表单数据
    async onInitFormData() {
      let info = {}
      if (this.info && this.info?.applyAllowanceDetailResponse?.applyId) {
        // 详情页 修改 detailCSS
        if(this.type === 'View') this.detailCSS = true

        // detail接口获取的数据  替换成表单需要的字段       此处得用深拷贝，否则会影响this.info，进而影响AttachInfo.vue中的this.info，导致watch一直执行
        // let needInfo = this.info.applyAllowanceDetailResponse
        let needInfo = JSON.parse(JSON.stringify(this.info.applyAllowanceDetailResponse))
        needInfo = {
          ...needInfo,
          applyAllowanceStudentAddRequests: needInfo.applyAllowanceStudentDetailResponseList,
          applyAllowanceStudentDetailResponseList: undefined,
          applyAllowanceFeeAddRequestList: needInfo.applyAllowanceFeeDetailResponses,
          applyAllowanceFeeDetailResponses: undefined
        }

        // 详情信息
        info = {
          applyAllowanceAddRequest: needInfo,
          id: needInfo.applyId,
          applyDate: this.info.applyDate
        }
      } else if (this.type === 'Create')  {
        // 机构基础信息
        info = await this.getOrgInfo()
      }


      this.formData = {
        ...this.formData,
        ...info
      }
    },
    // 操作 - 获取当前机构信息
    // async getOrgInfo() {
    //   let {code, data} = await this.$api.UniOrg.info()
    //   if (code !== 200) return null
    //   return {
    //     orgName: data.name,
    //     orgCategory: data.category,
    //     orgIntro: data.intro,

    //     contactEmail: data.contactEmail,
    //     contactNumber: data.contactNumber,
    //     contactPerson: data.contactPerson,

    //     legalEmail: data.legalEmail,
    //     legalNumber: data.legalNumber,
    //     legalPerson: data.legalPerson
    //   }
    // },
    // 操作 - 选择班级
    async onSelectClass() {
      this.classObj = await this.$refs.DialogPickClass.open()
      this.formData.applyAllowanceAddRequest.projectName = this.classObj.courseName
      this.formData.applyAllowanceAddRequest.className = this.classObj.name
      this.formData.applyAllowanceAddRequest.classId = this.classObj.id
    },

    // 操作 - 添加学员
    async onAddStudents() {
      // 编辑时，获取所需的班级id
      if(this.type === 'Edit') this.classObj = { id: this.formData.applyAllowanceAddRequest.classId}

      if(!this.formData.applyAllowanceAddRequest.projectName) return this.$message.warning('请先选择班级')
      let studentInfo = await this.$refs.DialogAllowancePickStudents.open({
        formData: this.classObj,
        selectedStudents: this.formData.applyAllowanceAddRequest.applyAllowanceStudentAddRequests
      })
      let newStudentInfo = studentInfo.map(item => {
          return {
            studentName: item.realName,
            studentId: item.studentId,
            unitName: item.unitName,
            memberId: item.userId,
            lessonNum: null,
            examScore: null,
            payAmount: null,
            supplyAmount: null
          }
      })

      // 编辑时，给新的学员数据添加applyAllowanceId
      if(this.type === 'Edit') {
        newStudentInfo = newStudentInfo.map(item => {
          return {
            ...item,
            applyAllowanceId: this.formData.applyAllowanceAddRequest.id
          }
        })
      }

      this.formData.applyAllowanceAddRequest.applyAllowanceStudentAddRequests = this.formData.applyAllowanceAddRequest.applyAllowanceStudentAddRequests.concat(newStudentInfo)
      // 使用classId后删除，避免污染    似乎不需要
      // this.classObj = undefined
    },
    // 操作 - 移除学员
    onDeleteStudent(list, index) {
      list.splice(index, 1)
    },

    // 操作 - 提交
    async onSubmit() {
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

        let {code, data} = await this.pageInfo.submitFn(this.submitFormData)
        
        this.loading.submit = false
        if (code !== 200) return false

        this.$msg[this.pageInfo.type]('')

        this.$emit('confirm', {
          type: 'next',
          id: this.submitFormData.id || data
        })
      })
    },

    // 取消按钮，跳转到  申请列表页
    close() {
      this.$router.push({
        name: 'ApplyCostList'
      })
    }
  }
}
</script>

<style scoped lang="stylus">
.el-form
  .el-input, .el-select, .el-input-number
    width 300px

  .el-table
    .el-input
      width 100%

.section
  .section-title
    font-size 14px
    line-height 14px
    margin-bottom 21px
    font-weight bold


  .width-input-area
    width 300px

    /*>>> .el-input__count
      bottom -40px*/

.el-form-item__content
  .setWidth
    width 420px
  .ifSelectClass
    margin-bottom 8px
  .setWidthTotalSubsidy
    width 420px
    top -44px
    background #f2f2f2
  .TotalSubsidyUnits
    display inline-block
    transform: translateX(-24px)

>>>.elInputInStudentTable
  width 90% !important
  .el-input__inner
    width 100%

// 考试成绩  用input，居中显示
>>>.inputCenter
  input
    text-align center
// 调节详情页 表单间的间距，去掉必选标识
>>>.elFormItemMarginBottom
  .el-form-item
    margin-bottom 4px
  .el-form-item__label
    &:before
      display none

// 隐藏头部，替换尾部背景色、字体颜色
>>>.hideHeader
  min-height 270px
  .el-table__header-wrapper
    display none

  .el-table__footer-wrapper
    td
      background #f2f2f2
      color black
      padding 10px 0px
      border-top none
      border-bottom none

.allFeeStyle
  margin-left 134px

//限制学员列表最小宽度，使得学员列表显示正常
>>>.studentsList
  min-width 1250px
</style>