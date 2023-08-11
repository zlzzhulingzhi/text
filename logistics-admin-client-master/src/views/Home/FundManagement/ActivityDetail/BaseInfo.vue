<!--课程基本信息-->
<template>
  <el-card>
    <el-form ref="form" :model="formData" :rules="rules"
             label-width="116px" size="medium"
             class="padding-top-16 padding-bottom-16">
      <div ref="formWrapper" class="content overflow-auto" :style="pageInfo.pageStyle" :class="{elFormItemMarginBottom: detailCSS}">

        <div class="section">
          <div class="section-title">基本信息</div>

          <div class="flex">
            
              <div class="flex column flex-1">
                <el-form-item prop="applyActivityAddRequest.activityName" label="活动名称">
                  <span v-if="detailCSS" class="text-6">{{formData.applyActivityAddRequest.activityName}}</span>
                  <el-input v-else class="setWidth" type="text" show-word-limit maxlength="30"
                            v-model.trim="formData.applyActivityAddRequest.activityName" 
                            placeholder="请输入活动名称"></el-input>
                </el-form-item>
  
                <el-form-item prop="applyActivityAddRequest.activityTheme" label="活动主题">
                  <span v-if="detailCSS" class="text-6">{{formData.applyActivityAddRequest.activityTheme}}</span>
                  <el-input v-else class="setWidth" type="text" show-word-limit maxlength="30"
                            v-model.trim="formData.applyActivityAddRequest.activityTheme" 
                            placeholder="请输入活动主题"></el-input>
                </el-form-item>

                <el-form-item prop="applyDate" label="申请日期">
                  <span v-if="detailCSS" class="text-6">{{formData.applyDate}}</span>
                  <el-date-picker v-else class="setWidth" type="date" placeholder="选择申请日期" v-model="formData.applyDate" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
                </el-form-item>
  
                <el-form-item prop="applyActivityAddRequest.budgetAmount" label="经费预算">
                  <span v-if="detailCSS" class="text-6">{{formData.applyActivityAddRequest.budgetAmount}}</span>
                  <el-input-number v-else class="setWidth" size="small" :precision="2" :controls="false" :min="0" :max="1000000"
                      v-model="formData.applyActivityAddRequest.budgetAmount" ></el-input-number>
                </el-form-item>
  
                <el-form-item prop="applyActivityAddRequest.activityAddress" label="举办地点">
                  <span v-if="detailCSS" class="text-6">{{formData.applyActivityAddRequest.activityAddress}}</span>
                  <el-input v-else class="setWidth" type="text" show-word-limit maxlength="30"
                            v-model.trim="formData.applyActivityAddRequest.activityAddress" 
                            placeholder="请输入举办地点"></el-input>
                </el-form-item>
  
                <el-form-item prop="applyActivityAddRequest.activityDate" label="举办时间">
                  <span v-if="detailCSS" class="text-6">{{formData.applyActivityAddRequest.activityDate}}</span>
                  <el-date-picker
                    v-else
                    class="setWidth"
                    v-model="formData.applyActivityAddRequest.activityDate"
                    type="date"
                    placeholder="选择日期"
                    value-format="yyyy-MM-dd" 
                    :picker-options="pickerOptions"
                    format="yyyy-MM-dd">
                  </el-date-picker>
                </el-form-item>
  
              </div>
  
              <div class="flex column flex-1">

                <el-form-item prop="applyActivityAddRequest.activityScale" label="活动规模">
                  <span v-if="detailCSS" class="text-6">{{formData.applyActivityAddRequest.activityScale}}</span>
                  <el-input v-else class="setWidth" type="text" show-word-limit maxlength="30"
                            v-model.trim="formData.applyActivityAddRequest.activityScale" 
                            placeholder="请输入活动规模"></el-input>
                </el-form-item>
  
                <el-form-item prop="applyActivityAddRequest.guideUnits" label="指导单位">
                  <span v-if="detailCSS" class="text-6">{{formData.applyActivityAddRequest.guideUnits}}</span>
                  <el-input v-else class="setWidth" type="text" show-word-limit maxlength="30"
                            v-model.trim="formData.applyActivityAddRequest.guideUnits" 
                            placeholder="请输入指导单位"></el-input>
                </el-form-item>
  
                <el-form-item prop="applyActivityAddRequest.activityUnits" label="主办单位">
                  <span v-if="detailCSS" class="text-6">{{formData.applyActivityAddRequest.activityUnits}}</span>
                  <el-input v-else class="setWidth" type="text" show-word-limit maxlength="30"
                            v-model.trim="formData.applyActivityAddRequest.activityUnits" 
                            placeholder="请输入主办单位"></el-input>
                </el-form-item>
  
                <el-form-item prop="applyActivityAddRequest.undertakerUnits" label="承办单位">
                  <span v-if="detailCSS" class="text-6">{{formData.applyActivityAddRequest.undertakerUnits}}</span>
                  <el-input v-else class="setWidth" type="text" show-word-limit maxlength="30"
                            v-model.trim="formData.applyActivityAddRequest.undertakerUnits" 
                            placeholder="请输入承办单位"></el-input>
                </el-form-item>
  
                <el-form-item prop="applyActivityAddRequest.partnerUnits" label="协办单位">
                  <span v-if="detailCSS" class="text-6">{{formData.applyActivityAddRequest.partnerUnits}}</span>
                  <el-input v-else class="setWidth" type="text" show-word-limit maxlength="30"
                            v-model.trim="formData.applyActivityAddRequest.partnerUnits" 
                            placeholder="请输入协办单位"></el-input>
                </el-form-item>
  
                <el-form-item prop="applyActivityAddRequest.guests" label="重要嘉宾">
                  <span v-if="detailCSS" class="text-6">{{formData.applyActivityAddRequest.guests}}</span>
                  <el-input v-else class="setWidth" type="text" show-word-limit maxlength="30"
                            v-model.trim="formData.applyActivityAddRequest.guests" 
                            placeholder="请输入重要嘉宾"></el-input>
                </el-form-item>
                
              </div>

          </div>  
           
          
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
    let {required} = this.$rules
    let defaultData = {
        applyActivityAddRequest: {
          activityName: null,
          activityTheme: null,
          budgetAmount: null,
          activityAddress: null,
          activityDate: null,
          activityScale: null,
          guideUnits: null,
          activityUnits: null,
          undertakerUnits: null,
          partnerUnits: null,
          guests: null,
        },
        applyDate: null
    }
    return {
      formData: JSON.parse(JSON.stringify(defaultData)),

      rules: {
        'applyActivityAddRequest.activityName': [required],
        'applyActivityAddRequest.activityTheme': [required],
        'applyActivityAddRequest.budgetAmount': [required],
        'applyActivityAddRequest.activityAddress': [required],
        'applyActivityAddRequest.activityDate': [required],
      },

      defaultData,

      loading: {
        submit: false
      },

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
      orgCategory: 'common/orgCategory'
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
          // submitFn: this.$api.ApplyCost.saveActivity,
          type: 'Create',
          name: '课程'
        },
        Edit: {
          // submitFn: this.$api.ApplyCost.updateActivity,
          type: 'Edit',
          name: '课程'
        }
      }

      return {
        ...pageStyles[this.type],
        ...typeMapping[this.info.id ? 'Edit' : 'Create']
      }
    },
    submitFormData() {
      if(this.type === 'Create'&&!(this.info.id)) {
        return {
        ...this.formData
        }
      } else {
       return {
        ...this.formData,
        applyActivity: this.formData.applyActivityAddRequest,
        applyActivityAddRequest: undefined
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
    }
  },
  methods: {
    // 操作 - 初始化表单数据
    async onInitFormData() {
      let info = {}
      if (this.info && this.info.id) {
        // 详情页 修改 detailCSS
        if(this.type === 'View') this.detailCSS = true
        // 此处得用深拷贝，否则会影响this.info，进而影响AttachInfo.vue中的this.info，导致watch一直执行
        let replaceThisInfo = JSON.parse(JSON.stringify(this.info))
        // detail接口获取的数据  替换成表单需要的字段
        let needInfo = {
          ...replaceThisInfo,
          applyActivity: undefined,
          applyActivityAddRequest: replaceThisInfo.applyActivity
        }
        info = {
          ...needInfo,
          id: needInfo.id,
          applyDate: replaceThisInfo.applyDate
        }
      } else if (this.type === 'Create') {
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

        // 更新时，需要将表单的一些字段，替换成传参需要的字段
        // let payloadInfo = JSON.parse(JSON.stringify(this.submitFormData))
        // if(this.submitFormData.applyActivityAddRequest?.applyId) {
        //   payloadInfo = {
        //     ...payloadInfo,
        //     applyActivity: payloadInfo.applyActivityAddRequest,
        //     applyActivityAddRequest: undefined
        //   }
        // }
        // let {code, data} = await this.pageInfo.submitFn(payloadInfo)
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
        name: 'ApplyCostList',
        params: {
          elTapPane: 'ActivityApply'
        }
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
    line-height 20px
    margin-bottom 21px
    font-weight bold


  .width-input-area
    width 300px

    /*>>> .el-input__count
      bottom -40px*/

.el-form-item__content
  .setWidth
    width 420px

// 调节详情页 表单间的宽度，去掉必选标识
>>>.elFormItemMarginBottom
  .el-form-item
    margin-bottom 4px

  .el-form-item__label
    &:before
      display none
</style>