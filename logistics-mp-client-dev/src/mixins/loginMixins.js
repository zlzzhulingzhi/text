// 登录事件通知，需要登录后才执行的，可引入这个文件
export const loginMixins = {
  onLoad() {
    uni.$on(this.$store.state.config.EVENT.LOGIN, this.onUniEventLogin)
  },
  onUnload() {
    uni.$off(this.$store.state.config.EVENT.LOGIN, this.onUniEventLogin)
  },
  methods: {
    onUniEventLogin() {
      this.onLoginSuccess && this.onLoginSuccess()
    }
  }
}