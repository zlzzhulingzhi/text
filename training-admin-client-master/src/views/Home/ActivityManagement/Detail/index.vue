<template>
  <div>
    <!-- 面包屑 -->
    <el-breadcrumb class="margin-bottom-16" separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>活动管理</el-breadcrumb-item>
      <el-breadcrumb-item>活动详情</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 头部内容 -->
    <el-card shadow="never" :body-style="{ padding: '16px' }">
      <div class="flex between-center">
        <div class="flex">
          <!-- 活动介绍图片 -->
          <div class="relative">
            <img :style="{ width: '120px', height: '67.5px', borderRadius: '4px', objectFit: 'cover' }"
                 :src="activityInfo.coverUrl">
          </div>
          <!-- 活动信息· -->
          <div class="margin-left-16">
            <div class="flex">
              <span class="text-bold font-16">{{ activityInfo.title }}</span>
              <span class="padding-left-4 padding-right-4 ico-shelfStatus text-f font-12 margin-left-8"
                    :style="{background: activityInfo.shelfStatus ? '#13C97F' : 'red'}">
                    {{ activityInfo.shelfStatus == 0 ? '已下架' : '已上架' }}
                </span>
            </div>
            <!-- <div class="margin-top-12 text-6 font-13">
              <div class="margin-right-16 text-ellipsis">讲师：
                {{ (lecturers.slice(0, 10)).map(item => item.lecturerName).join(' ')}}
                <b v-if="lecturers.length > 10">...</b>
              </div>
            </div> -->
          </div>
        </div>
      </div>
    </el-card>
    <el-tabs class="margin-top-6" v-model="currentTab" @tab-click="tabClick">
      <el-tab-pane label="学员列表" name="tab5">
        <!-- <CompStudent :activityId="activityId" :activityInfo="activityInfo" @onDelete="getStat"></CompStudent> -->
      </el-tab-pane>
    </el-tabs>
    <!-- <DialogCopyActivity ref="DialogCopyActivity" @success="getTableData"></DialogCopyActivity> -->
  </div>
</template>

<script>
// import CompActivity from './CompActivity.vue'
// import CompStat from './CompStat.vue'
// import CompRecord from './CompRecord.vue'
// import CompComment from './CompComment.vue'
import CompStudent from './CompStudent.vue'
// import CompMessage from './CompMessage.vue'
import {mapGetters, mapState} from 'vuex'

// import PopoverShare from '@/components/dialog/Activity/popoverShare.vue'

// import DialogCopyActivity from '@/components/dialog/Activity/DialogCopyActivity.vue'

export default {
  name: 'ActivityDetail',
  components: {
    // CompActivity,
    // CompStat,
    // CompRecord,
    // CompComment,
    CompStudent,
    // CompMessage,
    // DialogCopyActivity,
    // PopoverShare
  },
  data() {
    return {
      activityId: null,
      statistic: {},
      activityInfo: {},
      currentTab: 'tab5'
    }
  },
  computed: {
    ...mapState('common', {
      activityTypeList: 'activityType'
    }),
    ...mapGetters({
      isDev: 'isDev'
    }),
    lecturers() {
      return this.activityInfo.lecturers || []
    }
  },
  created() {
    this.activityId = this.$route.query.id
    // this.getStat()
    this.getInfo()
  },
  methods: {
    getTableData() {
      this.$router.push({name: 'ActivityListIndex'})
    },
    onCopy() {
      this.$refs.DialogCopyActivity.open({
        formData: {
          id: this.activityId,
          activityName: this.activityInfo.activityName
        }
      })
    },
    async onDelete() {
      let name = '活动'
      await this.$confirm(`此操作将永久删除该${name}，是否继续？`, '提示', {
        type: 'warning'
      })

      let {code} = await this.$api.Activity.delete({
        idList: [this.activityId]
      })
      if (code !== 200) return
      this.$msg.Delete(name)
      this.$router.push({name: 'ActivityListIndex'})
    },

    async onGooded() {
      let {code} = await this.$api.Activity.baseSetting({
        id: this.activityId,
        gooded: this.activityInfo.gooded == 1 ? 0 : 1
      })
      if (code !== 200) return false
      this.activityInfo.gooded = this.activityInfo.gooded == 1 ? 0 : 1
      this.$msg.Update()
    },
    onEdit() {
      window.open(`${origin}/#/Education/Activity/List/Edit?id=${this.activityInfo.id}`, Date.now() + '_bank')
    },
    async onShelve() {
      let {code} = await this.$api.Activity.baseSetting({
        id: this.activityId,
        shelfStatus: this.activityInfo.shelfStatus == 1 ? 0 : 1
      })
      if (code !== 200) return false
      if (this.activityInfo.shelfStatus == 1) {
        this.activityInfo.shelfStatus = 0
        this.$msg.OffShelve()
      } else {
        this.activityInfo.shelfStatus = 1
        this.$msg.OnShelve()
      }
    },
    tabClick(tab) {
      this.currentTab = tab.name
    },
    // 活动统计信息
    // async getStat() {
    //   if (!this.activityId) return
    //   let {code, data} = await this.$api.ActivityStat.info({
    //     id: this.activityId
    //   })
    //   if (code !== 200) return false
    //   this.statistic = data
    // },
    // 活动基本信息
    async getInfo() {
      if (!this.activityId) return
      let {code, data} = await this.$api.Activity.detail({
        id: this.activityId
      })
      if (code !== 200) return false
      this.activityInfo = data
    }
  }
}
</script>

<style lang="stylus" scoped>
.border-1 {
  border-radius: 4px
  border: 1px solid #eee
}

.height-290 {
  height: 290px;
}

.height-135 {
  height: 135px;
}

.position-left {
  top: 0;
  left: 0
  background: #FE993A
  padding: 2px 4px
  border-radius: 4px 0px 4px 0px;
}

.el-card
  border none

/deep/ .el-tabs__content
  // height calc(100vh - 48px - 32px - 30px - 98px - 44px - 8px - 16px)
  height calc(100vh - 32px - 30px - 103px - 44px - 6px - 16px)
  margin-top 16px
  background-color #fff
  overflow auto

.tag
  top 0px
  padding 4px 6px
  color #fff
  font-size 12px
  border-radius 4px

  &.record
    background linear-gradient(227.53deg, rgba(10, 137, 255, 1) 0%, rgba(0, 210, 252, 1) 100%)

  &.live
    background linear-gradient(224.03deg, rgba(248, 114, 32, 1) 0%, rgba(255, 190, 38, 1) 100%)

  &.mix
    background linear-gradient(227.43deg, rgba(112, 41, 255, 1) 0%, rgba(149, 92, 255, 1) 100%)

.ico-shelfStatus {
  display: inline-block;
  height: 20px;
  line-height: 20px
}

>>> .el-tabs__active-bar
  width 56px!important
</style>