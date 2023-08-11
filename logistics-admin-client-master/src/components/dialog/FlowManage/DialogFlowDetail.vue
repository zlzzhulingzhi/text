<template>
  <el-dialog :visible.sync="visible" append-to-body title="流程详情" :close-on-click-modal="false" :before-close="close" width="1200px">
    <div class="text-bold margin-bottom-28">流程名称：{{flowInfo.flowName}}</div>

    <div class="text-bold margin-bottom-6">流程节点操作顺序：</div>

    <BaseTreeEdit 
        :value="flowNodeInfo"
        :deleteList.sync="deleteInfo">

      <template slot="header">
        <div class="col-sort">节点顺序</div>
        <div class="col-name">节点名称</div>
        <!-- <div class="col-code">流程代码</div> -->
        <div class="col-people">负责人</div>
        <div class="col-desc">节点描述</div>
      </template>

      <template slot="row" slot-scope="{data}">
        <div class="col-sort">
          {{data.sort}}
        </div>

        <div class="col-name">
          <el-input v-model="data.nodeName" placeholder="节点名称" size="small" ref="input"
                    maxlength="10" show-word-limit></el-input>
        </div>

        <!-- <div class="col-code">
          <el-input v-model="data.nodeCode" placeholder="流程代码" size="small" ref="input"
                    maxlength="10" show-word-limit></el-input>
        </div> -->

        <div class="col-people">
          <el-input v-model="data.principalInfo" placeholder="请选择" size="small" ref="input"
                    maxlength="10" show-word-limit disabled></el-input>
          <el-button type="primary" size="small" class="margin-left-2" @click="pickPrincipal(data)">选择</el-button>
        </div>

        <div class="col-desc">
          <el-input v-model="data.nodeDesc" type="textarea" placeholder="节点描述" :row="2" size="small" ref="input"
                    maxlength="30" show-word-limit></el-input>
        </div>
      </template>

    </BaseTreeEdit>

    <div class="flex center-center margin-top-20">
      <el-button type="primary" size="medium" @click="onSubmit">保存</el-button>
      <el-button size="medium" @click="close">取消</el-button>
    </div>

    <DialogPickPrincipal ref="DialogPickPrincipal"></DialogPickPrincipal>
  </el-dialog>
</template>

<script>
import BaseTreeEdit from '@/components/panes/BaseTreeEdit'
import DialogPickPrincipal from '@/components/dialog/FlowManage/DialogPickPrincipal'
import { mapActions, mapGetters } from "vuex";


export default {
  name: "DialogFlowDetail",
  components: {
    BaseTreeEdit,
    DialogPickPrincipal
  },
  data() {
   
    return {
      visible: false, // 弹窗开关

      flowInfo: {},
      flowNodeInfo: [],
      deleteInfo: null,
      loading: {
        submit: false
      },
    };
  },
  computed: {
    ...mapGetters({
    }),
    
  },
  
  methods: {
    
    // 打开弹窗
    async open(data = {}) {
      
      this.visible = true

      // 流程信息
      this.flowInfo = data.formData

      let { code, data: d } = await this.$api.Workflow.nodePage({
        workflowId: this.flowInfo.id
      })
      if(code !== 200) return false
      d.records = d.records.map(item => {
        return {
          ...item,
          edited: 1
        }
      })
      this.flowNodeInfo = [
        ...d.records
      ]
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 保存
    async onSubmit() {
      // 校验  流程名称  是否填写
      if(this.flowNodeInfo.some(item => !item.nodeName)) return this.$message.warning('请填写“流程名称”')

      // 校验  负责人  是否选择
      if(this.flowNodeInfo.some(item => !item.managerType)) return this.$message.warning('请选择“流程负责人”')
      
      // 批量创建  流程节点
      this.flowNodeInfo = this.flowNodeInfo.map((item => {
        return {
          managerRef: item.managerRef,
          managerType: item.managerType,
          nodeCode: item.nodeCode,
          nodeName: item.nodeName,
          nodeDesc: item.nodeDesc,
          sort: item.sort
        }
      }))
      let { code } = await this.$api.Workflow.nodeAddBatch({
        workflowId: this.flowInfo.id,
        workflowNodeUpdateRequestList: this.flowNodeInfo
      })
      if(code !== 200) return false
      this.$msg('保存')
      this.close()
    },

    async pickPrincipal(params) {
      let principalInfoMid = await this.$refs.DialogPickPrincipal.open()
      params.managerType = principalInfoMid.managerType
      if(principalInfoMid.managerType === 'user') {
        // params.principalInfo = principalInfoMid.realName
        this.$set(params, 'principalInfo', principalInfoMid.realName)
        params.managerRef = principalInfoMid.id
      }
      else if(principalInfoMid.managerType === 'role') {
        // params.principalInfo = principalInfoMid.name
        this.$set(params, 'principalInfo', principalInfoMid.name)
        params.managerRef = principalInfoMid.code
      }
      // this.$set(params, 'principalInfo', principalInfoMid.realName)
    }
   
  },
};
</script>

<style scoped lang="stylus">
.col-sort
  width 80px

.col-name
  flex 1
  min-width 150px
  margin-right 12px

  .el-input
    width 150px

// .col-code
//   flex 1
//   min-width 100px
//   margin-right 12px

.col-people
  flex 1
  min-width 150px
  margin-right 72px

.col-desc
  flex 2
  min-width 180px
</style>
