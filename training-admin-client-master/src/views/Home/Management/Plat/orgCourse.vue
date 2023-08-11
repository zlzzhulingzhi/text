<template>
  <div class="flex">
    <TableView class="flex-1 overflow-auto" :options="options" :params.sync="filterData">

      <!-- <template v-slot:footer>
        <el-button size="small" :disabled="!selectionList.length" @click="onBatchDelete" v-if="permissions.Delete"> 批量删除 </el-button>
      </template> -->

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
        <el-table-column type="selection" width="55"></el-table-column>
        <!-- <el-table-column type="index" label="序号" width="55"></el-table-column> -->
        <el-table-column label="课程名称" prop="courseName" min-width="180">
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
        
        <el-table-column label="创建时间" prop="createTime" min-width="155"></el-table-column>

        <el-table-column label="报名人数/限制人数" prop="signUpNum" min-width="156">
          <template slot-scope="{ row }"> {{ row.signUpNum }}/{{ row.signUpLimit || "不限" }}</template>
        </el-table-column>

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
            <el-button type="text" size="small" @click="onDelete(row)">下架</el-button>
          </template>         
        </el-table-column>
      </el-table>
    </TableView>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from "vuex";
import mxTableView from '@/components/mixins/mxTableView'

export default {
  name: "Menu",
  components: {},
  mixins: [mxTableView],
  data() {
    return {
      options: {
        main: {
        },
        side: 'courseName:课程名称'
      },

    };
  },
  computed: {
    tableDataAPI() {
      return this.$api.OrgCourse.page
    }
  },
  watch: {
  },
  methods: {
    // 操作 - 下架
    async onDelete(params) {
      await this.$confirm(`确认下架课程“${params.courseName}”吗？`, {
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
    }
  },
};
</script>

<style lang="stylus" scoped></style>
