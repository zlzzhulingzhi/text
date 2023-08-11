<template>
  <view>
    <template v-if="listData.length">
      <view class="p-4 m-4 rounded shadow" v-for="item in listData" :key="item.id">
        <view class="flex justify-between items-center">
          <text class="text-lg">{{ item.visitorName }}</text>
          <text class="text-success" v-if="item.auditStats === 1">{{ item.auditStats | visitStatus }}</text>
          <text class="text-error" v-else-if="item.auditStats === 0">{{ item.auditStats | visitStatus }}</text>
          <text class="text-6" v-else>{{ item.auditStats | visitStatus }}</text>
        </view>
        <view class="text-6 mt-2">车牌号：{{ item.carNumber }}</view>
        <view class="text-6 mt-2">访问时间：{{ $u.timeFormat(item.visitTime, 'yyyy-mm-dd hh:MM') }}</view>
        <view class="text-6 mt-2">访问对象：{{ item.visitPeople }}</view>
        <view class="text-6 mt-2" v-if="!item.visitOrgId">访问部门：{{ item.visitDepartment }}</view>
        <view class="text-6 mt-2" v-if="item.visitOrgId">访问机构：{{ item.orgName }}</view>
        <view class="text-6 mt-2">访问事由：{{ item.visitReason }}</view>
        <view class="text-6 mt-2" v-if="item.auditResult">审批说明：{{ item.auditResult }}</view>
      </view>
    </template>
    <u-loading-icon v-else-if="loading"></u-loading-icon>
    <uni-empty v-else></uni-empty>
    <u-gap height="58"></u-gap>
    <view class="fixed bottom-0 left-0 right-0 p-3 bg-white">
      <u-button type="primary" icon="plus" text="新增登记" @click="onAdd"></u-button>
    </view>
  </view>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'MyVisit',
  data() {
    return {
      loading: false,
      form: {
        current: 1,
        size: 10
      },
      total: 0,
      listData: []
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    })
  },
  onLoad() {
    this.getList()
  },
  onReachBottom() {
    if (this.listData.length >= this.total) return false
    this.form.current = ++this.form.current
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let { code, data } = await this.$api.MemberVisit.page({
        ...this.form,
        memberId: this.userInfo.memberId
      })
      this.loading = false
      if (code !== 200) return false
      if (data.records && data.records.length) {
        this.listData = this.listData.concat(data.records)
      }
      this.total = data.total
    },
    onAdd() {
      uni.navigateTo({
        url: this.$utils.getRoutePath('AddVisit'),
        events: {
          addSuccess: () => {
            this.form.current = 1
            this.listData = []
            this.getList()
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>