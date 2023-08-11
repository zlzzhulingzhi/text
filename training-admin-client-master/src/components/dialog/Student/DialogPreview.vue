<template>
  <el-dialog :visible.sync="visible" append-to-body :title="`欢迎加入${detail.orgName}`" :close-on-click-modal="false" :before-close="close" width="500px">
    <div class="flex column center-center">
      <QrcodeVue :value="qrCodeUrl" :size="180" v-if="detail.id"></QrcodeVue>
      <div class="height-180" v-else></div>
      <span class="text-9 font-13 margin-top-16">邀请链接有效截止时间：{{ detail.endTime }}</span>
    </div>
  </el-dialog>
</template>

<script>
import { mapState } from 'vuex'
import QrcodeVue from 'qrcode.vue'

export default {
  name: 'DialogPreview',
  components: {
    QrcodeVue
  },
  data() {
    let defaultFormData = {
      id: null
    }
    return {
      loading: {
        submit: false
      },
      visible: false,
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      detail: {}
    }
  },
  computed: {
    ...mapState('config', {
      domain: 'domain'
    }),
    ...mapState('system', {
      innerDomain: 'innerDomain'
    }),
    qrCodeUrl() {
      return `${this.innerDomain}${this.domain.h5Path}/#/pages/invite/invite?id=${this.detail.id}&key=${this.detail.invitationKey}`
    }
  },
  methods: {
    reset() {
      this.formData = { ...this.defaultFormData }
      this.$refs.form && this.$refs.form.resetFields()
    },
    close() {
      this.visible = false
    },
    open(data = {}) {
      this.reset()
      this.visible = true
      Object.assign(this.formData, data.formData || {})

      this.getDetail()
    },
    async getDetail() {
      if (!this.formData.id) return false
      let {code, data} = await this.$api.StudentInvite.detail(this.formData)
      if (code !== 200) return false
      this.detail = data || {}
    }
  }
}
</script>

<style lang="stylus" scoped>
.el-date-editor
  &.el-input
    width 300px

.el-textarea
  .el-input__count
    bottom -32px
</style>