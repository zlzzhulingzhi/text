<template>
  <div class="full-screen flex column center-center"></div>
</template>

<script>
import {mapState} from 'vuex'

export default {
  name: 'Login',
  created() {
    // location.replace(this.domain.uniOrg)
  },
  computed: {
    ...mapState('config', {
      domain: 'domain'
    })
  }
}
</script>