<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="448px">

    <div class="font-14 text-6 margin-bottom-24">温馨提示：首次微信扫码登录，需绑定手机号</div>

    <el-form ref="form" :model="formData" :rules="rules" label-position="top" hide-required-asterisk>
      <el-form-item label="手机号" prop="phone">
        <el-input class="width-100p" v-model.trim="formData.phone" placeholder="请输入手机号"
           :maxlength="11" show-word-limit autocomplete="new-password"></el-input>
      </el-form-item>
      <el-form-item class="width-100p" label="验证码" prop="code">
        <div class="flex">
          <el-input class="margin-right-12 flex-1" v-model="formData.code" placeholder="请输入验证码"
             :maxlength="6" show-word-limit autocomplete="new-password"></el-input>
          <el-button class="btn-sms" :loading="loading.send" @click="onSendSms" :disabled="Boolean(smsTime)">
            <span class="font-14" v-if="smsTime">重新获取{{ smsTime }}s</span>
            <span class="font-14" v-else>获取验证码</span>
          </el-button>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button class="width-100p margin-top-16" type="primary" @click="onSubmit" :loading="loading.submit">绑定</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import {mapState, mapMutations} from 'vuex'
import {SmsTimeStamp} from '@/lib/storage'

export default {
  name: 'DialogBindPhone',
  data() {
    let {required, Phone, Password} = this.$rules
    let defaultFormData = {
      phone: null,
      code: null
    }

    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'BindPhone',
      typeMapping: {
        BindPhone: {title: '绑定手机', submitFn: this.$api.Token.bindingSocial}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        phone: Phone,
        code: [required]
      },
      smsTimeStamp: SmsTimeStamp.get(),
      timeStamp: Date.now(),
      loading: {
        send: false,
        submit: false
      },
      wechatInfo: {}
    }
  },
  computed: {
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
      if (this.dialogType === 'BindPhone') {
        return {
          ...this.formData,
          ...this.wechatInfo
        }
      }
    }
  },

  methods: {
    ...mapMutations({
      tokenUpdate: 'system/tokenUpdate',
      userInfoUpdate: 'system/userInfoUpdate'
    }),
    reset() {
      this.formData = {...this.defaultFormData}
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset()

      this.visible = true

      this.dialogType = data.type || 'BindPhone'
      this.wechatInfo = data.wechatInfo
    },
    // 关闭弹窗
    async close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true

      let {code, data} = await this.dialogInfo.submitFn(this.params)
      this.loading.submit = false
      if (code !== 200) return false
      this.$message.success('操作成功')
      await this.tokenUpdate(data.access_token)
      await this.userInfoUpdate(data)
      this.$emit('success', data)

      await this.close()
    },
    // 发送短信验证码
    async onSendSms() {
      let {pattern, message} = this.$rules.phone
      if (!pattern.test(this.formData.phone)) return this.$message.warning(message)
      // this.$refs.form.validateField('phone')

      if (this.loading.send) return false
      this.loading.send = true
      let { code } = await this.$api.Token.sendSmsCheck({
        action: 'bindPhone',
        account: this.formData.phone
      })
      this.loading.send = false
      if (code !== 200) return false
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
  /deep/.el-dialog
    border-radius 0

    .el-dialog__header
      border-bottom 1px solid NEUTRAL_COLOR_E
      
      .el-dialog__title
        font-weight normal

    .el-dialog__body
      padding 16px 64px 32px

      .el-form
        .el-form-item__label
          font-size 20px
          color NEUTRAL_COLOR_3

        .btn-sms
          background #EBF2FF
          color #1D61F2
          border none
</style>