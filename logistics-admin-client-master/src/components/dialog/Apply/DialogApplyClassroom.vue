<template>
  <div>
    <el-dialog :title="`确认审批 - ${classroomInfo.courseName}`" :visible.sync="visible" :close-on-click-modal="false" width="500px" append-to-body :before-close="close">
      <div>
        <!-- <div> 请输入相关审批说明：</div> -->
        <el-input type="textarea" v-model="approvalComment" placeholder="请输入相关审批说明" size="normal" clearable maxlength="30" show-word-limit></el-input>
      </div>
      <template v-slot:footer>
        <el-button size="small" @click="close">取消</el-button>
        <el-button type="primary" size="small" @click="doNotPass" :disabled="!approvalComment">驳回</el-button>
        <el-button type="success" size="small" @click="doPass" :disabled="!approvalComment" :loading="loading.submit">通过</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      loading: {
        submit: false
      },
      approvalComment: null,
      classroomInfo: null,
    }
  },
  computed: {},
  methods: {
    async open(data = {}) {
      this.classroomInfo = data
      this.visible = true
    },
    async doApplyData(params) {
      this.loading.submit = true
      let { code } = await this.$api.Approve.settle(params)
      this.loading.submit = false
      if (code !== 200) return false
      this.$message.success(params.result === 1 ? '已通过' :'已驳回')
      this.$emit('success')
    },
    doNotPass() {
      const _params = {
        result: 0,
        applyId: this.classroomInfo.id,
        comment: this.approvalComment,
        attachList: null,
        curNodeId:this.classroomInfo.curNodeId
      }
      this.doApplyData(_params)
      
      // this.$message.success('已驳回')
      this.close()
    },
    doPass() {
      const _params = {
        result: 1,
        applyId: this.classroomInfo.id,
        comment: this.approvalComment,
        attachList: null,
        curNodeId:this.classroomInfo.curNodeId
      }
      this.doApplyData(_params)
      this.close()
    },
    close() {
      this.approvalComment = null
      this.visible = false
    }
  }
}
</script>

<style lang="stylus" scoped></style>
