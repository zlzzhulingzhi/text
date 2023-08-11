<!--机构预报学员-->
<template>
  <view>
    <template v-if="listData.length">
      <view class="flex p-4 m-4 rounded shadow flex-col" v-for="item in listData" :key="item.id">
        <view class="w-full flex">
          <text>学员姓名：</text>
          <text>{{ item.realName }}</text>
        </view>
        <view class="w-full flex">
          <text>学员电话：</text>
          <text>{{ item.phone }}</text>
        </view>
        <view class="w-full flex">
          <text>创建时间：</text>
          <text>{{ item.createTime }}</text>
        </view>
      </view>
    </template>
    <u-loading-icon v-else-if="loading.list"></u-loading-icon>
    <uni-empty v-else></uni-empty>
  </view>
</template>

<script>

export default {
  name: 'ReserveStudent',
  data() {
    return {
      loading: {
        list: false
      },
      params: {
        current: 1,
        size: 10
      },
      total: 0,
      listData: []
    }
  },
  async onLoad(options) {
    const params = await this.$utils.getOptions(options);
    Object.assign(this.params, params);
    this.getList();
  },
  onReachBottom() {
    if (this.listData.length >= this.total) return false;
    this.params.current = ++this.params.current;
    this.getList();
  },
  methods: {
    async getList() {
      // console.log('this.params = ', this.params)
      this.loading.list = true;
      let { code, data } = await this.$api.Course.orgReserveStudent(this.params);
      this.loading.list = false;
      if (code !== 200) return false;
      // console.log('data.records == ', data.records);
      if (data.records && data.records.length) {
        let list = data.records;
        this.listData = this.listData.concat(list);
      }
      this.total = data.total;
    },

  },
}
</script>

<style lang="scss" scoped>

</style>