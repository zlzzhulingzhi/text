<template>
  <div class="width-100p height-100p relative overflow" v-loading="loading.selectFile">
    <div class="board-wrapper" :id="eleId"></div>

    <!--工具栏 toolbar-->
    <div class="toolbar absolute bg-f text-6">
      <div v-for="(item,index) in toolbarList" :key="index">
        <el-popover placement="left" width="200" trigger="click" :disabled="!item.children.length">
          <div slot="reference">
            <ToolTip placement="left">
              <div slot="icon" class="font-20 line-height-20 mdi pointer padding-6"
                   :class="item.icon"
                   @click="onSetToolType(item)"></div>
              <div slot="content">{{ item.name }}</div>
            </ToolTip>
          </div>

          <!--弹出内容-->
          <div class="flex start-center wrap">
            <div v-for="(cItem,cIndex) in item.children" :key="cIndex" @click="onSetToolType(cItem)"
                 class="pointer padding-10 flex start-center">
              <div v-if="/^mdi-/.test(cItem.icon)" class="font-18 mdi" :class="cItem.icon"></div>
              <el-image v-else class="width-40 height-40" :src="cItem.icon"></el-image>
              <div v-if="cItem.name" class="margin-left-10">{{ cItem.name }}</div>
            </div>
          </div>
        </el-popover>
      </div>
    </div>

    <!--底部工具栏 bottom-toolbar-->
    <div class="bottom-toolbar absolute bg-f text-6 flex start-center">
      <div v-for="(item,index) in bottomToolbarList" :key="index">
        <template v-if="item.toolType === 'divider'">
          <el-divider direction="vertical"></el-divider>
        </template>
        <template v-else-if="item.toolType === 'boardScale'">
          <div class="shrink-0 text-ellipsis">{{ boardScale }}%</div>
        </template>
        <template v-else-if="item.toolType === 'page'">
          <div class="shrink-0 text-ellipsis">{{ currentFile.currentPageIndex + 1 }} / {{ currentFile.pageCount }}</div>
        </template>
        <template v-else-if="item.toolType === 'addBoard'">
          <ToolTip placement="top" v-if="currentFile && currentFile.fid === '#DEFAULT'">
            <div slot="icon" class="font-20 line-height-20 mdi pointer padding-6"
                 :class="item.icon"
                 @click="onSetToolType(item)"></div>
            <div slot="content">{{ item.name }}</div>
          </ToolTip>
        </template>
        <template v-else>
          <ToolTip placement="top">
            <div slot="icon" class="font-20 line-height-20 mdi pointer padding-6"
                 :class="item.icon"
                 @click="onSetToolType(item)"></div>
            <div slot="content">{{ item.name }}</div>
          </ToolTip>
        </template>
      </div>
    </div>

    <DialogPaletteSetting ref="DialogPaletteSetting"></DialogPaletteSetting>
    <DialogPanelSetting ref="DialogPanelSetting"></DialogPanelSetting>
    <DialogSelectFile ref="DialogSelectFile" @select="loading.selectFile = true"></DialogSelectFile>
  </div>
</template>

<script>
import board from '../mixins/board'
import DialogPaletteSetting from './DialogPaletteSetting'
import DialogPanelSetting from './DialogPanelSetting'
import DialogSelectFile from './DialogSelectFile'

const TEduBoard = window.TEduBoard

export default {
  name: 'Board',
  mixins: [board],
  components: {
    DialogPaletteSetting,
    DialogPanelSetting,
    DialogSelectFile
  },
  props: {
    sdkAppId: {
      type: Number,
      required: true
    },
    userId: {
      type: [Number, String]
    },
    userSig: {
      type: String
    },
    classId: {
      type: [Number, String]
    }
  },
  data() {
    return {
      eleId: 'board-id',
      toolbarList: [
        {
          icon: 'mdi-cursor-default-outline',
          name: '鼠标',
          toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_MOUSE,
          children: []
        },
        {
          icon: 'mdi-draw',
          name: '涂鸦工具',
          toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_PEN,
          mode: TEduBoard.TEduBoardPenFittingMode.NONE,
          children: [
            {
              icon: 'mdi-pencil-outline',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_PEN,
              mode: TEduBoard.TEduBoardPenFittingMode.NONE
            },
            {
              icon: 'mdi-vector-line',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_LINE,
              mode: 0
            },
            {
              icon: 'mdi-arrow-top-right',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_LINE,
              mode: 1
            },
            {
              icon: 'mdi-arrow-top-right-bottom-left',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_LINE,
              mode: 2
            },
            {
              icon: 'mdi-rectangle-outline',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_RECT
            },
            {
              icon: 'mdi-square-outline',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_SQUARE
            },
            {
              icon: 'mdi-ellipse-outline',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_OVAL
            },
            {
              icon: 'mdi-circle-outline',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_CIRCLE
            },
            {
              icon: 'mdi-rectangle',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_RECT_SOLID
            },
            {
              icon: 'mdi-square',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_SQUARE_SOLID
            },
            {
              icon: 'mdi-ellipse',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_OVAL_SOLID
            },
            {
              icon: 'mdi-circle',
              toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_CIRCLE_SOLID
            }
          ]
        },
        {
          icon: 'mdi-auto-fix',
          name: '魔法笔',
          toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_PEN,
          mode: TEduBoard.TEduBoardPenFittingMode.AUTO,
          children: []
        },
        {
          icon: 'mdi-selection-drag',
          name: '选框',
          toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_RECT_SELECT,
          children: []
        },
        {
          icon: 'mdi-eraser',
          name: '橡皮擦',
          toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ERASER,
          children: [
            {
              icon: 'mdi-eraser',
              name: '整段擦除',
              toolType: 'setPiecewiseErasureDisable'
            },
            {
              icon: 'mdi-eraser-variant',
              name: '分段擦除',
              toolType: 'setPiecewiseErasureEnable'
            }
          ]
        },
        {
          icon: 'mdi-format-text',
          name: '文本',
          toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_TEXT,
          children: []
        },
        {
          icon: 'mdi-puzzle-outline',
          name: '自定义图形',
          toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_BOARD_CUSTOM_GRAPH,
          children: [
            {
              icon: 'https://test-1259648581.cos.ap-shanghai.myqcloud.com/%E4%B8%89%E8%A7%92%E5%BD%A2.svg',
              name: '自定义图形1',
              toolType: 'setCustomGraph'
            },
            {
              icon: 'https://test-1259648581.cos.ap-shanghai.myqcloud.com/%E6%8A%9B%E7%89%A9%E7%BA%BF_parabolic9.svg',
              name: '自定义图形2',
              toolType: 'setCustomGraph'
            },
            {
              icon: 'https://test-1259648581.cos.ap-shanghai.myqcloud.com/%E8%8F%B1%E5%BD%A2.svg',
              name: '自定义图形3',
              toolType: 'setCustomGraph'
            }
          ]
        },
        {
          icon: 'mdi-flare',
          name: '激光笔',
          toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_LASER,
          children: []
        },
        {
          icon: 'mdi-cursor-move',
          name: '缩放移动',
          toolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ZOOM_DRAG,
          children: []
        },
        {
          icon: 'mdi-palette',
          name: '工具样式',
          toolType: 'DialogPaletteSetting',
          children: []
        },
        {
          icon: 'mdi-broom',
          name: '一键清空',
          toolType: null,
          children: [
            {
              icon: 'mdi-format-clear',
              name: '清空涂鸦',
              toolType: 'cleanUp'
            },
            {
              icon: 'mdi-notification-clear-all',
              name: '清空白板',
              toolType: 'cleanAll'
            }
          ]
        }
        /*{
          icon: 'mdi-cog-outline',
          name: '其他设置',
          toolType: 'DialogPanelSetting',
          children: []
        }*/
      ],
      bottomToolbarList: [
        {
          icon: 'mdi-undo-variant',
          name: '撤销',
          toolType: 'undo',
          children: []
        },
        {
          icon: 'mdi-redo-variant',
          name: '重做',
          toolType: 'redo',
          children: []
        },
        {
          toolType: 'divider'
        },
        {
          icon: 'mdi-target',
          name: '重置比例',
          toolType: 'resetScale',
          children: []
        },
        {
          icon: 'mdi-minus',
          name: '缩小',
          toolType: 'minusScale',
          children: []
        },
        {
          toolType: 'boardScale'
        },
        {
          icon: 'mdi-plus',
          name: '放大',
          toolType: 'plusScale',
          children: []
        },
        {
          toolType: 'divider'
        },
        {
          icon: 'mdi-page-first',
          name: '上一页',
          toolType: 'prevPage',
          children: []
        },
        {
          icon: 'mdi-chevron-left',
          name: '上一步(PPT)',
          toolType: 'prevStep',
          children: []
        },
        {
          toolType: 'page'
        },
        {
          icon: 'mdi-chevron-right',
          name: '下一步(PPT)',
          toolType: 'nextStep',
          children: []
        },
        {
          icon: 'mdi-page-last',
          name: '下一页',
          toolType: 'nextPage',
          children: []
        },
        {
          icon: 'mdi-text-box-plus-outline',
          name: '添加白板',
          toolType: 'addBoard',
          children: []
        }
        /*{
          icon: 'mdi-text-box-minus-outline',
          name: '删除白板',
          toolType: 'deleteBoard',
          children: []
        },
        {
          icon: 'mdi-file-multiple-outline',
          name: '文件列表',
          toolType: 'DialogSelectFile',
          children: []
        }*/
      ],

      loading: {
        selectFile: false
      }
    }
  },
  watch: {
    currentFile() {
      this.loading.selectFile = false
    }
  },
  methods: {
    // 操作 - 初始化白板
    async onInitBoard() {
      await this.initBoard()
      await this.initEvent()
    },
    // 操作 - 销毁白板
    async onDestroyBoard() {
      await this.destroyBoard()
    },

    onSetToolType(item) {
      switch (typeof item.toolType) {
        case 'string':
          this[item.toolType] && this[item.toolType](item)
          break
        case 'number':
          this.setToolType(item.toolType, item.mode)
          break
      }
    },
    // 操作 - 获取文件列表
    getFileInfoList() {
      let fileList = this.teduBoard.getFileInfoList()
      this.$emit('update:fileList', fileList)
    },

    // 操作 - 设置工具样式
    async DialogPaletteSetting() {
      let teduBoard = this.teduBoard
      let isTEXT = this.currentToolType === TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_TEXT
      let formData = await this.$refs.DialogPaletteSetting.open({
        formData: {
          thin: teduBoard.getBrushThin(),
          fontSize: teduBoard.getTextSize(),
          color: isTEXT ? teduBoard.getTextColor() : teduBoard.getBrushColor(),
          backgroundColor: teduBoard.getBackgroundColor()
        }
      })

      teduBoard.setBrushThin(formData.thin)
      teduBoard.setTextSize(formData.fontSize)
      if (isTEXT) {
        teduBoard.setTextColor(formData.color)
      } else {
        teduBoard.setBrushColor(formData.color)
      }
      teduBoard.setBackgroundColor(formData.backgroundColor)
    },
    // 操作 - 其他设置
    DialogPanelSetting() {
      this.$refs.DialogPanelSetting.open()
    },
    // 操作 - 文件列表
    async DialogSelectFile() {
      await this.$refs.DialogSelectFile.open({
        teduBoard: this.teduBoard
      })
    },

    // 显示成功的 Log
    addSuccessLog(log) {
      let logStyle = 'background: #28e045;color:#f8f9fa;line-height:24px'
      console.log(`%c白板 - ${log}`, logStyle)
    },

    // 显示失败的 Log
    addFailedLog(log) {
      let logStyle = 'background: #dc3545;color:#f8f9fa;line-height:24px'
      console.log(`%c白板 - ${log}`, logStyle)
    }
  }
}
</script>

<style scoped lang="stylus">
.board-wrapper
  width calc(100vw - 304px)
  height 100%
  background-color #959A9F

.toolbar
  right 0
  top 68px
  padding 8px 4px

.bottom-toolbar
  bottom 0
  left 50%
  transform translateX(-50%)
  padding 4px 8px

</style>