<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button size="small"  icon="el-icon-plus" class="margin-bottom-8 margin-right-12" type="primary" @click="onCreate">新增流程</el-button>
    </template>

    <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(182)"  ref="table"
              v-loading="loading.table" @expand-change="expandChange">
      <el-table-column type="expand" align="center" width="55">
        <template slot-scope="{row}">

          <el-table :data="row.children" height="300" class="margin-right-100">
            <el-table-column label="节点顺序" prop="sort" min-width="100"></el-table-column>
            <el-table-column label="节点名称" prop="nodeName" min-width="200"></el-table-column>
            <el-table-column label="负责人" prop="principalInfo" min-width="200"></el-table-column>
          </el-table>

        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="55"></el-table-column>
      <el-table-column label="流程名称" prop="flowName" width="200"></el-table-column>
      <el-table-column label="流程代码" prop="flowCode" width="200"></el-table-column>
      <el-table-column label="说明" prop="flowDesc" min-width="88"></el-table-column>
      
      <el-table-column label="排序" prop="sort" width="88"></el-table-column>

      <el-table-column label="状态" prop="enabled" width="88">
        <EleDot slot-scope="{ row }" :id="row.enabled" type="enabled"></EleDot>
      </el-table-column>

      <el-table-column label="操作" width="230" fixed="right">
        <template slot-scope="{row}">
          <EleEnabledSwitch v-model="row.enabled" type="enabled" @change="onEnabled(row)"></EleEnabledSwitch>
          <el-button type="text" size="small" class="margin-left-10" icon="el-icon-delete" :disabled="!row.enabled" @click="onDetail(row)">节点</el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onEdit(row)" :disabled="!row.enabled">编辑</el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <DialogFormFlow ref="DialogFormFlow"></DialogFormFlow>
    <DialogFlowDetail ref="DialogFlowDetail"></DialogFlowDetail>
  </TableView>
</template>

<script>
import {mapGetters} from 'vuex'
import DialogFormFlow from '@/components/dialog/FlowManage/DialogFormFlow'
import DialogFlowDetail from '@/components/dialog/FlowManage/DialogFlowDetail'

export default {
  name: 'FlowManage',
  components: {
    DialogFormFlow,
    DialogFlowDetail
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          
          enabled: {
            label: '状态',
            options: 'enabled'
          }
        },
        // side: 'roomNo:房号,orgName:单位名称'
        side: {
          flowName: {
            label: '流程名称'
          },
          flowCode: {
            label: '流程代码'
          },
        }
      },
      tableData: [],
      filterData: {},
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
      enabled: 'common/enabled'
    }),
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
      let {code, data} = await this.$api.Workflow.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    // 操作 - 新增
    async onCreate() {
      await this.$refs.DialogFormFlow.open({
        type: 'Create'
      })
      await this.getTableData();
    },
    // 操作 - 编辑
    async onEdit(params) {
      await this.$refs.DialogFormFlow.open({
        type: 'Edit',
        formData: params
      });
      await this.getTableData();
    },
    // 操作 - 详情
    onDetail(params) {
      this.$refs.DialogFlowDetail.open({
        formData: params
      })
    },
    // 启用/禁用
    async onEnabled(params) {
      this.loading.table = true
      let { code } = await this.$api.Workflow.updateBatch({
        enabled: params.enabled,
        ids: [params.id]
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg['Update']('流程状态', false)
      await this.getTableData()
    },
    // 单个删除
    async onDelete(params) {
      await this.$confirm(`确认删除“${params.flowName}”吗？`, {
        title: '删除确认',
        type: 'warning'
      })
      this.loading.table = true
      let {code} = await this.$api.Workflow.delete({idList: [params.id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete()
      await this.getTableData()
    },

    // 展开  触发时间获取详细数据
    async expandChange (row, expandedRows) {
      if(expandedRows.length) {
        let { code, data } = await this.$api.Workflow.nodePage({
        workflowId: row.id
      })
      if(code !== 200) return false
      row.children = data.records
      }
    },
    
   
  }
}
</script>

<style scoped lang="stylus">
>>>.el-table__expanded-cell
  padding 16px 50px
  position relative
  z-index 1970
</style>
