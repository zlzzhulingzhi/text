<template>
  <el-container class="height-100p">
    <TreeSelectorAsSideBar ref="TreeSelectorAsSideBar" type="WCourse"
                           :visible.sync="treeVisible" :selection.sync="categorySelection"></TreeSelectorAsSideBar>

    <TableView class="flex-1 overflow-auto" :options="options" :params.sync="filterData">
      <template v-slot:main>
        <div class="flex center-center margin-bottom-8" v-if="!treeVisible">
          <div class="font-16 padding-8 padding-left-0 el-icon-s-unfold pointer text-main"
               @click="treeVisible = true"></div>
        </div>
        
        <div class="margin-bottom-8 margin-right-8 flex">
          <el-button type="primary" size="small" @click="onCreate"
                     icon="el-icon-plus" v-if="permissions.Create">
            添加课程
          </el-button>
        </div>
      </template>

      <el-table class="margin-top-16" :data="tableData"
                :height="$utils.FullViewHeight(188)"
                v-loading="loading.table"
                @selection-change="selectionList = $event.map(item=>item.id)">
        <template v-slot:empty>
          <Results></Results>
        </template>

        <!--        <el-table-column type="selection" width="50" ></el-table-column>-->
        <el-table-column type="index" label="序号" width="55"></el-table-column>

        <el-table-column label="课程名称" prop="courseName" min-width="88"></el-table-column>
        <el-table-column label="所属机构" prop="orgName" min-width="120" show-tooltip-when-overflow></el-table-column>
        <el-table-column label="课程分类" prop="categoryName" min-width="150" show-tooltip-when-overflow></el-table-column>
        <el-table-column label="是否精品课程" prop="gooded" width="100">
          <template slot-scope="{row}">
            {{ row.gooded ? '是' : '否' }}
          </template>
        </el-table-column>
        <el-table-column label="添加时间" prop="createTime" width="150"></el-table-column>

        <el-table-column label="操作" width="94" fixed="right"
                         v-if="permissions.Delete || permissions.Mark">
          <template slot-scope="{row}">
            <!--Mark - 由设置精品课程、拓展为编辑-->
            <el-button type="text" size="small" icon="el-icon-edit"
                       @click="onEdit(row)" v-if="permissions.Mark">
              编辑
            </el-button>
            <el-button v-if="permissions.Delete" type="text" size="small" icon="el-icon-delete"
                       @click="onDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <DialogTableWCourse ref="DialogTableWCourse"></DialogTableWCourse>
    </TableView>
  </el-container>
</template>

<script>
import DialogTableWCourse from '@/components/dialog/Plat/DialogTableWCourse'
import TreeSelectorAsSideBar from '@/components/form/TreeSelectorAsSideBar'
import {mapGetters} from 'vuex'

export default {
  name: 'wCourse',
  components: {
    DialogTableWCourse,
    TreeSelectorAsSideBar
  },
  data() {
    return {
      options: {
        total: 0,
        main: {},
        side: 'courseName:课程名称'
      },
      tableData: [],
      filterData: {},
      selectionList: [],

      treeVisible: true,
      categorySelection: null,

      timer: {
        table: null
      },
      loading: {
        table: false
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled',
      organization: 'common/organization',
      isDev: 'isDev'
    }),
    permissions() {
      return this.$route.meta.childPermissions || {}
    },
    params() {

      let categoryIdList = []
      if (this.categorySelection) {
        categoryIdList = this.$utils.ArrayFlat([this.categorySelection]).map(item => item.id)
      }

      return {
        ...this.filterData,
        categoryIdList
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
    },
    organization: {
      immediate: true,
      handler(val) {
        if (val && val[0]) {
          this.orgId = val[0].id
          this.orgName = val[0].name
        }
      }
    }
  },
  methods: {
    async getTableData() {
      if (this.params.orgId === null) return false
      this.loading.table = true
      let {code, data} = await this.$api.WCourse.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    // 操作 - 添加课程
    async onCreate() {
      await this.$refs.DialogTableWCourse.open({
        type: 'WCourseSelect',
        categoryIdList: this.categoryIdList
      })

      await this.getTableData()
    },
    // 操作 - 添加课程
    async onEdit(item) {
      await this.$refs.DialogTableWCourse.open({
        type: 'WCourseEdit',
        categoryIdList: item.categoryIdList,
        formData: item
      })

      await this.getTableData()
    },

    // 操作 - 删除
    async onDelete({id, courseName}) {
      await this.$confirm(`确认删除课程“${courseName}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.WCourse.delete({orgId: -1, idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(courseName)
      await this.getTableData()
    }
  }
}
</script>

<style scoped lang="stylus">
</style>
