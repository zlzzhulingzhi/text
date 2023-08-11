<template>
  <div class="item-wrap flex column relative px-2">
    <div class="item-title" :style="{ fontSize: $utils.getScaleSize('18px') }">
      <slot name="title"></slot>
    </div>

    <div class="flex-1 overflow padding-10">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ItemSubWrap',
  props: {},
  computed: {}
}
</script>

<style scoped lang="stylus">
.item-wrap
  .item-title
    color #1AFDFF
    text-align center
    font-weight bold
    margin 10px 0px 10px
</style>