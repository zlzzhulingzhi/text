<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="1100px">

    <TableView :options="options" :params.sync="filterData">

      <div class="flex">
        <div class="flex-2 width-0">
          <div class="flex between-center margin-bottom-12">
            <div></div>

            <div>
              <el-button type="primary" size="small" @click="onAddAll">全部添加</el-button>
            </div>
          </div>

          <el-table :data="tableData" v-loading="loading.table" :height="$utils.FullViewHeight(500)" @row-click="onAdd">
            <template v-slot:empty>
              <Results :type="typeof filterData.orgId === 'number'?'NoData':'PleaseSelect'"></Results>
            </template>

            <el-table-column label="讲师姓名" prop="realName" min-width="120" show-tooltip-when-overflow></el-table-column>
            <el-table-column label="头像" prop="headImgUrl" width="110">
              <template slot-scope="{row}">
                <el-image class="width-64 height-64" fit="cover" :src="row.headImgUrl"></el-image>
              </template>
            </el-table-column>
            <el-table-column label="讲师简介" prop="intro" min-width="88" show-tooltip-when-overflow></el-table-column>
            <el-table-column label="所属机构" prop="orgName" min-width="88" show-tooltip-when-overflow></el-table-column>
            <el-table-column label="显示状态" prop="enabled" width="72" >
              <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
            </el-table-column>
            <el-table-column label="操作" width="88" >
              <template slot-scope="{row}">
                <el-button type="text" icon="el-icon-circle-plus-outline"
                           v-if="selectionList.some(a => row.id === a.id) || row.added" disabled>
                  已添加
                </el-button>
                <el-button type="text" icon="el-icon-circle-plus-outline" @click.stop="onAdd(row)"
                           v-else>
                  添加
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="border-9 margin-12"></div>

        <div class="flex-1 width-0">
          <div class="flex between-center margin-bottom-12">
            <div class="line-height-32">已选中{{ selectionList.length }}个讲师</div>

            <div>
              <el-button type="primary" size="small" @click="selectionList = []">全部移除</el-button>
            </div>
          </div>
          <el-table :data="selectionList" :height="$utils.FullViewHeight(500)">
            <template v-slot:empty>
              <Results></Results>
            </template>

            <el-table-column label="讲师姓名" prop="realName" min-width="88"></el-table-column>
            <el-table-column label="头像" prop="headImgUrl" width="110">
              <template slot-scope="{row}">
                <el-image class="width-64 height-64 flex" fit="cover" :src="row.headImgUrl"></el-image>
              </template>
            </el-table-column>

            <el-table-column label="操作" width="88" >
              <template slot-scope="data">
                <el-button type="text" icon="el-icon-remove-outline" @click.stop="onRemove(data)">
                  移除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

    </TableView>
    <template v-slot:footer>
      <div>
        <el-button class="margin-right-14" type="primary" size="medium" @click="onSubmit"
                   :loading="loading.submit">
          确认添加
        </el-button>
        <el-button class="width-80" size="medium" @click="close">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'DialogTableWLecturer',
  data() {
    return {
      visible: false, // 弹窗开关

      options: {
        total: 0,
        main: {
          orgId: {
            options: 'organization',
            label: '选择机构'
          }
        }
      },
      filterData: {},

      tableData: [],
      selectionList: [],

      // 存储弹窗所需数据
      dialogType: 'WLecturerSelect',
      typeMapping: {
        WLecturerSelect: {
          title: '选择讲师',
          addApi: this.$api.WLecturer.create
        }
      },
      loading: {
        submit: false,
        table: false
      },
      timer: {
        table: null
      }
    }
  },
  computed: {
    ...mapGetters({
      courseType: 'common/courseType'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    permissions() {
      return this.$route.meta.childPermissions || {}
    }
  },

  watch: {
    filterData: {
      deep: true,
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
      // if (typeof this.filterData.orgId !== 'number') return this.tableData = []
      this.loading.table = true
      let {code, data: d} = await this.$api.WLecturer.selectPage(this.filterData)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = d.records
      this.options.total = d.total
    },
    // 打开弹窗
    async open(data = {}) {
      this.visible = true

      this.dialogType = data.type
      this.selectionList = []

      clearTimeout(this.timer.table)
      this.timer.table = setTimeout(() => {
        this.getTableData()
      }, 300)

      return new Promise(async (resolve, reject) => {
        this.$once('close', ({type, data}) => {
          if (type === 'success') return resolve(data)
          reject('取消')
        })
      })
    },
    // 关闭弹窗
    close() {
      this.$emit('close', {type: 'failed'})
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      if (!this.selectionList.length) return this.$message.warning('请选择讲师')
      this.loading.submit = true
      let {code} = await this.dialogInfo.addApi({
        orgId: -1,
        idList: this.selectionList.map(item => item.id)
      })

      this.loading.submit = false
      if (code !== 200) return false

      this.$emit('close', {type: 'success'})
      this.visible = false
    },

    // 操作 - 添加
    onAdd(item) {
      if (this.selectionList.some(a => item.id === a.id) || item.added) return false
      this.selectionList.push(item)
    },
    onRemove(data) {
      this.selectionList.splice(data.$index, 1)
    },
    onAddAll() {
      this.tableData.forEach(item => {
        if (this.selectionList.some(a => a.id === item.id)) return false
        this.selectionList.push(item)
      })
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-dialog
  .el-dialog__body
    padding 0 16px

    .table-view
      .filter-control
        margin-bottom 0

      .el-pagination
        flex 1

</style>