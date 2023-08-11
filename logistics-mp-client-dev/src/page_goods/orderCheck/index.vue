<template>
  <view class="px-4 py-2">
    <u--form class="mt-3" ref="form" :model="formData" :rules="rules" errorType="toast" labelWidth="auto">
      <!--报名信息-->
      <view class="bg-white rounded-md px-4">
        <u-form-item label="手机号" prop="phone" required borderBottom>
          <view class="w-full flex justify-end items-center py-2">
            <text>{{ formData.phone }}</text>
            <!-- <u-icon name="arrow-right"></u-icon> -->
          </view>
        </u-form-item>
        <u-form-item label="姓名" prop="realName" required borderBottom>
          <view class="w-full flex justify-end items-center py-2">
            <text v-if="memberInfo.realName">{{ formData.realName }}</text>
            <u--input v-model="formData.realName" :maxlength="10" placeholder="请填写" inputAlign="right" border="none"
              v-else></u--input>
          </view>
        </u-form-item>
        <u-form-item label="性别" prop="sex" borderBottom>
          <view class="w-full flex justify-end items-center py-2">
            <text v-if="memberInfo.sex">{{ formData.sex | sexList }}</text>
            <u-radio-group v-model="formData.sex" v-else>
              <u-radio :customStyle="{ margin: '0 12px' }" :label="item.name" :name="item.id" v-for="item in sexList"
                :key="item.id"></u-radio>
            </u-radio-group>
          </view>
        </u-form-item>
        <u-form-item label="工作单位" prop="workUnit" borderBottom>
          <view class="w-full flex justify-end items-center py-2">
            <u--input v-model="formData.workUnit" placeholder="请填写工作单位" inputAlign="right" border="none"></u--input>

            <!-- <u-icon name="arrow-right" :space="8" :label="formData.workUnit || '请选择'" labelPos="left" @click="showUnit = true" v-else></u-icon> -->
          </view>
        </u-form-item>
        <u-form-item label="备注" prop="remark">
          <view class="w-full py-2 mx-2">
            <u--input v-model="formData.remark" inputAlign="right" placeholder="请填写" placeholderClass="text-right text-sm"
              border="none"></u--input>
          </view>
        </u-form-item>
      </view>
      <!--报名机构-->
      <view class="bg-white rounded-md px-4 py-2 mt-3">
        <u-form-item label="报名机构">
          <text class="text-gray-500">{{ activityDetail.orgName }}</text>
        </u-form-item>
      </view>
      <!--报名课程-->
      <view class="bg-white rounded-md px-4 py-2 mt-3">
        <u-form-item label="报名课程"></u-form-item>
        <view class="flex pb-3">
          <u--image :src="activityDetail.coverUrl" width="70" height="52" :radius="4"></u--image>
          <view class="flex-1 ml-2">
            <view class="mb-2">{{ activityDetail.courseName }}</view>
            <view class="text-sm text-6">讲师：{{ (activityDetail.lecturers || []).map(v => v.lecturerName).join(', ') }}
            </view>
          </view>
        </view>
      </view>
    </u--form>
    <view class="h-20"></view>
    <view class="flex items-center bg-white px-4 py-3 footer-bar">
      <!-- <view class="flex items-center mr-3">
        <text class="text-gray-500">应付款：</text>
        <text class="text-lg text-error text-price">{{ activityDetail.price | price }}</text>
      </view> -->
      <view class="flex-1">
        <u-button type="primary" shape="circle" :loading="loading.submit" text="提交" @click="onSignUp"></u-button>
      </view>
    </view>
    <!--选择单位-->
    <u-picker :show="showUnit" :columns="[unitList]" keyName="name" :closeOnClickOverlay="true" @close="showUnit = false"
      @cancel="showUnit = false" @confirm="selectUnit"></u-picker>
  </view>
</template>

<script>
import { mapState, mapGetters } from 'vuex'

export default {
  name: 'OrderCheck',
  data() {
    return {
      loading: {
        submit: false
      },
      activityDetail: {},
      formData: {
        phone: null,      // 手机号
        headImgUrl: null, // 头像
        realName: null,   // 姓名
        sex: null,        // 性别
        unitId: null,     // 工作单位ID
        workUnit: null,   // 工作单位
        remark: null,     // 备注
      },
      rules: {
        phone: { type: 'string', required: true, message: '请填写手机号', trigger: ['blur', 'change'] },
        realName: { type: 'string', required: true, message: '请填写姓名', trigger: ['blur', 'change'] }
      },
      memberInfo: {},
      showUnit: false
    }
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo',
      authInfo: 'authInfo'
    }),
    ...mapGetters({
      sexList: 'common/sexList',
      unitList: 'common/unitList'
    })
  },
  async onLoad(options) {
    let params = await this.$utils.getOptions(options)
    this.activityDetail = { ...params }
    this.$logger.info(`🚀 ~ orderCheck onLoad ~ ${JSON.stringify(this.activityDetail)}`)
    this.formData.phone = this.userInfo.userAccount
    this.getDetail()
    this.getInfo()
  },
  onReady() {
    // #ifdef MP
    this.$refs.form.setRules(this.rules)
    // #endif
  },
  methods: {
    async getDetail() {
      uni.showLoading()
      let { code, data } = await this.$api.Course.info({
        id: this.activityDetail.id
      })
      uni.hideLoading()
      if (code !== 200) return false
      this.activityDetail = {
        ...this.activityDetail,
        ...(data || {})
      }
    },
    async getInfo() {
      uni.showLoading()
      let { code, data } = await this.$api.Member.info()
      uni.hideLoading()
      if (code !== 200) return false
      this.memberInfo = data || {}
      for (let key in this.formData) {
        this.formData[key] = this.memberInfo[key]
      }
      if (!data) return false
      this.$store.commit('system/updateUserInfo', {
        headImgUrl: this.memberInfo.headImgUrl,
        userName: this.memberInfo.realName
      })
    },
    selectUnit({ value }) {
      this.formData.unitId = value[0].id,
      this.formData.workUnit = value[0].name
      this.showUnit = false
    },
    async onSignUp() {
      let res = await this.$refs.form.validate().catch(() => { })
      if (!res) return false

      this.loading.submit = true
      let { code, data } = await this.$api.Course.signUp({
        courseId: this.activityDetail.id,
        orgId: this.activityDetail.orgId,
        register: {
          openId: this.authInfo.openId,
          ...this.formData
        }
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$message.showToast('报名成功')
      const eventChannel = this.getOpenerEventChannel()
      eventChannel.emit('updateOrder')
      this.goToOrder()
    },
    goToOrder() {
      uni.$u.route({
        type: 'redirect',
        url: this.$utils.getRoutePath('OrderDetail'),
        params: {
          ...this.formData,
          orgName: this.activityDetail.orgName,
          courseId: this.activityDetail.id,
          coverUrl: this.activityDetail.coverUrl,
          courseName: this.activityDetail.courseName,
          lecturers: (this.activityDetail.lecturers || []).map(v => v.lecturerName).join(', ')
        }
      })
    }
  }
}
</script>

<style lang="scss">
page {
  background: $uni-bg-color-grey;
}

.u-radio-group.u-radio-group--row {
  justify-content: flex-end;
}
</style>