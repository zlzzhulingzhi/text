<template>
  <el-container class="height-100p">
    <TreeSelectorAsSideBar ref="TreeSelectorAsSideBar" type="UnitList" :props="{label: 'name'}"
                            :visible.sync="treeVisible" :selection.sync="unitInfo"></TreeSelectorAsSideBar>

    <TableView  class="flex-1 overflow-auto" :options="options" :params.sync="filterData">
      <template slot="main"></template>
      <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(188)" v-loading="loading.table">
        <template v-slot:empty>
          <Results></Results>
        </template>

        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column label="职员姓名" prop="realName" min-width="120"></el-table-column>
        <el-table-column label="手机号" prop="phone" min-width="120"></el-table-column>
        <el-table-column label="性别" prop="sex" min-width="120">
          <template slot-scope="{row}">
            {{row.sex | sex}}
          </template>
        </el-table-column>
        <el-table-column label="注册时间" prop="createTime" min-width="120">

        </el-table-column>
        <!-- <el-table-column label="状态" prop="enabled" width="120">
          <template slot-scope="{row}">
            {{row.enabled | enabled}}
          </template>
        </el-table-column> -->
        <!-- <el-table-column label="操作" width="140" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="small" @click.stop="">编辑</el-button>
            <el-button type="text" size="small" @click.stop="">删除</el-button>
          </template>
        </el-table-column> -->
      </el-table>
    </TableView>
  </el-container>
</template>

<script>
  import {mapGetters} from 'vuex';
  import TreeSelectorAsSideBar from '@/components/form/TreeSelectorAsSideBar'
export default {
  components: {
    TreeSelectorAsSideBar
  },
  data () {
    return {
      options: {
        total: 0,
        main: {
        },
        side: 'realName:职员姓名,phone:手机号'
      },
      tableData: [
      ],
      filterData: {},
      selectionList: [],
      timer: {
        table: null
      },
      loading: {
        table: null
      },
      treeVisible: true,
      unitInfo:null
    }
  },
  computed: {
    ...mapGetters({
      sex: 'common/sex',
      enabled: 'common/enabled'
    }),
    params () {
      
      return {
        ...this.filterData,
        unitId: this.unitInfo?.id
        
      }
    }
  },
  watch: {
    params: {
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
      let { code, data } = await this.$api.Unit.userPage(this.params)
      this.loading.table = false
      console.log(data);
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    
  }
}
</script>

<style scoped></style>
