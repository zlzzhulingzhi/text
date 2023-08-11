<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="900px" class="popover-dialog">

    <TableView :options="options" :params.sync="filterData">
      <el-table :data="tableData" :height="$utils.FullViewHeight(135)" v-loading="loading.table"
                @selection-change="selectionList = $event.map(item => item.id)">
        <template v-slot:empty>
          <Results></Results>
        </template>

        <el-table-column label="姓名" prop="realName" min-width="88"></el-table-column>
        <el-table-column label="手机号" prop="phone" width="110"></el-table-column>

        <el-table-column label="组织" prop="deptFullName" min-width="120"></el-table-column>
        <el-table-column label="角色" prop="roleNames" min-width="100"></el-table-column>
        <el-table-column label="账号状态" prop="enabled" width="72">
          <EleDot slot-scope="{ row }" :id="row.enabled"></EleDot>
        </el-table-column>

        <el-table-column label="操作" width="80" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="small" @click="onSelect(row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
    </TableView>

  </el-dialog>
</template>

<script>
export default {
  name: 'DialogTableSelectUser',
  data() {
    return {
      visible: false, // 弹窗开关

      // 存储弹窗所需数据
      dialogType: 'BindUser',
      typeMapping: {
        BindUser: {title: '关联讲师', tableAPI: this.$api.Employee.page}
      },

      options: {
        total: 0,
        main: {
          roleId: {
            label: '角色',
            options: 'organizationRole'
          },
          enabled: {
            label: '账号状态'
          }
        },
        side: 'realName:姓名,phone:手机号'
      },
      filterData: {},
      tableData: [],

      outputData: null,

      loading: {
        table: false
      },
      timer: null
    }
  },
  computed: {
    dialogInfo() {
      return this.typeMapping[this.dialogType] || {}
    }
  },
  watch: {
    filterData: {
      immediate: true,
      deep: true,
      handler() {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    }
  },

  methods: {
    // 打开弹窗
    async open(data = {}) {
      this.visible = true

      this.dialogType = data.type

      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.outputData)
          reject('取消')
        })
      })
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      this.$emit('handle', 'submit')
      this.close()
    },
    // 操作 - 选择
    async onSelect(item) {
      this.outputData = item
      await this.onSubmit()
    },
    async getTableData() {
      this.loading.table = true
      let {data, code} = await this.dialogInfo.tableAPI(this.filterData)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-dialog .el-dialog__body
  padding 20px 24px 0

.table-view
  >>> .el-card
    &.filter-control
      margin-bottom 8px

    .el-card__body
      padding 0
</style>