<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-12" type="primary" size="small" @click="onCreate"
                 icon="el-icon-plus">
        新增设备
      </el-button>
    </template>

    <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(182)" v-loading="loading.table">
      <el-table-column label="设备名称" prop="deviceName"></el-table-column>
      <el-table-column label="排序" prop="sort"></el-table-column>
      <el-table-column label="备注" prop="remark"></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-delete" @click="onEdit(row)">编辑</el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <DialogAddEquipment ref="DialogAddEquipment" @success="getTableData"></DialogAddEquipment>
  </TableView>
</template>

<script>
import DialogAddEquipment from '@/components/dialog/Classes/DialogAddEquipment'

export default {
  name: 'ClassroomMange',
  components: {
    DialogAddEquipment
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          
        },
        // side: 'deviceName:设备名称'
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
      let {code, data} = await this.$api.SceneDevice.sceneDevicePage({
        ...this.params,
        category: "Classroom"
      })
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    // 操作 - 新增
    onCreate() {
      this.$refs.DialogAddEquipment.open({
        type: 'Create'
      })
    },
    // 操作 - 编辑
    onEdit(item) {
      this.$refs.DialogAddEquipment.open({
        type: 'Edit',
        formData: item
      })
    },
    // 操作 - 删除
    async onDelete(item) {
      await this.$confirm(`确认删除设备“${item.deviceName}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.SceneDevice.sceneDeviceDelete({idList: [item.id]})
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
