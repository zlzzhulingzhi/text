<!--课程评价-->
<template>
  <el-card shadow="never" :body-style="{ padding: '16px 0 0' }">
    <TableView :options="options" :params.sync="filterData">
        <template v-slot:footer>
            <el-button size="small" :disabled="!selectionList.length" @click="onBatchDelete">批量删除</el-button>
        </template>


      <!-- <div class="margin-bottom-16 flex between-center">
        <div>
          <el-button type="warning" size="small" :disabled="!selectionList.length" @click="onBatchDelete">批量删除</el-button>
        </div>
        <div>
          <el-button size="small" icon="el-icon-refresh" :loading="loading" @click="getListData">刷新</el-button>
        </div>
      </div> -->

      <el-table v-loading="loading" :data="listData" :height="$utils.FullViewHeight(310)" @selection-change="selectionList = $event.map(item=>item.id)">
        <template v-slot:empty>
          <Results v-if='!loading'></Results>
          <span v-else></span>
        </template>
        <el-table-column type="selection" width="50" ></el-table-column>
        <el-table-column :label="$t('user.Name')" prop="userNickName"></el-table-column>
        <el-table-column label="评分" prop="score"></el-table-column>
        <el-table-column label="评价" prop="content"></el-table-column>
        <el-table-column label="评论时间" prop="createTime"></el-table-column>
        <el-table-column label="操作" width="110"  fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="small" @click="onDelete(row)"><span class="text-error">删除</span></el-button>
          </template>
        </el-table-column>
      </el-table>
    </TableView>
  </el-card>
</template>

<script>
const name = '评论'

export default {
  data() {
    return {
      loading: true,
      listData: [],
      selectionList: [],
      options: {
        total: 0
      },
      filterData: {},
      timer: null
    }
  },
  computed: {
    params() {
      return {
        courseId: this.courseId,
        ...this.filterData
      }
    }
  },
  props: {
    courseId: {
      type: [String, Number]
    }
  },
  watch: {
    params: {
      deep: true,
      handler() {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
          this.getListData()
        }, 300)
      }
    }
  },
  methods: {
    // 列表
    async getListData() {
      if (!this.courseId) return
      this.loading = true
      let { code, data } = await this.$api.CourseComment.page(this.params)
      this.loading = false
      if (code !== 200) return false
      this.listData = data.records
      this.options.total = data.total
    },
    // 删除
    onDelete(row) {
      this.$confirm(`此操作将永久删除该${name}，是否继续？`, '提示', {
        type: 'warning'
      }).then(async () => {
        let { code } = await this.$api.CourseComment.delete({
          idList: [row.id]
        })
        if (code !== 200) return false
        this.$msg.Delete(name)
        this.getListData()
      }).catch(() => {})
    },
    // 批量删除
    onBatchDelete() {
      this.$confirm(`此操作将永久删除所选${name}，是否继续？`, '提示', {
        type: 'warning'
      }).then(async () => {
        let { code } = await this.$api.CourseComment.delete({
          idList: this.selectionList
        })
        if (code !== 200) return false
        this.$msg.Delete(name)
        this.getListData()
      }).catch(() => {})
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>