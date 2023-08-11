<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="1100px">

    <TableView :options="options" :params.sync="filterData">

      <div class="flex">
        <div class="flex-1 width-0">
          <div class="flex between-center margin-bottom-12">
            <div></div>

            <div>
              <el-button type="primary" size="small" @click="onAddAll">全部添加</el-button>
            </div>
          </div>

          <el-table :data="tableData" v-loading="loading.table" :height="$utils.FullViewHeight(500)">
            <template v-slot:empty>
              <Results :type="typeof filterData.orgId === 'number'?'NoData':'PleaseSelect'"></Results>
            </template>


            <template v-if="dialogType === 'LiveSelectInWPage'">
              <el-table-column label="直播名称" prop="liveName" min-width="88"></el-table-column>
              <el-table-column label="课程名称" prop="courseName" min-width="88"></el-table-column>
            </template>
            <template v-else-if="dialogType === 'LecturerSelectInWPage'">
              <el-table-column label="讲师名称" prop="realName" min-width="88"></el-table-column>
              <el-table-column label="头衔" prop="title" min-width="88"></el-table-column>
            </template>
            <template v-else-if="dialogType === 'CourseSelectInWPage'">
              <el-table-column label="课程名称" prop="courseName" min-width="88"></el-table-column>
              <el-table-column label="课程类型" prop="courseType" width="88" >
                <template slot-scope="{row}">
                  {{ row.courseType | courseType('name','-','value') }}
                </template>
              </el-table-column>
            </template>


            <el-table-column label="操作" width="88" >
              <template slot-scope="{row}">
                <el-button type="text" icon="el-icon-circle-plus-outline"
                           v-if="selectionList.some(a => row.id === a.id)" disabled>
                  已添加
                </el-button>
                <el-button type="text" icon="el-icon-circle-plus-outline" @click="onAdd(row)"
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
            <div class="line-height-32">已选中{{ selectionList.length }}个课程</div>

            <div>
              <el-button type="primary" size="small" @click="selectionList = []">全部移除</el-button>
            </div>
          </div>
          <el-table :data="selectionList" :height="$utils.FullViewHeight(500)">
            <template v-slot:empty>
              <Results></Results>
            </template>


            <template v-if="dialogType === 'LiveSelectInWPage'">
              <el-table-column label="直播名称" prop="liveName" min-width="88"></el-table-column>
              <el-table-column label="课程名称" prop="courseName" min-width="88"></el-table-column>
            </template>
            <template v-else-if="dialogType === 'LecturerSelectInWPage'">
              <el-table-column label="讲师名称" prop="realName" min-width="88"></el-table-column>
              <el-table-column label="头衔" prop="title" min-width="88"></el-table-column>
            </template>
            <template v-else-if="dialogType === 'CourseSelectInWPage'">
              <el-table-column label="课程名称" prop="courseName" min-width="88"></el-table-column>
              <el-table-column label="课程类型" prop="courseType" width="88" >
                <template slot-scope="{row}">
                  {{ row.courseType | courseType('name','-','value') }}
                </template>
              </el-table-column>
            </template>


            <el-table-column label="操作" width="88" >
              <template slot-scope="data">
                <el-button type="text" icon="el-icon-remove-outline" @click="onRemove(data)">
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

export default {
  name: 'DialogSelectWPage',
  data() {
    return {
      visible: false, // 弹窗开关

      options: {
        total: 0,
        main: {}
      },
      filterData: {},

      tableData: [],
      selectionList: [],

      // 存储弹窗所需数据
      dialogType: 'LiveSelectInWPage',
      typeMapping: {
        // 选择直播【微页面】
        LiveSelectInWPage: {
          title: '选择直播',
          tableAPI: this.$api.CourseLive.preparePage
        },
        // 选择讲师【微页面】
        LecturerSelectInWPage: {
          title: '选择讲师',
          tableAPI: this.$api.Lecturer.page
        },
        // 选择课程【微页面】
        CourseSelectInWPage: {
          title: '选择课程',
          tableAPI: this.$api.Course.page
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
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
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
      this.loading.table = true
      let {code, data: d} = await this.dialogInfo.tableAPI({
        ...this.filterData,
        shelfStatus: 1
      })
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = d.records
      this.options.total = d.total
    },
    // 打开弹窗
    async open(data = {}) {
      this.visible = true

      this.dialogType = data.type

      if (data.idList && data.idList.length) {
        let {data: d} = await this.dialogInfo.tableAPI({
          idList: data.idList,
          current: 1,
          size: 999
        })
        this.selectionList = (d || {}).records || []
      } else {
        this.selectionList = []
      }

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
      let data = [...this.selectionList]
      this.$emit('close', {type: 'success', data})
      this.visible = false
    },

    // 操作 - 添加
    onAdd(item) {
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