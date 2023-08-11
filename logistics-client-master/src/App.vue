<template>
  <div>
    <RouterAlive id="app"></RouterAlive>
    <FrameSelection :dragTargetList="dragTargetList" @input="updatePointer"></FrameSelection>
  </div>
</template>

<script>
import FrameSelection from '@/components/common/FrameSelection'
import {mapMutations, mapState} from 'vuex'

export default {
  name: 'App',
  components: {
    FrameSelection
  },
  async created() {
    console.log(`%c App created`, 'color: #ed5702;')
  },
  computed: {
    ...mapState('main', {
      dragTargetList: 'dragTargetList'
    })
  },
  methods: {
    ...mapMutations({
      updatePointer: 'main/updatePointer'
    })
  }
}
</script>