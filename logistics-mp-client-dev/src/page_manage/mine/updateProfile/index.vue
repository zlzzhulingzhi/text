<template>
  <view class="p-8">
    <template v-if="formData.hasOwnProperty('realName')">
      <view class="p-4 mb-4 text-lg text-gray-500">请填写姓名</view>
      <u-input v-model="formData.realName" :maxlength="10" placeholder="限定填写2-10个字" border="bottom" prefixIcon="edit-pen"></u-input>
    </template>
    <template v-else-if="formData.hasOwnProperty('idNumber')">
      <view class="p-4 mb-4 text-lg text-gray-500">请填写身份证号</view>
      <u-input v-model="formData.idNumber" type="idcard" :maxlength="18" placeholder="" border="bottom" prefixIcon="edit-pen"></u-input>
    </template>
    <template v-else-if="formData.hasOwnProperty('school')">
      <view class="p-4 mb-4 text-lg text-gray-500">请填写毕业院校</view>
      <u-input v-model="formData.school" :maxlength="30" placeholder="" border="bottom" prefixIcon="edit-pen"></u-input>
    </template>
    <template v-else-if="formData.hasOwnProperty('workUnit')">
      <view class="p-4 mb-4 text-lg text-gray-500">请填写工作单位</view>
      <u-input v-model="formData.workUnit" :maxlength="30" placeholder="" border="bottom" prefixIcon="edit-pen"></u-input>
    </template>
    <template v-else-if="formData.hasOwnProperty('workAddress')">
      <view class="p-4 mb-4 text-lg text-gray-500">请填写工作地址</view>
      <u-input v-model="formData.workAddress" :maxlength="50" placeholder="" border="bottom" prefixIcon="edit-pen"></u-input>
    </template>
    <template v-else-if="formData.hasOwnProperty('registerAddress')">
      <view class="p-4 mb-4 text-lg text-gray-500">请填写户籍地址</view>
      <u-input v-model="formData.registerAddress" :maxlength="50" placeholder="" border="bottom" prefixIcon="edit-pen"></u-input>
    </template>
    <template v-else-if="formData.hasOwnProperty('resideAddress')">
      <view class="p-4 mb-4 text-lg text-gray-500">请填写居住地</view>
      <u-input v-model="formData.resideAddress" :maxlength="50" placeholder="" border="bottom" prefixIcon="edit-pen"></u-input>
    </template>
    <template v-else-if="formData.hasOwnProperty('email')">
      <view class="p-4 mb-4 text-lg text-gray-500">请填写电子邮箱</view>
      <u-input v-model="formData.email" :maxlength="50" placeholder="" border="bottom" prefixIcon="edit-pen"></u-input>
    </template>
    <view class="h-12"></view>
    <u-button type="success" shape="circle" text="保存" :loading="loading.submit" @click="updateProfile"></u-button>
  </view>
</template>

<script>
export default {
  name: 'UpdateProfile',
  data() {
    return {
      loading: {
        submit: false
      },
      formData: {}
    }
  },
  async onLoad(options) {
    let params = await this.$utils.getOptions(options)
    this.formData = {
      [params.k]: params.v || ''
    }
  },
  methods: {
    async updateProfile() {
      if (!Object.values(this.formData).some(v => v)) return false
      // if (this.form.nickName.length < 2) return this.$message.showToast('限定填写2-10个字')

      this.loading.submit = true
      let { code } = await this.$api.Member.update(this.formData)
      this.loading.submit = false
      if (code !== 200) return false

      uni.$emit(this.$store.state.config.EVENT.PROFILE, this.formData)
      uni.$u.route({
        type: 'navigateBack'
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>