<template>
  <el-dialog :visible.sync="visible" append-to-body :close-on-click-modal="false" :before-close="close" width="448px">

    <div class="text-3 font-16" slot="title">
      <span v-if="(isLogin && step <= 2) || (!isLogin && step <= 1)">{{ dialogInfo.title }}</span>
      <span class="pointer" @click="() => (step--)" v-else><i class="el-icon-arrow-left"></i>返回上一步</span>
    </div>
    <!-- <el-input-number v-model="step" size="medium" :min="1" :max="3" :step="1"></el-input-number> -->

    <el-form ref="form" :model="formData" :rules="rules" label-position="top" hide-required-asterisk>
      <div v-show="step === 1">
        <el-form-item label="手机号" prop="account">
          <el-input v-model.trim="formData.account" placeholder="请输入手机号"
                    :maxlength="11" show-word-limit></el-input>
        </el-form-item>
        <el-form-item>
          <el-button class="width-100p margin-top-16" type="primary" :loading="loading.send" @click="onSendSms">
            <span>获取验证码</span>
          </el-button>
        </el-form-item>
        <el-form-item>
          <div class="text-6 font-12" :style="{ lineHeight: 1.5 }">温馨提示：若手机号已弃用，无法正常获取短信，请联系运维人员。</div>
        </el-form-item>
      </div>
      <div v-show="step === 2">
        <el-form-item label="验证码" prop="sms">
          <div class="font-14 margin-bottom-24" :style="{ lineHeight: 1 }">{{ isLogin ? '' : '已' }}发送至
            {{ formData.account }}
          </div>
          <div class="flex">
            <el-input class="margin-right-12 flex-1" v-model="formData.sms" placeholder="请输入验证码"
                      :maxlength="6"></el-input>
            <el-button class="btn-sms" :loading="loading.send" @click="onSendSms" :disabled="Boolean(smsTime)">
              <span v-if="smsTime">重新获取{{ smsTime }}s</span>
              <span v-else>获取验证码</span>
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="设置密码" prop="password">
          <el-input v-model="formData.password" placeholder="请输入新密码" type="password" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button class="width-100p margin-top-16" type="primary" @click="onSubmit"
                     :loading="loading.submit">确定
          </el-button>
        </el-form-item>
      </div>
      <div class="flex column center-center padding-100" v-show="step === 3">
        <el-image :src="require('@/assets/icons/ic_ok.png')"></el-image>
        <span class="text-3 font-20 margin-top-16">设置成功!</span>
      </div>
    </el-form>

    <!--修改/忘记/设置密码-->
    <!-- <template v-if="/^(ForgetPassword|ChangePassword|ResetPassword)$/.test(dialogType)">
      <el-form class="flex column start-center" ref="form" :model="formData" :rules="rules" size="small">

        <el-form-item prop="account">
          <el-input class="width-300" v-model.trim="formData.account" placeholder="请输入手机号"
                    prefix-icon="iconfont icon-shouji" maxlength="11" show-word-limit
                    autocomplete="new-password" :disabled="dialogInfo.handle.accountDisable"></el-input>
        </el-form-item>

        <el-form-item class="width-300" prop="sms">
          <div class="flex height-32">
            <el-input class="margin-right-12 flex-1" v-model="formData.sms" placeholder="请输入验证码"
                      prefix-icon="el-icon-lock" maxlength="6" autocomplete="new-password"></el-input>
            <el-button class="width-120 height-32" @click="onSendSms" :disabled="Boolean(smsTime)">
              <span v-if="smsTime">重新获取{{ smsTime }}s</span>
              <span v-else>获取验证码</span>
            </el-button>
          </div>
        </el-form-item>

        <el-form-item prop="password">
          <el-input class="width-300" v-model="formData.password" placeholder="请输入新密码" type="password"
                    prefix-icon="el-icon-lock" show-password autocomplete="new-password"></el-input>
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input class="width-300" v-model="formData.confirmPassword" placeholder="请输入确认密码" type="password"
                    prefix-icon="el-icon-lock" show-password autocomplete="new-password-a"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
                     :loading="loading.submit">保存
          </el-button>
          <el-button class="width-80" size="medium" @click="close">取消</el-button>
        </el-form-item>

      </el-form>
    </template> -->

  </el-dialog>
</template>

<script>
import {mapGetters} from 'vuex'
import {SmsTimeStamp} from '@/lib/storage'

export default {
  name: 'DialogChangePassword',
  data() {
    let {required, Phone, Password} = this.$rules
    let defaultFormData = {
      account: null,
      sms: null,
      password: null
      // confirmPassword: null
    }

    return {
      visible: false, // 弹窗开关
      step: 1,

      // 存储弹窗所需数据
      dialogType: 'ForgetPassword',
      typeMapping: {
        ForgetPassword: {
          title: '忘记密码', submitFn: this.$api.Token.resetPassword, handle: {
            accountDisable: false
          }
        },
        ChangePassword: {
          title: '修改密码', submitFn: this.$api.Token.resetPassword, handle: {
            accountDisable: true
          }
        },
        ResetPassword: {
          title: '设置密码', submitFn: this.$api.Token.resetPassword, handle: {
            accountDisable: false
          }
        }
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        account: Phone,
        sms: [required],
        password: [
          required,
          ...Password
          // {
          //   trigger: 'blur', validator: (rule, value, callback) => {
          //     this.formData.confirmPassword && this.$refs.form.validateField('confirmPassword')
          //     callback()
          //   }
          // }
        ]
        // confirmPassword: [
        //   required,
        //   {
        //     trigger: 'blur', validator: (rule, value, callback) => {
        //       if (value !== this.formData.password) {
        //         return callback(new Error('密码不一致，请重新输入！'))
        //       }
        //       callback()
        //     }
        //   }
        // ]
      },
      smsTimeStamp: SmsTimeStamp.get(),
      timeStamp: Date.now(),
      loading: {
        send: false,
        submit: false
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      role: 'common/role',
      sex: 'common/sex',
      isLogin: 'system/isLogin'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    smsTime() {
      if (!this.smsTimeStamp) return 0
      let t = Math.floor((this.smsTimeStamp - this.timeStamp) / 1000) + 60
      if (t < 0) return 0
      return t
    },
    params() {
      return {
        account: this.formData.account,
        code: this.formData.sms,
        password: this.formData.password
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
      if (this.isLogin) this.step = 2

      this.dialogType = data.type
      if (data.formData) {
        for (const k in data.formData) {
          this.formData[k] = data.formData[k]
        }
      }
    },
    // 关闭弹窗
    close() {
      this.step = 1
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true

      let password = await this.$utils.Encrypt(this.params.password)
      let {code} = await this.dialogInfo.submitFn({
        ...this.params,
        password
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$message.success('操作成功')
      this.$emit('success')

      // this.close()
      this.step = 3
    },
    // 发送短信验证码
    async onSendSms() {
      let {pattern, message} = this.$rules.phone
      if (!pattern.test(this.formData.account)) return this.$message.warning(message)
      this.$refs.form.validateField('account')

      this.loading.send = true
      let {code} = await this.$api.Token.sendSms({
        action: 'forgetPwd',
        account: this.formData.account
      })
      this.loading.send = false
      if (code !== 200) return false
      this.step = 2
      SmsTimeStamp.set(this.smsTimeStamp = Date.now())
      this.tick()
    },
    tick() {
      let fn = () => {
        this.timeStamp = Date.now()
        if (!this.smsTime) return false
        requestAnimationFrame(fn)
      }
      fn()
    }
  }
}
</script>

<style scoped lang="stylus">
/deep/ .el-dialog
  border-radius 0

  .el-dialog__header
    border-bottom 1px solid NEUTRAL_COLOR_E

    .el-dialog__title
      font-size 16px

  .el-dialog__body
    padding 16px 64px 12px

    .el-form
      .el-form-item__label
        font-size 20px
        color NEUTRAL_COLOR_3

      .btn-sms
        background #EBF2FF
        color #1D61F2
        border none
</style>