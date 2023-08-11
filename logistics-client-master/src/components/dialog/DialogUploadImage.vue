<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" :width="style.dialog">

    <!--应用创建、编辑-->
    <template v-if="/^(ImageEdit)$/.test(dialogType)">
      <div class="overflow"
           :style="style.cropperWrapper">
        <VueCropper ref="cropper"
                    :style="style.cropper"
                    :img="options.img"
                    :outputSize="options.outputSize"
                    :outputType="options.outputType"
                    :info="options.info"
                    :canScale="options.canScale"
                    :autoCrop="options.autoCrop"
                    :autoCropWidth="options.autoCropWidth"
                    :autoCropHeight="options.autoCropHeight"
                    :fixed="options.fixed"
                    :fixedNumber="options.fixedNumber"
                    :full="options.full"
                    :fixedBox="options.fixedBox"
                    :canMove="options.canMove"
                    :canMoveBox="options.canMoveBox"
                    :original="options.original"
                    :centerBox="options.centerBox"
                    :high="options.high"
                    :infoTrue="options.infoTrue"
                    :maxImgSize="options.maxImgSize"
                    :enlarge="options.enlarge"
                    :mode="options.mode"
        ></VueCropper>
      </div>

      <div class="text-center">
        <el-button type="text" icon="el-icon-refresh-left" @click="onRotateLeft"></el-button>
        <el-button type="text" icon="el-icon-refresh-right" @click="onRotateRight"></el-button>
        <el-button type="text" icon="el-icon-refresh" @click="onSelectFile">重选图片</el-button>
      </div>

      <div class="text-center">
        <el-button class="width-80 margin-right-14" type="primary" size="small" @click="onSubmit">确定</el-button>
        <el-button class="width-80" size="small" @click="close">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import {VueCropper} from 'vue-cropper'

export default {
  name: 'DialogUploadImage',
  components: {
    VueCropper
  },
  data() {
    let defaultOptions = {
      img: null, // 裁剪图片的地址   url 地址, base64, blob
      outputSize: 1, // 裁剪生成图片的质量   0.1 ~ 1
      outputType: 'png', // 裁剪生成图片的格式   jpeg, png, webp
      info: true, // 裁剪框的大小信息   true, false
      canScale: true, // 图片是否允许滚轮缩放   true, false
      autoCrop: true, // 是否默认生成截图框   true, false
      autoCropWidth: 80, // 默认生成截图框宽度  0 ~ max
      autoCropHeight: 80, // 默认生成截图框高度  0 ~ max
      fixed: true, // 是否开启截图框宽高固定比例   true, false
      fixedNumber: [1, 1], // 截图框的宽高比例   [ 宽度 , 高度 ]
      full: false, // 是否输出原图比例的截图   true, false
      fixedBox: false, // 固定截图框大小   false
      canMove: true, // 上传图片是否可以移动  true, false
      canMoveBox: true, // 截图框能否拖动   true, false
      original: false, // 上传图片按照原始比例渲染   true, false
      centerBox: true, // 截图框是否被限制在图片里面    true, false
      high: true, // 是否按照设备的dpr 输出等比例图片    true, false
      infoTrue: false, // true 为展示真实输出图片宽高 false 展示看到的截图框宽高    true, false
      maxImgSize: 2000, // 限制图片最大宽度和高度    0 ~ max
      enlarge: 1, // 图片根据截图框输出比例倍数    0 ~ max(建议不要太大不然会卡死的呢)
      mode: 'contain' // 图片默认渲染方式    contain , cover, 100px, 100% auto
    }
    return {
      visible: false, // 弹窗开关

      dialogType: 'ImageEdit',
      typeMapping: {
        ImageEdit: {
          title: '编辑图片'
        }
      },

      // VueCropper 配置项
      defaultOptions,
      options: {
        ...defaultOptions
      }
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    style() {
      let {clientWidth, clientHeight} = document.body
      let bw = clientWidth - 8 - 64
      let bh = clientHeight - 300

      let w = this.options.autoCropWidth
      let h = this.options.autoCropHeight
      let zoom = []

      let zW = bw / w
      let zH = bh / h


      if (zW < zH) {
        zoom = zW
        h = bh / zoom
      } else {
        zoom = zH
        w = bw / zoom
      }

      if (zoom > 1) {
        zoom = 1
        w = bw
        h = bh
      }

      return {
        cropper: {
          height: `${h}px`,
          width: `${w}px`,
          zoom,
          fontSize: `${16/zoom}px`
        },
        cropperWrapper: {
          width: `${bw}px`,
          height: `${bh}px`
        },
        dialog: `${clientWidth - 8}px`
      }
    }
  },

  methods: {
    // 打开弹窗
    async open(data = {}) {
      this.dialogType = data.type || 'ImageEdit'
      this.options = {
        ...this.defaultOptions,
        ...data.options
      }

      this.visible = true
      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type !== 'submit') return reject('取消')

          this.$refs.cropper.getCropData(fileData => {
            this.$refs.cropper.getCropBlob(fileBlob => {

              resolve({
                fileData,
                fileBlob
              })

            })
          })
        })
      })
    },
    onRotateLeft() {
      this.$refs.cropper.rotateLeft()
    },
    onRotateRight() {
      this.$refs.cropper.rotateRight()
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
    },
    onSelectFile() {
      this.$emit('handle', 'close')
      this.$emit('SelectFile')
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-dialog
  max-width 100vw
  margin-top 32px !important

//transform scale(0.7)
//transform-origin top center

>>> .cropper-crop-box
  font-size inherit

  .crop-info
    font-size inherit
    line-height 1
    white-space nowrap
</style>