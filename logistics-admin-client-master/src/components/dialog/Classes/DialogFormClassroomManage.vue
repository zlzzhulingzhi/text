<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
    :before-close="close" width="660px">

    <!--上传图片通用组件-->
    <div class="height-0 overflow">
      <ImageUpload ref="ImageUpload" isEdit :autoUpload="false" :width="120" :height="60" :options="{
        fixedNumber: [2, 1],
        autoCropWidth: 760,
        autoCropHeight: 380,
      }" @input="onSaveImage">
        <div slot="tips"></div>
      </ImageUpload>
    </div>


    <!--班级创建、编辑-->
    <template v-if="dialogType.startsWith('')">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

        <el-form-item prop="building" label="教室单元">
          <el-select class="width-300" v-model="formData.building" filterable>
            <el-option v-for="item in dormitoryUnit" :key="item.id" :label="item.dictValue" :value="item.dictKey">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="floor" label="楼层">
          <el-select class="width-300" v-model="formData.floor" filterable>
            <el-option v-for="item in dormitoryFloor" :key="item.id" :label="item.dictValue" :value="item.dictKey">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="roomNo" label="教室编号">
          <el-input class="width-300" v-model="formData.roomNo" show-word-limit
                    maxlength="6"></el-input>
        </el-form-item>
        
        <el-form-item prop="area" label="教室面积">
          <el-input-number class="width-300" controls-position="right" v-model="formData.area" :min="1" :max="999"></el-input-number>
        </el-form-item>

        <el-form-item prop="seats" label="教室座位数">
          <el-input-number class="width-300" v-model="formData.seats" controls-position="right" :min="1" :max="999">
          </el-input-number>
        </el-form-item>
        
        <el-form-item prop="roomType" label="教室类别">
          <el-select class="width-300" v-model="formData.roomType" filterable>
            <el-option v-for="item in classTypeInfo" :key="item.id" :label="item.dictValue" :value="item.dictKey">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="sceneDeviceId" label="设备">
          <el-select class="width-300" v-model="formData.sceneDeviceId" filterable multiple>
            <el-option v-for="item in sceneDeviceInfo" :key="item.id" :label="item.deviceName" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>


        <el-form-item prop="fileUrl" label="教室图片">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="onSelectFile()"
            :class="{ 'margin-bottom-8': formData.fileUrl.length !== 0 }" v-if="formData.fileUrl.length !== 6">上传图片
          </el-button>

          <div class="flex wrap start-start">
            <div v-for="(item, index) in formData.fileUrl" :key="index" class="relative">
              <el-image class="width-120 height-60 margin-right-8" :src="item.filePath"></el-image>

              <el-button class="close-btn" type="info" size="mini" circle icon="el-icon-close" @click="onDelete(index)">
              </el-button>
            </div>
          </div>
        </el-form-item>


        <el-form-item prop="remark" label="备注">
          <el-input class="width-300" controls-position="right" v-model="formData.remark" type="textarea"
            show-word-limit maxlength="100"></el-input>
        </el-form-item>


        <el-form-item>
          <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
            :loading="loading.submit">保存
          </el-button>
          <el-button class="width-80" size="medium" @click="close">取消</el-button>
        </el-form-item>
      </el-form>
    </template>
  </el-dialog>
</template>

<script>
import { mapGetters } from 'vuex'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'
export default {
  name: 'DialogFormClassroomManage',
  mixins: [mxBaseDialog],
  data() {
    let { required } = this.$rules
    let defaultFormData = {
      building: null,
      floor: null,
      fileUrl: [],
      remark: '',
      sceneDeviceId: [],
      roomNo: '',
      roomType: '',
      seats: null,
      areaUnit: 'm2',
      area: null
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'Create',
      typeMapping: {
        Create: { title: '新增教室', submitFn: this.$api.Classroom.classroomAdd, type: 'Create' },
        Edit: { title: '编辑教室', submitFn: this.$api.Classroom.classroomUpdate, type: 'Edit' }
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        building: [required],
        floor: [required],
        roomType: [required],
        roomNo: [required],
        seats: [required],
        area: [required],
      },
      loading: {
        submit: false
      },
      sceneDeviceInfo: [],
      classroomPictureInfo: {
        width: 120,
        height: 120
      },
      max: 6
    }
  },
  computed: {
    ...mapGetters({
      classTypeInfo: 'common/classTypeInfo',
      dormitoryUnit: 'common/dormitoryUnit',
      dormitoryFloor: 'common/dormitoryFloor',
      areaUnit: 'common/areaUnit'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    }
  },

  methods: {
    // 获取设备数据
    async getSceneDevice() {
      let { code, data } = await this.$api.SceneDevice.sceneDeviceList({
        category: 'Classroom'
      })
      if (code !== 200) return false
      this.sceneDeviceInfo = data
    },
    // 操作 - 初始化数据
    async initData(data) {
      await this.getSceneDevice();
      this.dialogType = data.type || 'Create'
      if (this.dialogType === 'Create') {
        return false
      }

      if (this.dialogType === 'Edit') {
        let { code, data: d } = await this.$api.Classroom.classroomDetail({
          id: data.formData.id
        })
        if (code !== 200) return false
        this.formData = {
          ...d,
          fileUrl: d.fileUrl.map(item => {
            return {
              filePath: item
            }
          })
        }
      }
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true

      // 上传图片
      for (const ele of this.formData.fileUrl) {
        if (ele.context) {
          ele.filePath = await this.$refs.ImageUpload.toolUploadFile(ele.context)
          delete ele.context
        }
      }

      let { code } = await this.dialogInfo.submitFn({
        ...this.formData,
        fileUrl: this.formData.fileUrl.map(item => item.filePath || '')
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.formData.code)

      this.$emit('success')
      this.close()
    },
    // 操作 - 调用上传图片
    onSelectFile() {
      this.$refs.ImageUpload.onSelectFile()
    },
    // 操作 - 保存图片回调
    onSaveImage(image) {
      this.formData.fileUrl.push({
        filePath: image,
        context: {
          name: this.$refs.ImageUpload.oFile?.name || '',
          Body: this.$refs.ImageUpload.fileBlob
        }
      })
    },
    onDelete(index) {
      this.formData.fileUrl.splice(index, 1)
    }
  }
}
</script>

<style scoped lang="stylus">

// 教室图片  悬浮展示
.relative
  .close-btn
    position absolute
    right 12px
    top 3px
    padding 4px
    display none

  &:hover
    .close-btn
      display block
</style>