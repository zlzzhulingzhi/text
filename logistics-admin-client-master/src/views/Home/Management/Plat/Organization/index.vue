<template>
  <TableView :options="options" :params.sync="filterData">
    <el-table class="margin-top-16" :data="tableData"
              :height="$utils.FullViewHeight(186)" v-loading="loading.table">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="index" label="序号" width="55"></el-table-column>

      <el-table-column label="机构名称" prop="name" min-width="100"></el-table-column>
      <el-table-column label="机构简称" prop="companyName" min-width="100"></el-table-column>
      <el-table-column label="机构Logo" prop="pcLogoUrl" width="88">
        <template slot-scope="{row}">
          <el-image :width="200" :src="row.pcLogoUrl">
            <div slot="error">
              <span class="font-13 text-9">&lt;未上传&gt;</span>
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="单位性质" prop="category" width="110">
        <template slot-scope="{row}">
          {{ row.category | orgCategory }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="150"></el-table-column>
      <el-table-column label="备注" prop="remark" min-width="88"></el-table-column>
      <el-table-column label="状态" prop="enabled" width="72">
        <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
      </el-table-column>

      <el-table-column label="操作" width="115" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" @click="onEdit(row)">授权</el-button>
          <EleEnabledSwitch v-model="row.enabled" @change="onEnabled(row)"></EleEnabledSwitch>
        </template>
      </el-table-column>
    </el-table>

    <DialogFormOrganization ref="DialogFormOrganization"></DialogFormOrganization>
  </TableView>
</template>

<script>
import {mapActions, mapGetters} from 'vuex'
import DialogFormOrganization from '@/components/dialog/Plat/DialogFormOrganization'

export default {
  name: 'PlatOrganization',
  components: {
    DialogFormOrganization
  },
  activated() {
    this.getTableData()
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          enabled: {
            label: '机构状态'
          }
        },
        side: 'name:机构名称,companyName:机构简称'
      },
      tableData: [],
      filterData: {},
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
      enabled: 'common/enabled'
    }),
    ...mapGetters({
      isDev: 'isDev'
    }),
    permissions() {
      return this.$route.meta.childPermissions || {}
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
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),
    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.$api.Organization.page(this.filterData)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    // 操作 - 启用/禁用
    onEnabled({id, enabled, name}) {
      this.onBatchEnabled(enabled, [id], name)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name) {
      this.loading.table = true
      let {code} = await this.$api.Organization.batchEnabled({
        idList,
        flag
      })
      this.loading.table = false
      if (code !== 200) return false
      this.$msg[flag ? 'Enabled' : 'Disabled'](name, !name)
      await this.getTableData()
      this.getDictionary(['organization']).then()
    },

    // 操作 - 授权[编辑]
    async onEdit(item) {

      await this.$refs.DialogFormOrganization.open({
        type: 'OrgPermissionEdit',
        formData: item
      })

    }
  }
}
</script>
