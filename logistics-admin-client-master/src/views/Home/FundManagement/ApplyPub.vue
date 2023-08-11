<template>
  <div class="width-100p">
    <Breadcrumb class="margin-bottom-12 margin-left-16"></Breadcrumb>
    <TableView :options="options" :params.sync="filterData">
   
      <template v-slot:footer>
        <!-- <el-button  size="small" :disabled="!selectionList.length" @click="onBatchEnabled(item.id,selectionList)">
          批量审批
        </el-button> -->
      </template>

      <div class=" flex center-center">
        <div>
          <!--        <el-button v-for="item in enabled" :key="item.id" size="small" :disabled="!selectionList.length"-->
          <!--                   @click="onBatchEnabled(item.id,selectionList)">-->
          <!--          批量{{ item.name }}-->
          <!--        </el-button>-->
          <!--        <el-button type="warning" size="small" :disabled="!selectionList.length"
                           @click="onBatchDelete">
                  批量删除
                </el-button>-->
        </div>
      </div>

      <el-table class="margin-top-16"  :data="tableData" :height="$utils.FullViewHeight(215)" v-loading="loading.table" @selection-change="selectionList = $event.map(item=>item.id)">
        <template v-slot:empty>
          <Results v-if='!loading.table'></Results>
          <span v-else></span>
        </template>
        <el-table-column type="index" label="序号" width="88"></el-table-column>
        <el-table-column label="公示日期" prop="name" min-width="150"></el-table-column>
        <el-table-column label="类型" prop="className" min-width="150"></el-table-column>
        <el-table-column label="标题" prop="contacts" min-width="150"></el-table-column>
        <el-table-column label="操作" min-width="100">
          <template slot-scope="{row}">
            <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)" :disabled="Boolean(row.code && (row.code === 'orgMaster'))">审批
            </el-button>
            <!--          <EleEnabledSwitch v-model="row.enabled" @change="onEnabled(row)"
                                      :disabled="Boolean(row.code)"></EleEnabledSwitch>-->
          </template>
        </el-table-column>
      </el-table>

      <DialogFormClassroomManage ref="DialogFormClassroomManage" @success="getTableData"></DialogFormClassroomManage>
    </TableView>
  </div>

</template>

<script>
import DialogFormClassroomManage from '@/components/dialog/Classes/DialogFormClassroomManage'
import { DownloadBase } from 'knight-utils'
import { mapGetters } from 'vuex'

export default {
  name: 'ApplyPub',
  components: {
    DialogFormClassroomManage
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          
        },
      
      },
      tableData: [],
      filterData: {},
      selectionList: [],
      timer: {
        table: null
      },
      loading: {
        table: true
      },
      dataTime: '', // 资助申请时间
      beginMoney: null, //资助费用范围
      endMoney: null,
      tableArr: [], //定义空数组，作为临时存储每一行已处理的数据
      pos: 0
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      app: 'common/app',
      ApplyAuditSatus:'common/ApplyAuditSatus',
      fundingObject: 'common/fundingObject',
      passStatus: 'common/passStatus'
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
      let { code, data } = await this.$api.ApplyPubAPI.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.tableArr = []
      this.options.total = data.total
    },

    // 操作 - 新增班级
    onCreate() {
      this.$refs.DialogFormClassroomManage.open({
        type: 'OrgRoleCreate'
      })
    },
    // 导出文件
    onExport() {
      console.log('导出')
    },
    // 操作 - 编辑班级
    onEdit(item) {
      this.$refs.DialogFormClassroomManage.open({
        type: 'OrgRoleEdit',
        formData: item
      })
    },
    // 操作 - 启用/禁用
    onEnabled({ id, enabled, name }) {
      this.onBatchEnabled(enabled, [id], name)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name) {
      this.loading.table = true
      let { code } = await this.$api.ClassroomManageAPI.batchEnabled({
        idList,
        flag
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg[flag ? 'Enabled' : 'Disabled'](name, !name)
      await this.getTableData()
    }
  }
}
</script>

<style scoped lang="stylus">
.tooltip-content
  max-height 300px
.timer
  margin 0 12px 8px 0
  font-size 14px

  /* 隐藏筛选区域 */
>>>.filter-control
  display: none

  /* 表头 */
>>> .el-table .el-table__header-wrapper .el-table__header thead tr th .cell
  height 52px !important
  line-height 24px
  display flex
  align-items center

.accessory
  &:hover {
    color #c63636
    text-decoration underline
  }
</style>
