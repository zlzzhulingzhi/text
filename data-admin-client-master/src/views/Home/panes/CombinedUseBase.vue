<template>
  <ItemWrap :heightSmall="true" :backgroud="require('@/assets/main/base_bg.png')">
    <div slot="title">联合成立培训基地</div>

    <div class="font-16 text-f height-120 padding-bottom-10 data-cover">
      <div class="oneData"><span class="ball"></span><span>中共湖北省委党校&nbsp;(湖北省行政学院)&nbsp;教学实践基地</span></div>
      <div class="oneData"><span class="ball"></span><span>湖北省警官学院网络安全人才与创新中心</span></div>
      <div class="oneData"><span class="ball"></span><span>湖北省商用密码人才培训基地</span></div>
      <div class="oneData lastData"><span class="ball"></span><span>麒麟软件学院培训基地</span></div>
    </div>
  </ItemWrap>
</template>

<script>
import itemMix from '@/views/Home/panes/itemMix'

export default {
  name: 'CombinedUseBase',
  mixins: [itemMix],
  data() {
    return {
      pageData: [],
      size: 1,
      pollingInterval: 0
    }
  },
  computed: {
   
  }
}
</script>

<style scoped lang="stylus">
.data-cover
  position relative
  z-index 9999
.ball
  width: 12px;
  height: 12px;
  background: linear-gradient(0deg, #0734C4 0%, #46DFFF 100%);
  border-radius: 50%;
  margin-right 9px

.oneData
  margin-bottom 18px
  margin-left 45px 
  display flex 
  justify-content flex-start
  align-items: center

.lastData
  margin-bottom: 0px
</style>