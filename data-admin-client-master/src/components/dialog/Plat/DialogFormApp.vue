<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <!--应用创建、编辑-->
    <template v-if="dialogType.startsWith('App')">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

<!--        <el-form-item prop="applicationTypeIdList" label="应用分类">
&lt;!&ndash;          <el-select class="width-300" v-model="formData.applicationTypeIdList" multiple :multiple-limit="1">
            <el-option v-for="item in appType" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>&ndash;&gt;
          <el-select class="width-300" v-model="formData.applicationTypeIdList[0]">
            <el-option v-for="item in appType" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>-->

        <el-form-item prop="name" label="应用名称">
          <el-input class="width-300" type="text" show-word-limit maxlength="10"
                    v-model.trim="formData.name"></el-input>
        </el-form-item>

        <el-form-item prop="uri" label="访问地址">
          <el-input class="width-300" type="text" show-word-limit maxlength="100"
                    v-model.trim="formData.uri"></el-input>
        </el-form-item>

        <el-form-item prop="host" label="主机名称">
          <el-input class="width-300" type="text" show-word-limit maxlength="30"
                    v-model.trim="formData.host"></el-input>
        </el-form-item>

        <el-form-item prop="permission" label="权限标识">
          <el-input class="width-300" type="text" show-word-limit maxlength="60"
                    v-model.trim="formData.permission" :disabled="disabledApp.test(formData.permission)"></el-input>
        </el-form-item>

        <el-form-item prop="iconUrl" label="图标">
          <el-input class="width-300" type="text" show-word-limit maxlength="100"
                    v-model="formData.iconUrl"></el-input>
        </el-form-item>

        <el-form-item prop="sort" label="排序">
          <el-input-number class="width-300" controls-position="right" v-model="formData.sort"
                           :min="0"></el-input-number>
        </el-form-item>

        <el-form-item prop="remark" label="备注">
          <el-input class="width-300" type="textarea" rows="3" show-word-limit maxlength="100"
                    v-model="formData.remark"></el-input>
        </el-form-item>

        <el-form-item prop="enabled" label="应用状态">
          <el-radio-group v-model="formData.enabled" :disabled="disabledApp.test(formData.permission)">
            <el-radio v-for="item in enabled" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
                     :loading="loading.submit">保存
          </el-button>
          <el-button class="width-80" size="medium" @click="close">取消</el-button>
        </el-form-item>
      </el-form>
    </template>

    <!--    <template v-slot:footer>
          <el-button class="margin-right-14" size="small" @click="close">取消</el-button>
          <el-button type="primary" size="small" @click="onSubmit">确定</el-button>
        </template>-->
  </el-dialog>
</template>

<script>
import {mapActions, mapGetters, mapState} from 'vuex'

export default {
  name: 'DialogFormApp',
  data() {
    let {required} = this.$rules
    let defaultFormData = {
      id: null,
      name: null,
      uri: null,
      host: null,
      permission: null,
      sort: null,
      iconUrl: null,
      remark: null,
      enabled: 1,
      appTypeId: 2
      // applicationTypeIdList: []
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'AppCreate',
      typeMapping: {
        AppCreate: {title: '新增应用', submitFn: this.$api.Application.create, type: 'Create'},
        AppEdit: {title: '编辑应用', submitFn: this.$api.Application.update, type: 'Edit'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        name: [required],
        uri: [required],
        permission: [required]
      },
      loading: {
        submit: false
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      // appType: 'common/appType'
    }),
    ...mapState('config', {
      disabledApp: 'disabledApp'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    }
  },

  methods: {
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),
    reset() {
      this.formData = {...this.defaultFormData}
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset()

      this.visible = true

      this.dialogType = data.type
      if (this.dialogType === 'AppCreate') {
        // if (this.appType[0]) this.formData.applicationTypeIdList = [this.appType[0].id]
        this.formData = {
          ...this.formData,
          ...data.formData
        }
        return false
      }

      if (this.dialogType === 'AppEdit') {
        let {code, data: d} = await this.$api.Application.detail({
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
        sort: this.formData.sort ? this.formData.sort : undefined
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.formData.name)
      this.$emit('success')
      this.getDictionary(['app']).then()

      this.close()
    }
  }
}
</script>

<style scoped lang="stylus">
</style>