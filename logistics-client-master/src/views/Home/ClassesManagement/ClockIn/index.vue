<template>
  <TableView :options="options" :params.sync="filterData">

    <div class="flex">
      <div class="clock-in-class shrink-0 width-240 margin-top-16 margin-bottom-16">
        <el-table :data="tableData" :height="$utils.FullViewHeight(144)"
                  @row-click="classInfo = $event"
                  :row-class-name="({row})=>row.id === classInfo.id?`pointer active`:`pointer`"
                  v-loading="loading.table">
          <template v-slot:empty>
            <Results v-if='!loading.table'></Results>
            <span v-else></span>
          </template>
          <el-table-column label="班级名称" prop="name" min-width="80"></el-table-column>
          <el-table-column label="专题名称" prop="courseName" width="80" show-tooltip-when-overflow></el-table-column>
          <el-table-column label="状态" prop="state" width="72">
            <EleDot slot-scope="{ row }" :id="row.state" type="classStateInfo"></EleDot>
          </el-table-column>
        </el-table>
      </div>

      <div class="flex-1 width-0">
        <ClockInDetail :classInfo="classInfo"></ClockInDetail>
      </div>
    </div>

  </TableView>
</template>

<script>
import {mapGetters} from 'vuex'
import ClockInDetail from '@/views/Home/ClassesManagement/ClockIn/ClockInDetail'
import mxTableView from '@/components/mixins/mxTableView'

export default {
  name: 'Attend',
  components: {
    ClockInDetail
  },
  mixins: [mxTableView],
  data() {
    return {
      options: {
        total: undefined,
        main: {
          state: {
            label: '班级状态',
            options: 'classStateInfo',
            order: 120
          }
        },
        side: 'name:班级名称'
      },

      classInfo: {},

    }
  },
  computed: {
    params() {
      return {
        ...this.filterData,
        size: 999
      }
    },
    tableDataAPI() {
      return this.$api.Classes.page
    }
  },
  watch: {
  },
  methods: {
    // 获取  班级信息
    async getTableData() {
      let data = await this.callTableDataAPI()
      if (data) {
        data.records.forEach(item => item.state = Number(item.state))
        this.tableData = data.records
        this.classInfo = this.tableData[0] || {}
      }
    }
  }
}
</script>

<style scoped lang="stylus">
.clock-in-class {
  /deep/ .el-table__body {
    .el-table__row {
      &.active, &:hover {
        td {
          background-color: BACKGROUND_COLOR;
        }
      }
    }
  }
}
</style>
