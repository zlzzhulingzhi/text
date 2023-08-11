<template>
  <el-dialog :visible.sync="visible" append-to-body :close-on-click-modal="false"
             :before-close="close" width="420px">

  </el-dialog>
</template>

<script>
export default {
  name: 'DialogPanelSetting',
  data() {
    return {
      visible: false, // 弹窗开关
      loading: {
        submit: false
      }
    }
  },
  computed: {
    outputData() {
      return {}
    }
  },
  methods: {
    // 打开弹窗
    async open(data = {}) {
      this.visible = true
      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.outputData)
          reject('取消')
        })
      })
    },

    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle', 'close')
    },
    // 确定提交
    async onSubmit() {
      this.visible = false
      this.$emit('handle', 'submit')
    }

  }
}
</script>

<style scoped lang="stylus">
</style>