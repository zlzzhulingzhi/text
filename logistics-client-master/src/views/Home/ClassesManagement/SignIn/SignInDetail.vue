<template>
  <TableView :options="options" :params.sync="filterData">
    <div class="flex between-center margin-bottom-8 margin-top-16">
      <!-- 班级、课程信息 -->
      <div class="flex height-32 line-height-32">
        <h2 class="margin-0 font-16">{{ classInfo.name }}</h2>
        <div class="margin-left-16 font-14">{{ classInfo.courseName }}</div>
      </div>

      <!-- 批量签到按钮 -->
      <!-- <div class="flex font-13">
        <el-button class="width-80 padding-0 height-32" type="primary" plain size="small" @click="onBatchSignIn"
                   :disabled="!selectionList.length">
          批量签到
        </el-button>
      </div> -->
    </div>

    <!-- 上课  请假  旷课 -->
    <div class="font-13 height-32 line-height-32 flex margin-bottom-8">
      <div class="flex start-center margin-right-24" v-for="item in signInStatus" :key="item.id">
        <div class="width-22 height-22 text-center line-height-24 margin-right-8" :class="`bg-${item.status}`">
          <template v-if="item.id === 1">
            <div class="el-icon-check text-bold text-f"></div>
          </template>
          <template v-else>
            <span class="text-f">{{item.wordSign}}</span>
          </template>
        </div>
        <div>{{ item.name }}</div>
      </div>
    </div>

    <template v-slot:footer>
      <!-- 批量签到按钮 -->
        <el-button class="width-80 padding-0 height-32" type="primary" plain size="small" @click="onBatchSignIn"
                   :disabled="!selectionList.length">
          批量签到
        </el-button>
    </template>

    <el-table :data="tableData" :height="$utils.FullViewHeight(266)"
              @selection-change="selectionList = $event.map(item=>item.memberId)"
              v-loading="loading.table">
      <template v-slot:empty>
        <Results v-if='!loading.table'></Results>
        <span v-else></span>
      </template>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="学生名称" prop="studentName" min-width="88"></el-table-column>
      <el-table-column min-width="160">
        <template slot="header" slot-scope="{row}">
          <span>签到情况</span>
          <el-select class="width-160 margin-left-12" v-model="extParams.lessonStart" filterable
                     size="small" placeholder="签到课次">
            <el-option v-for="item in lessonNumList" :key="item.id" :label="item.name"
                       :value="item.id"></el-option>
          </el-select>
        </template>
        <template slot-scope="{row}">
          <div class="flex">
            <div v-for="(sItem, index) in row.signInList" :key="index"
                 class="lesson-num" :class="(signInStatus.find(a => a.id === sItem.signInStatus) || {}).status">
              <el-tooltip :disabled="!sItem.remark" :content="sItem.remark" effect="light" placement="right">
                <div class="width-100p height-100p" @click="onSignIn(sItem, row)">
                  <template v-if="sItem.signInStatus === 1">
                    <div class="el-icon-check text-bold"></div>
                  </template>
                  <template v-if="sItem.signInStatus === 2">
                    <div class="text-bold">假</div>
                  </template>
                  <template v-if="sItem.signInStatus === 3">
                    <div class="text-bold">旷</div>
                  </template>
                  <div v-else>{{ sItem.lessonNum }}</div>
                </div>
              </el-tooltip>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <DialogSignIn ref="DialogSignIn"></DialogSignIn>
  </TableView>
</template>

<script>
import {mapGetters} from 'vuex'
import DialogSignIn from '@/components/dialog/SignIn/DialogSignIn'
import mxTableView from '@/components/mixins/mxTableView'

export default {
  name: 'SignInDetail',
  components: {
    DialogSignIn
  },
  mixins: [mxTableView],
  props: {
    classInfo: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data() {
    return {
      options: {
        total: 0
      },

    //  签到情况的  始末课时
      extParams: {
        lessonStart: 1
      },

    }
  },
  computed: {
    ...mapGetters({
      signInStatus: 'common/signInStatus'
    }),
    params() {
      return {
        clazzId: this.classInfo.id,
        ...this.filterData,
        ...this.extParams,
        lessonEnd: this.extParams.lessonStart && this.extParams.lessonStart + 9
      }
    },
    lessonNumList() {
      let lessonNumList = []

      let step = 10
      let maxNum = 60
      for (let i = 0; i < maxNum; i += step) {
        let lessonStart = i + 1
        let lessonEnd = i + step
        lessonNumList.push({
          // id  取起始值，用以给 extParams.lessonStart 赋值
          id: lessonStart,
          name: `${lessonStart}-${lessonEnd}课次`
        })
      }

      return lessonNumList
    },
  },
  // watch: {
  //   params: {
  //     deep: true,
  //     immediate: true,
  //     handler() {
  //       if (!this.params.clazzId) return false
  //       clearTimeout(this.timer.table)
  //       this.timer.table = setTimeout(() => {
  //         this.getTableData()
  //       }, 300)
  //     }
  //   }
  // },
  watch: {
    classInfo: {
      deep: true,
      handler() {
        this.tableData = []
      }
    }
  },
  methods: {
    // 获取  班级信息
    async getTableData() {
      // 控制没获取到  clazzId  时不进行数据请求
      if (!this.params.clazzId) return false

      this.loading.table = true
      let {code, data} = await this.$api.ClazzSignIn.page(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records
      this.options.total = data.total
    },

    // 操作 - 批量签到
    async onBatchSignIn() {
      await this.$refs.DialogSignIn.open({
        type: 'BatchSignIn',
        formData: {
          clazzId: this.classInfo.id,
          memberIds: this.selectionList
        },
        pParams: {
          studentCount: this.options.total
        }
      })

      await this.getTableData()
    },

    // 操作 - 签到
    async onSignIn(item, row) {
      let formData = {
        clazzId: this.classInfo.id,
        memberId: row.memberId,
        ...item,
        signInStatus: Number(item.signInStatus)
      }

      if(!item.signInStatus) {
        formData = {
          ...formData,
          signInStatus: undefined
        }
      }

      await this.$refs.DialogSignIn.open({
        type: 'SignIn',
        // formData: {
        //   clazzId: this.classInfo.id,
        //   memberId: row.memberId,
        //   ...item,
        //   signInStatus: Number(item.signInStatus)
        // }
        formData
      })

      await this.getTableData()
    }
  }
}
</script>

<style scoped lang="stylus">
.lesson-num
  width 30px
  height 30px
  border 1px solid NEUTRAL_COLOR_3
  border-radius 4px
  text-align center
  line-height 30px
  cursor pointer
  font-weight bold

  & + .lesson-num
    margin-left 6px

  &.success
    background-color SUCCESS_COLOR
    border-color SUCCESS_COLOR
    color NEUTRAL_COLOR_F

  &.warning
    background-color WARNING_COLOR
    border-color WARNING_COLOR
    color NEUTRAL_COLOR_F

  &.error
    background-color ERROR_COLOR
    border-color ERROR_COLOR
    color NEUTRAL_COLOR_F

  


</style>