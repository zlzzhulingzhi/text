<template>
    <el-dialog :visible.sync="isDepartment" append-to-body :close-on-click-modal="false" :before-close="close" width="600px">
        <template slot="title">
            <span class="text-3 font-14 margin-right-12 text-bold">批量设组织</span>
            <span class="text-6 font-12">提示：批量设组织后会覆盖原来已设置的组织</span>
        </template>
        <div class="flex column" style="min-width: 140px;">
            <el-tree ref="tree"
                v-model="submitParams.deptId"
                :data="courseDeptTree"
                default-expand-all
                show-checkbox
                check-on-click-node
                node-key="id"
                :check-strictly="true"
                :expand-on-click-node="false"
                :props="{ children: 'children', label: 'deptName'}"
                @check="onCurrentChange">
            </el-tree>
        </div>
        <div class="flex center-center">
             <el-button class=" margin-right-14" size="mini" type="primary" @click="onSubmit" :loading="loading.onSubmit">确定 </el-button>
            <el-button size="mini" @click="close">取 消</el-button>
        </div>
    </el-dialog>
</template>

<script>
import { mapGetters } from "vuex"

export default {
    name: "DialogFormOrgDepartment",
    data() {
        let submitParams = {
            deptId: null,
            studentIdList: [],
        }
        return {
            isDepartment: false,
            submitParams,
            deptName: '',
            loading: {
                submit: false,
            },
        }
    },
    computed: {
        ...mapGetters({
            courseDeptTree: "common/courseDeptTree",
        }),
    },
    methods: {
        clear() {
            this.$nextTick(()=> {
                this.$refs.tree.setCheckedKeys([]);
            })
        },
        open(studentIdList) {
            this.isDepartment = true;
            this.clear()
            this.submitParams.deptId = null;
            this.submitParams.studentIdList = studentIdList;
        },
        onCurrentChange(data) {
            this.$refs.tree.setCheckedKeys([data.id]);
            this.submitParams.deptId = data.id;
            this.deptName = data.deptName;
        },
        async onSubmit() {
            let { code } = await this.$api.Student.studentDeptAdd(this.submitParams);
            if (code !== 200) return false
            this.$message.success(`设置"${this.deptName}"组织成功`);
            this.$emit('success');
            this.close();
        },
        close() {
            this.isDepartment = false
        }
    },
}
</script>

<style></style>
