<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 font-14 width-48 height-32 padding-0" type="primary" style="order: 200" @click="importInfo">导入</el-button>

      <div class="margin-bottom-8 margin-right-12">
        <el-date-picker v-model="tradeDate" type="daterange" start-placeholder="起始日期" end-placeholder="截至日期"
                        value-format="yyyy-MM-dd" format="yyyy-MM-dd" align="center" size="small" style="width: 240px"></el-date-picker>
      </div>
    </template>

    <!-- <template v-slot:footer>
      <el-button size="small" :disabled="!selectionList.length" @click="onBatchDelete">
        批量删除
      </el-button>
    </template> -->

    <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(234)" v-loading="loading.table" @selection-change="selectionList = $event.map(item=>item.id)">
      <!-- <el-table-column type="selection" min-width="55"></el-table-column> -->
      <!-- <el-table-column type="index" label="序号" min-width="55"></el-table-column> -->
    
      <el-table-column label="日期" min-width="154" fixed="left">
        <template slot-scope="{row}">
          {{ row.day }}({{row.descr}})
        </template>
      </el-table-column>
      <el-table-column label="总量" prop="totals" min-width="55"></el-table-column>
      <el-table-column label="总量含超预留" prop="exceedTotals" min-width="108"></el-table-column>
      <el-table-column label="非确认" prop="unconfirmed" min-width="65"></el-table-column>
      <el-table-column label="出租率" prop="occupancyRate" min-width="75"></el-table-column>
      <el-table-column label="单人间" prop="roomType" min-width="78">
        <template slot="header" slot-scope="{row}">
          <span>单人间</span>
          <span>SRS-100</span>
        </template>
        <template  slot-scope="{row}">
          {{ row.dailyList.find(i => i.roomTypeCode === "SRS-100").free }}
        </template>
      </el-table-column>
      
      <el-table-column label="大单人间" prop="roomType" min-width="88">
        <template slot="header" slot-scope="{row}">
          <span>大单人间</span>
          <span>SQR-64</span>
        </template>
        <template  slot-scope="{row}">
          {{ row.dailyList.find(i => i.roomTypeCode === "SQR-64").free }}
        </template>
      </el-table-column>

      <el-table-column label="双人间" prop="roomType" min-width="78">
        <template slot="header" slot-scope="{row}">
          <span>双人间</span>
          <span>STR-212</span>
        </template>
        <template  slot-scope="{row}">
          {{ row.dailyList.find(i => i.roomTypeCode === "STR-212").free }}
        </template>
      </el-table-column>

      <el-table-column label="四人间" prop="roomType" min-width="78">
        <template slot="header" slot-scope="{row}">
          <span>四人间</span>
          <span>SDR-120</span>
        </template>
        <template  slot-scope="{row}">
          {{ row.dailyList.find(i => i.roomTypeCode === "SDR-120").free }}
        </template>
      </el-table-column>

      <el-table-column label="残疾人大单间" prop="roomType" min-width="128">
        <template slot="header" slot-scope="{row}">
          <span>残疾人大单间</span>
          <span>HQR-4</span>
        </template>
        <template  slot-scope="{row}">
          {{ row.dailyList.find(i => i.roomTypeCode === "HQR-4").free }}
        </template>
      </el-table-column>

      <el-table-column prop="roomType" min-width="128">
        <template slot="header" slot-scope="{row}">
          <span>单人间(19m<sup>2</sup>)</span>
          <span>19BRS-52</span>
        </template>
        <template  slot-scope="{row}">
          {{ row.dailyList.find(i => i.roomTypeCode === "19BRS-52").free }}
        </template>
      </el-table-column>

      <el-table-column prop="roomType" min-width="128">
        <template slot="header" slot-scope="{row}">
          <span>单人间(22m<sup>2</sup>)</span>
          <span>22BRS-108</span>
        </template>
        <template  slot-scope="{row}">
          {{ row.dailyList.find(i => i.roomTypeCode === "22BRS-108").free }}
        </template>
      </el-table-column>

      <el-table-column prop="roomType" min-width="128">
        <template slot="header" slot-scope="{row}">
          <span>单人间(27m<sup>2</sup>)</span>
          <span>27BRS-8</span>
        </template>
        <template  slot-scope="{row}">
          {{ row.dailyList.find(i => i.roomTypeCode === "27BRS-8").free }}
        </template>
      </el-table-column>

      <el-table-column prop="roomType" min-width="128">
        <template slot="header" slot-scope="{row}">
          <span>双人间(19m<sup>2</sup>)</span>
          <span>19BTR-83</span>
        </template>
        <template  slot-scope="{row}">
          {{ row.dailyList.find(i => i.roomTypeCode === "19BTR-83").free }}
        </template>
      </el-table-column>
      
      <el-table-column prop="roomType" min-width="128">
        <template slot="header" slot-scope="{row}">
          <span>双人间(22m<sup>2</sup>)</span>
          <span>22BTR-173</span>
        </template>
        <template  slot-scope="{row}">
          {{ row.dailyList.find(i => i.roomTypeCode === "22BTR-173").free }}
        </template>
      </el-table-column>
      
      <el-table-column prop="roomType" min-width="128">
        <template slot="header" slot-scope="{row}">
          <span>双人间(27m<sup>2</sup>)</span>
          <span>27BTR-24</span>
        </template>
        <template  slot-scope="{row}">
          {{ row.dailyList.find(i => i.roomTypeCode === "27BTR-24").free }}
        </template>
      </el-table-column>

      
      <!-- <el-table-column label="操作" min-width="88" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)">删除
          </el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <DialogImportRemain ref="DialogImportRemain" @importSuccess="getTableData"></DialogImportRemain>
  </TableView>
</template>


<script>
import DialogImportRemain from './DialogImportRemain';
export default {
  name: 'RemainImport',
  components: {
    DialogImportRemain
  },
  mixins: [],
  props: {},
  data() {
    return {
      options: {
        total: 0,
        main: {
          // building: {
          //   label: '宿舍单元',
          //   options: 'dormitoryUnit'
          // },
        },
        side: {
          // orgName: {
          //   label: '单位名称',
          //   order: -15
          // }
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
  created() {

  },
  mounted() {},
  computed: {
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
      let {code, data} = await this.$api.DormitoryRemainImport.importPage(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    // 导入入住数据
    importInfo() {        
      this.$refs.DialogImportRemain.open()
    },
  }
}
</script>


<style lang="stylus" scoped>
>>>.el-table
  .el-table__header-wrapper
    thead
      tr
        th
          height: 64px
          vertical-align: middle;
          .cell
            height: auto
            line-height: 23px
            display: flex
            flex-direction: column
            justify-content: center
            align-items: flex-start
  .el-table__body-wrapper
    tbody
        tr
          td
            .cell
              padding: 6px
            
  .el-table__fixed
    .el-table__fixed-header-wrapper
      thead
        tr
          th
            height: 64px
            vertical-align: middle;
            .cell
              height: auto
              line-height: 23px
    .el-table__fixed-body-wrapper
      tbody
          tr
            td
              .cell
                padding: 6px
</style>