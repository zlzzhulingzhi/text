<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title"
             :close-on-click-modal="dialogInfo.modalClose"
             :before-close="close" :width="dialogInfo.width">

    <!--证书编辑、创建-->
    <template v-if="/^(CertCreate|CertEdit)$/.test(dialogType)">
      <el-form ref="form" :model="formData" :rules="rules" v-loading="loading.page" label-width="120px" size="small">
        <el-form-item prop="name" label="证书名称">
          <el-input class="width-300" type="text" v-model="formData.name" placeholder="请输入证书名称"></el-input>
          <div class="margin-top-8 line-height-20 font-12 text-6 margin-right-12">
            <b class="el-icon-warning-outline"></b>
            证书名称是用于系统标识和查找证书，并非证书标题，请注意区别
          </div>
        </el-form-item>
        <el-form-item prop="templateId" label="证书模板">
          <!--编辑时，只能预览证书-->
          <template v-if="dialogInfo.type === 'Edit'">
            <div class="line-height-32">{{ templateName }}</div>
            <CertTemplateView v-if="visible" v-model="formData.list" :width="300"></CertTemplateView>
          </template>
          <!--创建时，预览模板-->

          <template v-else>
            <el-select class="width-300" v-model="formData.templateId" filterable placeholder="请选择证书模板">
              <el-option v-for="item in certTemplateList" :key="item.id" :label="item.name"
                         :value="item.id"></el-option>
            </el-select>
            <CertTemplateView v-show="formData.templateId" class="margin-top-12" :id="formData.templateId"
                              :width="300"></CertTemplateView>
          </template>


          <div v-if="/^(CertEdit)$/.test(dialogType)"
               class="margin-top-8 line-height-20 font-12 text-6 margin-right-12">
            <b class="el-icon-warning-outline"></b>
            已创建的证书只能修改证书名称，不能修改证书模板
          </div>
        </el-form-item>


        <el-form-item class="margin-bottom-0">
          <el-button class="width-80 margin-right-14" type="primary" @click="onSubmit"
                     :loading="loading.submit">保存
          </el-button>
          <el-button class="width-80" @click="close">取消</el-button>
        </el-form-item>
      </el-form>
    </template>

    <!--模板预览-->
    <div class="flex column center-center" v-else-if="/^(TemplateView)$/.test(dialogType)">
      <CertTemplateView v-if="visible" :id="formData.templateId"></CertTemplateView>

      <div class="text-center margin-top-12">
        <el-button size="small" class="width-80" @click="close">关闭</el-button>
      </div>
    </div>

    <!--证书预览-->
    <div class="flex column center-center" v-else-if="/^(CertView)$/.test(dialogType)">
      <CertTemplateView v-if="visible" v-model="formData.list"></CertTemplateView>

      <div class="text-center margin-top-12">
        <el-button size="small" class="width-80" @click="close">关闭</el-button>
      </div>
    </div>

    <!--证书预览【通过url】-->
    <template v-else-if="/^(UserCertView)$/.test(dialogType)">
      <el-image :src="detail.certImageUrl"></el-image>
      <div class="text-center margin-top-12">
        <el-button size="small" class="width-80" @click="close">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import {mapGetters} from 'vuex'
import CertTemplateView from '@/components/panes/CertTemplateView'

export default {
  name: 'DialogCertEdit',
  components: {
    CertTemplateView
  },
  data() {
    let defaultFormData = {
      enabled: 1,
      name: null,
      templateId: null,
      list: [],
      templateName: null
    }
    let {required} = this.$rules
    return {
      visible: false,
      dialogType: 'CertCreate',
      typeMapping: {
        CertCreate: {title: '创建证书', submitFn: this.$api.Cert.create, type: 'Create', width: '600px', modalClose: false},
        CertEdit: {
          title: '编辑证书', submitFn: this.$api.Cert.update, type: 'Edit', width: '600px', modalClose: false,
          detailApi: this.$api.Cert.detail
        },
        CertView: {
          title: '预览证书', submitFn: null, type: 'View', width: '864px', modalClose: true,
          detailApi: this.$api.Cert.detail
        },
        TemplateView: {title: '预览模板', submitFn: null, type: 'View', width: '864px', modalClose: true},
        UserCertView: {
          title: '预览证书', submitFn: null, type: 'DataView', width: '864px', modalClose: true,
          detailApi: null
        }
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      detail: {},
      rules: {
        name: [required],
        templateId: [required]
      },
      loading: {
        submit: false,
        page: false
      }
    }
  },
  computed: {
    ...mapGetters({
      certTemplateList: 'common/certTemplateList'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    submitFormData() {
      return {
        ...this.formData
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

      if (/^(Edit|View)$/.test(this.dialogInfo.type)) {
        await this.getDetail(data)
      }
      if (/^(DataView)$/.test(this.dialogInfo.type)) {
        this.detail = data.data
      }
    },
    async getDetail(o) {
      let formData = {
        enabled: 1,
        name: o.data.name,
        templateId: o.data.templateId,
        id: o.data.id
      }

      if (this.dialogInfo.detailApi) {
        let {code, data} = await this.dialogInfo.detailApi({id: o.data.id})
        if (code !== 200) return false
        formData.list = data.certTemplateConfig.configList
        this.templateName = data.templateName
      }

      this.formData = formData
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      this.loading.submit = true
      let {code} = await this.dialogInfo.submitFn(this.submitFormData)
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.submitFormData.name)
      this.$emit('success')

      this.close()
    }
  }
}
</script>

<style scoped lang="stylus">
</style>