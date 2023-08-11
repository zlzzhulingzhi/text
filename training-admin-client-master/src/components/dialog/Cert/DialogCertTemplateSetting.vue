<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <div v-loading="loading.page">
      <div class="margin-bottom-12">
        <el-checkbox v-for="(item,index) in ((this.oData || {}).certSearchConfig || {}).configList"
                     :key="index" v-model="item.selected" :disabled="!item.editable">
          {{ item.keyName }}
        </el-checkbox>
      </div>

      <div class="text-6">
        <b class="el-icon-warning-outline"></b>
        {{ $t('Please Select') }}
      </div>
    </div>

    <div class="text-center" slot="footer">
      <el-button class="width-80 margin-right-14" size="small" type="primary" @click="onSubmit"
                 :loading="loading.submit">保存
      </el-button>
      <el-button class="width-80" size="small" @click="close">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>

export default {
  name: 'DialogCertTemplateSetting',
  data() {
    return {
      visible: false,
      dialogType: 'SearchConfig',
      typeMapping: {
        SearchConfig: {title: '查询设置', submitFn: this.$api.StandardTemplate.updateSearchConfig}
      },
      oData: null,
      id: null,
      loading: {
        submit: false,
        page: false
      }
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    configList() {
      return ((this.oData || {}).certSearchConfig || {}).configList || []
    }
  },

  methods: {
    reset() {
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset()
      this.visible = true
      this.dialogType = data.type

      this.id = data.data.id
      await this.getDetail()
    },
    async getDetail() {
      if (!this.id) return false
      this.loading.page = true
      let {code, data} = await this.$api.StandardTemplate.detailSearchConfig({
        id: this.id
      })
      this.loading.page = false
      if (code !== 200) return false
      this.oData = data
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      if (this.configList.filter(item => item.selected).length < 2) return this.$message.warning(this.$t('Please Select'))
      this.loading.submit = true
      let {code} = await this.dialogInfo.submitFn({
        ...this.oData,
        id: this.id
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg.Edit('查询设置')
      this.$emit('success')
      this.close()
    }
  }
}
</script>

<style scoped lang="stylus">
</style>

<i18n>
{
  "en": {
    "Please Select": "Please Select"
  },
  "zh": {
    "Please Select": "请至少选择两项"
  }
}
</i18n>
