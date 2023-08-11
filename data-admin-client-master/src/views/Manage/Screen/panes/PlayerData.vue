<template>
  <TableView class="padding-top-20" :options="options" :params.sync="filterData">
    <div class="flex end-center bg-f padding-left-20 padding-right-20 padding-bottom-20">
      <!-- <UploadImage v-model="newFileUrl" @input="uploadSuccess">
        <el-button type="primary" size="small" icon="el-icon-upload2" :loading="loading.submit">上传图片</el-button>
        <span slot="tips"></span>
      </UploadImage> -->
      <el-button type="primary" size="small" icon="el-icon-upload2" @click="onAdd">上传视频</el-button>
    </div>

    <el-table v-loading="loading.table" :data="tableData" :height="$utils.FullViewHeight(214)">
      <el-table-column prop="fileName" label="视频名称">
        <!-- <template slot-scope="{row}">
          <el-image class="height-90" :src="row.fileUrl"></el-image>
        </template> -->
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="100"></el-table-column>
      <el-table-column prop="display" label="是否展示" width="100">
        <template slot-scope="{row}">{{ row.display | display }}</template>
      </el-table-column>
      <el-table-column label="操作" width="130">
        <div class="flex" slot-scope="{row}">
          <el-button type="text" size="small" @click="onEdit(row)">编辑</el-button>
          <el-button class="margin-left-12" type="text" size="small" @click="onDelete(row)">删除</el-button>
        </div>
      </el-table-column>
    </el-table>

    <DialogResourceUpload ref="DialogResourceUpload" @success="getTableData"></DialogResourceUpload>
  </TableView>
</template>

<script>
import DialogResourceUpload from '@/components/dialog/DialogResourceUpload.vue'
import UploadImage from '@/components/upload/UploadImage.vue'

const name = '播放视频'

export default {
  name: 'Banner',
  components: {
    DialogResourceUpload,
    UploadImage
  },
  data() {
    return {
      loading: {
        table: false,
        submit: false
      },
      options: {
        total: 0
      },
      filterData: {},
      tableData: [],
      timer: null,
      newFileUrl: null
    }
  },
  computed: {
    params() {
      return {
        ...this.filterData
      }
    }
  },
  watch: {
    params: {
      handler() {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
          this.getTableData()
        }, 300)
      },
      immediate: true
    }
  },
  methods: {
    async getTableData() {
      this.loading.table = true
      let { code, data } = await this.$api.ScreenManage.pageBanner(this.params)
      this.loading.table = false
      if (code !== 200) return false
      this.tableData = data.records || []
      this.options.total = data.total
    },
    onAdd() {
      this.$refs.DialogResourceUpload.open({
        type: 'Add',
      })
    },
    onEdit(row) {
      this.$refs.DialogResourceUpload.open({
        type: 'Update',
        formData: {
          ...row,
          videoName: '视频文件'
        }
      })
    },
    onDelete(row) {
      this.$confirm(`是否删除该${name}？`, '提示', {
        type: 'warning'
      }).then(async () => {
        let { code } = await this.$api.ScreenManage.deleteBanner({
          idList: [row.id]
        })
        if (code !== 200) return false
        this.$msg.Delete(name)
        this.getTableData()
      }).catch(() => {})
    },
  }
}
</script>

<style lang="stylus" scoped>

</style>