<template>
  <view>
    <!-- 顶部总数统计 -->
    <view class="flex">
      <view
        v-for="(item, index) in basicData"
        :key="index"
        class="flex-1 flex flex-col justify-center items-center h-20 rounded mx-2 my-2 text-white"
        :class="item.bgColor">
        <text>{{ item.num }}</text>
        <text class="mt-1">{{ item.title }}</text>
      </view>
    </view>

  </view>
  
</template>

<script>

export default {
  name: 'TopSum',
  data() {
    return {
      // 顶部  学员、机构  数量
      basicData: [],

      loading: {
        topSum: false
      }
    }  
  },
  created() {
    this.getTopSumData()
  },
  methods: {
    async getTopSumData() {
      this.loading.topSum = true
      let { code, data } = await this.$api.Static.topSum();
      this.loading.topSum = false
      if (code !== 200) return false;
      Object.keys(data).forEach(item => {
        let basicDataItem = {
          title: null,
          num: null,
          bgColor: null
        }
        if(item === 'orgNum') {
          basicDataItem.title = '入围机构'
          basicDataItem.num = data[item] || 0
          basicDataItem.bgColor = 'bg-green-500'
        } else {
          basicDataItem.title = '培训人次'
          basicDataItem.num = data[item] || 0
          basicDataItem.bgColor = 'bg-blue-500'
        }
        this.basicData.push(basicDataItem)
      })
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
