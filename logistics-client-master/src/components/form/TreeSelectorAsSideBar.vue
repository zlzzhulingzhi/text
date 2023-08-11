<template>
  <el-aside class="bg-f margin-right-16 padding-top-16 font-14 overflow wrapper"
            :class="{hide: !visibleInner}"
            :width="`${visibleInner? width : 0}px`"
            v-if="paneInfo"
            v-loading="loading.tree">

    <div class="height-100p flex column"
         :style="{width: `${width}px`}">    <!-- 控制侧边树的宽度，由父组件传过来的width决定 -->

      <!--搜索框-->
      <div class="flex padding-left-16 padding-right-8 padding-bottom-6">
        <el-input size="small"
                  :placeholder="paneInfo.searchText || '分类查询'"
                  suffix-icon="el-icon-search"
                  v-model="searchValue"
                  clearable
                  @input="onSearch"></el-input>
        <el-button type="text"
                   :title="isExpandAll ? '收起全部二级分类' : '展开全部二级分类'"
                   @click.stop="onExpandAll"
                   v-if="paneInfo.style === 'simple'">
          <iconpark-icon v-if="isExpandAll"
                         name="fold-up-one"></iconpark-icon>
          <iconpark-icon v-else
                         name="drop-down-list"></iconpark-icon>
        </el-button>
        <template v-else>
          <div v-if="!paneInfo.hideClose"
               class="font-16 el-icon-s-fold text-main padding-8 padding-right-0 pointer"
               @click="close"></div>
        </template>
      </div>

      <div class="overflow-auto flex-1">
        <!--全部-->
        <div class="padding-left-8 line-height-44 pointer tree-item flex start-center"
             :class="{active: selection === null}"
             @click="onSelect()"
             v-if="paneInfo.style === 'normal'">
          <b>{{ paneInfo.title }}</b>
          <b v-if="Number.isSafeInteger(total)">({{ total }})</b>
          <el-button type="text"
                     :title="isExpandAll ? '收起全部二级分类' : '展开全部二级分类'"
                     @click.stop="onExpandAll">
            <iconpark-icon v-if="isExpandAll"
                           name="fold-up-one"></iconpark-icon>
            <iconpark-icon v-else
                           name="drop-down-list"></iconpark-icon>
          </el-button>

          <div class="text-right flex-1">
            <el-button type="text"
                       v-if="paneInfo.EditRoute"
                       icon="el-icon-s-tools"
                       title="管理"
                       @click.stop="onBlank(paneInfo.EditRoute)">
            </el-button>
          </div>
        </div>

        <!--未选择分类-->
        <div class="padding-left-24 line-height-44 pointer tree-item text-6"
             v-if="paneInfo.emptyChild"
             :class="{active: selection&&selection.id === -1}"
             @click="onSelect(emptyItem)">

          <el-tooltip effect="light"
                      v-model="emptyItem.visible"
                      @mouseenter.native="onEnterTips(emptyItem)"
                      @mouseleave.native="onLeaveTips(emptyItem)"
                      manual
                      :enterable="false">
            <div>{{ paneInfo.emptyChild }}</div>

            <div class="flex start-center"
                 slot="content">
              <div>{{ paneInfo.emptyChild }}</div>
              <div v-if="paneInfo.countAPI">
                <div v-if="emptyItem.countInner === undefined"
                     class="el-icon-loading margin-left-4"></div>
                <div v-else>({{ emptyItem.countInner }})</div>
              </div>
            </div>
          </el-tooltip>
        </div>

        <el-tree ref="tree"
                 :data="treeList"
                 node-key="id"
                 class="tree-style-main overflow-auto"
                 :props="props"
                 :filter-node-method="filterNode"
                 :default-expanded-keys="defaultExpandedList"
                 highlight-current
                 @node-click="onSelect"
                 @node-expand="onNodeExpand"
                 @node-collapse="onNodeExpand"
                 :expand-on-click-node="false">
          <template slot-scope="{ data }">
            <el-tooltip effect="light"
                        v-model="data.visible"
                        @mouseenter.native="onEnterTips(data)"
                        @mouseleave.native="onLeaveTips(data)"
                        manual
                        :enterable="false"
                        :disabled="!paneInfo.countAPI">
              <div class="flex start-center width-100p overflow">
                <div class="text-ellipsis height-100p line-height-44 flex-1">
                  <span class="rich-text"
                        v-html="$xss(data[props.label].replace(new RegExp(`(${searchValue})`,'g'), `<b class='text-main'>$1</b>`))"></span>
                </div>
                <iconpark-icon class="margin-right-12"
                               name="jinzhiforbid"
                               color="#ccc"
                               v-if="!data.enabled"></iconpark-icon>
              </div>

              <div class="flex start-center"
                   slot="content">
                <div>{{ data.categoryName || data.name || data.groupName }}</div>
                <div v-if="paneInfo.countAPI">
                  <div v-if="data.countInner === undefined"
                       class="el-icon-loading margin-left-4"></div>
                  <div v-else>({{ data.countInner }})</div>
                </div>
              </div>
            </el-tooltip>
          </template>
        </el-tree>
      </div>
    </div>
  </el-aside>
</template>

<script>
import {mapActions} from 'vuex'

export default {
  name: 'TreeSelectorAsSideBar',
  created() {
    this.getDictionary(['deptTree']).then()
    this.getTreeList()

  },
  data() {
    return {
      visibleInner: true,

      total: 0,
      treeList: [],
      defaultExpandedList: [],
      isExpandAll: true,

      searchValue: null,

      // 选中对象
      selection: null,

      emptyItem: {
        id: -1
      },

      typeMapping: {
        // 班级
        Classes: {
          title: '全部班级',
          dataAPI: this.$api.Clazz.page,
          params: {
            parentId: 0
          },
          countAPI: null,
          searchText: '班级查询',
          style: '',
          hideClose: true
        }
      },

      loading: {
        tree: false
      },
      timer: {
        count: null
      }
    }
  },
  props: {
    width: {
      type: Number,
      default: 200
    },
    props: {
      type: Object,
      default() {
        return {
          label: 'name',
          children: 'children'
        }
      }
    },
    type: {
      type: String,
      required: true
    },
    visible: {
      type: Boolean,
      default: true
    }
  },

  computed: {
    paneInfo() {
      return this.typeMapping[this.type]    /* 此处的this.type就是从父组件传过来的，用以控制从typeMapping下的哪部分取值 */
    }
  },

  watch: {
    visible(visible) {
      this.visibleInner = visible
    }
  },

  methods: {
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),
    onBlank(route) {
      let newUrl = `${origin}/#${this.$router.match(route).fullPath}`
      window.open(newUrl, '_blank')
    },
    open() {
      this.visibleInner = true
      this.$emit('update:visible', this.visibleInner)
    },
    close() {
      this.visibleInner = false
      this.$emit('update:visible', this.visibleInner)
    },
    // 过滤
    filterNode(value, data) {
      if (value) return data[this.props.label].includes(value.trim())
      return true
    },

    async getTreeList() {

      this.searchValue = null
      this.loading.tree = true
      let {data, code} = await this.paneInfo.dataAPI(this.paneInfo.params)
      this.loading.tree = false
      if (code !== 200) return false

      let treeList = data.children || data.deptList || data.records || data
      this.treeList = treeList
      this.emptyItem = {...treeList[0], id: -1}
      this.defaultExpandedList = this.treeList.map(item => item.id)
      this.total = data.quantity
      this.$emit('update:treeList', this.treeList)
    },

    // 操作 - 搜索节点
    onSearch(val) {
      this.$refs.tree.filter(val)
    },

    // 操作 - 选择节点
    onSelect(val = null, node) {
      // console.log('00000000000000000000000000000000000000000000000000000000', Boolean(!val), Boolean(node), node)
      // let idList = []
      // let dataList = typeof val.id === 'string' ? [] : [val]
      // idList = this.$utils.ArrayFlat(dataList).map(item => item.id)

      // “未选择分类”有前台生成和后台返回之分，后台返回是放在el-tree里面的
      // if (!val || (!node && val.id === -1)) this.$refs.tree.setCurrentKey(null)
      // // if(val.id !== -1 && val.enabled) return false
      // this.selection = val
      // this.$emit('update:selection', val)
      // this.$emit('update:selectId', idList)
      // if (node && node.data.parentId) {
      //   this.$emit('update:selectId', [node.data.parentId, val.id])
      // } else {
      //   this.$emit('update:selectId', val ? [val.id] : [])
      // }
      // “未选择分类”有前台生成和后台返回之分，后台返回是放在el-tree里面的
      if (!val || (!node && val.id === -1)) {
        // 全部课程分类
        this.$refs.tree.setCurrentKey(null)
        this.$emit('update:selection', val)
        this.$emit('update:selections', null)
      } else {
        // 树节点分类
        let idList = []
        let nameList = []
        let dataList = typeof val.id === 'string' ? [] : [val]
        idList = this.$utils.ArrayFlat(dataList).map(item => item.id)
        nameList = this.$utils.ArrayFlat(dataList).map(item => item.deptName)
        // console.log('99999999999999999999999999999999999999999999999999999999',dataList, nameList, this.$utils.ArrayFlat(dataList), val)
        this.selection = val
        this.$emit('update:selection', val)
        // this.$emit('update:selectId', val && [val.id])
        this.$emit('update:selectId', idList)
        // this.$emit('update:selectId', idList)
        this.$emit('update:selectName', nameList)
        if (node && node.data.parentId) {
          this.$emit('update:selections', [node.data.parentId, val.id])
        } else {
          this.$emit('update:selections', val ? [val.id] : [])
        }
      }
      // if(val.id !== -1 && val.enabled) return false
    },

    // 操作 - 展开/收起全部节点
    onExpandAll() {
      this.isExpandAll = !this.isExpandAll
      this.$refs.tree.store.root.childNodes.forEach(node => {
        node.expanded = this.isExpandAll
      })
    },

    onNodeExpand() {
      this.$nextTick(() => {
        this.isExpandAll = this.$refs.tree.store.root.childNodes.every(
            node => node.expanded
        )
      })
    },

    onEnterTips(data) {
      this.$set(data, 'visible', true)
      if (!this.paneInfo.countAPI) return false

      let orgId = undefined
      let idList = []

      if (data.orgId) {
        // 机构+组织处理
        orgId = data.orgId
      }

      let dataList = typeof data.id === 'string' ? [] : [data]
      idList = this.$utils.ArrayFlat(dataList).map(item => item.id)
      clearTimeout(this.timer.count)
      this.timer.count = setTimeout(async () => {
        let {code, data: num} = await this.paneInfo.countAPI({
          // idList: this.paneInfo === 'OrgDept' ||  'OrgGroup' ? [idList[0]] : idList,
          idList: /^(OrgDept|OrgGroup)$/.test(this.type) ? [idList[0]] : idList,
          orgId,
          ...(this.paneInfo.countParams || {})
        })
        if (code !== 200) return false
        this.$set(data, 'countInner', num)
      }, 300)
    },

    onLeaveTips(data) {
      data.visible = false
    }
  }
}
</script>

<style scoped lang="stylus">
.wrapper {
  transition: width 0.3s;

  &.hide {
    margin: 0;
  }

  .tree-item {
    &.active, &:hover {
      background-color: BACKGROUND_COLOR;
      color: MAIN_COLOR;
    }
  }
}

.el-button--text {
  margin-left: 5px;
}
</style>