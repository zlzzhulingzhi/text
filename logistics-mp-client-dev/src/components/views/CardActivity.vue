<template>
  <view class="flex p-4 m-4 rounded shadow" @click="onClick">
    <u--image :src="item.coverUrl" width="100" height="75" :radius="4"></u--image>
    <view class="flex-1 flex flex-col ml-4">
      <text class="text-lg u-line-2">{{ item.title }}</text>
      <view class="flex h-8 items-center" v-if="item.activityTime">
        <u-icon name="clock-fill" color="#CCCCCC" size="16"></u-icon>
        <text class="text-sm text-gray-600 ml-2">{{ activityTime }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'CardActivity',
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  computed: {
    ...mapGetters({
      isStudent: 'system/isStudent',
    }),
    activityTime() {
      return uni.$u.timeFormat(this.item.activityTime,'mm月dd日 hh:MM')
    }
  },
  methods: {
    onClick() {
      this.$emit('click')
    }
  }
}
</script>

<style lang="scss" scoped>
.shadow {
  box-shadow: 0 3px 20px rgba(223, 223, 223, 0.6);
}
</style>