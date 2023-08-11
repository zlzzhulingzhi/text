<template>
  <el-dialog :visible.sync="visible" append-to-body title="导入各房型剩余数量" :close-on-click-modal="false" :before-close="close" width="1200px">
    <el-card :body-style="{ padding: '16px' }">

      <!-- 提示流程、两个操作按钮 -->
      <div class="flex between-center padding-bottom-16">
        <div class="flex around-start width-100p step">

          <div class="flex-1 flex column" v-for="index in 3" :key="index">
            <!-- 最上方的  数字 以及 其后面的线 -->
            <div class="flex start-center margin-bottom-12">
              <div class="flex center-center num">{{ index }}</div>
              <div class="flex-1 margin-left-12 margin-right-12 line" v-if="index < 4"></div>
            </div>

            <!-- 第 1，2，3 步的具体 -->
            <template v-if="index === 1">
              <a class="padding-left-10" :href="xlsxHref" download="客房可用与占用(含超预留)">
                <el-button class="normal" size="small" icon="el-icon-download">下载导入模板</el-button>
              </a>
            </template>
            <template v-else-if="index === 2">
              <el-upload class="margin-left-10 margin-right-10" action="" :http-request="onImport" :accept="'.xls, .xlsx'" :before-upload="onBeforeUpload" :show-file-list="false">
                <el-button type="primary" size="small" icon="el-icon-upload2">上传导入模板</el-button>
              </el-upload>
            </template>
            <template v-else-if="index === 3">
              <div>系统完成数据解析</div>
              <div class="text-6 font-12">确认导入操作</div>
            </template>
            <!-- <template v-else-if="index === 4">
              <div>等待导入完成</div>
              <div class="text-6 font-12">在导入记录查看【导入报告】</div>
            </template> -->
          </div>

        </div>

        <!-- <div class="flex">
          <el-button class="normal" size="small" @click="onImportRecord">导入记录</el-button>
        </div> -->
      </div>

      <el-table class="margin-top-16" :data="tableData" :row-class-name="rowClassName" height="45vh" v-loading="loading.table">
        <template v-slot:empty>
          <Results></Results>
        </template>

        <el-table-column label="日期" min-width="154" fixed="left">
          <template slot-scope="{row}">
            {{ row.day }}({{row.descr}})
          </template>
        </el-table-column>
        <el-table-column label="总量" prop="totals" min-width="55"></el-table-column>
        <el-table-column label="总量含超预留" prop="exceedTotals" min-width="108"></el-table-column>
        <el-table-column label="非确认" prop="unconfirmed" min-width="65"></el-table-column>
        <el-table-column label="出租率" prop="occupancyRate" min-width="65"></el-table-column>
        <el-table-column label="单人间" prop="roomType" min-width="78">
          <template slot="header" slot-scope="{row}">
            <span>单人间</span>
              <span>SRS-100</span>
          </template>
          <template  slot-scope="{row}">
            {{ row.dailyList.find(i => i.roomTypeCode === "SRS-100").free }}
          </template>
        </el-table-column>
        
        <el-table-column label="大单人间" prop="roomType" min-width="88">
          <template slot="header" slot-scope="{row}">
            <span>大单人间</span>
            <span>SQR-64</span>
          </template>
          <template  slot-scope="{row}">
            {{ row.dailyList.find(i => i.roomTypeCode === "SQR-64").free }}
          </template>
        </el-table-column>

        <el-table-column label="双人间" prop="roomType" min-width="78">
          <template slot="header" slot-scope="{row}">
            <span>双人间</span>
            <span>STR-212</span>
          </template>
          <template  slot-scope="{row}">
            {{ row.dailyList.find(i => i.roomTypeCode === "STR-212").free }}
          </template>
        </el-table-column>

        <el-table-column label="四人间" prop="roomType" min-width="78">
          <template slot="header" slot-scope="{row}">
            <span>四人间</span>
            <span>SDR-120</span>
          </template>
          <template  slot-scope="{row}">
            {{ row.dailyList.find(i => i.roomTypeCode === "SDR-120").free }}
          </template>
        </el-table-column>

        <el-table-column label="残疾人大单间" prop="roomType" min-width="128">
          <template slot="header" slot-scope="{row}">
            <span>残疾人大单间</span>
            <span>HQR-4</span>
          </template>
          <template  slot-scope="{row}">
            {{ row.dailyList.find(i => i.roomTypeCode === "HQR-4").free }}
          </template>
        </el-table-column>

        <el-table-column prop="roomType" min-width="128">
          <template slot="header" slot-scope="{row}">
            <span>单人间(19m<sup>2</sup>)</span>
            <span>19BRS-52</span>
          </template>
          <template  slot-scope="{row}">
            {{ row.dailyList.find(i => i.roomTypeCode === "19BRS-52").free }}
          </template>
        </el-table-column>

        <el-table-column prop="roomType" min-width="128">
          <template slot="header" slot-scope="{row}">
            <span>单人间(22m<sup>2</sup>)</span>
            <span>22BRS-108</span>
          </template>
          <template  slot-scope="{row}">
            {{ row.dailyList.find(i => i.roomTypeCode === "22BRS-108").free }}
          </template>
        </el-table-column>

        <el-table-column prop="roomType" min-width="128">
          <template slot="header" slot-scope="{row}">
            <span>单人间(27m<sup>2</sup>)</span>
            <span>27BRS-8</span>
          </template>
          <template  slot-scope="{row}">
            {{ row.dailyList.find(i => i.roomTypeCode === "27BRS-8").free }}
          </template>
        </el-table-column>

        <el-table-column prop="roomType" min-width="128">
          <template slot="header" slot-scope="{row}">
            <span>双人间(19m<sup>2</sup>)</span>
            <span>19BTR-83</span>
          </template>
          <template  slot-scope="{row}">
            {{ row.dailyList.find(i => i.roomTypeCode === "19BTR-83").free }}
          </template>
        </el-table-column>
        
        <el-table-column prop="roomType" min-width="128">
          <template slot="header" slot-scope="{row}">
            <span>双人间(22m<sup>2</sup>)</span>
            <span>22BTR-173</span>
          </template>
          <template  slot-scope="{row}">
            {{ row.dailyList.find(i => i.roomTypeCode === "22BTR-173").free }}
          </template>
        </el-table-column>
        
        <el-table-column prop="roomType" min-width="128">
          <template slot="header" slot-scope="{row}">
            <span>双人间(27m<sup>2</sup>)</span>
            <span>27BTR-24</span>
          </template>
          <template  slot-scope="{row}">
            <div class="padding-top-6 padding-bottom-6">{{ row.dailyList.find(i => i.roomTypeCode === "27BTR-24").free }}</div>
          </template>
        </el-table-column>
      </el-table>

      <div class="text-center margin-top-10">
        <el-button class="margin-right-16" type="primary" size="medium" @click="onSubmit" :loading="loading.submit" :disabled="!tableData.length">确认导入 </el-button>
        <el-button class="width-80" size="medium" @click="close">取消</el-button>
      </div>
    </el-card>

    <!--导入提示弹窗-->
    <el-dialog title="提示" :visible.sync="showImpDialog" append-to-body :close-on-click-modal="false" width="500px" @close="onClose">
      <!--导入完成-->
      <el-result icon="success" title="导入已完成" v-if="impStepEnd">
        <template slot="subTitle">
          <div v-html="impResult.msg"></div>
        </template>
        <!-- <template slot="extra">
          <el-button type="primary" size="medium" :disabled="!impResult.data" :loading="loading.download" @click="onDownload">下载导入报告</el-button>
        </template> -->
      </el-result>
      <!--导入中-->
      <div class="flex column center-center" v-else>
        <span class="font-18 margin-top-16 margin-bottom-16">数据导入中，请等待……</span>
        <span class="font-13 text-9">温馨提示：关闭当前页面不影响继续导入。</span>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "DialogImportRemain",
  data() {
    return {
      visible: false, // 弹窗开关

      tableData: [],
      loading: {
        table: false,
        submit: false,
        download: false,
      },
      isFinish: false,
      showImpDialog: false,
      impStepEnd: false,
      impResult: {},
      xlsxHref: `${location.pathname}客房可用与占用(含超预留).xls`,

      batchNo: null
    };
  },
  
  methods: {
    
    // 打开弹窗
    async open(data = {}) {

      this.visible = true;

    },
    // 关闭弹窗
    close() {
      this.tableData = []
      this.visible = false;
      this.$emit('importSuccess')
    },
    onBeforeUpload(file) {
      let isExcel = ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel"].includes(file.type);
      if (!isExcel) this.$message.warning("请选择.xls, .xlsx格式文件上传");
      return isExcel;
    },
    // 操作 - 预导入入住数据
    async onImport({ file }) {
      this.loading.table = true;
      let fileFormData = new FormData();
      fileFormData.append("file", file);

      let { code, data, msg } = await this.$api.DormitoryRemainImport.preImport({
        fileFormData,
      });
      this.loading.table = false;
      this.isFinish = false;
      if (code !== 200) return false;
      this.tableData = data.records
      this.batchNo = data.batchNo
    },
    // 确定导入
    async onSubmit() {
      if (!this.tableData.length) return this.$message.warning(`请导入入住信息`);
      // if (this.isFinish) return this.$message.warning(`入住信息已导入完毕`);
      this.showImpDialog = true;
      this.impStepEnd = false;
      this.loading.submit = true;
      // let result = await this.$api.DormitorySchedule.batchAdd(
      //   this.tableData.map((item, key) => {
      //     return {
      //       ...item,
      //       key,
      //     };
      //   })
      // );
      let result = await this.$api.DormitoryRemainImport.import(this.batchNo);

      if (result.code !== 200) return (this.loading.submit = false);
      this.impResult = result || {};
      this.impStepEnd = true;
      this.$message.success("导入入住信息成功");
      this.isFinish = true;
      this.loading.submit = false;
      // this.onClose()
    },
    rowClassName({ row }) {
      if (!row.passed) return "is-error";
    },
    // 导入记录
    onImportRecord() {
      this.$refs.DialogTableImportRecord.open({
        type: "Question",
      });
    },
    // 关闭导入弹窗
    onClose() {
      this.showImpDialog = false;
      this.tableData = [];
    },
    // 下载导入报告
    async onDownload() {
      this.loading.download = true;
      let { code } = await this.$api.Question.downloadReport({
        id: this.impResult.data,
      });
      this.loading.download = false;
      if (code !== 200) return false;
      this.$message.success("下载成功");
    },
  },
};
</script>

<style scoped lang="stylus">
.num
  width 24px
  height 24px
  background #F2F7FC
  color MAIN_COLOR
  border-radius 50%
  font-size 15px
  font-weight bold

.line
  width 132px
  height 1px
  background #EBF2FF

>>>.el-table
  .el-table__header-wrapper
    thead
      tr
        th
          height: 64px
          vertical-align: middle;
          .cell
            height: auto
            line-height: 23px
            display: flex
            flex-direction: column
            justify-content: center
            align-items: flex-start
            
  .el-table__fixed
    .el-table__fixed-header-wrapper
      thead
        tr
          th
            height: 64px
            vertical-align: middle;
            .cell
              height: auto
              line-height: 23px
  
</style>
