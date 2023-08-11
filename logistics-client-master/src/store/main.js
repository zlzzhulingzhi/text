const state = {
  dragTargetList: [],
  pointer: null
}


const getters = {}


const mutations = {
  updateDragTargetList: (state, d = []) => state.dragTargetList = d,
  updatePointer: (state, d = []) => state.pointer = d
}


const actions = {}


export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}