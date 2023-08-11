<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <div class="timer">
        <el-date-picker
            v-model="startVisitTime"
            class="setWidth"
            size="small"
            type="date"
            value-format="yyyy-MM-dd 00:00:00" 
            format="yyyy-MM-dd"
            placeholder="起始日期">
          </el-date-picker>
          -
          <el-date-picker
            v-model="endVisitTime"
            class="setWidth"
            size="small"
            type="date"
            value-format="yyyy-MM-dd 23:59:59" 
            format="yyyy-MM-dd"
            placeholder="截至日期">
          </el-date-picker>
      </div>

      <div class="searchInput width-140 margin-bottom-8 margin-right-12" >
        <el-input size="small" class="" placeholder="部门/机构名称" v-model="visitDepartment" clearable>
          <template v-slot:suffix>
            <div class="line-height-32 text-c font-14 el-icon-search margin-right-6 absolute"></div>
          </template>
        </el-input>
      </div>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(186)" v-loading="loading.table"
              @selection-change="selectionList = $event.map(item=>item.id)">
      <template v-slot:empty>
        <Results v-if='!loading.table'></Results>
        <span v-else></span>
      </template>

      <el-table-column type="index" label="序号" width="55"></el-table-column>

      <el-table-column label="访客姓名" prop="visitorName" min-width="88"></el-table-column>
      <el-table-column label="手机号" prop="phone" min-width="100"></el-table-column>
      <el-table-column label="车牌号" prop="carNumber" min-width="100"></el-table-column>

      <el-table-column label="访问时间" prop="visitTime" min-width="100"></el-table-column>
      <el-table-column label="访问对象" prop="visitPeople" min-width="100"></el-table-column>
      <el-table-column label="访问部门/机构" prop="visitDepartment" min-width="100"></el-table-column>
      <el-table-column label="访问事由" prop="visitReason" min-width="88"></el-table-column>

      <el-table-column label="状态" prop="auditStats">
        <EleDot slot-scope="{ row }" :id="row.auditStats" type="auditStats"></EleDot>
      </el-table-column>

      <el-table-column label="操作" width="80" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-edit" @click="onPassOrNot(row)" v-if="row.auditStats === -1">审批
          </el-button>
          <el-button type="text" size="small" icon="el-icon-edit" @click="onDelete(row)" v-if="row.auditStats === 0">删除
          </el-button>
        </template>
      </el-table-column>

    </el-table>

    <DialogApply ref="DialogApply" @success="getTableData"></DialogApply>

  </TableView>
</template>

<script>
import mxTableView from '@/components/mixins/mxTableView'
import DialogApply from '@/components/dialog/Apply/DialogApply'

export default {
  name: 'VisitorManage',
  components: {
    DialogApply
  },
  mixins: [mxTableView],
  data() {
    return {
      options: {
        main: {
          auditStats: {
            label: '审批状态',
            order: 120,
            value: -1
          }
        },
        side: 'visitorName:访客姓名,phone:手机号',
      },
      startVisitTime: null,
      endVisitTime: null,
      visitDepartment: null
    }
  },
  computed: {
    params() {
      return {
        ...this.filterData,
        startVisitTime: this.startVisitTime,
        endVisitTime: this.endVisitTime,
        visitDepartment: this.visitDepartment
      }
    },
    tableDataAPI() {
      return this.$api.visitorManage.page
    }
  },
  methods: {
    // 操作 - 审批
    async onPassOrNot(row) {
      await this.$refs.DialogApply.open({
        ...row
      })
    },
    // 操作 - 删除
    async onDelete(params) {
      await this.$confirm(`确认删除访客“${params.visitorName}”的记录吗？`, {
        title: '删除确认',
        type: 'warning'
      })
      let {code} = await this.$api.visitorManage.delete({
        idList: [params.id]
      })
      if(code !== 200) return false
      this.getTableData()
    }
  }
  
}
</script>

<style lang="stylus">
.timer
  margin 0 12px 8px 0
  font-size 14px
  .setWidth
      width 140px

.searchInput
  .el-input
    .el-input__inner
      padding 0 32px 0 8px
  .absolute
    right 0
  .el-input__clear
    margin-top 1px
    height 30px
    background-color NEUTRAL_COLOR_F
    position relative
    z-index 10
</style>