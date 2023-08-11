<template>
  <el-dialog
    :visible.sync="visible"
    append-to-body
    title="评审专家信息"
    :close-on-click-modal="false"
    :before-close="close"
    width="1200px"
    class="reviewExports">

    <div class="flex-1 basicPage">
      <div>
        <span class="title font-14 text-bold margin-right-8">评审专家列表</span>
        <el-button size="small" type="primary" @click="onAddReviewExport" :disabled="isCreate || isEdit"
          >添加评审专家</el-button
        >
      </div>
      <TableView :options="basicPageOptions" :params.sync="basicPageFilterData">
        <el-table class="margin-top-16" :data="basicPageTableData" height="400px" v-loading="loading.basicPageTable">
          <template v-slot:empty>
            <Results></Results>
          </template>
          <el-table-column label="评审专家" prop="name" width="200" align="center">
            <template slot-scope="{ row }">
              <el-select v-if="!row.id || row.isEdit === true" v-model="row.expertId">
                <el-option v-for="item in selectExports" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
              <span v-else>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="评审费用" prop="cost" width="160" align="center">
            <template slot-scope="{ row }">
              <el-input-number
                class="width-140"
                v-if="!row.id || row.isEdit === true"
                v-model="row.cost"
                controls-position="right"
                :min="1"
                :max="100000"></el-input-number>
              <span v-else>{{ row.cost }}</span>
            </template>
          </el-table-column>
          <el-table-column label="评审意见" prop="comment" min-width="120" align="center">
            <template slot-scope="{ row }">
              <el-input
                v-if="!row.id || row.isEdit === true"
                v-model="row.comment"
                type="textarea"
                :row="3"
                maxlength="150"
                show-word-limit></el-input>
              <span v-else>{{ row.comment }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" prop="" width="140" align="center">
            <template slot-scope="{ row }">
              <!-- 新增的保存和取消 -->
              <el-button type="text" v-if="row.isCreate" @click="onCreate(row)" v-loading="loading.done"
                >保存</el-button
              >
              <el-button type="text" v-if="row.isCreate" @click="onDelete(row)">取消</el-button>

              <!-- 编辑的保存和取消 -->
              <el-button type="text" v-if="row.isEdit" @click="onUpdate(row)" v-loading="loading.update"
                >保存</el-button
              >
              <el-button type="text" v-if="row.isEdit" @click="onCancel(row)">取消</el-button>

              <el-button
                type="text"
                v-if="!row.isCreate && !row.isEdit"
                :disabled="(isCreate && Boolean(row.id)) || (isEdit && !row.isEdit)"
                @click="onEdit(row)"
                >编辑</el-button
              >
              <el-button
                type="text"
                v-if="!row.isEdit && !row.isCreate"
                :disabled="(isCreate && Boolean(row.id)) || (isEdit && !row.isEdit)"
                @click="onDelete(row)"
                >删除</el-button
              >
              
            </template>
          </el-table-column>
        </el-table>
      </TableView>
    </div>

    <div class="flex-1 margin-top-12 attachmentPage">
      <div>
        <span class="title font-14 text-bold margin-right-8">附件列表</span>
        <el-button
          size="small"
          type="primary"
          @click="onAddAttach"
          :disabled="attachmentPageTableData.some((item) => item.isCreate || item.isEdit)"
          >添加附件</el-button
        >
      </div>

      <div class="height-0 overflow">
        <ImageUpload
          ref="ImageUpload"
          @input="onSaveImage"
          :autoUpload="true"
          :width="160"
          :height="160"
          :accept="'.png,.jpg,.jpeg,.gif,.webp,.pdf'"
          :options="{
            fixedNumber: [1, 1],
            autoCropWidth: 1920,
            autoCropHeight: 1080,
          }">
          <div slot="tips"></div>
        </ImageUpload>
      </div>

      <TableView :options="attachmentPageOptions" :params.sync="attachmentPageFilterData">
        <el-table
          class="margin-top-16"
          :data="attachmentPageTableData"
          height="400px"
          v-loading="loading.attachmentPageTable">
          <template v-slot:empty>
            <Results></Results>
          </template>
          <el-table-column label="材料名称" prop="attachName" min-width="120" align="center">
            <template slot-scope="{ row }">
              <el-input v-if="row.isCreate || row.isEdit" v-model="row.attachName" maxlength="100" show-word-limit></el-input>
              <span v-else>{{ row.attachName }}</span>
            </template>
          </el-table-column>

          <el-table-column label="附件" prop="attachList" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div class="flex center-center">
                <!-- 上传图片 -->
                <el-button type="primary" size="small" icon="el-icon-plus" @click="onUpload()"
                v-if="row.isCreate && !row.attachUrl">上传附件
                </el-button>

                <!-- <div class="flex-1 flex center-center wrap">
                  <div
                    class="image-wrapper"
                    v-for="(item, index) in row.attachList"
                    :key="index"
                    :class="/\.pdf$/.test(item.attachUrl) ? 'setOrder2' : 'setOrder1'">
                    
                    <template v-if="/\.pdf$/.test(item.attachUrl)">
                      <el-image
                        class="height-60 width-60"
                        :src="require('@/assets/resource/doc.png')"
                        @click="openPdfDetail(item)"></el-image>
                    </template>
                    
                    <template v-else>
                      <el-image
                        class="height-60 flex"
                        :src="item.attachUrl"
                        :preview-src-list="row.attachList.map(a=>a.attachUrl)"></el-image>
                    </template>
                  </div>
                </div> -->

                <div class="flex-1 flex center-center wrap" v-else>
                  <template v-if="/\.pdf$/.test(row.attachUrl)">
                      <el-image
                        class="height-60 width-60"
                        :src="require('@/assets/resource/doc.png')"
                        @click="openPdfDetail(row)"></el-image>
                    </template>
                    
                    <template v-else>
                      <el-image
                        class="height-60 flex"
                        :src="row.attachUrl"
                        :preview-src-list="[row.attachUrl]"></el-image>
                    </template>
                </div>


              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" prop="" width="160" align="center">
            <template slot-scope="{ row }">
              <!-- <el-button
                type="text"
                v-if="!row.isCreate && !row.isEdit"
                :disabled="attachmentPageTableData.some((item) => item.isCreate || item.isEdit)"
                @click="onEditAttachment(row)"
                >编辑</el-button
              > -->
              <el-button
                type="text"
                v-if="row.isCreate"
                @click="onAddAttachment(row)"
                >保存</el-button
              >
              <el-button
                type="text"
                v-if="!row.isCreate"
                :disabled="attachmentPageTableData.some((item) => item.isCreate || item.isEdit) && !row.isCreate"
                @click="onDeleteAttachment(row)"
                >删除</el-button
              >
              <el-button
                type="text"
                v-if="row.isCreate"
                @click="onDeleteAttachment(row)"
                >取消</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </TableView>
    </div>

    <DialogAddExpForReview ref="DialogAddExpForReview"></DialogAddExpForReview>
    <!--预览-->
    <ResourcePreview ref="Preview"></ResourcePreview>
  </el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';
import DialogAddExpForReview from '@/components/dialog/FundManagement/DialogAddExpForReview';
import ImageUpload from '@/components/form/ImageUpload';
import ResourcePreview from '@/views/Home/FundManagement/ResourcePreview.vue'

export default {
  name: 'DialogExpInfoForReview',
  components: {
    DialogAddExpForReview,
    ImageUpload,
    ResourcePreview
  },
  data() {
    return {
      visible: false, // 弹窗开关

      loading: {
        basicPageTable: false,
        done: false,
        update: false,

        attachmentPageTable: false,
      },

      selectExports: null,
      id: null,
      type: null,

      // 评审专家信息
      basicPageOptions: {
        total: undefined,
      },
      basicPageFilterData: {},
      basicPageTableData: [],

      // 选中的专家id
      expertId: null,

      // 评审专家附件
      attachmentPageOptions: {
        total: undefined,
      },
      attachmentPageFilterData: {},
      attachmentPageTableData: [],
      attachmentPageAddData: {
        attachName: null,
        attachList: [],
      },

      // 创建或编辑
      isCreate: false,
      isEdit: false,

      selectRow: null,
    };
  },

  computed: {
    ...mapGetters({}),
  },

  methods: {
    reset() {
      this.formData = { ...this.defaultFormData };
      this.$refs.form && this.$refs.form.resetFields();
    },

    // 打开弹窗
    async open(data = {}) {
      // this.reset();
      this.selectExports = data.selectExports;
      this.id = data.id;
      this.type = data.type

      this.getExportList();

      this.getAttachList();

      this.visible = true;
    },

    // 关闭弹窗
    close() {
      this.visible = false;
    },

    // 获取评审专家列表
    async getExportList() {
      this.isCreateOrEdit = false;
      this.loading.basicPageTable = true;
      let { code, data } = await this.$api.ReviewExport.ExportList({
        id: this.id,
        applyType: this.type,
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

    // 添加评审专家
    onAddReviewExport() {
      // this.$refs.DialogAddExpForReview.open();
      let basicPageAddData = {
        expertName: null,
        reviewCost: null,
        reviewSug: null,
        isEdit: false,
        isCreate: true,
      };
      this.isCreate = true;
      this.basicPageTableData.unshift(basicPageAddData);
    },

    // 操作 - 新增评审专家信息 完成
    async onCreate(param) {
      // this.loading.done = true
      if(!param.expertId || !param.comment || !param.cost) {
        this.$message.warning('请填写完整信息')
        return false
      }
      let { code } = await this.$api.ReviewExport.ExportCreate({
        applyId: this.id,
        applyType: this.type,
        expertId: param.expertId,
        comment: param.comment,
        cost: param.cost,
      });
      // this.loading.done = false
      this.isCreate = false;
      if (code !== 200) return false;
      this.getExportList();
    },

    // 操作 - 编辑评审专家信息
    onEdit(params) {
      params.isEdit = true;
      this.isEdit = true;
    },

    // 操作 - 编辑评审专家信息 更新
    async onUpdate(param) {
      // this.loading.update = true
      let { code } = await this.$api.ReviewExport.ExportUpdate({
        applyId: this.id,
        expertId: param.expertId,
        comment: param.comment,
        cost: param.cost,
        id: param.id,
      });
      // this.loading.update = false
      this.isEdit = false;
      if (code !== 200) return false;
      this.getExportList();
    },

    // 编辑评审专家信息时 取消
    onCancel(param) {
      param.isEdit = false;
      this.isEdit = false;
    },

    // 操作 - 删除评审专家信息
    async onDelete(params) {
      if (params.id) {
        await this.$confirm(`确认删除评审专家“${params.name}”吗？`, {
          title: '删除确认',
          type: 'warning',
        });
        let { code } = await this.$api.ReviewExport.ExportDelete({
          idList: [params.id],
        });
        if (code !== 200) return false;
        this.getExportList();
      } else {
        this.basicPageTableData.shift();
        this.isCreate = false;
      }
    },

    // 获取评审附件列表
    async getAttachList() {
      this.loading.attachmentPageTable = true;
      let { code, data } = await this.$api.ReviewExport.AttachList({
        id: this.id,
        applyType: this.type,
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

    // 操作 - 添加附件
    onAddAttach() {
      let attachmentPageAddData = {
        attachName: null,
        attachUrl: undefined,
        isEdit: false,
        isCreate: true,
      };
      this.attachmentPageTableData.unshift(attachmentPageAddData);
    },

    // 操作 - 上传图片或pdf
    async onUpload() {
      this.$refs.ImageUpload.onSelectFile();
    },

    // 操作 - 保存图片或pdf
    async onSaveImage(image) {
      if (!image) return false;

      this.attachmentPageTableData = this.attachmentPageTableData.map((item) => {
        if (item.isCreate || item.isEdit) {
          item.attachUrl = image
        }
        return item;
      });
    },

    // 操作 - 新增附件记录
    async onAddAttachment(param) {
      if(!param.attachName || !param.attachUrl) {
        this.$message.warning('请填写完整信息')
        return false
      }

      let {code} = await this.$api.ReviewExport.AttachCreate({
        attachList: [
          {
            attachName: param.attachName,
            attachUrl: param.attachUrl,
          }
        ],        
        applyId: this.id,
        applyType: this.type,
      })
      if(code !== 200) return false
      this.getAttachList()

    },

    // 操作 - 删除附件记录
    async onDeleteAttachment(param) {
      if(param.isCreate) {
        this.attachmentPageTableData.shift();
      } else {
        await this.$confirm(`确认删除评审附件“${param.attachName}”吗？`, {
          title: '删除确认',
          type: 'warning',
        });
        let {code} = await this.$api.ReviewExport.AttachDelete({
          idList: [param.id]
        })
        if(code !== 200) return false
        this.getAttachList()
      }
    },

    // pdf 预览
    async openPdfDetail(data) {
      await this.$refs.Preview.open(data)
    },
  },
};
</script>

<style scoped lang="stylus">
.image-wrapper
  position relative
  margin-right 8px
  margin-bottom: 4px

  .el-button
    position absolute
    right 0
    bottom 7px
    padding 0
    width 18px
    height 18px
    font-size 12px

  // & + .image-wrapper
  //   margin-left 8px

.setOrder1
  order 1

.setOrder2
  order 2
</style>

<style lang="stylus">
.reviewExports
  .el-dialog
    margin-top 10px !important
    margin-bottom 0
</style>
