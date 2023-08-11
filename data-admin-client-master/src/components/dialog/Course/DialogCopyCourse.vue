<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false" :before-close="close" width="600px">
    <el-form :model="formData" ref="form" :rules="rules" label-width="80px" size="small">
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="formData.courseName" :maxlength="30"></el-input>
      </el-form-item>
      <!-- <el-form-item label="复制到" prop="orgId">
        <el-select v-model="formData.orgId" placeholder="请选择机构">
          <el-option v-for="item in orgList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button class="width-80 margin-right-6" type="primary" size="small" @click="onSubmit">确定</el-button>
        <el-button class="width-80" size="small" @click="close">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'DialogCourseCopy',
  data() {
    let defaultFormData = {
      orgId: undefined,
      id: null,
      courseName: ''
    }
    let {required} = this.$rules
    return {
      visible: false,
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        courseName: [required],
        orgId: [required]
      }
    }
  },
  computed: {
    dialogInfo() {
      return {
        title: '复制课程'
      }
    },
    ...mapGetters({
      orgList: 'common/organization'
    })
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
      this.formData = Object.assign({}, this.formData, data.formData)
    },
    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle', 'close')
    },
    //
    async onSubmit() {
      await this.$refs.form.validate()

      let { code } = await this.$api.Course.copy(this.formData)
      if (code !== 200) return false
      this.$msg.Create('复制课程')
      this.$emit('success')

      this.close()
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>