<template>
  <view :catchtouchmove="!isCanTouch">
    <u-navbar bgColor="transparent">
      <view slot="left"></view>
    </u-navbar>
    <view class="relative">
      <u--image class="rounded-b-3xl overflow-hidden" :src="`${cosPath}/bg_top.png`" width="100%" mode="widthFix">
        <view slot="error" class="bg-red-800 w-full h-full"></view>
      </u--image>
      <view class="absolute p-4 left-0 right-0 bottom-5 flex">
        <view class="flex justify-center items-center relative avatar" @click="goToPage(menuList[0])" v-if="isLogin">
          <u-avatar :src="userInfo.headImgUrl || '/static/img_avatar.png'" size="78"></u-avatar>
          <view class="absolute right-0 bottom-0 flex justify-center items-center edit">
            <u-icon name="edit-pen" color="#666"></u-icon>
          </view>
        </view>
        <uni-bind-phone v-else>
          <view class="flex justify-center items-center relative avatar">
            <u-avatar src="/static/img_avatar.png" size="78"></u-avatar>
            <view class="absolute right-0 bottom-0 flex justify-center items-center edit">
              <u-icon name="edit-pen" color="#666"></u-icon>
            </view>
          </view>
        </uni-bind-phone>
        <view class="m-3 text-white" @click="goToPage(menuList[0])" v-if="isLogin">
          <view class="text-xl mb-2">{{ userInfo.userName || '&lt;未填写&gt;' }}</view>
          <view class="text-sm">{{ userInfo.userAccount }}</view>
        </view>
        <uni-bind-phone v-else>
          <view class="m-3 text-white text-left">
            <view class="text-xl mb-2">登录/注册</view>
            <view class="text-sm">绑定手机</view>
          </view>
        </uni-bind-phone>
      </view>
    </view>
    <template v-if="isLogin">
      <view class="flex items-center py-4 px-2 mx-4 mt-4 rounded-md menu-item" v-for="item in menuList" :key="item.id"
        @click="goToPage(item)">
        <u--image :src="item.icon" width="22" height="22"></u--image>
        <text class="flex-1 ml-3">{{ item.title }}</text>
        <u-icon name="arrow-right"></u-icon>
      </view>
    </template>
    <uni-bind-phone v-else>
      <view class="flex items-center py-4 px-2 mx-4 mt-4 rounded-md menu-item" v-for="item in menuList" :key="item.id">
        <u--image :src="item.icon" width="22" height="22"></u--image>
        <view class="flex-1 flex justify-start ml-3">{{ item.title }}</view>
        <u-icon name="arrow-right"></u-icon>
      </view>
    </uni-bind-phone>
    <PopupSwitch ref="popupSwitch" :code="identity" @click="switchAction"></PopupSwitch>
    <u-picker :show="show.clazzList" :columns="[selectClazzList]" keyName="clazzName" @cancel="cancelClazzList"
      @confirm="confirmClazzList"></u-picker>
  </view>
</template>

<script>
import { mapState, mapGetters } from 'vuex'
import uniBindPhone from '../../components/uni-bind-phone/uni-bind-phone.vue'
import PopupSwitch from '@/components/popup-switch/popup-switch.vue'
export default {
  components: {
    uniBindPhone,
    PopupSwitch
  },
  name: 'tabMine',
  data() {
    return {
      isCanTouch: true,
      show: {
        clazzList: false
      }
    }
  },
  computed: {
    ...mapState('config', {
      cosPath: 'COS_PATH'
    }),
    ...mapState('system', {
      userInfo: 'userInfo',
      identity: 'identity'
    }),
    ...mapGetters({
      isLogin: 'system/isLogin',
      isStudent: 'system/isStudent',
      isOrg: 'system/isOrg',
      isAdmin: 'system/isAdmin',
      selectClazzList: 'common/selectClazzList'
    }),
    menuList() {
      if (this.isOrg) {
        return [
          { id: 1, title: '账号信息', icon: '/static/menu_review.png', route: 'MyAccount' },
          { id: 2, title: '报事报修', icon: '/static/menu_course.png', route: 'WorkOrderList' },
          { id: 3, title: '切换身份', icon: '/static/menu_active.png', route: 'Switch' }
        ]
      }
      if (this.isAdmin) {
        return [
          { id: 1, title: '账号信息', icon: '/static/menu_review.png', route: 'MyAccount' },
          { id: 2, title: '切换身份', icon: '/static/menu_active.png', route: 'Switch' }
        ]
      }
      return [
        { id: 1, title: '账号信息', icon: '/static/menu_review.png', route: 'MyProfile' },
        { id: 2, title: '我的课程', icon: '/static/menu_course.png', route: 'MyCourse' },
        { id: 3, title: '切换身份', icon: '/static/menu_active.png', route: 'Switch' },
        { id: 4, title: '签到打卡', icon: '/static/menu_notice.png', route: 'LocationSign' },
        { id: 5, title: '访客登记', icon: '/static/menu_active.png', route: 'MyVisit' }
      ]
    }
  },
  methods: {
    async goToPage(item) {
      if (!item.route) return false
      if (item.route === 'Switch') {
        this.isCanTouch = false
        this.$refs.popupSwitch.openPopSwitch(this.identity)
        return false
      }
      if (item.route === 'LocationSign') {
        let { code, data } = await this.$api.SignRecord.selectClazzList()
        if (code === 200) {
          this.$store.dispatch('common/updateClazzList', data)
        }
        if (this.selectClazzList.length <= 0) {
          this.$message.showToast('你还未加入班级，请加入后再试！')
          return false
        }
        this.show.clazzList = true
        return false
      }
      uni.$u.route(this.$utils.getRoutePath(!this.isLogin && this.$utils.isNeedLogin(item.route) ? 'Login' : item.route))
    },
    async switchAction(item) {
      uni.showTabBar()
      this.isCanTouch = true
      if (item.isNeedLogin || !this.isLogin) {
        uni.$u.route(this.$utils.getRoutePath('Login'), {
          id: item.code
        })
        return
      }
      await this.$store.dispatch('appLogin', {}, { root: true })
      uni.$u.route({
        url: this.$utils.getRoutePath('NewHome'),
        type: 'tab'
      })
    },
    cancelClazzList() {
      this.show.clazzList = false
    },
    confirmClazzList({ value }) {
      if (!this.isLogin) {
        uni.$u.route(this.$utils.getRoutePath('Login'))
      } else {
        this.$store.dispatch('common/saveClazzId', value[0].id)
        uni.$u.route(this.$utils.getRoutePath('LocationSign'))
      }
      this.show.clazzList = false
    }
  },
}
</script>

<style lang="scss">
page {
  background-color: #F8F8F8;
}
</style>

<style lang="scss" scoped>
.avatar {
  width: 80px;
  height: 80px;
  border: 2px solid $uni-text-color-inverse;
  border-radius: 50%;

  .edit {
    width: 22px;
    height: 22px;
    background: #ECFFF6;
    border: 2px solid $uni-text-color-inverse;
    border-radius: 50%;
  }
}

.menu-item {
  background-color: #FCFCFC;
  box-shadow: 0 5px 10px #EBEBEB;
}
</style>