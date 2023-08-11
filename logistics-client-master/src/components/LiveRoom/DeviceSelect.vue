<!--
 * @Description: 设备选择组件
 * @Date: 2022-03-10 15:47:24
 * @LastEditTime: 2022-03-29 17:02:21
-->
<template>
  <div class="select-container">
    <span class="label">{{ type }}</span>
    <el-select
        class="select"
        v-model="activeDeviceId"
        :placeholder="type"
        @change="handleChange">
      <el-option
          v-for="item in deviceList"
          :key="item.deviceId"
          :label="item.label"
          :value="item.deviceId">
      </el-option>
    </el-select>
  </div>
</template>

<script>
import TRTC from 'trtc-js-sdk'

export default {
  name: 'DeviceSelect',
  props: {
    // camera | microphone | speaker
    type: {
      type: String
    }
  },
  data() {
    return {
      deviceList: [],
      activeDevice: {},
      activeDeviceId: ''
    }
  },
  methods: {
    async getDeviceList() {
      switch (this.type) {
        case 'camera':
          this.deviceList = await TRTC.getCameras()
          break
        case 'microphone':
          this.deviceList = await TRTC.getMicrophones()
          break
        case 'speaker':
          this.deviceList = await TRTC.getSpeakers()
          break
        default:
          break
      }
      [this.activeDevice] = this.deviceList
      this.activeDeviceId = this.deviceList[0].deviceId
      this.$emit('input', this.activeDeviceId)
    },
    handleChange() {
      this.$emit('input', this.activeDeviceId)
    }
  },
  mounted() {
    switch (this.type) {
      case 'camera':
        navigator.mediaDevices.getUserMedia({video: true}).then(() => {
          this.getDeviceList()
        }).catch(() => {
          console.error('摄像头调起失败')
        })
        break
      case 'microphone':
        navigator.mediaDevices.getUserMedia({audio: true}).then(() => {
          this.getDeviceList()
        }).catch(() => {
          console.error('麦克风调起失败')
        })
        break
      case 'speaker':
        this.getDeviceList()
        break
      default:
        break
    }

    navigator.mediaDevices.addEventListener('devicechange', this.getDeviceList)
  },
  beforeDestroy() {
    navigator.mediaDevices.removeEventListener('devicechange', this.getDeviceList)
  }
}
</script>

<style lang="stylus" scoped>
.select-container {
  display: flex;

  .label {
    display: inline-block;
    padding: 0 20px;
    width: 120px;
    height: 40px;
    text-align: left;
    line-height: 40px;
    border-top: 1px solid #DCDFE6;
    border-left: 1px solid #DCDFE6;
    border-bottom: 1px solid #DCDFE6;
    border-radius: 4px 0 0 4px;
    color: #909399;
    background-color: #F5F7FA;
    font-weight: bold;
  }

  .select {
    flex-grow: 1;
  }
}
</style>
