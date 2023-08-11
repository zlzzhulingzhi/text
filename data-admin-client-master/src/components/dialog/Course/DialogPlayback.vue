<template>
  <el-dialog custom-class="playback-dialog" :visible.sync="visible" width="1000px" top="5vh" append-to-body :close-on-click-modal="false" destroy-on-close @close="close">
    <div class="flex center-center play-container">
      <video :id="id" preload="auto" playsinline webkit-playsinline></video>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'DialogPlayback',
  data() {
    return {
      tcplayer: null,
      id: 'player-container-id',
      visible: false
    }
  },
  beforeDestroy() {
    this.tcplayer && this.tcplayer.dispose()
  },
  methods: {
    // 打开弹窗
    async open(data = {}) {
      if (!data.url) return this.$message.warning('无效链接')
      this.visible = true
      this.id = `player-container-${new Date().getTime()}`
      this.$nextTick(() => {
        this.tcplayer = TCPlayer(this.id, {
          autoplay: true
        })
        this.tcplayer.src(data.url)
        // this.tcplayer.play()
      })
    },
    // 关闭弹窗
    close() {
      this.tcplayer.pause()
      this.visible = false
    }
  }
}
</script>

<style lang="stylus">
  .playback-dialog
    &.el-dialog
      background transparent
      box-shadow none

      .el-dialog__header
        border-bottom none
        margin-bottom 8px

        .el-dialog__headerbtn
          right 0

          .el-dialog__close
            color #fff
            font-size 1.2rem

      .el-dialog__body
        padding 10px 0

    .play-container
      width 100%
      height 700px
      background #000
</style>