<template>
  <div class="item-wrap flex column relative" :class="{ backgroud: !backgroud }">
    <el-image class="absolute backgroud" :src="backgroud" v-if="backgroud"></el-image>

    <div class="item-title" :style="{ fontSize: $utils.getScaleSize('22px') }">
      <slot name="title"></slot>
    </div>

    <div class="flex-1 overflow padding-10">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ItemWrap',
  props: {
    backgroud: {
      type: String
    }
  }
}
</script>

<style scoped lang="stylus">
.item-wrap
  height 60%

  &.backgroud
    box-shadow 0 0 20px 4px #063cd1 inset
    border 4px solid #0d3dc9
    border-radius 16px

  .backgroud
    top 0
    bottom 0
    left 0
    right 0

  .item-title
    color #1AFDFF
    // line-height 80px
    text-align center
    font-weight bold
    font-size 24px
    margin 20px 0 20px
</style>