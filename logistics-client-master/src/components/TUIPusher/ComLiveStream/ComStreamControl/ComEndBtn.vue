<!--
 * @Description: 结束直播按钮
 * @Date: 2021-10-27 21:44:09
 * @LastEditTime: 2022-05-07 10:29:36
-->
<template>
  <div class="btn-container">
    <el-button :type="buttonType" :disabled="stopLiveDisabled" @click="open">
      {{ $t('End') }}
    </el-button>

    <!--确认结束直播的弹窗-->
    <el-dialog :visible.sync="showStopLiveDialog" width="340px" center append-to-body :title="$t('Note')">
      <div class="content-info">
        {{ $t('End Command 1') + $t('End Command 2') + $t('End Command 3') }}
      </div>

      <div class="dialog-footer" slot="footer">
        <!-- <el-button type="primary" @click="close">{{ $t('common.Cancel') }}</el-button> -->
        <el-button type="primary" @click="close">继续直播</el-button>
        <el-button  @click="stopLive" :disabled="timoutNum">
           <!-- <span> {{ $t('common.Sure') }} </span> -->
            <span>结束直播</span>
            <span v-if="timoutNum !== 0">（{{ timoutNum }}s）</span>
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {LIVE_STAGE} from '@/components/TUIPusher/constants/room'
import {mapState} from 'vuex'

export default {
  name: 'ComEndBtn',
  data() {
    return {
      showStopLiveDialog: false,
      timoutNum: 10,
      time: null
    }
  },
  computed: {
    ...mapState('livePusher', {
      liveStage: 'liveStage'
    }),
    stopLiveDisabled() {
      return this.liveStage === LIVE_STAGE.NOT_STARTED || this.liveStage === LIVE_STAGE.ENDED
    },
    buttonType() {
      return this.liveStage === LIVE_STAGE.NOT_STARTED || this.liveStage === LIVE_STAGE.ENDED ? 'info' : 'primary'
    }
  },
  methods: {
    open() {
        this.timoutNum = 10;
        this.showStopLiveDialog = true;
        this.time = setInterval(()=> {
            this.timoutNum--;
            console.log(this.timoutNum == 0 || !this.showStopLiveDialog, 'this.timoutNum == 0 || !this.showStopLiveDialog???');
            if (this.timoutNum == 0 || !this.showStopLiveDialog) {
                clearTimeout(this.time);
            }
        }, 1000)
    },
    close() {
        clearTimeout(this.time);
        this.showStopLiveDialog = false;
    },
    stopLive() {
      this.close()
      this.$store.commit('livePusher/updateLiveStage', LIVE_STAGE.ENDED)
    }
  }
}
</script>

<style lang="stylus" scoped>
.content-info
  text-align left
</style>

<i18n>
{
  "en": {
    "Note": "Note",
    "End": "End",
    "End Command 1": "After you end the session, a recording file will be generated for replay. ",
    "End Command 2": "If you want to start a new session, you need to create a new room. ",
    "End Command 3": "Are you sure you want to end the session?"
  },
  "zh": {
    "Note": "结束直播",
    "End": "结束直播",
    "End Command 1": "直播结束后，直播状态将变为直播已结束，不可继续直播。",
    "End Command 2": "如需继续直播只能重新创建其他直播。",
    "End Command 3": "确认要结束直播吗？"
  }
}
</i18n>
