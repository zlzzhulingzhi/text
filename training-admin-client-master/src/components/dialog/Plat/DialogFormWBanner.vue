<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <!--角色创建、编辑-->
    <template v-if="dialogType.startsWith('WBanner')">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

        <el-form-item prop="fileName" label="名称">
          <el-input class="width-300" type="text" show-word-limit maxlength="20"
                    placeholder="不超过20个字"
                    v-model.trim="formData.fileName"></el-input>
        </el-form-item>

        <el-form-item prop="filePath" label="图片">
          <UploadImage ref="UploadImage" v-model="formData.filePath" isEdit :autoUpload="false"
                       :width="300" :height="59.375" :options="{
                         autoCropWidth: 1920,
                         autoCropHeight: 380,
                         fixed: false,
                         fixedBox: true,
                         centerBox: false
                       }">
            <div class="margin-left-12 line-height-30 font-13 text-9" slot="tips">
              <div>固定尺寸：</div>
              <div>1920*380</div>
            </div>
          </UploadImage>
        </el-form-item>

        <el-form-item prop="openUrl" label="跳转链接">
          <el-input class="width-300" type="text" show-word-limit maxlength="100"
                    placeholder="不填写链接，则只展示图片，点击不跳转"
                    v-model.trim="formData.openUrl"></el-input>
        </el-form-item>

        <el-form-item prop="enabled" label="状态">
          <el-radio-group v-model="formData.enabled">
            <el-radio v-for="item in enabled" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="sort" label="排序">
          <el-input-number class="width-300" controls-position="right" v-model="formData.sort"
                           :min="0" :max="999" @keydown.native.190.110.prevent></el-input-number>
          <div class="text-9">
            <b class="el-icon-warning-outline"></b>
            填写正整数，数值越小，排序越前。
          </div>
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
import PermissionTree from '@/components/panes/PermissionTree'

export default {
  name: 'DialogFormWBanner',
  components: {
    PermissionTree
  },
  data() {
    let {required} = this.$rules
    let defaultFormData = {
      id: null,
      fileName: null,
      filePath: null,
      groupCode: 'fileBanner',
      openUrl: null,
      sort: null,
      orgId: -1,
      remark: null,
      enabled: 1
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'WBannerCreate',
      typeMapping: {
        WBannerCreate: {title: '新增图片', submitFn: this.$api.DecFile.create, type: 'Create'},
        WBannerEdit: {title: '编辑图片', submitFn: this.$api.DecFile.update, type: 'Edit'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        fileName: [required],
        filePath: [required]
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
    reset() {
      this.formData = {...this.defaultFormData}
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open(data = {}) {
      this.visible = true

      this.dialogType = data.type || 'WBannerCreate'
      this.reset()

      if (this.dialogType === 'WBannerEdit') {
        let {code, data: d} = await this.$api.DecFile.detail({
          id: data.formData.id
        })
        if (code !== 200) return false
        this.formData = d
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

      this.formData.filePath = await this.$refs.UploadImage.uploadFile()

      let {code} = await this.dialogInfo.submitFn({
        ...this.formData,
        sort: this.formData.sort ? this.formData.sort : undefined,
        groupCode: 'fileBanner'
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.formData.fileName)
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