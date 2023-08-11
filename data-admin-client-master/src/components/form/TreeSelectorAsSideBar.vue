<template>
  <el-aside class="bg-f margin-right-16 padding-top-16 font-14 overflow wrapper" :class="{hide: !visibleInner}" :width="`${visibleInner? width : 0}px`" v-if="paneInfo" v-loading="loading.tree">

    <div class="height-100p flex column" :style="{width: `${width}px`}">

      <!--搜索框-->
      <div class="flex padding-left-16 padding-right-8 padding-bottom-6">
        <el-input size="small" :placeholder="paneInfo.searchText || '分类查询'" suffix-icon="el-icon-search" v-model="searchValue" clearable @input="onSearch"></el-input>
        <el-button type="text" :title="isExpandAll ? '收起全部二级分类' : '展开全部二级分类'" @click.stop="onExpandAll" v-if="paneInfo.style === 'simple'">
          <iconpark-icon v-if="isExpandAll" name="fold-up-one"></iconpark-icon>
          <iconpark-icon v-else name="drop-down-list"></iconpark-icon>
        </el-button>
        <template v-else>
          <div v-if="!paneInfo.hideClose" class="font-16 el-icon-s-fold text-main padding-8 padding-right-0 pointer" @click="close"></div>
        </template>
      </div>

      <div class="overflow-auto flex-1">
        <!--收起或展示全部二级分类-->
        <div class="padding-left-8 line-height-44 pointer tree-item flex start-center" :class="{active: selection === null}" @click="onSelect()" v-if="paneInfo.style !== 'simple'">
          <b>{{ paneInfo.title }}</b>
          <b v-if="Number.isSafeInteger(total)">({{ total }})</b>
          <el-button type="text" :title="isExpandAll ? '收起全部二级分类' : '展开全部二级分类'" @click.stop="onExpandAll">
            <iconpark-icon v-if="isExpandAll" name="fold-up-one"></iconpark-icon>
            <iconpark-icon v-else name="drop-down-list"></iconpark-icon>
          </el-button>

          <div class="text-right flex-1">
            <el-button type="text" v-if="paneInfo.EditRoute" icon="el-icon-s-tools" title="管理" @click.stop="$router.push(paneInfo.EditRoute)">
            </el-button>
          </div>
        </div>

        <!--未选择分类-->
        <div class="padding-left-24 line-height-44 pointer tree-item text-6" v-if="paneInfo.emptyChild" :class="{active: selection&&selection.id === -1}" @click="onSelect(emptyItem)">

          <el-tooltip effect="light" v-model="emptyItem.visible" @mouseenter.native="onEnterTips(emptyItem)" @mouseleave.native="onLeaveTips(emptyItem)" manual :enterable="false">
            <div>{{ paneInfo.emptyChild }}</div>

            <div class="flex start-center" slot="content">
              <div>{{ paneInfo.emptyChild }}</div>
              <div v-if="paneInfo.countAPI">
                <div v-if="emptyItem.countInner === undefined" class="el-icon-loading margin-left-4"></div>
                <div v-else>({{ emptyItem.countInner }})</div>
              </div>
            </div>
          </el-tooltip>
        </div>

        <el-tree ref="tree" :data="treeList" node-key="id" class="tree-style-main overflow-auto" :props="props" :filter-node-method="filterNode" :default-expanded-keys="defaultExpandedList" highlight-current @node-click="onSelect" @node-expand="onNodeExpand" @node-collapse="onNodeExpand" :expand-on-click-node="false">
          <template slot-scope="{ data }">
            <el-tooltip effect="light" v-model="data.visible" @mouseenter.native="onEnterTips(data)" @mouseleave.native="onLeaveTips(data)" manual :enterable="false" :disabled="!paneInfo.countAPI">
              <div class="flex start-center width-100p overflow">
                <div class="text-ellipsis height-100p line-height-44 flex-1">
                  <span class="rich-text" v-html="$xss((data[props.label] || '').replace(new RegExp(`(${searchValue})`,'g'), `<b class='text-main'>$1</b>`))"></span>
                </div>
                <iconpark-icon class="margin-right-12" name="jinzhiforbid" color="#ccc" v-if="!data.enabled"></iconpark-icon>
              </div>

              <div class="flex start-center" slot="content">
                <div>{{ data[props.label] }}</div>
                <div v-if="paneInfo.countAPI">
                  <div v-if="data.countInner === undefined" class="el-icon-loading margin-left-4"></div>
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

export default {
  name: 'TreeSelectorAsSideBar',
  created () {
    this.getTreeList()
     let str = new RegExp(`(${this.searchValue})`,'g');
  },
  data () {
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
        //
        WCourse: {
          title: '全部课程分类',
          dataAPI: this.$api.WCategory.tree,
          params: {
            businessCode: 'businessCourse',
            orgId: -1
          },
          countAPI: null
        },

        // 学员管理
        OrgStudent: {
          title: '全部组织',
          dataAPI: this.$api.Dept.treeList,
          params: {
            parentId: 0
          },
          countAPI: this.$api.Student.categoryCount,
          searchText: '组织查询',
          EditRoute: {
            name: 'Dept'
          },
          emptyChild: '未选择组织'
        },
        // 后台用户
        OrgEmployee: {
          title: '全部组织',
          dataAPI: this.$api.Dept.treeList,
          params: {
            parentId: 0
          },
          countAPI: this.$api.Employee.categoryCount,
          searchText: '组织查询',
          EditRoute: {
            name: 'Dept'
          }
        },
        // 机构员工
        AllEmployeeDept: {
          title: '全部组织',
          dataAPI: this.$api.Organization.orgDeptTree,
          params: {
            parentId: 0
          },
          countAPI: this.$api.Dept.employeeCount,
          searchText: '组织查询',
          countKey: 'peopleCountTotal'
        },
        // 机构学员
        AllStudentDept: {
          title: '全部组织',
          dataAPI: this.$api.Organization.orgDeptTree,
          params: {
            parentId: 0
          },
          countAPI: this.$api.Dept.studentCount,
          searchText: '组织查询',
          countKey: 'studentCountTotal'
        },
        // 组织批量添加课程/培训
        // OrgDept: {
        //   title: '全部组织',
        //   dataAPI: this.$api.Dept.treeList,
        //   params: {
        //     parentId: 0
        //   },
        //   countAPI: (...arg) => (this.category === 'Course' ? this.$api.Course.deptCourseCount(...arg) : this.$api.Task.deptTaskCount(...arg)),
        //   searchText: '组织查询',
        //   style: 'simple'
        // },
        // 标签批量添加课程/培训
        // OrgGroup: {
        //   title: '全部标签',
        //   dataAPI: this.$api.labelPage.listAll,
        //   params: {
        //     parentId: 0
        //   },
        //   countAPI: (...arg) => (this.category === 'Course' ? this.$api.Course.groupCourseCount(...arg) : this.$api.Task.groupTaskCount(...arg)),
        //   searchText: '标签查询',
        //   style: 'simple'
        // }
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
      default () {
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
    category: {
      type: String,
    },
    visible: {
      type: Boolean,
      default: true
    }
  },

  computed: {
    paneInfo () {
      return this.typeMapping[this.type]
    }
  },

  watch: {
    visible (visible) {
      this.visibleInner = visible
    },
    searchValue(val){
      console.log("val",val);
      this.$refs.tree.filter(val);
    }
  },

  methods: {
    open () {
      this.visibleInner = true
      this.$emit('update:visible', this.visibleInner)
    },
    close () {
      this.visibleInner = false
      this.$emit('update:visible', this.visibleInner)
    },
    // 过滤
    filterNode (value, data) {
      if (!value) return true;
      console.log("..........................",data[this.props.label]);
      console.log("..........................",value);
      // console.log(",..............................",this.props.label);
      // console.log("过滤",data[this.props.label].includes(value.trim()));
      // this.props.label 为 name 
      return data[this.props.label].indexOf(value.trim()) !== -1
      
    },

    async getTreeList () {
      this.searchValue = null
      this.loading.tree = true
      let { data, code } = await this.paneInfo.dataAPI(this.paneInfo.params)
      this.loading.tree = false
      if (code !== 200) return false

      // 特殊处理
      if (/^(AllEmployeeDept|AllStudentDept)$/.test(this.type)) {
        data = data.map(item => {
          return {
            ...item,
            id: item.orgId + '-org'
          }
        })
      } else if (/^(OrgGroup)$/.test(this.type)) {
        data = data.map(item => {
          return {
            ...item,
            id: item.groupId
          }
        })
      }

      let treeList = data.children || data.deptList || data
      this.treeList = treeList
      // 默认选中第一项
      if (/^(OrgDept|OrgGroup)$/.test(this.type)) {
        this.$nextTick(() => {
          this.onSelect(this.treeList[0])
          this.$refs.tree.setCurrentKey(this.treeList[0].id)
        })
      }
      this.emptyItem = { ...treeList[0], id: -1 }
      this.defaultExpandedList = this.treeList.map(item => item.id)
      this.total = data.quantity
      this.$emit('update:treeList', this.treeList)
    },


    // 操作 - 搜索节点
    onSearch (val) {
      console.log(val);
      this.$refs.tree.filter(val)
    },

    // 操作 - 选择节点
    onSelect (val = null, node) {
      // “未选择分类”有前台生成和后台返回之分，后台返回是放在el-tree里面的
      if (!val || (!node && val.id === -1)) this.$refs.tree.setCurrentKey(null)
      // if(val.id !== -1 && val.enabled) return false
      this.selection = val
      this.$emit('update:selection', val)
      this.$emit('update:selectId', val && val.id)
      if (node && node.data.parentId) {
        this.$emit('update:selections', [node.data.parentId, val.id])
      } else {
        this.$emit('update:selections', val ? [val.id] : [])
      }
    },

    // 操作 - 展开/收起全部节点
    onExpandAll () {
      this.isExpandAll = !this.isExpandAll
      this.$refs.tree.store.root.childNodes.forEach(node => {
        node.expanded = this.isExpandAll
      })
    },

    onNodeExpand () {
      this.$nextTick(() => {
        this.isExpandAll = this.$refs.tree.store.root.childNodes.every(node => node.expanded)
      })
    },

    onEnterTips (data) {
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
        let { code, data: num } = await this.paneInfo.countAPI({
          idList,
          orgId,
          ...(this.paneInfo.countParams || {})
        })
        if (code !== 200) return false
        this.$set(data, 'countInner', num)
      }, 300)
    },

    onLeaveTips (data) {
      data.visible = false
    }
  }
}

</script>

<style scoped lang="stylus">
.wrapper
  transition width 0.3s
  &.hide
    margin 0
  .tree-item
    &.active, &:hover
      background-color BACKGROUND_COLOR
      color MAIN_COLOR
.el-button--text
  margin-left 5px
</style>