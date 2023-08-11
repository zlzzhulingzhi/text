<template>
  <button class="u-reset-button" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">
    <slot></slot>
  </button>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'uni-btn-phone',
  computed: {
    ...mapState('config', {
      appId: 'APPID'
    }),
    ...mapState('system', {
      authInfo: 'authInfo'
    })
  },
  methods: {
    async getPhoneNumber(e) {
      if (!e.detail.code) return false
      let { code, data } = await this.$api.Auth.wxPhone({
        appid: this.appId,
        code: e.detail.code
      })
      if (code !== 200) return false
      this.$emit('success', data)
    }
  }
}
</script>

<style lang="scss" scoped>

</style>