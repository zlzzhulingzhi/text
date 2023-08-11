<!-- 开课信息 -->
<template>
  <view>
    <view class="flex justify-center items-center py-4 head">
      <view>{{ `${$u.timeFormat(form.startDate, 'yyyy年mm月dd日')}-${$u.timeFormat(form.endDate, 'yyyy年mm月dd日')}` }}
      </view>
    </view>
    <view class="flex flex-wrap justify-between px-4 mt-4">
      <u-icon name="arrow-down" :label="$u.timeFormat(form.startDate, 'yyyy年mm月dd日')" labelPos="left"
        @click="show.useBeginDate = true"></u-icon>
      <u-icon name="arrow-down" :label="$u.timeFormat(form.endDate, 'yyyy年mm月dd日')" labelPos="left"
        @click="show.useEndDate = true"></u-icon>
    </view>
    <view class="mt-4 pr-4 pb-4">
      <u-icon class="mt-4" name="arrow-down" :label="(orgList.find(v => v.id === Number(form.orgId)) || {}).name || '请选择'" labelPos="left"
        @click="show.orgItem = true"></u-icon>
    </view>
    <view class="p-2 bg-gray-100"></view>
    <template v-if="listData.length">
      <view v-for="item, index in listData" :key="index" class="flex p-4 m-4 rounded shadow flex-col">
        <view class="w-full flex">
          <text>{{ item.orgName }}</text>
        </view>
        <view class="w-full flex">
          <text>班级：</text>
          <text>{{ item.clazzName }}</text>
        </view>
        <view class="w-full flex">
          <text>学员人数：</text>
          <text>{{ item.studentNum }}</text>
        </view>
      </view>
    </template>
    <uni-empty v-else></uni-empty>

    <u-picker :show="show.orgItem" :columns="[orgListComputed]" keyName="name" @cancel="cancelOrg"
      @confirm="confirmOrg"></u-picker>
    <u-datetime-picker :show="show.useBeginDate" v-model="form.startDate" mode="date" @cancel="cancelBeginDate"
      @confirm="confirmBeginDate"></u-datetime-picker>
    <u-datetime-picker :show="show.useEndDate" v-model="form.endDate" mode="date" @cancel="cancelEndDate"
      @confirm="confirmEndDate"></u-datetime-picker>
  </view>
</template>

<script>
import { mapGetters, mapState } from 'vuex'

export default {
  data() {
    return {
      form: {
        startDate: '',
        endDate: '',
        orgId: null,
      },
      params: {
        current: 1,
        size: 10,
        startDate: '',
        endDate: '',
        orgId: null,
      },
      show: {
        useBeginDate: false,
        useEndDate: false,
        orgItem: false
      },
      listData: [],
      total: 0,
      isWrongDate: false
    }
  },
  computed: {
    ...mapState('config', {
      cosPath: 'COS_PATH'
    }),
    ...mapGetters({
      orgList: 'common/orgList',
    }),
    orgListComputed() {
      return [{id:'',name:'全部'},...this.orgList];
    }
  },
  onLoad(options) {
    const params = this.$utils.getOptions(options)
    this.form = params;

    this.getDetail();
  },

  onReachBottom() {
    if (this.listData.length >= this.total) return false;
    this.params.current = ++this.params.current;
    this.getDetail();
  },

  methods: {
    async getDetail() {
      uni.showLoading()
      await this.$nextTick();
      this.params.startDate = this.$u.timeFormat(this.form.startDate, 'yyyy-mm-dd'),
        this.params.endDate = this.$u.timeFormat(this.form.endDate, 'yyyy-mm-dd')
      this.params.orgId = this.form.orgId;
      // console.log('params1111 == ', this.params);
      let { code, data } = await this.$api.WorkOrder.openClassPage(this.params);
      uni.hideLoading()
      // console.log('code == ', code);
      // console.log('data == ', data);
      if (code !== 200) return false
      if (data.records && data.records.length) {
        let list = data.records;
        this.listData = this.listData.concat(list);
      }
      this.total = data.total;
    },
    cancelBeginDate() {
      this.show.useBeginDate = false
    },
    async confirmBeginDate() {
      this.cancelBeginDate()
      let oldDate = this.form.startDate;
      await this.$nextTick();
      let { startDate, endDate } = this.form;
      this.checkDateWrong(startDate, endDate);
      if (this.isWrongDate) {
        this.form.startDate = oldDate;
        return this.$message.showToast('开始时间选择错误，请重新选择');
      }
      this.clearData();
      this.getDetail();
    },
    cancelEndDate() {
      this.show.useEndDate = false
    },
    async confirmEndDate() {
      this.cancelEndDate()
      let oldDate = this.form.endDate;
      await this.$nextTick();
      let { startDate, endDate } = this.form;
      this.checkDateWrong(startDate, endDate);
      if (this.isWrongDate) {
        this.form.endDate = oldDate;
        return this.$message.showToast('结束时间选择错误，请重新选择');
      }
      this.clearData();
      this.getDetail()
    },
    cancelOrg() {
      this.show.orgItem = false
    },
    confirmOrg({ value }) {
      this.form.orgId = value[0].id;
      this.cancelOrg();
      this.clearData();
      this.getDetail();
    },
    clearData() {
      this.total = 0;
      this.listData = [];
      this.params.current = 1;
    },
    checkDateWrong(startDate, endDate) {
      if (String(startDate).indexOf('-') > -1) {
        startDate = new Date(startDate).getTime();
      }

      if (String(endDate).indexOf('-') > -1) {
        endDate = new Date(endDate).getTime();
      }

      this.isWrongDate = Number(endDate) - Number(startDate) < 0;
    }
  },
}
</script>
<style lang="scss" scoped>
.head {
  color: $uni-text-color-inverse;
  background-color: $uni-color-primary;
}

.shadow {
  box-shadow: 0 3px 20px rgba(223, 223, 223, 0.6);
}
</style>