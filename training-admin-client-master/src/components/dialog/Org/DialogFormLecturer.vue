<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="800px" class="popover-dialog">

    <div class="flex center-center">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="medium">

        <!--讲师创建、编辑-->

        <template v-if="dialogType.startsWith('Lecturer')">

          <el-form-item class="margin-bottom-20" prop="lecturerName" label="讲师姓名">
            <el-input class="width-400" type="text" v-model.trim="formData.lecturerName" show-word-limit
                      maxlength="10" placeholder="输入讲师姓名"></el-input>
            <div class="padding-left-12 text-9 line-height-18 margin-top-8">讲师姓名仅用于学员端展示</div>
          </el-form-item>

          <el-form-item prop="bindUser" label="关联账号">
            <div class="flex">
              <el-input class="width-400 disabled-input" type="text" v-model.trim="formData.bindUser"
                        :disabled="true" placeholder="关联账号">
                <div slot="suffix">
                  <div v-if="!formData.bindUser" class="text-main pointer font-14 padding-right-10" @click="onBindUser">
                    选择
                  </div>
                  <div v-else class="text-9 pointer font-14 padding-right-10" @click="onUnBindUser">
                    <span class="el-icon-circle-close"></span>
                  </div>
                </div>
              </el-input>
              <div v-if="formData.bindUser" class="text-main pointer font-14 padding-left-12" @click="onBindUser">
                更改
              </div>
            </div>
            <div class="padding-left-12 text-9 line-height-20 margin-top-8 width-400">
              关联讲师账号后，学员可点击讲师简介跳转讲师个人主页，查看讲师的课程和直播
            </div>
          </el-form-item>

          <el-form-item prop="title" label="讲师头衔">
            <el-input class="width-400" type="text" v-model.trim="formData.title" :maxlength="20"
                      show-word-limit placeholder="输入头衔"></el-input>
          </el-form-item>

          <el-form-item prop="headImgUrl" label="讲师头像">
            <ImageUpload class="width-400" ref="ImageUpload" v-model="formData.headImgUrl"
                         isEdit :autoUpload="false" :width="150" :height="150"
                         :options="{
                          fixedNumber: [5, 4],
                          autoCropWidth: 1920,
                          autoCropHeight: 1080,
                        }">
              <div slot="tips" slot-scope="{data}" class="font-12 text-6 margin-left-12">
                请选择 500 * 400 的图片, 支持 {{ data.accept }} 格式图片，大小不超过 {{ data.name }}
              </div>
            </ImageUpload>
          </el-form-item>

          <el-form-item prop="intro" label="讲师简介">
            <el-input class="width-400" type="textarea" rows="4" show-word-limit maxlength="300"
                      v-model.trim="formData.intro"></el-input>
          </el-form-item>

          <el-form-item prop="enabled" label="显示状态">
            <el-radio-group v-model="formData.enabled">
              <el-radio v-for="item in showStatus" :key="item.id" :label="item.id">
                {{ item.name }}
              </el-radio>
            </el-radio-group>
            <div class="font-13 text-9">提示：设置讲师是否显示在机构网站-名师展示</div>
          </el-form-item>

        </template>

      </el-form>
    </div>

    <template v-slot:footer>
      <el-button class="width-80 margin-right-6" type="primary" size="medium" @click="onSubmit"
                 :loading="loading.submit">确定
      </el-button>
      <el-button class="width-80" size="medium" @click="close">取消</el-button>
    </template>

    <DialogTableSelectUser ref="DialogTableSelectUser"></DialogTableSelectUser>
  </el-dialog>
</template>

<script>
import ImageUpload from '@/components/form/ImageUpload'
import DialogTableSelectUser from '@/components/dialog/Org/DialogTableSelectUser'
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'DialogFormLecturer',
  components: {
    ImageUpload,
    DialogTableSelectUser
  },
  data() {
    let defaultFormData = {
      phone: null,
      lecturerName: null,
      realName: null,
      title: null,
      enabled: 1,
      headImgUrl: null,
      intro: null
    }
    let {required} = this.$rules
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'LecturerCreate',
      typeMapping: {
        LecturerCreate: {title: '新增讲师', submitFn: this.$api.Lecturer.create, search: true, type: 'Create'},
        LecturerEdit: {title: '编辑讲师', submitFn: this.$api.Lecturer.update, search: false, type: 'Edit'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        lecturerName: [required],
        title: [required],
        headImgUrl: [required],
        intro: [required]
      },

      loading: {
        submit: false
      }
    }
  },
  computed: {
    ...mapGetters({
      showStatus: 'common/showStatus'
    }),
    ...mapState('config', {
      initPassword: 'initPassword'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    submitFormData() {
      return {
        ...this.formData
      }
    }
  },

  methods: {
    reset() {
      this.formData = {...this.defaultFormData}
      this.$refs.form && this.$refs.form.resetFields()
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset()

      this.visible = true

      this.dialogType = data.type

      if (/Edit$/.test(this.dialogType)) {
        let {code, data: d} = await this.$api.Lecturer.detail({
          id: data.formData.id
        })
        if (code !== 200) return false
        let bindUser = null
        if (d.realName) bindUser = `${d.realName}-${d.phone}`
        this.formData = {
          ...d,
          password: this.initPassword,
          bindUser
        }
      }
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate()
      this.formData.headImgUrl = await this.$refs.ImageUpload.uploadFile()
      this.loading.submit = true
      let {code} = await this.dialogInfo.submitFn(this.submitFormData)
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type]('讲师')
      this.$emit('success')

      this.close()
    },
    async onBindUser() {
      let data = await this.$refs.DialogTableSelectUser.open({
        type: 'BindUser'
      })
      this.$set(this.formData, 'bindUser', `${data.realName}-${data.phone}`)
      this.formData.realName = data.realName
      this.formData.phone = data.phone
      this.formData.userId = data.userId
      this.formData.employeeId = data.id
    },
    onUnBindUser(){
      this.formData.bindUser = null
      this.formData.realName = null
      this.formData.phone = null
      this.formData.userId = null
      this.formData.employeeId = null
    }
  }
}
</script>

<style scoped lang="stylus">
.el-form-item.margin-bottom-20
  margin-bottom 20px

.disabled-input
  >>> .el-input__inner
    background-color NEUTRAL_COLOR_F
    cursor default

>>> .el-dialog
  min-height: 800px
</style>