<template>
  <div ref="item-wrap" class="item-wrap flex column">
    <svg class="absolute svg">
      <path :d="`
        M 0 ${content.radius + content.top} 
        Q 0 ${content.top}, ${content.radius} ${content.top} 
        L ${content.side_w + content.radius} ${content.top} 
        L ${content.side_w + content.radius + content.gradient_w} ${content.bar_h} 
        L ${content.side_w + content.radius + content.center_w} ${content.bar_h} 
        L ${content.side_w + content.radius + content.center_w + content.gradient_w} ${content.top} 
        L ${content.width - content.radius} ${content.top} 
        Q ${content.width} ${content.top}, ${content.width} ${content.radius} 
        L ${content.width} ${content.height - content.radius} 
        Q ${content.width} ${content.height}, ${content.width - content.radius} ${content.height} 
        L ${content.radius} ${content.height} 
        Q 0 ${content.height}, 0 ${content.height - content.radius} 
        Z`"
        fill="none"
        stroke="#0d3dc9"
        stroke-width="3">
      </path>
      <path :d="`
        M ${bar.start} 0 
        L ${bar.start + bar.length} 0 
        Q ${bar.start + bar.length - bar.radius / 2} ${bar.radius}, ${bar.start + bar.length - bar.radius / 2 - bar.radius} ${bar.radius} 
        L ${bar.start + bar.radius / 2 + bar.radius} ${bar.radius} 
        Q ${bar.start + bar.radius / 2} ${bar.radius}, ${bar.start} 0 
        Z`"
        fill="#0836bf"
        stroke="#0d3dc9"
        stroke-width="1">
      </path>
    </svg>

    <div class="item-title">
      <slot name="title"></slot>
    </div>

    <div class="flex-1 overflow">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ItemWrap',
  data() {
    return {
      content: {
        width: 500, // 组件宽
        height: 600, // 组件高
        radius: 20, // 圆角
        bar_h: 16, // 中间bar高
        gradient_w: 10, // 中间bar两边差距
        side_w: 138, // 顶部中间bar两边宽
        center_w: 172, // 中间bar底边宽
        top: 0 // 顶部边上边距
      }
    }
  },
  computed: {
    bar() {
      let length = this.content.center_w
      return {
        length, // bar
        start: (this.content.width - length) / 2 - 5,
        radius: 12
      }
    }
  },
  mounted() {
    let getScreen = () => {
      let width = this.$refs['item-wrap'].offsetWidth
      let height = this.$refs['item-wrap'].offsetHeight
      this.content = {
        ...this.content,
        width,
        height,
        side_w: (width - (this.content.center_w + this.content.gradient_w * 2) - this.content.radius * 2) / 2
      }
    }
    getScreen()
    let fn = () => {
      clearTimeout(this.timer)
      this.timer = setTimeout(getScreen, 100)
    }
    window.addEventListener('resize', fn)
    this.removeEvent = () => window.removeEventListener('resize', fn)
  },
  beforeDestroy() {
    this.removeEvent()
  }
}
</script>

<style scoped lang="stylus">
.item-wrap
  box-shadow 0 0 20px 4px #063cd1 inset
  // border 4px solid #0d3dc9
  border-radius 20px

  .item-title
    color #1AFDFF
    line-height 80px
    text-align center
    font-weight bold
    font-size 24px

  .svg
    width 100%
    height 100%
</style>