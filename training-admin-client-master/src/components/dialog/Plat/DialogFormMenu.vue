<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="600px">

    <div v-if="isDev">
      {{ submitFormData }}
    </div>

    <!--菜单创建、编辑-->
    <template v-if="dialogType.startsWith('Menu')">
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px" size="small">

        <el-form-item prop="innerParentId" label="上级菜单">
          <TreeSelect class="width-300" v-model="formData.innerParentId" :options="treeMenu"
                      :normalizer="normalizer"
                      :clearable="false" :defaultExpandLevel="Infinity"
          ></TreeSelect>
        </el-form-item>

        <el-form-item prop="type" label="菜单类型">
          <el-radio-group v-model="formData.type">
            <el-radio v-for="item in menuType" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="name" label="菜单名称">
          <el-input class="width-300" type="text" show-word-limit maxlength="10"
                    v-model.trim="formData.name"></el-input>
        </el-form-item>

        <el-form-item prop="sort" label="排序">
          <el-input-number class="width-300" controls-position="right" v-model="formData.sort"
                           :min="0"></el-input-number>
        </el-form-item>

        <el-form-item prop="iconUrl" label="图标">
          <el-input class="width-300" type="text" show-word-limit maxlength="100"
                    v-model.trim="formData.iconUrl"></el-input>
        </el-form-item>

        <el-form-item prop="uri" label="路由地址">
          <el-input class="width-300" type="text" show-word-limit maxlength="100"
                    v-model.trim="formData.uri"></el-input>
        </el-form-item>

        <el-form-item prop="permission" label="权限标识">
          <el-input class="width-300" type="text" show-word-limit maxlength="100"
                    v-model.trim="formData.permission" :disabled="initCategory.test(formData.category)"></el-input>
        </el-form-item>

        <el-form-item prop="enabled" label="菜单状态">
          <el-radio-group v-model="formData.enabled" :disabled="initCategory.test(formData.category)">
            <el-radio v-for="item in enabled" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="remark" label="备注">
          <el-input class="width-300" type="textarea" rows="3" show-word-limit maxlength="100"
                    v-model="formData.remark"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button class="width-80 margin-right-14" type="primary" size="medium" @click="onSubmit"
                     :loading="loading.submit">保存
          </el-button>
          <el-button class="width-80" size="medium" @click="close">取消</el-button>
        </el-form-item>
      </el-form>
    </template>

    <!--    <template v-slot:footer>
          <el-button class="margin-right-14" size="small" @click="close">取消</el-button>
          <el-button type="primary" size="small" @click="onSubmit">确定</el-button>
        </template>-->
  </el-dialog>
</template>

<script>
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'DialogFormMenu',
  data() {
    let {required} = this.$rules
    let defaultFormData = {
      id: null,
      parentId: null,
      type: 1,
      name: null,
      sort: null,
      iconUrl: null,
      uri: null,
      permission: null,
      enabled: 1,
      remark: null,

      innerParentId: null
    }
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'MenuCreate',
      typeMapping: {
        MenuCreate: {title: '新增菜单', submitAPI: this.$api.Menu.create, type: 'Create'},
        MenuEdit: {title: '编辑菜单', submitAPI: this.$api.Menu.update, type: 'Edit'}
      },
      defaultFormData,
      formData: {
        ...defaultFormData
      },
      rules: {
        innerParentId: [required],
        name: [required],
        permission: [required]
      },
      treeMenu: [],
      loading: {
        submit: false
      }
    }
  },
  computed: {
    ...mapGetters({
      isDev: 'isDev',
      enabled: 'common/enabled',
      menuType: 'common/menuType'
    }),
    ...mapState('config', {
      initCategory: 'initCategory'
    }),
    treeMenuFlat() {
      return this.$utils.ArrayFlat(this.treeMenu || [])
    },
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    submitFormData() {
      let {innerParentId, appId} = this.formData
      let parentId = Number(innerParentId) || 0
      appId = (this.treeMenuFlat.find(item => item.id === innerParentId) || {}).appId
      return {
        ...this.formData,
        innerParentId: undefined,
        parentId,
        appId
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
      this.treeMenu = data.treeMenu

      this.dialogType = data.type

      this.formData = {
        ...this.defaultFormData,
        ...data.formData,
        innerParentId: data.formData.parentId || `app-${data.formData.appId}`
      }
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      await this.$refs.form.validate()
      this.loading.submit = true

      let {code} = await this.dialogInfo.submitAPI(this.submitFormData)
      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type](this.formData.name)
      this.$emit('success')

      this.close()
    },
    normalizer(node) {
      if (node.id === this.formData.id && this.dialogInfo.type === 'Edit') return {
        children: undefined
      }

      return {
        id: node.id,
        label: node.menuName,
        children: node.children && node.children.length ? node.children : undefined
      }
    }
  }
}
</script>

<style scoped lang="stylus">
</style>