<template>
  <el-dialog :visible.sync="visible" append-to-body title="选择负责人" :close-on-click-modal="false" :before-close="close" width="600px">

    <span class="margin-right-2">筛选负责人类型:</span>
    <el-select class="width-200" v-model="principalType" filterable placeholder="请选择" size="small">
      <el-option v-for="(item, index) in principalTypeList" :key="index" :label="item.name" :value="item.id"></el-option>
    </el-select>

    <TableView :options="options" :params.sync="filterData">
    
      <template v-if="principalType === 'user'">
        <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(500)" 
                v-loading="loading.table"  @row-click="onSelect" :row-class-name="rowClassName">
          <!-- <el-table-column type="selection" width="55"></el-table-column> -->
          <el-table-column type="index" label="序号" width="88"></el-table-column>
          <el-table-column label="用户名称" prop="realName" min-width="200"></el-table-column>
          
        </el-table>
      </template>
      
      <template v-if="principalType === 'role'">
        <el-table class="margin-top-12" :data="tableData" :height="$utils.FullViewHeight(500)" 
                v-loading="loading.table"  @row-click="onSelect" :row-class-name="rowClassName">
          <el-table-column type="index" label="序号" width="88"></el-table-column>
          <el-table-column label="角色名称" prop="name" min-width="200"></el-table-column>
          
        </el-table>
      </template>
    
    </TableView>

  <div class="flex end-center margin-top-30">
    <el-button type="primary" size="medium" @click="onSubmit">保存</el-button>
    <el-button size="medium" @click="close">取消</el-button>
  </div>

  </el-dialog>
</template>

<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "DialogPickPrincipal",
  data() {
    
    return {
      visible: false, // 弹窗开关

      options: {
        total: 0,
        main: {
        },
        side: {
          // flowName: {
          //   label: '流程名称'
          // }
        }
      },
      filterData: {},
      tableData: [],
      timer: {
        table: null
      },
      loading: {
        table: false
      },
      principalType: 'user',
      selectionList: {}
    };
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      principalTypeList: 'common/principalTypeList'
    }),
    params() {
      return {
        ...this.filterData,
        // principalType: this.principalType
      }
    }
  },

  watch: {
    principalType: {
      deep: true,
      handler() {
        clearTimeout(this.timer.table)
        this.timer.table = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    },
    params: {
      deep: true,
      handler() {
        clearTimeout(this.timer.table)
        this.timer.table = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    }
  },
  
  methods: {
    // 打开弹窗
    async open(data = {}) {

      this.visible = true
 
      return new Promise(async (resolve, reject) => {
        this.$once('handle', type => {
          if (type === 'submit') return resolve(this.selectionList)
          reject('取消')
        })
      })
    },
    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle', 'close')
    },
    // 确定提交
    async onSubmit() {
      this.visible = false
      this.$emit('handle', 'submit')
    },
    // 获取  负责人数据
    async getTableData() {
      this.loading.table = true
      // delete this.params.principalType
      // 获取用户数据
      if(this.principalType === 'user') {
        let { code, data } = await this.$api.PrincipalInfo.userPage({
          ...this.params,
          // enabled: 1
        })
        this.loading.table = false
        if(code !== 200) return false
        this.tableData = data.records
        this.options.total = data.total
      } else {
        // 获取角色数据
        let { code, data } = await this.$api.PrincipalInfo.rolePage({
          ...this.params,
          // enabled: 1
        })
        this.loading.table = false
        if(code !== 200) return false
        this.tableData = data.records
        this.options.total = data.total
      }
      
    },
    // 选择 负责人
    onSelect (row) {
      if(this.principalType === 'user') {
        this.selectionList = {
          ...row,
          managerType: 'user'
        }
      } else {
        this.selectionList = {
          ...row,
          managerType: 'role'
        }
      }
    },
    // 被选行的样式
    rowClassName ({ row }) {
      if (row.id === this.selectionList.id) return 'is-active'
      },
    },
};
</script>

<style scoped lang="stylus">
>>>.filter-control
  margin-bottom 0 !important

>>>.is-active
  td
    background-color BACKGROUND_COLOR !important
</style>
