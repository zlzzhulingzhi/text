<template>
  <el-dialog :visible.sync="visible" append-to-body title="专家详情" :close-on-click-modal="false" :before-close="close" width="600px">

    <div class="flex around-center width-100p font-16 margin-bottom-18">

      <div class="flex column infoCard">
        <div>姓名：{{exportInfo.name}}</div>
        <div>性别：{{exportInfo.sex | sex}}</div>
        <div>邮箱：{{exportInfo.email}}</div>
        <div>联系电话：{{exportInfo.phone}}</div>
        <div>身份证号：{{exportInfo.idNumber}}</div>
        <div>职称：{{exportInfo.title | proTitle}}</div>
        <div>专业：{{exportInfo.major}}</div>
        <div>开户银行：{{exportInfo.bank}}</div>
        <div>银行卡号：{{exportInfo.bankAccount}}</div>
        <div>备注：{{exportInfo.remark}}</div>
        
      </div>  

      <!-- <div class="flex column infoCard">
        <div>职称：{{exportInfo.title | proTitle}}</div>
        <div>专业：{{exportInfo.major}}</div>
        <div>开户银行：{{exportInfo.bank}}</div>
        <div>银行卡号：{{exportInfo.bankAccount}}</div>
        <div>备注：{{exportInfo.remark}}</div>
      </div>   -->

    </div>


  
  </el-dialog>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'


export default {
  name: 'DialogExpertInfo',
  components: {
  },
  data () {
   
    return {
      visible: false, // 弹窗开关

      exportId: null,
      exportInfo: {},
      
    }
  },
  computed: {
    ...mapGetters({
      sex: 'common/sex',
      proTitle: 'common/proTitle'
    }),
  },

  methods: {
    // 获取专家详情数据
    async getClassroomDetail () {
      let {code, data} = await this.$api.Expert.detail({
        id: this.exportId
      })
      if(code !== 200) return false
      
      this.exportInfo = data
    },
    
    // 打开弹窗
    async open (data = {}) {
      this.visible = true

      // 获取教室id
      this.exportId = data.formData.id

      // 获取专家详情数据
      await this.getClassroomDetail()

      
    },
    // 关闭弹窗
    close () {
      this.visible = false
    },
   


  }
}
</script>

<style scoped lang="stylus">
.infoCard
  div
    margin-bottom 18px
</style>