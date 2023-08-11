<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 font-14 width-48 height-32 padding-0" type="primary" style="order: 200" @click="onCreate">新增</el-button>
    </template>

    <!-- <template v-slot:footer>
      <el-button size="small" :disabled="!selectionList.length" @click="onBatchDelete">
        批量删除
      </el-button>
    </template> -->

    <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(234)" v-loading="loading.table" @selection-change="selectionList = $event.map(item=>item.id)">
      <!-- <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column type="index" label="序号" width="55"></el-table-column> -->
      <el-table-column label="宿舍单元" prop="building" min-width="180">
        <template slot-scope="{row}">
          {{ row.building | dormitoryUnit }}
        </template>
      </el-table-column>
      <el-table-column label="房型" prop="roomTypeName" min-width="180">
        <template slot-scope="{row}">
          {{ row.roomTypeName }}<span v-if="row.area">({{ row.area }}m<sup>2</sup>)</span>
        </template>
      </el-table-column>
      <el-table-column label="房型编号" prop="roomTypeCode" min-width="180"></el-table-column>
      <el-table-column label="房间数量" prop="roomNum" min-width="180"></el-table-column>
      <el-table-column label="维修房数" prop="maintNum" min-width="180"></el-table-column>
      <el-table-column label="排序" prop="sort" min-width="180"></el-table-column>
      <el-table-column label="操作" width="158" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDetail(row)">详情</el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onEdit(row)">编辑</el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <DialogCOrEBasicInfo ref="DialogCOrEBasicInfo" @success="getTableData"></DialogCOrEBasicInfo>
    <DialogDetail ref="DialogDetail"></DialogDetail>
  </TableView>
</template>


<script>
import DialogCOrEBasicInfo from './DialogCOrEBasicInfo';
import DialogDetail from '@/components/dialog/DialogDetail';
export default {
  name: 'BasicInfo',
  components: {
    DialogCOrEBasicInfo,
    DialogDetail
  },
  mixins: [],
  props: {},
  data() {
    return {
      options: {
        total: 0,
        pageSizes: [25, 10, 50, 100],
        main: {
          building: {
            label: '宿舍单元',
            options: 'dormitoryUnit'
          },
          // roomType: {
          //   label: '房型',
          //   options: 'dormitoryType'
          // }
        },
        side: {
        }
      },
      // tradeDate: [],
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
      // let startDate
      // let endDate
      // if(this.tradeDate && this.tradeDate.length) {
      //   startDate = this.tradeDate[0]
      //   endDate = this.tradeDate[1]
      // }
      return {
        ...this.filterData,
        // startDate: startDate,
        // endDate: endDate
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
      let {code, data} = await this.$api.DormitoryRemainImport.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    // 操作 - 新增
    onCreate() {
      this.$refs.DialogCOrEBasicInfo.open({
        type: 'Create'
      })
    },
    // 操作 - 编辑
    onEdit(params) {
      this.$refs.DialogCOrEBasicInfo.open({
        type: 'Edit',
        formData: params
      })
    },
    // 操作 - 详情
    onDetail(params) {
      let detailInfo = {
        detailUrl: this.$api.DormitoryRemainImport.detail,
        params: {
          id: params.id
        },
        title: '宿舍详情'
      }
      this.$refs.DialogDetail.open({
        ...detailInfo
      })
    },
    // 单个删除
    async onDelete(params) {
      await this.$confirm(`确认删除房型“${params.roomTypeName}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.DormitoryRemainImport.delete({idList: [params.id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete()
      await this.getTableData()
    },
    // 批量删除
    // async onBatchDelete(){
    //   await this.$confirm(`确认删除${this.selectionList.length}个入住记录吗？`, {
    //     title: '删除确认'
    //   })
    //   this.loading.table = true
    //   let {code} = await this.$api.DormitorySchedule.delete({idList: this.selectionList})
    //   this.loading.table = false
    //   if (code !== 200) return false
    //   this.$msg.Delete('', true)
    //   await this.getTableData() 
    // },
  }
}
</script>


<style lang="stylus" scoped>

</style>