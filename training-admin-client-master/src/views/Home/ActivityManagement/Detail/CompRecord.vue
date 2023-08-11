<template>
  <div class="padding-16">
    <el-input class="margin-bottom-8" v-model="url" size="normal"></el-input>
    <el-button type="primary" size="small" @click="goToPreview">预览</el-button>
    <DialogPreview ref="DialogPreview"></DialogPreview>
  </div>
</template>

<script>
import DialogPreview from '@/components/dialog/Course/DialogPreview'

export default {
  components: {
    DialogPreview
  },
  data() {
    return {
      url: 'https://seczone-teach-test-1308712831.cos.ap-guangzhou.myqcloud.com/29/2021-12-16/教学资料.pptx'
    }
  },
  methods: {
    goToPreview() {
      if (!this.url) return this.$message.warning('请输入COS资源链接')
      this.$refs.DialogPreview.open({
        url: this.url
      })
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>