<template>
  <u-empty class="pt-8" icon="/static/empty.png" :text="text"></u-empty>
</template>

<script>
export default {
  name: 'uni-empty',
  props: {
    text: {
      type: String,
      default: '暂无数据'
    }
  }
}
</script>

<style lang="scss" scoped>

</style>