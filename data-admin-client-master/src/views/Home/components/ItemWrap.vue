<template>
  <div class="item-wrap flex column relative" :class="itemClass">
    <el-image class="absolute backgroud" :src="backgroud" v-if="backgroud"></el-image>

    <div class="item-title" :style="{ fontSize: $utils.getScaleSize('22px') }">
      <slot name="title"></slot>
    </div>

    <div class="item-vand"></div>

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
    },
    heightFull: {
      type: Boolean,
      default: false
    },
    heightHalfFull: {
      type: Boolean,
      default: false
    },
    heightSixtyFull: {
      type: Boolean,
      default: false
    },
    heightThirtyFull: {
      type: Boolean,
      default: false
    },
    heightMiddle: {
      type: Boolean,
      default: false
    },
    heightSmall: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    itemClass() {
      let { backgroud, heightFull, heightMiddle, heightSmall, heightHalfFull, heightSixtyFull, heightThirtyFull } = this
      return {
        backgroud: !backgroud,
        heightFull,
        heightMiddle,
        heightSmall,
        heightHalfFull,
        heightSixtyFull,
        heightThirtyFull
      }
    }
  }
}
</script>

<style scoped lang="stylus">
.item-wrap
  &.heightFull {
    height 100%
  }
  &.heightSixtyFull {
    height 60%
  }
  &.heightHalfFull {
    height 50%
  }
  &.heightMiddle {
    height 40%
  }
  &.heightThirtyFull {
    height 30%
  }
  &.heightSmall {
    height 20%
  }
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
    color #FEFFFF
    // line-height 80px
    text-align center
    font-weight bold
    font-size 20px
    position absolute
    left 100px
    top -6px
    
  .item-vand
    margin 46px 0 20px
</style>