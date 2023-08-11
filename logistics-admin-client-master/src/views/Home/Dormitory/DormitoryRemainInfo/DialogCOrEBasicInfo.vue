<template>
  <el-dialog
    :visible.sync="visible"
    append-to-body
    :title="dialogInfo.title"
    :close-on-click-modal="false"
    :before-close="close"
    width="600px">
    <el-form ref="form" :model="formData" :rules="rules" label-width="130px" size="small">
      <el-form-item prop="building" label="宿舍单元">
        <el-select class="width-300" v-model="formData.building" filterable>
          <el-option
            v-for="item in dormitoryUnit"
            :key="item.id"
            :label="item.dictValue"
            :value="item.dictKey"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item prop="roomTypeName" label="房型名称">
        <el-input class="width-300" v-model="formData.roomTypeName" placeholder="请输入房型名称"
           :maxlength="20" show-word-limit></el-input>
      </el-form-item>

      <el-form-item prop="roomTypeCode" label="房型编号">
        <el-input class="width-300" v-model="formData.roomTypeCode" placeholder="请输入房型编号"
           :maxlength="20" show-word-limit></el-input>
      </el-form-item>

      <el-form-item prop="area" label="面积数值(平方米)">
        <el-input-number
         
          class="width-300"
          v-model="formData.area"
          :min="1"
          :max="9999"
          :precision="2"
          controls-position="right"></el-input-number>
      </el-form-item>

      <el-form-item prop="roomNum" label="房间总数">
        <el-input-number
         
          class="width-300"
          v-model="formData.roomNum"
          :min="1"
          :max="9999"
          :precision="0"
          controls-position="right"></el-input-number>
      </el-form-item>

      <!-- <el-form-item prop="usingNum" label="在住数量">
        <el-input-number
          class="width-300"
          v-model="formData.usingNum"
          :min="1"
          :max="9999"
          controls-position="right"></el-input-number>
      </el-form-item> -->

      <el-form-item prop="maintNum" label="维修数量">
        <el-input-number
         
          class="width-300"
          v-model="formData.maintNum"
          :min="0"
          :max="9999"
          :precision="0"
          controls-position="right"></el-input-number>
      </el-form-item>

      <!-- <el-form-item prop="incomeNum" label="预计到达">
        <el-input-number
          class="width-300"
          v-model="formData.incomeNum"
          :min="1"
          :max="9999"
          controls-position="right"></el-input-number>
      </el-form-item> -->

      <!-- <el-form-item prop="outcomeNum" label="预计离店">
        <el-input-number
          class="width-300"
          v-model="formData.incomeNum"
          :min="1"
          :max="9999"
          controls-position="right"></el-input-number>
      </el-form-item> -->

      <el-form-item prop="sort" label="排序">
        <el-input-number
         
          class="width-300"
          v-model="formData.sort"
          :min="0"
          :max="9999"
          :precision="0"
          controls-position="right"></el-input-number>
      </el-form-item>

      <el-form-item prop="enabled" label="状态">
        <el-radio-group v-model="formData.enabled">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <el-button
          class="width-80 margin-right-14"
          type="primary"
          size="medium"
          @click="onSubmit"
          :loading="loading.submit"
          >保存
        </el-button>
        <el-button class="width-80" size="medium" @click="close">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'DialogCOrEBasicInfo',
  data() {
    let { required } = this.$rules;
    let defaultFormData = {
      building: null,
      roomTypeName: null,
      roomTypeCode: null,
      area: undefined,
      enabled: 1,
      roomNum: null,
      maintNum: null,
      sort: 1
    };
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'Create',
      typeMapping: {
        Create: { title: '新增宿舍', submitFn: this.$api.DormitoryRemainImport.add, type: 'Create' },
        Edit: { title: '编辑宿舍', submitFn: this.$api.DormitoryRemainImport.update, type: 'Edit' },
      },
      defaultFormData,
      formData: {
        ...defaultFormData,
      },
      rules: {
        building: [required],
        roomTypeName: [required],
        roomTypeCode: [required],
        // area: [required],
        // enabled: [required],
        roomNum: [required],
        maintNum: [required],
      },
      loading: {
        submit: false,
      },
    };
  },
  computed: {
    ...mapGetters({
      dormitoryUnit: 'common/dormitoryUnit',
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType];
      return dialogInfo || {};
    },
  },

  methods: {
    reset() {
      this.formData = { ...this.defaultFormData };
      this.$refs.form && this.$refs.form.resetFields();
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset();
      this.visible = true;
      this.dialogType = data.type || 'Create';
      if (this.dialogType === 'Create') {
        return false;
      }
      if (this.dialogType === 'Edit') {
        let { code, data: d } = await this.$api.DormitoryRemainImport.detail({
          id: data.formData.id,
        });
        if (code !== 200) return false;
        this.formData = {
          ...d,
        };
      }
    },
    // 关闭弹窗
    close() {
      this.visible = false;
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate();
      this.loading.submit = true;

      let { code } = await this.dialogInfo.submitFn({
        ...this.formData,
      });
      this.loading.submit = false;
      if (code !== 200) return false;
      this.$msg[this.dialogInfo.type](this.formData.name);

      this.$emit('success');
      this.close();
    },
  },
};
</script>

<style scoped lang="stylus">
.tree-wrapper
  max-height 300px
</style>
