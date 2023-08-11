<template>
  <ItemWrap :heightFull="true" :backgroud="require('@/assets/main/bg_side_bottom.png')">
    <div slot="title">万人计划入围机构</div>

    <div class="flex wrap content">
      <div class="box" v-for="item in pageData" :key="item.id">
        <div class="flex center-center font-14 text-f item">{{ item.name }}</div>
      </div>
    </div>
  </ItemWrap>
</template>

<script>
import itemMix from '@/views/Home/panes/itemMix'

export default {
  name: 'WinnersShow',
  mixins: [itemMix],
  data() {
    return {
      dataAPI: this.$api.Mock.winnersList,
      pageData: []
    }
  }
}
</script>

<style scoped lang="stylus">
.content
  margin 0 28px

  .box
    width 33%

    .item
      height 40px     
      margin 0 5px 10px
      background rgba(11, 65, 209, 0.1)
      box-shadow 0 0 4px 2px #0B41D1 inset
</style>