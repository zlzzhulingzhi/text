<template>
  <view class="px-4 py-2">
    <u--form class="px-2" ref="form" :model="form" :rules="rules" errorType="toast" labelWidth="auto">
      <u-form-item label="访客姓名" prop="visitorName" borderBottom required>
        <view class="w-full text-right" v-if="userInfo.userName">
          <text class="text-6">{{ userInfo.userName }}</text>
        </view>
        <u--input
          v-model="form.visitorName"
          placeholder="请填写访客姓名"
          :maxlength="20"
          inputAlign="right"
          border="none"
          v-else></u--input>
      </u-form-item>
      <u-form-item label="手机号" prop="phone" borderBottom required>
        <view class="w-full text-right" v-if="userInfo.userAccount">
          <text class="text-6">{{ userInfo.userAccount }}</text>
        </view>
        <u--input
          v-model="form.phone"
          placeholder="请填写手机号"
          :maxlength="11"
          inputAlign="right"
          border="none"
          v-else></u--input>
      </u-form-item>
      <u-form-item label="车牌号" prop="carNumber" borderBottom required>
        <u--input
          v-model="form.carNumber"
          placeholder="请填写车牌号"
          :maxlength="8"
          inputAlign="right"
          border="none"></u--input>
      </u-form-item>
      <u-form-item label="来访时间" prop="visitTime" borderBottom required>
        <view class="w-full text-right" @click="openPicker">
          <text class="text-6">{{ $u.timeFormat(form.visitTime, 'yyyy-mm-dd hh:MM') }}</text>
        </view>
      </u-form-item>
      <u-form-item label="访问对象" prop="visitPeople" borderBottom required>
        <u--input
          v-model="form.visitPeople"
          placeholder="请填写访问对象"
          :maxlength="20"
          inputAlign="right"
          border="none"></u--input>
      </u-form-item>

      <u-form-item :label="visitData" prop="visitDepartment" borderBottom required>
        <view class="flex-1">
          <u-radio-group v-model="visitDOrO" @change="radioChange" placement="row">
            <u-radio label="部门" name="部门"></u-radio>
            <u-radio label="机构" name="机构"></u-radio>
          </u-radio-group>

          <u--input
            v-model="form.visitDepartment"
            placeholder="请填写访问部门"
            :maxlength="20"
            inputAlign="right"
            v-if="visitDOrO === '部门'"
            border="none"></u--input>

          <view v-else class="elSelCon">
            <u-icon
              name="arrow-right"
              :space="8"
              :label="visitOrgName"
              labelPos="left"
              @click="showSelOrg = true"
              v-if="form.visitOrgId"></u-icon>
            <u-icon
              name="arrow-right"
              :space="8"
              label="请选择"
              labelPos="left"
              @click="showSelOrg = true"
              v-else></u-icon>
          </view>

          <u-picker
            :show="showSelOrg"
            :columns="[orgInfo]"
            keyName="name"
            :closeOnClickOverlay="true"
            @close="showSelOrg = false"
            @cancel="showSelOrg = false"
            @confirm="selectNation"></u-picker>
        </view>
      </u-form-item>

      <u-form-item label="访问事由" prop="visitReason" required>
        <u--textarea v-model="form.visitReason" placeholder="请填写访问事由" :maxlength="200"></u--textarea>
      </u-form-item>
    </u--form>
    <u-button class="mt-3" type="primary" text="提交" @click="onSubmit"></u-button>
    <u-datetime-picker
      :show="showPicker"
      v-model="form.visitTime"
      mode="datetime"
      :minDate="minDate"
      :maxDate="maxDate"
      closeOnClickOverlay
      @close="closePicker"
      @cancel="closePicker"
      @confirm="confirmPicker"></u-datetime-picker>
  </view>
</template>

<script>
import { mapState } from 'vuex';

export default {
  name: 'AddVisit',
  data() {
    return {
      form: {
        visitorName: null, // 访客姓名
        phone: null, // 手机号
        carNumber: null, // 车牌号
        visitTime: Date.now(), // 来访时间
        visitPeople: null, // 访问对象
        visitDepartment: null, // 访问部门
        visitOrgId: null, //访问机构

        visitOrgId: null,
        visitReason: null, // 访问事由
      },
      rules: {
        visitorName: { type: 'string', required: true, message: '请填写访客姓名', trigger: ['blur', 'change'] },
        phone: { type: 'string', required: true, message: '请填写手机号', trigger: ['blur', 'change'] },
        carNumber: { type: 'string', required: true, message: '请填写车牌号', trigger: ['blur', 'change'] },
        // visitTime: { type: 'string', required: true, message: '请选择来访时间', trigger: ['blur', 'change'] },
        visitPeople: { type: 'string', required: true, message: '请填写访问对象', trigger: ['blur', 'change'] },
        visitDepartment: {
          validator: (rule, value, callback) => {
            if (this.visitDOrO === '部门' && !this.form.visitDepartment) {
              callback(new Error('请输入要访问的部门名称'));
            } else if (this.visitDOrO === '机构' && !this.form.visitOrgId) {
              // 如果校验通过，也要执行callback()回调
              callback(new Error('请选择要访问的机构'));
            }
            callback()
          },
          trigger: ['change', 'blur'],
        },
        visitReason: { type: 'string', required: true, message: '请填写访问事由', trigger: ['blur', 'change'] },
      },
      showPicker: false,
      showSelOrg: false,
      orgInfo: [],
      visitOrgName: null,

      visitDOrO: '部门'
    };
  },
  created() {
    this.getOrgInfo();
  },
  computed: {
    ...mapState('system', {
      userInfo: 'userInfo',
    }),
    minDate() {
      return Date.now() - 7 * 24 * 3600 * 1000;
    },
    maxDate() {
      return Date.now() + 7 * 24 * 3600 * 1000;
    },
    params() {
      return {
        ...this.form,
        visitTime: uni.$u.timeFormat(this.form.visitTime, 'yyyy-mm-dd hh:MM:ss'),
        memberId: this.userInfo.memberId,
      };
    },
    visitData() {
      return `访问${this.visitDOrO}`;
    },
  },
  methods: {
    openPicker() {
      this.showPicker = true;
    },
    closePicker() {
      this.showPicker = false;
    },
    confirmPicker() {
      this.closePicker();
    },
    onSubmit() {
      if (this.userInfo.userName) this.form.visitorName = this.userInfo.userName;
      if (this.userInfo.userAccount) this.form.phone = this.userInfo.userAccount;
      if (this.form.carNumber) this.form.carNumber = this.form.carNumber.toUpperCase();
      if (!this.form.visitTime) this.form.visitTime = Date.now();
      

      this.$refs.form
        .validate()
        .then(async (res) => {
          let { code, data } = await this.$api.MemberVisit.add(this.params);
          if (code !== 200) return false;
          this.$message.showToast('添加成功');
          const eventChannel = this.getOpenerEventChannel();
          eventChannel.emit('addSuccess');
          uni.$u.route({
            type: 'back',
          });
        })
        .catch(() => {});
    },
    async getOrgInfo() {
      let { code, data } = await this.$api.OrgInfo.list();
      if (code !== 200) return false;
      this.orgInfo = data;
    },
    selectNation(params) {
      this.visitOrgName = params.value[0].name;
      this.form.visitOrgId = params.value[0].id;
      this.showSelOrg = false;
    },
    radioChange(params) {
      if(params === '部门') {
        this.form.visitOrgId = null
      } else {
        this.form.visitDepartment = null
      }
    }
  },
};
</script>

<style lang="scss" scoped>
::v-deep .u-radio-group {
  justify-content: flex-end;
  margin-bottom: 18rpx;

  .u-radio {
    margin-left: 8rpx;
  }
}

::v-deep .elSelCon {
  height: 48rpx;
  .u-icon {
    padding-top: 12rpx;
  }
}
</style>
