<template>
  <el-dialog :visible.sync="visible" append-to-body title="设置" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <div class="line-height-20 font-14 text-3 flex start-center padding-left-24 margin-bottom-16">
      <div class="padding-top-16 tab-item" :class="{active: step === 'microphone'}" @click="onStep('microphone')">音频设置
      </div>
      <div class="padding-top-16 margin-left-32 tab-item" :class="{active: step === 'camera'}"
           @click="onStep('camera')">视频设置
      </div>
    </div>

    <!--摄像头-->
    <div class="text-3 font-14 text-center" v-if="step === 'camera'">
      <div>
        <span class="margin-right-10">摄像头</span>
        <el-select class="width-282" v-model="cameraId" placeholder="摄像头" size="medium" filterable>
          <el-option
              v-for="item in cameraList"
              :key="item.deviceId"
              :label="item.label"
              :value="item.deviceId">
          </el-option>
        </el-select>
      </div>

      <div class="video-container" :id="cameraEleId"></div>

      <div class="video-row">
        <el-checkbox v-model="mirror" @change="playLocalStream(cameraEleId)">翻转镜像</el-checkbox>
      </div>

      <div class="flex center-center margin-top-16">
        <div class="width-96 text-left">分辨率</div>
        <el-select class="width-240" size="medium" v-model="profileValue" placeholder="请选择分辨率">
          <el-option v-for="(item, index) in profileList" :key="index" :label="item" :value="item"></el-option>
        </el-select>
      </div>

      <div class="flex center-center margin-top-12">
        <div class="width-96 text-left">帧率（fps）</div>
        <el-select class="width-240" size="medium" v-model="frameRate" placeholder="请选择帧率">
          <el-option v-for="(item, index) in [15, 30]" :key="index" :label="item" :value="item"></el-option>
        </el-select>
      </div>

      <div class="flex center-center margin-top-12">
        <div class="width-96 text-left">码率（kbps）</div>
        <el-input-number class="width-240" size="medium" v-model="bitrate" controls-position="right"
                         :min="1" :max="9000" :step="100"></el-input-number>
      </div>

      <div class="video-row padding-left-10 padding-top-24 padding-bottom-24">
        <el-button type="primary" class="width-80 padding-0 height-36 font-13 margin-right-6" size="medium"
                   @click="onSubmit">
          保存
        </el-button>
        <el-button class="width-80 padding-0 height-36 font-13" size="medium"
                   @click="close">
          取消
        </el-button>
      </div>
    </div>

    <!--麦克风-->
    <div class="text-3 font-14 text-center" v-else-if="step === 'microphone'">
      <div class="margin-top-16 space">
        <span class="margin-right-10">麦克风</span>
        <el-select class="width-282" v-model="microphoneId" placeholder="麦克风" size="medium" filterable>
          <el-option
              v-for="item in microphoneList"
              :key="item.deviceId"
              :label="item.label"
              :value="item.deviceId">
          </el-option>
        </el-select>
      </div>

      <div class="video-row padding-left-10 padding-top-24 padding-bottom-24">
        <el-button type="primary" class="width-80 padding-0 height-36 font-13 margin-right-6" size="medium"
                   @click="onStep('camera')">
          下一步
        </el-button>
        <el-button class="width-80 padding-0 height-36 font-13" size="medium"
                   @click="close">
          取消
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import TRTC from 'trtc-js-sdk'
import rtc from './mixins/rtc'

export default {
  name: 'DialogDeviceSetting',
  mixins: [rtc],
  props: {
    sdkAppId: Number,
    userId: String,
    userSig: String
  },
  data() {
    return {
      visible: false, // 弹窗开关
      step: 'microphone',

      mirror: false,
      profileList: [
        '160*120',
        '320*180',
        '320*240',
        '640*360',
        '640*480',
        '1280*720',
        '1920*1080',
        '2560*1440',
        '3840*2160'
      ],
      profileValue: '1280*720',
      frameRate: null,
      bitrate: null,

      cameraId: null,
      cameraList: [],
      microphoneId: null,
      microphoneList: [],

      cameraEleId: 'DialogDeviceSetting-camera',

      loading: {
        submit: false
      }
    }
  },
  computed: {
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    videoProfile() {
      const [width, height] = this.profileValue.split('*')
      return {
        width: Number(width),
        height: Number(height),
        frameRate: this.frameRate,
        bitrate: this.bitrate
      }
    },
    outputData() {
      return {
        videoProfile: this.videoProfile,
        mirror: this.mirror,
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
    },
    videoProfile(val) {
      this.setVideoProfile(val)
      this.playLocalStream(this.cameraEleId)
    }
  },

  methods: {
    // 打开弹窗
    async open(data = {}) {
      this.visible = true

      this.profileValue = `${data.videoProfile.width}*${data.videoProfile.height}`
      this.frameRate = data.videoProfile.frameRate
      this.bitrate = data.videoProfile.bitrate
      this.mirror = data.mirror
      this.cameraId = data.cameraId
      this.microphoneId = data.microphoneId

      this.onStep(data.type)

      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.outputData)
          reject('取消')
        })
      })
    },


    // 获取设备列表
    async getDeviceList() {
      if (this.step === 'camera') {
        this.cameraList = await TRTC.getCameras()
        this.cameraId = this.cameraId || this.cameraList[0].deviceId
        await this.initClient()
        await this.initLocalStream()
        this.$nextTick(() => {
          this.playLocalStream(this.cameraEleId)
        })
        return
      }

      if (this.step === 'microphone') {
        this.microphoneList = await TRTC.getMicrophones()
        this.microphoneId = this.microphoneId || this.microphoneList[0].deviceId
      }
    },
    getCameraList() {
      navigator.mediaDevices.getUserMedia({video: true}).then(() => {
        this.getDeviceList()
        navigator.mediaDevices.addEventListener('devicechange', this.getDeviceList)
      }).catch(() => {
        console.error('摄像头调起失败')
      })
    },
    getMicrophoneList() {
      navigator.mediaDevices.getUserMedia({audio: true}).then(() => {
        this.getDeviceList()
        navigator.mediaDevices.addEventListener('devicechange', this.getDeviceList)
      }).catch(() => {
        console.error('麦克风调起失败')
      })
    },

    // 操作 - 下一步
    onStep(value) {
      this.step = value
      if (this.step === 'camera') {
        this.destroyLocalStream()
        this.getCameraList()
      } else if (this.step === 'microphone') {
        this.getMicrophoneList()
      }
    },

    // 关闭弹窗
    close() {
      this.destroyLocalStream()
      this.visible = false
      this.$emit('handle', 'close')
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
    height 66px
    line-height 65px
    margin 0 24px

    .el-dialog__headerbtn
      right 24px
      top 24px
      font-size 18px
      line-height 18px

  .el-dialog__body
    padding 0

    .space
      height 426px

.tab-item
  &:after
    content ''
    display block
    width 20px
    height 2px
    margin 3px auto

  &.active
    color #1D61F2
    font-weight bold

    &:after
      background-color #1D61F2

.width-282
  width 282px

.video-container
  width 336px
  height 195px
  margin 16px auto 12px
  background-color #1C1F24
  overflow hidden
  border-radius 4px

.video-row
  width 336px
  margin auto
  text-align left
</style>