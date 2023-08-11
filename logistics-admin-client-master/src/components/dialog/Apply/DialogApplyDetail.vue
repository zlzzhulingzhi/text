<template>
  <div>
    <el-dialog :title="dialogInfo.title" :visible.sync="visible" :close-on-click-modal="true" width="800px" append-to-body :before-close="close">
      <div v-loading="loading">
        <div class="section-item" v-if="dialogInfo.type === 'Order'">
          <div class="title">申请信息</div>
          <el-row class="row-item">
            <el-col :span="8" class="item-col">
              <span class="col-label">申请人：</span><span>{{ applyInfo.applyUser }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">申请事由：</span><span>{{ applyInfo.applyReason }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">申请时间：</span><span>{{ detailData.createTime }}</span>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="8" class="item-col">
              <span class="col-label">联系人：</span><span>{{ applyInfo.contactPerson }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">联系电话：</span><span>{{ applyInfo.contactNumber}}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">联系邮箱：</span><span>{{ applyInfo.contactEmail || '未提供' }}</span>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="8" class="item-col">
              <span class="col-label">机构名称：</span><span>{{ applyInfo.orgName }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <span class="col-label">绑定专题名称：</span><span>{{ applyInfo.courseName }}</span>
            </el-col>
            <el-col :span="8" class="item-col">
              <!-- <span class="col-label">拼团结束时间：</span><span>{{ applyInfo.endTime }}</span> -->
            </el-col>
          </el-row>
        </div>
        <div class="section-item" v-else>
          <div class="title">申请信息</div>
          <el-row class="row-item">
            <el-col :span="12" class="item-col">
              <span class="col-label">机构名称：</span><span>{{ applyInfo.orgName }}</span>
            </el-col>
            <el-col :span="12" class="item-col">
              <span class="col-label">申请时间：</span><span>{{ applyInfo.applyDate || '暂无' }}</span>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="24" class="item-col">
              <span class="col-label">备注：</span><span>{{ applyInfo.remark  || '暂无'}}</span>
            </el-col>
          </el-row>
        </div>
        <div class="section-item">
          <div class="title">教室申请</div>
          <el-table class="margin-top-4" :data="tableClassRoomData">
            <el-table-column type="index" label="序号" width="55"></el-table-column>

            <el-table-column label="教室类型" prop="roomType" min-width="88"></el-table-column>
            <el-table-column label="教室号码" prop="roomNo" width="120"></el-table-column>
            <el-table-column v-if="tableClassRoomData.floor" label="楼层" prop="floor" width="120"></el-table-column>
            <el-table-column v-if="tableClassRoomData.building" label="单元" prop="building" width="120"></el-table-column>
            <el-table-column label="开始时间" prop="useDateStart" min-width="100"></el-table-column>
            <el-table-column label="结束时间" prop="useDateEnd" width="150">
              <template slot-scope="{row}">
                {{row.useDateEnd || '-'}}
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="section-item" v-if="dialogInfo.type === 'Order'">
          <div class="title">宿舍申请</div>
          <el-table class="margin-top-4" :data="tableDormitoryData">
            <el-table-column type="index" label="序号" width="55"></el-table-column>

            <el-table-column label="宿舍类型" prop="roomType" min-width="88"></el-table-column>
            <el-table-column label="宿舍数量" prop="roomNum" width="120"></el-table-column>
            <el-table-column label="开始时间" prop="useDateStart" min-width="120"></el-table-column>
            <el-table-column label="结束时间" prop="useDateEnd" width="100"></el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mxBaseDialog from '@/components/mixins/mxBaseDialog'

export default {
  mixins:[mxBaseDialog],
  data() {
    return {
      detailData: {}, //详情信息
      tableClassRoomData: [], //开团成员列表
      tableDormitoryData: [], //参团成员列表
      loading: false,
      load: {
        tableO: false,
        tableF: false
      },
      id: null, //申请信息ID
      applyInfo: {}, //信息
      filterData: {},
      dialogType: 'OrderDetail',
      typeMapping: {
        OrderDetail: {
          title: `申请详情`,
          type: 'Order',
          detailAPI: this.$api.Approve.detail
        },
        ResultDetail: {
          title: `教室详情`,
          type: 'Result',
          detailAPI: this.$api.Approve.classDetail
        }
      }
    }
  },
  computed: {
    ...mapGetters({
      orderStatus: 'common/orderStatus',
    }),
  },
  methods: {
    initData(data) {
      this.detailData = data
      this.id = data.id
      this.getTablelData()
    },
    async getTablelData() {
      const _params = {
        id: this.id
      }
      this.loading = true
      let { code, data } = await this.dialogInfo.detailAPI(_params)
      this.loading = false
      if (code !== 200) return false
      this.applyInfo = data
      this.tableClassRoomData = this.dialogInfo.type === 'Order' ? data.classroomList : [{
        roomNo:data.roomNo,
        roomType:data.roomType,
        useDateEnd:data.useDateEnd,
        useDateStart:data.useDateStart,
      }]
      this.tableDormitoryData = data.dormitoryList
      console.log(this.tableClassRoomData);
    },
  }
}
</script>

<style lang="stylus" scoped>
.spell-detail-wrap, .section-item
  +.section-item
    margin-top 30px
.title
  margin-bottom 10px
  font-weight 700
  font-size 16px
.row-item
  margin-bottom 10px
.item-col
  display flex
.col-label
  flex-shrink 0
>>>.el-table
  min-height auto
</style>
