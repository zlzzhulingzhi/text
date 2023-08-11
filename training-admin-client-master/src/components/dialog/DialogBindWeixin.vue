<template>
  <el-dialog :visible.sync="visible" append-to-body title="绑定微信" :close-on-click-modal="false"
             :before-close="close" width="500px">

    <WechatCode type="bind"></WechatCode>

    <template v-slot:footer>
      <el-button class="margin-right-14" size="small" @click="close">取消</el-button>
    </template>
  </el-dialog>
</template>

<script>
import WechatCode from '@/components/common/WechatCode.vue'

export default {
  name: 'DialogBindWeixin',
  components: {
    WechatCode
  },
  data() {
    return {
      visible: false // 弹窗开关
    }
  },

  methods: {
    // 打开弹窗
    open(data = {}) {
      this.visible = true
    },
    // 关闭弹窗
    close() {
      this.visible = false
    }
  }
}
</script>

<style scoped lang="stylus">

</style>