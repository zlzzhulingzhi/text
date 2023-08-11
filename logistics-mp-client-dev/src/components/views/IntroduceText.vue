<template>
  <view class="flex flex-col px-4">
    <view class="w-full" :class="index !== introduceTextArr.length - 1 ? 'notLastText' : ''"
      v-for="item, index in introduceTextArr" :key="index">
      <u--text :text="item.text" color="#666" size="15" lineHeight="26"></u--text>
    </view>
  </view>
</template>

<script>
export default {
  name: 'IntroduceText',
  props: {
    introduceTextArr: {
      type: Array
    }
  },
};
</script>

<style lang="scss" scoped>
.notLastText {
  margin-bottom: 10px;
}
</style>