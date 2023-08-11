<template>
  
  <div>
    <el-breadcrumb class="margin-bottom-12">
      <el-breadcrumb-item v-for="item in $route.matched.slice(1)" :key="item.id">
        {{ item.meta.title }}
      </el-breadcrumb-item>
    </el-breadcrumb>

    
    <TableView :options="options" :params.sync="filterData">
      <template v-slot:main>
        <div class="margin-bottom-8 margin-right-20 text-bold">学员姓名：{{ studentInfo.studentName }}</div>
        <el-select v-model="areaValue" size="small" placeholder="请选择" class="margin-bottom-8 margin-right-12 width-140">
          <el-option
            v-for="item in clockInArea"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </template>

      <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(212)" v-loading="loading.table">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>

        <el-table-column type="index" label="序号" width="55"></el-table-column>
        <el-table-column prop="clockInDate" label="日期" min-width="100"></el-table-column>
        <el-table-column prop="createTime" label="时间" min-width="100">
          <template slot-scope="{row}">
            {{ row.createTime | formatTime('HH:mm:ss') }}
          </template>
        </el-table-column>
        <el-table-column prop="siteCode" label="地点" min-width="100">
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
  name: 'studentClockDetail',
  components: {
  },
  mixins: [mxTableView],
  data() {
    return {
      options: {
        total: 0,
        main: {
          // state: {
          //   label: '打卡区域',
          //   options: 'clockInArea',
          //   value: 'js'
          // },
          date: {
            order: 120
          }
        },
        // side: 'name:班级名称'

      },
      areaValue: 'jxl',
      clazzId: null,
      memberId: null,
    }
  },
  created() {
    this.clazzId = this.$route.query.id,
    this.memberId = this.$route.query.memberId
  },
  computed: {
    ...mapGetters({
      clockInArea: 'common/clockInArea'
    }),
    ...mapState('router', {
      studentInfo: 'studentInfo'
    }),
    params() {
      return {
        ...this.filterData,
        siteCode: this.areaValue,
        clazzId: Number(this.studentInfo.id),
        memberId: Number(this.studentInfo.memberId),
        clockInDate: this.filterData.date,
        date: undefined
      }
    },
    tableDataAPI() {
      return this.$api.StuClockIn.page
    }
  },
  methods: {
    
  }
}
</script>

<style scoped lang="stylus">
</style>
