<!--工单/报事报修-->
<template>
  <view class="px-4 py-2">
    <u--form class="mt-3" ref="form" :model="formData" :rules="rules" errorType="toast" labelWidth="auto">
      <view class="bg-white rounded-md px-4">
        <u-form-item label="联系电话" prop="contactNumber" type="number" required borderBottom>
          <view class="w-full flex justify-end items-center py-2">
            <u--input v-model="formData.contactNumber" :maxlength="11" placeholder="请填写联系电话" inputAlign="right"
              border="none"></u--input>
          </view>
        </u-form-item>
        <u-form-item label="联系人" prop="contactPerson" required borderBottom>
          <view class="w-full flex justify-end items-center py-2">
            <u--input v-model="formData.contactPerson" :maxlength="10" placeholder="请填写联系人" inputAlign="right"
              border="none"></u--input>
          </view>
        </u-form-item>
        <u-form-item label="类别" prop="category" borderBottom required @click="showCategory = true">
          <u--input v-model="categoryName" disabled disabledColor="#ffffff" placeholder="请选择类别" border="none">
          </u--input>
          <u-icon slot="right" name="arrow-right"></u-icon>
        </u-form-item>
        <u-form-item label="关联教室" prop="classroomId" borderBottom @click="showSelectRoom = true">
          <u--input v-model="classroomName" disabled disabledColor="#ffffff" placeholder="请选择关联教室" border="none">
          </u--input>
          <u-icon slot="right" name="arrow-right"></u-icon>
        </u-form-item>
        <u-form-item borderBottom prop="fileList">
          <view class="mt-6 w-full flex-col">
            <text>报事图片(最多5张)</text>
            <view class="mt-4 w-full">
              <u-upload :fileList="fileList" @afterRead="afterRead" @delete="deletePic" multiple :maxCount="5"
                :previewFullImage="false">
              </u-upload>
            </view>
          </view>
        </u-form-item>

        <u-form-item label="报事说明" prop="description" borderBottom>
          <view class="w-full flex justify-end items-center py-2">
            <u--textarea v-model="formData.description" placeholder="请填写说明" autoHeight></u--textarea>
          </view>
        </u-form-item>
      </view>
    </u--form>
    <u-button @click="submit" :loading="loading.submit" type="primary" :disabled="isSubmitting">提交</u-button>

    <u-action-sheet :show="showCategory" :actions="workOrderList" title="请选择类别" @close="showCategory = false"
      @select="categorySelect">
    </u-action-sheet>
    <u-popup :show="showSelectRoom" :round="10" mode="right" @close="closePopup" @open="openPopUp">
      <scroll-view scroll-y="true" class="w-60 popupStyle" @scrolltolower="scrollToBottom">
        <PopupSelectList @click="popSelectClick" @showChooseBuilding="showChooseBuilding"
          @showChooseRoomType="showChooseRoomType" ref="popupSelect"></PopupSelectList>
      </scroll-view>
    </u-popup>
    <u-picker :show="show.building" :columns="[buildingList, floorList]" keyName="name" @cancel="cancelBuilding"
      @confirm="confirmBuilding"></u-picker>
    <u-picker :show="show.roomType" :columns="[classroomList]" keyName="name" @cancel="cancelType"
      @confirm="confirmType"></u-picker>
  </view>
</template>

<script>
import { mapState, mapGetters } from 'vuex'
import CosRequest from '@/lib/cos.js'
import PopupSelectList from '@/components/views/PopupSelectList.vue'

export default {
  name: 'WorkOrder',
  components: {
    PopupSelectList
  },
  data() {
    return {
      formData: {
        contactPerson: '', // 联系人
        contactNumber: '', // 联系电话
        description: '', // 报事说明
        attachList: [], //报事图片
        category: '', // 类别(1-开课、2-维修、3-更换、4-结课)
        classroomId: 0 // 关联教室
      },
      loading: {
        submit: false
      },
      rules: {
        contactNumber: [
          { type: 'string', required: true, message: '请填写联系电话', trigger: ['blur', 'change'] },

          {
            validator: (rule, value, callback) => {
              return uni.$u.test.mobile(value);
            },
            message: '手机号码不正确',
            // 触发器可以同时用blur和change
            trigger: ['change', 'blur'],
          }
        ],
        contactPerson: { type: 'string', required: true, message: '请填写联系人', trigger: ['blur', 'change'] },
        // description: [
        //   {
        //     required: true,
        //     message: '请填写说明'
        //   },
        //   {
        //     min: 5,
        //     message: '简介不能少于5个字',
        //     trigger: 'change',
        //   },
        // ]
      },
      showCategory: false,
      categoryName: '',
      fileList: [],
      // 正在提交
      isSubmitting: false,
      // 显示侧拉栏，选择楼层和教室
      showSelectRoom: false,
      // 关联教室名称
      classroomName: '',
      show: {
        building: false,
        roomType: false
      },
    }
  },
  onReady() {
    //onReady 为uni-app支持的生命周期之一
    this.$refs.form.setRules(this.rules)
  },
  onLoad() {
    this.cos = new CosRequest({
      type: 'matterReport'
    })
  },
  computed: {
    ...mapGetters({
      workOrderList: 'common/workOrderList',
      buildingList: 'common/buildingList',
      floorList: 'common/floorList',
      classroomList: 'common/classroomList',
    })
  },
  methods: {
    categorySelect(e) {
      this.formData.category = e.id;
      this.categoryName = e.name;
    },
    // 删除图片
    deletePic(event) {
      this.fileList.splice(event.index, 1);
      this.formData.attachList.splice(event.index, 1);
    },
    // 新增图片
    async afterRead(event) {
      // 当设置 multiple 为 true 时, file 为数组格式，否则为对象格式
      let lists = [].concat(event.file)
      let fileListLen = this.fileList.length

      lists.map((item) => {
        this.fileList.push({
          ...item,
          status: 'uploading',
          message: '上传中'
        })
      })

      // #ifndef MP-WEIXIN
      for (let i = 0; i < lists.length; i++) {
        const result = await this.uploadFilePromise(lists[i])
        if (result.status === 'success') {
          let item = this.fileList[fileListLen]
          this.fileList.splice(fileListLen, 1, Object.assign(item, {
            status: 'success',
            message: '',
            url: result.url
          }))
          fileListLen++
          this.formData.attachList.push(result.url);
        }
      }
      // #endif

      // #ifdef MP-WEIXIN
      for (let i = 0; i < lists.length; i++) {
        const result = await this.uploadFilePromiseMPWeixin(lists[i])
        if (result.status === 'success') {
          let item = this.fileList[fileListLen]
          this.fileList.splice(fileListLen, 1, Object.assign(item, {
            status: 'success',
            message: '',
            url: result.url
          }))
          fileListLen++
          this.formData.attachList.push(result.url);
        }
      }
      // #endif


    },

    uploadFilePromise(uploadObj) {
      // console.log('uploadObj === ', uploadObj);
      const imgTypeArr = uploadObj.name.split('.');
      const imgType = imgTypeArr[imgTypeArr.length - 1];
      return new Promise((resolve) => {
        this.cos.putObject({
          filePath: uploadObj.url,
          fileType: imgType,
          onSuccess: (url) => {
            resolve({ status: 'success', url })
          },
          onError: (err) => {
            resolve({ status: 'fail', err: err.error })
          }
        })
      })
    },

    uploadFilePromiseMPWeixin(uploadObj) {
      const imgTypeArr = uploadObj.thumb.split('.');
      const imgType = imgTypeArr[imgTypeArr.length - 1];
      return new Promise((resolve) => {
        this.cos.putObject({
          filePath: uploadObj.url,
          fileType: imgType,
          onSuccess: (url) => {
            resolve({ status: 'success', url })
          },
          onError: (err) => {
            resolve({ status: 'fail', err: err.error })
          }
        })
      })
    },

    async submit() {
      this.$refs.form.validate().then(async (res) => {
        if (this.formData.category) {
          this.loading.submit = true;
          this.isSubmitting = true;
          let { code, data } = await this.$api.WorkOrder.matterReportAdd(this.formData);
          // console.log('code == ', code);
          // console.log('data == ', data);
          this.loading.submit = false
          setTimeout(() => {
            this.isSubmitting = false;
          }, 500);
          if (code !== 200) return false
          this.$message.showToast('报事报修成功');
          this.$refs.form.resetFields();
          this.fileList = [];
          this.formData.attachList = [];
          this.formData.category = 0;
          this.categoryName = '';
          setTimeout(() => {
            // WorkOrderList
            uni.$u.route({
              type: 'redirect',
              url: this.$utils.getRoutePath('WorkOrderList')
            })
          }, 300);
        } else {
          uni.$u.toast('请选择类别')
        }
      }).catch(errors => {
        console.log('errors === ', errors);
        uni.$u.toast('校验失败')
      })
    },

    closePopup() {
      this.showSelectRoom = false;
    },
    openPopUp() {
      this.classroomName = '';
      this.$refs.popupSelect.clearPopdata();
    },
    // 选择教室类别点击方法
    popSelectClick(form) {
      Object.assign(this.formData, { ...form });
      this.classroomName = `${(this.buildingList.find(v => v.id === form.building) || {}).name}` +
        `${(this.floorList.find(v => v.id === form.floor) || {}).name}`
        + ` ${this.classroomList.find(v => v.id === form.roomType).name}`
        + ` 房间号：` + form.roomNo;
      this.closePopup();
    },
    // 选择单元/楼层（弹框放在popupSelect外层，不然显示不全）
    showChooseBuilding() {
      this.show.building = true;
    },
    // 选择房间类别
    showChooseRoomType() {
      this.show.roomType = true;
    },
    // 滑动到底部监听
    scrollToBottom() {
      this.$refs.popupSelect.onScrollToBottom();
    },
    cancelBuilding() {
      this.show.building = false
    },
    confirmBuilding({ value }) {
      this.$refs.popupSelect.confirmBuilding(value);
      this.cancelBuilding();
    },
    cancelType() {
      this.show.roomType = false
    },
    confirmType({ value }) {
      this.$refs.popupSelect.confirmType(value);
      this.cancelType()
    },
  },

}
</script>

<style lang="scss" scoped>
.popupStyle {
  max-height: 900px;
  overflow: auto;
}
</style>