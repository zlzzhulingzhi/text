<!--课程学员-->
<template>
    <!-- <el-card shadow="never" :body-style="{ padding: '16px 0 0' }"> -->
    <el-card shadow="never" :body-style="{ padding: '0 0 0' }">
        <TableView :options="options" :params.sync="filterData">
            <!-- <div>
                <div class="font-14 text-3 margin-top-20 margin-bottom-20">可以通过3种方式指派学员</div>
                <span class="font-14 text-6">1.通过指定组织自动指派学员</span>
                <el-button  size="mini" type="primary" class="margin-left-14" @click="onDepartment">添加组织</el-button>

                <div v-show="form.courseUserDeptVisibles.length" class="padding-8 margin-top-12 overflow-auto label-list"
                style="min-height: 59px; max-height: 188px; background: #f8fafc; width: 1078px">
                <el-tag class="height-36 text-6 font-14 label-item margin-right-10" color="#EEEEEE" style="height: 36px; line-height: 36px"
                    v-for="tag in form.courseUserDeptVisibles" :key="tag.id + 'Dept'" closable @close="removeDept(tag)">
                    <div class="text-ellipsis width-70" style="display: inline-block; vertical-align: bottom">
                    {{ tag.deptName }}
                    </div>
                </el-tag>
                </div>

            </div>


            <div class="margin-top-16">
                <span class="font-14 text-6">2.通过指定标签自动指派学员</span>
                <el-button size="mini" type="primary" class="margin-left-14" @click="onLabel">添加标签</el-button>
              
                <div v-show="form.studentGroup.length" class="padding-8 margin-top-12 overflow-auto label-list"
                style="min-height: 59px; max-height: 188px; background: #f8fafc; width: 1078px">
                <el-tag class="height-36 text-6 font-14 label-item margin-right-10" color="#EEEEEE" style="height: 36px; line-height: 36px"
                    v-for="item in form.studentGroup" :key="item.groupId + 'groupId'" closable @close="removeGroup(item)">
                    <div class="text-ellipsis width-70" style="display: inline-block; vertical-align: bottom">
                    {{ item.groupName }}
                    </div>
                </el-tag>
                </div>
            </div> -->


            <div class="padding-top-16 padding-bottom-12 flex start-center" >
                <!-- <div class="font-14 margin-right-16 text-6">3.直接指派学员</div> -->
                <div class="font-14 margin-right-16 text-6">直接指派学员</div>
                <el-button type="primary" size="mini" @click="onBatchAdd">添加学员</el-button>
                        <!-- <el-button size="small" icon="el-icon-refresh" :loading="loading" @click="getListData">刷新</el-button> -->
                <div class="flex-1"></div>
                <el-button size="small" icon="el-icon-download" :loading="exporting" :disabled="!listData.length" @click="onExport">导出学员</el-button>
                   
            </div>
            <template v-slot:footer>
                <el-button  size="small" :disabled="!selectionList.length" @click="onBatchDelete">批量移除</el-button>
            </template>
            

            <el-table v-loading="loading" :data="listData" :height="$utils.FullViewHeight(352)" @selection-change="selectionList = $event.map(item => item.id)">
                <template v-slot:empty>
                  <Results v-if='!loading'></Results>
                  <span v-else></span>
                </template>
                <el-table-column type="selection" width="50" ></el-table-column>
                <el-table-column :label="$t('user.Name')" prop="realName" sortable></el-table-column>
                <el-table-column label="手机号码" prop="phone"></el-table-column>
                <el-table-column label="报名时间" prop="signUpTime" sortable></el-table-column>
                <!-- <el-table-column label="完成度">
                    <template slot-scope="{ row }">
                        <span>{{ row.learningRate || 0 }}%</span>
                    </template>
                </el-table-column> -->
              
                <el-table-column label="操作" width="110"  fixed="right">
                    <template slot-scope="{ row }">
                        <el-button type="text" size="small" @click="onDelete(row)"><span class="text-error">移除</span></el-button>
                    </template>
                </el-table-column>
            </el-table>
        </TableView>
        <DialogAddStudent ref="DialogAddStudent" @success="getListData"></DialogAddStudent>
        <DialogAddLabel @handle="addHandle" ref="DialogAddLabel"></DialogAddLabel>
        <DialogAddDepartment @handle="addHandle" ref="DialogAddDepartment"></DialogAddDepartment>
    </el-card>
</template>

<script>
import DialogAddStudent from "./DialogAddStudent.vue";
import DialogAddLabel from '@/components/dialog/Course/DialogAddLabel.vue'
// 添加组织
import DialogAddDepartment from '@/components/dialog/Course/DialogAddDepartment.vue'
import {OrgId} from '@/lib/storage'

const name = "学员";

export default {
    name: "CourseStudent",
    components: {
        DialogAddStudent,
        DialogAddLabel,
        DialogAddDepartment
    },
    data() {
        return {
            loading: true,
            exporting: false,
            listData: [],
            selectionList: [],
            options: {
                total: 0,
            },
            filterData: {},
            timer: null,

            form: {
                studentGroup: [], //学员标签
                radiocheck: 1,
                courseUserDeptVisibles: [], // 组织
                groupId: [], // 学员标签id
            },
            orgId: this.$store.state.system.orgId

        };
    },

    created() {
    this.getDeptList();
    this.getGroupList();
  },

    computed: {
        params() {
            return {
                courseId: this.courseIdNum,
                ...this.filterData,
            };
        },
        courseIdNum() {
          return parseInt(this.courseId)
        }
    },
    props: {
        courseId: {
            type: [String, Number],
        },
        courseInfo: {
            type: Object,
            default: () => {},
        },
    },
    watch: {
        params: {
            deep: true,
            handler() {
                clearTimeout(this.timer);
                this.timer = setTimeout(() => {
                    this.getListData();
                }, 300);
            },
        },
    },
    methods: {
        // 列表
        async getListData() {
            if (!this.courseIdNum) return;
            this.loading = true;
            let { code, data } = await this.$api.CourseStudent.page(this.params);
            this.loading = false;
            if (code !== 200) return false;
            this.listData = data.records;
            this.options.total = data.total;
        },
        // 删除
        onDelete(row) {
            // this.$confirm(`移除${name}后，${name}无法再学习课程。若要继续学习，${name}需重新报名课程。`, "提示", {
            this.$confirm(`确认从课程报名中将"${row.realName}"移除？`, "提示", {
                type: "warning",
                title: "移除学员？",
                confirmButtonText: "确任移除",
            })
                .then(async () => {
                    let { code } = await this.$api.CourseStudent.delete({
                        idList: [row.id],
                    });
                    if (code !== 200) return false;
                    this.$msg.Delete(row.realName);
                    this.$emit("onDelete");
                    this.getListData();
                })
                .catch(() => {});
        },
        // 批量删除
        onBatchDelete() {
            this.$confirm(`确认从课程报名中将${this.selectionList.length}个学员移除？`, "提示", {
                type: "warning",
            })
                .then(async () => {
                    let { code } = await this.$api.CourseStudent.delete({
                        idList: this.selectionList,
                    });
                    if (code !== 200) return false;
                    this.$msg.Delete();
                    this.$emit("onDelete");
                    this.getListData();
                })
                .catch(() => {});
        },

        // 批量添加
        onBatchAdd() {
            this.$refs.DialogAddStudent.open({
                formData: {
                    courseId: this.courseIdNum,
                },
            });
        },
        // 导出
        async onExport() {
            if (this.exporting) return;
            this.exporting = true;
            await this.$api.CourseStudent.export({
                courseId: this.courseIdNum,
            });
            this.exporting = false;
        },

        async addApi(type, list) {
          let { code, data } = await this.$api.Course[type](list );
          if (code === 200) {
            if (type === 'addDept') this.getDeptList(this.courseIdNum);
            if (type === 'studentGroup') this.getGroupList(this.courseIdNum);
          }
        },

        // 子组件返回来的数据
        async addHandle(obj) {
          
          const list = {};
          let type;
          list.courseDeptList = [];
          list.courseGroupList = [];
          obj.params.forEach((item) => {
            console.log(item.deptId);
            if (item.deptId) {
              type = 'addDept'
              list.courseDeptList.push({
                deptId: item.deptId,
                deptName: item.deptName,
                courseId: this.courseIdNum,
                orgId: this.orgId 
              })
            }
            if (item.groupId) {
              type = 'studentGroup'
              list.courseGroupList.push({
                groupId: item.groupId,
                courseId: this.courseIdNum,
                orgId: this.orgId
              })
            }
          })          
          this.addApi(type, list)
        },

        // 获取组织数据
        async getDeptList() {
          const { code, data } = await this.$api.Course.courseDept({ id: this.courseIdNum });
          if (code === 200) this.form.courseUserDeptVisibles = data || [];
        },
        // 获取标签数据
        async getGroupList() {
          const { code, data } = await this.$api.Course.courseGroup({ id: this.courseIdNum });
          if (code === 200) this.form.studentGroup = data || [];
        },

        // 添加标签
        async onLabel() {
        //   if (this.taskDetail.status === 30) {
        //     return this.$message.warning('任务已结束，不可添加标签')
        //   }
          let data = await this.$refs.DialogAddLabel.open({
            formData: {
              groupList: this.form.studentGroup
            },
            type: 'AppEdit',
            title: '课程指定标签'
          })
          
          setTimeout(()=>{
            this.getListData()
          },1000)
        },
        // 删除标签
        async removeGroup(item) {
          let status = '';
          try {
            status = await this.$confirm(`删除学员标签后，停止对该学员下的标签继续自动指派项目，但不会删除已指派的学员`, {
              title: '确认删除该学员标签？'
            })
          } catch (error) {
            console.log(error);
          }
          const { code, data } = await this.$api.Course.deleteGroup({ idList: [item.id], orgId: item.orgId });
          if (code === 200) {
            this.getGroupList()
            // this.getListData()
          };
        },
        // 添加组织
        async onDepartment() {
        //   if (this.taskDetail.status === 30) {
        //     return this.$message.warning('任务已结束，不可添加组织')
        //   }
          let newDeptList = await this.$refs.DialogAddDepartment.open({
            formData: {
              deptList: this.form.courseUserDeptVisibles
            },
            type: 'AppEdit',
            title: '课程指定组织'
          })
          
          setTimeout(()=>{
            this.getListData()
          },1000)
        },
        // 删除组织
        async removeDept(item) {
          let status = '';
          try {
            status = await this.$confirm(`删除学员组织后，停止对该组织下的学员继续自动指派项目，但不会移除已指派的学员`, {
              title: '确认删除该组织？'
            })
          } catch (error) {
            console.log(error);
          }
          if (status) {
            let { code, data } = await this.$api.Course.deleteDept({ idList: [item.id], orgId: item.orgId });
            if (code === 200) this.getDeptList();
          }
        },
        

    },
};
</script>



<style lang="stylus" scoped>

.el-tag
    padding 0 10px


>>> .el-tag__close
  color: #666
  
.label-item {
    display: block;
    margin-right: 4px;
    margin-left: 4px;
    margin-bottom: 4px;
    margin-top: 4px;
    border-radius: 4px;
    width: calc((100% - (5px * 15)) / 8);
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    cursor pointer
  }

.label-list {
  flex-direction: row;
  flex-wrap: wrap;
  display: flex;
  width: 100%;
  overflow: auto;
  max-height: 100%;
}

</style>
