<template>
  <u-popup :show="show" closeable @open="onOpen" @close="onClose" round="4">
    <view class="w-100 h-40 pt-8 px-20">
      <view class="w-full h-10 flex justify-center items-center mb-2" :class="isChoosed(item) ? 'choosed_item':''" 
      v-for="item, index in listMenu" :key="index" @click="onClick(item)">
        <text :class="isChoosed(item) ? 'choosed_text':''">{{ item.title }}</text>
      </view>
    </view>
  </u-popup>
</template>

<script>

export default {
  name: 'PopupSwitch',
  data() {
    return {
      show: false,
      listMenu: [
        { id: 1, title: '学员', code: 'student', isNeedLogin: false },
        { id: 2, title: '机构', code: 'org', isNeedLogin: true },
        { id: 3, title: '管理员', code: 'admin', isNeedLogin: true }
      ],
      chooseCode:this.code
    };
  },
  methods: {
    openPopSwitch(code) {
      this.chooseCode = code
      this.show=true
    },
    onOpen() {
      this.show = true
      uni.hideTabBar()
    },
    onClose() {
      this.show = false
      uni.showTabBar()
    },
    isChoosed(item) {
      return this.chooseCode === item.code
    },
    onClick(item) {
      this.chooseCode = item.code
      this.show = false
      this.$emit('click', item)
    }
  },
};
</script>

<style lang="scss" scoped>
.choosed_item {
  background-color: #FFF2F2;

  .choosed_text {
    color: #C63636
  }
}
</style>