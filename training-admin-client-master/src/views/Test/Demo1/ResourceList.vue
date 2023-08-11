<template>
  <div class="flex">
    <el-card class="tree-card">
      <div class="width-200 overflow-auto">
        <el-select class="padding-16" v-model="appId" size="small">
          <el-option v-for="item in appList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
        <el-tree :data="treeMenuFilter" :props="{children: 'children',label: 'menuName'}" node-key="id"
                 @node-click="onNodeClick"
                 accordion></el-tree>
      </div>
    </el-card>

    <TableView class="table-view margin-left-16 flex-1"  :params.sync="filterData">
      <!-- <template v-slot:side>
        <el-button class="margin-bottom-8 margin-right-8" type="primary" size="small" @click="onCreate"
                   icon="el-icon-plus">
          新增资源
        </el-button>
      </template> -->

      <div class="margin-bottom-16 margin-top-16 flex between-center">
        <div>
            <el-input placeholder="所有类型"></el-input>
            <el-input placeholder="请输入名称资源搜索"></el-input>
            <el-button>搜索</el-button>
        </div>
        <div>
          <el-button type="warning" size="small" :disabled="!selectionList.length"
                     @click="onBatchDelete">
            新增资源
          </el-button>
        </div>
      </div>

      <el-table :data="tableData" :height="$utils.FullViewHeight(236)" v-loading="loading.table"
                @selection-change="selectionList = $event.map(item=>item.id)">
        <template v-slot:empty>
          <Results></Results>
        </template>

        <el-table-column type="selection" width="50" ></el-table-column>
        <el-table-column type="index" label="编号" width="55" ></el-table-column>

        <el-table-column label="文件名" prop="name" min-width="100"></el-table-column>
        <el-table-column label="文件类型" prop="uri" min-width="100"></el-table-column>
        <el-table-column label="大小" prop="iconUrl" min-width="72"></el-table-column>
        <el-table-column label="机构" prop="type" width="50" >
          <template slot-scope="{row}">
            {{ row.type | menuType }}
          </template>
        </el-table-column>
        <el-table-column label="分类" prop="sort" width="50" ></el-table-column>
        <el-table-column label="上传人" prop="remark" min-width="100" show-tooltip-when-overflow></el-table-column>
        <el-table-column label="上传时间" prop="enabled" width="72">
          <template slot-scope="{row}">
          <span v-for="item in enabled" :key="item.id" v-if="item.id === row.enabled" :class="`text-${item.status}`">
            <i class="dot" :class="`bg-${item.status}`"></i>
            {{ item.name }}
          </span>
          </template>
        </el-table-column>


        <el-table-column label="操作" width="150"  fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="small" @click="onEdit(row)">编辑</el-button>
            <el-button class="text-error" type="text" size="small" @click="onDelete(row)">删除</el-button>
            <el-button type="text" size="small" @click="onEnabled(row)">
              {{ enabled.find(a => a.id !== row.enabled).name }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

    </TableView>

    <DialogFormMenu ref="DialogFormMenu" @success="getPageData"></DialogFormMenu>
  </div>
</template>

<script>
// import DialogFormMenu from '@/components/dialog/DialogFormMenu'
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'Menu',
  components: {
    // DialogFormMenu
  },
  async created() {
    if (this.$route.meta.keepAlive) return false
    await this.getAppList()
    await this.getTreeTotal()
  },
  async activated() {
    await this.getAppList()
    await this.getTreeTotal()
  },
  data() {
    return {
      
      tableData: [],
      filterData: {},
      selectionList: [],

      treeMenu: [],
      parentId: 0,

      appList: [],
      appId: null,

      timer: {
        table: null
      },
      loading: {
        table: null
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      menuType: 'common/menuType'
    }),
    params() {
      return {
        ...this.filterData,
        parentId: this.parentId,
        appId: this.appId
      }
    },
    treeMenuFilter() {
    //   let fItem = this.appList.find(item => item.id === this.appId) || {name: '主目录'}
    //   return [
    //     {
    //       id: 0,
    //       menuName: fItem.name,
    //       children: this.treeMenu.filter(item => item.appId === this.appId)
    //     }
    //   ]
    }
  },
  watch: {
    params: {
    }
  },
  methods: {
    async getAppList() {
      let {code, data} = await this.$api.Application.list()
      if (code !== 200) return false
      this.appList = data
      if (data[0]) this.appId = data[0].id
    },
    async getPageData() {
      await this.getTreeTotal()
    },
    async getTreeTotal() {
      this.loading.tree = true
      let {code, data} = await this.$api.Menu.treeTotal({
        category: 'admin'
      })
      this.loading.tree = false
      if (code !== 200) return false
      /*this.treeMenu = [{
        id: 0,
        menuName: '主目录',
        children: data.menu || []
      }]*/
      this.treeMenu = data.menu || []
    },

    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.$api.Menu.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    // 操作 - 选择节点
    onNodeClick(d, node) {

    },
    // 操作 - 新增菜单
    onCreate() {
      this.$refs.DialogFormMenu.open({
        type: 'MenuCreate',
        formData: {
          parentId: this.parentId,
          appId: this.appId
        },
        treeMenu: this.treeMenuFilter
      })
    },
    // 操作 - 编辑菜单
    onEdit(item) {
      this.$refs.DialogFormMenu.open({
        type: 'MenuEdit',
        formData: item,
        treeMenu: this.treeMenuFilter
      })
    },
    // 操作 - 删除
    async onDelete({id, name}) {
      await this.$confirm(`确认删除菜单“${name}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Menu.delete({ids: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$message.success('删除成功')
      await this.getPageData()
    },
    // 操作 - 批量删除
    async onBatchDelete() {
      await this.$confirm(`确认删除${this.selectionList.length}个菜单吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Menu.delete({ids: this.selectionList.toString()})
      this.loading.table = false
      if (code !== 200) return false
      this.$message.success('删除成功')
      await this.getPageData()
    },
    // 操作 - 启用/禁用
    async onEnabled(item) {
      this.loading.table = true
      item.enabled = !item.enabled * 1
      // let {code} = await this.$api.Application.enabled({id:item.id, enabled:item.enabled})
      this.loading.table = false
      // if (code !== 200) return false
      // this.$message.success('操作成功')
    }
  }
}
</script>

<style scoped lang="stylus">
.table-view
  min-width 0

.tree-card
  >>> .el-card__body
    padding 0
</style>
