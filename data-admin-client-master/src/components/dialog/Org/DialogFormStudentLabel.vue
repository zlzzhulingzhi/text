
<template>
  <el-dialog  :visible.sync="isLabelDialog" append-to-body title="学员标签"
             :close-on-click-modal="false"
             :before-close="handleClose" width="600px">
    <el-form label-width="100px" :rules="rules" ref="labelfrom" :model="labelParams">
      <el-form-item label="标签名称:" prop="groupName">
        <el-input maxlength="10" show-word-limit class="width-300" v-model="labelParams.groupName" size="small"  placeholder="请输入标签名称"></el-input>
      </el-form-item>
      <el-form-item label="排序优先级:">
        <el-input class="width-300" v-model="labelParams.sort" type="Number" size="small"></el-input>
      </el-form-item>
      <el-form-item label="启用状态:">
        <el-radio-group v-model="labelParams.enabled">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
        <div class="el-icon-wrannig-outline font-14 text-6 line-height-24 ">
          禁用状态下，标签新增、编辑时将不会在选项中显示
        </div>
      </el-form-item>
      <el-form-item class="margin-bottom-0">
        <span class="dialog-footer">
            <el-button class="width-80 margin-right-14" type="primary" @click="onSubmit"
                       :loading="loading.onSubmit">保存
            </el-button>
            <el-button @click="handleClose">取 消</el-button>
        </span>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>

export default {
  name: 'DialogFormStudentLabel',
  data() {
    return {
      isLabelDialog: false,
      labelApi: '',
      labelParams: {
        enabled: 1, // 0禁用 1启用
        groupName: '', // 标签名称
        sort: 1, // 排序
        id: null
      },
      loading: {
        submit: false
      },
      rules: {
        groupName: [
          {required: true, message: '请输入标签名称', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    async onSubmit() {
        await this.$refs.labelfrom.validate();
        let {groupName} = this.labelParams;
        if (groupName.trim() === '') return
        let {code} = await this.$api.labelPage[this.labelApi](this.labelParams)
        if (code !== 200) return false
        this.$message.success(`新增“${groupName}”标签成功`);
        this.$parent.getTableData()
        this.handleClose()
    },
    openLabel(params) {
      this.isLabelDialog = true
      // 判断是否是编辑
      this.onEmpty()
      this.labelApi = params.labelName
      if (params.labelName == 'update') {
        this.labelParams.enabled = params.enabled
        this.labelParams.groupName = params.groupName
        this.labelParams.sort = params.sort
        this.labelParams.id = params.id
      }
    },
    onEmpty() {
      this.labelParams.enabled = 1
      this.labelParams.groupName = ''
      this.labelParams.sort = 1
      this.$refs.labelfrom && this.$refs.labelfrom.resetFields();
    },
    handleClose() {
      this.isLabelDialog = false
    }
  }

}
</script>

<style>
</style>

