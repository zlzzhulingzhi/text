<!--报事报修列表-->
<template>
  <view>
    <view v-if="isAdmin" class="flex justify-between p-4 head">
      <text @click="prevDate">前一天</text>
      <view>{{ $u.timeFormat(params.reportDate, 'yyyy年mm月dd日') }}</view>
      <text @click="nextDate">后一天</text>
    </view>
    <view v-if="isAdmin" class="flex justify-around py-4">
      <u-icon name="arrow-down" :label="(orgList.find(v => v.id === form.orgId) || {}).name || '请选择'" labelPos="left"
        @click="show.orgItem = true"></u-icon>
      <u-icon name="arrow-down" :label="(workOrderList.find(v => v.id === form.category) || {}).name || '请选择'"
        labelPos="left" @click="show.category = true"></u-icon>
    </view>
    <view v-if="isAdmin" class="p-2 bg-gray-100"></view>
    <template v-if="listData.length">
      <CardWorkerOrder :item="item" v-for="item in listData" :key="item.id" @click="goToDetail(item)">
      </CardWorkerOrder>
    </template>
    <u-loading-icon v-else-if="loading.list"></u-loading-icon>
    <uni-empty v-else></uni-empty>
    <u-picker :show="show.orgItem" :columns="[orgListComputed]" keyName="name" @cancel="cancelOrg"
      @confirm="confirmOrg"></u-picker>
    <u-picker :show="show.category" :columns="[workOrderListComputed]" keyName="name" @cancel="cancelCategory"
      @confirm="confirmCategory"></u-picker>
  </view>
</template>

<script>
import CardWorkerOrder from '@/components/views/CardWorkerOrder.vue'
import { mapGetters } from 'vuex'

export default {
  name: 'WorkOrderList',
  components: {
    CardWorkerOrder
  },
  data() {
    return {
      loading: {
        list: false
      },
      params: {
        current: 1,
        size: 10
      },
      show: {
        orgItem: false,
        category: false
      },
      form: {
        orgId: '',
        category: ''
      },
      total: 0,
      listData: []
    }
  },
  onLoad(options) {
    if (this.isAdmin) {
      let params = this.$utils.getOptions(options)
      Object.assign(this.params, params);
      // console.log('params === ', params);
      this.form.category = Number(params.category);
      this.form.orgId = Number(params.orgId);
      this.setColor();
    }

    this.getList();
  },
  onReachBottom() {
    if (this.listData.length >= this.total) return false;
    this.params.current = ++this.params.current;
    this.getList();
  },
  computed: {
    ...mapGetters({
      isLogin: 'system/isLogin',
      isAdmin: 'system/isAdmin',
      orgList: 'common/orgList',
      workOrderList: 'common/workOrderList',
    }),
    orgListComputed() {
      return [{ id: '', name: '全部' }, ...this.orgList];
    },
    workOrderListComputed() {
      return [{ id: '', name: '全部' }, ...this.workOrderList];
    }
  },
  methods: {
    async getList() {
      this.loading.list = true;
      let requestAction = this.isAdmin ? this.$api.WorkOrder.platMatterReportPage : this.$api.WorkOrder.matterReportPage
      if (this.isAdmin) {
        Object.assign(this.params, this.form);
      }
      let { code, data } = await requestAction(this.params);
      this.loading.list = false;
      if (code !== 200) return false;
      if (data.records && data.records.length) {
        let list = data.records;
        this.listData = this.listData.concat(list);
      }
      this.total = data.total;
    },
    goToPage(item) {
      if (!item.route) return false
      uni.$u.route(this.$utils.getRoutePath(item.route), item.params);
    },
    async goToDetail(item) {
      uni.$u.route(this.$utils.getRoutePath('WorkOrderDetail'), {
        id: item.id
      })
    },
    prevDate() {
      let dateStamp = this.getTimeStamp(this.params.reportDate);
      dateStamp = parseInt(dateStamp) - 24 * 60 * 60 * 1000;
      this.params.reportDate = this.$u.timeFormat(dateStamp, 'yyyy-mm-dd')
      this.clearData()
      this.getList()
    },
    nextDate() {
      let dateStamp = this.getTimeStamp(this.params.reportDate);
      dateStamp = parseInt(dateStamp) + 24 * 60 * 60 * 1000;
      this.params.reportDate = this.$u.timeFormat(dateStamp, 'yyyy-mm-dd')
      this.clearData()
      this.getList()
    },
    getTimeStamp(useDateStr) {
      if (useDateStr.indexOf('-') > 0) {
        return new Date(useDateStr).getTime();
      }
      return useDateStr;
    },
    clearData() {
      this.total = 0;
      this.listData = [];
      this.params.current = 1;
    },
    cancelOrg() {
      this.show.orgItem = false
    },
    confirmOrg({ value }) {
      this.form.orgId = value[0].id;
      this.cancelOrg();
      this.clearData();
      this.getList();
    },
    cancelCategory() {
      this.show.category = false
    },
    confirmCategory({ value }) {
      this.form.category = value[0].id;
      this.cancelCategory();
      this.clearData();
      this.getList();
    },
    // 对颜色和进场动画进行设置
    setColor() {
      uni.setNavigationBarColor({
        // 字体颜色 仅支持 #ffffff 和 #000000
        frontColor: '#ffffff',
        backgroundColor: '#C63636'
      });
    },
  },
}
</script>

<style lang="scss" scoped>
.head {
  color: $uni-text-color-inverse;
  background-color: $uni-color-primary;
}
</style>