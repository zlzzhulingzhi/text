<template>
  <div class="cert-template-view text-3" v-show="info.isEdit || id || value">
    <!--模板选择区域-->
    <div v-if="info.isEdit" class="flex margin-bottom-12"
         :class="certTemplateTypeList[2]&&certTemplateTypeList[2].value ? 'start-center':''">
      <div class="template-wrap flex center-start el-icon-check relative pointer margin-right-16 width-140 height-100"
           :class="{active: modelType === item.id,'image-bg': !item.value}"
           v-for="item in certTemplateTypeList" :key="item.id"
           @click="onSelectModal(item)">
        <el-image class="height-100" v-if="item.value" :src="item.value"></el-image>
        <div class="absolute text-center width-100p line-height-20 font-15 text-bold">{{ item.name }}</div>
      </div>
    </div>

    <div class="flex start-start">
      <!--预览、编辑区域-->
      <div>
        <div class="border-9">
          <div class="cert-wrapper relative"
               :style="{zoom: info.zoom}"
               @mousedown="onMouseDown" @wheel.prevent="onWheel" v-loading="loading.page">
            <!-- /* width: `${backgroundWidth}px`  */ -->
            <!--元素-->
            <template v-for="(el,code) in elMaps">
              <el-image ref="backgroundUrl" class="flex" v-if="code === 'backgroundUrl'"
                        :src="backgroundUrl || el.value"
                        :key="code" @load="setBackgroundHeight">
                <div slot="error" class="width-100p text-bold">
                  <div class="font-36 flex center-center column bg-background text-6"
                       :class="{'image-bg': modelType === 100}"
                       :style="{width: backgroundWidth + 'px',height: `${backgroundWidth * .75}px`}">
                    <div v-if="modelType === 100"></div>
                    <div v-else>加载失败</div>
                  </div>
                </div>
              </el-image>
              <div v-else :key="code" :code="code" v-show="el.visible"
                   class="element break-all" :class="{'is-active': code === activeIndex,edit: info.isEdit}"
                   :style="parseStyle(el.style)">
                <template v-if="code === 'headImg'">
                  <el-image :src="images.avatar"></el-image>
                </template>
                <template v-else>{{ el.text }}</template>
              </div>
            </template>
          </div>
        </div>
      </div>

      <div class="width-160 padding-left-16 padding-right-16" v-if="info.isEdit">
        <!--功能控制区域-->
        <div v-show="modelType === 100">
          <ImageUpload ref="ImageUpload" :is-edit="false" :auto-upload="false"
                       v-model="currentModel.value"
                       @input="onSelectModal(currentModel)"
                       accept=".png,.jpg,.jpeg"
                       :options="{
                         autoCrop: false,
                         centerBox: false,
                         canScale: false,
                         editMode: 'fixedImage',
                         canMove: false
                       }">
            <el-tooltip effect="light" content="支持png和jpg，图片大小不超过5MB，图片宽度建议不小于800">
              <el-button type="primary" icon="el-icon-upload" size="small">
                {{ backgroundUrl ? '更换证书图片' : '上传证书图片' }}
              </el-button>
            </el-tooltip>
            <div slot="tips"></div>
          </ImageUpload>
          <div class="margin-top-8 margin-bottom-4 font-12 text-6">
            上传提示：图片宽度建议不小于800
          </div>
        </div>

        <!--样式控制区域-->
        <div class="font-12">
          <div class="text-6 padding-bottom-8">
            <div>
              操作提示：可拖拽改变位置，可滚轮缩放字体
            </div>
          </div>

          <div v-for="(item,index) in elMaps" :key="index" v-show="index === activeIndex">

            <div>
              <div class="line-height-32">字体大小</div>
              <el-input-number size="small" controls-position="right" v-model="item.style.fontSize"
                               :min="12" :max="80"></el-input-number>
            </div>

            <!--            <div>
                          <div class="line-height-32">宽度</div>
                          <el-input-number size="small" controls-position="right"
                                           v-model="item.style.width" :min="0" :max="backgroundWidth - 10"></el-input-number>
                        </div>

                        <div>
                          <div class="line-height-32">高度</div>
                          <el-input-number size="small" controls-position="right"
                                           v-model="item.style.height" :min="0" :max="backgroundHeight > 200 ? backgroundHeight - 10 : 999"></el-input-number>
                        </div>-->


            <div>
              <div class="line-height-32">水平位置</div>
              <el-input-number size="small" controls-position="right"
                               v-model="item.style.x" :min="0" :max="backgroundWidth - 10"></el-input-number>
            </div>

            <div>
              <div class="line-height-32">垂直位置</div>
              <el-input-number size="small" controls-position="right"
                               v-model="item.style.y" :min="0"
                               :max="backgroundHeight > 200 ? backgroundHeight - 10 : 999"></el-input-number>
            </div>


          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {mapGetters, mapState} from 'vuex'
import ImageUpload from '@/components/form/ImageUpload'

export default {
  name: 'CertTemplateView',
  components: {
    ImageUpload
  },
  created() {
    // 初始化
    this.$once('initBackgroundUrl', value => {
      // 1. 编辑时，保存自定义模板
      if (this.modelType === 100) {
        let fItem = this.certTemplateTypeList.find(({id}) => 100 === id)
        fItem.value = value
      }
      // 2. 新增时,渲染初始模板
      this.onSelectModal(this.currentModel)
    })

    // 动态尺寸
    let resizeFn = () => {
      clearTimeout(this.timer.resize)
      this.timer.resize = setTimeout(() => {
        this.clientWidth = document.body.offsetWidth
      }, 100)
    }
    window.addEventListener('resize', resizeFn)
    this.destroyFn = () => window.removeEventListener('resize', resizeFn)
  },
  beforeDestroy() {
    this.destroyFn && this.destroyFn()
  },
  data() {
    return {
      // 当前协议
      protocol: location.protocol,
      // 组件类型映射
      typeMapping: {
        Preview: {
          isEdit: false
        },
        Edit: {
          isEdit: true
        }
      },

      // 证书模板 - 预先样式
      certTemplateTypeList: [
        {
          id: 1,
          name: '模板一',
          value: '//seczone-teach-prod-1312651338.cos.ap-guangzhou.myqcloud.com/static/%E5%8E%9F%E5%9B%BE%E8%AF%81%E4%B9%A61.png',
          style: {
            'certName': {'x': 324, 'y': 110, 'width': 0, 'height': 0, 'fontSize': 32},
            'userName': {'x': 150, 'y': 270, 'width': 0, 'height': 0, 'fontSize': 32},
            'headImg': {'x': 920, 'y': 88, 'width': 70, 'height': 98, 'fontSize': 32},
            'idNum': {'x': 360, 'y': 270, 'width': 0, 'height': 0, 'fontSize': 32},
             'searchAddress': {'x': 90, 'y': 435, 'width': 0, 'height': 0, 'fontSize': 28},
            'certContent': {'x': 365, 'y': 140, 'width': 0, 'height': 0, 'fontSize': 28},
            'certNumRule': {'x': 90, 'y': 385, 'width': 0, 'height': 0, 'fontSize': 28},
            'awardCompany': {'x': 550, 'y': 385, 'width': 0, 'height': 0, 'fontSize': 28},
            'awardDate': {'x': 550, 'y': 435, 'width': 0, 'height': 0, 'fontSize': 28},
            'backgroundUrl': null
          }
        },
        {
          id: 2,
          name: '模板二',
          value: '//seczone-teach-prod-1312651338.cos.ap-guangzhou.myqcloud.com/static/%E5%8E%9F%E5%9B%BE%E8%AF%81%E4%B9%A62.png',
          style: {
            'certName': {'x': 320, 'y': 70, 'width': 0, 'height': 0, 'fontSize': 28},
            'userName': {'x': 160, 'y': 260, 'width': 0, 'height': 0, 'fontSize': 28},
            'headImg': {'x': 170, 'y': 140, 'width': 70, 'height': 98, 'fontSize': 28},
            'idNum': {'x': 90, 'y': 330, 'width': 0, 'height': 0, 'fontSize': 28},
            'searchAddress': {'x': 90, 'y': 435, 'width': 0, 'height': 0, 'fontSize': 28},
            'certContent': {'x': 365, 'y': 140, 'width': 0, 'height': 0, 'fontSize': 28},
            'certNumRule': {'x': 90, 'y': 385, 'width': 0, 'height': 0, 'fontSize': 28},
            'awardCompany': {'x': 550, 'y': 385, 'width': 0, 'height': 0, 'fontSize': 28},
            'awardDate': {'x': 550, 'y': 435, 'width': 0, 'height': 0, 'fontSize': 28},
            'backgroundUrl': null
          }
        },
        {
          id: 3,
          name: '模板三',
          value: '//seczone-teach-prod-1312651338.cos.ap-guangzhou.myqcloud.com/static/%E5%8E%9F%E5%9B%BE%E8%AF%81%E4%B9%A63.png',
          style: {
            'certName': {'x': 235, 'y': 140, 'width': 0, 'height': 0, 'fontSize': 28},
            'userName': {'x': 160, 'y': 200, 'width': 0, 'height': 0, 'fontSize': 28},
            'headImg': {'x': 65, 'y': 195, 'width': 70, 'height': 98, 'fontSize': 28},
            'idNum': {'x': 160, 'y': 245, 'width': 0, 'height': 0, 'fontSize': 28},
            'searchAddress': {'x': 50, 'y': 615, 'width': 0, 'height': 0, 'fontSize': 28},
            'certContent': {'x': 75, 'y': 315, 'width': 0, 'height': 0, 'fontSize': 28},
            'certNumRule': {'x': 50, 'y': 570, 'width': 0, 'height': 0, 'fontSize': 28},
            'awardCompany': {'x': 320, 'y': 570, 'width': 0, 'height': 0, 'fontSize': 28},
            'awardDate': {'x': 320, 'y': 615, 'width': 0, 'height': 0, 'fontSize': 28},
            'backgroundUrl': null
          }
        },
        {
          id: 100, name: '自定义模板', value: null, style: {
            'headImg': {'x': 154, 'y': 178, 'width': 70, 'height': 98, 'fontSize': 28}
          }
        }
      ],

      // 通用样式
      commonStyle: {
        x: 0,
        y: 0,
        width: 0,
        height: 0,
        fontSize: 32
      },

      // 激活元素序号
      activeIndex: 0,
      // 元素映射（内部编辑使用）
      elMaps: {},
      clientWidth: document.body.offsetWidth,
      backgroundWidth: 1200,
      backgroundHeight: 0,

      timer: {
        emit: null,
        resize: null
      },
      loading: {
        page: false
      }
    }
  },

  props: {
    // 组件类型 Preview - 预览 Edit - 编辑
    type: {
      type: String,
      default: 'Preview'
    },

    // 视图宽度（显示大小）
    width: {
      type: [Number, String],
      default: 'auto'
    },
    // 模板宽度（生成证书实际大小）
    templateWidth: {
      type: Number,
      default: 800
    },

    // 模板元素列表 value  解析 --> 元素映射 elMaps --> $emit
    // 元素结构 {code,keyName,keyValue,extraOperation: hidden|...,customStyle:{x,y,width,height,fontSize,url}}
    value: {
      type: Array,
      default: () => []
    },

    // 模板类型
    modelType: {
      type: Number,
      default: 1
    },
    // 背景图片
    backgroundUrl: {
      type: String
    },

    // 预览ID
    id: {}
  },

  computed: {
    ...mapGetters({
      isFireFox: 'isFireFox'
    }),
    ...mapState('config', {
      images: 'images'
    }),
    // 组件信息
    info() {
      let obj = this.typeMapping[this.type] || {}
     
      // 缩放比例
      let width = this.width  //auto
      if (width === 'auto') {  
        console.log("obj",this.clientWidth)  // 1920
        width = this.clientWidth - 820
        if (width < 400) width = 400
        if (width > 800) width = 800
      }
      let zoom = width > this.backgroundWidth ? 1 : width / this.backgroundWidth
    
      return {
        ...obj,
        zoom
      }
    },
    // 当前模板类型对象
    currentModel() {
      return this.certTemplateTypeList.find(({id}) => this.modelType === id)
    }
  },

  watch: {
    value: {
      deep: true,
      handler(val) {
        this.$emit('initBackgroundUrl', this.backgroundUrl)
        // 解析: value --> elMaps
        this.parseElementList(val)
      }
    },
    elMaps: {
      deep: true,
      handler(val) {
        if (!this.info.isEdit) return false
        // $emit: elMaps --> value
        clearTimeout(this.timer.emit)
        this.timer.emit = setTimeout(() => {
          this.$emit('input', this.value.map(item => {
            let style = val[item.code].style
            if (item.code === 'backgroundUrl') {
              style = {
                ...style,
                height: this.backgroundHeight
              }
            }
            return {
              ...item,
              customStyle: JSON.stringify(style)
            }
          }))
        }, 300)
      }
    },

    // 预览证书详情
    id: {
      immediate: true,
      async handler(id) {
        if (this.info.isEdit) return false
        if (!id) return false
        this.loading.page = true
        let {code, data} = await this.$api.StandardTemplate.preview({id})
        this.loading.page = false
        if (code !== 200) return false
        this.parseElementList(data.certTemplateConfig.configList)
      }
    }
  },
  methods: {
    // 事件委托 - 获取指定元素
    getElement(el) {
      while (!/element/.test(el.className)) {
        if (/cert-wrapper/.test(el.className)) return false
        el = el.offsetParent
      }
      return el
    },

    async setBackgroundHeight(e) {
      const getBackgroundSize = () => {
        return new Promise((resolve) => {
          if (this.backgroundUrl) {
            const image = new Image()
            image.src = this.backgroundUrl
            image.onload = () => {
              resolve({width: image.width, height: image.height})
            }
          } else {
            resolve({
              width: this.$refs.backgroundUrl[0].$el.offsetWidth,
              height: this.$refs.backgroundUrl[0].$el.offsetHeight
            })
          }
        })
      }
      let {width, height} = await getBackgroundSize()
      this.backgroundWidth = width
      this.backgroundHeight = height
      console.log('宽高', `${this.backgroundWidth} * ${this.backgroundHeight}`)
    },

    // 解析元素对象
    async parseElementList(list) {
      for (const item of list) {
        if (!this.elMaps[item.code]) this.elMaps[item.code] = {}
        let el = this.elMaps[item.code]

        // code标识
        el.code = item.code
        // 文本内容
        el.text = item.keyValue || `<${item.keyName}>`
        // 内容
        el.value = item.keyValue
        // 是否展示
        el.visible = item.selected && !/hidden/.test(item.extraOperation)
        // 样式: 特殊处理 - backgroundUrl
        if (item.code === 'backgroundUrl') {
          // el.style = el.style || JSON.parse(item.customStyle) || {...this.commonBackgroundUrlStyle}
          el.style = {
            width: this.backgroundWidth,
            url: this.backgroundUrl
          }
        } else if (item.code === 'certContent') {
          el.style = el.style || JSON.parse(item.customStyle) || {...(this.currentModel.style[item.code] || this.commonStyle)}
          el.style.width = this.backgroundWidth - el.style.x - 64
        } else {
          // 样式: 当前样式 | val样式 | 预设样式 | 通用样式
          el.style = el.style || JSON.parse(item.customStyle) || {...(this.currentModel.style[item.code] || this.commonStyle)}
        }
      }

      // 更新渲染
      this.elMaps = {...this.elMaps}
    },

    // 解析(后台)样式
    parseStyle(data) {
      return {
        left: `${data.x || 0}px`,
        top: `${data.y || 0}px`,
        width: data.width ? `${data.width}px` : 'auto',
        height: data.height ? `${data.height}px` : 'auto',
        fontSize: `${data.fontSize}px`
      }
    },

    // 效果 - 拖拽位置
    onMouseDown(e) {
      if (!this.info.isEdit) return false

      let el = this.getElement(e.target)
      // 释放元素
      if (!el) return this.activeIndex = null
      // 选中元素
      this.activeIndex = el.getAttribute('code')
      let elItem = this.elMaps[this.activeIndex]

      let zoom = this.info.zoom
      let sx = e.clientX / zoom - el.offsetLeft
      let sy = e.clientY / zoom - el.offsetTop
      let maxX = el.offsetParent.clientWidth - el.offsetWidth
      let maxY = el.offsetParent.clientHeight - el.offsetHeight

      let $moveFn = ev => {
        let x = Math.round(ev.clientX / zoom - sx)
        let y = Math.round(ev.clientY / zoom - sy)

        // 边界限制
        x = x < 0 ? 0 : (x > maxX ? maxX : x)
        y = y < 0 ? 0 : (y > maxY ? maxY : y)

        elItem.style.x = x
        elItem.style.y = y
      }

      let $upFn = ev => {
        document.removeEventListener('mousemove', $moveFn)
        document.removeEventListener('mouseup', $upFn)
      }
      document.addEventListener('mousemove', $moveFn)
      document.addEventListener('mouseup', $upFn)
    },

    // 效果 - 滚轮控制字体大小
    onWheel(e) {
      if (!this.info.isEdit) return false

      // 滚轮方向
      let isDown = true
      if (this.isFireFox) {
        // 火狐
        isDown = e.detail > 0
      } else {
        isDown = e.wheelDelta < 0
      }

      // 获取元素: 当前元素 | 选中元素
      let el = this.getElement(e.target)
      let elItem = this.elMaps[el ? el.getAttribute('code') : this.activeIndex]
      if (!elItem) return false

      // 设置字体大小
      let fontSize = elItem.style.fontSize + (isDown ? -1 : 1)
      // 边界限制
      if (fontSize < 12) {
        fontSize = 12
      } else if (fontSize > 80) {
        fontSize = 80
      }
      elItem.style.fontSize = fontSize
    },

    // 操作 - 选择模板
    onSelectModal(item) {
      console.log(item);
      // if (!this.info.isEdit) return false
      this.$emit('update:modelType', item.id)
      this.$emit('update:backgroundUrl', item.value)

      this.backgroundWidth = 800
      this.backgroundHeight = 0

      if (item.id === 100) return false

      /*更换样式*/
      for (const k in this.elMaps) {
        let elMap = this.elMaps[k]
        elMap.style = {...item.style[elMap.code]}
      }
    },

    // 上传文件到COS
    async uploadFile() {
      let url = await this.$refs.ImageUpload.uploadFile()
      this.$emit('update:backgroundUrl', url)
    }
  }
}
</script>

<style scoped lang="stylus">
.image-bg
  background-image radial-gradient(#eee 0%, #fff 33%)
  background-size 10px 10px

>>> .el-input-number.is-controls-right .el-input__inner
  padding-right 32px
  padding-left 8px

.cert-template-view
  min-height 100%

  .template-wrap
    .text-center
      top 50%
      margin-top -10px

    &::before
      position absolute
      right 0
      bottom 0
      z-index 1
      border-bottom 20px solid NEUTRAL_COLOR_9
      border-left 30px solid transparent
      width 0
      height 0
      text-indent -15px
      line-height 28px
      font-size 12px
      color NEUTRAL_COLOR_F
      font-weight bold

    &.active::before
      border-bottom-color MAIN_COLOR

  .cert-wrapper
    transform-origin 0 0
    -webkit-user-select none

    &.el-loading-parent--relative
      min-height 300px

    .element
      position absolute
      z-index 20
      overflow hidden
      line-height 1.5

      &.edit
        cursor move
        overflow visible

        &:hover, &.is-active
          box-shadow 0 0 1px 1px NEUTRAL_COLOR_3
</style>