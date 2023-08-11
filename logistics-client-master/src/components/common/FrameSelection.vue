<template>
  <div class="frame-selection" ref="FrameSelection" :style="styleObj"></div>
</template>

<script>
const getElementGlobalPosition = function (el) {
  let x = el.offsetLeft
  let y = el.offsetTop
  let parent = el.offsetParent
  while (parent) {
    x += parent.offsetLeft + parent.clientLeft
    y += parent.offsetTop + parent.clientTop
    parent = parent.offsetParent
  }
  return {x, y, ex: x + el.offsetWidth, ey: y + el.offsetHeight}
}

export default {
  name: 'FrameSelection',
  created() {
    let evFn = ev => this.onMousedown(ev)
    document.addEventListener('mousedown', evFn)
    this.destroyFn = () => document.removeEventListener('mousedown', evFn)
  },
  mounted() {
    this.frameSelection = getElementGlobalPosition(this.$refs.FrameSelection)
  },
  destroyed() {
    this.destroyFn && this.destroyFn()
  },
  props: {
    dragTargetList: {
      type: Array,
      default(){
        return []
      }
    }
  },
  data() {
    return {
      startPointer: null,
      endPointer: null,
      frameSelection: {
        x: 0,
        y: 0,
        ex: 0,
        ey: 0
      }
    }
  },
  computed: {
    pointer() {
      let {startPointer, endPointer} = this
      let p = [{}, {}]
      if (!startPointer || !endPointer) return null
      if (startPointer.x < endPointer.x) {
        p[0].x = startPointer.x
        p[1].x = endPointer.x
      } else {
        p[1].x = startPointer.x
        p[0].x = endPointer.x
      }
      if (startPointer.y < endPointer.y) {
        p[0].y = startPointer.y
        p[1].y = endPointer.y
      } else {
        p[1].y = startPointer.y
        p[0].y = endPointer.y
      }
      return p
    },
    styleObj() {
      if (!this.pointer) return {}
      return {
        top: `${this.pointer[0].y - this.frameSelection.y}px`,
        left: `${this.pointer[0].x - this.frameSelection.x}px`,
        width: `${this.pointer[1].x - this.pointer[0].x}px`,
        height: `${this.pointer[1].y - this.pointer[0].y}px`
      }
    }
  },
  watch: {
    pointer: {
      deep: true,
      handler(p) {
        this.$emit('input',p)
      }
    }
  },
  methods: {
    onMousedown(ev) {
      if (!this.dragTargetList.length) return false

      this.startPointer = {
        x: ev.clientX,
        y: ev.clientY
      }

      let evFn1 = ev1 => {
        this.endPointer = {
          x: ev1.clientX,
          y: ev1.clientY
        }
        ev1.preventDefault()
      }
      let evFn2 = ev2 => {
        this.startPointer = null
        this.endPointer = null
        document.body.focus()
        document.removeEventListener('mousemove', evFn1)
        document.removeEventListener('mouseup', evFn2)
      }
      document.addEventListener('mousemove', evFn1)
      document.addEventListener('mouseup', evFn2)

      // ev.preventDefault()
    }
  }
}
</script>

<style scoped lang="stylus">
.frame-selection {
  position: fixed;
  background-color: BACKGROUND_COLOR;
  opacity: .5;
  z-index: 99999;
  pointer-events: none;
}
</style>