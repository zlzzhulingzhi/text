<template>
  <el-dialog :visible.sync="visible" append-to-body title="学员详情" :close-on-click-modal="false" :before-close="close" width="800px">

    <el-descriptions class="margin-top" title="" :column="2">
      <el-descriptions-item label="学员姓名">{{ studentsInfo.realName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="手机号码">{{ studentsInfo.phone || '-' }}</el-descriptions-item>
      <el-descriptions-item label="身份号码">{{ studentsInfo.idNumber || '-' }}</el-descriptions-item>
      <el-descriptions-item label="性别">{{ studentsInfo.sex | sex }}</el-descriptions-item>
      <el-descriptions-item label="名族">{{ studentsInfo.nationName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="籍贯">{{ studentsInfo.nativePlace || '-' }}</el-descriptions-item>
      <el-descriptions-item label="邮箱">{{ studentsInfo.email || '-' }}</el-descriptions-item>
      <el-descriptions-item label="居住地">{{ studentsInfo.resideAddress || '-' }}</el-descriptions-item>
      <el-descriptions-item label="户籍住址">{{ studentsInfo.registerAddress || '-' }}</el-descriptions-item>
      
      <!-- <el-descriptions-item labelClassName="usePlace" v-if="orgNames?.length > 15"></el-descriptions-item>
      <el-descriptions-item label="机构">{{ orgNames }}</el-descriptions-item> -->
    </el-descriptions>
    
  </el-dialog>
</template>

<script>
import {mapActions, mapGetters, mapState} from 'vuex'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'

export default {
  name: 'DialogOrgStuInfo',
  mixins: [mxBaseDialog],
  data() {
    return {
      studentsInfo: {}
    }
  },
  created() {

  },
  mounted() {

  },
  computed: {
    ...mapGetters({
      sex: 'common/sex',
      // nationDict: 'common/nationDict'
    }),
    orgNames() {
      if(this.studentsInfo.orgNameList && this.studentsInfo.orgNameList?.length) {
        return this.studentsInfo.orgNameList.toString() || ''
      }
    }
  },
  watch: {

  },
  methods: {
    // 操作 - 初始化数据
    async initData(params) {
      this.studentsInfo = {}
      let {code, data} = await this.$api.Student.dDetail({
        id: params.id
      })
      if(code !== 200) return false
      this.studentsInfo = data
    },
  }
}
</script>

<style scoped lang="stylus">
>>>.usePlace
 display none
</style>