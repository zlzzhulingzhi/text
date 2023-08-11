<template>

</template>

<script>
import {mapActions, mapState} from 'vuex'
import {Token} from '@/lib/storage'

export default {
  name: 'Logout',
  created() {
    // localStorage.clear()
    // sessionStorage.clear()
    // Token.del()
    // location.replace(this.domain.uniOrg + '/#/Logout')
  },
  computed: {
    ...mapState('config', {
      domain: 'domain'
    })
  },
  methods: {
    ...mapActions({
      logout: 'system/logout'
    })
  }
}
</script>