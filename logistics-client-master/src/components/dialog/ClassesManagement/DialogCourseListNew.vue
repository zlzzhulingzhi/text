<template>
  <el-dialog
    :visible.sync="visible"
    append-to-body
    :close-on-click-modal="false"
    :before-close="close"
    width="1200px"
    class="showClasses">
    <template slot="title">
      <span class="text-3 text-bold font-16 margin-right-8">{{ classInfo.className }}（课表）</span>
      <span class="text-3 text-bold font-16 absolute yearInfo" v-if="yearInfo">{{ yearInfo }}</span>

      <div class="showStyle absolute flex">
        <div
          class="padding-left-6 padding-right-6 pointer text-unselect"
          :class="{ 'text-main': viewType === 'table' }"
          @click="viewType = 'table'">
          <span class="iconfont icon-pla_man_icon"></span>
          课程列表
        </div>
        <div
          class="padding-left-6 padding-right-6 pointer text-unselect"
          :class="{ 'text-main': viewType === 'list' }"
          @click="viewType = 'list'">
          <span class="iconfont icon-list_icon"></span>
          周课表
        </div>
      </div>
    </template>

    <div v-if="viewType === 'list'">
      <div class="classContainer" v-if="weekName">
        <div class="allWeeks flex absolute bg-f" :class="isFold ? 'isFoldClass' : ''">
          <!-- <div class="choseWhichWeek height-60 line-height-60 text-center text-bold font-24">选择上课周数</div> -->
          <div class="beSelected flex-1 flex wrap">
            <template v-for="(item, index) in usedBeSelectedInfo(isFold, whichWeek)">
              <div
                class="beSelectedItem bg-e flex column center-center radius-4 pointer margin-right-12 margin-bottom-12 text-3"
                :class="item.start === currentStart ? 'active' : ''"
                :key="index"
                @click="onSelect(index, item)">
                <div class="beSelectedItemTitle text-center font-13">
                  {{ item.whichWeekName }}
                </div>
                <div class="beSelectedItemTime text-center font-10">
                  {{ item.start | formatTime('MM-DD') }}至{{ item.end | formatTime('MM-DD') }}
                </div>
              </div>
            </template>
          </div>
          <div class="controlArea pointer width-46 height-20" @click="isFold = !isFold" v-if="isHideControl">
            <!-- 图标 收起 -->
            <el-image :src="require('@/assets/icons/ic_fold.png')" v-if="!isFold"></el-image>
            <!-- 图标 展开 -->
            <el-image :src="require('@/assets/icons/ic_unfold.png')" v-if="isFold"></el-image>
          </div>
        </div>

        <div class="showWeek">
          <!-- <div class="choseWhichWeek height-60 line-height-60 text-center text-bold font-24 margin-top-20">
            {{ weekName }}
          </div> -->
          <div class="border-e">
            <div class="classesTitle height-50 flex between-center">
              <!-- 左侧  留空 -->
              <div class="width-88"></div>
              <!-- 右侧  周信息 -->
              <div class="classesTitleItem flex-1 text-center flex column center-center" v-for="(item, index) in classesTitleInfo" :key="index">
                <span class="font-13 text-3">{{ item.split('-')[1] }}</span>
                <span class="font-10 text-6">{{ item.split('-')[0] }}</span>
              </div>
            </div>

            <div class="classesContent flex">
              <!-- <div class="classesContentFlex"> -->
                <!-- 左侧  一节、二节... -->
                <div class="width-88 height-750 flex column classesOneDay">
                  <div
                    class="classesOneDayItem flex-1 flex center-center"
                    v-for="(item, index) in classesOneDay"
                    :key="index">
                    <span>{{ item.classTitle }}</span>
                  </div>
                </div>
                <!-- 右侧  具体课程信息 -->
                <div class="height-750 flex flex-1" v-if="classesInfo.some(a => a.lessonList && a.lessonList.length)">
                  <div class="contentEachDay flex-1 flex column" v-for="(a, index) in classesTitleInfo" :key="index">
                    <div
                      class="classesContentItem flex-1 flex center-center"
                      v-for="(item, index) in getClassesForCurDay(a.substr(11, 10))"
                      :key="index">

                      <el-popover
                        placement="top"
                        width="400"
                        trigger="hover"
                        popper-class="classInfoDetail"
                        :visible-arrow="false"
                        :offset="-42"
                        v-if="item?.contentName">

                        <div class="classInfoCon flex column center-center" :class="item?.contentName ? '' : 'classInfoConWith' ">
                          <span class="width-100p className text-ellipsis">{{ item?.contentName }}</span>
                          <span class="width-100p classTime margin-top-2">{{ item?.lessonTime }}</span>
                          <div class="width-100p flex between-center margin-top-2">
                            <span class="classTime">{{ item?.lecturerName }}</span>
                            <span class="location">{{ item?.roomNo }}</span>
                          </div>
                        </div>

                        <div slot="reference" class="classInfoCon height-66 width-144 font-13 text-3 flex column center-center" :class="item?.contentName ? '' : 'classInfoConWith' ">
                          <span class="width-100p className text-ellipsis">{{ item?.contentName }}</span>
                          <span class="width-100p classTime margin-top-2">{{ item?.lessonTime }}</span>
                          <div class="width-100p flex between-center margin-top-2">
                            <span class="classTime">{{ item?.lecturerName }}</span>
                            <span class="location">{{ item?.roomNo }}</span>
                          </div>
                        </div>
                      </el-popover>

                      <div class="classInfoCon height-66 width-144 font-13 text-3 flex column center-center" :class="item?.contentName ? '' : 'classInfoConWith' " v-else>
                        <span class="width-100p className text-ellipsis">{{ item?.contentName }}</span>
                        <span class="width-100p classTime margin-top-2">{{ item?.lessonTime }}</span>
                        <div class="width-100p flex between-center margin-top-2">
                          <span class="classTime">{{ item?.lecturerName }}</span>
                          <span class="location">{{ item?.roomNo }}</span>
                        </div>
                      </div>

                    </div>
                  </div>
                </div>
                <div class="height-750 flex center-start flex-1" v-else>
                  <Results class="margin-top-20"></Results>
                </div>
                
              <!-- </div> -->
            </div>
          </div>
        </div>
      </div>

      <Results v-else></Results>
    </div>

    <div class="tableCon" v-else>
      <el-table :data="tableData" style="width: 100%" max-height="600px">
        <template v-slot:empty>
          <Results v-if="!loading.table"></Results>
          <span v-else></span>
        </template>

        <el-table-column prop="contentName" label="课程名称" width="180"> </el-table-column>
        <el-table-column prop="" label="上课时间" width="300">
          <template slot-scope="{ row }">
            <div class="flex">
              <span>{{ row.startDate }}</span>
              <span v-if="row.endDate && row.startDate !== row.endDate">&nbsp;-&nbsp;{{ row.endDate }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="roomNo" label="上课教室" width="100"> </el-table-column>
        <el-table-column prop="lecturerName" label="讲师" width="100"> </el-table-column>
        <el-table-column prop="contentDescription" label="课程描述" show-overflow-tooltip></el-table-column>
      </el-table>
    </div>
  </el-dialog>
</template>

<script>
import mxBaseDialog from '@/components/mixins/mxBaseDialog';
import operateTime from 'moment';

export default {
  name: 'DialogCourseListNew',
  components: {},
  mixins: [mxBaseDialog],
  data() {
    return {
      viewType: 'table',
      classId: null,
      classInfo: {},

      weekName: null,

      classesOneDay: [
        { classTitle: '一节' },
        { classTitle: '二节' },
        { classTitle: '三节' },
        { classTitle: '四节' },
        { classTitle: '五节' },
        { classTitle: '六节' },
        { classTitle: '七节' },
        { classTitle: '八节' },
        { classTitle: '九节' },
        { classTitle: '十节' },
      ],

      classesInfo: [],

      // 所有周信息
      beSelectedInfo: [],
      // 选中周的星期 日期信息
      classesTitleInfo: [],
      // 当前周的信息
      currentStart: null,
      // 当前第几周（从0开始）
      whichWeek: null,

      tableData: [],
      loading: {
        table: false,
      },

      // 是否展开
      isFold: true,

      // 是否隐藏控制按钮
      isHideControl: false
    };
  },
  computed: {
    yearInfo() {
      // return moment((this.beSelectedInfo || [])[0]?.allDate[0].substr(11, 10)).format('YYYY年')
      return this.getStartOrEndWeek((this.beSelectedInfo || [])[0]?.allDate[0].substr(11, 10), '',  'YYYY年')
    }
  },
  methods: {
    // 获取一周的起始日期 降低对于
    getStartOrEndWeek(time, type, timeStyle) {
      if(!Boolean(time)) return false
      if(type === 'start') {
        return operateTime(time).startOf('week')
      } 
      if(type === 'end') {
        return operateTime(time).endOf('week')
      }
      return operateTime(time).format(timeStyle)
    },
    // 操作 - 初始化数据
    async initData(data) {
      this.classInfo = data.formData;
      this.classId = data.formData.id;

      // 获取课表信息
      this.getCourseList();

      let { beginDate, endDate, currentDate } = await this.getStartEndCurDateFunc();

      if (beginDate) {
        await this.getAllWeeksBySE(beginDate, endDate);
        if (this.beSelectedInfo && this.beSelectedInfo.length) {
          let currentWeek = this.beSelectedInfo.find((item, index) => {
            if (
              // moment(item.start).valueOf() <= moment(currentDate).valueOf() &&
              // moment(item.end).valueOf() >= moment(currentDate).valueOf()
              new Date(item.start).getTime() <= new Date(currentDate).getTime() &&
              new Date(item.end).getTime() >= new Date(currentDate).getTime()
            ) {
              this.weekName = `第${index + 1}周`;
              this.whichWeek = index
              return true;
            }
          });
          if (currentWeek) {
            this.currentStart = currentWeek.start;
            this.classesTitleInfo = currentWeek.allDateBq;
          } else {
            this.whichWeek = 0
            currentWeek = this.beSelectedInfo[0];
            this.currentStart = this.beSelectedInfo[0].start;
            this.weekName = '第一周';
            this.classesTitleInfo = this.beSelectedInfo[0].allDateBq;
          }

          // 拿当前周请求课表数据
          this.getListForAllClasses(currentWeek);
        }
      }
    },
    async getCourseList() {
      let { code, data } = await this.$api.Classes.classItemsList({
        clazzId: this.classInfo.id,
      });
      if (code !== 200) return false;
      this.loading.table = false;
      this.tableData = data;
    },
    // 获取 起始日期、当前日期
    async getStartEndCurDateFunc() {
      let { code, data } = await this.$api.newShowClasses.getStartEndCurDate({
        clazzId: this.classId,
      });
      if (code !== 200) return false;
      return data;
    },
    // 根据起始时间获取之间的所有周信息
    getAllWeeksBySE(startDate, endDate) {
      // 保留起始的 endDate  以便后面做判断
      let originEndOfWeek = endDate;
      // let startOfWeek = moment(startDate).startOf('week');
      let startOfWeek = this.getStartOrEndWeek(startDate, 'start')
      // let tempStart = moment(startDate).startOf('week');
      let tempStart = this.getStartOrEndWeek(startDate, 'start')

      let tempEnd = null;
      const beSelectedInfo = [];

      let time = 1;
      // 起始日期在同一周的处理
      if (
        // startOfWeek.format('YYYY-MM-DD') <= moment(startDate).format('YYYY-MM-DD') &&
        // moment(endDate).format('YYYY-MM-DD') <= moment(startOfWeek).endOf('week').format('YYYY-MM-DD')
        new Date(this.getStartOrEndWeek(startOfWeek, '', 'YYYY-MM-DD')).getTime() <= new Date(this.getStartOrEndWeek(startDate, '', 'YYYY-MM-DD')).getTime() &&
        new Date(this.getStartOrEndWeek(endDate, '', 'YYYY-MM-DD')).getTime() <= new Date(this.getStartOrEndWeek(this.getStartOrEndWeek(startOfWeek, 'end'), '', 'YYYY-MM-DD')).getTime()
      ) {
        beSelectedInfo.push({
          // start: moment(startDate).format('YYYY-MM-DD'),
          start: this.getStartOrEndWeek(startDate, '', 'YYYY-MM-DD'),
          // end: moment(endDate).format('YYYY-MM-DD'),
          end: this.getStartOrEndWeek(endDate, '', 'YYYY-MM-DD'),
        });
      } else {
        // while (tempStart.format('YYYY-MM-DD') <= moment(originEndOfWeek).format('YYYY-MM-DD')) {
        while (new Date(this.getStartOrEndWeek(tempStart, '', 'YYYY-MM-DD')).getTime() <= new Date(this.getStartOrEndWeek(originEndOfWeek, '', 'YYYY-MM-DD')).getTime()) {
          if (time === 1) {
            // tempEnd = moment(startOfWeek).endOf('week');
            tempEnd = this.getStartOrEndWeek(startOfWeek, 'end')
            // beSelectedInfo.push({ start: moment(startDate).format('YYYY-MM-DD'), end: tempEnd.format('YYYY-MM-DD') });
            beSelectedInfo.push({ start: this.getStartOrEndWeek(startDate, '', 'YYYY-MM-DD'), end: this.getStartOrEndWeek(tempEnd, '', 'YYYY-MM-DD') });
            time++;
          } else {
            // 最后一次添加
            if (
              // tempStart.format('YYYY-MM-DD') <= moment(originEndOfWeek).format('YYYY-MM-DD') &&
              // tempEnd.format('YYYY-MM-DD') >= moment(originEndOfWeek).format('YYYY-MM-DD')
              new Date(this.getStartOrEndWeek(tempStart, '', 'YYYY-MM-DD')).getTime() <= new Date(this.getStartOrEndWeek(originEndOfWeek, '', 'YYYY-MM-DD')).getTime() &&
              new Date(this.getStartOrEndWeek(tempEnd, '', 'YYYY-MM-DD')).getTime() >= new Date(this.getStartOrEndWeek(originEndOfWeek, '', 'YYYY-MM-DD')).getTime()
            ) {
              beSelectedInfo.push({
                // start: tempStart.format('YYYY-MM-DD'),
                start: this.getStartOrEndWeek(tempStart, '', 'YYYY-MM-DD'),
                // end: moment(originEndOfWeek).format('YYYY-MM-DD'),
                end: this.getStartOrEndWeek(originEndOfWeek, '', 'YYYY-MM-DD'),
              });
            } else {
              // beSelectedInfo.push({ start: tempStart.format('YYYY-MM-DD'), end: tempEnd.format('YYYY-MM-DD') });
              beSelectedInfo.push({ start: this.getStartOrEndWeek(tempStart, '', 'YYYY-MM-DD'), end: this.getStartOrEndWeek(tempEnd, '', 'YYYY-MM-DD') });
            }
            time++;
          }
          // tempStart = operateTime(tempEnd).add(1, 'days');
          tempStart = new Date(tempEnd).getTime() + 24 * 60 * 60 * 1000;
          // tempEnd = moment(tempStart).endOf('week');
          tempEnd = this.getStartOrEndWeek(tempStart, 'end')
        }
      }

      this.beSelectedInfo = beSelectedInfo.map((a) => {
        a.allDate = this.getAllDateAtRang(a.start, a.end);

        // let startFormEnd = moment(a.end).endOf('week').subtract(6, 'days');
        let startFormEnd = this.getStartOrEndWeek(a.end, 'end').subtract(6, 'days');
        // a.allDateBq = this.getAllDateAtRang(startFormEnd, moment(a.end).endOf('week'));
        a.allDateBq = this.getAllDateAtRang(startFormEnd, this.getStartOrEndWeek(a.end, 'end'));
        return a;
      }).map((a, index) => {
        // let whichWeekNum = this.$utils.numToCn(index + 1)
        let whichWeekName = `第${index + 1}周`
        a.whichWeekName = whichWeekName
        return a
      })
    },
    // 用以展示的周信息
    usedBeSelectedInfo(isFold, index) {
      if(this.beSelectedInfo.length <= 11) {
        this.isHideControl = false
        return this.beSelectedInfo        
      } else {
        // 周数大于11
        this.isHideControl = true
        // 折叠时
        if(isFold) {
          let whichWeek = Math.floor(index/11)
          return JSON.parse(JSON.stringify(this.beSelectedInfo)).splice((whichWeek)*11, 11)
        } else {
          // 未折叠时
          return this.beSelectedInfo
        }
      }
    },
    // 获取时间段的所有日期
    getAllDateAtRang(startDate, endDate) {
      let allDate = [];
      const arr = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
      let oneDayMS = 24 * 60 * 60 * 1000;
      // let startDateMS = moment(startDate).valueOf();
      let startDateMS = new Date(startDate).getTime();
      // let endDateMS = moment(endDate).valueOf();
      let endDateMS = new Date(endDate).getTime();
      while (startDateMS <= endDateMS) {
        // let weekDay = arr[operateTime(startDateMS).day()];
        let weekDay = arr[new Date(startDateMS).getDay()];
        // allDate.push(moment(startDateMS).format('MM月DD日') + `-${weekDay}-` + moment(startDateMS).format('YYYY-MM-DD'));
        allDate.push(this.getStartOrEndWeek(startDateMS, '', 'MM月DD日') + `-${weekDay}-` + this.getStartOrEndWeek(startDateMS, '', 'YYYY-MM-DD'));
        startDateMS = startDateMS + oneDayMS;
      }
      return allDate;
    },
    // 获取 当前周的课程
    async getListForAllClasses({ start, end }) {
      let { code, data } = await this.$api.newShowClasses.listForAllClasses({
        clazzId: this.classId,
        // startDate: moment(start).format('YYYY-MM-DD 00:00'),
        startDate: this.getStartOrEndWeek(start, '', 'YYYY-MM-DD 00:00'),
        // endDate: moment(end).format('YYYY-MM-DD 23:59'),
        endDate: this.getStartOrEndWeek(end, '', 'YYYY-MM-DD 23:59'),
      });
      if (code !== 200) return false;
      this.classesInfo = data;
    },
    // 选周
    onSelect(index, item) {
      // debugger
      if(this.isFold) {
        this.whichWeek = index + Math.floor(this.whichWeek/11)*11
      } else {
        this.whichWeek = index
      }
      this.isFold = true
      this.currentStart = item.start;
      this.weekName = `第${index + 1}周`;
      this.classesTitleInfo = item.allDateBq;
      let currentWeek = item;
      // 拿当前周请求课表数据
      this.getListForAllClasses(currentWeek);
    },
    // 获取 某天的所有课表
    getClassesForCurDay(param) {
      if (!param) return false;
      let arr = new Array(10);
      (
        // this.classesInfo.find((item) => moment(item.lessonDate).valueOf() === moment(param).valueOf())?.lessonList || []
        this.classesInfo.find((item) => new Date(item.lessonDate).getTime() === new Date(param).getTime())?.lessonList || []
      ).forEach((item, index) => {
        arr[index] = item;
      });
      return arr;
    },
    // 操作 - 关闭弹窗
    close() {
      this.visible = false;
      this.viewType = 'table',
      this.classInfo = {};
      this.classesInfo = [];
      this.beSelectedInfo = [];
      this.classesTitleInfo = [];
      this.currentStart = null;
      this.whichWeek = null
      this.weekName = null;
    },
  },
};
</script>

<style lang="stylus" scoped>
.showStyle
  top: 16px;
  right: 80px
.yearInfo
  top: 16px
  right 50%
  margin-right -25px
.classContainer
  // max-height 680px
  .isFoldClass
    height 67px
    box-shadow: 0px 0px 0px 0px !important
  .allWeeks
    // .beSelected
    //   height 230px
    //   overflow auto
    //   padding: 16px 0 16px 16px
    //   .beSelectedItem
    //     height 60px
    //     width 180px
    //     margin 0 16px 16px 0
    // height 67px
    // height 280px
    z-index 9
    width 1200px
    padding 16px 24px
    box-shadow: 0px 3px 6px 1px rgba(0,0,0,0.16);
    .beSelected
      .beSelectedItem
        height 36px
        width 88px
    .active
      background MAIN_COLOR
      color NEUTRAL_COLOR_F
  .showWeek
    padding 80px 24px 16px
    .classesTitle
      background: #F2F2F2
    .classesContent
      height 450px
      overflow auto
      .classesOneDay
        border-bottom 1px solid NEUTRAL_COLOR_E
        border-right 1px solid NEUTRAL_COLOR_E
      // .classesOneDayItem
      //   border-bottom 1px solid NEUTRAL_COLOR_E6
      //   &:last-child
      //     border-bottom none
      .height-750
        height 750px
      .classesContentItem
        border-bottom 1px solid NEUTRAL_COLOR_E
        &:last-child
          border-bottom none
        .classInfoCon
          padding 0 10px
        .classInfoConWith
          background #EDF2F6
.tableCon
  padding 24px 32px
</style>

<style lang="stylus">
.showClasses
  .el-dialog
    // margin-top 10px !important
    // margin-bottom 10px !important
    .el-dialog__body
      padding 0

.classInfoDetail
  width 230px !important
  height 98px
  background: #FFFFFF;
  box-shadow: 0px 3px 6px 1px rgba(51,51,51,0.16);
  border-radius: 0px 0px 0px 0px;
  border-top 2px solid MAIN_COLOR
  transform: translateY(110px);
  .classInfoCon
    color MAIN_COLOR
    font-size 14px
</style>
