<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="500px">
    <el-form :model="formData" ref="form" :rules="rules" label-width="80px" size="small">
      <el-form-item label="上级分类" v-if="formData.parentId">
        <el-input class="width-300" v-model="formData.parentName" disabled></el-input>
      </el-form-item>
<!--      <el-form-item prop="businessCode" label="业务编码">
        <el-input class="width-300" type="text" show-word-limit maxlength="10"
                  v-model.trim="formData.businessCode" :disabled="dialogInfo.el.codeDisabled"></el-input>
      </el-form-item>-->
      <el-form-item label="分类名称" prop="name">
        <el-input class="width-300" v-model="formData.name" :maxlength="20" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number class="width-300" v-model="formData.sort" :min="0" :max="9999"
                         controls-position="right"></el-input-number>
      </el-form-item>
      <el-form-item label="状态" prop="enabled">
        <el-radio-group v-model="formData.enabled">
          <el-radio v-for="item in enabledList" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button class="width-80 margin-right-6" type="primary" size="small" :loading="loading" @click="onSubmit">确定
        </el-button>
        <el-button class="width-80" size="small" @click="close">取消</el-button>
<!--        <span :style="{ position: 'absolute', right: '0', bottom: '0' }" v-if="dialogInfo.el.common">
          <el-button size="small" :loading="loading" @click="addCommonCategory">添加通用分类</el-button>
          <el-popover :style="{ paddingRight: '0' }" trigger="hover" placement="bottom">
            <el-tree :style="{ maxHeight: '300px', overflow: 'auto', paddingRight: '10px' }" :data="dictCategory"
                     :props="treeProps"></el-tree>
            <i class="el-icon-question text-6 margin-left-8" slot="reference"></i>
          </el-popover>
        </span>-->
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import {mapState} from 'vuex'

const name = '分类'

export default {
  name: 'DialogFormCommonCategory',
  data() {
    let defaultFormData = {
      parentId: undefined,
      parentName: undefined,
      name: null,
      businessCode: null,
      sort: null,
      enabled: 1,
      orgId: -1,

      // remark: null,
      // categoryCode: null,
    }
    let {required} = this.$rules
    return {
      loading: false,
      visible: false,
      // 存储弹窗所需数据
      dialogType: 'Create',
      typeMapping: {
        Create: {
          title: `新增分类`,
          type: 'Create',
          submitAPI: this.$api.WCategory.create,
          // commonAPI: this.$api.WCategory.addTemplate,
          el: {
            codeDisabled: false,
            common: false,
            parentId: false
          },
          businessCode: 'businessCourse'
        },
        Edit: {
          title: `编辑分类`,
          type: 'Edit',
          submitAPI: this.$api.WCategory.update,
          detailAPI: this.$api.WCategory.detail,
          el: {
            codeDisabled: false,
            common: false,
            parentId: false
          },
          businessCode: 'businessCourse'
        },
        CreateChildren: {
          title: `新增分类`,
          type: 'Create',
          submitAPI: this.$api.WCategory.create,
          el: {
            codeDisabled: true,
            common: false,
            parentId: true
          },
          businessCode: 'businessCourse'
        },
        EditChildren: {
          title: `编辑分类`,
          type: 'Edit',
          submitAPI: this.$api.WCategory.update,
          detailAPI: this.$api.WCategory.detail,
          el: {
            codeDisabled: true,
            common: false,
            parentId: true
          },
          businessCode: 'businessCourse'
        }
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        name: [required],
        sort: [required],
        enabled: [required],
        businessCode: [required]
      },
      treeProps: {
        label: 'dictValue'
      }
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {el:{}}
    },
    ...mapState('common', {
      enabledList: 'enabled'
    }),
    dictCategory() {
      let dict = this.$store.getters['common/dictCategory'] || []
      return dict.length && dict[0].children || []
    },
    submitFormData(){
      return {
        ...this.formData,
        sort: this.formData.sort ? this.formData.sort : undefined,
        businessCode: this.dialogInfo.businessCode
      }
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

      this.dialogType = data.type

      if (this.dialogInfo.type === 'Create') {
        if (this.dialogInfo.el.codeDisabled) {
          this.formData.businessCode = data.formData.businessCode
        }

        if (this.dialogInfo.el.parentId) {
          this.formData.parentId = data.formData.id
          this.formData.parentName = data.formData.categoryName
        }
      } else if (this.dialogInfo.type === 'Edit') {
        let {code, data: d} = await this.dialogInfo.detailAPI({
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
      this.visible = false
      this.$emit('handle', {})
    },
    // 提交
    async onSubmit() {
      if (this.loading) return false
      await this.$refs.form.validate()
      this.loading = true
      let {code} = await this.dialogInfo.submitAPI(this.submitFormData)
      this.loading = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](name)
      this.$emit('handle', {
        type: 'success',
        data: this.formData
      })

      this.close()
    },
    // 添加通用分类
    async addCommonCategory() {
      if (this.loading) return false
      this.loading = true
      let {code} = await this.dialogInfo.commonAPI(this.dictCategory)
      this.loading = false
      if (code !== 200) return false
      this.$msg.Create('通用分类')
      this.$emit('handle', {
        type: 'success',
        data: this.formData
      })

      this.close()
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>