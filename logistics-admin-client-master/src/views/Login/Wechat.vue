<!--当前的微信登录和绑定不会用到这个文件-->
<template>
  <div class="page-wechat-skip">
    <div class="inner text-center" :class="status.className">
      <div>
        <div class="code">
          <el-image class="margin-10" :src="require('@/assets/login/QRCodeTips.png')"></el-image>
          <div class="code-content">
            <i class="icon" :class="status.icon"></i>
            <div class="box">{{ status.message }}</div>
          </div>
        </div>
      </div>
<!--      <div class="description">
        <div>请使用微信扫描二维码登录</div>
        <div>“学易优信息系统平台”</div>
      </div>-->
    </div>
  </div>
</template>

<script>
import {mapMutations, mapState} from 'vuex'
import url from 'url'

export default {
  name: "Skip",
  created() {
    if (this.type === 'login') return this.login()
    if (this.type === 'bind') return this.bind()
  },
  data() {
    return {
      type: this.$route.params.type,
      message: null,
      code: null
    }
  },

  computed: {
    ...mapState('system', {
      token: 'token'
    }),
    status() {
      let loading = {
        className: 'text-3',
        message: '加载中...',
        icon: 'el-icon-loading'
      }
      if (this.type === 'bind') {
        loading.message = '绑定中...'
      }
      if (this.code === null) return loading

      if (this.code === 200) return {
        className: 'text-success',
        // message: this.type === 'bind' ? '绑定成功' : '登录成功',
        message: this.message,
        icon: 'el-icon-success'
      }

      if (this.code !== 200) return {
        className: 'text-error',
        // message: this.type === 'bind' ? '绑定失败' : '登录失败',
        message: this.message,
        icon: 'el-icon-warning'
      }
      return loading
    }
  },

  methods: {
    ...mapMutations({
      updateLoginInfo: 'system/updateLoginInfo'
    }),

    // 微信扫码登录
    async login() {
      let {code, message, data} = await this.$api.Token.loginSocial({
        code: this.$route.query.code
      })

      this.message = message
      this.code = code

      let {protocol, host} = location
      if (code === 200) {
        // 登录成功
        this.updateLoginInfo(data)
        return window.open(url.format({protocol, host}), '_top')
      } else if (code === 201) {
        // 未绑定手机
        return window.open(url.format({protocol, host}), '_top')
      }
    },

    // 绑定微信
    async bind() {
      let {code, message, data} = await this.$api.Token.oauth({
        code: this.$route.query.code
      })

      this.message = message
      this.code = code
      if (code !== 200) return false
      let {protocol, host, hash} = location
      window.open(url.format({protocol, host, hash}), '_top')
    }
  },
}
</script>

<style scoped lang="stylus">
.page-wechat-skip
  padding-top 40px

  .inner
    width 300px
    //height 300px

    .code
      width 300px
      height 300px
      position relative

      .el-image
        position absolute
        top 0
        left 0

      .code-content
        position absolute
        top 0
        left 0
        width 300px
        height 300px
        background-color rgba(255, 255, 255, .9)

        .icon
          margin-top 88px
          font-size 45px

        .box
          //width 120px
          //height 48px
          max-width 243px
          padding 8px
          margin 16px auto
          background-color rgba(51, 51, 51, .9)
          border-radius 4px
          font-size 16px
          //line-height 48px
          color NEUTRAL_COLOR_F


    .description
      font-size 13px

</style>