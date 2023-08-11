<template>
  <div>
    <el-dialog :title="`审批 - 访客姓名: ${passOrNotInfo.visitorName}`" :visible.sync="visible" :close-on-click-modal="false" width="500px" append-to-body :before-close="close">
      <div>
        <!-- <div> 请输入相关审批说明：</div> -->
        <el-input type="textarea" v-model="approvalComment" placeholder="请输入相关审批说明" size="normal" clearable maxlength="30" show-word-limit></el-input>
      </div>
      <template v-slot:footer>
        <!-- <el-button size="small" @click="close">取消</el-button> -->
        <el-button type="success" size="small" @click="doPass" :disabled="!approvalComment" :loading="loading.submit">通过</el-button>
        <el-button type="primary" size="small" @click="doNotPass" :disabled="!approvalComment">不通过</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'DialogApply',
  data() {
    return {
      visible: false,
      loading: {
        submit: false
      },
      approvalComment: null,
      passOrNotInfo: {},
    }
  },
  computed: {},
  methods: {
    async open(data = {}) {
      this.passOrNotInfo = data
      this.visible = true
    },
    async doApplyData(params) {
      this.loading.submit = true
      let { code } = await this.$api.visitorManage.update({
        ...params,
        auditResult: this.approvalComment
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$message.success(params.auditStats === 1 ? '已通过' :'不通过')
      this.$emit('success')
    },
    doNotPass() {
      const _params = {
        ...this.passOrNotInfo,
        auditStats: 0
      }
      this.doApplyData(_params)
      
      this.close()
    },
    doPass() {
      const _params = {
        ...this.passOrNotInfo,
        auditStats: 1
      }
      this.doApplyData(_params)

      this.close()
    },
    close() {
      this.approvalComment = null
      this.passOrNotInfo = {}
      this.visible = false
    }
  }
}
</script>

<style lang="stylus" scoped></style>
