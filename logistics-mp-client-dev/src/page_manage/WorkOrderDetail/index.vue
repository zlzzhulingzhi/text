<!--报事报修详情-->

<template>
  <view>
    <view class="bg-white rounded-md mx-4 p-3">
      <view class="flex justify-between items-center py-2">
        <text class="mr-3">类别</text>
        <text class="text-gray-500">{{ detailData.category | workOrderList }}</text>
      </view>
      <view class="flex justify-between items-center py-2">
        <text class="mr-3">联系人</text>
        <text class="text-gray-500">{{ detailData.contactPerson }}</text>
      </view>
      <view class="flex justify-between items-center py-2">
        <text class="mr-3">联系电话</text>
        <text class="text-gray-500">{{ detailData.contactNumber }}</text>
      </view>
      <view class="flex justify-between items-center py-2">
        <text class="mr-3">提交机构</text>
        <text class="text-gray-500">{{ detailData.orgName }}</text>
      </view>
      <view class="flex justify-between items-center py-2">
        <text class="mr-3">提交时间</text>
        <text class="text-gray-500">{{ detailData.createTime }}</text>
      </view>
      <view class="flex justify-between items-center py-2" v-if="detailData.roomNo">
        <text class="mr-3">关联教室</text>
        <text class="text-gray-500">{{ classroomName }}</text>
      </view>
      <view class="flex-col py-2" v-if="detailData.attachList && detailData.attachList.length">
        <text class="mr-3">报事图片</text>
        <view class="flex flex-wrap justify-start">
          <view class="mr-2 mt-4" v-for="item, index in detailData.attachList" :key="index">
            <u--image :src="item.fileUrl" :name="item.id" width="80" height="80" :radius="4"
              @click="imagePreview(index)"></u--image>
          </view>
        </view>
      </view>
    </view>

  </view>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import { loginMixins } from '@/mixins/loginMixins.js'
export default {
  name: 'WorkOrderDetail',
  mixins: [loginMixins],
  data() {
    return {
      loading: {
        init: true
      },
      params: {},
      detailData: {},
      classroomName: '',
      imgArr: []
    }
  },
  async onLoad(options) {
    const params = await this.$utils.getOptions(options);
    this.params = params;
    this.getDetail();
  },
  computed: {
    ...mapGetters({
      isLogin: 'system/isLogin',
      workOrderList: 'common/workOrderList',
      buildingList: 'common/buildingList',
      floorList: 'common/floorList',
      classroomList: 'common/classroomList',
      isStudent: 'system/isStudent',
      isOrg: 'system/isOrg',
      isAdmin: 'system/isAdmin'
    }),
  },
  methods: {
    onLoginSuccess() {
      this.getDetail();
    },
    async getDetail() {
      this.loading.init = true;
      let requestAction = this.isAdmin ? this.$api.WorkOrder.platMatterReportDetail : this.$api.WorkOrder.matterReportDetail;
      let { code, data } = await requestAction(this.params);
      this.loading.init = false;
      if (code !== 200) return false;
      this.detailData = { ...data };
      // console.log('detailData == ', data);
      if (data && data.attachList && data.attachList.length) {
        data.attachList.forEach(item => {
          // 这里要加https才能看到效果
          const imgUrl = 'https:' + item.fileUrl;
          this.imgArr.push(imgUrl);
        });
      }
      if (data && data.roomNo) {
        setTimeout(() => {
          this.getCourseNameAndType();
        }, 300);
      }
    },
    getCourseNameAndType() {
      let form = this.detailData;
      let buildingItem = `${(this.buildingList.find(v => v.id === form.building) || {}).name}`;
      let floorItem = `${(this.floorList.find(v => v.id === form.floor) || {}).name}`;
      let classRoomItem = ` ${(this.classroomList.find(v => v.id === form.roomType) || {}).name}`
      this.classroomName = buildingItem + floorItem + classRoomItem
        + ` 房间号：` + form.roomNo;
    },
    // 图片预览
    imagePreview(index) {
      // console.log('this.imgArr == ', this.imgArr)
      uni.previewImage({
        current: index,
        urls: this.imgArr
      })
    }
  },
}
</script>

<style lang="scss" scoped>

</style>