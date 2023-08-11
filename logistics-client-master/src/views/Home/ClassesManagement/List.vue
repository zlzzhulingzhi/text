<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-12" type="primary" size="small" @click="onCreate"
                 icon="el-icon-plus">
        新增班级
      </el-button>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(186)" v-loading="loading.table"
              @selection-change="selectionList = $event.map(item=>item.id)">
      <template v-slot:empty>
        <Results v-if='!loading.table'></Results>
        <span v-else></span>
      </template>

      <el-table-column type="index" label="序号" width="55"></el-table-column>

      <el-table-column label="班级名称" prop="name" min-width="88"></el-table-column>
      <el-table-column label="专题名称" prop="courseName" min-width="100"></el-table-column>
      <el-table-column label="负责教师" prop="realName" min-width="100"></el-table-column>

      <el-table-column label="开课日期" prop="startDate" min-width="200">
        <template slot-scope="{row}">
          {{ row.startDate | formatTime('YYYY-MM-DD') }}&nbsp;至&nbsp;{{ row.endDate | formatTime('YYYY-MM-DD') }}
        </template>
      </el-table-column>

      <!-- <el-table-column label="计划人数" prop="studentNum" min-width="88">
        <template slot-scope="{row}">
          {{ row.studentNum || '--' }}
        </template>
      </el-table-column> -->

      <!-- <el-table-column label="计划讲次" prop="lessonNum" min-width="88">
        <template slot-scope="{row}">
          {{ row.lessonNum || '--' }}
        </template>
      </el-table-column> -->
      
      <el-table-column label="上课教室" prop="roomNo" width="88">
          <template slot-scope="{row}">
            {{ row.roomNo || '--' }}
          </template>
        </el-table-column>

      <el-table-column label="班级状态" prop="state">
        <EleDot slot-scope="{ row }" :id="row.state" type="classStateInfo"></EleDot>
      </el-table-column>


      <el-table-column label="操作" width="230" fixed="right">
        <template slot-scope="{row}">
          <el-switch :width="20" v-model="row.state"
                     :active-value="getClassStateItem(1).id"
                     :inactive-value="getClassStateItem(2).id"
                     :active-text="getClassStateItem(row.state).name" @change="onEnabled(row.id, $event)"></el-switch>

          <el-button type="text" size="small" @click="onSeeClasses(row)" class="margin-left-10">课表</el-button>
          
          <el-button type="text" class="margin-left-8" @click="onDetail(row)">详情</el-button>
          <el-dropdown trigger="click" @command="onCommand" :hide-on-click="false" v-if="row.state === 1">
            <span class="margin-left-8 font-12 text-main pointer">更多<i class="el-icon-caret-bottom"></i></span>
            <el-dropdown-menu class="font-13" slot="dropdown">
              <el-dropdown-item :command="{ type: 'onAddStudents', row }">添加学员</el-dropdown-item>
              <el-dropdown-item :command="{ type: 'onEdit', row }">编辑</el-dropdown-item>
              <el-dropdown-item :command="{ type: 'onDelete', row }">
                <span class="text-error">删除</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <DialogAddStudents ref="DialogAddStudents"></DialogAddStudents>
    <DialogClassDetail ref="DialogClassDetail"></DialogClassDetail>
    <!-- 课表弹窗 -->
    <DialogCourseListNew ref="DialogCourseListNew"></DialogCourseListNew>
  </TableView>
</template>

<script>
// 添加学员
import DialogAddStudents from '@/components/dialog/ClassesManagement/DialogAddStudents'
// 班级详情
import DialogClassDetail from '@/components/dialog/ClassesManagement/DialogClassDetail'
// 课表弹窗
// import DialogCourseList from '@/components/dialog/ClassesManagement/DialogCourseList.vue'
import DialogCourseListNew from '@/components/dialog/ClassesManagement/DialogCourseListNew.vue'

import mxTableView from '@/components/mixins/mxTableView'
import {mapGetters} from 'vuex'

export default {
  name: 'ClassesManage',
  components: {
    DialogAddStudents,
    DialogClassDetail,
    // DialogCourseList,
    DialogCourseListNew
  },
  mixins: [mxTableView],
  data() {
    return {
      options: {
        total: 0,
        main: {
          state: {
            label: '班级状态',
            options: 'classStateInfo',
            order: 120
          }
        },
        side: 'name:班级名称'
      }
    }
  },
  computed: {
    ...mapGetters({
      classStateInfo: 'common/classStateInfo'
    }),
    tableDataAPI() {
      return this.$api.Classes.page
    }
  },
  methods: {
    // 获取 classStateInfo 选项
    getClassStateItem(id) {
      return this.classStateInfo.find(a => a.id === id) || {}
    },

    // 操作 - 下拉菜单
    onCommand({type, row}) {
      this[type] && this[type](row)
    },

    // 操作 - 新增班级 （新版）
    onCreate() {
      this.$router.push({
        name: 'classesCreate'
      })
    },

    // 操作 - 编辑班级
    async onEdit(item) {
      this.$router.push({
        name: 'ClassEdit',
        query: {
          id: item.id
        }
      })
    },
    // 操作 - 删除
    async onDelete({id, name}) {
      await this.$confirm(`确认删除班级“${name}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Classes.delete({idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(name)
      await this.getTableData()
    },
    // 操作 - 开班/结班
    async onEnabled(id, state) {
      this.loading.table = true
      let {code} = await this.$api.Classes.update({
        id,
        state
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg['Update']('班级状态', false)
      await this.getTableData()
    },
    // 操作 - 详情
    onDetail(row) {
      this.$refs.DialogClassDetail.open({
        pParams: row
      })
    },
    // 操作 - 添加学员
    onAddStudents(row) {
      this.$refs.DialogAddStudents.open({
        formData: row
      })
    },
    // 课表
    onSeeClasses(row) {
      this.$refs.DialogCourseListNew.open({
        formData: {
          id: row.id,
          className: row.name
        }
      })
    }
  }
}
</script>

<style scoped lang="stylus">
</style>
