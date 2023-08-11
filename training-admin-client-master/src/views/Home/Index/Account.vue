<template>
  <div class="flex center-center">
    <div class="main">
      <h2>账号设置</h2>
      <el-divider></el-divider>
      <div class="font-16 margin-bottom-16">绑定微信</div>
      <div class="flex between-center">
        <div class="flex center-center">
          <el-image class="avatar" :src="authInfo.headImgUrl" fit="fill" v-if="authInfo.headImgUrl"></el-image>
          <el-avatar :size="100" v-else>{{ authInfo.nickName || authInfo.realName || '--' }}</el-avatar>
          <span class="font-16 margin-left-16">{{ authInfo.nickName || authInfo.realName || '--' }}</span>
        </div>
        <el-button size="medium" @click="rebindWx" v-if="authInfo.uid">更换绑定</el-button>
        <el-button size="medium" @click="bindWx" v-else>绑定微信</el-button>
      </div>
      <el-divider></el-divider>
      <div class="flex between-center">
        <div class="flex center-center font-16">
          <div class="width-100">手机号</div>
          <span class="margin-left-16">{{ authInfo.phone || '--' }}</span>
        </div>
        <el-button size="medium" @click="setPwd('ResetPassword')" v-if="authInfo.pwdMiss">设置密码</el-button>
        <el-button size="medium" @click="setPwd('ChangePassword')" v-else>修改密码</el-button>
      </div>
      <el-divider></el-divider>
      <div class="flex center-center margin-top-32">
        <el-button size="medium" @click="$router.push({ name: 'Index' })">返回首页</el-button>
      </div>
    </div>
    <DialogBindWeixin ref="DialogBindWeixin"></DialogBindWeixin>
    <DialogChangePassword ref="DialogChangePassword" @success="getOauthInfo()"></DialogChangePassword>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import DialogBindWeixin from '@/components/dialog/DialogBindWeixin'
import DialogChangePassword from '@/components/dialog/DialogChangePassword'

export default {
  name: 'IndexAccount',
  components: {
    DialogBindWeixin,
    DialogChangePassword
  },
  data() {
    return {
      loading: false,
      authInfo: {}
    }
  },
  computed: {
    ...mapState('system', {
      wechatCode: 'wechatCode'
    })
  },
  created() {
    this.getOauthInfo()
    // 微信绑定
    if (this.wechatCode) {
      this.wxBind()
    }
  },
  methods: {
    async getOauthInfo() {
      this.loading = true
      let { code, data } = await this.$api.UserOauth.info()
      this.loading = false
      if (code !== 200) return false
      this.authInfo = data
    },
    // 绑定微信
    bindWx() {
      this.$refs.DialogBindWeixin.open()
    },
    // 更绑微信
    rebindWx() {
      if (!this.authInfo.id) return false
      this.$confirm('更换绑定会先解绑当前微信，是否继续？', '提示', {
        type: 'warning'
      }).then(async () => {
        let { code } = await this.$api.UserOauth.unbindSocial({
          id: this.authInfo.id
        })
        if (code !== 200) return false
        this.getOauthInfo()
        this.$refs.DialogBindWeixin.open()
      }).catch(() => {})
    },
    // 设置密码
    setPwd(type) {
      if (!this.authInfo.phone) return false
      this.$refs.DialogChangePassword.open({
        type: type,
        formData: {
          account: this.authInfo.phone
        }
      })
    },
    // 微信绑定
    async wxBind() {
      let { code, data } = await this.$api.Token.oauth({
        code: this.wechatCode
      })
      this.$store.commit('system/setWechatCode', null)
      if (code !== 200) return false
      let result = await this.$api.UserOauth.bindingSocial({
        account: this.authInfo.phone,
        uid: data.unionId,
        nickName: data.nickname,
        headImgUrl: data.headImgUrl
      })
      if (result.code !== 200) return false
      this.$message.success('绑定成功')
      this.getOauthInfo()
    }
  }
}
</script>

<style lang="stylus" scoped>
.main
  padding 16px
  width 1200px

  .avatar
    width 100px
    height 100px
    border-radius 50px
</style>