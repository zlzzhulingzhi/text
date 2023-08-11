<!--
 * @Description: 房间名称修改
 * @Date: 2021-11-08 19:36:05
 * @LastEditTime: 2021-11-08 20:02:41
-->
<template>
  <div class="room-name-container">
    <span class="room-name">{{ roomName }}</span>
    <span class="edit-name" v-if="roomName" @click="handleShowNameDialog">
      <SvgIcon icon-name="edit"></SvgIcon>
    </span>

    <el-dialog :visible.sync="showEditNameDialog" width="460px" :before-close="handleClose"
               :close-on-click-modal="false" :title="$t('Room Settings')">
      <div class="dialog-content">
        <span class="title">{{ $t('Room Name') }}</span>
        <div class="input-container">
          <el-input class="input" v-model="inputName" type="text" maxlength="12"
                    :placeholder="$t('Please input Room Name')"></el-input>
        </div>
      </div>
      <div class="dialog-footer" slot="footer">
        <el-button @click="handleClose">{{ $t('common.Cancel') }}</el-button>
        <el-button type="primary" @click="handleSure">{{ $t('common.Sure') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'ComRoomName',
  data() {
    return {
      showEditNameDialog: false,
      inputName: ''
    }
  },
  computed: {
    ...mapGetters({
      roomName: 'livePusher/roomName'
    })
  },
  watch: {
    roomName: {
      immediate: true,
      handler(val) {
        this.inputName = val
      }
    }
  },
  methods: {
    handleShowNameDialog() {
      this.showEditNameDialog = true
    },
    handleClose() {
      this.showEditNameDialog = false
    },
    handleSure() {
      if (!this.inputName) {
        this.$message.error(this.$t('Enter a room name.'))
        return
      }
      this.$store.commit('livePusher/updateRoomName', this.inputName)
      this.handleClose()
    }
  }
}
</script>

<style lang="stylus" scoped>
.room-name-container
  position absolute
  height 100%
  left 10px
  display flex
  align-items center
  color NEUTRAL_COLOR_F

  .room-name
    font-weight bold
    font-size 16px

  .edit-name
    margin-left 16px
    width 20px
    height 20px
    cursor pointer

    svg
      width 100%
      height 100%

.dialog-title
  font-weight bold
  color NEUTRAL_COLOR_F
  font-size 16px

.dialog-content
  padding 0 10px
  text-align left

  .title
    font-weight bold
    font-size 16px
    display inline-block
    margin-bottom 14px

  .input-container
    position relative
    margin-bottom 30px

    .sure-button
      height 40px
      line-height 40px
      position absolute
      right 12px
      cursor pointer
      font-size 14px

  .screen-type
    width 100%

    .radio
      margin-right 50px

.dialog-footer
  width 100%
  height 100%
  text-align center
</style>

<i18n>
{
  "en": {
    "Room Settings": "Room Settings",
    "Room Name": "Room Name",
    "Please input Room Name": "Please input Room Name",
    "Enter a room name.": "Enter a room name."
  },
  "zh": {
    "Room Settings": "直播间设置",
    "Room Name": "昵称设置",
    "Please input Room Name": "请输入直播间名字",
    "Enter a room name.": "直播间名称不能为空!"
  }
}
</i18n>
