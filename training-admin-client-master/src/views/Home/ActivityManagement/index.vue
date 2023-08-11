<template>
  
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-12" type="primary" size="small" @click="onCreate" icon="el-icon-plus">
        新增活动
      </el-button>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(188)" v-loading="loading.table" >
      <template v-slot:empty>
        <Results v-if='!loading.table'></Results>
        <span v-else></span>
      </template>

      <!-- <el-table-column type="selection" width="55"></el-table-column> -->

      <el-table-column label="活动名称" prop="" min-width="230">
        <template slot-scope="{ row }">
          <div class="flex start-center">
            <div>
              <el-image class="width-60 height-34" :style="{ borderRadius: '4px' }" :src="row.coverUrl" fit="cover"></el-image>
            </div>
            <span class="margin-left-12">{{ row.title }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="活动时间" prop="activityTime" min-width="180">
        <template slot-scope="{ row }">
          <span v-if="!row.activityTime">--</span>
          <span v-else>{{ row.activityTime | formatTime('yyyy年MM月DD日 HH时') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" prop="createTime" min-width="180"></el-table-column>

      <el-table-column label="上架时间" prop="shelfTime" min-width="180">
        <template slot-scope="{ row }">
          <span v-if="row.shelfStatus">{{row.shelfTime}}</span>
          <span v-else>--</span>
        </template>
      </el-table-column>

      <el-table-column label="排序" prop="sort" width="55"></el-table-column>

      <el-table-column label="状态" prop="shelfStatus" width="100">
        <EleDot slot-scope="{ row }" type="doShelf" :id="row.shelfStatus"></EleDot>
      </el-table-column>

      <el-table-column label="操作" prop="" width="200" fixed="right">
        <div class="flex start-center" slot-scope="{ row }">
          <el-button type="text" size="small" @click="onView(row)">详情</el-button>
          <el-button type="text" size="small" @click="onEdit(row)">编辑</el-button>
          <el-button type="text" size="small" @click="onDelete(row)" v-if="!row.shelfStatus">删除</el-button>
          <EleEnabledSwitch type="doShelf" v-model="row.shelfStatus" @change="onShelfStatus(row)"></EleEnabledSwitch>
        </div>
      </el-table-column>
    </el-table>

    
  </TableView>

</template>

<script>
import { mapGetters } from 'vuex'
import mxTableView from '@/components/mixins/mxTableView'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'

export default {
  name: 'Activity',
  mixins: [mxTableView, mxBaseDialog],
  components: {
  },
  data () {
    return {
      options: {
        main: {
          shelfStatus: {
            label: '活动状态',
            options: "doShelf"
          }
        },
        side: 'title:活动名称'
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled'
    }),
    tableDataAPI() {
      return this.$api.Activity.page
    },
    // params () {}
  },
  // watch: {},
  methods: {
    // 操作 - 新建活动
    onCreate() {
      this.$router.push({ name: 'CreateActivity', params: { type: 'Create' } })
    },
    // 操作 - 活动详情
    onView(item) {
      this.$router.push({
        name: 'CreateActivity', 
        params: {
          type: 'Detail' 
        },
        query: {
          id: item.id
        }
      })
    },
    // 操作 - 编辑活动
    onEdit(item) {
      this.$router.push({ 
        name: 'CreateActivity', 
        params: {
          type: 'Edit'
        },
        query: {
          id: item.id
        }
      })
    },
    // 操作 - 上架/下架
    async onShelfStatus(params) {
      let { code } = await this.$api.Activity.updateShelfStatus({
        ids: [params.id],
        shelfStatus: params.shelfStatus
      })
      if (code !== 200) return false
      this.$msg[params.shelfStatus ? 'OnShelve' : 'OffShelve'](params.title, false)
      await this.getTableData()
    },
    // 操作 - 删除
    async onDelete(params) {
      await this.$confirm(`是否删除活动"${params.title}"？`, {
         title: '删除确认',
         type: 'warning' 
        })
      let { code } = await this.$api.Activity.delete({
        idList: [params.id]
      })
      if (code !== 200) return false
      this.$msg.Delete(params.title, false)
      await this.getTableData()
    }
  }
}
</script>

<style scoped></style>
