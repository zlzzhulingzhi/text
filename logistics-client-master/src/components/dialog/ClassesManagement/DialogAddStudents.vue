<template>
  <el-dialog :visible.sync="visible" append-to-body :close-on-click-modal="false"
             :before-close="onClose" top="5vh" width="1000px">

    <template slot="title">
      <span class="text-3 text-bold font-16 margin-right-8">添加学员</span>
      <el-checkbox v-model="isThisCourse">筛选本课程学员</el-checkbox>
    </template>

    <el-form :model="formData" ref="formData" label-width="0" :inline="true" size="small">
      <el-form-item>
        <TreeSelect
            class="width-140"
            v-model="groupId"
            :options="studentGroupTree"
            :normalizer="node => ({ id: node.groupId, label: node.groupName })"
            :clearable="true"
            :defaultExpandLevel="Infinity"
            placeholder="学员标签">
        </TreeSelect>
      </el-form-item>

      <el-form-item>
        <el-input class="width-140" v-model="formData.realName" placeholder="学员姓名" clearable></el-input>
      </el-form-item>

      <el-form-item>
        <el-input class="width-140" v-model="formData.phone" placeholder="手机号" clearable></el-input>
      </el-form-item>
    </el-form>


    <div class="flex between-center">
      <el-table v-loading="loading.table" :data="tableData" :height="$utils.FullViewHeight(350)">
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

      <el-table class="margin-left-16" :data="selectionList" :height="$utils.FullViewHeight(350)">
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
  name: 'DialogAddStudents',
  components: {
    PaginationControl
  },
  data() {
    let defaultFormData = {
      // courseId: '',
      realName: '',
      phone: '',
      groupIdList: []
    }
    return {
      visible: false, // 弹窗开关
      formData: {
        ...defaultFormData
      },
      groupId: null,
      defaultFormData,
      
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
      
      clazzId: null,
      isThisCourse: true,
      courseId: null
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
      if(this.groupId) this.formData.groupIdList = [this.groupId]
      else this.formData.groupIdList = []
      return {
        ...this.formData,
        orgId: this.orgId,
        current: this.pagination.pageNum,
        size: this.pagination.pageSize,
        byCourseId: this.isThisCourse ? 1 : 0
        // deptIdList: this.deptIdList,
        // groupId: this.groupId,
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
      // 默认进入时选择筛选课程
      this.isThisCourse = true
      // this.formData = Object.assign(this.formData, data.formData)
      this.clazzId = data.formData.id

      clearTimeout(this.timer)
      this.timer = setTimeout(() => {
        this.getTableData()
      }, 300)
    },
    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.$api.classesAddStudents.pageForClass({
        ...this.params,
        clazzId: this.clazzId
      })
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.total = data.total
    },
    onAdd(row) {
      this.selectionList.push(row)
    },
    onAddAll() {
      let list = this.tableData.filter((item) => item.added === 0)
      if(this.selectionList != null || this.selectionList.length > 0){
        list = list.filter((i) => !this.selectionList.map(v => v.userId).includes(i.userId))
      }
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

      // this.groupId = null
      // this.formData = {
      //   ...this.defaultFormData
      // }
      // this.$refs.formData.resetFields()

      this.tableData = []
      this.selectionList = []
    },
    // 确定
    async onSubmit() {
      if (!this.selectionList.length) return this.$message.warning('请选择学员')
      this.loading.submit = true
      this.selectionList = this.selectionList.map(item => {
        return {
          clazzId: this.clazzId,
          orgId: this.orgId,
          memberId: item.userId,
          studentId: item.studentId
        }
      })
      let {code, data} = await this.$api.classesStudents.add(this.selectionList)
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
>>>.el-dialog
  margin-top 2vh !important
  margin-bottom 0
</style>