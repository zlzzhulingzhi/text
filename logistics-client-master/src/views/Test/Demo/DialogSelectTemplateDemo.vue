<template>
  <div>
    <el-button @click="onSelectCampus">选择校区</el-button>
    <DialogSelectTemplate ref="DialogSelectTemplate"></DialogSelectTemplate>
  </div>
</template>

<script>
import DialogSelectTemplate from '@/components/dialog/DialogSelectTemplate'

export default {
  name: 'DialogSelectTemplateDemo',
  components: {
    DialogSelectTemplate
  },
  methods: {
    async onSelectCampus() {
      let data = await this.$refs.DialogSelectTemplate.open({
        type: 'Campus'
      })
      console.log(data)
    }
  }
}
</script>

<style scoped>

</style>