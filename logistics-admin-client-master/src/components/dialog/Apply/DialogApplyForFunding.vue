<template>

  <el-dialog :visible.sync="visible" append-to-body :close-on-click-modal="false" :before-close="close" width="600px">
    <div slot="title" class="flex">
      <div class="font-16 text-3 text-bold">
        {{ `审批详情 - ${classroomInfo?.orgName ? classroomInfo.orgName : classroomInfo?.projectName}` }}
      </div>
      <div class="approving-status"
        :class="`approving-status-${(flowStatus?.find(a => a.id === classroomInfo?.flowStatus) || {}).status}`">
        {{ classroomInfo?.flowStatus | flowStatus }}
      </div>
    </div>
    <div class="stepsTitle">
      审批流程
    </div>
    <div class="approvalProcess">
      <el-steps :active="activeStep" direction="vertical">
        <el-step v-for="(item, index) in approvalProcessProject" :key="index" :id="item.id">

          <template slot="icon">
            <div v-if="item.status !== 21" class="width-24 height-24 radius-12 flex center-center approving-icon">
              <el-image :src="require('@/assets/icons/approve.png')" class="height-14 width-12">
              </el-image>
            </div>
            <el-image v-else :src="require('@/assets/icons/pass_icon.png')"
              class="height-24 width-24 radius-12 approving-icon">
            </el-image>
          </template>

          <template slot="title">
            <div class="margin-left-10 flex start-center">
              <span class="font-14 text-3 text-bold">{{ item.nodeName }}</span>
              <div class="width-6 height-6 radius-2 margin-left-10 back-point">
              </div>
              <span class="font-14 text-3 margin-left-6 approve-node"
                :class="`approve-node-${item.status === 11 ? 'warning' : ''}`">{{
                    item.status | approveNodeStatus
                }}</span>
            </div>
          </template>

          <template slot="description">
            <div class="processing_content_td">
              <div class="processing_content_detail">
                <el-avatar :size="24" fit="contain" :src="require('@/assets/icons/avatar.png')">
                </el-avatar>
                <span class="font-size-12 margin-left-16 margin-top-4 text-6">{{
                    item.managerInfo
                }}</span>
              </div>
              <div class="processing_content_detail" v-if="item.approveApplyTime !== null"><span
                  class="text-6 font-size-12">{{
                      item.approveApplyTime |
                      formatTime('MM月DD日 HH:mm')
                  }}</span> </div>
            </div>

            <div class="margin-top-10 margin-bottom-16" v-if="item.comment !== null">
              <span class="text-3 font-14">审批说明</span>
              <span class="text-6 font-14 margin-left-10">{{ item.comment }}</span>
            </div>
          </template>

        </el-step>
      </el-steps>
    </div>

    <div v-if="classroomInfo?.flowStatus === 1 && dialogType !== 'detail'">
      <!-- <div> 请输入相关审批说明：</div> -->
      <el-input type="textarea" v-model="approvalComment" placeholder="请输入相关审批说明" size="normal" clearable maxlength="30"
        show-word-limit></el-input>
    </div>
    <template v-slot:footer v-if="classroomInfo?.flowStatus === 1 && dialogType !== 'detail'">
      <!-- <el-button size="small" @click="close">取消</el-button> -->
      <div class="flex">
        <el-button type="primary" size="small" @click="doPass" :disabled="!approvalComment" :loading="loading.submit">通过
        </el-button>
        <el-button size="small" @click="doNotPass" :disabled="!approvalComment">驳回</el-button>
      </div>

    </template>
  </el-dialog>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'DialogApplyForFunding',
  components: {
  },
  data() {
    return {
      visible: false,
      loading: {
        submit: false
      },
      approvalComment: null,
      classroomInfo: null,
      dialogType: '',
      typeMapping: {
        qualification: {
          type: 'qualification',
          sumbitAPI: this.$api.Approve.qualification
        },
        course: {
          type: 'course',
          sumbitAPI: this.$api.Approve.allowance
        },
        activity: {
          type: 'activity',
          sumbitAPI: this.$api.Approve.activity
        }
      },

      // 审批数据
      approvalProcessProject: [],


      // 最终审批通过  升级为万人计划时  所需数据
      // isConfirm: null,
      // key: null,
    }
  },
  computed: {
    ...mapGetters({
      flowStatus: 'common/flowStatus',
      approveNodeStatus: 'common/approveNodeStatus'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    activeStep() {
      return this.approvalProcessProject.filter(a => a.status === 21).length;
    }
  },
  methods: {
    ...mapActions({
      getQualificationNum: 'system/getQualificationNum',
      getCostNum: 'system/getCostNum',
      getClassroomApplyNum: 'system/getClassroomApplyNum',
    }),
    async open(data = {}) {
      this.visible = true
      this.classroomInfo = data
      this.dialogType = data.type
      let { code, data: d } = await this.$api.Approve.approveDetail({
        id: data.id
      });
      if (code !== 200) return false;
      this.approvalProcessProject = d.workflowInstanceNodeResponses;
    },
    async doApplyData(params) {
      this.loading.submit = true;
      let result = {};
      if (this.dialogType === 'classroom') {
        result = await this.$api.Approve.settle(params)
      } else {
        result = await this.dialogInfo.sumbitAPI(params)
      }
      const {code} = result
      // if(result.data) {
      //   const { isConfirm,key } = result.data;
      //   this.isConfirm = isConfirm
      //   this.key = key
      // }
      this.loading.submit = false
      if (code !== 200) return false
      // this.$message.success(params.result === 1 ? '已通过' : '已驳回')
      // if(this.isConfirm) {
      //   this.$confirm(`确认将机构“${this.classroomInfo.orgName}”设置为“万人计划机构”吗？`, {
      //     title: '万人计划确认'
      //   }).then(async () => {
      //     let {code} = await this.$api.Approve.setOrgPlanOrg({
      //       key: this.key
      //     })
      //     if(code !== 200) return false
      //     this.$msg('已通过，且机构设置为“万人计划机构”')
      //     this.$emit('success')
      //   }).catch(() => {
      //     this.$msg('已通过，但机构未设置为“万人计划机构”')
      //     this.$emit('success')
      //   })
      // }
      // if(!this.isConfirm) {
      //   this.$message.success(params.result === 1 ? '已通过' : '已驳回')
      //   this.$emit('success')
      // }
      // 更新剩余数量
      if(this.dialogType === 'qualification') {
        this.getQualificationNum()
      }
      
      if(this.dialogType === 'course' || this.dialogType === 'activity') {
        this.getCostNum()
      }
      
      if(this.dialogType === 'classroom') {
        this.getClassroomApplyNum()
      }
      

      this.$message.success(params.result === 1 ? '已通过' : '已驳回')
      this.$emit('success')
    },
    doNotPass() {
      const _params = {
        result: 0,
        applyId: this.classroomInfo.id,
        comment: this.approvalComment,
        attachList: null,
        curNodeId: this.classroomInfo.curNodeId
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
        curNodeId: this.classroomInfo.curNodeId
      }
      this.doApplyData(_params)
      this.close()
    },
    close() {
      this.visible = false
      this.approvalComment = null
      // this.classroomInfo = null
    }
  }
}
</script>

<style  scoped lang="stylus">
.stepsTitle {
  margin: 10px 0px 10px 10px;
  font-family: Microsoft YaHei-Bold, Microsoft YaHei;
  font-weight: bold;
  color: #313131;
  line-height: 13px;
}

.approvalProcess {
  color: #9EADC4;
  font-size: 14px;
  margin-left: 20px;
  margin-right: 0px;
  margin-top: 20px;
}

.processing_content_detail {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.processing_content_td {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-left: 10px;
  /* background-color: yellow; */
  width: 450px;
}

.approving-status {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 51px;
  height: 23px;
  border-radius: 2px;
  font-size: 13px;
  font-family: Microsoft YaHei-Regular, Microsoft YaHei;
  line-height: 13px;
  margin-left: 20px;

  &.approving-status-warning{
    border 1px solid WARNING_COLOR
    color WARNING_COLOR
  }
  &.approving-status-success{
    border 1px solid SUCCESS_COLOR
    color SUCCESS_COLOR
  }
  &.approving-status-error{
    border 1px solid ERROR_COLOR
    color ERROR_COLOR
  }
}

.approve-node {
    font-weight 400
  &.approve-node-warning{
    color WARNING_COLOR
  }
}

.approving-icon {
  background #CCCCCC
  flex-shrink 0
}

.back-point {
  background #CCCCCC
}


>>>.el-step__head.is-finish {
    color: SUCCESS_COLOR;
    border-color: SUCCESS_COLOR;
}

>>>.el-step__icon {
  width: 22px;
  height: 22px;
}

</style>
