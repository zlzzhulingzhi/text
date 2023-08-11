<template>
  <el-dialog :visible.sync="visible" append-to-body :title="title" :close-on-click-modal="false" :before-close="onClose"
    width="800px">

    <div class="flex center-center ">
      <div class="left-box">
        <div class="text-3 font-16 text-bold margin-bottom-22">组织</div>
        <el-tree ref="tree" :data="courseDeptTree" default-expand-all check-on-click-node node-key="id"
          :check-strictly="true" :expand-on-click-node="false" :props="{ children: 'children', label: 'deptName' }"
          @node-click="onCurrentChange">
          <div class="custom-tree-node flex between-center" slot-scope="{ data }">
            <div>
              <span class="el-icon-folder-opened margin-right-4" style="color: #1D61F2;"></span>
              <span class="font-14">{{ data.deptName }}</span>
            </div>
            <div v-if="selectionList.map(item => item.id).includes(data.id)" class="el-icon-check "></div>
          </div>
        </el-tree>
      </div>
      <div class="right-box">
        <div class="text-3 font-16 text-bold margin-bottom-22">已选组织</div>
        <el-tree ref="setree" :data="selectionList" default-expand-all check-on-click-node node-key="id"
          :check-strictly="true" :expand-on-click-node="false" :props="{ children: '', label: 'deptName' }">
          <div class="custom-tree-node flex between-center" slot-scope="{ node, data }">
            <div>
              <span class="el-icon-folder-opened margin-right-4" style="color: #1D61F2;"></span>
              <span class="font-14">{{ data.deptName }}</span>
            </div>
            <div class="el-icon-close remove" @click="() => remove(node, data)"></div>
          </div>
        </el-tree>
      </div>
    </div>

    <template v-slot:footer>
      <div class="text-center">
        <el-button class="width-80" size="small" @click="onClose">取消</el-button>
        <el-button class="width-80" type="primary" size="small" @click="onSubmit" :loading="loading.submit">确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import { mapGetters } from 'vuex'
import PaginationControl from '@/components/form/PaginationControl'

export default {
  name: 'DialogFormOrgDepartment',
  components: {
    PaginationControl
  },
  data() {
    return {
      visible: false, // 弹窗开关
      title: '',
      dialogType: 'AppCreate',
      typeMapping: {
        studentGroupTree: { treeList: this.studentGroupTree },
        courseGroup: { treeList: this.courseGroup },
      },

      formData: {
        groupName: '',
      },
      pagination: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      visibleUsers: [],
      tableData: [],
      selectionList: [],
      loading: {
        table: false,
        submit: false
      },
      timer: null
    }
  },
  computed: {
    ...mapGetters({
      //   orgRoleList: 'common/orgRoleList',
      deptTree: 'common/deptTree',
      courseDeptTree: 'common/courseDeptTree',
      studentGroupTree: 'common/studentGroupTree',
      courseGroup: 'common/courseGroup',
    }),
    params() {
      return {
        ...this.formData,
        current: this.pagination.pageNum,
        size: this.pagination.pageSize
      }
    },
    dialogInfo() {
      return this.typeMapping[this.dialogType] || {}
    }
  },
  watch: {
    visible() { 

    },
  },
  methods: {
    remove(node, data) {
      const parent = node.parent;
      const children = parent.data.children || parent.data;
      const index = children.findIndex(d => d.id === data.id);
      children.splice(index, 1);
      let newDeptList = this.selectionList.map(item => item.id)
      this.$refs.tree.setCheckedKeys(newDeptList);
    },
    onCurrentChange(val) {
      // console.log(val, "val");
      let list = this.$refs.tree.getCheckedNodes()
      let newList = list.map(item => {
        let { deptName, id, orgId } = item;
        let newItem = Object.assign({
          deptName, id, orgId, deptId: id
        })
        return newItem
      })
      // console.log(newList, "newList");
      if (!this.selectionList.map(v => v.id).includes(val.id)) {
        this.selectionList = this.selectionList.concat({
          deptName: val.deptName,
          id: val.id,
          orgId: val.orgId,
          deptId: val.id,
        });
      }
      // this.selectionList = newList;
    },
    async open(data) {
      this.visible = true
      this.title = data.title || '课程可见指定组织'
      this.dialogType = data.type || 'AppCreate'

      if (this.dialogType === "AppCreate") {
        return false
      }
      if (this.dialogType === "AppEdit") {

      }
      let { deptList } = data.formData;
      console.log(deptList, "deptList11");
      if (Array.isArray(deptList)) {
        this.$nextTick(() => {
          this.$refs.tree.setCheckedKeys(deptList.map(item => item.id));
        })
        this.selectionList = deptList
        this.formData = {
          ...data.formData,
        }
      }


      return new Promise((resolve, reject) => {
        this.$once("handle", ({ params, type }) => {
          if (type == "success") {
            resolve(params)
          } else {
            reject("取消")
          }

        })
      })
    },
    async onSubmit() {
      await this.$emit("handle", {
        type: "success",
        params: this.selectionList,
      })
      await this.close()
    },
    close() {
      this.visible = false
    },

    // 关闭
    onClose() {
      this.visible = false
      //   this.tableData = []
      this.selectionList = []
    },
  }
}
</script>

<style scoped lang="stylus">
    .left-box {
        width: 300px
        height: 450px
        overflow: auto
        margin-right: 12px
    }
    .right-box {
        width: 324px
        height: 450px
        overflow: auto
        padding-left: 25px
        border-left: 1px solid #ccc
    }

    >>>.el-tree {
        .el-tree-node__content {
        height: 36px
        padding-right: 16px !important
        padding-left: 0 !important
        }
    }
    >>>.el-tree-node__children {
        .el-tree-node {
            .el-tree-node__content {
                padding-right: 16px !important;
                padding-left: 16px !important
            }
        }   
    }
    

    >>>.custom-tree-node.flex.between-center{
        width 100%
    }
    >>>.el-tree-node__content:hover {
        padding-right: 16px !important
        padding-left: 0 !important
    }
    .remove:hover {
        color: rgb(29, 97, 242);
    }
</style>


<!-- <template>
    <el-dialog :visible.sync="isDepartment" append-to-body title="课程可见指定组织" :close-on-click-modal="false" :before-close="close" width="600px">
        <el-tree ref="tree" :data="courseDeptTree" default-expand-all show-checkbox check-on-click-node node-key="id" :check-strictly="true" :expand-on-click-node="false" :props="{ children: 'children', label: 'deptName' }" @node-click="onCurrentChange"> </el-tree>

        <el-button class="width-80 margin-right-14 margin-top-20" type="primary" @click="onSubmit" :loading="loading.onSubmit">确定 </el-button>
        <el-button @click="close">取 消</el-button>
    </el-dialog>
</template> -->


