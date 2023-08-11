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
                @selection-change="selectionList = $event.map(item=>item.id)">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>

        <el-table-column type="index" label="序号" width="55"></el-table-column>
        <el-table-column label="机构名称" prop="orgName" min-width="88" show-tooltip-when-overflow></el-table-column>
        <el-table-column label="班级名称" prop="name" min-width="88" show-tooltip-when-overflow></el-table-column>
        <el-table-column label="课程名称" prop="courseName" min-width="100" show-tooltip-when-overflow></el-table-column>
        <el-table-column label="负责教师" prop="realName" min-width="100" show-tooltip-when-overflow></el-table-column>

        <el-table-column label="开课日期" prop="startDate" width="220">
          <template slot-scope="{row}">
            {{ row.startDate | formatTime('YYYY-MM-DD') }}&nbsp;至&nbsp;{{ row.endDate | formatTime('YYYY-MM-DD') }}
          </template>
        </el-table-column>

        <el-table-column label="计划人数" prop="studentNum" width="88">
          <template slot-scope="{row}">
            {{ row.studentNum || '--' }}
          </template>
        </el-table-column>

        <el-table-column label="计划讲次" prop="lessonNum" width="88">
          <template slot-scope="{row}">
            {{ row.lessonNum || '--' }}
          </template>
        </el-table-column>

        <el-table-column label="班级状态" prop="state" width="88">
          <EleDot slot-scope="{ row }" :id="row.state" type="classStateInfo"></EleDot>
        </el-table-column>

        <el-table-column label="操作" width="80" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" class="margin-left-8" @click="onDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <DialogClassDetail ref="DialogClassDetail"></DialogClassDetail>
    </TableView>

  </el-container>
</template>

<script>
import DialogClassDetail from '@/components/dialog/Classes/DialogClassDetail'
import TreeSelectorAsSideBar from '@/components/form/TreeSelectorAsSideBar'
import {mapGetters, mapState} from 'vuex'
import mxTableView from '@/components/mixins/mxTableView'

export default {
  name: 'Employee',
  components: {
    DialogClassDetail,
    TreeSelectorAsSideBar
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
        side:'name:班级名称'
      },

      // 开始和结束日期
      dateStart: null,
      dateEnd: null,

      // 从左侧树传过来的
      treeVisible: true,
      categorySelection: null,
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
        orgId: orgId,
        startDate: this.dateStart,
        endDate: this.dateEnd
      }
    },
    tableDataAPI() {
      return this.$api.LogisticsOrg.orgClassPage
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
    
    // 操作 - 详情
    onDetail(row) {
      this.$refs.DialogClassDetail.open({
        pParams: row
      })
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
