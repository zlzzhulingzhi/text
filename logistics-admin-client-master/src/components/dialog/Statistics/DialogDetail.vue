<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <!--应用创建、编辑-->
    <template v-if="/^(Campus)$/.test(dialogType)">
      <div>{{ formData }}</div>
      <el-select v-model="formData.id">
        <el-option v-for="item in originData" :key="item.id" :label="item.name" :value="item.id"></el-option>
      </el-select>
    </template>

    <template v-slot:footer>
      <el-button class="margin-right-14" size="small" @click="close">取消</el-button>
      <el-button type="primary" size="small" @click="onSubmit" :loading="loading.submit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: 'DialogDetail',
  data() {
    return {
      visible: false, // 弹窗开关

      dialogType: 'Campus',
      typeMapping: {
        Campus: {
          title: '选择校区',
          // 模拟后台接口
          dataAPI: async () => {
            return {
              code: 200,
              data: [
                {id: 1, name: '北京校区'},
                {id: 2, name: '南京校区'}
              ]
            }
          },
          // 后台接口固定参数
          params: null,
          // 初始参数与传递参数
          formData: {
            id: null
          }
        }
      },

      // 父级传递参数
      pParams: {},
      // 请求得到的原始资源
      originData: [],

      formData: {},

      loading: {
        submit: false
      }
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    outputData() {
      let formData = {}
      for (const formDataKey in this.dialogInfo.formData) {
        formData[formDataKey] = this.formData[formDataKey]
      }
      return formData
    }
  },

  methods: {
    // 打开弹窗
    async open(data = {}) {
      this.dialogType = data.type
      this.pParams = data.params || {}

      if (data.data) {
        let formData = {
          ...this.dialogType.formData,
          ...data.data
        }
        for (const formDataKey in formData) {
          this.$set(this.formData, formDataKey, formData[formDataKey])
        }
      }

      if (this.dialogInfo.dataAPI) {
        let {data, code} = await this.dialogInfo.dataAPI({
          ...this.pParams,
          ...(this.dialogInfo.params || {})
        })
        if (code !== 200) return false
        this.originData = data

        if (data[0]) {
          this.$set(this.formData, 'id', data[0].id)
        }
      }

      this.visible = true
      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.outputData)
          reject('取消')
        })
      })
    },
    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle', 'close')
    },
    // 确定提交
    async onSubmit() {
      this.visible = false
      this.$emit('handle', 'submit')
    }
  }
}
</script>

<style scoped lang="stylus">
</style>