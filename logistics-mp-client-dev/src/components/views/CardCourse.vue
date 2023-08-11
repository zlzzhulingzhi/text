<template>
  <view class="flex p-4 m-4 rounded shadow" @click="onClick">
    <u--image :src="item.coverUrl" width="100" height="75" :radius="4"></u--image>
    <view class="flex-1 flex flex-col justify-between ml-4">
      <text class="text-lg u-line-2">{{ item.courseName }}</text>
      <view class="flex justify-between items-end">
        <view v-if="isStudent" class="flex-1 flex items-center">
          <text class="text-sm text-6">{{ item.orgName }}</text>
        </view>
        <view v-else class="flex-1 flex items-center">
          <text class="text-sm text-6">{{ item.lecturers.length === 1 ? item.lecturers[0].lecturerName :
              item.lecturers[0].lecturerName + '...'
          }}</text>
        </view>
        <slot name="action">
          <view class="ml-2">
            <u-button type="success" shape="circle" size="small" v-if="item.isSignUp">已报名</u-button>
            <u-button type="primary" shape="circle" size="small" v-else>立即报名</u-button>
          </view>
        </slot>
      </view>
    </view>
  </view>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'CardCourse',
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  computed: {
    ...mapGetters({
      isStudent: 'system/isStudent',
    })
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