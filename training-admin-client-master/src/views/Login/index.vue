<template>
  <div class="login full-screen flex column">
    <div class="padding-24 flex start-center">
      <el-image class="height-35" :src="logo.Full"></el-image>
    </div>
    <div class="inner-area wrapper overflow flex center-center flex-1">
      <el-image class="image-1" :src="require('@/assets/login/Scene2.png')"></el-image>
      <div class="inner bg-f flex margin-left-46">
        <!-- <div class="flex-1 bg-background padding-top-8">
          <div class="padding-24 flex start-center">
            <el-image class="height-32" :src="logo.Full"></el-image>
            <div class="height-16 margin-left-6 margin-right-6 bg-main width-1"></div>
            <el-image class="height-22" :src="require('@/assets/login/title.png')"></el-image>
          </div>
          <div class="text-center margin-top-44">
            <el-image class="image-1" :src="require('@/assets/login/Scene1.png')"></el-image>
          </div>
        </div> -->

        <el-card class="login-box flex-1 flex column start-center">
          <!-- <h1 slot="header" class="font-20 text-3 line-height-28 text-center margin-24">
            {{ name }}
          </h1> -->

          <el-tabs class="width-300" v-model="loginMode">
            <!--密码登录-->
            <el-tab-pane label="密码登录" name="Password">
              <el-form class="padding-top-24" ref="Password" :model="formData" :rules="rules">
                <el-form-item prop="account">
                  <el-input class="width-300" v-model.trim="formData.account" placeholder="请输入手机号"
                            prefix-icon="iconfont icon-shouji" :maxlength="11"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                  <el-input class="width-300" v-model="formData.password" placeholder="请输入密码" type="password"
                            prefix-icon="el-icon-lock" :maxlength="20" show-password></el-input>
                </el-form-item>
                <el-form-item class="margin-bottom-12" prop="code">
                  <div class="flex height-40">
                    <el-input class="margin-right-12 flex-1" v-model="formData.code" placeholder="请输入校验码"
                              prefix-icon="el-icon-lock" maxlength="4"
                              @keyup.enter.native="onLoginSubmit"></el-input>
                    <div class="pointer relative" @click="refreshCaptcha">
                      <Captcha ref="Captcha" :requestId.sync="formData.requestId" :width="120" :height="40"></Captcha>
                      <el-button class="absolute width-120 height-40" v-show="!timer.captcha">
                        刷新
                      </el-button>
                    </div>
                  </div>
                </el-form-item>
                <div class="font-14 text-right margin-bottom-24">
                  <span class="text-main pointer" @click="onForgetMessage">忘记密码？</span>
                </div>
                <el-button class="width-300" type="primary" @click="onLoginSubmit"
                           :loading="loading.submit">登录
                </el-button>
              </el-form>
            </el-tab-pane>
            <!--短信验证码登录-->
            <el-tab-pane label="验证码登录" name="Sms">
              <el-form class="padding-top-24" ref="Sms" :model="formData" :rules="rules">
                <el-form-item prop="account">
                  <el-input class="width-300" v-model.trim="formData.account" placeholder="请输入手机号"
                            prefix-icon="iconfont icon-shouji" maxlength="11"></el-input>
                </el-form-item>
                <el-form-item prop="sms">
                  <div class="flex height-40">
                    <el-input class="margin-right-12 flex-1" v-model="formData.sms" placeholder="请输入验证码"
                              prefix-icon="el-icon-lock" maxlength="6"
                              @keyup.enter.native="onLoginSubmit"></el-input>
                    <el-button class="width-120 height-40" :loading="loading.send" @click="onSendSms"
                               :disabled="Boolean(smsTime)">
                      <span v-if="smsTime">重新获取{{ smsTime }}s</span>
                      <span v-else>获取验证码</span>
                    </el-button>
                  </div>
                </el-form-item>
                <el-button class="width-300" type="primary" @click="onLoginSubmit"
                           :loading="loading.submit">
                  登录
                </el-button>
              </el-form>
            </el-tab-pane>
            <!--微信扫码登录-->
            <el-tab-pane label="微信登录" name="WeiXin">
              <template v-if="isDev || isTesting || isProduction">
                <WechatCode v-if="loginMode === 'WeiXin'" type="login"></WechatCode>
              </template>
              <div class="flex center-center padding-top-48" v-else>
                <span>测试环境暂停该功能</span>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </div>
    <ICPInFooter class="website-info"></ICPInFooter>
    <DialogChangePassword ref="DialogChangePassword"></DialogChangePassword>
    <DialogBindPhone ref="DialogBindPhone" @success="goToIndex"></DialogBindPhone>
  </div>
</template>

<script>
import {mapActions, mapGetters, mapMutations, mapState} from 'vuex'
import Captcha from '@/components/common/CaptchaServer'
import WechatCode from '@/components/common/WechatCode.vue'
import DialogChangePassword from '@/components/dialog/DialogChangePassword'
import DialogBindPhone from '@/components/dialog/DialogBindPhone'
import {SmsTimeStamp} from '@/lib/storage'

export default {
  name: 'Login',
  components: {
    Captcha,
    WechatCode,
    DialogChangePassword,
    DialogBindPhone
  },
  created() {
    this.smsTimeStamp && this.tick()
    // 微信登录
    if (this.wechatCode) {
      this.wxLogin()
    }   
   
  },
  mounted() {
    this.refreshCaptcha();
  },
  beforeDestroy() {
    clearTimeout(this.timer.captcha)
  },
  data() {
    let {Phone, required} = this.$rules
    return {
      loginMode: 'Password', // 登录模式
      captcha: null, // 验证码
      formData: {
        account: null,
        password: null,
        code: null,
        sms: null,
        requestId: null
      },
      // 登录表单数据
      rules: {
        account: Phone,
        password: [required],
        code: [
          required
          /*{
            trigger: 'blur', validator: (rule, value, callback) => {
              if (value !== this.captcha || !this.timer.captcha) {
                return callback(new Error('校验码错误，请重新输入！'))
              }
              callback()
            }
          }*/
        ],
        sms: [required]
      },
      
      timer: {
        captcha: null
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
    ...mapState('config', {
      logo: 'logo',
      name: 'name'
    }),
    ...mapState('system', {
      wechatCode: 'wechatCode'
    }),
    ...mapGetters({
      isDev: 'isDev',
      isTesting: 'isTesting',
      isProduction: 'isProduction'
    }),
    smsTime() {
      if (!this.smsTimeStamp) return 0
      let t = Math.floor((this.smsTimeStamp - this.timeStamp) / 1000) + 60
      if (t < 0) return 0
      return t
    }
  },
  methods: {
    ...mapMutations({
      tokenUpdate: 'system/tokenUpdate',
      userInfoUpdate: 'system/userInfoUpdate'
    }),
    ...mapActions({
      appInit: 'appInit'
    }),
    // 刷新校验码
    async refreshCaptcha() {
      clearTimeout(this.timer.captcha)
      // this.captcha = Date.now().toString().slice(-4)
      this.$refs.Captcha.getCaptcha()
      this.timer.captcha = setTimeout(() => {
        this.timer.captcha = null
      }, 60000)
    },
    // 发送短信验证码
    async onSendSms() {
      let {pattern, message} = this.$rules.phone
      if (!this.formData.account) return this.$message.warning('请输入手机号')
      if (!pattern.test(this.formData.account)) return this.$message.warning(message)
      this.$refs.Sms.validateField('account')

      if (this.loading.send) return false
      this.loading.send = true
      let {code} = await this.$api.Token.sendSms({
        action: 'login',
        account: this.formData.account
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
    },
    // 操作 - 登录
    async onLoginSubmit(ev) {
      await this.$refs[this.loginMode].validate()
      this.loading.submit = true
      ev.target.blur()
      console.log(`onLoginSubmit:发起${this.loginMode}登录`)

      let result = null
      let {account, password, sms, requestId, code} = this.formData
      if (this.loginMode === 'Password') {

        password = await this.$utils.Encrypt(password)
        // 密码登录
        result = await this.$api.Token.login({account, password, requestId, code})
      } else {
        // 验证码登录
        result = await this.$api.Token.loginSms({account, code: sms})
      }

      // 登录失败
      if (result.code !== 200) {
        this.refreshCaptcha().then()
        return this.loading.submit = false
      }

      // 登录成功
      await this.tokenUpdate(result.data.access_token)
      // 配合后台，在此储存userInfo信息 - A部分
      await this.userInfoUpdate(result.data)

      this.$message.success('登录成功')
      this.$refs[this.loginMode].resetFields()
      await this.goToIndex(result.data)
      this.loading.submit = false
    },
    onForgetMessage() {
      this.$refs.DialogChangePassword.open({
        type: 'ForgetPassword',
        formData: {
          account: this.formData.account
        }
      })
    },
    // 微信登录
    async wxLogin() {
      let {code, data} = await this.$api.Token.loginSocial({
        code: this.wechatCode
      })
      this.$store.commit('system/setWechatCode', null)
      if (code === 200) {
        // 登录成功
        await this.tokenUpdate(data.access_token)
        await this.userInfoUpdate(data)
        this.$message.success('登录成功')
        this.$refs[this.loginMode].resetFields()
        this.goToIndex(data)
      } else if (code === 201) {
        // 未绑定手机
        this.$refs.DialogBindPhone.open({
          wechatInfo: data
        })
      }
    },
    async goToIndex(data) {
      // await this.appInit()
      await this.$router.replace({name: 'Index'}, () => {
        if (this.initPassword !== this.formData.password) return false
        if (!data.first_login) return false
        this.$notify({
          title: '提示',
          message: '您正在使用默认密码，为了您的账户安全，请尽快修改密码。点击前往',
          duration: 0,
          offset: parseInt(this.constant.headerHeight) + 16,
          onClick: () => {
            this.$refs.DialogChangePassword.open({
              type: 'ChangePassword',
              formData: {
                account: this.formData.account
              }
            })
          }
        })
      })
    }
  }
}
</script>

<style scoped lang="stylus">
.login
  background-image url('../../assets/login/bg1.png')
  background-size cover

  .wrapper
    min-width 800px
    min-height 635px

    .image-1
      width 500px
      height 500px
      flex-shrink 0

    .inner
      width 412px
      height 490px
      padding 56px
      box-shadow 0 3px 27px rgba(0, 0, 0, 0.1)
      border-radius 4px


      .width-1
        width 1px

      .login-box
        box-shadow none

        .el-tabs
          >>> .el-tabs__header
            padding-left 0

            .is-active
              font-size 16px

          .el-button.absolute
            top 0
            left 0
            background-color rgba(255, 255, 255, 0.8)

  .website-info
    min-width 800px
    font-family Microsoft YaHei
    background-color transparent
</style>


<style scoped lang="stylus">
@media only screen and (max-width: 1920px)
  .inner-area
    zoom 1.2

@media only screen and (min-width: 1920px)
  .inner-area
    zoom 1.2

@media only screen and (max-width: 1600px)
  .inner-area
    zoom 1.0

@media only screen and (max-width: 1440px)
  .inner-area
    zoom .9

</style>