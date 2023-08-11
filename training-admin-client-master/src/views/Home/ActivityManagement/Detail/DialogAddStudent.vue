<template>
  <el-dialog :visible.sync="visible" append-to-body title="选择学员" :close-on-click-modal="false"
             :before-close="onClose" top="5vh" width="1000px">

    <el-form :model="formData" ref="formData" label-width="0" :inline="true" size="small">
      <!-- <el-form-item>
        <TreeSelect
            class="width-160 margin-right-16"
            style="align-self: baseline;"
            placeholder="请选择组织"
            :multiple="true"
            :clearable="true"
            :limit="0"
            :limitText="(list) => `已选择${list}个组织`"
            :defaultExpandLevel="Infinity"
            :normalizer="node => ({ id: node.id, label: node.deptName, children: node.children && node.children.length ? node.children : undefined })"
            :options="deptTree"
            value-consists-of="ALL"
            v-model="deptIdList"
        />
      </el-form-item> -->
      <el-form-item>
        <TreeSelect
            class="width-140 margin-right-16"
            style="align-self: baseline;"
            v-model="groupId"
            :options="studentGroupTree"
            :normalizer="node => ({ id: node.groupId, label: node.groupName })"
            :clearable="true"
            :defaultExpandLevel="Infinity"
            placeholder="学员标签">
        </TreeSelect>
      </el-form-item>

      <el-form-item>
        <el-input class="width-180" v-model="formData.realName" placeholder="学员姓名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input class="width-180" v-model="formData.phone" placeholder="手机号" clearable></el-input>
      </el-form-item>
      <!-- <el-form-item>
        <el-checkbox v-model="formData.inner" :true-label="1" :false-label="0">只筛选内部员工学员</el-checkbox>
      </el-form-item> -->
      <!-- <template v-if="formData.inner">
        <el-form-item>
          <el-select class="width-180" v-model="formData.roleId" placeholder="角色" clearable>
            <el-option v-for="item in orgRoleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <TreeSelect
            class="width-180 tree-select"
            v-model="formData.deptId"
            placeholder="组织"
            :options="deptTree"
            :normalizer="node => ({id: node.id, label: node.deptName, children: node.children.length ? node.children:undefined})"
            :defaultExpandLevel="Infinity">
          </TreeSelect>
        </el-form-item>
      </template> -->
    </el-form>
    <div class="flex between-center">
      <el-table v-loading="loading.table" :data="tableData" :height="$utils.FullViewHeight(443)">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>
        <el-table-column label="学员姓名" prop="realName"></el-table-column>
        <el-table-column label="手机号" prop="phone"></el-table-column>
        <el-table-column label="操作" width="100" >
          <template slot="header">
            <el-button type="primary" plain size="mini" @click="onAddAll">全部添加</el-button>
          </template>
          <template slot-scope="{row}">
            <span class="text-9 font-14" v-if="selectionList.map(v => v.userId).includes(row.userId) || row.added === 1">已添加</span>
            <span class="text-main font-14 pointer" @click="onAdd(row)" v-else>添加</span>
          </template>
        </el-table-column>
      </el-table>
      <el-table class="margin-left-16" :data="selectionList" :height="$utils.FullViewHeight(443)">
        <template v-slot:empty>
          <Results></Results>
        </template>
        <el-table-column label="学员姓名" prop="realName"></el-table-column>
        <el-table-column label="手机号" prop="phone"></el-table-column>
        <el-table-column label="操作" width="100" >
          <template slot="header">
            <el-button type="danger" plain size="mini" @click="onRemoveAll">全部移除</el-button>
          </template>
          <template slot-scope="{row}">
            <span class="text-error font-14 pointer" @click="onRemove(row)" v-if="selectionList.map(v => v.userId).includes(row.userId)">移除</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="flex between-center">
      <PaginationControl class="padding-16" :total="total" :pagination.sync="pagination"></PaginationControl>
      <span class="font-14 text-6">已选中{{selectionList.length}}个学员</span>
    </div>

    <template v-slot:footer>
      <el-button size="small" @click="onClose">取消</el-button>
      <el-button type="primary" size="small" @click="onSubmit" :loading="loading.submit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import PaginationControl from '@/components/form/PaginationControl'

export default {
  name: 'DialogAddStudent',
  components: {
    PaginationControl
  },
  data() {
    return {
      visible: false, // 弹窗开关
      formData: {
        courseId: '',
        realName: '',
        phone: ''
      },
      pagination: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      tableData: [],
      selectionList: [],
      loading: {
        table: true,
        submit: false
      },
      timer: null,
      deptIdList: [],
      groupId: null,
    }
  },
  computed: {
    ...mapState('system', {
      orgId: 'orgId'
    }),
    ...mapGetters({
      orgRoleList: 'common/orgRoleList',
      deptTree: 'common/deptTree',
      studentGroupTree: 'common/studentGroupTree'
    }),
    params() {
      return {
        ...this.formData,
        orgId: this.orgId,
        current: this.pagination.pageNum,
        size: this.pagination.pageSize,
        deptIdList: this.deptIdList,
        groupId: this.groupId,
      }
    }
  },
  watch: {
    params: {
      deep: true,
      handler() {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    }
  },
  methods: {
    // 打开弹窗
    open(data = {}) {
      this.visible = true
      this.formData = Object.assign(this.formData, data.formData)

      clearTimeout(this.timer)
      this.timer = setTimeout(() => {
        this.getTableData()
      }, 300)
    },
    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.$api.CourseStudent.selectPage(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.total = data.total
    },
    onAdd(row) {
      this.selectionList.push(row)
    },
    onAddAll() {
      // let selections = this.selectionList.map(item => item.userId) || []
      // this.tableData.forEach(item => {
      //   if (!selections.includes(item.userId)) {
      //     this.selectionList.push(item)
      //   }
      // })

      
      let list = this.tableData.filter((item) => item.added === 0)
      if(this.selectionList != null || this.selectionList.length > 0){
        list = list.filter((i) => !this.selectionList.map(v => v.userId).includes(i.userId))
      }
      console.log('list---------------', list)
      this.selectionList = this.selectionList.concat(list)
    },
    
    onRemove(row) {
      let index = this.selectionList.findIndex(item => item.userId === row.userId)
      if (index < 0) return false
      this.selectionList.splice(index, 1)
    },
    onRemoveAll() {
      this.selectionList = []
    },
    // 关闭
    onClose() {
      this.visible = false
      this.tableData = []
      this.selectionList = []
    },
    // 确定
    async onSubmit() {
      if (!this.selectionList.length) return this.$message.warning('请选择学员')
      if (!this.formData.courseId) return this.$message.error('课程ID不能为空')
      this.loading.submit = true
      let {code, data} = await this.$api.CourseStudent.batchAdd({
        courseId: this.formData.courseId,
        orgId: this.orgId,
        signUpInfos: this.selectionList.map(item => {
          return {
            studentId: item.id,
            userId: item.userId,
            assign: true,
          }
        })
      })
      this.loading.submit = false
      if (code !== 200) return false
      this.$emit('success')
      this.$message.success('添加成功')
      this.onClose()
    }
  }
}
</script>

<style scoped lang="stylus">

</style>