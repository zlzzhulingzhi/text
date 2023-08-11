<template>
  
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-12" type="primary" size="small" @click="onCreate" icon="el-icon-plus">
        新增单位
      </el-button>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(188)" v-loading="loading.table" >
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="index" label="序号" width="60"></el-table-column>
      <el-table-column label="单位名称" prop="name" min-width="120"></el-table-column>
      <el-table-column label="联系人" prop="contactPerson" min-width="120"></el-table-column>
      <el-table-column label="联系电话" prop="contactNumber" min-width="120"></el-table-column>
      <el-table-column label="排序号" prop="sort"></el-table-column>
      <el-table-column label="备注" prop="remark"></el-table-column>
      <el-table-column label="单位状态" prop="enabled" min-width="120">
        <EleDot slot-scope="{ row }" :id="row.enabled" type="enabled"></EleDot>
      </el-table-column>
      
      <el-table-column label="操作" width="180" fixed="right">
        <template slot-scope="{ row }">
          <EleEnabledSwitch v-model="row.enabled" type="enabled" @change="onEnabled(row)"></EleEnabledSwitch>
          <el-button type="text" size="small" @click.stop="onEdit(row)" class="margin-left-10">编辑</el-button>
          <el-button type="text" size="small" @click.stop="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <DialogFormOrEditUnit ref="DialogFormOrEditUnit" @success="getTableData"></DialogFormOrEditUnit>
  </TableView>
</template>

<script>
import DialogFormOrEditUnit from '@/components/dialog/Unit/DialogFormOrEditUnit'
import { mapGetters } from 'vuex'

export default {
  components: {
    DialogFormOrEditUnit
  },
  data () {
    return {
      options: {
        total: 0,
        main: {
          // discountType: {
          //   label: '状态',
          //   options: 'discountType'
          // }
          // tradeDate: {
          //   order: 300,
          // },
        },
        side: 'name:单位名称'
      },
      tableData: [],
      filterData: {},
      selectionList: [],
      timer: {
        table: null
      },
      loading: {
        table: null
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled'
    }),
    params () {
      // let { startDate, endDate } = this.filterData
      // startDate = startDate && startDate + ' 00:00:00'
      // endDate = endDate && endDate + ' 23:59:59'
      return {
        ...this.filterData,
        // startDate,
        // endDate
      }
    }
  },
  watch: {
    filterData: {
      deep: true,
      immediate: true,
      handler () {
        clearTimeout(this.timer.table)
        this.timer.table = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    }
  },
  methods: {
    async getTableData () {
      this.loading.table = true
      let { code, data } = await this.$api.Unit.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    // 新增
    onCreate() {
      this.$refs.DialogFormOrEditUnit.open({
        type: 'Create'
      })
    },
    // 编辑
    onEdit(item) {
      this.$refs.DialogFormOrEditUnit.open({
        type: 'Edit',
        formData: item
      })
    },
    // 启用/禁用
    async onEnabled(params) {
      this.loading.table = true
      let { code } = await this.$api.Unit.update({
        ...params,
        enabled: params.enabled
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg['Update']('单位状态', false)
      await this.getTableData()
    },
    
    // 删除
    async onDelete(params) {
      await this.$confirm(`确认删除单位“${params.name}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let { code } = await this.$api.Unit.delete({ idList: [params.id] })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(params.name)
      await this.getTableData()
    }
  }
}
</script>

<style scoped></style>
