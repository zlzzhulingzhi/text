<template>
  <view class="p-8">
    <view class="text-2xl font-bold mb-8">{{ identity | idList }}登录</view>
    <uni-btn-phone @success="getPhoneSuccess">
      <u-button type="primary" plain shape="circle" text="微信快捷登录"></u-button>
    </uni-btn-phone>
  </view>
</template>

<script>
import { mapState, mapGetters } from 'vuex'

export default {
  name: 'Login',
  data() {
    return {
      loading: {
        submit: false
      },
      form: {
        account: '', // 手机号
        code: '', // 短信验证码
        mode: 'weixin' // 手机号获取方式: manual | weixin
      },
      rules: {
        account: { type: 'string', required: true, message: '必填项', trigger: ['blur', 'change'] },
        code: { type: 'string', required: true, message: '必填项', trigger: ['blur', 'change'] }
      },
      smsTime: 0,
      identity: '',
      reBind: false
    }
  },
  computed: {
    ...mapState('system', {
      authInfo: 'authInfo'
    }),
    ...mapGetters({
      isLogin: 'system/isLogin',
      idList: 'common/idList'
    })
  },
  onLoad(options) {
    let params = this.$utils.getOptions(options)
    this.identity = params.id
    this.reBind = Boolean(params.reBind)
    if (this.identity !== 'student' && !this.reBind) this.checkLogin()
  },
  methods: {
    // 机构/平台自动登录
    async checkLogin() {
      uni.showLoading({ title: '请稍候' })
      await this.$store.dispatch('appLogin', { identity: this.identity }, { root: true })
      setTimeout(() => {
        if (this.isLogin) uni.$u.route({
          url: this.$utils.getRoutePath('NewHome'),
          type: 'tab'
        })
        uni.hideLoading()
      }, 300)
    },
    // 获取微信手机号
    getPhoneSuccess(phone) {
      this.form.account = phone
      this.form.mode = 'weixin'
      this.onLogin() // 顺便登录
    },
    // 机构/平台登录
    async onLogin() {
      this.loading.submit = true
      let { code, data } = await this.$api.AuthAdmin.binding({
        phone: this.form.account,
        code: this.form.code,
        openId: this.authInfo.openId,
        loginCode: this.identity,
        reBind: this.reBind,
        mode: this.form.mode
      })
      this.loading.submit = false
      if (code !== 200 || !data) return uni.$u.toast('您未绑定相关平台用户，请绑定后再登陆！')
      this.$store.commit('system/updateToken', data.access_token)
      this.$store.commit('system/updateUserInfo', { ...data, access_token: undefined })
      this.$store.commit('system/updateIdentity', this.identity)
      uni.$u.route({
        url: this.$utils.getRoutePath('NewHome'),
        type: 'tab'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .u-input {
  height: 40px;
}

.register {
  text-decoration: underline;
}

.wechat_icon {
  left: 50%;
  transform: translateX(-50%);
}
</style>