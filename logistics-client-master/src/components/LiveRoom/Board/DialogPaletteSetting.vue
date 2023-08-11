<template>
  <el-dialog :visible.sync="visible" append-to-body title="工具样式" :close-on-click-modal="false"
             :before-close="close" width="420px">

    <div>
      <div>画笔粗细</div>
      <div class="flex start-center">
        <div class="width-48 shrink-0">{{ formData.thin }}</div>
        <el-slider class="flex-1" v-model="formData.thin" :show-tooltip="false"
                   :step="10" :min="10" :max="500"></el-slider>
      </div>
    </div>

    <div>
      <div>文本大小</div>
      <div class="flex start-center">
        <div class="width-48 shrink-0">{{ formData.fontSize }}</div>
        <el-slider class="flex-1" v-model="formData.fontSize" :show-tooltip="false"
                   :step="10" :min="100" :max="500"></el-slider>
      </div>
    </div>

    <div>
      <div>当前工具颜色</div>
      <div class="flex relative">
        <div class="width-36 height-36 margin-right-12 margin-top-8 shrink-0 border-c"
             :style="{backgroundColor: formData.color}"></div>
        <div class="flex center-center color-picker-wrapper">
          <el-color-picker ref="ColorPicker" v-model="formData.color"
                           :popper-class="visible ? 'dialog-palette':'dialog-palette-hide'"
                           @active-change="formData.color = $event"></el-color-picker>
        </div>
      </div>
    </div>

    <div>
      <div>当前背景颜色</div>
      <div class="flex relative">
        <div class="width-36 height-36 margin-right-12 margin-top-8 shrink-0 border-c"
             :style="{backgroundColor: formData.backgroundColor}"></div>
        <div class="flex center-center color-picker-wrapper">
          <el-color-picker ref="BackgroundColorPicker" v-model="formData.backgroundColor"
                           :popper-class="visible ? 'dialog-palette p-bgc':'dialog-palette-hide'"
                           @active-change="formData.backgroundColor = $event"></el-color-picker>
        </div>
      </div>
    </div>

    <div class="text-center margin-top-8">
      <el-button class="width-80 margin-right-6" type="primary" size="medium" @click="onSubmit">确认</el-button>
      <el-button class="width-80" size="medium" @click="close">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'DialogPaletteSetting',
  data() {
    return {
      visible: false, // 弹窗开关

      formData: {
        thin: null,
        fontSize: null,
        color: null,
        backgroundColor: null
      },

      loading: {
        submit: false
      }
    }
  },
  methods: {
    // 打开弹窗
    async open(data = {}) {
      this.visible = true

      this.formData = data.formData

      this.$nextTick(() => {
        this.$refs.ColorPicker.showPicker = true
        this.$refs.ColorPicker.hide = () => {
        }
        this.$refs.BackgroundColorPicker.showPicker = true
        this.$refs.BackgroundColorPicker.hide = () => {
        }
      })
      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.formData)
          reject('取消')
        })
      })
    },

    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle', 'close')
    },
    // 确定提交
    async onSubmit() {
      this.visible = false
      this.$emit('handle', 'submit')
    }

  }
}
</script>

<style scoped lang="stylus">
.color-picker-wrapper
  width 308px
  height 188px
</style>

<style lang="stylus">
.dialog-palette
  display block !important
  position fixed !important
  top calc(15vh + 216px) !important
  left calc(50vw - 210px + 80px) !important
  padding 4px
  box-shadow none
  border none
  z-index 99999 !important

  &.p-bgc
    top calc(15vh + 421px) !important

  .el-color-dropdown__main-wrapper
    margin-bottom 0

  .el-color-dropdown__btns
    display none

.dialog-palette-hide
  display none !important
</style>