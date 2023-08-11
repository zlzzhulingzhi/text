<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" :width="dialogInfo.width">

    <template v-if="/^WCourseEdit$/.test(dialogType)">
      <div class="margin-top-16 margin-left-16 flex start-center">
        <b class="font-15 shrink-0 margin-right-16 text-3">课程名称：</b>

        <div>{{ formData.courseName }}</div>
      </div>

      <div class="margin-top-16 margin-left-16 flex start-center">
        <b class="font-15 shrink-0 margin-right-16 text-3">所属机构：</b>

        <div>{{ formData.orgName }}</div>
      </div>

      <!--      <div class="margin-top-16 margin-left-16 flex start-center">
              <b class="font-15 shrink-0 margin-right-16 text-3">上架状态：</b>

              <div>{{ formData.enabled ? '已上架' : '已下架' }}</div>
            </div>-->

      <div class="margin-top-16 margin-left-16 flex start-center">
        <b class="font-15 shrink-0 margin-right-16 text-3">精品课程：</b>

        <el-radio-group v-model="formData.gooded">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </div>

      <div class="margin-16 margin-right-0 flex start-center" v-if="WCourseCategoryTree.length">

        <b class="font-15 shrink-0 margin-right-16 text-3">课程分类：</b>

        <TreeSelect class=" margin-right-16 line-height-20" v-model="categoryIdList"
                    :options="WCourseCategoryTree"
                    :normalizer="node => ({id: node.id,label: node.name,children: (node.children || []).length ? node.children:undefined})"
                    :clearable="false" :defaultExpandLevel="Infinity"
                    :value-consists-of="'ALL_WITH_INDETERMINATE'"
                    placeholder="课程分类"
                    append-to-body
                    clearable
                    z-index="9999"
                    multiple
                    :limit="6"
                    :limitText="n=> `已选( ${n+6} )`"
                    flat
        ></TreeSelect>
      </div>

    </template>

    <template v-else-if="/^WCourseSelect$/.test(dialogType)">
      <div class="margin-top-16 margin-left-16 flex start-center" v-if="WCourseCategoryTree.length">

        <b class="font-15 shrink-0 margin-right-16 text-3">选择课程分类：</b>

        <TreeSelect class=" margin-right-16 line-height-20" v-model="categoryIdList"
                    :options="WCourseCategoryTree"
                    :normalizer="node => ({id: node.id,label: node.name,children: (node.children || []).length ? node.children:undefined})"
                    :clearable="false" :defaultExpandLevel="Infinity"
                    :value-consists-of="'ALL_WITH_INDETERMINATE'"
                    placeholder="课程分类"
                    append-to-body
                    clearable
                    z-index="9999"
                    multiple
                    :limit="6"
                    :limitText="n=> `已选( ${n+6} )`"
                    flat
        ></TreeSelect>
      </div>

      <div class="margin-top-16 margin-left-16 flex start-center">
        <b class="font-15 shrink-0 margin-right-16 text-3">是否精品课程：</b>

        <el-radio-group v-model="formData.gooded">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </div>
      <TableView :options="options" :params.sync="filterData">

        <div slot="other" class="margin-bottom-8 font-15">
          <b>选择课程：</b>
        </div>

        <div class="flex">
          <div class="flex-1 width-0">
            <div class="flex between-center margin-bottom-12">
              <div></div>

              <div>
                <el-button type="primary" size="small" @click="onAddAll">全部添加</el-button>
              </div>
            </div>

            <el-table :data="tableData" v-loading="loading.table" :height="$utils.FullViewHeight(500)"
                      @row-click="onAdd">
              <template v-slot:empty>
                <Results :type="typeof filterData.orgId === 'number'?'NoData':'PleaseSelect'"></Results>
              </template>

              <el-table-column label="课程名称" prop="courseName" min-width="88"></el-table-column>
              <el-table-column label="课程类型" prop="courseType" width="88" >
                <template slot-scope="{row}">
                  {{ row.courseType | courseType('name','-','value') }}
                </template>
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
              <div class="line-height-32">已选中{{ selectionList.length }}个课程</div>

              <div>
                <el-button type="primary" size="small" @click="selectionList = []">全部移除</el-button>
              </div>
            </div>
            <el-table :data="selectionList" :height="$utils.FullViewHeight(500)">
              <template v-slot:empty>
                <Results></Results>
              </template>

              <el-table-column label="课程名称" prop="courseName" min-width="88"></el-table-column>
              <el-table-column label="课程类型" prop="courseType" width="88" >
                <template slot-scope="{row}">
                  {{ row.courseType | courseType('name','-','value') }}
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
    </template>


    <template v-slot:footer>
      <div>
        <el-button class="margin-right-14" type="primary" size="medium" @click="onSubmit"
                   :loading="loading.submit">
          {{ dialogInfo.confirmText }}
        </el-button>
        <el-button class="width-80" size="medium" @click="close">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'DialogTableWCourse',
  data() {
    return {
      visible: false, // 弹窗开关

      options: {
        total: 0,
        main: {
          orgId: {
            options: 'organization',
            label: '机构筛选'
          }
        },
        side: {
          courseName: {
            label: '课程名称'
          }
        }
      },
      filterData: {},
      categoryIdList: [],

      formData: {
        gooded: 1
      },

      tableData: [],
      selectionList: [],

      // 存储弹窗所需数据
      dialogType: 'WCourseSelect',
      typeMapping: {
        WCourseSelect: {
          title: '添加课程',
          submitAPI: this.$api.WCourse.create,
          type: 'Create',
          width: '1100px',
          confirmText: '确认添加'
        },
        WCourseEdit: {
          title: '编辑课程',
          submitAPI: this.$api.WCourse.update,
          type: 'Edit',
          width: '600px',
          confirmText: '确定'
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
      courseType: 'common/courseType',
      WCourseCategoryTree: 'common/WCourseCategoryTree'
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
      let {code, data: d} = await this.$api.WCourse.selectPage(this.filterData)
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
      this.categoryIdList = data.categoryIdList ? [...data.categoryIdList] : []

      clearTimeout(this.timer.table)
      this.timer.table = setTimeout(() => {
        this.getTableData()
      }, 300)

      if (this.dialogInfo.type === 'Edit') {
        this.formData = {
          ...data.formData
        }
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
      if (!this.categoryIdList.length) return this.$message.warning('请选择课程分类')
      let params = null
      if (this.dialogInfo.type === 'Edit') {
        params = {
          orgId: -1,
          categoryIdList: this.categoryIdList,
          id: this.formData.id,
          gooded: this.formData.gooded
        }
      } else {
        if (!this.selectionList.length) return this.$message.warning('请选择课程')
        params = {
          orgId: -1,
          idList: this.selectionList.map(item => item.id),
          categoryIdList: this.categoryIdList,
          gooded: this.formData.gooded
        }
      }
      this.loading.submit = true

      let {code} = await this.dialogInfo.submitAPI(params)

      this.loading.submit = false
      if (code !== 200) return false
      this.$msg[this.dialogInfo.type]('课程')

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