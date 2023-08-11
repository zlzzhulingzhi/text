<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 font-14 width-48 height-32 padding-0" type="primary" style="order: 200" @click="importInfo">导入</el-button>

      <div class="margin-bottom-8 margin-right-12">
        <el-date-picker v-model="tradeDate" type="daterange" start-placeholder="起始日期" end-placeholder="截至日期"
                        value-format="yyyy-MM-dd" format="yyyy-MM-dd" align="center" size="small" style="width: 240px"></el-date-picker>
      </div>
    </template>

    <template v-slot:footer>
      <el-button size="small" :disabled="!selectionList.length" @click="onBatchDelete">
        批量删除
      </el-button>
    </template>
    <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(182)" v-loading="loading.table" @selection-change="selectionList = $event.map(item=>item.id)">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column type="index" label="序号" width="55"></el-table-column>
      <el-table-column label="房间信息" width="200">
        <template slot-scope="{row}">
          {{ row.building }}{{row.floor}}{{ row.roomNo }}
        </template>
      </el-table-column>
      <el-table-column label="房型" prop="roomType" min-width="88"></el-table-column>
      <el-table-column label="入住日期" prop="useDate" min-width="150">
        <template slot-scope="{row}">
          {{ row.useDate | formatTime('yyyy-MM-DD') }}
        </template>
      </el-table-column>
      <el-table-column label="单位名称" prop="orgName" min-width="180"></el-table-column>
      <el-table-column label="操作" min-width="88" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <DialogImportDormitoryUseInfo ref="DialogImportDormitoryUseInfo" @importSuccess="getTableData"></DialogImportDormitoryUseInfo>
  </TableView>
</template>

<script>
import {mapGetters} from 'vuex'
import DialogImportDormitoryUseInfo from '@/components/dialog/Dormitory/DialogImportDormitoryUseInfo'

export default {
  name: 'DormitoryInfo',
  components: {
    DialogImportDormitoryUseInfo
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          building: {
            label: '宿舍单元',
            options: 'dormitoryUnit'
          },
          roomType: {
            label: '房型',
            options: 'dormitoryType'
          },
          // tradeDate: {
          //   order: 80
          // }
        },
        // side: 'roomNo:房号,orgName:单位名称'
        side: {
          roomNo: {
            label: '房号'
          },
          orgName: {
            label: '单位名称',
            order: -15
          }
        }
      },
      tradeDate: [],
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
    ...mapGetters({
    }),
    params() {
      let startDate
      let endDate
      if(this.tradeDate && this.tradeDate.length) {
        startDate = this.tradeDate[0]
        endDate = this.tradeDate[1]
      }
      return {
        ...this.filterData,
        startDate: startDate,
        endDate: endDate
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
      let {code, data} = await this.$api.DormitorySchedule.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    // 操作 - 新增
    onCreate() {
      this.$refs.DialogFormDormitoryApply.open({
        type: 'Create'
      })
    },
    // 单个删除
    async onDelete(params) {
      await this.$confirm(`确认删除“${params.roomNo}”的使用记录吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.DormitorySchedule.delete({idList: [params.id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete()
      await this.getTableData()
    },
    // 批量删除
    async onBatchDelete(){
      await this.$confirm(`确认删除${this.selectionList.length}个入住记录吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.DormitorySchedule.delete({idList: this.selectionList})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete('', true)
      await this.getTableData() 
    },
    // 导入入住数据
    importInfo() {        
      this.$refs.DialogImportDormitoryUseInfo.open()
    },
   
  }
}
</script>

<style scoped lang="stylus">
</style>
