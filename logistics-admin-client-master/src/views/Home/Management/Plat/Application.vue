<template>
  <TableView :options="options" :params.sync="filterData">
    <template v-slot:main>
      <el-button class="margin-bottom-8 margin-right-8" type="primary" size="small" @click="onCreate"
                 icon="el-icon-plus" v-if="permissions.Create">
        新增应用
      </el-button>
    </template>
    <template v-slot:footer>
      <template v-if="permissions.Enabled">
        <el-button v-for="item in enabled" :key="item.id" size="small" :disabled="!selectionList.length"
                   @click="onBatchEnabled(item.id,selectionList)">
          批量{{ item.name }}
        </el-button>
      </template>

      <!--        <el-button size="small" :disabled="!selectionList.length"
                         @click="onBatchDelete">
                批量删除
              </el-button>-->

    </template>
    <el-table class="margin-top-16" :data="tableData" :height="$utils.FullViewHeight(190 - 48 * !permissions.Enabled)"
              v-loading="loading.table" @selection-change="selectionList = $event.map(item=>item.id)">
      <template v-slot:empty>
        <Results></Results>
      </template>

      <el-table-column type="selection" width="50"
                       :selectable="row=>!disabledApp.test(row.permission)"></el-table-column>
      <el-table-column type="index" label="序号" width="55"></el-table-column>

      <el-table-column label="应用名称" prop="name" min-width="88"></el-table-column>
      <el-table-column label="访问地址" prop="uri" min-width="88"></el-table-column>
      <el-table-column label="主机名称" prop="host" min-width="88"></el-table-column>
      <el-table-column label="权限标识" prop="permission" min-width="88"></el-table-column>
      <el-table-column label="图标" prop="iconUrl" min-width="88">
        <template slot-scope="{row}">
          <EleIcon :src="row.iconUrl"></EleIcon>
        </template>
      </el-table-column>
      <el-table-column label="排序" prop="sort" min-width="88"></el-table-column>
      <el-table-column label="备注" prop="remark" min-width="88"></el-table-column>
      <el-table-column label="应用状态" prop="enabled" width="72">
        <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
      </el-table-column>

      <el-table-column label="操作" width="154" fixed="right"
                       v-if="permissions.Edit || permissions.Delete || permissions.Enabled">
        <template slot-scope="{row}">
          <el-button type="text" size="small" icon="el-icon-edit" @click="onEdit(row)" v-if="permissions.Edit">编辑
          </el-button>
          <el-button :disabled="disabledApp.test(row.permission)" type="text" size="small"
                     icon="el-icon-delete" @click="onDelete(row)" v-if="permissions.Delete">删除
          </el-button>
          <EleEnabledSwitch :disabled="disabledApp.test(row.permission)" v-model="row.enabled"
                            @change="onEnabled(row)" v-if="permissions.Enabled"></EleEnabledSwitch>
        </template>
      </el-table-column>
    </el-table>

    <DialogFormApp ref="DialogFormApp" @success="getTableData"></DialogFormApp>
  </TableView>
</template>

<script>
import DialogFormApp from '@/components/dialog/Plat/DialogFormApp'
import {mapActions, mapGetters, mapState} from 'vuex'

export default {
  name: 'Application',
  components: {
    DialogFormApp
  },
  props: {
    type: {
      type: String,
      default: 'Plat'
    }
  },
  data() {
    return {
      options: {
        total: 0,
        main: {
          enabled: {
            label: '应用状态'
          }
        },
        side: 'name:应用名称'
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
  computed: {
    ...mapGetters({
      enabled: 'common/enabled'
    }),
    ...mapState('config', {
      disabledApp: 'disabledApp'
    }),
    permissions() {
      return this.$route.meta.childPermissions || {}
    },
    pageInfo() {
      let typeMapping = {
        Plat: {
          appTypeId: 2
        },
        Org: {
          appTypeId: 3
        }
      }
      return typeMapping[this.type]
    },
    params() {
      return {
        ...this.filterData,
        appTypeId: this.pageInfo.appTypeId
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
    ...mapActions({
      getDictionary: 'common/getDictionary'
    }),
    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.$api.Application.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    // 操作 - 新增应用
    onCreate() {
      this.$refs.DialogFormApp.open({
        type: 'AppCreate',
        formData: {
          appTypeId: this.pageInfo.appTypeId
        }
      })
    },
    // 操作 - 编辑应用
    onEdit(item) {
      this.$refs.DialogFormApp.open({
        type: 'AppEdit',
        formData: item
      })
    },
    // 操作 - 删除
    async onDelete({id, name}) {
      await this.$confirm(`确认删除应用“${name}”吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Application.delete({idList: [id]})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete(name) // 删除“name”成功
      await this.getTableData()
      this.getDictionary(['app']).then()
    },
    // 操作 - 批量删除
    async onBatchDelete() {
      await this.$confirm(`确认删除${this.selectionList.length}个应用吗？`, {
        title: '删除确认'
      })
      this.loading.table = true
      let {code} = await this.$api.Application.delete({idList: this.selectionList})
      this.loading.table = false
      if (code !== 200) return false
      this.$msg.Delete('', true) // 批量删除成功
      await this.getTableData()
      this.getDictionary(['app']).then()
    },
    // 操作 - 启用/禁用
    onEnabled({id, name, enabled}) {
      this.onBatchEnabled(enabled, [id], name)
    },
    // 操作 - 批量启用/禁用
    async onBatchEnabled(flag, idList, name = '') {
      this.loading.table = true
      let {code} = await this.$api.Application.batchEnabled({
        idList,
        flag
      })
      this.loading.table = false
      if (code !== 200) return false
      // 批量启用||禁用“name”成功
      this.$msg[flag ? 'Enabled' : 'Disabled'](name, !name)
      await this.getTableData()
      this.getDictionary(['app']).then()
    }
  }
}
</script>

<style scoped>

</style>
