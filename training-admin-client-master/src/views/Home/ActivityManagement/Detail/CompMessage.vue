<!--课程留言-->
<template>
  <el-card shadow="never" :body-style="{ padding: '16px 0 0' }">
    <TableView :options="options" :params.sync="filterData">
      <div class="margin-bottom-16 flex between-center">
        <div></div>
        <div>
          <!-- <el-button size="small" icon="el-icon-refresh" :loading="loading" @click="getListData">刷新</el-button> -->
        </div>
      </div>

      <el-table v-loading="loading" :data="listData" :height="$utils.FullViewHeight(320)">
        <template v-slot:empty>
          <Results></Results>
        </template>
        <el-table-column label="留言内容" prop="message"></el-table-column>
        <el-table-column label="留言用户" prop="userNickName" width="160"></el-table-column>
        <el-table-column label="时间" prop="createTime" width="160"></el-table-column>
        <el-table-column label="状态" prop="replyStatus" width="120">
          <template slot-scope="{row}">
            {{ row.replyStatus ? '已回复' : '未回复' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120"  fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="small" @click="onView(row)">查看</el-button>
            <el-button type="text" size="small" @click="onReply(row)">回复</el-button>
            <el-button type="text" size="small" @click="onDelete(row)"><span class="text-error">删除</span></el-button>
          </template>
        </el-table-column>
      </el-table>
    </TableView>
    <DialogReplyList ref="DialogReplyList"></DialogReplyList>
    <DialogReplyAdd ref="DialogReplyAdd" @success="getListData"></DialogReplyAdd>
  </el-card>
</template>

<script>
import DialogReplyList from '@/components/dialog/Course/DialogReplyList.vue'
import DialogReplyAdd from '@/components/dialog/Course/DialogReplyAdd.vue'
import { mapState } from 'vuex'

const name = '留言'

export default {
  components: {
    DialogReplyList,
    DialogReplyAdd
  },
  data() {
    return {
      loading: false,
      listData: [],
      options: {
        total: 0,
        // main: {
        //   enabled: {
        //     label: '分类状态'
        //   }
        // }
        // side: `categoryName:要查找的${name}名称`
      },
      filterData: {},
      timer: null
    }
  },
  computed: {
    params() {
      return {
        courseId: this.courseId,
        ...this.filterData
      }
    },
    ...mapState('system', {
      userInfo: 'userInfo'
    })
  },
  props: {
    courseId: {
      type: [String, Number]
    }
  },
  watch: {
    params: {
      deep: true,
      handler() {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
          this.getListData()
        }, 300)
      }
    }
  },
  methods: {
    // 列表
    async getListData() {
      if (!this.courseId) return
      this.loading = true
      let { code, data } = await this.$api.CourseMessage.page(this.params)
      this.loading = false
      if (code !== 200) return false
      this.listData = data.records
      this.options.total = data.total
    },
    // 查看
    onView(row) {
      this.$refs.DialogReplyList.open({
        formData: {
          messageId: row.id
        }
      })
    },
    // 回复
    onReply(row) {
      this.$refs.DialogReplyAdd.open({
        type: 'Comment',
        formData: {
          replyType: 'comment',
          messageId: row.id,
          replyId: row.id,
          replyNickName: this.userInfo.realName,
          toUserId: row.createBy
        }
      })
    },
    // 删除
    onDelete(row) {
      this.$confirm(`此操作将永久删除该${name}，是否继续？`, '提示', {
        type: 'warning'
      }).then(async () => {
        let { code } = await this.$api.CourseMessage.delete({
          idList: [row.id]
        })
        if (code !== 200) return false
        this.$msg.Delete(name)
        this.getListData()
      }).catch(() => {})
    }
  }
}
</script>

<style lang="stylus" scoped>

</style>