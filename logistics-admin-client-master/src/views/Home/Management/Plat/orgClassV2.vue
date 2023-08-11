<template>
  <el-container class="height-100p">

    <TreeSelectorAsSideBar ref="TreeSelectorAsSideBar" type="AllOrgForOrgClass" 
                           :visible.sync="treeVisible" :selection.sync="categorySelection" ></TreeSelectorAsSideBar>

    <TableView class="flex-1 overflow-auto" :options="options" :params.sync="filterData">
      <template v-slot:main>
        <div class="flex center-center margin-bottom-8" v-if="!treeVisible">
          <div class="font-16 padding-8 padding-left-0 el-icon-s-unfold pointer text-main"
               @click="treeVisible = true"></div>
        </div>

        <div class="margin-bottom-8 margin-right-12 timer">
          <el-date-picker
            v-model="dateStart"
            class="setWidth"
            size="small"
            type="date"
            value-format="yyyy-MM-dd 00:00:00" 
            format="yyyy-MM-dd"
            placeholder="起始日期">
          </el-date-picker>
          -
          <el-date-picker
            v-model="dateEnd"
            class="setWidth"
            size="small"
            type="date"
            value-format="yyyy-MM-dd 23:59:59" 
            format="yyyy-MM-dd"
            placeholder="截至日期">
          </el-date-picker>
        </div>
      </template>
      
      <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(186)" v-loading="loading.table"
                @selection-change="selectionList = $event.map(item=>item.id)" @sort-change="sortChange" :default-sort="{prop: 'startDate', order: 'descending'}">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>

        <el-table-column type="index" label="序号" width="55"></el-table-column>
        <el-table-column label="机构名称" prop="orgName" min-width="88"></el-table-column>
        <el-table-column label="班级名称" prop="name" min-width="88"></el-table-column>
        <el-table-column label="课程专题" prop="courseName" min-width="100"></el-table-column>
        <el-table-column label="学员人数" prop="studentCount" width="100">
          <template  slot-scope="{row}">
            <div>
              {{ row.studentCount || '-' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="负责教师" prop="realName" min-width="100" show-tooltip-when-overflow></el-table-column>
        <!-- <el-table-column label="联系电话" prop="phone" min-width="100" show-tooltip-when-overflow></el-table-column> -->

        <el-table-column label="开课日期" prop="startDate" width="220" sortable="custom">
          <template slot-scope="{row}">
            {{ row.startDate | formatTime('YYYY-MM-DD') }}&nbsp;至&nbsp;{{ row.endDate | formatTime('YYYY-MM-DD') }}
          </template>
        </el-table-column>

        <!-- <el-table-column label="计划人数" prop="studentNum" width="88">
          <template slot-scope="{row}">
            {{ row.studentNum || '-' }}
          </template>
        </el-table-column>

        <el-table-column label="计划讲次" prop="lessonNum" width="88">
          <template slot-scope="{row}">
            {{ row.lessonNum || '-' }}
          </template>
        </el-table-column> -->

        <el-table-column label="班级状态" prop="state" width="88">
          <!-- <EleDot slot-scope="{ row }" :id="row.state" type="classStateInfo"></EleDot> -->
          <template slot-scope="{row}">
            {{ row.state | classStateInfo }}
          </template>
        </el-table-column>

        <el-table-column label="上课教室" prop="roomNo" width="108">
          <template slot-scope="{row}">
            {{ row.roomNo || '-' }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" class="margin-left-8" @click="onDetail(row)">详情</el-button>
            <el-button type="text" size="small" @click="onSeeClasses(row)" class="margin-left-10">课表</el-button>
          </template>
        </el-table-column>
      </el-table>

    </TableView>

    <DialogClassDetail ref="DialogClassDetail"></DialogClassDetail>
      <!-- 课表弹窗 -->
    <DialogCourseList ref="DialogCourseList"></DialogCourseList>
    <DialogCourseListNew ref="DialogCourseListNew"></DialogCourseListNew>

  </el-container>
</template>

<script>
import DialogClassDetail from '@/components/dialog/Classes/DialogClassDetail'
import TreeSelectorAsSideBar from '@/components/form/TreeSelectorAsSideBar'
import {mapGetters, mapState} from 'vuex'
import mxTableView from '@/components/mixins/mxTableView'
// 课表弹窗
import DialogCourseList from '@/components/dialog/Classes/DialogCourseList.vue'
import DialogCourseListNew from '@/components/dialog/Classes/DialogCourseListNew.vue'

export default {
  name: 'orgClassV2',
  components: {
    DialogClassDetail,
    TreeSelectorAsSideBar,
    DialogCourseList,
    DialogCourseListNew
  },
  mixins: [mxTableView],
  data() {
    return {
      options: {
        main: {
          state: {
            label: '班级状态',
            options: 'classStateInfo'
          }
        },
        side:'name:班级名称,courseName:课程专题,lecturerName:讲师姓名'
      },

      // 开始和结束日期
      dateStart: null,
      dateEnd: null,

      // 从左侧树传过来的
      treeVisible: true,
      categorySelection: null,

      sort: {
        sortField: 'startDate',
        sortOrder: 'desc'
      },
    }
  },
  computed: {
    ...mapGetters({
      classStateInfo: 'common/classStateInfo'
    }),
    params() {
      let orgId = undefined
      if (this.categorySelection) {
        let {id} = this.categorySelection
        orgId = id
      }
      return {
        ...this.filterData,
        ...this.sort,
        orgId: orgId,
        startDate: this.dateStart,
        endDate: this.dateEnd
      }
    },
    tableDataAPI() {
      return this.$api.LogisticsOrg.orgClassPageV2
    },
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
    // 操作 - 详情
    onDetail(row) {
      this.$refs.DialogClassDetail.open({
        pParams: row
      })
    },
    // 课表
    onSeeClasses(row) {
      this.$refs.DialogCourseListNew.open({
        formData: {
          id: row.id,
          orgId: row.orgId,
          className: row.name
        }
      })
    },
    // 排序
    sortChange ({ prop, order }) {
      this.sort.sortField = prop || undefined
      this.sort.sortOrder = order ? order.replace('ending', '') : undefined
    },
  }
}
</script>

<style scoped lang="stylus">
.timer
  margin 0 12px 8px 0
  font-size 14px
  .setWidth
      width 140px
</style>
