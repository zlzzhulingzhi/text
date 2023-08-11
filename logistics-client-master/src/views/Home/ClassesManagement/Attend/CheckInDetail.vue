<template>
  <div class="checkin-detail bg-f padding-16 radius-4">
    <div class="flex between-center margin-bottom-8">
      <!-- 班级名称、课程名称 -->
      <div class="flex height-32 line-height-32">
        <h2 class="margin-0 font-16">{{ classInfo.name }}</h2>
        <div class="margin-left-16 font-14">{{ classInfo.courseName }}</div>
      </div>

      <!-- 两种显示方式选择 -->
      <div class="flex font-13">
        <div class="margin-right-6">显示方式：</div>
        <div class="padding-left-6 padding-right-6 pointer text-unselect"
             :class="{'text-main': viewType === 'table'}"
             @click="viewType = 'table'">
          <span class="iconfont icon-pla_man_icon"></span>
          表格
        </div>
        <div class="padding-left-6 padding-right-6 pointer text-unselect" :class="{'text-main': viewType === 'list'}"
             @click="viewType = 'list'">
          <span class="iconfont icon-list_icon"></span>
          列表
        </div>
      </div>
    </div>

    <div class="font-13 margin-bottom-12 height-32 line-height-32 flex between-center">
      <!-- 列表显示  选择课次 -->
      <div class="flex" v-if="viewType === 'list'">
        <el-select class="width-120 margin-right-12" v-model="extParams.lessonStart" filterable
                   size="small" placeholder="签到课次">
          <el-option v-for="item in options.lessonNumList" :key="item.id" :label="item.name"
                     :value="item.id"></el-option>
        </el-select>
      </div>

      <!-- 列表显示  上课状态，字典 -->
      <div class="flex" v-if="viewType === 'table'">
        <div class="flex start-center margin-right-24" v-for="item in signInStatus" :key="item.id">
          <div class="width-12 height-12 margin-right-8" :class="`bg-${item.status}`"></div>
          <div>{{ item.name }}</div>
        </div>
      </div>
    </div>

    <!--表格形式-->
    <div v-if="viewType === 'table'" class="view-table border-e radius-4 flex"
         :style="{height:$utils.FullViewHeight(236)}" v-loading="loading.signTableData">
      <div class="flex-1 font-13" @wheel.prevent="onWheel">

        <!-- 相当于表格的header -->
        <div class="height-40 line-height-40 flex text-3 border-top-e text-unselect">
          <div class="width-160 padding-left-16">序号/学员名称</div>
          <div class="flex-1 flex">
            <!-- 向左 双箭头 -->
            <div @click="onJump(600)" class="flex center-center pointer width-36 el-icon-d-arrow-left"></div>

            <div class="flex-1 table-scroll-wrapper height-40 line-height-40" @mousedown="onMousedown"
                 title="左右拖拽或滚动">
              <div ref="scrollInner" class="flex table-scroll-inner"
                   :style="{left: tableLeft + 'px'}">

                <!-- 课次1 - 课次60 -->
                <div class="status-box flex center-center margin-right-2 batch pointer count-box" v-for="n in 60"
                     :key="n"
                     @click="onSelect(n)"
                     :class="{'count-selected':signTableDataColumn[n]?.some(item => item)&&signTableDataColumn[n].every(ln => checkInIds.includes(ln))}">
                  <!-- <span class="icon"></span> -->
                  课次{{ n }}
                </div>

              </div>
            </div>

            <!-- 向右 双箭头 -->
            <div @click="onJump(-600)" class="flex center-center pointer width-36 el-icon-d-arrow-right"></div>

          </div>
        </div>

        <!-- 相当于表格的body -->
        <div class="height-52 line-height-52 flex text-6 border-top-e" v-for="(item,index) in signTableDataComputed"
             :key="item.studentId">
          <div class="width-160 flex padding-left-16">
            <div class="width-30">{{ index + 1 }}</div>
            <div>
              {{ item.studentName }}
            </div>
          </div>
          <div class="flex flex-1 text-unselect">
            <!--              <div class="width-36"></div>-->
            <div class="flex-1 table-scroll-wrapper">
              <div class="table-scroll-inner padding-left-36 padding-right-36 flex" :style="{left: tableLeft + 'px'}">
                <!--签到信息-->
                <div class="margin-right-2" v-for="sItem in item.signInList" :key="sItem.signInId">
                  <div v-if="sItem.signInStatus"
                       class="status-box body font-12 flex column around-start margin-top-2"
                       :class="[
                           `status-${sItem.signInStatus}`,
                           isBatch?'batch':'',
                           checkInIds.includes(sItem.signInId)?'selected':''
                       ]" ref="statusBox">
                    <div class="font-13">{{ sItem.signInDate }}</div>
                    <div>{{ sItem.lessonHour }}课时</div>
                  </div>
                  <div v-else class="status-box body" :class="[isBatch?'batch':'',]"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
      </div>
    </div>

    <!--列表形式-->
    <div v-else-if="viewType === 'list'">
      <el-table :data="signTableList" :height="$utils.FullViewHeight(276)" v-loading="loading.signTableList"
                @selection-change="checkInIds = $event.map(({id})=>id)">
        <el-table-column prop="studentName" label="学生名称" min-width="88"></el-table-column>
        <el-table-column prop="phone" label="联系方式" min-width="88"></el-table-column>
        <el-table-column prop="signInDate" label="签到日期" min-width="100"></el-table-column>
        <el-table-column prop="lessonNum" label="签到课次" min-width="88" sortable></el-table-column>
        <el-table-column prop="signInStatusName" label="考勤状态" min-width="88">
          <template slot-scope="{row}">
            {{ row.signInStatus | signInStatus }}
          </template>
        </el-table-column>
      </el-table>

      <div class="margin-top-16 text-right">
        <el-pagination class="kehao-pagination" background :page-sizes="[10, 20, 30, 40]"
                       :current-page.sync="extParams.current"
                       :page-size.sync="extParams.size" :total="totalData.total"
                       layout="total, prev, pager, next, sizes"></el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'CheckInDetail',
  mounted() {
    if (this.$refs.scrollInner) {
      this.tableMaxLeft = this.$refs.scrollInner.offsetParent.clientWidth - this.$refs.scrollInner.clientWidth
    }
  },
  data() {
    return {
      totalData: {
        total: 0
      },
      extParams: {
        current: 1,
        size: 20,
        lessonStart: 1
      },

      viewType: 'table',
      tableLeft: 0,
      tableMaxLeft: 0,
      checkInIds: [],
      signTableList: [],
      signTableData: [],
      isBatch: true,

      loading: {
        signTableList: false,
        signTableData: false
      },
      timer: {
        signTableList: false,
        signTableData: false
      },
      mouseMoving: false
    }
  },
  props: {
    classInfo: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  computed: {
    ...mapGetters({
      signInStatus: 'common/signInStatus'
    }),
    tableDataParams() {
      let lessonNum = {lessonStart: 1, lessonEnd: 60}
      return {
        ...lessonNum,
        clazzId: this.classInfo.id,
        size: 199
      }
    },
    signTableDataComputed() {
      let tArr = []
      for (let i = 1; i < 61; i++) {
        tArr.push(i)
      }
      return this.signTableData.map(item => {
        let signInList = [...tArr]
        item.signInList.forEach(sItem => {
          signInList.splice(sItem.lessonNum - 1, 1, {
            ...sItem
          })
        })

        return {
          ...item,
          signInList
        }
      })
    },
    signTableDataColumn() {
      let obj = {}
      this.signTableData.forEach(stu => stu.signInList.forEach(sItem => {
        if (obj[sItem.lessonNum]) {
          obj[sItem.lessonNum].push(sItem.signInId)
        } else {
          obj[sItem.lessonNum] = [sItem.signInId]
        }
      }))
      return obj
    },
    params() {
      return {
        clazzId: this.classInfo.id,
        ...this.extParams,
        lessonEnd: this.extParams.lessonStart && this.extParams.lessonStart + 9
      }
    },
    options() {
      let lessonNumList = []

      let step = 10
      let maxNum = 60
      for (let i = 0; i < maxNum; i += step) {
        let lessonStart = i + 1
        let lessonEnd = i + step
        lessonNumList.push({
          id: lessonStart,
          name: `${lessonStart}-${lessonEnd}课时`
          // value: {lessonStart, lessonEnd,}
        })
      }

      return {
        lessonNumList
      }
    }
  },
  watch: {
    params: {
      immediate: true,
      deep: true,
      handler() {
        this.getSignTableList()
      }
    },
    tableDataParams: {
      immediate: true,
      deep: true,
      handler(val) {
        if (!val.clazzId) return false
        this.getSignTableData()
      }
    },
    viewType() {
      this.checkInIds = []
      // this.isBatch = false;
    },
    classInfo: {
      deep: true,
      handler() {
        this.checkInIds = []
        this.signTableData = []
        // this.isBatch = false;
      }
    }
  },
  methods: {
    init() {
      this.getSignTableData()
      this.getSignTableList()
    },
    // 获取班级签到表 - 列表
    async getSignTableList() {
      clearTimeout(this.timer.signTableList)
      this.timer.signTableList = setTimeout(async () => {
        if (!this.params.clazzId) return false
        this.loading.signTableList = true
        let {data, code} = await this.$api.ClazzSignIn.pageRecord(this.params)
        if (code !== 200) return this.loading.signTableList = false
        this.signTableList = data.records
        this.totalData.total = data.total
        this.loading.signTableList = false
      }, 100)
    },
    // 获取班级签到表 - 表格
    async getSignTableData() {
      clearTimeout(this.timer.signTableData)
      this.timer.signTableData = setTimeout(async () => {
        this.loading.signTableData = true
        let {data, code} = await this.$api.ClazzSignIn.classSignTable(this.tableDataParams)
        if (code !== 200) return this.loading.signTableData = false

        this.signTableData = data || []

        this.loading.signTableData = false
      }, 300)
    },

    onSelect(n) {
      if (this.mouseMoving) return this.mouseMoving = false
      let lessonNumList = this.signTableDataColumn[n]
      if (lessonNumList) {
        let checkInIds = new Set(this.checkInIds)
        if (lessonNumList.every(ln => checkInIds.has(ln))) {
          lessonNumList.forEach(ln => checkInIds.delete(ln))
        } else {
          lessonNumList.forEach(ln => checkInIds.add(ln))
        }
        this.checkInIds = Array.of(...checkInIds)
      }
      this.mouseMoving = false
    },

    onMousedown(ev) {
      let staticLeft = ev.clientX - this.$refs.scrollInner.offsetLeft
      let staticX = ev.clientX

      let evFn = e => this.tableLeft = e.clientX - staticLeft
      let evFn1 = e => {
        if (this.tableLeft > 0) this.tableLeft = 0
        if (this.tableLeft < this.tableMaxLeft) this.tableLeft = this.tableMaxLeft
        if (e.target !== ev.target) this.mouseMoving = false
        document.removeEventListener('mousemove', evFn)
        document.removeEventListener('mouseup', evFn1)
        document.removeEventListener('mousemove', evFn2)
      }
      document.addEventListener('mousemove', evFn)
      document.addEventListener('mouseup', evFn1)

      let evFn2 = e => {
        if (Math.abs(e.clientX - staticX) > 10) {
          this.mouseMoving = true
          document.removeEventListener('mousemove', evFn2)
        }
      }
      document.addEventListener('mousemove', evFn2)
    },
    onWheel(ev) {
      if (ev.wheelDelta === undefined) {
        if (ev.deltaY < 0) return this.onJump(100)
        this.onJump(-100)
      } else {
        if (ev.wheelDelta > 0) return this.onJump(100)
        this.onJump(-100)
      }
    },
    onJump(n = 100) {
      this.tableLeft += n
      if (this.tableLeft > 0) this.tableLeft = 0
      if (this.tableLeft < this.tableMaxLeft) this.tableLeft = this.tableMaxLeft
    }
  }
}
</script>

<style scoped lang="stylus">
.checkin-detail {
  .el-date-editor {
    width: 240px;
  }

  .kehao-pagination {
    margin-right: -5px;
  }

  .view-table {
    min-height: 400px;
    overflow: auto;

    > .flex-1 {
      min-width: 600px;
    }

    .table-scroll-wrapper {
      position: relative;
      overflow: hidden;

      .table-scroll-inner {
        position: absolute;
        top: 0;
      }
    }

    .status-box {
      padding: 4px 6px;
      width: 120px;
      height: 40px;
      position: relative;
      line-height: 1;
      cursor: pointer;
      border: 1px solid transparent;

      &.body {
        height: 48px;
        background-color: GLOBAL_BACKGROUND_COLOR;
      }

      &.status-1 {
        // 正常上课
        background-color: #DFFAED;
        color: SUCCESS_COLOR;
      }

      &.status-2 {
        // 请假
        background-color: #FFF6E4;
        color: WARNING_COLOR;
      }

      &.status-3 {
        // 旷课
        background-color: #FFEDEF;
        color: ERROR_COLOR;
      }

      &.audit-3 {
        // 已提交
        background-color: rgba(223, 223, 223, 0.6);
        color: #ccc;
        cursor: not-allowed;
      }

      &.batch {
        cursor: auto;

        &.pointer {
          cursor: pointer;
        }

        &.audit-1 {
          // 未确认
        }

        &.audit-2 {
          // 已确认

        }

        &.audit-3 {
          // 已提交
          background-color: rgba(223, 223, 223, 0.6);
          color: #ccc;
        }

        .icon {
          width: 16px;
          height: 16px;
          border-radius: 2px;
          border: 1px solid SUCCESS_COLOR;
          margin-right: 8px;
          display: flex;
          justify-content: center;
          align-items: center;
        }

        &.count-selected {
          .icon {
            background-color: SUCCESS_COLOR;

            &:after {
              content: "\e6da";
              font-family: element-icons;
              color: NEUTRAL_COLOR_F;
            }
          }
        }

        /*        &.count-box {
                  &:before {
                    content: "";
                    position: absolute;
                    right: 0;
                    top: 0;
                    border-top: 20px solid NEUTRAL_COLOR_C;
                    border-left: 24px solid transparent;
                  }

                  &:after {
                    content: "\e6da";
                    font-family: element-icons;
                    position: absolute;
                    right: 0;
                    top: 0;
                    color: NEUTRAL_COLOR_F;
                  }
                }*/

        &.selected {
          border: 1px solid SUCCESS_COLOR;
          position: relative;

          &:before {
            content: "";
            position: absolute;
            right: 0;
            bottom: 0;
            border-bottom: 20px solid SUCCESS_COLOR;
            border-left: 24px solid transparent;
          }

          &:after {
            content: "\e6da";
            font-family: element-icons;
            position: absolute;
            right: 0;
            bottom: 0;
            color: NEUTRAL_COLOR_F;
          }
        }
      }

      .status-audit-flag {
        position: absolute;
        top: 0;
        right: 0;
        opacity: .6;
        transform: rotate(-25deg);
      }
    }
  }
}

.border-top-e {
  border-bottom: 1px solid NEUTRAL_COLOR_E !important;
}
</style>