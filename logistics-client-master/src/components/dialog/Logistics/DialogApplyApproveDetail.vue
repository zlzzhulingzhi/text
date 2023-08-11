<!-- 审批详情 -->
<template>
  <el-dialog :visible.sync="visible" append-to-body :close-on-click-modal="false" :before-close="close" width="600px">
    <div slot="title" class="flex">
      <div class="font-16 text-3 text-bold">
        {{ `审批详情 - ${applyInfo.orgName ? applyInfo.orgName : applyInfo.projectName}` }}
      </div>
      <div class="approving-status" :class="`approving-status-${(flowStatus.find((a) => a.id === applyInfo.flowStatus) || {}).status}`">
        {{ applyInfo.flowStatus | flowStatus }}
      </div>
    </div>
    <div class="stepsTitle">审批流程</div>
    <div class="approvalProcess">
      <el-steps :active="activeStep" direction="vertical">
        <el-step v-for="(item, i) in approvalProcessProject" :key="i" :id="item.id">
          <template slot="icon">
            <div v-if="item.status !== 21" class="width-24 height-24 radius-12 flex center-center approving-icon">
              <el-image :src="require('@/assets/icons/approve.png')" class="height-14 width-12"> </el-image>
            </div>
            <el-image v-else="item.status === 21" :src="require('@/assets/icons/pass_icon.png')" class="height-24 width-24 radius-12 approving-icon"> </el-image>
          </template>

          <template slot="title">
            <div class="margin-left-10 flex start-center">
              <span class="font-14 text-3 text-bold">{{ item.nodeName }}</span>
              <div class="width-6 height-6 radius-2 margin-left-10 back-point"></div>
              <span class="font-14 text-3 margin-left-6 approve-node" :class="`approve-node-${item.status === 11 ? 'warning' : ''}`">{{ item.status | approveNodeStatus }}</span>
            </div>
          </template>

          <template slot="description">
            <div class="processing_content_td">
              <div class="processing_content_detail">
                <el-avatar :size="24" fit="contain" :src="require('@/assets/icons/avatar.png')"> </el-avatar>
                <span class="font-size-12 margin-left-16 margin-top-4 text-6">{{ item.managerInfo }}</span>
              </div>
              <div class="processing_content_detail" v-if="item.approveApplyTime !== null">
                <span class="text-6 font-size-12">{{ item.approveApplyTime | formatTime("MM月DD日 HH:mm") }}</span>
              </div>
            </div>

            <div class="margin-top-10" v-if="item.comment !== null">
              <span class="text-3 font-14">审批说明</span>
              <span class="text-6 font-14 margin-left-10">{{ item.comment }}</span>
            </div>
          </template>
        </el-step>
      </el-steps>
    </div>
  </el-dialog>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import mxBaseDialog from '@/components/mixins/mxBaseDialog'

export default {
  name: "DialogApplyApproveDetail",
  mixins: [mxBaseDialog],
  data() {
    return {
      // 审批数据
      approvalProcessProject: [],
      applyInfo: {},
    };
  },
  computed: {
    ...mapGetters({
      flowStatus: "common/flowStatus",
      approveNodeStatus: "common/approveNodeStatus",
    }),
    activeStep() {
      return this.approvalProcessProject.filter((a) => a.status === 21).length;
    },
  },

  methods: {
    // 操作 - 初始化数据
    async initData(params) {
      const { formData } = params;
      this.applyInfo = formData;
      let { code, data } = await this.$api.AllowanceApply.applyWorkflowNodes({
        id: formData.id,
      });
      if (code !== 200) return false;

      this.approvalProcessProject = data.workflowInstanceNodeResponses;
    },
  },
};
</script>
<style scoped lang="stylus">
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
    /* margin-left: 10px; */
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
    width:450px
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
    margin-left: 20px

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
