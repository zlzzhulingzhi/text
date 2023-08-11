<template>

</template>

<script>
import {mapActions} from 'vuex'

export default {
  name: 'Logout',
  created() {
    this.logout()
  },
  methods: {
    ...mapActions({
      logout: 'system/logout'
    })
  }
}
</script>