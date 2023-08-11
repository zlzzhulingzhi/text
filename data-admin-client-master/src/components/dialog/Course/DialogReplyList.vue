<template>
  <el-dialog :visible.sync="visible" append-to-body title="回复列表" :close-on-click-modal="false" :before-close="close" width="800px" top="8vh">
    <TableView :options="options" :params.sync="filterData">
      <div class="margin-bottom-16 flex between-center">
        <div></div>
        <div></div>
      </div>

      <el-table v-loading="loading" :data="listData" :height="$utils.FullViewHeight(380)">
        <template v-slot:empty>
          <Results></Results>
        </template>
        <el-table-column label="回复内容" prop="replyContent"></el-table-column>
        <el-table-column label="回复人" prop="replyNickName" width="160"></el-table-column>
        <el-table-column label="时间" prop="createTime" width="160"></el-table-column>
        <el-table-column label="操作" width="120"  fixed="right">
          <template slot-scope="{row}">
            <!-- <el-button type="text" size="small" @click="onReply(row)">回复</el-button> -->
            <el-button type="text" size="small" @click="onDelete(row)"><span class="text-error">删除</span></el-button>
          </template>
        </el-table-column>
      </el-table>
    </TableView>

    <template slot="footer">
      <el-button size="small" @click="close">关 闭</el-button>
    </template>

    <DialogReplyAdd ref="DialogReplyAdd" @success="getList"></DialogReplyAdd>
  </el-dialog>
</template>

<script>
import DialogReplyAdd from '@/components/dialog/Course/DialogReplyAdd.vue'

const name = '回复'

export default {
  name: 'DialogAddLesson',
  components: {
    DialogReplyAdd
  },
  data() {
    return {
      loading: false,
      visible: false,
      listData: [],
      options: {
        total: 0
      },
      filterData: {},
      messageId: ''
    }
  },
  computed: {
    params() {
      return {
        ...this.filterData
      }
    }
  },
  methods: {
    // 打开弹窗
    async open(data = {}) {
      this.visible = true
      this.messageId = data.formData.messageId
      this.getList()
    },
    // 关闭弹窗
    close() {
      this.visible = false
      this.$emit('handle', 'close')
    },
    // 回复列表
    async getList() {
      this.loading = true
      let { code, data } = await this.$api.CourseMessageReply.page({
        ...this.params,
        messageId: this.messageId
      })
      this.loading = false
      if (code !== 200) return false
      this.listData = data.records
      this.options.total = data.total
    },
    // 回复回复
    onReply(row) {
      this.$refs.DialogReplyAdd.open({
        type: 'Reply',
        formData: {
          replyType: 'reply',
          messageId: row.messageId,
          replyId: row.id
        }
      })
    },
    // 删除回复
    onDelete(row) {
      this.$confirm(`此操作将永久删除该${name}，是否继续？`, '提示', {
        type: 'warning'
      }).then(async () => {
        let { code } = await this.$api.CourseMessageReply.delete({
          idList: [row.id]
        })
        if (code !== 200) return false
        this.$msg.Delete(name)
        this.getList()
      }).catch(() => {})
    }
  }
}
</script>

<style lang="stylus" scoped>
  /deep/.el-card .el-card__body
    padding 0
</style>