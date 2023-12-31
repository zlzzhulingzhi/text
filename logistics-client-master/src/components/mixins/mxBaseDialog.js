export default {
  data() {
    return {
      visible: false,
      dialogType: '',
      typeMapping: {},
      pParams: {},

      defaultFormData: {},
      formData: {},

      loading: {
        submit: false
      }
    }
  },
  computed: {
    dialogInfo() {
      return this.typeMapping[this.dialogType] || {}
    },
    submitFormData() {
      return {
        ...this.formData
      }
    }
  },
  methods: {
    // 操作 - 初始化数据
    initData(data) {
    },
    // 操作 - 打开弹窗
    open(data = {}) {
      this.visible = true
      this.dialogType = data.type
      this.formData = {
        ...this.defaultFormData,
        ...data.formData
      }
      this.$refs.form && this.$refs.form.resetFields()
      this.pParams = data.pParams

      this.initData && this.initData(data)

      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.outputData)
          reject('取消')
        })
      })
    },
    // 操作 - 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle', 'close')
    },
    // 操作 - 确定提交
    async onSubmit() {
      if (this.$refs.form) await this.$refs.form.validate()
      if (!this.dialogInfo.submitAPI) return false
      this.loading.submit = true
      let {code} = await this.dialogInfo.submitAPI(this.submitFormData)
      this.loading.submit = false
      if (code !== 200) return false

      this.$msg(this.dialogInfo.msgText || '')

      this.visible = false
      this.$emit('handle', 'submit')
    }
  }
}