<template>
    <el-dialog :visible.sync="visible"
     append-to-body 
     title="课程可见指定标签" 
     :close-on-click-modal="false" 
     :before-close="close" width="800px">

        <div class="flex center-center">
            <div class="left-box">
                <div class="text-3 font-16 text-bold margin-bottom-22">标签</div>
                <el-tree ref="tree" :data="courseGroup" default-expand-all check-on-click-node node-key="groupId" :check-strictly="true" :expand-on-click-node="false" :props="{ label: 'groupName' }" @node-click="onCurrentChange">
                    <div class="custom-tree-node flex between-center" slot-scope="{ data }">
                        <div>
                            <span class="el-icon-folder-opened margin-right-4" style="color: #1d61f2"></span>
                            <span class="font-14">{{ data.groupName }}</span>
                        </div>
                        <div v-if="selectionList.map(item => item.groupId).includes(data.groupId)" class="el-icon-check"></div>
                    </div>
                </el-tree>
            </div>
            <div class="right-box">
                <div class="text-3 font-16 text-bold margin-bottom-22">已选标签</div>
                <el-tree ref="setree" :data="selectionList" default-expand-all check-on-click-node node-key="groupId" :check-strictly="true" :expand-on-click-node="false" :props="{ label: 'groupName' }">
                    <div class="custom-tree-node flex between-center" slot-scope="{ node, data }">
                        <div>
                            <span class="el-icon-folder-opened margin-right-4" style="color: #1d61f2"></span>
                            <span class="font-14">{{ data.groupName }}</span>
                        </div>
                        <div class="el-icon-close remove" @click="() => remove(node, data)"></div>
                    </div>
                </el-tree>
            </div>
        </div>
        <template v-slot:footer>
            <div class="text-center">
            <el-button class="width-80 margin-right-14 margin-top-20" size="small" type="primary" @click="onSubmit" :loading="loading.onSubmit"> 保存 </el-button>
             <el-button class="width-80" size="small" @click="close">取 消</el-button>
        </div>
        </template>
    </el-dialog>
</template>

<script>
import { mapGetters } from "vuex"

export default {
    name: "DialogAddLabel",
    data() {
        let defaultFormData = {
            test: null,
        }
        return {
            visible: false, // 弹窗开关

            // 存储弹窗所需数据
            dialogType: "AppCreate",
            typeMapping: {
                AppCreate: { title: "创建应用", submitFn: this.$api.Application.create, type: "Create" },
                AppEdit: { title: "编辑应用", submitFn: this.$api.Application.update, type: "Edit" },
                // AppGroup: { title: "添加标签", submitFn: this.$api.Task.studentGroup, type: "AppGroup" },
            },
            defaultFormData,
            formData: {
                ...defaultFormData,
            },
            selectionList: [],
            newCourseGroup: [],
            loading: {
                submit: false,
            },
        }
    },
    computed: {
        ...mapGetters({
            courseGroup: "common/courseGroup",
        }),
        dialogInfo() {
            let dialogInfo = this.typeMapping[this.dialogType]
            return dialogInfo || {}
        },
        params() {
            return this.formData
        },
    },

    methods: {
        remove(node, data) {
            const parent = node.parent;
            const children = parent.data.children || parent.data;
            const index = children.findIndex(d => d.id === data.id);
            children.splice(index, 1);
            let newDeptList = this.selectionList.map(item => item.groupId)
            this.$refs.tree.setCheckedKeys(newDeptList);
        },
        // 选中
        selectLabel(item) {
            if (!item.hasOwnProperty("isActive")) {
                this.$set(item, "isActive", true)
                this.selectionList.push(item)
                return
            }
            if (item.isActive) {
                item.isActive = false
                this.selectionList.map((seItem, index) => {
                    if (item.groupId == seItem.groupId) {
                        this.selectionList.splice(index, 1)
                    }
                })
            } else {
                item.isActive = true
                this.selectionList.push(item)
            }
        },
        // 重制
        reset() {
            this.formData = { ...this.defaultFormData }
        },
        // 更改树结构
        // onCurrentChange() {
        //     this.selectionList = this.$refs.tree.getCheckedNodes()
        // },
        onCurrentChange() {
            let list = this.$refs.tree.getCheckedNodes()
            let newList = list.map(item => {
                let { groupName, id, orgId, groupId } = item
                let newItem = Object.assign({
                    groupName,
                    id,
                    orgId,
                    groupId,
                })
                return newItem
            })
            this.selectionList = newList
        },
        // 打开弹窗
        async open(data = {}) {
            this.visible = true
            this.dialogType = data.type || "AppCreate"

            if (this.dialogType === "AppCreate") {
                return false
            }

            if (this.dialogType === "AppEdit") {
            }

            let { groupList } = data.formData
            this.$nextTick(() => {
                this.$refs.tree.setCheckedKeys(groupList.map(item => item.groupId))
            })
            this.selectionList = groupList
            this.formData = {
                ...data.formData,
            }
            return new Promise((resolve, reject) => {
                this.$once("handle", ({ params, type }) => {
                    if (type == "success") {
                        resolve(params)
                    } else {
                        reject("取消")
                    }
                })
            })
        },
        // 关闭弹窗
        close() {
            this.visible = false
        },
        // 确定提交
        async onSubmit() {
            await this.$emit("handle", {
                type: "success",
                params: this.selectionList,
            })
            await this.close()
        },
    },
}
</script>

<style scoped lang="stylus">
.left-box {
    width: 300px
    height: 450px
    overflow: auto
    margin-right: 12px
}
.right-box {
    width: 324px
    height: 450px
    overflow: auto
    padding-left: 25px
    border-left: 1px solid #ccc
}

>>>.el-tree-node__content {
    height: 36px
    padding-right: 16px !important
    padding-left: 16px !important
    
}

>>>.custom-tree-node.flex.between-center{
    width 100%
}
>>>.is-leaf.el-tree-node__expand-icon.el-icon-caret-right {
    display: none;
}
>>>.el-tree-node__content:hover {
    padding-right: 16px !important
    padding-left: 16px !important
}
.remove:hover {
    color: rgb(29, 97, 242);
}
</style>
