<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-8" type="primary" size="small" @click="onCreate"
                 icon="el-icon-plus">
        添加讲师
      </el-button>
    </template>

    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(188)" v-loading="loading.table"
              @selection-change="selectionList = $event.map(item=>item.id)">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <!--      <el-table-column type="selection" width="50" ></el-table-column>-->
      <el-table-column type="index" label="序号" width="55" ></el-table-column>

      <el-table-column label="讲师姓名" prop="realName" min-width="88"></el-table-column>
      <el-table-column label="头像" prop="headImgUrl" width="110">
        <template slot-scope="{row}">
          <el-image class="width-64 height-64 flex" fit="cover" :src="row.headImgUrl"></el-image>
        </template>
      </el-table-column>
      <!--      <el-table-column label="联系方式" prop="phone" width="110" ></el-table-column>-->
      <el-table-column label="讲师简介" prop="intro" min-width="180">
        <template slot-scope="{row}">
          <el-tooltip effect="light" :content="row.intro">
            <div class="text-ellipsis-3 height-64">{{ row.intro }}</div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="所属机构" prop="orgName" min-width="120" show-tooltip-when-overflow></el-table-column>

      <el-table-column label="显示状态" prop="enabled" width="72" >
        <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
      </el-table-column>

      <el-table-column label="排序数值" prop="sort" width="156">
        <template slot-scope="{row}">
          <div v-if="row.editable">
            <el-input-number class="width-64 margin-right-8" size="small" controls-position="right"
                             v-model="row.sortInput" :min="0" :max="999"
                             @keydown.native.190.110.prevent></el-input-number>
            <el-button type="text" size="small"
                       @click="onSortSave(row)">保存
            </el-button>
            <el-button type="text" size="small"
                       @click="onSortEdit(row)">取消
            </el-button>
          </div>
          <div v-else>
            <el-button type="text" size="small" @click="onSortEdit(row)">
              <span class="text-3">{{ row.sort }}</span>
              <span class="el-icon-edit margin-left-4"></span>
            </el-button>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="60"  fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <DialogTableWLecturer ref="DialogTableWLecturer"></DialogTableWLecturer>
  </TableView>
</template>

<script>
import DialogTableWLecturer from '@/components/dialog/Plat/DialogTableWLecturer'

export default {
  name: 'wLecturer',
  components: {
    DialogTableWLecturer
  },
  data() {
    return {
      options: {
        total: 0,
        main: {},
        side: 'name:讲师名称'
      },
      tableData: [],
      filterData: {},
      selectionList: [],
      timer: {
        table: null
      },
      loading: {
        table: null
      }
    }
  },
  watch: {
    filterData: {
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
      let {code, data} = await this.$api.WLecturer.page(this.filterData)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    // 操作 - 新增讲师
    async onCreate() {
      await this.$refs.DialogTableWLecturer.open({
        type: 'WLecturerSelect'
      })

      await this.getTableData()
    },
    // 操作 - 删除
    async onDelete({id, realName}) {
      await this.$confirm(`确认删除讲师“${realName}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.WLecturer.delete({idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(realName)
      await this.getTableData()
    },

    // 操作 - 编辑排序
    async onSortEdit(item) {
      this.$set(item, 'editable', !item.editable)
      this.$set(item, 'sortInput', item.sort)
    },
    // 操作 - 编辑排序
    async onSortSave(item) {
      if (item.sort === item.sortInput) return this.onSortEdit(item)
      let {code} = await this.$api.WLecturer.update({
        id: item.id,
        sort: item.sortInput
      })
      if (code !== 200) return false
      this.getTableData().then()
      // this.$set(item, 'sort', item.sortInput)
      // this.$set(item, 'editable', !item.editable)
      // this.tableData.sort((a, b) => a.sort - b.sort)
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-input-number.is-controls-right .el-input__inner
  padding-right 32px
  padding-left 8px
</style>
