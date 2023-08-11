<template>
  <TableView :options="options" :params.sync="filterData">

    <template v-slot:main>
      <el-date-picker
        size="small"
        class="margin-bottom-8 margin-right-12"
        v-model="filterData.reportDate"
        value-format="yyyy-MM-dd"
        format="yyyy-MM-dd"
        type="date"
        placeholder="报事报修日期">
      </el-date-picker>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(186)" v-loading="loading.table">
      <template v-slot:empty>
        <Results v-if='!loading.table'></Results>
        <span v-else></span>
      </template>

      <el-table-column type="index" label="序号" width="55"></el-table-column>

      <el-table-column label="机构名称" prop="orgName" min-width="120"></el-table-column>

      <el-table-column label="报事报修类别" prop="category" min-width="100">
        <template slot-scope="{row}">
          {{row.category | repairsCategory}}
        </template>
      </el-table-column>

      <el-table-column label="提交时间" prop="createTime" min-width="120"></el-table-column>

      <el-table-column label="报事报修说明" prop="description" min-width="120"></el-table-column>


      <el-table-column label="操作" width="120" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" @click="onDetail(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <DialogRepairsDetail ref="DialogRepairsDetail"></DialogRepairsDetail>

  </TableView>
</template>

<script>
import DialogRepairsDetail from '@/components/dialog/Classes/DialogRepairsDetail'
import mxTableView from '@/components/mixins/mxTableView'
import {mapGetters} from 'vuex'

export default {
  name: 'RepairsManagement',
  components: {
    DialogRepairsDetail
  },
  mixins: [mxTableView],
  data() {
    return {
      options: {
        main: {
          category: {
            label: '报事报修类别',
            options: 'repairsCategory'
          },
        },
        side: 'orgName:机构名称,roomNo:教室编号'
      }
    }
  },
  computed: {
    ...mapGetters({
      repairsCategory: 'common/repairsCategory'
    }),
    tableDataAPI() {
      return this.$api.Classroom.repairsPage
    }
  },
  methods: {
    // 操作 - 详情
    onDetail(row) {
      this.$refs.DialogRepairsDetail.open(row)
    },
    
  }
}
</script>

<style scoped lang="stylus">
.el-date-editor
  width 140px
</style>
