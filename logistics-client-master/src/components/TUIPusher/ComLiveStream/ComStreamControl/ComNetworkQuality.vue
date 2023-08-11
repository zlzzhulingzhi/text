<!--
 * @Description: 网络质量
 * @Date: 2021-10-27 21:57:25
 * @LastEditTime: 2021-11-09 15:44:06
-->
<template>
  <div class="network-quality" v-if="liveStage === LIVE_STAGE.ONGOING">
    <span>{{ $t('Network Quality') }}</span>
    <div class="network-level-container">
      <div class="level-item" v-for="(item, index) in Array(5)" :class="index < uplinkQualityLevel ? 'green' : ''"
           :style="{ height: 3 * (index + 1) + 'px' }"></div>
    </div>
  </div>
</template>

<script>
import {LIVE_STAGE} from '@/components/TUIPusher/constants/room'
import {mapState} from 'vuex'

export default {
  name: 'ComNetworkQuality',
  data() {
    return {
      LIVE_STAGE
    }
  },
  computed: {
    ...mapState('livePusher', {
      liveStage: 'liveStage',
      uplinkQualityLevel: 'uplinkQualityLevel'
    })
  }
}
</script>

<style lang="stylus" scoped>
.network-quality
  margin-right 14px
  height 100%
  display flex
  color NEUTRAL_COLOR_F

  .network-level-container
    display flex
    width 20px
    height 18px
    justify-content space-around
    align-items flex-end
    margin-left 6px

    .level-item
      width 3px
      background-color NEUTRAL_COLOR_F

      &.green
        background-color SUCCESS_COLOR
</style>

<i18n>
{
  "en": {
    "Network Quality": "Network Quality"
  },
  "zh": {
    "Network Quality": "网络质量"
  }
}
</i18n>
