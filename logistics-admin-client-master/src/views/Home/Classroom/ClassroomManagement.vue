<template>
    <TableView :options="options" :params.sync="filterData">
      <template v-slot:main>
        <el-button class="margin-bottom-8 margin-right-2" type="primary" size="small" @click="onCreate"
                   icon="el-icon-plus">
          新增教室
        </el-button>

        <el-button class="margin-bottom-8 margin-right-12" type="primary" size="small" @click="onImport"
                   icon="el-icon-plus">
          导入教室
        </el-button>
      </template>
  
      <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(182)" v-loading="loading.table">
        <el-table-column label="教室单元" prop="building" min-width="100"></el-table-column>
        <el-table-column label="楼层" prop="floor" min-width="88"></el-table-column>
        <el-table-column label="教室编号" prop="roomNo" min-width="88"></el-table-column>
        <el-table-column label="教室座位数" prop="seats" min-width="88"></el-table-column>
        <el-table-column prop="area" min-width="88">
          <template slot="header">
            <span>教室面积&nbsp;&nbsp;(m<sup>2</sup>)</span>
          </template>
        </el-table-column>
        <el-table-column label="教室类别" prop="roomType" min-width="100"></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="{row}">
            <el-button type="text" size="small" icon="el-icon-edit" @click="onDetail(row)">详情</el-button>
            <!-- <el-button type="text" size="small" icon="el-icon-edit" @click="onFeeDetail(row)">资费</el-button> -->
            <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)">编辑</el-button>
            <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 新建教室弹窗 -->
      <DialogFormClassroomManage ref="DialogFormClassroomManage" @success="getTableData"></DialogFormClassroomManage>
      <!-- 教室详情弹窗 -->
      <DialogClassroomDetail ref="DialogClassroomDetail"></DialogClassroomDetail>
      <!-- 教室资费详情 -->
      <DialogFeeDetail ref="DialogFeeDetail"></DialogFeeDetail>
      <!-- 导入教室 -->
      <DialogImpClassroom ref="DialogImpClassroom" @importSuccess="getTableData"></DialogImpClassroom>
    </TableView>
  </template>
  
  <script>
  import DialogFormClassroomManage from '@/components/dialog/Classes/DialogFormClassroomManage'
  import DialogClassroomDetail from '@/components/dialog/Classes/DialogClassroomDetail'
  import DialogFeeDetail from '@/components/dialog/Classes/DialogFeeDetail'
  import DialogImpClassroom from '@/components/dialog/Classes/DialogImpClassroom'
  
  export default {
    name: 'ClassroomMange',
    components: {
      DialogFormClassroomManage,
      DialogClassroomDetail,
      DialogFeeDetail,
      DialogImpClassroom
    },
    data() {
      return {
        options: {
          total: 0,
          main: {
            building: {
              label: '教室单元',
              options: 'dormitoryUnit'
            },
            roomType: {
              label: '教室类别',
              options: 'classTypeInfo'
            },
          },
          side: 'roomNo:教室编号'
        },
        tableData: [],
        filterData: {},
        selectionList: [],
        timer: {
          table: null
        },
        loading: {
          table: true
        }
      }
    },
    computed: {
      params() {
        return {
          ...this.filterData
        }
      }
    },
    watch: {
      params: {
        deep: true,
        immediate: true,
        handler() {
          clearTimeout(this.timer.table)
          this.timer.table = setTimeout(() => {
            this.getTableData()
          }, 300)
        }
      }
    },
    methods: {
      async getTableData() {
        this.loading.table = true
        let {code, data} = await this.$api.Classroom.classroomPage(this.params)
        this.loading.table = false
        if (code !== 200) return false
        this.tableData = data.records
        this.options.total = data.total
      },
      // 操作 - 新增
      onCreate() {
        this.$refs.DialogFormClassroomManage.open({
          type: 'Create'
        })
      },
      // 操作 - 编辑
      onEdit(item) {
        this.$refs.DialogFormClassroomManage.open({
          type: 'Edit',
          formData: item
        })
      },
      // 操作 - 删除
      async onDelete(item) {
        await this.$confirm(`确认删除教室“${item.roomNo}”吗？`, {
          title: '删除确认'
        })
        this.loading.table = true
        let {code} = await this.$api.Classroom.classroomDelete({idList: [item.id]})
        this.loading.table = false
        if (code !== 200) return false
        this.$msg.Delete()
        await this.getTableData()
      },
      // 操作 - 详情
      onDetail(item) {
        this.$refs.DialogClassroomDetail.open({
          formData: item
        })
      },
      // 操作 - 资费详情
      onFeeDetail(item) {
        this.$refs.DialogFeeDetail.open({
          formData: item
        })
      },
      // 操作 - 导入教室信息
      onImport(){
        this.$refs.DialogImpClassroom.open()
      }
    }
  }
  </script>
  
  <style scoped lang="stylus">
  </style>
  