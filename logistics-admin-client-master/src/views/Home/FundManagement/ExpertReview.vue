<template>
  <el-card class="container">
    <div class="flex-1 basicPage">
      <div class="title font-14 text-bold">专家列表</div>
      <TableView :options="basicPageOptions" :params.sync="basicPageFilterData">
        <el-table class="margin-top-16" :data="basicPageTableData" max-height="600px">
          <template v-slot:empty>
            <Results></Results>
          </template>
          <el-table-column label="评审专家" prop="name" width="200" align="center"></el-table-column>
          <el-table-column label="评审费用" prop="cost" width="160" align="center" v-loading="loading.basicPageTable">
            <template slot-scope="{ row }">
              {{ row.cost }}
            </template>
          </el-table-column>
          <el-table-column label="评审意见" prop="comment" min-width="120" align="center">
            <template slot-scope="{ row }">
              {{ row.comment }}
            </template>
          </el-table-column>
        </el-table>
      </TableView>
    </div>

    <div class="flex-1 attachmentPage">
      <div class="title font-14 text-bold">附件列表</div>
      <TableView :options="attachmentPageOptions" :params.sync="attachmentPageFilterData">
        <el-table
          class="margin-top-16"
          :data="attachmentPageTableData"
          height="300px"
          v-loading="loading.attachmentPageTable">
          <template v-slot:empty>
            <Results></Results>
          </template>
          <el-table-column label="材料名称" prop="attachName" min-width="40" align="center"></el-table-column>
          <el-table-column label="附件" prop="attachList" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div class="flex-1 flex center-center wrap">
                <template v-if="/\.pdf$/.test(row.attachUrl)">
                  <el-image
                    class="height-60 width-60"
                    :src="require('@/assets/resource/doc.png')"
                    @click="openPdfDetail(row)"></el-image>
                </template>

                <template v-else>
                  <el-image class="height-60 flex" :src="row.attachUrl" :preview-src-list="[row.attachUrl]"></el-image>
                </template>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="88">
            <template slot-scope="{ row }">
              <el-button type="text" size="small" @click.stop="onDownload(row)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
      </TableView>
    </div>

    <!--预览-->
    <ResourcePreview ref="Preview"></ResourcePreview>
  </el-card>
</template>

<script>
import ResourcePreview from '@/views/Home/FundManagement/ResourcePreview.vue';
import mxTencentCos from '@/components/mixins/mxTencentCos';
export default {
  name: 'ExpertReview',
  components: {
    ResourcePreview,
  },
  mixins: [mxTencentCos],
  data() {
    return {
      loading: {
        basicPageTable: false,
        done: false,
        update: false,

        attachmentPageTable: false,
      },

      basicPageOptions: {
        total: undefined,
      },
      basicPageFilterData: {},
      basicPageTableData: [],

      attachmentPageOptions: {
        total: undefined,
      },
      attachmentPageFilterData: {},
      attachmentPageTableData: [],

      id: null,
    };
  },
  props: {
    info: {
      type: Object,
      default() {
        return {};
      },
    },
    type: {
      type: String,
      default: 'Create',
    },
    applyType: {
      type: String,
      default: 'allowance',
    },
    
  },
  created() {
    this.id = this.$route.query.id;
  },
  watch: {
    info: {
      deep: true,
      immediate: true,
      handler() {
        if (this.id) {
          this.onInitFormData();
        }
      },
    },
  },
  methods: {
    // 操作 - 初始化表单数据
    async onInitFormData() {
      this.getExportList();

      this.getAttachList();
    },

    // 获取评审专家列表
    async getExportList() {
      this.isCreateOrEdit = false;
      this.loading.basicPageTable = true;
      let { code, data } = await this.$api.ReviewExport.ExportList({
        id: this.id,
        applyType: this.applyType,
      });
      this.loading.basicPageTable = false;
      if (code !== 200) return false;
      this.basicPageTableData = data.map((item) => {
        return {
          ...item,
          isEdit: false,
          isCreate: false,
        };
      });
    },

    // 获取评审附件列表
    async getAttachList() {
      this.loading.attachmentPageTable = true;
      let { code, data } = await this.$api.ReviewExport.AttachList({
        id: this.id,
        applyType: this.applyType,
      });
      this.loading.attachmentPageTable = false;
      if (code !== 200) return false;
      this.attachmentPageTableData = data.map((item) => {
        return {
          ...item,
          isEdit: false,
          isCreate: false,
        };
      });
    },

    // pdf 预览
    async openPdfDetail(data) {
      await this.$refs.Preview.open(data);
    },

    // 操作 - 下载附件
    async onDownload(param) {
      this.loading.download = true;
      let res = param.attachUrl.split('.');
      await this.onTencentDownLoad({
        fileName: `${param.attachName}.${res[res.length - 1]}`,
        filePath: param.attachUrl,
      });
      this.loading.download = false;
    },
  },
};
</script>

<style lang="stylus" scoped>
.container
  height calc(100vh - 83px)
  overflow: auto
  >>>.el-card__body
    height 100%
    
    display flex
    flex-direction: column

    .basicPage, .attachmentPage
      padding 8px 0

    .basicPage
      .el-table__body-wrapper
        max-height 600px

    .attachmentPage
      .el-table__body-wrapper
        max-height 600px
</style>
