<template>
  <el-dialog class="confirm-dialog" :visible.sync="visible" append-to-body :title="dialogInfo.title"
             :close-on-click-modal="false"
             :before-close="close" width="600px" :show-close="dialogInfo.showClose">

    <template v-if="/^OrderCancel$/.test(dialogType)">
      <div slot="title" class="font-16 padding-left-24">
        {{ dialogInfo.title }}
      </div>

      <div class="font-16 line-height-16 padding-left-24 padding-bottom-40">
        {{ dialogInfo.content }}
      </div>
    </template>

    <template v-else-if="/^(MarkingPaperFinish|MarkingPaperNext)$/.test(dialogType)">
      <div class="font-16 line-height-16 padding-bottom-28">
        {{ dialogInfo.content }}
      </div>
    </template>

    <template v-slot:footer>
      <template v-if="/^OrderCancel$/.test(dialogType)">
        <el-button class="margin-right-14" type="primary" round size="medium" @click="close">
          {{ dialogInfo.cancelButtonText }}
        </el-button>
        <el-button class="bg-background border-background text-main" round size="medium" @click="onSubmit" :loading="loading.submit">
          {{ dialogInfo.confirmButtonText }}
        </el-button>
      </template>
      <div v-else class="text-center">
        <el-button class="margin-right-14" type="primary" size="medium" @click="close">
          {{ dialogInfo.cancelButtonText }}
        </el-button>
        <el-button class="bg-background border-background text-main" size="medium" @click="onSubmit" :loading="loading.submit">
          {{ dialogInfo.confirmButtonText }}
        </el-button>
      </div>
    </template>

  </el-dialog>
</template>

<script>
export default {
  name: 'DialogConfirm',
  data() {
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'OrderCancel',
      typeMapping: {
        OrderCancel: {
          title: '取消订单？',
          cancelButtonText: '我再想想',
          confirmButtonText: '取消订单',
          content: '取消后，将不再保留当前课程名额',
          showClose: false
        },
        MarkingPaperFinish: {
          title: '确认退出吗？',
          cancelButtonText: '继续阅卷',
          confirmButtonText: '结束阅卷',
          content: '含有未阅卷的试题，是否退出？',
          showClose: true
        },
        MarkingPaperNext: {
          title: '',
          cancelButtonText: '取消',
          confirmButtonText: '下一个',
          content: '此试卷含有未阅卷的试题，是否进入下一个？',
          showClose: true
        }
      },

      loading: {
        submit: false
      }
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    }
  },

  methods: {
    // 打开弹窗
    open(data = {}) {
      this.visible = true

      this.dialogType = data.type || 'OrderCancel'
      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve({})
          reject('取消')
        })
      })
    },
    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle')
    },
    // 确定提交
    async onSubmit() {
      this.visible = false
      this.$emit('handle','submit')
    }
  }
}
</script>

<style scoped lang="stylus">
/*.confirm-dialog
  >>> .el-dialog
    border-radius 4px

    .el-dialog__header
      margin 0
      padding 0
      height 72px
      border-bottom none
      line-height 72px

    .el-dialog__body
      padding 0

    .el-dialog__footer
      padding 0 24px 24px

      .el-button
        width 88px
        height 36px
        padding 0*/
</style>