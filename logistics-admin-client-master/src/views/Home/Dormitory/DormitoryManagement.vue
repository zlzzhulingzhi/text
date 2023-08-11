<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-12" type="primary" size="small" @click="onCreate"
                 icon="el-icon-plus">
        新增宿舍
      </el-button>
    </template>

    <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(182)" v-loading="loading.table">
      <el-table-column label="宿舍单元" prop="building" min-width="100"></el-table-column>
      <el-table-column label="楼层" prop="floor" min-width="88"></el-table-column>
      <el-table-column label="房号" prop="roomNo" min-width="88"></el-table-column>
      <el-table-column label="房型" prop="roomType" min-width="88">
        <!-- <template slot-scope="{row}">
          {{ row.type | dormitoryType }}
        </template> -->
      </el-table-column>
      <!-- <el-table-column label="宿舍状态" prop="status" min-width="88">
        <template slot-scope="{row}">
          {{ row.status | dormitoryStatus }}
        </template>
      </el-table-column> -->
      <el-table-column label="备注" prop="remark" min-width="100" show-tooltip-when-overflow></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)">编辑
          </el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <DialogFormDormitoryManage ref="DialogFormDormitoryManage" @success="getTableData"></DialogFormDormitoryManage>
  </TableView>
</template>

<script>
import DialogFormDormitoryManage from '@/components/dialog/Dormitory/DialogFormDormitoryManage'

export default {
  name: 'DormitoryManagement',
  components: {
    DialogFormDormitoryManage
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
          // status: {
          //   label: '宿舍状态',
          //   options: 'dormitoryStatus'
          // }
        },
        side: 'roomNo:房号'
      },
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
    params() {
      return {
        ...this.filterData
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
      let {code, data} = await this.$api.Dormitory.dormitoryPage(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    // 操作 - 新增
    onCreate() {
      this.$refs.DialogFormDormitoryManage.open({
        type: 'Create'
      })
    },
    // 操作 - 编辑
    onEdit(item) {
      this.$refs.DialogFormDormitoryManage.open({
        type: 'Edit',
        formData: item
      })
    },
    // 操作 - 删除
    async onDelete({id, roomNo}) {
      await this.$confirm(`确认删除宿舍“${roomNo}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Dormitory.dormitoryDelete({idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete()
      await this.getTableData()
    }
  }
}
</script>

<style scoped lang="stylus">
</style>
