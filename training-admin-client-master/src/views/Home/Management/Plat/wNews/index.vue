<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-8" type="primary" size="small" @click="onCreate"
                 icon="el-icon-plus">新建资讯
      </el-button>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(188)" v-loading="loading"
              @selection-change="selectionList = $event.map(item=>item.id)">
      <template v-slot:empty>
        <Results></Results>
      </template>
      <!--      <el-table-column type="selection" width="50"  :selectable="row=>!row.code"></el-table-column>-->
      <el-table-column type="index" label="序号" width="55" ></el-table-column>

      <el-table-column label="封面" prop="coverUrl" width="100">
        <template slot-scope="{row}">
          <el-image :src="row.coverUrl" fit="cover" :key="row.createTime + new Date().getTime()" :lazy="true"
                    :style="{ width: '100px', height: '56px', }">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="标题" prop="title" min-width="88"></el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="160"></el-table-column>
      <el-table-column label="状态" prop="shelfStatus" width="72">
        <EleDot slot-scope="{row}" :id="row.shelfStatus" type="shelfStatus"></EleDot>
      </el-table-column>
      <el-table-column label="操作" width="176"  fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-view" @click="onView(row)">查看</el-button>
          <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)">编辑</el-button>
          <el-button type="text" size="small" icon="el-icon-top" @click="offShelf(row)" v-if="row.shelfStatus">下架
          </el-button>
          <el-button type="text" size="small" icon="el-icon-bottom" @click="onShelf(row)" v-else>上架</el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </TableView>
</template>

<script>
import {mapState} from 'vuex'

const name = '资讯'

export default {
  name: 'wNews',
  data() {
    return {
      loading: false,
      options: {
        total: 0,
        side: 'title:关键词搜索'
      },
      filterData: {},
      tableData: [],
      selectionList: [],
      timer: null
    }
  },
  computed: {
    ...mapState('config', {
      domain: 'domain'
    }),
    params() {
      return {
        ...this.filterData,
        categoryIdList: this.filterData.categoryIdList ? [this.filterData.categoryIdList] : []
      }
    }
  },
  watch: {
    params: {
      deep: true,
      immediate: true,
      handler() {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    }
  },
  methods: {
    async getTableData() {
      this.loading = true
      let {code, data} = await this.$api.WNews.page(this.params)
      this.loading = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },
    onCreate() {
      this.$router.push({name: 'wNewsCreate', params: {type: 'Create'}})
    },
    onView(row) {
      window.open(`${this.domain.platform}/#/News/${row.id}`, '_blank')
    },
    onEdit(row) {
      this.$router.push({name: 'wNewsCreate', params: {type: 'Edit'}, query: {id: row.id}})
    },
    async onShelf(row) {
      let {code} = await this.$api.WNews.batchStatus({
        idList: [row.id],
        flag: 1
      })
      if (code !== 200) return false
      this.$set(row, 'shelfStatus', 1)
      this.$msg.OnShelve()
    },
    async offShelf(row) {
      let {code} = await this.$api.WNews.batchStatus({
        idList: [row.id],
        flag: 0
      })
      if (code !== 200) return false
      this.$set(row, 'shelfStatus', 0)
      this.$msg.OffShelve()
    },
    onDelete(row) {
      this.$confirm(`此操作将永久删除该${name}，是否继续？`, '提示', {
        type: 'warning'
      }).then(async () => {
        let {code} = await this.$api.WNews.delete({
          idList: [row.id]
        })
        if (code !== 200) return
        this.$msg.Delete(name)
        this.getTableData()
      }).catch(() => {
      })
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>