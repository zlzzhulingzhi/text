<template>
  <div class="font-14" :class="{border, h5: type === 'H5'}">

    <div class="nav-tree overflow-auto">
      <slot v-if="header" name="header">
        <div class="tree-header flex between-center text-bold">
          <!--标题-->
          <div class="col-name">导航名称</div>
          <!--映射页面-->
          <div class="col-link">跳转链接</div>
          <!--默认图标-->
          <div v-if="/^(H5)$/.test(type)" class="col-icon">默认图标</div>
          <!--选中图标-->
          <div v-if="/^(H5)$/.test(type)" class="col-icon">选中图标</div>
          <!--操控区域-->
          <div class="col-handle">操作</div>
        </div>
      </slot>

      <el-tree :data="value"
               draggable
               :allow-drop="allowDrop"
               :allow-drag="allowDrag"
               @node-drop="onNodeDrop"
               default-expand-all>
        <div slot-scope="{node,data}" class="flex between-center width-100p tree-row">

          <!--1. 展示模式（可拖拽）-->
          <template v-if="editItem.id !== data.id">
            <!--标题-->
            <div class="col-name">{{ data.navbarName }}</div>

            <!--映射页面-->
            <div class="col-link">
              <template v-if="data.linkType === 1 || data.linkType === 3">{{ data.linkUrl }}</template>
              <template v-else-if="type === 'H5'">微页【{{ data.linkId | waH5Pages('pageName', data.linkUrl) }}】</template>
              <template v-else>微页【{{ data.linkId | waOrgPages('pageName', data.linkUrl) }}】</template>
            </div>

            <!--默认图标-->
            <div v-if="/^(H5)$/.test(type)" class="col-icon">
              <!-- <UploadImage v-model="data.iconUrl" isEdit
                           :width="40" :height="40" :options="{
                             autoCropWidth: 80,
                             autoCropHeight: 80,
                             fixed: false,
                             fixedBox: true,
                             centerBox: false
                           }"
                           @input="$api.Navbar.update(data)">
                <div slot="tips"></div>
              </UploadImage> -->
              <el-image class="width-40 height-40" :src="data.iconUrl">
                <template slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </template>
              </el-image>
            </div>

            <!--选中图标-->
            <div v-if="/^(H5)$/.test(type)" class="col-icon">
              <el-image class="width-40 height-40" :src="data.focusIconUrl">
                <template slot="error" class="image-slot">
                  <i class="el-icon-picture-outline center"></i>
                </template>
              </el-image>
              <!-- <UploadImage v-model="data.focusIconUrl" isEdit
                           :width="40" :height="40" :options="{
                             autoCropWidth: 80,
                             autoCropHeight: 80,
                             fixed: false,
                             fixedBox: true,
                             centerBox: false
                           }"
                           @input="$api.Navbar.update(data)">
                <div slot="tips"></div>
              </UploadImage> -->
            </div>

            <!--操控区域-->
            <div class="col-handle">
              <!-- <el-button class="text-main text-bold" type="text" icon="el-icon-rank" size="mini"
                         :disabled="isEditing || Boolean(!data.edited) && data.sort === 0">
                {{ type === 'H5' ? '' : '拖拽排序' }}
              </el-button>

              <el-button class="text-main text-bold" type="text" icon="el-icon-edit" size="mini"
                         @click.stop="onEdit(data,node)"
                         :disabled="Boolean(!data.edited) || isEditing">
                {{ type === 'H5' ? '' : '编辑' }}
              </el-button>

              <el-button class="text-main text-bold" type="text" icon="el-icon-delete" size="mini"
                         @click.stop="onDelete(data,node)" :disabled="Boolean(!data.edited) || loading.delete">
                {{ type === 'H5' ? '' : '删除' }}
              </el-button> -->

              <el-button v-if="type !== 'PC'" class="text-main text-bold" type="text" size="mini"
                         @click.stop="onIconSelect(data, node)">
                选择图标
              </el-button>

              <el-button class="text-main text-bold" type="text" size="mini"
                         :disabled="isEditing || Boolean(!data.edited) && data.sort === 0">
                拖拽排序
              </el-button>

              <el-button class="text-main text-bold" type="text" size="mini"
                         @click.stop="onEdit(data,node)"
                         :disabled="Boolean(!data.edited) || isEditing">
                编辑
              </el-button>

              <el-button class="text-main text-bold" type="text" size="mini"
                         @click.stop="onDelete(data,node)" :disabled="Boolean(!data.edited) || loading.delete">
                删除
              </el-button>
            </div>
          </template>


          <!--2. 编辑模式-->
          <template v-else>
            <!--标题-->
            <el-input class="col-name" v-model="editItem.navbarName" placeholder="导航名称" :maxlength="4"
                      show-word-limit size="small" ref="input">
            </el-input>

            <!--映射页面-->
            <div class="col-link">
              <!--1. 下拉选择-->
              <el-select v-model="editItem.linkUrl" size="small" class="width-120" @change="onLinkUrlChange">
                <el-option v-for="(item,index) in list2" :key="index" :label="item.navbarName"
                           :value="item.linkUrl"></el-option>
              </el-select>

              <!--2.2 自定义链接输入-->
              <el-input v-if="editItem.linkUrl === customUrl"
                        v-model="editItem.customLinkUrl" size="small"
                        placeholder="请输入自定义链接" class="width-240" maxlength="100" show-word-limit></el-input>

              <!--2.3 微页面-->
              <el-select v-else-if="editItem.linkUrl === wPageUrl"
                         v-model="editItem.wPageLinkId" size="small" class="width-240">
                <el-option v-for="(item,index) in wPages" :key="index" :label="item.pageName"
                           :value="item.id"></el-option>
              </el-select>

              <!--2.1 正常选项-->
              <el-button v-else size="small" class="width-240" @click="onSetName(editItem)">
                设置导航名称
              </el-button>

              <!-- <div v-else class="width-240"></div> -->
            </div>

            <!--默认图标-->
            <div v-if="/^(H5)$/.test(type)" class="col-icon">
              <el-image class="width-40 height-40" :src="data.iconUrl">
                <template slot="error" class="image-slot">
                  <i class="el-icon-picture-outline center"></i>
                </template>
              </el-image>
              <!-- <UploadImage ref="UploadIcon" v-model="editItem.iconUrl" isEdit :autoUpload="false"
                           :width="40" :height="40" :options="{
                             autoCropWidth: 80,
                             autoCropHeight: 80,
                             fixed: false,
                             fixedBox: true,
                             centerBox: false
                           }">
                <div slot="tips"></div>
              </UploadImage> -->
            </div>

            <!--选中图标-->
            <div v-if="/^(H5)$/.test(type)" class="col-icon">
              <el-image class="width-40 height-40" :src="data.focusIconUrl">
                <template slot="error" class="image-slot">
                  <i class="el-icon-picture-outline center"></i>
                </template>
              </el-image>
              <!-- <UploadImage ref="UploadFocusIcon" v-model="editItem.focusIconUrl" isEdit :autoUpload="false"
                           :width="40" :height="40" :options="{
                             autoCropWidth: 80,
                             autoCropHeight: 80,
                             fixed: false,
                             fixedBox: true,
                             centerBox: false
                           }">
                <div slot="tips"></div>
              </UploadImage> -->
            </div>

            <!--操控区域-->
            <div class="col-handle">
              <el-button v-if="type !== 'PC'" class="text-main text-bold" type="text" size="mini"
                         @click.stop="onIconSelect(data,node)">
                选择图标
              </el-button>
              <el-button class="text-main text-bold" type="text" size="mini"
                         :disabled="true">
                <!-- {{ type === 'H5' ? '' : '拖拽排序' }} -->
                拖拽排序
              </el-button>

              <el-button class="text-main text-bold" type="text" size="mini"
                         @click.stop="onSave(data,node)">
                <!-- {{ type === 'H5' ? '' : '保存' }} -->
                保存
              </el-button>

              <el-button class="text-main text-bold" type="text" size="mini"
                         @click.stop="onDelete(data,node)" :disabled="Boolean(!data.edited) || loading.delete">
                <!-- {{ type === 'H5' ? '' : '删除' }} -->
                删除
              </el-button>
            </div>
          </template>

        </div>
      </el-tree>
    </div>

    <div class="margin-top-12 text-right">
      <el-button type="primary" size="small" icon="el-icon-plus"
                 :disabled="isEditing" @click="onAdd()">添加导航
      </el-button>
    </div>
    <NavIconSelection ref="NavIconSelection"></NavIconSelection>
  </div>
</template>

<script>
import {mapActions, mapGetters} from 'vuex'
import NavIconSelection from './NavIconSelection.vue'

export default {
  name: 'NavTreeEdit',
  components: {NavIconSelection},
  activated() {
    this.editItem = {}
  },
  data() {
    return {
      editItem: {},
      customUrl: 'nav:custom',
      wPageUrl: 'nav:wpage',
      loading: {
        delete: false
      }
    }
  },
  props: {
    value: {
      type: Array,
      default() {
        return []
      }
    },
    list: {
      type: Array,
      default() {
        return []
      }
    },
    groupCode: {
      type: String,
      default: 'navbarOrg'
    },
    max: {
      type: Number,
      default: 10
    },
    border: {
      type: Boolean,
      default: false
    },
    header: {
      type: Boolean,
      default: true
    },
    type: {
      type: String,
      default: 'PC'
    }
  },
  computed: {
    ...mapGetters({
      waOrgPages: 'common/waOrgPages',
      waH5Pages: 'common/waH5Pages'
    }),
    wPages() {
      if (this.type === 'H5') return this.waH5Pages
      return this.waOrgPages
    },
    // 是否正在编辑
    isEditing() {
      return this.editItem.id !== undefined
    },
    list2() {
      console.log(this.list, '1111111111111111111111111111')
      // return this.list.slice(0, 10)
      for(let item of this.list) {
        if(item.navbarName === '课程中心') item.navbarName = '课程商城'
        if(item.navbarName === '我的任务') item.navbarName = '培训项目'

        if(item.navbarName === '我的课程') item.navbarName = '课程学习'

        if(item.navbarName === '课程列表') item.navbarName = '课程商城'
        if(item.navbarName === '考试列表') item.navbarName = '考试中心'
        if(item.navbarName === '我的培训') item.navbarName = '培训项目'
      }
      return this.list.slice(0, 10)
    }
  },
  methods: {
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),
    async onIconSelect(data) {
      let {iconUrl, focusIconUrl} = await this.$refs.NavIconSelection.open({
        type: 'AddCategory',
        selectionList: this.value
      })
      data.iconUrl = iconUrl
      data.focusIconUrl = focusIconUrl
      this.editItem.iconUrl = iconUrl
      this.editItem.focusIconUrl = focusIconUrl
      if (data.id) {
        await this.$api.Navbar.update(data).then()
      }
    },
    // 操作 - 添加导航
    onAdd(parent, node) {
      // 导航项 数据结构
      let child = {
        id: null,
        groupCode: this.groupCode,
        edited: 1,
        linkId: null,
        linkType: 1,
        linkUrl: this.list[0].linkUrl,
        navbarName: null,
        sort: this.value.length
      }

      if (/^(H5)$/.test(this.type)) {
        child.iconUrl = null
        child.focusIconUrl = null
      }

      if (this.value.length >= this.max) return this.$message.warning(`最多可设置${this.max}个导航`)
      this.value.push(child)
      this.onEdit(child)

      this.$nextTick(() => {
        try {
          this.$refs.input.$el.children[0].focus()
        } catch (e) {
        }
      })
    },
    // 操作 - 保存
    async onSave(data) {
      let editItem = this.editItem
      // 校验
      if (!editItem.navbarName) return this.$message.warning('请输入导航名称')
      if (!editItem.navbarName.trim()) return this.$message.warning('不能输入空格')

      // 自定义链接 - 校验
      if (editItem.linkUrl === this.customUrl) {
        if (!editItem.customLinkUrl) return this.$message.warning('请输入自定义链接')
        data.linkUrl = this.editItem.customLinkUrl
        data.linkId = null
      } else if (editItem.linkUrl === this.wPageUrl) {
        if (!editItem.wPageLinkId) return this.$message.warning('请选择微页')
        data.linkUrl = '/'
        data.linkId = this.editItem.wPageLinkId
      } else {
        data.linkUrl = this.editItem.linkUrl
        data.linkId = null
      }
      if (/^(H5)$/.test(this.type) && !this.editItem.iconUrl && !this.editItem.focusIconUrl) {
        let {iconUrl, focusIconUrl} = await this.$refs.NavIconSelection.open({
          type: 'AddCategory',
          selectionList: this.value
        })
        this.editItem.iconUrl = iconUrl
        this.editItem.focusIconUrl = focusIconUrl
      }


      for (const dataKey in data) {
        if (/^(linkUrl|linkId)$/.test(dataKey)) continue
        data[dataKey] = this.editItem[dataKey]
      }

      // 退出编辑模式
      this.editItem.id = undefined

      // 更新后台
      if (data.id) {
        this.$api.Navbar.update(data).then()
      } else {
        await this.$api.Navbar.create(data)
        this.$emit('update')
      }
    },

    // 操作 - 进入编辑模式
    onEdit(data) {
      let editItem = {...data}

      if (editItem.linkType === 2) {
        // 处理 - 微页
        editItem.linkUrl = this.wPageUrl
        editItem.wPageLinkId = data.linkId
      } else if (editItem.linkType === 3) {
        // 处理 - 自定义链接
        editItem.linkUrl = this.customUrl
        editItem.customLinkUrl = data.linkUrl
      }

      this.editItem = editItem
      this.editing = true
    },

    // 操作 - 删除
    async onDelete(data, node) {
      if (data.id) {
        this.loading.delete = true
        let {code} = await this.$api.Navbar.delete({
          idList: [data.id]
        })
        this.loading.delete = false
        if (code !== 200) return false
      }

      let parent = node.parent
      let children = parent.data.children || parent.data
      let index = children.findIndex(d => d.$treeNodeId === data.$treeNodeId)
      children.splice(index, 1)
      if (data.id === this.editItem.id) this.editItem.id = undefined
      this.onNodeDrop()
    },

    onLinkUrlChange(d) {
      if (d === this.wPageUrl) {
        this.editItem.linkType = 2
      } else if (d === this.customUrl) {
        this.editItem.linkType = 3
      } else {
        this.editItem.linkType = 1
      }
    },

    // 快捷设置名称
    onSetName(data) {
      let navbarName = this.list.find(item => item.linkUrl === data.linkUrl).navbarName
      this.$set(data, 'navbarName', navbarName)
    },

    allowDrop(draggingNode, dropNode, type) {
      if (dropNode.data.sort === 0) return false
      if (type === 'inner') return false
      return true
    },

    allowDrag(node) {
      return node.data.sort !== 0 && !this.isEditing
    },
    onNodeDrop() {
      this.value.forEach((item, sort) => {
        if (!sort) return false
        this.$api.Navbar.update({
          ...item,
          sort
        }).then()
      })
    }
  }
}
</script>

<style scoped lang="stylus">
.nav-tree
  >>> .el-tree
    > .el-tree-node
      &:focus .el-tree-node__content
        background-color transparent

      .el-tree-node__content
        width 100%
        height auto

      .el-tree-node__children
        border-radius 4px
        background-color BACKGROUND_COLOR

      .el-tree-node__expand-icon
        width 0
        padding 0

  >>> .el-input-group__append
    padding 0
    width 32px

    .el-button
      margin 0


.tree-header
  background-color GLOBAL_BACKGROUND_COLOR

.tree-row
  &:hover
    background-color GLOBAL_BACKGROUND_COLOR

    > div
      background-color GLOBAL_BACKGROUND_COLOR


.tree-header, .tree-row
  min-width 710px

  .col-name
    width 150px

  .col-link
    width 360px

  .col-handle
    width 200px

  .col-icon
    width 64px

  > div
    flex-shrink 0
    height 50px
    display flex
    align-items center
    padding-left 8px

.h5
  .tree-header, .tree-row
    min-width 656px

    .col-name
      width 120px

      >>> .el-input__inner
        padding-right 24px
        padding-left 12px

    .col-link
      width 320px

    .col-handle
      min-width 88px
      max-width 200px

.border
  .tree-header
    border 1px solid GLOBAL_BACKGROUND_COLOR
    border-bottom none

  >>> .el-tree-node
    .tree-row
      border 1px solid GLOBAL_BACKGROUND_COLOR

    & + .el-tree-node .tree-row
      border-top none
</style>