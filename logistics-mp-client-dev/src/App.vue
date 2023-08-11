<script>
export default {
  onLaunch: function () {
    this.$logger.info(`App Launch (${process.env.NODE_ENV})`, this.$logger.TAG.LAUNCH)
  },
  onShow: function () {
    // this.$logger.info('App Show', this.$logger.TAG.LAUNCH)
  },
  onHide: function () {
    // this.$logger.info('App Hide', this.$logger.TAG.LAUNCH)
  }
}
</script>

<style lang="scss">
/*每个页面公共css */
@import 'tailwindcss/tailwind.css';
@import "uview-ui/index.scss";
@import "style/index.scss";

uni-page-body,
html,
body {
  height: 100%;
}
</style>
