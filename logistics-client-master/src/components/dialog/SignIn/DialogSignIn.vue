<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

      <!-- 批量签到  表单 -->
      <template v-if="dialogType === 'BatchSignIn'">
        <el-form-item label="消耗课时" prop="lessonHour">
          <el-input-number class="width-300" v-model="formData.lessonHour" controls-position="right" :step="1" :min="1" :max="100" ></el-input-number>
          <div class="text-9 height-16 line-height-16 font-12 margin-top-2">说明备注：1课时=1节课</div>
        </el-form-item>

        <el-form-item label="签到日期" prop="signInDate">
          <el-date-picker
              v-model="formData.signInDate"
              class="width-300"
              type="date"
              placeholder="签到日期"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="签到对象" prop="selectMode">
          <el-radio-group v-model="formData.selectMode">
            <el-radio :label="1">已选学生({{ formData.memberIds.length }}人)</el-radio>
            <el-radio :label="2">全班({{ pParams.studentCount }}人)</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="签到课次" prop="lessonNums">
          <el-select class="width-300" v-model="lessonStart" filterable
                     size="small" placeholder="签到课次">
            <el-option v-for="item in lessonNumList" :key="item.id" :label="item.name"
                       :value="item.id"></el-option>
          </el-select>

          <div class="flex margin-top-12">
            <div v-for="n in 10" :key="n" class="lesson-num"
                 :class="{success: formData.lessonNums === (n + lessonStart - 1)}"
                 @click="onSelectLessonNum(n + lessonStart - 1)">
              <template v-if="formData.lessonNums === (n + lessonStart - 1)">
                <span class="el-icon-check text-bold"></span>
              </template>
              <template v-else>{{ n + lessonStart - 1 }}</template>
            </div>
          </div>
        </el-form-item>

        <el-form-item label="上课情况" prop="signInStatus">
          <el-radio-group v-model="formData.signInStatus">
            <el-radio :label="1">上课</el-radio>
            <el-radio :label="2">请假</el-radio>
            <el-radio :label="3">旷课</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="签到备注" prop="remark">
          <el-input v-model="formData.remark" class="width-300" type="textarea" :rows="3" placeholder="请输入备注"
                    :maxlength="100" show-word-limit></el-input>
        </el-form-item>
      </template>

      <!-- 单个签到  表单 -->
      <template v-else-if="dialogType === 'SignIn'">
        <el-form-item label="消耗课时" prop="lessonHour">
          <el-input-number class="width-300" v-model="formData.lessonHour" controls-position="right" :step="1" :min="1" :max="100"></el-input-number>
          <div class="text-9 height-16 line-height-16 font-12 margin-top-2">说明备注：1课时=1节课</div>
        </el-form-item>

        <el-form-item label="签到课次">
          <div>第{{ formData.lessonNum }}课次</div>
        </el-form-item>

        <el-form-item label="签到日期" prop="signInDate">
          <el-date-picker
              v-model="formData.signInDate"
              class="width-300"
              type="date"
              placeholder="签到日期"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="上课情况" prop="signInStatus">
          <el-radio-group v-model="formData.signInStatus">
            <el-radio :label="1">上课</el-radio>
            <el-radio :label="2">请假</el-radio>
            <el-radio :label="3">旷课</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- <el-form-item label="签到备注" prop="remark">
          <el-input
              v-if="formData.signInStatus"
              v-model="formData.remark" class="width-300" type="textarea" :rows="3"
              placeholder="请输入备注" :maxlength="100" show-word-limit></el-input>
          <div v-else>{{ formData.remark }}</div>
        </el-form-item> -->
        <el-form-item label="签到备注" prop="remark">
          <el-input
              v-model="formData.remark" class="width-300" type="textarea" :rows="3"
              placeholder="请输入备注" :maxlength="100" show-word-limit></el-input>
        </el-form-item>
      </template>


      <el-form-item>
        <el-button type="primary" size="small" @click="onSubmit" :loading="loading.submit">
          {{ dialogInfo.submitText }}
        </el-button>
        <!-- 单签且已签 -->
        <template v-if="initSignInStatus">
          <el-button size="small" @click="onSubmit('cancelSign')">取消签到</el-button>
        </template>
        <template v-else>
          <el-button size="small" @click="close">取消</el-button>
        </template>
      </el-form-item>


    </el-form>

    <!-- <template v-slot:footer>
      <el-button class="margin-right-14" size="small" @click="close">取消</el-button>
      <el-button type="primary" size="small" @click="onSubmit" :loading="loading.submit">
        {{ dialogInfo.submitText }}
      </el-button>
    </template> -->

    

  </el-dialog>
</template>

<script>
export default {
  name: 'DialogSignIn',
  data() {
    let {required} = this.$rules
    let defaultFormData = {
      lessonHour: null,
      signInDate: this.$moment().format('YYYY-MM-DD'),
      lessonNums: [],
      memberIds: [],
      selectMode: 1,
      signInStatus: null
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'BatchSignIn',
      typeMapping: {
        BatchSignIn: {title: '批量签到详情', submitFn: this.$api.ClazzSignIn.batch, msg: '批量签到', submitText: '确定'},
        SignIn: {title: '签到详情', submitFn: this.$api.ClazzSignIn.single, msg: '签到', submitText: '确定'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        lessonHour: [required],
        signInDate: [required],
        lessonNums: [required],
        signInStatus: [required]
      },

      pParams: {},
      lessonStart: 1,

      pickerOptions: {
        // 可选日期  当前日期为止，一年以内
        disabledDate: time => time.getTime() > new Date().getTime() || time.getTime() < new Date().getTime() - 365 * 24 * 60 * 60 * 1000
      },
      loading: {
        submit: false
      },
      // 记录单签时，打开弹窗的初始签到状态
      initSignInStatus: null
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType] || {}
      // 针对单个签到  取消签到时，修改按钮字段、信息提示
      if (this.dialogType === 'SignIn' && this.initSignInStatus) {
        return {
          ...dialogInfo,
          msg: '修改上课状态',
          submitText: '修改上课状态'
        }
      }
      return dialogInfo
    },
    lessonNumList() {
      let lessonNumList = []

      let step = 10
      let maxNum = 60
      for (let i = 0; i < maxNum; i += step) {
        let lessonStart = i + 1
        let lessonEnd = i + step
        lessonNumList.push({
          id: lessonStart,
          name: `${lessonStart}-${lessonEnd}课时`
        })
      }

      return lessonNumList
    },
    // 从弹窗返回出去的数据  预留
    outputData() {
      return this.formData
    },
    submitFormData() {

      if (this.dialogType === 'BatchSignIn') {
        let {lessonNums} = this.formData
        lessonNums = lessonNums ? [lessonNums] : []
        return {
          ...this.formData,
          lessonNums
        }
      }

      if (this.dialogType === 'SignIn') {
        let {clazzId, lessonHour, lessonNum, memberId, remark, signInDate, signInId, signInStatus} = this.formData
        return {clazzId, lessonHour, lessonNum, memberId, remark, signInDate, signInId, signInStatus}
      }
    }
  },

  methods: {
    reset() {
      this.formData = {...this.defaultFormData}
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset()
      this.visible = true
      this.dialogType = data.type

      this.initSignInStatus = data.formData?.signInStatus

      this.formData = {
        ...this.defaultFormData,
        ...data.formData,
        // 批量签到  没有 lessonNums
        lessonNums: (data.formData?.lessonNums || [])[0] || null,
        signInDate: data.formData.signInDate || this.defaultFormData.signInDate
      }

      // 当前页的学生总数
      this.pParams = data.pParams || {}

      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.outputData)
          reject('取消')
        })
      })
    },
    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle', 'close')
    },
    // 确定提交
    async onSubmit(params) {
      await this.$refs.form.validate()
      this.loading.submit = true

      // 取消签到
      if(params === 'cancelSign') {
        // this.submitFormData = {
        //   ...this.submitFormData,
        //   signInStatus: 0
        // }
        this.$set(this.submitFormData, 'signInStatus', 0)
      }

      let {code} = await this.dialogInfo.submitFn(this.submitFormData)
      this.loading.submit = false
      if (code !== 200) return false
      if(this.dialogType === 'SignIn' && !this.submitFormData.signInStatus){
        this.$msg('取消签到')
        this.visible = false
        // 这里用close还是submit，暂时未定
        this.$emit('handle', 'submit')
      } else {
        this.$msg(this.dialogInfo.msg)
        this.visible = false
        this.$emit('handle', 'submit')
      }
      
    },

    // 批量签到  奇次点击加类名且给lessonNums赋值，再点击消去  
    onSelectLessonNum(n) {
      if (this.formData.lessonNums === n) {
        this.formData.lessonNums = null
      } else {
        this.formData.lessonNums = n
      }
    }
  }
}
</script>

<style scoped lang="stylus">
.width-300
  width 354px

.lesson-num
  width 30px
  height 30px
  border 1px solid NEUTRAL_COLOR_3
  border-radius 4px
  text-align center
  line-height 30px
  cursor pointer
  font-weight bold

  & + .lesson-num
    margin-left 6px

  &.success
    background-color SUCCESS_COLOR
    border-color SUCCESS_COLOR
    color NEUTRAL_COLOR_F
</style>