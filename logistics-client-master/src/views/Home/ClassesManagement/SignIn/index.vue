<template>
  <TableView :options="options" :params.sync="filterData">

    <div class="flex">
      <!-- 左侧  班级表格 -->
      <div class="sign-in-class shrink-0 width-240 margin-top-16 margin-bottom-16">
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

      <!-- 右侧  签到表格 -->
      <div class="flex-1 width-0">
        <SignInDetail :classInfo="classInfo"></SignInDetail>
      </div>
    </div>

  </TableView>
</template>

<script>
import {mapGetters} from 'vuex'
import SignInDetail from '@/views/Home/ClassesManagement/SignIn/SignInDetail'
import mxTableView from '@/components/mixins/mxTableView'

export default {
  name: 'SignIn',
  components: {
    SignInDetail
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
      
      // 选择的班级的信息  传递到右侧使用
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
  methods: {   
    // 获取 - 班级信息
    async getTableData() {
      let data = await this.callTableDataAPI()
      if (data) {
        data.records.forEach(item => item.state = Number(item.state))
        this.tableData = data.records

        this.classInfo = this.tableData[0] || {}
      }
    },
  }
}
</script>

<style scoped lang="stylus">
.sign-in-class {
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
