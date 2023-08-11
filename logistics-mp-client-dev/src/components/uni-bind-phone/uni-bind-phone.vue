<template>
  <uni-btn-phone @success="getPhone">
    <slot></slot>
  </uni-btn-phone>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'uni-bind-phone',
  computed: {
    ...mapState('system', {
      authInfo: 'authInfo'
    })
  },
  methods: {
    async getPhone(phone) {
      uni.showLoading()
      let { code, data } = await this.$api.Auth.binding({
        openId: this.authInfo.openId,
        phone: phone
      })
      uni.hideLoading()
      if (code !== 200 || !data) return false
      this.$store.commit('system/updateToken', data.access_token)
      this.$store.commit('system/updateUserInfo', { ...data, access_token: undefined })
      this.$store.commit('system/updateIdentity', 'student')
      this.$emit('success')
    }
  }
}
</script>

<style lang=
UniBtnPhone"scss" scoped>

</style>