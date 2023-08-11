<template>
  <view>
    <view class="mb-4">
      <view class="relative rounded-b-3xl overflow-hidden">
        <u--image :src="`${cosPath}/bg_top.png`" width="100%" height="225px" mode="widthFix">
          <view slot="error" class="bg-red-800 w-full h-full"></view>
        </u--image>
      </view>
    </view>
    <view class="px-4">
      <u--form v-if="(type === 'Classroom' || type === 'Dormitory')" ref="form" :model="form" :rules="rules"
        labelPosition="left" labelWidth="auto">
        <u-form-item label="日期" prop="useDate" borderBottom>
          <view class="flex justify-end items-center w-full" @click="show.useDate = true">
            <u-icon name="arrow-right" :label="$u.timeFormat(form.useDate, 'yyyy年mm月dd日')" labelPos="left"></u-icon>
          </view>
        </u-form-item>
        <u-form-item :label="`${pageInfo.name}类型`" prop="roomType" borderBottom>
          <view class="flex justify-end items-center w-full" @click="show.roomType = true">
            <u-icon name="arrow-right" :label="(listType.find(v => v.id === form.roomType) || {}).name || '请选择'"
              labelPos="left"></u-icon>
          </view>
        </u-form-item>
        <u-form-item label="单元/楼层" prop="building" borderBottom>
          <view class="flex justify-end items-center w-full" @click="show.building = true">
            <u-icon v-if="form.building || form.floor" name="arrow-right"
              :label="`${(buildingList.find(v => v.id === form.building) || {}).name || '全部单元'}${(floorList.find(v => v.id === form.floor) || {}).name || '全部楼层'}`"
              labelPos="left"></u-icon>
            <u-icon name="arrow-right" label="请选择" labelPos="left" v-else></u-icon>
          </view>
        </u-form-item>
      </u--form>
      <u--form v-else-if="type === 'WorkOrder'" ref="form" :model="form" :rules="rules" labelPosition="left"
        labelWidth="auto">
        <u-form-item label="日期" prop="useDate" borderBottom>
          <view class="flex justify-end items-center w-full" @click="show.useDate = true">
            <u-icon name="arrow-right" :label="$u.timeFormat(form.useDate, 'yyyy年mm月dd日')" labelPos="left"></u-icon>
          </view>
        </u-form-item>
        <u-form-item label="提交机构" prop="orgId" borderBottom>
          <view class="flex justify-end items-center w-full" @click="show.orgItem = true">
            <u-icon name="arrow-right" :label="(orgList.find(v => v.id === form.orgId) || {}).name || '请选择'"
              labelPos="left"></u-icon>
          </view>
        </u-form-item>
        <u-form-item label="类别" prop="category" borderBottom>
          <view class="flex justify-end items-center w-full" @click="show.category = true">
            <u-icon name="arrow-right" :label="(workOrderList.find(v => v.id === form.category) || {}).name || '请选择'"
              labelPos="left"></u-icon>
          </view>
        </u-form-item>
      </u--form>
      <u--form v-else ref="form" :model="form" :rules="rules" labelPosition="left">
        <u-form-item label="机构" prop="orgId" borderBottom>
          <view class="flex justify-end items-center w-full" @click="show.orgItem = true">
            <u-icon name="arrow-right"
              :label="(orgList.find(v => v.id === openClassForm.orgId) || {}).name || '请选择'"
              labelPos="left"></u-icon>
          </view>
        </u-form-item>
        <u-form-item label="开始日期" prop="beginDate" borderBottom>
          <view class="flex justify-end items-center w-full" @click="show.useBeginDate = true">
            <u-icon name="arrow-right" :label="$u.timeFormat(openClassForm.beginDate, 'yyyy年mm月dd日')"
              labelPos="left"></u-icon>
          </view>
        </u-form-item>
        <u-form-item label="结束日期" prop="endDate" borderBottom>
          <view class="flex justify-end items-center w-full" @click="show.useEndDate = true">
            <u-icon name="arrow-right" :label="$u.timeFormat(openClassForm.endDate, 'yyyy年mm月dd日')"
              labelPos="left"></u-icon>
          </view>
        </u-form-item>
      </u--form>
      <view class="mt-8">
        <u-button type="primary" text="查询" @click="onSeacrh"></u-button>
      </view>
    </view>
    <u-picker :show="show.building" :columns="[buildingListComputed, floorListComputed]" keyName="name" @cancel="cancelBuilding"
      @confirm="confirmBuilding"></u-picker>
    <u-picker :show="show.roomType" :columns="[listType]" keyName="name" @cancel="cancelType"
      @confirm="confirmType"></u-picker>
    <u-picker :show="show.orgItem" :columns="[orgList]" keyName="name" @cancel="cancelOrg"
      @confirm="confirmOrg"></u-picker>
    <u-picker :show="show.category" :columns="[workOrderList]" keyName="name" @cancel="cancelCategory"
      @confirm="confirmCategory"></u-picker>
    <u-datetime-picker :show="show.useDate" v-model="form.useDate" mode="date" @cancel="cancelDate"
      @confirm="confirmDate"></u-datetime-picker>
    <u-datetime-picker :show="show.useBeginDate" v-model="openClassForm.beginDate" mode="date" @cancel="cancelBeginDate"
      @confirm="confirmBeginDate"></u-datetime-picker>
    <u-datetime-picker :show="show.useEndDate" v-model="openClassForm.endDate" mode="date" @cancel="cancelEndDate"
      @confirm="confirmEndDate"></u-datetime-picker>
  </view>
</template>

<script>
import { mapState, mapGetters } from 'vuex'

export default {
  name: 'Query',
  data() {
    return {
      type: null,
      typeMapping: {
        Classroom: { name: '教室', route: 'Classroom' },
        Dormitory: { name: '宿舍', route: 'Dormitory' },
        WorkOrder: { name: '报事报修', route: 'WorkOrderList' },
        OpenClass: { name: '开课信息', route: 'OpenClass' }
      },
      form: {
        building: null,
        floor: null,
        roomType: null,
        orgId: null,
        useDate: Number(new Date())
      },
      openClassForm: {
        beginDate: Number(new Date(new Date().getTime() - 24*60*60*1000)),
        endDate: Number(new Date()),
        orgId: null
      },
      rules: {
        // building: [{ required: true, message: '请选择', trigger: ['blur', 'change'] }],
        roomType: [{ required: true, message: '请选择', trigger: ['blur', 'change'] }],
        useDate: [{ required: true, message: '请选择', trigger: ['blur', 'change'] }]
      },
      show: {
        building: false,
        roomType: false,
        useDate: false,
        orgItem: false,
        category: false,
        useBeginDate: false,
        useEndDate: false
      }
    }
  },
  computed: {
    ...mapState('config', {
      cosPath: 'COS_PATH'
    }),
    ...mapGetters({
      buildingList: 'common/buildingList',
      floorList: 'common/floorList',
      classroomList: 'common/classroomList',
      dormitoryList: 'common/dormitoryList',
      orgList: 'common/orgList',
      workOrderList: 'common/workOrderList',
    }),
    listType() {
      if (this.type === 'Classroom' || this.type === 'WorkOrder') return this.classroomList
      if (this.type === 'Dormitory') return this.dormitoryList
      return []
    },
    pageInfo() {
      if (!this.type) return {}
      return this.typeMapping[this.type]
    },
    buildingListComputed() {
      return [{ id: '', name: '全部' }, ...this.buildingList];
    },
    floorListComputed() {
      return [{ id: '', name: '全部' }, ...this.floorList];
    },
  },
  onLoad(options) {
    let params = this.$utils.getOptions(options)
    this.type = params.type
    uni.setNavigationBarTitle({
      title: `查询${this.pageInfo.name}`
    })
  },
  methods: {
    cancelBuilding() {
      this.show.building = false
    },
    confirmBuilding({ value }) {
      this.form.building = value[0].id
      this.form.floor = value[1].id
      this.cancelBuilding()
    },
    cancelType() {
      this.show.roomType = false
    },
    confirmType({ value }) {
      this.form.roomType = value[0].id
      this.cancelType()
    },
    cancelDate() {
      this.show.useDate = false
    },
    confirmDate() {
      this.cancelDate()
    },
    cancelBeginDate() {
      this.show.useBeginDate = false
    },
    confirmBeginDate() {
      this.cancelBeginDate()
    },
    cancelEndDate() {
      this.show.useEndDate = false
    },
    confirmEndDate() {
      this.cancelEndDate()
    },
    cancelOrg() {
      this.show.orgItem = false
    },
    confirmOrg({ value }) {
      if (this.type === 'OpenClass') {
        this.openClassForm.orgId = value[0].id;
      } else {
        this.form.orgId = value[0].id;
      }
      this.cancelOrg()
    },
    cancelCategory() {
      this.show.category = false
    },
    confirmCategory({ value }) {
      this.form.category = value[0].id;
      this.cancelCategory()
    },
    onSeacrh() {
      // if (!this.form.building || !this.form.floor) return this.$message.showToast('请选择单元/楼层')
      let params = this.form;
      if (this.type === 'WorkOrder') {
        const { useDate, category, orgId } = this.form;
        params = { category, orgId, reportDate: uni.$u.timeFormat(useDate, 'yyyy-mm-dd') };
      } else if (this.type === 'OpenClass') {
        const { orgId, beginDate, endDate } = this.openClassForm;
        if (endDate - beginDate < 0) {
          return this.$message.showToast('结束时间选择错误，请重新选择');
        }
        params = { orgId, startDate: uni.$u.timeFormat(beginDate, 'yyyy-mm-dd'), endDate: uni.$u.timeFormat(endDate, 'yyyy-mm-dd') };
      } else {
        if (!this.form.roomType) return this.$message.showToast('请选择类型');
      }
      uni.$u.route(this.$utils.getRoutePath(this.pageInfo.route), params)
    }
  }
}
</script>

<style lang="scss" scoped>

</style>