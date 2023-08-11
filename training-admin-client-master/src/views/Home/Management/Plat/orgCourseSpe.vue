<template>
  <el-container class="height-100p">
    <TreeSelectorAsSideBar ref="TreeSelectorAsSideBar" type="OrgCourseSpe" 
                           :visible.sync="treeVisible" :selection.sync="categorySelection" ></TreeSelectorAsSideBar>

    <TableView class="overflow-auto flex-1" :options="options" :params.sync="filterData">
      <template v-slot:main>
        <div class="flex center-center margin-bottom-8" v-if="!treeVisible">
          <div class="font-16 padding-8 padding-left-0 el-icon-s-unfold pointer text-main" @click="treeVisible = true"></div>
        </div>
      </template>
      <!-- 表格 -->
      <el-table class="margin-top-16"
                v-loading="loading.table"
                :data="tableData"
                :height="$utils.FullViewHeight(186)"
                @selection-change="selectionList = $event.map((item) => item.id)">
        <template v-slot:empty>
          <Results v-if="!loading"></Results>
          <span v-else></span>
        </template>
        <!-- <el-table-column type="selection" width="55"></el-table-column> -->
        <!-- <el-table-column type="index" label="序号" width="55"></el-table-column> -->
        <el-table-column label="专题名称" prop="courseName" min-width="180">
          <template slot-scope="{ row }">
            <div class="flex start-center">
              <div>
                <el-image class="width-60 height-34" :style="{ borderRadius: '4px' }" :src="row.coverUrl" fit="cover"></el-image>
              </div>
              <span class="margin-left-12">{{ row.courseName }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="培训机构" prop="orgName" min-width="155"></el-table-column>
        
        <!-- <el-table-column label="创建时间" prop="createTime" min-width="155"></el-table-column> -->
        <el-table-column label="讲师" prop="createTime" min-width="155">
          <template slot-scope="{ row }">
            <div class="text-ellipsis" :title="row.lecturers.map(item => item.lecturerName).join('、')">
              {{ row.lecturers.map(item => item.lecturerName).join('、') }}
            </div>
          </template>
        </el-table-column>

        <el-table-column label="报名人数" prop="signUpNum" min-width="156">
          <template slot-scope="{ row }"> {{ row.signUpNum }}</template>
        </el-table-column>
        <!-- <el-table-column label="报名人数/限制人数" prop="signUpNum" min-width="156">
          <template slot-scope="{ row }"> {{ row.signUpNum }}/{{ row.signUpLimit || "不限" }}</template>
        </el-table-column> -->

        <el-table-column label="排序" prop="sort" min-width="156">
          <template slot-scope="{ row }">
            <div class="flex start-center" v-if="row.isEdit">
              <el-input-number v-model="row.sort" size="small" label="请输入" :min="1" :max="100000" :step="1"
                              :controls="false"></el-input-number>
              <i class="el-icon-check text-main pointer margin-left-4" @click="onSetSort(row)"></i>
              <i class="el-icon-close text-main pointer margin-left-4" @click="row.isEdit = false"></i>
            </div>
            <template v-else>
              <span class="margin-right-8">{{ row.sort }}</span>
              <i class="el-icon-edit text-main pointer" @click="row.isEdit = true"></i>
            </template>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="100" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="small" @click="onDetail(row)">详情</el-button>
            <el-button type="text" size="small" @click="onDelete(row)">下架</el-button>
          </template>         
        </el-table-column>
      </el-table>
    </TableView>
  </el-container>
</template>

<script>
import { mapActions, mapGetters, mapState } from "vuex";
import mxTableView from '@/components/mixins/mxTableView'
import TreeSelectorAsSideBar from '@/components/form/TreeSelectorAsSideBar'

export default {
  name: "Menu",
  components: {
    TreeSelectorAsSideBar
  },
  mixins: [mxTableView],
  data() {
    return {
      options: {
        main: {
        },
        side: 'courseName:专题名称,lecturerName:讲师姓名'
      },

      // 传递给TreeSelectorAsSideBar
      treeVisible: true,
      categorySelection: null,

      orgId: undefined
    };
  },
  computed: {
    tableDataAPI() {
      return this.$api.OrgCourse.pageV2
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
    }
  },
  watch: {
  },
  methods: {
    // 操作 - 下架
    async onDelete(params) {
      await this.$confirm(`确认下架专题“${params.courseName}”吗？`, {
        title: '下架确认'
      })

      let {code} = await this.$api.OrgCourse.delete({
        idList: [params.id]
      })
      if (code !== 200) return false
      this.$msg('下架')
      await this.getTableData()
    },

    // 操作 - 排序
    async onSetSort(params) {
      let {code} = await this.$api.OrgCourse.update({
        id: params.courseId,
        sort: params.sort
      })
      if (code !== 200) return false
      this.$msg('排序')
      await this.getTableData()
    },

    // 操作 - 详情
    async onDetail(params) {
      this.$utils.WindowOpenInParentFrame(`/Management/Plat/orgCourseSpeDe&id=${params.courseId}&orgId=${params.orgId}`)
      // this.$router.push({
      //   name: 'orgCourseSpeDe',
      //   query: {
      //     id: params.courseId,
      //     orgId: params.orgId
      //   }
      // })
    }
  },
};
</script>

<style lang="stylus" scoped></style>
