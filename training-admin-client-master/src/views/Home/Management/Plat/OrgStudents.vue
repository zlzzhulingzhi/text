<template>
  <el-container class="height-100p">
    <TreeSelectorAsSideBar ref="TreeSelectorAsSideBar" type="AllStudents" 
                           :visible.sync="treeVisible" :selection.sync="categorySelection" ></TreeSelectorAsSideBar>

    <TableView class="overflow-auto flex-1" :options="options" :params.sync="filterData">
      <template v-slot:main>
        <div class="flex center-center margin-bottom-8" v-if="!treeVisible">
          <div class="font-16 padding-8 padding-left-0 el-icon-s-unfold pointer text-main" @click="treeVisible = true"></div>
        </div>
      </template>
      
      <el-table class="margin-top-16"
                v-loading="loading.table"
                :data="tableData"
                ref="table"
                :height="$utils.FullViewHeight(186)"
                @selection-change="selectionList = $event.map((item) => item.id)"
                @expand-change="expandChange"
                :row-class-name="tableRowClassName">
        <template v-slot:empty>
          <Results v-if="!loading"></Results>
          <span v-else></span>
        </template>

        <el-table-column type="expand">
          <template slot-scope="{ row }">
            <el-table :data="row.children" height="300" class="margin-right-100">
              <el-table-column label="机构名称" prop="orgName" min-width="100"></el-table-column>
              <el-table-column label="专题名称" prop="courseName" min-width="100"></el-table-column>
              <el-table-column label="班级" prop="clazzName" min-width="100"></el-table-column>
              <el-table-column label="班级状态" prop="state" width="100">
                <!-- <EleDot slot-scope="{ row }" :id="row.state" type="enabled"></EleDot> -->
                <template slot-scope="{ row }">
                  {{ row.state | classStateInfo }}
                </template>
              </el-table-column>
              <el-table-column label="教室编号" prop="roomNo" width="100">
                <template slot-scope="{row}">
                  {{ row.roomNo || '-' }}
                </template>
              </el-table-column>
              <el-table-column label="开课日期" prop="" width="300">
                <template slot-scope="{row}">
                  {{ row.startDate | formatTime('yyyy-MM-DD') }} - {{ row.endDate | formatTime('yyyy-MM-DD') }}
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>

        <el-table-column label="学员姓名" prop="realName" min-width="155">
          <template slot-scope="{ row }">
            <span class="pointer" @click="onSeeDetail(row.memberId)">{{ row.realName || '-' }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="手机号" prop="phone" min-width="155">
          <!-- <template slot-scope="{ row }">
            <div class="text-ellipsis" :title="row.lecturers.map(item => item.lecturerName).join('、')">
              {{ row.lecturers.map(item => item.lecturerName).join('、') }}
            </div>
          </template> -->
        </el-table-column>

        <el-table-column label="性别" prop="sex" min-width="156">
          <template slot-scope="{ row }"> {{ row.sex | sex }}</template>
        </el-table-column>

        <el-table-column label="状态" prop="enabled" min-width="156">
          <EleDot slot-scope="{ row }" :id="row.enabled" type="enabled"></EleDot>
          <!-- <template slot-scope="{ row }">
            {{ row.enabled | enabled }}
          </template> -->
        </el-table-column>



        <el-table-column label="当前班级" prop="clazzName" min-width="156">
          <template slot-scope="{ row }">
            {{ row.clazzName || '-' }}
          </template>
        </el-table-column>

        <!-- <el-table-column label="操作" width="100" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="small" @click="onDetail(row)">详情</el-button>
          </template>         
        </el-table-column> -->
      </el-table>
    </TableView>

    <DialogOrgStuInfo ref="DialogOrgStuInfo"></DialogOrgStuInfo>
  </el-container>
</template>


<script>
import mxTableView from '@/components/mixins/mxTableView'
import TreeSelectorAsSideBar from '@/components/form/TreeSelectorAsSideBar'
import DialogOrgStuInfo from '@/components/dialog/Org/DialogOrgStuInfo'
import {mapGetters} from 'vuex'


export default {
  name: 'OrgStudents',
  components: {
    TreeSelectorAsSideBar,
    DialogOrgStuInfo
  },
  mixins: [mxTableView],
  props: {

  },
  data() {
    return {
      options: {
        main: {

        },
        side: 'realName:学员姓名,phone:学员手机号,courseName: 课程专题'
      },
      treeVisible: true,
      categorySelection: null,

      orgId: undefined,

      classesData: []
    }
  },
  created() {

  },
  mounted() {

  },
  computed: {
    tableDataAPI() {
      return this.$api.orgStudents.page
    },
    params() {
      if(this.categorySelection) {
        let { id: orgId } = this.categorySelection
        this.orgId = orgId
      } else {
        this.orgId = undefined
      }
      return {
        ...this.filterData,
        orgId: this.orgId
      }
    },
    ...mapGetters({
      enabled: 'common/enabled',
      classStateInfo: 'common/classStateInfo',
      role: 'common/role',
      sex: 'common/sex',
      isLogin: 'system/isLogin'
    }),
  },
  watch: {

  },
  methods: {
    // 表格 - 展开
    async expandChange(row, expandedRows) {
      const isExpend = expandedRows.some(r => r.id === row.id) // 判断当前行展开状态
      if (isExpend && row.clazzName) {
          let {code, data} = await this.$api.orgStudents.pageClasses({
            id: row.memberId
          })
          if(code !== 200) return false
          row.children = data
      } else {
          this.$refs.table.toggleRowExpansion(row, false)
      }
    },
    // 给行添加类名
    tableRowClassName({row, rowIndex}) {
      if(!row.clazzName) {
        return 'HideExpand'
      }
    },
    // 查看学员详情
    async onSeeDetail(params) {
      let returnInfo = await this.$refs.DialogOrgStuInfo.open({
        id: params
      })
    }
  }
}
</script>


<style lang="stylus" scoped>
>>>.el-table__expanded-cell
  position relative
  z-index 1970

>>>.HideExpand
  .el-icon-arrow-right
    display: none 
</style>