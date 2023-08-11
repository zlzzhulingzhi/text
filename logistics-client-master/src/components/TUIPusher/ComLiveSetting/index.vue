<template>
  <el-card>
    <div slot="header">{{ $t('Streaming Settings') }}</div>

    <div class="text-center flex wrap">
      <div class="pointer bg-background width-72 padding-8 radius-8 margin-4" @click="handleAudioSetting">
        <SvgIcon class="font-32 text-main" icon-name="mic"></SvgIcon>
        <div class="font-14">{{ $t('Audio') }}</div>
      </div>

      <div class="pointer bg-background width-72 padding-8 radius-8 margin-4" @click="handleVideoSetting">
        <SvgIcon class="font-32 text-main" icon-name="camera"></SvgIcon>
        <div class="font-14">{{ $t('Video') }}</div>
      </div>

<!--      <div class="pointer bg-background width-72 padding-8 radius-8 margin-4" @click="handleBeautySetting">
        <SvgIcon class="font-32 text-main" icon-name="beauty"></SvgIcon>
        <div class="font-14">{{ $t('Beauty Filter') }}</div>
      </div>-->


      <div class="pointer bg-background width-72 padding-8 radius-8 margin-4" @click="handleExit">
        <SvgIcon class="font-32 text-main" icon-name="exit"></SvgIcon>
        <div class="font-14">{{ $t('Exit') }}</div>
      </div>
    </div>

    <el-dialog :title="$t('Streaming Settings')" :visible.sync="dialogVisible" width="600px"
               :before-close="handleDialogClose" class="live-setting-dialog">
      <el-tabs class="tabs-style" tab-position="left" style="height: 420px;" v-model="activeTab"
               @tab-click="handleTabClick">
        <!--音频设置-->
        <el-tab-pane :label="$t('Audio')" name="audio">
          <AudioSetting :activeTab="activeTab"></AudioSetting>
        </el-tab-pane>
        <!--视频设置-->
        <el-tab-pane :label="$t('Video')" name="video">
          <VideoSetting :activeTab="activeTab" :dialogVisible="dialogVisible"></VideoSetting>
        </el-tab-pane>
        <!--美颜设置-->
<!--        <el-tab-pane :label="$t('Beauty Filter')" name="beauty">
          <BeautySetting :activeTab="activeTab" :dialogVisible="dialogVisible"></BeautySetting>
        </el-tab-pane>-->
      </el-tabs>
    </el-dialog>
  </el-card>
</template>

<script>
import AudioSetting from './AudioSetting'
import VideoSetting from './VideoSetting'
import BeautySetting from './BeautySetting'

export default {
  name: 'ComLiveSetting',
  data() {
    return {
      dialogVisible: false,
      activeTab: '',
      cameraList: [],
      choseCameraId: '',
      microphoneList: [],
      choseMicrophoneId: '',
      speakerList: [],
      choseSpeakerId: '',
      localStream: null,
      isOpenBeauty: true
    }
  },
  components: {
    AudioSetting,
    VideoSetting,
    BeautySetting
  },
  computed: {},
  methods: {
    handleAudioSetting() {
      this.activeTab = 'audio'
      this.dialogVisible = true
    },
    handleVideoSetting() {
      this.dialogVisible = true
      this.activeTab = 'video'
    },
    handleBeautySetting() {
      this.activeTab = 'beauty'
      this.dialogVisible = true
    },
    handleTabClick(tab) {
      this.activeTab = tab.name
    },
    handleDialogClose() {
      this.activeTab = ''
      this.dialogVisible = false
    },
    handleExit() {
      this.$router.back()
    }
  }
}
</script>

<style lang="stylus" scoped>
.live-setting-dialog
  text-align left

>>> .tabs-style
  .el-tabs__item
    color NEUTRAL_COLOR_3

  .el-tabs__nav-wrap::after
    background-color NEUTRAL_COLOR_9

  .el-tabs__active-bar.is-left
    padding-left 6px !important
    padding-right 6px !important
</style>

<i18n>
{
  "en": {
    "Streaming Settings": "Streaming Settings",
    "Audio": "Audio",
    "Video": "Video",
    "Beauty Filter": "Beauty Filter",
    "Others": "Others",
    "Exit": "Exit"
  },
  "zh": {
    "Streaming Settings": "快速开播",
    "Audio": "音频设置",
    "Video": "视频设置",
    "Beauty Filter": "美颜设置",
    "Others": "其他设置",
    "Exit": "离开"
  }
}
</i18n>
