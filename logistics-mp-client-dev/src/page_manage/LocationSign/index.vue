<template>
  <view class="flex flex-col h-full bg-content">
    <u-sticky bgColor="#FFFFFF">
      <u-tabs :list="tabList" :scrollable="false" lineColor="#C63636" @click="tabClick" :current="currentTab"></u-tabs>
    </u-sticky>
    <view class="h-full">
      <LocationMap v-if="isShowMap" :myLoactionInfo="myLoactionInfo" :destiLocation="locationInfo"
        :signDistance="signDistance" @refresh="handleRefresh"></LocationMap>
      <PersonSign v-else :signText="siteInfo.name" @signTouch="signTouch"></PersonSign>
    </view>
    <u-loading-page :loading="loading.init"></u-loading-page>
    <u-tabbar :value="value" @change="name => value = name" :fixed="true" :placeholder="true"
      :safeAreaInsetBottom="true">
      <u-tabbar-item text="打卡" icon="photo"></u-tabbar-item>
      <u-tabbar-item text="记录" icon="play-right" @click="recordClickAction"></u-tabbar-item>
    </u-tabbar>
  </view>
</template>
 
<script>
import LocationMap from "./components/LocationMap.vue"
import PersonSign from "./components/PersonSign.vue"
import { mapGetters, mapState } from 'vuex'

export default {
  name: 'LocationSign',
  components: {
    LocationMap,
    PersonSign
  },
  data() {
    return {
      // 底部tab
      value: 0,
      // 头部tab
      currentTab: 0,
      // 目的地位置信息
      locationInfo: {
        latitude: null,
        longitude: null
      },
      // 当前定位信息
      myLoactionInfo: {
        latitude: null,
        longitude: null
      },
      // 打卡范围
      signDistance: 20,
      loading: {
        init: true
      },
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    ...mapState('common', {
      clazzId: 'clazzId',
      signTime: 'signTime'
    }),
    tabList() {
      return [
        { id: 0, code: 'jxl', name: '学习打卡' },
        { id: 1, code: 'ss', name: '宿舍打卡' }
      ]
    },

    distance() {
      let { latitude: mLat, longitude: mLon } = this.myLoactionInfo
      let { latitude, longitude } = this.locationInfo
      let red1 = mLat * Math.PI / 180.0;
      let red2 = latitude * Math.PI / 180.0;
      let a = red1 - red2;
      let b = mLon * Math.PI / 180.0 - longitude * Math.PI / 180.0;
      let R = 6378137;
      let distance = R * 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(red1) * Math.cos(red2) * Math.pow(Math.sin(b / 2), 2)));
      return distance.toFixed(2) * 1
    },
    // 超过打卡距离，显示地图
    isShowMap() {
      // 距离有误差
      return this.distance - this.signDistance > 10
    },
    siteInfo() {
      return this.tabList.find(a => a.id === this.currentTab)
    }
  },
  methods: {
    recordClickAction() {
      uni.$u.route({
        type: 'redirect',
        url: this.$utils.getRoutePath('SignRecord')
      })
    },
    tabClick(item) {
      if (this.currentTab === item.id) return false
      this.currentTab = item.id
      this.getColockInfo()
    },

    // 获取打卡配置表
    async getColockInfo() {
      this.loading.init = true
      const { code, data } = await this.$api.Train.clockInfoConfig({
        siteCode: this.siteInfo.code
      })
      setTimeout(() => {
        this.loading.init = false
      }, 300)
      if (code !== 200) return false
      this.locationInfo.latitude = Number(data[0].latitude)
      this.locationInfo.longitude = Number(data[0].longitude)
      this.signDistance = data[0].distance
    },

    async authorization() {
      try {
        await this.getWxLocation();
      } catch (error) {
        uni.showModal({
          title: '温馨提示',
          content: '获取权限失败，需要获取您的地理位置才能为您提供更好的服务！是否授权获取地理位置？',
          success: (res) => {
            if (res.confirm) {
              this.handleOpenSettng();
            }
          }
        });
        return;
      }
    },
    // 获取定位
    getWxLocation() {
      uni.showLoading({
        title: '定位中...'
      });
      return new Promise((resolve, reject) => {
        console.log("定位中...");
        // 开启定位追踪
        wx.startLocationUpdate({
          success: (res) => {
            // console.log("开启定位追踪", res);
            wx.onLocationChange(this.handleLocationChange);
            resolve();
          },
          fail: (err) => {
            console.log('获取当前位置失败', err);
            uni.hideLoading();
            reject();
          }
        })
      })
    },

    // 用户授权定位
    handleOpenSettng() {
      // console.log("获取用户授权");
      wx.openSetting({
        success: (res) => {
          if (res.authSetting["scope.userLocation"]) {   // 用户同意授权
            // console.log("用户同意授权");
            this.authorization();
          }
        }
      })
    },
    // 关闭定位追踪
    stopLocation() {
      wx.offLocationChange(this.handleLocationChange);
      return new Promise((resolve, reject) => {
        wx.stopLocationUpdate({
          success: (res) => {
            console.log("关闭定位追踪", res);
            resolve();
          },
          fail: (err) => {
            reject();
          },
        });
      })
    },

    // 刷新定位
    async handleRefresh() {
      this.$message.showToast(`距离打卡点${this.distance}米`)
      // await this.stopLocation();
      // this.authorization();
    },

    // 监听到定位变化, 绘制定位点
    handleLocationChange(res) {
      // console.log('定位改变, 绘制定位点:', res);
      this.myLoactionInfo.latitude = res.latitude.toFixed(4)
      this.myLoactionInfo.longitude = res.longitude.toFixed(4)
      uni.hideLoading();
    },

    // 手动打卡
    signTouch() {
      if (!this.signTime) {
        // 记录当前打卡时间
        this.$store.dispatch('common/saveSignTime', new Date())
        this.clockInAction()
      } else {
        let currentDate = new Date()
        // 计算时间差,并把毫秒转换成秒
        let difftime = (currentDate - this.signTime) / 1000
        // 分钟 -(day*24) 以60秒为一整份 取余 剩下秒数 秒数/60 就是分钟数
        let minutes = parseInt(difftime % 3600 / 60)
        // 超过2分钟才能打卡
        if (minutes >= 2) {
          this.clockInAction()
          this.$store.dispatch('common/saveSignTime', new Date())
        } else {
          this.$message.showToast('请稍后再试')
        }
      }
    },
    // 打卡
    async clockInAction() {
      let { latitude, longitude } = this.myLoactionInfo
      let signParams = { clazzId: this.clazzId, latitude, longitude, siteCode: this.siteInfo.code }
      // uni.showLoading({ title: '打卡中' })
      let { code } = await this.$api.SignRecord.memberClockInAdd(signParams)
      // uni.hideLoading()
      if (code !== 200) {
        this.$message.showToast('打卡失败，稍后重试')
      }
      this.$message.showToast('打卡成功')
    }
  },
  onLoad(option) {
    // 默认获取教学楼位置信息
    this.getColockInfo('jxl')
    // #ifndef H5
    this.authorization()
    // #endif
  },
  onUnload() {
    // #ifndef H5
    this.stopLocation()
    // #endif
  }
}
</script>
 
<style lang="scss" scoped>
.bg-content {
  background: rgb(243, 244, 246);
}
</style>