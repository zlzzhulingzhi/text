/* 云直播播放端 */
import {LINE_TYPE, PLAY_STATE} from '@/components/TUIPlayer/constants/room'

const state = {
  sdkAppId: Number(process.env.VUE_APP_PLAYER_SDK_APP_ID),
  roomInfo: {
    roomId: 0,
    roomName: ''
  },
  userInfo: {
    userId: '',
    userName: '',
    userAvatar: '',
    userSig: ''
  },

  lineType: LINE_TYPE.LEB,
  playState: PLAY_STATE.PAUSED
}

const getters = {
  sdkAppId: state => state.sdkAppId,
  roomId: state => state.roomInfo.roomId,
  roomName: state => state.roomInfo.roomName,

  userId: state => state.userInfo.userId,
  userSig: state => state.userInfo.userSig,
}

const mutations = {
  // 更新直播间ID
  updateRoomId: (state, value) => state.roomInfo.roomId = value,
  // 更新直播间昵称
  updateRoomName: (state, value) => state.roomInfo.roomName = value,
  // 更新用户信息
  updateUserInfo: (state, data) => state.userInfo = data,
  // 设置sdkAppId信息
  updateSdkAppId: (state, id) => state.sdkAppId = id,
  // 更新播放状态
  updatePlayState: (state, playState) => state.playState = playState,
  // 更换线路
  updateLineType: (state, lineType) => state.lineType = lineType
}

const actions = {}

export default {
  state,
  getters,
  mutations,
  actions,
  namespaced: true
}