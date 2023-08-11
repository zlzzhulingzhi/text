<template>
  <div v-loading="loading.page">
    <el-breadcrumb class="margin-bottom-12" v-if="pageInfo.breadcrumbVisible">
      <el-breadcrumb-item v-for="item in $route.matched.slice(1)" :key="item.id">
        {{ item.meta.title }}
      </el-breadcrumb-item>
    </el-breadcrumb>

    <div class="bg-f radius-4 overflow">
      <div class="text-3 text-bold font-14 padding-16">
        机构基本信息
      </div>

      <el-form ref="form" class="padding-top-4" :model="formData" :rules="rules" size="medium"
               label-width="120px">

        <div class="overflow-auto margin-bottom-8 flex wrap around-start padding-16"
             :style="{height: $utils.FullViewHeight(pageInfo.fullHeight)}">

          <div>
            <el-form-item prop="companyName" label="公司名称">
              <el-input class="width-300" type="text" show-word-limit maxlength="100"
                        v-model.trim="formData.companyName" placeholder="请输入公司名称"></el-input>
            </el-form-item>

            <el-form-item prop="orgId" label="机构名称">
              <!--              <el-input class="width-300" type="text" show-word-limit maxlength="30"
                                      v-model.trim="formData.name" placeholder="请输入机构名称"></el-input>-->
              <div v-if="pageInfo.isEdit">
                {{ formData.name }}
              </div>
              <el-select v-else class="width-300" v-model="formData.orgId" @change="onOrgChange" filterable>
                <el-option v-for="item in orgBaseList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item prop="category" label="机构分类">
              <el-select class="width-300" v-model="formData.category">
                <el-option v-for="item in orgCategory" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item prop="pcLogoUrl" label="机构Logo(PC端)">
              <!--              <el-input class="width-300" type="text" show-word-limit maxlength="100"
                                      v-model.trim="formData.pcLogoUrl" placeholder="请输入图片链接"></el-input>-->

              <!-- <UploadImage class="width-300 wrap" :width="140" :height="40" v-model="formData.pcLogoUrl">
                <div slot="tips" slot-scope="{data}" class="font-12 text-6 margin-left-12 overflow">
                  请选择 140*40 的图片, 支持 {{ data.accept }} 格式图片，大小不超过 {{ data.name }}
                </div>
              </UploadImage> -->

              <ImageUpload class="width-300 wrap" ref="pcLogoUrl" v-model="formData.pcLogoUrl"
                           isEdit :autoUpload="false" :width="140" :height="40"
                           :options="{
                                fixedNumber: [7, 2],
                                autoCropWidth: 1920,
                                autoCropHeight: 1080,
                            }">
                <div slot="tips" slot-scope="{data}" class="font-12 text-6 margin-left-12 overflow">
                  请选择 140*40 的图片, 支持 {{ data.accept }} 格式图片，大小不超过 {{ data.name }}
                </div>
              </ImageUpload>
            </el-form-item>

            <el-form-item prop="h5LogoUrl" label="机构Logo(H5端)">
              <!--              <el-input class="width-300" type="text" show-word-limit maxlength="100"
                                      v-model.trim="formData.h5LogoUrl" placeholder="请输入图片链接"></el-input>-->


              <!-- <UploadImage class="width-300 wrap" :width="40" :height="40" v-model="formData.h5LogoUrl">
                <div slot="tips" slot-scope="{data}" class="font-12 text-6 margin-left-12 overflow">
                  请选择 40*40 的图片, 支持 {{ data.accept }} 格式图片，大小不超过 {{ data.name }}
                </div>
              </UploadImage> -->

              <ImageUpload class="width-300 wrap" ref="h5LogoUrl" v-model="formData.h5LogoUrl"
                           isEdit :autoUpload="false" :width="40" :height="40"
                           :options="{
                                fixed: false,
                                fixedBox: true,
                                autoCropWidth: 40,
                                autoCropHeight: 40,
                                original: true,
                                centerBox: false
                            }">
                <div slot="tips" slot-scope="{data}" class="font-12 text-6 margin-left-12 overflow">
                  请选择 40*40 的图片, 支持 {{ data.accept }} 格式图片，大小不超过 {{ data.name }}
                </div>
              </ImageUpload>
            </el-form-item>

            <el-form-item prop="domain" label="自定义域名">
              <el-input class="width-300 domain-input" type="text" show-word-limit :maxlength="30"
                        v-model.trim="formData.domain" placeholder="请输入二级域名">
                <span slot="suffix">{{ domain.primary }}</span>
              </el-input>
            </el-form-item>

            <!--            <el-form-item prop="templateId" label="企业模板">
                          <el-radio-group v-model="formData.templateId">
                            <el-radio v-for="item in orgTemplate" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
                          </el-radio-group>
                        </el-form-item>-->

            <el-form-item prop="intro" label="机构介绍">
              <el-input class="width-300" type="textarea" rows="6" show-word-limit maxlength="1000"
                        v-model="formData.intro"></el-input>
            </el-form-item>

          </div>

          <div>

            <el-form-item prop="sort" label="机构排序">
              <el-input-number class="width-300" controls-position="right" v-model="formData.sort"
                               :min="0"></el-input-number>
            </el-form-item>

            <el-form-item prop="enabled" label="使用状态">
              <el-radio-group v-model="formData.enabled">
                <el-radio v-for="item in enabled" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item prop="remark" label="备注">
              <el-input class="width-300" type="textarea" rows="6" show-word-limit maxlength="200"
                        v-model="formData.remark"></el-input>
            </el-form-item>


            <el-form-item prop="menuIdList" class="margin-bottom-0" label="机构权限">
              <!--{{ formData.menuIdList }}-->
              <PermissionTree v-model="formData.menuIdList" type="Org"
                              :disabled="!permissions.EditPermission"></PermissionTree>
            </el-form-item>
          </div>
        </div>

        <el-form-item>
          <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
                     :loading="loading.submit">保存
          </el-button>
          <el-button class="width-80" size="medium" @click="close">取消</el-button>
        </el-form-item>
      </el-form>

    </div>
  </div>
</template>

<script>
import {mapActions, mapGetters, mapState} from 'vuex'
import PermissionTree from '@/components/panes/PermissionTree'
import ImageUpload from '@/components/form/ImageUpload'

export default {
  name: 'PlatOrganizationCreate',
  components: {
    PermissionTree,
    ImageUpload
  },
  created() {
    if (Number.isSafeInteger(Number(this.$route.query.id))) {
      this.getDetail()
    } else {
      this.getOrgBaseList()
    }
  },
  data() {
    let {required} = this.$rules
    return {
      typeMapping: {
        Create: {submitFn: this.$api.Organization.create, breadcrumbVisible: true, fullHeight: 181, isEdit: false},
        Edit: {submitFn: this.$api.Organization.adminUpdate, breadcrumbVisible: false, fullHeight: 155, isEdit: true}
      },

      formData: {
        companyName: null,
        // 机构名称
        name: null,
        // 机构分类
        category: null,
        // PC端logo
        pcLogoUrl: null,
        // H5端logo
        h5LogoUrl: null,
        // 域名
        domain: null,
        // 企业模板
        templateId: 1,
        // 机构介绍
        intro: null,
        // 备注
        remark: null,
        sort: null,
        enabled: 1,
        menuIdList: []
      },
      rules: {
        companyName: [required],
        orgId: [required],
        domain: [
          {
            validator: (rule, value, callback) => {
              if (!/^[a-z0-9]{1,30}$/.test(value)) {
                return callback('二级域名只能使用小写英文字母、数字，且长度不超过30个字符')
              }
              callback()
            },
            trigger: 'change'
          }
        ]
      },

      orgBaseList: [],

      loading: {
        page: false,
        submit: false
      },
      timer: {
        employee: null
      }
    }
  },
  computed: {
    ...mapState('config', {
      domain: 'domain'
    }),
    ...mapGetters({
      enabled: 'common/enabled',
      orgCategory: 'common/orgCategory',
      orgTemplate: 'common/orgTemplate'
    }),
    pageInfo() {
      return this.typeMapping[this.$route.params.type]
    },
    permissions() {
      return this.$route.meta.childPermissions || {}
    }
  },
  watch: {
    orgCategory: {
      immediate: true,
      handler(val) {
        if (val && val[0]) this.formData.category = val[0].id
      }
    }
  },
  methods: {
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),
    // 获取公共机构列表
    async getOrgBaseList() {
      let {code, data} = await this.$api.Remote.orgPage({
        size: 999
      })

      if (code !== 200) return false

      this.orgBaseList = data.records
    },
    // 获取机构详情
    async getDetail() {
      this.loading.page = true
      let {code, data} = await this.$api.Organization.detailAdmin({
        id: this.$route.query.id
      })
      this.loading.page = false
      if (code !== 200) return false

      this.formData = {
        ...data
      }
    },
    close() {
      this.$router.back()
    },
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true
      this.formData.pcLogoUrl = await this.$refs.pcLogoUrl.uploadFile()
      this.formData.h5LogoUrl = await this.$refs.h5LogoUrl.uploadFile()
      let {code} = await this.pageInfo.submitFn({
        ...this.formData
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.$route.params.type](this.formData.name)
      this.$emit('success')
      this.close()
      this.getDictionary('organization').then()
    },

    onOrgChange(id) {
      let fOrg = this.orgBaseList.find(item => item.id === id)
      this.formData.name = fOrg.name
      this.formData.intro = fOrg.intro
    }
  }
}
</script>

<style scoped lang="stylus">
.domain-input
  >>> .el-input__inner
    padding-right 130px
</style>