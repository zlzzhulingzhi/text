<template>
    <TableView :options="options" :params.sync="filterData">
      <!-- <template v-slot:footer>
        <el-button size="small" :disabled="!selectionList.length" @click="onBatchDelete">
          批量删除
        </el-button>
      </template> -->
      <template slot="main">
        <el-button class="margin-bottom-6 margin-right-12 margin-left-0" size="small" type="primary" @click="onAdd">新增专家</el-button>
      </template>
  
      <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(188)" v-loading="loading.table" @selection-change="selectionList = $event.map(item=>item.id)">
        <template v-slot:empty>
          <Results></Results>
        </template>
  
        <!-- <el-table-column type="selection" width="50"></el-table-column> -->
        <el-table-column type="index" label="序号" width="55"></el-table-column>
        <el-table-column label="姓名" prop="name" min-width="60"></el-table-column>
        <el-table-column label="性别" prop="esex" width="55"></el-table-column>
        <el-table-column label="身份证号" prop="idNumber" width="200"></el-table-column>
        <el-table-column label="职称" prop="title" min-width="88"></el-table-column>
        <el-table-column label="联系电话" prop="phone" width="120"></el-table-column>
        <el-table-column label="专业" prop="major" min-width="160"></el-table-column>
        <el-table-column label="邮箱" prop="email" min-width="160"></el-table-column>
        <el-table-column label="银行卡号" prop="bankAccount" width="180"></el-table-column>
        <el-table-column label="开户银行" prop="bank" min-width="160"></el-table-column>
        <el-table-column label="备注" prop="remark" min-width="160"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="small" class="margin-right-8" @click="onDetail(row)">详情</el-button>
            <el-button type="text" size="small" class="margin-right-8" @click="onEdit(row)">编辑</el-button>
            <el-button type="text" size="small" class="margin-right-8" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <DialogAddExpert ref="DialogAddExpert" @success="getTableData"></DialogAddExpert>
      <DialogExpertInfo ref="DialogExpertInfo"></DialogExpertInfo>
    </TableView>
  </template>
  
  <script>
  import DialogAddExpert from '@/components/dialog/FundManagement/DialogAddExpert'
  import DialogExpertInfo from '@/components/dialog/FundManagement/DialogExpertInfo'

  export default {
    name: 'OrderList',
    components: {
      DialogAddExpert,
      DialogExpertInfo
    },
    data () {
      return {
        options: {
          total: 10,
          side: 'name:请输入姓名,phone:请输入联系电话'
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
      params () {
        return {
          ...this.filterData
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
        let { code, data } = await this.$api.Expert.page(this.params)
        this.loading.table = false
        if (code !== 200) return false
        this.tableData = data.records
        this.options.total = data.total
      },
      
      // 删除
      async onDelete (params) {
        await this.$confirm(`确认删除专家“${params.name}”`, {
          title: '删除确认'
        })
        this.loading.table = true
        let { code } = await this.$api.Expert.delete({idList: [params.id]})
        this.loading.table = false
        this.$msg.Delete(params.name)
        await this.getTableData()
      },

      // 操作 - 批量删除
      // async onBatchDelete() {
      //   await this.$confirm(`确认删除${this.selectionList.length}个专家吗？`, {
      //     title: '删除确认'
      //   })
      //   this.loading.table = true
      //   let { code } = await this.$api.Expert.delete({ idList: this.selectionList })
      //   this.loading.table = false
      //   if (code !== 200) return false
      //   this.$msg.Delete('', true)
      //   await this.getTableData()
      // },

      // 新增专家信息
      async onAdd () {
        this.$refs.DialogAddExpert.open({
          type: 'Create'
        })
      },

      // 操作 - 编辑
      onEdit(item) {
        this.$refs.DialogAddExpert.open({
          type: 'Edit',
          formData: item
        })
      },

      // 操作 - 详情
      onDetail(item) {
        this.$refs.DialogExpertInfo.open({
          formData: item
        })
      }

    }
  }
  </script>
  
  <style lang="stylus" scoped>
    .el-button
      margin-left 0px
  </style>
  