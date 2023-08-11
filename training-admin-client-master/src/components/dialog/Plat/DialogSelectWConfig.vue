<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="true"
             :before-close="close" width="400px">

    <!--应用创建、编辑-->
    <template v-if="/^(WConfigSelect)$/.test(dialogType)">
      <div class="text-center">
        <div class="margin-bottom-16 line-height-32">该微页面适用于哪个终端？</div>

        <el-button class="margin-right-14" size="medium" v-for="(item,index) in outputDataList" :key="index"
                   @click="outputData = item.id;onSubmit()">
          {{ item.name }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: 'DialogSelectWConfig',
  data() {
    return {
      visible: false, // 弹窗开关

      dialogType: 'WConfigSelect',
      typeMapping: {
        WConfigSelect: {
          title: '终端'
        }
      },

      outputData: {},
      outputDataList: [
        {id: 'H5', name: '手机移动端'},
        {id: 'WEB', name: '电脑网页端'}
      ],

      loading: {
        submit: false
      }
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    }
  },

  methods: {
    // 打开弹窗
    async open(data = {}) {
      this.dialogType = data.type
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