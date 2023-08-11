<template>
  <el-card>
    <div  :style="pageInfo.pageStyle">
    <!-- <div> -->
      
      <div class="font-14 line-height-32 text-bold margin-left-20 margin-right-20 margin-bottom-20">基本信息</div>
  
      <el-form ref="form" class="padding-left-32 padding-right-32 overflow" :model="formData" :rules="rules" label-width="120px" size="small">
        <div ref="formWrapper" class="overflow-auto padding-right-20"  style="min-height: 88px; max-height: 81vh;" :class="{elFormItemMarginBottom: type === 'View'}">
          <el-form-item prop="orgName" label="机构信息">
            <span v-if="type === 'View'">{{ formData.orgName }}</span>
            <el-input v-else :style="{width:'460px'}" size="small" disabled v-model="formData.orgName"></el-input>
          </el-form-item>
          <el-form-item prop="applyDate" label="申请时间">
            <span v-if="type === 'View'">{{ formData.applyDate }}</span>
            <el-date-picker v-else :style="{width:'460px'}" class="applyDateCSS" size="small" type="date" placeholder="选择申请时间" v-model="formData.applyDate" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
          </el-form-item>
          <el-form-item prop="applyReason" label="申请事由">
            <span v-if="type === 'View'">{{ formData.applyReason }}</span>
            <el-input v-else :style="{width:'460px'}" type="textarea" v-model="formData.applyReason" placeholder="请填写申请说明" maxlength="100" show-word-limit></el-input>
          </el-form-item>
          <el-form-item prop="applyRemark" label="申请备注">
            <span v-if="type === 'View'">{{ formData.applyRemark }}</span>
            <el-input v-else :style="{width:'460px'}" type="textarea" v-model="formData.applyRemark" placeholder="请填写申请备注" maxlength="100" show-word-limit></el-input>
          </el-form-item>
          <el-form-item prop="applySettleAddRequest.courseName" label="专题课程">
            <span v-if="type === 'View'">{{ formData.applySettleAddRequest.courseName }}</span>
            <div v-else>
              <el-input :style="{width:'460px'}" size="small" disabled v-model="formData.applySettleAddRequest.courseName" placeholder="暂未选择" class="margin-right-8" maxlength="30" show-word-limit></el-input>
              <el-button @click="choseClass" size="small" type="primary" v-if="type !== 'View'">选择课程</el-button>
            </div>
          </el-form-item>
  
          <el-divider></el-divider>
  
          <!-- 教室申请 -->
          <div style="width: 860px" class="margin-top-10" :class="{active:type === 'View'}">
            <span class="margin-left-52 font-14" style="flexShrink:0">教室申请&nbsp;&nbsp;&nbsp;</span>
            <el-button @click="choseClassroom" size="small" type="primary" v-if="type !== 'View'">选择教室</el-button>
            <el-button @click="reservationStatus" size="small" type="primary" v-if="type !== 'View'">查看教室预订情况</el-button>
            <el-form-item prop="applySettleAddRequest.applySettleClassroomAddRequests" label="教室申请"  class="margin-top-20 margin-bottom-16 hideLabel">
              <el-table :data="formData.applySettleAddRequest.applySettleClassroomAddRequests">
                <el-table-column prop="roomNo" label="教室号码" width="98"></el-table-column>
                <el-table-column prop="roomType" label="教室类型" width="110"></el-table-column>
                <el-table-column prop="useDateStart" label="开始时间" min-width="160">
                  <template slot-scope="{row}">
                    <span v-if="type === 'View'">{{ row.useDateStart }}</span>
                    <el-date-picker v-else class="pickTime" size="small" type="date" placeholder="选择开始日期" v-model="row.useDateStart" :picker-options="pickerOptions" format="yyyy-MM-dd" value-format="yyyy-MM-dd">
                    </el-date-picker>
                  </template>
                </el-table-column>
                <el-table-column prop="useDateEnd" label="结束时间" min-width="160">
                  <template slot-scope="{row}">
                    <span v-if="type === 'View'">{{ row.useDateEnd }}</span>
                    <el-date-picker v-else class="pickTime" size="small" type="date" placeholder="选择结束日期" v-model="row.useDateEnd" :picker-options="pickerOptions" format="yyyy-MM-dd" value-format="yyyy-MM-dd">
                    </el-date-picker>
                  </template>
                </el-table-column>
                <el-table-column prop="usePeriod" label="时间段" width="128">
                  <template slot-scope="{row}">
                    <span v-if="type === 'View'">{{ row.usePeriod | usePeriodList }}</span>
                    <el-select v-else v-model="row.usePeriod" placeholder="请选择" class="selectUsePeriod">
                      <el-option
                        v-for="item in usePeriodList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="操作" min-width="80" v-if="type !== 'View'">
                  <template slot-scope="{row}">
                    <el-button type="text" size="small" @click="removeClassroom(row)">移除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </div>
  
          <el-divider></el-divider>
  
          <!-- 宿舍申请 -->
          <div style="width: 860px" class="margin-top-10 padding-bottom-20" :class="{active:type === 'View'}">
            <span class="margin-left-52 font-14" style="flexShrink:0">宿舍申请&nbsp;&nbsp;&nbsp;</span>
            <el-button @click="choseDormitoryType" size="small" type="primary" v-if="type !== 'View'">选择宿舍</el-button>
            <el-form-item prop="applySettleAddRequest.applySettleDormitoryAddRequests"  label="宿舍申请" class="margin-top-20 margin-bottom-16 hideLabel">
              <el-table :data="formData.applySettleAddRequest.applySettleDormitoryAddRequests">
                <el-table-column prop="roomType" label="宿舍类型" width="118">
                  <template slot-scope="{row}">
                    {{ row.roomType  }}
                  </template>
                </el-table-column>
                <el-table-column prop="roomNum" label="选择宿舍数量" width="180">
                  <template slot-scope="{row}">
                    <span v-if="type === 'View'">{{ row.roomNum }}</span>
                    <el-input-number v-else v-model="row.roomNum" size="small" class="pickTime" :controls="false" :min="1" :max="1440" placeholder="请填写数量">
                    </el-input-number>
                  </template>
                </el-table-column>
                <!-- <el-table-column prop="apartmentResidue" label="宿舍剩余数量" width="110"></el-table-column> -->
                <el-table-column prop="useDateStart" label="开始时间" min-width="160">
                  <template slot-scope="{row}">
                    <span v-if="type === 'View'">{{ row.useDateStart }}</span>
                    <el-date-picker v-else class="pickTime" size="small" type="date" placeholder="选择开始日期" v-model="row.useDateStart" :picker-options="pickerOptions" format="yyyy-MM-dd" value-format="yyyy-MM-dd">
                    </el-date-picker>
                  </template>
                </el-table-column>
                <el-table-column prop="useDateEnd" label="结束时间" min-width="160">
                  <template slot-scope="{row}">
                    <span v-if="type === 'View'">{{ row.useDateEnd }}</span>
                    <el-date-picker v-else class="pickTime" size="small" type="date" placeholder="选择结束日期" v-model="row.useDateEnd" :picker-options="pickerOptions" format="yyyy-MM-dd" value-format="yyyy-MM-dd">
                    </el-date-picker>
                  </template>
                </el-table-column>
                <el-table-column label="操作" min-width="80" v-if="type !== 'View'">
                  <template slot-scope="{row}">
                    <el-button type="text" size="small" @click="removeDomitoryType(row)">移除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </div>
  
        </div>
      </el-form>

    </div>  

    <ButtonSave v-if="type !== 'View'">
      <el-button class="width-96" type="primary" size="medium" @click="onSubmit" :loading="loading.submit">
        保存
      </el-button>
      <el-button class="width-96" size="medium" @click="close">
        取消
      </el-button>
    </ButtonSave>

    <!-- <DialogPickApplyInfo ref="DialogPickApplyInfo"></DialogPickApplyInfo> -->
    <!-- <DialogClassroomOrderInfo ref="DialogClassroomOrderInfo"></DialogClassroomOrderInfo> -->
  </el-card>
</template>
  
  <script>
// import DialogPickApplyInfo from '@/components/dialog/Logistics/DialogPickApplyInfo'
// import DialogClassroomOrderInfo from '@/components/dialog/Logistics/DialogClassroomOrderInfo'
import { mapGetters } from 'vuex'

export default {
  name: 'BaseInfo',
  // created() {
  //   this.formData.orgName = this.userInfo.orgName
  // },
  components: {
    // DialogPickApplyInfo,
    // DialogClassroomOrderInfo
  },
  props: {
    type: {
      type: String,
      default: 'Create'
    },
    info: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data () {
    let { required } = this.$rules
    let defaultFormData = {
      orgName: null,
      applyDate: null,
      applyReason: null,
      applyRemark: null,
      applySettleAddRequest: {
        courseId: null, //课程ID,
        courseName: null,
        orgId: null,
        applySettleClassroomAddRequests: [],
        applySettleDormitoryAddRequests: []
      }
    }
    return {
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        orgName: [required],
        applyReason: [required],
        // applyRemark: [required],
        'applySettleAddRequest.courseName': [required],
        'applySettleAddRequest.applySettleClassroomAddRequests': [
          required,
          {
            validator: (rule, value, callback) => {
              if (!value.every(item => item.useDateStart && item.useDateEnd)) {
                return callback(new Error('请选择“开始日期”和“结束日期”'))
              }
              
              if (!value.every(item => new Date(item.useDateStart).getTime() <= new Date(item.useDateEnd).getTime())) {
                return callback(new Error('开始日期不能晚于结束日期'))
              }
              
              callback()
            },
            trigger: ['change', 'blur']
          }
        ],
        // 'applySettleAddRequest.applySettleDormitoryAddRequests': [
        //   required,
        //   {
        //     validator: (rule, value, callback) => {
        //       if (!value.every(item => item.roomNum)) {
        //         return callback(new Error('请填写宿舍数量'))
        //       }

        //       if (!value.every(item => item.useDateStart && item.useDateEnd)) {
        //         return callback(new Error('请选择“开始日期”和“结束日期”'))
        //       }
              
        //       if (!value.every(item => new Date(item.useDateStart).getTime() <= new Date(item.useDateEnd).getTime())) {
        //         return callback(new Error('开始日期不能晚于结束日期'))
        //       }
              
        //       callback()
        //     },
        //     trigger: ['change', 'blur']
        //   }
        // ],
        useDateStart: [required],
        useDateEnd: [required]
      },
      loading: {
        submit: false
      },
      number: null,
      pickerOptions: {
        // disabledDate (time) {
        //   return time.getTime() < Date.now() - 86400000
        // }
      },
      // 当前机构信息
      UniOrgData: null,
      applyId: undefined,
      id: undefined
    }
  },
  computed: {
    ...mapGetters({
      // enabled: 'common/enabled',
      // classType: 'common/classType',
      // dormitoryType: 'common/dormitoryType',
      // orgInfo: 'common/orgInfo'
      usePeriodList: 'common/usePeriodList',
    }),
    pageInfo() {
      let pageStyles = {
        Create: {
          pageStyle: {
            height: this.$utils.FullViewHeight(90)
          }
        },
        Edit: {
          pageStyle: {
            height: this.$utils.FullViewHeight(140)
          }
        },
        View: {
          pageStyle: {
            // height: this.$utils.FullViewHeight(this.type === 'View' ? 115 : 175)
            height: this.$utils.FullViewHeight(83)
          }
        }
      }

      // let typeMapping = {
      //   Create: {
      //     submitFn: this.$api.AllowanceApply.settleAdd,
      //     type: 'Create',
      //     name: '培训申请'
      //   },
      //   Edit: {
      //     submitFn: this.$api.AllowanceApply.update,
      //     type: 'Edit',
      //     name: '编辑申请'
      //   }
      // }

      return {
        ...pageStyles[this.type],
        // ...typeMapping[this.info.id ? 'Edit' : 'Create']
      }
    },
  },
  watch: {
    info: {
      immediate: true,
      deep: true,
      handler() {
        this.onInitFormData()
      }
    }
  },
  methods: {
    // 操作 - 初始化表单数据
    async onInitFormData() {
      let info = {}
      if (this.info && this.info.applyId) {

        // 编辑时获取申请id   入驻申请id
        if (this.type === 'Edit') {
          this.applyId = this.info.applyId
          this.id = this.info.id
        }

         // 详情信息
         info = {
          orgName: this.info.orgName,
          applyDate: this.info.applyDate,
          applyReason: this.info.applyReason,
          applyRemark: this.info.applyRemark,
          applySettleAddRequest: {
            courseName: this.info.courseName,
            orgId: null,
            applySettleClassroomAddRequests: this.info.classroomList.map(item => {
              return {
                roomNo: item.roomNo,
                roomType: item.roomType,
                useDateEnd: item.useDateEnd,
                useDateStart: item.useDateStart,
                usePeriod: item.usePeriod,

                building: item.building,
                classroomId: item.classroomId,
                floor: item.floor,
                remark: item.remark,
              }
            }),
            applySettleDormitoryAddRequests: this.info.dormitoryList.map(item => {
              return {
                roomNum: item.roomNum,
                roomType: item.roomType,
                useDateEnd: item.useDateEnd,
                useDateStart: item.useDateStart,

                remark: item.remark,
              }
            })
          }
        }

        // 获取机构信息，目的是为了后面提交时有orgId
        // this.getOrgInfo()

      } else if (this.type === 'Create') {
        // 机构基础信息
        info = await this.getOrgInfo()
      }

      this.formData = {
        ...this.formData,
        ...info
      }
    },
    // 获取当前机构信息
    async getOrgInfo() {
      let {code, data} = await this.$api.UniOrg.info()
      if (code !== 200) return null
      this.UniOrgData = data
      return {
        orgName: data.name
      }
    },
    
    // 确定提交
    async onSubmit () {
      // await this.$refs.form.validate()
      this.$refs.form.validate(async valid => {

        if (!valid) {
          // 校验错误 处理
          let errNode = this.$refs.form.$children.find(node => node.validateState === 'error')
          this.$refs.formWrapper.scrollTop = errNode.$el.offsetTop
          this.$message.warning(`${errNode.label}:${errNode.validateMessage}`)
          return false
        }

        this.loading.submit = true
        // let params = {
        //   ...this.formData,
        //   applySettleAddRequest: {
        //     ...this.formData.applySettleAddRequest,
        //     orgId: this.UniOrgData.id
        //   },
        //   orgId: this.UniOrgData.id || null,
        //   contactPerson: this.UniOrgData.contactPerson || null,
        //   contactNumber: this.UniOrgData.contactNumber || null,
        //   contactEmail: this.UniOrgData.contactEmail || null,
        //   applyType: 'settle'
        // }
        let params
        if(this.type === 'Create') {
          params = {
            ...this.formData,
            applySettleAddRequest: {
              ...this.formData.applySettleAddRequest,
              orgId: this.UniOrgData.id
            },
            orgId: this.UniOrgData.id || null,
            contactPerson: this.UniOrgData.contactPerson || null,
            contactNumber: this.UniOrgData.contactNumber || null,
            contactEmail: this.UniOrgData.contactEmail || null,
            applyType: 'settle'
          }
        } else {
          params = {
            applyId: this.applyId,
            applyDate: this.formData.applyDate,
            applyReason: this.formData.applyReason,
            applyRemark: this.formData.applyRemark,
            applySettleClassroomUpdateRequests: this.formData.applySettleAddRequest.applySettleClassroomAddRequests.map(item => {
              return {
                applySettleId: this.id,
                ...item,
                id: undefined,

              }
            }),
            applySettleDormitoryUpdateRequests: this.formData.applySettleAddRequest.applySettleDormitoryAddRequests.map(item => {
              return {
                applySettleId: this.id,
                ...item,
                id: undefined,

              }
            }),
            courseName: this.formData.applySettleAddRequest.courseName,
            orgId: this.UniOrgData.id,
            id: this.id
          }
        }
        let { code, data } = await this.pageInfo.submitFn(params)
        this.loading.submit = false
        if (code !== 200) return false
        this.$msg[this.pageInfo.type]()

        // 编辑时，将applyId赋值给data
        if(this.type === 'Edit') data = this.info.applyId

        // 下一步
        this.$emit('confirm', {
          type: 'next',
          id: data
        })

      })
    },

    // 取消按钮，跳转到  申请列表页
    close() {
      this.$router.push({
        name: 'FacilityApplyList'
      })
    },

    // 打开  选择教室的弹窗
    async choseClassroom () {
      let classRoomArr = await this.$refs.DialogPickApplyInfo.open({
        type: 'choseClassroom',
        choseClassList: this.formData.applySettleAddRequest.applySettleClassroomAddRequests
      })
      let newClassroom = classRoomArr.map(item => {
        return {
          ...item,
          classroomId: item.id,
          usePeriod: 'allDay'
        }
      })
      this.formData.applySettleAddRequest.applySettleClassroomAddRequests = this.formData.applySettleAddRequest.applySettleClassroomAddRequests.concat(newClassroom)
    },

    // 打开  选择课程的弹窗
    async choseClass () {
      let classObj = await this.$refs.DialogPickApplyInfo.open({
        type: 'choseClass'
      })

      this.formData.applySettleAddRequest.courseName = classObj.courseName
      this.formData.applySettleAddRequest.courseId = classObj.id
    },

    // 打开  选择宿舍类型的弹窗
    // async choseDormitoryType () {
    //   this.formData.applySettleAddRequest.applySettleDormitoryAddRequests = await this.$refs.DialogPickApplyInfo.open({
    //     type: 'choseDormitoryType'
    //   })
    // },
    async choseDormitoryType () {
      let dormitoryArr = await this.$refs.DialogPickApplyInfo.open({
        type: 'choseDormitoryType',
        choseDormitoryTypeList: this.formData.applySettleAddRequest.applySettleDormitoryAddRequests
      })
      let Dormitoryobjf = dormitoryArr.map(item => {
        return {
          ...item,
          roomType:item.dictValue
        }
      })
      this.formData.applySettleAddRequest.applySettleDormitoryAddRequests = this.formData.applySettleAddRequest.applySettleDormitoryAddRequests.concat(Dormitoryobjf)
    },

    removeClassroom (row) {
      this.formData.applySettleAddRequest.applySettleClassroomAddRequests = this.formData.applySettleAddRequest.applySettleClassroomAddRequests.filter(item => item.classroomId !== row.classroomId)
    },

    removeDomitoryType (row) {
      this.formData.applySettleAddRequest.applySettleDormitoryAddRequests = this.formData.applySettleAddRequest.applySettleDormitoryAddRequests.filter(item => item.roomType !== row.roomType)
    },
    // 教室预订情况
    reservationStatus(){
      this.$refs.DialogClassroomOrderInfo.open();
    }
  }
}
</script>
  
  <style scoped lang="stylus">
  .tree-wrapper
    max-height 300px
  >>> .el-input--prefix
    .el-input__inner
      width 300px
  >>>.pickTime
    .el-input__inner
      width 150px
    .el-input-number.is-without-controls
      width 150px
  >>>.el-form
    .cell 
      .selectUsePeriod
        width 90px
  >>> .el-date-editor.el-input
    width 300px
  .el-form
    .el-input, .el-select, .el-input-number
      width 300px
  .el-divider.el-divider--horizontal
    background NEUTRAL_COLOR_E6
  .active
    display flex
    >>> .el-form-item
      flex 1
      margin-top 0px
      .el-form-item__content
        margin-left 0px !important
  >>> .el-table
    min-height auto
  >>> .el-dialog
    margin-top 3vh !important
    .el-dialog__body
      padding 24px 0
  .saveButton
    >>>.el-form-item__content
      margin-left 0 !important

// 隐藏  教室申请  宿舍申请  的label
>>>.hideLabel
  .el-form-item__label
    display none

>>>.applyDateCSS
  .el-input__inner
    width 460px

// 调节详情页 表单间的间距，去掉必选标识
>>>.elFormItemMarginBottom
  .el-form-item
    margin-bottom 4px
  .el-form-item__label
    &:before
      display none
</style>