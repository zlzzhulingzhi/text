<template>
  <el-dialog
    :visible.sync="visible"
    append-to-body
    :close-on-click-modal="false"
    :before-close="close"
    width="1000px">

    <template slot="title">
      <span class="text-3 text-bold font-16 margin-right-8">课表</span>
      <span class="font-14">班级名称：{{ classInfo.className }}</span>
    </template>

    <el-table :data="tableData" style="width: 100%" max-height="600">

      <template v-slot:empty>
        <Results v-if='!loading.table'></Results>
        <span v-else></span>
      </template>

      <el-table-column prop="contentName" label="课程名称" width="180"> </el-table-column>
      <el-table-column prop="" label="上课时间" width="300">
        <template slot-scope="{row}">
          <div class="flex">
            <span>{{ row.startDate }}</span>
            <span v-if="row.endDate && row.startDate !== row.endDate">&nbsp;-&nbsp;{{ row.endDate }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="roomNo" label="上课教室" width="100"> </el-table-column>
      <el-table-column prop="lecturerName" label="讲师" width="100"> </el-table-column>
      <el-table-column prop="contentDescription" label="课程描述" show-overflow-tooltip></el-table-column>
    </el-table>

  </el-dialog>
</template>

<script>
const name = '节';

export default {
  name: 'DialogCourseList',
  data() {
    return {
      visible: false,
      tableData: null,
      classInfo: {},

      loading: {
        table: true
      }
    };
  },
  computed: {},
  methods: {
    // 打开弹窗
    async open(data = {}) {
      this.classInfo = {}

      this.visible = true;
      this.classInfo = data.formData
      // 获取课表信息
      this.getCourseList()
    },
    // 关闭弹窗
    close() {
      this.visible = false;
      this.$emit('handle', 'close');
    },
    async getCourseList() {
      let {code, data} = await this.$api.Classes.classItemsList({
        clazzId: this.classInfo.id
      })
      if(code !== 200) return false
      this.loading.table = false
      this.tableData = data

    }
  },
};
</script>

<style lang="stylus" scoped>
// .el-table
//   max-height: 400px
</style>
