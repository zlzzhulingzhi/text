<template>
  <view class="px-4">
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>头像</text>
      <view class="flex items-center">
        <button class="u-reset-button mr-2" open-type="chooseAvatar" @chooseavatar="chooseAvatar">
          <u-avatar :src="userInfo.headImgUrl || '/static/img_avatar.png'" size="60"></u-avatar>
        </button>
        <u-icon name="arrow-right" :space="8"></u-icon>
      </view>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>手机号</text>
      <!-- <u-icon name="arrow-right" :space="8" :label="formData.account" labelPos="left"></u-icon> -->
      <text class="text-read">{{ formData.account }}</text>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>姓名</text>
      <u-icon name="arrow-right" :space="8" :label="formData.realName" labelPos="left"
        @click="goToProfile({ realName: formData.realName })"></u-icon>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>身份证号</text>
      <u-icon name="arrow-right" :space="8" :label="formData.idNumber || '未填写'" labelPos="left"
        @click="goToProfile({ idNumber: formData.idNumber })"></u-icon>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>性别</text>
      <u-icon name="arrow-right" :space="8" :label="formData.sex | sexList" labelPos="left" @click="show.sex = true"
        v-if="formData.sex"></u-icon>
      <u-icon name="arrow-right" :space="8" label="请选择" labelPos="left" @click="show.sex = true" v-else></u-icon>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>出生年月</text>
      <u-icon name="arrow-right" :space="8" :label="formData.birthday || '请选择'" labelPos="left"
        @click="show.date = true"></u-icon>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>民族</text>
      <u-icon name="arrow-right" :space="8" :label="formData.nation | nationList" labelPos="left"
        @click="show.nation = true" v-if="formData.nation"></u-icon>
      <u-icon name="arrow-right" :space="8" label="请选择" labelPos="left" @click="show.nation = true" v-else></u-icon>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <!-- #ifdef MP-WEIXIN -->
      <text>籍贯</text>
      <picker mode="region" @change="selectRegion">
        <u-icon name="arrow-right" :space="8" :label="formData.nativePlace || '请选择'" labelPos="left"></u-icon>
      </picker>
      <!-- #endif -->
      <!-- #ifndef MP-WEIXIN -->
      <text>籍贯（请在小程序设置）</text>
      <u-icon name="arrow-right" :space="8" :label="formData.nativePlace || '请选择'" labelPos="left"></u-icon>
      <!-- #endif -->
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>学历</text>
      <u-icon name="arrow-right" :space="8" :label="formData.education | eduList" labelPos="left"
        @click="show.edu = true" v-if="formData.education"></u-icon>
      <u-icon name="arrow-right" :space="8" label="请选择" labelPos="left" @click="show.edu = true" v-else></u-icon>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>毕业院校</text>
      <u-icon name="arrow-right" :space="8" :label="formData.school || '未填写'" labelPos="left"
        @click="goToProfile({ school: formData.school })"></u-icon>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>工作单位</text>
      <u--input v-model="formData.workUnit" placeholder="请填写工作单位" inputAlign="right" border="none" @blur="confirmWorkUnit"></u--input>
      <!-- <u-icon name="arrow-right" :space="8" :label="formData.workUnit" labelPos="left" @click="show.unit = true"
        v-if="formData.unitId"></u-icon>
      <u-icon name="arrow-right" :space="8" label="请选择" labelPos="left" @click="show.unit = true" v-else></u-icon> -->
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>工作地址</text>
      <u-icon name="arrow-right" :space="8" :label="formData.workAddress || '未填写'" labelPos="left"
        @click="goToProfile({ workAddress: formData.workAddress })"></u-icon>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>户籍住址</text>
      <u-icon name="arrow-right" :space="8" :label="formData.registerAddress || '未填写'" labelPos="left"
        @click="goToProfile({ registerAddress: formData.registerAddress })"></u-icon>
    </view>
    <view class="flex justify-between items-center py-4 u-border-bottom">
      <text>居住地</text>
      <u-icon name="arrow-right" :space="8" :label="formData.resideAddress || '未填写'" labelPos="left"
        @click="goToProfile({ resideAddress: formData.resideAddress })"></u-icon>
    </view>
    <view class="flex justify-between items-center py-4">
      <text>电子邮箱</text>
      <u-icon name="arrow-right" :space="8" :label="formData.email || '未填写'" labelPos="left"
        @click="goToProfile({ email: formData.email })"></u-icon>
    </view>
    <!--选择性别-->
    <u-picker :show="show.sex" :columns="[sexList]" keyName="name" :closeOnClickOverlay="true" @close="show.sex = false"
      @cancel="show.sex = false" @confirm="selectSex"></u-picker>
    <!--选择年月日-->
    <u-datetime-picker :show="show.date" @close="show.date = false" v-model="defaultDate" mode="date"
      :minDate="-315648000000"  @cancel="show.date = false" @confirm="selectDate"></u-datetime-picker>

    <!--选择民族-->
    <u-picker :show="show.nation" :columns="[nationList]" keyName="name" :closeOnClickOverlay="true"
      @close="show.nation = false" @cancel="show.nation = false" @confirm="selectNation"></u-picker>
    <!--选择学历-->
    <u-picker :show="show.edu" :columns="[eduList]" keyName="name" :closeOnClickOverlay="true" @close="show.edu = false"
      @cancel="show.edu = false" @confirm="selectEdu" :defaultIndex="[5]"></u-picker>
    <!--选择单位-->
    <u-picker :show="show.unit" :columns="[unitList]" keyName="name" :closeOnClickOverlay="true"
      @close="show.unit = false" @cancel="show.unit = false" @confirm="selectUnit"></u-picker>
  </view>
</template>

<script>
import { mapState, mapGetters } from 'vuex'
import CosRequest from '@/lib/cos.js'

export default {
  name: 'MyProfile',
  data() {
    return {
      cos: null,
      formData: {
        account: null,      // 手机号
        headImgUrl: null,   // 头像
        realName: null,     // 姓名
        idNumber: null,     // 身份证号
        sex: null,          // 性别
        birthday: null,     // 出生年月
        nation: null,       // 民族
        nativePlace: null,  // 籍贯
        education: null,    // 学历
        school: null,       // 毕业院校
        unitId: null,       // 工作单位ID
        workUnit: null,     // 工作单位
        workAddress: null,  // 工作地址
        registerAddress: null,     // 户籍住址
        resideAddress: null,  // 居住地
        email: null         // 邮箱
      },
      show: {
        sex: false,
        date: false,
        nation: false,
        edu: false,
        unit: false
      },
      // 默认2000年
      defaultDate:946656000000
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    ...mapGetters({
      sexList: 'common/sexList',
      eduList: 'common/eduList',
      nationList: 'common/nationList',
      unitList: 'common/unitList'
    })
  },
  watch: {
    'formData.realName': {
      handler(newVal) {
        if (newVal) this.$store.commit('system/updateUserInfo', {
          userName: newVal
        })
      }
    }
  },
  onLoad() {
    this.cos = new CosRequest({
      type: 'avatar'
    })
    this.formData.account = this.userInfo.userAccount
    this.formData.realName = this.userInfo.userName
    this.getInfo()
    uni.$on(this.$store.state.config.EVENT.PROFILE, (formData) => {
      this.formData = Object.assign(this.formData, formData)
    })
  },
  methods: {
    async getInfo() {
      uni.showLoading()
      let { code, data } = await this.$api.Member.info()
      uni.hideLoading()
      if (code !== 200) return false
      this.formData = Object.assign(this.formData, data || {})
      if (!data) return false
      this.$store.commit('system/updateUserInfo', {
        headImgUrl: data.headImgUrl,
        userName: data.realName
      })
    },
    chooseAvatar(e) {
      uni.getImageInfo({
        src: e.detail.avatarUrl,
        success: (res) => {
          console.log('getImageInfo == ', res)
          this.cos.putObject({
            filePath: res.path,
            fileType: res.type,
            onProgress: () => uni.showLoading({ title: '加载中' }),
            onSuccess: (url) => this.updateProfile({ headImgUrl: url }),
            onError: () => uni.hideLoading()
          })
        },
        fail: () => this.$message.showToast('获取图片信息失败')
      })
    },
    goToProfile(formData) {
      uni.$u.route(this.$utils.getRoutePath('UpdateProfile'), {
        k: Object.keys(formData).join(''),
        v: Object.values(formData).join('')
      })
    },
    async selectSex({ value }) {
      await this.updateProfile({ sex: value[0].id })
      this.show.sex = false
    },
    async selectDate({ value }) {
      await this.updateProfile({ birthday: uni.$u.timeFormat(value, 'yyyy-mm-dd') })
      this.show.date = false
    },
    selectRegion(e) {
      this.updateProfile({ nativePlace: e.target.value.map(v => v).join(' ') })
    },
    async selectNation({ value }) {
      await this.updateProfile({ nation: value[0].id })
      this.show.nation = false
    },
    async selectEdu({ value }) {
      await this.updateProfile({ education: value[0].id })
      this.show.edu = false
    },
    async selectUnit({ value }) {
      await this.updateProfile({ unitId: value[0].id, workUnit: value[0].name })
      this.show.unit = false
    },
    async confirmWorkUnit(value) {
      await this.updateProfile({ workUnit: value })
    },
    async updateProfile(formData) {
      uni.showLoading({ title: '请稍候' })
      let { code } = await this.$api.Member.update(formData)
      uni.hideLoading()
      if (code !== 200) return false
      for (let key in formData) {
        this.formData[key] = formData[key]
      }
      this.$message.showToast('更新成功')
      // 更新头像
      if (formData.hasOwnProperty('headImgUrl')) {
        this.$store.commit('system/updateUserInfo', {
          headImgUrl: formData.headImgUrl
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.text-read {
  font-size: 15px;
  color: #606266;
}
</style>