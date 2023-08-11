<template>
  <el-dialog :visible.sync="visible" append-to-body :title="dialogInfo.title" :close-on-click-modal="false"
             :before-close="close" width="1100px">

    <div class="margin-bottom-12 flex between-center">
      <div>
        已选中{{ selectionList.length }}个机构
      </div>
      <el-input class="width-240" v-model="orgName" placeholder="请输入机构名称" size="small" clearable>
        <el-button slot="append" icon="el-icon-search"></el-button>
      </el-input>
    </div>

    <el-table ref="table" :data="orgList" :height="$utils.FullViewHeight(500)" row-key="id"
              @row-click="onRowClick" @selection-change="selectionList = $event" :row-class-name="rowClassName">
      <template v-slot:empty>
        <Results></Results>
      </template>
      <!-- 表格数据内容 -->
      <el-table-column type="selection" width="50"  :selectable="item => !item.added"></el-table-column>
      <el-table-column label="机构名称" prop="name" min-width="88" sortable></el-table-column>
      <el-table-column label="机构Logo" prop="pcLogoUrl" width="88">
        <template slot-scope="{row}">
          <el-image :width="200" :src="row.pcLogoUrl"></el-image>
        </template>
      </el-table-column>
<!--      <el-table-column label="机构分类" prop="category" width="100" sortable>
        <template slot-scope="{row}">
          {{ row.category | orgCategory }}
        </template>
      </el-table-column>-->
      <el-table-column label="机构简介" prop="intro" min-width="180" show-tooltip-when-overflow></el-table-column>
      <el-table-column label="机构状态" prop="enabled" width="72" >
        <EleDot slot-scope="{row}" :id="row.enabled"></EleDot>
      </el-table-column>

      <el-table-column label="操作" prop="added" width="88"  sortable>
        <template slot-scope="{row}">
          <el-button type="text" icon="el-icon-circle-plus-outline"
                     v-if="row.added" disabled  key="1">
            已添加
          </el-button>
          <el-button type="text" icon="el-icon-circle-plus-outline" v-else>
            添加
          </el-button>
        </template>
      </el-table-column>
    </el-table>

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
import {mapGetters} from 'vuex'

export default {
  name: 'DialogTableWOrg',
  data() {
    return {
      visible: false, // 弹窗开关

      orgName: null,
      selectionList: [],

      // 存储弹窗所需数据
      dialogType: 'WOrgSelect',
      typeMapping: {
        WOrgSelect: {
          title: '选择机构',
          submitAPI: this.$api.WOrg.create
        }
      },
      loading: {
        submit: false
      },
      timer: {
        table: null
      }
    }
  },
  computed: {
    ...mapGetters({
      courseType: 'common/courseType',
      // 机构列表
      organization: 'common/organization'
    }),
    orgObj() {
      if (!this.organization.map) return []
      return this.organization.map(item => ({...item}))
    },
    orgList() {
      // 获取文本框里面的值
      let orgName = (this.orgName || '').replace(/\s/g, '')
      // 判断文本框是否有值
      if (!orgName) return this.orgObj
      // this.orgObj.forEach(item => {
      //   item.isSearch = Boolean(orgName) && new RegExp(orgName, 'i').test(item.name)
      // })
      // 过滤搜索关键词展示的数据
      let orgArr =  this.orgObj.filter(item => {
        return Boolean(orgName) && new RegExp(orgName, 'i').test(item.name)
      })
      // this.orgObj.sort((a, b) => b.isSearch - a.isSearch)
      return orgArr
    },
    dialogInfo() {
      let dialogInfo = this.typeMapping[this.dialogType]
      return dialogInfo || {}
    },
    permissions() {
      return this.$route.meta.childPermissions || {}
    }
  },

  methods: {
    // 打开弹窗
    async open(data = {}) {
      this.visible = true

      this.dialogType = data.type
      this.selectionList = []

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
      this.$refs.table.clearSelection()
      this.visible = false
    },
    // 确定提交
    async onSubmit() {
      if (!this.selectionList.length) return this.$message.warning('请选择课程')
      this.loading.submit = true
      let {code} = await this.dialogInfo.submitAPI({
        idList: this.selectionList.map(item => item.id)
      })

      this.loading.submit = false
      if (code !== 200) return false

      this.$emit('close', {type: 'success'})
      this.visible = false
    },
    // Row行点击
    onRowClick(row) {
      // 判断是否已添加，已添加不能再点击选中
      if( row.added === 1 ) return false
      this.$refs.table.toggleRowSelection(row)
    },
    rowClassName({row}) {
      return row.isSearch ? 'is-active pointer' : 'pointer'
    }
  }
}
</script>

<style scoped lang="stylus">
>>> .el-table__row.is-active
  background-color BACKGROUND_COLOR

  &:hover > td
    background-color BACKGROUND_COLOR
</style>