<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false" :before-close="close" width="600px">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

      <el-form-item prop="flowName" label="流程名称">
        <el-input class="width-300" show-word-limit maxlength="10" v-model="formData.flowName"></el-input>
      </el-form-item>

      <el-form-item prop="flowCode" label="流程代码">
        <el-input class="width-300" show-word-limit maxlength="10" v-model="formData.flowCode"></el-input>
      </el-form-item>

      <el-form-item prop="enabled" label="状态">
        <el-radio-group v-model="formData.enabled">
          <el-radio v-for="item in enabled" :key="item.id" :label="item.id">{{item.name}}</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item prop="sort" label="排序">
        <el-input-number class="width-300" v-model="formData.sort" controls-position="right" :min="1" :max="1000" :step="1"></el-input-number>
      </el-form-item>

      <el-form-item prop="flowDesc" label="说明">
        <el-input type="textarea" class="width-300" :rows=4 show-word-limit maxlength="100" v-model="formData.flowDesc"></el-input>
      </el-form-item>
      
      <el-form-item>
        <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
                  :loading="loading.submit">保存
        </el-button>
        <el-button class="width-80" size="medium" @click="close">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import mxBaseDialog from '@/components/mixins/mxBaseDialog'

export default {
  name: "DialogFormFlow",
  mixins:[mxBaseDialog],
  data() {
    let {required} = this.$rules
    let defaultFormData = {
      flowName: '',
      flowCode: '',
      flowDesc: '',
      enabled: 1
    }
    return {
      dialogType: 'Create',
      typeMapping: {
        Create: {title: '新增流程', submitAPI: this.$api.Workflow.add, type: 'Create',msgText:'新增流程'},
        Edit: {title: '编辑流程', submitAPI: this.$api.Workflow.update, type: 'Edit',msgText:'编辑流程'}
      },
      formData: {
        ...defaultFormData
      },
      rules: {
        flowName: [required],
        flowCode: [required],
        enabled: [required],
        sort: [required]
      },
    };
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled'
    })
  },
  
  methods: {
    async initData(data) {
      this.dialogType = data.type || 'Create'
      if (this.dialogType === 'Create') {
        return false
      }

      if (this.dialogType === 'Edit') {
        let {code, data: d} = await this.$api.Workflow.detail({
          id: data.formData.id
        })
        if (code !== 200) return false
        this.formData = {
          ...d
        }
      }
    },
  },
};
</script>

<style scoped lang="stylus">
</style>
