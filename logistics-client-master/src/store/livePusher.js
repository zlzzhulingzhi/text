/* 云直播直播端 */
import {LIVE_STAGE} from '@/components/TUIPusher/constants/room'

const state = {
  sdkAppId: Number(process.env.VUE_APP_PUSHER_SDK_APP_ID),
  basicLiveShowInfo: {},
  roomInfo: {
    roomId: 0,
    roomName: '我的直播间'
  },
  anchorUserInfo: {
    userId: '',
    userName: '',
    userAvatar: '',
    userSig: '',
    shareUserId: '',
    shareUserSig: ''
  },

  // 使用的摄像头设备
  activeCamera: {},
  // 使用的麦克风设备
  activeMicrophone: {},
  // 使用的扬声器设备
  activeSpeaker: {},
  // 本地流是否设置镜像
  isSetMirror: true,
  // 视频参数
  videoProfile: {
    width: 1920,
    height: 1080,
    frameRate: 15,
    bitrate: 2000
  },
  // 是否开启美颜
  isOpenBeauty: false,
  // 美颜参数
  beautyParam: {
    beautyValue: 50,
    brightnessValue: 50,
    ruddyValue: 50
  },
  // 音频采集状态
  isAudioMuted: false,
  // 视频采集状态
  isVideoMuted: false,
  // 是否开启录制
  isRecordLive: true,
  // 是否在屏幕分享中
  isScreenSharing: false,
  // 屏幕分享参数
  screenProfile: {
    width: 1920,
    height: 1080,
    frameRate: 5,
    bitrate: 1600
  },

  // 直播间阶段
  liveStage: LIVE_STAGE.NOT_STARTED,

  // 音量大小
  audioLevel: 0,
  // 上行网络质量等级
  uplinkQualityLevel: 0,

  // 观看人员列表
  memberList: [],
  // 聊天群 info
  groupInfo:{},
}

const getters = {
  sdkAppId: state => state.sdkAppId,
  roomId: state => state.roomInfo.roomId,
  roomName: state => state.roomInfo.roomName,

  anchorUserId: state => state.anchorUserInfo.userId,
  anchorShareUserId: state => state.anchorUserInfo.shareUserId,

  anchorUserSig: (state, getters) => state.anchorUserInfo.userSig,
  anchorShareUserSig: (state, getters) => state.anchorUserInfo.shareUserSig,

  activeCameraId: state => state.activeCamera.deviceId,
  activeMicrophoneId: state => state.activeMicrophone.deviceId,
  activeSpeakerId: state => state.activeSpeaker.deviceId
}

const mutations = {
  // 更新直播数据
  updateBasicLiveShowInfo: (state, data) => state.basicLiveShowInfo = data,
  // 更新直播间ID
  updateRoomId: (state, value) => state.roomInfo.roomId = value,
  // 更新直播间昵称
  updateRoomName: (state, value) => state.roomInfo.roomName = value,
  // 更新主播信息
  updateAnchorUserInfo: (state, data) => state.anchorUserInfo = data,
  // 设置sdkAppId信息
  updateSdkAppId: (state, id) => state.sdkAppId = id,
  // 更新直播间阶段
  updateLiveStage: (state, value) => state.liveStage = value,
  // 更新是否在屏幕分享中
  updateIsScreenSharing: (state, value) => state.isScreenSharing = value,
  // 更新使用的摄像头设备
  updateActiveCamera: (state, device) => state.activeCamera = device,
  // 更新使用的麦克风设备
  updateActiveMicrophone: (state, device) => state.activeMicrophone = device,
  // 更新使用的扬声器设备
  updateActiveSpeaker: (state, device) => state.activeSpeaker = device,
  // 更新是否设置本地流镜像
  updateIsSetMirror: (state, value) => state.isSetMirror = value,
  // 更新是否设置本地流镜像
  updateIsOpenBeauty: (state, value) => state.isOpenBeauty = value,
  // 更新视频参数
  updateVideoProfile: (state, data) => state.videoProfile = data,
  // 更新美颜参数
  updateBeautyParam: (state, data) => state.beautyParam = data,
  // 更新是否录制直播
  updateIsRecordLive: (state, value) => state.isRecordLive = value,
  // 更新视频mute状态
  updateIsVideoMuted: (state, value) => state.isVideoMuted = value,
  // 更新音频mute状态
  updateIsAudioMuted: (state, value) => state.isAudioMuted = value,
  // 更新音量大小l
  updateAudioLevel: (state, value) => state.audioLevel = value,
  // 更新上行网络等级
  updateUplinkQualityLevel: (state, value) => state.uplinkQualityLevel = value,
  // 更新直播间人员列表
  updateMemberList: (state, value) => state.memberList = value,
  // 更新聊天群信息
  updateGroupInfo: (state, data) => state.groupInfo = data,
}

const actions = {}

export default {
  state,
  getters,
  mutations,
  actions,
  namespaced: true
}