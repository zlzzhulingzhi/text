<template>
  <ItemWrap :heightHalfFull="true" :backgroud="require('@/assets/main/wanren_plan_bg.png')">
    <div slot="title">网络安全万人培训资助计划</div>
    <ItemSubWrap>
      <div slot="title">共同发起单位</div>
      <div class="flex column height-120 text-f">
        <div class="flex-1 flex start-center" v-for="(row, index) in groupList" :key="index">
          <div class="flex-1 flex column center-center" v-for="item in row" :key="item.fieldCode">
            <el-image class="width-60" :src="require(`@/assets/main/img_spon_0.png`)"
              alt="开班课程"></el-image>
            <div class="data-title text-center" :style="{ fontSize: $utils.getScaleSize('14px') }">{{ item.displayName
            }}</div>
          </div>
          <div class="flex-1" v-if="index === groupList.length - 1"></div>
        </div>
      </div>
    </ItemSubWrap>

    <ItemSubWrap>
      <div slot="title">专项资金旨在资助</div>

      <div class="font-16 text-f height-120 padding-bottom-10">
        <div class="oneData"><span class="ball"></span><span>在国家网安基地组织开展网络安全培训的机构</span></div>
        <div class="oneData"><span class="ball"></span><span>高水平网络安全会议（论坛）和竞赛活动</span></div>
        <div class="oneData lastData"><span class="ball"></span><span>奖励网络安全优秀学员</span></div>
      </div>
    </ItemSubWrap>

  </ItemWrap>
</template>

<script>
import itemMix from '@/views/Home/panes/itemMix'

export default {
  name: 'CoSponsors',
  mixins: [itemMix],
  data() {
    /**
     * 
     * 
     * 在国家网安基地组织开展网络安全培训
高水平网络安全会议（论坛）和竞赛活动
奖励网络安全优秀学员
     */
    return {
      dataAPI: this.$api.Screen.dataOverview,
      pageData: [],
      size: 3,
      pollingInterval: 0
    }
  },
  computed: {
    groupList() {
      let size = this.size
      this.pageData = [
        { fieldCode: 1, displayName: '武汉市人民政府' },
        { fieldCode: 3, displayName: '中国互联网发展基金会' },
        { fieldCode: 2, displayName: '国家互联网应急中心' },
        { fieldCode: 4, displayName: '中国信息安全测评中心' },
        { fieldCode: 5, displayName: '中国网络安全审查技术与认证中心' },
      ]
      let max = Math.ceil(this.pageData.length / size)
      let arr = []
      for (let i = 0; i < max; i++) {
        arr.push(this.pageData.slice(size * i, size * (i + 1)))
      }
      // console.log('arr === ', arr)
      return arr
    }
  }
}
</script>

<style scoped lang="stylus">
.data-num
  font-size 30px
  font-weight bold
  margin-top 22px

.data-title
  font-size 16px
  margin-top 16px
  width 160px
  height 60px

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