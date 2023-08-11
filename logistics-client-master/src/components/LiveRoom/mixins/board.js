/*
需要
sdkAppId
userId
userSig
classId 课堂 ID
* */

const TEduBoard = window.TEduBoard

export default {
  data() {
    return {
      teduBoard: null,
      TEduBoard,

      currentFile: {},
      currentToolType: TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_PEN,
      boardScale: 100,

      setting: {
        // 允许涂鸦
        drawEnable: true,
        // 开启笔锋
        handwritingEnable: true,
        // 允许鼠标点击普通白板翻页
        whiteBoard: true,
        // 允许鼠标点击H5PPT翻页
        h5PPT: true,
        // 允许鼠标点击静态PPT翻页
        imgPPT: true,
        // 允许鼠标点击图片组文件翻页
        imgFile: true
      }
    }
  },
  methods: {
    // 阶段 - 初始化白板
    async initBoard() {
      this.addSuccessLog(`初始化白板`)
      await this.destroyBoard()
      this.teduBoard = new TEduBoard({
        id: this.eleId,

        sdkAppId: this.sdkAppId,
        userId: this.userId,
        userSig: this.userSig,
        classId: String(this.classId),

        config: {
          boardContentFitMode: TEduBoard.TEduBoardContentFitMode.TEDU_BOARD_FILE_FIT_MODE_CENTER_INSIDE
        },

        styleConfig: {
          brushThin: 50,
          selectBoxColor: '#888',
          selectAnchorColor: '#888'
        },

        authConfig: {
          mathGraphEnable: true,
          formulaEnable: true
        }
      })
    },

    // 阶段 - 监听事件
    initEvent() {
      this.addSuccessLog(`监听事件`)
      // 监听错误事件
      this.teduBoard.on(TEduBoard.EVENT.TEB_ERROR, (errorCode, errorMessage) => {
        let message = ''
        switch (errorCode) {
          case TEduBoard.TEduBoardErrorCode.TEDU_BOARD_ERROR_INIT:
            message = '初始化失败，请重试'
            break
          case TEduBoard.TEduBoardErrorCode.TEDU_BOARD_ERROR_AUTH:
            message = '服务鉴权失败，请先购买服务'
            break
          case TEduBoard.TEduBoardErrorCode.TEDU_BOARD_ERROR_LOAD:
            message = '白板加载失败，请重试'
            break
          case TEduBoard.TEduBoardErrorCode.TEDU_BOARD_ERROR_HISTORYDATA:
            message = '同步历史数据失败，请重试'
            break
          case TEduBoard.TEduBoardErrorCode.TEDU_BOARD_ERROR_RUNTIME:
            message = '白板运行错误，请检查sdkAppId，userId, userSig是否正确'
            break
          case TEduBoard.TEduBoardErrorCode.TEDU_BOARD_ERROR_AUTH_TIMEOUT:
            message = '服务鉴权超时，请重试'
            break
        }

        this.$emit('error', {message})
        this.addFailedLog(`error -${message}`)
      })

      // 监听警告事件
      this.teduBoard.on(TEduBoard.EVENT.TEB_WARNING, (warnCode, warnMessage) => {
        let message = ''
        switch (warnCode) {
          case TEduBoard.TEduBoardWarningCode.TEDU_BOARD_WARNING_SYNC_DATA_PARSE_FAILED:
            message = 'addData 接口接收到其他端的同步数据解析错误，请检查是否将非白板信令同步到了白板中'
            break
          case TEduBoard.TEduBoardWarningCode.TEDU_BOARD_WARNING_H5PPT_ALREADY_EXISTS:
            message = 'addTranscodeFile 接口添加动态PPT时，要添加的动态PPT已存在'
            break
          case TEduBoard.TEduBoardWarningCode.TEDU_BOARD_WANNING_ILLEGAL_OPERATION:
            message = '非法操作，历史数据同步未完成, 禁止改变白板行为操作'
            break
          case TEduBoard.TEduBoardWarningCode.TEDU_BOARD_WARNING_H5FILE_ALREADY_EXISTS:
            message = 'addH5File 接口添加网页文件时，要添加的网页文件已存在'
            break
          case TEduBoard.TEduBoardWarningCode.TEDU_BOARD_WARNING_VIDEO_ALREADY_EXISTS:
            message = 'addVideoFile 接口添加视频时，要添加的视频已存在'
            break
          case TEduBoard.TEduBoardWarningCode.TEDU_BOARD_WARNING_IMAGESFILE_ALREADY_EXISTS:
            message = 'addImagesFile 接口添加图片集合文件时，要添加的图片集合文件已存在'
            break
          case TEduBoard.TEduBoardWarningCode.TEDU_BOARD_WARNING_GRAFFITI_LOST:
            message = '有涂鸦丢失'
            break
          case TEduBoard.TEduBoardWarningCode.TEDU_BOARD_WARNING_CUSTOM_GRAPH_URL_NON_EXISTS:
            message = '自定义图形的URL不存在'
            break
          case TEduBoard.TEduBoardWarningCode.TEDU_BOARD_WARNING_IMAGESFILE_TOO_LARGE:
            message = 'addImagesFile 接口添加图片集合文件时，文件内容过大'
            break
          case TEduBoard.TEduBoardWarningCode.TEDU_BOARD_WARNING_IMAGE_COURSEWARE_ALREADY_EXISTS:
            message = 'addTranscodeFile 接口添加静态转码课件时，要添加的静态转码课件已存在'
            break
        }

        this.$emit('warn', {message})
        this.addFailedLog(`warn - ${message}`)
      })

      // 白板历史数据同步完成回调
      // 注意：针对白板行为的操作，一定要在该事件回调完成后再进行操作，否则无效。如翻页，设置背景色等行为
      this.teduBoard.on(TEduBoard.EVENT.TEB_HISTROYDATA_SYNCCOMPLETED, () => {
        this.setCurrentFile(this.teduBoard.getFileInfo(this.teduBoard.getCurrentFile()))
        // 设置开启笔锋
        this.teduBoard.setHandwritingEnable(true)
        // 设置开启点击擦除
        this.teduBoard.setPiecewiseErasureEnable(true)
      })

      // 白板同步数据回调
      this.teduBoard.on(TEduBoard.EVENT.TEB_SYNCDATA, data => this.$emit('sync-data', data))

      // 切换文件回调
      this.teduBoard.on(TEduBoard.EVENT.TEB_SWITCHFILE, (fid) => {
        this.setCurrentFile(this.teduBoard.getFileInfo(fid))
      })

      // 跳转白板页回调
      this.teduBoard.on(TEduBoard.EVENT.TEB_GOTOBOARD, (boardId, fid) => {
        this.setCurrentFile(this.teduBoard.getFileInfo(fid))
      })

      // 缩放白板页回调
      this.teduBoard.on('TEB_ZOOM_DRAG_STATUS', ({boardId, scale}) => {
        this.setCurrentFile(this.teduBoard.getFileInfo())
      })

      // 转码进度
      this.teduBoard.on(TEduBoard.EVENT.TEB_TRANSCODEPROGRESS, (res) => {
        if (res.code) {
          this.addFailedLog(`转码失败code:${res.code} message:${res.message}`)
        } else {
          const {status} = res
          if (status === TEduBoard.TEduBoardTranscodeFileStatus.TEDU_BOARD_TRANSCODEFILE_STATUS_ERROR) {
            this.addFailedLog('转码失败')
          } else if (status === TEduBoard.TEduBoardTranscodeFileStatus.TEDU_BOARD_TRANSCODEFILE_STATUS_UPLOADING) {
            this.addSuccessLog(`上传中，当前进度:${parseInt(res.progress)}%`)
          } else if (status === TEduBoard.TEduBoardTranscodeFileStatus.TEDU_BOARD_TRANSCODEFILE_STATUS_CREATED) {
            this.addSuccessLog('创建转码任务')
          } else if (status === TEduBoard.TEduBoardTranscodeFileStatus.TEDU_BOARD_TRANSCODEFILE_STATUS_QUEUED) {
            this.addSuccessLog('正在排队等待转码')
          } else if (status === TEduBoard.TEduBoardTranscodeFileStatus.TEDU_BOARD_TRANSCODEFILE_STATUS_PROCESSING) {
            this.addSuccessLog(`转码中，当前进度:${res.progress}%`)
          } else if (status === TEduBoard.TEduBoardTranscodeFileStatus.TEDU_BOARD_TRANSCODEFILE_STATUS_FINISHED) {
            this.addSuccessLog('转码完成')
            this.teduBoard.addTranscodeFile({
              url: res.resultUrl,
              title: res.title,
              pages: res.pages,
              resolution: res.resolution
            })
          }
        }
      })
    },

    // 阶段 - 销毁白板
    destroyBoard() {
      this.addSuccessLog(`销毁白板`)
      // 如果白板存在，则先销毁掉，避免页面多个白板对象
      if (this.teduBoard) return this.teduBoard.destroy()
    },

    // 操作 - 记录当前文件信息
    setCurrentFile(fileInfo) {
      this.addSuccessLog(`记录当前文件信息 文件ID:[${fileInfo.fid}].`)
      this.currentFile = fileInfo
      this.$emit('update:currentFile', fileInfo)
      this.boardScale = this.teduBoard.getBoardScale()
    },

    // 操作 - 设置工具类型
    setToolType(toolType, mode = 0) {
      this.currentToolType = toolType
      this.teduBoard.setToolType(toolType)
      if (toolType === TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_LINE) {
        if (mode === 1) {
          this.teduBoard.setLineStyle({
            lineType: TEduBoard.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID,
            startArrowType: TEduBoard.TEduBoardArrowType.TEDU_BOARD_ARROW_TYPE_NONE,
            endArrowType: TEduBoard.TEduBoardArrowType.TEDU_BOARD_ARROW_TYPE_SOLID
          })
        } else if (mode === 2) {
          this.teduBoard.setLineStyle({
            lineType: TEduBoard.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID,
            startArrowType: TEduBoard.TEduBoardArrowType.TEDU_BOARD_ARROW_TYPE_SOLID,
            endArrowType: TEduBoard.TEduBoardArrowType.TEDU_BOARD_ARROW_TYPE_SOLID
          })
        }
      } else if (toolType === TEduBoard.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_PEN) {
        this.teduBoard.setPenAutoFittingMode(mode)
        if (mode === 1) {
          this.teduBoard.setHandwritingEnable(false)
        } else {
          this.teduBoard.setHandwritingEnable(this.setting.handwritingEnable)
        }
      }
    },

    // 操作 - 设置自定义图形
    setCustomGraph(item) {
      this.teduBoard.addElement(TEduBoard.TEduBoardElementType.TEDU_BOARD_ELEMENT_CUSTOM_GRAPH, item.icon)
    },
    // 操作 - 整段擦除
    setPiecewiseErasureDisable() {
      this.teduBoard.setPiecewiseErasureEnable(false)
    },
    // 操作 - 分段擦除
    setPiecewiseErasureEnable() {
      this.teduBoard.setPiecewiseErasureEnable(true)
    },
    // 操作 - 清空涂鸦
    cleanUp() {
      this.teduBoard.clear(false)
    },
    // 操作 - 清空白板
    cleanAll() {
      this.teduBoard.clear(true)
    },
    // 操作 - 撤销
    undo() {
      this.teduBoard.undo()
    },
    // 操作 - 重做
    redo() {
      this.teduBoard.redo()
    },
    // 操作 - 重置比例
    resetScale() {
      this.boardScale = 100
      this.teduBoard.setBoardScale(this.boardScale)
    },
    // 操作 - 缩小
    minusScale() {
      if (this.boardScale <= 100) return false
      this.boardScale += -10
      this.teduBoard.setBoardScale(this.boardScale)
    },
    // 操作 - 放大
    plusScale() {
      if (this.boardScale >= 1000) return false
      this.boardScale += 10
      this.teduBoard.setBoardScale(this.boardScale)
    },
    // 操作 - 上一页
    prevPage() {
      this.teduBoard.prevBoard()
    },
    // 操作 - 上一步(PPT)
    prevStep() {
      this.teduBoard.prevStep()
    },
    // 操作 - 下一步(PPT)
    nextStep() {
      this.teduBoard.nextStep()
    },
    // 操作 - 下一页
    nextPage() {
      this.teduBoard.nextBoard()
    },
    // 操作 - 添加白板
    addBoard() {
      this.teduBoard.addBoard()
    },
    // 操作 - 删除白板
    deleteBoard() {
      this.teduBoard.deleteBoard()
    }
  }
}