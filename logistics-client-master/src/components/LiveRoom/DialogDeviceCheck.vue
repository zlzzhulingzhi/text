<template>
  <el-dialog :visible.sync="visible" append-to-body :before-close="close" width="420px">

    <div class="padding-left-32 padding-right-32 margin-bottom-24 flex between-center">
      <div class="btn" :class="{
        active: step === 'camera',
        success: cameraReady === 1,
        fail: cameraReady === -1
      }">
        <div class="round relative">
          <el-image :src="require('@/assets/live/icon_camera.png')"></el-image>
          <b class="el-icon-check"></b>
          <b class="el-icon-warning"></b>
        </div>
        <div v-if="step === 'camera'">检测中...</div>
        <div v-else>摄像头</div>
      </div>
      <div class="btn" :class="{
        active: step === 'microphone',
        success: microphoneReady === 1,
        fail: microphoneReady === -1
      }">
        <div class="round relative">
          <el-image :src="require('@/assets/live/icon_microphone.png')"></el-image>
          <b class="el-icon-check"></b>
          <b class="el-icon-warning"></b>
        </div>
        <div v-if="step === 'microphone'">检测中...</div>
        <div v-else>麦克风</div>
      </div>
      <div class="btn" :class="{
        active: step === 'result',
        success: cameraReady + microphoneReady === 2,
        fail: cameraReady + microphoneReady !== 2 && step === 'result'
      }">
        <div class="round relative">
          <el-image :src="require('@/assets/live/icon_setting.png')"></el-image>
          <b class="el-icon-check"></b>
          <b class="el-icon-warning"></b>
        </div>
        <div>检测结果</div>
      </div>
    </div>


    <!--摄像头-->
    <div class="text-3 font-14 text-center" v-if="step === 'camera'">
      <b>您是否能看到自己的摄像头画面？</b>
      <div class="margin-top-16">
        <span class="margin-right-10">摄像头</span>
        <el-select class="width-200" v-model="cameraId" placeholder="摄像头" size="medium" filterable>
          <el-option
              v-for="item in cameraList"
              :key="item.deviceId"
              :label="item.label"
              :value="item.deviceId">
          </el-option>
        </el-select>
      </div>

      <div class="video-container" :id="cameraEleId"></div>

      <div class="padding-bottom-32">
        <el-button class="width-80 padding-0 height-36 font-13 margin-right-14" size="medium"
                   @click="onStep(-1)">
          不可以看到
        </el-button>
        <el-button type="primary" class="width-80 padding-0 height-36 font-13" size="medium"
                   @click="onStep(1)">
          可以看到
        </el-button>
      </div>
    </div>

    <!--麦克风-->
    <div class="text-3 font-14 text-center" v-else-if="step === 'microphone'">
      <b>对着麦克风讲话，您是否能看到音量条波动？</b>
      <div class="margin-top-16">
        <span class="margin-right-10">麦克风</span>
        <el-select class="width-200" v-model="microphoneId" placeholder="麦克风" size="medium" filterable>
          <el-option
              v-for="item in microphoneList"
              :key="item.deviceId"
              :label="item.label"
              :value="item.deviceId">
          </el-option>
        </el-select>
      </div>

      <div class="height-0" :id="cameraEleId"></div>
      <div class="flex center-center volume-wrapper">
        <div v-for="n in 11" :key="n" class="line" :class="{active: volume > (n - 1) / 11 * 100}"></div>
      </div>

      <div class="padding-bottom-32">
        <el-button class="width-80 padding-0 height-36 font-13 margin-right-14" size="medium"
                   @click="onStep(-1)">
          不可以看到
        </el-button>
        <el-button type="primary" class="width-80 padding-0 height-36 font-13" size="medium"
                   @click="onStep(1)">
          可以看到
        </el-button>
      </div>
    </div>

    <!--监测结果-->
    <div class="padding-left-32 padding-right-32 padding-top-8" v-else-if="step === 'result'">
      <el-table :data="resultList" :border="false">
        <el-table-column label="检测项目" prop="name"></el-table-column>
        <el-table-column label="检测结果" prop="isReady" width="134">
          <template slot-scope="{row}">
            <div class="line-height-20" v-if="row.isReady">
              <span class="dot bg-success"></span>
              <span>正常</span>
            </div>
            <div class="flex start-center" v-else>
              <span class="dot bg-error"></span>
              <span>异常</span>
              <ToolTip placement="left">
                <span class="margin-left-12 text-main">问题排查</span>
                <span slot="icon"></span>
                <div slot="content" class="font-12 text-6">
                  <div class="tip-content" v-html="row.tipText"></div>
                </div>
              </ToolTip>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="padding-bottom-32 text-center padding-top-30">
        <el-button class="width-80 padding-0 height-36 font-13 margin-right-14" size="medium"
                   @click="onStep(-1)">
          重新检测
        </el-button>
        <el-button type="primary" class="width-80 padding-0 height-36 font-13" size="medium"
                   @click="onStep(1)">
          检测完成
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import TRTC from 'trtc-js-sdk'
import rtc from './mixins/rtc'

export default {
  name: 'DialogDeviceCheck',
  mixins: [rtc],
  props: {
    sdkAppId: Number,
    userId: String,
    userSig: String
  },
  data() {
    return {
      visible: false, // 弹窗开关

      step: 'camera',

      cameraId: null,
      cameraList: [],
      cameraReady: 0,
      microphoneId: null,
      microphoneList: [],
      microphoneReady: 0,
      volume: 0,

      cameraEleId: 'DialogDeviceCheck-camera',

      loading: {
        submit: false
      }
    }
  },
  computed: {
    resultList() {
      return [
        {
          name: '摄像头',
          isReady: this.cameraReady === 1,
          tipText: `请按以下方法排查：<br/>1.在浏览器“允许”使用摄像头<br/>2.请确认摄像头已正确连接并开启<br/>3.请确认摄像头没有被其他软件（如QQ、微信）占用<br/>4.如果是外置摄像头，请拔出重新插入尝试<br/>5.重启您的电脑`
        },
        {
          name: '麦克风',
          isReady: this.microphoneReady === 1,
          tipText: `请按以下方法排查：<br/>1.在浏览器“允许”使用麦克风<br/>2.请确认麦克风已正确连接并开启（麦克风插孔通常是粉红色））<br/>3.如果是外置麦克风，请拔出重新插入尝试<br/>4.重启您的电脑`
        }
      ]
    },
    outputData() {
      return {
        cameraId: this.cameraId,
        microphoneId: this.microphoneId
      }
    }
  },

  watch: {
    cameraId(val) {
      this.switchDevice('video', val)
    },
    microphoneId(val) {
      this.switchDevice('audio', val)
    }
  },

  methods: {
    // 打开弹窗
    async open(data = {}) {
      // this.dialogType = data.type

      this.step = 'result'
      this.onStep(-1)
      this.getMicrophoneList()

      this.visible = true
      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.outputData)
          reject('取消')
        })
      })
    },


    // 获取设备列表
    async getDeviceList(type) {
      if (type === 'camera') {
        this.cameraList = await TRTC.getCameras()
        this.cameraId = this.cameraList[0].deviceId
        await this.initClient()
        await this.initLocalStream()
        this.$nextTick(() => {
          this.playLocalStream(this.cameraEleId)
        })
        return
      }

      if (type === 'microphone') {
        this.microphoneList = await TRTC.getMicrophones()
        this.microphoneId = this.microphoneList[0].deviceId
        await this.initClient()
        await this.initLocalStream()
        this.$nextTick(() => {
          this.playLocalStream(this.cameraEleId)
          this.onGetLocalAudioLevel()
        })
        return
      }
    },
    getCameraList() {
      navigator.mediaDevices.getUserMedia({video: true}).then(() => {
        this.getDeviceList('camera')
        navigator.mediaDevices.addEventListener('devicechange', () => this.getDeviceList('camera'))
      }).catch(() => {
        console.error('摄像头调起失败')
      })
    },
    getMicrophoneList() {
      navigator.mediaDevices.getUserMedia({audio: true}).then(() => {
        this.getDeviceList('microphone')
        navigator.mediaDevices.addEventListener('devicechange', () => this.getDeviceList('microphone'))
      }).catch(() => {
        console.error('麦克风调起失败')
      })
    },
    onGetLocalAudioLevel() {
      let fn = () => {
        if (!this.localStream) return false
        this.volume = this.localStream.getAudioLevel() * 100
        requestAnimationFrame(fn)
      }
      fn()
    },

    // 操作 - 下一步
    onStep(value) {
      if (this.step === 'camera') {
        if (value === 1 && !this.cameraId) return false
        this.cameraReady = value
        this.step = 'microphone'
        this.destroyLocalStream()
        this.getMicrophoneList()

      } else if (this.step === 'microphone') {
        if (value === 1 && !this.microphoneId) return false
        this.microphoneReady = value
        this.step = 'result'
        this.destroyLocalStream()

      } else if (this.step === 'result') {
        if (value === 1) {
          // 监测完成
          if (this.cameraReady + this.microphoneReady === -2) {
            return this.$message.warning('请确保摄像头或麦克风可用')
          } else {
            this.onSubmit()
          }
        } else {
          // 重新监测
          this.cameraReady = 0
          this.microphoneReady = 0
          this.step = 'camera'
          this.destroyLocalStream()
          this.getCameraList()
        }
      }
    },

    // 关闭弹窗
    close() {
      this.destroyLocalStream()
      this.visible = false
      // this.$emit('handle', 'close')
      this.$emit('handle', 'submit')
    },
    // 确定提交
    async onSubmit() {
      this.destroyLocalStream()
      this.visible = false
      this.$emit('handle', 'submit')
    },

    addSuccessLog(log) {
      let logStyle = 'background: #28e045;color:#f8f9fa;line-height:24px'
      console.log(`%c弹窗 - ${log}`, logStyle)
    },
    addFailedLog(log) {
      let logStyle = 'background: #dc3545;color:#f8f9fa;line-height:24px'
      console.log(`%c弹窗 - ${log}`, logStyle)
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-dialog
  background-image linear-gradient(180deg, #EBF2FF 0%, rgba(235, 242, 255, 0.03) 100%)
  background-size 100% 110px
  background-repeat no-repeat

  .el-dialog__header
    padding 0
    border-bottom none
    height 48px

    .el-dialog__headerbtn
      right 24px
      top 24px
      font-size 18px
      line-height 18px

  .el-dialog__body
    padding 0

.btn
  width 72px
  text-align center
  font-size 14px

  &.success
    .el-icon-check
      display block
      background-color #13C97F
      color #FFF
      border-radius 50%
      width 16px
      height 16px
      font-size 12px
      text-align center
      line-height 16px

  &.fail
    .el-icon-warning
      display block
      color #FF4B5B
      width 16px
      height 16px
      font-size 16px

  & + .btn
    margin-left 10px

  .el-icon-check, .el-icon-warning
    display none
    position absolute
    right 0
    bottom 0

  .round
    width 48px
    height 48px
    background-image linear-gradient(130deg, #DBDBDB 0%, #CCCCCC 100%)
    border-radius 50%
    margin auto
    display flex
    justify-content center
    align-items center
    margin-bottom 16px

    .el-image
      width 24px
      height 24px

  &.active
    .round
      background-image linear-gradient(130deg, #276BFC 0%, #1D61F2 100%)

.video-container
  width 240px
  height 140px
  margin 12px auto 24px
  background-color #F2F2F2
  overflow hidden

.volume-wrapper
  margin 24px 0 28px

  .line
    background-color #CCC
    width 6px
    height 24px
    border-radius 5px

    &.active
      background-color #276BFC

    + .line
      margin-left 18px

.dot
  width 4px
  height 4px
  margin-right 6px

.tip-content
  width 250px

>>> .el-table
  min-height auto

  .el-table__header-wrapper .el-table__header thead tr th .cell
    height 40px
    line-height 40px

  .el-table__cell
    padding 0
    height 40px
    line-height 40px
</style>