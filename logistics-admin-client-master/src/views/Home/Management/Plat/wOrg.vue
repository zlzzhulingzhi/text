<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <div class="margin-bottom-8 margin-right-8 flex">
        <el-button type="primary" size="small" @click="onCreate"
                   icon="el-icon-plus" v-if="permissions.Create">
          添加机构
        </el-button>
      </div>
    </template>

    <el-table class="margin-top-16" :data="tableData"
              :height="$utils.FullViewHeight(188)"
              v-loading="loading.table">

      <template v-slot:empty>
        <Results></Results>
      </template>

      <!--      <el-table-column type="selection" width="50" ></el-table-column>-->
      <el-table-column type="index" label="序号" width="55" ></el-table-column>

      <el-table-column label="机构名称" prop="name" min-width="150" show-tooltip-when-overflow></el-table-column>
      <el-table-column label="机构Logo" prop="pcLogoUrl" width="88">
        <template slot-scope="{row}">
          <el-image :width="200" :src="row.pcLogoUrl"></el-image>
        </template>
      </el-table-column>
      <!--      <el-table-column label="机构分类" prop="category" width="88">
              <template slot-scope="{row}">
                {{ row.category | orgCategory }}
              </template>
            </el-table-column>-->

      <el-table-column label="机构简介" prop="intro" min-width="180" show-tooltip-when-overflow></el-table-column>
      <el-table-column label="机构状态" prop="enabled" width="72" >
        <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
      </el-table-column>

      <el-table-column label="排序数值" prop="sort" width="156">
        <template slot="header">
          <el-tooltip effect="light" placement="top" content="该数值决定了平台官网的机构排序,排序数值越小，越排前">
            <div>排序数值<b class="margin-left-4 el-icon-warning-outline"></b></div>
          </el-tooltip>
        </template>
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

      <el-table-column label="操作" width="58" fixed="right"
                       v-if="permissions.Delete">
        <template slot-scope="{row}">
          <el-button v-if="permissions.Delete" type="text" size="small" icon="el-icon-delete"
                     @click="onDelete(row)">
            移除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <DialogTableWOrg ref="DialogTableWOrg"></DialogTableWOrg>
  </TableView>
</template>

<script>
import DialogTableWOrg from '@/components/dialog/Plat/DialogTableWOrg'
import {mapActions, mapGetters} from 'vuex'

export default {
  name: 'wOrg',
  components: {
    DialogTableWOrg
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          category: {
            label: '机构分类',
            options: 'orgCategory'
          }
        },
        side: 'name:机构名称'
      },
      tableData: [],
      filterData: {},

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
      enabled: 'common/enabled'
    }),
    permissions() {
      return this.$route.meta.childPermissions || {}
    },
    params() {
      return {
        ...this.filterData
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
    ...mapActions({
      getOrganization: 'common/getDictionary'
    }),
    async getTableData() {
      if (this.params.orgId === null) return false
      this.loading.table = true
      let {code, data} = await this.$api.WOrg.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },


    // 操作 - 添加机构
    async onCreate() {
      this.getOrganization(['organization']).then()
      await this.$refs.DialogTableWOrg.open({
        type: 'WOrgSelect'
      })

      await this.getTableData()
    },
    // 操作 - 移除
    async onDelete({id, name}) {
      await this.$confirm(`确认移除机构“${name}”吗？移除机构后，该机构将不会平台官网-合作机构显示。`, {
        title: '移除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.WOrg.delete({idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(name)
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
      let {code} = await this.$api.WOrg.update({
        id: item.id,
        sort: item.sortInput
      })
      if (code !== 200) return false
      this.getTableData().then()
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-input-number.is-controls-right .el-input__inner
  padding-right 32px
  padding-left 8px
</style>
