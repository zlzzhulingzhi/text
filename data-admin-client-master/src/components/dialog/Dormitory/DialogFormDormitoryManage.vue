<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <!--班级创建、编辑-->
    <template v-if="dialogType.startsWith('')">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

        <el-form-item prop="unit" label="宿舍单元">
          <el-select class="width-300" v-model="formData.unit" filterable>
            <el-option v-for="item in dormitoryUnit" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="floor" label="楼层">
          <el-select class="width-300" v-model="formData.floor" filterable>
            <el-option v-for="item in dormitoryFloor" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="numbe" label="房号">
          <el-input class="width-300" show-word-limit maxlength="6" :disabled="dialogInfo.type === 'Edit'"
                    v-model="formData.numbe"></el-input>
        </el-form-item>

        <el-form-item prop="type" label="房型">
          <el-select class="width-300" v-model="formData.type" filterable>
            <el-option v-for="item in dormitoryType" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="status" label="宿舍状态">
          <el-select class="width-300" v-model="formData.status" filterable>
            <el-option v-for="item in dormitoryStatus" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="bz" label="备注">
          <el-input class="width-300" type="textarea" :rows="3" show-word-limit maxlength="100"
                    v-model="formData.bz"></el-input>
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
import {mapGetters} from 'vuex'

export default {
  name: 'DialogFormDormitoryManage',
  data() {
    let {required} = this.$rules
    let defaultFormData = {}
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'Create',
      typeMapping: {
        Create: {title: '添加宿舍', submitFn: this.$api.DormitoryAPI.create, type: 'Create'},
        Edit: {title: '编辑宿舍', submitFn: this.$api.DormitoryAPI.update, type: 'Edit'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        unit: [required],
        floor: [required],
        numbe: [required],
        type: [required],
        status: [required]
      },
      loading: {
        submit: false
      }
    }
  },
  computed: {
    ...mapGetters({
      dormitoryType: 'common/dormitoryType',
      dormitoryStatus: 'common/dormitoryStatus',
      dormitoryUnit: 'common/dormitoryUnit',
      dormitoryFloor: 'common/dormitoryFloor'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    }
  },

  methods: {
    reset() {
      this.formData = {...this.defaultFormData}
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset()

      this.visible = true

      this.dialogType = data.type || 'Create'
      if (this.dialogType === 'Create') {
        return false
      }

      if (this.dialogType === 'Edit') {
        let {code, data: d} = await this.$api.DormitoryAPI.detail({
          id: data.formData.id
        })
        if (code !== 200) return false
        this.formData = {
          ...d
        }
      }
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true

      let {code} = await this.dialogInfo.submitFn({
        ...this.formData,
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.formData.name)


      this.$emit('success')
      this.close()
    }
  }
}
</script>

<style scoped lang="stylus">
.tree-wrapper
  max-height 300px
</style>