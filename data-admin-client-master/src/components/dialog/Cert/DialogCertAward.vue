<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title"
             :close-on-click-modal="false"
             :before-close="close" :width="'1100px'">
    <template slot="title"><span class="text-3 text-bold font-16 margin-right-8">发放证书</span>
      <el-checkbox v-model="isFilterExam">筛选考试用户</el-checkbox>
    </template>
    <div class="flex margin-bottom-8" :class="{'hide-next': isFilterExam}" :style="{order: -1}">
      <!--      <div class="line-height-32 margin-right-12">-->
      <!--        <el-checkbox v-model="isFilterExam">筛选考试用户</el-checkbox>-->
      <!--      </div>-->

      <div v-show="isFilterExam" class="margin-top-8 margin-bottom-8">
        <el-button type="primary" size="small" @click="onSelectExam">
          {{ selectExamName ? '重新选择' : '选择考试' }}
        </el-button>
        <span class="margin-left-8" v-show="selectExamName">《{{ selectExamName }}》</span>
        <el-select v-show="selectExamName" class="margin-left-16 width-140" v-model="examParams.searchStatus"
                   placeholder="考试通过状态" filterable clearable size="small">
          <el-option v-for="item in searchStatusList" :key="item.id" :label="item.name"
                     :value="item.id"></el-option>
        </el-select>
      </div>
    </div>
    <el-form v-show="!isFilterExam" :model="formData" ref="formData" label-width="0" :inline="true" size="small">
        <el-form-item>
        <!-- <TreeSelect class="width-140" style="align-self: baseline;" v-model="formData.deptId" :options="deptTree"
            :normalizer="node => ({id: node.id,label: node.deptName,children: node.children&&node.children.length ? node.children:undefined})"
            :clearable="true" :defaultExpandLevel="Infinity" placeholder="组织"
        ></TreeSelect> -->

        <TreeSelect
            class="width-160"
            placeholder="组织"
            :multiple="true"
            :limit="0"
            :limitText="(list) => `已选择${list}个组织`"
            :defaultExpandLevel="Infinity"
            :normalizer="node => ({ id: node.id, label: node.deptName, children: node.children && node.children.length ? node.children : undefined })"
            :options="deptTree"
            value-consists-of="ALL"
            v-model="formData.deptIdList"
        />

      </el-form-item> 
      <el-form-item>
        <TreeSelect 
            class="width-140"
            style="align-self: baseline;"
            v-model="formData.groupId"
            :options="studentGroupTree"
            :normalizer="node => ({ id: node.groupId, label: node.groupName })"
            :clearable="true"
            :defaultExpandLevel="Infinity"
            placeholder="学员标签">
        </TreeSelect>
      </el-form-item>

      <el-form-item>
        <el-input class="width-160" v-model="formData.realName" placeholder="学员姓名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input class="width-160" v-model="formData.phone" placeholder="手机号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input class="width-160" v-model="formData.idNum" placeholder="身份证号" clearable></el-input>
      </el-form-item>
      <!-- <el-form-item>
        <el-checkbox v-model="formData.inner" :true-label="1" :false-label="0">只筛选内部员工学员</el-checkbox>
      </el-form-item> -->
      <!-- <template v-if="formData.inner">
        <el-form-item>
          <el-select class="width-180" v-model="formData.roleId" placeholder="角色" clearable>
            <el-option v-for="item in orgRoleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <TreeSelect
              class="width-180 tree-select"
              v-model="formData.deptIdList"
              placeholder="组织"
              :options="deptTree"
              :normalizer="node => ({id: node.id, label: node.deptName, children: node.children.length ? node.children:undefined})"
              :defaultExpandLevel="Infinity">
          </TreeSelect>
        </el-form-item>
      </template> -->
    </el-form>

    <!--证书发放-->
    <template v-if="/^(CertAward)$/.test(dialogType)">
      <TableView :options="options" :params.sync="filterData">

        <!--        <template slot="main">-->
        <!--          <div v-show="!isFilterExam" class="flex margin-bottom-8">-->
        <!--&lt;!&ndash;            <div class="padding-right-12 text-right line-height-32">组织</div>&ndash;&gt;-->
        <!--&lt;!&ndash;            <TreeSelect class="width-240"&ndash;&gt;-->
        <!--&lt;!&ndash;                        v-model="otherParams.deptIdList" :options="deptTree"&ndash;&gt;-->
        <!--&lt;!&ndash;                        :normalizer="node => ({id: node.id,label: node.deptName,children: node.children&&node.children.length ? node.children:undefined})"&ndash;&gt;-->
        <!--&lt;!&ndash;                        clearable :defaultExpandLevel="Infinity" placeholder="请选择组织"&ndash;&gt;-->
        <!--&lt;!&ndash;                        multiple&ndash;&gt;-->
        <!--&lt;!&ndash;            ></TreeSelect>&ndash;&gt;-->
        <!--          </div>-->
        <!--        </template>-->

        <!--        <template slot="other">-->

        <!--        </template>-->

        <div class="flex">
          <!--备选列表-->
          <div class="flex-1">
            <div class="text-right margin-bottom-12">
              <el-button type="primary" size="small" @click="onSelectAllUser">全部选择</el-button>
            </div>

            <el-table :data="tableData" :height="$utils.FullViewHeight(500)"
                      v-loading="loading.table">
              <template v-slot:empty>
                <Results></Results>
              </template>

              <!--            <el-table-column type="selection" width="50" ></el-table-column>-->
              <!--            <el-table-column type="index" label="序号" width="55" ></el-table-column>-->
              <el-table-column label="姓名" prop="userName" min-width="132"></el-table-column>
              <el-table-column label="手机号码" prop="phone" min-width="112" ></el-table-column>
              <el-table-column label="身份证号" prop="idNum" min-width="132" >
                <template slot-scope="{row}">
                  <span v-if="row.idNum">{{ row.idNum }}</span>
                  <span v-else class="text-error font-12">未填写</span>
                </template>
              </el-table-column>
              <!--              <el-table-column label="分数" prop="score" min-width="176"></el-table-column>-->

              <el-table-column label="操作" width="72"  fixed="right">
                <template slot-scope="{row}">
                  <span class="text-3 font-12 line-height-32"
                        v-if="row.awarded">已发放</span>

                  <span class="text-9 font-12 line-height-32"
                        v-else-if="selectionList.some(({studentId})=> studentId === row.studentId)">已添加</span>

                  <el-button v-else type="text" size="small" icon="el-icon-circle-plus-outline"
                             @click="onSelectUser(row)">
                    添加
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <div class="width-12"></div>
          <div class="border-9"></div>
          <div class="width-12"></div>

          <!--已选列表-->
          <div class="flex-1">
            <div class="flex between-center margin-bottom-12">
              <div class="height-32">
                <span>已选中{{ selectionList.length }}个用户。</span>
                <span v-if="hasIdNumber&&errorSelectionList.length" class="text-error">有{{
                    errorSelectionList.length
                  }}个缺少身份证号。
                <span>继续发放证书，用户证书中该项信息将显示为空</span>
                </span>
              </div>
              <el-button type="primary" size="small" @click="onRemoveAllUser">全部移除</el-button>
            </div>

            <el-table :data="selectionList"
                      :height="$utils.FullViewHeight(500)"
                      :default-sort="{prop: 'id', order: 'ascending'}">
              <template v-slot:empty>
                <Results></Results>
              </template>

              <el-table-column label="姓名" prop="userName" min-width="132"></el-table-column>
              <el-table-column label="手机号码" prop="phone" min-width="112" ></el-table-column>
              <el-table-column label="身份证号" prop="idNum" min-width="132" >
                <template slot-scope="{row}">
                  <span v-if="row.idNum">{{ row.idNum }}</span>
                  <span v-else class="text-error font-12">未填写</span>
                </template>
              </el-table-column>
              <!--            <el-table-column label="分数" prop="score" min-width="176"></el-table-column>-->

              <el-table-column label="操作" prop="id" width="72"  fixed="right">
                <template slot-scope="{$index}">
                  <el-button type="text" size="small" icon="el-icon-remove-outline" @click="onRemoveUser($index)">
                    移除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <DialogCertAward ref="DialogCertAward" @success="getTableData"></DialogCertAward>
      </TableView>
    </template>

    <DialogSelectExam ref="DialogSelectExam"></DialogSelectExam>

    <template v-slot:footer>
      <div class="flex end-center" :class="{'margin-top-16': !options.total}">
        <el-button class="margin-right-14" type="primary" size="medium" @click="onSubmit" :loading="loading.submit">
          确定发放证书
        </el-button>
        <el-button size="medium" @click="close">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import {mapGetters, mapState} from 'vuex'
import CertTemplateView from '@/components/panes/CertTemplateView'
import DialogSelectExam from '@/components/dialog/Exam/DialogSelectExam'

export default {
  name: 'DialogCertAward',
  components: {
    CertTemplateView,
    DialogSelectExam
  },
  data() {
    return {
      visible: false,
      dialogType: 'CertAward',
      typeMapping: {
        CertAward: {title: '发放证书', submitFn: this.$api.Cert.award}
      },
      formData: {
        realName: '',
        phone: '',
        inner: 0,
        idNum: ''
      },
      certId: 0,
      hasIdNumber: false,
      options: {
        total: 0
        // main: {
        //   roleId: {
        //     options: 'organizationRole',
        //     label: '角色'
        //   }
        // },
        // side: {
        //   realName: {label: '用户名称'},
        //   idNum: {label: '身份证号'},
        //   phone: {label: '手机号码'}
        // }
      },
      filterData: {},

      otherParams: {
        deptIdList: []
      },

      searchStatusList: [
        {id: 1, name: '通过'},
        {id: 2, name: '不通过'}
      ],
      isFilterExam: false,
      selectExamName: null,
      examParams: {
        examId: null,
        searchStatus: 1
      },

      tableData: [],
      selectionList: [],

      timer: {
        table: null
      },
      loading: {
        submit: false,
        table: false
      }
    }
  },
  computed: {
    ...mapGetters({
      certTemplateList: 'common/certTemplateList',
      deptTree: 'common/deptTree',
      orgRoleList: 'common/orgRoleList',
      studentGroupTree: 'common/studentGroupTree',
      
    }),
    ...mapState('system', {
      userInfo: 'userInfo'
    }),
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    submitFormData() {
      return {
        certId: this.certId,
        orgId: this.userInfo.orgId,
        userList: this.selectionList,
        
        sourceMark: '手动添加',
        sourceType: 1
      }
    },
    dataApiInfo() {
      if (this.isFilterExam && this.examParams.examId) {
        return {
          api: this.$api.Cert.examUserList,
          params: {
            ...this.examParams,
            certId: this.certId
          }
        }
      }
      return {
        // api: this.$api.Cert.userPage,
        api: this.$api.Cert.studentPage,
       
        params: {
          ...this.filterData,
          ...this.formData,
        //   deptIdList: this.otherParams.deptIdList,
          certId: this.certId
        }
      }
    },
    errorSelectionList() {
      return this.selectionList.filter(item => !item.idNum)
    }
  },
  watch: {
    dataApiInfo: {
      handler() {
        clearTimeout(this.timer.table)
        this.timer.table = setTimeout(() => {
          this.getTableData()
        }, 300)
      }
    }
  },

  methods: {
    reset() {
      this.selectionList = []
    },
    // 打开弹窗
    async open(data = {}) {
      this.reset()
      this.certId = data.formData.certId
      this.hasIdNumber = data.formData.hasIdNumber
      this.visible = true
      this.dialogType = data.type

      clearTimeout(this.timer.table)
      this.timer.table = setTimeout(() => {
        this.getTableData()
      }, 300)
    },
    // 关闭弹窗
    close() {
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      if (!this.selectionList.length) return this.$message.warning('请添加用户')
      this.loading.submit = true
      let {code} = await this.dialogInfo.submitFn(this.submitFormData)
      this.loading.submit = false
      if (code !== 200) return false
      this.$message.success('发放证书成功')
      this.$emit('success')

      this.close()
    },


    async getTableData() {
      this.loading.table = true
      let {code, data} = await this.dataApiInfo.api(this.dataApiInfo.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = (data || {}).records || data || []
      this.options.total = (data || {}).total
    },

    // 操作 - 选择全部用户
    onSelectAllUser() {
      this.tableData.forEach(item => {
        if (item.awarded) return false
        if (this.selectionList.some(({studentId}) => studentId === item.studentId)) return false
        this.selectionList.push(item)
      })
    },
    // 操作 - 选择用户
    onSelectUser(item) {
      this.selectionList.push(item)
    },
    // 操作 - 移除全部用户
    onRemoveAllUser() {
      this.selectionList = []
    },
    // 操作 - 移除用户
    onRemoveUser(index) {
      this.selectionList.splice(index, 1)
    },
    // 操作 - 选择考试
    async onSelectExam(item) {
      let {examId, examName} = await this.$refs.DialogSelectExam.open({
        type: 'Exam'
      })
      this.selectExamName = examName
      this.examParams.examId = examId
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-dialog__body
  padding 0 16px

  .filter-control
    overflow visible
    margin-bottom 0

    .hide-next + div
      display none

  .el-pagination
    flex 1

//margin-right 524px
</style>