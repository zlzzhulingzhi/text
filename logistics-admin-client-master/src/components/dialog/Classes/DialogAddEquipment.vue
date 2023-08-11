<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
    :before-close="close" width="600px">

    <!--班级创建、编辑-->
    <template v-if="dialogType.startsWith('')">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

        <!-- <el-form-item prop="roomType" label="教室类别">
            <el-select class="width-300" v-model="formData.roomType" filterable>
              <el-option v-for="item in classTypeInfo[0].children" :key="item.id" :label="item.dictValue" :value="item.dictKey"></el-option>
            </el-select>
          </el-form-item> -->

        <el-form-item prop="deviceName" label="设备名称">
          <el-input class="width-300" v-model="formData.deviceName" show-word-limit maxlength="10"></el-input>
        </el-form-item>

        <el-form-item prop="sort" label="排序">
          <el-input-number class="width-300" v-model="formData.sort" controls-position="right" :min="1" :max="999">
          </el-input-number>
        </el-form-item>

        <el-form-item prop="remark" label="备注">
          <el-input class="width-300" v-model="formData.remark" type="textarea" show-word-limit maxlength="100">
          </el-input>
        </el-form-item>


        <el-form-item>
          <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
            :loading="loading.submit">保存
          </el-button>
          <el-button class="width-80" size="medium" @click="close">取消</el-button>
        </el-form-item>
      </el-form>
    </template>
  </el-dialog>
</template>
  
<script>
import { mapActions, mapGetters } from 'vuex'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'

export default {
  name: 'DialogAddEquipment',
  mixins:[mxBaseDialog],
  data() {
    let { required } = this.$rules
    return {
      // 存储弹窗所需数据
      dialogType: 'Create',
      typeMapping: {
        Create: { title: '新增设备', submitFn: this.$api.SceneDevice.sceneDeviceAdd, type: 'Create' },
        Edit: { title: '编辑设备', submitFn: this.$api.SceneDevice.sceneDeviceUpdate, type: 'Edit' }
      },
      rules: {
        deviceName: [required],
        sort: [required],
      }
    }
  },
  computed: {
    ...mapGetters({
      classTypeInfo: 'common/classTypeInfo'
    }),
    submitAPI() {
      return 
    }
  },

  methods: {
    reset() {
      this.formData = { ...this.defaultFormData }
      this.$refs.form && this.$refs.form.resetFields()
    },
     // 操作 - 初始化数据
    async initData(data) {
      if (this.dialogType === 'Edit') {
        let { code, data: d } = await this.$api.SceneDevice.sceneDeviceDetail({
          id: data.formData.id
        })
        if (code !== 200) return false
        this.formData = {
          ...d
        }
        // if(this.formData.roomType = 'CPSC') this.formData.roomType = '普通教室'
      }
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true

      let { code } = await this.dialogInfo.submitFn({
        ...this.formData,
        category: "Classroom",
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.formData.code)

      this.$emit('success');
      this.close();
    },
    onSelectAll(node) {
      node.checked = !(node.checked && node.childNodes.every(item => item.checked))
      node.childNodes.forEach(item => item.checked = node.checked)
    }
  }
}
</script>
  
<style scoped lang="stylus">
.tree-wrapper
  max-height 300px
</style>