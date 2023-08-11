<!--职工名片-->
<template>
  <u-sticky bgColor="#fff" v-if="item.showStatus">
    <CardContact :item="item" @onWechat="onWechat(item)" @onCall="onCall(item)"></CardContact>
    <ModalContact ref="ModalContact"></ModalContact>
  </u-sticky>
</template>

<script>
import CardContact from './CardContact.vue'
import ModalContact from './ModalContact.vue'

export default {
  name: 'EmployeeCard',
  components: {
    CardContact,
    ModalContact
  },
  props: {
    item: {
      type: Object,
      default: () => {}
    }
  },
  methods: {
    onCall(item) {
      uni.makePhoneCall({
        phoneNumber: item.phone
      })
    },
    onWechat(item) {
      this.$refs.ModalContact.open(item)
    }
  }
}
</script>

<style lang="scss" scoped>

</style>