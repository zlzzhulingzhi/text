<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <div class="margin-bottom-8 margin-right-8 flex">
        <el-button type="primary" size="small" @click="onCreate"
                   icon="el-icon-plus" v-if="permissions.Create">
          新增
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

      <el-table-column type="index" label="序号" width="55" ></el-table-column>

      <el-table-column label="名称" prop="fileName" min-width="120" show-tooltip-when-overflow></el-table-column>
      <el-table-column label="图片" prop="filePath" width="222">
        <template slot-scope="{row}">
          <el-image class="height-40 flex" :src="row.filePath"></el-image>
        </template>
      </el-table-column>

      <el-table-column label="跳转链接" prop="openUrl" min-width="150" show-tooltip-when-overflow></el-table-column>
      <el-table-column label="状态" prop="enabled" width="72" >
        <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
      </el-table-column>

      <el-table-column label="排序" prop="sort" min-width="156">
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

      <el-table-column label="操作" width="155" fixed="right"
                       v-if="permissions.Edit || permissions.Delete || permissions.Enabled">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)"
                     v-if="permissions.Edit">编辑
          </el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="onDelete(row)"
                     v-if="permissions.Delete">删除
          </el-button>
          <EleEnabledSwitch v-model="row.enabled" @change="onEnabled(row)"
                            v-if="permissions.Enabled"></EleEnabledSwitch>
        </template>
      </el-table-column>
    </el-table>

    <DialogFormWBanner ref="DialogFormWBanner" @success="getTableData"></DialogFormWBanner>
  </TableView>
</template>

<script>
import DialogFormWBanner from '@/components/dialog/Plat/DialogFormWBanner'
import {mapGetters} from 'vuex'

export default {
  name: 'wBanner',
  components: {
    DialogFormWBanner
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          enabled: {
            label: '状态'
          }
        }
      },
      tableData: [],
      filterData: {},
      selectionList: [],

      timer: {
        table: null
      },
      loading: {
        table: false,
        tree: false
      }
    }
  },
  computed: {
    ...mapGetters({
      enabled: 'common/enabled'
    }),
    permissions() {
      return this.$route.meta.childPermissions || {}
    },
    params() {
      return {
        ...this.filterData,
        groupCode: 'fileBanner'
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
      if (this.params.orgId === null) return false
      this.loading.table = true
      let {code, data} = await this.$api.DecFile.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.tableData.sort((a, b) => a.sort - b.sort)
      this.options.total = data.total
    },

    // 操作 - 添加图片
    async onCreate() {
      await this.$refs.DialogFormWBanner.open({
        type: 'WBannerCreate'
      })

      await this.getTableData()
    },

    // 操作 - 编辑图片
    async onEdit(item) {
      await this.$refs.DialogFormWBanner.open({
        type: 'WBannerEdit',
        formData: item
      })
    },

    // 操作 - 删除
    async onDelete({id, fileName}) {
      await this.$confirm(`确认删除图片“${fileName}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.DecFile.delete({orgId: -1, idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(fileName)
      await this.getTableData()
    },
    // 操作 - 启用/禁用
    onEnabled({id, enabled, name}) {
      this.onBatchEnabled(enabled, [id], name)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name) {
      this.loading.table = true
      let {code} = await this.$api.DecFile.batchEnabled({
        idList,
        flag
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg[flag ? 'Enabled' : 'Disabled'](name, !name)
      await this.getTableData()
      this.getDictionary(['organizationRole']).then()
    },

    // 操作 - 编辑排序
    async onSortEdit(item) {
      this.$set(item, 'editable', !item.editable)
      this.$set(item, 'sortInput', item.sort)
    },
    // 操作 - 编辑排序
    async onSortSave(item) {
      if (item.sort === item.sortInput) return this.onSortEdit(item)
      let {code} = await this.$api.DecFile.update({
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
