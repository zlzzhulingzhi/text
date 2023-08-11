<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <!--参数创建、编辑-->
    <template v-if="dialogType.startsWith('Dict')">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

        <el-form-item prop="parentId" label="上级参数" v-if="dialogInfo.el.parentId">
          <el-input class="width-300" type="text" show-word-limit maxlength="10"
                    v-model.trim="formData.parentName" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item prop="code" label="参数编号">
          <el-input class="width-300" type="text" show-word-limit maxlength="10"
                    v-model.trim="formData.code" :disabled="dialogInfo.el.codeDisabled"></el-input>
        </el-form-item>

        <el-form-item prop="dictKey" label="键值" v-if="dialogInfo.el.dictKey">
          <el-input class="width-300" type="text" show-word-limit maxlength="10"
                    v-model.trim="formData.dictKey"></el-input>
        </el-form-item>

        <el-form-item prop="dictValue" label="参数名称">
          <el-input class="width-300" type="text" show-word-limit maxlength="100"
                    v-model.trim="formData.dictValue"></el-input>
        </el-form-item>

        <el-form-item prop="sort" label="排序">
          <el-input-number class="width-300" controls-position="right" v-model="formData.sort"
                           :min="0"></el-input-number>
        </el-form-item>

        <el-form-item prop="enabled" label="参数状态">
          <el-radio-group v-model="formData.enabled">
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
  name: 'DialogFormDict',
  data() {
    let {required} = this.$rules
    let defaultFormData = {
      code: null,
      dictKey: null,
      dictValue: null,
      sort: null,
      enabled: 1
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'DictCreate',
      typeMapping: {
        DictCreate: {
          title: '新增参数', submitFn: this.$api.Dict.create,
          type: 'Create',
          el: {
            dictKey: false,
            codeDisabled: false
          }
        },
        DictEdit: {
          title: '编辑参数', submitFn: this.$api.Dict.update,
          type: 'Edit',
          el: {
            dictKey: false,
            codeDisabled: false
          }
        },
        DictChildrenCreate: {
          title: '新增参数子项', submitFn: this.$api.Dict.create,
          type: 'Create',
          el: {
            dictKey: true,
            codeDisabled: true,
            parentId: true
          }
        },
        DictChildrenEdit: {
          title: '编辑参数子项', submitFn: this.$api.Dict.update,
          type: 'Edit',
          el: {
            dictKey: true,
            codeDisabled: true,
            parentId: true
          }
        }
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        code: [required],
        dictKey: [required],
        dictValue: [required]
      },
      loading: {
        submit: false
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled'
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

      if (this.dialogType.endsWith('Create')) {
        if (this.dialogInfo.el.codeDisabled) {
          this.formData.code = data.formData.code
        }

        if (this.dialogInfo.el.parentId) {
          this.formData.parentId = data.formData.id
          this.formData.parentName = data.formData.dictValue
        }
      }

      if (this.dialogType.endsWith('Edit')) {
        let {code, data: d} = await this.$api.Dict.detail({
          id: data.formData.id
        })
        if (code !== 200) return false
        this.formData = {
          ...d,
          parentName: data.formData.parentName
        }
      }

      return new Promise((resolve, reject) => {
        this.$once('handle', ({type, data}) => {
          if (type === 'success') {
            resolve(data)
          } else {
            reject('取消')
          }
        })
      })
    },
    // 关闭弹窗
    close() {
      this.$emit('handle', {})
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
      this.$msg[this.dialogInfo.type](this.formData.code)

      this.$emit('handle', {
        type: 'success'
      })
      this.visible = false
    }
  }
}
</script>

<style scoped lang="stylus">
</style>