<template>
  <svg class="absolute svg" :width="screen.width" :height="screen.height">
    <defs>
      <radialGradient id="light" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">
        <stop offset="0%" style="stop-color:#FFF;stop-opacity:1"/>
        <stop offset="10%" style="stop-color:#FFF;stop-opacity:0.6"/>
        <stop offset="20%" style="stop-color:#FFF;stop-opacity:0.3"/>
        <stop offset="80%" style="stop-color:#FFF;stop-opacity:0"/>
      </radialGradient>
      <linearGradient id="titleSvg" x1="0%" y1="100%" x2="0%" y2="0%">
        <stop offset="0%" style="stop-color:#1e62ec;stop-opacity:1"/>
        <stop offset="100%" style="stop-color:#0636c4;stop-opacity:1"/>
      </linearGradient>
    </defs>
    <!-- <path :d="outLine.d" :fill="outLine.fill" :stroke="outLine.stroke"
          :stroke-width="outLine.strokeWidth"></path> -->
    <!-- <rect :width="topDec.width" :height="topDec.height" :fill="topDec.fill"></rect>
    <path :d="titleSvg.d" :fill="titleSvg.fill" :stroke="titleSvg.stroke"
          :stroke-width="titleSvg.strokeWidth" style="fill:url(#titleSvg)"></path>
    <path :d="titleDec.d" :fill="titleDec.fill" :stroke="titleDec.stroke"
          :stroke-width="titleDec.strokeWidth"></path>
    <path :d="titleDecLight.d" :fill="titleDecLight.fill" :stroke="titleDecLight.stroke"
          :stroke-width="titleDecLight.strokeWidth" style="fill:url(#light)"></path> -->
    <image :x="(screen.width - 1920) / 2" :xlink:href="require('@/assets/main/new_top.gif')"></image>
    <text :x="screen.width / 2 + 6" :y="options.mg + options.title_h * 0.6" font-size="30" fill="#FFF"
          style="letter-spacing: 4px">
      {{ options.titleText }}
    </text>
  </svg>
</template>

<script>
export default {
  name: 'SvgBG',
  props: {
    // 配置数据
    options: {
      type: Object,
      default() {
        return {}
      }
    },
    screen: {
      type: Object,
      default() {
        return {
          width: 0,
          height: 0
        }
      }
    },
  },
  data() {
    return {
      timer: null,
      removeEvent: null
    }
  },
  computed: {
    // 图形 - 外边框
    outLine() {
      let {width, height} = this.screen

      let {mg, line_r, line_Rs, line_Start, line_End} = this.options

      // 左边坐标点集合
      let pointers = [
        /*[width - (line_r + mg), mg],
        [width - mg, line_r + mg],
        [width - mg, height * line_Start],
        [width - (line_r + mg) * line_Rs, height * line_Start + line_r],
        [width - (line_r + mg) * line_Rs, height * (line_End)],
        [width - mg, height * (line_End) + line_r],
        [width - mg, height - (line_r + mg)],
        [width - (line_r + mg), height - mg],*/

        // 对称的半边
        [line_r + mg, height - mg],
        [mg, height - (line_r + mg)],
        [mg, height * (line_End) + line_r],
        [(line_r + mg) * line_Rs, height * (line_End)],
        [(line_r + mg) * line_Rs, height * line_Start + line_r],
        [mg, height * line_Start],
        [mg, line_r + mg],
        [line_r + mg, mg]
      ]

      return {
        d: this.createPath(pointers),
        strokeWidth: 2,
        fill: 'none',
        stroke: '#0a3fc9'
      }
    },
    topDec() {
      let {width} = this.screen
      let {topbar_h} = this.options
      return {
        width: width,
        height: topbar_h,
        fill: '#0636c4'
      }
    },
    titleSvg() {
      let {width, height} = this.screen

      let {mg, title_h, title_w, title_deg, deco_w, deco_h} = this.options

      let x_1 = (width - title_w) / 2
      let tan_x = Math.tan(title_deg / 180 * Math.PI)

      // 左边坐标点集合
      let pointers = [
        [x_1 + title_h / tan_x + (title_w - deco_w) / 2 + deco_h / tan_x, mg + title_h - deco_h],
        [x_1 + title_h / tan_x + (title_w - deco_w) / 2, mg + title_h],
        [x_1 + title_h / tan_x, mg + title_h],
        [x_1, mg]
      ]


      return {
        d: this.createPath(pointers),
        strokeWidth: 2,
        fill: '#0a3fc9',
        stroke: 'none'
      }
    },
    titleDec() {
      let {width, height} = this.screen

      let {mg, title_h, title_w, title_deg, deco_w, deco_h, deco_mg} = this.options

      let x_1 = (width - title_w) / 2
      let tan_x = Math.tan(title_deg / 180 * Math.PI)

      // 左边坐标点集合
      let pointers = [
        [x_1 + title_h / tan_x + (title_w - deco_w) / 2 + deco_h / tan_x + deco_mg, mg + title_h - deco_h + deco_mg],
        [x_1 + title_h / tan_x + (title_w - deco_w) / 2 + deco_mg, mg + title_h],
        [x_1 + title_h / tan_x + (title_w - deco_w) / 2 + deco_h / tan_x + deco_mg, mg + title_h + deco_h - deco_mg]
      ]

      return {
        d: this.createPath(pointers),
        strokeWidth: 3,
        fill: '#01c9c6',
        stroke: 'none'
      }
    },
    titleDecLight() {
      let {width, height} = this.screen

      let {mg, title_h, title_w, title_deg, deco_w, deco_h, deco_mg} = this.options

      let x_1 = (width - title_w) / 2
      let tan_x = Math.tan(title_deg / 180 * Math.PI)
      deco_h = 30

      // 左边坐标点集合
      let pointers = [
        [x_1 + title_h / tan_x + (title_w - deco_w) / 2, mg + title_h - deco_h + deco_mg],
        [x_1 + title_h / tan_x + (title_w - deco_w) / 2, mg + title_h],
        [x_1 + title_h / tan_x + (title_w - deco_w) / 2, mg + title_h + deco_h - deco_mg]
      ]

      return {
        d: this.createPath(pointers),
        strokeWidth: 3,
        fill: 'none',
        stroke: 'none'
      }
    }
  },
  methods: {
    // 工具 - 生成坐标路径
    createPath(pointers) {
      pointers = [...pointers.map(([x, y]) => [this.screen.width - x, y]).reverse(), ...pointers]
      return pointers.map(([x, y], i) => `${i ? 'L' : 'M'}${x},${y}`).join(' ') + ' Z'
    }
  }
}
</script>

<style scoped lang="stylus">
.svg
  top 0
  left 0
  right 0
  user-select none

text
  user-select none
  text-anchor middle
  font-weight 700
</style>