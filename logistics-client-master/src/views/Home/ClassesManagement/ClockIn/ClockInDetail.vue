<template>
  <div>

    <div class="flex height-32 line-height-32 padding-left-16">
      <h2 class="margin-0 font-16">{{ classInfo.name }}</h2>
      <div class="margin-left-16 font-14">{{ classInfo.courseName }}</div>
    </div>

    <TableView :options="options" :params.sync="filterData">

      <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(282)" v-loading="loading.table">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>

        <el-table-column type="index" label="序号" width="55"></el-table-column>
        <el-table-column prop="realName" label="学员姓名" min-width="100"></el-table-column>
        <el-table-column prop="phone" label="联系方式" min-width="100"></el-table-column>
        <el-table-column prop="createTime" label="签到时间" min-width="100">
          <template slot-scope="{row}">
            {{ row.createTime }}
          </template>
        </el-table-column>
        <el-table-column prop="siteCode" label="签到地点" min-width="100">
          <template slot-scope="{row}">
            {{ row.siteCode | clockInArea }}
          </template>
        </el-table-column>

      </el-table>
    </TableView>
  </div>
</template>

<script>
import mxTableView from '@/components/mixins/mxTableView'
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'ClockInDetail',
  mixins: [mxTableView],
  data() {
    return{
      options: {
        main: {
          siteCode: {
            label: '打卡类型',
            options: 'clockInArea'
          },
          date: {
            order: 80
          }
        },
        side: 'studentName:学员姓名'
      },
    }
  },
  props: {
    classInfo: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  computed: {
    ...mapGetters({
      clockInArea: 'common/clockInArea'
    }),
    params() {
      let clazzId = this.classInfo.id
      if(clazzId) clazzId = Number(clazzId)
      return {
        ...this.filterData,
        clazzId,
        clockInDate: this.filterData.date,
        date: undefined
      }
    },
    tableDataAPI() {
      return this.$api.StuClockIn.pages
    }
  },
  watch: {
    classInfo: {
      deep: true,
      handler() {
        this.tableData = []
      }
    }
  },
  methods: {
    async getTableData() {
      if(!this.params.clazzId) return false
      let data = await this.callTableDataAPI()
      if (data) {
        this.tableData = data.records
        this.options.total = data.total
      }
    },
  }
}
</script>

<style lang="stylus" scoped>
>>>.filter-control
  margin-bottom: 0
  .el-card__body
    .el-form-item
      .el-form-item__content
        .el-date-editor
          width: 140px 
</style>