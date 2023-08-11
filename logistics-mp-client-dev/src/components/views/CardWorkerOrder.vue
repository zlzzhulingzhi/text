<template>
  <view class="flex p-4 m-4 rounded shadow flex-col" @click="onClick">
    <view class="w-full flex">
      <text class="w-14">类别：</text>
      <text>{{ item.category | workOrderList }}</text>
    </view>
    <view class="w-full flex">
      <text class="w-14">日期：</text>
      <text>{{ item.createTime }}</text>
    </view>
    <view v-if="item.description" class="w-full flex">
      <text class="w-16">说明：</text>
      <text class="w-80" style="word-break:break-all">{{ item.description }}</text>
    </view>
  </view>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'CardWorkerOrder',
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  methods: {
    onClick() {
      this.$emit('click')
    }
  },
  computed: {
    ...mapGetters({
      workOrderList: 'common/workOrderList'
    })
  }
}
</script>

<style lang="scss" scoped>
.shadow {
  box-shadow: 0 3px 20px rgba(223, 223, 223, 0.6);
}
</style>